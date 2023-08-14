package com.tealeaf.leona.mvc.components.streams;

import com.google.common.collect.Streams;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class LINQ {
    public static <T> long count(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).count();
    }

    public static <T> Optional<T> findFirst(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).findFirst();
    }

    public static <T> T first(Stream<T> stream) {
        return stream.findFirst().orElseThrow();
    }

    public static <T> T first(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).findFirst().orElseThrow();
    }

    public static <T> T firstOrDefault(Stream<T> stream, Predicate<T> predicate, @Nullable T fallback) {
        return stream.filter(predicate).findFirst().orElse(fallback);
    }

    public static <T> T firstOrDefault(Stream<T> stream, Predicate<T> predicate) {
        return firstOrDefault(stream, predicate, null);
    }

    public static <T> T firstOrDefault(Stream<T> stream, @Nullable T fallback) {
        return stream.findFirst().orElse(fallback);
    }

    public static <T> T firstOrDefault(Stream<T> stream) {
        return stream.findFirst().orElse(null);
    }

    public static <T> T firstOrGet(Stream<T> stream, @NotNull Predicate<T> predicate, @NotNull Supplier<T> fallback) {
        return stream.filter(predicate).findFirst().orElseGet(fallback);
    }

    public static <T> Optional<T> findLast(Stream<T> stream) {
        return Streams.findLast(stream);
    }

    public static <T> Optional<T> findLast(Stream<T> stream, Predicate<T> predicate) {
        return Streams.findLast(stream.filter(predicate));
    }

    public static <T> T last(Stream<T> stream) {
        return Streams.findLast(stream).orElseThrow();
    }

    public static <T> T last(Stream<T> stream, Predicate<T> predicate) {
        return Streams.findLast(stream.filter(predicate)).orElseThrow();
    }

    public static <T> T lastOrDefault(Stream<T> stream, Predicate<T> predicate, @Nullable T fallback) {
        return Streams.findLast(stream.filter(predicate)).orElse(fallback);
    }

    public static <T> T lastOrDefault(Stream<T> stream, Predicate<T> predicate) {
        return lastOrDefault(stream, predicate, null);
    }

    public static <T> T lastOrDefault(Stream<T> stream, @Nullable T fallback) {
        return Streams.findLast(stream).orElse(fallback);
    }

    public static <T> T lastOrDefault(Stream<T> stream) {
        return Streams.findLast(stream).orElse(null);
    }

    public static <T> T lastOrGet(Stream<T> stream, Predicate<T> predicate, Supplier<T> fallback) {
        return Streams.findLast(stream.filter(predicate)).orElseGet(fallback);
    }

    public static <T> LINQStream<T> concat(Stream<? extends T> stream1, Stream<? extends T> stream2) {
        return new LINQStream<>(Stream.concat(stream1, stream2));
    }

    public static <T> LINQStream<T> concat(Stream<T> stream1, Collection<? extends T> collection) {
        return concat(stream1, collection.stream());
    }

    @SafeVarargs
    public static <T> LINQStream<T> join(Stream<T> baseStream, Stream<? extends T>... streams) {
        for (Stream<? extends T> stream : streams) {
            baseStream = Stream.concat(baseStream, stream);
        }
        return new LINQStream<>(baseStream);
    }

    public static <T, R> LINQStream<R> ofType(Stream<T> stream, Class<R> targetClass) {
        //noinspection unchecked
        return new LINQStream<>(stream).filter(i -> targetClass.isAssignableFrom(i.getClass())).map(i -> (R)i);
    }

    public static <T> List<T> toList(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).toList();
    }

    public static <T, TKey, TValue> Map<TKey, TValue> toMap(Stream<T> stream, Function<T, TKey> keyFunction, Function<T, TValue> valueFunction) {
        return stream.collect(Collectors.toMap(keyFunction, valueFunction));
    }

    public static <T, TKey, TValue> Map<TKey, TValue> toMap(Stream<T> stream, Function<T, TKey> keyFunction, Function<T, TValue> valueFunction, Supplier<Map<TKey, TValue>> mapSupplier) {
        return stream.collect(Collectors.toMap(keyFunction, valueFunction, (m1, m2) -> m1, mapSupplier));
    }

    public static <T> LINQStream<T> stream(T[] array) {
        return new LINQStream<>(Arrays.stream(array));
    }

    public static <T> LINQStream<T> stream(Collection<T> collection) {
        return new LINQStream<>(collection.stream());
    }

    public static <T> LINQStream<T> stream(Iterable<T> collection) {
        return new LINQStream<>(StreamSupport.stream(collection.spliterator(), false));
    }

    public static <T> LINQStream<T> stream(Stream<T> stream) {
        if (stream instanceof LINQStream<T> linqStream) return linqStream;
        return new LINQStream<>(stream);
    }
}
