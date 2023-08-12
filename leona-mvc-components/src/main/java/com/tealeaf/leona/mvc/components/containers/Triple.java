package com.tealeaf.leona.mvc.components.containers;

import com.google.common.collect.Iterators;
import com.tealeaf.leona.mvc.components.streams.Streamable;
import jakarta.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public record Triple<T1, T2, T3>(@NotNull T1 item1, @NotNull T2 item2, @NotNull T3 item3) implements Streamable<Object> {
    public Triple {
        Objects.requireNonNull(item1, "item1");
        Objects.requireNonNull(item2, "item2");
        Objects.requireNonNull(item3, "item3");
    }

    public <R> Triple<R, T2, T3> mapT1(Function<T1, R> mapper) {
        return new Triple<>(mapper.apply(item1), item2, item3);
    }

    public <R> Triple<T1, R, T3> mapT2(Function<T2, R> mapper) {
        return new Triple<>(item1, mapper.apply(item2), item3);
    }

    public <R> Triple<T1, T2, R> mapT3(Function<T3, R> mapper) {
        return new Triple<>(item1, item2, mapper.apply(item3));
    }

    public Object[] toArray() {
        return new Object[] {item1, item2, item3};
    }

    @Override
    public Iterator<Object> iterator() {
        return Iterators.forArray(toArray());
    }
}
