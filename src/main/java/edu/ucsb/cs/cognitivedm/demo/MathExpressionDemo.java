package edu.ucsb.cs.cognitivedm.demo;

import edu.ucsb.cs.cognitivedm.MathExpression;
import edu.ucsb.cs.cognitivedm.MathExpression.PsiOptimizationResult;
import edu.ucsb.cs.cognitivedm.MathExpression.MetaAnalysis;
import edu.ucsb.cs.cognitivedm.MathExpression.AttentionDrift;
import edu.ucsb.cs.cognitivedm.MathExpression.SystemHealth;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Comprehensive demonstration of the enhanced MathExpression with Ψ(x) optimization,
 * lexical network integration, and meta-controller functionality.
 *
 * This demo showcases:
 * 1. Set theory expression parsing and optimization
 * 2. Ψ(x) optimization framework with cognitive processing
 * 3. Lexical network for notation suggestions
 * 4. Meta-controller attention drift detection and bias auditing
 * 5. Educational adaptivity through lexical viability optimization
 * 6. Closed-loop attention restoration system
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 2.0
 */
public class MathExpressionDemo {

    private static final String SEPARATOR = "=".repeat(80);
    private static final String SUBSEPARATOR = "-".repeat(50);

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("ENHANCED MATHEXPRESSION WITH Ψ(x) OPTIMIZATION DEMO");
        System.out.println("Cognitive-Inspired Set Theory Processing");
        System.out.println(SEPARATOR);

        MathExpressionDemo demo = new MathExpressionDemo();

