package com.sylvona.leona.mvc.components.containers;

import com.google.common.collect.Iterators;
import com.sylvona.leona.core.commons.streams.Streamable;
import jakarta.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * A record representing a tuple of four elements (quadruple), each with distinct types.
 * This record provides utility methods for mapping elements and converting the tuple to an array.
 *
 * @param <T1> The type of the first element.
 * @param <T2> The type of the second element.
 * @param <T3> The type of the third element.
 * @param <T4> The type of the fourth element.
 */
public record Quadruple<T1, T2, T3, T4>(@NotNull T1 item1, @NotNull T2 item2, @NotNull T3 item3, @NotNull T4 item4) implements Streamable<Object> {

    /**
     * Constructs a new {@code Quatra} instance with the provided items.
     *
     * @param item1 The first element of the quadruple.
     * @param item2 The second element of the quadruple.
     * @param item3 The third element of the quadruple.
     * @param item4 The fourth element of the quadruple.
     */
    public Quadruple {
        Objects.requireNonNull(item1, "item1");
        Objects.requireNonNull(item2, "item2");
        Objects.requireNonNull(item3, "item3");
        Objects.requireNonNull(item4, "item4");
    }

    /**
     * Maps the first element of the quadruple using the provided mapper function,
     * creating a new quadruple with the mapped element.
     *
     * @param mapper The function to map the first element.
     * @param <R> The type of the mapped element.
     * @return A new quadruple with the mapped first element.
     */
    public <R> Quadruple<R, T2, T3, T4> mapT1(Function<T1, R> mapper) {
        return new Quadruple<>(mapper.apply(item1), item2, item3, item4);
    }

    /**
     * Maps the second element of the quadruple using the provided mapper function,
     * creating a new quadruple with the mapped element.
     *
     * @param mapper The function to map the second element.
     * @param <R> The type of the mapped element.
     * @return A new quadruple with the mapped second element.
     */
    public <R> Quadruple<T1, R, T3, T4> mapT2(Function<T2, R> mapper) {
        return new Quadruple<>(item1, mapper.apply(item2), item3, item4);
    }

    /**
     * Maps the third element of the quadruple using the provided mapper function,
     * creating a new quadruple with the mapped element.
     *
     * @param mapper The function to map the third element.
     * @param <R> The type of the mapped element.
     * @return A new quadruple with the mapped third element.
     */
    public <R> Quadruple<T1, T2, R, T4> mapT3(Function<T3, R> mapper) {
        return new Quadruple<>(item1, item2, mapper.apply(item3), item4);
    }

    /**
     * Maps the fourth element of the quadruple using the provided mapper function,
     * creating a new quadruple with the mapped element.
     *
     * @param mapper The function to map the fourth element.
     * @param <R> The type of the mapped element.
     * @return A new quadruple with the mapped fourth element.
     */
    public <R> Quadruple<T1, T2, T3, R> mapT4(Function<T4, R> mapper) {
        return new Quadruple<>(item1, item2, item3, mapper.apply(item4));
    }

    /**
     * Converts the quadruple to an array containing the first three elements.
     *
     * @return An array containing the first three elements of the quadruple.
     */
    public Object[] toArray() {
        return new Object[] {item1, item2, item3};
    }

    /**
     * Returns an iterator over the first three elements of the quadruple as an array.
     *
     * @return An iterator over the first three elements of the quadruple.
     */
    @Override
    public Iterator<Object> iterator() {
        return Iterators.forArray(toArray());
    }
}

