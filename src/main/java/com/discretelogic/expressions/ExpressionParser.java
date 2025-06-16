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

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("[A-Za-z][A-Za-z0-9]*");
    
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
            if (!var.equalsIgnoreCase("AND") && 
                !var.equalsIgnoreCase("OR") && 
                !var.equalsIgnoreCase("NOT") && 
                !var.equalsIgnoreCase("XOR") && 
                !variables.contains(var)) {
                variables.add(var);
            }
        }
        
        return variables;
    }
    
    /**
     * Evaluates a boolean expression with the given variable values.
     *
     * @param expression the boolean expression to evaluate
     * @param variableValues map of variable names to their boolean values
     * @return the result of evaluating the expression
     * @throws IllegalArgumentException if the expression is invalid or variables are missing
     */
    public static boolean evaluate(String expression, Map<String, Boolean> variableValues) {
        // Convert to postfix notation
        List<String> postfix = convertToPostfix(expression);
        
        // Evaluate the postfix expression
        Stack<Boolean> stack = new Stack<>();
        
        for (String token : postfix) {
            if (isOperator(token)) {
                if (token.equals("NOT")) {
                    if (stack.isEmpty()) {
                        throw new IllegalArgumentException("Invalid expression: missing operand for NOT");
                    }
                    boolean operand = stack.pop();
                    stack.push(!operand);
                } else {
                    if (stack.size() < 2) {
                        throw new IllegalArgumentException("Invalid expression: insufficient operands");
                    }
                    boolean right = stack.pop();
                    boolean left = stack.pop();
                    
                    switch (token) {
                        case "AND":
                            stack.push(left && right);
                            break;
                        case "OR":
                            stack.push(left || right);
                            break;
                        case "XOR":
                            stack.push(left ^ right);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown operator: " + token);
                    }
                }
            } else {
                // Handle variables and constants
                if (token.equalsIgnoreCase("true")) {
                    stack.push(true);
                } else if (token.equalsIgnoreCase("false")) {
                    stack.push(false);
                } else {
                    if (!variableValues.containsKey(token)) {
                        throw new IllegalArgumentException("Missing value for variable: " + token);
                    }
                    stack.push(variableValues.get(token));
                }
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: improper number of operands");
        }
        
        return stack.pop();
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
                while (!operators.isEmpty() && !operators.peek().equals("(") &&
                       getPrecedence(operators.peek()) >= getPrecedence(token)) {
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
                    throw new IllegalArgumentException("Mismatched parentheses");
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
                if (current.equalsIgnoreCase("AND") || 
                    current.equalsIgnoreCase("OR") || 
                    current.equalsIgnoreCase("NOT") || 
                    current.equalsIgnoreCase("XOR")) {
                    
                    // Only if followed by a non-alphanumeric character or end of string
                    if (i == expression.length() - 1 || 
                        !Character.isLetterOrDigit(expression.charAt(i + 1))) {
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
        return token.equals("AND") || token.equals("OR") || 
               token.equals("NOT") || token.equals("XOR");
    }
    
    /**
     * Gets the precedence of an operator.
     *
     * @param operator the operator
     * @return the precedence value (higher is more precedent)
     */
    private static int getPrecedence(String operator) {
        switch (operator) {
            case "NOT": return 3;
            case "AND": return 2;
            case "XOR": return 1;
            case "OR": return 0;
            default: return -1;
        }
    }
    
    /**
     * Generates all possible combinations of variable values.
     *
     * @param variables the list of variables
     * @return a list of maps, each representing one combination of values
     */
    public static List<Map<String, Boolean>> generateTruthTable(List<String> variables) {
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
    public static com.discretelogic.model.Expression parseExpression(String expression) {
        // Extract variables
        List<String> variables = extractVariables(expression);
        
        // Create and return an Expression object
        return new com.discretelogic.model.Expression(expression, variables);
    }
} 