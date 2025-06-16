package edu.ucsb.cs.cognitivedm.framework;

import edu.ucsb.cs.cognitivedm.patterns.PatternDetector;
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
     * Represents a cognitive state with attention, recognition, and wandering metrics.
     */
    public static class CognitiveState implements CognitiveFrameworkInterfaces.CognitiveState {
        private final double attention;
        private final double recognition;
        private final double wandering;
        private final double cognitiveLoad;
        private final boolean inFlowState;
        private final Map<String, Double> metrics;

        /**
         * Creates a new cognitive state.
         */
        public CognitiveState(
            double attention,
            double recognition,
            double wandering,
            double cognitiveLoad,
            boolean inFlowState
        ) {
            this.attention = attention;
            this.recognition = recognition;
            this.wandering = wandering;
            this.cognitiveLoad = cognitiveLoad;
            this.inFlowState = inFlowState;
            this.metrics = new HashMap<>();
        }

        /**
         * Creates a new cognitive state with custom metrics.
         */
        public CognitiveState(
            double attention,
            double recognition,
            double wandering,
            double cognitiveLoad,
            boolean inFlowState,
            Map<String, Double> metrics
        ) {
            this.attention = attention;
            this.recognition = recognition;
            this.wandering = wandering;
            this.cognitiveLoad = cognitiveLoad;
            this.inFlowState = inFlowState;
            this.metrics = new HashMap<>(metrics);
        }

        @Override
        public double getAttention() {
            return attention;
        }

        @Override
        public double getRecognition() {
            return recognition;
        }

        @Override
        public double getWandering() {
            return wandering;
        }

        @Override
        public double getCognitiveLoad() {
            return cognitiveLoad;
        }

        @Override
        public boolean isInFlowState() {
            return inFlowState;
        }

        @Override
        public Map<String, Double> getMetrics() {
            return metrics;
        }

        @Override
        public String toString() {
            return "CognitiveState{" +
                "attention=" + attention +
                ", recognition=" + recognition +
                ", wandering=" + wandering +
                ", cognitiveLoad=" + cognitiveLoad +
                ", inFlowState=" + inFlowState +
                ", metrics=" + metrics +
                '}';
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

        public ProcessingResult(
            int scale,
            CognitiveState state,
            List<Pattern> patterns,
            double cognitiveLoad
        ) {
            this.scale = scale;
            this.state = state;
            this.detectedPatterns = new ArrayList<>(patterns);
            this.cognitiveLoad = cognitiveLoad;
            this.metadata = new HashMap<>();
        }

        // Getters
        public int getScale() {
            return scale;
        }

        public CognitiveState getState() {
            return state;
        }

        public List<Pattern> getDetectedPatterns() {
            return new ArrayList<>(detectedPatterns);
        }

        public double getCognitiveLoad() {
            return cognitiveLoad;
        }

        public Map<String, Object> getMetadata() {
            return new HashMap<>(metadata);
        }

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

        public String getType() {
            return type;
        }

        public double getStrength() {
            return strength;
        }

        public long getDuration() {
            return duration;
        }

        public Map<String, Double> getCharacteristics() {
            return new HashMap<>(characteristics);
        }

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
            this.currentState = new CognitiveState(0.5, 0.5, 0.1, 0.5, false);
            this.stateHistory = new LinkedList<>();
            this.patternDetector = new PatternDetector(scale);
        }

        /**
         * Update scale level with new input and higher-scale influence
         */
        public ProcessingResult update(
            Object input,
            CognitiveState higherScaleInfluence
        ) {
            synchronized (stateLock) {
                // Process input at this scale
                double processedInput = processInput(input);

                // Apply higher-scale influence
                double scaleInfluence = higherScaleInfluence != null
                    ? higherScaleInfluence.getAttention() * 0.3
                    : 0.0;

                // Evolve cognitive state
                CognitiveState newState = currentState.evolve(
                    processedInput + scaleInfluence
                );

                // Update history with bounded memory
                stateHistory.offer(currentState);
                if (stateHistory.size() > 100) {
                    stateHistory.poll();
                }

                currentState = newState;

                // Detect patterns and assess cognitive load
                List<Pattern> patterns = detectPatterns();
                double cognitiveLoad = assessCognitiveLoad();

                ProcessingResult result = new ProcessingResult(
                    scale,
                    currentState,
                    patterns,
                    cognitiveLoad
                );
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
                return Math.min(
                    1.0,
                    Math.abs(((Number) input).doubleValue()) / 100.0
                );
            } else if (input instanceof String) {
                String str = (String) input;
                return Math.min(
                    1.0,
                    str.length() / 50.0 + (str.matches(".*\\d.*") ? 0.2 : 0.0)
                );
            } else if (input instanceof Collection) {
                return Math.min(1.0, ((Collection<?>) input).size() / 20.0);
            }

            // Default processing with some randomness for unknown types
            return Math.random() * 0.5 + 0.25;
        }

        private List<Pattern> detectPatterns() {
            List<edu.ucsb.cs.cognitivedm.patterns.PatternDetector.Pattern> detectorPatterns = patternDetector.analyzeSequence(
                stateHistory
                    .stream()
                    .map(state ->
                        new double[] {
                            state.getAttention(),
                            state.getRecognition(),
                            state.getWandering(),
                        }
                    )
                    .collect(Collectors.toList())
            );

            // Convert detector patterns to framework patterns
            return detectorPatterns.stream()
                .map(dp -> new Pattern(dp.getType(), dp.getConfidence(), dp.getDuration()))
                .collect(Collectors.toList());
        }

        private double assessCognitiveLoad() {
            if (stateHistory.size() < 5) return 0.5;

            // Multi-dimensional cognitive load assessment
            double attentionVariance = calculateVariance(
                stateHistory.stream().mapToDouble(CognitiveState::getAttention)
            );
            double recognitionVariance = calculateVariance(
                stateHistory
                    .stream()
                    .mapToDouble(CognitiveState::getRecognition)
            );
            double wanderingMean = stateHistory
                .stream()
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

        public int getScale() {
            return scale;
        }
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
            List<CompletableFuture<ProcessingResult>> futures =
                new ArrayList<>();

            // Process from finest to coarsest scale
            for (int i = 0; i < numScales; i++) {
                final int scaleIndex = i;
                final CognitiveState higherScaleInfluence = i > 0
                    ? scales.get(i - 1).getCurrentState()
                    : null;

                CompletableFuture<ProcessingResult> future =
                    CompletableFuture.supplyAsync(
                        () ->
                            scales
                                .get(scaleIndex)
                                .update(input, higherScaleInfluence),
                        executorService
                    );
                futures.add(future);
            }

            // Collect results
            try {
                for (CompletableFuture<ProcessingResult> future : futures) {
                    results.add(future.get(1, TimeUnit.SECONDS));
                }
            } catch (
                InterruptedException | ExecutionException | TimeoutException e
            ) {
                System.err.println(
                    "Error in parallel processing: " + e.getMessage()
                );
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
    public int getNumScales() {
        return numScales;
    }

    public List<ScaleLevel> getScales() {
        return new ArrayList<>(scales);
    }

    /**
     * Process input across multiple scales with comprehensive analysis
     */
    public CompletableFuture<List<ProcessingResult>> processMultiScale(
        String input
    ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return process(input, 1);
            },
            executorService
        );
    }

    /**
     * Get current cognitive state (averaged across all scales)
     */
    public CognitiveState getCurrentCognitiveState() {
        if (scales.isEmpty()) {
            return new CognitiveState(0.5, 0.5, 0.3, 0.5, false);
        }

        double avgAttention = scales
            .stream()
            .mapToDouble(scale -> scale.getCurrentState().getAttention())
            .average()
            .orElse(0.5);

        double avgRecognition = scales
            .stream()
            .mapToDouble(scale -> scale.getCurrentState().getRecognition())
            .average()
            .orElse(0.5);

        double avgWandering = scales
            .stream()
            .mapToDouble(scale -> scale.getCurrentState().getWandering())
            .average()
            .orElse(0.3);

        double avgCognitiveLoad = scales
            .stream()
            .mapToDouble(scale -> scale.getCurrentState().getCognitiveLoad())
            .average()
            .orElse(0.5);

        boolean avgInFlowState = scales
            .stream()
            .map(scale -> scale.getCurrentState().isInFlowState())
            .anyMatch(Boolean::booleanValue);

        return new CognitiveState(avgAttention, avgRecognition, avgWandering, avgCognitiveLoad, avgInFlowState);
    }

    /**
     * Get comprehensive system analysis
     */
    public SystemAnalysis getSystemAnalysis() {
        CognitiveState currentState = getCurrentCognitiveState();

        return new SystemAnalysis(
            currentState.getCognitiveLoad(),
            currentState.getAttention(),
            currentState.getRecognition(),
            currentState.getWandering(),
            System.currentTimeMillis(),
            numScales
        );
    }

    /**
     * System-wide analysis and statistics
     */
    public static class SystemAnalysis {

        private final double systemCognitiveLoad;
        private final double averageAttention;
        private final double averageRecognition;
        private final double averageWandering;
        private final long totalProcessingTime;
        private final int totalScales;
        private final Map<String, Double> performanceMetrics;
        private final long timestamp;

        public SystemAnalysis(
            double systemCognitiveLoad,
            double averageAttention,
            double averageRecognition,
            double averageWandering,
            long totalProcessingTime,
            int totalScales
        ) {
            this.systemCognitiveLoad = Math.max(
                0.0,
                Math.min(1.0, systemCognitiveLoad)
            );
            this.averageAttention = averageAttention;
            this.averageRecognition = averageRecognition;
            this.averageWandering = averageWandering;
            this.totalProcessingTime = totalProcessingTime;
            this.totalScales = totalScales;
            this.performanceMetrics = new HashMap<>();
            this.timestamp = System.currentTimeMillis();
        }

        public double getSystemCognitiveLoad() {
            return systemCognitiveLoad;
        }

        public double getAverageAttention() {
            return averageAttention;
        }

        public double getAverageRecognition() {
            return averageRecognition;
        }

        public double getAverageWandering() {
            return averageWandering;
        }

        public long getTotalProcessingTime() {
            return totalProcessingTime;
        }

        public int getTotalScales() {
            return totalScales;
        }

        public Map<String, Double> getPerformanceMetrics() {
            return new HashMap<>(performanceMetrics);
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void addPerformanceMetric(String key, double value) {
            performanceMetrics.put(key, value);
        }

        public String getSystemState() {
            if (systemCognitiveLoad < 0.3) {
                return "LOW_LOAD";
            } else if (systemCognitiveLoad < 0.7) {
                return "NORMAL_LOAD";
            } else {
                return "HIGH_LOAD";
            }
        }

        @Override
        public String toString() {
            return String.format(
                "SystemAnalysis{cognitiveLoad=%.3f, attention=%.3f, recognition=%.3f, wandering=%.3f}",
                systemCognitiveLoad,
                averageAttention,
                averageRecognition,
                averageWandering
            );
        }
    }

    /**
     * Analytics for learning and cognitive data.
     */
    public static class LearningAnalytics {
        private Map<String, Double> metrics;
        
        public LearningAnalytics() {
            this.metrics = new HashMap<>();
        }
        
        public void addMetric(String name, double value) {
            metrics.put(name, value);
        }
        
        public double getMetric(String name) {
            return metrics.getOrDefault(name, 0.0);
        }
        
        public Map<String, Double> getAllMetrics() {
            return new HashMap<>(metrics);
        }
    }

    /**
     * Create a new cognitive state.
     */
    public static CognitiveState createCognitiveState(double attention, double recognition, 
                                                     double wandering) {
        double cognitiveLoad = calculateCognitiveLoad(attention, recognition, wandering);
        boolean inFlow = determineFlowState(attention, recognition, wandering);
        return new CognitiveState(attention, recognition, wandering, cognitiveLoad, inFlow);
    }

    /**
     * Calculate cognitive load based on attention, recognition, and wandering.
     */
    private static double calculateCognitiveLoad(double attention, double recognition, 
                                               double wandering) {
        // Simple model: high attention and recognition with low wandering = lower cognitive load
        return 1.0 - ((attention + recognition) / 2.0) + wandering;
    }

    /**
     * Determine if the learner is in a flow state.
     */
    private static boolean determineFlowState(double attention, double recognition, 
                                            double wandering) {
        // Simple model: high attention and recognition with low wandering = flow state
        return attention > 0.7 && recognition > 0.6 && wandering < 0.3;
    }

    /**
     * Analyze a sequence of cognitive states to identify patterns.
     */
    public LearningAnalytics analyzeSequence(List<CognitiveState> states) {
        LearningAnalytics analytics = new LearningAnalytics();
        
        if (states == null || states.isEmpty()) {
            return analytics;
        }
        
        // Calculate averages
        double avgAttention = states.stream().mapToDouble(CognitiveState::getAttention).average().orElse(0);
        double avgRecognition = states.stream().mapToDouble(CognitiveState::getRecognition).average().orElse(0);
        double avgWandering = states.stream().mapToDouble(CognitiveState::getWandering).average().orElse(0);
        double avgLoad = states.stream().mapToDouble(CognitiveState::getCognitiveLoad).average().orElse(0);
        
        analytics.addMetric("avg_attention", avgAttention);
        analytics.addMetric("avg_recognition", avgRecognition);
        analytics.addMetric("avg_wandering", avgWandering);
        analytics.addMetric("avg_cognitive_load", avgLoad);
        
        // Calculate flow percentage
        double flowPercentage = states.stream().filter(CognitiveState::isInFlowState).count() / (double)states.size();
        analytics.addMetric("flow_percentage", flowPercentage);
        
        return analytics;
    }

    /**
     * Predict future cognitive state based on current state and context.
     */
    public CompletableFuture<CognitiveState> predictFutureState(CognitiveState currentState, 
                                                              String context) {
        return CompletableFuture.supplyAsync(() -> {
            // Simple prediction model
            double predictedAttention = Math.min(currentState.getAttention() * 1.1, 1.0);
            double predictedRecognition = Math.min(currentState.getRecognition() * 1.05, 1.0);
            double predictedWandering = Math.max(currentState.getWandering() * 0.95, 0.0);
            
            return createCognitiveState(predictedAttention, predictedRecognition, predictedWandering);
        });
    }
}
