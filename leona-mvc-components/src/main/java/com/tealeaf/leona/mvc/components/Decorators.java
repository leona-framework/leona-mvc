package com.tealeaf.leona.mvc.components;

import com.tealeaf.leona.mvc.components.containers.ThreadContext;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Decorators {
    public static Runnable contextAware(Runnable runnable) {
        ThreadContext currentThreadContext = new ThreadContext();
        return () -> {
            currentThreadContext.copyTo(new ThreadContext());
            runnable.run();
        };
    }

    public static <T> Supplier<T> contextAware(Supplier<T> supplier) {
        ThreadContext currentThreadContext = new ThreadContext();
        return () -> {
            currentThreadContext.copyTo(new ThreadContext());
            return supplier.get();
        };
    }

    public static <T> Consumer<T> contextAware(Consumer<T> consumer) {
        ThreadContext currentThreadContext = new ThreadContext();
        return element -> {
            currentThreadContext.copyTo(new ThreadContext());
            consumer.accept(element);
        };
    }
}
