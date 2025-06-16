package edu.ucsb.cs.cognitivedm.education;

import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cognitive Education Framework
 *
 * Provides educational scaffolding and adaptive learning support for cognitive
 * discrete mathematics, integrating with the attention-recognition framework
 * to optimize learning experiences based on cognitive load and attention patterns.
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class CognitiveEducationFramework {

    // Core learning concepts
    public enum ContentType {
        SET_THEORY,
        BOOLEAN_LOGIC,
        GRAPH_THEORY,
        COMBINATORICS,
        NUMBER_THEORY,
        PROOF_TECHNIQUES,
        RECURSIVE_STRUCTURES,
    }

    public enum DifficultyLevel {
        BEGINNER(1),
        INTERMEDIATE(2),
        ADVANCED(3),
        EXPERT(4);

        private final int level;

        DifficultyLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    public static class LearningPath {

        private final String pathId;
        private final List<String> topics;
        private final DifficultyLevel difficulty;
        private final Map<String, Double> prerequisites;
        private double completionRate;

        public LearningPath(
            String pathId,
            List<String> topics,
            DifficultyLevel difficulty
        ) {
            this.pathId = pathId;
            this.topics = new ArrayList<>(topics);
            this.difficulty = difficulty;
            this.prerequisites = new HashMap<>();
            this.completionRate = 0.0;
        }

        public String getPathId() {
            return pathId;
        }

        public List<String> getTopics() {
            return new ArrayList<>(topics);
        }

        public DifficultyLevel getDifficulty() {
            return difficulty;
        }

        public Map<String, Double> getPrerequisites() {
            return new HashMap<>(prerequisites);
        }

        public double getCompletionRate() {
            return completionRate;
        }

        public void setCompletionRate(double rate) {
            this.completionRate = Math.max(0.0, Math.min(1.0, rate));
        }

        public void addPrerequisite(String topic, double requiredMastery) {
            prerequisites.put(topic, requiredMastery);
        }
    }

    public static class CognitiveSession {

        private final String sessionId;
        private final long startTime;
        private final Map<String, Object> sessionData;
        private volatile boolean active;

        public CognitiveSession(String sessionId) {
            this.sessionId = sessionId;
            this.startTime = System.currentTimeMillis();
            this.sessionData = new ConcurrentHashMap<>();
            this.active = true;
        }

        public String getSessionId() {
            return sessionId;
        }

        public long getStartTime() {
            return startTime;
        }

        public Map<String, Object> getSessionData() {
            return new HashMap<>(sessionData);
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void addData(String key, Object value) {
            sessionData.put(key, value);
        }

        public Object getData(String key) {
            return sessionData.get(key);
        }
    }

    public static class LearningAnalytics {

        private final Map<String, Double> topicMastery;
        private final Map<String, Integer> attemptCounts;
        private final Map<String, Long> timeSpent;

        public LearningAnalytics() {
            this.topicMastery = new ConcurrentHashMap<>();
            this.attemptCounts = new ConcurrentHashMap<>();
            this.timeSpent = new ConcurrentHashMap<>();
        }

        public void updateMastery(String topic, double mastery) {
            topicMastery.put(topic, Math.max(0.0, Math.min(1.0, mastery)));
        }

        public void incrementAttempts(String topic) {
            attemptCounts.merge(topic, 1, Integer::sum);
        }

        public void addTimeSpent(String topic, long milliseconds) {
            timeSpent.merge(topic, milliseconds, Long::sum);
        }

        public double getMastery(String topic) {
            return topicMastery.getOrDefault(topic, 0.0);
        }

        public int getAttempts(String topic) {
            return attemptCounts.getOrDefault(topic, 0);
        }

        public long getTimeSpent(String topic) {
            return timeSpent.getOrDefault(topic, 0L);
        }

        public Map<String, Double> getAllMastery() {
            return new HashMap<>(topicMastery);
        }
    }

    // Framework instance variables
    private final AttentionRecognitionFramework attentionFramework;
    private final Map<String, LearningPath> learningPaths;
    private final Map<String, CognitiveSession> activeSessions;
    private final LearningAnalytics analytics;

    public CognitiveEducationFramework(
        AttentionRecognitionFramework attentionFramework
    ) {
        this.attentionFramework = attentionFramework;
        this.learningPaths = new ConcurrentHashMap<>();
        this.activeSessions = new ConcurrentHashMap<>();
        this.analytics = new LearningAnalytics();
        initializeDefaultPaths();
    }

    private void initializeDefaultPaths() {
        // Set Theory path
        LearningPath setTheoryPath = new LearningPath(
            "set_theory_basics",
            Arrays.asList(
                "sets",
                "unions",
                "intersections",
                "complements",
                "venn_diagrams"
            ),
            DifficultyLevel.BEGINNER
        );
        learningPaths.put("set_theory_basics", setTheoryPath);

        // Boolean Logic path
        LearningPath booleanLogicPath = new LearningPath(
            "boolean_logic_fundamentals",
            Arrays.asList(
                "truth_tables",
                "logical_operators",
                "boolean_algebra",
                "karnaugh_maps"
            ),
            DifficultyLevel.INTERMEDIATE
        );
        booleanLogicPath.addPrerequisite("set_theory_basics", 0.7);
        learningPaths.put("boolean_logic_fundamentals", booleanLogicPath);

        // Graph Theory path
        LearningPath graphTheoryPath = new LearningPath(
            "graph_theory_intro",
            Arrays.asList(
                "vertices",
                "edges",
                "paths",
                "cycles",
                "trees",
                "connectivity"
            ),
            DifficultyLevel.ADVANCED
        );
        graphTheoryPath.addPrerequisite("set_theory_basics", 0.8);
        graphTheoryPath.addPrerequisite("boolean_logic_fundamentals", 0.6);
        learningPaths.put("graph_theory_intro", graphTheoryPath);
    }

    public CognitiveSession createSession(String sessionId) {
        CognitiveSession session = new CognitiveSession(sessionId);
        activeSessions.put(sessionId, session);
        return session;
    }

    public void endSession(String sessionId) {
        CognitiveSession session = activeSessions.get(sessionId);
        if (session != null) {
            session.setActive(false);
            activeSessions.remove(sessionId);
        }
    }

    public LearningPath createLearningPath(
        String pathId,
        List<String> topics,
        DifficultyLevel difficulty
    ) {
        LearningPath path = new LearningPath(pathId, topics, difficulty);
        learningPaths.put(pathId, path);
        return path;
    }

    public LearningPath getLearningPath(String pathId) {
        return learningPaths.get(pathId);
    }

    public List<String> getRecommendedTopics(
        String sessionId,
        ContentType contentType,
        double attention,
        double cognitiveLoad
    ) {
        List<String> recommendations = new ArrayList<>();

        // Use attention and cognitive load to determine appropriate difficulty
        DifficultyLevel targetDifficulty;
        if (attention > 0.8 && cognitiveLoad < 0.3) {
            targetDifficulty = DifficultyLevel.ADVANCED;
        } else if (attention > 0.6 && cognitiveLoad < 0.6) {
            targetDifficulty = DifficultyLevel.INTERMEDIATE;
        } else {
            targetDifficulty = DifficultyLevel.BEGINNER;
        }

        // Find appropriate learning paths
        for (LearningPath path : learningPaths.values()) {
            if (
                path.getDifficulty().getLevel() <= targetDifficulty.getLevel()
            ) {
                recommendations.addAll(path.getTopics());
            }
        }

        return recommendations;
    }

    public void updateLearningProgress(
        String sessionId,
        String topic,
        double masteryScore,
        boolean success
    ) {
        analytics.updateMastery(topic, masteryScore);
        analytics.incrementAttempts(topic);

        CognitiveSession session = activeSessions.get(sessionId);
        if (session != null) {
            session.addData("last_topic", topic);
            session.addData("last_score", masteryScore);
            session.addData("last_success", success);
        }
    }

    public double assessTopicReadiness(String topic, String sessionId) {
        double currentMastery = analytics.getMastery(topic);
        int attempts = analytics.getAttempts(topic);

        // Find paths containing this topic
        double prerequisiteScore = 1.0;
        for (LearningPath path : learningPaths.values()) {
            if (path.getTopics().contains(topic)) {
                for (Map.Entry<String, Double> prereq : path
                    .getPrerequisites()
                    .entrySet()) {
                    double prereqMastery = analytics.getMastery(
                        prereq.getKey()
                    );
                    if (prereqMastery < prereq.getValue()) {
                        prerequisiteScore *= (prereqMastery /
                            prereq.getValue());
                    }
                }
            }
        }

        // Combine mastery, attempts, and prerequisites
        double attemptsBonus = Math.min(0.2, attempts * 0.05);
        return Math.min(
            1.0,
            (currentMastery + attemptsBonus) * prerequisiteScore
        );
    }

    public Map<String, Object> generateProgressReport(String sessionId) {
        Map<String, Object> report = new HashMap<>();
        CognitiveSession session = activeSessions.get(sessionId);

        if (session != null) {
            report.put("sessionId", sessionId);
            report.put(
                "duration",
                System.currentTimeMillis() - session.getStartTime()
            );
            report.put("sessionData", session.getSessionData());
        }

        report.put("topicMastery", analytics.getAllMastery());
        report.put("totalTopics", analytics.getAllMastery().size());
        report.put(
            "averageMastery",
            analytics
                .getAllMastery()
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0)
        );

        return report;
    }

    public LearningAnalytics getAnalytics() {
        return analytics;
    }

    public Collection<LearningPath> getAllLearningPaths() {
        return new ArrayList<>(learningPaths.values());
    }

    public Collection<CognitiveSession> getActiveSessions() {
        return new ArrayList<>(activeSessions.values());
    }

    /**
     * System analytics for education framework
     */
    public static class SystemAnalytics {

        private final double averageEngagement;
        private final int totalSessions;
        private final int activeSessions;
        private final Map<String, Double> topicMasteryAverages;
        private final double systemEfficiency;
        private final long timestamp;

        public SystemAnalytics(
            double averageEngagement,
            int totalSessions,
            int activeSessions,
            Map<String, Double> topicMasteryAverages,
            double systemEfficiency
        ) {
            this.averageEngagement = averageEngagement;
            this.totalSessions = totalSessions;
            this.activeSessions = activeSessions;
            this.topicMasteryAverages = new HashMap<>(topicMasteryAverages);
            this.systemEfficiency = systemEfficiency;
            this.timestamp = System.currentTimeMillis();
        }

        public double getAverageEngagement() {
            return averageEngagement;
        }

        public int getTotalSessions() {
            return totalSessions;
        }

        public int getActiveSessions() {
            return activeSessions;
        }

        public Map<String, Double> getTopicMasteryAverages() {
            return new HashMap<>(topicMasteryAverages);
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
                "SystemAnalytics{engagement=%.2f, sessions=%d, efficiency=%.2f}",
                averageEngagement,
                totalSessions,
                systemEfficiency
            );
        }
    }

    /**
     * Generate system-wide analytics
     */
    public SystemAnalytics getSystemAnalytics() {
        double avgEngagement = activeSessions.isEmpty() ? 0.0 : 0.75; // Simplified calculation
        Map<String, Double> masteryAverages = analytics.getAllMastery();
        double efficiency = masteryAverages.isEmpty()
            ? 0.0
            : masteryAverages
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new SystemAnalytics(
            avgEngagement,
            learningPaths.size(),
            activeSessions.size(),
            masteryAverages,
            efficiency
        );
    }
}
