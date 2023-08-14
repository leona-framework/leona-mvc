package com.tealeaf.leona.mvc.services.flow;

import com.tealeaf.leona.mvc.components.Decorators;
import com.tealeaf.leona.mvc.components.containers.Context;
import com.tealeaf.leona.mvc.services.ServiceExecutionFilter;
import com.tealeaf.leona.mvc.services.ServiceMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.function.Supplier;

@ConditionalOnClass(name = "com.tealeaf.leona.mvc.flow.LeonaFlowAutoConfiguration")
public class FlowSupplierDecorator implements ServiceExecutionFilter {
    @Override
    public <T> Supplier<T> beforeExecution(ServiceMetadata serviceMetadata, Supplier<T> supplier, Context context) {
        return Decorators.contextAware(supplier, context);
    }
}
