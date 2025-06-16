package com.discretelogic.model;

/**
 * Enumeration of different types of logic gates.
 */
public enum GateType {
    /**
     * AND gate: Output is true only when all inputs are true.
     */
    AND("AND", "∧"),
    
    /**
     * OR gate: Output is true when at least one input is true.
     */
    OR("OR", "∨"),
    
    /**
     * NOT gate: Inverts the input (true to false, false to true).
     */
    NOT("NOT", "¬"),
    
    /**
     * NAND gate: Negated AND. Output is false only when all inputs are true.
     */
    NAND("NAND", "⊼"),
    
    /**
     * NOR gate: Negated OR. Output is true only when all inputs are false.
     */
    NOR("NOR", "⊽"),
    
    /**
     * XOR gate: Output is true when odd number of inputs are true.
     */
    XOR("XOR", "⊕"),
    
    /**
     * XNOR gate: Output is true when even number of inputs are true.
     */
    XNOR("XNOR", "≡"),
    
    /**
     * BUFFER gate: Output is the same as input. Often used for signal amplification.
     */
    BUFFER("BUFFER", "→");
    
    private final String name;
    private final String symbol;
    
    GateType(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    
    /**
     * Gets the name of the gate type.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the symbol representation of the gate type.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }
    
    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }
} 