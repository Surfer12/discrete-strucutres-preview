package com.discretelogic.util;

import java.util.List;
import java.util.Set;

public class MathematicalSet {
    private final List<Integer> elements;
    private final Set<Integer> uniqueElements;

    public MathematicalSet(List<Integer> elements, Set<Integer> uniqueElements) {
        this.elements = List.copyOf(elements);  // Immutable copy
        this.uniqueElements = Set.copyOf(uniqueElements);  // Immutable copy
    }

    public List<Integer> getElements() {
        return elements;
    }

    public Set<Integer> getUniqueElements() {
        return uniqueElements;
    }
}
