package com.baidu.inf.iis.bcs.request;

import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.utils.StringUtils;

public abstract class BaiduBCSRequest {
    protected String bucket = null;
    protected HttpMethodName httpMethod = null;
    protected String object = null;

    public BaiduBCSRequest(String str, HttpMethodName httpMethodName) {
        this.bucket = str;
        this.object = "/";
        this.httpMethod = httpMethodName;
    }

    public BaiduBCSRequest(String str, String str2, HttpMethodName httpMethodName) {
        this.bucket = str;
        this.object = StringUtils.trimSlash(str2);
        this.httpMethod = httpMethodName;
    }

    public String getBucket() {
        return this.bucket;
    }

    public HttpMethodName getHttpMethod() {
        return this.httpMethod;
    }

    public String getObject() {
        return this.object;
    }

    public void setBucket(String str) {
        this.bucket = str;
    }

    public void setHttpMethod(HttpMethodName httpMethodName) {
        this.httpMethod = httpMethodName;
    }

    public void setObject(String str) {
        this.object = StringUtils.trimSlash(str);
    }
}
