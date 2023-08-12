package com.tealeaf.leona.mvc.components.containers;

import java.util.HashMap;
import java.util.Map;

/**
 * Heavily based on reactor's Context / ContextView
 */
@SuppressWarnings("unchecked")
public interface Context extends Map<Object, Object>, ContextView {
    @Override
    default <T> T getOrElse(Object key, T defaultValue) {
        return (T) getOrDefault(key, defaultValue);
    }

    Context put(Object key, Object value);

    default Context putAll(ContextView other) {
        this.putAll(other.asMap());
        return this;
    }

    @Override
    default Map<Object, Object> asMap() {
        return new HashMap<>(this);
    }

    default ContextView readOnly() {
        return this;
    }
}
