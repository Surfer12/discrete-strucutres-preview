package com.discretelogic.discrete.sets;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides static methods for common set operations.
 */
public class SetOperations {
    
    /**
     * Computes the union of two sets.
     *
     * @param <T> the type of elements in the sets
     * @param set1 the first set
     * @param set2 the second set
     * @return a new set containing all elements from both sets
     */
    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.addAll(set2);
        return result;
    }
    
    /**
     * Computes the intersection of two sets.
     *
     * @param <T> the type of elements in the sets
     * @param set1 the first set
     * @param set2 the second set
     * @return a new set containing elements common to both sets
     */
    public static <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }
    
    /**
     * Computes the difference of two sets (set1 - set2).
     *
     * @param <T> the type of elements in the sets
     * @param set1 the first set
     * @param set2 the second set
     * @return a new set containing elements in set1 but not in set2
     */
    public static <T> Set<T> difference(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<>(set1);
        result.removeAll(set2);
        return result;
    }
    
    /**
     * Checks if one set is a subset of another.
     *
     * @param <T> the type of elements in the sets
     * @param subset the potential subset
     * @param superset the potential superset
     * @return true if subset is a subset of superset, false otherwise
     */
    public static <T> boolean isSubset(Set<T> subset, Set<T> superset) {
        return superset.containsAll(subset);
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
} 