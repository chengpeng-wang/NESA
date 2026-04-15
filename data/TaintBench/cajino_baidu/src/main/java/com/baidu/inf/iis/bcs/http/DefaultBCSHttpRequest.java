package com.baidu.inf.iis.bcs.http;

import com.baidu.inf.iis.bcs.request.BaiduBCSRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DefaultBCSHttpRequest implements BCSHttpRequest {
    private InputStream content;
    private String endpoint;
    private Map<String, String> headers;
    private HttpMethodName httpMethod;
    private final BaiduBCSRequest originalRequest;
    private Map<String, String> parameters;
    private String resourcePath;
    private String serviceName;

    public DefaultBCSHttpRequest() {
        this(null);
    }

    public DefaultBCSHttpRequest(BaiduBCSRequest baiduBCSRequest) {
        this.parameters = new HashMap();
        this.headers = new HashMap();
        this.originalRequest = baiduBCSRequest;
    }

    public void addHeader(String str, String str2) {
        this.headers.put(str, str2);
    }

    public void addParameter(String str, String str2) {
        this.parameters.put(str, str2);
    }

    public InputStream getContent() {
        return this.content;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public HttpMethodName getHttpMethod() {
        return this.httpMethod;
    }

    public BaiduBCSRequest getOriginalRequest() {
        return this.originalRequest;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public String getResourcePath() {
        return this.resourcePath;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setContent(InputStream inputStream) {
        this.content = inputStream;
    }

    public void setEndpoint(String str) {
        this.endpoint = str;
    }

    public void setHttpMethod(HttpMethodName httpMethodName) {
        this.httpMethod = httpMethodName;
    }

    public void setResourcePath(String str) {
        this.resourcePath = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getHttpMethod().toString() + " ");
        stringBuilder.append(getEndpoint().toString() + " ");
        stringBuilder.append("/" + (getResourcePath() != null ? getResourcePath() : "") + " ");
        if (!getParameters().isEmpty()) {
            stringBuilder.append("Parameters: (");
            for (String str : getParameters().keySet()) {
                stringBuilder.append(str + ": " + ((String) getParameters().get(str)) + ", ");
            }
            stringBuilder.append(") ");
        }
        if (!getHeaders().isEmpty()) {
            stringBuilder.append("Headers: (");
            for (String str2 : getHeaders().keySet()) {
                stringBuilder.append(str2 + ": " + ((String) getHeaders().get(str2)) + ", ");
            }
            stringBuilder.append(") ");
        }
        return stringBuilder.toString();
    }

    public BCSHttpRequest withParameter(String str, String str2) {
        addParameter(str, str2);
        return this;
    }
}
