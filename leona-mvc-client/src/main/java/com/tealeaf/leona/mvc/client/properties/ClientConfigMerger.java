package com.tealeaf.leona.mvc.client.properties;

import com.tealeaf.leona.core.commons.streams.LINQ;
import com.tealeaf.leona.core.commons.streams.LINQStream;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

class ClientConfigMerger {
    public static boolean isMergeable(Class<?> cls) {
        return cls.getAnnotation(MergeCandidate.class) != null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T merge(T obj1, T obj2) {
        Class<?> cls = obj1.getClass();
        Method mergeMethod = LINQ.stream(cls.getDeclaredMethods())
                .firstOrDefault(m -> Modifier.isStatic(m.getModifiers()) && m.getAnnotation(MergeCandidate.MergeMethod.class) != null);
        if (mergeMethod == null) throw new NoSuchElementException("No valid method annotated with MergeMethod on MergeCandidate class.");
        try {
            return (T) mergeMethod.invoke(null, obj1, obj2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T mergeConfigs(List<T> configurations, T result, String... ignoredFieldNames) {
        Set<String> ignoredFieldNameSet = Set.of(ignoredFieldNames);
        Consumer<Field> mergeConfigField = mergeHighestConfig(configurations, result);

        getAllFieldsList(result.getClass()).stream()
                .filter(f -> !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))
                .filter(f -> !ignoredFieldNameSet.contains(f.getName()))
                .forEach(mergeConfigField);

        return result;
    }

    private static List<Field> getAllFieldsList(Class<?> cls) {
        final List<Field> allFields = new ArrayList<>();
        while (cls != null) {
            final Field[] declaredFields = cls.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            cls = cls.getSuperclass();
        }
        return allFields;
    }

    private static <T> Consumer<Field> mergeHighestConfig(List<T> configurations, T result) {
        return configField -> {
            LINQStream<Object> configValueStream = LINQ.stream(configurations)
                    .map(config -> readField(configField, config))
                    .filter(Objects::nonNull);

            Optional<Object> mergedValue = isMergeable(configField.getType())
                    ? configValueStream.reduce(ClientConfigMerger::merge)
                    : configValueStream.findLast();

            mergedValue.ifPresent(value -> setField(configField, result, value));
        };
    }

    private static Object readField(Field field, Object config) {
        try {
            return FieldUtils.readField(field, config, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setField(Field field, Object config, Object value) {
        try {
            FieldUtils.writeField(field, config, value,true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
