package com.discretelogic.model;

import java.util.List;

/**
 * Represents a logical gate in a digital circuit.
 * This class provides functionality for evaluating different types of logic gates.
 */
public class Gate {
    /**
     * Represents the type of logic gate.
     */
    public enum GateType {
        AND, OR, NOT, NAND, NOR, XOR, XNOR, BUFFER
    }

    private GateType type;
    private final String symbol;

    /**
     * Creates a new Gate with the specified type.
     * 
     * @param type The type of the gate (AND, OR, NOT, etc.)
     */
    public Gate(GateType type) {
        this.type = type;
        this.symbol = assignSymbol(type);
    }

    /**
     * Evaluates the gate with the given inputs.
     * 
     * @param inputs List of boolean inputs to the gate
     * @return The boolean result of applying the gate's logic
     */
    public boolean evaluate(List<Boolean> inputs) {
        if (inputs == null || inputs.isEmpty()) {
            throw new IllegalArgumentException("Gate inputs cannot be null or empty");
        }

        // Handle special case for NOT gate which takes only one input
        if (type == GateType.NOT || type == GateType.BUFFER) {
            if (inputs.size() < 1) {
                throw new IllegalArgumentException(type.name() + " gate requires at least one input");
            }
            
            boolean input = inputs.get(0);
            return type == GateType.NOT ? !input : input;
        }

        // For all other gates, require at least two inputs
        if (inputs.size() < 2) {
            throw new IllegalArgumentException(type.name() + " gate requires at least two inputs");
        }

        boolean result = false;
        
        switch (type) {
            case AND:
                result = true;
                for (boolean input : inputs) {
                    result &= input;
                }
                break;
                
            case OR:
                result = false;
                for (boolean input : inputs) {
                    result |= input;
                }
                break;
                
            case NAND:
                result = true;
                for (boolean input : inputs) {
                    result &= input;
                }
                result = !result;
                break;
                
            case NOR:
                result = false;
                for (boolean input : inputs) {
                    result |= input;
                }
                result = !result;
                break;
                
            case XOR:
                result = false;
                for (boolean input : inputs) {
                    result ^= input;
                }
                break;
                
            case XNOR:
                result = false;
                for (boolean input : inputs) {
                    result ^= input;
                }
                result = !result;
                break;
                
            default:
                throw new UnsupportedOperationException("Unsupported gate type: " + type);
        }
        
        return result;
    }

    /**
     * Gets the type of this gate.
     * 
     * @return The gate type
     */
    public GateType getType() {
        return type;
    }

    /**
     * Gets the symbol used to represent this gate.
     * 
     * @return The gate symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Assigns a symbol to the gate based on its type.
     * 
     * @param type The gate type
     * @return The corresponding symbol
     */
    private String assignSymbol(GateType type) {
        switch (type) {
            case AND: return "∧";
            case OR: return "∨";
            case NOT: return "¬";
            case NAND: return "⊼";
            case NOR: return "⊽";
            case XOR: return "⊕";
            case XNOR: return "⊙";
            case BUFFER: return "→";
            default: return "?";
        }
    }

    @Override
    public String toString() {
        return type.name() + " Gate";
    }
} 