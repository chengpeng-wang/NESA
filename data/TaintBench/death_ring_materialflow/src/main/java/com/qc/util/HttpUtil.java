package com.qc.util;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
    public static HttpClient getHttpClent() {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);
        return new DefaultHttpClient(httpParameters);
    }

    public static HttpGet getHttpGet(String url) {
        return new HttpGet(url);
    }

    public static HttpPost getHttpPost(String url) {
        return new HttpPost(url);
    }

    public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException {
        return getHttpClent().execute(request);
    }

    public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException {
        return new DefaultHttpClient().execute(request);
    }

    public static String queryAPKForGet(String url) {
        HttpGet request = getHttpGet(url);
        request.setHeader("Accept-Charset", "GB2312,GBK,utf-8;q=0.7,*;q=0.7");
        request.setHeader("Connection", "Keep-Alive");
        try {
            HttpResponse response = getHttpResponse(request);
            return response.getStatusLine().getStatusCode() == 200 ? EntityUtils.toString(response.getEntity()) : "网络异常IOException";
        } catch (ClientProtocolException e) {
            return "网络异常ClientProtocolException";
        } catch (IOException e2) {
            return "网络异常IOException";
        }
    }

    public static String queryStringForGet(String url) {
        HttpGet request = getHttpGet(url);
        request.setHeader("Accept", "application/xml;q=0.9,*/*;q=0.8");
        request.setHeader("Accept-Charset", "GB2312,GBK,utf-8;q=0.7,*;q=0.7");
        request.setHeader("Connection", "Keep-Alive");
        try {
            HttpResponse response = getHttpResponse(request);
            return response.getStatusLine().getStatusCode() == 200 ? EntityUtils.toString(response.getEntity()) : "网络异常IOException";
        } catch (ClientProtocolException e) {
            return "网络异常ClientProtocolException";
        } catch (IOException e2) {
            return "网络异常IOException";
        }
    }
}
