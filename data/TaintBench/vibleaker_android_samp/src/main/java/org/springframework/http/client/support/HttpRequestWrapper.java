package org.springframework.http.client.support;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.util.Assert;

public class HttpRequestWrapper implements HttpRequest {
    private final HttpRequest request;

    public HttpRequestWrapper(HttpRequest request) {
        Assert.notNull(request, "'request' must not be null");
        this.request = request;
    }

    public HttpRequest getRequest() {
        return this.request;
    }

    public HttpMethod getMethod() {
        return this.request.getMethod();
    }

    public URI getURI() {
        return this.request.getURI();
    }

    public HttpHeaders getHeaders() {
        return this.request.getHeaders();
    }
}
