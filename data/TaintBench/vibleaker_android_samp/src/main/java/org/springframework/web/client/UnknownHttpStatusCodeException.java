package org.springframework.web.client;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;

public class UnknownHttpStatusCodeException extends RestClientException {
    private static final String DEFAULT_CHARSET = "ISO-8859-1";
    private static final long serialVersionUID = 4702443689088991600L;
    private final int rawStatusCode;
    private final byte[] responseBody;
    private final String responseCharset;
    private final HttpHeaders responseHeaders;
    private final String statusText;

    public UnknownHttpStatusCodeException(int rawStatusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super("Unknown status code [" + String.valueOf(rawStatusCode) + "]" + " " + statusText);
        this.rawStatusCode = rawStatusCode;
        this.statusText = statusText;
        this.responseHeaders = responseHeaders;
        if (responseBody == null) {
            responseBody = new byte[0];
        }
        this.responseBody = responseBody;
        this.responseCharset = responseCharset != null ? responseCharset.name() : DEFAULT_CHARSET;
    }

    public int getRawStatusCode() {
        return this.rawStatusCode;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public HttpHeaders getResponseHeaders() {
        return this.responseHeaders;
    }

    public byte[] getResponseBodyAsByteArray() {
        return this.responseBody;
    }

    public String getResponseBodyAsString() {
        try {
            return new String(this.responseBody, this.responseCharset);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
