package edu.ucsb.cs.cognitivedm.math;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.ProcessingResult;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.CognitiveState;
import edu.ucsb.cs.cognitivedm.framework.CognitiveBiasAdjuster;
import edu.ucsb.cs.cognitivedm.patterns.PatternDetector;
import com.discretelogic.expressions.ExpressionParser;
import com.discretelogic.model.Expression;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CognitiveExpressionParser - Integration of cognitive awareness with mathematical expression parsing
 *
 * This parser extends traditional expression parsing by incorporating cognitive load assessment,
 * attention-recognition dynamics, and human-like bias modeling. It provides insights into
 * the cognitive complexity of expressions and adapts parsing strategies based on cognitive state.
 *
 * Key Features:
 * - Cognitive load assessment for expressions
 * - Multi-scale cognitive processing integration
 * - Adaptive parsing strategies based on attention state
 * - Human bias modeling for expression interpretation
 * - Pattern detection in mathematical structures
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class CognitiveExpressionParser {
    private final AttentionRecognitionFramework cognitiveFramework;
    private final ExpressionParser traditionalParser;
    private final CognitiveBiasAdjuster biasAdjuster;
    private final PatternDetector patternDetector;
    private final CognitiveParsingConfiguration config;

    /**
     * Represents the result of cognitive expression parsing
     */
    public static class CognitiveParsingResult {
        private final Expression ast;
        private final List<String> variables;
        private final double cognitiveLoad;
        private final List<ProcessingResult> cognitiveAnalysis;
        private final CognitiveComplexityMetrics complexityMetrics;
        private final List<CognitiveInsight> insights;
        private final Map<String, Object> metadata;

        public CognitiveParsingResult(Expression ast, List<String> variables, double cognitiveLoad,
                                    List<ProcessingResult> cognitiveAnalysis,
                                    CognitiveComplexityMetrics complexityMetrics,
                                    List<CognitiveInsight> insights) {
            this.ast = ast;
            this.variables = new ArrayList<>(variables);
            this.cognitiveLoad = cognitiveLoad;
            this.cognitiveAnalysis = new ArrayList<>(cognitiveAnalysis);
            this.complexityMetrics = complexityMetrics;
            this.insights = new ArrayList<>(insights);
            this.metadata = new HashMap<>();
        }

        // Getters
        public Expression getAST() { return ast; }
        public List<String> getVariables() { return new ArrayList<>(variables); }
        public double getCognitiveLoad() { return cognitiveLoad; }
        public List<ProcessingResult> getCognitiveAnalysis() { return new ArrayList<>(cognitiveAnalysis); }
        public CognitiveComplexityMetrics getComplexityMetrics() { return complexityMetrics; }
        public List<CognitiveInsight> getInsights() { return new ArrayList<>(insights); }
        public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }

        public void addMetadata(String key, Object value) {
            metadata.put(key, value);
        }

        /**
         * Get cognitive difficulty rating (0-1, where 1 is most difficult)
         */
        public double getCognitiveDifficulty() {
            return (cognitiveLoad * 0.4 +
                   complexityMetrics.getStructuralComplexity() * 0.3 +
                   complexityMetrics.getSemanticComplexity() * 0.3);
        }

        /**
         * Check if expression requires high cognitive attention
         */
        public boolean requiresHighAttention() {
            return getCognitiveDifficulty() > 0.7 || cognitiveLoad > 0.8;
        }
    }

    /**
     * Represents cognitive complexity metrics for an expression
     */
    public static class CognitiveComplexityMetrics {
        private final double structuralComplexity;  // Based on nesting, operators, etc.
        private final double semanticComplexity;    // Based on meaning and context
        private final double workingMemoryLoad;     // Estimated WM requirements
        private final double attentionalDemand;     // Required attention focus
        private final int nestingDepth;
        private final int operatorCount;
        private final int variableCount;

        public CognitiveComplexityMetrics(double structuralComplexity, double semanticComplexity,
                                        double workingMemoryLoad, double attentionalDemand,
                                        int nestingDepth, int operatorCount, int variableCount) {
            this.structuralComplexity = structuralComplexity;
            this.semanticComplexity = semanticComplexity;
            this.workingMemoryLoad = workingMemoryLoad;
            this.attentionalDemand = attentionalDemand;
            this.nestingDepth = nestingDepth;
            this.operatorCount = operatorCount;
            this.variableCount = variableCount;
        }

        // Getters
        public double getStructuralComplexity() { return structuralComplexity; }
        public double getSemanticComplexity() { return semanticComplexity; }
        public double getWorkingMemoryLoad() { return workingMemoryLoad; }
        public double getAttentionalDemand() { return attentionalDemand; }
        public int getNestingDepth() { return nestingDepth; }
        public int getOperatorCount() { return operatorCount; }
        public int getVariableCount() { return variableCount; }

        public double getOverallComplexity() {
            return (structuralComplexity * 0.3 + semanticComplexity * 0.25 +
                   workingMemoryLoad * 0.25 + attentionalDemand * 0.2);
        }
    }

    /**
     * Represents a cognitive insight about the expression
     */
    public static class CognitiveInsight {
        private final String type;
        private final String description;
        private final double confidence;
        private final String recommendation;

        public CognitiveInsight(String type, String description, double confidence, String recommendation) {
            this.type = type;
            this.description = description;
            this.confidence = confidence;
            this.recommendation = recommendation;
        }

        // Getters
        public String getType() { return type; }
        public String getDescription() { return description; }
        public double getConfidence() { return confidence; }
        public String getRecommendation() { return recommendation; }
    }

    /**
     * Configuration for cognitive parsing behavior
     */
    public static class CognitiveParsingConfiguration {
        private boolean enableBiasModeling = true;
        private boolean enablePatternDetection = true;
        private boolean enableComplexityAnalysis = true;
        private boolean enableInsightGeneration = true;
        private double attentionThreshold = 0.6;
        private double cognitiveLoadThreshold = 0.8;
        private int maxProcessingSteps = 10;

        // Getters and setters
        public boolean isBiasModelingEnabled() { return enableBiasModeling; }
        public void setBiasModelingEnabled(boolean enabled) { this.enableBiasModeling = enabled; }

        public boolean isPatternDetectionEnabled() { return enablePatternDetection; }
        public void setPatternDetectionEnabled(boolean enabled) { this.enablePatternDetection = enabled; }

        public boolean isComplexityAnalysisEnabled() { return enableComplexityAnalysis; }
        public void setComplexityAnalysisEnabled(boolean enabled) { this.enableComplexityAnalysis = enabled; }

        public boolean isInsightGenerationEnabled() { return enableInsightGeneration; }
        public void setInsightGenerationEnabled(boolean enabled) { this.enableInsightGeneration = enabled; }

        public double getAttentionThreshold() { return attentionThreshold; }
        public void setAttentionThreshold(double threshold) { this.attentionThreshold = threshold; }

        public double getCognitiveLoadThreshold() { return cognitiveLoadThreshold; }
        public void setCognitiveLoadThreshold(double threshold) { this.cognitiveLoadThreshold = threshold; }

        public int getMaxProcessingSteps() { return maxProcessingSteps; }
        public void setMaxProcessingSteps(int steps) { this.maxProcessingSteps = steps; }
    }

    /**
     * Constructor
     */
    public CognitiveExpressionParser() {
        this(new CognitiveParsingConfiguration());
    }

    /**
     * Constructor with configuration
     */
    public CognitiveExpressionParser(CognitiveParsingConfiguration config) {
        this.cognitiveFramework = new AttentionRecognitionFramework(3);
        this.traditionalParser = new ExpressionParser();
        this.biasAdjuster = new CognitiveBiasAdjuster();
        this.patternDetector = new PatternDetector(1);
        this.config = config;
    }

    /**
     * Parse expression with full cognitive analysis
     */
    public CognitiveParsingResult parseExpression(String expression) {
        return parseExpression(expression, new HashMap<>());
    }

    /**
     * Parse expression with cognitive context
     */
    public CognitiveParsingResult parseExpression(String expression, Map<String, Object> context) {
        // Phase 1: Cognitive pre-processing
        List<ProcessingResult> cognitiveAnalysis = performCognitivePreprocessing(expression, context);

        // Phase 2: Traditional parsing
        Expression ast = traditionalParser.parseExpression(expression);
        List<String> variables = ast.getVariables();

        // Phase 3: Cognitive complexity analysis
        CognitiveComplexityMetrics complexityMetrics = analyzeComplexity(expression, ast, cognitiveAnalysis);

        // Phase 4: Calculate cognitive load
        double cognitiveLoad = calculateCognitiveLoad(cognitiveAnalysis, complexityMetrics);

        // Phase 5: Generate cognitive insights
        List<CognitiveInsight> insights = generateCognitiveInsights(expression, ast, cognitiveAnalysis, complexityMetrics);

        // Phase 6: Apply adaptive strategies if needed
        applyAdaptiveStrategies(cognitiveLoad, complexityMetrics, context);

        CognitiveParsingResult result = new CognitiveParsingResult(
            ast, variables, cognitiveLoad, cognitiveAnalysis, complexityMetrics, insights
        );

        // Add metadata
        result.addMetadata("parsing_timestamp", System.currentTimeMillis());
        result.addMetadata("expression_length", expression.length());
        result.addMetadata("processing_steps", cognitiveAnalysis.size());

        return result;
    }

    /**
     * Perform cognitive preprocessing of the expression
     */
    private List<ProcessingResult> performCognitivePreprocessing(String expression, Map<String, Object> context) {
        // Convert expression to cognitive input format
        Object cognitiveInput = convertExpressionToCognitiveInput(expression);

        // Process through multi-scale cognitive framework
        List<ProcessingResult> results = cognitiveFramework.process(cognitiveInput, config.getMaxProcessingSteps());

        // Apply bias adjustments if enabled
        if (config.isBiasModelingEnabled()) {
            applyBiasAdjustments(results, context);
        }

        return results;
    }

    /**
     * Convert mathematical expression to format suitable for cognitive processing
     */
    private Object convertExpressionToCognitiveInput(String expression) {
        // Extract key characteristics for cognitive processing
        Map<String, Object> cognitiveInput = new HashMap<>();

        cognitiveInput.put("length", expression.length());
        cognitiveInput.put("complexity_estimate", estimateInitialComplexity(expression));
        cognitiveInput.put("operator_density", calculateOperatorDensity(expression));
        cognitiveInput.put("nesting_depth", estimateNestingDepth(expression));
        cognitiveInput.put("variable_count", countVariables(expression));

        return cognitiveInput;
    }

    /**
     * Analyze cognitive complexity of the expression
     */
    private CognitiveComplexityMetrics analyzeComplexity(String expression, Expression ast,
                                                       List<ProcessingResult> cognitiveAnalysis) {
        if (!config.isComplexityAnalysisEnabled()) {
            return new CognitiveComplexityMetrics(0.5, 0.5, 0.5, 0.5, 1, 1, 1);
        }

        // Structural complexity analysis
        int nestingDepth = calculateNestingDepth(expression);
        int operatorCount = countOperators(expression);
        int variableCount = ast.getVariables().size();

        double structuralComplexity = calculateStructuralComplexity(nestingDepth, operatorCount, variableCount);

        // Semantic complexity analysis
        double semanticComplexity = calculateSemanticComplexity(expression, ast);

        // Working memory load estimation
        double workingMemoryLoad = estimateWorkingMemoryLoad(nestingDepth, variableCount, operatorCount);

        // Attentional demand calculation
        double attentionalDemand = calculateAttentionalDemand(cognitiveAnalysis, structuralComplexity);

        return new CognitiveComplexityMetrics(
            structuralComplexity, semanticComplexity, workingMemoryLoad,
            attentionalDemand, nestingDepth, operatorCount, variableCount
        );
    }

    /**
     * Calculate overall cognitive load
     */
    private double calculateCognitiveLoad(List<ProcessingResult> cognitiveAnalysis,
                                        CognitiveComplexityMetrics complexityMetrics) {
        // Average cognitive load from multi-scale processing
        double avgCognitiveLoad = cognitiveAnalysis.stream()
            .mapToDouble(ProcessingResult::getCognitiveLoad)
            .average()
            .orElse(0.5);

        // Weight by complexity metrics
        double complexityWeight = complexityMetrics.getOverallComplexity();

        // Calculate attention demand factor
        double attentionFactor = cognitiveAnalysis.stream()
            .mapToDouble(r -> r.getState().getAttention())
            .average()
            .orElse(0.5);

        // Combined cognitive load calculation
        return (avgCognitiveLoad * 0.4 + complexityWeight * 0.4 + (1.0 - attentionFactor) * 0.2);
    }

    /**
     * Generate cognitive insights about the expression
     */
    private List<CognitiveInsight> generateCognitiveInsights(String expression, Expression ast,
                                                           List<ProcessingResult> cognitiveAnalysis,
                                                           CognitiveComplexityMetrics complexityMetrics) {
        List<CognitiveInsight> insights = new ArrayList<>();

        if (!config.isInsightGenerationEnabled()) {
            return insights;
        }

        // High complexity insight
        if (complexityMetrics.getOverallComplexity() > 0.8) {
            insights.add(new CognitiveInsight(
                "high_complexity",
                "This expression has high cognitive complexity and may require sustained attention",
                0.9,
                "Consider breaking down into smaller sub-expressions or providing additional working memory support"
            ));
        }

        // Working memory overload insight
        if (complexityMetrics.getWorkingMemoryLoad() > 0.7) {
            insights.add(new CognitiveInsight(
                "working_memory_load",
                "High working memory requirements detected",
                0.8,
                "Use external memory aids or step-by-step evaluation strategy"
            ));
        }

        // Attention switching insight
        long attentionSwitches = cognitiveAnalysis.stream()
            .filter(r -> r.getState().getWandering() > 0.5)
            .count();

        if (attentionSwitches > cognitiveAnalysis.size() * 0.3) {
            insights.add(new CognitiveInsight(
                "attention_instability",
                "Expression processing shows frequent attention switching",
                0.7,
                "Consider using attention-focusing techniques or simplifying the expression structure"
            ));
        }

        // Pattern recognition insight
        if (config.isPatternDetectionEnabled()) {
            List<PatternDetector.Pattern> patterns = detectMathematicalPatterns(expression, cognitiveAnalysis);
            if (!patterns.isEmpty()) {
                insights.add(new CognitiveInsight(
                    "pattern_recognition",
                    String.format("Detected %d cognitive patterns in expression processing", patterns.size()),
                    0.8,
                    "Leverage recognized patterns to reduce cognitive load"
                ));
            }
        }

        return insights;
    }

    /**
     * Apply adaptive strategies based on cognitive state
     */
    private void applyAdaptiveStrategies(double cognitiveLoad, CognitiveComplexityMetrics complexityMetrics,
                                       Map<String, Object> context) {
        // High cognitive load strategy
        if (cognitiveLoad > config.getCognitiveLoadThreshold()) {
            context.put("suggested_strategy", "step_by_step");
            context.put("attention_support_needed", true);
        }

        // High complexity strategy
        if (complexityMetrics.getOverallComplexity() > 0.8) {
            context.put("suggested_strategy", "decomposition");
            context.put("working_memory_support_needed", true);
        }

        // Low attention strategy
        double avgAttention = cognitiveFramework.getScales().stream()
            .mapToDouble(scale -> scale.getCurrentState().getAttention())
            .average()
            .orElse(0.5);

        if (avgAttention < config.getAttentionThreshold()) {
            context.put("suggested_strategy", "attention_restoration");
            context.put("break_recommended", true);
        }
    }

    /**
     * Apply bias adjustments to cognitive processing results
     */
    private void applyBiasAdjustments(List<ProcessingResult> results, Map<String, Object> context) {
        // Apply cognitive biases to processing probabilities
        for (ProcessingResult result : results) {
            Map<String, Object> evidence = new HashMap<>();
            evidence.put("cognitive_load", result.getCognitiveLoad());
            evidence.put("attention_level", result.getState().getAttention());
            evidence.put("pattern_count", result.getDetectedPatterns().size());

            // Example: Apply confirmation bias to pattern detection confidence
            for (PatternDetector.Pattern pattern : result.getDetectedPatterns()) {
                double adjustedConfidence = biasAdjuster.calculateBiasedProbability(
                    pattern.getConfidence(), evidence, context
                );
                // Note: In a full implementation, we'd need a way to update pattern confidence
                // This would require modifying the Pattern class or creating a new adjusted pattern
            }
        }
    }

    // Utility methods for complexity calculation

    private double estimateInitialComplexity(String expression) {
        return Math.min(1.0, expression.length() / 100.0 + countOperators(expression) / 20.0);
    }

    private double calculateOperatorDensity(String expression) {
        int operatorCount = countOperators(expression);
        return expression.length() > 0 ? (double) operatorCount / expression.length() : 0.0;
    }

    private int estimateNestingDepth(String expression) {
        int maxDepth = 0;
        int currentDepth = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                currentDepth++;
                maxDepth = Math.max(maxDepth, currentDepth);
            } else if (c == ')') {
                currentDepth--;
            }
        }
        return maxDepth;
    }

    private int countVariables(String expression) {
        return (int) expression.chars()
            .filter(c -> Character.isLetter(c))
            .distinct()
            .count();
    }

    private int calculateNestingDepth(String expression) {
        return estimateNestingDepth(expression);
    }

    private int countOperators(String expression) {
        return (int) expression.chars()
            .filter(c -> "+-*/^&|!<>=".indexOf(c) >= 0)
            .count();
    }

    private double calculateStructuralComplexity(int nestingDepth, int operatorCount, int variableCount) {
        // Normalize and combine structural factors
        double depthFactor = Math.min(1.0, nestingDepth / 5.0);
        double operatorFactor = Math.min(1.0, operatorCount / 10.0);
        double variableFactor = Math.min(1.0, variableCount / 8.0);

        return (depthFactor * 0.4 + operatorFactor * 0.35 + variableFactor * 0.25);
    }

    private double calculateSemanticComplexity(String expression, Expression ast) {
        // Analyze semantic complexity based on operator types and relationships
        double complexity = 0.5; // Base complexity

        // Increase complexity for logical operators
        if (expression.contains("&") || expression.contains("|") || expression.contains("!")) {
            complexity += 0.2;
        }

        // Increase complexity for comparison operators
        if (expression.contains("<") || expression.contains(">") || expression.contains("=")) {
            complexity += 0.15;
        }

        // Increase complexity for mixed operator types
        boolean hasLogical = expression.matches(".*[&|!].*");
        boolean hasArithmetic = expression.matches(".*[+\\-*/].*");
        boolean hasComparison = expression.matches(".*[<>=].*");

        int operatorTypes = (hasLogical ? 1 : 0) + (hasArithmetic ? 1 : 0) + (hasComparison ? 1 : 0);
        if (operatorTypes > 1) {
            complexity += 0.1 * (operatorTypes - 1);
        }

        return Math.min(1.0, complexity);
    }

    private double estimateWorkingMemoryLoad(int nestingDepth, int variableCount, int operatorCount) {
        // Estimate based on cognitive psychology research on working memory capacity
        double depthLoad = nestingDepth * 0.15; // Each level of nesting
        double variableLoad = variableCount * 0.1; // Each variable to track
        double operatorLoad = operatorCount * 0.05; // Each operation to remember

        return Math.min(1.0, depthLoad + variableLoad + operatorLoad);
    }

    private double calculateAttentionalDemand(List<ProcessingResult> cognitiveAnalysis, double structuralComplexity) {
        // Calculate based on cognitive analysis and complexity
        double avgWandering = cognitiveAnalysis.stream()
            .mapToDouble(r -> r.getState().getWandering())
            .average()
            .orElse(0.1);

        double avgAttention = cognitiveAnalysis.stream()
            .mapToDouble(r -> r.getState().getAttention())
            .average()
            .orElse(0.5);

        // High wandering or low attention indicates high attentional demand
        double attentionDemand = (avgWandering + (1.0 - avgAttention)) / 2.0;

        // Weight by structural complexity
        return Math.min(1.0, attentionDemand * 0.6 + structuralComplexity * 0.4);
    }

    private List<PatternDetector.Pattern> detectMathematicalPatterns(String expression, List<ProcessingResult> cognitiveAnalysis) {
        // Convert cognitive analysis to pattern detection format
        List<double[]> timeSeries = cognitiveAnalysis.stream()
            .map(r -> new double[]{
                r.getState().getAttention(),
                r.getState().getRecognition(),
                r.getState().getWandering(),
                r.getCognitiveLoad()
            })
            .collect(Collectors.toList());

        return patternDetector.analyzeSequence(timeSeries);
    }

    // Public accessors and configuration methods

    /**
     * Get current cognitive framework state
     */
    public AttentionRecognitionFramework getCognitiveFramework() {
        return cognitiveFramework;
    }

    /**
     * Get bias adjuster
     */
    public CognitiveBiasAdjuster getBiasAdjuster() {
        return biasAdjuster;
    }

    /**
     * Get parsing configuration
     */
    public CognitiveParsingConfiguration getConfiguration() {
        return config;
    }

    /**
     * Set cognitive profile for bias adjustment
     */
    public void setCognitiveProfile(String profileName) {
        biasAdjuster.applyCognitiveProfile(profileName);
    }

    /**
     * Quick cognitive difficulty assessment
     */
    public double assessCognitiveDifficulty(String expression) {
        return parseExpression(expression).getCognitiveDifficulty();
    }

    /**
     * Check if expression requires attention support
     */
    public boolean requiresAttentionSupport(String expression) {
        return parseExpression(expression).requiresHighAttention();
    }

    /**
     * Shutdown cognitive framework
     */
    public void shutdown() {
        cognitiveFramework.shutdown();
    }
}
