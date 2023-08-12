package com.tealeaf.leona.mvc.flow;

import brave.Tracer;
import com.tealeaf.leona.mvc.components.captures.CaptureElement;
import com.tealeaf.leona.mvc.components.captures.CapturePlan;
import com.tealeaf.leona.mvc.components.captures.PersistentCapturer;
import com.tealeaf.leona.mvc.components.containers.Context;
import com.tealeaf.leona.mvc.components.containers.ThreadContext;
import com.tealeaf.leona.mvc.components.utils.ClassConstructor;
import com.tealeaf.leona.mvc.flow.LeonaFlowAutoConfigurationSource.RequestInterceptorConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SharedContextRequestInterceptor implements LeonaFlowRequestInterceptor {
    private final Logger log = LoggerFactory.getLogger(LeonaFlowRequestInterceptor.class);
    private final Context context = new ThreadContext();
    private final OnRequestEntryCapturePlan entryCapturePlan;
    private final OnRequestExitCapturePlan exitCapturePlan;
    private final Function<HttpServletRequest, String> entryMessage;
    private final Function<InterceptedRequestView, String> exitMessage;
    private final Level level;

    public SharedContextRequestInterceptor(RequestInterceptorConfig requestInterceptorConfig, OnRequestEntryCapturePlan entryCapturePlan, OnRequestExitCapturePlan exitCapturePlan, Tracer tracer) {
        List<HttpServletRequestCapturer> configCapturers = requestInterceptorConfig.getRequestCapturers();

        List<CaptureElement<HttpServletRequest>> captureElements = new ArrayList<>(entryCapturePlan.getCaptures());

        if (!requestInterceptorConfig.isUseDefaultCapturers()) {
            captureElements.clear();
        }

        if (configCapturers != null) {
            captureElements.addAll(configCapturers.stream().map(c -> new CaptureElement<>(c, c.getHeaderName())).toList());
        }

        this.entryCapturePlan = new DefaultOnRequestEntryCapturePlan(captureElements, tracer);
        this.exitCapturePlan = exitCapturePlan;
        this.level = requestInterceptorConfig.getLevel();

        Class<? extends Function<HttpServletRequest, String>> entryMessageClass = requestInterceptorConfig.getOnEntryMessage();
        Class<? extends Function<InterceptedRequestView, String>> exitMessageClass = requestInterceptorConfig.getOnExitMessage();
        if (entryMessageClass != null) {
            entryMessage = ClassConstructor.createInstance(entryMessageClass);
        } else {
            entryMessage = req -> "Incoming - %s: %s".formatted(req.getMethod(), req.getServletPath());
        }

        if (exitMessageClass != null) {
            exitMessage = ClassConstructor.createInstance(exitMessageClass);
        } else {
            exitMessage = (irv) -> "Responding - %s: %s".formatted(irv.getRequest().getMethod(), irv.getRequest().getServletPath());
        }
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        InterceptedRequestView requestView = new InterceptedRequestView(System.nanoTime(), request); // Start time

        context.clear();
        context.put(InterceptedRequestView.class, requestView);

        List<MDCCloseable> closeables = createEntryCloseables(requestView);
        log.atLevel(level).log(entryMessage.apply(request));
        closeables.forEach(MDCCloseable::close);

        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        InterceptedRequestView requestView = context.get(InterceptedRequestView.class);
        requestView.getElapsedTime(true);
        requestView.setResponse(response);

        List<MDCCloseable> closeables = createExitCloseables(requestView);
        log.atLevel(level).log(exitMessage.apply(requestView));
        closeables.forEach(MDCCloseable::close);
        context.clear();
    }

    private List<MDCCloseable> createEntryCloseables(InterceptedRequestView requestView) {
        HttpServletRequest request = requestView.getRequest();

        List<MDCCloseable> closeables = new ArrayList<>();
        for (CaptureElement<HttpServletRequest> captureElement : entryCapturePlan.getCaptures()) {
            CapturePlan.Capturer<HttpServletRequest> capturer = captureElement.capturer();
            if (!capturer.isCaptureable(request)) continue;

            String key = captureElement.name();
            String value = Objects.toString(capturer.apply(request));

            if (!(capturer instanceof PersistentCapturer<HttpServletRequest> persistentCapturer) || !persistentCapturer.isPersistent()) {
                closeables.add(MDC.putCloseable(key, value));
            } else {
                requestView.put(key, value);
                MDC.put(key, value);
            }
        }
        return closeables;
    }

    private List<MDCCloseable> createExitCloseables(InterceptedRequestView requestView) {
        List<MDCCloseable> closeables = new ArrayList<>();
        for (CaptureElement<InterceptedRequestView> captureElement : exitCapturePlan.getCaptures()) {
            CapturePlan.Capturer<InterceptedRequestView> capturer = captureElement.capturer();
            if (!capturer.isCaptureable(requestView)) continue;

            String key = captureElement.name();
            String value = Objects.toString(capturer.apply(requestView));
            closeables.add(MDC.putCloseable(key, value));
        }
        return closeables;
    }
}
