package com.discretelogic.cognitive.emergence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a pattern of cognitive states that lead to flow state emergence.
 * This class encapsulates the analysis and prediction of flow state transitions.
 */
public class FlowStatePattern {
    private final List<CognitiveState> stateSequence;
    private final Map<String, Double> patternMetrics;
    private final String patternId;
    
    /**
     * Creates a new flow state pattern with the specified ID.
     * 
     * @param patternId A unique identifier for this pattern
     */
    public FlowStatePattern(String patternId) {
        this.patternId = patternId;
        this.stateSequence = new ArrayList<>();
        this.patternMetrics = new HashMap<>();
        initializeDefaultMetrics();
    }
    
    /**
     * Creates a new flow state pattern with the specified ID and state sequence.
     * 
     * @param patternId A unique identifier for this pattern
     * @param stateSequence A list of cognitive states in sequence
     */
    public FlowStatePattern(String patternId, List<CognitiveState> stateSequence) {
        this.patternId = patternId;
        this.stateSequence = new ArrayList<>(stateSequence);
        this.patternMetrics = new HashMap<>();
        initializeDefaultMetrics();
        calculateMetrics();
    }
    
    /**
     * Initializes the pattern with default metric values.
     */
    private void initializeDefaultMetrics() {
        patternMetrics.put("flowProbability", 0.0);
        patternMetrics.put("stabilityIndex", 0.0);
        patternMetrics.put("transitionRate", 0.0);
        patternMetrics.put("sustainabilityScore", 0.0);
    }
    
    /**
     * Adds a cognitive state to the sequence.
     * 
     * @param state The cognitive state to add
     */
    public void addState(CognitiveState state) {
        stateSequence.add(state);
        calculateMetrics();
    }
    
    /**
     * Gets the list of cognitive states in this pattern.
     * 
     * @return A list of cognitive states
     */
    public List<CognitiveState> getStateSequence() {
        return new ArrayList<>(stateSequence);
    }
    
    /**
     * Gets the ID of this pattern.
     * 
     * @return The pattern ID
     */
    public String getPatternId() {
        return patternId;
    }
    
    /**
     * Gets a metric value.
     * 
     * @param metricName The name of the metric
     * @return The value of the metric
     * @throws IllegalArgumentException if the metric does not exist
     */
    public double getMetric(String metricName) {
        if (!patternMetrics.containsKey(metricName)) {
            throw new IllegalArgumentException("Metric does not exist: " + metricName);
        }
        return patternMetrics.get(metricName);
    }
    
    /**
     * Gets all metrics for this pattern.
     * 
     * @return A map of all metric names to their values
     */
    public Map<String, Double> getAllMetrics() {
        return new HashMap<>(patternMetrics);
    }
    
    /**
     * Calculates metrics based on the state sequence.
     */
    private void calculateMetrics() {
        if (stateSequence.isEmpty()) {
            return;
        }
        
        // Calculate flow probability
        int flowStateCount = 0;
        for (CognitiveState state : stateSequence) {
            if (state.isFlowState()) {
                flowStateCount++;
            }
        }
        double flowProbability = (double) flowStateCount / stateSequence.size();
        patternMetrics.put("flowProbability", flowProbability);
        
        // Calculate stability index
        double stabilitySum = 0.0;
        for (CognitiveState state : stateSequence) {
            double attentionLevel = state.getParameter("attentionLevel");
            double cognitiveLoad = state.getParameter("cognitiveLoad");
            // Stability is high when attention is high and cognitive load is moderate
            double stateStability = attentionLevel * (1.0 - Math.abs(cognitiveLoad - 0.5) * 2.0);
            stabilitySum += stateStability;
        }
        double stabilityIndex = stabilitySum / stateSequence.size();
        patternMetrics.put("stabilityIndex", stabilityIndex);
        
        // Calculate transition rate
        if (stateSequence.size() > 1) {
            int transitions = stateSequence.size() - 1;
            double transitionRate = (double) transitions / stateSequence.size();
            patternMetrics.put("transitionRate", transitionRate);
        }
        
        // Calculate sustainability score
        double sustainabilityScore = flowProbability * stabilityIndex;
        patternMetrics.put("sustainabilityScore", sustainabilityScore);
    }
    
    /**
     * Predicts the likelihood of a flow state emerging next in the sequence.
     * 
     * @return A probability value [0,1] indicating the likelihood of flow state
     */
    public double predictFlowLikelihood() {
        if (stateSequence.isEmpty()) {
            return 0.0;
        }
        
        // Simple prediction based on current metrics
        double flowProbability = patternMetrics.get("flowProbability");
        double stabilityIndex = patternMetrics.get("stabilityIndex");
        
        // Get the last state in the sequence
        CognitiveState lastState = stateSequence.get(stateSequence.size() - 1);
        double lastFlowPotential = lastState.getParameter("flowPotential");
        
        // Prediction is a weighted combination of historical pattern and current state
        return (0.7 * lastFlowPotential) + (0.3 * flowProbability * stabilityIndex);
    }
    
    /**
     * Determines if this pattern represents a sustainable flow state pattern.
     * 
     * @return true if this is a sustainable flow pattern, false otherwise
     */
    public boolean isSustainableFlowPattern() {
        double sustainabilityScore = patternMetrics.get("sustainabilityScore");
        return sustainabilityScore >= 0.6;
    }
    
    @Override
    public String toString() {
        return "FlowStatePattern[" + patternId + ", states=" + stateSequence.size() + "]" + patternMetrics;
    }
} 