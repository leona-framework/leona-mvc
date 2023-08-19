package org.lyora.leona.mvc.client.logging;

import org.lyora.leona.mvc.client.ClientExecutionView;
import org.lyora.leona.mvc.client.MvcLeonaConstants;
import org.lyora.leona.mvc.components.MdcContextSupplier;
import org.slf4j.MDC.MDCCloseable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface MdcAwareClientLogger extends ClientLogger {
    default void log(ClientExecutionView executionView, MdcContextSupplier contextSupplier) {
        List<MDCCloseable> closeables = contextSupplier.fillContext();
        log(executionView);
        closeables.forEach(MDCCloseable::close);
    }

    @Override
    default <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, ResponseEntity<T> response) {
        Optional<MdcContextSupplier> contextSupplier = clientExecutionView.context().getOrEmpty(MvcLeonaConstants.MDC_CAPTURE_KEY);
        contextSupplier.ifPresentOrElse(cs -> log(clientExecutionView, cs), () -> log(clientExecutionView));
        return response;
    }
}
