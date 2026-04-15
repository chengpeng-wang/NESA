package com.baidu.inf.iis.bcs.http;

import com.baidu.inf.iis.bcs.model.BCSClientException;
import com.baidu.inf.iis.bcs.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.spi.LocationInfo;

public class HttpRequestFactory {
    private static final Log log = LogFactory.getLog(HttpRequestFactory.class);

    public HttpRequestBase createHttpRequestBase(ClientConfiguration clientConfiguration, BCSHttpRequest bCSHttpRequest) {
        HttpRequestBase httpGet;
        String buildUri = buildUri(clientConfiguration, bCSHttpRequest);
        log.debug(bCSHttpRequest.getHttpMethod().toString() + "  " + buildUri);
        if (bCSHttpRequest.getHttpMethod() == HttpMethodName.GET) {
            httpGet = new HttpGet(buildUri);
        } else if (bCSHttpRequest.getHttpMethod() == HttpMethodName.PUT) {
            HttpRequestBase httpPut = new HttpPut(buildUri);
            if (bCSHttpRequest.getContent() != null) {
                httpPut.setEntity(new RepeatableInputStreamRequestEntity(bCSHttpRequest));
            }
            httpGet = httpPut;
        } else if (bCSHttpRequest.getHttpMethod() == HttpMethodName.DELETE) {
            httpGet = new HttpDelete(buildUri);
        } else if (bCSHttpRequest.getHttpMethod() == HttpMethodName.HEAD) {
            httpGet = new HttpHead(buildUri);
        } else {
            throw new BCSClientException("Unknown HTTP method name:" + bCSHttpRequest.getHttpMethod().toString());
        }
        for (Entry entry : bCSHttpRequest.getHeaders().entrySet()) {
            if (!((String) entry.getKey()).equalsIgnoreCase("Content-Length")) {
                httpGet.addHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (httpGet.getHeaders("Content-Type") == null || httpGet.getHeaders("Content-Type").length == 0) {
            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + Constants.DEFAULT_ENCODING.toLowerCase());
        }
        return httpGet;
    }

    public String buildUri(ClientConfiguration clientConfiguration, BCSHttpRequest bCSHttpRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(clientConfiguration.getProtocol().toString());
        stringBuilder.append("://");
        stringBuilder.append(bCSHttpRequest.getEndpoint());
        if (bCSHttpRequest.getResourcePath() != null && bCSHttpRequest.getResourcePath().length() > 0) {
            if (!bCSHttpRequest.getResourcePath().startsWith("/")) {
                stringBuilder.append("/");
            }
            stringBuilder.append(bCSHttpRequest.getResourcePath());
        }
        stringBuilder.append(LocationInfo.NA).append(encodeParameters(bCSHttpRequest));
        return stringBuilder.toString();
    }

    private String encodeParameters(BCSHttpRequest bCSHttpRequest) {
        List list;
        if (bCSHttpRequest.getParameters().size() > 0) {
            ArrayList arrayList = new ArrayList(bCSHttpRequest.getParameters().size());
            for (Entry entry : bCSHttpRequest.getParameters().entrySet()) {
                arrayList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
            list = arrayList;
        } else {
            list = null;
        }
        if (list != null) {
            return URLEncodedUtils.format(list, Constants.DEFAULT_ENCODING);
        }
        return null;
    }
}
