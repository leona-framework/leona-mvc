package com.sylvona.leona.mvc.client.logging;

import com.sylvona.leona.mvc.client.*;
import com.sylvona.leona.mvc.components.MdcSupplier;
import com.sylvona.leona.core.commons.Priority;
import com.sylvona.leona.mvc.components.MvcLeonaConstants;
import com.sylvona.leona.mvc.components.captures.MdcCaptureFilter;
import com.sylvona.leona.mvc.components.containers.Context;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;

/**
 * The {@code MdcClientCaptureFilter} class is a combination of {@link ClientInitializationHook} and
 * {@link PostExchangeExecutionFilter}, used to capture and populate MDC (Mapped Diagnostic Context) with
 * information from the execution context of a client request.
 */
public class MdcClientCaptureFilter implements ClientInitializationHook, PostExchangeExecutionFilter, MdcCaptureFilter<ClientExecutionView> {
    private final ClientCapturePlan clientCapturePlan;
    private Context context;

    /**
     * Constructs an {@code MdcClientCaptureFilter} instance with the specified client capture plan.
     *
     * @param clientCapturePlan The client capture plan to configure.
     */
    public MdcClientCaptureFilter(ClientCapturePlan clientCapturePlan) {
        this.clientCapturePlan = clientCapturePlan;
    }

    /**
     * Initializes the MDC (Mapped Diagnostic Context) capture filter for the client. This method is called during the
     * initialization phase of the client and configures the capture plan and post-execution filter to capture and populate
     * the MDC with relevant context information.
     *
     * @param clientModifier The modifier for the client, providing access to modify the client's properties.
     */
    @Override
    public void onInitialize(RestClient.Modifier clientModifier) {
        context = clientModifier.getContext();
        clientModifier.getPostExecutionFilters().add(this);
        clientCapturePlan.configureFor(clientModifier.getClient());
    }

    /**
     * Filters the client execution view after the request exchange. This method captures the context information from the
     * client capture plan and associates it with the execution context for MDC population.
     *
     * @param clientExecutionView The execution view representing the client request execution.
     * @param response            The response entity from the request exchange.
     * @param <T>                 The type of the response entity.
     * @return The response entity after applying the filter.
     */
    @Nullable
    @Override
    public <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, @Nullable ResponseEntity<T> response) {
        MdcSupplier closeables = () -> doFilter(clientCapturePlan, clientExecutionView);
        context.put(MvcLeonaConstants.MDC_CAPTURE_KEY, closeables);
        return response;
    }

    @Override
    public Priority postExecutionPriority() {
        return Priority.VERY_HIGH;
    }
}
