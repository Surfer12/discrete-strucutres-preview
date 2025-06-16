package edu.ucsb.cs.cognitivedm.framework;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Attention-Recognition Decoupling Framework
 *
 * Implements a multi-scale cognitive processing system that models the dynamic
 * relationship between attention and recognition processes, supporting mind-wandering
 * detection and adaptive cognitive load management.
 *
 * Based on the fractal communication principle z = z² + c where:
 * - z represents current cognitive state
 * - z² represents recursive elaboration of existing patterns
 * - c represents novel input that shifts trajectory
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class AttentionRecognitionFramework {
    private final int numScales;
    private final List<ScaleLevel> scales;
    private final MetaAwarenessProcessor metaProcessor;
    private final ExecutorService executorService;
    private final CognitiveBiasAdjuster biasAdjuster;

    /**
     * Represents a cognitive state at a specific moment in time
     */
    public static class CognitiveState {
        private final double attention;      // Focus level [0,1]
        private final double recognition;    // Pattern recognition strength [0,1]
        private final double wandering;      // Mind-wandering intensity [0,1]
        private final long timestamp;
        private final Map<String, Double> contextualFactors;

        public CognitiveState(double attention, double recognition, double wandering) {
            this(attention, recognition, wandering, new HashMap<>());
        }

        public CognitiveState(double attention, double recognition, double wandering,
                            Map<String, Double> contextualFactors) {
            this.attention = Math.max(0, Math.min(1, attention));
            this.recognition = Math.max(0, Math.min(1, recognition));
            this.wandering = Math.max(0, Math.min(1, wandering));
            this.timestamp = System.currentTimeMillis();
            this.contextualFactors = new HashMap<>(contextualFactors);
        }

        /**
         * Recursive state evolution implementing z = z² + c
         * @param novelInput The 'c' component - external influence
         * @return New evolved cognitive state
         */
        public CognitiveState evolve(double novelInput) {
            // z² component: recursive elaboration of current state
            double attentionSquared = attention * attention;
            double recognitionSquared = recognition * recognition;
            double wanderingSquared = wandering * wandering;

            // c component: novel input influence with adaptive weighting
            double adaptiveInfluence = calculateAdaptiveInfluence(novelInput);

            // New state calculation with bounded evolution
            double newAttention = boundedEvolution(
                attentionSquared + adaptiveInfluence * 0.1,
                0.05 // minimum change threshold
            );

            double newRecognition = boundedEvolution(
                recognitionSquared + (1 - wandering) * adaptiveInfluence * 0.2,
                0.03
            );

            double newWandering = boundedEvolution(
                wanderingSquared + (novelInput > 0.5 ? -0.1 : 0.05) * adaptiveInfluence,
                0.02
            );

            // Update contextual factors
            Map<String, Double> newContextualFactors = evolveContextualFactors(novelInput);

            return new CognitiveState(newAttention, newRecognition, newWandering, newContextualFactors);
        }

        private double calculateAdaptiveInfluence(double novelInput) {
            // Adaptive influence based on current cognitive load
            double cognitiveLoad = (attention + recognition + (1 - wandering)) / 3.0;
            return novelInput * (1 - cognitiveLoad * 0.5); // Reduce influence when overloaded
        }

        private double boundedEvolution(double value, double minChange) {
            double evolved = Math.max(0, Math.min(1, value));
            // Ensure minimum change for system dynamics
            return Math.abs(evolved - value) < minChange ? value + minChange : evolved;
        }

        private Map<String, Double> evolveContextualFactors(double novelInput) {
            Map<String, Double> evolved = new HashMap<>(contextualFactors);
            evolved.put("novelty_sensitivity",
                evolved.getOrDefault("novelty_sensitivity", 0.5) * 0.9 + novelInput * 0.1);
            evolved.put("stability_preference",
                evolved.getOrDefault("stability_preference", 0.5) + (1 - wandering) * 0.05);
            return evolved;
        }

        // Getters
        public double getAttention() { return attention; }
        public double getRecognition() { return recognition; }
        public double getWandering() { return wandering; }
        public long getTimestamp() { return timestamp; }
        public Map<String, Double> getContextualFactors() { return new HashMap<>(contextualFactors); }

        @Override
        public String toString() {
            return String.format("CognitiveState{attention=%.3f, recognition=%.3f, wandering=%.3f}",
                               attention, recognition, wandering);
        }
    }

    /**
     * Represents processing results with detected patterns and cognitive metrics
     */
    public static class ProcessingResult {
        private final int scale;
        private final CognitiveState state;
        private final List<Pattern> detectedPatterns;
        private final double cognitiveLoad;
        private final Map<String, Object> metadata;

        public ProcessingResult(int scale, CognitiveState state,
                              List<Pattern> patterns, double cognitiveLoad) {
            this.scale = scale;
            this.state = state;
            this.detectedPatterns = new ArrayList<>(patterns);
            this.cognitiveLoad = cognitiveLoad;
            this.metadata = new HashMap<>();
        }

        // Getters
        public int getScale() { return scale; }
        public CognitiveState getState() { return state; }
        public List<Pattern> getDetectedPatterns() { return new ArrayList<>(detectedPatterns); }
        public double getCognitiveLoad() { return cognitiveLoad; }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }

        public void addMetadata(String key, Object value) {
            metadata.put(key, value);
        }
    }

    /**
     * Represents a detected pattern in cognitive processing
     */
    public static class Pattern {
        private final String type;
        private final double strength;
        private final long duration;
        private final Map<String, Double> characteristics;

        public Pattern(String type, double strength, long duration) {
            this.type = type;
            this.strength = strength;
            this.duration = duration;
            this.characteristics = new HashMap<>();
        }

        public String getType() { return type; }
        public double getStrength() { return strength; }
        public long getDuration() { return duration; }
        public Map<String, Double> getCharacteristics() { return new HashMap<>(characteristics); }

        public void addCharacteristic(String key, double value) {
            characteristics.put(key, value);
        }
    }

    /**
     * Individual scale level processor
     */
    public class ScaleLevel {
        private final int scale;
        private volatile CognitiveState currentState;
        private final Queue<CognitiveState> stateHistory;
        private final PatternDetector patternDetector;
        private final Object stateLock = new Object();

        public ScaleLevel(int scale) {
            this.scale = scale;
            this.currentState = new CognitiveState(0.5, 0.5, 0.1);
            this.stateHistory = new LinkedList<>();
            this.patternDetector = new PatternDetector(scale);
        }

        /**
         * Update scale level with new input and higher-scale influence
         */
        public ProcessingResult update(Object input, CognitiveState higherScaleInfluence) {
            synchronized (stateLock) {
                // Process input at this scale
                double processedInput = processInput(input);

                // Apply higher-scale influence
                double scaleInfluence = higherScaleInfluence != null ?
                    higherScaleInfluence.getAttention() * 0.3 : 0.0;

                // Evolve cognitive state
                CognitiveState newState = currentState.evolve(processedInput + scaleInfluence);

                // Update history with bounded memory
                stateHistory.offer(currentState);
                if (stateHistory.size() > 100) {
                    stateHistory.poll();
                }

                currentState = newState;

                // Detect patterns and assess cognitive load
                List<Pattern> patterns = detectPatterns();
                double cognitiveLoad = assessCognitiveLoad();

                ProcessingResult result = new ProcessingResult(scale, currentState, patterns, cognitiveLoad);
                result.addMetadata("input_processed", processedInput);
                result.addMetadata("scale_influence", scaleInfluence);
                result.addMetadata("history_size", stateHistory.size());

                return result;
            }
        }

        private double processInput(Object input) {
            if (input == null) return 0.1;

            // Enhanced input processing based on type and complexity
            if (input instanceof Number) {
                return Math.min(1.0, Math.abs(((Number) input).doubleValue()) / 100.0);
            } else if (input instanceof String) {
                String str = (String) input;
                return Math.min(1.0, str.length() / 50.0 + (str.matches(".*\\d.*") ? 0.2 : 0.0));
            } else if (input instanceof Collection) {
                return Math.min(1.0, ((Collection<?>) input).size() / 20.0);
            }

            // Default processing with some randomness for unknown types
            return Math.random() * 0.5 + 0.25;
        }

        private List<Pattern> detectPatterns() {
            return patternDetector.analyzeSequence(
                stateHistory.stream()
                    .map(state -> new double[]{
                        state.getAttention(),
                        state.getRecognition(),
                        state.getWandering()
                    })
                    .collect(Collectors.toList())
            );
        }

        private double assessCognitiveLoad() {
            if (stateHistory.size() < 5) return 0.5;

            // Multi-dimensional cognitive load assessment
            double attentionVariance = calculateVariance(
                stateHistory.stream().mapToDouble(CognitiveState::getAttention)
            );
            double recognitionVariance = calculateVariance(
                stateHistory.stream().mapToDouble(CognitiveState::getRecognition)
            );
            double wanderingMean = stateHistory.stream()
                .mapToDouble(CognitiveState::getWandering)
                .average()
                .orElse(0.5);

            // Combined cognitive load metric
            double varianceLoad = (attentionVariance + recognitionVariance) * 2;
            double wanderingLoad = wanderingMean;

            return Math.min(1.0, (varianceLoad * 0.7 + wanderingLoad * 0.3));
        }

        private double calculateVariance(java.util.stream.DoubleStream values) {
            double[] vals = values.toArray();
            if (vals.length == 0) return 0.0;

            double mean = Arrays.stream(vals).average().orElse(0.0);
            return Arrays.stream(vals)
                .map(x -> (x - mean) * (x - mean))
                .average()
                .orElse(0.0);
        }

        public CognitiveState getCurrentState() {
            return currentState;
        }

        public int getScale() { return scale; }
    }

    /**
     * Constructor
     */
    public AttentionRecognitionFramework(int numScales) {
        this.numScales = numScales;
        this.scales = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(numScales);
        this.biasAdjuster = new CognitiveBiasAdjuster();

        // Initialize scale levels
        for (int i = 0; i < numScales; i++) {
            scales.add(new ScaleLevel(i));
        }

        this.metaProcessor = new MetaAwarenessProcessor(scales);
    }

    /**
     * Process input across all scales with recursive multi-scale integration
     */
    public List<ProcessingResult> process(Object input, int timeSteps) {
        List<ProcessingResult> results = new ArrayList<>();

        for (int step = 0; step < timeSteps; step++) {
            List<CompletableFuture<ProcessingResult>> futures = new ArrayList<>();

            // Process from finest to coarsest scale
            for (int i = 0; i < numScales; i++) {
                final int scaleIndex = i;
                final CognitiveState higherScaleInfluence = i > 0 ?
                    scales.get(i - 1).getCurrentState() : null;

                CompletableFuture<ProcessingResult> future = CompletableFuture.supplyAsync(() ->
                    scales.get(scaleIndex).update(input, higherScaleInfluence),
                    executorService
                );
                futures.add(future);
            }

            // Collect results
            try {
                for (CompletableFuture<ProcessingResult> future : futures) {
                    results.add(future.get(1, TimeUnit.SECONDS));
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                System.err.println("Error in parallel processing: " + e.getMessage());
            }

            // Apply meta-awareness processing
            metaProcessor.processMetaAwareness(results);

            // Propagate influence back from coarser to finer scales
            propagateDownwardInfluence();
        }

        return results;
    }

    /**
     * Propagate influence from coarser to finer scales
     */
    private void propagateDownwardInfluence() {
        for (int i = numScales - 1; i > 0; i--) {
            CognitiveState coarserState = scales.get(i).getCurrentState();
            // This influence will be used in the next processing cycle
            // Implementation depends on specific downward influence model
        }
    }

    /**
     * Get current meta-cognitive assessment
     */
    public MetaAwarenessProcessor.MetaAssessment getMetaAssessment() {
        return metaProcessor.getCurrentAssessment();
    }

    /**
     * Shutdown framework
     */
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

    // Getters
    public int getNumScales() { return numScales; }
    public List<ScaleLevel> getScales() { return new ArrayList<>(scales); }
}
