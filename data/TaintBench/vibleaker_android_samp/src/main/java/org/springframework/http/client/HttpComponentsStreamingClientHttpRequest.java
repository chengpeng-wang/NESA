package org.springframework.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.http.StreamingHttpOutputMessage.Body;

final class HttpComponentsStreamingClientHttpRequest extends AbstractClientHttpRequest implements StreamingHttpOutputMessage {
    private Body body;
    private final CloseableHttpClient httpClient;
    private final HttpContext httpContext;
    private final HttpUriRequest httpRequest;

    private static class StreamingHttpEntity implements HttpEntity {
        private final Body body;
        private final HttpHeaders headers;

        public StreamingHttpEntity(HttpHeaders headers, Body body) {
            this.headers = headers;
            this.body = body;
        }

        public boolean isRepeatable() {
            return false;
        }

        public boolean isChunked() {
            return false;
        }

        public long getContentLength() {
            return this.headers.getContentLength();
        }

        public Header getContentType() {
            MediaType contentType = this.headers.getContentType();
            return contentType != null ? new BasicHeader(HttpHeaders.CONTENT_TYPE, contentType.toString()) : null;
        }

        public Header getContentEncoding() {
            String contentEncoding = this.headers.getFirst(HttpHeaders.CONTENT_ENCODING);
            return contentEncoding != null ? new BasicHeader(HttpHeaders.CONTENT_ENCODING, contentEncoding) : null;
        }

        public InputStream getContent() throws IOException, IllegalStateException {
            throw new IllegalStateException("No content available");
        }

        public void writeTo(OutputStream outputStream) throws IOException {
            this.body.writeTo(outputStream);
        }

        public boolean isStreaming() {
            return true;
        }

        @Deprecated
        public void consumeContent() throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    HttpComponentsStreamingClientHttpRequest(CloseableHttpClient httpClient, HttpUriRequest httpRequest, HttpContext httpContext) {
        this.httpClient = httpClient;
        this.httpRequest = httpRequest;
        this.httpContext = httpContext;
    }

    public HttpMethod getMethod() {
        return HttpMethod.valueOf(this.httpRequest.getMethod());
    }

    public URI getURI() {
        return this.httpRequest.getURI();
    }

    public void setBody(Body body) {
        assertNotExecuted();
        this.body = body;
    }

    /* access modifiers changed from: protected */
    public OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
        throw new UnsupportedOperationException("getBody not supported");
    }

    /* access modifiers changed from: protected */
    public ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
        HttpComponentsClientHttpRequest.addHeaders(this.httpRequest, headers);
        if ((this.httpRequest instanceof HttpEntityEnclosingRequest) && this.body != null) {
            this.httpRequest.setEntity(new StreamingHttpEntity(getHeaders(), this.body));
        }
        return new HttpComponentsClientHttpResponse(this.httpClient.execute(this.httpRequest, this.httpContext));
    }
}
