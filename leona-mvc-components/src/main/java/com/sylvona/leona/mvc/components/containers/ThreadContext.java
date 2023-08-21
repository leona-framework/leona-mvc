package com.sylvona.leona.mvc.components.containers;

import java.util.Map;


/**
 * A context implementation that associates objects with the current thread.
 * This class extends the {@link SimpleContext} class and uses a {@link ThreadLocal}
 * to manage per-thread maps for object association.
 */
public class ThreadContext extends SimpleContext {

    /**
     * Thread-local storage for maps associating objects with threads.
     */
    private static final ThreadLocal<Map<Object, Object>> THREAD_LOCAL_MAPS = ThreadLocal.withInitial(ThreadAwareMap::new);

    /**
     * Constructs a new {@code ThreadContext} using the thread-local map.
     * The thread-local map is initialized with a new instance of {@link ThreadAwareMap}.
     */
    public ThreadContext() {
        super(THREAD_LOCAL_MAPS.get());
    }

    /**
     * Creates a new {@code ThreadContext} instance with the provided object map.
     * This method sets the thread-local map to the given map, and constructs
     * a new {@code ThreadContext} using the provided map for object association.
     *
     * @param objectMap The map associating objects with threads.
     * @return A new {@code ThreadContext} instance associated with the provided map.
     */
    public static ThreadContext create(Map<Object, Object> objectMap) {
        THREAD_LOCAL_MAPS.set(objectMap);
        return new ThreadContext(objectMap);
    }

    /**
     * Constructs a new {@code ThreadContext} instance using the provided object map.
     * This constructor is used internally when creating instances from the
     * {@link #create(Map)} method.
     *
     * @param objectMap The map associating objects with threads.
     */
    protected ThreadContext(Map<Object, Object> objectMap) {
        super(objectMap);
    }

    /**
     * Associates the provided key with the given value in the thread's context.
     *
     * @param key The key to associate with the value.
     * @param value The value to be associated with the key.
     * @return This {@code ThreadContext} instance to allow method chaining.
     */
    @Override
    public ThreadContext put(Object key, Object value) {
        super.put(key, value);
        return this;
    }
}
