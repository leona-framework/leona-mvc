package com.sylvona.leona.mvc.client;

import com.sylvona.leona.core.commons.Priority;

/**
 * Functional interface representing a pre-exchange execution filter that can be applied before sending an HTTP request.
 * Implementations of this interface can modify the request or perform actions based on the request before it's sent.
 */
@FunctionalInterface
public interface PreExchangeExecutionFilter {
    /**
     * Filters and processes the incoming request, allowing modifications or actions to be performed before sending it.
     *
     * @param request The incoming request to be filtered.
     * @return The filtered request after applying the necessary modifications or actions.
     */
    Request filter(Request request);

    /**
     * Provides the priority level of this pre-execution filter. Filters with higher priority values are applied first.
     *
     * @return The priority of this pre-execution filter.
     */
    default Priority preExecutionPriority() {
        return Priority.NORMAL;
    }
}
