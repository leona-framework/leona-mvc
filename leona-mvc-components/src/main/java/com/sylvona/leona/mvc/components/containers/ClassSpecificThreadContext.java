package com.sylvona.leona.mvc.components.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A specialized thread context implementation that associates objects with specific classes.
 * This class extends the {@link ThreadContext} class and uses a static map to manage
 * per-class thread-local maps for object association.
 */
public class ClassSpecificThreadContext extends ThreadContext {

    /**
     * Thread-local storage for class-specific maps associating objects with threads.
     */
    private static final Map<Class<?>, ThreadLocal<Map<Object, Object>>> THREAD_LOCAL_MAPS = new HashMap<>();

    /**
     * Supplier function to create thread-local maps for specific classes.
     */
    private static final Function<Class<?>, ThreadLocal<Map<Object, Object>>> THREAD_LOCAL_SUPPLIER = c -> ThreadLocal.withInitial(HashMap::new);

    /**
     * Constructs a new {@code ClassSpecificThreadContext} instance using the
     * thread-local map associated with the provided class.
     *
     * @param cls The class for which the context is being constructed.
     */
    private ClassSpecificThreadContext(Class<?> cls) {
        super(THREAD_LOCAL_MAPS.computeIfAbsent(cls, THREAD_LOCAL_SUPPLIER).get());
    }

    /**
     * Creates a new {@code ClassSpecificThreadContext} instance associated with the provided class.
     * This method constructs a new instance using the thread-local map associated with the class.
     *
     * @param cls The class for which the context is being created.
     * @return A new {@code ClassSpecificThreadContext} instance associated with the provided class.
     */
    public static ClassSpecificThreadContext from(Class<?> cls) {
        return new ClassSpecificThreadContext(cls);
    }
}

