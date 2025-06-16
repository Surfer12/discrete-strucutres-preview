package edu.ucsb.cs.cognitivedm.recommendations;

/**
 * Types of recommendations that can be provided by the cognitive recommendation engine.
 */
public enum RecommendationType {
    /**
     * Recommended content based on the user's current learning needs.
     */
    LEARNING_CONTENT,
    
    /**
     * Practice exercises to reinforce current learning.
     */
    PRACTICE_EXERCISE,
    
    /**
     * Recommended next steps in the learning path.
     */
    NEXT_STEP,
    
    /**
     * Review material to strengthen understanding of previously covered topics.
     */
    REVIEW,
    
    /**
     * Advanced material to challenge the user.
     */
    CHALLENGE,
    
    /**
     * Material to remediate knowledge gaps or misunderstandings.
     */
    REMEDIATION,
    
    /**
     * Explanatory material to clarify complex concepts.
     */
    EXPLANATION,
    
    /**
     * Related content that might be of interest but is not on the direct learning path.
     */
    RELATED_CONTENT
} 