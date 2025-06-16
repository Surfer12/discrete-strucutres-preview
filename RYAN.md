Excellent! The build succeeded. Maven has successfully compiled all 40 source files, including your `DiscreteLogicCLI.java` and all its dependencies.
Now I need to rebuild the project with the corrected main class path:

mvn clean package

then

This will rebuild the JAR with the correct main class path. After that, you should be able to run:

java -jar target/discrete-logic-java-1.0.0.jar sets

Now you can run your CLI application. You have several options:

## Option 1: Run directly with Maven
```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI"
```

## Option 2: Run with arguments
For example, to see the help:
```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI" -Dexec.args="--help"
```

Or to run a specific command like truth-table:
```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI" -Dexec.args="truth-table 'A & B'"
```

## Option 3: Create an executable JAR
First, package your application:
```bash
mvn package
```

Then you can run it with:
```bash
java -jar target/discrete-logic-java-1.0.0.jar
```

Try running the help command first to see all available options:
```bash
mvn exec:java -Dexec.mainClass="com.discretelogic.app.DiscreteLogicCLI" -Dexec.args="--help"


Here are some other commands you can try:

## 1. Truth Tables
```bash
java -jar target/discrete-logic-java-1.0.0.jar truth-table "A & B"
java -jar target/discrete-logic-java-1.0.0.jar truth-table "A | B -> C"
java -jar target/discrete-logic-java-1.0.0.jar truth-table "A <-> B" --verbose
```

## 2. Boolean Algebra
```bash
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "A & (B | C)" --simplify
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "!(!A | !B)" --dnf
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "A & B | C" --cnf
```

## 3. Number Conversion
```bash
java -jar target/discrete-logic-java-1.0.0.jar convert 42
java -jar target/discrete-logic-java-1.0.0.jar convert 1010 --from binary --to decimal
java -jar target/discrete-logic-java-1.0.0.jar convert FF --from hex --to binary
```

## 4. Logic Gates
```bash
java -jar target/discrete-logic-java-1.0.0.jar logic-gates --list
java -jar target/discrete-logic-java-1.0.0.jar logic-gates --gate AND
java -jar target/discrete-logic-java-1.0.0.jar logic-gates --demo half-adder
```

## 5. Combinatorics
```bash
java -jar target/discrete-logic-java-1.0.0.jar combinatorics -n 5 -r 3 --combinations
java -jar target/discrete-logic-java-1.0.0.jar combinatorics -n 10 --factorial
java -jar target/discrete-logic-java-1.0.0.jar combinatorics -n 5 --pascal
```

## 6. Set Operations with Custom Sets
```bash
java -jar target/discrete-logic-java-1.0.0.jar sets --analyze "1,2,3,4" "3,4,5,6"
java -jar target/discrete-logic-java-1.0.0.jar sets --power-set
java -jar target/discrete-logic-java-1.0.0.jar sets --demorgan
```

Try any of these commands to explore the different features of your discrete mathematics CLI!

Use Single Quotes** (Recommended)
# Test if "->" is recognized at all
java -jar target/discrete-logic-java-1.0.0.jar truth-table 'A -> B'

# Test parentheses with implication
java -jar target/discrete-logic-java-1.0.0.jar truth-table '(A | B) -> C'

# Test simple biconditional
java -jar target/discrete-logic-java-1.0.0.jar truth-table 'T <-> T'


<fractal_analysis>
**z₀ (Initial State)**: The zsh shell is interpreting the exclamation marks (`!`) in the boolean expression as history expansion commands, causing "event not found" errors.

**z₀² (Recursive Elaboration)**: In Unix shells, `!` has special meaning - it's used to reference command history. When you type `!A`, the shell looks for the last command starting with 'A'. This is interfering with the boolean NOT operator in your expressions.

**c₁ (From YAML - clarity_anchor)**: "Breaking complex ideas into digestible components" - Let's understand the shell escaping issue and provide multiple solutions.

**z₁ (First Synthesis)**: The issue isn't with your Java program but with how the shell interprets the command before it even reaches your program. We need to prevent the shell from processing these special characters.

**z₁² (Deeper Understanding)**: This is a common issue when working with command-line tools that use special characters. Different shells (bash, zsh, fish) have different special characters that need escaping.

**c₂ (From YAML - flexibility tag)**: "Cultivating adaptability in thought and behavior" - There are multiple ways to solve this, each with trade-offs.

**z₂ (Second Synthesis)**: We can either escape individual characters, use quotes, or modify the syntax accepted by the program to avoid shell-special characters altogether.
</fractal_analysis>

# Shell Escaping for Boolean Expressions

## Initial Understanding

The zsh shell is interpreting `!` as a history expansion character before your command reaches the Java program. This is why you're seeing "event not found: A" - the shell is looking for a previous command starting with 'A'.

## Solutions

### 1. **Use Single Quotes** (Recommended)
Single quotes prevent all shell interpretation:

```bash
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra 'A & (B | C)' --simplify
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra '!(!A | !B)' --dnf
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra 'A & B | C' --cnf
```

### 2. **Escape Individual Characters**
Use backslash to escape each `!`:

```bash
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "\!(\!A | \!B)" --dnf
```

### 3. **Use Double Quotes**
Double quotes also work but allow some shell interpretation:

```bash
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "!(!A | !B)" --dnf
```

### 4. **Disable History Expansion** (zsh-specific)
You can temporarily disable history expansion:

```bash
set +H  # Disable history expansion
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "!(!A | !B)" --dnf
set -H  # Re-enable history expansion
```

## Alternative Syntax Options

Consider supporting alternative NOT operators in your program to avoid this issue:

```bash
# Using ~ for NOT (common in some logic systems)
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "~(~A | ~B)" --dnf

