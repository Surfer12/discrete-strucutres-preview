package edu.ucsb.cs.cognitivedm.framework;

import edu.ucsb.cs.cognitivedm.CognitiveDiscreteMathLibrary.MathSessionType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Represents an active cognitive session for a user.
 * Manages the cognitive state tracking and monitoring for a user session.
 */
public class CognitiveSession {
    private final String id;
    private final String userId;
    private final AttentionRecognitionFramework cognitiveFramework;
    private MathSessionType sessionType;
    private AttentionRecognitionFramework.CognitiveState currentState;
    private final ConcurrentLinkedQueue<AttentionRecognitionFramework.CognitiveState> stateHistory;
    private final Map<String, Object> sessionData;
    private long startTime;
    private long lastUpdateTime;
    private boolean active;
    private int cognitiveMonitoringInterval;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> monitoringTask;
    
    /**
     * Creates a new cognitive session for a user.
     * 
     * @param userId The ID of the user
     * @param cognitiveFramework The cognitive framework to use for this session
     */
    public CognitiveSession(String userId, AttentionRecognitionFramework cognitiveFramework) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.cognitiveFramework = cognitiveFramework;
        this.currentState = new AttentionRecognitionFramework.CognitiveState(0.5, 0.5, 0.3);
        this.stateHistory = new ConcurrentLinkedQueue<>();
        this.sessionData = new HashMap<>();
        this.active = false;
        this.cognitiveMonitoringInterval = 60; // Default: 60 seconds
        this.scheduler = Executors.newScheduledThreadPool(1);
    }
    
    /**
     * Starts the session and begins cognitive monitoring.
     */
    public void startSession() {
        if (active) {
            return; // Already active
        }
        
        startTime = System.currentTimeMillis();
        lastUpdateTime = startTime;
        active = true;
        
        // Add initial state
        stateHistory.add(currentState);
        
        // Start cognitive monitoring
        startMonitoring();
    }
    
    /**
     * Stops the session and cognitive monitoring.
     */
    public void endSession() {
        if (!active) {
            return; // Already inactive
        }
        
        active = false;
        
        // Stop monitoring
        if (monitoringTask != null && !monitoringTask.isDone()) {
            monitoringTask.cancel(false);
        }
    }
    
    /**
     * Updates the cognitive state based on user input or activity.
     * 
     * @param input The input data to process
     */
    public void updateState(Object input) {
        if (!active) {
            return;
        }
        
        // Process the input and update cognitive state
        List<AttentionRecognitionFramework.ProcessingResult> results = 
                cognitiveFramework.process(input, 1);
        
        if (!results.isEmpty()) {
            AttentionRecognitionFramework.ProcessingResult result = results.get(results.size() - 1);
            currentState = result.getState();
            stateHistory.add(currentState);
            lastUpdateTime = System.currentTimeMillis();
        }
    }
    
    /**
     * Sets the cognitive monitoring interval.
     * 
     * @param intervalSeconds The monitoring interval in seconds
     */
    public void setCognitiveMonitoringInterval(int intervalSeconds) {
        this.cognitiveMonitoringInterval = intervalSeconds;
        
        // Restart monitoring if active
        if (active) {
            if (monitoringTask != null && !monitoringTask.isDone()) {
                monitoringTask.cancel(false);
            }
            startMonitoring();
        }
    }
    
    /**
     * Sets the session type.
     * 
     * @param sessionType The type of session
     */
    public void setSessionType(MathSessionType sessionType) {
        this.sessionType = sessionType;
    }
    
    /**
     * Gets the current cognitive state.
     * 
     * @return The current cognitive state
     */
    public AttentionRecognitionFramework.CognitiveState getCurrentCognitiveState() {
        return currentState;
    }
    
    /**
     * Gets the session history of cognitive states.
     * 
     * @return An unmodifiable list of cognitive states
     */
    public List<AttentionRecognitionFramework.CognitiveState> getStateHistory() {
        return Collections.unmodifiableList(new ArrayList<>(stateHistory));
    }
    
    /**
     * Gets the session data.
     * 
     * @return The session data
     */
    public Map<String, Object> getSessionData() {
        return Collections.unmodifiableMap(sessionData);
    }
    
    /**
     * Adds or updates a session data item.
     * 
     * @param key The data key
     * @param value The data value
     */
    public void setSessionData(String key, Object value) {
        sessionData.put(key, value);
    }
    
    /**
     * Gets the session ID.
     * 
     * @return The session ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the user ID.
     * 
     * @return The user ID
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * Gets the session type.
     * 
     * @return The session type
     */
    public MathSessionType getSessionType() {
        return sessionType;
    }
    
    /**
     * Gets the session start time.
     * 
     * @return The start time in milliseconds
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Gets the time of the last update.
     * 
     * @return The last update time in milliseconds
     */
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * Checks if the session is active.
     * 
     * @return true if the session is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Gets the session duration.
     * 
     * @return The session duration in milliseconds
     */
    public long getSessionDuration() {
        if (!active) {
            return lastUpdateTime - startTime;
        }
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * Gets the cognitive monitoring interval.
     * 
     * @return The monitoring interval in seconds
     */
    public int getCognitiveMonitoringInterval() {
        return cognitiveMonitoringInterval;
    }
    
    /**
     * Starts the cognitive monitoring task.
     */
    private void startMonitoring() {
        monitoringTask = scheduler.scheduleAtFixedRate(
            this::monitorCognitiveState,
            cognitiveMonitoringInterval,
            cognitiveMonitoringInterval,
            TimeUnit.SECONDS
        );
    }
    
    /**
     * Monitors the cognitive state and updates it if necessary.
     */
    private void monitorCognitiveState() {
        if (!active) {
            return;
        }
        
        // Calculate time since last update
        long currentTime = System.currentTimeMillis();
        double timeDeltaSeconds = (currentTime - lastUpdateTime) / 1000.0;
        
        // Evolve the cognitive state based on time
        if (currentState != null) {
            currentState = currentState.evolve(timeDeltaSeconds);
            stateHistory.add(currentState);
            lastUpdateTime = currentTime;
        }
    }
    
    /**
     * Shuts down the session and releases resources.
     */
    public void shutdown() {
        endSession();
        scheduler.shutdown();
    }
    
    @Override
    public String toString() {
        return "CognitiveSession{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", sessionType=" + sessionType +
                ", active=" + active +
                ", duration=" + getSessionDuration() + "ms" +
                ", stateHistorySize=" + stateHistory.size() +
                '}';
    }
} 