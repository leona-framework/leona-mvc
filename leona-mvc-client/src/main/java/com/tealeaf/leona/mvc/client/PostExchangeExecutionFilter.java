package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.core.commons.Priority;
import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;

@FunctionalInterface
public interface PostExchangeExecutionFilter {
    @Nullable <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, @Nullable ResponseEntity<T> response);

    default Priority postExecutionPriority() {
        return Priority.NORMAL;
    }
}
