package com.discretelogic.core;

import com.discretelogic.model.Expression;
import java.util.*;

/**
 * Parser for boolean expressions.
 * This class provides functionality to parse boolean expressions and convert them
 * to expression objects for evaluation and analysis.
 */
public class ExpressionParser {
    
    /**
     * Parses a string representation of a boolean expression into an Expression object.
     * 
     * @param expressionString The string to parse
     * @return An Expression object representing the parsed expression
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parseExpression(String expressionString) {
        // This is a placeholder implementation
        // In a real implementation, this would parse the expression using
        // appropriate parsing techniques (e.g., recursive descent parser)
        
        // For now, we'll create a simple expression that just stores the string
        Set<String> variables = extractVariables(expressionString);
        return new Expression(expressionString, variables);
    }
    
    /**
     * Extracts variable names from an expression string.
     * 
     * @param expressionString The expression string
     * @return A set of variable names found in the expression
     */
    private static Set<String> extractVariables(String expressionString) {
        Set<String> variables = new HashSet<>();
        
        // Simple algorithm to extract variables - assumes variables are single letters
        // This is a simplified implementation for demonstration purposes
        for (int i = 0; i < expressionString.length(); i++) {
            char c = expressionString.charAt(i);
            if (Character.isLetter(c) && Character.isLowerCase(c)) {
                variables.add(String.valueOf(c));
            }
        }
        
        return variables;
    }
} 