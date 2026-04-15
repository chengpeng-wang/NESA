package com.splunk.mint;

public class NetSenderResponse {
    private String data;
    private Exception exception;
    private int responseCode;
    private Boolean sentSuccessfully = Boolean.valueOf(false);
    private String serverResponse;
    private String url;

    protected NetSenderResponse(String url, String data) {
        this.url = url;
        this.data = data;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    /* access modifiers changed from: protected */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Exception getException() {
        return this.exception;
    }

    /* access modifiers changed from: protected */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Boolean getSentSuccessfully() {
        return this.sentSuccessfully;
    }

    /* access modifiers changed from: protected */
    public void setSentSuccessfully(Boolean sendSuccessfully) {
        this.sentSuccessfully = sendSuccessfully;
    }

    public String getServerResponse() {
        return this.serverResponse;
    }

    /* access modifiers changed from: protected */
    public void setServerResponse(String serverResponse) {
        this.serverResponse = serverResponse;
    }

    public String getData() {
        return this.data;
    }

    /* access modifiers changed from: protected */
    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return this.url;
    }

    public String toString() {
        return "NetSenderResponse [exception=" + this.exception + ", sendSuccessfully=" + this.sentSuccessfully + ", serverResponse=" + this.serverResponse + ", data=" + this.data + ", url=" + this.url + ", responseCode=" + this.responseCode + "]";
    }
}
