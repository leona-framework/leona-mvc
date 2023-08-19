package com.sylvona.leona.mvc.services.logging;

import com.sylvona.leona.mvc.services.ServiceExecutionFilter;
import com.sylvona.leona.mvc.services.ServiceExecutionView;

public interface ServiceLogger extends ServiceExecutionFilter {
    void log(ServiceExecutionView<?> executionView);

    @Override
    default void afterExecution(ServiceExecutionView<?> executionView) {
        log(executionView);
    }
}