package edu.ucsb.cs.cognitivedm;

import edu.ucsb.cs.cognitivedm.education.CognitiveEducationFramework;
import edu.ucsb.cs.cognitivedm.education.CognitiveEducationFramework.*;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.CognitiveState;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework.ProcessingResult;
import edu.ucsb.cs.cognitivedm.graph.ScalableConcurrentGraphEngine;
import edu.ucsb.cs.cognitivedm.patterns.PatternDetector;
import edu.ucsb.cs.cognitivedm.recommendations.CognitiveRecommendationEngine;
import edu.ucsb.cs.cognitivedm.recommendations.CognitiveRecommendationEngine.*;
import edu.ucsb.cs.cognitivedm.sessions.CognitiveSessionManager;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Cognitive-Inspired Discrete Mathematics Library
 *
 * Main integration class that brings together all cognitive components
 * with enhanced mathematical expression processing capabilities.
 *
 * This library extends traditional discrete mathematics operations with:
 * - Attention-aware expression processing
 * - Cognitive load management
 * - Adaptive learning recommendations
 * - Multi-scale pattern recognition
 * - Flow state optimization for mathematical problem-solving
 */
public class CognitiveDiscreteMathLibrary {

    private final AttentionRecognitionFramework cognitiveFramework;
    private final ScalableConcurrentGraphEngine<MathExpression> graphEngine;
    private final CognitiveEducationFramework educationFramework;
    private final CognitiveRecommendationEngine recommendationEngine;
    private final MathExpressionProcessor expressionProcessor;
    private final CognitiveSessionManager sessionManager;

    // Configuration parameters
    private final int cognitiveScales;
    private final int threadPoolSize;
    private final double defaultAttentionThreshold;
    private final Map<String, Object> libraryConfig;

    /**
     * Initialize the cognitive discrete mathematics library
     *
     * @param cognitiveScales Number of cognitive processing scales (recommended: 3-5)
     * @param threadPoolSize Size of concurrent processing thread pool
     */
    public CognitiveDiscreteMathLibrary(
        int cognitiveScales,
        int threadPoolSize
    ) {
        this.cognitiveScales = cognitiveScales;
        this.threadPoolSize = threadPoolSize;
        this.defaultAttentionThreshold = 0.6;
        this.libraryConfig = new ConcurrentHashMap<>();

        // Initialize core components
        this.cognitiveFramework = new AttentionRecognitionFramework(
            cognitiveScales
        );
        this.graphEngine = new ScalableConcurrentGraphEngine<>(
            threadPoolSize,
            cognitiveScales
        );
        this.educationFramework = new CognitiveEducationFramework(
            cognitiveFramework
        );
        this.recommendationEngine = new CognitiveRecommendationEngine(
            cognitiveFramework,
            educationFramework
        );
        this.expressionProcessor = new MathExpressionProcessor(
            cognitiveFramework
        );
        this.sessionManager = new CognitiveSessionManager(cognitiveFramework);

        // Setup component integration
        setupComponentIntegration();

        // Initialize with sample mathematical content
        initializeMathematicalContent();

        System.out.println(
            "🧠 Cognitive Discrete Mathematics Library initialized with " +
            cognitiveScales +
            " cognitive scales and " +
            threadPoolSize +
            " threads"
        );
    }

    /**
     * Simplified constructor with default parameters
     */
    public CognitiveDiscreteMathLibrary() {
        this(3, 4); // Default: 3 cognitive scales, 4 threads
    }

    /**
     * Setup integration between all framework components
     */
    private void setupComponentIntegration() {
        // Connect recommendation engine with other components
        recommendationEngine.updateLearnerProfile(
            "system",
            educationFramework.registerLearner("system")
        );

        // Setup educational monitoring
        educationFramework.initializeSampleContent();

        // Configure expression processor
        expressionProcessor.setRecommendationEngine(recommendationEngine);
        expressionProcessor.setEducationFramework(educationFramework);

        System.out.println("✅ Component integration completed");
    }

