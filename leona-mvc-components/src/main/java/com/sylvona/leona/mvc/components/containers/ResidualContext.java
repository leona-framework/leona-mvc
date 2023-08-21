package com.sylvona.leona.mvc.components.containers;

import com.google.common.collect.Sets;
import com.sylvona.leona.core.commons.streams.LINQ;
import com.sylvona.leona.core.commons.streams.LINQStream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A specialized {@link Context} that is linked to a {@link Context} parent.
 * While linked, all operations on this context are propagated to its parent.
 * <p>
 * Parents are unlinked when they call .clear() as it's understood clearing a context essentially means resetting it. Thus,
 * operations will no longer propagate to the parent object.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResidualContext extends SimpleContext {
    private final Context parent;
    private final long parentId;

    /**
     * Creates ands link a new {@link ResidualContext} to a {@link Context} parent.
     * @param parent the {@link Context} parent to link to.
     * @return a new linked {@link ResidualContext}
     */
    public static ResidualContext from(Context parent) {
        return new ResidualContext(parent, parent.contextId());
    }

    /**
     * Extended implementation of {@link Map#putAll(Map)} that propagates the operation to a linked parent.
     */
    @Override
    public void putAll(Map<?, ?> m) {
        super.putAll(m);
        if (isLinked()) parent.putAll(m);
    }

    /**
     * Extended implementation of {@link Map#put(Object, Object)} that propagates the operation to a linked parent.
     */
    @Override
    public ResidualContext put(Object key, Object value) {
        super.put(key, value);
        if (isLinked()) parent.put(key, value);
        return this;
    }

    /**
     * Extended implementation of {@link Context#get(Object)} that propagates the operation to a linked parent.
     */
    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        if (value != null) return value;
        return isLinked() ? parent.get(key) : null;
    }

    /**
     * Extended implementation of {@link Context#containsKey(Object)} that additionally checks for the key in a linked parent.
     */
    @Override
    public boolean containsKey(Object key) {
        boolean containsKey = super.containsKey(key);
        if (containsKey) return true;
        return isLinked() && parent.containsKey(key);
    }

    /**
     * Extended implementation of {@link Context#containsValue(Object)} that additionally checks for the value in a linked parent.
     */
    @Override
    public boolean containsValue(Object value) {
        boolean containsValue = super.containsValue(value);
        if (containsValue) return true;
        return isLinked() && parent.containsValue(value);
    }

    /**
     * Extended implementation of {@link Context#values()} that additionally returns the values of a linked parent.
     */
    @Override
    public Collection<Object> values() {
        Collection<Object> values = super.values();
        return isLinked() ? LINQ.stream(values).concat(parent.values()).distinct().toList() : values;
    }

    /**
     * Extended implementation of {@link Context#keySet()} that additionally returns the keys of a linked parent.
     */
    @Override
    public Set<Object> keySet() {
        Set<Object> keys = super.keySet();
        return isLinked() ? Sets.union(keys, parent.keySet()) : keys;
    }

    /**
     * Extended implementation of {@link Context#stream()} that additionally concatenates the stream of a linked parent.
     */
    @Override
    public LINQStream<Entry<Object, Object>> stream() {
        LINQStream<Entry<Object, Object>> entries = stream();
        return isLinked() ? entries.concat(parent.stream()) : entries;
    }

    /**
     * Extended implementation of {@link Context#entrySet()} that additionally gets the entries of a linked parent.
     */
    @Override
    public Set<Entry<Object, Object>> entrySet() {
        Set<Entry<Object, Object>> entries = super.entrySet();
        return isLinked() ? Sets.union(entries, parent.entrySet()) : entries;
    }

    private boolean isLinked() {
        return parent.contextId() == parentId;
    }
}
