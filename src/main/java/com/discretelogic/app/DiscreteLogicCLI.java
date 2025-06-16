package com.discretelogic.app;

import com.discretelogic.discrete.tables.KarnaughMap;
import com.discretelogic.logicgatesim.LogicGate;
import com.discretelogic.model.*;
import com.discretelogic.discrete.sets.*;
import com.discretelogic.logicgatesim.NumberSystemConverter;
import com.discretelogic.core.*;
// import com.discretelogic.educational.*; // This package seems to not exist in the new structure

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Main CLI application for discrete logic mathematics operations.
 */
@Command(
    name = "discrete-logic",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "A comprehensive Java library and CLI tool for discrete logic mathematics concepts",
    subcommands = {
        DiscreteLogicCLI.TruthTableCommand.class,
        DiscreteLogicCLI.BooleanAlgebraCommand.class,
        DiscreteLogicCLI.LogicGateCommand.class,
        DiscreteLogicCLI.SetOperationsCommand.class,
        DiscreteLogicCLI.NumberConversionCommand.class,
        DiscreteLogicCLI.CombinatoricsCommand.class,
        DiscreteLogicCLI.KarnaughMapCommand.class,
        DiscreteLogicCLI.TutorialCommand.class,
        DiscreteLogicCLI.QuizCommand.class
    }
)
public class DiscreteLogicCLI implements Callable<Integer> {