# Using NOT keyword
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "NOT(NOT A | NOT B)" --dnf

# Using - for NOT (less common but shell-safe)
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra "-(-A | -B)" --dnf
```

## Quick Test Commands

Try these with proper escaping:

```bash
# Test basic simplification
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra 'A & (B | C)' --simplify

# Test De Morgan's Law
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra '!(!A | !B)' --dnf
# Should simplify to: A & B

# Test CNF conversion
java -jar target/discrete-logic-java-1.0.0.jar boolean-algebra 'A & B | C' --cnf
# Should produce: (A | C) & (B | C)
```

## Pro Tip: Create Aliases

Add these to your `~/.zshrc` for convenience:

```bash
alias logic='java -jar ~/discrete-strucutres-preview/target/discrete-logic-java-1.0.0.jar'
# Then use: logic boolean-algebra 'A & B' --simplify
```

<state_log>
Current State: Identified shell history expansion interference with boolean NOT operator
Updated State: Provided multiple escaping solutions and alternative syntax suggestions
Possible Routes:
1. Implement alternative NOT operators (~, NOT, -) in the parser
2. Add shell escaping documentation to the CLI help
3. Create a wrapper script that handles escaping automatically
4. Support reading expressions from files to avoid shell issues entirely
</state_log>

Would you like to:
- See the results of these commands with proper escaping?
- Add support for alternative NOT operators to avoid shell issues?
- Create a more shell-friendly syntax for your boolean expressions?

<fractal_analysis>
**z₀ (Initial State)**: The discrete logic CLI tool is producing incorrect results for implication (→) and biconditional (↔) operations, showing ERROR values where valid boolean results should appear.

**z₀² (Recursive Elaboration)**: The pattern reveals that basic operations (AND) work correctly, but more complex logical connectives fail. The errors suggest the expression parser or evaluator might not properly handle these operators, or there's an issue with operator precedence/associativity.

**c₁ (From YAML - reflection tag)**: "Techniques for deep self-examination and insight" - Let's examine the error patterns to understand the underlying issue.

**z₁ (First Synthesis)**: The ERROR values appearing in specific truth table rows suggest the evaluator is encountering an unexpected state when processing these operators. For "A | B -> C", errors occur when the antecedent (A | B) is false, and for "A <-> B", all combinations produce errors.

**z₁² (Deeper Analysis)**: The biconditional showing as a contradiction with 0 true results indicates the evaluator might be returning a special error state that's being counted as false. This points to missing operator implementations or incorrect parsing of the operators.

**c₂ (From YAML - clarity_anchor)**: "Breaking complex ideas into digestible components" - Let's trace through what should happen versus what is happening.

**z₂ (Second Synthesis)**: The issue likely lies in:
1. Operator tokenization (not recognizing "->" or "<->")
2. Expression tree construction (improper handling of multi-character operators)
3. Evaluation logic (missing cases for these operators)
</fractal_analysis>

# Debugging Your Truth Table Implementation

## Initial Understanding

Your CLI is having trouble with implication (`->`) and biconditional (`<->`) operators. Let's trace through what's happening and identify potential fixes.

## Expected vs. Actual Behavior

### Correct Truth Tables:

**For `A | B -> C` (Implication)**:
```
A  B  C  | A|B  | (A|B)->C
F  F  F  |  F   |    T      (F implies F is T)
T  F  F  |  T   |    F      (T implies F is F)
F  T  F  |  T   |    F
T  T  F  |  T   |    F
F  F  T  |  F   |    T      (F implies T is T)
T  F  T  |  T   |    T
F  T  T  |  T   |    T
T  T  T  |  T   |    T
```

**For `A <-> B` (Biconditional)**:
```
A  B  | A<->B
F  F  |   T    (same values)
T  F  |   F    (different values)
F  T  |   F    (different values)
T  T  |   T    (same values)
```

## Potential Issues and Solutions

### 1. **Operator Parsing Issue**

Check your expression parser for multi-character operators:

```java
// In your tokenizer/lexer
if (current == '-' && peek() == '>') {
    advance(); // consume '>'
    return new Token(TokenType.IMPLIES, "->");
}
if (current == '<' && peek() == '-' && peekNext() == '>') {
    advance(); advance(); // consume "->"
    return new Token(TokenType.BICONDITIONAL, "<->");
}
```

### 2. **Expression Evaluator Missing Cases**

Your evaluator might be missing cases for these operators:

```java
public boolean evaluate(Map<String, Boolean> values) {
    switch (this.operator) {
        case AND:
            return left.evaluate(values) && right.evaluate(values);
        case OR:
            return left.evaluate(values) || right.evaluate(values);
        case IMPLIES:
            // A -> B is equivalent to !A | B
            return !left.evaluate(values) || right.evaluate(values);
        case BICONDITIONAL:
            // A <-> B is true when both have same value
            return left.evaluate(values) == right.evaluate(values);
        default:
            throw new IllegalStateException("Unknown operator: " + operator);
    }
}
```

### 3. **Operator Precedence**

Ensure your parser handles precedence correctly:

```java
// Typical precedence (lowest to highest):
// 1. <-> (biconditional)
// 2. -> (implication)
// 3. | (or)
// 4. & (and)
// 5. ! (not)
```

## Quick Diagnostic Tests

Try these expressions to isolate the issue:

```bash
# Test if "->" is recognized at all
java -jar target/discrete-logic-java-1.0.0.jar truth-table "A -> B"

