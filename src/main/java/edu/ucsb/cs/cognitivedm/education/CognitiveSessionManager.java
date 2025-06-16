package edu.ucsb.cs.cognitivedm.education;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.education.CognitiveEducationFramework.CognitiveSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Cognitive Session Manager
 *
 * Manages cognitive learning sessions, tracking user progress, attention patterns,
 * and learning effectiveness across multiple mathematical topics. Integrates with
 * the attention-recognition framework to provide adaptive session management.
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class CognitiveSessionManager {

    public enum SessionState {
        STARTING,
        ACTIVE,
        PAUSED,
        COMPLETED,
        ABANDONED
    }

    public static class SessionMetrics {
        private final String sessionId;
        private final long startTime;
        private volatile long endTime;
        private final Map<String, Double> attentionHistory;
        private final Map<String, Double> cognitiveLoadHistory;
        private final Map<String, Integer> topicAttempts;
        private final Map<String, Double> topicScores;
        private volatile double averageAttention;
        private volatile double averageCognitiveLoad;
        private volatile int totalInteractions;

        public SessionMetrics(String sessionId) {
            this.sessionId = sessionId;
            this.startTime = System.currentTimeMillis();
            this.endTime = 0L;
            this.attentionHistory = new ConcurrentHashMap<>();
            this.cognitiveLoadHistory = new ConcurrentHashMap<>();
            this.topicAttempts = new ConcurrentHashMap<>();
            this.topicScores = new ConcurrentHashMap<>();
            this.averageAttention = 0.0;
            this.averageCognitiveLoad = 0.0;
            this.totalInteractions = 0;
        }

        public String getSessionId() { return sessionId; }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public long getDuration() { return endTime > 0 ? endTime - startTime : System.currentTimeMillis() - startTime; }
        public Map<String, Double> getAttentionHistory() { return new HashMap<>(attentionHistory); }
        public Map<String, Double> getCognitiveLoadHistory() { return new HashMap<>(cognitiveLoadHistory); }
        public Map<String, Integer> getTopicAttempts() { return new HashMap<>(topicAttempts); }
        public Map<String, Double> getTopicScores() { return new HashMap<>(topicScores); }
        public double getAverageAttention() { return averageAttention; }
        public double getAverageCognitiveLoad() { return averageCognitiveLoad; }
        public int getTotalInteractions() { return totalInteractions; }

        public void recordAttention(double attention) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            attentionHistory.put(timestamp, attention);
            updateAverageAttention();
        }

        public void recordCognitiveLoad(double cognitiveLoad) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            cognitiveLoadHistory.put(timestamp, cognitiveLoad);
            updateAverageCognitiveLoad();
        }

        public void recordTopicAttempt(String topic, double score) {
            topicAttempts.merge(topic, 1, Integer::sum);
            topicScores.put(topic, score);
            totalInteractions++;
        }

        public void endSession() {
            this.endTime = System.currentTimeMillis();
        }

        private void updateAverageAttention() {
            if (!attentionHistory.isEmpty()) {
                averageAttention = attentionHistory.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            }
        }

        private void updateAverageCognitiveLoad() {
            if (!cognitiveLoadHistory.isEmpty()) {
                averageCognitiveLoad = cognitiveLoadHistory.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            }
        }
    }

    public static class SessionConfiguration {
        private final long maxSessionDuration; // milliseconds
        private final double attentionThreshold;
        private final double cognitiveLoadThreshold;
        private final int maxConsecutiveFailures;
        private final boolean enableAdaptiveBreaks;
        private final boolean enableProgressTracking;

        public SessionConfiguration() {
            this(60 * 60 * 1000L, 0.3, 0.8, 3, true, true); // 1 hour default
        }

        public SessionConfiguration(long maxSessionDuration, double attentionThreshold,
                                  double cognitiveLoadThreshold, int maxConsecutiveFailures,
                                  boolean enableAdaptiveBreaks, boolean enableProgressTracking) {
            this.maxSessionDuration = maxSessionDuration;
            this.attentionThreshold = attentionThreshold;
            this.cognitiveLoadThreshold = cognitiveLoadThreshold;
            this.maxConsecutiveFailures = maxConsecutiveFailures;
            this.enableAdaptiveBreaks = enableAdaptiveBreaks;
            this.enableProgressTracking = enableProgressTracking;
        }

        public long getMaxSessionDuration() { return maxSessionDuration; }
        public double getAttentionThreshold() { return attentionThreshold; }
        public double getCognitiveLoadThreshold() { return cognitiveLoadThreshold; }
        public int getMaxConsecutiveFailures() { return maxConsecutiveFailures; }
        public boolean isEnableAdaptiveBreaks() { return enableAdaptiveBreaks; }
        public boolean isEnableProgressTracking() { return enableProgressTracking; }
    }

    // Manager instance variables
    private final Map<String, CognitiveSession> activeSessions;
    private final Map<String, SessionMetrics> sessionMetrics;
    private final Map<String, SessionState> sessionStates;
    private final SessionConfiguration defaultConfiguration;
    private final AtomicLong sessionIdCounter;
    private final AttentionRecognitionFramework attentionFramework;

    public CognitiveSessionManager() {
        this(new SessionConfiguration());
    }

    public CognitiveSessionManager(SessionConfiguration configuration) {
        this.activeSessions = new ConcurrentHashMap<>();
        this.sessionMetrics = new ConcurrentHashMap<>();
        this.sessionStates = new ConcurrentHashMap<>();
        this.defaultConfiguration = configuration;
        this.sessionIdCounter = new AtomicLong(0);
        this.attentionFramework = null; // Can be set later if needed
    }

    public CognitiveSessionManager(AttentionRecognitionFramework attentionFramework) {
        this(new SessionConfiguration());
    }

    public String createSession(String userId) {
        String sessionId = generateSessionId(userId);
        CognitiveSession session = new CognitiveSession(sessionId);
        SessionMetrics metrics = new SessionMetrics(sessionId);

        activeSessions.put(sessionId, session);
        sessionMetrics.put(sessionId, metrics);
        sessionStates.put(sessionId, SessionState.STARTING);

        return sessionId;
    }

    public boolean startSession(String sessionId) {
        if (!activeSessions.containsKey(sessionId)) {
            return false;
        }

        sessionStates.put(sessionId, SessionState.ACTIVE);
        return true;
    }

    public boolean pauseSession(String sessionId) {
        if (!activeSessions.containsKey(sessionId)) {
            return false;
        }

        SessionState currentState = sessionStates.get(sessionId);
        if (currentState == SessionState.ACTIVE) {
            sessionStates.put(sessionId, SessionState.PAUSED);
            return true;
        }
        return false;
    }

    public boolean resumeSession(String sessionId) {
        if (!activeSessions.containsKey(sessionId)) {
            return false;
        }

        SessionState currentState = sessionStates.get(sessionId);
        if (currentState == SessionState.PAUSED) {
            sessionStates.put(sessionId, SessionState.ACTIVE);
            return true;
        }
        return false;
    }

    public boolean endSession(String sessionId) {
        if (!activeSessions.containsKey(sessionId)) {
            return false;
        }

        sessionStates.put(sessionId, SessionState.COMPLETED);
        SessionMetrics metrics = sessionMetrics.get(sessionId);
        if (metrics != null) {
            metrics.endSession();
        }

        CognitiveSession session = activeSessions.get(sessionId);
        if (session != null) {
            session.setActive(false);
        }

        activeSessions.remove(sessionId);
        return true;
    }

    public void updateSessionState(String sessionId, double attention, double cognitiveLoad,
                                 String currentTopic, double topicScore, boolean success) {
        SessionMetrics metrics = sessionMetrics.get(sessionId);
        if (metrics == null) {
            return;
        }

        // Record cognitive state
        metrics.recordAttention(attention);
        metrics.recordCognitiveLoad(cognitiveLoad);

        if (currentTopic != null) {
            metrics.recordTopicAttempt(currentTopic, topicScore);
        }

        // Check for automatic session management
        if (defaultConfiguration.isEnableAdaptiveBreaks()) {
            checkForAdaptiveBreaks(sessionId, attention, cognitiveLoad);
        }

        // Check session duration limits
        checkSessionDuration(sessionId);
    }

    private void checkForAdaptiveBreaks(String sessionId, double attention, double cognitiveLoad) {
        if (attention < defaultConfiguration.getAttentionThreshold() ||
            cognitiveLoad > defaultConfiguration.getCognitiveLoadThreshold()) {

            // Could trigger a break recommendation
            CognitiveSession session = activeSessions.get(sessionId);
            if (session != null) {
                session.addData("break_recommended", true);
                session.addData("break_reason",
                    attention < defaultConfiguration.getAttentionThreshold() ? "low_attention" : "high_cognitive_load");
            }
        }
    }

    private void checkSessionDuration(String sessionId) {
        SessionMetrics metrics = sessionMetrics.get(sessionId);
        if (metrics != null && metrics.getDuration() > defaultConfiguration.getMaxSessionDuration()) {
            CognitiveSession session = activeSessions.get(sessionId);
            if (session != null) {
                session.addData("session_timeout", true);
            }
        }
    }

    public CognitiveSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public SessionMetrics getSessionMetrics(String sessionId) {
        return sessionMetrics.get(sessionId);
    }

    public SessionState getSessionState(String sessionId) {
        return sessionStates.getOrDefault(sessionId, SessionState.ABANDONED);
    }

    public List<String> getActiveSessionIds() {
        return new ArrayList<>(activeSessions.keySet());
    }

    public List<CognitiveSession> getActiveSessions() {
        return new ArrayList<>(activeSessions.values());
    }

    public Map<String, Object> generateSessionReport(String sessionId) {
        Map<String, Object> report = new HashMap<>();

        CognitiveSession session = activeSessions.get(sessionId);
        SessionMetrics metrics = sessionMetrics.get(sessionId);
        SessionState state = sessionStates.get(sessionId);

        if (session != null) {
            report.put("sessionId", sessionId);
            report.put("sessionData", session.getSessionData());
            report.put("isActive", session.isActive());
        }

        if (metrics != null) {
            report.put("startTime", metrics.getStartTime());
            report.put("duration", metrics.getDuration());
            report.put("averageAttention", metrics.getAverageAttention());
            report.put("averageCognitiveLoad", metrics.getAverageCognitiveLoad());
            report.put("totalInteractions", metrics.getTotalInteractions());
            report.put("topicAttempts", metrics.getTopicAttempts());
            report.put("topicScores", metrics.getTopicScores());
        }

        if (state != null) {
            report.put("sessionState", state.toString());
        }

        return report;
    }

    public Map<String, Object> generateOverallReport() {
        Map<String, Object> report = new HashMap<>();

        report.put("totalActiveSessions", activeSessions.size());
        report.put("totalTrackedSessions", sessionMetrics.size());

        // Calculate aggregate statistics
        double avgSessionDuration = sessionMetrics.values().stream()
            .mapToLong(SessionMetrics::getDuration)
            .average()
            .orElse(0.0);

        double avgAttentionAcrossSessions = sessionMetrics.values().stream()
            .mapToDouble(SessionMetrics::getAverageAttention)
            .average()
            .orElse(0.0);

        double avgCognitiveLoadAcrossSessions = sessionMetrics.values().stream()
            .mapToDouble(SessionMetrics::getAverageCognitiveLoad)
            .average()
            .orElse(0.0);

        report.put("averageSessionDuration", avgSessionDuration);
        report.put("averageAttentionAcrossSessions", avgAttentionAcrossSessions);
        report.put("averageCognitiveLoadAcrossSessions", avgCognitiveLoadAcrossSessions);

        // Session state distribution
        Map<SessionState, Long> stateDistribution = sessionStates.values().stream()
            .collect(java.util.stream.Collectors.groupingBy(
                state -> state,
                java.util.stream.Collectors.counting()
            ));
        report.put("sessionStateDistribution", stateDistribution);

        return report;
    }

    public void cleanupInactiveSessions() {
        List<String> toRemove = new ArrayList<>();

        for (Map.Entry<String, CognitiveSession> entry : activeSessions.entrySet()) {
            CognitiveSession session = entry.getValue();
            if (!session.isActive()) {
                toRemove.add(entry.getKey());
            }
        }

        for (String sessionId : toRemove) {
            activeSessions.remove(sessionId);
            sessionStates.put(sessionId, SessionState.ABANDONED);
        }
    }

    private String generateSessionId(String userId) {
        long counter = sessionIdCounter.incrementAndGet();
        return String.format("session_%s_%d_%d", userId, System.currentTimeMillis(), counter);
    }

    public SessionConfiguration getDefaultConfiguration() {
        return defaultConfiguration;
    }

    public int getActiveSessionCount() {
        return activeSessions.size();
    }

    public int getTotalSessionCount() {
        return sessionMetrics.size();
    }
}
