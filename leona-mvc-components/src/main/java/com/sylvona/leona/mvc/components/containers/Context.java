package com.sylvona.leona.mvc.components.containers;

import java.util.HashMap;
import java.util.Map;

/**
 * A map-like interface representing a context containing associations between keys and values.
 * This interface extends both the {@link Map} and {@link ContextView} interfaces.
 * It provides methods for putting, getting, and viewing context entries.
 *
 * <p>While heavily based on reactor's Context and ContextView, this interface serves as a simplified
 * and more generic version of those concepts.
 *
 * @see ContextView
 * @see java.util.Map
 */
@SuppressWarnings("unchecked")
public interface Context extends Map<Object, Object>, ContextView {

    /**
     * Retrieves the value associated with the specified key, or returns the provided default value
     * if the key is not present in the context.
     *
     * @param key The key to retrieve the value for.
     * @param defaultValue The value to return if the key is not present.
     * @param <T> The type of the value.
     * @return The value associated with the key, or the default value if the key is not present.
     */
    @Override
    default <T> T getOrElse(Object key, T defaultValue) {
        return (T) getOrDefault(key, defaultValue);
    }

    /**
     * Associates the specified value with the specified key in this context.
     *
     * @param key The key to associate the value with.
     * @param value The value to be associated.
     * @return The modified context instance with the new association.
     */
    Context put(Object key, Object value);

    /**
     * Copies all the entries from the provided context view into this context.
     *
     * @param other The context view from which to copy entries.
     * @return The modified context instance with added entries.
     */
    default Context putAll(ContextView other) {
        this.putAll(other.asMap());
        return this;
    }

    /**
     * Returns a new {@link Map} containing the entries of this context.
     *
     * @return A new map containing the entries of this context.
     */
    @Override
    default Map<Object, Object> asMap() {
        return new HashMap<>(this);
    }

    /**
     * Returns a read-only view of this context, providing read-only access to its entries.
     *
     * @return A read-only context view representing this context.
     */
    default ContextView readOnly() {
        return this;
    }
}
