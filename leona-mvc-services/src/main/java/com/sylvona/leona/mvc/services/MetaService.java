package com.sylvona.leona.mvc.services;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

/**
 * MetaService is a custom annotation used to provide metadata for services.
 * It can be applied to service classes or methods to associate additional information.
 * This annotation supports specifying a key or name for the service and a target identifier.
 */
@Service
public @interface MetaService {
    /**
     * Alias for {@link #key()}. Specifies the key for the service.
     *
     * @return The key of the service.
     */
    @AliasFor("key")
    String value() default "";

    /**
     * Alias for {@link #value()}. Specifies the name for the service.
     *
     * @return The name of the service.
     */
    @AliasFor("value")
    String name() default "";

    /**
     * Specifies the key for the service.
     *
     * @return The key of the service.
     */
    String key() default "";

    /**
     * Specifies the target identifier for the service.
     *
     * @return The target identifier.
     */
    String target() default "";
}
