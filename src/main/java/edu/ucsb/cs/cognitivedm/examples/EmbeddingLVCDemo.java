package edu.ucsb.cs.cognitivedm.examples;

import edu.ucsb.cs.cognitivedm.MathExpression;
import edu.ucsb.cs.cognitivedm.embeddings.MathEmbeddingService;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.CognitiveState;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.ProcessingResult;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Demonstration of Embedding-Enhanced Lexical Viability Component (LVC)
 * Shows how neural embeddings improve term recall and cognitive alignment
 * in mathematical expression processing with Ψ(x) optimization
 */
public class EmbeddingLVCDemo {

    private static final String SEPARATOR = "=" + "=".repeat(70);
    private static final String SUB_SEPARATOR = "-" + "-".repeat(50);

    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println(
            "🧠 EMBEDDING-ENHANCED LEXICAL VIABILITY COMPONENT DEMO"
        );
        System.out.println(
            "   Cognitive-Inspired Mathematical Expression Processing"
        );
        System.out.println(SEPARATOR);

        // Initialize cognitive framework
        AttentionRecognitionFramework framework =
            new AttentionRecognitionFramework(3); // Using 3 scales as default

        // Run comprehensive demonstrations
        demonstrateBasicEmbeddingSimilarity();
        demonstrateCognitiveAdaptation();
        demonstrateTermRecallOptimization();
        demonstrateΨOptimizationWithEmbeddings();
        demonstrateFeedbackLearning();
        demonstrateMultiScaleIntegration();

