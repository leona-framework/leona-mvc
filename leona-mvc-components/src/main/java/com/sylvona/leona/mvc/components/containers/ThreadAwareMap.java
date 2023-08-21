package com.sylvona.leona.mvc.components.containers;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A specialized implementation of the {@link HashMap} class that is thread-aware.
 * This class maintains a list of thread change listeners and checks that operations
 * on the map are performed within the context of the same thread, notifying listeners if the thread has changed.
 *
 * @param <TKey> The type of keys maintained by this map.
 * @param <TValue> The type of mapped values.
 */
public class ThreadAwareMap<TKey, TValue> extends HashMap<TKey, TValue> {
    private final List<Consumer<? super ThreadAwareMap<TKey, TValue>>> threadChangeListeners = new ArrayList<>();
    private final ReentrantLock checkThreadLock = new ReentrantLock(true);
    private long lastThreadId = Thread.currentThread().getId();

    /**
     * Constructs a new thread-aware map with the specified initial capacity and load factor.
     *
     * @param initialCapacity The initial capacity of the map.
     * @param loadFactor The load factor of the map.
     */
    public ThreadAwareMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructs a new thread-aware map with the specified initial capacity.
     *
     * @param initialCapacity The initial capacity of the map.
     */
    public ThreadAwareMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs a new thread-aware map with default initial capacity and load factor.
     */
    public ThreadAwareMap() {
    }

    /**
     * Constructs a new thread-aware map with the same mappings as the specified map.
     *
     * @param m The map whose mappings are to be placed in this map.
     */
    public ThreadAwareMap(Map<? extends TKey, ? extends TValue> m) {
        super(m);
    }

    /**
     * Adds a listener to be notified of changes in the map due to thread switches.
     *
     * @param changeListener The consumer to be notified of thread changes.
     */
    public void addListener(Consumer<? super ThreadAwareMap<TKey, TValue>> changeListener) {
        threadChangeListeners.add(changeListener);
    }

    @Override
    public TValue put(TKey key, TValue value) {
        checkForThreadChange();
        return super.put(key, value);
    }

    @Override
    public int size() {
        checkForThreadChange();
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        checkForThreadChange();
        return super.isEmpty();
    }

    @Override
    public TValue get(Object key) {
        checkForThreadChange();
        return super.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        checkForThreadChange();
        return super.containsKey(key);
    }

    @Override
    public void putAll(Map<? extends TKey, ? extends TValue> m) {
        checkForThreadChange();
        super.putAll(m);
    }

    @Override
    public TValue remove(Object key) {
        checkForThreadChange();
        return super.remove(key);
    }

    @Override
    public void clear() {
        checkForThreadChange();
        super.clear();
    }

    @Override
    public boolean containsValue(Object value) {
        checkForThreadChange();
        return super.containsValue(value);
    }

    @Override
    public Set<TKey> keySet() {
        checkForThreadChange();
        return super.keySet();
    }

    @Override
    public Collection<TValue> values() {
        checkForThreadChange();
        return super.values();
    }

    @Override
    public Set<Entry<TKey, TValue>> entrySet() {
        checkForThreadChange();
        return super.entrySet();
    }

    @Override
    public TValue getOrDefault(Object key, TValue defaultValue) {
        checkForThreadChange();
        return super.getOrDefault(key, defaultValue);
    }

    @Override
    public TValue putIfAbsent(TKey key, TValue value) {
        checkForThreadChange();
        return super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        checkForThreadChange();
        return super.remove(key, value);
    }

    @Override
    public boolean replace(TKey key, TValue oldValue, TValue newValue) {
        checkForThreadChange();
        return super.replace(key, oldValue, newValue);
    }

    @Override
    public TValue replace(TKey key, TValue value) {
        checkForThreadChange();
        return super.replace(key, value);
    }

    @Override
    public TValue computeIfAbsent(TKey key, Function<? super TKey, ? extends TValue> mappingFunction) {
        checkForThreadChange();
        return super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public TValue computeIfPresent(TKey key, BiFunction<? super TKey, ? super TValue, ? extends TValue> remappingFunction) {
        checkForThreadChange();
        return super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public TValue compute(TKey key, BiFunction<? super TKey, ? super TValue, ? extends TValue> remappingFunction) {
        checkForThreadChange();
        return super.compute(key, remappingFunction);
    }

    @Override
    public TValue merge(TKey key, TValue value, BiFunction<? super TValue, ? super TValue, ? extends TValue> remappingFunction) {
        checkForThreadChange();
        return super.merge(key, value, remappingFunction);
    }

    @Override
    public void forEach(BiConsumer<? super TKey, ? super TValue> action) {
        checkForThreadChange();
        super.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super TKey, ? super TValue, ? extends TValue> function) {
        checkForThreadChange();
        super.replaceAll(function);
    }

    @Override
    public Object clone() {
        checkForThreadChange();
        return super.clone();
    }

    private void checkForThreadChange() {
        if (!checkThreadLock.tryLock()) return;
        long threadId = Thread.currentThread().getId();
        try {
            if (lastThreadId != threadId) {
                lastThreadId = threadId;
                for (Consumer<? super ThreadAwareMap<TKey, TValue>> changeListener : threadChangeListeners) {
                    changeListener.accept(this);
                }
                for (TValue value : values()) {
                    if (value instanceof ThreadAware threadAware) threadAware.onThreadCopy();
                }
            }
        } finally {
            checkThreadLock.unlock();
            lastThreadId = threadId;
        }
    }
}
