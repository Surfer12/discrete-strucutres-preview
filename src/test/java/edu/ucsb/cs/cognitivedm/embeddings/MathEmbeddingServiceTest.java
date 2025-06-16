package edu.ucsb.cs.cognitivedm.embeddings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Comprehensive test suite for MathEmbeddingService with LVC integration
 * Tests embedding-based similarity, cognitive adaptation, and feedback learning
 */
public class MathEmbeddingServiceTest {

    private MathEmbeddingService embeddingService;
    private MathEmbeddingService.EmbeddingConfig config;

    @BeforeEach
    void setUp() {
        config = MathEmbeddingService.EmbeddingConfig.defaultConfig();
        embeddingService = new MathEmbeddingService(config);
    }

    @Test
    @DisplayName("Test basic embedding similarity for set theory terms")
    void testBasicEmbeddingSimilarity() {
        // Test semantic similarity between related set operations
        double unionIntersectionSim = embeddingService.calculateEmbeddingSimilarity("union", "intersection");
        double unionComplementSim = embeddingService.calculateEmbeddingSimilarity("union", "complement");

        // Union and intersection should be more similar than union and complement
        assertTrue(unionIntersectionSim > unionComplementSim,
                  "Union and intersection should be more semantically similar than union and complement");

        // Test symbolic equivalence
        double unionSymbolicSim = embeddingService.calculateEmbeddingSimilarity("union", "∪");
        assertTrue(unionSymbolicSim > 0.8,
                  "Union and its symbolic representation should have high similarity");
    }

    @Test
    @DisplayName("Test cognitive-aware similarity adaptation")
    void testCognitiveSimilarity() {
        double highAttention = 0.9;
        double lowAttention = 0.3;
        double lowLoad = 0.2;
        double highLoad = 0.8;

        // High attention should increase similarity sensitivity
        double highAttentionSim = embeddingService.calculateCognitiveSimilarity(
            "union", "intersection", highAttention, lowLoad);
        double lowAttentionSim = embeddingService.calculateCognitiveSimilarity(
            "union", "intersection", lowAttention, lowLoad);

        assertTrue(highAttentionSim > lowAttentionSim,
                  "High attention should yield higher similarity scores");

        // High cognitive load should decrease similarity
        double lowLoadSim = embeddingService.calculateCognitiveSimilarity(
            "union", "intersection", highAttention, lowLoad);
        double highLoadSim = embeddingService.calculateCognitiveSimilarity(
            "union", "intersection", highAttention, highLoad);

        assertTrue(lowLoadSim > highLoadSim,
                  "Low cognitive load should yield higher similarity scores");
    }

    @Test
    @DisplayName("Test similar terms retrieval with cognitive parameters")
    void testSimilarTermsRetrieval() {
        double attention = 0.8;
        double cognitiveLoad = 0.3;

        List<MathEmbeddingService.SimilarityResult> similarToUnion =
            embeddingService.findSimilarTerms("union", 5, attention, cognitiveLoad);

        assertFalse(similarToUnion.isEmpty(), "Should find similar terms to union");
        assertTrue(similarToUnion.size() <= 5, "Should respect topK limit");

        // Results should be sorted by similarity (descending)
        for (int i = 1; i < similarToUnion.size(); i++) {
            assertTrue(similarToUnion.get(i-1).getSimilarity() >=
                      similarToUnion.get(i).getSimilarity(),
                      "Results should be sorted by similarity in descending order");
        }

        // Check that intersection appears in similar terms
        boolean containsIntersection = similarToUnion.stream()
            .anyMatch(result -> result.getTerm().equals("intersection"));
        assertTrue(containsIntersection, "Similar terms to union should include intersection");
    }

    @Test
    @DisplayName("Test embedding feedback learning")
    void testEmbeddingFeedbackLearning() {
        String term = "union";
        String context = "{1,2} ∪ {2,3}";
        double attention = 0.7;
        double cognitiveLoad = 0.4;

        // Get initial embedding
        double[] initialEmbedding = embeddingService.getEmbedding(term).clone();

        // Provide positive feedback
        embeddingService.updateEmbeddingFromFeedback(term, context, attention, cognitiveLoad, true);
        double[] positiveEmbedding = embeddingService.getEmbedding(term);

        // Embeddings should have changed
        assertFalse(java.util.Arrays.equals(initialEmbedding, positiveEmbedding),
                   "Embedding should change after positive feedback");

        // Test adaptive weight increase after positive feedback
        MathEmbeddingService.EmbeddingStats statsAfterPositive = embeddingService.getEmbeddingStats();
        assertTrue(statsAfterPositive.getAvgAdaptiveWeight() > 1.0,
                  "Average adaptive weight should increase after positive feedback");

        // Provide negative feedback
        embeddingService.updateEmbeddingFromFeedback(term, context, attention, cognitiveLoad, false);
        MathEmbeddingService.EmbeddingStats statsAfterNegative = embeddingService.getEmbeddingStats();

        assertTrue(statsAfterNegative.getAvgAdaptiveWeight() < statsAfterPositive.getAvgAdaptiveWeight(),
                  "Average adaptive weight should decrease after negative feedback");
    }

    @Test
    @DisplayName("Test cognitive alignment parameter adaptation")
    void testCognitiveAlignmentAdaptation() {
        double initialAttentionWeight = embeddingService.getEmbeddingStats().getAttentionWeight();

        // Simulate high cognitive load scenario
        embeddingService.updateCognitiveAlignment(0.6, 0.9);
        double highLoadAttentionWeight = embeddingService.getEmbeddingStats().getAttentionWeight();

        assertTrue(highLoadAttentionWeight > initialAttentionWeight,
                  "Attention weight should increase under high cognitive load");

        // Simulate low cognitive load scenario
        embeddingService.updateCognitiveAlignment(0.8, 0.2);
        double lowLoadAttentionWeight = embeddingService.getEmbeddingStats().getAttentionWeight();

        assertTrue(lowLoadAttentionWeight < highLoadAttentionWeight,
                  "Attention weight should decrease under low cognitive load");
    }

