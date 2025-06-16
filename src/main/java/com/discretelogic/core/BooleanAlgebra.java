package com.discretelogic.core;

import com.discretelogic.model.Expression;
import java.util.*;

/**
 * Provides operations for boolean algebra transformations and analysis.
 * This class includes methods for simplifying boolean expressions,
 * converting to normal forms, and analyzing expression properties.
 */
public class BooleanAlgebra {
    
    /**
     * Simplifies a boolean expression.
     * 
     * @param expression The expression to simplify
     * @return A simplified version of the expression
     */
    public static String simplify(String expression) {
        // This is a placeholder implementation
        // In a real implementation, this would use algebraic rules to simplify
        
        // For demonstration, we'll make a trivial change
        return expression.replace(" ", "");
    }
    
    /**
     * Converts an expression to Disjunctive Normal Form (DNF).
     * 
     * @param expr The expression to convert
     * @return A string representation of the DNF expression
     */
    public static String toDNF(Expression expr) {
        // This is a placeholder implementation
        // In a real implementation, this would perform DNF conversion
        
        // For demonstration, we'll add "in DNF" to the expression
        return expr.getExpressionString() + " in DNF";
    }
    
    /**
     * Converts an expression to Conjunctive Normal Form (CNF).
     * 
     * @param expr The expression to convert
     * @return A string representation of the CNF expression
     */
    public static String toCNF(Expression expr) {
        // This is a placeholder implementation
        // In a real implementation, this would perform CNF conversion
        
        // For demonstration, we'll add "in CNF" to the expression
        return expr.getExpressionString() + " in CNF";
    }
    
    /**
     * Checks if an expression is a tautology (always true).
     * 
     * @param expr The expression to check
     * @return true if the expression is a tautology, false otherwise
     */
    public static boolean isTautology(Expression expr) {
        // A tautology is true for all possible variable assignments
        for (Map<String, Boolean> assignment : expr.getAllAssignments()) {
            if (!expr.evaluate(assignment)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if an expression is a contradiction (always false).
     * 
     * @param expr The expression to check
     * @return true if the expression is a contradiction, false otherwise
     */
    public static boolean isContradiction(Expression expr) {
        // A contradiction is false for all possible variable assignments
        for (Map<String, Boolean> assignment : expr.getAllAssignments()) {
            if (expr.evaluate(assignment)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if an expression is satisfiable (true for at least one assignment).
     * 
     * @param expr The expression to check
     * @return true if the expression is satisfiable, false otherwise
     */
    public static boolean isSatisfiable(Expression expr) {
        // An expression is satisfiable if it's not a contradiction
        return !isContradiction(expr);
    }
    
    /**
     * Checks if two expressions are equivalent.
     * 
     * @param expr1 The first expression
     * @param expr2 The second expression
     * @return true if the expressions are equivalent, false otherwise
     */
    public static boolean areEquivalent(Expression expr1, Expression expr2) {
        // Two expressions are equivalent if they evaluate to the same value
        // for all possible variable assignments
        
        // Combine variables from both expressions
        Set<String> allVars = new HashSet<>();
        allVars.addAll(expr1.getVariables());
        allVars.addAll(expr2.getVariables());
        
        // Create all possible assignments for the combined variables
        List<Map<String, Boolean>> allAssignments = new ArrayList<>();
        generateAssignments(new HashMap<>(), new ArrayList<>(allVars), 0, allAssignments);
        
        // Check if both expressions evaluate to the same value for all assignments
        for (Map<String, Boolean> assignment : allAssignments) {
            if (expr1.evaluate(assignment) != expr2.evaluate(assignment)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Recursively generates all possible variable assignments.
     */
    private static void generateAssignments(Map<String, Boolean> current, List<String> vars, 
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
} 