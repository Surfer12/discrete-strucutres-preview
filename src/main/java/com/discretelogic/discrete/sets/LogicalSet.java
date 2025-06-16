package com.discretelogic.discrete.sets;

import java.util.*;

/**
 * Represents a mathematical set with standard set operations.
 * Supports union, intersection, difference, complement, and cartesian product.
 */
public class LogicalSet<T> {
    private final Set<T> elements;
    private final String name;

    public LogicalSet(String name) {
        this.name = name;
        this.elements = new HashSet<>();
    }

    public LogicalSet(String name, Collection<T> elements) {
        this.name = name;
        this.elements = new HashSet<>(elements);
    }

    @SafeVarargs
    public LogicalSet(String name, T... elements) {
        this.name = name;
        this.elements = new HashSet<>(Arrays.asList(elements));
    }

    /**
     * Adds an element to the set.
     */
    public boolean add(T element) {
        return elements.add(element);
    }

    /**
     * Removes an element from the set.
     */
    public boolean remove(T element) {
        return elements.remove(element);
    }

    /**
     * Checks if the set contains an element.
     */
    public boolean contains(T element) {
        return elements.contains(element);
    }

    /**
     * Returns the union of this set with another set.
     */
    public LogicalSet<T> union(LogicalSet<T> other) {
        LogicalSet<T> result = new LogicalSet<>(name + " âˆª " + other.name);
        result.elements.addAll(this.elements);
        result.elements.addAll(other.elements);
        return result;
    }

    /**
     * Returns the intersection of this set with another set.
     */
    public LogicalSet<T> intersection(LogicalSet<T> other) {
        LogicalSet<T> result = new LogicalSet<>(name + " âˆ© " + other.name);
        result.elements.addAll(this.elements);
        result.elements.retainAll(other.elements);
        return result;
    }

    /**
     * Returns the difference of this set with another set (elements in this set but not in other).
     */
    public LogicalSet<T> difference(LogicalSet<T> other) {
        LogicalSet<T> result = new LogicalSet<>(name + " - " + other.name);
        result.elements.addAll(this.elements);
        result.elements.removeAll(other.elements);
        return result;
    }

    /**
     * Returns the symmetric difference of this set with another set.
     */
    public LogicalSet<T> symmetricDifference(LogicalSet<T> other) {
        LogicalSet<T> result = new LogicalSet<>(name + " â–³ " + other.name);
        LogicalSet<T> union = this.union(other);
        LogicalSet<T> intersection = this.intersection(other);
        result.elements.addAll(union.elements);
        result.elements.removeAll(intersection.elements);
        return result;
    }

    /**
     * Returns the complement of this set with respect to a universal set.
     */
    public LogicalSet<T> complement(LogicalSet<T> universalSet) {
        LogicalSet<T> result = new LogicalSet<>(name + "'");
        result.elements.addAll(universalSet.elements);
        result.elements.removeAll(this.elements);
        return result;
    }

    /**
     * Returns the Cartesian product of this set with another set.
     */
    public <U> LogicalSet<String> cartesianProduct(LogicalSet<U> other) {
        LogicalSet<String> result = new LogicalSet<>(name + " Ã— " + other.name);
        for (T thisElement : this.elements) {
            for (U otherElement : other.elements) {
                result.add("(" + thisElement + ", " + otherElement + ")");
            }
        }
        return result;
    }

    /**
     * Checks if this set is a subset of another set.
     */
    public boolean isSubsetOf(LogicalSet<T> other) {
        return other.elements.containsAll(this.elements);
    }

    /**
     * Checks if this set is a proper subset of another set.
     */
    public boolean isProperSubsetOf(LogicalSet<T> other) {
        return this.isSubsetOf(other) && !this.equals(other);
    }

    /**
     * Returns the power set of this set.
     */
    public LogicalSet<LogicalSet<T>> powerSet() {
        LogicalSet<LogicalSet<T>> result = new LogicalSet<>("P(" + name + ")");
        List<T> elementList = new ArrayList<>(elements);
        int powerSetSize = (int) Math.pow(2, elementList.size());

        for (int i = 0; i < powerSetSize; i++) {
            LogicalSet<T> subset = new LogicalSet<>("subset" + i);
            for (int j = 0; j < elementList.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(elementList.get(j));
                }
            }
            result.add(subset);
        }

        return result;
    }

    /**
     * Returns the size (cardinality) of the set.
     */
    public int size() {
        return elements.size();
    }

    /**
     * Checks if the set is empty.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Returns an unmodifiable view of the elements.
     */
    public Set<T> getElements() {
        return Collections.unmodifiableSet(elements);
    }

    /**
     * Gets the name of the set.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (elements.isEmpty()) {
            return name + " = âˆ…";
        }
        return name + " = {" + String.join(", ", 
            elements.stream().map(Object::toString).sorted().toArray(String[]::new)) + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LogicalSet<?> that = (LogicalSet<?>) obj;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}