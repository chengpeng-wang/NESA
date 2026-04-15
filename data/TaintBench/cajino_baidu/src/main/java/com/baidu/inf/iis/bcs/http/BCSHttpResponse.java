package com.baidu.inf.iis.bcs.http;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BCSHttpResponse {
    private InputStream content;
    private Map<String, String> headers = new HashMap();
    private BCSHttpRequest request;
    private int statusCode;
    private String statusText;

    public void addHeader(String str, String str2) {
        this.headers.put(str, str2);
    }

    public InputStream getContent() {
        return this.content;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public BCSHttpRequest getRequest() {
        return this.request;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public void setContent(InputStream inputStream) {
        this.content = inputStream;
    }

    public void setRequest(BCSHttpRequest bCSHttpRequest) {
        this.request = bCSHttpRequest;
    }

    public void setStatusCode(int i) {
        this.statusCode = i;
    }

    public void setStatusText(String str) {
        this.statusText = str;
    }
}
