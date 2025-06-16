package com.discretelogic.discrete.sets;

import java.util.*;

/**
 * A logical set implementation that supports standard set operations
 * with enhanced functionality for discrete mathematics applications.
 *
 * @param <T> the type of elements in this set
 */
public class LogicalSet<T> {
    private final String name;
    private final Set<T> elements;
    
    /**
     * Creates a new empty LogicalSet with the given name.
     *
     * @param name the name of the set
     */
    public LogicalSet(String name) {
        this.name = name;
        this.elements = new HashSet<>();
    }
    
    /**
     * Creates a new LogicalSet with the given name and elements.
     *
     * @param name the name of the set
     * @param elements the initial elements
     */
    public LogicalSet(String name, Collection<T> elements) {
        this.name = name;
        this.elements = new HashSet<>(elements);
    }
    
    /**
     * Gets the name of this set.
     *
     * @return the set name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Adds an element to this set.
     *
     * @param element the element to add
     * @return true if the element was added (wasn't already present)
     */
    public boolean add(T element) {
        return elements.add(element);
    }
    
    /**
     * Removes an element from this set.
     *
     * @param element the element to remove
     * @return true if the element was removed (was present)
     */
    public boolean remove(T element) {
        return elements.remove(element);
    }
    
    /**
     * Checks if this set contains the given element.
     *
     * @param element the element to check
     * @return true if the element is in this set
     */
    public boolean contains(T element) {
        return elements.contains(element);
    }
    
    /**
     * Gets the size of this set.
     *
     * @return the number of elements in this set
     */
    public int size() {
        return elements.size();
    }
    
    /**
     * Checks if this set is empty.
     *
     * @return true if this set has no elements
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }
    
    /**
     * Returns the union of this set with another set.
     *
     * @param other the other set
     * @return a new set containing all elements from both sets
     */
    public LogicalSet<T> union(LogicalSet<T> other) {
        LogicalSet<T> result = new LogicalSet<>(name + " ∪ " + other.name);
        result.elements.addAll(this.elements);
        result.elements.addAll(other.elements);
        return result;
    }
    
    /**
     * Returns the intersection of this set with another set.
     *
     * @param other the other set
     * @return a new set containing elements present in both sets
     */
    public LogicalSet<T> intersection(LogicalSet<T> other) {
        LogicalSet<T> result = new LogicalSet<>(name + " ∩ " + other.name);
        for (T element : this.elements) {
            if (other.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }
    
    /**
     * Returns the difference of this set with another set (elements in this set but not in other).
     *
     * @param other the other set
     * @return a new set containing elements in this set but not in the other
     */
    public LogicalSet<T> difference(LogicalSet<T> other) {
        LogicalSet<T> result = new LogicalSet<>(name + " - " + other.name);
        for (T element : this.elements) {
            if (!other.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }
    
    /**
     * Checks if this set is a subset of another set.
     *
     * @param other the other set
     * @return true if all elements of this set are in the other set
     */
    public boolean isSubsetOf(LogicalSet<T> other) {
        for (T element : this.elements) {
            if (!other.contains(element)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if this set is a superset of another set.
     *
     * @param other the other set
     * @return true if all elements of the other set are in this set
     */
    public boolean isSupersetOf(LogicalSet<T> other) {
        return other.isSubsetOf(this);
    }
    
    /**
     * Returns the elements of this set as a list.
     *
     * @return a list containing all elements in this set
     */
    public List<T> toList() {
        return new ArrayList<>(elements);
    }
    
    /**
     * Returns the elements of this set.
     *
     * @return a set containing all elements (defensive copy)
     */
    public Set<T> getElements() {
        return new HashSet<>(elements);
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
    
    @Override
    public String toString() {
        return name + " = " + elements.toString();
    }
}