    @Spec CommandSpec spec;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new DiscreteLogicCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        spec.commandLine().usage(System.out);
        return 0;
    }

    @Command(name = "truth-table", description = "Generate truth tables for boolean expressions")
    static class TruthTableCommand implements Callable<Integer> {

        @Parameters(index = "0", description = "Boolean expression (use &, |, !, ^, ->, <->)")
        private String expression;

        @Option(names = {"-f", "--format"}, description = "Output format: table, binary, analysis", defaultValue = "table")
        private String format;

        @Option(names = {"-v", "--verbose"}, description = "Show detailed analysis")
        private boolean verbose;

        @Override
        public Integer call() {
            try {
                Expression expr = ExpressionParser.parseExpression(expression);
                TruthTable truthTable = new TruthTable(expr);

                System.out.println("Truth Table for: " + expression);
                System.out.println("=".repeat(50));
                System.out.println();

                switch (format.toLowerCase()) {
                    case "binary":
                        System.out.println(truthTable.generateBinaryTable());
                        break;
                    case "analysis":
                        System.out.println(truthTable.analyze());
                        break;
                    default:
                        System.out.println(truthTable.generateTable());
                }

                if (verbose) {
                    System.out.println();
                    System.out.println(truthTable.analyze());
                }

                return 0;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "boolean-algebra", description = "Perform boolean algebra operations")
    static class BooleanAlgebraCommand implements Callable<Integer> {

        @Parameters(index = "0", description = "Boolean expression")
        private String expression;

        @Option(names = {"-s", "--simplify"}, description = "Simplify the expression")
        private boolean simplify;

        @Option(names = {"-d", "--dnf"}, description = "Convert to Disjunctive Normal Form")
        private boolean dnf;

        @Option(names = {"-c", "--cnf"}, description = "Convert to Conjunctive Normal Form")
        private boolean cnf;

        @Option(names = {"-e", "--equivalent"}, description = "Compare with another expression")
        private String compareWith;

        @Override
        public Integer call() {
            try {
                Expression expr = ExpressionParser.parseExpression(expression);

                System.out.println("Boolean Algebra Operations");
                System.out.println("=".repeat(30));
                System.out.println("Original expression: " + expression);
                System.out.println();

                if (simplify) {
                    String simplified = BooleanAlgebra.simplify(expression);
                    System.out.println("Simplified: " + simplified);
                }

                if (dnf) {
                    String dnfForm = BooleanAlgebra.toDNF(expr);
                    System.out.println("DNF: " + dnfForm);
                }

                if (cnf) {
                    String cnfForm = BooleanAlgebra.toCNF(expr);
                    System.out.println("CNF: " + cnfForm);
                }

                System.out.println("Tautology: " + BooleanAlgebra.isTautology(expr));
                System.out.println("Contradiction: " + BooleanAlgebra.isContradiction(expr));
                System.out.println("Satisfiable: " + BooleanAlgebra.isSatisfiable(expr));

                if (compareWith != null) {
                    Expression expr2 = ExpressionParser.parseExpression(compareWith);
                    boolean equivalent = BooleanAlgebra.areEquivalent(expr, expr2);
                    System.out.println();
                    System.out.println("Equivalent to '" + compareWith + "': " + equivalent);
                }

                return 0;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "logic-gates", description = "Simulate logic gates and circuits")
    static class LogicGateCommand implements Callable<Integer> {

        @Option(names = {"-g", "--gate"}, description = "Gate type: AND, OR, NOT, NAND, NOR, XOR, XNOR")
        private String gateType;

        @Option(names = {"-l", "--list"}, description = "List all available gates")
        private boolean list;

        @Option(names = {"-d", "--demo"}, description = "Demonstration: half-adder, full-adder")
        private String demo;

        @Override
        public Integer call() {
            if (list) {
                System.out.println(LogicGate.listGateTypes());
                return 0;
            }

            if (demo != null) {
                switch (demo.toLowerCase()) {
                    case "half-adder":
                        System.out.println(LogicGate.demonstrateHalfAdder());
                        break;
                    case "full-adder":
                        System.out.println(LogicGate.demonstrateFullAdder());
                        break;
                    default:
                        System.err.println("Unknown demo: " + demo);
                        return 1;
                }
                return 0;
            }

            if (gateType != null) {
                try {
                    Gate.GateType type = Gate.GateType.valueOf(gateType.toUpperCase());
                    System.out.println(LogicGate.generateGateTruthTable(type));
                    return 0;
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid gate type: " + gateType);
                    return 1;
                }
            }

            System.out.println(LogicGate.listGateTypes());
            return 0;
        }
    }

    @Command(name = "sets", description = "Perform set theory operations")
    static class SetOperationsCommand implements Callable<Integer> {

        @Option(names = {"-d", "--demo"}, description = "Run demonstration")
        private boolean demo;

        @Option(names = {"-p", "--power-set"}, description = "Show power set demonstration")
        private boolean powerSet;

        @Option(names = {"-m", "--demorgan"}, description = "Verify De Morgan's laws")
        private boolean deMorgan;

        @Option(names = {"-a", "--analyze"}, description = "Analyze two sets", arity = "2")
        private List<String> analyzeSets;

        @Override
        public Integer call() {
            if (demo) {
                System.out.println(SetOperations.demonstrateBasicOperations());
                return 0;
            }

            if (powerSet) {
                System.out.println(SetOperations.demonstratePowerSet());
                return 0;
            }

            if (deMorgan) {
                System.out.println(SetOperations.verifyDeMorganLaws());
                return 0;
            }

            if (analyzeSets != null && analyzeSets.size() == 2) {
                // Parse sets from string representation
                LogicalSet<Integer> setA = parseIntegerSet("A", analyzeSets.get(0));
                LogicalSet<Integer> setB = parseIntegerSet("B", analyzeSets.get(1));
                System.out.println(SetOperations.analyzeSetRelationships(setA, setB));
                return 0;
            }

            System.out.println(SetOperations.demonstrateBasicOperations());
            return 0;
        }

        private LogicalSet<Integer> parseIntegerSet(String name, String setString) {
            LogicalSet<Integer> set = new LogicalSet<>(name);
            setString = setString.replaceAll("[{}\\s]", "");
            if (!setString.isEmpty()) {
                String[] elements = setString.split(",");
                for (String element : elements) {
                    try {
                        set.add(Integer.parseInt(element.trim()));
                    } catch (NumberFormatException e) {
                        // Skip invalid elements
                    }
                }
            }
            return set;
        }
    }

    @Command(name = "convert", description = "Convert between number systems")
    static class NumberConversionCommand implements Callable<Integer> {

        @Parameters(index = "0", description = "Number to convert")
        private String number;

        @Option(names = {"-f", "--from"}, description = "Source base: decimal, binary, octal, hex", defaultValue = "decimal")
        private String fromBase;

        @Option(names = {"-t", "--to"}, description = "Target base: decimal, binary, octal, hex, all", defaultValue = "all")
        private String toBase;

        @Option(names = {"-a", "--arithmetic"}, description = "Perform binary arithmetic: add, subtract, multiply", arity = "2")
        private List<String> arithmetic;

        @Option(names = {"-s", "--steps"}, description = "Show conversion steps")
        private boolean showSteps;

        @Option(names = {"--table"}, description = "Show conversion table (0-15)")
        private boolean showTable;

        @Override
        public Integer call() {
            if (showTable) {
                System.out.println(NumberSystemConverter.createConversionTable());
                return 0;
            }

            if (arithmetic != null && arithmetic.size() == 2) {
                String operation = arithmetic.get(1);
                System.out.println(NumberSystemConverter.performBinaryArithmetic(number, arithmetic.get(0), operation));
                return 0;
            }

            try {
                long decimal;

                // Convert to decimal first
                switch (fromBase.toLowerCase()) {
                    case "binary":
                        decimal = NumberSystemConverter.binaryToDecimal(number);
                        break;
                    case "octal":
                        decimal = NumberSystemConverter.octalToDecimal(number);
                        break;
                    case "hex":
                    case "hexadecimal":
                        decimal = NumberSystemConverter.hexadecimalToDecimal(number);
                        break;
                    case "decimal":
                    default:
                        decimal = Long.parseLong(number);
                        break;
                }

                if (showSteps && fromBase.equals("decimal")) {
                    System.out.println(NumberSystemConverter.showDecimalToBinarySteps(decimal));
                    System.out.println();
                }

                if (toBase.equals("all")) {
                    System.out.println(NumberSystemConverter.demonstrateConversions(decimal));
                } else {
                    String result;
                    switch (toBase.toLowerCase()) {
                        case "binary":
                            result = NumberSystemConverter.decimalToBinary(decimal);
                            break;
                        case "octal":
                            result = NumberSystemConverter.decimalToOctal(decimal);
                            break;
                        case "hex":
                        case "hexadecimal":
                            result = NumberSystemConverter.decimalToHexadecimal(decimal);
                            break;
                        case "decimal":
                        default:
                            result = String.valueOf(decimal);
                            break;
                    }
                    System.out.println(fromBase + " " + number + " = " + toBase + " " + result);
                }

                return 0;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "combinatorics", description = "Perform combinatorial calculations")
    static class CombinatoricsCommand implements Callable<Integer> {

        @Option(names = {"-n"}, description = "Value of n")
        private Integer n;

        @Option(names = {"-r"}, description = "Value of r")
        private Integer r;

        @Option(names = {"-f", "--factorial"}, description = "Calculate factorial")
        private boolean factorial;

        @Option(names = {"-p", "--permutations"}, description = "Calculate permutations P(n,r)")
        private boolean permutations;

        @Option(names = {"-c", "--combinations"}, description = "Calculate combinations C(n,r)")
        private boolean combinations;

        @Option(names = {"--pascal"}, description = "Generate Pascal's triangle")
        private boolean pascal;

        @Option(names = {"--pigeonhole"}, description = "Demonstrate pigeonhole principle")
        private boolean pigeonhole;

        @Option(names = {"--derangements"}, description = "Calculate derangements")
        private boolean derangements;

        @Option(names = {"--table"}, description = "Show combinatorics table")
        private boolean table;

        @Override
        public Integer call() {
            if (pigeonhole) {
                System.out.println(Combinatorics.demonstratePigeonholePrinciple());
                return 0;
            }

            if (table) {
                System.out.println(Combinatorics.createCombinatoricsTable(10));
                return 0;
            }

            if (n == null) {
                System.err.println("Please specify -n value");
                return 1;
            }

            if (factorial) {
                System.out.println(n + "! = " + Combinatorics.factorial(n));
            }

            if (pascal) {
                System.out.println(Combinatorics.generatePascalsTriangle(n));
            }

            if (derangements) {
                System.out.println(Combinatorics.demonstrateDerangements(n));
            }

            if (r != null) {
                if (permutations) {
                    System.out.println("P(" + n + "," + r + ") = " + Combinatorics.permutations(n, r));
                }

                if (combinations) {
                    System.out.println("C(" + n + "," + r + ") = " + Combinatorics.combinations(n, r));
                }
            }

            return 0;
        }
    }

    @Command(name = "karnaugh", description = "Generate and analyze Karnaugh maps")
    static class KarnaughMapCommand implements Callable<Integer> {

        @Parameters(index = "0", description = "Boolean expression (up to 4 variables)")
        private String expression;

        @Option(names = {"-a", "--analyze"}, description = "Analyze the K-map for groupings")
        private boolean analyze;

        @Override
        public Integer call() {
            try {
                Expression expr = ExpressionParser.parseExpression(expression);

                if (expr.getVariables().size() > 4) {
                    System.err.println("Karnaugh maps support up to 4 variables only");
                    return 1;
                }

                KarnaughMap kmap = new KarnaughMap(expr);

                System.out.println("Karnaugh Map for: " + expression);
                System.out.println("=".repeat(40));
                System.out.println();
                System.out.println(kmap.generateKMap());

                if (analyze) {
                    System.out.println();
                    System.out.println(kmap.findGroups());
                }

                return 0;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                return 1;
            }
        }
    }

    @Command(name = "tutorial", description = "Interactive tutorials on discrete mathematics")
    static class TutorialCommand implements Callable<Integer> {

        @Option(names = {"-t", "--topic"}, description = "Tutorial topic: boolean, sets, logic-gates, number-systems")
        private String topic;

        @Option(names = {"-l", "--list"}, description = "List available tutorials")
        private boolean list;

        @Override
        public Integer call() {
            if (list || topic == null) {
                System.out.println(Tutorial.listAvailableTutorials());
                return 0;
            }

            System.out.println(Tutorial.getTutorial(topic));
            return 0;
        }
    }

    @Command(name = "quiz", description = "Take quizzes on discrete mathematics topics")
    static class QuizCommand implements Callable<Integer> {

        @Option(names = {"-t", "--topic"}, description = "Quiz topic: boolean, sets, logic-gates")
        private String topic;

        @Option(names = {"-q", "--questions"}, description = "Number of questions", defaultValue = "5")
        private int questionCount;

        @Override
        public Integer call() {
            if (topic == null) {
                System.out.println("Available quiz topics: boolean, sets, logic-gates");
                return 0;
            }

            Quiz quiz = new Quiz(topic, questionCount);
            quiz.start();
            return 0;
        }
    }
}