package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.EventType;
import com.sylvona.leona.mvc.components.MdcLoggingConstants;
import com.sylvona.leona.mvc.components.captures.DefaultCapturePlan;
import com.sylvona.leona.mvc.services.logging.ServiceCapturePlan;
import org.springframework.http.HttpStatus;

class MdcServiceCaptures extends DefaultCapturePlan<ServiceExecutionView<?>> implements ServiceCapturePlan {
    private static final int ERROR_STATUS_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
    private static final int OK_STATUS_CODE = HttpStatus.OK.value();

    public MdcServiceCaptures() {
        execution(MdcLoggingConstants.BACKEND_NAME).capture(sev -> sev.metadata().serviceName());
        execution(MdcLoggingConstants.EVENT_TYPE).capture(i -> EventType.SERVICE);
        execution(MdcLoggingConstants.RESPONSE_TIME).capture(i -> i.executionTime().toMillis());
        execution(MdcLoggingConstants.STATUS_CODE).capture(i -> i.hasRight() ? ERROR_STATUS_CODE :OK_STATUS_CODE);
        contingent(MdcLoggingConstants.EXECUTION_TARGET)
                .condition(sev -> sev.metadata().executionTarget() != null)
                .capture(sev -> sev.metadata().executionTarget());
    }
}
