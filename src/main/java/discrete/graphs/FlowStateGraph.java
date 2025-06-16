package cognitive.graphs;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * FlowStateGraph represents a mathematical model of cognitive flow states
 * using discrete graph structures. It implements concurrent access patterns
 * for real-time flow state analysis.
 */
public class FlowStateGraph<T> {
    private final ConcurrentHashMap<T, Set<FlowTransition<T>>> adjacencyMap;
    private final Set<FlowState<T>> states;

    public FlowStateGraph() {
        this.adjacencyMap = new ConcurrentHashMap<>();
        this.states = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    /**
     * Represents a cognitive state with associated metrics
     */
    public static class FlowState<T> {
        private final T id;
        private final double arousalLevel;      // Physiological arousal
        private final double skillLevel;        // Current skill level
        private final double challengeLevel;    // Current challenge level

        public FlowState(T id, double arousal, double skill, double challenge) {
            this.id = id;
            this.arousalLevel = arousal;
            this.skillLevel = skill;
            this.challengeLevel = challenge;
        }

        public boolean isInFlowChannel() {
            // Flow channel: skill level matches challenge level within a margin
            final double FLOW_MARGIN = 0.2;
            return Math.abs(skillLevel - challengeLevel) <= FLOW_MARGIN;
        }

        public T getId() { return id; }
    }

    /**
     * Represents a transition between flow states with associated properties
     */
    public static class FlowTransition<T> {
        private final FlowState<T> from;
        private final FlowState<T> to;
        private final double transitionProbability;

        public FlowTransition(FlowState<T> from, FlowState<T> to, double prob) {
            this.from = from;
            this.to = to;
            this.transitionProbability = prob;
        }
    }

    /**
     * Adds a new flow state to the graph
     */
    public void addState(FlowState<T> state) {
        states.add(state);
        adjacencyMap.putIfAbsent(state.getId(), new HashSet<>());
    }

    /**
     * Adds a transition between flow states
     */
    public void addTransition(FlowTransition<T> transition) {
        adjacencyMap.get(transition.from.getId()).add(transition);
    }

    /**
     * Finds all states in the flow channel
     */
    public Set<FlowState<T>> findFlowStates() {
        return states.stream()
                    .filter(FlowState::isInFlowChannel)
                    .collect(Collectors.toSet());
    }

    /**
     * Finds optimal path to achieve flow state
     */
    public List<FlowState<T>> findPathToFlow(FlowState<T> start) {
        Set<T> visited = new HashSet<>();
        Queue<Pair<FlowState<T>, List<FlowState<T>>>> queue = new LinkedList<>();
        
        List<FlowState<T>> initialPath = new ArrayList<>();
        initialPath.add(start);
        queue.offer(new Pair<>(start, initialPath));

        while (!queue.isEmpty()) {
            Pair<FlowState<T>, List<FlowState<T>>> current = queue.poll();
            FlowState<T> currentState = current.first;
            List<FlowState<T>> currentPath = current.second;

            if (currentState.isInFlowChannel()) {
                return currentPath;
            }

            if (!visited.contains(currentState.getId())) {
                visited.add(currentState.getId());
                
                for (FlowTransition<T> transition : adjacencyMap.get(currentState.getId())) {
                    if (!visited.contains(transition.to.getId())) {
                        List<FlowState<T>> newPath = new ArrayList<>(currentPath);
                        newPath.add(transition.to);
                        queue.offer(new Pair<>(transition.to, newPath));
                    }
                }
            }
        }
        
        return Collections.emptyList();
    }

    /**
     * Utility class for storing pairs of values
     */
    private static class Pair<A, B> {
        final A first;
        final B second;

        Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }

    /**
     * Verifies if the current graph structure maintains flow state properties
     */
    public boolean verifyFlowProperties() {
        return states.stream().allMatch(state -> {
            // Verify each state has valid transitions
            Set<FlowTransition<T>> transitions = adjacencyMap.get(state.getId());
            return transitions != null && 
                   transitions.stream()
                            .allMatch(t -> t.transitionProbability >= 0 && 
                                          t.transitionProbability <= 1);
        });
    }
}
