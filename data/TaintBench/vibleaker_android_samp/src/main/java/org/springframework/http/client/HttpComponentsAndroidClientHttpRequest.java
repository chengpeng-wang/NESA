package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Deprecated
final class HttpComponentsAndroidClientHttpRequest extends AbstractBufferingClientHttpRequest {
    private final HttpClient httpClient;
    private final HttpContext httpContext;
    private final HttpUriRequest httpRequest;

    public HttpComponentsAndroidClientHttpRequest(HttpClient httpClient, HttpUriRequest httpRequest, HttpContext httpContext) {
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

    public ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = (String) entry.getKey();
            if (!(headerName.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH) || headerName.equalsIgnoreCase(HttpHeaders.TRANSFER_ENCODING))) {
                for (String headerValue : (List) entry.getValue()) {
                    this.httpRequest.addHeader(headerName, headerValue);
                }
            }
        }
        if (this.httpRequest instanceof HttpEntityEnclosingRequest) {
            this.httpRequest.setEntity(new ByteArrayEntity(bufferedOutput));
        }
        return new HttpComponentsAndroidClientHttpResponse(this.httpClient.execute(this.httpRequest, this.httpContext));
    }
}
