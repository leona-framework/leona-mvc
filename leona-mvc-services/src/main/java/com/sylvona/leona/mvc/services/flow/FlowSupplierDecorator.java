package com.sylvona.leona.mvc.services.flow;

import com.sylvona.leona.mvc.components.Decorators;
import com.sylvona.leona.mvc.components.containers.Context;
import com.sylvona.leona.mvc.flow.InterceptedRequestView;
import com.sylvona.leona.mvc.services.ExecutionType;
import com.sylvona.leona.mvc.services.ServiceExecutionFilter;
import com.sylvona.leona.mvc.services.ServiceMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.function.Supplier;

/**
 * A service execution filter that ensures the correct passage of current context into asynchronous executions.
 * <p>
 * This filter is only enabled with the presence of the leona-app-flow library and functionally does nothing when called
 * from a synchronous scope.
 */
@ConditionalOnClass(InterceptedRequestView.class)
public class FlowSupplierDecorator implements ServiceExecutionFilter {
    /**
     * Enhances asynchronous service method executions with contextual information.
     * <p>
     * This method is invoked before the execution of an asynchronous service method.
     * It checks if the execution type is asynchronous and, if true, decorates the supplied {@link Supplier}
     * with contextual awareness using the {@link Decorators#contextAware(Supplier, Context)} method.
     * This contextual awareness allows for the manipulation of the execution flow within the provided context.
     *
     * @param serviceMetadata The metadata of the service method being executed.
     * @param supplier        The original supplier representing the asynchronous method execution.
     * @param context         The execution context containing contextual information.
     * @param <T>             The type of the result produced by the asynchronous execution.
     * @return A decorated supplier with contextual awareness for the asynchronous method execution.
     */
    @Override
    public <T> Supplier<T> beforeExecution(ServiceMetadata serviceMetadata, Supplier<T> supplier, Context context) {
        if (context.get(ExecutionType.class) != ExecutionType.ASYNCHRONOUS) {
            return supplier; // Do not modify synchronous execution
        }

        // Decorate the supplier to propagate the current thread context
        return Decorators.contextAware(supplier, context);
    }
}
