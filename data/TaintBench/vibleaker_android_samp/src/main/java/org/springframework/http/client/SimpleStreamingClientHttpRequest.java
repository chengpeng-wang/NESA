package org.springframework.http.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map.Entry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;

final class SimpleStreamingClientHttpRequest extends AbstractClientHttpRequest {
    private OutputStream body;
    private final int chunkSize;
    private final HttpURLConnection connection;
    private final boolean outputStreaming;
    private final boolean reuseConnection;

    SimpleStreamingClientHttpRequest(HttpURLConnection connection, int chunkSize, boolean outputStreaming, boolean reuseConnection) {
        this.connection = connection;
        this.chunkSize = chunkSize;
        this.outputStreaming = outputStreaming;
        this.reuseConnection = reuseConnection;
    }

    public HttpMethod getMethod() {
        return HttpMethod.valueOf(this.connection.getRequestMethod());
    }

    public URI getURI() {
        try {
            return this.connection.getURL().toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not get HttpURLConnection URI: " + ex.getMessage(), ex);
        }
    }

    /* access modifiers changed from: protected */
    public OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
        if (this.body == null) {
            if (this.outputStreaming) {
                int contentLength = (int) headers.getContentLength();
                if (contentLength >= 0) {
                    this.connection.setFixedLengthStreamingMode(contentLength);
                } else {
                    this.connection.setChunkedStreamingMode(this.chunkSize);
                }
            }
            if (!this.reuseConnection) {
                headers.setConnection("close");
            }
            writeHeaders(headers);
            this.connection.connect();
            this.body = this.connection.getOutputStream();
        }
        return StreamUtils.nonClosing(this.body);
    }

    private void writeHeaders(HttpHeaders headers) {
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = (String) entry.getKey();
            for (String headerValue : (List) entry.getValue()) {
                this.connection.addRequestProperty(headerName, headerValue);
            }
        }
    }

    /* access modifiers changed from: protected */
    public ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
        try {
            if (this.body != null) {
                this.body.close();
            } else {
                writeHeaders(headers);
                this.connection.connect();
            }
        } catch (IOException e) {
        }
        return new SimpleClientHttpResponse(this.connection);
    }
}
