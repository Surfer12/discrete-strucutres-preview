package edu.ucsb.cs.cognitivedm.embeddings;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * Mathematical Embedding Service for semantic similarity computation
 * Integrates with Lexical Viability Component for enhanced term recall
 */
public class MathEmbeddingService {

    private final Map<String, double[]> termEmbeddings;
    private final Map<String, double[]> contextEmbeddings;
    private final int embeddingDimension;
    private final double learningRate;
    private final EmbeddingConfig config;

    // Cognitive alignment parameters
    private volatile double attentionWeight = 0.7;
    private volatile double cognitiveLoadThreshold = 0.8;
    private final Map<String, Double> adaptiveWeights;

    public MathEmbeddingService(EmbeddingConfig config) {
        this.config = config;
        this.embeddingDimension = config.getDimension();
        this.learningRate = config.getLearningRate();
        this.termEmbeddings = new ConcurrentHashMap<>();
        this.contextEmbeddings = new ConcurrentHashMap<>();
        this.adaptiveWeights = new ConcurrentHashMap<>();
        initializeMathematicalEmbeddings();
    }

    /**
     * Initialize pre-trained mathematical term embeddings
     */
    private void initializeMathematicalEmbeddings() {
        // Set theory terms
        addMathEmbedding(
            "union",
            createSetOperationEmbedding(0.9, 0.8, 0.7, 0.6)
        );
        addMathEmbedding("∪", createSetOperationEmbedding(0.9, 0.8, 0.7, 0.6));
        addMathEmbedding(
            "intersection",
            createSetOperationEmbedding(0.8, 0.9, 0.6, 0.7)
        );
        addMathEmbedding("∩", createSetOperationEmbedding(0.8, 0.9, 0.6, 0.7));
        addMathEmbedding(
            "complement",
            createSetOperationEmbedding(0.6, 0.5, 0.9, 0.8)
        );
        addMathEmbedding(
            "difference",
            createSetOperationEmbedding(0.7, 0.6, 0.8, 0.9)
        );
        addMathEmbedding(
            "cartesian_product",
            createSetOperationEmbedding(0.5, 0.4, 0.7, 0.8)
        );
        addMathEmbedding("×", createSetOperationEmbedding(0.5, 0.4, 0.7, 0.8));
        addMathEmbedding(
            "power_set",
            createSetOperationEmbedding(0.4, 0.3, 0.8, 0.9)
        );
        addMathEmbedding(
            "subset",
            createSetOperationEmbedding(0.7, 0.8, 0.5, 0.6)
        );
        addMathEmbedding(
            "superset",
            createSetOperationEmbedding(0.6, 0.7, 0.4, 0.5)
        );

        // Notation types
        addMathEmbedding("roster", createNotationEmbedding(0.9, 0.8, 0.3, 0.4));
        addMathEmbedding(
            "builder",
            createNotationEmbedding(0.6, 0.7, 0.9, 0.8)
        );
        addMathEmbedding(
            "symbolic",
            createNotationEmbedding(0.4, 0.5, 0.8, 0.9)
        );
        addMathEmbedding("verbal", createNotationEmbedding(0.8, 0.9, 0.4, 0.5));

        // Algebraic terms
        addMathEmbedding(
            "polynomial",
            createAlgebraEmbedding(0.7, 0.8, 0.6, 0.5)
        );
        addMathEmbedding(
            "variable",
            createAlgebraEmbedding(0.8, 0.7, 0.5, 0.6)
        );
        addMathEmbedding(
            "constant",
            createAlgebraEmbedding(0.9, 0.6, 0.4, 0.7)
        );
        addMathEmbedding(
            "coefficient",
            createAlgebraEmbedding(0.6, 0.9, 0.7, 0.4)
        );

        // Initialize adaptive weights
        termEmbeddings.keySet().forEach(term -> adaptiveWeights.put(term, 1.0));
    }

