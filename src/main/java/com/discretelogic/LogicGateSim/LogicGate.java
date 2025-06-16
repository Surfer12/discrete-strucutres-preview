package com.discretelogic.logicgatesim;

import com.discretelogic.model.Gate;
import com.discretelogic.model.GateType;
import java.util.*;

/**
 * Digital logic circuit simulator with support for various logic gates.
 */
public class LogicGate {

    /**
     * Simulates a circuit with multiple gates and returns the final output.
     */
    public static boolean simulateCircuit(List<Gate> gates, Map<String, Boolean> inputs) {
        // This is a simplified circuit simulator
        // In a real implementation, you'd have a proper circuit graph
        boolean result = true;

        for (Gate gate : gates) {
            List<Boolean> gateInputs = new ArrayList<>();
            // For simplicity, we'll use the first available inputs
            gateInputs.addAll(inputs.values());

            if (gateInputs.size() > gate.getType().name().equals("NOT") ? 1 : 2) {
                gateInputs = gateInputs.subList(0, gate.getType().name().equals("NOT") ? 1 : 2);
            }

            result = gate.evaluate(gateInputs);
        }

        return result;
    }

    /**
     * Generates a truth table for a single gate.
     */
    public static String generateGateTruthTable(GateType gateType) {
        StringBuilder sb = new StringBuilder();

        switch (gateType) {
            case NOT:
                sb.append("NOT Gate Truth Table:\n");
                sb.append("A | Â¬A\n");
                sb.append("--|---\n");
                sb.append("0 |  1\n");
                sb.append("1 |  0\n");
                break;

            case AND:
                sb.append("AND Gate Truth Table:\n");
                sb.append("A B | Aâˆ§B\n");
                sb.append("----|----\n");
                sb.append("0 0 |  0\n");
                sb.append("0 1 |  0\n");
                sb.append("1 0 |  0\n");
                sb.append("1 1 |  1\n");
                break;

            case OR:
                sb.append("OR Gate Truth Table:\n");
                sb.append("A B | Aâˆ¨B\n");
                sb.append("----|----\n");
                sb.append("0 0 |  0\n");
                sb.append("0 1 |  1\n");
                sb.append("1 0 |  1\n");
                sb.append("1 1 |  1\n");
                break;

            case NAND:
                sb.append("NAND Gate Truth Table:\n");
                sb.append("A B | AâŠ¼B\n");
                sb.append("----|----\n");
                sb.append("0 0 |  1\n");
                sb.append("0 1 |  1\n");
                sb.append("1 0 |  1\n");
                sb.append("1 1 |  0\n");
                break;

            case NOR:
                sb.append("NOR Gate Truth Table:\n");
                sb.append("A B | AâŠ½B\n");
                sb.append("----|----\n");
                sb.append("0 0 |  1\n");
                sb.append("0 1 |  0\n");
                sb.append("1 0 |  0\n");
                sb.append("1 1 |  0\n");
                break;

            case XOR:
                sb.append("XOR Gate Truth Table:\n");
                sb.append("A B | AâŠ•B\n");
                sb.append("----|----\n");
                sb.append("0 0 |  0\n");
                sb.append("0 1 |  1\n");
                sb.append("1 0 |  1\n");
                sb.append("1 1 |  0\n");
                break;

            case XNOR:
                sb.append("XNOR Gate Truth Table:\n");
                sb.append("A B | AâŠ™B\n");
                sb.append("----|----\n");
                sb.append("0 0 |  1\n");
                sb.append("0 1 |  0\n");
                sb.append("1 0 |  0\n");
                sb.append("1 1 |  1\n");
                break;
        }

        return sb.toString();
    }

    /**
     * Demonstrates how gates can be combined to create more complex circuits.
     */
    public static String demonstrateHalfAdder() {
        StringBuilder sb = new StringBuilder();
        sb.append("Half Adder Circuit:\n");
        sb.append("==================\n");
        sb.append("Sum = A âŠ• B (XOR gate)\n");
        sb.append("Carry = A âˆ§ B (AND gate)\n\n");

        sb.append("A B | Sum Carry\n");
        sb.append("----|----------\n");

        Gate xorGate = new Gate(GateType.XOR);
        Gate andGate = new Gate(GateType.AND);

        boolean[][] inputs = {{false, false}, {false, true}, {true, false}, {true, true}};

        for (boolean[] input : inputs) {
            List<Boolean> gateInputs = Arrays.asList(input[0], input[1]);
            boolean sum = xorGate.evaluate(gateInputs);
            boolean carry = andGate.evaluate(gateInputs);

            sb.append(input[0] ? "1" : "0").append(" ");
            sb.append(input[1] ? "1" : "0").append(" |  ");
            sb.append(sum ? "1" : "0").append("    ");
            sb.append(carry ? "1" : "0").append("\n");
        }

        return sb.toString();
    }

    /**
     * Demonstrates a full adder circuit.
     */
    public static String demonstrateFullAdder() {
        StringBuilder sb = new StringBuilder();
        sb.append("Full Adder Circuit:\n");
        sb.append("==================\n");
        sb.append("Sum = A âŠ• B âŠ• Cin\n");
        sb.append("Cout = (A âˆ§ B) âˆ¨ (Cin âˆ§ (A âŠ• B))\n\n");

        sb.append("A B Cin | Sum Cout\n");
        sb.append("--------|----------\n");

        boolean[][][] inputs = {
            {{false, false, false}}, {{false, false, true}},
            {{false, true, false}}, {{false, true, true}},
            {{true, false, false}}, {{true, false, true}},
            {{true, true, false}}, {{true, true, true}}
        };

        for (boolean[][] inputSet : inputs) {
            boolean a = inputSet[0][0];
            boolean b = inputSet[0][1];
            boolean cin = inputSet[0][2];

            // Calculate sum: A âŠ• B âŠ• Cin
            boolean sum = a ^ b ^ cin;

            // Calculate carry out: (A âˆ§ B) âˆ¨ (Cin âˆ§ (A âŠ• B))
            boolean cout = (a && b) || (cin && (a ^ b));

            sb.append(a ? "1" : "0").append(" ");
            sb.append(b ? "1" : "0").append("  ");
            sb.append(cin ? "1" : "0").append("  |  ");
            sb.append(sum ? "1" : "0").append("    ");
            sb.append(cout ? "1" : "0").append("\n");
        }

        return sb.toString();
    }

    /**
     * Lists all available gate types with their descriptions.
     */
    public static String listGateTypes() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available Logic Gates:\n");
        sb.append("====================\n");

        for (GateType type : GateType.values()) {
            Gate gate = new Gate(type);
            sb.append(String.format("%-8s %s - %s\n", 
                type.name(), 
                gate.getSymbol(), 
                getGateDescription(type)));
        }

        return sb.toString();
    }

    private static String getGateDescription(GateType type) {
        switch (type) {
            case AND: return "Output is true only when all inputs are true";
            case OR: return "Output is true when at least one input is true";
            case NOT: return "Output is the opposite of the input";
            case NAND: return "Output is false only when all inputs are true";
            case NOR: return "Output is true only when all inputs are false";
            case XOR: return "Output is true when inputs are different";
            case XNOR: return "Output is true when inputs are the same";
            case BUFFER: return "Output is the same as the input";
            default: return "Unknown gate type";
        }
    }
}