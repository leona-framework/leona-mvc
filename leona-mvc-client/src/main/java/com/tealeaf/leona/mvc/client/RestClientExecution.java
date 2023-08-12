package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.components.containers.ContextView;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

@Accessors(fluent = true)
@Getter
@Setter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
class RestClientExecution implements ClientExecutionView {
    private final ClientExecuter client;
    private final Request request;
    private final ContextView context;
    private Duration executionTime;
    @Nullable private ResponseEntity<?> result;
    @Nullable private Throwable error;

    @Override
    public String clientName() {
        return client.getName();
    }
}
