package com.mopub.exceptions;

public class UrlParseException extends Exception {
    public UrlParseException(String detailMessage) {
        super(detailMessage);
    }

    public UrlParseException(Throwable throwable) {
        super(throwable);
    }
}
