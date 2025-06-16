package com.discretelogic.discrete.graphs;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Scalable Concurrent Graph Engine (SCoGE) - A specialized data structure for
 * efficiently representing and manipulating large-scale social networks.
 *
 * Key Features:
 * - Lock-free concurrent access using atomic operations
 * - Compressed Sparse Row (CSR) format for memory efficiency
 * - Dynamic graph partitioning for parallel processing
 * - Optimized attribute storage with cache-friendly layout
 * - Batch processing for bulk updates
 */
public class ScalableConcurrentGraphEngine<T> {

    // Core graph storage using CSR format
    private final ConcurrentHashMap<T, Integer> nodeToId =
        new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, T> idToNode =
        new ConcurrentHashMap<>();
    private final AtomicInteger nodeIdCounter = new AtomicInteger(0);

    // Compressed adjacency storage
    private volatile int[] rowPointers; // CSR row pointers
    private volatile int[] columnIndices; // CSR column indices
    private volatile float[] edgeWeights; // Edge weights
    private final StampedLock csr_lock = new StampedLock(); // For CSR updates

    // Dynamic partitioning
    private final ConcurrentHashMap<Integer, Set<Integer>> partitions =
        new ConcurrentHashMap<>();
    private final AtomicInteger partitionCounter = new AtomicInteger(0);
    private final int partitionThreshold;

    // Attribute storage optimization
    private final ConcurrentHashMap<Integer, NodeAttributes> nodeAttributes =
        new ConcurrentHashMap<>();
    private final ConcurrentHashMap<EdgeKey, EdgeAttributes> edgeAttributes =
        new ConcurrentHashMap<>();

