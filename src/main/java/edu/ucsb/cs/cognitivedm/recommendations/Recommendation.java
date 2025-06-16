package edu.ucsb.cs.cognitivedm.recommendations;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a cognitive-aware recommendation for the user.
 * This class encapsulates recommendation details including content, 
 * relevance score, and cognitive properties.
 */
public class Recommendation {
    private final String id;
    private final String title;
    private final String description;
    private final String contentId;
    private final RecommendationType type;
    private final double relevanceScore;
    private final Map<String, Object> metadata;
    private final AttentionRecognitionFramework.CognitiveState cognitiveState;
    
    /**
     * Creates a new recommendation with the specified parameters.
     * 
     * @param title The title of the recommendation
     * @param description A description of the recommendation
     * @param contentId The ID of the associated content
     * @param type The type of recommendation
     * @param relevanceScore How relevant this recommendation is (0.0-1.0)
     * @param cognitiveState The cognitive state associated with this recommendation
     */
    public Recommendation(
            String title,
            String description,
            String contentId,
            RecommendationType type,
            double relevanceScore,
            AttentionRecognitionFramework.CognitiveState cognitiveState) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.contentId = contentId;
        this.type = type;
        this.relevanceScore = relevanceScore;
        this.metadata = new HashMap<>();
        this.cognitiveState = cognitiveState;
    }
    
    /**
     * Creates a new recommendation with the specified parameters and metadata.
     * 
     * @param title The title of the recommendation
     * @param description A description of the recommendation
     * @param contentId The ID of the associated content
     * @param type The type of recommendation
     * @param relevanceScore How relevant this recommendation is (0.0-1.0)
     * @param cognitiveState The cognitive state associated with this recommendation
     * @param metadata Additional metadata for this recommendation
     */
    public Recommendation(
            String title,
            String description,
            String contentId,
            RecommendationType type,
            double relevanceScore,
            AttentionRecognitionFramework.CognitiveState cognitiveState,
            Map<String, Object> metadata) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.contentId = contentId;
        this.type = type;
        this.relevanceScore = relevanceScore;
        this.cognitiveState = cognitiveState;
        this.metadata = new HashMap<>(metadata);
    }
    
    // Getters
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getContentId() {
        return contentId;
    }
    
    public RecommendationType getType() {
        return type;
    }
    
    public double getRelevanceScore() {
        return relevanceScore;
    }
    
    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }
    
    public AttentionRecognitionFramework.CognitiveState getCognitiveState() {
        return cognitiveState;
    }
    
    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    @Override
    public String toString() {
        return "Recommendation{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", contentId='" + contentId + '\'' +
                ", type=" + type +
                ", relevanceScore=" + relevanceScore +
                '}';
    }
} 