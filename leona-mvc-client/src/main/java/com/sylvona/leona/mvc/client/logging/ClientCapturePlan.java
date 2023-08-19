package com.sylvona.leona.mvc.client.logging;

import com.sylvona.leona.mvc.client.ClientExecuter;
import com.sylvona.leona.mvc.client.ClientExecutionView;
import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.CaptureRepository;

public interface ClientCapturePlan extends CapturePlan<ClientExecutionView>, CaptureRepository<ClientExecutionView> {
    default void configureFor(ClientExecuter clientExecuter) {}
}
