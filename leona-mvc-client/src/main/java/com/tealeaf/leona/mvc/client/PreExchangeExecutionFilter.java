package com.tealeaf.leona.mvc.client;

import com.tealeaf.leona.core.commons.Priority;

@FunctionalInterface
public interface PreExchangeExecutionFilter {
    Request filter(Request request);

    default Priority preExecutionPriority() {
        return Priority.NORMAL;
    }
}
