package com.discretelogic.core;

import com.discretelogic.model.Expression;
import com.discretelogic.util.TableFormatter;
import java.util.*;

/**
 * Generates and displays truth tables for boolean expressions.
 */
public class TruthTable {
    private final Expression expression;
    private final List<String> variables;
    private final List<Map<String, Boolean>> assignments;
    private final List<Boolean> results;

    public TruthTable(Expression expression) {
        this.expression = expression;
        this.variables = new ArrayList<>(expression.getVariables());
        Collections.sort(this.variables);
        this.assignments = generateAssignments();
        this.results = calculateResults();
    }

    /**
     * Generates all possible truth value assignments for the variables.
     */
    private List<Map<String, Boolean>> generateAssignments() {
        List<Map<String, Boolean>> allAssignments = new ArrayList<>();
        int numVars = variables.size();
        int numRows = (int) Math.pow(2, numVars);

        for (int i = 0; i < numRows; i++) {
            Map<String, Boolean> assignment = new HashMap<>();
            for (int j = 0; j < numVars; j++) {
                boolean value = (i & (1 << (numVars - 1 - j))) != 0;
                assignment.put(variables.get(j), value);
            }
            allAssignments.add(assignment);
        }

        return allAssignments;
    }

    /**
     * Calculates the expression results for all assignments.
     */
    private List<Boolean> calculateResults() {
        List<Boolean> calculatedResults = new ArrayList<>();
        for (Map<String, Boolean> assignment : assignments) {
            calculatedResults.add(expression.evaluate(assignment));
        }
        return calculatedResults;
    }

    /**
     * Generates a formatted truth table string.
     */
    public String generateTable() {
        List<String> headers = new ArrayList<>(variables);
        headers.add(expression.getExpression());

        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++) {
            List<String> row = new ArrayList<>();
            Map<String, Boolean> assignment = assignments.get(i);

            for (String var : variables) {
                row.add(booleanToString(assignment.get(var)));
            }
            row.add(booleanToString(results.get(i)));
            rows.add(row);
        }

        return TableFormatter.formatTable(headers, rows);
    }

    /**
     * Generates a compact binary truth table.
     */
    public String generateBinaryTable() {
        StringBuilder sb = new StringBuilder();

        // Header
        for (String var : variables) {
            sb.append(var).append(" ");
        }
        sb.append("| ").append(expression.getExpression()).append("\n");

        // Separator
        sb.append("-".repeat(variables.size() * 2 + expression.getExpression().length() + 3)).append("\n");

        // Rows
        for (int i = 0; i < assignments.size(); i++) {
            Map<String, Boolean> assignment = assignments.get(i);

            for (String var : variables) {
                sb.append(assignment.get(var) ? "1" : "0").append(" ");
            }
            sb.append("| ").append(results.get(i) ? "1" : "0").append("\n");
        }

        return sb.toString();
    }

    /**
     * Gets the minterms (rows where the expression is true).
     */
    public List<Integer> getMinterms() {
        List<Integer> minterms = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i)) {
                minterms.add(i);
            }
        }
        return minterms;
    }

    /**
     * Gets the maxterms (rows where the expression is false).
     */
    public List<Integer> getMaxterms() {
        List<Integer> maxterms = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            if (!results.get(i)) {
                maxterms.add(i);
            }
        }
        return maxterms;
    }

    /**
     * Analyzes the truth table and provides insights.
     */
    public String analyze() {
        StringBuilder analysis = new StringBuilder();

        int trueCount = (int) results.stream().mapToInt(b -> b ? 1 : 0).sum();
        int totalRows = results.size();

        analysis.append("Truth Table Analysis:\n");
        analysis.append("=====================\n");
        analysis.append("Expression: ").append(expression.getExpression()).append("\n");
        analysis.append("Variables: ").append(String.join(", ", variables)).append("\n");
        analysis.append("Total rows: ").append(totalRows).append("\n");
        analysis.append("True rows: ").append(trueCount).append("\n");
        analysis.append("False rows: ").append(totalRows - trueCount).append("\n");

        if (trueCount == totalRows) {
            analysis.append("Classification: TAUTOLOGY (always true)\n");
        } else if (trueCount == 0) {
            analysis.append("Classification: CONTRADICTION (always false)\n");
        } else {
            analysis.append("Classification: CONTINGENCY (sometimes true, sometimes false)\n");
        }

        analysis.append("Minterms: ").append(getMinterms()).append("\n");
        analysis.append("Maxterms: ").append(getMaxterms()).append("\n");

        return analysis.toString();
    }

    /**
     * Converts boolean to string representation.
     */
    private String booleanToString(boolean value) {
        return value ? "T" : "F";
    }

    public Expression getExpression() {
        return expression;
    }

    public List<String> getVariables() {
        return new ArrayList<>(variables);
    }

    public List<Map<String, Boolean>> getAssignments() {
        return new ArrayList<>(assignments);
    }

    public List<Boolean> getResults() {
        return new ArrayList<>(results);
    }
}