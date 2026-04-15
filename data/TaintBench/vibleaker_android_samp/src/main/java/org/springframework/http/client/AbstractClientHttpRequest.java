package org.springframework.http.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

public abstract class AbstractClientHttpRequest implements ClientHttpRequest {
    private GZIPOutputStream compressedBody;
    private boolean executed = false;
    private final HttpHeaders headers = new HttpHeaders();

    public abstract ClientHttpResponse executeInternal(HttpHeaders httpHeaders) throws IOException;

    public abstract OutputStream getBodyInternal(HttpHeaders httpHeaders) throws IOException;

    public final HttpHeaders getHeaders() {
        return this.executed ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
    }

    public final OutputStream getBody() throws IOException {
        assertNotExecuted();
        OutputStream body = getBodyInternal(this.headers);
        if (shouldCompress()) {
            return getCompressedBody(body);
        }
        return body;
    }

    private boolean shouldCompress() {
        for (ContentCodingType contentCodingType : this.headers.getContentEncoding()) {
            if (contentCodingType.equals(ContentCodingType.GZIP)) {
                return true;
            }
        }
        return false;
    }

    private OutputStream getCompressedBody(OutputStream body) throws IOException {
        if (this.compressedBody == null) {
            this.compressedBody = new GZIPOutputStream(body);
        }
        return this.compressedBody;
    }

    public final ClientHttpResponse execute() throws IOException {
        assertNotExecuted();
        if (this.compressedBody != null) {
            this.compressedBody.close();
        }
        ClientHttpResponse result = executeInternal(this.headers);
        this.executed = true;
        return result;
    }

    /* access modifiers changed from: protected */
    public void assertNotExecuted() {
        Assert.state(!this.executed, "ClientHttpRequest already executed");
    }
}
