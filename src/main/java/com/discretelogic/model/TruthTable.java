package com.discretelogic.model;

import com.discretelogic.expressions.ExpressionParser;
import java.util.List;
import java.util.Map;

/**
 * Represents a truth table for a boolean expression.
 */
public class TruthTable {
    private final Expression expression;
    private final List<String> variables;
    private final List<Map<String, Boolean>> truthTable;
    
    /**
     * Creates a truth table for the given expression.
     *
     * @param expression the boolean expression
     */
    public TruthTable(Expression expression) {
        this.expression = expression;
        this.variables = expression.getVariables();
        this.truthTable = ExpressionParser.generateTruthTable(variables);
    }
    
    /**
     * Generates a formatted truth table.
     *
     * @return formatted truth table string
     */
    public String generateTable() {
        StringBuilder sb = new StringBuilder();
        
        // Header
        for (String var : variables) {
            sb.append(String.format("%-8s", var));
        }
        sb.append(String.format("%-8s", "Result"));
        sb.append("\n");
        
        // Separator
        sb.append("-".repeat(variables.size() * 8 + 8)).append("\n");
        
        // Rows
        for (Map<String, Boolean> row : truthTable) {
            for (String var : variables) {
                sb.append(String.format("%-8s", row.get(var) ? "T" : "F"));
            }
            
            try {
                boolean result = ExpressionParser.evaluate(expression.getExpressionString(), row);
                sb.append(String.format("%-8s", result ? "T" : "F"));
            } catch (Exception e) {
                sb.append(String.format("%-8s", "ERROR"));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Generates a binary truth table.
     *
     * @return binary formatted truth table string
     */
    public String generateBinaryTable() {
        StringBuilder sb = new StringBuilder();
        
        // Header
        for (String var : variables) {
            sb.append(String.format("%-4s", var));
        }
        sb.append(String.format("%-4s", "Out"));
        sb.append("\n");
        
        // Separator
        sb.append("-".repeat(variables.size() * 4 + 4)).append("\n");
        
        // Rows
        for (Map<String, Boolean> row : truthTable) {
            for (String var : variables) {
                sb.append(String.format("%-4s", row.get(var) ? "1" : "0"));
            }
            
            try {
                boolean result = ExpressionParser.evaluate(expression.getExpressionString(), row);
                sb.append(String.format("%-4s", result ? "1" : "0"));
            } catch (Exception e) {
                sb.append(String.format("%-4s", "?"));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Analyzes the truth table and provides insights.
     *
     * @return analysis string
     */
    public String analyze() {
        StringBuilder sb = new StringBuilder();
        sb.append("TRUTH TABLE ANALYSIS\n");
        sb.append("====================\n\n");
        
        int trueCount = 0;
        int totalRows = truthTable.size();
        
        for (Map<String, Boolean> row : truthTable) {
            try {
                boolean result = ExpressionParser.evaluate(expression.getExpressionString(), row);
                if (result) trueCount++;
            } catch (Exception e) {
                // Skip invalid evaluations
            }
        }
        
        sb.append("Expression: ").append(expression.getExpressionString()).append("\n");
        sb.append("Variables: ").append(variables).append("\n");
        sb.append("Total rows: ").append(totalRows).append("\n");
        sb.append("True results: ").append(trueCount).append("\n");
        sb.append("False results: ").append(totalRows - trueCount).append("\n\n");
        
        if (trueCount == totalRows) {
            sb.append("Classification: TAUTOLOGY (always true)\n");
        } else if (trueCount == 0) {
            sb.append("Classification: CONTRADICTION (always false)\n");
        } else {
            sb.append("Classification: CONTINGENCY (sometimes true, sometimes false)\n");
        }
        
        return sb.toString();
    }
}
