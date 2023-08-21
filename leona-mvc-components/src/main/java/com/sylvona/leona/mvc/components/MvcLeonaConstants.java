package com.sylvona.leona.mvc.components;

/**
 * The {@code MvcLeonaConstants} class defines constant values used across multiple leona-mvc libraries for various purposes.
 * These constants can be used to access or identify specific keys or values in different parts of the framework.
 */
public class MvcLeonaConstants {
    /**
     * The constant key used to store and retrieve the duration of a request execution within internal contexts.
     * This duration represents the time taken for the execution of a specific operation or request.
     */
    public static final String REQUEST_DURATION_KEY = "REQUEST_DURATION_KEY";

    /**
     * The constant key used to store and retrieve MDC (Mapped Diagnostic Context) capture suppliers in the context of a client execution.
     * MDC captures are used to provide diagnostic information for log messages during client interactions.
     */
    public static final String MDC_CAPTURE_KEY = "MDC_CAPTURES_KEY";
}
