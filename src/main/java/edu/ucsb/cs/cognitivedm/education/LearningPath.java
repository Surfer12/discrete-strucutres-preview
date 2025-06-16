package edu.ucsb.cs.cognitivedm.education;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a personalized learning path for a user.
 * A learning path is a sequence of learning content items organized
 * to guide the user through a specific subject area.
 */
public class LearningPath {
    private final String id;
    private final String learnerId;
    private final String subject;
    private final List<LearningContent> content;
    private final AttentionRecognitionFramework.CognitiveState initialCognitiveState;
    private final long creationTimestamp;
    
    /**
     * Creates a new learning path.
     * 
     * @param learnerId The ID of the learner for whom this path is created
     * @param subject The subject of the learning path
     * @param content The sequence of learning content items
     * @param initialCognitiveState The initial cognitive state of the learner
     */
    public LearningPath(
            String learnerId,
            String subject,
            List<LearningContent> content,
            AttentionRecognitionFramework.CognitiveState initialCognitiveState) {
        this.id = UUID.randomUUID().toString();
        this.learnerId = learnerId;
        this.subject = subject;
        this.content = new ArrayList<>(content);
        this.initialCognitiveState = initialCognitiveState;
        this.creationTimestamp = System.currentTimeMillis();
    }
    
    /**
     * Gets the unique identifier of this learning path.
     * 
     * @return The learning path ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the ID of the learner for whom this path was created.
     * 
     * @return The learner ID
     */
    public String getLearnerId() {
        return learnerId;
    }
    
    /**
     * Gets the subject of this learning path.
     * 
     * @return The subject
     */
    public String getSubject() {
        return subject;
    }
    
    /**
     * Gets the sequence of learning content items in this path.
     * 
     * @return An unmodifiable list of learning content items
     */
    public List<LearningContent> getContent() {
        return Collections.unmodifiableList(content);
    }
    
    /**
     * Gets the initial cognitive state of the learner.
     * 
     * @return The initial cognitive state
     */
    public AttentionRecognitionFramework.CognitiveState getInitialCognitiveState() {
        return initialCognitiveState;
    }
    
    /**
     * Gets the timestamp when this learning path was created.
     * 
     * @return The creation timestamp in milliseconds
     */
    public long getCreationTimestamp() {
        return creationTimestamp;
    }
    
    /**
     * Gets the estimated difficulty progression of this learning path.
     * 
     * @return A list of difficulty levels corresponding to each content item
     */
    public List<DifficultyLevel> getDifficultyProgression() {
        List<DifficultyLevel> progression = new ArrayList<>();
        for (LearningContent item : content) {
            progression.add(item.getDifficultyLevel());
        }
        return progression;
    }
    
    /**
     * Gets the estimated cognitive load progression of this learning path.
     * 
     * @return A list of cognitive load estimates for each content item
     */
    public List<Double> getCognitiveLoadProgression() {
        List<Double> progression = new ArrayList<>();
        for (LearningContent item : content) {
            progression.add(item.getEstimatedCognitiveLoad());
        }
        return progression;
    }
    
    @Override
    public String toString() {
        return "LearningPath{" +
                "id='" + id + '\'' +
                ", learnerId='" + learnerId + '\'' +
                ", subject='" + subject + '\'' +
                ", contentItems=" + content.size() +
                ", creationTimestamp=" + creationTimestamp +
                '}';
    }
} 