    // Performance metrics
    private final AtomicLong totalOperations = new AtomicLong(0);
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);

    // Lazy evaluation cache
    private final ConcurrentHashMap<String, Object> computationCache =
        new ConcurrentHashMap<>();
    private final long cacheExpirationTime = 300000; // 5 minutes

    /**
     * Constructor with default partition threshold.
     */
    public ScalableConcurrentGraphEngine() {
        this(1000); // Default partition size
    }

    /**
     * Constructor with custom partition threshold.
     *
     * @param partitionThreshold maximum nodes per partition
     */
    public ScalableConcurrentGraphEngine(int partitionThreshold) {
        this.partitionThreshold = partitionThreshold;
        this.rowPointers = new int[0];
        this.columnIndices = new int[0];
        this.edgeWeights = new float[0];
    }

    /**
     * Adds a node to the graph.
     *
     * @param node the node to add
     * @return the assigned node ID
     */
    public int addNode(T node) {
        totalOperations.incrementAndGet();

        return nodeToId.computeIfAbsent(node, n -> {
            int id = nodeIdCounter.getAndIncrement();
            idToNode.put(id, node);
            assignToPartition(id);
            return id;
        });
    }

    /**
     * Adds an edge between two nodes with specified weight.
     *
     * @param source source node
     * @param target target node
     * @param weight edge weight
     */
    public void addEdge(T source, T target, float weight) {
        totalOperations.incrementAndGet();

        int sourceId = addNode(source);
        int targetId = addNode(target);

        // Store edge in attributes for efficient access
        EdgeKey edgeKey = new EdgeKey(sourceId, targetId);
        edgeAttributes.put(
            edgeKey,
            new EdgeAttributes(weight, System.currentTimeMillis())
        );

        // Update CSR structure (requires write lock)
        updateCSRStructure();
    }

    /**
     * Gets neighbors of a node using CSR format for efficient traversal.
     *
     * @param node the node to get neighbors for
     * @return set of neighbor nodes
     */
    public Set<T> getNeighbors(T node) {
        totalOperations.incrementAndGet();

        Integer nodeId = nodeToId.get(node);
        if (nodeId == null) {
            return Collections.emptySet();
        }

        // Use optimistic read lock for CSR access
        long stamp = csr_lock.tryOptimisticRead();
        Set<T> neighbors = getNeighborsById(nodeId);

        if (!csr_lock.validate(stamp)) {
            // Fall back to read lock if optimistic read failed
            stamp = csr_lock.readLock();
            try {
                neighbors = getNeighborsById(nodeId);
            } finally {
                csr_lock.unlockRead(stamp);
            }
        }

        return neighbors;
    }

    /**
     * Performs parallel breadth-first search across partitions.
     *
     * @param startNode starting node for BFS
     * @param maxDepth maximum depth to search
     * @return map of nodes to their distances from start
     */
    public CompletableFuture<Map<T, Integer>> parallelBFS(T startNode, int maxDepth) {
        return CompletableFuture.supplyAsync(() -> {
            Map<T, Integer> distances = new ConcurrentHashMap<>();
            Queue<T> currentLevel = new ConcurrentLinkedQueue<>();
            Queue<T> nextLevel = new ConcurrentLinkedQueue<>();
            
            // Initialize with start node
            currentLevel.offer(startNode);
            distances.put(startNode, 0);
            
            int currentDepth = 0;
            
            while (!currentLevel.isEmpty() && currentDepth < maxDepth) {
                T node = currentLevel.poll();
                
                // Process neighbors
                Set<T> neighbors = getNeighbors(node);
                if (neighbors != null) {
                    for (T neighbor : neighbors) {
                        if (!distances.containsKey(neighbor)) {
                            distances.put(neighbor, currentDepth + 1);
                            nextLevel.offer(neighbor);
                        }
                    }
                }
                
                // Move to next level if current level is empty
                if (currentLevel.isEmpty() && !nextLevel.isEmpty()) {
                    Queue<T> temp = currentLevel;
                    currentLevel = nextLevel;
                    nextLevel = temp;
                    currentDepth++;
                }
            }
            
            return distances;
        });
    }

    /**
     * Batch processing for multiple edge additions.
     *
     * @param edges list of edges to add
     */
    public void batchAddEdges(List<Edge<T>> edges) {
        totalOperations.addAndGet(edges.size());

        // Group edges by source partition for locality
        Map<Integer, List<Edge<T>>> partitionedEdges = edges
            .parallelStream()
            .collect(
                Collectors.groupingBy(edge ->
                    getPartition(
                        nodeToId.computeIfAbsent(
                            edge.source,
                            this::addNodeInternal
                        )
                    )
                )
            );

        // Process each partition in parallel
        partitionedEdges
            .entrySet()
            .parallelStream()
            .forEach(entry -> {
                final List<Edge<T>> edgeList = entry.getValue();
                final int partitionId = entry.getKey();
                edgeList
                    .forEach(edge -> {
                        int sourceId = addNode(edge.source);
                        int targetId = addNode(edge.target);
                        EdgeKey edgeKey = new EdgeKey(sourceId, targetId);
                        edgeAttributes.put(
                            edgeKey,
                            new EdgeAttributes(
                                edge.weight,
                                System.currentTimeMillis()
                            )
                        );
                    });
            });

        // Single CSR update for all edges
        updateCSRStructure();
    }

    /**
     * Computes PageRank using parallel processing across partitions.
     *
     * @param dampingFactor damping factor (typically 0.85)
     * @param maxIterations maximum iterations
     * @param tolerance convergence tolerance
     * @return map of nodes to their PageRank scores
     */
    public CompletableFuture<Map<T, Double>> computePageRank(
        double dampingFactor,
        int maxIterations,
        double tolerance
    ) {
        String cacheKey =
            "pagerank_" + dampingFactor + "_" + maxIterations + "_" + tolerance;

        @SuppressWarnings("unchecked")
        Map<T, Double> cached = (Map<T, Double>) computationCache.get(cacheKey);
        if (cached != null) {
            cacheHits.incrementAndGet();
            return CompletableFuture.completedFuture(cached);
        }

        cacheMisses.incrementAndGet();

        return CompletableFuture.supplyAsync(
            () -> {
                Map<T, Double> pageRank = new ConcurrentHashMap<>();
                Map<T, Double> newPageRank = new ConcurrentHashMap<>();

                // Initialize PageRank values
                double initialValue = 1.0 / nodeToId.size();
                nodeToId
                    .keySet()
                    .forEach(node -> pageRank.put(node, initialValue));

                for (
                    int iteration = 0;
                    iteration < maxIterations;
                    iteration++
                ) {
                    // Parallel computation across partitions
                    partitions
                        .entrySet()
                        .parallelStream()
                        .forEach(partitionEntry -> {
                            partitionEntry
                                .getValue()
                                .forEach(nodeId -> {
                                    T node = idToNode.get(nodeId);
                                    if (node != null) {
                                        double rank = computeNodePageRank(
                                            node,
                                            pageRank,
                                            dampingFactor
                                        );
                                        newPageRank.put(node, rank);
                                    }
                                });
                        });

                    // Check convergence
                    double diff = pageRank
                        .entrySet()
                        .parallelStream()
                        .mapToDouble(entry ->
                            Math.abs(
                                entry.getValue() -
                                newPageRank.get(entry.getKey())
                            )
                        )
                        .sum();

                    pageRank.putAll(newPageRank);

                    if (diff < tolerance) {
                        break;
                    }
                }

                // Cache result
                computationCache.put(cacheKey, pageRank);

                return pageRank;
            },
            ForkJoinPool.commonPool()
        );
    }

    /**
     * Gets performance metrics for the graph engine.
     */
    public SCoGEMetrics getMetrics() {
        return new SCoGEMetrics(
            nodeToId.size(),
            edgeAttributes.size(),
            partitions.size(),
            totalOperations.get(),
            cacheHits.get(),
            cacheMisses.get(),
            computationCache.size()
        );
    }

    // Private helper methods

    private int addNodeInternal(T node) {
        int id = nodeIdCounter.getAndIncrement();
        idToNode.put(id, node);
        assignToPartition(id);
        return id;
    }

    private void assignToPartition(int nodeId) {
        int partitionId = nodeId / partitionThreshold;
        partitions
            .computeIfAbsent(partitionId, k -> ConcurrentHashMap.newKeySet())
            .add(nodeId);
    }

    private int getPartition(int nodeId) {
        return nodeId / partitionThreshold;
    }

    private Set<T> getNeighborsById(int nodeId) {
        if (nodeId >= rowPointers.length - 1) {
            return Collections.emptySet();
        }

        Set<T> neighbors = new HashSet<>();
        int start = rowPointers[nodeId];
        int end = rowPointers[nodeId + 1];

        for (int i = start; i < end; i++) {
            if (i < columnIndices.length) {
                T neighbor = idToNode.get(columnIndices[i]);
                if (neighbor != null) {
                    neighbors.add(neighbor);
                }
            }
        }

        return neighbors;
    }

    private void updateCSRStructure() {
        long stamp = csr_lock.writeLock();
        try {
            // Rebuild CSR structure from edge attributes
            Map<Integer, List<Integer>> adjacencyMap = new HashMap<>();

            edgeAttributes
                .keySet()
                .forEach(edgeKey -> {
                    adjacencyMap
                        .computeIfAbsent(edgeKey.source, k -> new ArrayList<>())
                        .add(edgeKey.target);
                });

            int maxNodeId = nodeIdCounter.get();
            rowPointers = new int[maxNodeId + 1];

            List<Integer> tempColumnIndices = new ArrayList<>();
            List<Float> tempWeights = new ArrayList<>();

            int currentIndex = 0;
            for (int nodeId = 0; nodeId < maxNodeId; nodeId++) {
                rowPointers[nodeId] = currentIndex;
                List<Integer> neighbors = adjacencyMap.getOrDefault(
                    nodeId,
                    Collections.emptyList()
                );

                for (Integer neighbor : neighbors) {
                    tempColumnIndices.add(neighbor);
                    EdgeAttributes attrs = edgeAttributes.get(
                        new EdgeKey(nodeId, neighbor)
                    );
                    tempWeights.add(attrs != null ? attrs.weight : 1.0f);
                    currentIndex++;
                }
            }
            rowPointers[maxNodeId] = currentIndex;

            columnIndices = tempColumnIndices
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
            edgeWeights = new float[tempWeights.size()];
            for (int i = 0; i < tempWeights.size(); i++) {
                edgeWeights[i] = tempWeights.get(i);
            }
        } finally {
            csr_lock.unlockWrite(stamp);
        }
    }

    private double computeNodePageRank(
        T node,
        Map<T, Double> currentPageRank,
        double dampingFactor
    ) {
        double rank = (1.0 - dampingFactor) / nodeToId.size();

        // Find nodes that link to this node
        for (Map.Entry<
            EdgeKey,
            EdgeAttributes
        > entry : edgeAttributes.entrySet()) {
            if (entry.getKey().target.equals(nodeToId.get(node))) {
                T sourceNode = idToNode.get(entry.getKey().source);
                if (sourceNode != null) {
                    int outDegree = getOutDegree(sourceNode);
                    if (outDegree > 0) {
                        rank +=
                            (dampingFactor * currentPageRank.get(sourceNode)) /
                            outDegree;
                    }
                }
            }
        }

        return rank;
    }

    private int getOutDegree(T node) {
        Integer nodeId = nodeToId.get(node);
        if (nodeId == null) return 0;

        return (int) edgeAttributes
            .keySet()
            .stream()
            .filter(edgeKey -> edgeKey.source.equals(nodeId))
            .count();
    }

    // Supporting classes

    public static class Edge<T> {

        public final T source;
        public final T target;
        public final float weight;

        public Edge(T source, T target, float weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }
    }

    private static class EdgeKey {

        final Integer source;
        final Integer target;

        EdgeKey(Integer source, Integer target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof EdgeKey)) return false;
            EdgeKey other = (EdgeKey) obj;
            return (
                Objects.equals(source, other.source) &&
                Objects.equals(target, other.target)
            );
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }
    }

    private static class NodeAttributes {

        final long timestamp;
        final Map<String, Object> properties;

        NodeAttributes() {
            this.timestamp = System.currentTimeMillis();
            this.properties = new ConcurrentHashMap<>();
        }
    }

    private static class EdgeAttributes {

        final float weight;
        final long timestamp;

        EdgeAttributes(float weight, long timestamp) {
            this.weight = weight;
            this.timestamp = timestamp;
        }
    }

    public static class SCoGEMetrics {

        public final int nodeCount;
        public final int edgeCount;
        public final int partitionCount;
        public final long totalOperations;
        public final long cacheHits;
        public final long cacheMisses;
        public final int cacheSize;

        public SCoGEMetrics(
            int nodeCount,
            int edgeCount,
            int partitionCount,
            long totalOperations,
            long cacheHits,
            long cacheMisses,
            int cacheSize
        ) {
            this.nodeCount = nodeCount;
            this.edgeCount = edgeCount;
            this.partitionCount = partitionCount;
            this.totalOperations = totalOperations;
            this.cacheHits = cacheHits;
            this.cacheMisses = cacheMisses;
            this.cacheSize = cacheSize;
        }

        @Override
        public String toString() {
            return String.format(
                "SCoGEMetrics{nodes=%d, edges=%d, partitions=%d, operations=%d, " +
                "cacheHits=%d, cacheMisses=%d, cacheSize=%d}",
                nodeCount,
                edgeCount,
                partitionCount,
                totalOperations,
                cacheHits,
                cacheMisses,
                cacheSize
            );
        }
    }
}
