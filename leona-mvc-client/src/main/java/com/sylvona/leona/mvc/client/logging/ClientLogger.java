package com.sylvona.leona.mvc.client.logging;

import com.sylvona.leona.mvc.client.RestClient;
import com.sylvona.leona.mvc.client.ClientExecutionView;
import com.sylvona.leona.mvc.client.ClientInitializationHook;
import com.sylvona.leona.mvc.client.PostExchangeExecutionFilter;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

/**
 * An interface that combines the functionalities of a {@link PostExchangeExecutionFilter} and a {@link ClientInitializationHook},
 * designed to provide logging capabilities for client executions within a {@link RestClient}.
 */
public interface ClientLogger extends PostExchangeExecutionFilter, ClientInitializationHook {
    /**
     * Logs the execution details of a client operation.
     *
     * @param executionView The view of the client execution.
     */
    void log(ClientExecutionView executionView);

    /**
     * Sets the Slf4j {@link Logger} to be invoked when logging. This is technically optional, but helps enhance logging clarity when properly implemented.
     *
     * @param logger The logger instance to use.
     */
    default void setLogger(Logger logger) {}

    /**
     * Calls the logging function of the implementer.
     */
    @Override
    default <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, ResponseEntity<T> response) {
        log(clientExecutionView);
        return response;
    }

    /**
     * Initializes the client logger by adding it as a post-execution filter and setting the logger.
     *
     * @param clientModifier The modifier for the client configuration.
     */
    @Override
    default void onInitialize(RestClient.Modifier clientModifier) {
        clientModifier.getPostExecutionFilters().add(this);
        setLogger(clientModifier.getLogger());
    }
}
