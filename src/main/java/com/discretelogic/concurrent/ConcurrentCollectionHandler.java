package com.discretelogic.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Utility class for managing thread-safe and immutable collections.
 * Provides robust methods for concurrent collection handling.
 */
public final class ConcurrentCollectionHandler {

    private ConcurrentCollectionHandler() {
        // Prevent instantiation
    }

    /**
     * Create a thread-safe, immutable copy of a collection.
     * 
     * @param <T> the type of elements in the collection
     * @param sourceCollection the source collection to be copied
     * @return an immutable, thread-safe collection
     */
    public static <T> Collection<T> createImmutableConcurrentCollection(Collection<T> sourceCollection) {
        return Collections.unmodifiableCollection(
            new ConcurrentLinkedQueue<>(sourceCollection)
        );
    }

    /**
     * Transform a collection using a mapping function in a thread-safe manner.
     * 
     * @param <T> the source element type
     * @param <R> the result element type
     * @param sourceCollection the source collection
     * @param mappingFunction the function to transform elements
     * @return a new immutable collection with transformed elements
     */
    public static <T, R> Collection<R> transformCollection(
        Collection<T> sourceCollection, 
        java.util.function.Function<T, R> mappingFunction
    ) {
        return Collections.unmodifiableCollection(
            sourceCollection.stream()
                .map(mappingFunction)
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new))
        );
    }

    /**
     * Safely merge multiple collections into a single thread-safe collection.
     * 
     * @param <T> the element type
     * @param collections variable number of collections to merge
     * @return a merged, immutable, thread-safe collection
     */
    @SafeVarargs
    public static <T> Collection<T> mergeConcurrentCollections(Collection<T>... collections) {
        ConcurrentLinkedQueue<T> mergedQueue = new ConcurrentLinkedQueue<>();
        for (Collection<T> collection : collections) {
            mergedQueue.addAll(collection);
        }
        return Collections.unmodifiableCollection(mergedQueue);
    }
} 