package com.tealeaf.leona.mvc.components;

import com.tealeaf.leona.core.commons.VoidLike;
import com.tealeaf.leona.mvc.components.containers.Context;
import com.tealeaf.leona.mvc.components.containers.ThreadContext;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Decorators {
    public static Runnable contextAware(Runnable runnable, Context context) {
        Map<Object, Object> currentThreadContext = context.asMap();
        return () -> {
            ThreadContext.create(currentThreadContext);
            runnable.run();
        };
    }

    public static Runnable contextAware(Runnable runnable) {
        return contextAware(runnable, new ThreadContext());
    }

    public static <T> Supplier<T> contextAware(Supplier<T> supplier, Context context) {
        Map<Object, Object> currentThreadContext = context.asMap();
        return () -> {
            ThreadContext.create(currentThreadContext);
            return supplier.get();
        };
    }

    public static <T> Supplier<T> contextAware(Supplier<T> supplier) {
        return contextAware(supplier, new ThreadContext());
    }

    public static <T> Consumer<T> contextAware(Consumer<T> consumer, Context context) {
        Map<Object, Object> currentThreadContext = context.asMap();
        return element -> {
            ThreadContext.create(currentThreadContext);
            consumer.accept(element);
        };
    }

    public static <T> Consumer<T> contextAware(Consumer<T> consumer) {
        return contextAware(consumer, new ThreadContext());
    }

    public static Supplier<VoidLike> toSupplier(Runnable runnable) {
        return () -> {
            runnable.run();
            return VoidLike.INSTANCE;
        };
    }
}
