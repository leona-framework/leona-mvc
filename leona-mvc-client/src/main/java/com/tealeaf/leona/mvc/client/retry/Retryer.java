package org.lyora.leona.mvc.client.retry;

import org.lyora.leona.mvc.client.ClientExecuter;
import org.lyora.leona.mvc.client.ClientInitializationHook;
import io.github.resilience4j.retry.Retry;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public interface Retryer extends ClientInitializationHook {
    default <T> ResponseEntity<T> execute(Supplier<ResponseEntity<T>> callable) {
        return getRetry().executeSupplier(callable);
    }
    void onRetry(RetryView retryView);
    boolean isRetryable(ResponseEntity<?> response);
    boolean isRetryable(Throwable throwable);
    Retry getRetry();

    @Override
    default void onInitialize(ClientExecuter.Modifier clientModifier) {}
}
