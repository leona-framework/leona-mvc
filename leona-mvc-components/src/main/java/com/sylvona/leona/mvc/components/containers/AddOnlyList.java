package com.sylvona.leona.mvc.components.containers;

import com.sylvona.leona.core.commons.streams.LINQStream;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * An interface representing an add-only list data structure.
 *
 * @param <T> The type of elements stored in the list.
 */
public interface AddOnlyList<T> {
    /**
     * Returns the number of elements in the list.
     *
     * @return The number of elements in the list.
     */
    int size();

    /**
     * Checks if the list is empty.
     *
     * @return {@code true} if the list is empty, {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * Retrieves the element at the specified index in the list.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    T get(int index);

    /**
     * Adds the specified item to the end of the list.
     *
     * @param item The item to be added to the list.
     * @return {@code true} if the item was added successfully, {@code false} otherwise.
     */
    boolean add(T item);

    /**
     * Adds all elements from the specified collection to the end of the list.
     *
     * @param collection The collection containing elements to be added.
     * @return {@code true} if at least one item was added, {@code false} if the list remains unchanged.
     */
    boolean addAll(Collection<? extends T> collection);

    /**
     * Returns a {@link LINQStream} instance for this list, enabling functional-style operations.
     *
     * @return A {@link LINQStream} instance for this list.
     */
    LINQStream<T> stream();

    /**
     * Performs the given action on each element of the list.
     *
     * @param consumer The action to be performed on each element.
     */
    void forEach(Consumer<? super T> consumer);
}
