package edu.ucsb.cs.cognitivedm.framework;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CognitiveFrameworkInterfaces {
    
    public interface CognitiveState {
        double getAttention();
        double getRecognition();
        double getWandering();
        double getCognitiveLoad();
        boolean isInFlowState();
        Map<String, Double> getMetrics();
    }

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
        void pauseSession();
        void resumeSession();
        void endSession();
        CognitiveState getCurrentCognitiveState();
        String getUserId();
        long getSessionStartTime();
        boolean isActive();
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
        Object getContext();
    }

    public interface LearningPath {
        // Placeholder for learning path methods
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