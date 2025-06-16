package edu.ucsb.cs.cognitivedm;

import com.discretelogic.util.LoggingUtil;
import edu.ucsb.cs.cognitivedm.education.CognitiveEducationFramework;
import edu.ucsb.cs.cognitivedm.education.LearningAnalytics;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.framework.CognitiveFrameworkTypes;
import edu.ucsb.cs.cognitivedm.framework.CognitiveFrameworkInterfaces;
import edu.ucsb.cs.cognitivedm.framework.CognitiveFrameworkUtility;
import static edu.ucsb.cs.cognitivedm.framework.CognitiveFrameworkUtility.*;
import edu.ucsb.cs.cognitivedm.graph.ScalableConcurrentGraphEngine;
import edu.ucsb.cs.cognitivedm.patterns.PatternDetector;
import edu.ucsb.cs.cognitivedm.recommendations.CognitiveRecommendationEngine;
import edu.ucsb.cs.cognitivedm.sessions.CognitiveSessionManager;
import edu.ucsb.cs.cognitivedm.education.LearningPath;
import edu.ucsb.cs.cognitivedm.education.LearningContent;
import edu.ucsb.cs.cognitivedm.education.DifficultyLevel;
import edu.ucsb.cs.cognitivedm.framework.CognitiveSession;
import edu.ucsb.cs.cognitivedm.recommendations.Recommendation;
import edu.ucsb.cs.cognitivedm.recommendations.RecommendationRequest;
import edu.ucsb.cs.cognitivedm.recommendations.RecommendationType;

