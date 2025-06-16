package com.discretelogic.LogicGateSim;

import java.util.ArrayList;
import java.util.List;
import com.discretelogic.model.Gate;
import com.discretelogic.model.Gate.GateType;

public class LogicGate {

    public boolean simulateCircuit(List<Gate> gates, List<Boolean> inputs) {
        boolean result = false;

        for (Gate gate : gates) {
            List<Boolean> gateInputs = new ArrayList<>();
            // For simplicity, we'll use the first available inputs
            gateInputs.addAll(inputs);

            int requiredInputs = gate.getType() == GateType.NOT ? 1 : 2;
            if (gateInputs.size() > requiredInputs) {
                gateInputs = gateInputs.subList(0, requiredInputs);
            }

            result = gate.evaluate(gateInputs);
        }

        return result;
    }

    /**
     * Lists available gate types.
     */
    public static String listGateTypes() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available Logic Gate Types:\n");
        sb.append("=========================\n");
        
        for (GateType type : GateType.values()) {
            sb.append("- ").append(type.name()).append("\n");
        }
        
        return sb.toString();
    }

    /**
     * Demonstrates a half adder circuit.
     */
    public static String demonstrateHalfAdder() {
        StringBuilder sb = new StringBuilder();
        sb.append("Half Adder Demonstration\n");
        sb.append("=======================\n\n");
        sb.append("A half adder adds two bits and produces a sum and carry bit.\n\n");
        
        sb.append("Truth Table:\n");
        sb.append("A | B | Sum | Carry\n");
        sb.append("-----------------\n");
        sb.append("0 | 0 |  0  |   0  \n");
        sb.append("0 | 1 |  1  |   0  \n");
        sb.append("1 | 0 |  1  |   0  \n");
        sb.append("1 | 1 |  0  |   1  \n\n");
        
        sb.append("Implementation:\n");
        sb.append("Sum = A XOR B\n");
        sb.append("Carry = A AND B\n");
        
        return sb.toString();
    }
    
    /**
     * Demonstrates a full adder circuit.
     */
    public static String demonstrateFullAdder() {
        StringBuilder sb = new StringBuilder();
        sb.append("Full Adder Demonstration\n");
        sb.append("=======================\n\n");
        sb.append("A full adder adds three bits (including a carry-in) and produces a sum and carry-out bit.\n\n");
        
        sb.append("Truth Table:\n");
        sb.append("A | B | Cin | Sum | Cout\n");
        sb.append("----------------------\n");
        sb.append("0 | 0 |  0  |  0  |  0  \n");
        sb.append("0 | 0 |  1  |  1  |  0  \n");
        sb.append("0 | 1 |  0  |  1  |  0  \n");
        sb.append("0 | 1 |  1  |  0  |  1  \n");
        sb.append("1 | 0 |  0  |  1  |  0  \n");
        sb.append("1 | 0 |  1  |  0  |  1  \n");
        sb.append("1 | 1 |  0  |  0  |  1  \n");
        sb.append("1 | 1 |  1  |  1  |  1  \n\n");
        
        sb.append("Implementation:\n");
        sb.append("Sum = A XOR B XOR Cin\n");
        sb.append("Cout = (A AND B) OR (Cin AND (A XOR B))\n");
        
        return sb.toString();
    }
    
    /**
     * Generates a truth table for a specific gate type.
     */
    public static String generateGateTruthTable(GateType type) {
        StringBuilder sb = new StringBuilder();
        sb.append(type.name()).append(" Gate Truth Table\n");
        sb.append("=".repeat(type.name().length() + 16)).append("\n\n");
        
        if (type == GateType.NOT) {
            sb.append("Input | Output\n");
            sb.append("-------------\n");
            sb.append("  0   |   ").append(!false).append("  \n");
            sb.append("  1   |   ").append(!true).append("  \n");
        } else {
            sb.append("A | B | Output\n");
            sb.append("-------------\n");
            
            List<Boolean> inputs = new ArrayList<>();
            
            // Truth table for various gates with two inputs
            for (int i = 0; i < 4; i++) {
                boolean a = (i & 2) != 0;
                boolean b = (i & 1) != 0;
                inputs.clear();
                inputs.add(a);
                inputs.add(b);
                
                Gate gate = new Gate(type);
                boolean result = gate.evaluate(inputs);
                
                sb.append(a ? "1" : "0").append(" | ")
                  .append(b ? "1" : "0").append(" | ")
                  .append("  ").append(result ? "1" : "0").append("  \n");
            }
        }
        
        return sb.toString();
    }
}