package edu.ucsb.cs.cognitivedm;

import com.discretelogic.model.Expression;
import edu.ucsb.cs.cognitivedm.embeddings.MathEmbeddingService;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.CognitiveState;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.ProcessingResult;
import edu.ucsb.cs.cognitivedm.framework.CognitiveBiasAdjuster;
import edu.ucsb.cs.cognitivedm.patterns.PatternDetector;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Enhanced MathExpression with cognitive processing, lexical network integration,
 * and meta-controller for attention drift monitoring and bias auditing.
 *
 * Implements the Ψ(x) optimization framework:
 * Ψ(x) = ∫[α(t)S(x) + (1-α(t))N(x)] × exp(-[λ₁R_cognitive + λ₂R_efficiency]) × P(H|E,β) dt
 *
 * Features:
 * - Set theory operations (union, intersection, power sets, Cartesian products)
 * - Multilayer lexical network for semantic/syntactic connections
 * - Cognitive state monitoring and adaptive parsing
 * - Meta-controller for attention drift detection and bias auditing
 * - Educational adaptivity through lexical viability optimization
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 2.0
 */
public class MathExpression extends Expression {

    private final AttentionRecognitionFramework cognitiveFramework;
    private final CognitiveBiasAdjuster biasAdjuster;
    private final PatternDetector patternDetector;
    private final LexicalNetwork lexicalNetwork;
    private final MetaController metaController;
    private final LexicalViabilityComponent viabilityComponent;
    private final MathEmbeddingService embeddingService;

    private volatile CognitiveState processingState;
    private final Map<String, Double> cognitiveTags;
    private SetExpression setExpression;
    private double complexityScore;
    private final ExecutorService processingExecutor;

    // Ψ(x) parameters
    private volatile double alpha; // α(t) - symbolic/neural balance
    private final double lambda1 = 0.7; // cognitive penalty weight
    private final double lambda2 = 0.3; // efficiency penalty weight
    private volatile double beta = 1.2; // bias parameter

    // Add missing variables
    private double cognitivePenalty = 0.1;
    private double efficiencyPenalty = 0.2;
    private double biasedProbability = 0.3;

    public MathExpression(String expressionString) {
        this(expressionString, new AttentionRecognitionFramework(3));
    }

    public MathExpression(
        String expressionString,
        AttentionRecognitionFramework framework
    ) {
        super(expressionString, extractVariables(expressionString));
        this.cognitiveFramework = framework;
        this.biasAdjuster = new CognitiveBiasAdjuster();
        this.patternDetector = new PatternDetector(1);
        this.lexicalNetwork = new LexicalNetwork();
        this.metaController = new MetaController();
        this.embeddingService = new MathEmbeddingService(
            MathEmbeddingService.EmbeddingConfig.defaultConfig()
        );
        this.viabilityComponent = new LexicalViabilityComponent(
            lexicalNetwork,
            embeddingService
        );

        this.processingState = new CognitiveState(0.5, 0.5, 0.1);
        this.cognitiveTags = new ConcurrentHashMap<>();
        this.alpha = 0.6; // default symbolic preference
        this.processingExecutor = Executors.newFixedThreadPool(3);

        initializeExpression(expressionString);
    }

    // Add no-argument constructor
    public MathExpression() {
        this("", new AttentionRecognitionFramework(3));
    }

    /**
     * Initialize expression with cognitive analysis and lexical network setup
     */
    private void initializeExpression(String expr) {
        // Parse set theory expression if applicable
        this.setExpression = parseSetExpression(expr);

        // Initialize lexical network with expression terms
        initializeLexicalNodes(expr);

        // Calculate initial complexity
        this.complexityScore = calculateInitialComplexity(expr);

        // Initialize cognitive tags
        cognitiveTags.put("initialized", 1.0);
        cognitiveTags.put("initialComplexity", complexityScore);
    }

    /**
     * Process expression using Ψ(x) optimization framework
     */
    public CompletableFuture<
        PsiOptimizationResult
    > processWithPsiOptimization() {
        return CompletableFuture.supplyAsync(
            () -> {
                try {
                    // Step 1: Cognitive pre-processing
                    List<ProcessingResult> cognitiveResults =
                        cognitiveFramework.process(getExpressionString(), 5);

                    // Step 2: Calculate S(x) - symbolic processing
                    double symbolicOutput = calculateSymbolicOutput();

                    // Step 3: Calculate N(x) - neural/lexical processing
                    double neuralOutput = calculateNeuralOutput(
                        cognitiveResults
                    );

                    // Step 4: Update α(t) based on cognitive state
                    updateAlpha(cognitiveResults);

                    // Step 5: Calculate hybrid output
                    double hybridOutput =
                        alpha * symbolicOutput + (1 - alpha) * neuralOutput;

                    // Step 6: Calculate cognitive and efficiency penalties
                    double cognitivePenalty = calculateCognitivePenalty(
                        cognitiveResults
                    );
                    double efficiencyPenalty = calculateEfficiencyPenalty();

                    // Step 7: Apply exponential penalty factor
                    double penaltyFactor = Math.exp(
                        -(lambda1 * cognitivePenalty +
                            lambda2 * efficiencyPenalty)
                    );

                    // Step 8: Calculate biased probability P(H|E,β)
                    double biasedProbability = calculateBiasedProbability(
                        cognitiveResults
                    );

                    // Step 9: Final Ψ(x) calculation
                    double psiValue =
                        hybridOutput * penaltyFactor * biasedProbability;

                    // Step 10: Meta-controller analysis
                    MetaAnalysis metaAnalysis = metaController.analyze(
                        cognitiveResults,
                        this
                    );

                    // Step 11: Apply adaptations if needed
                    String optimizedExpression = applyAdaptiveOptimizations(
                        psiValue,
                        metaAnalysis
                    );

                    return new PsiOptimizationResult(
                        psiValue,
                        optimizedExpression,
                        symbolicOutput,
                        neuralOutput,
                        alpha,
                        cognitivePenalty,
                        efficiencyPenalty,
                        biasedProbability,
                        metaAnalysis,
                        cognitiveTags
                    );
                } catch (Exception e) {
                    cognitiveTags.put("processingError", 1.0);
                    throw new RuntimeException("Ψ(x) optimization failed", e);
                }
            },
            processingExecutor
        );
    }

    /**
     * Calculate symbolic output S(x)
     */
    private double calculateSymbolicOutput() {
        if (setExpression != null) {
            return calculateSetSymbolicOutput();
        } else {
            return calculateAlgebraicSymbolicOutput();
        }
    }

    private double calculateSetSymbolicOutput() {
        try {
            switch (setExpression.getOperation()) {
                case UNION:
                case INTERSECTION:
                    return 0.9; // High confidence for basic operations
                case CARTESIAN_PRODUCT:
                    return 0.85; // Slightly lower for more complex operations
                case POWER_SET:
                    return 0.8; // Lower for exponential complexity
                case COMPLEMENT:
                    return 0.88;
                case DIFFERENCE:
                    return 0.87;
                case BUILDER:
                    return 0.75; // Lower for set-builder notation complexity
                default:
                    return 0.5;
            }
        } catch (Exception e) {
            cognitiveTags.put("symbolicError", 1.0);
            return 0.3;
        }
    }

