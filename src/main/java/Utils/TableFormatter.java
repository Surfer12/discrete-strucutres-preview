package com.discretelogic.util;

import java.util.List;

/**
 * Utility class for formatting tables in ASCII text.
 */
public class TableFormatter {

    /**
     * Formats a table with headers and rows.
     */
    public static String formatTable(List<String> headers, List<List<String>> rows) {
        if (headers.isEmpty()) {
            return "";
        }

        // Calculate column widths
        int[] widths = new int[headers.size()];

        // Consider header widths
        for (int i = 0; i < headers.size(); i++) {
            widths[i] = headers.get(i).length();
        }

        // Consider row widths
        for (List<String> row : rows) {
            for (int i = 0; i < Math.min(row.size(), widths.length); i++) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }

        StringBuilder sb = new StringBuilder();

        // Create header
        sb.append(createSeparatorLine(widths, "â”Œ", "â”¬", "â”", "â”€")).append("\n");
        sb.append(createRowLine(headers, widths)).append("\n");
        sb.append(createSeparatorLine(widths, "â”œ", "â”¼", "â”¤", "â”€")).append("\n");

        // Create rows
        for (List<String> row : rows) {
            sb.append(createRowLine(row, widths)).append("\n");
        }

        // Create bottom border
        sb.append(createSeparatorLine(widths, "â””", "â”´", "â”˜", "â”€"));

        return sb.toString();
    }

    /**
     * Creates a simple ASCII table without fancy borders.
     */
    public static String formatSimpleTable(List<String> headers, List<List<String>> rows) {
        if (headers.isEmpty()) {
            return "";
        }

        // Calculate column widths
        int[] widths = new int[headers.size()];

        for (int i = 0; i < headers.size(); i++) {
            widths[i] = headers.get(i).length();
        }

        for (List<String> row : rows) {
            for (int i = 0; i < Math.min(row.size(), widths.length); i++) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }

        StringBuilder sb = new StringBuilder();

        // Header
        for (int i = 0; i < headers.size(); i++) {
            if (i > 0) sb.append(" | ");
            sb.append(String.format("%-" + widths[i] + "s", headers.get(i)));
        }
        sb.append("\n");

        // Separator
        for (int i = 0; i < headers.size(); i++) {
            if (i > 0) sb.append("-+-");
            sb.append("-".repeat(widths[i]));
        }
        sb.append("\n");

        // Rows
        for (List<String> row : rows) {
            for (int i = 0; i < headers.size(); i++) {
                if (i > 0) sb.append(" | ");
                String value = i < row.size() ? row.get(i) : "";
                sb.append(String.format("%-" + widths[i] + "s", value));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private static String createSeparatorLine(int[] widths, String left, String middle, String right, String fill) {
        StringBuilder sb = new StringBuilder();
        sb.append(left);

        for (int i = 0; i < widths.length; i++) {
            if (i > 0) sb.append(middle);
            sb.append(fill.repeat(widths[i] + 2)); // +2 for padding
        }

        sb.append(right);
        return sb.toString();
    }

    private static String createRowLine(List<String> values, int[] widths) {
        StringBuilder sb = new StringBuilder();
        sb.append("â”‚");

        for (int i = 0; i < widths.length; i++) {
            if (i > 0) sb.append("â”‚");
            String value = i < values.size() ? values.get(i) : "";
            sb.append(String.format(" %-" + widths[i] + "s ", value));
        }

        sb.append("â”‚");
        return sb.toString();
    }

    /**
     * Creates a matrix-style table representation.
     */
    public static String formatMatrix(String title, String[][] matrix) {
        StringBuilder sb = new StringBuilder();

        if (title != null && !title.isEmpty()) {
            sb.append(title).append("\n");
            sb.append("=".repeat(title.length())).append("\n\n");
        }

        if (matrix.length == 0) {
            return sb.append("Empty matrix").toString();
        }

        // Find max width
        int maxWidth = 0;
        for (String[] row : matrix) {
            for (String cell : row) {
                maxWidth = Math.max(maxWidth, cell.length());
            }
        }
        maxWidth = Math.max(maxWidth, 3); // Minimum width

        // Create matrix
        for (String[] row : matrix) {
            sb.append("â”‚");
            for (String cell : row) {
                sb.append(String.format(" %" + maxWidth + "s ", cell));
            }
            sb.append("â”‚\n");
        }

        return sb.toString();
    }
}