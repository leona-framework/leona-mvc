package com.sylvona.leona.mvc.flow;

import com.sylvona.leona.mvc.components.EventType;
import com.sylvona.leona.mvc.components.MdcLoggingConstants;
import com.sylvona.leona.mvc.components.captures.DefaultCapturePlan;

class DefaultOnRequestExitCapturePlan extends DefaultCapturePlan<InterceptedRequestView> implements OnRequestExitCapturePlan {
    public DefaultOnRequestExitCapturePlan() {
        execution(MdcLoggingConstants.EVENT_TYPE).capture(i -> EventType.EXIT);
        execution(MdcLoggingConstants.STATUS_CODE).capture(irv -> irv.getResponse().getStatus());
        execution(MdcLoggingConstants.RESPONSE_TIME).capture(irv -> irv.getElapsedTime(true).toMillis());
    }
}
