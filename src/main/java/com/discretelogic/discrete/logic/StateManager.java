package com.discretelogic.discrete.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages state transitions and validates state configurations in discrete structures.
 * This class provides functionality for tracking, transitioning, and validating states.
 */
public class StateManager {
    private final Map<String, Boolean> stateValues;
    private final Map<String, String> stateTransitions;

    /**
     * Creates a new StateManager with empty state and transition maps.
     */
    public StateManager() {
        this.stateValues = new HashMap<>();
        this.stateTransitions = new HashMap<>();
    }

    /**
     * Sets a state value.
     * 
     * @param stateName The name of the state
     * @param value The boolean value of the state
     */
    public void setState(String stateName, boolean value) {
        stateValues.put(stateName, value);
    }

    /**
     * Gets a state value.
     * 
     * @param stateName The name of the state
     * @return The boolean value of the state
     * @throws IllegalArgumentException if the state does not exist
     */
    public boolean getState(String stateName) {
        if (!stateValues.containsKey(stateName)) {
            throw new IllegalArgumentException("State does not exist: " + stateName);
        }
        return stateValues.get(stateName);
    }

    /**
     * Defines a state transition rule.
     * 
     * @param fromState The starting state name
     * @param toState The target state name
     */
    public void defineTransition(String fromState, String toState) {
        stateTransitions.put(fromState, toState);
    }

    /**
     * Performs a transition from one state to another based on defined rules.
     * 
     * @param fromState The current state name
     * @return The name of the new state after transition
     * @throws IllegalArgumentException if no transition is defined for the given state
     */
    public String transition(String fromState) {
        if (!stateTransitions.containsKey(fromState)) {
            throw new IllegalArgumentException("No transition defined for state: " + fromState);
        }
        return stateTransitions.get(fromState);
    }

    /**
     * Validates a state configuration against a set of rules.
     * 
     * @param stateName The name of the state to validate
     * @return true if the state is valid, false otherwise
     */
    public boolean validateState(String stateName) {
        // This is a placeholder implementation
        // In a real implementation, this would check the state against predefined rules
        return stateValues.containsKey(stateName);
    }

    /**
     * Gets all current states.
     * 
     * @return A map of all state names to their boolean values
     */
    public Map<String, Boolean> getAllStates() {
        return new HashMap<>(stateValues);
    }

    /**
     * Gets all defined transitions.
     * 
     * @return A map of all transition rules (from state to to state)
     */
    public Map<String, String> getAllTransitions() {
        return new HashMap<>(stateTransitions);
    }

    /**
     * Clears all states and transitions.
     */
    public void reset() {
        stateValues.clear();
        stateTransitions.clear();
    }
} 