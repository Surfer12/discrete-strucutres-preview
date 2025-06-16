package com.discretelogic.tutorial;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides tutorials on various discrete mathematics topics.
 */
public class Tutorial {
    
    private static final Map<String, String> tutorials = new HashMap<>();
    
    static {
        initializeTutorials();
    }
    
    private static void initializeTutorials() {
        // Set Theory
        tutorials.put("sets", 
            "SET THEORY TUTORIAL\n\n" +
            "Sets are collections of distinct objects. Key operations include:\n" +
            "- Union (A ∪ B): Elements in either A or B\n" +
            "- Intersection (A ∩ B): Elements in both A and B\n" +
            "- Difference (A - B): Elements in A but not in B\n" +
            "- Complement (A'): Elements not in A\n\n" +
            "Example: If A = {1,2,3} and B = {2,3,4}, then:\n" +
            "A ∪ B = {1,2,3,4}\n" +
            "A ∩ B = {2,3}\n" +
            "A - B = {1}\n"
        );
        
        // Boolean Algebra
        tutorials.put("boolean", 
            "BOOLEAN ALGEBRA TUTORIAL\n\n" +
            "Boolean algebra deals with operations on logical values. Key operations:\n" +
            "- AND (∧): True only when both inputs are true\n" +
            "- OR (∨): True when at least one input is true\n" +
            "- NOT (¬): Inverts the value\n\n" +
            "Key laws:\n" +
            "- Commutative: A ∧ B = B ∧ A, A ∨ B = B ∨ A\n" +
            "- Associative: (A ∧ B) ∧ C = A ∧ (B ∧ C)\n" +
            "- Distributive: A ∧ (B ∨ C) = (A ∧ B) ∨ (A ∧ C)\n" +
            "- DeMorgan's: ¬(A ∧ B) = ¬A ∨ ¬B, ¬(A ∨ B) = ¬A ∧ ¬B\n"
        );
        
        // Logic Gates
        tutorials.put("gates", 
            "LOGIC GATES TUTORIAL\n\n" +
            "Logic gates implement boolean functions in hardware:\n" +
            "- AND gate: Output is 1 only when all inputs are 1\n" +
            "- OR gate: Output is 1 when at least one input is 1\n" +
            "- NOT gate: Inverts the input\n" +
            "- XOR gate: Output is 1 when inputs are different\n" +
            "- NAND gate: Negated AND, universal gate\n" +
            "- NOR gate: Negated OR, universal gate\n\n" +
            "Gates can be combined to create complex digital circuits.\n"
        );
        
        // Number Systems
        tutorials.put("numbers", 
            "NUMBER SYSTEMS TUTORIAL\n\n" +
            "Different bases for representing numbers:\n" +
            "- Binary (base 2): Uses digits 0-1\n" +
            "- Octal (base 8): Uses digits 0-7\n" +
            "- Decimal (base 10): Uses digits 0-9\n" +
            "- Hexadecimal (base 16): Uses digits 0-9 and A-F\n\n" +
            "Conversion examples:\n" +
            "- Decimal 42 to binary: 101010\n" +
            "- Binary 1101 to decimal: 13\n" +
            "- Decimal 15 to hexadecimal: F\n"
        );
    }
    
    /**
     * Displays a tutorial on the specified topic.
     *
     * @param topic the tutorial topic
     * @return the tutorial content
     */
    public static String getTutorial(String topic) {
        String tutorial = tutorials.get(topic.toLowerCase());
        if (tutorial == null) {
            return "Tutorial not found. Available topics: " + String.join(", ", tutorials.keySet());
        }
        return tutorial;
    }
    
    /**
     * Lists all available tutorial topics.
     *
     * @return a string listing all tutorial topics
     */
    public static String listTopics() {
        return "Available tutorials: " + String.join(", ", tutorials.keySet());
    }
} 