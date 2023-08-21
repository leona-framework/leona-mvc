package com.sylvona.leona.mvc.components.containers;

import com.google.common.collect.Iterators;
import com.sylvona.leona.core.commons.streams.Streamable;
import jakarta.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * A record representing a tuple of two elements, each with distinct types.
 * This record provides utility methods for appending elements, concatenating tuples,
 * mapping elements, and converting the tuple to an array.
 *
 * @param <T1> The type of the first element.
 * @param <T2> The type of the second element.
 */
public record Tuple<T1, T2>(@NotNull T1 item1, @NotNull T2 item2) implements Streamable<Object> {

    /**
     * Constructs a new {@code Tuple} instance with the provided items.
     *
     * @param item1 The first element of the tuple.
     * @param item2 The second element of the tuple.
     */
    public Tuple {
        Objects.requireNonNull(item1, "item1");
        Objects.requireNonNull(item2, "item2");
    }

    /**
     * Appends a third element to the tuple, creating a new {@code Triple} instance.
     *
     * @param item3 The third element to append.
     * @param <T3> The type of the third element.
     * @return A new triple with the appended third element.
     */
    public <T3> Triple<T1, T2, T3> append(T3 item3) {
        return new Triple<>(item1, item2, item3);
    }

    /**
     * Concatenates the tuple with a third element, creating a new tuple containing the original tuple and the third element.
     *
     * @param item3 The third element to concatenate.
     * @param <T3> The type of the third element.
     * @return A new tuple containing the original tuple and the concatenated third element.
     */
    public <T3> Tuple<Tuple<T1, T2>, T3> concat(T3 item3) {
        return new Tuple<>(this, item3);
    }

    /**
     * Maps the first element of the tuple using the provided mapper function,
     * creating a new tuple with the mapped first element.
     *
     * @param mapper The function to map the first element.
     * @param <R> The type of the mapped element.
     * @return A new tuple with the mapped first element.
     */
    public <R> Tuple<R, T2> mapT1(Function<T1, R> mapper) {
        return new Tuple<>(mapper.apply(item1), item2);
    }

    /**
     * Maps the second element of the tuple using the provided mapper function,
     * creating a new tuple with the mapped second element.
     *
     * @param mapper The function to map the second element.
     * @param <R> The type of the mapped element.
     * @return A new tuple with the mapped second element.
     */
    public <R> Tuple<T1, R> mapT2(Function<T2, R> mapper) {
        return new Tuple<>(item1, mapper.apply(item2));
    }

    /**
     * Converts the tuple to an array containing both elements.
     *
     * @return An array containing both elements of the tuple.
     */
    public Object[] toArray() {
        return new Object[] {item1, item2};
    }

    /**
     * Returns an iterator over both elements of the tuple as an array.
     *
     * @return An iterator over both elements of the tuple.
     */
    @Override
    public Iterator<Object> iterator() {
        return Iterators.forArray(toArray());
    }
}
