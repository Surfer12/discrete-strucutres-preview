package edu.ucsb.cs.cognitivedm.education;

import edu.ucsb.cs.cognitivedm.framework.CognitiveFrameworkTypes;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a piece of learning content within the cognitive education framework.
 */
public class LearningContent {
    private final String id;
    private final String title;
    private final CognitiveFrameworkTypes.ContentType contentType;
    private final DifficultyLevel difficultyLevel;
    private final Map<String, Object> content;
    private final Map<String, Double> cognitiveProperties;
    private final double estimatedCognitiveLoad;
    private final String[] prerequisites;
    private final String[] relatedTopics;
    
    /**
     * Creates a new learning content item.
     * 
     * @param title The title of the learning content
     * @param contentType The type of content
     * @param difficultyLevel The difficulty level
     * @param content The actual content data
     * @param estimatedCognitiveLoad The estimated cognitive load (0.0-1.0)
     * @param prerequisites IDs of prerequisite content items
     * @param relatedTopics Related topic names
     */
    public LearningContent(
            String title,
            CognitiveFrameworkTypes.ContentType contentType,
            DifficultyLevel difficultyLevel,
            Map<String, Object> content,
            double estimatedCognitiveLoad,
            String[] prerequisites,
            String[] relatedTopics) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.contentType = contentType;
        this.difficultyLevel = difficultyLevel;
        this.content = new HashMap<>(content);
        this.cognitiveProperties = new HashMap<>();
        this.estimatedCognitiveLoad = estimatedCognitiveLoad;
        this.prerequisites = prerequisites != null ? prerequisites.clone() : new String[0];
        this.relatedTopics = relatedTopics != null ? relatedTopics.clone() : new String[0];
        
        // Set default cognitive properties based on difficulty level
        this.cognitiveProperties.put("attention_requirement", difficultyLevel.getCognitiveLoadFactor());
        this.cognitiveProperties.put("recognition_complexity", difficultyLevel.getCognitiveLoadFactor() * 0.8);
        this.cognitiveProperties.put("wandering_potential", Math.max(0.1, 1.0 - difficultyLevel.getCognitiveLoadFactor()));
    }
    
    /**
     * Creates a new learning content item with a predefined ID.
     * This constructor is primarily for creating test fixtures or loading from storage.
     * 
     * @param id The ID of the learning content
     * @param title The title of the learning content
     * @param contentType The type of content
     * @param difficultyLevel The difficulty level
     * @param content The actual content data
     * @param estimatedCognitiveLoad The estimated cognitive load (0.0-1.0)
     * @param prerequisites IDs of prerequisite content items
     * @param relatedTopics Related topic names
     */
    public LearningContent(
            String id,
            String title,
            CognitiveFrameworkTypes.ContentType contentType,
            DifficultyLevel difficultyLevel,
            Map<String, Object> content,
            double estimatedCognitiveLoad,
            String[] prerequisites,
            String[] relatedTopics) {
        this.id = id;
        this.title = title;
        this.contentType = contentType;
        this.difficultyLevel = difficultyLevel;
        this.content = new HashMap<>(content);
        this.cognitiveProperties = new HashMap<>();
        this.estimatedCognitiveLoad = estimatedCognitiveLoad;
        this.prerequisites = prerequisites != null ? prerequisites.clone() : new String[0];
        this.relatedTopics = relatedTopics != null ? relatedTopics.clone() : new String[0];
        
        // Set default cognitive properties based on difficulty level
        this.cognitiveProperties.put("attention_requirement", difficultyLevel.getCognitiveLoadFactor());
        this.cognitiveProperties.put("recognition_complexity", difficultyLevel.getCognitiveLoadFactor() * 0.8);
        this.cognitiveProperties.put("wandering_potential", Math.max(0.1, 1.0 - difficultyLevel.getCognitiveLoadFactor()));
    }
    
    // Getters
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public CognitiveFrameworkTypes.ContentType getContentType() {
        return contentType;
    }
    
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public Map<String, Object> getContent() {
        return new HashMap<>(content);
    }
    
    public Map<String, Double> getCognitiveProperties() {
        return new HashMap<>(cognitiveProperties);
    }
    
    public double getEstimatedCognitiveLoad() {
        return estimatedCognitiveLoad;
    }
    
    public String[] getPrerequisites() {
        return prerequisites.clone();
    }
    
    public String[] getRelatedTopics() {
        return relatedTopics.clone();
    }
    
    /**
     * Adds a cognitive property to this content item.
     * 
     * @param key The property name
     * @param value The property value
     */
    public void addCognitiveProperty(String key, Double value) {
        cognitiveProperties.put(key, value);
    }
    
    @Override
    public String toString() {
        return "LearningContent{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", contentType=" + contentType +
                ", difficultyLevel=" + difficultyLevel +
                ", estimatedCognitiveLoad=" + estimatedCognitiveLoad +
                '}';
    }
} 