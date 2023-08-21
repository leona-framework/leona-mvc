package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.components.containers.ContextView;
import com.sylvona.leona.mvc.components.containers.ExecutionView;
import org.springframework.http.ResponseEntity;

/**
 * An interface representing the result of a client execution, providing access to various aspects of the execution, including its possible {@link ResponseEntity}
 */
public interface ClientExecutionView extends ExecutionView<ResponseEntity<?>> {

    /**
     * Retrieves the {@link RestClient} associated with the execution.
     *
     * @return The {@link RestClient} responsible for the execution.
     */
    RestClient client();

    /**
     * Retrieves the name of the {@link RestClient} associated with the execution.
     *
     * @return The name of the client.
     */
    String clientName();

    /**
     * Retrieves the {@link Request} associated with the execution.
     *
     * @return The {@link Request} that was executed.
     */
    Request request();

    /**
     * Retrieves the current {@link ContextView} associated with the execution.
     *
     * @return The {@link ContextView} of the execution.
     */
    ContextView context();
}

