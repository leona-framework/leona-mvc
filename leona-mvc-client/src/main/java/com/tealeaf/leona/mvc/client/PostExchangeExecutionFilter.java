package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.mvc.components.Priority;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;

@FunctionalInterface
public interface PostExchangeExecutionFilter {
    @Nullable <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, @Nullable ResponseEntity<T> response);

    default Priority postExecutionPriority() {
        return Priority.NORMAL;
    }
}