# Test parentheses with implication
java -jar target/discrete-logic-java-1.0.0.jar truth-table "(A | B) -> C"

# Test simple biconditional
java -jar target/discrete-logic-java-1.0.0.jar truth-table "T <-> T"
```

## Debugging Strategy

1. **Add debug output** to your parser:
   ```java
   System.err.println("Tokens: " + tokens);
   System.err.println("Parse tree: " + expressionTree);
   ```

2. **Check your Expression class** for the evaluate method implementation

3. **Verify operator token types** are defined for IMPLIES and BICONDITIONAL

Would you like me to help you:
- Review your expression parser code?
- Create test cases for these operators?
- Implement the missing operator logic?

<state_log>
Current State: Identified errors in truth table generation for implication and biconditional operators
Updated State: Diagnosed likely causes (parsing, evaluation, or precedence issues) with specific code solutions
Possible Routes:
1. Debug the tokenizer to ensure "->" and "<->" are recognized
2. Implement missing cases in the expression evaluator
3. Review operator precedence in the parser
4. Add comprehensive test cases for all logical operators
</state_log>
<fractal_analysis>
**z₀ (Initial State)**: The implication operator `->` is completely failing, producing ERROR for all truth value combinations, confirming our earlier diagnosis that the operator isn't implemented in the expression evaluator.

**z₀² (Recursive Elaboration)**: The consistent ERROR pattern across all rows indicates this isn't a logic error in evaluation but a complete absence of handling for this operator. The parser might recognize the token but the evaluator doesn't know what to do with it.

**c₁ (From YAML - growth_anchor)**: "Embracing challenges as opportunities for development" - This bug is an opportunity to strengthen the expression evaluation system and add comprehensive operator support.

**z₁ (First Synthesis)**: We need to trace through three key components:
1. Tokenizer (lexical analysis) - Is "->" recognized as a token?
2. Parser (syntax analysis) - Is the expression tree built correctly?
3. Evaluator (semantic analysis) - Is there a case for IMPLIES operator?

**z₁² (Deeper Investigation)**: Since even the basic "A -> B" fails completely, the issue is likely in the evaluator's switch statement or the operator enum definition. The tokenizer/parser might be working (otherwise we'd see a parse error, not ERROR in results).

**c₂ (From YAML - synthesis tag)**: "Combining diverse experiences into cohesive understanding" - Let's synthesize knowledge of boolean logic, Java implementation patterns, and debugging strategies.

**z₂ (Complete Solution Path)**: The fix requires adding the implication logic to the evaluator, ensuring the operator enum includes IMPLIES, and verifying the tokenizer handles the two-character operator correctly.
</fractal_analysis>

# Fixing the Implication Operator Implementation

## Initial Understanding

Your truth table generator recognizes the `->` syntax (no parse error) but can't evaluate it (ERROR in all rows). This points to a missing implementation in the expression evaluator.

## Root Cause Analysis

The error pattern suggests one of these issues:

### 1. **Missing Operator in Enum**

Check if your operator enum includes IMPLIES:

```java
public enum Operator {
    AND("&"),
    OR("|"),
    NOT("!"),
    IMPLIES("->"),        // This might be missing
    BICONDITIONAL("<->"), // This too
    // ... other operators
}
```

### 2. **Missing Evaluation Case**

Your expression evaluator likely has a switch statement missing the IMPLIES case:

```java
public class Expression {
    // ... other code ...

