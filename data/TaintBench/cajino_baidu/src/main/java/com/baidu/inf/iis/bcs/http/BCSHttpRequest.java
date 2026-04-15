package com.baidu.inf.iis.bcs.http;

import com.baidu.inf.iis.bcs.request.BaiduBCSRequest;
import java.io.InputStream;
import java.util.Map;

public interface BCSHttpRequest {
    void addHeader(String str, String str2);

    void addParameter(String str, String str2);

    InputStream getContent();

    String getEndpoint();

    Map<String, String> getHeaders();

    HttpMethodName getHttpMethod();

    BaiduBCSRequest getOriginalRequest();

    Map<String, String> getParameters();

    String getResourcePath();

    String getServiceName();

    void setContent(InputStream inputStream);

    void setEndpoint(String str);

    void setHttpMethod(HttpMethodName httpMethodName);

    void setResourcePath(String str);

    BCSHttpRequest withParameter(String str, String str2);
}
