package com.tealeaf.leona.mvc.client.logging;

import com.tealeaf.leona.mvc.client.*;
import com.tealeaf.leona.mvc.components.MdcContextSupplier;
import com.tealeaf.leona.mvc.components.Priority;
import com.tealeaf.leona.mvc.components.captures.MdcCaptureFilter;
import com.tealeaf.leona.mvc.components.containers.Context;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;

public class MdcClientCaptureFilter implements ClientInitializationHook, PostExchangeExecutionFilter, MdcCaptureFilter<ClientExecutionView> {
    private final ClientCapturePlan clientCapturePlan;
    private Context context;

    public MdcClientCaptureFilter(ClientCapturePlan clientCapturePlan) {
        this.clientCapturePlan = clientCapturePlan;
    }

    @Override
    public void onInitialize(ClientExecuter.Modifier clientModifier) {
        context = clientModifier.getContext();
        clientModifier.getPostExecutionFilters().add(this);
        clientCapturePlan.configureFor(clientModifier.getClient());
    }

    @Nullable
    @Override
    public <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, @Nullable ResponseEntity<T> response) {
        MdcContextSupplier closeables = () -> doFilter(clientCapturePlan, clientExecutionView);
        context.put(MvcLeonaConstants.MDC_CAPTURE_KEY, closeables);
        return response;
    }

    @Override
    public Priority postExecutionPriority() {
        return Priority.VERY_HIGH;
    }
}
