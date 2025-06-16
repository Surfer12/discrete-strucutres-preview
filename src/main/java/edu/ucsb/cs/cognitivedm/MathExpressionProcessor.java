package edu.ucsb.cs.cognitivedm;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.CognitiveState;
import edu.ucsb.cs.cognitivedm.education.CognitiveEducationFramework;
import edu.ucsb.cs.cognitivedm.recommendations.CognitiveRecommendationEngine;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Mathematical Expression Processor with Cognitive Integration
 *
 * Enhances traditional mathematical expression parsing with cognitive awareness:
 * - Cognitive load assessment for mathematical expressions
 * - Attention-based expression simplification
 * - Pattern recognition in mathematical structures
 * - Adaptive complexity management
 * - Integration with legacy expression parsing systems
 */
public class MathExpressionProcessor {

    private final AttentionRecognitionFramework cognitiveFramework;
    private CognitiveEducationFramework educationFramework;
    private CognitiveRecommendationEngine recommendationEngine;

    // Expression complexity patterns
    private static final Map<String, Double> COMPLEXITY_WEIGHTS = new HashMap<>();
    private static final Map<String, String> SIMPLIFICATION_RULES = new HashMap<>();

    // Mathematical pattern recognizers
    private final List<MathPatternRecognizer> patternRecognizers;

    // Configuration
    private double complexityThreshold = 0.7;
    private boolean enableAutoSimplification = true;
    private boolean enableCognitiveAdaptation = true;

    static {
        // Initialize complexity weights for different mathematical operations
        COMPLEXITY_WEIGHTS.put("integration", 0.9);
        COMPLEXITY_WEIGHTS.put("differentiation", 0.8);
        COMPLEXITY_WEIGHTS.put("trigonometric", 0.7);
        COMPLEXITY_WEIGHTS.put("logarithmic", 0.6);
        COMPLEXITY_WEIGHTS.put("exponential", 0.6);
        COMPLEXITY_WEIGHTS.put("polynomial", 0.4);
        COMPLEXITY_WEIGHTS.put("arithmetic", 0.2);
        COMPLEXITY_WEIGHTS.put("variable", 0.1);

        // Initialize simplification rules
        SIMPLIFICATION_RULES.put("sin^2(x) + cos^2(x)", "1");
        SIMPLIFICATION_RULES.put("e^(ln(x))", "x");
        SIMPLIFICATION_RULES.put("ln(e^x)", "x");
        SIMPLIFICATION_RULES.put("x + 0", "x");
        SIMPLIFICATION_RULES.put("x * 1", "x");
        SIMPLIFICATION_RULES.put("x * 0", "0");
        SIMPLIFICATION_RULES.put("0 + x", "x");
        SIMPLIFICATION_RULES.put("1 * x", "x");
    }

    public MathExpressionProcessor(AttentionRecognitionFramework cognitiveFramework) {
        this.cognitiveFramework = cognitiveFramework;
        this.patternRecognizers = initializePatternRecognizers();
    }

    private List<MathPatternRecognizer> initializePatternRecognizers() {
        List<MathPatternRecognizer> recognizers = new ArrayList<>();

        // Algebraic patterns
        recognizers.add(new AlgebraicPatternRecognizer());

        // Trigonometric patterns
        recognizers.add(new TrigonometricPatternRecognizer());

        // Calculus patterns
        recognizers.add(new CalculusPatternRecognizer());

        // Set theory patterns
        recognizers.add(new SetTheoryPatternRecognizer());

        // Logic patterns
        recognizers.add(new LogicPatternRecognizer());

        return recognizers;
    }

    /**
     * Create a cognitive-enhanced mathematical expression
     */
    public CognitiveDiscreteMathLibrary.MathExpression createMathExpression(
            String expressionString, CognitiveState cognitiveState) {

        CognitiveDiscreteMathLibrary.MathExpression mathExpr =
            new CognitiveDiscreteMathLibrary.MathExpression(expressionString);

        // Analyze expression complexity
        double complexity = analyzeComplexity(expressionString);
        mathExpr.getMetadata().put("originalComplexity", complexity);

        // Apply cognitive processing
        if (enableCognitiveAdaptation) {
            mathExpr = applyCognitiveAdaptation(mathExpr, cognitiveState);
        }

        // Detect mathematical patterns
        List<MathPattern> patterns = detectMathematicalPatterns(expressionString);
        mathExpr.getMetadata().put("detectedPatterns", patterns);

        // Add educational metadata
        addEducationalMetadata(mathExpr, patterns);

        return mathExpr;
    }

