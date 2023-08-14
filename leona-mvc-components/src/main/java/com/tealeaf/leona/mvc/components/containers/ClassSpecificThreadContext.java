package com.tealeaf.leona.mvc.components.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ClassSpecificThreadContext extends ThreadContext {
    private static final Map<Class<?>, ThreadLocal<Map<Object, Object>>> THREAD_LOCAL_MAPS = new HashMap<>();
    private static final Function<Class<?>, ThreadLocal<Map<Object, Object>>> THREAD_LOCAL_SUPPLIER = c -> ThreadLocal.withInitial(HashMap::new);

    private ClassSpecificThreadContext(Class<?> cls) {
        super(THREAD_LOCAL_MAPS.computeIfAbsent(cls, THREAD_LOCAL_SUPPLIER).get());
    }

    public static ClassSpecificThreadContext from(Class<?> cls) {
        return new ClassSpecificThreadContext(cls);
    }
}
