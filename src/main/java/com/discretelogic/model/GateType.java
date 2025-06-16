package com.discretelogic.model;

/**
 * Enum representing the different types of logic gates.
 * Each gate type corresponds to a specific boolean operation.
 */
public enum GateType {
    /**
     * AND gate - output is true only when all inputs are true
     */
    AND,
    
    /**
     * OR gate - output is true when at least one input is true
     */
    OR,
    
    /**
     * NOT gate - output is the opposite of the input
     */
    NOT,
    
    /**
     * NAND gate - output is false only when all inputs are true
     */
    NAND,
    
    /**
     * NOR gate - output is true only when all inputs are false
     */
    NOR,
    
    /**
     * XOR gate - output is true when inputs are different
     */
    XOR,
    
    /**
     * XNOR gate - output is true when inputs are the same
     */
    XNOR,
    
    /**
     * BUFFER gate - output is the same as the input
     */
    BUFFER
} 