package com.discretelogic.discrete.graphs;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * OptimizedRecommendationGraph - A hybrid graph representation with compressed bitsets
 * and concurrent hashmaps for real-time e-commerce recommendation systems.
 *
 * This implementation uses:
 * - Compressed bitsets (RoaringBitmap simulation) for user profiles and product sets
 * - Hybrid adjacency representation (matrix for dense, list for sparse)
 * - Concurrent data structures for high-throughput operations
 * - Cache-optimized layouts for improved performance
 */
public class OptimizedRecommendationGraph {

    // Core mapping structures
    private final ConcurrentHashMap<String, Integer> productToId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, String> idToProduct = new ConcurrentHashMap<>();
    private final AtomicInteger productIdCounter = new AtomicInteger(0);

    // Hybrid co-purchase graph representation
    private final ConcurrentHashMap<Integer, CompressedBitSet> frequentCoPurchaseMatrix = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Set<Integer>> sparseCoPurchaseList = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Double> coPurchaseWeights = new ConcurrentHashMap<>();

    // User profile storage using compressed bitsets
    private final ConcurrentHashMap<String, UserProfile> userProfiles = new ConcurrentHashMap<>();

    // Product metadata and scoring
    private final ConcurrentHashMap<Integer, ProductMetadata> productMetadata = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, CategoryBitSet> categoryProducts = new ConcurrentHashMap<>();

