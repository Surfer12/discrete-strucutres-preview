package com.discretelogic.discrete.logic;

/**
 * Handles the logical operations for flow state management in discrete structures.
 */
public class FlowStateLogic {
    // State manager to handle the state transitions
    private final StateManager stateManager;
    
    /**
     * Creates a new FlowStateLogic with default state manager.
     */
    public FlowStateLogic() {
        this.stateManager = new StateManager();
    }
    
    /**
     * Creates a new FlowStateLogic with a provided state manager.
     * 
     * @param stateManager The state manager to use
     */
    public FlowStateLogic(StateManager stateManager) {
        this.stateManager = stateManager;
    }
    
    /**
     * Gets the state manager used by this logic handler.
     * 
     * @return The state manager
     */
    public StateManager getStateManager() {
        return stateManager;
    }
}