    /**
     * Initialize mathematical content for discrete mathematics
     */
    private void initializeMathematicalContent() {
        // Add discrete mathematics specific content
        addMathematicalContent(
            "set_operations",
            "Set Operations",
            "Basic set operations: union, intersection, complement",
            ContentType.CONCEPT,
            DifficultyLevel.BEGINNER
        );

        addMathematicalContent(
            "boolean_logic",
            "Boolean Logic",
            "Propositional logic and truth tables",
            ContentType.PROCEDURE,
            DifficultyLevel.INTERMEDIATE
        );

        addMathematicalContent(
            "graph_algorithms",
            "Graph Algorithms",
            "Graph traversal and shortest path algorithms",
            ContentType.PROBLEM_SOLVING,
            DifficultyLevel.ADVANCED
        );

        addMathematicalContent(
            "combinatorics",
            "Combinatorics",
            "Permutations, combinations, and counting principles",
            ContentType.CONCEPT,
            DifficultyLevel.INTERMEDIATE
        );

        addMathematicalContent(
            "discrete_probability",
            "Discrete Probability",
            "Probability in discrete sample spaces",
            ContentType.PROBLEM_SOLVING,
            DifficultyLevel.ADVANCED
        );

        System.out.println("📚 Mathematical content library initialized");
    }

    private void addMathematicalContent(
        String id,
        String title,
        String description,
        ContentType type,
        DifficultyLevel difficulty
    ) {
        LearningContent content = new LearningContent(
            id,
            title,
            "Discrete Mathematics",
            type,
            difficulty
        );
        content.setContent("description", description);

        // Set cognitive style alignments for mathematics
        content.setStyleAlignment(LearningStyle.ANALYTICAL, 0.9);
        content.setStyleAlignment(LearningStyle.VISUAL, 0.7);
        content.setStyleAlignment(LearningStyle.KINESTHETIC, 0.5);

        educationFramework.addLearningContent(content);
        recommendationEngine.addLearningContent(id, content);
    }

    // ================== MAIN API METHODS ==================

