package org.lyora.leona.mvc.client;
@FunctionalInterface
public interface RequestBuilder {
    Request build(Request.Builder builder);
}
