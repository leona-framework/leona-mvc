package org.lyora.leona.mvc.client;

import org.lyora.leona.core.commons.Priority;

@FunctionalInterface
public interface PreExchangeExecutionFilter {
    Request filter(Request request);

    default Priority preExecutionPriority() {
        return Priority.NORMAL;
    }
}
