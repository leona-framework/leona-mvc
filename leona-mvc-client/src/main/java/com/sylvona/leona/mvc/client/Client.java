package com.sylvona.leona.mvc.client;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The {@code Client} annotation is a specialized annotation used to indicate that a class is a client component.
 * Similar to the {@link Service} annotation, it is used to annotate classes that provide client functionality.
 * When used in conjunction with classes that implement {@link RestClient}, this annotation allows specifying a value that represents the name of the client.
 * This can be useful for identifying and distinguishing different client instances.
 *
 * <p>This annotation is used in conjunction with Spring's component scanning mechanism to automatically detect
 * and create instances of client components.
 *
 * <p>Usage example:
 * <pre>
 * {@code
 * @Client("exampleClient")
 * public class ExampleClientImpl extends RestClient {
 *     // ...
 * }
 * }
 * </pre>
 *
 * <p>In this example, the {@code ExampleClientImpl} class is annotated with {@code @Client},
 * and the value {@code "exampleClient"} is provided as the name of the client.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Client {

    /**
     * The name of the client. This value can be used to uniquely identify and distinguish client instances.
     *
     * @return The name of the client.
     */
    String value() default "";
}