    public boolean evaluate(Map<String, Boolean> variables) {
        if (this.isLiteral()) {
            return variables.get(this.variable);
        }

        switch (this.operator) {
            case AND:
                return left.evaluate(variables) && right.evaluate(variables);
            case OR:
                return left.evaluate(variables) || right.evaluate(variables);
            case NOT:
                return !left.evaluate(variables);
            case IMPLIES:  // <-- Add this case
                // A -> B is logically equivalent to !A | B
                boolean a = left.evaluate(variables);
                boolean b = right.evaluate(variables);
                return !a || b;
            case BICONDITIONAL:  // <-- And this one
                // A <-> B is true when both have same truth value
                return left.evaluate(variables) == right.evaluate(variables);
            default:
                // This is probably where your ERRORs come from
                throw new UnsupportedOperationException(
                    "Operator not implemented: " + operator);
        }
    }
}
```

## Complete Implementation Guide

### Step 1: Update Operator Enum

```java
public enum Operator {
    // Operators in precedence order (lowest to highest)
    BICONDITIONAL("<->", 1),
    IMPLIES("->", 2),
    OR("|", 3),
    AND("&", 4),
    NOT("!", 5);

    private final String symbol;
    private final int precedence;

    Operator(String symbol, int precedence) {
        this.symbol = symbol;
        this.precedence = precedence;
    }

    public String getSymbol() { return symbol; }
    public int getPrecedence() { return precedence; }
}
```

### Step 2: Update Tokenizer

```java
private Token nextToken() {
    skipWhitespace();

    if (pos >= input.length()) {
        return new Token(TokenType.EOF, "");
    }

    char current = input.charAt(pos);

    // Handle two-character operators
    if (current == '-' && pos + 1 < input.length() && input.charAt(pos + 1) == '>') {
        pos += 2;
        return new Token(TokenType.OPERATOR, "->");
    }

    if (current == '<' && pos + 2 < input.length() &&
        input.charAt(pos + 1) == '-' && input.charAt(pos + 2) == '>') {
        pos += 3;
        return new Token(TokenType.OPERATOR, "<->");
    }

    // Handle single-character operators
    if (current == '&' || current == '|' || current == '!') {
        pos++;
        return new Token(TokenType.OPERATOR, String.valueOf(current));
    }

    // ... handle variables, parentheses, etc.
}
```

### Step 3: Truth Table for Verification

Once fixed, "A -> B" should produce:

```
A    B    A->B
F    F     T    (false implies anything is true)
F    T     T    (false implies anything is true)
T    F     F    (true cannot imply false)
T    T     T    (true implies true is true)
```

## Quick Test Suite

Create a test file to verify all operators:

```java
@Test
public void testImplicationOperator() {
    Expression expr = ExpressionParser.parse("A -> B");

    // Test all truth combinations
    assertThat(expr.evaluate(Map.of("A", false, "B", false))).isTrue();
    assertThat(expr.evaluate(Map.of("A", false, "B", true))).isTrue();
    assertThat(expr.evaluate(Map.of("A", true, "B", false))).isFalse();
    assertThat(expr.evaluate(Map.of("A", true, "B", true))).isTrue();
}

