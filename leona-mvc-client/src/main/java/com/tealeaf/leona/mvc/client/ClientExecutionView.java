package org.lyora.leona.mvc.client;

import org.lyora.leona.mvc.components.containers.ContextView;
import org.lyora.leona.mvc.components.containers.ExecutionView;
import org.springframework.http.ResponseEntity;

public interface ClientExecutionView extends ExecutionView<ResponseEntity<?>> {
    ClientExecuter client();
    String clientName();
    Request request();
    ContextView context();
}
