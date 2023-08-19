package com.sylvona.leona.mvc.client.retry;

import com.sylvona.leona.mvc.client.ClientExecuter;
import com.sylvona.leona.mvc.client.ClientExecutionView;
import com.sylvona.leona.mvc.client.Request;
import com.sylvona.leona.mvc.components.containers.ContextView;
import io.github.resilience4j.retry.event.RetryOnRetryEvent;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

record ClientEchoedRetryView(ClientExecutionView view, RetryOnRetryEvent event, Retryer retryer) implements RetryView {

    @Override
    public ClientExecuter client() {
        return view.client();
    }

    @Override
    public String clientName() {
        return view.clientName();
    }

    @Override
    public Request request() {
        return view.request();
    }

    @Override
    public ContextView context() {
        return view.context();
    }

    @Override
    public RetryOnRetryEvent event() {
        return event;
    }

    @Override
    public Retryer retryer() {
        return retryer;
    }

    @Override
    public ResponseEntity<?> result() {
        return view.result();
    }

    @Override
    public Throwable error() {
        return view.error();
    }

    @Override
    public Duration executionTime() {
        return view.executionTime();
    }
}
