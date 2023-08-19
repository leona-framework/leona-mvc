package com.sylvona.leona.mvc.client;

import com.sylvona.leona.core.commons.Priority;

@FunctionalInterface
public interface PreExchangeExecutionFilter {
    Request filter(Request request);

    default Priority preExecutionPriority() {
        return Priority.NORMAL;
    }
}