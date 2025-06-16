package edu.ucsb.cs.cognitivedm.framework;

import java.util.Map;

/**
 * Comprehensive type definitions for the Cognitive Framework.
 * Centralizes enums and interfaces used across the library.
 */
public final class CognitiveFrameworkTypes {

    // Prevent instantiation
    private CognitiveFrameworkTypes() {
        throw new AssertionError("CognitiveFrameworkTypes cannot be instantiated");
    }

    /**
     * Represents the type of learning content.
     */
    public enum ContentType {
        CONCEPT,       // Theoretical understanding
        PROCEDURE,     // Step-by-step processes
        PROBLEM_SOLVING, // Application and reasoning
        EXAMPLE,       // Illustrative cases
        INTERACTIVE    // Hands-on learning
    }

    /**
     * Represents the difficulty level of learning content.
     */
    public enum DifficultyLevel {
        BEGINNER,      // Introductory level
        INTERMEDIATE,  // Moderate complexity
        ADVANCED,      // High complexity
        EXPERT         // Specialized knowledge
    }

    /**
     * Represents different learning styles.
     */
    public enum LearningStyle {
        VISUAL,        // Learns through seeing
        AUDITORY,      // Learns through hearing
        KINESTHETIC,   // Learns through doing
        ANALYTICAL,    // Learns through logical reasoning
        GLOBAL         // Learns through big-picture understanding
    }

    /**
     * Represents recommendation types for cognitive learning.
     */
    public enum RecommendationType {
        CONCEPT_REVIEW,       // Review of fundamental concepts
        PRACTICE_EXERCISE,    // Practice to reinforce learning
        CHALLENGE_PROBLEM,    // Challenge to extend understanding
        ALTERNATIVE_APPROACH, // Alternative approach to existing knowledge
        PREREQUISITE_MATERIAL // Material needed before proceeding
    }

    /**
     * Represents a pattern for pattern detection.
     */
    public static class Pattern {
        private final String name;
        private final String description;
        private final int patternType;
        private final double threshold;
        private final long timestamp;

        public Pattern(String name, String description, int patternType, double threshold) {
            this.name = name;
            this.description = description;
            this.patternType = patternType;
            this.threshold = threshold;
            this.timestamp = System.currentTimeMillis();
        }

        public Pattern(String name, String description, int patternType, double threshold, long timestamp) {
            this.name = name;
            this.description = description;
            this.patternType = patternType;
            this.threshold = threshold;
            this.timestamp = timestamp;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getPatternType() { return patternType; }
        public double getThreshold() { return threshold; }
        public long getTimestamp() { return timestamp; }
    }

    /**
     * Represents a learning content item.
     */
    public static class LearningContent {
        private final String id;
        private final String title;
        private final String domain;
        private final ContentType type;
        private final DifficultyLevel difficulty;
        private final Map<LearningStyle, Double> styleAlignments;
        private Map<String, Object> contentDetails;

        public LearningContent(
            String id, 
            String title, 
            String domain, 
            ContentType type, 
            DifficultyLevel difficulty
        ) {
            this.id = id;
            this.title = title;
            this.domain = domain;
            this.type = type;
            this.difficulty = difficulty;
            this.styleAlignments = new java.util.HashMap<>();
        }

        // Getters and setters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDomain() { return domain; }
        public ContentType getType() { return type; }
        public DifficultyLevel getDifficulty() { return difficulty; }

        public void setStyleAlignment(LearningStyle style, double alignment) {
            styleAlignments.put(style, alignment);
        }

        public void setContent(String key, Object value) {
            if (contentDetails == null) {
                contentDetails = new java.util.HashMap<>();
            }
            contentDetails.put(key, value);
        }
    }

    /**
     * Represents a learning path for adaptive learning.
     */
    public static class LearningPath {
        private final String userId;
        private final java.util.List<LearningContent> contents;
        private final DifficultyLevel targetLevel;

        public LearningPath(String userId, DifficultyLevel targetLevel) {
            this.userId = userId;
            this.targetLevel = targetLevel;
            this.contents = new java.util.ArrayList<>();
        }

        public void addContent(LearningContent content) {
            contents.add(content);
        }

        public String getUserId() { return userId; }
        public java.util.List<LearningContent> getContents() { return contents; }
        public DifficultyLevel getTargetLevel() { return targetLevel; }
    }

    /**
     * Represents a recommendation for learning.
     */
    public static class Recommendation {
        private final LearningContent content;
        private final double relevanceScore;
        private final RecommendationType type;
        private final String reason;

        public Recommendation(LearningContent content, double relevanceScore) {
            this(content, relevanceScore, RecommendationType.CONCEPT_REVIEW, null);
        }

        public Recommendation(
            LearningContent content, 
            double relevanceScore, 
            RecommendationType type,
            String reason
        ) {
            this.content = content;
            this.relevanceScore = relevanceScore;
            this.type = type;
            this.reason = reason;
        }

        public LearningContent getContent() { return content; }
        public double getRelevanceScore() { return relevanceScore; }
        public RecommendationType getType() { return type; }
        public String getReason() { return reason; }
    }

    /**
     * Represents a request for learning recommendations.
     */
    public static class RecommendationRequest implements CognitiveFrameworkInterfaces.RecommendationRequest {
        private final String userId;
        private final CognitiveFrameworkInterfaces.CognitiveState cognitiveState;
        private final RecommendationType preferredType;
        private final Map<String, Object> context;
        
        public RecommendationRequest(String userId, CognitiveFrameworkInterfaces.CognitiveState cognitiveState) {
            this(userId, cognitiveState, null, new java.util.HashMap<>());
        }
        
        public RecommendationRequest(
            String userId, 
            CognitiveFrameworkInterfaces.CognitiveState cognitiveState,
            RecommendationType preferredType,
            Map<String, Object> context
        ) {
            this.userId = userId;
            this.cognitiveState = cognitiveState;
            this.preferredType = preferredType;
            this.context = new java.util.HashMap<>(context);
        }
        
        public String getUserId() { return userId; }
        public CognitiveFrameworkInterfaces.CognitiveState getCognitiveState() { return cognitiveState; }
        public RecommendationType getPreferredType() { return preferredType; }
        
        @Override
        public Object getContext() {
            return context;
        }
        
        public void addContextItem(String key, Object value) {
            context.put(key, value);
        }
    }
} 