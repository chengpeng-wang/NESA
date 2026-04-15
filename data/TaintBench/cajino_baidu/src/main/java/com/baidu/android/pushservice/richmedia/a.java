package com.baidu.android.pushservice.richmedia;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class a {
    DefaultHttpClient a;
    private l b = l.a(a.class.getName());

    public a() {
        HttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "utf-8");
        HttpProtocolParams.setUseExpectContinue(basicHttpParams, false);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 15000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 15000);
        this.a = new DefaultHttpClient(basicHttpParams);
        this.a.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, false));
    }

    public HttpResponse a(String str, String str2, Map map, List list) {
        HttpUriRequest httpGet;
        if (str.equals(HttpGet.METHOD_NAME)) {
            httpGet = new HttpGet(str2);
        } else {
            HttpPost httpPost = new HttpPost(str2);
            if (list != null && list.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
            }
            Object httpGet2 = httpPost;
        }
        this.b.b("url:" + str2);
        if (map != null) {
            for (Entry entry : map.entrySet()) {
                httpGet2.setHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return this.a.execute(httpGet2);
    }
}
