package com.tealeaf.leona.mvc.services.flow;

import com.tealeaf.leona.mvc.components.Decorators;
import com.tealeaf.leona.mvc.components.containers.Context;
import com.tealeaf.leona.mvc.flow.InterceptedRequestView;
import com.tealeaf.leona.mvc.services.ExecutionType;
import com.tealeaf.leona.mvc.services.ServiceExecutionFilter;
import com.tealeaf.leona.mvc.services.ServiceMetadata;
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
