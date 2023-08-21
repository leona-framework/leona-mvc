package com.sylvona.leona.mvc.client;

import jakarta.annotation.Nullable;
import com.sylvona.leona.core.commons.Priority;
import org.springframework.http.ResponseEntity;

/**
 * An interface representing a post-exchange execution filter for client operations.
 * Post-exchange filters are used to perform additional processing on the result of a client operation after the exchange has been executed.
 */
@FunctionalInterface
public interface PostExchangeExecutionFilter {
    /**
     * Filters and processes the result of a client execution.
     *
     * @param clientExecutionView the execution view of a client operation.
     * @param response The response entity received from the execution.
     * @param <T> The type of the response body.
     * @return The response entity after filtering
     */
    @Nullable <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, @Nullable ResponseEntity<T> response);

    /**
     * Gets the priority of the post-execution filter. Filters with higher priority values are applied first.
     *
     * @return The priority of the filter.
     */
    default Priority postExecutionPriority() {
        return Priority.NORMAL;
    }
}