    /**
     * Analyze the cognitive complexity of a mathematical expression
     */
    public double analyzeComplexity(String expression) {
        double totalComplexity = 0.0;
        int componentCount = 0;

        // Analyze different types of mathematical components
        for (Map.Entry<String, Double> entry : COMPLEXITY_WEIGHTS.entrySet()) {
            String component = entry.getKey();
            double weight = entry.getValue();

            int count = countOccurrences(expression, component);
            if (count > 0) {
                totalComplexity += count * weight;
                componentCount += count;
            }
        }

        // Factor in expression length and nesting depth
        double lengthFactor = Math.min(1.0, expression.length() / 50.0);
        double nestingFactor = calculateNestingDepth(expression) / 10.0;

        // Normalize complexity score
        double baseComplexity = componentCount > 0 ? totalComplexity / componentCount : 0.0;
        return Math.min(1.0, baseComplexity + lengthFactor * 0.2 + nestingFactor * 0.3);
    }

    /**
     * Apply cognitive adaptation to the mathematical expression
     */
    private CognitiveDiscreteMathLibrary.MathExpression applyCognitiveAdaptation(
            CognitiveDiscreteMathLibrary.MathExpression mathExpr, CognitiveState cognitiveState) {

        double cognitiveLoad = cognitiveState.getCognitiveLoad();
        double attention = cognitiveState.getAttention();

        // If cognitive load is high, suggest simplification
        if (cognitiveLoad > complexityThreshold) {
            String simplifiedExpression = attemptSimplification(mathExpr.getExpression());
            if (!simplifiedExpression.equals(mathExpr.getExpression())) {
                mathExpr.getMetadata().put("simplifiedExpression", simplifiedExpression);
                mathExpr.getMetadata().put("simplificationApplied", true);
            }
        }

        // If attention is low, break down into steps
        if (attention < 0.5) {
            List<String> steps = breakDownIntoSteps(mathExpr.getExpression());
            mathExpr.getMetadata().put("stepByStepBreakdown", steps);
            mathExpr.getMetadata().put("stepwiseProcessing", true);
        }

        // If in flow state, suggest extensions or related problems
        if (cognitiveState.isInFlowState()) {
            List<String> extensions = generateExtensions(mathExpr.getExpression());
            mathExpr.getMetadata().put("suggestedExtensions", extensions);
            mathExpr.getMetadata().put("flowStateOptimized", true);
        }

        return mathExpr.enhanceWithCognition(cognitiveState);
    }

    /**
     * Detect mathematical patterns in the expression
     */
    private List<MathPattern> detectMathematicalPatterns(String expression) {
        List<MathPattern> detectedPatterns = new ArrayList<>();

        for (MathPatternRecognizer recognizer : patternRecognizers) {
            List<MathPattern> patterns = recognizer.recognizePatterns(expression);
            detectedPatterns.addAll(patterns);
        }

        return detectedPatterns;
    }

    /**
     * Add educational metadata based on detected patterns
     */
    private void addEducationalMetadata(CognitiveDiscreteMathLibrary.MathExpression mathExpr,
                                       List<MathPattern> patterns) {

        Set<String> concepts = new HashSet<>();
        Set<String> prerequisites = new HashSet<>();
        Set<String> applications = new HashSet<>();

        for (MathPattern pattern : patterns) {
            concepts.addAll(pattern.getRelatedConcepts());
            prerequisites.addAll(pattern.getPrerequisites());
            applications.addAll(pattern.getApplications());
        }

        mathExpr.getMetadata().put("relatedConcepts", concepts);
        mathExpr.getMetadata().put("prerequisites", prerequisites);
        mathExpr.getMetadata().put("applications", applications);

        // Estimate difficulty level
        double avgComplexity = patterns.stream()
            .mapToDouble(MathPattern::getComplexityScore)
            .average()
            .orElse(0.5);

        String difficultyLevel;
        if (avgComplexity < 0.3) {
            difficultyLevel = "Beginner";
        } else if (avgComplexity < 0.6) {
            difficultyLevel = "Intermediate";
        } else if (avgComplexity < 0.8) {
            difficultyLevel = "Advanced";
        } else {
            difficultyLevel = "Expert";
        }

        mathExpr.getMetadata().put("estimatedDifficulty", difficultyLevel);
    }

