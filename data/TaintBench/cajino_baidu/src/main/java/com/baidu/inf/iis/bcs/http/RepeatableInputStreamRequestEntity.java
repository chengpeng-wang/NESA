package com.baidu.inf.iis.bcs.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.InputStreamEntity;

class RepeatableInputStreamRequestEntity extends BasicHttpEntity {
    private static final Log log = LogFactory.getLog(RepeatableInputStreamRequestEntity.class);
    private InputStream content;
    private boolean firstAttempt = true;
    private InputStreamEntity inputStreamRequestEntity;

    RepeatableInputStreamRequestEntity(BCSHttpRequest bCSHttpRequest) {
        String str;
        setChunked(false);
        long j = -1;
        try {
            long parseLong;
            str = (String) bCSHttpRequest.getHeaders().get("Content-Length");
            if (str != null) {
                parseLong = Long.parseLong(str);
            } else {
                parseLong = -1;
            }
            j = parseLong;
        } catch (NumberFormatException e) {
            log.warn("Unable to parse content length from request.  Buffering contents in memory.");
        }
        str = (String) bCSHttpRequest.getHeaders().get("Content-Type");
        this.inputStreamRequestEntity = new InputStreamEntity(bCSHttpRequest.getContent(), j);
        this.inputStreamRequestEntity.setContentType(str);
        this.content = bCSHttpRequest.getContent();
        setContent(this.content);
        setContentType(str);
        setContentLength(j);
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isRepeatable() {
        return this.content.markSupported() || this.inputStreamRequestEntity.isRepeatable();
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (!this.firstAttempt && isRepeatable()) {
            this.content.reset();
        }
        this.firstAttempt = false;
        this.inputStreamRequestEntity.writeTo(outputStream);
    }
}
