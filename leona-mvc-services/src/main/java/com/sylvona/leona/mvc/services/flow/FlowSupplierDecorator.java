package com.sylvona.leona.mvc.services.flow;

import com.sylvona.leona.mvc.components.Decorators;
import com.sylvona.leona.mvc.components.containers.Context;
import com.sylvona.leona.mvc.flow.InterceptedRequestView;
import com.sylvona.leona.mvc.services.ExecutionType;
import com.sylvona.leona.mvc.services.ServiceExecutionFilter;
import com.sylvona.leona.mvc.services.ServiceMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.function.Supplier;

@ConditionalOnClass(InterceptedRequestView.class)
public class FlowSupplierDecorator implements ServiceExecutionFilter {
    @Override
    public <T> Supplier<T> beforeExecution(ServiceMetadata serviceMetadata, Supplier<T> supplier, Context context) {
        if (context.get(ExecutionType.class) != ExecutionType.ASYNCHRONOUS) return supplier;
        return Decorators.contextAware(supplier, context);
    }
}
