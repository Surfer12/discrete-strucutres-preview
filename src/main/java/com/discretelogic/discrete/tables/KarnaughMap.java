package com.discretelogic.core;

import com.discretelogic.model.Expression;
import java.util.*;

/**
 * Generates and simplifies Karnaugh maps for boolean expression minimization.
 */
public class KarnaughMap {
    private final int numVariables;
    private final List<String> variables;
    private final boolean[][] kmap;
    private final int rows;
    private final int cols;

    public KarnaughMap(Expression expression) {
        this.numVariables = expression.getVariables().size();
        this.variables = new ArrayList<>(expression.getVariables());
        Collections.sort(this.variables);

        if (numVariables > 4) {
            throw new IllegalArgumentException("Karnaugh maps support up to 4 variables");
        }

        // Determine K-map dimensions
        switch (numVariables) {
            case 1:
                this.rows = 1;
                this.cols = 2;
                break;
            case 2:
                this.rows = 2;
                this.cols = 2;
                break;
            case 3:
                this.rows = 2;
                this.cols = 4;
                break;
            case 4:
                this.rows = 4;
                this.cols = 4;
                break;
            default:
                this.rows = 1;
                this.cols = 1;
        }

        this.kmap = new boolean[rows][cols];
        populateKMap(expression);
    }

    /**
     * Populates the K-map with values from the truth table.
     */
    private void populateKMap(Expression expression) {
        List<Map<String, Boolean>> assignments = expression.getAllAssignments();

        for (Map<String, Boolean> assignment : assignments) {
            int[] position = getKMapPosition(assignment);
            int row = position[0];
            int col = position[1];
            kmap[row][col] = expression.evaluate(assignment);
        }
    }

    /**
     * Converts a variable assignment to K-map coordinates.
     */
    private int[] getKMapPosition(Map<String, Boolean> assignment) {
        int row = 0, col = 0;

        switch (numVariables) {
            case 1:
                col = assignment.get(variables.get(0)) ? 1 : 0;
                break;
            case 2:
                row = assignment.get(variables.get(0)) ? 1 : 0;
                col = assignment.get(variables.get(1)) ? 1 : 0;
                break;
            case 3:
                row = assignment.get(variables.get(0)) ? 1 : 0;
                // Gray code for columns: 00, 01, 11, 10
                boolean var1 = assignment.get(variables.get(1));
                boolean var2 = assignment.get(variables.get(2));
                if (!var1 && !var2) col = 0;      // 00
                else if (!var1 && var2) col = 1;  // 01
                else if (var1 && var2) col = 2;   // 11
                else col = 3;                     // 10
                break;
            case 4:
                // Gray code for rows: 00, 01, 11, 10
                boolean var0 = assignment.get(variables.get(0));
                boolean var1_4 = assignment.get(variables.get(1));
                if (!var0 && !var1_4) row = 0;      // 00
                else if (!var0 && var1_4) row = 1;  // 01
                else if (var0 && var1_4) row = 2;   // 11
                else row = 3;                       // 10

                // Gray code for columns: 00, 01, 11, 10
                boolean var2 = assignment.get(variables.get(2));
                boolean var3 = assignment.get(variables.get(3));
                if (!var2 && !var3) col = 0;      // 00
                else if (!var2 && var3) col = 1;  // 01
                else if (var2 && var3) col = 2;   // 11
                else col = 3;                     // 10
                break;
        }

        return new int[]{row, col};
    }

    /**
     * Generates a visual representation of the K-map.
     */
    public String generateKMap() {
        StringBuilder sb = new StringBuilder();
        sb.append("Karnaugh Map\n");
        sb.append("============\n\n");

        switch (numVariables) {
            case 1:
                sb.append(generateKMap1Variable());
                break;
            case 2:
                sb.append(generateKMap2Variables());
                break;
            case 3:
                sb.append(generateKMap3Variables());
                break;
            case 4:
                sb.append(generateKMap4Variables());
                break;
        }

        return sb.toString();
    }

    private String generateKMap1Variable() {
        StringBuilder sb = new StringBuilder();
        String var = variables.get(0);

        sb.append("    ").append(var).append("\n");
        sb.append("   0 1\n");
        sb.append("  +---+\n");
        sb.append("  |").append(kmap[0][0] ? "1" : "0").append("|").append(kmap[0][1] ? "1" : "0").append("|\n");
        sb.append("  +---+\n");

        return sb.toString();
    }

