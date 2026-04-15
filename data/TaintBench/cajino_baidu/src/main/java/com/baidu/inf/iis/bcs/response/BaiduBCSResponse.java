package com.baidu.inf.iis.bcs.response;

public class BaiduBCSResponse<T> {
    private String requestId;
    private T result;

    public String getRequestId() {
        return this.requestId;
    }

    public T getResult() {
        return this.result;
    }

    public void setRequestId(String str) {
        this.requestId = str;
    }

    public void setResult(T t) {
        this.result = t;
    }
}
