package com.sylvona.leona.mvc.client.logging;

import com.sylvona.leona.mvc.client.ClientExecutionView;
import lombok.Data;
import org.slf4j.event.Level;

import java.util.function.Function;
import java.util.function.Predicate;

@Data
public class LoggerConfiguration {
    private boolean enabled = true;
    private Class<? extends ClientLogger> clientLogger;
    private Class<? extends Predicate<ClientExecutionView>> logPredicate;
    private Class<? extends Function<ClientExecutionView, String>> messageSupplier;
    private Level level = Level.INFO;
}