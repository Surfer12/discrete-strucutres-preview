package edu.ucsb.cs.cognitivedm.framework;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * MetaAwarenessProcessor for cognitive meta-awareness and self-monitoring
 *
 * Implements meta-cognitive processing that monitors and integrates cognitive states
 * across multiple scales, detecting attention lapses, mind-wandering episodes,
 * and providing meta-cognitive insights for the attention-recognition framework.
 *
 * Based on the recursive integration principle where higher-order awareness
 * monitors and modulates lower-level cognitive processes.
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class MetaAwarenessProcessor {
    private final List<AttentionRecognitionFramework.ScaleLevel> scales;
    private final Map<String, Double> metaMetrics;
    private final Queue<MetaAssessment> assessmentHistory;
    private final int maxHistorySize;
    private volatile MetaAssessment currentAssessment;

    /**
     * Represents a meta-cognitive assessment at a point in time
     */
    public static class MetaAssessment {
        private final double metaAwarenessLevel;
        private final double cognitiveCoherence;
        private final double attentionalStability;
        private final double mindWanderingIntensity;
        private final long timestamp;
        private final Map<String, Double> scaleMetrics;
        private final List<String> detectedPhenomena;

        public MetaAssessment(double metaAwarenessLevel, double cognitiveCoherence,
                            double attentionalStability, double mindWanderingIntensity,
                            Map<String, Double> scaleMetrics, List<String> phenomena) {
            this.metaAwarenessLevel = Math.max(0.0, Math.min(1.0, metaAwarenessLevel));
            this.cognitiveCoherence = Math.max(0.0, Math.min(1.0, cognitiveCoherence));
            this.attentionalStability = Math.max(0.0, Math.min(1.0, attentionalStability));
            this.mindWanderingIntensity = Math.max(0.0, Math.min(1.0, mindWanderingIntensity));
            this.timestamp = System.currentTimeMillis();
            this.scaleMetrics = new HashMap<>(scaleMetrics);
            this.detectedPhenomena = new ArrayList<>(phenomena);
        }

        // Getters
        public double getMetaAwarenessLevel() { return metaAwarenessLevel; }
        public double getCognitiveCoherence() { return cognitiveCoherence; }
        public double getAttentionalStability() { return attentionalStability; }
        public double getMindWanderingIntensity() { return mindWanderingIntensity; }
        public long getTimestamp() { return timestamp; }
        public Map<String, Double> getScaleMetrics() { return new HashMap<>(scaleMetrics); }
        public List<String> getDetectedPhenomena() { return new ArrayList<>(detectedPhenomena); }

        /**
         * Calculate overall cognitive wellness score
         */
        public double getCognitiveWellnessScore() {
            return (metaAwarenessLevel * 0.3 +
                   cognitiveCoherence * 0.3 +
                   attentionalStability * 0.25 +
                   (1.0 - mindWanderingIntensity) * 0.15);
        }

        @Override
        public String toString() {
            return String.format("MetaAssessment{awareness=%.3f, coherence=%.3f, stability=%.3f, wandering=%.3f, wellness=%.3f}",
                               metaAwarenessLevel, cognitiveCoherence, attentionalStability,
                               mindWanderingIntensity, getCognitiveWellnessScore());
        }
    }

    /**
     * Constructor
     */
    public MetaAwarenessProcessor(List<AttentionRecognitionFramework.ScaleLevel> scales) {
        this.scales = new ArrayList<>(scales);
        this.metaMetrics = new ConcurrentHashMap<>();
        this.assessmentHistory = new LinkedList<>();
        this.maxHistorySize = 50;
        this.currentAssessment = createInitialAssessment();
        initializeMetaMetrics();
    }

    /**
     * Process meta-awareness across all processing results
     */
    public void processMetaAwareness(List<AttentionRecognitionFramework.ProcessingResult> results) {
        if (results == null || results.isEmpty()) return;

        // Extract cross-scale patterns
        Map<String, Double> scaleMetrics = analyzeCrossScalePatterns(results);

        // Calculate meta-cognitive metrics
        double metaAwarenessLevel = calculateMetaAwarenessLevel(results);
        double cognitiveCoherence = calculateCognitiveCoherence(results);
        double attentionalStability = calculateAttentionalStability(results);
        double mindWanderingIntensity = calculateMindWanderingIntensity(results);

        // Detect meta-cognitive phenomena
        List<String> phenomena = detectMetaCognitivePhenomena(results);

        // Create new assessment
        MetaAssessment newAssessment = new MetaAssessment(
            metaAwarenessLevel, cognitiveCoherence, attentionalStability,
            mindWanderingIntensity, scaleMetrics, phenomena
        );

        // Update state
        updateAssessmentHistory(newAssessment);
        currentAssessment = newAssessment;

        // Update meta-metrics
        updateMetaMetrics(newAssessment);
    }

    /**
     * Calculate meta-awareness level based on cognitive state monitoring
     */
    private double calculateMetaAwarenessLevel(List<AttentionRecognitionFramework.ProcessingResult> results) {
        // Meta-awareness is high when the system can accurately assess its own state
        double totalAwareness = 0.0;
        int validResults = 0;

        for (AttentionRecognitionFramework.ProcessingResult result : results) {
            AttentionRecognitionFramework.CognitiveState state = result.getState();

            // High attention with low wandering indicates good meta-awareness
            double stateAwareness = state.getAttention() * (1.0 - state.getWandering());

            // Pattern detection also indicates meta-cognitive monitoring
            double patternAwareness = Math.min(1.0, result.getDetectedPatterns().size() / 5.0);

            totalAwareness += (stateAwareness * 0.7 + patternAwareness * 0.3);
            validResults++;
        }

        return validResults > 0 ? totalAwareness / validResults : 0.5;
    }

    /**
     * Calculate cognitive coherence across scales
     */
    private double calculateCognitiveCoherence(List<AttentionRecognitionFramework.ProcessingResult> results) {
        if (results.size() < 2) return 0.5;

        // Coherence is high when different scales show consistent patterns
        double totalCoherence = 0.0;
        int comparisons = 0;

        for (int i = 0; i < results.size() - 1; i++) {
            for (int j = i + 1; j < results.size(); j++) {
                AttentionRecognitionFramework.CognitiveState state1 = results.get(i).getState();
                AttentionRecognitionFramework.CognitiveState state2 = results.get(j).getState();

                // Calculate state similarity
                double attentionSimilarity = 1.0 - Math.abs(state1.getAttention() - state2.getAttention());
                double recognitionSimilarity = 1.0 - Math.abs(state1.getRecognition() - state2.getRecognition());
                double wanderingSimilarity = 1.0 - Math.abs(state1.getWandering() - state2.getWandering());

                double similarity = (attentionSimilarity + recognitionSimilarity + wanderingSimilarity) / 3.0;
                totalCoherence += similarity;
                comparisons++;
            }
        }

        return comparisons > 0 ? totalCoherence / comparisons : 0.5;
    }

    /**
     * Calculate attentional stability over time
     */
    private double calculateAttentionalStability(List<AttentionRecognitionFramework.ProcessingResult> results) {
        if (results.isEmpty()) return 0.5;

        // Extract attention values
        double[] attentionValues = results.stream()
            .mapToDouble(r -> r.getState().getAttention())
            .toArray();

        // Calculate stability as inverse of variance
        double mean = Arrays.stream(attentionValues).average().orElse(0.5);
        double variance = Arrays.stream(attentionValues)
            .map(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);

        // Convert variance to stability score (lower variance = higher stability)
        return Math.max(0.0, 1.0 - variance * 4.0); // Scale factor for reasonable range
    }

    /**
     * Calculate mind-wandering intensity
     */
    private double calculateMindWanderingIntensity(List<AttentionRecognitionFramework.ProcessingResult> results) {
        if (results.isEmpty()) return 0.1;

        // Average wandering across all scales
        double totalWandering = results.stream()
            .mapToDouble(r -> r.getState().getWandering())
            .average()
            .orElse(0.1);

        // Weight by cognitive load (high load with high wandering is more concerning)
        double avgCognitiveLoad = results.stream()
            .mapToDouble(AttentionRecognitionFramework.ProcessingResult::getCognitiveLoad)
            .average()
            .orElse(0.5);

        return totalWandering * (1.0 + avgCognitiveLoad * 0.5);
    }

    /**
     * Analyze patterns across different scales
     */
    private Map<String, Double> analyzeCrossScalePatterns(List<AttentionRecognitionFramework.ProcessingResult> results) {
        Map<String, Double> scaleMetrics = new HashMap<>();

        // Group results by scale
        Map<Integer, List<AttentionRecognitionFramework.ProcessingResult>> byScale = results.stream()
            .collect(Collectors.groupingBy(AttentionRecognitionFramework.ProcessingResult::getScale));

        // Analyze each scale
        for (Map.Entry<Integer, List<AttentionRecognitionFramework.ProcessingResult>> entry : byScale.entrySet()) {
            int scale = entry.getKey();
            List<AttentionRecognitionFramework.ProcessingResult> scaleResults = entry.getValue();

            // Calculate scale-specific metrics
            double scaleAttention = scaleResults.stream()
                .mapToDouble(r -> r.getState().getAttention())
                .average()
                .orElse(0.5);

            double scaleCognitiveLoad = scaleResults.stream()
                .mapToDouble(AttentionRecognitionFramework.ProcessingResult::getCognitiveLoad)
                .average()
                .orElse(0.5);

            int totalPatterns = scaleResults.stream()
                .mapToInt(r -> r.getDetectedPatterns().size())
                .sum();

            scaleMetrics.put("scale_" + scale + "_attention", scaleAttention);
            scaleMetrics.put("scale_" + scale + "_cognitive_load", scaleCognitiveLoad);
            scaleMetrics.put("scale_" + scale + "_pattern_count", (double) totalPatterns);
        }

        // Cross-scale correlations
        if (byScale.size() > 1) {
            scaleMetrics.put("cross_scale_coherence", calculateCrossScaleCoherence(byScale));
        }

        return scaleMetrics;
    }

    /**
     * Calculate coherence between different scales
     */
    private double calculateCrossScaleCoherence(Map<Integer, List<AttentionRecognitionFramework.ProcessingResult>> byScale) {
        List<Integer> scaleKeys = new ArrayList<>(byScale.keySet());
        double totalCoherence = 0.0;
        int comparisons = 0;

        for (int i = 0; i < scaleKeys.size() - 1; i++) {
            for (int j = i + 1; j < scaleKeys.size(); j++) {
                List<AttentionRecognitionFramework.ProcessingResult> scale1 = byScale.get(scaleKeys.get(i));
                List<AttentionRecognitionFramework.ProcessingResult> scale2 = byScale.get(scaleKeys.get(j));

                double coherence = calculateScaleCoherence(scale1, scale2);
                totalCoherence += coherence;
                comparisons++;
            }
        }

        return comparisons > 0 ? totalCoherence / comparisons : 0.5;
    }

    /**
     * Calculate coherence between two scales
     */
    private double calculateScaleCoherence(List<AttentionRecognitionFramework.ProcessingResult> scale1,
                                         List<AttentionRecognitionFramework.ProcessingResult> scale2) {
        if (scale1.isEmpty() || scale2.isEmpty()) return 0.5;

        // Calculate average states for each scale
        double avg1Attention = scale1.stream().mapToDouble(r -> r.getState().getAttention()).average().orElse(0.5);
        double avg1Recognition = scale1.stream().mapToDouble(r -> r.getState().getRecognition()).average().orElse(0.5);

        double avg2Attention = scale2.stream().mapToDouble(r -> r.getState().getAttention()).average().orElse(0.5);
        double avg2Recognition = scale2.stream().mapToDouble(r -> r.getState().getRecognition()).average().orElse(0.5);

        // Calculate similarity
        double attentionSimilarity = 1.0 - Math.abs(avg1Attention - avg2Attention);
        double recognitionSimilarity = 1.0 - Math.abs(avg1Recognition - avg2Recognition);

        return (attentionSimilarity + recognitionSimilarity) / 2.0;
    }

    /**
     * Detect meta-cognitive phenomena
     */
    private List<String> detectMetaCognitivePhenomena(List<AttentionRecognitionFramework.ProcessingResult> results) {
        List<String> phenomena = new ArrayList<>();

        if (results.isEmpty()) return phenomena;

        // Detect attention lapses
        long attentionLapses = results.stream()
            .filter(r -> r.getState().getAttention() < 0.3)
            .count();

        if (attentionLapses > results.size() * 0.3) {
            phenomena.add("FrequentAttentionLapses");
        }

        // Detect mind-wandering episodes
        long wanderingEpisodes = results.stream()
            .filter(r -> r.getState().getWandering() > 0.7)
            .count();

        if (wanderingEpisodes > results.size() * 0.2) {
            phenomena.add("ExtendedMindWandering");
        }

        // Detect cognitive overload
        long overloadInstances = results.stream()
            .filter(r -> r.getCognitiveLoad() > 0.8)
            .count();

        if (overloadInstances > results.size() * 0.4) {
            phenomena.add("CognitiveOverload");
        }

        // Detect pattern clusters
        int totalPatterns = results.stream()
            .mapToInt(r -> r.getDetectedPatterns().size())
            .sum();

        if (totalPatterns > results.size() * 2) {
            phenomena.add("RichPatternActivity");
        }

        // Detect state instability
        double attentionVariance = calculateAttentionVariance(results);
        if (attentionVariance > 0.3) {
            phenomena.add("AttentionalInstability");
        }

        // Detect cross-scale disconnection
        if (results.size() > 1) {
            double coherence = calculateCognitiveCoherence(results);
            if (coherence < 0.4) {
                phenomena.add("CrossScaleDisconnection");
            }
        }

        return phenomena;
    }

    /**
     * Calculate attention variance across results
     */
    private double calculateAttentionVariance(List<AttentionRecognitionFramework.ProcessingResult> results) {
        if (results.isEmpty()) return 0.0;

        double[] attentionValues = results.stream()
            .mapToDouble(r -> r.getState().getAttention())
            .toArray();

        double mean = Arrays.stream(attentionValues).average().orElse(0.5);
        return Arrays.stream(attentionValues)
            .map(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);
    }

    /**
     * Update assessment history
     */
    private void updateAssessmentHistory(MetaAssessment assessment) {
        assessmentHistory.offer(assessment);
        while (assessmentHistory.size() > maxHistorySize) {
            assessmentHistory.poll();
        }
    }

    /**
     * Update meta-metrics
     */
    private void updateMetaMetrics(MetaAssessment assessment) {
        metaMetrics.put("current_awareness", assessment.getMetaAwarenessLevel());
        metaMetrics.put("current_coherence", assessment.getCognitiveCoherence());
        metaMetrics.put("current_stability", assessment.getAttentionalStability());
        metaMetrics.put("current_wandering", assessment.getMindWanderingIntensity());
        metaMetrics.put("wellness_score", assessment.getCognitiveWellnessScore());

        // Calculate historical trends
        if (assessmentHistory.size() > 5) {
            double[] awarenessHistory = assessmentHistory.stream()
                .mapToDouble(MetaAssessment::getMetaAwarenessLevel)
                .toArray();

            double awarenessTrend = calculateTrend(awarenessHistory);
            metaMetrics.put("awareness_trend", awarenessTrend);

            double[] wellnessHistory = assessmentHistory.stream()
                .mapToDouble(MetaAssessment::getCognitiveWellnessScore)
                .toArray();

            double wellnessTrend = calculateTrend(wellnessHistory);
            metaMetrics.put("wellness_trend", wellnessTrend);
        }
    }

    /**
     * Calculate trend in a time series
     */
    private double calculateTrend(double[] values) {
        if (values.length < 2) return 0.0;

        int n = values.length;
        double sumX = n * (n - 1.0) / 2.0;
        double sumY = Arrays.stream(values).sum();
        double sumXY = 0.0;
        double sumX2 = n * (n - 1) * (2 * n - 1) / 6.0;

        for (int i = 0; i < n; i++) {
            sumXY += i * values[i];
        }

        return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
    }

    /**
     * Initialize meta-metrics
     */
    private void initializeMetaMetrics() {
        metaMetrics.put("current_awareness", 0.5);
        metaMetrics.put("current_coherence", 0.5);
        metaMetrics.put("current_stability", 0.5);
        metaMetrics.put("current_wandering", 0.1);
        metaMetrics.put("wellness_score", 0.5);
        metaMetrics.put("awareness_trend", 0.0);
        metaMetrics.put("wellness_trend", 0.0);
    }

    /**
     * Create initial assessment
     */
    private MetaAssessment createInitialAssessment() {
        return new MetaAssessment(0.5, 0.5, 0.5, 0.1, new HashMap<>(), new ArrayList<>());
    }

    // Public accessors

    /**
     * Get current meta-assessment
     */
    public MetaAssessment getCurrentAssessment() {
        return currentAssessment;
    }

    /**
     * Get current meta-metrics
     */
    public Map<String, Double> getMetaMetrics() {
        return new HashMap<>(metaMetrics);
    }

    /**
     * Get assessment history
     */
    public List<MetaAssessment> getAssessmentHistory() {
        return new ArrayList<>(assessmentHistory);
    }

    /**
     * Get cognitive wellness trend
     */
    public double getCognitiveWellnessTrend() {
        return metaMetrics.getOrDefault("wellness_trend", 0.0);
    }

    /**
     * Check if meta-awareness is stable
     */
    public boolean isMetaAwarenessStable() {
        return currentAssessment.getMetaAwarenessLevel() > 0.6 &&
               currentAssessment.getCognitiveCoherence() > 0.6;
    }

    /**
     * Get recommendations based on current meta-state
     */
    public List<String> getRecommendations() {
        List<String> recommendations = new ArrayList<>();

        if (currentAssessment.getMindWanderingIntensity() > 0.7) {
            recommendations.add("Consider mindfulness or attention training exercises");
        }

        if (currentAssessment.getAttentionalStability() < 0.4) {
            recommendations.add("Take a break to restore attention capacity");
        }

        if (currentAssessment.getCognitiveCoherence() < 0.4) {
            recommendations.add("Focus on single-task processing to improve coherence");
        }

        if (currentAssessment.getCognitiveWellnessScore() < 0.5) {
            recommendations.add("Overall cognitive state needs attention - consider rest or reduced complexity");
        }

        double wellnessTrend = metaMetrics.getOrDefault("wellness_trend", 0.0);
        if (wellnessTrend < -0.1) {
            recommendations.add("Cognitive wellness is declining - intervention recommended");
        }

        return recommendations;
    }
}
