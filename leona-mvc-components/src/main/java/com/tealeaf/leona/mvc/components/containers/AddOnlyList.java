package com.tealeaf.leona.mvc.components.containers;

import com.tealeaf.leona.mvc.components.streams.LINQStream;

import java.util.Collection;
import java.util.function.Consumer;

public interface AddOnlyList<T> {
    int size();

    boolean isEmpty();

    T get(int index);

    boolean add(T item);

    boolean addAll(Collection<? extends T> collection);
    LINQStream<T> stream();

    void forEach(Consumer<? super T> consumer);
}
