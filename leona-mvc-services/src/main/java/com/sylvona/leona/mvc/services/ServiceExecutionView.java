package com.sylvona.leona.mvc.services;

import com.sylvona.leona.mvc.components.containers.Context;
import com.sylvona.leona.mvc.components.containers.ExecutionView;

public interface ServiceExecutionView<T> extends ExecutionView<T> {
    ServiceMetadata metadata();
    Context context();
}