import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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

    private static final Logger LOGGER = LoggingUtil.getLogger(CognitiveDiscreteMathLibrary.class);

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
        LOGGER.info("Initializing Cognitive Discrete Mathematics Library");
        
        this.cognitiveScales = cognitiveScales;
        this.threadPoolSize = threadPoolSize;
        this.defaultAttentionThreshold = 0.6;
        this.libraryConfig = new ConcurrentHashMap<>();

        // Initialize core components with error handling
        try {
            this.cognitiveFramework = new AttentionRecognitionFramework(cognitiveScales);
            this.graphEngine = new ScalableConcurrentGraphEngine<>(threadPoolSize, cognitiveScales);
            this.educationFramework = new CognitiveEducationFramework(cognitiveFramework);
            this.recommendationEngine = new CognitiveRecommendationEngine(cognitiveFramework, educationFramework);
            this.expressionProcessor = new MathExpressionProcessor(cognitiveFramework);
            this.sessionManager = new CognitiveSessionManager(cognitiveFramework);

            // Setup component integration
            setupComponentIntegration();

            // Initialize with sample mathematical content
            initializeMathematicalContent();

            LOGGER.info("Cognitive Discrete Mathematics Library initialized with {} cognitive scales and {} threads", 
                cognitiveScales, threadPoolSize);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Cognitive Discrete Mathematics Library", e);
            throw new IllegalStateException("Library initialization failed", e);
        }
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
            CognitiveFrameworkTypes.ContentType.CONCEPT,
            CognitiveFrameworkTypes.DifficultyLevel.BEGINNER
        );

        addMathematicalContent(
            "boolean_logic",
            "Boolean Logic",
            "Propositional logic and truth tables",
            CognitiveFrameworkTypes.ContentType.PROCEDURE,
            CognitiveFrameworkTypes.DifficultyLevel.INTERMEDIATE
        );

        addMathematicalContent(
            "graph_algorithms",
            "Graph Algorithms",
            "Graph traversal and shortest path algorithms",
            CognitiveFrameworkTypes.ContentType.PROBLEM_SOLVING,
            CognitiveFrameworkTypes.DifficultyLevel.ADVANCED
        );

        addMathematicalContent(
            "combinatorics",
            "Combinatorics",
            "Permutations, combinations, and counting principles",
            CognitiveFrameworkTypes.ContentType.CONCEPT,
            CognitiveFrameworkTypes.DifficultyLevel.INTERMEDIATE
        );

        addMathematicalContent(
            "discrete_probability",
            "Discrete Probability",
            "Probability in discrete sample spaces",
            CognitiveFrameworkTypes.ContentType.PROBLEM_SOLVING,
            CognitiveFrameworkTypes.DifficultyLevel.ADVANCED
        );

        System.out.println("📚 Mathematical content library initialized");
    }

    private void addMathematicalContent(
        String id,
        String title,
        String description,
        CognitiveFrameworkTypes.ContentType type,
        CognitiveFrameworkTypes.DifficultyLevel difficulty
    ) {
        CognitiveFrameworkTypes.LearningContent content = createLearningContent(
            id,
            title,
            "Discrete Mathematics",
            type,
            difficulty
        );
        content.setContent("description", description);

        // Set cognitive style alignments for mathematics
        content.setStyleAlignment(CognitiveFrameworkTypes.LearningStyle.ANALYTICAL, 0.9);
        content.setStyleAlignment(CognitiveFrameworkTypes.LearningStyle.VISUAL, 0.7);
        content.setStyleAlignment(CognitiveFrameworkTypes.LearningStyle.KINESTHETIC, 0.5);

        educationFramework.addLearningContent(content);
        recommendationEngine.addLearningContent(id, content);
    }

    // ================== MAIN API METHODS ==================

    /**
     * Process a mathematical expression with cognitive awareness
     *
     * @param expression Mathematical expression string
     * @param userId User identifier for personalization
     * @return CompletableFuture with processed MathExpression
     */
    public CompletableFuture<MathExpression> processExpression(
        String expression,
        String userId
    ) {
        LOGGER.info("Processing expression: {} for user: {}", expression, userId);
        
        try {
            // Get or create cognitive session for user
            CognitiveFrameworkInterfaces.CognitiveSession session = sessionManager.getOrCreateSession(userId);
            
            // Process expression through cognitive framework
            CompletableFuture<MathExpression> result = cognitiveFramework
                .processExpression(expression)
                .thenApply(processingResult -> {
                    // Extract cognitive state from processing
                    AttentionRecognitionFramework.CognitiveState cognitiveState = 
                        processingResult.getCognitiveState();
                    
                    // Create mathematical expression with cognitive enhancement
                    MathExpression mathExpr = new MathExpression(expression)
                        .enhanceWithCognition(cognitiveState);
                    
                    // Add expression to graph for pattern recognition
                    graphEngine.addNode(mathExpr.getId(), mathExpr);
                    
                    return mathExpr;
                });
            
            LOGGER.debug("Expression processing started asynchronously");
            return result;
            
        } catch (Exception e) {
            LOGGER.error("Error processing expression", e);
            return CompletableFuture.failedFuture(
                new CompletionException("Expression processing failed", e)
            );
        }
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
                AttentionRecognitionFramework.CognitiveState currentState =
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
                    new AttentionRecognitionFramework.CognitiveState(0.5, 0.5, 0.3)
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
        private AttentionRecognitionFramework.CognitiveState processingState;
        private final Map<String, Double> cognitiveTags;
        private final Map<String, Object> metadata;
        private final long createdTime;

        public MathExpression(String expression) {
            this.id = UUID.randomUUID().toString();
            this.expression = expression;
            this.cognitiveTags = new HashMap<>();
            this.metadata = new HashMap<>();
            this.createdTime = System.currentTimeMillis();
        }

        public MathExpression enhanceWithCognition(AttentionRecognitionFramework.CognitiveState state) {
            this.processingState = state;
            
            // Calculate cognitive tags from state
            cognitiveTags.put("attention_level", state.getAttention());
            cognitiveTags.put("recognition_level", state.getRecognition());
            cognitiveTags.put("wandering_level", state.getWandering());
            cognitiveTags.put("cognitive_load", state.getCognitiveLoad());
            
            // Add metadata about cognitive processing
            metadata.put("flow_state", state.isInFlowState());
            metadata.put("processing_time", System.currentTimeMillis() - createdTime);
            
            return this;
        }

        public String getId() {
            return id;
        }

        public String getExpression() {
            return expression;
        }

        public AttentionRecognitionFramework.CognitiveState getProcessingState() {
            return processingState;
        }

        public Map<String, Double> getCognitiveTags() {
            return cognitiveTags;
        }

        public Map<String, Object> getMetadata() {
            return metadata;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        @Override
        public String toString() {
            return "MathExpression{" +
                "id='" + id + '\'' +
                ", expression='" + expression + '\'' +
                ", processingState=" + processingState +
                '}';
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
