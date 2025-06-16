import java.util.ArrayList;

import java.util.List;

public class LogicGate {

    public boolean simulateCircuit(List<Gate> gates, List<Boolean> inputs) {
        boolean result = false;

        for (Gate gate : gates) {
            List<Boolean> gateInputs = new ArrayList<>();
            // For simplicity, we'll use the first available inputs
            gateInputs.addAll(inputs.values());

            int requiredInputs = gate.getType() == GateType.NOT ? 1 : 2;
            if (gateInputs.size() > requiredInputs) {
                gateInputs = gateInputs.subList(0, requiredInputs);
            }

            result = gate.evaluate(gateInputs);
        }

        return result;
    }
}