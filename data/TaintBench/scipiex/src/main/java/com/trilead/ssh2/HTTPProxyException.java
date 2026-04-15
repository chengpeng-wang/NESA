package com.trilead.ssh2;

import java.io.IOException;

public class HTTPProxyException extends IOException {
    private static final long serialVersionUID = 2241537397104426186L;
    public final int httpErrorCode;
    public final String httpResponse;

    public HTTPProxyException(String httpResponse, int httpErrorCode) {
        super("HTTP Proxy Error (" + httpErrorCode + " " + httpResponse + ")");
        this.httpResponse = httpResponse;
        this.httpErrorCode = httpErrorCode;
    }
}
