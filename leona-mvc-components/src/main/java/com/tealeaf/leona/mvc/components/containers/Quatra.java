package com.tealeaf.leona.mvc.components.containers;

import com.google.common.collect.Iterators;
import com.tealeaf.leona.core.commons.streams.Streamable;
import jakarta.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public record Quatra<T1, T2, T3, T4>(@NotNull T1 item1, @NotNull T2 item2, @NotNull T3 item3, @NotNull T4 item4) implements Streamable<Object> {
    public Quatra {
        Objects.requireNonNull(item1, "item1");
        Objects.requireNonNull(item2, "item2");
        Objects.requireNonNull(item3, "item3");
        Objects.requireNonNull(item4, "item4");
    }

    public <R> Quatra<R, T2, T3, T4> mapT1(Function<T1, R> mapper) {
        return new Quatra<>(mapper.apply(item1), item2, item3, item4);
    }

    public <R> Quatra<T1, R, T3, T4> mapT2(Function<T2, R> mapper) {
        return new Quatra<>(item1, mapper.apply(item2), item3, item4);
    }

    public <R> Quatra<T1, T2, R, T4> mapT3(Function<T3, R> mapper) {
        return new Quatra<>(item1, item2, mapper.apply(item3), item4);
    }

    public <R> Quatra<T1, T2, T3, R> mapT4(Function<T4, R> mapper) {
        return new Quatra<>(item1, item2, item3, mapper.apply(item4));
    }

    public Object[] toArray() {
        return new Object[] {item1, item2, item3};
    }

    @Override
    public Iterator<Object> iterator() {
        return Iterators.forArray(toArray());
    }
}
