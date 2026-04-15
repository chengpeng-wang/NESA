package com.mb.bdapp.down;

public class DownloadException extends Exception {
    private static final long serialVersionUID = 1;
    private int exceptionCode;

    public DownloadException(String detailMessage) {
        super(detailMessage);
    }

    public DownloadException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DownloadException(Throwable throwable) {
        super(throwable);
    }

    public DownloadException(int exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public DownloadException(int exceptionCode, String detailMessage) {
        super(detailMessage);
        this.exceptionCode = exceptionCode;
    }

    public DownloadException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.exceptionCode = exceptionCode;
    }

    public DownloadException(int exceptionCode, Throwable throwable) {
        super(throwable);
        this.exceptionCode = exceptionCode;
    }

    public int getExceptionCode() {
        return this.exceptionCode;
    }
}
