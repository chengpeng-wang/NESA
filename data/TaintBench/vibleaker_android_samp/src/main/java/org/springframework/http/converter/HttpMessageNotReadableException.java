package org.springframework.http.converter;

public class HttpMessageNotReadableException extends HttpMessageConversionException {
    public HttpMessageNotReadableException(String msg) {
        super(msg);
    }

    public HttpMessageNotReadableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
