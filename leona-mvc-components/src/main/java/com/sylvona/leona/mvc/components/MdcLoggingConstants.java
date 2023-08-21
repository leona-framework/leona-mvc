package com.sylvona.leona.mvc.components;

/**
 * A class containing constants used for logging with Mapped Diagnostic Context (MDC).
 * MDC is used to enrich log statements with contextual information.
 */
public class MdcLoggingConstants {

    /**
     * The key used for specifying the backend name in the MDC.
     */
    public static final String BACKEND_NAME = "backendName";

    /**
     * The key used for specifying the event type in the MDC.
     */
    public static final String EVENT_TYPE = "event";

    /**
     * The key used for specifying the response time in the MDC.
     */
    public static final String RESPONSE_TIME = "responseTime";

    /**
     * The key used for specifying the status code in the MDC.
     */
    public static final String STATUS_CODE = "status";

    /**
     * The key used for specifying the execution target in the MDC.
     */
    public static final String EXECUTION_TARGET = "executionTarget";

    /**
     * The key used for specifying the trace ID in the MDC.
     */
    public static final String TRACE_ID = "traceId";

    /**
     * The key used for specifying the span ID in the MDC.
     */
    public static final String SPAN_ID = "spanId";

    /**
     * The key used for specifying the endpoint in the MDC.
     */
    public static final String ENDPOINT = "endpoint";
}
