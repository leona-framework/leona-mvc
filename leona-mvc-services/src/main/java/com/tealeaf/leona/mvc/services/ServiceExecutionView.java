package org.lyora.leona.mvc.services;

import org.lyora.leona.mvc.components.containers.Context;
import org.lyora.leona.mvc.components.containers.ContextView;
import org.lyora.leona.mvc.components.containers.ExecutionView;

public interface ServiceExecutionView<T> extends ExecutionView<T> {
    ServiceMetadata metadata();
    Context context();
}
