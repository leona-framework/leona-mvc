package com.tealeaf.leona.mvc.components.streams;

import com.google.common.collect.Streams;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@UtilityClass
public class LINQ {
    public <T> long count(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).count();
    }

    public <T> Optional<T> findFirst(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).findFirst();
    }

    public <T> T first(Stream<T> stream) {
        return stream.findFirst().orElseThrow();
    }

    public <T> T first(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).findFirst().orElseThrow();
    }

    public <T> T firstOrDefault(Stream<T> stream, Predicate<T> predicate, @Nullable T fallback) {
        return stream.filter(predicate).findFirst().orElse(fallback);
    }

    public <T> T firstOrDefault(Stream<T> stream, Predicate<T> predicate) {
        return firstOrDefault(stream, predicate, null);
    }

    public <T> T firstOrDefault(Stream<T> stream, @Nullable T fallback) {
        return stream.findFirst().orElse(fallback);
    }

    public <T> T firstOrDefault(Stream<T> stream) {
        return stream.findFirst().orElse(null);
    }

    public <T> T firstOrGet(Stream<T> stream, @NotNull Predicate<T> predicate, @NotNull Supplier<T> fallback) {
        return stream.filter(predicate).findFirst().orElseGet(fallback);
    }

    public <T> Optional<T> findLast(Stream<T> stream) {
        return Streams.findLast(stream);
    }

    public <T> Optional<T> findLast(Stream<T> stream, Predicate<T> predicate) {
        return Streams.findLast(stream.filter(predicate));
    }

    public <T> T last(Stream<T> stream) {
        return Streams.findLast(stream).orElseThrow();
    }

    public <T> T last(Stream<T> stream, Predicate<T> predicate) {
        return Streams.findLast(stream.filter(predicate)).orElseThrow();
    }

    public <T> T lastOrDefault(Stream<T> stream, Predicate<T> predicate, @Nullable T fallback) {
        return Streams.findLast(stream.filter(predicate)).orElse(fallback);
    }

    public <T> T lastOrDefault(Stream<T> stream, Predicate<T> predicate) {
        return lastOrDefault(stream, predicate, null);
    }

    public <T> T lastOrDefault(Stream<T> stream, @Nullable T fallback) {
        return Streams.findLast(stream).orElse(fallback);
    }

    public <T> T lastOrDefault(Stream<T> stream) {
        return Streams.findLast(stream).orElse(null);
    }

    public <T> T lastOrGet(Stream<T> stream, Predicate<T> predicate, Supplier<T> fallback) {
        return Streams.findLast(stream.filter(predicate)).orElseGet(fallback);
    }

    public <T> LINQStream<T> concat(Stream<? extends T> stream1, Stream<? extends T> stream2) {
        return new LINQStream<>(Stream.concat(stream1, stream2));
    }

    public <T> LINQStream<T> concat(Stream<T> stream1, Collection<? extends T> collection) {
        return concat(stream1, collection.stream());
    }

    @SafeVarargs
    public <T> LINQStream<T> join(Stream<T> baseStream, Stream<? extends T>... streams) {
        for (Stream<? extends T> stream : streams) {
            baseStream = Stream.concat(baseStream, stream);
        }
        return new LINQStream<>(baseStream);
    }

    public <T, R> LINQStream<R> ofType(Stream<T> stream, Class<R> targetClass) {
        //noinspection unchecked
        return new LINQStream<>(stream).filter(i -> targetClass.isAssignableFrom(i.getClass())).map(i -> (R)i);
    }

    public <T> List<T> toList(Stream<T> stream, Predicate<T> predicate) {
        return stream.filter(predicate).toList();
    }

    public <T, TKey, TValue> Map<TKey, TValue> toMap(Stream<T> stream, Function<T, TKey> keyFunction, Function<T, TValue> valueFunction) {
        return stream.collect(Collectors.toMap(keyFunction, valueFunction));
    }

    public <T, TKey, TValue> Map<TKey, TValue> toMap(Stream<T> stream, Function<T, TKey> keyFunction, Function<T, TValue> valueFunction, Supplier<Map<TKey, TValue>> mapSupplier) {
        return stream.collect(Collectors.toMap(keyFunction, valueFunction, (m1, m2) -> m1, mapSupplier));
    }

    public <T> LINQStream<T> stream(T[] array) {
        return new LINQStream<>(Arrays.stream(array));
    }

    public <T> LINQStream<T> stream(Collection<T> collection) {
        return new LINQStream<>(collection.stream());
    }

    public <T> LINQStream<T> stream(Stream<T> stream) {
        if (stream instanceof LINQStream<T> linqStream) return linqStream;
        return new LINQStream<>(stream);
    }
}
