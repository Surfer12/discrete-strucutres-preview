package edu.ucsb.cs.cognitivedm.framework;

import com.discretelogic.util.LoggingUtil;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mock implementation of the CognitiveSessionManager interface.
 * Provides a thread-safe implementation for managing cognitive sessions.
 */
public class MockCognitiveSessionManager implements CognitiveFrameworkInterfaces.CognitiveSessionManager {
    private static final Logger LOGGER = LoggingUtil.getLogger(MockCognitiveSessionManager.class);

    // Concurrent map to store sessions
    private final ConcurrentHashMap<String, CognitiveFrameworkInterfaces.CognitiveSession> sessionStore;
    
    // Atomic counter for active sessions
    private final AtomicInteger activeSessionCount;
    
    // Default attention threshold
    private volatile double defaultAttentionThreshold;
    
    // Default monitoring interval
    private volatile int defaultMonitoringInterval;

    /**
     * Constructor initializes the session store and counters.
     */
    public MockCognitiveSessionManager() {
        this.sessionStore = new ConcurrentHashMap<>();
        this.activeSessionCount = new AtomicInteger(0);
        this.defaultAttentionThreshold = 0.5; // Default value
        this.defaultMonitoringInterval = 1000; // Default 1 second
        
        LOGGER.info("MockCognitiveSessionManager initialized");
    }

    @Override
    public CognitiveFrameworkInterfaces.CognitiveSession getOrCreateSession(String userId) {
        LOGGER.debug("Attempting to get or create session for user: {}", userId);
        return sessionStore.computeIfAbsent(userId, key -> {
            CognitiveFrameworkInterfaces.CognitiveSession newSession = createNewSession();
            activeSessionCount.incrementAndGet();
            return newSession;
        });
    }

    @Override
    public int getActiveSessionCount() {
        return activeSessionCount.get();
    }

    @Override
    public void setDefaultAttentionThreshold(double threshold) {
        if (threshold >= 0.0 && threshold <= 1.0) {
            this.defaultAttentionThreshold = threshold;
            LOGGER.info("Default attention threshold set to: {}", threshold);
        } else {
            LOGGER.warn("Invalid attention threshold: {}. Must be between 0.0 and 1.0", threshold);
        }
    }

    @Override
    public void setDefaultMonitoringInterval(int interval) {
        if (interval > 0) {
            this.defaultMonitoringInterval = interval;
            LOGGER.info("Default monitoring interval set to: {} ms", interval);
        } else {
            LOGGER.warn("Invalid monitoring interval: {}. Must be positive", interval);
        }
    }

    /**
     * Create a new cognitive session with default parameters.
     *
     * @return A new CognitiveSession instance
     */
    private CognitiveFrameworkInterfaces.CognitiveSession createNewSession() {
        MockCognitiveSession session = new MockCognitiveSession();
        session.setSessionType("default");
        session.setCognitiveMonitoringInterval(defaultMonitoringInterval);
        return session;
    }

    /**
     * Inner class to represent a mock cognitive session.
     */
    private static class MockCognitiveSession implements CognitiveFrameworkInterfaces.CognitiveSession {
        private static final Logger SESSION_LOGGER = LoggingUtil.getLogger(MockCognitiveSession.class);
        
        private Object sessionType;
        private CognitiveState currentState;
        private int monitoringInterval;
        private boolean isActive;

        @Override
        public void updateCognitiveState(CognitiveState state) {
            this.currentState = state;
            SESSION_LOGGER.debug("Cognitive state updated to: {}", state);
        }

        @Override
        public void setSessionType(Object sessionType) {
            this.sessionType = sessionType;
            SESSION_LOGGER.info("Session type set to: {}", sessionType);
        }

        @Override
        public void startSession() {
            this.isActive = true;
            SESSION_LOGGER.info("Session started");
        }

        @Override
        public void setCognitiveMonitoringInterval(int interval) {
            if (interval > 0) {
                this.monitoringInterval = interval;
                SESSION_LOGGER.debug("Monitoring interval set to: {} ms", interval);
            } else {
                SESSION_LOGGER.warn("Invalid monitoring interval: {}. Must be positive", interval);
            }
        }

        @Override
        public CognitiveState getCurrentCognitiveState() {
            return this.currentState;
        }
    }

    /**
     * Represents a simplified cognitive state for demonstration.
     */
    public static class CognitiveState {
        private final String description;
        private final double attentionLevel;

        public CognitiveState(String description, double attentionLevel) {
            this.description = description;
            this.attentionLevel = attentionLevel;
        }

        @Override
        public String toString() {
            return String.format("CognitiveState{description='%s', attentionLevel=%.2f}", 
                description, attentionLevel);
        }
    }
} 