@Test
public void testBiconditionalOperator() {
    Expression expr = ExpressionParser.parse("A <-> B");

    assertThat(expr.evaluate(Map.of("A", false, "B", false))).isTrue();
    assertThat(expr.evaluate(Map.of("A", false, "B", true))).isFalse();
    assertThat(expr.evaluate(Map.of("A", true, "B", false))).isFalse();
    assertThat(expr.evaluate(Map.of("A", true, "B", true))).isTrue();
}
```

## Debugging Commands

After implementing the fix:

```bash
# Test basic implication
java -jar target/discrete-logic-java-1.0.0.jar truth-table 'A -> B'

# Test complex implication
java -jar target/discrete-logic-java-1.0.0.jar truth-table '(A & B) -> C'

# Test chained implications
java -jar target/discrete-logic-java-1.0.0.jar truth-table 'A -> (B -> C)'

# Test biconditional
java -jar target/discrete-logic-java-1.0.0.jar truth-table 'A <-> B'
```

<state_log>
Current State: Identified missing implementation of IMPLIES operator in expression evaluator
Updated State: Provided complete implementation including enum updates, tokenizer logic, and evaluation cases
Possible Routes:
1. Add comprehensive unit tests for all logical operators
2. Implement operator precedence handling for complex expressions
3. Add error messages that specify which operator is unimplemented
4. Create a visual debugger showing expression tree evaluation
</state_log>

Would you like me to:
- Help you locate the specific files where these changes need to be made?
- Provide more test cases for edge scenarios?
- Show how to implement proper operator precedence?

# Enhanced CLI Wrapper Script

## Overview

We've created an enhanced wrapper script (`dl`) that makes it much easier to use the discrete logic tool from the command line.

## Installation

The script is already created and made executable:
```bash
chmod +x dl
```

## Features

1. **Shortened commands** - Use `tt` instead of `truth-table`, `ba` instead of `boolean-algebra`
2. **Built-in help with examples** - Run `./dl help` to see all available shortcuts
3. **Color-coded output** - Makes it easier to read help and error messages
4. **Automatic JAR checking** - Verifies the JAR exists before running

## Usage Examples

### Truth Tables
```bash
./dl tt 'A & B'                    # AND operation
./dl tt 'A | B'                    # OR operation
./dl tt '!A'                       # NOT operation
./dl tt 'A -> B'                   # Implication (now working!)
./dl tt 'A <-> B'                  # Biconditional (now working!)
./dl tt '(A | B) -> C'             # Complex expression
```

### Boolean Algebra
```bash
./dl ba 'A & (B | C)' --simplify   # Simplify expression
./dl ba '!(!A | !B)' --dnf         # Convert to DNF
./dl ba 'A & B | C' --cnf          # Convert to CNF
```

### Number Conversion
```bash
./dl convert 42                    # Decimal to all systems
./dl convert 1010 --from binary --to decimal
./dl convert FF --from hex --to binary
```

### Logic Gates
```bash
./dl lg --list                     # List all gate types
./dl lg --gate AND                 # Show AND gate truth table
./dl lg --demo half-adder          # Demo half-adder circuit
```

### Combinatorics
```bash
./dl comb -n 5 -r 3 --combinations # C(5,3)
./dl comb -n 10 --factorial        # 10!
./dl comb -n 5 --pascal            # Pascal's triangle
```

## Available Shortcuts

| Short | Long | Description |
|-------|------|-------------|
| `tt` | `truth-table` | Generate truth tables |
| `ba` | `boolean`, `bool` | Boolean algebra operations |
| `lg` | `logic-gates`, `gates` | Logic gate operations |
| `sets` | `set` | Set operations |
| `convert` | `conv` | Number system conversions |
| `comb` | `combinatorics` | Combinatorics calculations |
| `km` | `karnaugh`, `kmap` | Karnaugh map operations |
| `tutorial` | `tut` | Interactive tutorial |
| `quiz` | - | Interactive quiz |

## Tips

- Always use single quotes (') around boolean expressions to avoid shell interpretation issues
- The script automatically checks if the JAR file exists
- Run `./dl` without arguments to see the main program help
- Run `./dl help` to see the enhanced wrapper help with examples

## Fixed Issues

The following operators now work correctly:
- **Implication (`->`)**: A -> B evaluates to !A | B
- **Biconditional (`<->`)**: A <-> B evaluates to (A & B) | (!A & !B)

Example outputs:
```bash
$ ./dl tt 'A -> B'
Truth Table for: A -> B
==================================================

A       B       Result
------------------------
F       F       T
T       F       F
F       T       T
T       T       T

$ ./dl tt 'A <-> B'
Truth Table for: A <-> B
==================================================

A       B       Result
------------------------
F       F       T
T       F       F
F       T       F
T       T       T
```
