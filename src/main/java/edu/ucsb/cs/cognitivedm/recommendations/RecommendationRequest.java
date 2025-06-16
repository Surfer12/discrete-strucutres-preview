package edu.ucsb.cs.cognitivedm.recommendations;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a request for recommendations from the recommendation engine.
 */
public class RecommendationRequest {
    private final String id;
    private final String userId;
    private final AttentionRecognitionFramework.CognitiveState cognitiveState;
    private final String topic;
    private final int maxResults;
    private final EnumSet<RecommendationType> allowedTypes;
    private final Map<String, Object> context;
    private final long timestamp;
    
    /**
     * Creates a new recommendation request.
     * 
     * @param userId The ID of the user requesting recommendations
     * @param cognitiveState The current cognitive state of the user
     * @param topic The topic for which recommendations are requested
     * @param maxResults The maximum number of recommendations to return
     * @param allowedTypes The types of recommendations that are allowed
     * @param context Additional context for the recommendation request
     */
    public RecommendationRequest(
            String userId,
            AttentionRecognitionFramework.CognitiveState cognitiveState,
            String topic,
            int maxResults,
            EnumSet<RecommendationType> allowedTypes,
            Map<String, Object> context) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.cognitiveState = cognitiveState;
        this.topic = topic;
        this.maxResults = maxResults;
        this.allowedTypes = allowedTypes.clone();
        this.context = new HashMap<>(context);
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Creates a new recommendation request with all recommendation types allowed.
     * 
     * @param userId The ID of the user requesting recommendations
     * @param cognitiveState The current cognitive state of the user
     * @param topic The topic for which recommendations are requested
     * @param maxResults The maximum number of recommendations to return
     */
    public RecommendationRequest(
            String userId,
            AttentionRecognitionFramework.CognitiveState cognitiveState,
            String topic,
            int maxResults) {
        this(userId, cognitiveState, topic, maxResults, 
             EnumSet.allOf(RecommendationType.class), new HashMap<>());
    }
    
    // Getters
    
    public String getId() {
        return id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public AttentionRecognitionFramework.CognitiveState getCognitiveState() {
        return cognitiveState;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public int getMaxResults() {
        return maxResults;
    }
    
    public EnumSet<RecommendationType> getAllowedTypes() {
        return allowedTypes.clone();
    }
    
    public Map<String, Object> getContext() {
        return context;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "RecommendationRequest{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", topic='" + topic + '\'' +
                ", maxResults=" + maxResults +
                ", allowedTypes=" + allowedTypes +
                ", timestamp=" + timestamp +
                '}';
    }
} 