    // Performance metrics and caching
    private final AtomicLong totalRecommendations = new AtomicLong(0);
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);
    private final ConcurrentHashMap<String, CachedRecommendation> recommendationCache = new ConcurrentHashMap<>();

    // Configuration parameters
    private final int denseThreshold;
    private final int maxRecommendations;
    private final long cacheExpirationMs;

    /**
     * Constructor with default parameters.
     */
    public OptimizedRecommendationGraph() {
        this(50, 20, 300000); // 50 co-purchases for dense, max 20 recommendations, 5min cache
    }

    /**
     * Constructor with custom parameters.
     *
     * @param denseThreshold threshold for dense vs sparse representation
     * @param maxRecommendations maximum recommendations to return
     * @param cacheExpirationMs cache expiration time in milliseconds
     */
    public OptimizedRecommendationGraph(int denseThreshold, int maxRecommendations, long cacheExpirationMs) {
        this.denseThreshold = denseThreshold;
        this.maxRecommendations = maxRecommendations;
        this.cacheExpirationMs = cacheExpirationMs;
    }

    /**
     * Adds a co-purchase relationship between two products.
     *
     * @param product1 first product
     * @param product2 second product
     * @param weight co-purchase strength (0.0 to 1.0)
     * @param isFrequent whether this is a frequent co-purchase pattern
     */
    public void addCoPurchase(String product1, String product2, double weight, boolean isFrequent) {
        int id1 = getOrCreateProductId(product1);
        int id2 = getOrCreateProductId(product2);

        String weightKey = Math.min(id1, id2) + ":" + Math.max(id1, id2);
        coPurchaseWeights.put(weightKey, weight);

        if (isFrequent) {
            // Store in compressed adjacency matrix for fast access
            frequentCoPurchaseMatrix.computeIfAbsent(id1, k -> new CompressedBitSet()).add(id2);
            frequentCoPurchaseMatrix.computeIfAbsent(id2, k -> new CompressedBitSet()).add(id1);
        } else {
            // Store in sparse adjacency list
            sparseCoPurchaseList.computeIfAbsent(id1, k -> ConcurrentHashMap.newKeySet()).add(id2);
            sparseCoPurchaseList.computeIfAbsent(id2, k -> ConcurrentHashMap.newKeySet()).add(id1);
        }
    }

    /**
     * Adds a product to a user's interaction history.
     *
     * @param userId user identifier
     * @param productId product identifier
     * @param interactionType type of interaction (VIEW, CART, PURCHASE)
     * @param score interaction strength score
     */
    public void addUserInteraction(String userId, String productId, InteractionType interactionType, double score) {
        int prodId = getOrCreateProductId(productId);

        UserProfile profile = userProfiles.computeIfAbsent(userId, k -> new UserProfile());

        switch (interactionType) {
            case VIEW:
                profile.viewedProducts.add(prodId);
                profile.interactionScores.put(prodId, profile.interactionScores.getOrDefault(prodId, 0.0) + score * 0.1);
                break;
            case CART:
                profile.cartProducts.add(prodId);
                profile.interactionScores.put(prodId, profile.interactionScores.getOrDefault(prodId, 0.0) + score * 0.5);
                break;
            case PURCHASE:
                profile.purchasedProducts.add(prodId);
                profile.interactionScores.put(prodId, profile.interactionScores.getOrDefault(prodId, 0.0) + score * 1.0);
                break;
        }

        profile.lastUpdate = System.currentTimeMillis();

        // Invalidate cache for this user
        invalidateUserCache(userId);
    }

    /**
     * Generates personalized recommendations for a user.
     *
     * @param userId user identifier
     * @param contextualFilters optional filters (category, price range, etc.)
     * @return list of recommended product IDs with scores
     */
    public CompletableFuture<List<RecommendationResult>> generateRecommendations(
            String userId,
            Map<String, Object> contextualFilters) {

        totalRecommendations.incrementAndGet();

        // Check cache first
        String cacheKey = userId + ":" + (contextualFilters != null ? contextualFilters.hashCode() : 0);
        CachedRecommendation cached = recommendationCache.get(cacheKey);

        if (cached != null && !cached.isExpired(cacheExpirationMs)) {
            cacheHits.incrementAndGet();
            return CompletableFuture.completedFuture(cached.recommendations);
        }

        cacheMisses.incrementAndGet();

        return CompletableFuture.supplyAsync(() -> {
            UserProfile profile = userProfiles.get(userId);
            if (profile == null) {
                return Collections.emptyList();
            }

            // Step 1: Identify candidate products from co-purchase patterns
            CompressedBitSet candidateProducts = new CompressedBitSet();
            Map<Integer, Double> productScores = new ConcurrentHashMap<>();

            // From frequent co-purchases (dense matrix)
            addCandidatesFromFrequentCoPurchases(profile, candidateProducts, productScores);

            // From sparse co-purchases
            addCandidatesFromSparseCoPurchases(profile, candidateProducts, productScores);

            // From user similarity (collaborative filtering)
            addCandidatesFromUserSimilarity(userId, profile, candidateProducts, productScores);

            // Step 2: Apply contextual filters
            if (contextualFilters != null) {
                applyContextualFilters(candidateProducts, productScores, contextualFilters);
            }

            // Step 3: Rank and return top recommendations
            List<RecommendationResult> recommendations = rankAndSelectRecommendations(
                candidateProducts, productScores, profile);

            // Cache the results
            recommendationCache.put(cacheKey, new CachedRecommendation(recommendations));

            return recommendations;
        }, ForkJoinPool.commonPool());
    }

    /**
     * Analyzes user behavior patterns and segments.
     *
     * @param userId user identifier
     * @return user behavior analysis
     */
    public UserBehaviorAnalysis analyzeUserBehavior(String userId) {
        UserProfile profile = userProfiles.get(userId);
        if (profile == null) {
            return new UserBehaviorAnalysis();
        }

        UserBehaviorAnalysis analysis = new UserBehaviorAnalysis();
        analysis.totalViews = profile.viewedProducts.cardinality();
        analysis.totalCartAdditions = profile.cartProducts.cardinality();
        analysis.totalPurchases = profile.purchasedProducts.cardinality();

        // Calculate conversion rates
        analysis.viewToCartRate = analysis.totalViews > 0 ?
            (double) analysis.totalCartAdditions / analysis.totalViews : 0.0;
        analysis.cartToPurchaseRate = analysis.totalCartAdditions > 0 ?
            (double) analysis.totalPurchases / analysis.totalCartAdditions : 0.0;

        // Analyze category preferences
        analysis.topCategories = getTopCategoriesForUser(profile);

        // Analyze interaction recency
        analysis.lastInteractionTime = profile.lastUpdate;
        analysis.daysSinceLastInteraction = (System.currentTimeMillis() - profile.lastUpdate) / (1000 * 60 * 60 * 24);

        return analysis;
    }

    /**
     * Batch processes user interactions for improved performance.
     *
     * @param interactions list of user interactions to process
     */
    public void batchProcessInteractions(List<UserInteraction> interactions) {
        // Group interactions by user for locality
        Map<String, List<UserInteraction>> userGroups = interactions.stream()
            .collect(Collectors.groupingBy(interaction -> interaction.userId));

        // Process each user's interactions in parallel
        userGroups.entrySet().parallelStream().forEach(entry -> {
            String userId = entry.getKey();
            List<UserInteraction> userInteractions = entry.getValue();

            UserProfile profile = userProfiles.computeIfAbsent(userId, k -> new UserProfile());

            for (UserInteraction interaction : userInteractions) {
                int prodId = getOrCreateProductId(interaction.productId);

                switch (interaction.type) {
                    case VIEW:
                        profile.viewedProducts.add(prodId);
                        break;
                    case CART:
                        profile.cartProducts.add(prodId);
                        break;
                    case PURCHASE:
                        profile.purchasedProducts.add(prodId);
                        break;
                }

                // Update interaction scores
                double currentScore = profile.interactionScores.getOrDefault(prodId, 0.0);
                profile.interactionScores.put(prodId, currentScore + interaction.score);
            }

            profile.lastUpdate = System.currentTimeMillis();
            invalidateUserCache(userId);
        });
    }

    // Private helper methods

    private int getOrCreateProductId(String productId) {
        return productToId.computeIfAbsent(productId, k -> {
            int id = productIdCounter.getAndIncrement();
            idToProduct.put(id, productId);
            return id;
        });
    }

    private void addCandidatesFromFrequentCoPurchases(UserProfile profile,
            CompressedBitSet candidates, Map<Integer, Double> scores) {

        // Check cart and purchased items for co-purchase patterns
        profile.cartProducts.forEach(productId -> {
            CompressedBitSet coPurchased = frequentCoPurchaseMatrix.get(productId);
            if (coPurchased != null) {
                coPurchased.forEach(coProductId -> {
                    if (!profile.purchasedProducts.contains(coProductId)) {
                        candidates.add(coProductId);
                        String weightKey = Math.min(productId, coProductId) + ":" + Math.max(productId, coProductId);
                        double weight = coPurchaseWeights.getOrDefault(weightKey, 0.5);
                        scores.merge(coProductId, weight * 0.8, Double::sum); // 0.8 multiplier for cart items
                    }
                });
            }
        });

        profile.purchasedProducts.forEach(productId -> {
            CompressedBitSet coPurchased = frequentCoPurchaseMatrix.get(productId);
            if (coPurchased != null) {
                coPurchased.forEach(coProductId -> {
                    if (!profile.purchasedProducts.contains(coProductId)) {
                        candidates.add(coProductId);
                        String weightKey = Math.min(productId, coProductId) + ":" + Math.max(productId, coProductId);
                        double weight = coPurchaseWeights.getOrDefault(weightKey, 0.5);
                        scores.merge(coProductId, weight * 1.0, Double::sum); // Full weight for purchased items
                    }
                });
            }
        });
    }

    private void addCandidatesFromSparseCoPurchases(UserProfile profile,
            CompressedBitSet candidates, Map<Integer, Double> scores) {

        profile.cartProducts.forEach(productId -> {
            Set<Integer> coPurchased = sparseCoPurchaseList.get(productId);
            if (coPurchased != null) {
                coPurchased.forEach(coProductId -> {
                    if (!profile.purchasedProducts.contains(coProductId)) {
                        candidates.add(coProductId);
                        String weightKey = Math.min(productId, coProductId) + ":" + Math.max(productId, coProductId);
                        double weight = coPurchaseWeights.getOrDefault(weightKey, 0.3);
                        scores.merge(coProductId, weight * 0.6, Double::sum); // Lower weight for sparse patterns
                    }
                });
            }
        });
    }

    private void addCandidatesFromUserSimilarity(String userId, UserProfile profile,
            CompressedBitSet candidates, Map<Integer, Double> scores) {

        // Find similar users based on purchase overlap
        List<String> similarUsers = findSimilarUsers(userId, profile, 10);

        for (String similarUserId : similarUsers) {
            UserProfile similarProfile = userProfiles.get(similarUserId);
            if (similarProfile != null) {
                similarProfile.purchasedProducts.forEach(productId -> {
                    if (!profile.purchasedProducts.contains(productId)) {
                        candidates.add(productId);
                        scores.merge(productId, 0.4, Double::sum); // Collaborative filtering score
                    }
                });
            }
        }
    }

    private void applyContextualFilters(CompressedBitSet candidates,
            Map<Integer, Double> scores, Map<String, Object> filters) {

        if (filters.containsKey("category")) {
            String category = (String) filters.get("category");
            CategoryBitSet categorySet = categoryProducts.get(category);
            if (categorySet != null) {
                // Keep only products in the specified category
                candidates.and(categorySet.products);
            }
        }

        if (filters.containsKey("min_score")) {
            Double minScore = (Double) filters.get("min_score");
            scores.entrySet().removeIf(entry -> entry.getValue() < minScore);
        }

        if (filters.containsKey("exclude_viewed")) {
            Boolean excludeViewed = (Boolean) filters.get("exclude_viewed");
            if (excludeViewed) {
                // Implementation would remove viewed products from candidates
            }
        }
    }

    private List<RecommendationResult> rankAndSelectRecommendations(
            CompressedBitSet candidates, Map<Integer, Double> scores, UserProfile profile) {

        return candidates.stream()
            .mapToObj(productId -> {
                double score = scores.getOrDefault(productId, 0.0);

                // Apply user preference boost
                Double userScore = profile.interactionScores.get(productId);
                if (userScore != null) {
                    score += userScore * 0.3; // Boost based on user's past interactions
                }

                // Apply recency decay
                ProductMetadata metadata = productMetadata.get(productId);
                if (metadata != null) {
                    long daysSinceUpdate = (System.currentTimeMillis() - metadata.lastUpdated) / (1000 * 60 * 60 * 24);
                    double recencyFactor = Math.exp(-daysSinceUpdate / 30.0); // 30-day half-life
                    score *= recencyFactor;
                }

                return new RecommendationResult(idToProduct.get(productId), productId, score);
            })
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .limit(maxRecommendations)
            .collect(Collectors.toList());
    }

    private List<String> findSimilarUsers(String userId, UserProfile profile, int maxSimilar) {
        return userProfiles.entrySet().parallelStream()
            .filter(entry -> !entry.getKey().equals(userId))
            .map(entry -> {
                double similarity = computeUserSimilarity(profile, entry.getValue());
                return new UserSimilarity(entry.getKey(), similarity);
            })
            .sorted((a, b) -> Double.compare(b.similarity, a.similarity))
            .limit(maxSimilar)
            .map(us -> us.userId)
            .collect(Collectors.toList());
    }

    private double computeUserSimilarity(UserProfile profile1, UserProfile profile2) {
        // Jaccard similarity on purchased products
        CompressedBitSet intersection = profile1.purchasedProducts.clone();
        intersection.and(profile2.purchasedProducts);

        CompressedBitSet union = profile1.purchasedProducts.clone();
        union.or(profile2.purchasedProducts);

        return union.cardinality() > 0 ?
            (double) intersection.cardinality() / union.cardinality() : 0.0;
    }

    private List<String> getTopCategoriesForUser(UserProfile profile) {
        Map<String, Integer> categoryCount = new HashMap<>();

        profile.purchasedProducts.forEach(productId -> {
            ProductMetadata metadata = productMetadata.get(productId);
            if (metadata != null && metadata.category != null) {
                categoryCount.merge(metadata.category, 1, Integer::sum);
            }
        });

        return categoryCount.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(5)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private void invalidateUserCache(String userId) {
        recommendationCache.entrySet().removeIf(entry -> entry.getKey().startsWith(userId + ":"));
    }

    // Public metrics and monitoring

    public RecommendationMetrics getMetrics() {
        return new RecommendationMetrics(
            productToId.size(),
            userProfiles.size(),
            totalRecommendations.get(),
            cacheHits.get(),
            cacheMisses.get(),
            recommendationCache.size(),
            frequentCoPurchaseMatrix.size(),
            sparseCoPurchaseList.size()
        );
    }

    // Supporting classes and enums

    public enum InteractionType {
        VIEW, CART, PURCHASE
    }

    public static class UserInteraction {
        public final String userId;
        public final String productId;
        public final InteractionType type;
        public final double score;
        public final long timestamp;

        public UserInteraction(String userId, String productId, InteractionType type, double score) {
            this.userId = userId;
            this.productId = productId;
            this.type = type;
            this.score = score;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public static class RecommendationResult {
        public final String productId;
        public final int internalId;
        public final double score;

        public RecommendationResult(String productId, int internalId, double score) {
            this.productId = productId;
            this.internalId = internalId;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("RecommendationResult{productId='%s', score=%.3f}", productId, score);
        }
    }

    public static class UserBehaviorAnalysis {
        public int totalViews = 0;
        public int totalCartAdditions = 0;
        public int totalPurchases = 0;
        public double viewToCartRate = 0.0;
        public double cartToPurchaseRate = 0.0;
        public List<String> topCategories = new ArrayList<>();
        public long lastInteractionTime = 0;
        public long daysSinceLastInteraction = 0;

        @Override
        public String toString() {
            return String.format(
                "UserBehaviorAnalysis{views=%d, cart=%d, purchases=%d, " +
                "viewToCart=%.2f%%, cartToPurchase=%.2f%%, daysSinceLast=%d}",
                totalViews, totalCartAdditions, totalPurchases,
                viewToCartRate * 100, cartToPurchaseRate * 100, daysSinceLastInteraction
            );
        }
    }

    public static class RecommendationMetrics {
        public final int totalProducts;
        public final int totalUsers;
        public final long totalRecommendations;
        public final long cacheHits;
        public final long cacheMisses;
        public final int cacheSize;
        public final int frequentCoPurchases;
        public final int sparseCoPurchases;

        public RecommendationMetrics(int totalProducts, int totalUsers, long totalRecommendations,
                                   long cacheHits, long cacheMisses, int cacheSize,
                                   int frequentCoPurchases, int sparseCoPurchases) {
            this.totalProducts = totalProducts;
            this.totalUsers = totalUsers;
            this.totalRecommendations = totalRecommendations;
            this.cacheHits = cacheHits;
            this.cacheMisses = cacheMisses;
            this.cacheSize = cacheSize;
            this.frequentCoPurchases = frequentCoPurchases;
            this.sparseCoPurchases = sparseCoPurchases;
        }

        public double getCacheHitRate() {
            long total = cacheHits + cacheMisses;
            return total > 0 ? (double) cacheHits / total : 0.0;
        }

        @Override
        public String toString() {
            return String.format(
                "RecommendationMetrics{products=%d, users=%d, recommendations=%d, " +
                "cacheHitRate=%.2f%%, cacheSize=%d, frequentCo=%d, sparseCo=%d}",
                totalProducts, totalUsers, totalRecommendations,
                getCacheHitRate() * 100, cacheSize, frequentCoPurchases, sparseCoPurchases
            );
        }
    }

    // Private supporting classes

    private static class UserProfile {
        final CompressedBitSet viewedProducts = new CompressedBitSet();
        final CompressedBitSet cartProducts = new CompressedBitSet();
        final CompressedBitSet purchasedProducts = new CompressedBitSet();
        final ConcurrentHashMap<Integer, Double> interactionScores = new ConcurrentHashMap<>();
        volatile long lastUpdate = System.currentTimeMillis();
    }

    private static class ProductMetadata {
        final String category;
        final double baseScore;
        final long lastUpdated;

        ProductMetadata(String category, double baseScore) {
            this.category = category;
            this.baseScore = baseScore;
            this.lastUpdated = System.currentTimeMillis();
        }
    }

    private static class CategoryBitSet {
        final CompressedBitSet products = new CompressedBitSet();
    }

    private static class CachedRecommendation {
        final List<RecommendationResult> recommendations;
        final long timestamp;

        CachedRecommendation(List<RecommendationResult> recommendations) {
            this.recommendations = recommendations;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired(long expirationMs) {
            return (System.currentTimeMillis() - timestamp) > expirationMs;
        }
    }

    private static class UserSimilarity {
        final String userId;
        final double similarity;

        UserSimilarity(String userId, double similarity) {
            this.userId = userId;
            this.similarity = similarity;
        }
    }

    // Simplified CompressedBitSet implementation (in production, use RoaringBitmap)
    private static class CompressedBitSet implements Cloneable {
        private final Set<Integer> bits = ConcurrentHashMap.newKeySet();

        void add(int bit) {
            bits.add(bit);
        }

        boolean contains(int bit) {
            return bits.contains(bit);
        }

        void and(CompressedBitSet other) {
            bits.retainAll(other.bits);
        }

        void or(CompressedBitSet other) {
            bits.addAll(other.bits);
        }

        int cardinality() {
            return bits.size();
        }

        void forEach(java.util.function.IntConsumer action) {
            bits.forEach(action::accept);
        }

        java.util.stream.Stream<Integer> stream() {
            return bits.stream();
        }

        @Override
        public CompressedBitSet clone() {
            CompressedBitSet clone = new CompressedBitSet();
            clone.bits.addAll(this.bits);
            return clone;
        }
    }
}
