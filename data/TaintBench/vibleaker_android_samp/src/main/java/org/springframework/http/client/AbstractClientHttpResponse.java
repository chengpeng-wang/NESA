package org.springframework.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpStatus;

public abstract class AbstractClientHttpResponse implements ClientHttpResponse {
    private InputStream compressedBody;

    public abstract void closeInternal();

    public abstract InputStream getBodyInternal() throws IOException;

    public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.valueOf(getRawStatusCode());
    }

    public InputStream getBody() throws IOException {
        InputStream body = getBodyInternal();
        if (isCompressed()) {
            return getCompressedBody(body);
        }
        return body;
    }

    public void close() {
        if (this.compressedBody != null) {
            try {
                this.compressedBody.close();
            } catch (IOException e) {
            }
        }
        closeInternal();
    }

    private boolean isCompressed() {
        for (ContentCodingType contentCodingType : getHeaders().getContentEncoding()) {
            if (contentCodingType.equals(ContentCodingType.GZIP)) {
                return true;
            }
        }
        return false;
    }

    private InputStream getCompressedBody(InputStream body) throws IOException {
        if (this.compressedBody == null) {
            this.compressedBody = new GZIPInputStream(body);
        }
        return this.compressedBody;
    }
}
