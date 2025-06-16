package edu.ucsb.cs.cognitivedm.education;

/**
 * Represents the difficulty level of educational content.
 */
public enum DifficultyLevel {
    /**
     * Introductory level suitable for beginners.
     */
    BEGINNER(1, "Beginner", 0.2),
    
    /**
     * Elementary level with basic concepts.
     */
    ELEMENTARY(2, "Elementary", 0.4),
    
    /**
     * Intermediate level requiring some prior knowledge.
     */
    INTERMEDIATE(3, "Intermediate", 0.6),
    
    /**
     * Advanced level requiring substantial knowledge.
     */
    ADVANCED(4, "Advanced", 0.8),
    
    /**
     * Expert level requiring deep understanding of the subject.
     */
    EXPERT(5, "Expert", 1.0);
    
    private final int level;
    private final String label;
    private final double cognitiveLoadFactor;
    
    DifficultyLevel(int level, String label, double cognitiveLoadFactor) {
        this.level = level;
        this.label = label;
        this.cognitiveLoadFactor = cognitiveLoadFactor;
    }
    
    /**
     * Gets the numeric level (1-5).
     * 
     * @return The numeric difficulty level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Gets the human-readable label.
     * 
     * @return The difficulty level label
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Gets the cognitive load factor associated with this difficulty level.
     * 
     * @return The cognitive load factor (0.0-1.0)
     */
    public double getCognitiveLoadFactor() {
        return cognitiveLoadFactor;
    }
    
    /**
     * Gets a difficulty level from a numeric value.
     * 
     * @param level The numeric level (1-5)
     * @return The corresponding difficulty level
     * @throws IllegalArgumentException if the level is invalid
     */
    public static DifficultyLevel fromLevel(int level) {
        for (DifficultyLevel dl : values()) {
            if (dl.level == level) {
                return dl;
            }
        }
        throw new IllegalArgumentException("Invalid difficulty level: " + level);
    }
    
    /**
     * Gets a difficulty level from a cognitive load factor.
     * 
     * @param factor The cognitive load factor (0.0-1.0)
     * @return The closest matching difficulty level
     */
    public static DifficultyLevel fromCognitiveLoadFactor(double factor) {
        DifficultyLevel closest = BEGINNER;
        double minDiff = Math.abs(factor - BEGINNER.cognitiveLoadFactor);
        
        for (DifficultyLevel dl : values()) {
            double diff = Math.abs(factor - dl.cognitiveLoadFactor);
            if (diff < minDiff) {
                minDiff = diff;
                closest = dl;
            }
        }
        
        return closest;
    }
} 