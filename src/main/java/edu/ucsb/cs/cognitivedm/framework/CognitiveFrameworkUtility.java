package edu.ucsb.cs.cognitivedm.framework;

/**
 * Utility class to resolve ambiguous type references between
 * CognitiveFrameworkTypes and CognitiveFrameworkInterfaces.
 */
public final class CognitiveFrameworkUtility {

    private CognitiveFrameworkUtility() {
        // Prevent instantiation
    }
    
    // Import these types from CognitiveFrameworkTypes
    public static final CognitiveFrameworkTypes.ContentType TYPE_ContentType = null;
    public static final CognitiveFrameworkTypes.DifficultyLevel TYPE_DifficultyLevel = null;
    public static final CognitiveFrameworkTypes.LearningStyle TYPE_LearningStyle = null;
    public static final CognitiveFrameworkTypes.LearningContent TYPE_LearningContent = null;
    public static final CognitiveFrameworkTypes.LearningPath TYPE_LearningPath = null;
    public static final CognitiveFrameworkTypes.Recommendation TYPE_Recommendation = null;
    
    // Import these types from CognitiveFrameworkInterfaces
    public static final CognitiveFrameworkInterfaces.CognitiveState INTERFACE_CognitiveState = null;
    public static final CognitiveFrameworkInterfaces.CognitiveSession INTERFACE_CognitiveSession = null;
    public static final CognitiveFrameworkInterfaces.CognitiveSessionManager INTERFACE_CognitiveSessionManager = null;
    public static final CognitiveFrameworkInterfaces.CognitiveRecommendationEngine INTERFACE_CognitiveRecommendationEngine = null;
    public static final CognitiveFrameworkInterfaces.CognitiveEducationFramework INTERFACE_CognitiveEducationFramework = null;
    public static final CognitiveFrameworkInterfaces.LearningContent INTERFACE_LearningContent = null;
    public static final CognitiveFrameworkInterfaces.RecommendationRequest INTERFACE_RecommendationRequest = null;
    public static final CognitiveFrameworkInterfaces.LearningPath INTERFACE_LearningPath = null;
    
    /**
     * Helper method to create a ContentType from CognitiveFrameworkTypes
     * @param type The enum value
     * @return The type instance
     */
    public static CognitiveFrameworkTypes.ContentType contentType(CognitiveFrameworkTypes.ContentType type) {
        return type;
    }
    
    /**
     * Helper method to create a DifficultyLevel from CognitiveFrameworkTypes
     * @param level The enum value
     * @return The level instance
     */
    public static CognitiveFrameworkTypes.DifficultyLevel difficultyLevel(CognitiveFrameworkTypes.DifficultyLevel level) {
        return level;
    }
    
    /**
     * Helper method to create a LearningStyle from CognitiveFrameworkTypes
     * @param style The enum value
     * @return The style instance
     */
    public static CognitiveFrameworkTypes.LearningStyle learningStyle(CognitiveFrameworkTypes.LearningStyle style) {
        return style;
    }
    
    /**
     * Helper method to create a new LearningContent instance
     */
    public static CognitiveFrameworkTypes.LearningContent createLearningContent(
            String id, 
            String title, 
            String domain, 
            CognitiveFrameworkTypes.ContentType type, 
            CognitiveFrameworkTypes.DifficultyLevel difficulty) {
        return new CognitiveFrameworkTypes.LearningContent(id, title, domain, type, difficulty);
    }
    
    /**
     * Helper method to create a new LearningPath instance
     */
    public static CognitiveFrameworkTypes.LearningPath createLearningPath(
            String userId, 
            CognitiveFrameworkTypes.DifficultyLevel targetLevel) {
        return new CognitiveFrameworkTypes.LearningPath(userId, targetLevel);
    }
} 