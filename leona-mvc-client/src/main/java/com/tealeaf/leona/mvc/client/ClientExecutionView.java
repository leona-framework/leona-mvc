package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.components.containers.ContextView;
import com.tealeaf.leona.mvc.components.containers.ExecutionView;
import org.springframework.http.ResponseEntity;

public interface ClientExecutionView extends ExecutionView<ResponseEntity<?>> {
    ClientExecuter client();
    String clientName();
    Request request();
    ContextView context();
}