    /**
     * Attempt to simplify a mathematical expression
     */
    private String attemptSimplification(String expression) {
        String simplified = expression;

        // Apply predefined simplification rules
        for (Map.Entry<String, String> rule : SIMPLIFICATION_RULES.entrySet()) {
            simplified = simplified.replaceAll(Pattern.quote(rule.getKey()), rule.getValue());
        }

        // Apply algebraic simplifications
        simplified = simplifyAlgebraic(simplified);

        // Apply trigonometric simplifications
        simplified = simplifyTrigonometric(simplified);

        return simplified;
    }

    private String simplifyAlgebraic(String expression) {
        // Basic algebraic simplifications
        String simplified = expression;

        // Remove unnecessary parentheses for single terms
        simplified = simplified.replaceAll("\\(([a-zA-Z0-9]+)\\)", "$1");

        // Combine like terms (simplified version)
        simplified = simplified.replaceAll("([0-9]*)x \\+ ([0-9]*)x", 
            (match) -> {
                try {
                    String[] parts = match.group().split(" \\+ ");
                    int coeff1 = parts[0].equals("x") ? 1 : Integer.parseInt(parts[0].replace("x", ""));
                    int coeff2 = parts[1].equals("x") ? 1 : Integer.parseInt(parts[1].replace("x", ""));
                    return (coeff1 + coeff2) + "x";
                } catch (Exception e) {
                    return match.group();
                }
            });

        return simplified;
    }

    private String simplifyTrigonometric(String expression) {
        String simplified = expression;

        // Basic trigonometric identities
        simplified = simplified.replaceAll("sin\\^2\\(([^)]+)\\) \\+ cos\\^2\\(\\1\\)", "1");
        simplified = simplified.replaceAll("1 - sin\\^2\\(([^)]+)\\)", "cos^2($1)");
        simplified = simplified.replaceAll("1 - cos\\^2\\(([^)]+)\\)", "sin^2($1)");

        return simplified;
    }

    /**
     * Break down complex expression into manageable steps
     */
    private List<String> breakDownIntoSteps(String expression) {
        List<String> steps = new ArrayList<>();

        // Identify major operations and break them down
        if (expression.contains("∫")) {
            steps.add("Identify the function to integrate: " + extractIntegrand(expression));
            steps.add("Choose appropriate integration method");
            steps.add("Apply integration rules");
            steps.add("Simplify the result");
        } else if (expression.contains("d/dx")) {
            steps.add("Identify the function to differentiate: " + extractDerivativeFunction(expression));
            steps.add("Apply differentiation rules");
            steps.add("Simplify the result");
        } else if (containsComplexArithmetic(expression)) {
            steps.addAll(breakDownArithmetic(expression));
        } else {
            steps.add("Evaluate: " + expression);
        }

        return steps;
    }

    /**
     * Generate related problems or extensions
     */
    private List<String> generateExtensions(String expression) {
        List<String> extensions = new ArrayList<>();

        // Detect expression type and generate appropriate extensions
        if (expression.contains("sin") || expression.contains("cos")) {
            extensions.add("Try with different trigonometric functions");
            extensions.add("Explore trigonometric identities");
            extensions.add("Graph the function");
        } else if (expression.contains("x^")) {
            extensions.add("Try with different powers");
            extensions.add("Explore polynomial behavior");
            extensions.add("Find roots and critical points");
        } else if (expression.contains("∪") || expression.contains("∩")) {
            extensions.add("Try with different sets");
            extensions.add("Explore set complement operations");
            extensions.add("Visualize with Venn diagrams");
        }

        return extensions;
    }

    // Utility methods
    private int countOccurrences(String text, String pattern) {
        // Simplified pattern matching for mathematical components
        Map<String, String[]> patterns = new HashMap<>();
        patterns.put("integration", new String[]{"∫", "integral"});
        patterns.put("differentiation", new String[]{"d/dx", "derivative", "'"});
        patterns.put("trigonometric", new String[]{"sin", "cos", "tan", "sec", "csc", "cot"});
        patterns.put("logarithmic", new String[]{"log", "ln"});
        patterns.put("exponential", new String[]{"e^", "exp"});
        patterns.put("polynomial", new String[]{"x^", "^2", "^3", "^4", "^5"});
        patterns.put("arithmetic", new String[]{"+", "-", "*", "/"});
        patterns.put("variable", new String[]{"x", "y", "z", "a", "b", "c"});

        String[] searchPatterns = patterns.get(pattern);
        if (searchPatterns == null) return 0;

        int count = 0;
        for (String searchPattern : searchPatterns) {
            int index = 0;
            while ((index = text.indexOf(searchPattern, index)) != -1) {
                count++;
                index += searchPattern.length();
            }
        }
        return count;
    }

