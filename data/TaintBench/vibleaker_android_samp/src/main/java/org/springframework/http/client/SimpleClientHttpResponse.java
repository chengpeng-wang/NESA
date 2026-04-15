package org.springframework.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

final class SimpleClientHttpResponse extends AbstractClientHttpResponse {
    private static final String AUTH_ERROR = "Received authentication challenge is null";
    private static final String AUTH_ERROR_JELLY_BEAN = "No authentication challenges found";
    private static final String PROXY_AUTH_ERROR = "Received HTTP_PROXY_AUTH (407) code while not using proxy";
    private final HttpURLConnection connection;
    private HttpHeaders headers;

    SimpleClientHttpResponse(HttpURLConnection connection) {
        this.connection = connection;
    }

    public int getRawStatusCode() throws IOException {
        try {
            return this.connection.getResponseCode();
        } catch (IOException ex) {
            return handleIOException(ex);
        }
    }

    private int handleIOException(IOException ex) throws IOException {
        if (AUTH_ERROR.equals(ex.getMessage()) || AUTH_ERROR_JELLY_BEAN.equals(ex.getMessage())) {
            return HttpStatus.UNAUTHORIZED.value();
        }
        if (PROXY_AUTH_ERROR.equals(ex.getMessage())) {
            return HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value();
        }
        throw ex;
    }

    public String getStatusText() throws IOException {
        try {
            return this.connection.getResponseMessage();
        } catch (IOException ex) {
            return HttpStatus.valueOf(handleIOException(ex)).getReasonPhrase();
        }
    }

    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            this.headers = new HttpHeaders();
            String name = this.connection.getHeaderFieldKey(0);
            if (StringUtils.hasLength(name)) {
                this.headers.add(name, this.connection.getHeaderField(0));
            }
            int i = 1;
            while (true) {
                name = this.connection.getHeaderFieldKey(i);
                if (!StringUtils.hasLength(name)) {
                    break;
                }
                this.headers.add(name, this.connection.getHeaderField(i));
                i++;
            }
        }
        return this.headers;
    }

    /* access modifiers changed from: protected */
    public InputStream getBodyInternal() throws IOException {
        InputStream errorStream = this.connection.getErrorStream();
        return errorStream != null ? errorStream : this.connection.getInputStream();
    }

    /* access modifiers changed from: protected */
    public void closeInternal() {
        this.connection.disconnect();
    }
}