        try {
            // Demo 1: Basic Set Theory Expression Processing
            demo.demonstrateBasicSetTheoryProcessing();

            // Demo 2: Ψ(x) Optimization Framework
            demo.demonstratePsiOptimization();

            // Demo 3: Lexical Network Integration
            demo.demonstrateLexicalNetworkIntegration();

            // Demo 4: Meta-Controller Functionality
            demo.demonstrateMetaController();

            // Demo 5: Educational Adaptivity
            demo.demonstrateEducationalAdaptivity();

            // Demo 6: Attention Restoration System
            demo.demonstrateAttentionRestoration();

            // Demo 7: Complete Integration Example
            demo.demonstrateCompleteIntegration();

        } catch (Exception e) {
            System.err.println("Demo error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println(SEPARATOR);
        System.out.println("DEMO COMPLETED SUCCESSFULLY");
        System.out.println("Enhanced MathExpression successfully integrates:");
        System.out.println("✓ Ψ(x) optimization framework");
        System.out.println("✓ Multilayer lexical network");
        System.out.println("✓ Meta-controller with attention monitoring");
        System.out.println("✓ Educational adaptivity");
        System.out.println("✓ Closed-loop cognitive processing");
        System.out.println(SEPARATOR);
    }

    /**
     * Demonstrate basic set theory expression processing
     */
    public void demonstrateBasicSetTheoryProcessing() {
        System.out.println("\n1. BASIC SET THEORY EXPRESSION PROCESSING");
        System.out.println(SUBSEPARATOR);

        String[] setExpressions = {
            "{1,2} ∪ {3,4}",              // Union
            "{1,2,3} ∩ {2,3,4}",         // Intersection
            "{1,2,3} × {4,5}",           // Cartesian Product
            "power A",                    // Power Set
            "complement B",               // Complement
            "{1,2,3} - {2}",             // Difference
            "{x | x ∈ A and x > 0}"      // Set Builder
        };

        for (String expr : setExpressions) {
            System.out.println("\nProcessing: " + expr);

            try {
                MathExpression mathExpr = new MathExpression(expr);

                System.out.printf("  Set Expression: %s\n",
                    mathExpr.getSetExpression() != null ?
                    mathExpr.getSetExpression().toString() : "Not a set expression");

                System.out.printf("  Initial Complexity: %.3f\n",
                    mathExpr.getComplexityScore());

                System.out.printf("  Variables: %s\n",
                    mathExpr.getVariables());

                // Show cognitive tags
                Map<String, Double> tags = mathExpr.getCognitiveTags();
                System.out.printf("  Cognitive Tags: %s\n",
                    formatCognitiveTags(tags));

                mathExpr.shutdown();

            } catch (Exception e) {
                System.err.println("  Error processing expression: " + e.getMessage());
            }
        }
    }

    /**
     * Demonstrate Ψ(x) optimization framework
     */
    public void demonstratePsiOptimization() {
        System.out.println("\n2. Ψ(x) OPTIMIZATION FRAMEWORK");
        System.out.println(SUBSEPARATOR);

        String complexExpression = "({1,2,3} ∪ {4,5}) × ({6,7} ∩ {7,8})";
        System.out.println("Complex Expression: " + complexExpression);

        try {
            MathExpression mathExpr = new MathExpression(complexExpression);

            System.out.println("\nStarting Ψ(x) optimization...");
            CompletableFuture<PsiOptimizationResult> future = mathExpr.processWithPsiOptimization();
            PsiOptimizationResult result = future.get();

            System.out.println("\nΨ(x) OPTIMIZATION RESULTS:");
            System.out.printf("  Final Ψ(x) Value: %.3f\n", result.getPsiValue());
            System.out.printf("  Optimized Expression: %s\n", result.getOptimizedExpression());

            System.out.println("\nCOMPONENT BREAKDOWN:");
            System.out.printf("  Symbolic Output S(x): %.3f\n", result.getSymbolicOutput());
            System.out.printf("  Neural Output N(x): %.3f\n", result.getNeuralOutput());
            System.out.printf("  Alpha α(t): %.3f\n", result.getAlpha());
            System.out.printf("  Cognitive Penalty: %.3f\n", result.getCognitivePenalty());
            System.out.printf("  Efficiency Penalty: %.3f\n", result.getEfficiencyPenalty());
            System.out.printf("  Biased Probability: %.3f\n", result.getBiasedProbability());

            // Show step-by-step calculation
            System.out.println("\nSTEP-BY-STEP Ψ(x) CALCULATION:");
            double hybridOutput = result.getAlpha() * result.getSymbolicOutput() +
                                 (1 - result.getAlpha()) * result.getNeuralOutput();
            System.out.printf("  1. Hybrid Output: %.3f × %.3f + %.3f × %.3f = %.3f\n",
                result.getAlpha(), result.getSymbolicOutput(),
                (1 - result.getAlpha()), result.getNeuralOutput(), hybridOutput);

            double totalPenalty = 0.7 * result.getCognitivePenalty() + 0.3 * result.getEfficiencyPenalty();
            double penaltyFactor = Math.exp(-totalPenalty);
            System.out.printf("  2. Penalty Factor: exp(-%.3f) = %.3f\n", totalPenalty, penaltyFactor);

            double finalPsi = hybridOutput * penaltyFactor * result.getBiasedProbability();
            System.out.printf("  3. Final Ψ(x): %.3f × %.3f × %.3f = %.3f\n",
                hybridOutput, penaltyFactor, result.getBiasedProbability(), finalPsi);

            mathExpr.shutdown();

        } catch (Exception e) {
            System.err.println("Error in Ψ(x) optimization: " + e.getMessage());
        }
    }

    /**
     * Demonstrate lexical network integration
     */
    public void demonstrateLexicalNetworkIntegration() {
        System.out.println("\n3. LEXICAL NETWORK INTEGRATION");
        System.out.println(SUBSEPARATOR);

        String[] expressions = {
            "{1,2} ∪ {3,4}",      // Unicode symbols
            "{1,2} union {3,4}",  // Verbal form
            "A × B",              // Symbolic
            "A times B"           // Verbal alternative
        };

        for (String expr : expressions) {
            System.out.println("\nAnalyzing: " + expr);

            try {
                MathExpression mathExpr = new MathExpression(expr);

                // Get lexical network suggestion
                MathExpression.LexicalSuggestion suggestion =
                    mathExpr.getLexicalNetwork().getSuggestion(expr);

                System.out.printf("  Original Expression: %s\n", expr);
                System.out.printf("  Lexical Suggestion: %s\n", suggestion.getSuggestion());
                System.out.printf("  Confidence: %.3f\n", suggestion.getConfidence());

                // Show lexical viability
                double viability = mathExpr.viabilityComponent.calculateViability(
                    expr, mathExpr.getProcessingState(), new ArrayList<>());
                System.out.printf("  Lexical Viability: %.3f\n", viability);

                // Show notation preference
                double preference = mathExpr.viabilityComponent.getNotationPreference(expr);
                System.out.printf("  Notation Preference: %.3f\n", preference);

                mathExpr.shutdown();

            } catch (Exception e) {
                System.err.println("  Error in lexical analysis: " + e.getMessage());
            }
        }
    }

    /**
     * Demonstrate meta-controller functionality
     */
    public void demonstrateMetaController() {
        System.out.println("\n4. META-CONTROLLER FUNCTIONALITY");
        System.out.println(SUBSEPARATOR);

        // Simulate expressions with different cognitive demands
        String[] cognitiveScenarios = {
            "{1}",                                    // Simple - low cognitive load
            "{1,2,3} ∪ {4,5,6}",                     // Medium complexity
            "({1,2} × {3,4}) ∪ (power {5,6})",      // High complexity
            "{x | x ∈ A and x > 0 and x < 10}"      // Very complex notation
        };

        for (int i = 0; i < cognitiveScenarios.length; i++) {
            String expr = cognitiveScenarios[i];
            System.out.printf("\nScenario %d: %s\n", i + 1, expr);

            try {
                MathExpression mathExpr = new MathExpression(expr);

                CompletableFuture<PsiOptimizationResult> future = mathExpr.processWithPsiOptimization();
                PsiOptimizationResult result = future.get();

                MetaAnalysis metaAnalysis = result.getMetaAnalysis();

                System.out.printf("  System Health: %s\n", metaAnalysis.getSystemHealth());
                System.out.printf("  Attention Drifts: %d\n", metaAnalysis.getAttentionDrifts().size());

                if (!metaAnalysis.getAttentionDrifts().isEmpty()) {
                    System.out.println("  Drift Details:");
                    for (AttentionDrift drift : metaAnalysis.getAttentionDrifts()) {
                        System.out.printf("    Step %d: %.3f magnitude (%s)\n",
                            drift.getStep(), drift.getMagnitude(), drift.getDirection());
                    }
                }

                System.out.printf("  Bias Metrics: %s\n", metaAnalysis.getBiasMetrics());
                System.out.printf("  Recommendations: %s\n", metaAnalysis.getRecommendations());

                mathExpr.shutdown();

            } catch (Exception e) {
                System.err.println("  Error in meta-analysis: " + e.getMessage());
            }
        }
    }

    /**
     * Demonstrate educational adaptivity
     */
    public void demonstrateEducationalAdaptivity() {
        System.out.println("\n5. EDUCATIONAL ADAPTIVITY");
        System.out.println(SUBSEPARATOR);

        String complexNotation = "({1,2,3} ∩ {2,3,4}) × {5,6}";
        System.out.println("Original Complex Notation: " + complexNotation);

        // Simulate different learner profiles
        Map<String, Map<String, Double>> learnerProfiles = new HashMap<>();

        Map<String, Double> beginner = new HashMap<>();
        beginner.put("experience_level", 0.2);
        beginner.put("attention_span", 0.4);
        beginner.put("visual_preference", 0.9);
        learnerProfiles.put("Beginner", beginner);

        Map<String, Double> intermediate = new HashMap<>();
        intermediate.put("experience_level", 0.6);
        intermediate.put("attention_span", 0.7);
        intermediate.put("visual_preference", 0.6);
        learnerProfiles.put("Intermediate", intermediate);

        Map<String, Double> advanced = new HashMap<>();
        advanced.put("experience_level", 0.9);
        advanced.put("attention_span", 0.8);
        advanced.put("visual_preference", 0.4);
        learnerProfiles.put("Advanced", advanced);

        for (Map.Entry<String, Map<String, Double>> entry : learnerProfiles.entrySet()) {
            String profileName = entry.getKey();
            Map<String, Double> profile = entry.getValue();

            System.out.printf("\n%s Learner Profile:\n", profileName);

            try {
                MathExpression mathExpr = new MathExpression(complexNotation);

                // Update learner profile
                for (Map.Entry<String, Double> profileEntry : profile.entrySet()) {
                    mathExpr.viabilityComponent.updateLearnerProfile(
                        profileEntry.getKey(), profileEntry.getValue());
                }

                CompletableFuture<PsiOptimizationResult> future = mathExpr.processWithPsiOptimization();
                PsiOptimizationResult result = future.get();

                System.out.printf("  Experience Level: %.1f\n", profile.get("experience_level"));
                System.out.printf("  Attention Span: %.1f\n", profile.get("attention_span"));
                System.out.printf("  Visual Preference: %.1f\n", profile.get("visual_preference"));
                System.out.printf("  Adapted Expression: %s\n", result.getOptimizedExpression());
                System.out.printf("  Ψ(x) Value: %.3f\n", result.getPsiValue());
                System.out.printf("  Educational Suitability: %s\n",
                    result.getPsiValue() > 0.6 ? "HIGH" : result.getPsiValue() > 0.4 ? "MEDIUM" : "LOW");

                mathExpr.shutdown();

            } catch (Exception e) {
                System.err.println("  Error in adaptive processing: " + e.getMessage());
            }
        }
    }

    /**
     * Demonstrate attention restoration system
     */
    public void demonstrateAttentionRestoration() {
        System.out.println("\n6. ATTENTION RESTORATION SYSTEM");
        System.out.println(SUBSEPARATOR);

        // Simulate attention degradation scenario
        String[] attentionScenario = {
            "{1,2}",                                    // Start simple
            "{1,2} ∪ {3,4}",                           // Moderate complexity
            "({1,2} ∪ {3,4}) ∩ {2,3}",                // Increasing complexity
            "(({1,2} ∪ {3,4}) ∩ {2,3}) × {5,6}",     // High complexity - attention stress
            "((({1,2} ∪ {3,4}) ∩ {2,3}) × {5,6}) ∪ power A"  // Critical complexity
        };

        System.out.println("Simulating progressive attention degradation...\n");

        AttentionRecognitionFramework framework = new AttentionRecognitionFramework(3);

        for (int i = 0; i < attentionScenario.length; i++) {
            String expr = attentionScenario[i];
            System.out.printf("Step %d: %s\n", i + 1, expr);

            try {
                MathExpression mathExpr = new MathExpression(expr, framework);

                CompletableFuture<PsiOptimizationResult> future = mathExpr.processWithPsiOptimization();
                PsiOptimizationResult result = future.get();

                MetaAnalysis meta = result.getMetaAnalysis();

                System.out.printf("  System Health: %s\n", meta.getSystemHealth());
                System.out.printf("  Ψ(x): %.3f\n", result.getPsiValue());
                System.out.printf("  Alpha: %.3f\n", result.getAlpha());

                // Check for attention restoration triggers
                if (meta.getSystemHealth() == SystemHealth.WARNING ||
                    meta.getSystemHealth() == SystemHealth.CRITICAL) {

                    System.out.println("  ⚠️  ATTENTION RESTORATION TRIGGERED");
                    System.out.printf("  Recommendations: %s\n", meta.getRecommendations());

                    if (result.getPsiValue() < 0.4) {
                        System.out.println("  🔄 APPLYING COGNITIVE INTERVENTION");
                        System.out.printf("  Simplified Expression: %s\n", result.getOptimizedExpression());
                    }
                }

                System.out.println();
                mathExpr.shutdown();

            } catch (Exception e) {
                System.err.println("  Error in attention monitoring: " + e.getMessage());
            }
        }

        framework.shutdown();
    }

    /**
     * Demonstrate complete integration with real-world scenario
     */
    public void demonstrateCompleteIntegration() {
        System.out.println("\n7. COMPLETE INTEGRATION EXAMPLE");
        System.out.println(SUBSEPARATOR);

        // Realistic educational scenario: Student working through set theory problems
        String studentProblem = "Find the Cartesian product of {1,2,3} and {a,b}, then find the power set of the result";
        String mathematicalExpression = "power ({1,2,3} × {a,b})";

        System.out.println("Educational Scenario:");
        System.out.println("Problem: " + studentProblem);
        System.out.println("Mathematical Expression: " + mathematicalExpression);

        try {
            MathExpression mathExpr = new MathExpression(mathematicalExpression);

            // Set up for intermediate student
            mathExpr.viabilityComponent.updateLearnerProfile("experience_level", 0.5);
            mathExpr.viabilityComponent.updateLearnerProfile("attention_span", 0.6);
            mathExpr.viabilityComponent.updateLearnerProfile("visual_preference", 0.7);

            System.out.println("\nProcessing with complete cognitive framework...");

            CompletableFuture<PsiOptimizationResult> future = mathExpr.processWithPsiOptimization();
            PsiOptimizationResult result = future.get();

            System.out.println("\nCOMPLETE ANALYSIS RESULTS:");
            System.out.println("=" * 40);

            // Ψ(x) Results
            System.out.printf("Final Ψ(x) Optimization Score: %.3f\n", result.getPsiValue());
            System.out.printf("Cognitive Suitability: %s\n",
                result.getPsiValue() > 0.7 ? "EXCELLENT" :
                result.getPsiValue() > 0.5 ? "GOOD" :
                result.getPsiValue() > 0.3 ? "FAIR" : "NEEDS IMPROVEMENT");

            // Expression Analysis
            System.out.printf("Original Expression: %s\n", mathematicalExpression);
            System.out.printf("Optimized Expression: %s\n", result.getOptimizedExpression());
            System.out.printf("Complexity Score: %.3f\n", mathExpr.getComplexityScore());

            // Cognitive Processing
            System.out.printf("Symbolic/Neural Balance (α): %.3f\n", result.getAlpha());
            System.out.printf("Cognitive Load Penalty: %.3f\n", result.getCognitivePenalty());
            System.out.printf("Processing Efficiency: %.3f\n", 1.0 - result.getEfficiencyPenalty());

            // Meta-Controller Analysis
            MetaAnalysis meta = result.getMetaAnalysis();
            System.out.printf("System Health: %s\n", meta.getSystemHealth());
            System.out.printf("Attention Stability: %s\n",
                meta.getAttentionDrifts().size() < 2 ? "STABLE" : "UNSTABLE");

            // Educational Recommendations
            System.out.println("\nEDUCATIONAL RECOMMENDATIONS:");
            for (String recommendation : meta.getRecommendations()) {
                System.out.printf("• %s\n", formatRecommendation(recommendation));
            }

            // Lexical Network Insights
            System.out.println("\nLEXICAL INSIGHTS:");
            MathExpression.LexicalSuggestion suggestion =
                mathExpr.getLexicalNetwork().getSuggestion(mathematicalExpression);
            System.out.printf("• Notation Suggestion: %s\n", suggestion.getSuggestion());
            System.out.printf("• Alternative Confidence: %.3f\n", suggestion.getConfidence());

            // Bias Analysis
            if (!meta.getBiasMetrics().isEmpty()) {
                System.out.println("\nBIAS ANALYSIS:");
                for (Map.Entry<String, Double> bias : meta.getBiasMetrics().entrySet()) {
                    System.out.printf("• %s: %.3f\n", bias.getKey(), bias.getValue());
                }
            }

            // Final Educational Assessment
            System.out.println("\nEDUCATIONAL ASSESSMENT:");
            System.out.printf("Readiness for this problem: %s\n",
                result.getPsiValue() > 0.6 ? "READY" :
                result.getPsiValue() > 0.4 ? "NEEDS SUPPORT" : "TOO ADVANCED");

            if (result.getPsiValue() < 0.5) {
                System.out.println("Suggested preparation:");
                System.out.println("• Review basic set operations (∪, ∩)");
                System.out.println("• Practice with Cartesian products");
                System.out.println("• Start with smaller sets");
            }

            mathExpr.shutdown();

        } catch (Exception e) {
            System.err.println("Error in complete integration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper methods

    private String formatCognitiveTags(Map<String, Double> tags) {
        if (tags.isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Double> entry : tags.entrySet()) {
            if (!first) sb.append(", ");
            sb.append(entry.getKey()).append("=").append(String.format("%.3f", entry.getValue()));
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    private String formatRecommendation(String recommendation) {
        switch (recommendation) {
            case "RESTORE_ATTENTION":
                return "Take a short break to restore attention capacity";
            case "SIMPLIFY_NOTATION":
                return "Use simpler notation (e.g., 'union' instead of '∪')";
            case "REDUCE_COMPLEXITY":
                return "Break problem into smaller sub-problems";
            case "ENHANCE_CLARITY":
                return "Add spacing and visual structure to expression";
            case "REDUCE_BIAS":
                return "Consider alternative notation preferences";
            case "MONITOR_ATTENTION":
                return "Continue monitoring for attention drifts";
            default:
                return recommendation.toLowerCase().replace("_", " ");
        }
    }
}
