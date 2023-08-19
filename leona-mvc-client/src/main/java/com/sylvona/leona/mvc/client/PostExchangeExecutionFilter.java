package com.sylvona.leona.mvc.client;

import jakarta.annotation.Nullable;
import com.sylvona.leona.core.commons.Priority;
import org.springframework.http.ResponseEntity;

@FunctionalInterface
public interface PostExchangeExecutionFilter {
    @Nullable <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, @Nullable ResponseEntity<T> response);

    default Priority postExecutionPriority() {
        return Priority.NORMAL;
    }
}
