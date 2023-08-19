package org.lyora.leona.mvc.flow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.lyora.leona.mvc.components.captures.PersistentCapturer;
import org.lyora.leona.mvc.flow.serializers.HttpServletRequestCapturerDeserializer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize(using = HttpServletRequestCapturerDeserializer.class)
public class HttpServletRequestCapturer extends PersistentCapturer<HttpServletRequest> {
    private String headerName;
    private String contextKey;

    public HttpServletRequestCapturer(String headerName, String contextKey, boolean isPersistent) {
        super(null, isPersistent);
        this.headerName = headerName;
        this.contextKey = contextKey;
    }

    public HttpServletRequestCapturer(String headerName, String contextKey) {
        this(headerName, contextKey, false);
    }

    public HttpServletRequestCapturer(String headerAndContextName) {
        this(headerAndContextName, headerAndContextName);
    }

    @Override
    public Object apply(HttpServletRequest servletRequest) {
        return servletRequest.getHeader(headerName);
    }

    @Override
    public boolean isCaptureable(HttpServletRequest item) {
        return item.getHeader(headerName) != null && super.isCaptureable(item);
    }
}
