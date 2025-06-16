package com.discretelogic.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a boolean expression with variables and operations.
 * This class provides functionality for evaluating the expression with different variable assignments.
 */
public class Expression {
    private final String expression;
    private final Set<String> variables;

    /**
     * Creates a new Expression with the specified string representation.
     * 
     * @param expression The string representation of the boolean expression
     * @param variables The set of variables used in the expression
     */
    public Expression(String expression, Set<String> variables) {
        this.expression = expression;
        this.variables = new HashSet<>(variables);
    }

    /**
     * Creates a new Expression with the specified string representation.
     * Variables are automatically extracted from the expression.
     * 
     * @param expression The string representation of the boolean expression
     */
    public Expression(String expression) {
        this.expression = expression;
        this.variables = extractVariables(expression);
    }

    /**
     * Extracts variable names from the expression.
     * This is a simple implementation that assumes single-letter variables.
     * 
     * @param expr The expression to analyze
     * @return A set of variable names found in the expression
     */
    private Set<String> extractVariables(String expr) {
        Set<String> vars = new HashSet<>();
        for (char c : expr.toCharArray()) {
            if (Character.isLetter(c) && Character.isLowerCase(c)) {
                vars.add(String.valueOf(c));
            }
        }
        return vars;
    }

    /**
     * Gets the string representation of this expression.
     * 
     * @return The expression string
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Gets the set of variables used in this expression.
     * 
     * @return The set of variable names
     */
    public Set<String> getVariables() {
        return new HashSet<>(variables);
    }

    /**
     * Evaluates the expression with the given variable assignments.
     * 
     * @param assignment A map from variable names to boolean values
     * @return The boolean result of evaluating the expression
     */
    public boolean evaluate(Map<String, Boolean> assignment) {
        // This is a simplified implementation.
        // In a real-world scenario, this would involve parsing and evaluating
        // the expression based on the provided variable assignments.
        
        // For demonstration purposes, we'll use a simple approach based on the expression:
        // If the expression contains "AND" or "∧", we'll perform AND operation on all variables
        // If the expression contains "OR" or "∨", we'll perform OR operation on all variables
        // If the expression contains "NOT" or "¬", we'll negate the result
        
        boolean result;
        
        if (expression.contains("AND") || expression.contains("∧")) {
            result = true;
            for (String var : variables) {
                if (assignment.containsKey(var)) {
                    result &= assignment.get(var);
                }
            }
        } else if (expression.contains("OR") || expression.contains("∨")) {
            result = false;
            for (String var : variables) {
                if (assignment.containsKey(var)) {
                    result |= assignment.get(var);
                }
            }
        } else {
            // Default to the first variable's value
            String firstVar = variables.iterator().next();
            result = assignment.getOrDefault(firstVar, false);
        }
        
        // Apply NOT if needed
        if (expression.contains("NOT") || expression.contains("¬")) {
            result = !result;
        }
        
        return result;
    }

    /**
     * Generates all possible variable assignments for the variables in this expression.
     * 
     * @return A list of all possible assignments
     */
    public List<Map<String, Boolean>> getAllAssignments() {
        List<Map<String, Boolean>> allAssignments = new ArrayList<>();
        int numVars = variables.size();
        int numRows = (int) Math.pow(2, numVars);
        
        List<String> varList = new ArrayList<>(variables);
        
        for (int i = 0; i < numRows; i++) {
            Map<String, Boolean> assignment = new HashMap<>();
            for (int j = 0; j < numVars; j++) {
                boolean value = (i & (1 << (numVars - 1 - j))) != 0;
                assignment.put(varList.get(j), value);
            }
            allAssignments.add(assignment);
        }
        
        return allAssignments;
    }

    @Override
    public String toString() {
        return expression;
    }
} 