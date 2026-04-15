package com.baidu.inf.iis.bcs.model;

public class BCSServiceException extends BCSClientException {
    private static final long serialVersionUID = -6120510420311024191L;
    private int bcsErrorCode;
    private String bcsErrorMessage;
    private int httpErrorCode;
    private String requestId;

    public BCSServiceException(String str) {
        super(str);
    }

    public BCSServiceException(String str, Throwable th) {
        super(str, th);
    }

    public int getBcsErrorCode() {
        return this.bcsErrorCode;
    }

    public String getBcsErrorMessage() {
        return this.bcsErrorMessage;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setBcsErrorCode(int i) {
        this.bcsErrorCode = i;
    }

    public void setBcsErrorMessage(String str) {
        this.bcsErrorMessage = str;
    }

    public void setRequestId(String str) {
        this.requestId = str;
    }

    public int getHttpErrorCode() {
        return this.httpErrorCode;
    }

    public void setHttpErrorCode(int i) {
        this.httpErrorCode = i;
    }
}
