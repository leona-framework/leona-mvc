package com.sylvona.leona.mvc.components.containers;

import com.sylvona.leona.core.commons.streams.LINQStream;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ContextView {
    long contextId();
    Object get(Object key);

    boolean containsKey(Object key);

    @SuppressWarnings("unchecked")
    default <T> T get(Class<T> key) {
        Object v = get((Object) key);
        if (key.isInstance(v)) {
            return (T) v;
        }
        throw new NoSuchElementException("Context does not contain a value of type " + key.getName());
    }
    <T> T getOrElse(Object key, T defaultValue);

    @SuppressWarnings("unchecked")
    default <T> Optional<T> getOrEmpty(Object key) {
        if (!containsKey(key)) return Optional.empty();
        return Optional.of((T)get(key));
    }

    boolean isEmpty();

    int size();

    LINQStream<Map.Entry<Object, Object>> stream();

    Map<Object, Object> asMap();
}
