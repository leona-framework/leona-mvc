package com.tealeaf.leona.mvc.components.containers;

import com.google.common.collect.Iterators;
import com.tealeaf.leona.mvc.components.streams.Streamable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public record Tuple<T1, T2>(@NotNull T1 item1, @NotNull T2 item2) implements Streamable<Object> {
    public Tuple {
        Objects.requireNonNull(item1, "item1");
        Objects.requireNonNull(item2, "item2");
    }

    public <R> Tuple<R, T2> mapT1(Function<T1, R> mapper) {
        return new Tuple<>(mapper.apply(item1), item2);
    }

    public <R> Tuple<T1, R> mapT2(Function<T2, R> mapper) {
        return new Tuple<>(item1, mapper.apply(item2));
    }

    public Object[] toArray() {
        return new Object[] {item1, item2};
    }

    @Override
    public Iterator<Object> iterator() {
        return Iterators.forArray(toArray());
    }
}