    private double calculateAlgebraicSymbolicOutput() {
        // Simplified algebraic complexity assessment
        String expr = getExpressionString();
        int operatorCount = (int) expr
            .chars()
            .filter(c -> "+-*/^".indexOf(c) != -1)
            .count();
        int parenthesesDepth = calculateParenthesesDepth(expr);

        double confidence = Math.max(
            0.1,
            1.0 - (operatorCount * 0.1 + parenthesesDepth * 0.15)
        );
        return Math.min(0.95, confidence);
    }

    /**
     * Calculate neural output N(x) using lexical network
     */
    private double calculateNeuralOutput(
        List<ProcessingResult> cognitiveResults
    ) {
        // Get lexical network suggestions
        LexicalSuggestion suggestion = lexicalNetwork.getSuggestion(
            getExpressionString()
        );

        // Calculate viability score
        double viabilityScore = viabilityComponent.calculateViability(
            getExpressionString(),
            processingState,
            cognitiveResults
        );

        // Combine suggestion confidence with viability
        return (suggestion.getConfidence() * 0.6 + viabilityScore * 0.4);
    }

    /**
     * Update α(t) based on cognitive state and attention stability
     */
    private void updateAlpha(List<ProcessingResult> cognitiveResults) {
        double avgAttention = cognitiveResults
            .stream()
            .mapToDouble(r -> r.getState().getAttention())
            .average()
            .orElse(0.5);

        double attentionStability = calculateAttentionStability(
            cognitiveResults
        );
        double flowFrequency = calculateFlowStateFrequency(cognitiveResults);

        // Higher attention and stability favor symbolic processing
        // Lower attention favors neural/lexical processing
        this.alpha =
            0.3 +
            0.4 * avgAttention +
            0.2 * attentionStability +
            0.1 * flowFrequency;
        this.alpha = Math.max(0.1, Math.min(0.9, alpha));

        cognitiveTags.put("alpha", alpha);
        cognitiveTags.put("attentionStability", attentionStability);
        cognitiveTags.put("flowFrequency", flowFrequency);
    }

    /**
     * Calculate cognitive penalty R_cognitive
     */
    private double calculateCognitivePenalty(
        List<ProcessingResult> cognitiveResults
    ) {
        double avgWandering = cognitiveResults
            .stream()
            .mapToDouble(r -> r.getState().getWandering())
            .average()
            .orElse(0.1);

        double cognitiveLoad = cognitiveResults
            .stream()
            .mapToDouble(ProcessingResult::getCognitiveLoad)
            .average()
            .orElse(0.5);

        // Penalty increases with mind-wandering and cognitive overload
        double notationComplexity = calculateNotationComplexity();

        return Math.min(
            1.0,
            avgWandering * 0.4 + cognitiveLoad * 0.4 + notationComplexity * 0.2
        );
    }

    /**
     * Calculate efficiency penalty R_efficiency
     */
    private double calculateEfficiencyPenalty() {
        double processingTime =
            System.currentTimeMillis() - processingState.getTimestamp();
        double complexityFactor = complexityScore;
        double lexicalQueryTime = lexicalNetwork.getLastQueryTime();

        // Normalize to 0-1 range
        double timePenalty = Math.min(1.0, processingTime / 1000.0); // 1 second baseline
        double complexityPenalty = complexityFactor;
        double queryPenalty = Math.min(1.0, lexicalQueryTime / 100.0); // 100ms baseline

        return (
            timePenalty * 0.4 + complexityPenalty * 0.3 + queryPenalty * 0.3
        );
    }

    /**
     * Calculate biased probability P(H|E,β)
     */
    private double calculateBiasedProbability(
        List<ProcessingResult> cognitiveResults
    ) {
        // Base probability for notation preference
        double baseProb = viabilityComponent.getNotationPreference(
            getExpressionString()
        );

        // Prepare evidence and context for bias adjustment
        Map<String, Object> evidence = new HashMap<>();
        evidence.put(
            "cognitive_load",
            cognitiveResults
                .stream()
                .mapToDouble(ProcessingResult::getCognitiveLoad)
                .average()
                .orElse(0.5)
        );
        evidence.put("attention_level", processingState.getAttention());
        evidence.put("complexity", complexityScore);

        Map<String, Object> context = new HashMap<>();
        context.put("confirms_expectation", alpha > 0.6); // Symbolic preference
        context.put(
            "frame_type",
            complexityScore > 0.7 ? "negative" : "positive"
        );
        context.put("recency_score", calculateRecencyScore());

        return biasAdjuster.calculateBiasedProbability(
            baseProb,
            evidence,
            context
        );
    }

    /**
     * Parse set theory expressions
     */
    private SetExpression parseSetExpression(String expr) {
        // Set theory operation patterns
        String[] unionParts = expr.split("∪|union");
        String[] intersectionParts = expr.split("∩|intersection");
        String[] differenceParts = expr.split("\\\\|-");
        String[] cartesianParts = expr.split("×|×");

        try {
            if (unionParts.length == 2) {
                return new SetExpression(
                    SetOperation.UNION,
                    unionParts[0].trim(),
                    unionParts[1].trim()
                );
            } else if (intersectionParts.length == 2) {
                return new SetExpression(
                    SetOperation.INTERSECTION,
                    intersectionParts[0].trim(),
                    intersectionParts[1].trim()
                );
            } else if (cartesianParts.length == 2) {
                return new SetExpression(
                    SetOperation.CARTESIAN_PRODUCT,
                    cartesianParts[0].trim(),
                    cartesianParts[1].trim()
                );
            } else if (expr.contains("power") || expr.contains("P(")) {
                String setName = expr
                    .replaceAll("power\\s*|P\\(|\\)", "")
                    .trim();
                return new SetExpression(SetOperation.POWER_SET, setName, null);
            } else if (expr.contains("complement") || expr.contains("'")) {
                String setName = expr.replaceAll("complement\\s*|'", "").trim();
                return new SetExpression(
                    SetOperation.COMPLEMENT,
                    setName,
                    null
                );
            } else if (expr.matches("\\{[^}]*\\|[^}]*\\}")) {
                return new SetExpression(SetOperation.BUILDER, expr, null);
            } else if (differenceParts.length == 2) {
                return new SetExpression(
                    SetOperation.DIFFERENCE,
                    differenceParts[0].trim(),
                    differenceParts[1].trim()
                );
            }
        } catch (Exception e) {
            cognitiveTags.put("setParseError", 1.0);
        }

        return null;
    }

    /**
     * Initialize lexical network with expression terms
     */
    private void initializeLexicalNodes(String expr) {
        // Extract mathematical terms
        Set<String> terms = extractMathematicalTerms(expr);

        for (String term : terms) {
            Map<String, Double> features = extractTermFeatures(term);
            lexicalNetwork.addNode(term, features);
        }

        // Add operation nodes
        addOperationNodes(expr);

        // Build semantic and syntactic edges
        lexicalNetwork.buildEdges();
    }

