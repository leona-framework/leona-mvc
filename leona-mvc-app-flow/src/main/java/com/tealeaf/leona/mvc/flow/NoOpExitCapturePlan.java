package org.lyora.leona.mvc.flow;

import org.lyora.leona.mvc.components.captures.DefaultCapturePlan;

class NoOpExitCapturePlan extends DefaultCapturePlan<InterceptedRequestView> implements OnRequestExitCapturePlan {
}
