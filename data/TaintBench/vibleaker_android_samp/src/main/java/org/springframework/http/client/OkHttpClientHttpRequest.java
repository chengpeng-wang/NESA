package org.springframework.http.client;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

class OkHttpClientHttpRequest extends AbstractBufferingClientHttpRequest implements ClientHttpRequest {
    private static final String PROXY_AUTH_ERROR = "Received HTTP_PROXY_AUTH (407) code while not using proxy";
    private final OkHttpClient client;
    private final HttpMethod method;
    private final URI uri;

    public OkHttpClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
        this.client = client;
        this.uri = uri;
        this.method = method;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public URI getURI() {
        return this.uri;
    }

    /* access modifiers changed from: protected */
    public ClientHttpResponse executeInternal(HttpHeaders headers, byte[] content) throws IOException {
        RequestBody body = content.length > 0 ? RequestBody.create(getContentType(headers), content) : null;
        Builder builder = new Builder().url(this.uri.toURL()).method(this.method.name(), body);
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = (String) entry.getKey();
            for (String headerValue : (List) entry.getValue()) {
                builder.addHeader(headerName, headerValue);
            }
        }
        try {
            return new OkHttpClientHttpResponse(this.client.newCall(builder.build()).execute());
        } catch (ProtocolException e) {
            if (PROXY_AUTH_ERROR.equals(e.getMessage())) {
                throw new HttpClientErrorException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, HttpStatus.PROXY_AUTHENTICATION_REQUIRED.getReasonPhrase());
            }
            throw e;
        }
    }

    private MediaType getContentType(HttpHeaders headers) {
        String rawContentType = headers.getFirst(HttpHeaders.CONTENT_TYPE);
        return StringUtils.hasText(rawContentType) ? MediaType.parse(rawContentType) : null;
    }
}
