package org.lyora.leona.mvc.services.logging;

import org.lyora.leona.mvc.services.ServiceExecutionView;
import lombok.Data;
import org.slf4j.event.Level;

import java.util.function.Function;
import java.util.function.Predicate;

@Data
public class LoggerConfiguration {
    private boolean enabled = true;
    private Class<? extends ServiceLogger> clientLogger;
    private Class<? extends Predicate<ServiceExecutionView<?>>> logPredicate;
    private Class<? extends Function<ServiceExecutionView<?>, String>> messageSupplier;
    private Level level = Level.INFO;
}