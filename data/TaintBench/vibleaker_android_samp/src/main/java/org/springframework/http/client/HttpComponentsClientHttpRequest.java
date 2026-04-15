package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntityHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

final class HttpComponentsClientHttpRequest extends AbstractBufferingClientHttpRequest {
    private final CloseableHttpClient httpClient;
    private final HttpContext httpContext;
    private final HttpUriRequest httpRequest;

    HttpComponentsClientHttpRequest(CloseableHttpClient httpClient, HttpUriRequest httpRequest, HttpContext httpContext) {
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

    /* access modifiers changed from: 0000 */
    public HttpContext getHttpContext() {
        return this.httpContext;
    }

    /* access modifiers changed from: protected */
    public ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
        addHeaders(this.httpRequest, headers);
        if (this.httpRequest instanceof HttpEntityEnclosingRequest) {
            this.httpRequest.setEntity(new ByteArrayEntityHC4(bufferedOutput));
        }
        return new HttpComponentsClientHttpResponse(this.httpClient.execute(this.httpRequest, this.httpContext));
    }

    static void addHeaders(HttpUriRequest httpRequest, HttpHeaders headers) {
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = (String) entry.getKey();
            if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) {
                httpRequest.addHeader(headerName, StringUtils.collectionToDelimitedString((Collection) entry.getValue(), "; "));
            } else if (!(HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(headerName) || HttpHeaders.TRANSFER_ENCODING.equalsIgnoreCase(headerName))) {
                for (String headerValue : (List) entry.getValue()) {
                    httpRequest.addHeader(headerName, headerValue);
                }
            }
        }
    }
}
