package org.lyora.leona.mvc.flow;

import org.lyora.leona.mvc.components.EventType;
import org.lyora.leona.mvc.components.MdcLoggingConstants;
import org.lyora.leona.mvc.components.captures.DefaultCapturePlan;

class DefaultOnRequestExitCapturePlan extends DefaultCapturePlan<InterceptedRequestView> implements OnRequestExitCapturePlan {
    public DefaultOnRequestExitCapturePlan() {
        execution(MdcLoggingConstants.EVENT_TYPE).capture(i -> EventType.EXIT);
        execution(MdcLoggingConstants.STATUS_CODE).capture(irv -> irv.getResponse().getStatus());
        execution(MdcLoggingConstants.RESPONSE_TIME).capture(irv -> irv.getElapsedTime(true).toMillis());
    }
}
