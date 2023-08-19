package com.sylvona.leona.mvc.components.containers;


import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.core.commons.streams.LINQStream;

import java.util.*;

public class SimpleContext implements Context {
    protected final Map<Object, Object> objectMap;
    private final Random idGenerator = new Random();

    /**
     * Represents the current ID of this context.
     * The context ID is NOT static during this object's lifespan and changes when this context is cleared.
     * Semantically, clearing a context is equivalent to generating a new context thus the ID changes on clear.
     */
    private long contextId = idGenerator.nextLong();

    public SimpleContext() {
        this.objectMap = new HashMap<>();
    }

    public SimpleContext(Map<Object, Object> objectMap) {
        this.objectMap = objectMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> cls) {
        return (T) objectMap.get(cls);
    }

    @Override
    public int size() {
        return objectMap.size();
    }

    @Override
    public LINQStream<Entry<Object, Object>> stream() {
        return LINQ.stream(objectMap.entrySet());
    }

    @Override
    public boolean isEmpty() {
        return objectMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return objectMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return objectMap.containsValue(value);
    }

    @Override
    public long contextId() {
        return contextId;
    }

    @Override
    public Object get(Object key) {
        return objectMap.get(key);
    }

    @Override
    public SimpleContext put(Object key, Object value) {
        objectMap.put(key, value);
        return this;
    }

    @Override
    public Object remove(Object key) {
        return objectMap.remove(key);
    }

    @Override
    public void putAll(Map<?, ?> m) {
        objectMap.putAll(m);
    }

    @Override
    public void clear() {
        objectMap.clear();
        contextId = idGenerator.nextLong();
    }

    @Override
    public Set<Object> keySet() {
        return objectMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return objectMap.values();
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return objectMap.entrySet();
    }
}
