package edu.ucsb.cs.cognitivedm.graph;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Scalable Concurrent Graph Engine
 *
 * Provides high-performance graph processing capabilities for cognitive discrete
 * mathematics applications. Supports concurrent operations, distributed processing,
 * and integration with the attention-recognition framework for cognitive-aware
 * graph algorithms.
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class ScalableConcurrentGraphEngine {

    public static class Vertex {
        private final String id;
        private final Map<String, Object> properties;
        private final Set<String> outgoingEdges;
        private final Set<String> incomingEdges;
        private final Object lock = new Object();

        public Vertex(String id) {
            this.id = id;
            this.properties = new ConcurrentHashMap<>();
            this.outgoingEdges = ConcurrentHashMap.newKeySet();
            this.incomingEdges = ConcurrentHashMap.newKeySet();
        }

        public String getId() { return id; }
        public Map<String, Object> getProperties() { return new HashMap<>(properties); }
        public Set<String> getOutgoingEdges() { return new HashSet<>(outgoingEdges); }
        public Set<String> getIncomingEdges() { return new HashSet<>(incomingEdges); }

        public void setProperty(String key, Object value) {
            synchronized (lock) {
                properties.put(key, value);
            }
        }

        public Object getProperty(String key) {
            return properties.get(key);
        }

        public void addOutgoingEdge(String edgeId) {
            outgoingEdges.add(edgeId);
        }

        public void addIncomingEdge(String edgeId) {
            incomingEdges.add(edgeId);
        }

        public void removeOutgoingEdge(String edgeId) {
            outgoingEdges.remove(edgeId);
        }

        public void removeIncomingEdge(String edgeId) {
            incomingEdges.remove(edgeId);
        }

        public int getDegree() {
            return outgoingEdges.size() + incomingEdges.size();
        }

        @Override
        public String toString() {
            return String.format("Vertex{id='%s', degree=%d}", id, getDegree());
        }
    }

    public static class Edge {
        private final String id;
        private final String sourceId;
        private final String targetId;
        private final Map<String, Object> properties;
        private final boolean directed;
        private double weight;

        public Edge(String id, String sourceId, String targetId, boolean directed) {
            this(id, sourceId, targetId, directed, 1.0);
        }

        public Edge(String id, String sourceId, String targetId, boolean directed, double weight) {
            this.id = id;
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.directed = directed;
            this.weight = weight;
            this.properties = new ConcurrentHashMap<>();
        }

        public String getId() { return id; }
        public String getSourceId() { return sourceId; }
        public String getTargetId() { return targetId; }
        public boolean isDirected() { return directed; }
        public double getWeight() { return weight; }
        public Map<String, Object> getProperties() { return new HashMap<>(properties); }

        public void setWeight(double weight) { this.weight = weight; }
        public void setProperty(String key, Object value) { properties.put(key, value); }
        public Object getProperty(String key) { return properties.get(key); }

        @Override
        public String toString() {
            String arrow = directed ? " -> " : " -- ";
            return String.format("Edge{%s%s%s, weight=%.2f}", sourceId, arrow, targetId, weight);
        }
    }

    public static class GraphSnapshot {
        private final Map<String, Vertex> vertices;
        private final Map<String, Edge> edges;
        private final long timestamp;
        private final String snapshotId;

        public GraphSnapshot(Map<String, Vertex> vertices, Map<String, Edge> edges) {
            this.vertices = new HashMap<>(vertices);
            this.edges = new HashMap<>(edges);
            this.timestamp = System.currentTimeMillis();
            this.snapshotId = UUID.randomUUID().toString();
        }

        public Map<String, Vertex> getVertices() { return new HashMap<>(vertices); }
        public Map<String, Edge> getEdges() { return new HashMap<>(edges); }
        public long getTimestamp() { return timestamp; }
        public String getSnapshotId() { return snapshotId; }

        public int getVertexCount() { return vertices.size(); }
        public int getEdgeCount() { return edges.size(); }
    }

    public static class GraphAnalytics {
        private final AtomicLong totalOperations;
        private final AtomicLong totalTraversals;
        private final Map<String, Long> operationCounts;
        private final Map<String, Long> executionTimes;

        public GraphAnalytics() {
            this.totalOperations = new AtomicLong(0);
            this.totalTraversals = new AtomicLong(0);
            this.operationCounts = new ConcurrentHashMap<>();
            this.executionTimes = new ConcurrentHashMap<>();
        }

        public void recordOperation(String operation, long executionTime) {
            totalOperations.incrementAndGet();
            operationCounts.merge(operation, 1L, Long::sum);
            executionTimes.merge(operation, executionTime, Long::sum);
        }

        public void recordTraversal() {
            totalTraversals.incrementAndGet();
        }

        public long getTotalOperations() { return totalOperations.get(); }
        public long getTotalTraversals() { return totalTraversals.get(); }
        public Map<String, Long> getOperationCounts() { return new HashMap<>(operationCounts); }
        public Map<String, Long> getExecutionTimes() { return new HashMap<>(executionTimes); }

        public double getAverageExecutionTime(String operation) {
            Long count = operationCounts.get(operation);
            Long totalTime = executionTimes.get(operation);
            return (count != null && totalTime != null && count > 0) ? (double) totalTime / count : 0.0;
        }
    }

    // Engine instance variables
    private final Map<String, Vertex> vertices;
    private final Map<String, Edge> edges;
    private final ExecutorService executorService;
    private final GraphAnalytics analytics;
    private final AttentionRecognitionFramework attentionFramework;
    private final ReadWriteLock graphLock;
    private final boolean enableConcurrentOperations;

    public ScalableConcurrentGraphEngine() {
        this(true, 4);
    }

    public ScalableConcurrentGraphEngine(boolean enableConcurrentOperations, int threadPoolSize) {
        this.vertices = new ConcurrentHashMap<>();
        this.edges = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.analytics = new GraphAnalytics();
        this.attentionFramework = null; // Can be set later
        this.graphLock = new ReentrantReadWriteLock();
        this.enableConcurrentOperations = enableConcurrentOperations;
    }

    public ScalableConcurrentGraphEngine(AttentionRecognitionFramework attentionFramework) {
        this(true, 4);
    }

    // Vertex operations
    public Vertex addVertex(String vertexId) {
        long startTime = System.currentTimeMillis();

        graphLock.writeLock().lock();
        try {
            if (vertices.containsKey(vertexId)) {
                return vertices.get(vertexId);
            }

            Vertex vertex = new Vertex(vertexId);
            vertices.put(vertexId, vertex);
            analytics.recordOperation("addVertex", System.currentTimeMillis() - startTime);
            return vertex;
        } finally {
            graphLock.writeLock().unlock();
        }
    }

    public Vertex getVertex(String vertexId) {
        graphLock.readLock().lock();
        try {
            return vertices.get(vertexId);
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public boolean removeVertex(String vertexId) {
        long startTime = System.currentTimeMillis();

        graphLock.writeLock().lock();
        try {
            Vertex vertex = vertices.get(vertexId);
            if (vertex == null) {
                return false;
            }

            // Remove all connected edges
            Set<String> edgesToRemove = new HashSet<>();
            edgesToRemove.addAll(vertex.getOutgoingEdges());
            edgesToRemove.addAll(vertex.getIncomingEdges());

            for (String edgeId : edgesToRemove) {
                removeEdge(edgeId);
            }

            vertices.remove(vertexId);
            analytics.recordOperation("removeVertex", System.currentTimeMillis() - startTime);
            return true;
        } finally {
            graphLock.writeLock().unlock();
        }
    }

    // Edge operations
    public Edge addEdge(String edgeId, String sourceId, String targetId, boolean directed) {
        return addEdge(edgeId, sourceId, targetId, directed, 1.0);
    }

    public Edge addEdge(String edgeId, String sourceId, String targetId, boolean directed, double weight) {
        long startTime = System.currentTimeMillis();

        graphLock.writeLock().lock();
        try {
            if (edges.containsKey(edgeId)) {
                return edges.get(edgeId);
            }

            // Ensure vertices exist
            if (!vertices.containsKey(sourceId) || !vertices.containsKey(targetId)) {
                throw new IllegalArgumentException("Source or target vertex does not exist");
            }

            Edge edge = new Edge(edgeId, sourceId, targetId, directed, weight);
            edges.put(edgeId, edge);

            // Update vertex edge lists
            vertices.get(sourceId).addOutgoingEdge(edgeId);
            vertices.get(targetId).addIncomingEdge(edgeId);

            if (!directed) {
                vertices.get(targetId).addOutgoingEdge(edgeId);
                vertices.get(sourceId).addIncomingEdge(edgeId);
            }

            analytics.recordOperation("addEdge", System.currentTimeMillis() - startTime);
            return edge;
        } finally {
            graphLock.writeLock().unlock();
        }
    }

    public Edge getEdge(String edgeId) {
        graphLock.readLock().lock();
        try {
            return edges.get(edgeId);
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public boolean removeEdge(String edgeId) {
        long startTime = System.currentTimeMillis();

        graphLock.writeLock().lock();
        try {
            Edge edge = edges.get(edgeId);
            if (edge == null) {
                return false;
            }

            // Update vertex edge lists
            Vertex source = vertices.get(edge.getSourceId());
            Vertex target = vertices.get(edge.getTargetId());

            if (source != null) {
                source.removeOutgoingEdge(edgeId);
                if (!edge.isDirected()) {
                    source.removeIncomingEdge(edgeId);
                }
            }

            if (target != null) {
                target.removeIncomingEdge(edgeId);
                if (!edge.isDirected()) {
                    target.removeOutgoingEdge(edgeId);
                }
            }

            edges.remove(edgeId);
            analytics.recordOperation("removeEdge", System.currentTimeMillis() - startTime);
            return true;
        } finally {
            graphLock.writeLock().unlock();
        }
    }

    // Graph traversal operations
    public List<String> breadthFirstSearch(String startVertexId) {
        return breadthFirstSearch(startVertexId, null);
    }

    public List<String> breadthFirstSearch(String startVertexId, String targetVertexId) {
        long startTime = System.currentTimeMillis();

        graphLock.readLock().lock();
        try {
            analytics.recordTraversal();

            if (!vertices.containsKey(startVertexId)) {
                return new ArrayList<>();
            }

            List<String> result = new ArrayList<>();
            Queue<String> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();

            queue.offer(startVertexId);
            visited.add(startVertexId);

            while (!queue.isEmpty()) {
                String currentId = queue.poll();
                result.add(currentId);

                if (targetVertexId != null && currentId.equals(targetVertexId)) {
                    break;
                }

                Vertex current = vertices.get(currentId);
                for (String edgeId : current.getOutgoingEdges()) {
                    Edge edge = edges.get(edgeId);
                    String neighborId = edge.getTargetId().equals(currentId) ?
                        edge.getSourceId() : edge.getTargetId();

                    if (!visited.contains(neighborId)) {
                        visited.add(neighborId);
                        queue.offer(neighborId);
                    }
                }
            }

            analytics.recordOperation("breadthFirstSearch", System.currentTimeMillis() - startTime);
            return result;
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public List<String> depthFirstSearch(String startVertexId) {
        return depthFirstSearch(startVertexId, null);
    }

    public List<String> depthFirstSearch(String startVertexId, String targetVertexId) {
        long startTime = System.currentTimeMillis();

        graphLock.readLock().lock();
        try {
            analytics.recordTraversal();

            if (!vertices.containsKey(startVertexId)) {
                return new ArrayList<>();
            }

            List<String> result = new ArrayList<>();
            Set<String> visited = new HashSet<>();
            depthFirstSearchRecursive(startVertexId, targetVertexId, visited, result);

            analytics.recordOperation("depthFirstSearch", System.currentTimeMillis() - startTime);
            return result;
        } finally {
            graphLock.readLock().unlock();
        }
    }

    private void depthFirstSearchRecursive(String currentId, String targetId,
                                          Set<String> visited, List<String> result) {
        visited.add(currentId);
        result.add(currentId);

        if (targetId != null && currentId.equals(targetId)) {
            return;
        }

        Vertex current = vertices.get(currentId);
        for (String edgeId : current.getOutgoingEdges()) {
            Edge edge = edges.get(edgeId);
            String neighborId = edge.getTargetId().equals(currentId) ?
                edge.getSourceId() : edge.getTargetId();

            if (!visited.contains(neighborId)) {
                depthFirstSearchRecursive(neighborId, targetId, visited, result);
            }
        }
    }

    // Parallel processing operations
    public CompletableFuture<List<String>> parallelBreadthFirstSearch(String startVertexId) {
        if (!enableConcurrentOperations) {
            return CompletableFuture.completedFuture(breadthFirstSearch(startVertexId));
        }

        return CompletableFuture.supplyAsync(() -> breadthFirstSearch(startVertexId), executorService);
    }

    public CompletableFuture<Map<String, Double>> parallelShortestPaths(String startVertexId) {
        if (!enableConcurrentOperations) {
            return CompletableFuture.completedFuture(calculateShortestPaths(startVertexId));
        }

        return CompletableFuture.supplyAsync(() -> calculateShortestPaths(startVertexId), executorService);
    }

    private Map<String, Double> calculateShortestPaths(String startVertexId) {
        Map<String, Double> distances = new HashMap<>();
        PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(
            Map.Entry.comparingByValue()
        );

        for (String vertexId : vertices.keySet()) {
            distances.put(vertexId, vertexId.equals(startVertexId) ? 0.0 : Double.POSITIVE_INFINITY);
        }

        pq.offer(new AbstractMap.SimpleEntry<>(startVertexId, 0.0));

        while (!pq.isEmpty()) {
            Map.Entry<String, Double> current = pq.poll();
            String currentId = current.getKey();
            double currentDistance = current.getValue();

            if (currentDistance > distances.get(currentId)) {
                continue;
            }

            Vertex currentVertex = vertices.get(currentId);
            for (String edgeId : currentVertex.getOutgoingEdges()) {
                Edge edge = edges.get(edgeId);
                String neighborId = edge.getTargetId().equals(currentId) ?
                    edge.getSourceId() : edge.getTargetId();

                double newDistance = currentDistance + edge.getWeight();
                if (newDistance < distances.get(neighborId)) {
                    distances.put(neighborId, newDistance);
                    pq.offer(new AbstractMap.SimpleEntry<>(neighborId, newDistance));
                }
            }
        }

        return distances;
    }

    // Graph analysis operations
    public GraphSnapshot createSnapshot() {
        graphLock.readLock().lock();
        try {
            return new GraphSnapshot(vertices, edges);
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public Map<String, Object> analyzeGraph() {
        graphLock.readLock().lock();
        try {
            Map<String, Object> analysis = new HashMap<>();

            analysis.put("vertexCount", vertices.size());
            analysis.put("edgeCount", edges.size());

            // Calculate degree statistics
            List<Integer> degrees = vertices.values().stream()
                .mapToInt(Vertex::getDegree)
                .boxed()
                .collect(Collectors.toList());

            if (!degrees.isEmpty()) {
                analysis.put("avgDegree", degrees.stream().mapToInt(Integer::intValue).average().orElse(0.0));
                analysis.put("maxDegree", degrees.stream().mapToInt(Integer::intValue).max().orElse(0));
                analysis.put("minDegree", degrees.stream().mapToInt(Integer::intValue).min().orElse(0));
            }

            // Calculate density
            int n = vertices.size();
            int m = edges.size();
            double density = n > 1 ? (2.0 * m) / (n * (n - 1)) : 0.0;
            analysis.put("density", density);

            return analysis;
        } finally {
            graphLock.readLock().unlock();
        }
    }

    // Utility methods
    public Collection<Vertex> getVertices() {
        graphLock.readLock().lock();
        try {
            return new ArrayList<>(vertices.values());
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public Collection<Edge> getEdges() {
        graphLock.readLock().lock();
        try {
            return new ArrayList<>(edges.values());
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public int getVertexCount() {
        graphLock.readLock().lock();
        try {
            return vertices.size();
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public int getEdgeCount() {
        graphLock.readLock().lock();
        try {
            return edges.size();
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public boolean isEmpty() {
        graphLock.readLock().lock();
        try {
            return vertices.isEmpty() && edges.isEmpty();
        } finally {
            graphLock.readLock().unlock();
        }
    }

    public void clear() {
        graphLock.writeLock().lock();
        try {
            vertices.clear();
            edges.clear();
        } finally {
            graphLock.writeLock().unlock();
        }
    }

    public GraphAnalytics getAnalytics() {
        return analytics;
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