    /**
     * Process a mathematical expression with cognitive awareness
     *
     * @param expression Mathematical expression string
     * @param userId User identifier for personalization
     * @return Enhanced mathematical expression with cognitive processing
     */
    public CompletableFuture<MathExpression> processExpression(
        String expression,
        String userId
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Get or create user session
                CognitiveSession session = sessionManager.getOrCreateSession(
                    userId
                );

                // Process through cognitive framework
                var cognitiveResults = cognitiveFramework
                    .processMultiScale(expression)
                    .get(5, TimeUnit.SECONDS);
                CognitiveState currentState = cognitiveResults
                    .get(0)
                    .getCognitiveState();

                // Update user's cognitive state
                session.updateCognitiveState(currentState);

                // Create enhanced math expression
                MathExpression mathExpr =
                    expressionProcessor.createMathExpression(
                        expression,
                        currentState
                    );

                // Add to graph for relationship analysis
                graphEngine.addNode(mathExpr.getId(), mathExpr);

                // Process with cognitive context
                var processingResult = graphEngine
                    .processNode(
                        mathExpr.getId(),
                        expr -> expr.enhanceWithCognition(currentState),
                        currentState,
                        2
                    )
                    .get(5, TimeUnit.SECONDS);

                if (processingResult.isSuccess()) {
                    return processingResult.getProcessedData();
                } else {
                    System.err.println(
                        "Graph processing failed: " +
                        processingResult.getErrorMessage()
                    );
                    return mathExpr; // Return original if processing fails
                }
            } catch (Exception e) {
                System.err.println(
                    "Error processing expression: " + e.getMessage()
                );
                // Return basic expression on error
                return expressionProcessor.createMathExpression(
                    expression,
                    new CognitiveState(0.5, 0.5, 0.3)
                );
            }
        });
    }

    /**
     * Generate cognitive-aware recommendations for mathematical learning
     *
     * @param userId User identifier
     * @param currentTopic Current mathematical topic or null for general recommendations
     * @param maxRecommendations Maximum number of recommendations
     * @return List of personalized recommendations
     */
    public CompletableFuture<List<Recommendation>> generateMathRecommendations(
        String userId,
        String currentTopic,
        int maxRecommendations
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                CognitiveSession session = sessionManager.getOrCreateSession(
                    userId
                );
                CognitiveState currentState =
                    session.getCurrentCognitiveState();

                // Create recommendation request
                RecommendationRequest request = new RecommendationRequest(
                    userId,
                    currentState,
                    currentTopic != null
                        ? currentTopic
                        : "Discrete Mathematics",
                    maxRecommendations,
                    EnumSet.allOf(RecommendationType.class),
                    new HashMap<>()
                );

                // Add mathematical context
                request.getContext().put("domainType", "mathematics");
                request
                    .getContext()
                    .put("cognitiveLoad", currentState.getCognitiveLoad());
                request
                    .getContext()
                    .put("attentionLevel", currentState.getAttention());

                return recommendationEngine
                    .generateRecommendations(request)
                    .get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.err.println(
                    "Error generating recommendations: " + e.getMessage()
                );
                return Collections.emptyList();
            }
        });
    }

    /**
     * Create adaptive learning path for discrete mathematics
     *
     * @param userId User identifier
     * @param targetLevel Target difficulty level
     * @param focusAreas Specific areas to focus on (optional)
     * @return Personalized learning path
     */
    public CompletableFuture<LearningPath> createAdaptiveLearningPath(
        String userId,
        DifficultyLevel targetLevel,
        List<String> focusAreas
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Ensure user is registered in education framework
                if (educationFramework.getLearnerProfile(userId) == null) {
                    educationFramework.registerLearner(userId);
                }

                // Update cognitive state from current session
                CognitiveSession session = sessionManager.getOrCreateSession(
                    userId
                );
                educationFramework.updateLearnerState(
                    userId,
                    session.getCurrentCognitiveState()
                );

                // Generate learning path
                LearningPath path = educationFramework
                    .createLearningPath(
                        userId,
                        "Discrete Mathematics",
                        targetLevel
                    )
                    .get(10, TimeUnit.SECONDS);

                // Filter path based on focus areas if specified
                if (focusAreas != null && !focusAreas.isEmpty()) {
                    path = filterLearningPathByFocusAreas(path, focusAreas);
                }

                return path;
            } catch (Exception e) {
                System.err.println(
                    "Error creating learning path: " + e.getMessage()
                );
                // Return empty path on error
                return new LearningPath(
                    userId,
                    "Discrete Mathematics",
                    Collections.emptyList(),
                    new CognitiveState(0.5, 0.5, 0.3)
                );
            }
        });
    }

    /**
     * Analyze mathematical problem-solving patterns
     *
     * @param userId User identifier
     * @param problemHistory List of solved problems with timestamps
     * @return Pattern analysis results
     */
    public MathPatternAnalysis analyzeProblemSolvingPatterns(
        String userId,
        List<SolvedProblem> problemHistory
    ) {
        // Convert problem history to cognitive time series
        List<double[]> timeSeries = problemHistory
            .stream()
            .map(problem ->
                new double[] {
                    problem.getDifficultyScore(),
                    problem.getAccuracyScore(),
                    problem.getTimeEfficiencyScore(),
                }
            )
            .collect(Collectors.toList());

        // Detect patterns using our pattern detector
        var patterns = PatternDetector.analyzeSequence(timeSeries);

        // Create mathematical pattern analysis
        return new MathPatternAnalysis(
            userId,
            patterns,
            calculateLearningTrends(problemHistory)
        );
    }

    /**
     * Start an interactive mathematical session with cognitive monitoring
     *
     * @param userId User identifier
     * @param sessionType Type of mathematical session
     * @return Interactive cognitive session
     */
    public CognitiveSession startInteractiveSession(
        String userId,
        MathSessionType sessionType
    ) {
        CognitiveSession session = sessionManager.getOrCreateSession(userId);
        session.setSessionType(sessionType);
        session.startSession();

        // Initialize session with appropriate cognitive monitoring
        switch (sessionType) {
            case PROBLEM_SOLVING:
                session.setCognitiveMonitoringInterval(30); // 30 seconds
                break;
            case CONCEPT_LEARNING:
                session.setCognitiveMonitoringInterval(60); // 1 minute
                break;
            case PRACTICE_EXERCISES:
                session.setCognitiveMonitoringInterval(20); // 20 seconds
                break;
            case EXPLORATION:
                session.setCognitiveMonitoringInterval(90); // 1.5 minutes
                break;
        }

        return session;
    }

    /**
     * Get comprehensive system analytics
     *
     * @return System performance and usage analytics
     */
    public CognitiveLibraryAnalytics getSystemAnalytics() {
        var cognitiveAnalysis = cognitiveFramework.getSystemAnalysis();
        var graphStats = graphEngine.getStatistics();
        var educationAnalytics = educationFramework.getSystemAnalytics();
        var recommendationStats = recommendationEngine.getStatistics();

        return new CognitiveLibraryAnalytics(
            cognitiveAnalysis,
            graphStats,
            educationAnalytics,
            recommendationStats,
            sessionManager.getActiveSessionCount(),
            calculateSystemEfficiency()
        );
    }

    // ================== UTILITY METHODS ==================

    private LearningPath filterLearningPathByFocusAreas(
        LearningPath originalPath,
        List<String> focusAreas
    ) {
        List<LearningContent> filteredContent = originalPath
            .getContent()
            .stream()
            .filter(content ->
                focusAreas
                    .stream()
                    .anyMatch(
                        area ->
                            content
                                .getTitle()
                                .toLowerCase()
                                .contains(area.toLowerCase()) ||
                            content
                                .getContent()
                                .get("description")
                                .toString()
                                .toLowerCase()
                                .contains(area.toLowerCase())
                    )
            )
            .collect(Collectors.toList());

        return new LearningPath(
            originalPath.getLearnerId(),
            originalPath.getSubject(),
            filteredContent,
            originalPath.getInitialCognitiveState()
        );
    }

    private Map<String, Double> calculateLearningTrends(
        List<SolvedProblem> problemHistory
    ) {
        Map<String, Double> trends = new HashMap<>();

        if (problemHistory.size() < 2) {
            return trends; // Need at least 2 problems for trends
        }

        // Calculate difficulty progression
        double difficultyTrend = calculateTrend(
            problemHistory
                .stream()
                .mapToDouble(SolvedProblem::getDifficultyScore)
                .toArray()
        );
        trends.put("difficultyProgression", difficultyTrend);

        // Calculate accuracy trend
        double accuracyTrend = calculateTrend(
            problemHistory
                .stream()
                .mapToDouble(SolvedProblem::getAccuracyScore)
                .toArray()
        );
        trends.put("accuracyTrend", accuracyTrend);

        // Calculate efficiency trend
        double efficiencyTrend = calculateTrend(
            problemHistory
                .stream()
                .mapToDouble(SolvedProblem::getTimeEfficiencyScore)
                .toArray()
        );
        trends.put("efficiencyTrend", efficiencyTrend);

        return trends;
    }

    private double calculateTrend(double[] values) {
        if (values.length < 2) return 0.0;

        // Simple linear trend calculation
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
        int n = values.length;

        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += values[i];
            sumXY += i * values[i];
            sumXX += i * i;
        }

        // Calculate slope
        return (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
    }

    private double calculateSystemEfficiency() {
        // Calculate overall system efficiency based on component performance
        double cognitiveEfficiency =
            1.0 -
            cognitiveFramework.getSystemAnalysis().getSystemCognitiveLoad();
        double graphEfficiency = Math.min(
            1.0,
            100.0 / graphEngine.getStatistics().getAverageProcessingTimeMs()
        );
        double educationEfficiency = educationFramework
            .getSystemAnalytics()
            .getAverageEngagement();

        return (
            (cognitiveEfficiency + graphEfficiency + educationEfficiency) / 3.0
        );
    }

    // ================== CONFIGURATION METHODS ==================

    /**
     * Configure library parameters
     */
    public void setConfiguration(String key, Object value) {
        libraryConfig.put(key, value);

        // Apply configuration changes
        switch (key) {
            case "defaultAttentionThreshold":
                if (value instanceof Number) {
                    sessionManager.setDefaultAttentionThreshold(
                        ((Number) value).doubleValue()
                    );
                }
                break;
            case "cognitiveMonitoringInterval":
                if (value instanceof Number) {
                    sessionManager.setDefaultMonitoringInterval(
                        ((Number) value).intValue()
                    );
                }
                break;
        }
    }

    public Object getConfiguration(String key) {
        return libraryConfig.get(key);
    }

    /**
     * Get current library configuration
     */
    public Map<String, Object> getAllConfiguration() {
        Map<String, Object> config = new HashMap<>(libraryConfig);
        config.put("cognitiveScales", cognitiveScales);
        config.put("threadPoolSize", threadPoolSize);
        config.put("defaultAttentionThreshold", defaultAttentionThreshold);
        return config;
    }

    // ================== SHUTDOWN AND CLEANUP ==================

    /**
     * Shutdown the library and clean up resources
     */
    public void shutdown() {
        System.out.println(
            "🛑 Shutting down Cognitive Discrete Mathematics Library..."
        );

        try {
            // Shutdown all components
            cognitiveFramework.shutdown();
            graphEngine.shutdown();
            educationFramework.shutdown();
            recommendationEngine.shutdown();
            sessionManager.shutdown();

            System.out.println(
                "✅ Cognitive library shutdown completed successfully"
            );
        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
    }

    // ================== GETTERS FOR COMPONENT ACCESS ==================

    public AttentionRecognitionFramework getCognitiveFramework() {
        return cognitiveFramework;
    }

    public ScalableConcurrentGraphEngine<MathExpression> getGraphEngine() {
        return graphEngine;
    }

    public CognitiveEducationFramework getEducationFramework() {
        return educationFramework;
    }

    public CognitiveRecommendationEngine getRecommendationEngine() {
        return recommendationEngine;
    }

    public CognitiveSessionManager getSessionManager() {
        return sessionManager;
    }

    // ================== SUPPORTING CLASSES ==================

    /**
     * Mathematical expression enhanced with cognitive processing
     */
    public static class MathExpression {

        private final String id;
        private final String expression;
        private CognitiveState processingState;
        private final Map<String, Double> cognitiveTags;
        private final Map<String, Object> metadata;
        private final long createdTime;

        public MathExpression(String expression) {
            this.id = UUID.randomUUID().toString();
            this.expression = expression;
            this.cognitiveTags = new ConcurrentHashMap<>();
            this.metadata = new ConcurrentHashMap<>();
            this.createdTime = System.currentTimeMillis();
        }

        public MathExpression enhanceWithCognition(CognitiveState state) {
            this.processingState = state;

            // Apply cognitive enhancements
            cognitiveTags.put("attention", state.getAttention());
            cognitiveTags.put("recognition", state.getRecognition());
            cognitiveTags.put("wandering", state.getWandering());
            cognitiveTags.put("cognitiveLoad", state.getCognitiveLoad());

            // Add complexity adjustments based on cognitive state
            if (state.getCognitiveLoad() > 0.8) {
                metadata.put("simplificationSuggested", true);
                metadata.put("complexityReduction", 0.3);
            }

            if (state.isInFlowState()) {
                metadata.put("flowStateOptimal", true);
                metadata.put("difficultyBoost", 0.2);
            }

            return this;
        }

        // Getters
        public String getId() {
            return id;
        }

        public String getExpression() {
            return expression;
        }

        public CognitiveState getProcessingState() {
            return processingState;
        }

        public Map<String, Double> getCognitiveTags() {
            return new HashMap<>(cognitiveTags);
        }

        public Map<String, Object> getMetadata() {
            return new HashMap<>(metadata);
        }

        public long getCreatedTime() {
            return createdTime;
        }

        @Override
        public String toString() {
            return expression;
        }
    }

    /**
     * Represents a solved mathematical problem for pattern analysis
     */
    public static class SolvedProblem {

        private final String problemId;
        private final double difficultyScore;
        private final double accuracyScore;
        private final double timeEfficiencyScore;
        private final long timestamp;

        public SolvedProblem(
            String problemId,
            double difficultyScore,
            double accuracyScore,
            double timeEfficiencyScore
        ) {
            this.problemId = problemId;
            this.difficultyScore = difficultyScore;
            this.accuracyScore = accuracyScore;
            this.timeEfficiencyScore = timeEfficiencyScore;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public String getProblemId() {
            return problemId;
        }

        public double getDifficultyScore() {
            return difficultyScore;
        }

        public double getAccuracyScore() {
            return accuracyScore;
        }

        public double getTimeEfficiencyScore() {
            return timeEfficiencyScore;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    /**
     * Mathematical pattern analysis results
     */
    public static class MathPatternAnalysis {

        private final String userId;
        private final List<
            AttentionRecognitionFramework.Pattern
        > cognitivePatterns;
        private final Map<String, Double> learningTrends;
        private final long analysisTime;

        public MathPatternAnalysis(
            String userId,
            List<AttentionRecognitionFramework.Pattern> cognitivePatterns,
            Map<String, Double> learningTrends
        ) {
            this.userId = userId;
            this.cognitivePatterns = new ArrayList<>(cognitivePatterns);
            this.learningTrends = new HashMap<>(learningTrends);
            this.analysisTime = System.currentTimeMillis();
        }

        // Getters
        public String getUserId() {
            return userId;
        }

        public List<
            AttentionRecognitionFramework.Pattern
        > getCognitivePatterns() {
            return new ArrayList<>(cognitivePatterns);
        }

        public Map<String, Double> getLearningTrends() {
            return new HashMap<>(learningTrends);
        }

        public long getAnalysisTime() {
            return analysisTime;
        }
    }

    /**
     * Types of mathematical sessions
     */
    public enum MathSessionType {
        PROBLEM_SOLVING, // Focused problem-solving sessions
        CONCEPT_LEARNING, // Concept introduction and explanation
        PRACTICE_EXERCISES, // Drill and practice
        EXPLORATION, // Open-ended mathematical exploration
    }

    /**
     * Comprehensive library analytics
     */
    public static class CognitiveLibraryAnalytics {

        private final AttentionRecognitionFramework.SystemAnalysis cognitiveAnalysis;
        private final ScalableConcurrentGraphEngine.GraphStatistics graphStats;
        private final LearningAnalytics educationAnalytics;
        private final CognitiveRecommendationEngine.RecommendationStatistics recommendationStats;
        private final int activeSessionCount;
        private final double systemEfficiency;
        private final long timestamp;

        public CognitiveLibraryAnalytics(
            AttentionRecognitionFramework.SystemAnalysis cognitiveAnalysis,
            ScalableConcurrentGraphEngine.GraphStatistics graphStats,
            LearningAnalytics educationAnalytics,
            CognitiveRecommendationEngine.RecommendationStatistics recommendationStats,
            int activeSessionCount,
            double systemEfficiency
        ) {
            this.cognitiveAnalysis = cognitiveAnalysis;
            this.graphStats = graphStats;
            this.educationAnalytics = educationAnalytics;
            this.recommendationStats = recommendationStats;
            this.activeSessionCount = activeSessionCount;
            this.systemEfficiency = systemEfficiency;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
        public AttentionRecognitionFramework.SystemAnalysis getCognitiveAnalysis() {
            return cognitiveAnalysis;
        }

        public ScalableConcurrentGraphEngine.GraphStatistics getGraphStats() {
            return graphStats;
        }

        public LearningAnalytics getEducationAnalytics() {
            return educationAnalytics;
        }

        public CognitiveRecommendationEngine.RecommendationStatistics getRecommendationStats() {
            return recommendationStats;
        }

        public int getActiveSessionCount() {
            return activeSessionCount;
        }

        public double getSystemEfficiency() {
            return systemEfficiency;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return String.format(
                "CognitiveLibraryAnalytics{sessions=%d, efficiency=%.3f, cognitiveState=%s}",
                activeSessionCount,
                systemEfficiency,
                cognitiveAnalysis.getSystemState()
            );
        }
    }
}
