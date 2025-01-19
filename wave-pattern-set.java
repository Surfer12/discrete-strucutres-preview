package fluid.sets;

import java.util.*;
import java.util.function.Predicate;

/**
 * WavePatternSet implements a discrete mathematical model for analyzing
 * wave patterns using set theory and relations.
 */
public class WavePatternSet {
    private final Set<WaveState> waveStates;
    private final Map<WaveState, Set<WaveTransition>> transitions;

    public WavePatternSet() {
        this.waveStates = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    /**
     * Represents a discrete wave state with associated properties
     */
    public static class WaveState {
        private final double amplitude;
        private final double frequency;
        private final double energy;
        private final Vector2D direction;

        public WaveState(double amplitude, double frequency, double energy, Vector2D direction) {
            this.amplitude = amplitude;
            this.frequency = frequency;
            this.energy = energy;
            this.direction = direction;
        }

        public boolean isSurfable() {
            // Define conditions for a surfable wave
            return amplitude >= 1.0 && 
                   frequency >= 0.08 && 
                   frequency <= 0.2 && 
                   energy >= 1000;
        }
    }

    /**
     * Represents a transition between wave states
     */
    public static class WaveTransition {
        private final WaveState from;
        private final WaveState to;
        private final double probability;
        private final double energyTransfer;

        public WaveTransition(WaveState from, WaveState to, double probability, double energyTransfer) {
            this.from = from;
            this.to = to;
            this.probability = probability;
            this.energyTransfer = energyTransfer;
        }
    }

    /**
     * 2D Vector representation for wave direction
     */
    public static class Vector2D {
        public final double x;
        public final double y;

        public Vector2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double magnitude() {
            return Math.sqrt(x * x + y * y);
        }
    }

    /**
     * Adds a wave state to the set
     */
    public void addWaveState(WaveState state) {
        waveStates.add(state);
        transitions.putIfAbsent(state, new HashSet<>());
    }

    /**
     * Adds a transition between wave states
     */
    public void addTransition(WaveTransition transition) {
        transitions.get(transition.from).add(transition);
    }

    /**
     * Finds all surfable wave states
     */
    public Set<WaveState> findSurfableWaves() {
        return waveStates.stream()
                        .filter(WaveState::isSurfable)
                        .collect(Collectors.toSet());
    }

    /**
     * Verifies conservation of energy in wave transitions
     */
    public boolean verifyEnergyConservation() {
        return transitions.values().stream()
                        .flatMap(Set::stream)
                        .allMatch(t -> Math.abs(t.energyTransfer) <= t.from.energy);
    }

    /**
     * Calculates the intersection of two wave pattern sets
     */
    public static Set<WaveState> intersection(WavePatternSet set1, WavePatternSet set2) {
        Set<WaveState> result = new HashSet<>(set1.waveStates);
        result.retainAll(set2.waveStates);
        return result;
    }

    /**
     * Predicts wave pattern evolution using discrete time steps
     */
    public List<WaveState> predictWaveEvolution(WaveState initial, int steps) {
        List<WaveState> evolution = new ArrayList<>();
        WaveState current = initial;
        evolution.add(current);

        for (int i = 0; i < steps && transitions.containsKey(current); i++) {
            Optional<WaveTransition> nextTransition = transitions.get(current)
                .stream()
                .max(Comparator.comparingDouble(t -> t.probability));

            if (nextTransition.isPresent()) {
                current = nextTransition.get().to;
                evolution.add(current);
            } else {
                break;
            }
        }

        return evolution;
    }
}
