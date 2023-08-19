package org.lyora.leona.mvc.client.properties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RestClientConfig {
    public static final String DEFAULT_HTTP_METHOD = "GET";
    public static final Integer DEFAULT_PORT = 443;
    public static final String DEFAULT_ACCEPT = MediaType.ALL_VALUE;
    public static final String DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    public static final Resilience4JClientRetryConfig DEFAULT_RETRY_CONFIG = new Resilience4JClientRetryConfig();
    public static final ClientTimeoutConfig DEFAULT_TIMEOUT_CONFIG = new ClientTimeoutConfig();

    private String clientName;
    @Pattern(regexp = "^[^?]*$", message = "host should not contain queries")
    private String host;
    @Pattern(regexp = "^[^?]*$", message = "path should not contain queries")
    private String path;
    private Integer port;
    @Pattern(flags = Pattern.Flag.CASE_INSENSITIVE, regexp = "^GET|HEAD|POST|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH$")
    private String method;
    private String accept;
    private String contentType;
    @NotNull
    private ClientTimeoutConfig timeout;
    @NotNull
    private Resilience4JClientRetryConfig retry;

    public HttpMethod httpMethod() {
        return HttpMethod.valueOf(method);
    }

    public MediaType acceptType() {
        return MediaType.valueOf(accept);
    }

    public MediaType getContentMedia() {
        return MediaType.valueOf(contentType);
    }

    @Data
    @MergeCandidate
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class ClientTimeoutConfig {
        private Duration readTimeout;
        private Duration connectionTimeout;

        @MergeCandidate.MergeMethod
        public static ClientTimeoutConfig merge(ClientTimeoutConfig config1, ClientTimeoutConfig config2) {
            ClientTimeoutConfig freshConfig = config2.toBuilder().build();

            if (freshConfig.readTimeout == null)
                freshConfig.readTimeout = config1.readTimeout;

            if (freshConfig.connectionTimeout == null)
                freshConfig.connectionTimeout = config1.connectionTimeout;

            return freshConfig;
        }
    }





    public static RestClientConfig defaults() {
        return RestClientConfig.builder()
                .accept(RestClientConfig.DEFAULT_ACCEPT)
                .contentType(RestClientConfig.DEFAULT_CONTENT_TYPE)
                .method(RestClientConfig.DEFAULT_HTTP_METHOD)
                .port(RestClientConfig.DEFAULT_PORT)
                .retry(RestClientConfig.DEFAULT_RETRY_CONFIG)
                .timeout(RestClientConfig.DEFAULT_TIMEOUT_CONFIG)
                .build();
    }

}
