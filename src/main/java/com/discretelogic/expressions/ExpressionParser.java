package com.discretelogic.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for boolean expressions and related operations.
 */
public class ExpressionParser {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile(
        "[A-Za-z][A-Za-z0-9]*"
    );

    /**
     * Parses a boolean expression and extracts all variables.
     *
     * @param expression the boolean expression to parse
     * @return a list of variable names found in the expression
     */
    public static List<String> extractVariables(String expression) {
        List<String> variables = new ArrayList<>();
        Matcher matcher = VARIABLE_PATTERN.matcher(expression);

        while (matcher.find()) {
            String var = matcher.group();
            // Skip common operators that might be written as words
            if (
                !var.equalsIgnoreCase("AND") &&
                !var.equalsIgnoreCase("OR") &&
                !var.equalsIgnoreCase("NOT") &&
                !var.equalsIgnoreCase("XOR") &&
                !var.equalsIgnoreCase("T") &&
                !var.equalsIgnoreCase("F") &&
                !variables.contains(var)
            ) {
                variables.add(var);
            }
        }

        return variables;
    }

    /**
     * Evaluates a boolean expression with variable assignments.
     *
     * @param expression the string representation of the expression
     * @param assignments map of variable names to their boolean values
     * @return the result of evaluating the expression
     * @throws IllegalArgumentException if the expression is invalid or variables are missing
     */
    public static boolean evaluate(
        String expression,
        Map<String, Boolean> assignments
    ) {
        // This is a simplified implementation for demonstration purposes
        // In a real-world scenario, we would implement a proper expression parser

        // For now, we'll use a simple approach to evaluate basic expressions
        try {
            // Remove all whitespace
            expression = expression.replaceAll("\\s+", "");

            // Replace all variables with their values
            for (Map.Entry<String, Boolean> entry : assignments.entrySet()) {
                expression = expression.replace(
                    entry.getKey(),
                    entry.getValue() ? "T" : "F"
                );
            }

            // Check if there are still variables left
            for (char c : expression.toCharArray()) {
                if (Character.isLowerCase(c)) {
                    throw new IllegalArgumentException(
                        "Missing assignment for variable: " + c
                    );
                }
            }

            // Simple expression evaluator for basic operations
            return evaluateExpression(expression);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Failed to evaluate expression: " + e.getMessage(),
                e
            );
        }
    }

    private static boolean evaluateExpression(String expr) {
        // Remove outer parentheses if they exist
        expr = expr.trim();
        if (expr.startsWith("(") && expr.endsWith(")")) {
            // Check if these parentheses are matching
            int depth = 0;
            boolean isOuter = true;
            for (int i = 0; i < expr.length() - 1; i++) {
                if (expr.charAt(i) == '(') depth++;
                else if (expr.charAt(i) == ')') depth--;
                if (depth == 0 && i < expr.length() - 1) {
                    isOuter = false;
                    break;
                }
            }
            if (isOuter) {
                expr = expr.substring(1, expr.length() - 1);
            }
        }

        // Handle NOT operator
        if (expr.startsWith("!")) {
            return !evaluateExpression(expr.substring(1));
        }

        // Simplest case: just "T" or "F"
        if (expr.equals("T")) return true;
        if (expr.equals("F")) return false;

        // Handle operations with proper precedence
        // Biconditional has lowest precedence
        if (expr.contains("<->")) {
            int index = findOperatorIndex(expr, "<->");
            if (index != -1) {
                String left = expr.substring(0, index);
                String right = expr.substring(index + 3);
                boolean leftVal = evaluateExpression(left);
                boolean rightVal = evaluateExpression(right);
                return leftVal == rightVal; // A <-> B is true when both have same value
            }
        }

        // Implication has second lowest precedence
        if (expr.contains("->")) {
            int index = findOperatorIndex(expr, "->");
            if (index != -1) {
                String left = expr.substring(0, index);
                String right = expr.substring(index + 2);
                boolean leftVal = evaluateExpression(left);
                boolean rightVal = evaluateExpression(right);
                return !leftVal || rightVal; // A -> B is equivalent to !A | B
            }
        }

        // OR has lower precedence than AND
        if (expr.contains("|")) {
            int index = findOperatorIndex(expr, "|");
            if (index != -1) {
                String left = expr.substring(0, index);
                String right = expr.substring(index + 1);
                return evaluateExpression(left) || evaluateExpression(right);
            }
        }

        // XOR has same precedence as OR
        if (expr.contains("^")) {
            int index = findOperatorIndex(expr, "^");
            if (index != -1) {
                String left = expr.substring(0, index);
                String right = expr.substring(index + 1);
                return evaluateExpression(left) != evaluateExpression(right);
            }
        }

        // AND has highest precedence among binary operators
        if (expr.contains("&")) {
            int index = findOperatorIndex(expr, "&");
            if (index != -1) {
                String left = expr.substring(0, index);
                String right = expr.substring(index + 1);
                return evaluateExpression(left) && evaluateExpression(right);
            }
        }

        // For any other expression, we'd need a more complex parser
        throw new IllegalArgumentException("Unsupported expression: " + expr);
    }

    /**
     * Finds the index of an operator in an expression, respecting parentheses.
     */
    private static int findOperatorIndex(String expr, String operator) {
        int depth = 0;
        for (int i = 0; i < expr.length() - operator.length() + 1; i++) {
            if (expr.charAt(i) == '(') {
                depth++;
            } else if (expr.charAt(i) == ')') {
                depth--;
            } else if (depth == 0 && expr.substring(i).startsWith(operator)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Converts an infix boolean expression to postfix notation.
     *
     * @param expression the infix expression
     * @return a list of tokens in postfix order
     */
    private static List<String> convertToPostfix(String expression) {
        List<String> tokens = tokenize(expression);
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isOperator(token)) {
                while (
                    !operators.isEmpty() &&
                    !operators.peek().equals("(") &&
                    getPrecedence(operators.peek()) >= getPrecedence(token)
                ) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (!operators.isEmpty() && operators.peek().equals("(")) {
                    operators.pop(); // Discard the opening parenthesis
                } else {
                    throw new IllegalArgumentException(
                        "Mismatched parentheses"
                    );
                }
            } else {
                output.add(token); // Variable or constant
            }
        }

        // Pop remaining operators
        while (!operators.isEmpty()) {
            if (operators.peek().equals("(")) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.add(operators.pop());
        }

        return output;
    }

    /**
     * Tokenizes an expression into a list of tokens.
     *
     * @param expression the expression to tokenize
     * @return a list of tokens
     */
    private static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
            } else if (c == '(' || c == ')') {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else {
                currentToken.append(c);

                // Check if we've completed a keyword
                String current = currentToken.toString();
                if (
                    current.equalsIgnoreCase("AND") ||
                    current.equalsIgnoreCase("OR") ||
                    current.equalsIgnoreCase("NOT") ||
                    current.equalsIgnoreCase("XOR")
                ) {
                    // Only if followed by a non-alphanumeric character or end of string
                    if (
                        i == expression.length() - 1 ||
                        !Character.isLetterOrDigit(expression.charAt(i + 1))
                    ) {
                        tokens.add(current.toUpperCase());
                        currentToken.setLength(0);
                    }
                }
            }
        }

        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }

    /**
     * Checks if a token is an operator.
     *
     * @param token the token to check
     * @return true if the token is an operator
     */
    private static boolean isOperator(String token) {
        return (
            token.equals("AND") ||
            token.equals("OR") ||
            token.equals("NOT") ||
            token.equals("XOR")
        );
    }

    /**
     * Gets the precedence of an operator.
     *
     * @param operator the operator
     * @return the precedence value (higher is more precedent)
     */
    private static int getPrecedence(String operator) {
        switch (operator) {
            case "NOT":
                return 3;
            case "AND":
                return 2;
            case "XOR":
                return 1;
            case "OR":
                return 0;
            default:
                return -1;
        }
    }

    /**
     * Generates all possible combinations of variable values.
     *
     * @param variables the list of variables
     * @return a list of maps, each representing one combination of values
     */
    public static List<Map<String, Boolean>> generateTruthTable(
        List<String> variables
    ) {
        int rows = 1 << variables.size(); // 2^n combinations
        List<Map<String, Boolean>> truthTable = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            Map<String, Boolean> row = new HashMap<>();

            for (int j = 0; j < variables.size(); j++) {
                // Check if the jth bit is set in i
                boolean value = ((i >> j) & 1) == 1;
                row.put(variables.get(j), value);
            }

            truthTable.add(row);
        }

        return truthTable;
    }

    /**
     * Parses a string expression into an Expression object.
     *
     * @param expression the boolean expression to parse
     * @return an Expression object representing the parsed expression
     */
    public static com.discretelogic.model.Expression parseExpression(
        String expression
    ) {
        // Extract variables
        List<String> variables = extractVariables(expression);

        // Create and return an Expression object
        return new com.discretelogic.model.Expression(expression, variables);
    }
}
