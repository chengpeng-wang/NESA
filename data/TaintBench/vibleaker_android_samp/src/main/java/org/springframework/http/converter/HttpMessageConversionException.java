package org.springframework.http.converter;

import org.springframework.core.NestedRuntimeException;

public class HttpMessageConversionException extends NestedRuntimeException {
    public HttpMessageConversionException(String msg) {
        super(msg);
    }

    public HttpMessageConversionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
