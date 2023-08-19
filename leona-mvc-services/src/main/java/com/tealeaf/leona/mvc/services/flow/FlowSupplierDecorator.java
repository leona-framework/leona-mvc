package org.lyora.leona.mvc.services.flow;

import org.lyora.leona.mvc.components.Decorators;
import org.lyora.leona.mvc.components.containers.Context;
import org.lyora.leona.mvc.flow.InterceptedRequestView;
import org.lyora.leona.mvc.services.ExecutionType;
import org.lyora.leona.mvc.services.ServiceExecutionFilter;
import org.lyora.leona.mvc.services.ServiceMetadata;
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
