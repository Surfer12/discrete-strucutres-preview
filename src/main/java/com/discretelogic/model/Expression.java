package com.discretelogic.model;

import java.util.*;

/**
 * Represents a boolean expression with variables and operations.
 * This class provides functionality to evaluate the expression with different
 * variable assignments and analyze its properties.
 */
public class Expression {
    private final String expressionString;
    private final Set<String> variables;
    
    /**
     * Creates a new Expression.
     * 
     * @param expressionString The string representation of the expression
     * @param variables The set of variables used in the expression
     */
    public Expression(String expressionString, Set<String> variables) {
        this.expressionString = expressionString;
        this.variables = Collections.unmodifiableSet(new HashSet<>(variables));
    }
    
    /**
     * Gets the string representation of the expression.
     * 
     * @return The expression string
     */
    public String getExpressionString() {
        return expressionString;
    }
    
    /**
     * Gets the set of variables used in the expression.
     * 
     * @return The set of variable names
     */
    public Set<String> getVariables() {
        return variables;
    }
    
    /**
     * Evaluates the expression with the given variable assignments.
     * 
     * @param assignments A map of variable names to boolean values
     * @return The result of evaluating the expression
     */
    public boolean evaluate(Map<String, Boolean> assignments) {
        // This is a placeholder implementation
        // In a real implementation, this would actually evaluate the expression
        // using the variable assignments
        
        // For demonstration purposes, we'll return a simple result
        // based on the number of true assignments
        int trueCount = 0;
        for (String variable : variables) {
            if (assignments.containsKey(variable) && assignments.get(variable)) {
                trueCount++;
            }
        }
        
        // Even number of true variables will give true result
        return trueCount % 2 == 0;
    }
    
    /**
     * Gets all possible variable assignments for this expression.
     * 
     * @return A list of all possible variable assignment combinations
     */
    public List<Map<String, Boolean>> getAllAssignments() {
        List<Map<String, Boolean>> assignments = new ArrayList<>();
        generateAssignments(new HashMap<>(), new ArrayList<>(variables), 0, assignments);
        return assignments;
    }
    
    /**
     * Recursively generates all possible variable assignments.
     */
    private void generateAssignments(Map<String, Boolean> current, List<String> vars, 
                                    int index, List<Map<String, Boolean>> result) {
        if (index == vars.size()) {
            result.add(new HashMap<>(current));
            return;
        }
        
        String var = vars.get(index);
        
        // Try with variable set to false
        current.put(var, false);
        generateAssignments(current, vars, index + 1, result);
        
        // Try with variable set to true
        current.put(var, true);
        generateAssignments(current, vars, index + 1, result);
    }
    
    @Override
    public String toString() {
        return expressionString;
    }
} 