package edu.ucsb.cs.cognitivedm.framework;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CognitiveFrameworkInterfaces {
    
    public interface CognitiveSessionManager {
        CognitiveSession getOrCreateSession(String userId);
        int getActiveSessionCount();
        void setDefaultAttentionThreshold(double threshold);
        void setDefaultMonitoringInterval(int interval);
    }

    public interface CognitiveSession {
        void updateCognitiveState(CognitiveState state);
        void setSessionType(Object sessionType);
        void startSession();
        void setCognitiveMonitoringInterval(int interval);
        CognitiveState getCurrentCognitiveState();
    }

    public interface CognitiveRecommendationEngine {
        void updateLearnerProfile(String userId, Object learnerProfile);
        void addLearningContent(String id, Object content);
        void shutdown();
    }

    public interface CognitiveEducationFramework {
        Object registerLearner(String userId);
        void initializeSampleContent();
        void addLearningContent(Object content);
    }

    public interface LearningContent {
        String getContent();
        String getLearnerId();
        String getSubject();
        CognitiveState getInitialCognitiveState();
    }

    public interface RecommendationRequest {
        // Placeholder for recommendation request methods
    }

    public interface LearningPath {
        // Placeholder for learning path methods
    }

    public static class CognitiveState {
        private final double attentionLevel;
        private final double recognitionLevel;
        private final double wanderingLevel;

        public CognitiveState(double attentionLevel, double recognitionLevel, double wanderingLevel) {
            this.attentionLevel = attentionLevel;
            this.recognitionLevel = recognitionLevel;
            this.wanderingLevel = wanderingLevel;
        }

        // Getters
        public double getAttentionLevel() { return attentionLevel; }
        public double getRecognitionLevel() { return recognitionLevel; }
        public double getWanderingLevel() { return wanderingLevel; }
    }

    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

    public enum ContentType {
        CONCEPT, PROCEDURE, PROBLEM_SOLVING
    }

    public enum LearningStyle {
        ANALYTICAL, VISUAL, KINESTHETIC
    }
} 