package com.sylvona.leona.mvc.client.logging;

import com.sylvona.leona.mvc.client.RestClient;
import com.sylvona.leona.mvc.client.ClientExecutionView;
import com.sylvona.leona.mvc.components.captures.CapturePlan;
import com.sylvona.leona.mvc.components.captures.CaptureRepository;

/**
 * An interface of {@link CapturePlan} and {@link CaptureRepository} responsible capturing {@link ClientExecutionView}.
 */
public interface ClientCapturePlan extends CapturePlan<ClientExecutionView>, CaptureRepository<ClientExecutionView> {
    /**
     * Configures the capture plan for the given {@link RestClient}
     * @param restClient the {@link RestClient} to configure for
     */
    default void configureFor(RestClient restClient) {}
}
