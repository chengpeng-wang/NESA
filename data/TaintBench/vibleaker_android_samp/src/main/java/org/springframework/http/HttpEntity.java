package org.springframework.http;

import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

public class HttpEntity<T> {
    public static final HttpEntity EMPTY = new HttpEntity();
    private final T body;
    private final HttpHeaders headers;

    protected HttpEntity() {
        this(null, null);
    }

    public HttpEntity(T body) {
        this(body, null);
    }

    public HttpEntity(MultiValueMap<String, String> headers) {
        this(null, headers);
    }

    public HttpEntity(T body, MultiValueMap<String, String> headers) {
        this.body = body;
        HttpHeaders tempHeaders = new HttpHeaders();
        if (headers != null) {
            tempHeaders.putAll(headers);
        }
        this.headers = HttpHeaders.readOnlyHttpHeaders(tempHeaders);
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public T getBody() {
        return this.body;
    }

    public boolean hasBody() {
        return this.body != null;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HttpEntity)) {
            return false;
        }
        HttpEntity<?> otherEntity = (HttpEntity) other;
        if (ObjectUtils.nullSafeEquals(this.headers, otherEntity.headers) && ObjectUtils.nullSafeEquals(this.body, otherEntity.body)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (ObjectUtils.nullSafeHashCode(this.headers) * 29) + ObjectUtils.nullSafeHashCode(this.body);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        if (this.body != null) {
            builder.append(this.body);
            if (this.headers != null) {
                builder.append(',');
            }
        }
        if (this.headers != null) {
            builder.append(this.headers);
        }
        builder.append('>');
        return builder.toString();
    }
}
