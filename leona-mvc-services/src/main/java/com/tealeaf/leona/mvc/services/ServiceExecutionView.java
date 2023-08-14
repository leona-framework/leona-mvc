package com.tealeaf.leona.mvc.services;

import com.tealeaf.leona.mvc.components.containers.Context;
import com.tealeaf.leona.mvc.components.containers.ContextView;
import com.tealeaf.leona.mvc.components.containers.ExecutionView;

public interface ServiceExecutionView<T> extends ExecutionView<T> {
    ServiceMetadata metadata();
    Context context();
}
