package org.springframework.http.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

final class BufferingClientHttpRequestWrapper extends AbstractBufferingClientHttpRequest {
    private final ClientHttpRequest request;

    BufferingClientHttpRequestWrapper(ClientHttpRequest request) {
        Assert.notNull(request, "'request' must not be null");
        this.request = request;
    }

    public HttpMethod getMethod() {
        return this.request.getMethod();
    }

    public URI getURI() {
        return this.request.getURI();
    }

    /* access modifiers changed from: protected */
    public ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
        OutputStream body = this.request.getBody();
        this.request.getHeaders().putAll(headers);
        StreamUtils.copy(bufferedOutput, body);
        return new BufferingClientHttpResponseWrapper(this.request.execute());
    }
}
