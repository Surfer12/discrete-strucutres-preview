package com.discretelogic.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Provides quiz functionality for testing knowledge on discrete mathematics topics.
 */
public class Quiz {
    
    private final String topic;
    private final List<Question> questions;
    private int score;
    private static final Map<String, List<Question>> questionBank = new HashMap<>();
    
    static {
        initializeQuestionBank();
    }
    
    private static void initializeQuestionBank() {
        // Set Theory Questions
        List<Question> setQuestions = new ArrayList<>();
        setQuestions.add(new Question(
            "What is the union of sets A = {1, 2, 3} and B = {3, 4, 5}?",
            "{1, 2, 3, 4, 5}",
            new String[]{"{1, 2, 3, 4, 5}", "{1, 2, 4, 5}", "{3}", "{1, 2, 3, 3, 4, 5}"}
        ));
        setQuestions.add(new Question(
            "What is the intersection of sets A = {1, 2, 3, 4} and B = {3, 4, 5, 6}?",
            "{3, 4}",
            new String[]{"{3, 4}", "{1, 2, 5, 6}", "{}", "{1, 2, 3, 4, 5, 6}"}
        ));
        questionBank.put("sets", setQuestions);
        
        // Boolean Algebra Questions
        List<Question> booleanQuestions = new ArrayList<>();
        booleanQuestions.add(new Question(
            "What is the result of (A AND B) OR (NOT A)?",
            "B OR (NOT A)",
            new String[]{"B OR (NOT A)", "A", "B", "NOT B"}
        ));
        booleanQuestions.add(new Question(
            "According to De Morgan's law, what is the equivalent of NOT(A OR B)?",
            "(NOT A) AND (NOT B)",
            new String[]{"(NOT A) AND (NOT B)", "(NOT A) OR (NOT B)", "A AND B", "NOT(A AND B)"}
        ));
        questionBank.put("boolean", booleanQuestions);
        
        // Logic Gates Questions
        List<Question> gateQuestions = new ArrayList<>();
        gateQuestions.add(new Question(
            "Which gate outputs 1 only when exactly one of its inputs is 1?",
            "XOR",
            new String[]{"XOR", "AND", "OR", "NAND"}
        ));
        gateQuestions.add(new Question(
            "Which of these gates is universal (can be used to build any other logic function)?",
            "NAND",
            new String[]{"NAND", "XOR", "AND", "Buffer"}
        ));
        questionBank.put("gates", gateQuestions);
        
        // Number System Questions
        List<Question> numberQuestions = new ArrayList<>();
        numberQuestions.add(new Question(
            "What is the binary representation of decimal 25?",
            "11001",
            new String[]{"11001", "10101", "10011", "11010"}
        ));
        numberQuestions.add(new Question(
            "What is the hexadecimal representation of binary 1010 1111?",
            "AF",
            new String[]{"AF", "A7", "BF", "9F"}
        ));
        questionBank.put("numbers", numberQuestions);
    }
    
    /**
     * Creates a new quiz for the specified topic.
     *
     * @param topic the quiz topic
     */
    public Quiz(String topic) {
        this.topic = topic.toLowerCase();
        this.questions = questionBank.getOrDefault(this.topic, new ArrayList<>());
        Collections.shuffle(this.questions);
        this.score = 0;
    }
    
    /**
     * Starts the quiz.
     */
    public void start() {
        if (questions.isEmpty()) {
            System.out.println("No questions available for topic: " + topic);
            System.out.println("Available topics: " + String.join(", ", questionBank.keySet()));
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Starting quiz on " + topic + " (" + questions.size() + " questions)");
        System.out.println("----------------------------------------");
        
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println("Question " + (i + 1) + ": " + q.getQuestion());
            
            String[] options = q.getOptions();
            for (int j = 0; j < options.length; j++) {
                System.out.println((j + 1) + ". " + options[j]);
            }
            
            System.out.print("Your answer (1-" + options.length + "): ");
            try {
                int answer = Integer.parseInt(scanner.nextLine().trim());
                if (answer >= 1 && answer <= options.length) {
                    if (options[answer - 1].equals(q.getCorrectAnswer())) {
                        System.out.println("Correct!");
                        score++;
                    } else {
                        System.out.println("Incorrect. The correct answer is: " + q.getCorrectAnswer());
                    }
                } else {
                    System.out.println("Invalid option. The correct answer is: " + q.getCorrectAnswer());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. The correct answer is: " + q.getCorrectAnswer());
            }
            System.out.println("----------------------------------------");
        }
        
        System.out.println("Quiz completed!");
        System.out.println("Your score: " + score + " out of " + questions.size());
        double percentage = (double) score / questions.size() * 100;
        System.out.println("Percentage: " + String.format("%.1f", percentage) + "%");
    }
    
    /**
     * Returns the list of available quiz topics.
     *
     * @return available quiz topics
     */
    public static List<String> getAvailableTopics() {
        return new ArrayList<>(questionBank.keySet());
    }
    
    /**
     * Inner class representing a quiz question.
     */
    private static class Question {
        private final String question;
        private final String correctAnswer;
        private final String[] options;
        
        public Question(String question, String correctAnswer, String[] options) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.options = options;
        }
        
        public String getQuestion() {
            return question;
        }
        
        public String getCorrectAnswer() {
            return correctAnswer;
        }
        
        public String[] getOptions() {
            return options;
        }
    }
} 