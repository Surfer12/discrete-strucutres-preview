package edu.ucsb.cs.cognitivedm.education;

import java.util.HashMap;
import java.util.Map;

public class LearningContent {
    private final String id;
    private final String title;
    private final String subject;
    private final ContentType type;
    private final DifficultyLevel difficulty;
    private final Map<String, String> content;
    private final Map<LearningStyle, Double> styleAlignments;

    public LearningContent(
        String id,
        String title,
        String subject,
        ContentType type,
        DifficultyLevel difficulty
    ) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.type = type;
        this.difficulty = difficulty;
        this.content = new HashMap<>();
        this.styleAlignments = new HashMap<>();
    }

    public void setContent(String key, String value) {
        content.put(key, value);
    }

    public String getContent(String key) {
        return content.get(key);
    }

    public void setStyleAlignment(LearningStyle style, double alignment) {
        styleAlignments.put(style, alignment);
    }

    public double getStyleAlignment(LearningStyle style) {
        return styleAlignments.getOrDefault(style, 0.0);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public ContentType getType() { return type; }
    public DifficultyLevel getDifficulty() { return difficulty; }
    public Map<String, String> getAllContent() { return new HashMap<>(content); }
    public Map<LearningStyle, Double> getStyleAlignments() { return new HashMap<>(styleAlignments); }
} 