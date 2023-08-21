package com.sylvona.leona.mvc.components.containers;

import com.google.common.collect.Iterators;
import com.sylvona.leona.core.commons.streams.Streamable;
import jakarta.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * A record representing a tuple of three elements (triple), each with distinct types.
 * This record provides utility methods for mapping elements and converting the tuple to an array.
 *
 * @param <T1> The type of the first element.
 * @param <T2> The type of the second element.
 * @param <T3> The type of the third element.
 */
public record Triple<T1, T2, T3>(@NotNull T1 item1, @NotNull T2 item2, @NotNull T3 item3) implements Streamable<Object> {

    /**
     * Constructs a new {@code Triple} instance with the provided items.
     *
     * @param item1 The first element of the triple.
     * @param item2 The second element of the triple.
     * @param item3 The third element of the triple.
     */
    public Triple {
        Objects.requireNonNull(item1, "item1");
        Objects.requireNonNull(item2, "item2");
        Objects.requireNonNull(item3, "item3");
    }

    /**
     * Maps the first element of the triple using the provided mapper function,
     * creating a new triple with the mapped element.
     *
     * @param mapper The function to map the first element.
     * @param <R> The type of the mapped element.
     * @return A new triple with the mapped first element.
     */
    public <R> Triple<R, T2, T3> mapT1(Function<T1, R> mapper) {
        return new Triple<>(mapper.apply(item1), item2, item3);
    }

    /**
     * Maps the second element of the triple using the provided mapper function,
     * creating a new triple with the mapped element.
     *
     * @param mapper The function to map the second element.
     * @param <R> The type of the mapped element.
     * @return A new triple with the mapped second element.
     */
    public <R> Triple<T1, R, T3> mapT2(Function<T2, R> mapper) {
        return new Triple<>(item1, mapper.apply(item2), item3);
    }

    /**
     * Maps the third element of the triple using the provided mapper function,
     * creating a new triple with the mapped element.
     *
     * @param mapper The function to map the third element.
     * @param <R> The type of the mapped element.
     * @return A new triple with the mapped third element.
     */
    public <R> Triple<T1, T2, R> mapT3(Function<T3, R> mapper) {
        return new Triple<>(item1, item2, mapper.apply(item3));
    }

    /**
     * Converts the triple to an array containing all three elements.
     *
     * @return An array containing all three elements of the triple.
     */
    public Object[] toArray() {
        return new Object[] {item1, item2, item3};
    }

    /**
     * Returns an iterator over all three elements of the triple as an array.
     *
     * @return An iterator over all three elements of the triple.
     */
    @Override
    public Iterator<Object> iterator() {
        return Iterators.forArray(toArray());
    }
}

