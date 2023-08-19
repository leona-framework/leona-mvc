package org.lyora.leona.mvc.client;

import org.lyora.leona.mvc.components.containers.Context;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.time.Duration;

final class ExecutionDurationRequestInterceptor implements ClientHttpRequestInterceptor, ClientInitializationHook {
    private Context clientContext;

    @Override
    public ClientHttpResponse intercept(@NotNull HttpRequest request, @NotNull byte[] body, @NotNull ClientHttpRequestExecution execution) throws IOException {
        long startNanos = System.nanoTime();
        try {
            ClientHttpResponse response = execution.execute(request, body);
            recordExecutionTime(startNanos);
            return response;
        } catch (RuntimeException | IOException exception) {
            recordExecutionTime(startNanos);
            throw exception;
        }
    }

    @Override
    public void onInitialize(ClientExecuter.Modifier clientModifier) {
        clientContext = clientModifier.getContext();

        clientModifier.modifyRestTemplate(restTemplate -> {
            restTemplate.getInterceptors().add(this);
            return restTemplate;
        });
    }

    private void recordExecutionTime(long startTime) {
        Duration executionTime = Duration.ofNanos(System.nanoTime() - startTime);
        clientContext.put(MvcLeonaConstants.REQUEST_DURATION_KEY, executionTime);
    }
}