    /**
     * Extract mathematical terms from expression
     */
    private Set<String> extractMathematicalTerms(String expr) {
        Set<String> terms = new HashSet<>();

        // Variable names
        Pattern variablePattern = Pattern.compile("[A-Za-z][A-Za-z0-9]*");
        Matcher variableMatcher = variablePattern.matcher(expr);
        while (variableMatcher.find()) {
            terms.add(variableMatcher.group());
        }

        // Numbers
        Pattern numberPattern = Pattern.compile("\\d+");
        Matcher numberMatcher = numberPattern.matcher(expr);
        while (numberMatcher.find()) {
            terms.add(numberMatcher.group());
        }

        // Set notation
        Pattern setPattern = Pattern.compile("\\{[^}]*\\}");
        Matcher setMatcher = setPattern.matcher(expr);
        while (setMatcher.find()) {
            terms.add(setMatcher.group());
        }

        return terms;
    }

    /**
     * Extract features for lexical terms
     */
    private Map<String, Double> extractTermFeatures(String term) {
        Map<String, Double> features = new HashMap<>();

        // Semantic features
        if (term.matches("[A-Z]")) {
            features.put("semantic_type", 1.0); // Set name
        } else if (term.matches("\\d+")) {
            features.put("semantic_type", 0.8); // Number
        } else if (term.matches("\\{.*\\}")) {
            features.put("semantic_type", 0.9); // Explicit set
        } else {
            features.put("semantic_type", 0.5); // Variable
        }

        // Syntactic features
        features.put("length", Math.min(1.0, term.length() / 10.0));
        features.put("complexity", calculateTermComplexity(term));

        // Educational features
        features.put("familiarity", calculateTermFamiliarity(term));
        features.put("visual_clarity", calculateVisualClarity(term));

        return features;
    }

    /**
     * Apply adaptive optimizations based on Ψ(x) value and meta-analysis
     */
    private String applyAdaptiveOptimizations(
        double psiValue,
        MetaAnalysis metaAnalysis
    ) {
        String currentExpr = getExpressionString();

        // Low Ψ(x) indicates need for optimization
        if (psiValue < 0.5) {
            currentExpr = viabilityComponent.optimizeNotation(
                currentExpr,
                processingState
            );
        }

        // Apply meta-controller recommendations
        for (String recommendation : metaAnalysis.getRecommendations()) {
            switch (recommendation) {
                case "SIMPLIFY_NOTATION":
                    currentExpr = simplifyNotation(currentExpr);
                    break;
                case "RESTORE_ATTENTION":
                    currentExpr = addAttentionCues(currentExpr);
                    break;
                case "REDUCE_BIAS":
                    beta = Math.max(0.8, beta - 0.1);
                    break;
                case "ENHANCE_CLARITY":
                    currentExpr = enhanceClarity(currentExpr);
                    break;
            }
        }

        return currentExpr;
    }

    // Helper methods

    private double calculateInitialComplexity(String expr) {
        if (setExpression != null) {
            return calculateSetComplexity(expr);
        }
        return calculateAlgebraicComplexity(expr);
    }

    private double calculateSetComplexity(String expr) {
        int operationCount = (int) expr
            .chars()
            .filter(c -> "∪∩\\×".indexOf(c) != -1)
            .count();
        int setCount = (int) expr.chars().filter(c -> c == '{').count();
        int nestingDepth = calculateSetNestingDepth(expr);

        return Math.min(
            1.0,
            (operationCount * 0.2 + setCount * 0.1 + nestingDepth * 0.3) / 5.0
        );
    }

    private double calculateAlgebraicComplexity(String expr) {
        int operatorCount = (int) expr
            .chars()
            .filter(c -> "+-*/^".indexOf(c) != -1)
            .count();
        int parenthesesDepth = calculateParenthesesDepth(expr);
        int termCount = expr.split("[+\\-]").length;

        return Math.min(
            1.0,
            (operatorCount * 0.15 + parenthesesDepth * 0.25 + termCount * 0.1) /
            8.0
        );
    }

    private int calculateParenthesesDepth(String expr) {
        int maxDepth = 0, currentDepth = 0;
        for (char c : expr.toCharArray()) {
            if (c == '(') {
                currentDepth++;
                maxDepth = Math.max(maxDepth, currentDepth);
            } else if (c == ')') {
                currentDepth--;
            }
        }
        return maxDepth;
    }

    private int calculateSetNestingDepth(String expr) {
        int maxDepth = 0, currentDepth = 0;
        for (char c : expr.toCharArray()) {
            if (c == '{') {
                currentDepth++;
                maxDepth = Math.max(maxDepth, currentDepth);
            } else if (c == '}') {
                currentDepth--;
            }
        }
        return maxDepth;
    }

    private double calculateAttentionStability(List<ProcessingResult> results) {
        if (results.size() < 2) return 0.5;

        double[] attentionValues = results
            .stream()
            .mapToDouble(r -> r.getState().getAttention())
            .toArray();

        double mean = Arrays.stream(attentionValues).average().orElse(0.5);
        double variance = Arrays.stream(attentionValues)
            .map(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);

        return Math.max(0.0, 1.0 - variance * 4.0); // Lower variance = higher stability
    }

    private double calculateFlowStateFrequency(List<ProcessingResult> results) {
        long flowStates = results
            .stream()
            .filter(
                r ->
                    r.getState().getAttention() > 0.8 &&
                    r.getState().getWandering() < 0.2
            )
            .count();

        return results.isEmpty() ? 0.0 : (double) flowStates / results.size();
    }

    private double calculateNotationComplexity() {
        String expr = getExpressionString();

        // Count complex notation elements
        int unicodeSymbols = (int) expr.chars().filter(c -> c > 127).count();
        int specialChars = (int) expr
            .chars()
            .filter(c -> "∪∩∅⊆⊇∈∉×".indexOf(c) != -1)
            .count();
        int subscripts = expr.split("_").length - 1;
        int superscripts = expr.split("\\^").length - 1;

        return Math.min(
            1.0,
            (unicodeSymbols + specialChars + subscripts + superscripts) / 10.0
        );
    }

    private double calculateRecencyScore() {
        // Simplified recency calculation based on processing timestamp
        long timeSinceProcessing =
            System.currentTimeMillis() - processingState.getTimestamp();
        return Math.max(0.0, 1.0 - timeSinceProcessing / 10000.0); // 10 second decay
    }

    private double calculateTermComplexity(String term) {
        if (term.matches("\\d+")) return 0.1; // Numbers are simple
        if (term.matches("[A-Za-z]")) return 0.2; // Single variables
        if (term.matches("\\{.*\\}")) return 0.6; // Sets
        return 0.4; // Default
    }

    private double calculateTermFamiliarity(String term) {
        // Common mathematical terms
        Set<String> familiarTerms = Set.of(
            "A",
            "B",
            "C",
            "x",
            "y",
            "z",
            "1",
            "2",
            "3"
        );
        return familiarTerms.contains(term) ? 0.9 : 0.5;
    }

    private double calculateVisualClarity(String term) {
        return Math.max(0.1, 1.0 - term.length() / 20.0); // Shorter terms are clearer
    }

    private String simplifyNotation(String expr) {
        // Replace complex unicode with simpler ASCII alternatives
        return expr
            .replace("∪", " union ")
            .replace("∩", " intersect ")
            .replace("×", " x ")
            .replace("∅", "empty");
    }

    private String addAttentionCues(String expr) {
        // Add visual separators and grouping
        if (setExpression != null && setExpression.getOperation() != null) {
            return "(" + expr + ")"; // Add parentheses for clarity
        }
        return expr;
    }

