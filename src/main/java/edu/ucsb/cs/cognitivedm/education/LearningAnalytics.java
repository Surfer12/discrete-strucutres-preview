package edu.ucsb.cs.cognitivedm.education;

import java.util.Map;
import java.util.HashMap;

/**
 * Analytics for learning activities and cognitive processes.
 */
public class LearningAnalytics {
    private final int totalUsers;
    private final int activeUsers;
    private final int totalSessions;
    private final Map<String, Double> subjectPerformance;
    private final Map<String, Integer> contentViews;
    private final double averageCognitiveLoad;
    private final double attentionMetric;
    private final long timestamp;

    /**
     * Creates learning analytics with all metrics.
     */
    public LearningAnalytics(
        int totalUsers,
        int activeUsers,
        int totalSessions,
        Map<String, Double> subjectPerformance,
        Map<String, Integer> contentViews,
        double averageCognitiveLoad,
        double attentionMetric
    ) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.totalSessions = totalSessions;
        this.subjectPerformance = new HashMap<>(subjectPerformance);
        this.contentViews = new HashMap<>(contentViews);
        this.averageCognitiveLoad = averageCognitiveLoad;
        this.attentionMetric = attentionMetric;
        this.timestamp = System.currentTimeMillis();
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public Map<String, Double> getSubjectPerformance() {
        return subjectPerformance;
    }

    public Map<String, Integer> getContentViews() {
        return contentViews;
    }

    public double getAverageCognitiveLoad() {
        return averageCognitiveLoad;
    }

    public double getAttentionMetric() {
        return attentionMetric;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "LearningAnalytics{" +
            "totalUsers=" + totalUsers +
            ", activeUsers=" + activeUsers +
            ", totalSessions=" + totalSessions +
            ", subjectPerformance=" + subjectPerformance +
            ", contentViews=" + contentViews +
            ", averageCognitiveLoad=" + averageCognitiveLoad +
            ", attentionMetric=" + attentionMetric +
            ", timestamp=" + timestamp +
            '}';
    }
} 