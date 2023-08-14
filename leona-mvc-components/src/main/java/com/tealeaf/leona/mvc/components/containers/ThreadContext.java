package com.tealeaf.leona.mvc.components.containers;

import java.util.Map;

public class ThreadContext extends SimpleContext {
    private static final ThreadLocal<Map<Object, Object>> THREAD_LOCAL_MAPS = ThreadLocal.withInitial(ThreadAwareMap::new);

    public ThreadContext() {
        super(THREAD_LOCAL_MAPS.get());
    }

    public static ThreadContext create(Map<Object, Object> objectMap) {
        THREAD_LOCAL_MAPS.set(objectMap);
        return new ThreadContext(objectMap);
    }

    protected ThreadContext(Map<Object, Object> objectMap) {
        super(objectMap);
    }

    @Override
    public ThreadContext put(Object key, Object value) {
        super.put(key, value);
        return this;
    }
}
