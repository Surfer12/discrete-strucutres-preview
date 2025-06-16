package edu.ucsb.cs.cognitivedm.demo;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.framework.MetaAwarenessProcessor;
import edu.ucsb.cs.cognitivedm.framework.CognitiveBiasAdjuster;
import edu.ucsb.cs.cognitivedm.math.CognitiveExpressionParser;
import edu.ucsb.cs.cognitivedm.math.CognitiveExpressionParser.CognitiveParsingResult;
import edu.ucsb.cs.cognitivedm.math.CognitiveExpressionParser.CognitiveInsight;
import edu.ucsb.cs.cognitivedm.patterns.PatternDetector;
import com.discretelogic.model.Expression;

import java.util.*;

/**
 * CognitiveMathDemo - Demonstration of the integrated cognitive-inspired discrete mathematics system
 *
 * This demo showcases the key features of our cognitive framework:
 * 1. Fractal pattern analysis with Hurst exponent calculation
 * 2. Attention-recognition decoupling framework
 * 3. Cognitive expression parsing with bias modeling
 * 4. Meta-awareness processing and recommendations
 * 5. Adaptive parsing strategies based on cognitive load
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class CognitiveMathDemo {

    private static final String SEPARATOR = "=".repeat(80);
    private static final String SUBSEPARATOR = "-".repeat(50);

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("COGNITIVE-INSPIRED DISCRETE MATHEMATICS SYSTEM DEMO");
        System.out.println(SEPARATOR);

        CognitiveMathDemo demo = new CognitiveMathDemo();

        try {
            // Demo 1: Basic Cognitive Expression Parsing
            demo.demonstrateBasicCognitiveParsing();

            // Demo 2: Fractal Pattern Analysis
            demo.demonstrateFractalPatternAnalysis();

            // Demo 3: Attention-Recognition Framework
            demo.demonstrateAttentionRecognitionFramework();

            // Demo 4: Cognitive Bias Modeling
            demo.demonstrateCognitiveBiasModeling();

            // Demo 5: Meta-Awareness Processing
            demo.demonstrateMetaAwarenesProcessing();

            // Demo 6: Adaptive Parsing Strategies
            demo.demonstrateAdaptiveParsingStrategies();

            // Demo 7: Integrated System Performance
            demo.demonstrateIntegratedSystemPerformance();

        } catch (Exception e) {
            System.err.println("Demo error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println(SEPARATOR);
        System.out.println("DEMO COMPLETED SUCCESSFULLY");
        System.out.println(SEPARATOR);
    }

    /**
     * Demonstrate basic cognitive expression parsing
     */
    public void demonstrateBasicCognitiveParsing() {
        System.out.println("\n1. BASIC COGNITIVE EXPRESSION PARSING");
        System.out.println(SUBSEPARATOR);

        CognitiveExpressionParser parser = new CognitiveExpressionParser();

        // Test expressions of varying complexity
        String[] testExpressions = {
            "A & B",                                    // Simple
            "(A | B) & (C | D)",                       // Medium
            "((A & B) | (C & D)) -> ((E | F) & G)",   // Complex
            "!(((A | B) & C) -> (D & (E | F)))",      // Very Complex
            "A <-> B",                                 // Biconditional
            "(A & B & C) | (D & E & F) | (G & H)"     // Multi-clause
        };

        for (String expr : testExpressions) {
            System.out.println("\nExpression: " + expr);

            CognitiveParsingResult result = parser.parseExpression(expr);

            System.out.printf("  Cognitive Load: %.3f\n", result.getCognitiveLoad());
            System.out.printf("  Cognitive Difficulty: %.3f\n", result.getCognitiveDifficulty());
            System.out.printf("  Requires High Attention: %s\n", result.requiresHighAttention());
            System.out.printf("  Variables: %s\n", result.getVariables());
            System.out.printf("  Structural Complexity: %.3f\n",
                result.getComplexityMetrics().getStructuralComplexity());
            System.out.printf("  Working Memory Load: %.3f\n",
                result.getComplexityMetrics().getWorkingMemoryLoad());

            // Show insights
            if (!result.getInsights().isEmpty()) {
                System.out.println("  Cognitive Insights:");
                for (CognitiveInsight insight : result.getInsights()) {
                    System.out.printf("    - %s: %s (confidence: %.2f)\n",
                        insight.getType(), insight.getDescription(), insight.getConfidence());
                    System.out.printf("      Recommendation: %s\n", insight.getRecommendation());
                }
            }
        }

        parser.shutdown();
    }

    /**
     * Demonstrate fractal pattern analysis
     */
    public void demonstrateFractalPatternAnalysis() {
        System.out.println("\n2. FRACTAL PATTERN ANALYSIS");
        System.out.println(SUBSEPARATOR);

        PatternDetector detector = new PatternDetector(1);

        // Generate synthetic time series with different characteristics
        List<double[]> persistentSeries = generatePersistentTimeSeries(50);
        List<double[]> fractalSeries = generateFractalTimeSeries(50);
        List<double[]> randomSeries = generateRandomTimeSeries(50);

        System.out.println("Analyzing Persistent Time Series:");
        analyzeAndDisplayPatterns(detector, persistentSeries);

        System.out.println("\nAnalyzing Fractal Time Series:");
        analyzeAndDisplayPatterns(detector, fractalSeries);

        System.out.println("\nAnalyzing Random Time Series:");
        analyzeAndDisplayPatterns(detector, randomSeries);
    }

    /**
     * Demonstrate attention-recognition framework
     */
    public void demonstrateAttentionRecognitionFramework() {
        System.out.println("\n3. ATTENTION-RECOGNITION FRAMEWORK");
        System.out.println(SUBSEPARATOR);

        AttentionRecognitionFramework framework = new AttentionRecognitionFramework(3);

        // Simulate different cognitive scenarios
        Object[] inputs = {
            "simple task",           // Low cognitive demand
            "moderate complexity",   // Medium demand
            "very complex problem with multiple nested components", // High demand
            null,                   // No input (mind wandering scenario)
            "sudden interruption!"  // Attention disruption
        };

        for (int i = 0; i < inputs.length; i++) {
            System.out.printf("\nScenario %d: %s\n", i + 1,
                inputs[i] != null ? inputs[i].toString() : "No input (mind wandering)");

            List<AttentionRecognitionFramework.ProcessingResult> results =
                framework.process(inputs[i], 5);

            // Analyze results across scales
            for (int scale = 0; scale < 3; scale++) {
                AttentionRecognitionFramework.ProcessingResult result = results.get(scale * 5); // Get first result for each scale
                AttentionRecognitionFramework.CognitiveState state = result.getState();

                System.out.printf("  Scale %d - Attention: %.3f, Recognition: %.3f, Wandering: %.3f, Load: %.3f\n",
                    scale, state.getAttention(), state.getRecognition(),
                    state.getWandering(), result.getCognitiveLoad());
            }

            // Display detected patterns
            int totalPatterns = results.stream()
                .mapToInt(r -> r.getDetectedPatterns().size())
                .sum();

            if (totalPatterns > 0) {
                System.out.printf("  Total Patterns Detected: %d\n", totalPatterns);
            }
        }

        // Get meta-assessment
        MetaAwarenessProcessor.MetaAssessment assessment = framework.getMetaAssessment();
        if (assessment != null) {
            System.out.println("\nMeta-Cognitive Assessment:");
            System.out.printf("  Meta-Awareness Level: %.3f\n", assessment.getMetaAwarenessLevel());
            System.out.printf("  Cognitive Coherence: %.3f\n", assessment.getCognitiveCoherence());
            System.out.printf("  Attentional Stability: %.3f\n", assessment.getAttentionalStability());
            System.out.printf("  Cognitive Wellness Score: %.3f\n", assessment.getCognitiveWellnessScore());

            if (!assessment.getDetectedPhenomena().isEmpty()) {
                System.out.println("  Detected Phenomena: " + assessment.getDetectedPhenomena());
            }
        }

        framework.shutdown();
    }

    /**
     * Demonstrate cognitive bias modeling
     */
    public void demonstrateCognitiveBiasModeling() {
        System.out.println("\n4. COGNITIVE BIAS MODELING");
        System.out.println(SUBSEPARATOR);

        CognitiveBiasAdjuster biasAdjuster = new CognitiveBiasAdjuster();

        // Test different cognitive profiles
        String[] profiles = {"conservative", "optimistic", "analytical", "intuitive"};
        double baseProb = 0.6;

        System.out.printf("Base Probability: %.3f\n", baseProb);

        for (String profile : profiles) {
            biasAdjuster.applyCognitiveProfile(profile);

            // Test with different contexts
            Map<String, Object> evidence = new HashMap<>();
            evidence.put("cognitive_load", 0.7);
            evidence.put("attention_level", 0.5);

            Map<String, Object> context = new HashMap<>();
            context.put("confirms_expectation", true);
            context.put("frame_type", "positive");
            context.put("recency_score", 0.8);

            double biasedProb = biasAdjuster.calculateBiasedProbability(baseProb, evidence, context);

            System.out.printf("\n%s Profile:\n", profile.toUpperCase());
            System.out.printf("  Biased Probability: %.3f (adjustment: %+.3f)\n",
                biasedProb, biasedProb - baseProb);
            System.out.printf("  Overall Bias Intensity: %.3f\n",
                biasAdjuster.calculateOverallBiasIntensity());

            // Show active biases
            List<CognitiveBiasAdjuster.BiasModel> activeBiases = biasAdjuster.getActiveBiases();
            System.out.printf("  Active Biases: %d\n", activeBiases.size());
            for (CognitiveBiasAdjuster.BiasModel bias : activeBiases) {
                if (bias.getStrength() > 1.2) { // Only show strong biases
                    System.out.printf("    - %s: %.2f\n", bias.getName(), bias.getStrength());
                }
            }
        }

        // Generate bias report
        biasAdjuster.resetToDefaults();
        System.out.println("\nBias Report:");
        System.out.println(biasAdjuster.generateBiasReport());
    }

    /**
     * Demonstrate meta-awareness processing
     */
    public void demonstrateMetaAwarenesProcessing() {
        System.out.println("\n5. META-AWARENESS PROCESSING");
        System.out.println(SUBSEPARATOR);

        AttentionRecognitionFramework framework = new AttentionRecognitionFramework(3);

        // Simulate a sequence of cognitive states that might trigger meta-awareness
        Object[] sequence = {
            "focused task",
            "continuing focus",
            "distraction appears",
            "mind starts wandering",
            "deep mind wandering",
            "sudden awareness",
            "refocusing attempt",
            "restored attention"
        };

        System.out.println("Simulating cognitive state sequence:");

        for (int i = 0; i < sequence.length; i++) {
            System.out.printf("\nStep %d: %s\n", i + 1, sequence[i]);

            List<AttentionRecognitionFramework.ProcessingResult> results =
                framework.process(sequence[i], 3);

            MetaAwarenessProcessor.MetaAssessment assessment = framework.getMetaAssessment();
            if (assessment != null) {
                System.out.printf("  Meta-Awareness: %.3f\n", assessment.getMetaAwarenessLevel());
                System.out.printf("  Cognitive Coherence: %.3f\n", assessment.getCognitiveCoherence());
                System.out.printf("  Attentional Stability: %.3f\n", assessment.getAttentionalStability());
                System.out.printf("  Mind Wandering: %.3f\n", assessment.getMindWanderingIntensity());
                System.out.printf("  Wellness Score: %.3f\n", assessment.getCognitiveWellnessScore());

                if (!assessment.getDetectedPhenomena().isEmpty()) {
                    System.out.println("  Phenomena: " + assessment.getDetectedPhenomena());
                }
            }
        }

        framework.shutdown();
    }

    /**
     * Demonstrate adaptive parsing strategies
     */
    public void demonstrateAdaptiveParsingStrategies() {
        System.out.println("\n6. ADAPTIVE PARSING STRATEGIES");
        System.out.println(SUBSEPARATOR);

        CognitiveExpressionParser parser = new CognitiveExpressionParser();

        // Test expressions with different cognitive profiles
        String complexExpression = "((A & B & C) | (D & E & F)) -> ((G | H) & (I | J | K))";

        String[] profiles = {"conservative", "optimistic", "analytical", "intuitive"};

        for (String profile : profiles) {
            System.out.printf("\nTesting with %s cognitive profile:\n", profile);
            parser.setCognitiveProfile(profile);

            CognitiveParsingResult result = parser.parseExpression(complexExpression);

            System.out.printf("  Cognitive Load: %.3f\n", result.getCognitiveLoad());
            System.out.printf("  Difficulty Assessment: %.3f\n", result.getCognitiveDifficulty());
            System.out.printf("  Attention Support Needed: %s\n",
                result.requiresHighAttention() ? "YES" : "NO");

            // Show adaptive recommendations
            for (CognitiveInsight insight : result.getInsights()) {
                if (insight.getType().contains("complexity") ||
                    insight.getType().contains("attention") ||
                    insight.getType().contains("memory")) {
                    System.out.printf("  Strategy: %s\n", insight.getRecommendation());
                }
            }

            // Check metadata for suggested strategies
            Map<String, Object> metadata = result.getMetadata();
            if (metadata.containsKey("suggested_strategy")) {
                System.out.printf("  Suggested Strategy: %s\n", metadata.get("suggested_strategy"));
            }
        }

        parser.shutdown();
    }

    /**
     * Demonstrate integrated system performance
     */
    public void demonstrateIntegratedSystemPerformance() {
        System.out.println("\n7. INTEGRATED SYSTEM PERFORMANCE");
        System.out.println(SUBSEPARATOR);

        // Create comprehensive test scenario
        CognitiveExpressionParser parser = new CognitiveExpressionParser();

        // Complex nested expression that should trigger multiple cognitive phenomena
        String integrationTest = "(((A & B) | C) -> ((D | E) & F)) <-> (!(G & H) | (I & J & K))";

        System.out.println("Complex Integration Test Expression:");
        System.out.println(integrationTest);

        long startTime = System.currentTimeMillis();
        CognitiveParsingResult result = parser.parseExpression(integrationTest);
        long endTime = System.currentTimeMillis();

        System.out.printf("\nProcessing Time: %d ms\n", endTime - startTime);
        System.out.printf("Processing Steps: %d\n", result.getCognitiveAnalysis().size());

        System.out.println("\nCOMPREHENSIVE ANALYSIS:");
        System.out.println("Expression Components:");
        System.out.printf("  Variables: %s\n", result.getVariables());
        System.out.printf("  AST Type: %s\n", result.getAST().getClass().getSimpleName());

        System.out.println("\nCognitive Metrics:");
        System.out.printf("  Overall Cognitive Load: %.3f\n", result.getCognitiveLoad());
        System.out.printf("  Cognitive Difficulty: %.3f\n", result.getCognitiveDifficulty());
        System.out.printf("  Structural Complexity: %.3f\n",
            result.getComplexityMetrics().getStructuralComplexity());
        System.out.printf("  Semantic Complexity: %.3f\n",
            result.getComplexityMetrics().getSemanticComplexity());
        System.out.printf("  Working Memory Load: %.3f\n",
            result.getComplexityMetrics().getWorkingMemoryLoad());
        System.out.printf("  Attentional Demand: %.3f\n",
            result.getComplexityMetrics().getAttentionalDemand());

        System.out.println("\nStructural Analysis:");
        System.out.printf("  Nesting Depth: %d\n", result.getComplexityMetrics().getNestingDepth());
        System.out.printf("  Operator Count: %d\n", result.getComplexityMetrics().getOperatorCount());
        System.out.printf("  Variable Count: %d\n", result.getComplexityMetrics().getVariableCount());

        System.out.println("\nCognitive Insights and Recommendations:");
        if (result.getInsights().isEmpty()) {
            System.out.println("  No specific insights generated");
        } else {
            for (CognitiveInsight insight : result.getInsights()) {
                System.out.printf("  %s (%.0f%% confidence):\n",
                    insight.getType().toUpperCase(), insight.getConfidence() * 100);
                System.out.printf("    Description: %s\n", insight.getDescription());
                System.out.printf("    Recommendation: %s\n", insight.getRecommendation());
            }
        }

        System.out.println("\nMulti-Scale Cognitive Analysis:");
        List<AttentionRecognitionFramework.ProcessingResult> cognitiveResults = result.getCognitiveAnalysis();

        // Group by scale and show summary
        Map<Integer, List<AttentionRecognitionFramework.ProcessingResult>> byScale = new HashMap<>();
        for (AttentionRecognitionFramework.ProcessingResult cr : cognitiveResults) {
            byScale.computeIfAbsent(cr.getScale(), k -> new ArrayList<>()).add(cr);
        }

        for (Map.Entry<Integer, List<AttentionRecognitionFramework.ProcessingResult>> entry : byScale.entrySet()) {
            int scale = entry.getKey();
            List<AttentionRecognitionFramework.ProcessingResult> scaleResults = entry.getValue();

            double avgAttention = scaleResults.stream()
                .mapToDouble(r -> r.getState().getAttention())
                .average().orElse(0.0);
            double avgCogLoad = scaleResults.stream()
                .mapToDouble(AttentionRecognitionFramework.ProcessingResult::getCognitiveLoad)
                .average().orElse(0.0);

            System.out.printf("  Scale %d: Avg Attention=%.3f, Avg Cognitive Load=%.3f\n",
                scale, avgAttention, avgCogLoad);
        }

        // Performance and quality assessment
        System.out.println("\nSYSTEM PERFORMANCE ASSESSMENT:");
        System.out.printf("  High Cognitive Load: %s\n",
            result.getCognitiveLoad() > 0.7 ? "YES - Intervention recommended" : "NO");
        System.out.printf("  Attention Support Needed: %s\n",
            result.requiresHighAttention() ? "YES" : "NO");
        System.out.printf("  Processing Efficiency: %s\n",
            (endTime - startTime) < 100 ? "EXCELLENT" : "GOOD");

        parser.shutdown();
    }

    // Helper methods for generating synthetic data

    private List<double[]> generatePersistentTimeSeries(int length) {
        List<double[]> series = new ArrayList<>();
        Random random = new Random(42); // Fixed seed for reproducibility
        double value = 0.5;

        for (int i = 0; i < length; i++) {
            // Persistent: current value influences next value
            value = 0.3 * value + 0.7 * random.nextGaussian() * 0.1 + 0.5;
            value = Math.max(0.0, Math.min(1.0, value)); // Bound to [0,1]

            series.add(new double[]{
                value,                              // attention
                value * 0.8 + 0.2 * random.nextDouble(), // recognition
                (1.0 - value) * 0.3 + 0.1          // wandering
            });
        }
        return series;
    }

    private List<double[]> generateFractalTimeSeries(int length) {
        List<double[]> series = new ArrayList<>();
        Random random = new Random(123);

        // Generate self-similar patterns at different scales
        for (int i = 0; i < length; i++) {
            double t = (double) i / length;

            // Multi-scale fractal pattern
            double value = 0.5 + 0.3 * Math.sin(2 * Math.PI * t) +
                          0.15 * Math.sin(4 * Math.PI * t) +
                          0.075 * Math.sin(8 * Math.PI * t) +
                          0.05 * random.nextGaussian();

            value = Math.max(0.0, Math.min(1.0, value));

            series.add(new double[]{
                value,
                value * 0.9 + 0.1 * random.nextDouble(),
                0.2 + 0.3 * (1.0 - value)
            });
        }
        return series;
    }

    private List<double[]> generateRandomTimeSeries(int length) {
        List<double[]> series = new ArrayList<>();
        Random random = new Random(456);

        for (int i = 0; i < length; i++) {
            series.add(new double[]{
                random.nextDouble(),
                random.nextDouble(),
                random.nextDouble() * 0.5
            });
        }
        return series;
    }

    private void analyzeAndDisplayPatterns(PatternDetector detector, List<double[]> timeSeries) {
        List<PatternDetector.Pattern> patterns = detector.analyzeSequence(timeSeries);

        if (patterns.isEmpty()) {
            System.out.println("  No significant patterns detected");
            return;
        }

        System.out.printf("  Detected %d patterns:\n", patterns.size());
        for (PatternDetector.Pattern pattern : patterns) {
            System.out.printf("    - %s: %s (confidence: %.3f)\n",
                pattern.getType(), pattern.getDescription(), pattern.getConfidence());

            // Show key characteristics
            Map<String, Double> characteristics = pattern.getCharacteristics();
            if (!characteristics.isEmpty()) {
                System.out.print("      Characteristics: ");
                characteristics.entrySet().stream()
                    .forEach(entry -> System.out.printf("%s=%.3f ", entry.getKey(), entry.getValue()));
                System.out.println();
            }
        }
    }
}