    @Test
    @DisplayName("Test context embedding generation")
    void testContextEmbeddingGeneration() {
        String setContext = "{1,2,3} ∪ {4,5,6}";
        String algebraContext = "2x + 3y = 7";

        // Test that different contexts generate different embeddings
        embeddingService.updateEmbeddingFromFeedback("test", setContext, 0.7, 0.3, true);
        double[] setContextEmbedding = embeddingService.getEmbedding("test");

        embeddingService.updateEmbeddingFromFeedback("test2", algebraContext, 0.7, 0.3, true);
        double[] algebraContextEmbedding = embeddingService.getEmbedding("test2");

        // Contexts should influence embeddings differently
        double similarity = cosineSimilarity(setContextEmbedding, algebraContextEmbedding);
        assertTrue(similarity < 0.9, "Different mathematical contexts should produce distinct embeddings");
    }

    @Test
    @DisplayName("Test embedding dimension consistency")
    void testEmbeddingDimensionConsistency() {
        int expectedDimension = config.getDimension();

        // All term embeddings should have consistent dimensions
        for (String term : List.of("union", "intersection", "complement", "roster", "symbolic")) {
            double[] embedding = embeddingService.getEmbedding(term);
            assertNotNull(embedding, "Embedding should exist for term: " + term);
            assertEquals(expectedDimension, embedding.length,
                        "Embedding dimension should be consistent for term: " + term);
        }
    }

    @Test
    @DisplayName("Test embedding normalization")
    void testEmbeddingNormalization() {
        // All embeddings should be normalized (unit vectors)
        for (String term : List.of("union", "intersection", "complement")) {
            double[] embedding = embeddingService.getEmbedding(term);
            double norm = Math.sqrt(java.util.Arrays.stream(embedding)
                .map(x -> x * x).sum());

            assertEquals(1.0, norm, 0.001,
                        "Embedding should be normalized for term: " + term);
        }
    }

    @Test
    @DisplayName("Test custom embedding addition")
    void testCustomEmbeddingAddition() {
        String customTerm = "custom_operation";
        double[] customEmbedding = new double[config.getDimension()];

        // Create a custom embedding
        for (int i = 0; i < customEmbedding.length; i++) {
            customEmbedding[i] = Math.random();
        }

        embeddingService.addMathEmbedding(customTerm, customEmbedding);

        double[] retrievedEmbedding = embeddingService.getEmbedding(customTerm);
        assertNotNull(retrievedEmbedding, "Custom embedding should be retrievable");

        // Should be normalized after addition
        double norm = Math.sqrt(java.util.Arrays.stream(retrievedEmbedding)
            .map(x -> x * x).sum());
        assertEquals(1.0, norm, 0.001, "Custom embedding should be normalized");
    }

    @Test
    @DisplayName("Test embedding service statistics")
    void testEmbeddingServiceStatistics() {
        MathEmbeddingService.EmbeddingStats stats = embeddingService.getEmbeddingStats();

        assertNotNull(stats, "Statistics should be available");
        assertTrue(stats.getTotalTerms() > 0, "Should have registered terms");
        assertTrue(stats.getAvgAdaptiveWeight() > 0, "Should have positive average adaptive weight");
        assertTrue(stats.getAttentionWeight() > 0 && stats.getAttentionWeight() <= 1.0,
                  "Attention weight should be in valid range");
    }

    @Test
    @DisplayName("Test embedding similarity symmetry")
    void testEmbeddingSimilaritySymmetry() {
        // Similarity should be symmetric
        double sim1 = embeddingService.calculateEmbeddingSimilarity("union", "intersection");
        double sim2 = embeddingService.calculateEmbeddingSimilarity("intersection", "union");

        assertEquals(sim1, sim2, 0.001, "Embedding similarity should be symmetric");
    }

    @Test
    @DisplayName("Test embedding similarity bounds")
    void testEmbeddingSimilarityBounds() {
        // Similarity scores should be bounded [0, 1]
        List<String> terms = List.of("union", "intersection", "complement", "difference");

        for (String term1 : terms) {
            for (String term2 : terms) {
                double similarity = embeddingService.calculateEmbeddingSimilarity(term1, term2);
                assertTrue(similarity >= 0.0 && similarity <= 1.0,
                          "Similarity should be in [0,1] range for " + term1 + " and " + term2);

                if (term1.equals(term2)) {
                    assertEquals(1.0, similarity, 0.001,
                               "Self-similarity should be 1.0 for " + term1);
                }
            }
        }
    }

    @Test
    @DisplayName("Test mathematical notation preferences")
    void testMathematicalNotationPreferences() {
        // Test that notation-specific embeddings capture preferences
        double rosterBuilderSim = embeddingService.calculateEmbeddingSimilarity("roster", "builder");
        double rosterSymbolicSim = embeddingService.calculateEmbeddingSimilarity("roster", "symbolic");

        // These should reflect different cognitive preferences
        assertNotEquals(rosterBuilderSim, rosterSymbolicSim, 0.001,
                       "Different notation types should have distinct similarity patterns");
    }

    /**
     * Helper method to calculate cosine similarity between two vectors
     */
    private double cosineSimilarity(double[] vec1, double[] vec2) {
        if (vec1.length != vec2.length) return 0.0;

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }

        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);

        if (norm1 == 0.0 || norm2 == 0.0) return 0.0;

        return dotProduct / (norm1 * norm2);
    }
}
