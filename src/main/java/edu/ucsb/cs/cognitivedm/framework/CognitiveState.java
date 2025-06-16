package edu.ucsb.cs.cognitivedm.framework;

import java.util.HashMap;
import java.util.Map;

public class CognitiveState {
    private final Map<String, Double> metrics;
    private double attentionLevel;
    private double workingMemoryLoad;
    private double cognitiveLoad;

    public CognitiveState() {
        this.metrics = new HashMap<>();
        this.attentionLevel = 1.0;
        this.workingMemoryLoad = 0.0;
        this.cognitiveLoad = 0.0;
    }

    public void updateMetric(String key, double value) {
        metrics.put(key, value);
    }

    public double getMetric(String key) {
        return metrics.getOrDefault(key, 0.0);
    }

    public void setAttentionLevel(double level) {
        this.attentionLevel = Math.max(0.0, Math.min(1.0, level));
    }

    public double getAttentionLevel() {
        return attentionLevel;
    }

    public void setWorkingMemoryLoad(double load) {
        this.workingMemoryLoad = Math.max(0.0, Math.min(1.0, load));
    }

    public double getWorkingMemoryLoad() {
        return workingMemoryLoad;
    }

    public void setCognitiveLoad(double load) {
        this.cognitiveLoad = Math.max(0.0, Math.min(1.0, load));
    }

    public double getCognitiveLoad() {
        return cognitiveLoad;
    }

    public Map<String, Double> getAllMetrics() {
        return new HashMap<>(metrics);
    }
} 