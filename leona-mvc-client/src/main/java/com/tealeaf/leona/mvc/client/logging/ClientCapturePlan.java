package com.tealeaf.leona.mvc.client.logging;

import com.tealeaf.leona.mvc.client.ClientExecuter;
import com.tealeaf.leona.mvc.client.ClientExecutionView;
import com.tealeaf.leona.mvc.components.captures.CapturePlan;
import com.tealeaf.leona.mvc.components.captures.CaptureRepository;

public interface ClientCapturePlan extends CapturePlan<ClientExecutionView>, CaptureRepository<ClientExecutionView> {
    default void configureFor(ClientExecuter clientExecuter) {}
}
