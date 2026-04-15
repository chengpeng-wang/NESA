package com.mb.bdapp.net;

public class MyHttpException extends Exception {
    private static final long serialVersionUID = 1;
    private final int mStatusCode;

    public MyHttpException(String message, int statusCode) {
        super(message);
        this.mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return this.mStatusCode;
    }
}
