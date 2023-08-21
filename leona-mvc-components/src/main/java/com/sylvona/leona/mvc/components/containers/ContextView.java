package com.sylvona.leona.mvc.components.containers;

import com.sylvona.leona.core.commons.streams.LINQStream;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * An interface providing a view into a context containing associations between keys and values.
 * This interface defines methods for querying and retrieving values from a context without
 * modifying its content.
 *
 * @see Context
 */
public interface ContextView {
    /**
     * Returns a unique identifier representing the context.
     *
     * @return The unique identifier of the context.
     */
    long contextId();

    /**
     * Retrieves the value associated with the specified key in the context.
     *
     * @param key The key for which to retrieve the associated value.
     * @return The value associated with the key, or {@code null} if the key is not present.
     */
    Object get(Object key);

    /**
     * Checks if the context contains an association with the specified key.
     *
     * @param key The key to check for association.
     * @return {@code true} if the context contains the key, {@code false} otherwise.
     */
    boolean containsKey(Object key);

    /**
     * Retrieves the value associated with the specified class type key in the context.
     * If the associated value is not of the specified type, a {@link NoSuchElementException} is thrown.
     *
     * @param key The class type key for which to retrieve the associated value.
     * @param <T> The type of the value.
     * @return The value associated with the class type key, if present and of the correct type.
     * @throws NoSuchElementException If the value associated with the key is not of the specified type.
     */
    @SuppressWarnings("unchecked")
    default <T> T get(Class<T> key) {
        Object v = get((Object) key);
        if (key.isInstance(v)) {
            return (T) v;
        }
        throw new NoSuchElementException("Context does not contain a value of type " + key.getName());
    }

    /**
     * Retrieves the value associated with the specified key in the context, or a default value
     * if the key is not present.
     *
     * @param key The key for which to retrieve the associated value.
     * @param defaultValue The value to return if the key is not present.
     * @param <T> The type of the value.
     * @return The value associated with the key, or the default value if the key is not present.
     */
    <T> T getOrElse(Object key, T defaultValue);

    /**
     * Retrieves the value associated with the specified key in the context as an {@link Optional}.
     * If the key is not present in the context, an empty {@link Optional} is returned.
     *
     * @param key The key for which to retrieve the associated value.
     * @param <T> The type of the value.
     * @return An {@link Optional} containing the associated value, or an empty {@link Optional}
     *         if the key is not present.
     */
    @SuppressWarnings("unchecked")
    default <T> Optional<T> getOrEmpty(Object key) {
        if (!containsKey(key)) return Optional.empty();
        return Optional.of((T)get(key));
    }

    /**
     * Checks if the context is empty.
     *
     * @return {@code true} if the context is empty, {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the number of associations in the context.
     *
     * @return The number of associations in the context.
     */
    int size();

    /**
     * Returns a {@link LINQStream} providing a stream of entries in the context.
     *
     * @return A {@link LINQStream} of context entries.
     */
    LINQStream<Map.Entry<Object, Object>> stream();

    /**
     * Returns a {@link Map} containing the associations in the context.
     *
     * @return A {@link Map} of context associations.
     */
    Map<Object, Object> asMap();
}