    /**
     * Create embedding vector for set operations
     */
    private double[] createSetOperationEmbedding(
        double combine,
        double separate,
        double include,
        double exclude
    ) {
        double[] embedding = new double[embeddingDimension];
        // Base semantic features
        embedding[0] = combine; // combining operation strength
        embedding[1] = separate; // separating operation strength
        embedding[2] = include; // inclusion semantics
        embedding[3] = exclude; // exclusion semantics

        // Fill remaining dimensions with normalized random values influenced by base features
        Random random = new Random(
            Double.hashCode(combine) + Double.hashCode(separate)
        );
        for (int i = 4; i < embeddingDimension; i++) {
            embedding[i] =
                (combine + separate + include + exclude) / 4.0 +
                (random.nextGaussian() * 0.1);
        }

        return normalizeVector(embedding);
    }

    /**
     * Create embedding vector for notation types
     */
    private double[] createNotationEmbedding(
        double visual,
        double explicit,
        double abstractLevel,
        double compact
    ) {
        double[] embedding = new double[embeddingDimension];
        embedding[0] = visual; // visual clarity
        embedding[1] = explicit; // explicitness level
        embedding[2] = abstractLevel; // abstraction level
        embedding[3] = compact; // compactness

        Random random = new Random(
            Double.hashCode(visual) + Double.hashCode(explicit)
        );
        for (int i = 4; i < embeddingDimension; i++) {
            embedding[i] =
                (visual + explicit + abstractLevel + compact) / 4.0 +
                (random.nextGaussian() * 0.1);
        }

        return normalizeVector(embedding);
    }

    /**
     * Create embedding vector for algebraic terms
     */
    private double[] createAlgebraEmbedding(
        double structural,
        double functional,
        double symbolic,
        double numeric
    ) {
        double[] embedding = new double[embeddingDimension];
        embedding[0] = structural; // structural role
        embedding[1] = functional; // functional role
        embedding[2] = symbolic; // symbolic nature
        embedding[3] = numeric; // numeric content

        Random random = new Random(
            Double.hashCode(structural) + Double.hashCode(functional)
        );
        for (int i = 4; i < embeddingDimension; i++) {
            embedding[i] =
                (structural + functional + symbolic + numeric) / 4.0 +
                (random.nextGaussian() * 0.1);
        }

        return normalizeVector(embedding);
    }

    /**
     * Add or update mathematical term embedding
     */
    public void addMathEmbedding(String term, double[] embedding) {
        if (embedding.length != embeddingDimension) {
            throw new IllegalArgumentException("Embedding dimension mismatch");
        }
        termEmbeddings.put(term, normalizeVector(embedding.clone()));
        adaptiveWeights.put(term, 1.0);
    }

    /**
     * Calculate semantic similarity between two terms using embeddings
     */
    public double calculateEmbeddingSimilarity(String term1, String term2) {
        double[] embedding1 = getEmbedding(term1);
        double[] embedding2 = getEmbedding(term2);

        if (embedding1 == null || embedding2 == null) {
            return 0.0;
        }

        return cosineSimilarity(embedding1, embedding2);
    }

    /**
     * Calculate cognitive-aware similarity with attention and load consideration
     */
    public double calculateCognitiveSimilarity(
        String term1,
        String term2,
        double attention,
        double cognitiveLoad
    ) {
        double baseSimilarity = calculateEmbeddingSimilarity(term1, term2);

        // Adjust similarity based on cognitive state
        double attentionFactor = Math.pow(attention, attentionWeight);
        double loadFactor = Math.max(0.1, 1.0 - cognitiveLoad);

        // Apply adaptive weights
        double weight1 = adaptiveWeights.getOrDefault(term1, 1.0);
        double weight2 = adaptiveWeights.getOrDefault(term2, 1.0);
        double weightFactor = Math.sqrt(weight1 * weight2);

        return baseSimilarity * attentionFactor * loadFactor * weightFactor;
    }

