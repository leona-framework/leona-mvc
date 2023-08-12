package com.tealeaf.leona.mvc.components.containers;

import com.tealeaf.leona.mvc.components.streams.LINQ;
import com.tealeaf.leona.mvc.components.streams.LINQStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ThreadContext implements Context {
    private static final ThreadLocal<Map<Object, Object>> THREAD_LOCAL_MAPS = ThreadLocal.withInitial(HashMap::new);
    private final Map<Object, Object> objectMap;

    public ThreadContext() {
        objectMap = THREAD_LOCAL_MAPS.get();
    }

    public void copyTo(ThreadContext context) {
        context.objectMap.clear();
        context.objectMap.putAll(this);
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
    public Object get(Object key) {
        return objectMap.get(key);
    }

    @Override
    public ThreadContext put(Object key, Object value) {
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
    public Set<Entry<Object, Object>> entrySet() {
        return objectMap.entrySet();
    }
}
