package com.baidu.inf.iis.bcs.model;

public class BCSClientException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public BCSClientException(String str) {
        super(str);
    }

    public BCSClientException(String str, Throwable th) {
        super(str, th);
    }
}