    /**
     * Find most similar terms to a given term
     */
    public List<SimilarityResult> findSimilarTerms(
        String term,
        int topK,
        double attention,
        double cognitiveLoad
    ) {
        if (!termEmbeddings.containsKey(term)) {
            return Collections.emptyList();
        }

        return termEmbeddings
            .keySet()
            .stream()
            .filter(t -> !t.equals(term))
            .map(t ->
                new SimilarityResult(
                    t,
                    calculateCognitiveSimilarity(
                        term,
                        t,
                        attention,
                        cognitiveLoad
                    )
                )
            )
            .sorted((a, b) ->
                Double.compare(b.getSimilarity(), a.getSimilarity())
            )
            .limit(topK)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Update embeddings based on usage patterns and cognitive feedback
     */
    public void updateEmbeddingFromFeedback(
        String term,
        String context,
        double attention,
        double cognitiveLoad,
        boolean positiveOutcome
    ) {
        if (!termEmbeddings.containsKey(term)) return;

        double[] currentEmbedding = termEmbeddings.get(term);
        double[] contextVector = getOrCreateContextEmbedding(context);

        // Adaptive learning rate based on cognitive state
        double adaptiveLR = learningRate * attention * (1.0 - cognitiveLoad);

        if (positiveOutcome) {
            // Move embedding closer to successful context
            for (int i = 0; i < embeddingDimension; i++) {
                currentEmbedding[i] +=
                    adaptiveLR * (contextVector[i] - currentEmbedding[i]);
            }
            // Increase adaptive weight
            adaptiveWeights.put(
                term,
                Math.min(2.0, adaptiveWeights.get(term) * 1.1)
            );
        } else {
            // Move embedding away from unsuccessful context
            for (int i = 0; i < embeddingDimension; i++) {
                currentEmbedding[i] -=
                    adaptiveLR * 0.5 * (contextVector[i] - currentEmbedding[i]);
            }
            // Decrease adaptive weight
            adaptiveWeights.put(
                term,
                Math.max(0.1, adaptiveWeights.get(term) * 0.9)
            );
        }

        termEmbeddings.put(term, normalizeVector(currentEmbedding));
    }

    /**
     * Get or create context embedding for expression contexts
     */
    private double[] getOrCreateContextEmbedding(String context) {
        return contextEmbeddings.computeIfAbsent(context, ctx -> {
            // Create context embedding based on mathematical symbols and structure
            double[] embedding = new double[embeddingDimension];

            // Analyze context features
            double setOperations =
                countOccurrences(ctx, "∪∩-×") / (double) ctx.length();
            double symbols =
                countOccurrences(ctx, "{}|∈⊆⊇") / (double) ctx.length();
            double complexity = calculateContextComplexity(ctx);
            double notation = determineNotationType(ctx);

            embedding[0] = setOperations;
            embedding[1] = symbols;
            embedding[2] = complexity;
            embedding[3] = notation;

            // Fill remaining dimensions
            Random random = new Random(ctx.hashCode());
            for (int i = 4; i < embeddingDimension; i++) {
                embedding[i] =
                    (setOperations + symbols + complexity + notation) / 4.0 +
                    (random.nextGaussian() * 0.1);
            }

            return normalizeVector(embedding);
        });
    }

    /**
     * Calculate context complexity score
     */
    private double calculateContextComplexity(String context) {
        int parenDepth = 0, maxDepth = 0, braceDepth = 0;
        for (char c : context.toCharArray()) {
            if (c == '(') parenDepth++;
            else if (c == ')') parenDepth--;
            else if (c == '{') braceDepth++;
            else if (c == '}') braceDepth--;
            maxDepth = Math.max(maxDepth, Math.max(parenDepth, braceDepth));
        }
        return Math.min(1.0, maxDepth / 5.0);
    }

    /**
     * Determine notation type score
     */
    private double determineNotationType(String context) {
        if (context.matches(".*\\{[^}]*\\}.*")) return 0.8; // roster
        if (context.matches(".*\\{[^}]*\\|[^}]*\\}.*")) return 0.6; // builder
        if (context.matches(".*[∪∩-×].*")) return 0.4; // symbolic
        return 0.2; // other
    }

    /**
     * Count occurrences of characters from a set
     */
    private int countOccurrences(String text, String chars) {
        return (int) text.chars().filter(c -> chars.indexOf(c) >= 0).count();
    }

    /**
     * Get embedding vector for a term
     */
    public double[] getEmbedding(String term) {
        return termEmbeddings.get(term);
    }

    /**
     * Calculate cosine similarity between two vectors
     */
    private double cosineSimilarity(double[] vec1, double[] vec2) {
        if (vec1.length != vec2.length) return 0.0;

        double dotProduct = IntStream.range(0, vec1.length)
            .mapToDouble(i -> vec1[i] * vec2[i])
            .sum();

        double norm1 = Math.sqrt(Arrays.stream(vec1).map(x -> x * x).sum());
        double norm2 = Math.sqrt(Arrays.stream(vec2).map(x -> x * x).sum());

        if (norm1 == 0.0 || norm2 == 0.0) return 0.0;

        return dotProduct / (norm1 * norm2);
    }

    /**
     * Normalize vector to unit length
     */
    private double[] normalizeVector(double[] vector) {
        double norm = Math.sqrt(Arrays.stream(vector).map(x -> x * x).sum());
        if (norm == 0.0) return vector;

        return Arrays.stream(vector).map(x -> x / norm).toArray();
    }

    /**
     * Update cognitive alignment parameters
     */
    public void updateCognitiveAlignment(
        double attention,
        double cognitiveLoad
    ) {
        // Adapt attention weight based on current cognitive state
        if (cognitiveLoad > cognitiveLoadThreshold) {
            attentionWeight = Math.min(1.0, attentionWeight * 1.05); // Rely more on attention
        } else {
            attentionWeight = Math.max(0.3, attentionWeight * 0.98); // Allow more flexibility
        }
    }

    /**
     * Get embedding statistics for monitoring
     */
    public EmbeddingStats getEmbeddingStats() {
        double avgWeight = adaptiveWeights
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(1.0);

        long highWeightTerms = adaptiveWeights
            .values()
            .stream()
            .mapToLong(w -> w > 1.5 ? 1 : 0)
            .sum();

        return new EmbeddingStats(
            termEmbeddings.size(),
            avgWeight,
            highWeightTerms,
            attentionWeight
        );
    }

    /**
     * Similarity result class
     */
    public static class SimilarityResult {

        private final String term;
        private final double similarity;

        public SimilarityResult(String term, double similarity) {
            this.term = term;
            this.similarity = similarity;
        }

        public String getTerm() {
            return term;
        }

        public double getSimilarity() {
            return similarity;
        }

        @Override
        public String toString() {
            return String.format(
                "SimilarityResult{term='%s', similarity=%.3f}",
                term,
                similarity
            );
        }
    }

    /**
     * Embedding statistics class
     */
    public static class EmbeddingStats {

        private final int totalTerms;
        private final double avgAdaptiveWeight;
        private final long highWeightTerms;
        private final double attentionWeight;
        private final int contextCount;

        public EmbeddingStats(
            int totalTerms,
            double avgAdaptiveWeight,
            long highWeightTerms,
            double attentionWeight
        ) {
            this(totalTerms, avgAdaptiveWeight, highWeightTerms, attentionWeight, 0);
        }
        
        public EmbeddingStats(
            int totalTerms,
            double avgAdaptiveWeight,
            long highWeightTerms,
            double attentionWeight,
            int contextCount
        ) {
            this.totalTerms = totalTerms;
            this.avgAdaptiveWeight = avgAdaptiveWeight;
            this.highWeightTerms = highWeightTerms;
            this.attentionWeight = attentionWeight;
            this.contextCount = contextCount;
        }

        public int getTotalTerms() {
            return totalTerms;
        }

        public double getAvgAdaptiveWeight() {
            return avgAdaptiveWeight;
        }

        public long getHighWeightTerms() {
            return highWeightTerms;
        }

        public double getAttentionWeight() {
            return attentionWeight;
        }

        @Override
        public String toString() {
            return String.format(
                "EmbeddingStats{terms=%d, avgWeight=%.3f, highWeight=%d, attention=%.3f}",
                totalTerms,
                avgAdaptiveWeight,
                highWeightTerms,
                attentionWeight
            );
        }
    }

    /**
     * Embedding configuration class
     */
    public static class EmbeddingConfig {

        private final int dimension;
        private final double learningRate;
        private final double attentionWeight;
        private final double cognitiveLoadThreshold;

        public EmbeddingConfig(
            int dimension,
            double learningRate,
            double attentionWeight,
            double cognitiveLoadThreshold
        ) {
            this.dimension = dimension;
            this.learningRate = learningRate;
            this.attentionWeight = attentionWeight;
            this.cognitiveLoadThreshold = cognitiveLoadThreshold;
        }

        public static EmbeddingConfig defaultConfig() {
            return new EmbeddingConfig(128, 0.01, 0.7, 0.8);
        }

        public int getDimension() {
            return dimension;
        }

        public double getLearningRate() {
            return learningRate;
        }

        public double getAttentionWeight() {
            return attentionWeight;
        }

        public double getCognitiveLoadThreshold() {
            return cognitiveLoadThreshold;
        }
    }

    /**
     * Find similar expressions to an embedding vector
     * 
     * @param embedding The embedding vector to search for
     * @param limit The maximum number of results to return
     * @return A map of expressions and their similarity scores
     */
    public Map<String, Double> findSimilarExpressions(double[] embedding, int limit) {
        if (embedding == null || embedding.length != embeddingDimension) {
            return Collections.emptyMap();
        }
        
        Map<String, Double> results = new LinkedHashMap<>();
        
        contextEmbeddings.entrySet().stream()
            .map(entry -> new AbstractMap.SimpleEntry<>(
                entry.getKey(), 
                cosineSimilarity(embedding, entry.getValue())
            ))
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .limit(limit)
            .forEach(entry -> results.put(entry.getKey(), entry.getValue()));
        
        return results;
    }

    /**
     * Update feedback for an expression
     * 
     * @param expression The expression to update
     * @param embedding The embedding vector for the expression
     * @param feedback A map of feedback metrics
     */
    public void updateFeedback(String expression, double[] embedding, Map<String, Double> feedback) {
        if (expression == null || embedding == null || feedback == null) {
            return;
        }
        
        // Store or update the embedding
        if (!contextEmbeddings.containsKey(expression)) {
            contextEmbeddings.put(expression, embedding.clone());
        }
        
        // Apply feedback to adapt the embedding
        double[] currentEmbedding = contextEmbeddings.get(expression);
        double successFactor = feedback.getOrDefault("success", 0.5);
        double attention = feedback.getOrDefault("attention", 0.5);
        double cognitiveLoad = feedback.getOrDefault("cognitiveLoad", 0.5);
        
        // Adaptive learning rate
        double adaptiveLR = learningRate * attention * (1.0 - cognitiveLoad);
        
        // Update embedding based on feedback
        for (int i = 0; i < embeddingDimension; i++) {
            // Slight adjustment based on success factor
            if (successFactor > 0.5) {
                // Reinforce successful patterns
                currentEmbedding[i] += adaptiveLR * 0.1 * (successFactor - 0.5);
            } else {
                // Diminish unsuccessful patterns
                currentEmbedding[i] -= adaptiveLR * 0.1 * (0.5 - successFactor);
            }
        }
        
        // Normalize and store the updated embedding
        contextEmbeddings.put(expression, normalizeVector(currentEmbedding));
        
        // Update cognitive alignment
        updateCognitiveAlignment(attention, cognitiveLoad);
    }

    /**
     * Gets the embedding service statistics
     * 
     * @return The embedding service statistics
     */
    public EmbeddingStats getStats() {
        double avgWeight = adaptiveWeights
            .values()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(1.0);

        long highWeightTerms = adaptiveWeights
            .values()
            .stream()
            .filter(w -> w > 1.5)
            .count();
            
        int contextCount = contextEmbeddings.size();

        return new EmbeddingStats(
            termEmbeddings.size(),
            avgWeight,
            highWeightTerms,
            attentionWeight,
            contextCount
        );
    }
}
