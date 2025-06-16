package com.discretelogic.quiz;

/**
 * Represents a quiz question with multiple choice answers.
 */
public class QuizQuestion {
    private final String question;
    private final String correctAnswer;
    private final String[] choices;
    
    /**
     * Creates a new quiz question.
     *
     * @param question the question text
     * @param correctAnswer the correct answer
     * @param choices array of possible answers including the correct one
     */
    public QuizQuestion(String question, String correctAnswer, String[] choices) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.choices = choices;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    public String[] getChoices() {
        return choices;
    }
}
