package com.discretelogic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import com.discretelogic.expressions.ExpressionParser;

/**
 * Represents a boolean expression with variables and operations.
 */
public class Expression {
    private final String expressionString;
    private final Set<String> variables;
    
    /**
     * Creates a new Expression.
     *
     * @param expressionString the string representation of the expression
     * @param variables the variables used in the expression
     */
    public Expression(String expressionString, List<String> variables) {
        this.expressionString = expressionString;
        this.variables = new HashSet<>(variables);
    }
    
    /**
     * Gets the string representation of the expression.
     *
     * @return the expression string
     */
    public String getExpressionString() {
        return expressionString;
    }
    
    /**
     * Gets the expression string (alias for getExpressionString).
     *
     * @return the expression string
     */
    public String getExpression() {
        return expressionString;
    }
    
    /**
     * Gets the variables used in the expression.
     *
     * @return list of variable names
     */
    public List<String> getVariables() {
        return new ArrayList<>(variables);
    }
    
    /**
     * Evaluates the expression with the given variable assignments.
     *
     * @param assignment map of variable names to their boolean values
     * @return the result of evaluating the expression
     * @throws IllegalArgumentException if the expression is invalid or variables are missing
     */
    public boolean evaluate(Map<String, Boolean> assignment) {
        return ExpressionParser.evaluate(expressionString, assignment);
    }
    
    /**
     * Generates all possible truth value assignments for the variables in this expression.
     *
     * @return a list of maps, each representing one combination of variable values
     */
    public List<Map<String, Boolean>> getAllAssignments() {
        List<String> variableList = new ArrayList<>(variables);
        int rows = 1 << variableList.size(); // 2^n combinations
        List<Map<String, Boolean>> assignments = new ArrayList<>(rows);
        
        for (int i = 0; i < rows; i++) {
            Map<String, Boolean> assignment = new HashMap<>();
            
            for (int j = 0; j < variableList.size(); j++) {
                // Check if the jth bit is set in i
                boolean value = ((i >> j) & 1) == 1;
                assignment.put(variableList.get(j), value);
            }
            
            assignments.add(assignment);
        }
        
        return assignments;
    }
    
    @Override
    public String toString() {
        return expressionString;
    }
}
