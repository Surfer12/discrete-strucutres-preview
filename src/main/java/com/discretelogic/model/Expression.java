package com.discretelogic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
     * Gets the variables used in the expression.
     *
     * @return list of variable names
     */
    public List<String> getVariables() {
        return new ArrayList<>(variables);
    }
    
    @Override
    public String toString() {
        return expressionString;
    }
}
