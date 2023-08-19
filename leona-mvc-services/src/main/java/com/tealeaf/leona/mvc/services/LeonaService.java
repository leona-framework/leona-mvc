package org.lyora.leona.mvc.services;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

@Service
public @interface LeonaService {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String target() default "";
}