    private String generateKMap2Variables() {
        StringBuilder sb = new StringBuilder();
        String var1 = variables.get(0);
        String var2 = variables.get(1);

        sb.append("      ").append(var2).append("\n");
        sb.append("     0 1\n");
        sb.append("   +-----+\n");
        sb.append(" 0 |").append(kmap[0][0] ? "1" : "0").append("|").append(kmap[0][1] ? "1" : "0").append("| ").append(var1).append("\n");
        sb.append(" 1 |").append(kmap[1][0] ? "1" : "0").append("|").append(kmap[1][1] ? "1" : "0").append("|\n");
        sb.append("   +-----+\n");

        return sb.toString();
    }

    private String generateKMap3Variables() {
        StringBuilder sb = new StringBuilder();
        String var1 = variables.get(0);
        String var2 = variables.get(1);
        String var3 = variables.get(2);

        sb.append("        ").append(var2).append(var3).append("\n");
        sb.append("       00 01 11 10\n");
        sb.append("     +-----------+\n");
        sb.append("   0 |").append(kmap[0][0] ? "1" : "0").append(" |").append(kmap[0][1] ? "1" : "0").append(" |").append(kmap[0][2] ? "1" : "0").append(" |").append(kmap[0][3] ? "1" : "0").append(" | ").append(var1).append("\n");
        sb.append("   1 |").append(kmap[1][0] ? "1" : "0").append(" |").append(kmap[1][1] ? "1" : "0").append(" |").append(kmap[1][2] ? "1" : "0").append(" |").append(kmap[1][3] ? "1" : "0").append(" |\n");
        sb.append("     +-----------+\n");

        return sb.toString();
    }

    private String generateKMap4Variables() {
        StringBuilder sb = new StringBuilder();
        String var1 = variables.get(0);
        String var2 = variables.get(1);
        String var3 = variables.get(2);
        String var4 = variables.get(3);

        sb.append("          ").append(var3).append(var4).append("\n");
        sb.append("         00 01 11 10\n");
        sb.append("       +-----------+\n");
        sb.append("    00 |").append(kmap[0][0] ? "1" : "0").append(" |").append(kmap[0][1] ? "1" : "0").append(" |").append(kmap[0][2] ? "1" : "0").append(" |").append(kmap[0][3] ? "1" : "0").append(" |\n");
        sb.append("    01 |").append(kmap[1][0] ? "1" : "0").append(" |").append(kmap[1][1] ? "1" : "0").append(" |").append(kmap[1][2] ? "1" : "0").append(" |").append(kmap[1][3] ? "1" : "0").append(" | ").append(var1).append(var2).append("\n");
        sb.append("    11 |").append(kmap[2][0] ? "1" : "0").append(" |").append(kmap[2][1] ? "1" : "0").append(" |").append(kmap[2][2] ? "1" : "0").append(" |").append(kmap[2][3] ? "1" : "0").append(" |\n");
        sb.append("    10 |").append(kmap[3][0] ? "1" : "0").append(" |").append(kmap[3][1] ? "1" : "0").append(" |").append(kmap[3][2] ? "1" : "0").append(" |").append(kmap[3][3] ? "1" : "0").append(" |\n");
        sb.append("       +-----------+\n");

        return sb.toString();
    }

    /**
     * Attempts to find rectangular groups of 1s for simplification.
     */
    public String findGroups() {
        StringBuilder sb = new StringBuilder();
        sb.append("K-map Analysis\n");
        sb.append("==============\n\n");

        List<String> groups = identifyGroups();

        if (groups.isEmpty()) {
            sb.append("No simplification groups found.\n");
        } else {
            sb.append("Identified groups:\n");
            for (int i = 0; i < groups.size(); i++) {
                sb.append((i + 1)).append(". ").append(groups.get(i)).append("\n");
            }
        }

        sb.append("\nSimplified expression: ").append(getSimplifiedExpression()).append("\n");

        return sb.toString();
    }

    /**
     * Identifies rectangular groups of 1s in the K-map.
     */
    private List<String> identifyGroups() {
        List<String> groups = new ArrayList<>();

        // This is a simplified grouping algorithm
        // A complete implementation would use more sophisticated algorithms
        // like the Quine-McCluskey method

        // Find isolated 1s
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (kmap[i][j]) {
                    groups.add("Cell (" + i + "," + j + ")");
                }
            }
        }

        return groups;
    }

    /**
     * Gets a simplified boolean expression from the K-map.
     */
    private String getSimplifiedExpression() {
        // This is a placeholder for K-map simplification
        // A complete implementation would analyze the groups and generate
        // the minimal sum-of-products expression

        List<String> minterms = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (kmap[i][j]) {
                    minterms.add("m" + (i * cols + j));
                }
            }
        }

        return minterms.isEmpty() ? "0" : String.join(" + ", minterms);
    }

    /**
     * Gets the K-map truth values as a 2D array.
     */
    public boolean[][] getKMapValues() {
        boolean[][] copy = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(kmap[i], 0, copy[i], 0, cols);
        }
        return copy;
    }
}