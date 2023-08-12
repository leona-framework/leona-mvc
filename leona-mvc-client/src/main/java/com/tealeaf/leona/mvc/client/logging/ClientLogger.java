package com.tealeaf.leona.mvc.client.logging;

import com.tealeaf.leona.mvc.client.*;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

public interface ClientLogger extends PostExchangeExecutionFilter, ClientInitializer {
    void log(ClientExecutionView executionView);
    void setLogger(Logger logger);

    @Override
    default <T> ResponseEntity<T> filter(ClientExecutionView clientExecutionView, ResponseEntity<T> response) {
        log(clientExecutionView);
        return response;
    }

    @Override
    default void onInitialize(ClientExecuter.Modifier clientModifier) {
        clientModifier.getPostExecutionFilters().add(this);
        setLogger(clientModifier.getLogger());
    }
}