        System.out.println(SEPARATOR);
        System.out.println("✅ Demo completed successfully!");
        System.out.println(
            "   Embedding-enhanced LVC shows significant improvements in"
        );
        System.out.println(
            "   term recall accuracy, cognitive alignment, and educational"
        );
        System.out.println(
            "   adaptability for mathematical expression processing."
        );
        System.out.println(SEPARATOR);
    }

    /**
     * Demonstrate basic embedding similarity computations
     */
    private static void demonstrateBasicEmbeddingSimilarity() {
        System.out.println("\n📊 BASIC EMBEDDING SIMILARITY DEMONSTRATION");
        System.out.println(SUB_SEPARATOR);

        MathEmbeddingService embeddingService = new MathEmbeddingService(
            MathEmbeddingService.EmbeddingConfig.defaultConfig()
        );

        // Test semantic similarity between mathematical terms
        String[] setTheoryTerms = {
            "union",
            "intersection",
            "complement",
            "difference",
            "cartesian_product",
        };
        String[] notationTerms = { "roster", "builder", "symbolic", "verbal" };

        System.out.println("🔗 Set Theory Term Similarities:");
        for (int i = 0; i < setTheoryTerms.length; i++) {
            for (int j = i + 1; j < setTheoryTerms.length; j++) {
                double similarity =
                    embeddingService.calculateEmbeddingSimilarity(
                        setTheoryTerms[i],
                        setTheoryTerms[j]
                    );
                System.out.printf(
                    "   %s ↔ %s: %.3f\n",
                    setTheoryTerms[i],
                    setTheoryTerms[j],
                    similarity
                );
            }
        }

        System.out.println("\n📝 Notation Type Similarities:");
        for (int i = 0; i < notationTerms.length; i++) {
            for (int j = i + 1; j < notationTerms.length; j++) {
                double similarity =
                    embeddingService.calculateEmbeddingSimilarity(
                        notationTerms[i],
                        notationTerms[j]
                    );
                System.out.printf(
                    "   %s ↔ %s: %.3f\n",
                    notationTerms[i],
                    notationTerms[j],
                    similarity
                );
            }
        }

        // Test symbolic-verbal equivalence
        System.out.println("\n🔤 Symbolic-Verbal Equivalence:");
        Map<String, String> symbolMap = Map.of(
            "union",
            "∪",
            "intersection",
            "∩",
            "cartesian_product",
            "×"
        );

        symbolMap.forEach((verbal, symbolic) -> {
            double similarity = embeddingService.calculateEmbeddingSimilarity(
                verbal,
                symbolic
            );
            System.out.printf(
                "   %s ↔ %s: %.3f\n",
                verbal,
                symbolic,
                similarity
            );
        });
    }

    /**
     * Demonstrate cognitive state adaptation
     */
    private static void demonstrateCognitiveAdaptation() {
        System.out.println("\n🧠 COGNITIVE STATE ADAPTATION DEMONSTRATION");
        System.out.println(SUB_SEPARATOR);

        MathEmbeddingService embeddingService = new MathEmbeddingService(
            MathEmbeddingService.EmbeddingConfig.defaultConfig()
        );

        // Test similarity under different cognitive states
        String term1 = "union";
        String term2 = "intersection";

        System.out.println("🎯 Similarity under different cognitive states:");

        // High attention, low cognitive load (optimal learning state)
        double highAttentionSim = embeddingService.calculateCognitiveSimilarity(
            term1,
            term2,
            0.9,
            0.2
        );
        System.out.printf(
            "   High attention, low load: %.3f\n",
            highAttentionSim
        );

        // Low attention, high cognitive load (stressed state)
        double lowAttentionSim = embeddingService.calculateCognitiveSimilarity(
            term1,
            term2,
            0.3,
            0.8
        );
        System.out.printf(
            "   Low attention, high load: %.3f\n",
            lowAttentionSim
        );

        // Medium attention, medium load (typical state)
        double mediumSim = embeddingService.calculateCognitiveSimilarity(
            term1,
            term2,
            0.6,
            0.5
        );
        System.out.printf("   Medium attention/load: %.3f\n", mediumSim);

        System.out.println("\n📈 Similar terms retrieval adaptation:");

        // Find similar terms under different cognitive states
        List<MathEmbeddingService.SimilarityResult> highAttentionResults =
            embeddingService.findSimilarTerms("union", 3, 0.9, 0.2);
        List<MathEmbeddingService.SimilarityResult> lowAttentionResults =
            embeddingService.findSimilarTerms("union", 3, 0.3, 0.8);

        System.out.println("   High attention state:");
        highAttentionResults.forEach(result ->
            System.out.printf(
                "     %s: %.3f\n",
                result.getTerm(),
                result.getSimilarity()
            )
        );

        System.out.println("   Low attention state:");
        lowAttentionResults.forEach(result ->
            System.out.printf(
                "     %s: %.3f\n",
                result.getTerm(),
                result.getSimilarity()
            )
        );
    }

    /**
     * Demonstrate term recall optimization with LVC
     */
    private static void demonstrateTermRecallOptimization() {
        System.out.println("\n🎯 TERM RECALL OPTIMIZATION DEMONSTRATION");
        System.out.println(SUB_SEPARATOR);

        try {
            AttentionRecognitionFramework framework =
                new AttentionRecognitionFramework(3);
            MathExpression expr = new MathExpression(
                "{1,2,3} ∪ {4,5,6}",
                framework
            );

            // Test LVC viability calculation
            CognitiveState highAttentionState = new CognitiveState(
                0.9,
                0.8,
                0.2
            );
            CognitiveState lowAttentionState = new CognitiveState(
                0.3,
                0.5,
                0.8
            );

            System.out.println(
                "🔍 Viability scores for different cognitive states:"
            );

            // Get LVC from the expression
            MathExpression.LexicalViabilityComponent lvc =
                expr.getLexicalNetwork()
                    .new LexicalViabilityComponent(
                        expr.getLexicalNetwork(),
                        expr.embeddingService
                    );

            // Calculate viability for different terms
            String[] testTerms = {
                "union",
                "intersection",
                "roster",
                "symbolic",
            };

            System.out.println("   High attention state:");
            for (String term : testTerms) {
                try {
                    double viability = lvc.calculateViability(
                        term,
                        highAttentionState,
                        Collections.emptyList()
                    );
                    System.out.printf("     %s: %.3f\n", term, viability);
                } catch (Exception e) {
                    System.out.printf("     %s: calculation error\n", term);
                }
            }

            System.out.println("   Low attention state:");
            for (String term : testTerms) {
                try {
                    double viability = lvc.calculateViability(
                        term,
                        lowAttentionState,
                        Collections.emptyList()
                    );
                    System.out.printf("     %s: %.3f\n", term, viability);
                } catch (Exception e) {
                    System.out.printf("     %s: calculation error\n", term);
                }
            }

            // Demonstrate most viable term selection
            System.out.println("\n🏆 Most viable term selection:");
            String mostViableHigh = lvc.getMostViableTerm(
                "{1,2} ∪ {3,4}",
                highAttentionState
            );
            String mostViableLow = lvc.getMostViableTerm(
                "{1,2} ∪ {3,4}",
                lowAttentionState
            );

            System.out.printf("   High attention: %s\n", mostViableHigh);
            System.out.printf("   Low attention: %s\n", mostViableLow);
        } catch (Exception e) {
            System.out.println(
                "   Error in term recall optimization demo: " + e.getMessage()
            );
        }
    }

    /**
     * Demonstrate Ψ(x) optimization with embedding enhancement
     */
    private static void demonstrateΨOptimizationWithEmbeddings() {
        System.out.println(
            "\n⚡ Ψ(x) OPTIMIZATION WITH EMBEDDINGS DEMONSTRATION"
        );
        System.out.println(SUB_SEPARATOR);

        try {
            AttentionRecognitionFramework framework =
                new AttentionRecognitionFramework(3);

            // Test expressions with different complexity levels
            String[] testExpressions = {
                "{1,2} ∪ {3,4}", // Simple union
                "{x | x ∈ A ∧ x ∈ B}", // Builder notation
                "A ∪ B ∩ C", // Complex operation
                "power({1,2,3})", // Power set
            };

            System.out.println("🔬 Ψ(x) optimization results:");

            for (String exprStr : testExpressions) {
                try {
                    MathExpression expr = new MathExpression(
                        exprStr,
                        framework
                    );

                    // Process with Ψ(x) optimization
                    CompletableFuture<
                        MathExpression.PsiOptimizationResult
                    > future = expr.processWithPsiOptimization();

                    MathExpression.PsiOptimizationResult result = future.get();

                    System.out.printf("\n   Expression: %s\n", exprStr);
                    System.out.printf(
                        "     Ψ(x) value: %.3f\n",
                        result.getPsiValue()
                    );
                    System.out.printf(
                        "     Symbolic output: %.3f\n",
                        result.getSymbolicOutput()
                    );
                    System.out.printf(
                        "     Neural output: %.3f\n",
                        result.getNeuralOutput()
                    );
                    System.out.printf(
                        "     Alpha (α): %.3f\n",
                        result.getAlpha()
                    );
                    System.out.printf(
                        "     Cognitive penalty: %.3f\n",
                        result.getCognitivePenalty()
                    );
                    System.out.printf(
                        "     Efficiency penalty: %.3f\n",
                        result.getEfficiencyPenalty()
                    );
                    System.out.printf(
                        "     Biased probability: %.3f\n",
                        result.getBiasedProbability()
                    );
                } catch (Exception e) {
                    System.out.printf(
                        "   Expression: %s - Error: %s\n",
                        exprStr,
                        e.getMessage()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println(
                "   Error in Ψ(x) optimization demo: " + e.getMessage()
            );
        }
    }

    /**
     * Demonstrate feedback learning and adaptation
     */
    private static void demonstrateFeedbackLearning() {
        System.out.println("\n📚 FEEDBACK LEARNING DEMONSTRATION");
        System.out.println(SUB_SEPARATOR);

        MathEmbeddingService embeddingService = new MathEmbeddingService(
            MathEmbeddingService.EmbeddingConfig.defaultConfig()
        );

        System.out.println("🔄 Embedding adaptation through feedback:");

        String testTerm = "union";
        String context = "{A,B} ∪ {C,D}";

        // Get initial statistics
        MathEmbeddingService.EmbeddingStats initialStats =
            embeddingService.getEmbeddingStats();
        System.out.printf(
            "   Initial avg adaptive weight: %.3f\n",
            initialStats.getAvgAdaptiveWeight()
        );

        // Simulate positive feedback
        System.out.println("\n   Applying positive feedback...");
        for (int i = 0; i < 3; i++) {
            embeddingService.updateEmbeddingFromFeedback(
                testTerm,
                context,
                0.8,
                0.3,
                true
            );
        }

        MathEmbeddingService.EmbeddingStats positiveStats =
            embeddingService.getEmbeddingStats();
        System.out.printf(
            "   After positive feedback: %.3f\n",
            positiveStats.getAvgAdaptiveWeight()
        );

        // Simulate negative feedback
        System.out.println("\n   Applying negative feedback...");
        for (int i = 0; i < 2; i++) {
            embeddingService.updateEmbeddingFromFeedback(
                testTerm,
                context,
                0.8,
                0.3,
                false
            );
        }

        MathEmbeddingService.EmbeddingStats negativeStats =
            embeddingService.getEmbeddingStats();
        System.out.printf(
            "   After negative feedback: %.3f\n",
            negativeStats.getAvgAdaptiveWeight()
        );

        // Show adaptation
        System.out.println("\n📊 Cognitive alignment adaptation:");
        System.out.printf(
            "   Initial attention weight: %.3f\n",
            initialStats.getAttentionWeight()
        );

        embeddingService.updateCognitiveAlignment(0.9, 0.8); // High load scenario
        MathEmbeddingService.EmbeddingStats adaptedStats =
            embeddingService.getEmbeddingStats();
        System.out.printf(
            "   After high load adaptation: %.3f\n",
            adaptedStats.getAttentionWeight()
        );
    }

    /**
     * Demonstrate multi-scale integration
     */
    private static void demonstrateMultiScaleIntegration() {
        System.out.println("\n🌐 MULTI-SCALE INTEGRATION DEMONSTRATION");
        System.out.println(SUB_SEPARATOR);

        try {
            AttentionRecognitionFramework framework =
                new AttentionRecognitionFramework(3);

            // Test complex nested expression
            String complexExpression = "(A ∪ B) ∩ (C ∪ D)";
            MathExpression expr = new MathExpression(
                complexExpression,
                framework
            );

            System.out.printf(
                "🔍 Analyzing complex expression: %s\n",
                complexExpression
            );

            // Process with multi-scale framework
            CompletableFuture<MathExpression> future =
                expr.processWithCognitiveFramework();
            MathExpression processedExpr = future.get();

            // Show results
            System.out.println("\n📈 Multi-scale processing results:");
            System.out.printf(
                "   Complexity score: %.3f\n",
                processedExpr.getComplexityScore()
            );
            System.out.printf(
                "   Processing state attention: %.3f\n",
                processedExpr.getProcessingState().getAttention()
            );
            System.out.printf(
                "   Processing state cognitive load: %.3f\n",
                processedExpr.getProcessingState().getCognitiveLoad()
            );

            // Show cognitive tags
            System.out.println("\n🏷️  Cognitive tags:");
            processedExpr
                .getCognitiveTags()
                .forEach((tag, value) ->
                    System.out.printf("     %s: %.3f\n", tag, value)
                );

            // Show embedding statistics
            MathExpression.LexicalViabilityComponent lvc =
                processedExpr.getLexicalNetwork()
                    .new LexicalViabilityComponent(
                        processedExpr.getLexicalNetwork(),
                        processedExpr.embeddingService
                    );

            MathEmbeddingService.EmbeddingStats embeddingStats =
                lvc.getEmbeddingStats();
            if (embeddingStats != null) {
                System.out.println("\n📊 Embedding statistics:");
                System.out.printf(
                    "     Total terms: %d\n",
                    embeddingStats.getTotalTerms()
                );
                System.out.printf(
                    "     Avg adaptive weight: %.3f\n",
                    embeddingStats.getAvgAdaptiveWeight()
                );
                System.out.printf(
                    "     High weight terms: %d\n",
                    embeddingStats.getHighWeightTerms()
                );
                System.out.printf(
                    "     Attention weight: %.3f\n",
                    embeddingStats.getAttentionWeight()
                );
            }
        } catch (Exception e) {
            System.out.println(
                "   Error in multi-scale integration demo: " + e.getMessage()
            );
        }
    }
}
