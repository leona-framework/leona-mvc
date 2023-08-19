package org.lyora.leona.mvc.client;

import org.lyora.leona.mvc.client.logging.ClientCapturePlan;
import org.lyora.leona.mvc.components.EventType;
import org.lyora.leona.mvc.components.MdcLoggingConstants;
import org.lyora.leona.mvc.components.captures.DefaultCapturePlan;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

class MdcClientCaptures extends DefaultCapturePlan<ClientExecutionView> implements ClientCapturePlan {
    private static final int ERROR_STATUS_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public MdcClientCaptures() {
        execution(MdcLoggingConstants.BACKEND_NAME).capture(ClientExecutionView::clientName);
        execution(MdcLoggingConstants.EVENT_TYPE).capture(i -> EventType.SERVICE);
        execution(MdcLoggingConstants.RESPONSE_TIME).capture(i -> i.executionTime().toMillis());
        execution(MdcLoggingConstants.STATUS_CODE).capture(i -> i.produce(response -> response.getStatusCode().value(), this::getStatusCodeErrorValue));
        execution(MdcLoggingConstants.EXECUTION_TARGET).capture(i -> i.request().getUriString());
    }

    private int getStatusCodeErrorValue(Throwable exception) {
        if (exception instanceof HttpStatusCodeException statusCodeException) return statusCodeException.getStatusCode().value();
        return ERROR_STATUS_CODE;
    }
}
