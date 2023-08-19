package com.sylvona.leona.mvc.client;

import com.sylvona.leona.mvc.components.containers.ContextView;
import com.sylvona.leona.mvc.components.containers.ExecutionView;
import org.springframework.http.ResponseEntity;

public interface ClientExecutionView extends ExecutionView<ResponseEntity<?>> {
    ClientExecuter client();
    String clientName();
    Request request();
    ContextView context();
}
