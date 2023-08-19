package org.lyora.leona.mvc.client.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows for the automatic merging of properties based on a public static merging method.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface MergeCandidate {

    @java.lang.annotation.Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MergeMethod {
    }
}
