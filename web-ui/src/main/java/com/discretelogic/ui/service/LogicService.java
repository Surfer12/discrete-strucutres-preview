package com.discretelogic.ui.service;

import com.discretelogic.ui.config.ApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for handling discrete logic operations.
 * This service provides methods for evaluating logical expressions,
 * generating truth tables, and performing logical calculations.
 */
@Service
public class LogicService {

    @Autowired
    private ApiConfiguration apiConfiguration;

    /**
     * Evaluate a logical expression
     * @param expression The logical expression to evaluate
     * @return The result of the evaluation
     */
    public String evaluateExpression(String expression) {
        // TODO: Integrate with the existing discrete logic library
        // For now, return a placeholder result
        
        if (expression == null || expression.trim().isEmpty()) {
            return "Error: Empty expression";
        }

        // Basic validation
        if (!isValidExpression(expression)) {
            return "Error: Invalid expression format";
        }

        // Placeholder logic - replace with actual implementation
        String result = String.format("Evaluating: %s", expression);
        
        // Add some basic pattern matching for demonstration
        if (expression.toUpperCase().contains("AND")) {
            result += " (Contains AND operation)";
        }
        if (expression.toUpperCase().contains("OR")) {
            result += " (Contains OR operation)";
        }
        if (expression.toUpperCase().contains("NOT")) {
            result += " (Contains NOT operation)";
        }

        return result;
    }

    /**
     * Generate a truth table for a logical expression
     * @param expression The logical expression
     * @param variables The variables in the expression
     * @return A map representing the truth table
     */
    public Map<String, Object> generateTruthTable(String expression, List<String> variables) {
        // TODO: Integrate with the existing discrete logic library
        Map<String, Object> truthTable = new HashMap<>();
        
        if (variables == null || variables.isEmpty()) {
            variables = extractVariables(expression);
        }

        truthTable.put("expression", expression);
        truthTable.put("variables", variables);
        
        // Generate all possible combinations
        int numVariables = variables.size();
        int numRows = (int) Math.pow(2, numVariables);
        
        List<Map<String, Object>> rows = new ArrayList<>();
        
        for (int i = 0; i < numRows; i++) {
            Map<String, Object> row = new HashMap<>();
            
            // Generate binary representation for this row
            for (int j = 0; j < numVariables; j++) {
                String variable = variables.get(j);
                boolean value = ((i >> (numVariables - 1 - j)) & 1) == 1;
                row.put(variable, value);
            }
            
            // TODO: Evaluate the expression with these variable values
            // For now, just add a placeholder result
            row.put("result", evaluateExpressionWithValues(expression, row));
            
            rows.add(row);
        }
        
        truthTable.put("rows", rows);
        return truthTable;
    }

    /**
     * Extract variables from a logical expression
     * @param expression The logical expression
     * @return List of variable names
     */
    private List<String> extractVariables(String expression) {
        Set<String> variables = new HashSet<>();
        
        // Simple regex to find single letter variables
        // TODO: Improve this to handle more complex variable names
        String[] tokens = expression.split("\\s+");
        for (String token : tokens) {
            if (token.matches("[A-Z]")) {
                variables.add(token);
            }
        }
        
        List<String> sortedVariables = new ArrayList<>(variables);
        Collections.sort(sortedVariables);
        return sortedVariables;
    }

    /**
     * Evaluate an expression with specific variable values
     * @param expression The logical expression
     * @param values The variable values
     * @return The result of the evaluation
     */
    private boolean evaluateExpressionWithValues(String expression, Map<String, Object> values) {
        // TODO: Implement actual expression evaluation
        // For now, return a placeholder result based on the first variable
        if (!values.isEmpty()) {
            Object firstValue = values.values().iterator().next();
            return firstValue instanceof Boolean ? (Boolean) firstValue : false;
        }
        return false;
    }

    /**
     * Validate if an expression is properly formatted
     * @param expression The expression to validate
     * @return true if the expression is valid
     */
    private boolean isValidExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }

        // Check for balanced parentheses
        int openParens = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') openParens++;
            else if (c == ')') openParens--;
            if (openParens < 0) return false;
        }

        return openParens == 0;
    }

    /**
     * Get supported logical operators
     * @return List of supported operators
     */
    public List<String> getSupportedOperators() {
        return Arrays.asList(
            "AND", "OR", "NOT", "XOR", "NAND", "NOR", 
            "IMPLIES", "BICONDITIONAL", "IFF"
        );
    }

    /**
     * Get operator precedence
     * @return Map of operator precedence
     */
    public Map<String, Integer> getOperatorPrecedence() {
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("NOT", 4);
        precedence.put("AND", 3);
        precedence.put("NAND", 3);
        precedence.put("OR", 2);
        precedence.put("NOR", 2);
        precedence.put("XOR", 2);
        precedence.put("IMPLIES", 1);
        precedence.put("BICONDITIONAL", 1);
        precedence.put("IFF", 1);
        return precedence;
    }

    /**
     * Get examples of logical expressions
     * @return List of example expressions
     */
    public List<String> getExampleExpressions() {
        return Arrays.asList(
            "A AND B",
            "A OR B",
            "NOT A",
            "A XOR B",
            "A IMPLIES B",
            "(A AND B) OR C",
            "NOT (A OR B)",
            "A BICONDITIONAL B"
        );
    }
}