    private double calculateNestingDepth(String expression) {
        int maxDepth = 0;
        int currentDepth = 0;

        for (char c : expression.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                currentDepth++;
                maxDepth = Math.max(maxDepth, currentDepth);
            } else if (c == ')' || c == ']' || c == '}') {
                currentDepth--;
            }
        }

        return maxDepth;
    }

    private String extractIntegrand(String expression) {
        Pattern pattern = Pattern.compile("∫(.+?)dx");
        Matcher matcher = pattern.matcher(expression);
        return matcher.find() ? matcher.group(1).trim() : expression;
    }

    private String extractDerivativeFunction(String expression) {
        Pattern pattern = Pattern.compile("d/dx\\((.+?)\\)");
        Matcher matcher = pattern.matcher(expression);
        return matcher.find() ? matcher.group(1).trim() : expression;
    }

    private boolean containsComplexArithmetic(String expression) {
        return expression.contains("+") || expression.contains("-") ||
               expression.contains("*") || expression.contains("/");
    }

    private List<String> breakDownArithmetic(String expression) {
        List<String> steps = new ArrayList<>();

        // Simple breakdown for arithmetic expressions
        if (expression.contains("(")) {
            steps.add("Evaluate expressions in parentheses first");
        }
        if (expression.contains("*") || expression.contains("/")) {
            steps.add("Perform multiplication and division");
        }
        if (expression.contains("+") || expression.contains("-")) {
            steps.add("Perform addition and subtraction");
        }

        return steps;
    }

    // Setters for component integration
    public void setEducationFramework(CognitiveEducationFramework educationFramework) {
        this.educationFramework = educationFramework;
    }

    public void setRecommendationEngine(CognitiveRecommendationEngine recommendationEngine) {
        this.recommendationEngine = recommendationEngine;
    }

    // Configuration setters
    public void setComplexityThreshold(double threshold) {
        this.complexityThreshold = threshold;
    }

    public void setAutoSimplification(boolean enabled) {
        this.enableAutoSimplification = enabled;
    }

    public void setCognitiveAdaptation(boolean enabled) {
        this.enableCognitiveAdaptation = enabled;
    }

    // ==================== PATTERN RECOGNITION CLASSES ====================

    /**
     * Base class for mathematical pattern recognizers
     */
    public abstract static class MathPatternRecognizer {
        public abstract List<MathPattern> recognizePatterns(String expression);
    }

    /**
     * Algebraic pattern recognizer
     */
    public static class AlgebraicPatternRecognizer extends MathPatternRecognizer {
        @Override
        public List<MathPattern> recognizePatterns(String expression) {
            List<MathPattern> patterns = new ArrayList<>();

            if (expression.matches(".*x\\^2.*")) {
                patterns.add(new MathPattern("Quadratic", 0.4,
                    Arrays.asList("Algebra", "Polynomials"),
                    Arrays.asList("Basic arithmetic"),
                    Arrays.asList("Physics equations", "Optimization")));
            }

            if (expression.matches(".*a.*x.*\\+.*b.*")) {
                patterns.add(new MathPattern("Linear", 0.2,
                    Arrays.asList("Algebra", "Linear equations"),
                    Arrays.asList("Basic arithmetic"),
                    Arrays.asList("Economics", "Physics")));
            }

            return patterns;
        }
    }

    /**
     * Trigonometric pattern recognizer
     */
    public static class TrigonometricPatternRecognizer extends MathPatternRecognizer {
        @Override
        public List<MathPattern> recognizePatterns(String expression) {
            List<MathPattern> patterns = new ArrayList<>();

            if (expression.contains("sin") || expression.contains("cos")) {
                patterns.add(new MathPattern("Trigonometric", 0.6,
                    Arrays.asList("Trigonometry", "Periodic functions"),
                    Arrays.asList("Geometry", "Unit circle"),
                    Arrays.asList("Signal processing", "Physics waves")));
            }

            if (expression.contains("sin^2") && expression.contains("cos^2")) {
                patterns.add(new MathPattern("Pythagorean Identity", 0.5,
                    Arrays.asList("Trigonometric identities"),
                    Arrays.asList("Basic trigonometry"),
                    Arrays.asList("Simplification", "Integration")));
            }

            return patterns;
        }
    }

    /**
     * Calculus pattern recognizer
     */
    public static class CalculusPatternRecognizer extends MathPatternRecognizer {
        @Override
        public List<MathPattern> recognizePatterns(String expression) {
            List<MathPattern> patterns = new ArrayList<>();

            if (expression.contains("∫")) {
                patterns.add(new MathPattern("Integration", 0.8,
                    Arrays.asList("Calculus", "Antiderivatives"),
                    Arrays.asList("Algebra", "Functions"),
                    Arrays.asList("Area calculation", "Physics")));
            }

            if (expression.contains("d/dx")) {
                patterns.add(new MathPattern("Differentiation", 0.7,
                    Arrays.asList("Calculus", "Derivatives"),
                    Arrays.asList("Algebra", "Functions"),
                    Arrays.asList("Rate of change", "Optimization")));
            }

            return patterns;
        }
    }

    /**
     * Set theory pattern recognizer
     */
    public static class SetTheoryPatternRecognizer extends MathPatternRecognizer {
        @Override
        public List<MathPattern> recognizePatterns(String expression) {
            List<MathPattern> patterns = new ArrayList<>();

            if (expression.contains("∪")) {
                patterns.add(new MathPattern("Set Union", 0.3,
                    Arrays.asList("Set theory", "Boolean algebra"),
                    Arrays.asList("Basic sets"),
                    Arrays.asList("Database operations", "Logic")));
            }

            if (expression.contains("∩")) {
                patterns.add(new MathPattern("Set Intersection", 0.3,
                    Arrays.asList("Set theory", "Boolean algebra"),
                    Arrays.asList("Basic sets"),
                    Arrays.asList("Database operations", "Logic")));
            }

            if (expression.contains("∈")) {
                patterns.add(new MathPattern("Set Membership", 0.2,
                    Arrays.asList("Set theory"),
                    Arrays.asList("Basic sets"),
                    Arrays.asList("Classification", "Logic")));
            }

            return patterns;
        }
    }

    /**
     * Logic pattern recognizer
     */
    public static class LogicPatternRecognizer extends MathPatternRecognizer {
        @Override
        public List<MathPattern> recognizePatterns(String expression) {
            List<MathPattern> patterns = new ArrayList<>();

            if (expression.contains("∧") || expression.contains("AND")) {
                patterns.add(new MathPattern("Logical AND", 0.3,
                    Arrays.asList("Propositional logic", "Boolean algebra"),
                    Arrays.asList("Basic logic"),
                    Arrays.asList("Computer science", "Circuit design")));
            }

            if (expression.contains("∨") || expression.contains("OR")) {
                patterns.add(new MathPattern("Logical OR", 0.3,
                    Arrays.asList("Propositional logic", "Boolean algebra"),
                    Arrays.asList("Basic logic"),
                    Arrays.asList("Computer science", "Circuit design")));
            }

            if (expression.contains("→") || expression.contains("⇒")) {
                patterns.add(new MathPattern("Logical Implication", 0.4,
                    Arrays.asList("Propositional logic", "Conditional statements"),
                    Arrays.asList("Basic logic"),
                    Arrays.asList("Mathematical proofs", "Programming")));
            }

            return patterns;
        }
    }

    /**
     * Mathematical pattern representation
     */
    public static class MathPattern {
        private final String name;
        private final double complexityScore;
        private final List<String> relatedConcepts;
        private final List<String> prerequisites;
        private final List<String> applications;

        public MathPattern(String name, double complexityScore,
                          List<String> relatedConcepts, List<String> prerequisites,
                          List<String> applications) {
            this.name = name;
            this.complexityScore = complexityScore;
            this.relatedConcepts = new ArrayList<>(relatedConcepts);
            this.prerequisites = new ArrayList<>(prerequisites);
            this.applications = new ArrayList<>(applications);
        }

        // Getters
        public String getName() { return name; }
        public double getComplexityScore() { return complexityScore; }
        public List<String> getRelatedConcepts() { return new ArrayList<>(relatedConcepts); }
        public List<String> getPrerequisites() { return new ArrayList<>(prerequisites); }
        public List<String> getApplications() { return new ArrayList<>(applications); }

        @Override
        public String toString() {
            return String.format("MathPattern{name='%s', complexity=%.2f}", name, complexityScore);
        }
    }
}
