package com.sylvona.leona.mvc.client.properties;

import com.sylvona.leona.mvc.client.RestClient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import java.time.Duration;

/**
 * Configuration class for defining properties related to the behavior of {@link RestClient} instances.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RestClientConfig {
    /**
     * The default http method used in configuration
     */
    public static final String DEFAULT_HTTP_METHOD = "GET";
    /**
     * The default port used in configuration
     */
    public static final Integer DEFAULT_PORT = 443;
    /**
     * The default accept value used in configuration
     */
    public static final String DEFAULT_ACCEPT = MediaType.ALL_VALUE;
    /**
     * The default content-type used in configuration
     */
    public static final String DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    /**
     * The default retry config used in configuration
     */
    public static final Resilience4JClientRetryConfig DEFAULT_RETRY_CONFIG = new Resilience4JClientRetryConfig();
    /**
     * The default timeout config used in configuration
     */
    public static final ClientTimeoutConfig DEFAULT_TIMEOUT_CONFIG = new ClientTimeoutConfig();

    /** The name of the client. */
    private String clientName;

    /**
     * The host URL of the client, excluding queries. Should not contain queries.
     * @see Pattern
     */
    @Pattern(regexp = "^[^?]*$", message = "host should not contain queries")
    private String host;

    /**
     * The path of the client, excluding queries. Should not contain queries.
     * @see Pattern
     */
    @Pattern(regexp = "^[^?]*$", message = "path should not contain queries")
    private String path;

    /** The port number for the client. */
    private Integer port;

    /**
     * The HTTP method for the client request. Allowed values: GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH.
     * @see Pattern
     */
    @Pattern(flags = Pattern.Flag.CASE_INSENSITIVE, regexp = "^GET|HEAD|POST|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH$")
    private String method;

    /** The "Accept" header value for the client request. */
    private String accept;

    /** The "Content-Type" header value for the client request. */
    private String contentType;

    /** The default headers used in requests made by this client. */
    private MultiValueMap<String, String> headers;

    /** The configuration for client timeout properties. */
    @NotNull
    private ClientTimeoutConfig timeout;

    /** The configuration for client retry properties. */
    @NotNull
    private Resilience4JClientRetryConfig retry;

    /**
     * Converts the method value to an HttpMethod instance.
     *
     * @return The HttpMethod instance representing the configured HTTP method.
     */
    public HttpMethod httpMethod() {
        return HttpMethod.valueOf(method);
    }

    /**
     * Converts the accept value to a MediaType instance.
     *
     * @return The MediaType instance representing the configured accept value.
     */
    public MediaType acceptType() {
        return MediaType.valueOf(accept);
    }

    /**
     * Converts the contentType value to a MediaType instance.
     *
     * @return The MediaType instance representing the configured contentType value.
     */
    public MediaType getContentMedia() {
        return MediaType.valueOf(contentType);
    }

    /**
     * Configuration class for defining timeout properties for RestClient instances.
     */
    @Data
    @MergeCandidate
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class ClientTimeoutConfig {
        /** The read timeout for the client request. */
        private Duration readTimeout;

        /** The connection timeout for the client request. */
        private Duration connectionTimeout;

        /**
         * Merges two instances of ClientTimeoutConfig, prioritizing non-null values from the second instance.
         *
         * @param config1 The first ClientTimeoutConfig instance.
         * @param config2 The second ClientTimeoutConfig instance.
         * @return A merged ClientTimeoutConfig instance.
         */
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

    /**
     * Creates a default RestClientConfig instance with predefined default values.
     *
     * @return The default RestClientConfig instance.
     */
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
