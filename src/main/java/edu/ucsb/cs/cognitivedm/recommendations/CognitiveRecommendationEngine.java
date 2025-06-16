package edu.ucsb.cs.cognitivedm.recommendations;

import edu.ucsb.cs.cognitivedm.education.CognitiveEducationFramework;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Cognitive Recommendation Engine
 *
 * Provides intelligent recommendations for mathematical learning and problem-solving
 * based on cognitive state, attention patterns, and learning analytics. Integrates
 * with the attention-recognition framework to deliver personalized educational content.
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class CognitiveRecommendationEngine {

    public enum RecommendationType {
        TOPIC_SUGGESTION,
        DIFFICULTY_ADJUSTMENT,
        LEARNING_STRATEGY,
        BREAK_RECOMMENDATION,
        REVIEW_CONTENT,
        PRACTICE_PROBLEM,
        CONCEPTUAL_BRIDGE,
    }

    public static class Recommendation {

        private final String id;
        private final RecommendationType type;
        private final String title;
        private final String description;
        private final double confidence;
        private final Map<String, Object> parameters;
        private final long timestamp;
        private final String reasoning;

        public Recommendation(
            String id,
            RecommendationType type,
            String title,
            String description,
            double confidence,
            String reasoning
        ) {
            this.id = id;
            this.type = type;
            this.title = title;
            this.description = description;
            this.confidence = Math.max(0.0, Math.min(1.0, confidence));
            this.parameters = new HashMap<>();
            this.timestamp = System.currentTimeMillis();
            this.reasoning = reasoning;
        }

        public String getId() {
            return id;
        }

        public RecommendationType getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public double getConfidence() {
            return confidence;
        }

        public Map<String, Object> getParameters() {
            return new HashMap<>(parameters);
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getReasoning() {
            return reasoning;
        }

        public void addParameter(String key, Object value) {
            parameters.put(key, value);
        }

        @Override
        public String toString() {
            return String.format(
                "Recommendation{type=%s, title='%s', confidence=%.2f}",
                type,
                title,
                confidence
            );
        }
    }

    public static class RecommendationContext {

        private final String userId;
        private final String sessionId;
        private final double attention;
        private final double cognitiveLoad;
        private final double wandering;
        private final Map<String, Double> topicMastery;
        private final List<String> recentTopics;
        private final long sessionDuration;

        public RecommendationContext(
            String userId,
            String sessionId,
            double attention,
            double cognitiveLoad,
            double wandering
        ) {
            this.userId = userId;
            this.sessionId = sessionId;
            this.attention = attention;
            this.cognitiveLoad = cognitiveLoad;
            this.wandering = wandering;
            this.topicMastery = new HashMap<>();
            this.recentTopics = new ArrayList<>();
            this.sessionDuration = 0L;
        }

        public String getUserId() {
            return userId;
        }

        public String getSessionId() {
            return sessionId;
        }

        public double getAttention() {
            return attention;
        }

        public double getCognitiveLoad() {
            return cognitiveLoad;
        }

        public double getWandering() {
            return wandering;
        }

        public Map<String, Double> getTopicMastery() {
            return new HashMap<>(topicMastery);
        }

        public List<String> getRecentTopics() {
            return new ArrayList<>(recentTopics);
        }

        public long getSessionDuration() {
            return sessionDuration;
        }

        public void addTopicMastery(String topic, double mastery) {
            topicMastery.put(topic, mastery);
        }

        public void addRecentTopic(String topic) {
            recentTopics.add(topic);
            if (recentTopics.size() > 10) {
                recentTopics.remove(0);
            }
        }
    }

    public static class RecommendationStrategy {

        private final String name;
        private final double weightAttention;
        private final double weightCognitiveLoad;
        private final double weightMastery;
        private final double weightRecency;

        public RecommendationStrategy(
            String name,
            double weightAttention,
            double weightCognitiveLoad,
            double weightMastery,
            double weightRecency
        ) {
            this.name = name;
            this.weightAttention = weightAttention;
            this.weightCognitiveLoad = weightCognitiveLoad;
            this.weightMastery = weightMastery;
            this.weightRecency = weightRecency;
        }

        public String getName() {
            return name;
        }

        public double getWeightAttention() {
            return weightAttention;
        }

        public double getWeightCognitiveLoad() {
            return weightCognitiveLoad;
        }

        public double getWeightMastery() {
            return weightMastery;
        }

        public double getWeightRecency() {
            return weightRecency;
        }
    }

    public static class RecommendationAnalytics {

        private final Map<String, Integer> recommendationCounts;
        private final Map<String, Double> successRates;
        private final Map<String, Long> totalTimeSpent;

        public RecommendationAnalytics() {
            this.recommendationCounts = new ConcurrentHashMap<>();
            this.successRates = new ConcurrentHashMap<>();
            this.totalTimeSpent = new ConcurrentHashMap<>();
        }

        public void recordRecommendation(String type) {
            recommendationCounts.merge(type, 1, Integer::sum);
        }

        public void recordSuccess(String type, boolean success) {
            double currentRate = successRates.getOrDefault(type, 0.0);
            int count = recommendationCounts.getOrDefault(type, 1);
            double newRate =
                (currentRate * (count - 1) + (success ? 1.0 : 0.0)) / count;
            successRates.put(type, newRate);
        }

        public void recordTimeSpent(String type, long milliseconds) {
            totalTimeSpent.merge(type, milliseconds, Long::sum);
        }

        public Map<String, Integer> getRecommendationCounts() {
            return new HashMap<>(recommendationCounts);
        }

        public Map<String, Double> getSuccessRates() {
            return new HashMap<>(successRates);
        }

        public Map<String, Long> getTotalTimeSpent() {
            return new HashMap<>(totalTimeSpent);
        }
    }

    // Engine instance variables
    private final AttentionRecognitionFramework attentionFramework;
    private final CognitiveEducationFramework educationFramework;
    private final Map<String, RecommendationStrategy> strategies;
    private final RecommendationAnalytics analytics;
    private final Map<String, List<Recommendation>> userRecommendationHistory;

    public CognitiveRecommendationEngine(
        AttentionRecognitionFramework attentionFramework,
        CognitiveEducationFramework educationFramework
    ) {
        this.attentionFramework = attentionFramework;
        this.educationFramework = educationFramework;
        this.strategies = new ConcurrentHashMap<>();
        this.analytics = new RecommendationAnalytics();
        this.userRecommendationHistory = new ConcurrentHashMap<>();
        initializeDefaultStrategies();
    }

    private void initializeDefaultStrategies() {
        strategies.put(
            "adaptive",
            new RecommendationStrategy("adaptive", 0.3, 0.4, 0.2, 0.1)
        );
        strategies.put(
            "mastery_focused",
            new RecommendationStrategy("mastery_focused", 0.1, 0.2, 0.6, 0.1)
        );
        strategies.put(
            "attention_based",
            new RecommendationStrategy("attention_based", 0.6, 0.3, 0.1, 0.0)
        );
        strategies.put(
            "balanced",
            new RecommendationStrategy("balanced", 0.25, 0.25, 0.25, 0.25)
        );
    }

    public List<Recommendation> generateRecommendations(
        RecommendationContext context,
        int maxRecommendations
    ) {
        return generateRecommendations(context, maxRecommendations, "adaptive");
    }

    public List<Recommendation> generateRecommendations(
        RecommendationContext context,
        int maxRecommendations,
        String strategyName
    ) {
        List<Recommendation> recommendations = new ArrayList<>();
        RecommendationStrategy strategy = strategies.getOrDefault(
            strategyName,
            strategies.get("adaptive")
        );

        // Generate different types of recommendations
        recommendations.addAll(generateTopicRecommendations(context, strategy));
        recommendations.addAll(
            generateDifficultyRecommendations(context, strategy)
        );
        recommendations.addAll(
            generateStrategyRecommendations(context, strategy)
        );
        recommendations.addAll(generateBreakRecommendations(context, strategy));
        recommendations.addAll(
            generateReviewRecommendations(context, strategy)
        );

        // Sort by confidence and return top recommendations
        return recommendations
            .stream()
            .sorted((r1, r2) ->
                Double.compare(r2.getConfidence(), r1.getConfidence())
            )
            .limit(maxRecommendations)
            .collect(Collectors.toList());
    }

    /**
     * Generate recommendations based on a RecommendationRequest
     */
    public List<Recommendation> generateRecommendations(RecommendationRequest request) {
        // Convert RecommendationRequest to RecommendationContext
        RecommendationContext context = convertRequestToContext(request);
        return generateRecommendations(context, 10, "adaptive");
    }
    
    /**
     * Convert RecommendationRequest to RecommendationContext
     */
    private RecommendationContext convertRequestToContext(RecommendationRequest request) {
        RecommendationContext context = new RecommendationContext(
            request.getUserId(),
            request.getId(), // use request ID as session ID
            request.getCognitiveState().getAttention(),
            request.getCognitiveState().getCognitiveLoad(),
            request.getCognitiveState().getWandering()
        );
        
        // Add context from request
        context.addRecentTopic(request.getTopic());
        
        return context;
    }

    private List<Recommendation> generateTopicRecommendations(
        RecommendationContext context,
        RecommendationStrategy strategy
    ) {
        List<Recommendation> recommendations = new ArrayList<>();

        // Get recommended topics from education framework
        List<String> suggestedTopics = educationFramework.getRecommendedTopics(
            context.getSessionId(),
            CognitiveEducationFramework.ContentType.SET_THEORY,
            context.getAttention(),
            context.getCognitiveLoad()
        );

        for (String topic : suggestedTopics) {
            if (!context.getRecentTopics().contains(topic)) {
                double readiness = educationFramework.assessTopicReadiness(
                    topic,
                    context.getSessionId()
                );
                double confidence = calculateConfidence(
                    context,
                    strategy,
                    readiness
                );

                Recommendation rec = new Recommendation(
                    UUID.randomUUID().toString(),
                    RecommendationType.TOPIC_SUGGESTION,
                    "Study " + topic,
                    "Based on your current progress and cognitive state, studying " +
                    topic +
                    " would be beneficial.",
                    confidence,
                    "Topic readiness: " +
                    String.format("%.2f", readiness) +
                    ", attention: " +
                    String.format("%.2f", context.getAttention())
                );
                rec.addParameter("topic", topic);
                rec.addParameter("readiness", readiness);
                recommendations.add(rec);
            }
        }

        return recommendations;
    }

    private List<Recommendation> generateDifficultyRecommendations(
        RecommendationContext context,
        RecommendationStrategy strategy
    ) {
        List<Recommendation> recommendations = new ArrayList<>();

        if (context.getCognitiveLoad() > 0.8) {
            Recommendation rec = new Recommendation(
                UUID.randomUUID().toString(),
                RecommendationType.DIFFICULTY_ADJUSTMENT,
                "Reduce Difficulty",
                "Your cognitive load is high. Consider switching to easier problems or reviewing fundamentals.",
                0.8,
                "High cognitive load detected: " +
                String.format("%.2f", context.getCognitiveLoad())
            );
            rec.addParameter("adjustment", "decrease");
            recommendations.add(rec);
        } else if (
            context.getAttention() > 0.8 && context.getCognitiveLoad() < 0.3
        ) {
            Recommendation rec = new Recommendation(
                UUID.randomUUID().toString(),
                RecommendationType.DIFFICULTY_ADJUSTMENT,
                "Increase Challenge",
                "You're highly focused with low cognitive load. Try more challenging problems!",
                0.7,
                "High attention with low cognitive load - ready for challenge"
            );
            rec.addParameter("adjustment", "increase");
            recommendations.add(rec);
        }

        return recommendations;
    }

    private List<Recommendation> generateStrategyRecommendations(
        RecommendationContext context,
        RecommendationStrategy strategy
    ) {
        List<Recommendation> recommendations = new ArrayList<>();

        if (context.getWandering() > 0.6) {
            Recommendation rec = new Recommendation(
                UUID.randomUUID().toString(),
                RecommendationType.LEARNING_STRATEGY,
                "Active Learning Techniques",
                "Your mind is wandering. Try active techniques like teaching concepts aloud or drawing diagrams.",
                0.6,
                "High mind-wandering detected: " +
                String.format("%.2f", context.getWandering())
            );
            rec.addParameter(
                "techniques",
                Arrays.asList(
                    "verbal_explanation",
                    "visual_diagrams",
                    "hands_on_practice"
                )
            );
            recommendations.add(rec);
        }

        return recommendations;
    }

    private List<Recommendation> generateBreakRecommendations(
        RecommendationContext context,
        RecommendationStrategy strategy
    ) {
        List<Recommendation> recommendations = new ArrayList<>();

        if (
            context.getSessionDuration() > 45 * 60 * 1000 || // 45 minutes
            context.getCognitiveLoad() > 0.9
        ) {
            Recommendation rec = new Recommendation(
                UUID.randomUUID().toString(),
                RecommendationType.BREAK_RECOMMENDATION,
                "Take a Break",
                "You've been studying for a while or your cognitive load is very high. A short break would help.",
                0.9,
                "Session duration or cognitive load threshold exceeded"
            );
            rec.addParameter("break_duration", 10); // minutes
            rec.addParameter("break_type", "active_rest");
            recommendations.add(rec);
        }

        return recommendations;
    }

    private List<Recommendation> generateReviewRecommendations(
        RecommendationContext context,
        RecommendationStrategy strategy
    ) {
        List<Recommendation> recommendations = new ArrayList<>();

        // Find topics with low mastery that were studied recently
        for (Map.Entry<String, Double> entry : context
            .getTopicMastery()
            .entrySet()) {
            if (
                entry.getValue() < 0.7 &&
                context.getRecentTopics().contains(entry.getKey())
            ) {
                Recommendation rec = new Recommendation(
                    UUID.randomUUID().toString(),
                    RecommendationType.REVIEW_CONTENT,
                    "Review " + entry.getKey(),
                    "Your mastery of " +
                    entry.getKey() +
                    " could be improved. Consider reviewing this topic.",
                    0.5 + (0.7 - entry.getValue()),
                    "Low mastery level: " +
                    String.format("%.2f", entry.getValue())
                );
                rec.addParameter("topic", entry.getKey());
                rec.addParameter("mastery", entry.getValue());
                recommendations.add(rec);
            }
        }

        return recommendations;
    }

    private double calculateConfidence(
        RecommendationContext context,
        RecommendationStrategy strategy,
        double topicReadiness
    ) {
        double attentionScore =
            context.getAttention() * strategy.getWeightAttention();
        double cognitiveLoadScore =
            (1.0 - context.getCognitiveLoad()) *
            strategy.getWeightCognitiveLoad();
        double masteryScore = topicReadiness * strategy.getWeightMastery();
        double recencyScore = 1.0 * strategy.getWeightRecency(); // Simplified recency calculation

        return Math.max(
            0.0,
            Math.min(
                1.0,
                attentionScore +
                cognitiveLoadScore +
                masteryScore +
                recencyScore
            )
        );
    }

    public void recordRecommendationFeedback(
        String recommendationId,
        boolean accepted,
        boolean successful
    ) {
        // Find the recommendation and update analytics
        for (List<
            Recommendation
        > userRecs : userRecommendationHistory.values()) {
            for (Recommendation rec : userRecs) {
                if (rec.getId().equals(recommendationId)) {
                    analytics.recordRecommendation(rec.getType().toString());
                    if (accepted) {
                        analytics.recordSuccess(
                            rec.getType().toString(),
                            successful
                        );
                    }
                    break;
                }
            }
        }
    }

    public void addRecommendationStrategy(
        String name,
        RecommendationStrategy strategy
    ) {
        strategies.put(name, strategy);
    }

    public RecommendationStrategy getStrategy(String name) {
        return strategies.get(name);
    }

    public Collection<String> getAvailableStrategies() {
        return strategies.keySet();
    }

    public RecommendationAnalytics getAnalytics() {
        return analytics;
    }

    public void storeRecommendationHistory(
        String userId,
        List<Recommendation> recommendations
    ) {
        userRecommendationHistory
            .computeIfAbsent(userId, k -> new ArrayList<>())
            .addAll(recommendations);
    }

    public List<Recommendation> getRecommendationHistory(String userId) {
        return userRecommendationHistory.getOrDefault(
            userId,
            new ArrayList<>()
        );
    }

    public Map<String, Object> generateRecommendationReport(String userId) {
        Map<String, Object> report = new HashMap<>();
        List<Recommendation> history = getRecommendationHistory(userId);

        report.put("userId", userId);
        report.put("totalRecommendations", history.size());
        report.put(
            "recommendationsByType",
            history
                .stream()
                .collect(
                    Collectors.groupingBy(
                        r -> r.getType().toString(),
                        Collectors.counting()
                    )
                )
        );
        report.put(
            "averageConfidence",
            history
                .stream()
                .mapToDouble(Recommendation::getConfidence)
                .average()
                .orElse(0.0)
        );
        report.put("analytics", analytics);

        return report;
    }

    /**
     * Recommendation statistics and performance metrics
     */
    public static class RecommendationStatistics {

        private final int totalRecommendations;
        private final int totalUsers;
        private final double averageConfidence;
        private final Map<String, Double> successRates;
        private final Map<String, Integer> typeDistribution;
        private final double systemEfficiency;
        private final long timestamp;

        public RecommendationStatistics(
            int totalRecommendations,
            int totalUsers,
            double averageConfidence,
            Map<String, Double> successRates,
            Map<String, Integer> typeDistribution,
            double systemEfficiency
        ) {
            this.totalRecommendations = totalRecommendations;
            this.totalUsers = totalUsers;
            this.averageConfidence = averageConfidence;
            this.successRates = new HashMap<>(successRates);
            this.typeDistribution = new HashMap<>(typeDistribution);
            this.systemEfficiency = systemEfficiency;
            this.timestamp = System.currentTimeMillis();
        }

        public int getTotalRecommendations() {
            return totalRecommendations;
        }

        public int getTotalUsers() {
            return totalUsers;
        }

        public double getAverageConfidence() {
            return averageConfidence;
        }

        public Map<String, Double> getSuccessRates() {
            return new HashMap<>(successRates);
        }

        public Map<String, Integer> getTypeDistribution() {
            return new HashMap<>(typeDistribution);
        }

        public double getSystemEfficiency() {
            return systemEfficiency;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return String.format(
                "RecommendationStatistics{recommendations=%d, users=%d, avgConfidence=%.2f, efficiency=%.2f}",
                totalRecommendations,
                totalUsers,
                averageConfidence,
                systemEfficiency
            );
        }
    }

    /**
     * Generate current recommendation statistics
     */
    public RecommendationStatistics getStatistics() {
        int totalRecs = userRecommendationHistory
            .values()
            .stream()
            .mapToInt(List::size)
            .sum();

        double avgConfidence = userRecommendationHistory
            .values()
            .stream()
            .flatMap(List::stream)
            .mapToDouble(Recommendation::getConfidence)
            .average()
            .orElse(0.0);

        Map<String, Integer> typeDistribution = userRecommendationHistory
            .values()
            .stream()
            .flatMap(List::stream)
            .collect(
                Collectors.groupingBy(
                    r -> r.getType().toString(),
                    Collectors.collectingAndThen(
                        Collectors.counting(),
                        Math::toIntExact
                    )
                )
            );

        double systemEfficiency = analytics
            .getSuccessRates()
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        return new RecommendationStatistics(
            totalRecs,
            userRecommendationHistory.size(),
            avgConfidence,
            analytics.getSuccessRates(),
            typeDistribution,
            systemEfficiency
        );
    }

    /**
     * Update learner profile
     */
    public void updateLearnerProfile(String userId, edu.ucsb.cs.cognitivedm.education.CognitiveEducationFramework.LearnerProfile profile) {
        // Store or update the learner profile
        System.out.println("Updating learner profile for user: " + userId);
        // Implementation would store profile data
    }
    
    /**
     * Add learning content to the recommendation engine
     */
    public void addLearningContent(String contentId, edu.ucsb.cs.cognitivedm.framework.CognitiveFrameworkTypes.LearningContent content) {
        // Add content to the recommendation engine's knowledge base
        System.out.println("Adding learning content: " + contentId);
        // Implementation would index content for recommendations
    }
    
    /**
     * Shutdown the recommendation engine
     */
    public void shutdown() {
        System.out.println("Shutting down CognitiveRecommendationEngine");
        // Clean up resources, close connections, etc.
        strategies.clear();
    }
}
