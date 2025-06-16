package edu.ucsb.cs.cognitivedm.sessions;

import edu.ucsb.cs.cognitivedm.education.CognitiveEducationFramework;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * Cognitive Session Manager
 *
 * Manages cognitive learning sessions with attention-recognition decoupling,
 * implementing the recursive multi-scale integration approach for tracking
 * learner cognitive states and optimizing educational experiences.
 *
 * This class embodies the fractal principle z = z² + c where:
 * - z represents the current session state
 * - z² represents recursive elaboration of learning patterns
 * - c represents new learning inputs that shift cognitive trajectory
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class CognitiveSessionManager {

    private final AttentionRecognitionFramework cognitiveFramework;
    private final Map<String, CognitiveSession> activeSessions;
    private final Map<String, List<SessionMetrics>> sessionHistory;
    private final ExecutorService sessionExecutor;
    private final ScheduledExecutorService monitoringService;

    // Session configuration
    private final int maxConcurrentSessions;
    private final Duration defaultSessionTimeout;
    private final double cognitiveLoadThreshold;

    /**
     * Represents an active cognitive learning session
     */
    public static class CognitiveSession {

        private final String sessionId;
        private final String learnerId;
        private final LocalDateTime startTime;
        private final Map<String, Object> sessionContext;
        private AttentionRecognitionFramework.CognitiveState currentCognitiveState;
        private SessionType sessionType;
        private SessionStatus status;
        private int cognitiveMonitoringInterval;
        private List<CognitiveEvent> events;

        public CognitiveSession(String sessionId, String learnerId) {
            this.sessionId = sessionId;
            this.learnerId = learnerId;
            this.startTime = LocalDateTime.now();
            this.sessionContext = new ConcurrentHashMap<>();
            this.status = SessionStatus.INITIALIZING;
            this.cognitiveMonitoringInterval = 5000; // 5 seconds default
            this.events = new ArrayList<>();
        }

        // Getters and setters
        public String getSessionId() {
            return sessionId;
        }

        public String getLearnerId() {
            return learnerId;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public SessionStatus getStatus() {
            return status;
        }

        public void setStatus(SessionStatus status) {
            this.status = status;
        }

        public AttentionRecognitionFramework.CognitiveState getCurrentCognitiveState() {
            return currentCognitiveState;
        }

        public void updateCognitiveState(
            AttentionRecognitionFramework.CognitiveState state
        ) {
            this.currentCognitiveState = state;
            this.events.add(
                    new CognitiveEvent(
                        LocalDateTime.now(),
                        "cognitive_state_update",
                        state
                    )
                );
        }

        public void setSessionType(Object sessionType) {
            this.sessionType = (SessionType) sessionType;
        }

        public void startSession() {
            this.status = SessionStatus.ACTIVE;
            this.events.add(
                    new CognitiveEvent(
                        LocalDateTime.now(),
                        "session_started",
                        null
                    )
                );
        }

        public void setCognitiveMonitoringInterval(int intervalMs) {
            this.cognitiveMonitoringInterval = intervalMs;
        }

        public int getCognitiveMonitoringInterval() {
            return cognitiveMonitoringInterval;
        }

        public List<CognitiveEvent> getEvents() {
            return new ArrayList<>(events);
        }

        public Map<String, Object> getContext() {
            return new HashMap<>(sessionContext);
        }

        public void setContextValue(String key, Object value) {
            sessionContext.put(key, value);
        }
    }

    /**
     * Represents different types of learning sessions
     */
    public enum SessionType {
        EXPLORATORY, // Free exploration of concepts
        GUIDED_PRACTICE, // Structured practice with guidance
        ASSESSMENT, // Evaluation and testing
        CREATIVE_MODE, // Open-ended problem solving
        REVIEW_SESSION, // Reinforcement of previous learning
    }

    /**
     * Session status enumeration
     */
    public enum SessionStatus {
        INITIALIZING,
        ACTIVE,
        PAUSED,
        COMPLETED,
        TERMINATED,
        ERROR,
    }

    /**
     * Represents a cognitive event within a session
     */
    public static class CognitiveEvent {

        private final LocalDateTime timestamp;
        private final String eventType;
        private final Object eventData;

        public CognitiveEvent(
            LocalDateTime timestamp,
            String eventType,
            Object eventData
        ) {
            this.timestamp = timestamp;
            this.eventType = eventType;
            this.eventData = eventData;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getEventType() {
            return eventType;
        }

        public Object getEventData() {
            return eventData;
        }
    }

    /**
     * Session performance metrics
     */
    public static class SessionMetrics {

        private final String sessionId;
        private final Duration sessionDuration;
        private final double averageCognitiveLoad;
        private final double attentionStability;
        private final int mindWanderingEpisodes;
        private final double learningEfficiency;

        public SessionMetrics(
            String sessionId,
            Duration duration,
            double avgLoad,
            double attentionStab,
            int wanderingEpisodes,
            double efficiency
        ) {
            this.sessionId = sessionId;
            this.sessionDuration = duration;
            this.averageCognitiveLoad = avgLoad;
            this.attentionStability = attentionStab;
            this.mindWanderingEpisodes = wanderingEpisodes;
            this.learningEfficiency = efficiency;
        }

        // Getters
        public String getSessionId() {
            return sessionId;
        }

        public Duration getSessionDuration() {
            return sessionDuration;
        }

        public double getAverageCognitiveLoad() {
            return averageCognitiveLoad;
        }

        public double getAttentionStability() {
            return attentionStability;
        }

        public int getMindWanderingEpisodes() {
            return mindWanderingEpisodes;
        }

        public double getLearningEfficiency() {
            return learningEfficiency;
        }
    }

    /**
     * Initialize the Cognitive Session Manager
     */
    public CognitiveSessionManager(AttentionRecognitionFramework framework) {
        this.cognitiveFramework = framework;
        this.activeSessions = new ConcurrentHashMap<>();
        this.sessionHistory = new ConcurrentHashMap<>();
        this.maxConcurrentSessions = 100;
        this.defaultSessionTimeout = Duration.ofHours(2);
        this.cognitiveLoadThreshold = 0.8;
        this.sessionExecutor = Executors.newCachedThreadPool();
        this.monitoringService = Executors.newScheduledThreadPool(4);
    }

    /**
     * Create a new cognitive learning session
     */
    public CognitiveSession createSession(String learnerId) {
        String sessionId = generateSessionId(learnerId);
        CognitiveSession session = new CognitiveSession(sessionId, learnerId);

        activeSessions.put(sessionId, session);

        // Initialize cognitive monitoring
        startCognitiveMonitoring(session);

        return session;
    }

    /**
     * Get an active session by ID
     */
    public CognitiveSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    /**
     * Close a cognitive session and calculate metrics
     */
    public SessionMetrics closeSession(String sessionId) {
        CognitiveSession session = activeSessions.remove(sessionId);
        if (session == null) {
            return null;
        }

        session.setStatus(SessionStatus.COMPLETED);

        // Calculate session metrics
        SessionMetrics metrics = calculateSessionMetrics(session);

        // Store in history
        sessionHistory
            .computeIfAbsent(session.getLearnerId(), k -> new ArrayList<>())
            .add(metrics);

        return metrics;
    }

    /**
     * Update cognitive state for a session
     */
    public void updateSessionCognitiveState(
        String sessionId,
        AttentionRecognitionFramework.CognitiveState state
    ) {
        CognitiveSession session = activeSessions.get(sessionId);
        if (session != null) {
            session.updateCognitiveState(state);

            // Check for cognitive overload
            if (state.getCognitiveLoad() > cognitiveLoadThreshold) {
                handleCognitiveOverload(session);
            }
        }
    }

    /**
     * Get session history for a learner
     */
    public List<SessionMetrics> getLearnerHistory(String learnerId) {
        return sessionHistory.getOrDefault(learnerId, new ArrayList<>());
    }

    /**
     * Get all active sessions
     */
    public Collection<CognitiveSession> getActiveSessions() {
        return new ArrayList<>(activeSessions.values());
    }

    /**
     * Shutdown the session manager
     */
    public void shutdown() {
        // Close all active sessions
        for (String sessionId : new ArrayList<>(activeSessions.keySet())) {
            closeSession(sessionId);
        }

        // Shutdown executors
        sessionExecutor.shutdown();
        monitoringService.shutdown();

        try {
            if (!sessionExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                sessionExecutor.shutdownNow();
            }
            if (!monitoringService.awaitTermination(30, TimeUnit.SECONDS)) {
                monitoringService.shutdownNow();
            }
        } catch (InterruptedException e) {
            sessionExecutor.shutdownNow();
            monitoringService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Private helper methods

    private String generateSessionId(String learnerId) {
        return (
            learnerId +
            "_" +
            System.currentTimeMillis() +
            "_" +
            UUID.randomUUID().toString().substring(0, 8)
        );
    }

    private void startCognitiveMonitoring(CognitiveSession session) {
        monitoringService.scheduleAtFixedRate(
            () -> {
                if (session.getStatus() == SessionStatus.ACTIVE) {
                    // Generate cognitive state update based on session activity
                    AttentionRecognitionFramework.CognitiveState currentState =
                        cognitiveFramework.getCurrentCognitiveState();
                    session.updateCognitiveState(currentState);
                }
            },
            0,
            session.getCognitiveMonitoringInterval(),
            TimeUnit.MILLISECONDS
        );
    }

    private SessionMetrics calculateSessionMetrics(CognitiveSession session) {
        Duration duration = Duration.between(
            session.getStartTime(),
            LocalDateTime.now()
        );

        List<CognitiveEvent> events = session.getEvents();
        double avgCognitiveLoad = events
            .stream()
            .filter(e ->
                e.getEventData() instanceof
                AttentionRecognitionFramework.CognitiveState
            )
            .mapToDouble(e ->
                ((AttentionRecognitionFramework.CognitiveState) e.getEventData()).getCognitiveLoad()
            )
            .average()
            .orElse(0.0);

        double attentionStability = calculateAttentionStability(events);
        int wanderingEpisodes = countMindWanderingEpisodes(events);
        double learningEfficiency = calculateLearningEfficiency(
            session,
            avgCognitiveLoad,
            attentionStability
        );

        return new SessionMetrics(
            session.getSessionId(),
            duration,
            avgCognitiveLoad,
            attentionStability,
            wanderingEpisodes,
            learningEfficiency
        );
    }

    private double calculateAttentionStability(List<CognitiveEvent> events) {
        List<Double> attentionValues = events
            .stream()
            .filter(e ->
                e.getEventData() instanceof
                AttentionRecognitionFramework.CognitiveState
            )
            .map(e ->
                ((AttentionRecognitionFramework.CognitiveState) e.getEventData()).getAttention()
            )
            .collect(Collectors.toList());

        if (attentionValues.size() < 2) {
            return 1.0; // Perfect stability if only one or no measurements
        }

        // Calculate coefficient of variation (inverse of stability)
        double mean = attentionValues
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
        double variance = attentionValues
            .stream()
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);
        double stdDev = Math.sqrt(variance);

        return mean > 0 ? Math.max(0, 1.0 - (stdDev / mean)) : 0.0;
    }

    private int countMindWanderingEpisodes(List<CognitiveEvent> events) {
        int episodes = 0;
        boolean inWanderingState = false;
        final double wanderingThreshold = 0.5;

        for (CognitiveEvent event : events) {
            if (
                event.getEventData() instanceof
                AttentionRecognitionFramework.CognitiveState
            ) {
                AttentionRecognitionFramework.CognitiveState state =
                    (AttentionRecognitionFramework.CognitiveState) event.getEventData();

                boolean isWandering = state.getWandering() > wanderingThreshold;

                if (!inWanderingState && isWandering) {
                    episodes++;
                    inWanderingState = true;
                } else if (inWanderingState && !isWandering) {
                    inWanderingState = false;
                }
            }
        }

        return episodes;
    }

    private double calculateLearningEfficiency(
        CognitiveSession session,
        double avgCognitiveLoad,
        double attentionStability
    ) {
        // Learning efficiency combines optimal cognitive load with attention stability
        // Optimal cognitive load is around 0.6-0.7 (not too low, not too high)
        double optimalLoad = 0.65;
        double loadEfficiency =
            1.0 - Math.abs(avgCognitiveLoad - optimalLoad) / optimalLoad;

        // Combine load efficiency with attention stability
        return (loadEfficiency * 0.6) + (attentionStability * 0.4);
    }

    private void handleCognitiveOverload(CognitiveSession session) {
        // Log the overload event
        session
            .getEvents()
            .add(
                new CognitiveEvent(
                    LocalDateTime.now(),
                    "cognitive_overload_detected",
                    session.getCurrentCognitiveState()
                )
            );

        // Could implement intervention strategies here:
        // - Suggest break
        // - Reduce task complexity
        // - Switch to different learning mode
        // - Provide mindfulness prompt

        // For now, just mark in session context
        session.setContextValue("overload_detected", true);
        session.setContextValue("overload_timestamp", LocalDateTime.now());
    }
}
