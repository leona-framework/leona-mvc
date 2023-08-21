package com.sylvona.leona.mvc.client.logging;

import com.sylvona.leona.mvc.client.ClientExecutionView;
import com.sylvona.leona.mvc.components.MvcLeonaConstants;
import com.sylvona.leona.mvc.components.MdcSupplier;
import org.slf4j.MDC.MDCCloseable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * The {@code MdcAwareClientLogger} interface extends the {@link ClientLogger} interface and introduces MDC (Mapped Diagnostic Context) aware logging for client executions.
 * Classes implementing this interface can log client executions while also managing MDC context during logging.
 * MDC context is used to store diagnostic information that can be included in log messages.
 *
 * <p>This interface provides a default method to log client executions along with MDC context handling. It accepts an {@link MdcSupplier}
 * to populate MDC context before logging and clear it afterward. The {@code MdcAwareClientLogger} also overrides the default {@link ClientLogger}
 * filter method to enable MDC context-based logging.
 */
public interface MdcAwareClientLogger extends ClientLogger {
    /**
     * Logs a client execution view along with MDC context handling. Under the default implementation, the MDC context is populated with values from the provided
     * {@link MdcSupplier}, and the context is cleared after logging.
     *
     * @param executionView   The client execution view to be logged.
     * @param contextSupplier The supplier of MDC context values.
     */
    default void log(ClientExecutionView executionView, MdcSupplier contextSupplier) {
        List<MDCCloseable> closeables = contextSupplier.fillContext();
        log(executionView);
        closeables.forEach(MDCCloseable::close);
    }

    /**
     * Overrides the {@link ClientLogger} filter method to enable MDC context-based logging.
     * If an {@link MdcSupplier} is present in the client execution view's context, it populates the MDC context
     * before logging and clears it afterward. Otherwise, it performs regular logging without MDC context handling.
     *
     * @param clientExecutionView The client execution view.
     * @param response            The ResponseEntity representing the response.
     * @param <T>                 The type of the response body.
     * @return The filtered ResponseEntity.
     */
    @Override
    default <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, ResponseEntity<T> response) {
        Optional<MdcSupplier> contextSupplier = clientExecutionView.context().getOrEmpty(MvcLeonaConstants.MDC_CAPTURE_KEY);
        contextSupplier.ifPresentOrElse(cs -> log(clientExecutionView, cs), () -> log(clientExecutionView));
        return response;
    }
}
