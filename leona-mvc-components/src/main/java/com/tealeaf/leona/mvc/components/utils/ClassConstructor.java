package org.lyora.leona.mvc.components.utils;

import java.lang.reflect.InvocationTargetException;

public class ClassConstructor {
    public static <T> T createInstance(Class<T> cls, Object... args) {
        try {
            return cls.getDeclaredConstructor().newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
