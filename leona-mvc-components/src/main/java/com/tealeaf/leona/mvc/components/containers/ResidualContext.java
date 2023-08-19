package com.tealeaf.leona.mvc.components.containers;

import com.google.common.collect.Sets;
import com.tealeaf.leona.core.commons.streams.LINQ;
import com.tealeaf.leona.core.commons.streams.LINQStream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResidualContext extends SimpleContext {
    private final Context parent;
    private final long parentId;

    public static ResidualContext from(Context parent) {
        return new ResidualContext(parent, parent.contextId());
    }

    @Override
    public void putAll(Map<?, ?> m) {
        super.putAll(m);
        if (isLinked()) parent.putAll(m);
    }

    @Override
    public ResidualContext put(Object key, Object value) {
        super.put(key, value);
        if (isLinked()) parent.put(key, value);
        return this;
    }

    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        if (value != null) return value;
        return isLinked() ? parent.get(key) : null;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean containsKey = super.containsKey(key);
        if (containsKey) return true;
        return isLinked() && parent.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        boolean containsValue = super.containsValue(value);
        if (containsValue) return true;
        return isLinked() && parent.containsValue(value);
    }

    @Override
    public Collection<Object> values() {
        Collection<Object> values = super.values();
        return isLinked() ? LINQ.stream(values).concat(parent.values()).distinct().toList() : values;
    }

    @Override
    public Set<Object> keySet() {
        Set<Object> keys = super.keySet();
        return isLinked() ? Sets.union(keys, parent.keySet()) : keys;
    }

    @Override
    public LINQStream<Entry<Object, Object>> stream() {
        LINQStream<Entry<Object, Object>> entries = stream();
        return isLinked() ? entries.concat(parent.stream()) : entries;
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        Set<Entry<Object, Object>> entries = super.entrySet();
        return isLinked() ? Sets.union(entries, parent.entrySet()) : entries;
    }

    private boolean isLinked() {
        return parent.contextId() == parentId;
    }
}
