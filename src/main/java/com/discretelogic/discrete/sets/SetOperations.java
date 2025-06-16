package com.discretelogic.discrete.sets;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides static methods for common set operations.
 */
public class SetOperations {
    
    /**
     * Demonstrates basic set operations.
     *
     * @return a string containing demonstration of basic set operations
     */
    public static String demonstrateBasicOperations() {
        StringBuilder sb = new StringBuilder();
        sb.append("SET OPERATIONS DEMONSTRATION\n");
        sb.append("============================\n\n");
        
        LogicalSet<Integer> setA = new LogicalSet<>("A");
        setA.add(1);
        setA.add(2);
        setA.add(3);
        setA.add(4);
        
        LogicalSet<Integer> setB = new LogicalSet<>("B");
        setB.add(3);
        setB.add(4);
        setB.add(5);
        setB.add(6);
        
        sb.append("Set A: ").append(setA).append("\n");
        sb.append("Set B: ").append(setB).append("\n\n");
        
        sb.append("Union (A ∪ B): ").append(setA.union(setB)).append("\n");
        sb.append("Intersection (A ∩ B): ").append(setA.intersection(setB)).append("\n");
        sb.append("Difference (A - B): ").append(setA.difference(setB)).append("\n");
        sb.append("Difference (B - A): ").append(setB.difference(setA)).append("\n");
        sb.append("Is A subset of B? ").append(setA.isSubsetOf(setB)).append("\n");
        
        LogicalSet<Integer> subset = new LogicalSet<>("C");
        subset.add(3);
        subset.add(4);
        sb.append("Is {3, 4} subset of A? ").append(subset.isSubsetOf(setA)).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Demonstrates power set generation.
     *
     * @return a string containing power set demonstration
     */
    public static String demonstratePowerSet() {
        StringBuilder sb = new StringBuilder();
        sb.append("POWER SET DEMONSTRATION\n");
        sb.append("=======================\n\n");
        
        LogicalSet<Integer> set = new LogicalSet<>("A");
        set.add(1);
        set.add(2);
        set.add(3);
        
        sb.append("Original Set: ").append(set).append("\n\n");
        sb.append("Power Set:\n");
        
        Set<Set<Integer>> powerSet = powerSet(set.getElements());
        for (Set<Integer> subset : powerSet) {
            sb.append("  ").append(subset).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Verifies De Morgan's laws using set operations.
     *
     * @return a string containing De Morgan's law verification
     */
    public static String verifyDeMorganLaws() {
        StringBuilder sb = new StringBuilder();
        sb.append("DE MORGAN'S LAWS VERIFICATION\n");
        sb.append("=============================\n\n");
        
        LogicalSet<Integer> universal = new LogicalSet<>("U");
        for (int i = 1; i <= 10; i++) {
            universal.add(i);
        }
        
        LogicalSet<Integer> setA = new LogicalSet<>("A");
        setA.add(1);
        setA.add(2);
        setA.add(3);
        
        LogicalSet<Integer> setB = new LogicalSet<>("B");
        setB.add(3);
        setB.add(4);
        setB.add(5);
        
        LogicalSet<Integer> complementA = universal.difference(setA);
        LogicalSet<Integer> complementB = universal.difference(setB);
        
        LogicalSet<Integer> unionAB = setA.union(setB);
        LogicalSet<Integer> complementUnion = universal.difference(unionAB);
        LogicalSet<Integer> intersectionComplements = complementA.intersection(complementB);
        
        sb.append("First Law: (A ∪ B)' = A' ∩ B'\n");
        sb.append("A: ").append(setA).append("\n");
        sb.append("B: ").append(setB).append("\n");
        sb.append("(A ∪ B)': ").append(complementUnion).append("\n");
        sb.append("A' ∩ B': ").append(intersectionComplements).append("\n");
        sb.append("Equal? ").append(complementUnion.getElements().equals(intersectionComplements.getElements())).append("\n\n");
        
        LogicalSet<Integer> intersectionAB = setA.intersection(setB);
        LogicalSet<Integer> complementIntersection = universal.difference(intersectionAB);
        LogicalSet<Integer> unionComplements = complementA.union(complementB);
        
        sb.append("Second Law: (A ∩ B)' = A' ∪ B'\n");
        sb.append("(A ∩ B)': ").append(complementIntersection).append("\n");
        sb.append("A' ∪ B': ").append(unionComplements).append("\n");
        sb.append("Equal? ").append(complementIntersection.getElements().equals(unionComplements.getElements())).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Creates the power set of a given set.
     *
     * @param <T> the type of elements in the set
     * @param originalSet the input set
     * @return the power set (set of all subsets)
     */
    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> powerSet = new HashSet<>();
        powerSet.add(new HashSet<>()); // Add empty set
        
        for (T item : originalSet) {
            Set<Set<T>> newSets = new HashSet<>();
            
            for (Set<T> subset : powerSet) {
                Set<T> newSubset = new HashSet<>(subset);
                newSubset.add(item);
                newSets.add(newSubset);
            }
            
            powerSet.addAll(newSets);
        }
        
        return powerSet;
    }
    
    /**
     * Analyzes relationships between two sets.
     *
     * @param setA the first set
     * @param setB the second set
     * @return a string containing analysis of set relationships
     */
    public static String analyzeSetRelationships(LogicalSet<Integer> setA, LogicalSet<Integer> setB) {
        StringBuilder sb = new StringBuilder();
        sb.append("SET RELATIONSHIP ANALYSIS\n");
        sb.append("=========================\n\n");
        
        sb.append("Set A: ").append(setA).append("\n");
        sb.append("Set B: ").append(setB).append("\n\n");
        
        sb.append("Union: ").append(setA.union(setB)).append("\n");
        sb.append("Intersection: ").append(setA.intersection(setB)).append("\n");
        sb.append("A - B: ").append(setA.difference(setB)).append("\n");
        sb.append("B - A: ").append(setB.difference(setA)).append("\n\n");
        
        sb.append("Is A subset of B? ").append(setA.isSubsetOf(setB)).append("\n");
        sb.append("Is B subset of A? ").append(setB.isSubsetOf(setA)).append("\n");
        sb.append("Are A and B equal? ").append(setA.getElements().equals(setB.getElements())).append("\n");
        sb.append("Are A and B disjoint? ").append(setA.intersection(setB).size() == 0).append("\n");
        
        return sb.toString();
    }
}
