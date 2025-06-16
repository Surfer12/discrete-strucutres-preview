package com.discretelogic.util;

import java.util.List;
import java.util.Set;

public record MathematicalSet(
    List<Integer> elements, 
    Set<Integer> uniqueElements
) {
    public MathematicalSet {
        elements = List.copyOf(elements);  // Immutable copy
        uniqueElements = Set.copyOf(uniqueElements);  // Immutable copy
    }
}
