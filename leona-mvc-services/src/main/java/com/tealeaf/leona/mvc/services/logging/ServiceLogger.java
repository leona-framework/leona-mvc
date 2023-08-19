package org.lyora.leona.mvc.services.logging;

import org.lyora.leona.mvc.services.ServiceExecutionFilter;
import org.lyora.leona.mvc.services.ServiceExecutionView;
import org.slf4j.Logger;

public interface ServiceLogger extends ServiceExecutionFilter {
    void log(ServiceExecutionView<?> executionView);

    @Override
    default void afterExecution(ServiceExecutionView<?> executionView) {
        log(executionView);
    }
}