package com.sylvona.leona.mvc.services;

import org.slf4j.Logger;

/**
 * ServiceMetadata is a record that encapsulates metadata related to a service component.
 * It holds references to the service itself, a logger instance, and information about the service name and execution target.
 *
 * @param service         The service component associated with this metadata.
 * @param logger          The logger instance used for logging related to the service.
 * @param serviceName     The name of the service.
 * @param executionTarget The execution target of the service.
 *
 * @see AsyncService
 * @see SynchronousService
 */
public record ServiceMetadata(ServiceComponent service, Logger logger, String serviceName, String executionTarget) {
}
