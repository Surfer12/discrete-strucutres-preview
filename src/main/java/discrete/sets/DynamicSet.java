package discrete.sets;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a dynamic set with advanced operations 
 * for discrete mathematical structures.
 * @param <T> The type of elements in the set
 */
public class DynamicSet<T> {
    private Set<T> internalSet;

    public DynamicSet() {
        this.internalSet = new HashSet<>();
    }

    /**
     * Perform set union with another set.
     * @param otherSet The set to union with
     * @return A new DynamicSet representing the union
     */
    public DynamicSet<T> union(DynamicSet<T> otherSet) {
        DynamicSet<T> result = new DynamicSet<>();
        result.internalSet.addAll(this.internalSet);
        result.internalSet.addAll(otherSet.internalSet);
        return result;
    }

    /**
     * Perform set intersection with another set.
     * @param otherSet The set to intersect with
     * @return A new DynamicSet representing the intersection
     */
    public DynamicSet<T> intersection(DynamicSet<T> otherSet) {
        DynamicSet<T> result = new DynamicSet<>();
        result.internalSet.addAll(this.internalSet);
        result.internalSet.retainAll(otherSet.internalSet);
        return result;
    }

    // Additional set operations can be added here
}