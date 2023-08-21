package com.sylvona.leona.mvc.components;

import com.sylvona.leona.core.commons.VoidLike;
import com.sylvona.leona.mvc.components.containers.Context;
import com.sylvona.leona.mvc.components.containers.ThreadContext;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A utility class containing methods for creating context-aware decorators for functional interfaces.
 * These decorators allow the execution of functional interfaces (e.g., Runnable, Supplier, Consumer)
 * within a specific context.
 */
public class Decorators {

    /**
     * Creates a context-aware decorator for a {@link Runnable} that runs within the specified context.
     *
     * @param runnable The original Runnable to be decorated.
     * @param context The context in which the Runnable should execute.
     * @return A context-aware Runnable decorator that executes within the provided context.
     */
    public static Runnable contextAware(Runnable runnable, Context context) {
        Map<Object, Object> currentThreadContext = context.asMap();
        return () -> {
            ThreadContext.create(currentThreadContext);
            runnable.run();
        };
    }

    /**
     * Creates a context-aware decorator for a {@link Runnable} that runs within a new thread context.
     *
     * @param runnable The original Runnable to be decorated.
     * @return A context-aware Runnable decorator that executes within a new thread context.
     */
    public static Runnable contextAware(Runnable runnable) {
        return contextAware(runnable, new ThreadContext());
    }

    /**
     * Creates a context-aware decorator for a {@link Supplier} that runs within the specified context.
     *
     * @param supplier The original Supplier to be decorated.
     * @param context The context in which the Supplier should execute.
     * @param <T> The type of value returned by the Supplier.
     * @return A context-aware Supplier decorator that executes within the provided context.
     */
    public static <T> Supplier<T> contextAware(Supplier<T> supplier, Context context) {
        Map<Object, Object> currentThreadContext = context.asMap();
        return () -> {
            ThreadContext.create(currentThreadContext);
            return supplier.get();
        };
    }

    /**
     * Creates a context-aware decorator for a {@link Supplier} that runs within a new thread context.
     *
     * @param supplier The original Supplier to be decorated.
     * @param <T> The type of value returned by the Supplier.
     * @return A context-aware Supplier decorator that executes within a new thread context.
     */
    public static <T> Supplier<T> contextAware(Supplier<T> supplier) {
        return contextAware(supplier, new ThreadContext());
    }

    /**
     * Creates a context-aware decorator for a {@link Consumer} that runs within the specified context.
     *
     * @param consumer The original Consumer to be decorated.
     * @param context The context in which the Consumer should execute.
     * @param <T> The type of element consumed by the Consumer.
     * @return A context-aware Consumer decorator that executes within the provided context.
     */
    public static <T> Consumer<T> contextAware(Consumer<T> consumer, Context context) {
        Map<Object, Object> currentThreadContext = context.asMap();
        return element -> {
            ThreadContext.create(currentThreadContext);
            consumer.accept(element);
        };
    }

    /**
     * Creates a context-aware decorator for a {@link Consumer} that runs within a new thread context.
     *
     * @param consumer The original Consumer to be decorated.
     * @param <T> The type of element consumed by the Consumer.
     * @return A context-aware Consumer decorator that executes within a new thread context.
     */
    public static <T> Consumer<T> contextAware(Consumer<T> consumer) {
        return contextAware(consumer, new ThreadContext());
    }

    /**
     * Converts a {@link Runnable} into a {@link Supplier} that returns a predefined instance
     * after running the Runnable.
     *
     * @param runnable The Runnable to be converted.
     * @return A Supplier that executes the Runnable and returns a predefined instance.
     */
    public static Supplier<VoidLike> toSupplier(Runnable runnable) {
        return () -> {
            runnable.run();
            return VoidLike.INSTANCE;
        };
    }
}
