package com.tealeaf.leona.mvc.services.logging;

import com.tealeaf.leona.mvc.services.ServiceExecutionFilter;
import com.tealeaf.leona.mvc.services.ServiceExecutionView;
import org.slf4j.Logger;

public interface ServiceLogger extends ServiceExecutionFilter {
    void log(ServiceExecutionView<?> executionView);

    @Override
    default void afterExecution(ServiceExecutionView<?> executionView) {
        log(executionView);
    }
}