    private String enhanceClarity(String expr) {
        // Add spacing and formatting
        return expr
            .replaceAll("([∪∩×])", " $1 ")
            .replaceAll("\\s+", " ")
            .trim();
    }

    // Static helper for variable extraction
    private static List<String> extractVariables(String expression) {
        Set<String> variables = new HashSet<>();
        Pattern pattern = Pattern.compile("[A-Za-z][A-Za-z0-9]*");
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            String var = matcher.group();
            if (!isReservedWord(var)) {
                variables.add(var);
            }
        }

        return new ArrayList<>(variables);
    }

    private static boolean isReservedWord(String word) {
        Set<String> reserved = Set.of(
            "union",
            "intersect",
            "power",
            "complement",
            "empty"
        );
        return reserved.contains(word.toLowerCase());
    }

    // Getters
    public CognitiveState getProcessingState() {
        return processingState;
    }

    public Map<String, Double> getCognitiveTags() {
        return new HashMap<>(cognitiveTags);
    }

    public double getComplexityScore() {
        return complexityScore;
    }

    public SetExpression getSetExpression() {
        return setExpression;
    }

    public LexicalNetwork getLexicalNetwork() {
        return lexicalNetwork;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    // Cleanup
    public void shutdown() {
        try {
            processingExecutor.shutdown();
            if (!processingExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                processingExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            processingExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Set theory expression representation
     */
    public static class SetExpression {

        private final SetOperation operation;
        private final String leftOperand;
        private final String rightOperand;

        public SetExpression(
            SetOperation operation,
            String leftOperand,
            String rightOperand
        ) {
            this.operation = operation;
            this.leftOperand = leftOperand;
            this.rightOperand = rightOperand;
        }

        public SetOperation getOperation() {
            return operation;
        }

        public String getLeftOperand() {
            return leftOperand;
        }

        public String getRightOperand() {
            return rightOperand;
        }

        public String simplify() {
            if (leftOperand != null && leftOperand.equals(rightOperand)) {
                switch (operation) {
                    case UNION:
                    case INTERSECTION:
                        return leftOperand; // A ∪ A = A, A ∩ A = A
                    case DIFFERENCE:
                        return "∅"; // A - A = ∅
                }
            }
            return toString();
        }

        @Override
        public String toString() {
            switch (operation) {
                case COMPLEMENT:
                case POWER_SET:
                    return operation.getSymbol() + "(" + leftOperand + ")";
                case BUILDER:
                    return leftOperand;
                default:
                    return (
                        leftOperand +
                        " " +
                        operation.getSymbol() +
                        " " +
                        rightOperand
                    );
            }
        }
    }

    public enum SetOperation {
        UNION("∪"),
        INTERSECTION("∩"),
        DIFFERENCE("\\"),
        COMPLEMENT("'"),
        POWER_SET("P"),
        CARTESIAN_PRODUCT("×"),
        BUILDER("builder");

        private final String symbol;

        SetOperation(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    /**
     * Multilayer lexical network for mathematical terms
     */
    public static class LexicalNetwork {

        private final Map<String, LexicalNode> nodes;
        private final Map<String, Map<String, Double>> semanticEdges;
        private final Map<String, Map<String, Double>> syntacticEdges;
        private final Map<String, Map<String, Double>> embeddingEdges;
        private long lastQueryTime = 0;
        private MathEmbeddingService embeddingService;

        public LexicalNetwork() {
            this.nodes = new ConcurrentHashMap<>();
            this.semanticEdges = new ConcurrentHashMap<>();
            this.syntacticEdges = new ConcurrentHashMap<>();
            this.embeddingEdges = new ConcurrentHashMap<>();
        }

        public void setEmbeddingService(MathEmbeddingService embeddingService) {
            this.embeddingService = embeddingService;
        }

        public void addNode(String term, Map<String, Double> features) {
            nodes.put(term, new LexicalNode(term, features));
            semanticEdges.put(term, new ConcurrentHashMap<>());
            syntacticEdges.put(term, new ConcurrentHashMap<>());
            embeddingEdges.put(term, new ConcurrentHashMap<>());

            // Add term to embedding service if available
            if (embeddingService != null) {
                double[] embedding = createEmbeddingFromFeatures(features);
                embeddingService.addMathEmbedding(term, embedding);
            }
        }

        public void buildEdges() {
            for (String term1 : nodes.keySet()) {
                for (String term2 : nodes.keySet()) {
                    if (!term1.equals(term2)) {
                        double semanticSim = calculateSemanticSimilarity(
                            term1,
                            term2
                        );
                        double syntacticSim = calculateSyntacticSimilarity(
                            term1,
                            term2
                        );
                        double embeddingSim = calculateEmbeddingSimilarity(
                            term1,
                            term2
                        );

                        if (semanticSim > 0.3) {
                            semanticEdges.get(term1).put(term2, semanticSim);
                        }
                        if (syntacticSim > 0.3) {
                            syntacticEdges.get(term1).put(term2, syntacticSim);
                        }
                        if (embeddingSim > 0.3) {
                            embeddingEdges.get(term1).put(term2, embeddingSim);
                        }
                    }
                }
            }
        }

        public LexicalSuggestion getSuggestion(String expr) {
            long startTime = System.currentTimeMillis();

            // Find best notation alternative
            double maxConfidence = 0.0;
            String bestSuggestion = expr;

            for (String term : nodes.keySet()) {
                if (expr.contains(term)) {
                    double confidence = calculateSuggestionConfidence(
                        term,
                        expr
                    );
                    if (confidence > maxConfidence) {
                        maxConfidence = confidence;
                        bestSuggestion = generateNotationAlternative(
                            expr,
                            term
                        );
                    }
                }
            }

            lastQueryTime = System.currentTimeMillis() - startTime;
            return new LexicalSuggestion(bestSuggestion, maxConfidence);
        }

        private double calculateSemanticSimilarity(String term1, String term2) {
            LexicalNode node1 = nodes.get(term1);
            LexicalNode node2 = nodes.get(term2);

            double semanticType1 = node1.getFeature("semantic_type", 0.5);
            double semanticType2 = node2.getFeature("semantic_type", 0.5);

            // Higher similarity for same semantic types
            if (Math.abs(semanticType1 - semanticType2) < 0.2) {
                return 0.8;
            }

            return semanticType1 * semanticType2;
        }

        private double calculateSyntacticSimilarity(
            String term1,
            String term2
        ) {
            LexicalNode node1 = nodes.get(term1);
            LexicalNode node2 = nodes.get(term2);

            double length1 = node1.getFeature("length", 0.5);
            double length2 = node2.getFeature("length", 0.5);

            // Higher similarity for similar lengths
            if (Math.abs(length1 - length2) < 0.2) {
                return 0.7;
            }

            return length1 * length2;
        }

        private double calculateSuggestionConfidence(String term, String expr) {
            LexicalNode node = nodes.get(term);
            double familiarity = node.getFeature("familiarity", 0.5);
            double clarity = node.getFeature("visual_clarity", 0.5);
            double complexity = node.getFeature("complexity", 0.5);

            return (
                familiarity * 0.4 + clarity * 0.4 + (1.0 - complexity) * 0.2
            );
        }

        private String generateNotationAlternative(String expr, String term) {
            // Generate simpler alternative notation
            if (term.equals("∪")) return expr.replace("∪", " union ");
            if (term.equals("∩")) return expr.replace("∩", " intersect ");
            if (term.equals("×")) return expr.replace("×", " x ");
            return expr;
        }

        /**
         * Calculate embedding-based similarity between terms
         */
        private double calculateEmbeddingSimilarity(
            String term1,
            String term2
        ) {
            if (embeddingService == null) return 0.0;
            return embeddingService.calculateEmbeddingSimilarity(term1, term2);
        }

        /**
         * Calculate hybrid similarity combining rule-based and embedding approaches
         */
        public double calculateHybridSimilarity(
            String term1,
            String term2,
            double attention,
            double cognitiveLoad
        ) {
            double semanticSim = calculateSemanticSimilarity(term1, term2);
            double syntacticSim = calculateSyntacticSimilarity(term1, term2);
            double embeddingSim = embeddingService != null
                ? embeddingService.calculateCognitiveSimilarity(
                    term1,
                    term2,
                    attention,
                    cognitiveLoad
                )
                : 0.0;

            // Weighted combination based on cognitive state
            double ruleWeight = Math.max(0.2, 1.0 - attention); // Lower attention = rely more on rules
            double embeddingWeight = attention; // Higher attention = rely more on embeddings

            return (
                ((ruleWeight * (semanticSim + syntacticSim)) / 2.0) +
                (embeddingWeight * embeddingSim)
            );
        }

        /**
         * Create embedding from node features
         */
        private double[] createEmbeddingFromFeatures(
            Map<String, Double> features
        ) {
            double[] embedding = new double[128]; // Default dimension
            embedding[0] = features.getOrDefault("semantic_type", 0.5);
            embedding[1] = features.getOrDefault("syntactic_type", 0.5);
            embedding[2] = features.getOrDefault("complexity", 0.5);
            embedding[3] = features.getOrDefault("familiarity", 0.5);

            // Fill remaining dimensions with feature-influenced values
            Random random = new Random(features.hashCode());
            double featureSum =
                embedding[0] + embedding[1] + embedding[2] + embedding[3];
            for (int i = 4; i < 128; i++) {
                embedding[i] = featureSum / 4.0 + (random.nextGaussian() * 0.1);
            }

            return embedding;
        }

        public long getLastQueryTime() {
            return lastQueryTime;
        }
    }

    public static class LexicalNode {

        private final String term;
        private final Map<String, Double> features;

        public LexicalNode(String term, Map<String, Double> features) {
            this.term = term;
            this.features = new HashMap<>(features);
        }

        public double getFeature(String name, double defaultValue) {
            return features.getOrDefault(name, defaultValue);
        }

        public String getTerm() {
            return term;
        }

        public Map<String, Double> getFeatures() {
            return new HashMap<>(features);
        }
    }

    public static class LexicalSuggestion {

        private final String suggestion;
        private final double confidence;

        public LexicalSuggestion(String suggestion, double confidence) {
            this.suggestion = suggestion;
            this.confidence = confidence;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public double getConfidence() {
            return confidence;
        }
    }

    /**
     * Lexical viability component for optimizing mathematical expression processing
     */
    public static class LexicalViabilityComponent {

        private final LexicalNetwork network;
        private final Map<String, Double> notationPreferences;
        private final Map<String, Double> learnerProfile;
        private final MathEmbeddingService embeddingService;
        private final Map<String, Double> viabilityCache;

        public LexicalViabilityComponent(
            LexicalNetwork network,
            MathEmbeddingService embeddingService
        ) {
            this.network = network;
            this.embeddingService = embeddingService;
            this.notationPreferences = new ConcurrentHashMap<>();
            this.learnerProfile = new ConcurrentHashMap<>();
            this.viabilityCache = new ConcurrentHashMap<>();
            initializeDefaults();
        }

        private void initializeDefaults() {
            // Default notation preferences
            notationPreferences.put("algebraic_notation", 0.8);
            notationPreferences.put("set_notation", 0.7);
            notationPreferences.put("symbolic_density", 0.6);
            notationPreferences.put("parentheses_depth", 0.5);
            
            // Default learner profile
            learnerProfile.put("attention_span", 0.7);
            learnerProfile.put("cognitive_load_threshold", 0.65);
            learnerProfile.put("familiarity_set_theory", 0.6);
            learnerProfile.put("familiarity_algebra", 0.75);
        }

        public double calculateViability(
            String expr,
            CognitiveState state,
            List<ProcessingResult> results
        ) {
            // Check cache first
            String cacheKey = expr + "_" + state.getAttention() + "_" + 
                state.getRecognition() + "_" + state.getWandering();
            if (viabilityCache.containsKey(cacheKey)) {
                return viabilityCache.get(cacheKey);
            }
            
            // Multi-dimensional viability calculation
            double attentionViability = calculateAttentionViability(state, results);
            double notationViability = calculateNotationViability(expr);
            double learnerViability = calculateLearnerViability(expr);
            double embeddingViability = calculateEmbeddingViability(expr, state);
            
            // Weighted combination
            double attentionWeight = 0.3;
            double notationWeight = 0.25;
            double learnerWeight = 0.25;
            double embeddingWeight = 0.2;
            
            // Adjust weights based on cognitive state
            if (state.getAttention() < 0.4) {
                // When attention is low, prioritize attention viability
                attentionWeight = 0.5;
                notationWeight = 0.2;
                learnerWeight = 0.2;
                embeddingWeight = 0.1;
            } else if (state.getCognitiveLoad() > 0.7) {
                // When cognitive load is high, prioritize learner viability
                attentionWeight = 0.2;
                notationWeight = 0.2;
                learnerWeight = 0.4;
                embeddingWeight = 0.2;
            }
            
            // Calculate combined viability
            double viability = 
                attentionWeight * attentionViability +
                notationWeight * notationViability +
                learnerWeight * learnerViability + 
                embeddingWeight * embeddingViability;
            
            // Cache result
            viabilityCache.put(cacheKey, viability);
            
            return viability;
        }

        private double calculateAttentionViability(
            CognitiveState state,
            List<ProcessingResult> results
        ) {
            // Higher viability when attention is focused and wandering is low
            double attentionFactor = state.getAttention() * (1.0 - state.getWandering());
            
            // Analyze attention stability across time
            double stabilityFactor = 1.0;
            if (results != null && !results.isEmpty()) {
                double attentionVariance = calculateAttentionVariance(results);
                stabilityFactor = 1.0 / (1.0 + attentionVariance);
            }
            
            return attentionFactor * stabilityFactor;
        }

        private double calculateAttentionVariance(List<ProcessingResult> results) {
            if (results.size() < 2) return 0.0;
            
            // Calculate variance of attention values
            double sum = 0.0;
            double sumSq = 0.0;
            for (ProcessingResult result : results) {
                double attention = result.getState().getAttention();
                sum += attention;
                sumSq += attention * attention;
            }
            
            double mean = sum / results.size();
            double variance = (sumSq / results.size()) - (mean * mean);
            return variance;
        }

        private double calculateNotationViability(String expr) {
            // Examine notation style and match with preferences
            double algebraicNotationFactor = expr.contains("=") || expr.contains("+") ? 
                notationPreferences.getOrDefault("algebraic_notation", 0.5) : 0.5;
                
            double setNotationFactor = expr.contains("∪") || expr.contains("∩") ?
                notationPreferences.getOrDefault("set_notation", 0.5) : 0.5;
                
            // Analyze symbolic density
            double symbolDensity = (double) expr.replaceAll("[a-zA-Z0-9\\s]", "").length() / expr.length();
            double preferredDensity = notationPreferences.getOrDefault("symbolic_density", 0.5);
            double densityViability = 1.0 - Math.abs(symbolDensity - preferredDensity);
            
            // Combine factors
            return (algebraicNotationFactor + setNotationFactor + densityViability) / 3.0;
        }

        private double calculateLearnerViability(String expr) {
            // Match expression with learner profile
            double attentionSpanFactor = learnerProfile.getOrDefault("attention_span", 0.5);
            double expressionLengthViability = 
                1.0 - Math.min(1.0, (double) expr.length() / (100 * attentionSpanFactor));
            
            // Cognitive load threshold
            double loadThreshold = learnerProfile.getOrDefault("cognitive_load_threshold", 0.5);
            double complexityEstimate = calculateExpressionComplexity(expr);
            double complexityViability = 1.0 - Math.min(1.0, complexityEstimate / (1.5 * loadThreshold));
            
            // Domain familiarity
            double domainFamiliarity = expr.contains("∪") || expr.contains("∩") ?
                learnerProfile.getOrDefault("familiarity_set_theory", 0.5) :
                learnerProfile.getOrDefault("familiarity_algebra", 0.5);
                
            // Combine factors
            return (expressionLengthViability + complexityViability + domainFamiliarity) / 3.0;
        }

        private double calculateExpressionComplexity(String expr) {
            // Simple complexity metric based on:
            // 1. Length
            // 2. Number of operators
            // 3. Nesting depth
            
            double lengthFactor = Math.min(1.0, expr.length() / 50.0);
            
            int operatorCount = expr.replaceAll("[^+\\-*/=∪∩\\\\×']", "").length();
            double operatorFactor = Math.min(1.0, operatorCount / 10.0);
            
            int nestingDepth = 0;
            int maxDepth = 0;
            for (char c : expr.toCharArray()) {
                if (c == '(' || c == '{' || c == '[') nestingDepth++;
                if (c == ')' || c == '}' || c == ']') nestingDepth--;
                maxDepth = Math.max(maxDepth, nestingDepth);
            }
            double nestingFactor = Math.min(1.0, maxDepth / 5.0);
            
            return (lengthFactor + operatorFactor + nestingFactor) / 3.0;
        }

        public double getNotationPreference(String expr) {
            // For backward compatibility
            return calculateNotationViability(expr);
        }

        public String optimizeNotation(String expr, CognitiveState state) {
            double viability = calculateViability(expr, state, null);
            
            if (viability < 0.4) {
                // Low viability - simplify notation
                return simplifyNotation(expr);
            } else if (viability > 0.8) {
                // High viability - can use more advanced notation
                return expr; // Keep as is
            } else {
                // Medium viability - make moderate adjustments
                return adjustNotation(expr, state);
            }
        }

        private String simplifyNotation(String expr) {
            // Replace complex symbols with simpler ones
            String simplified = expr
                .replace("∪", " union ")
                .replace("∩", " intersect ")
                .replace("×", " cartesian ")
                .replace("\\", " minus ");
                
            // Add more spacing for clarity
            simplified = simplified.replaceAll("([\\(\\)\\{\\}])", " $1 ");
            
            return simplified.replaceAll("\\s+", " ").trim();
        }

        private String adjustNotation(String expr, CognitiveState state) {
            // Adjust notation based on cognitive state
            if (state.getAttention() < 0.5) {
                // Add attention cues for low attention
                return addAttentionCues(expr);
            } else if (state.getCognitiveLoad() > 0.7) {
                // Simplify for high cognitive load
                return breakComplexExpressions(expr);
            } else {
                return expr; // No changes needed
            }
        }

        private String addAttentionCues(String expr) {
            // Simple implementation - add spacing around key operators
            return expr.replaceAll("([\\+\\-\\*/=∪∩\\\\×'])", " $1 ")
                       .replaceAll("\\s+", " ")
                       .trim();
        }

        private String breakComplexExpressions(String expr) {
            // If expression is long, insert line breaks
            if (expr.length() > 30) {
                // Insert breaks after operators
                return expr.replaceAll("([\\+\\-\\*/=∪∩\\\\×'])", "$1\n")
                           .replaceAll("\\n+", "\n");
            }
            return expr;
        }

        private double calculateEmbeddingViability(
            String expr,
            CognitiveState state
        ) {
            if (embeddingService == null) {
                return 0.5; // Default when embedding service not available
            }
            
            try {
                // Get embedding for expression
                double[] embedding = embeddingService.getEmbedding(expr);
                if (embedding == null) {
                    return 0.5;
                }
                
                // Find similar expressions
                Map<String, Double> similarExpressions = 
                    embeddingService.findSimilarExpressions(embedding, 3);
                
                if (similarExpressions.isEmpty()) {
                    return 0.5;
                }
                
                // Calculate average similarity
                double totalSimilarity = similarExpressions.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
                double avgSimilarity = totalSimilarity / similarExpressions.size();
                
                // Adjust based on cognitive state
                if (state.getWandering() > 0.6) {
                    // High wandering - prioritize familiar expressions
                    return avgSimilarity;
                } else if (state.getRecognition() < 0.4) {
                    // Low recognition - prioritize recognizable patterns
                    return avgSimilarity * 0.8;
                } else {
                    // Normal state
                    return avgSimilarity * 0.5 + 0.25; // Scale to range 0.25-0.75
                }
            } catch (Exception e) {
                // Fallback for embedding errors
                return 0.5;
            }
        }

        public String getMostViableTerm(String expr, CognitiveState state) {
            // Extract terms
            String[] terms = expr.split("[^a-zA-Z0-9_]");
            
            // Find most viable term
            String bestTerm = "";
            double bestViability = 0.0;
            
            for (String term : terms) {
                if (term.length() < 2) continue; // Skip short terms
                
                // Evaluate term viability
                double viability = 0.0;
                
                // Check lexical network
                if (network != null && network.nodes.containsKey(term)) {
                    LexicalNode node = network.nodes.get(term);
                    viability += 0.4 * node.getFeature("usage_frequency", 0.5);
                    viability += 0.3 * node.getFeature("specificity", 0.5);
                }
                
                // Consider cognitive state
                viability += 0.3 * (1.0 - Math.abs(state.getAttention() - 0.7));
                
                if (viability > bestViability) {
                    bestViability = viability;
                    bestTerm = term;
                }
            }
            
            return bestTerm;
        }

        public void updateEmbeddingFeedback(
            String expr,
            CognitiveState state,
            boolean success
        ) {
            if (embeddingService == null) return;
            
            try {
                // Get embedding for expression
                double[] embedding = embeddingService.getEmbedding(expr);
                if (embedding == null) return;
                
                // Update feedback in embedding service
                Map<String, Double> feedback = new HashMap<>();
                feedback.put("success", success ? 1.0 : 0.0);
                feedback.put("attention", state.getAttention());
                feedback.put("recognition", state.getRecognition());
                feedback.put("wandering", state.getWandering());
                
                embeddingService.updateFeedback(expr, embedding, feedback);
                
                // Update viability cache
                String cacheKey = expr + "_" + state.getAttention() + "_" + 
                    state.getRecognition() + "_" + state.getWandering();
                viabilityCache.remove(cacheKey);
            } catch (Exception e) {
                // Ignore feedback errors
            }
        }

        public void updateLearnerProfile(String key, double value) {
            if (key != null && !key.isEmpty()) {
                learnerProfile.put(key, value);
                // Clear cache after profile update
                viabilityCache.clear();
            }
        }

        public MathEmbeddingService.EmbeddingStats getEmbeddingStats() {
            if (embeddingService == null) {
                return null;
            }
            return embeddingService.getStats();
        }
    }

    /**
     * Meta-controller for attention drift monitoring and bias auditing
     */
    public static class MetaController {

        private final List<AttentionDrift> driftHistory;
        private final Map<String, Double> biasMetrics;
        private final AtomicReference<SystemHealth> systemHealth;

        public MetaController() {
            this.driftHistory = Collections.synchronizedList(new ArrayList<>());
            this.biasMetrics = new ConcurrentHashMap<>();
            this.systemHealth = new AtomicReference<>(SystemHealth.HEALTHY);
        }

        public MetaAnalysis analyze(
            List<ProcessingResult> results,
            MathExpression expression
        ) {
            // Detect attention drifts
            List<AttentionDrift> newDrifts = detectAttentionDrifts(results);
            driftHistory.addAll(newDrifts);

            // Audit bias patterns
            Map<String, Double> currentBias = auditBias(results, expression);
            biasMetrics.putAll(currentBias);

            // Assess system health
            SystemHealth health = assessSystemHealth(results, newDrifts);
            systemHealth.set(health);

            // Generate recommendations
            List<String> recommendations = generateRecommendations(
                health,
                newDrifts,
                currentBias
            );

            return new MetaAnalysis(
                newDrifts,
                currentBias,
                health,
                recommendations,
                System.currentTimeMillis()
            );
        }

        private List<AttentionDrift> detectAttentionDrifts(
            List<ProcessingResult> results
        ) {
            List<AttentionDrift> drifts = new ArrayList<>();

            for (int i = 1; i < results.size(); i++) {
                double prevAttention = results
                    .get(i - 1)
                    .getState()
                    .getAttention();
                double currAttention = results.get(i).getState().getAttention();
                double drift = Math.abs(currAttention - prevAttention);

                if (drift > 0.3) { // Significant attention change
                    drifts.add(
                        new AttentionDrift(
                            i,
                            drift,
                            currAttention < prevAttention
                                ? "DECREASE"
                                : "INCREASE",
                            System.currentTimeMillis()
                        )
                    );
                }
            }

            return drifts;
        }

        private Map<String, Double> auditBias(
            List<ProcessingResult> results,
            MathExpression expression
        ) {
            Map<String, Double> bias = new HashMap<>();

            // Check for notation bias
            double symbolicPreference = expression.getAlpha();
            if (symbolicPreference > 0.8) {
                bias.put("symbolic_bias", symbolicPreference);
            }

            // Check for complexity bias
            double avgComplexity = expression.getComplexityScore();
            if (avgComplexity > 0.7) {
                bias.put("complexity_bias", avgComplexity);
            }

            // Check for attention bias
            double avgAttention = results
                .stream()
                .mapToDouble(r -> r.getState().getAttention())
                .average()
                .orElse(0.5);
            if (avgAttention < 0.3) {
                bias.put("attention_bias", 1.0 - avgAttention);
            }

            return bias;
        }

        private SystemHealth assessSystemHealth(
            List<ProcessingResult> results,
            List<AttentionDrift> drifts
        ) {
            double avgCognitiveLoad = results
                .stream()
                .mapToDouble(ProcessingResult::getCognitiveLoad)
                .average()
                .orElse(0.5);

            int driftCount = drifts.size();
            double avgAttention = results
                .stream()
                .mapToDouble(r -> r.getState().getAttention())
                .average()
                .orElse(0.5);

            if (
                avgCognitiveLoad > 0.8 ||
                driftCount > results.size() * 0.5 ||
                avgAttention < 0.3
            ) {
                return SystemHealth.CRITICAL;
            } else if (
                avgCognitiveLoad > 0.6 ||
                driftCount > results.size() * 0.3 ||
                avgAttention < 0.5
            ) {
                return SystemHealth.WARNING;
            }

            return SystemHealth.HEALTHY;
        }

        private List<String> generateRecommendations(
            SystemHealth health,
            List<AttentionDrift> drifts,
            Map<String, Double> bias
        ) {
            List<String> recommendations = new ArrayList<>();

            switch (health) {
                case CRITICAL:
                    recommendations.add("RESTORE_ATTENTION");
                    recommendations.add("SIMPLIFY_NOTATION");
                    recommendations.add("REDUCE_COMPLEXITY");
                    break;
                case WARNING:
                    recommendations.add("ENHANCE_CLARITY");
                    if (bias.containsKey("symbolic_bias")) {
                        recommendations.add("REDUCE_BIAS");
                    }
                    break;
                case HEALTHY:
                    if (!drifts.isEmpty()) {
                        recommendations.add("MONITOR_ATTENTION");
                    }
                    break;
            }

            return recommendations;
        }
    }

    public static class AttentionDrift {

        private final int step;
        private final double magnitude;
        private final String direction;
        private final long timestamp;

        public AttentionDrift(
            int step,
            double magnitude,
            String direction,
            long timestamp
        ) {
            this.step = step;
            this.magnitude = magnitude;
            this.direction = direction;
            this.timestamp = timestamp;
        }

        public int getStep() {
            return step;
        }

        public double getMagnitude() {
            return magnitude;
        }

        public String getDirection() {
            return direction;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public enum SystemHealth {
        HEALTHY,
        WARNING,
        CRITICAL,
    }

    public static class MetaAnalysis {

        private final List<AttentionDrift> attentionDrifts;
        private final Map<String, Double> biasMetrics;
        private final SystemHealth systemHealth;
        private final List<String> recommendations;
        private final long timestamp;

        public MetaAnalysis(
            List<AttentionDrift> drifts,
            Map<String, Double> bias,
            SystemHealth health,
            List<String> recommendations,
            long timestamp
        ) {
            this.attentionDrifts = new ArrayList<>(drifts);
            this.biasMetrics = new HashMap<>(bias);
            this.systemHealth = health;
            this.recommendations = new ArrayList<>(recommendations);
            this.timestamp = timestamp;
        }

        public List<AttentionDrift> getAttentionDrifts() {
            return new ArrayList<>(attentionDrifts);
        }

        public Map<String, Double> getBiasMetrics() {
            return new HashMap<>(biasMetrics);
        }

        public SystemHealth getSystemHealth() {
            return systemHealth;
        }

        public List<String> getRecommendations() {
            return new ArrayList<>(recommendations);
        }

        public long getTimestamp() {
            return timestamp;
        }

        public double getAttentionDrift() {
            return attentionDrifts.stream()
                .mapToDouble(AttentionDrift::getMagnitude)
                .average()
                .orElse(0.0);
        }

        public double getSystemHealthScore() {
            switch (systemHealth) {
                case HEALTHY:
                    return 1.0;
                case WARNING:
                    return 0.5;
                case CRITICAL:
                    return 0.0;
                default:
                    return 0.0;
            }
        }

        @Override
        public String toString() {
            return String.format(
                "MetaAnalysis{drifts=%d, health=%s, recommendations=%d, timestamp=%d}",
                attentionDrifts.size(),
                systemHealth,
                recommendations.size(),
                timestamp
            );
        }
    }

    /**
     * Result of Ψ(x) optimization process
     */
    public static class PsiOptimizationResult {

        private final double psiValue;
        private final String optimizedExpression;
        private final double symbolicOutput;
        private final double neuralOutput;
        private final double alpha;
        private final double cognitivePenalty;
        private final double efficiencyPenalty;
        private final double biasedProbability;
        private final MetaAnalysis metaAnalysis;
        private final Map<String, Double> cognitiveTags;

        public PsiOptimizationResult(
            double psiValue,
            String optimizedExpression,
            double symbolicOutput,
            double neuralOutput,
            double alpha,
            double cognitivePenalty,
            double efficiencyPenalty,
            double biasedProbability,
            MetaAnalysis metaAnalysis,
            Map<String, Double> cognitiveTags
        ) {
            this.psiValue = psiValue;
            this.optimizedExpression = optimizedExpression;
            this.symbolicOutput = symbolicOutput;
            this.neuralOutput = neuralOutput;
            this.alpha = alpha;
            this.cognitivePenalty = cognitivePenalty;
            this.efficiencyPenalty = efficiencyPenalty;
            this.biasedProbability = biasedProbability;
            this.metaAnalysis = metaAnalysis;
            this.cognitiveTags = new HashMap<>(cognitiveTags);
        }

        // Getters
        public double getPsiValue() {
            return psiValue;
        }

        public String getOptimizedExpression() {
            return optimizedExpression;
        }

        public double getSymbolicOutput() {
            return symbolicOutput;
        }

        public double getNeuralOutput() {
            return neuralOutput;
        }

        public double getAlpha() {
            return alpha;
        }

        public double getCognitivePenalty() {
            return cognitivePenalty;
        }

        public double getEfficiencyPenalty() {
            return efficiencyPenalty;
        }

        public double getBiasedProbability() {
            return biasedProbability;
        }

        public MetaAnalysis getMetaAnalysis() {
            return metaAnalysis;
        }

        public Map<String, Double> getCognitiveTags() {
            return new HashMap<>(cognitiveTags);
        }

        public double getOptimizationScore() {
            return psiValue;
        }

        @Override
        public String toString() {
            return String.format(
                "PsiOptimizationResult{psi=%.3f, optimized='%s', alpha=%.3f, health=%s}",
                psiValue,
                optimizedExpression,
                alpha,
                metaAnalysis.getSystemHealth()
            );
        }
    }

    private void addOperationNodes(String expr) {
        if (expr.contains("∪")) lexicalNetwork.addNode(
            "union",
            Map.of("operation", 1.0, "complexity", 0.3, "familiarity", 0.8)
        );
        if (expr.contains("∩")) lexicalNetwork.addNode(
            "intersection",
            Map.of("operation", 1.0, "complexity", 0.3, "familiarity", 0.8)
        );
        if (expr.contains("×")) lexicalNetwork.addNode(
            "cartesian_product",
            Map.of("operation", 1.0, "complexity", 0.6, "familiarity", 0.6)
        );
        if (
            expr.contains("power") || expr.contains("P(")
        ) lexicalNetwork.addNode(
            "power_set",
            Map.of("operation", 1.0, "complexity", 0.8, "familiarity", 0.4)
        );
    }

    // Add missing methods to match demo calls
    public double calculateLexicalViability(String expr, ArrayList<Object> results) {
        return viabilityComponent.calculateViability(
            expr, 
            processingState, 
            results != null ? 
                results.stream()
                    .filter(r -> r instanceof ProcessingResult)
                    .map(r -> (ProcessingResult) r)
                    .collect(Collectors.toList()) : 
                new ArrayList<>()
        );
    }

    public List<String> getNotationSuggestions(String expr) {
        LexicalSuggestion suggestion = lexicalNetwork.getSuggestion(expr);
        return Collections.singletonList(suggestion.getSuggestion());
    }

    public String optimizeExpression(String expr) {
        return viabilityComponent.optimizeNotation(expr, processingState);
    }

    public double getOptimizationScore(String expr) {
        return viabilityComponent.calculateViability(expr, processingState, new ArrayList<>());
    }

    public MetaAnalysis analyzeMetaController(String expr) {
        try {
            List<ProcessingResult> results = cognitiveFramework.process(expr, 5);
            return metaController.analyze(results, this);
        } catch (Exception e) {
            // Log error or handle appropriately
            return new MetaAnalysis(
                Collections.emptyList(), 
                Collections.emptyMap(), 
                SystemHealth.WARNING, 
                Collections.singletonList("Error in meta-analysis"), 
                System.currentTimeMillis()
            );
        }
    }

    public void restoreAttention() {
        // Reset cognitive state and attention parameters
        processingState = new CognitiveState(0.5, 0.5, 0.1);
        alpha = 0.6; // Reset symbolic preference
        beta = 1.2; // Reset bias parameter
        
        // Clear any accumulated cognitive tags related to attention drift
        cognitiveTags.remove("attentionDrift");
        cognitiveTags.remove("processingError");
    }

    public PsiOptimizationResult optimizePsi(String expr) {
        try {
            // Perform Psi optimization using the cognitive framework
            List<ProcessingResult> results = cognitiveFramework.process(expr, 5);
            
            // Calculate symbolic and neural outputs
            double symbolicOutput = results.stream()
                .mapToDouble(result -> {
                    // Use the existing methods in the class to calculate symbolic output
                    return calculateSymbolicOutput();
                })
                .average()
                .orElse(0.0);
            
            double neuralOutput = results.stream()
                .mapToDouble(result -> {
                    // Use the existing methods in the class to calculate neural output
                    return calculateNeuralOutput(results);
                })
                .average()
                .orElse(0.0);
            
            // Calculate Psi value with cognitive and efficiency penalties
            double psiValue = alpha * symbolicOutput + (1 - alpha) * neuralOutput 
                - beta * (cognitivePenalty + efficiencyPenalty);
            
            // Optimize the expression notation
            String optimizedExpression = viabilityComponent.optimizeNotation(expr, processingState);
            
            // Create and return the optimization result
            return new PsiOptimizationResult(
                psiValue,
                optimizedExpression,
                symbolicOutput,
                neuralOutput,
                alpha,
                cognitivePenalty,
                efficiencyPenalty,
                biasedProbability,
                metaController.analyze(results, this),
                cognitiveTags
            );
        } catch (Exception e) {
            // Handle optimization failure
            throw new RuntimeException("Psi optimization failed", e);
        }
    }

    public List<ProcessingResult> processWithCognitiveFramework(String expr) {
        try {
            // Use the existing cognitive framework to process the expression
            return cognitiveFramework.process(expr, 5);
        } catch (Exception e) {
            // Log error or handle appropriately
            System.err.println("Error processing expression with cognitive framework: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
