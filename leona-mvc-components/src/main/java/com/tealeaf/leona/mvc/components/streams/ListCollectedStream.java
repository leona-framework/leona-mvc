package com.tealeaf.leona.mvc.components.streams;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Stream;

class ListCollectedStream<T> extends LINQStream<T> {
    private final List<T> list;

    ListCollectedStream(Stream<T> stream, List<T> list) {
        super(stream);
        this.list = list;
    }

    @Override
    public List<T> toList() {
        return list;
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return list.toArray(generator);
    }
}
