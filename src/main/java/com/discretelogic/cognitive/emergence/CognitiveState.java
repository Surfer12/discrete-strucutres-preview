package com.discretelogic.cognitive.emergence;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a cognitive state within the flow state model.
 * This class encapsulates parameters that describe a cognitive state
 * such as attention level, cognitive load, and processing capacity.
 */
public class CognitiveState {
    private final Map<String, Double> parameters;
    private final String stateId;
    
    /**
     * Creates a new cognitive state with the specified ID.
     * 
     * @param stateId A unique identifier for this cognitive state
     */
    public CognitiveState(String stateId) {
        this.stateId = stateId;
        this.parameters = new HashMap<>();
        initializeDefaultParameters();
    }
    
    /**
     * Creates a new cognitive state with the specified ID and parameters.
     * 
     * @param stateId A unique identifier for this cognitive state
     * @param parameters A map of parameter names to their values
     */
    public CognitiveState(String stateId, Map<String, Double> parameters) {
        this.stateId = stateId;
        this.parameters = new HashMap<>(parameters);
    }
    
    /**
     * Initializes the state with default parameter values.
     */
    private void initializeDefaultParameters() {
        // Default values for cognitive state parameters
        parameters.put("attentionLevel", 0.5);
        parameters.put("cognitiveLoad", 0.3);
        parameters.put("processingCapacity", 0.7);
        parameters.put("flowPotential", 0.0);
    }
    
    /**
     * Sets a parameter value.
     * 
     * @param paramName The name of the parameter
     * @param value The value to set (typically in the range [0,1])
     */
    public void setParameter(String paramName, double value) {
        parameters.put(paramName, value);
    }
    
    /**
     * Gets a parameter value.
     * 
     * @param paramName The name of the parameter
     * @return The value of the parameter
     * @throws IllegalArgumentException if the parameter does not exist
     */
    public double getParameter(String paramName) {
        if (!parameters.containsKey(paramName)) {
            throw new IllegalArgumentException("Parameter does not exist: " + paramName);
        }
        return parameters.get(paramName);
    }
    
    /**
     * Gets the ID of this cognitive state.
     * 
     * @return The state ID
     */
    public String getStateId() {
        return stateId;
    }
    
    /**
     * Gets all parameters for this state.
     * 
     * @return A map of all parameter names to their values
     */
    public Map<String, Double> getAllParameters() {
        return new HashMap<>(parameters);
    }
    
    /**
     * Calculates the flow potential of this cognitive state.
     * Flow state typically emerges when there's a balance between
     * challenge (cognitive load) and skill (processing capacity).
     * 
     * @return A value representing the potential for flow state [0,1]
     */
    public double calculateFlowPotential() {
        double attentionLevel = parameters.getOrDefault("attentionLevel", 0.5);
        double cognitiveLoad = parameters.getOrDefault("cognitiveLoad", 0.3);
        double processingCapacity = parameters.getOrDefault("processingCapacity", 0.7);
        
        // Simple flow model: high when challenge matches skill, and attention is high
        double challengeSkillBalance = 1.0 - Math.abs(cognitiveLoad - processingCapacity);
        double flowPotential = attentionLevel * challengeSkillBalance;
        
        // Update the stored flow potential
        parameters.put("flowPotential", flowPotential);
        
        return flowPotential;
    }
    
    /**
     * Determines if this state represents a flow state.
     * Flow states typically have high flow potential values.
     * 
     * @return true if this is a flow state, false otherwise
     */
    public boolean isFlowState() {
        // Ensure flow potential is calculated
        if (!parameters.containsKey("flowPotential")) {
            calculateFlowPotential();
        }
        
        // Flow threshold is typically above 0.7
        return parameters.get("flowPotential") >= 0.7;
    }
    
    @Override
    public String toString() {
        return "CognitiveState[" + stateId + "]" + parameters;
    }
} 