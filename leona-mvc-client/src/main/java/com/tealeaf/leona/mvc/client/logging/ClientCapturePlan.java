package org.lyora.leona.mvc.client.logging;

import org.lyora.leona.mvc.client.ClientExecuter;
import org.lyora.leona.mvc.client.ClientExecutionView;
import org.lyora.leona.mvc.components.captures.CapturePlan;
import org.lyora.leona.mvc.components.captures.CaptureRepository;

public interface ClientCapturePlan extends CapturePlan<ClientExecutionView>, CaptureRepository<ClientExecutionView> {
    default void configureFor(ClientExecuter clientExecuter) {}
}
