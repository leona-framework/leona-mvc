package com.tealeaf.leona.mvc.flow;

import com.tealeaf.leona.mvc.components.EventType;
import com.tealeaf.leona.mvc.components.MdcLoggingConstants;
import com.tealeaf.leona.mvc.components.captures.DefaultCapturePlan;

public class DefaultOnRequestExitCapturePlan extends DefaultCapturePlan<InterceptedRequestView> implements OnRequestExitCapturePlan {
    public DefaultOnRequestExitCapturePlan() {
        execution(MdcLoggingConstants.EVENT_TYPE).capture(i -> EventType.EXIT);
        execution(MdcLoggingConstants.STATUS_CODE).capture(irv -> irv.getResponse().getStatus());
        execution(MdcLoggingConstants.RESPONSE_TIME).capture(irv -> irv.getElapsedTime(true).toMillis());
    }
}
