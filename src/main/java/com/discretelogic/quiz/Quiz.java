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
    private final List<QuizQuestion> questions;
    private int score;
    private static final Map<String, List<QuizQuestion>> questionBank = new HashMap<>();
    
    static {
        initializeQuestionBank();
    }
    
    private static void initializeQuestionBank() {
        // Set Theory Questions
        List<QuizQuestion> setQuestions = new ArrayList<>();
        setQuestions.add(new QuizQuestion(
            "What is the union of sets A = {1, 2, 3} and B = {3, 4, 5}?",
            "{1, 2, 3, 4, 5}",
            new String[]{"{1, 2, 3, 4, 5}", "{1, 2, 4, 5}", "{3}", "{1, 2, 3, 3, 4, 5}"}
        ));
        setQuestions.add(new QuizQuestion(
            "What is the intersection of sets A = {1, 2, 3, 4} and B = {3, 4, 5, 6}?",
            "{3, 4}",
            new String[]{"{3, 4}", "{1, 2, 5, 6}", "{}", "{1, 2, 3, 4, 5, 6}"}
        ));
        questionBank.put("sets", setQuestions);
        
        // Boolean Algebra Questions
        List<QuizQuestion> booleanQuestions = new ArrayList<>();
        booleanQuestions.add(new QuizQuestion(
            "What is the result of (A AND B) OR (NOT A)?",
            "B OR (NOT A)",
            new String[]{"B OR (NOT A)", "A", "B", "NOT B"}
        ));
        booleanQuestions.add(new QuizQuestion(
            "According to De Morgan's law, what is the equivalent of NOT(A OR B)?",
            "(NOT A) AND (NOT B)",
            new String[]{"(NOT A) AND (NOT B)", "(NOT A) OR (NOT B)", "A AND B", "NOT(A AND B)"}
        ));
        questionBank.put("boolean", booleanQuestions);
        
        // Logic Gates Questions
        List<QuizQuestion> gateQuestions = new ArrayList<>();
        gateQuestions.add(new QuizQuestion(
            "Which gate outputs 1 only when all inputs are 1?",
            "AND",
            new String[]{"AND", "OR", "NOT", "XOR"}
        ));
        gateQuestions.add(new QuizQuestion(
            "What is the output of a NAND gate when both inputs are 1?",
            "0",
            new String[]{"0", "1", "Undefined", "Both"}
        ));
        questionBank.put("gates", gateQuestions);
    }
    
    /**
     * Creates a new quiz for the specified topic.
     *
     * @param topic the quiz topic
     */
    public Quiz(String topic) {
        this.topic = topic.toLowerCase();
        this.questions = new ArrayList<>(questionBank.getOrDefault(this.topic, new ArrayList<>()));
        Collections.shuffle(this.questions);
        this.score = 0;
    }
    
    /**
     * Starts the quiz and handles user interaction.
     */
    public void start() {
        if (questions.isEmpty()) {
            System.out.println("No questions available for topic: " + topic);
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nQUIZ: " + topic.toUpperCase() + "\n");
        
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion question = questions.get(i);
            System.out.println("Question " + (i + 1) + ":");
            System.out.println(question.getQuestion());
            System.out.println();
            
            String[] choices = question.getChoices();
            for (int j = 0; j < choices.length; j++) {
                System.out.println((j + 1) + ") " + choices[j]);
            }
            
            System.out.print("\nYour answer (1-" + choices.length + "): ");
            try {
                int answer = Integer.parseInt(scanner.nextLine().trim());
                if (answer >= 1 && answer <= choices.length) {
                    if (choices[answer - 1].equals(question.getCorrectAnswer())) {
                        System.out.println("Correct!\n");
                        score++;
                    } else {
                        System.out.println("Incorrect. The correct answer is: " + question.getCorrectAnswer() + "\n");
                    }
                } else {
                    System.out.println("Invalid choice. The correct answer is: " + question.getCorrectAnswer() + "\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. The correct answer is: " + question.getCorrectAnswer() + "\n");
            }
        }
        
        System.out.println("Quiz complete!");
        System.out.println("Your score: " + score + "/" + questions.size());
        double percentage = (double) score / questions.size() * 100;
        System.out.printf("Percentage: %.1f%%\n", percentage);
    }
}
