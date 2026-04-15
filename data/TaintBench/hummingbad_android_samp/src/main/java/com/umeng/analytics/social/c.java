package com.umeng.analytics.social;

import android.text.TextUtils;
import com.moceanmobile.mast.Defaults;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.a;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/* compiled from: UMNetwork */
public abstract class c {
    protected static String a(String str) {
        int nextInt = new Random().nextInt(1000);
        try {
            String property = System.getProperty("line.separator");
            if (str.length() <= 1) {
                b.b(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tInvalid baseUrl.").toString());
                return null;
            }
            HttpGet httpGet = new HttpGet(str);
            b.a(a.e, new StringBuilder(String.valueOf(nextInt)).append(": GET_URL: ").append(str).toString());
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
            HttpConnectionParams.setSoTimeout(basicHttpParams, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
            HttpResponse execute = new DefaultHttpClient(basicHttpParams).execute(httpGet);
            if (execute.getStatusLine().getStatusCode() == CtaButton.WIDTH_DIPS) {
                HttpEntity entity = execute.getEntity();
                if (entity == null) {
                    return null;
                }
                InputStream gZIPInputStream;
                InputStream content = entity.getContent();
                Header firstHeader = execute.getFirstHeader("Content-Encoding");
                if (firstHeader != null && firstHeader.getValue().equalsIgnoreCase("gzip")) {
                    b.a(a.e, new StringBuilder(String.valueOf(nextInt)).append("  Use GZIPInputStream get data....").toString());
                    gZIPInputStream = new GZIPInputStream(content);
                } else if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("deflate")) {
                    gZIPInputStream = content;
                } else {
                    b.a(a.e, new StringBuilder(String.valueOf(nextInt)).append("  Use InflaterInputStream get data....").toString());
                    gZIPInputStream = new InflaterInputStream(content);
                }
                String a = a(gZIPInputStream);
                b.a(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tresponse: ").append(property).append(a).toString());
                if (a != null) {
                    return a;
                }
                return null;
            }
            b.a(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tFailed to get message.").append(str).toString());
            return null;
        } catch (ClientProtocolException e) {
            b.c(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tClientProtocolException,Failed to send message.").append(str).toString(), e);
            return null;
        } catch (Exception e2) {
            b.c(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tIOException,Failed to send message.").append(str).toString(), e2);
            return null;
        }
    }

    private static String a(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8192);
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(new StringBuilder(String.valueOf(readLine)).append(MASTNativeAdConstants.NEWLINE).toString());
            } catch (IOException e) {
                stringBuilder = a.e;
                b.b(stringBuilder, "Caught IOException in convertStreamToString()", e);
                return null;
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    b.b(a.e, "Caught IOException in convertStreamToString()", e2);
                    return null;
                }
            }
        }
        return stringBuilder.toString();
    }

    protected static String a(String str, String str2) {
        int nextInt = new Random().nextInt(1000);
        String property = System.getProperty("line.separator");
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(basicHttpParams);
        b.a(a.e, new StringBuilder(String.valueOf(nextInt)).append(": POST_URL: ").append(str).toString());
        try {
            HttpPost httpPost = new HttpPost(str);
            if (!TextUtils.isEmpty(str2)) {
                b.a(a.e, new StringBuilder(String.valueOf(nextInt)).append(": POST_BODY: ").append(str2).toString());
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(new BasicNameValuePair("data", str2));
                httpPost.setEntity(new UrlEncodedFormEntity(arrayList, Defaults.ENCODING_UTF_8));
            }
            HttpResponse execute = defaultHttpClient.execute(httpPost);
            if (execute.getStatusLine().getStatusCode() == CtaButton.WIDTH_DIPS) {
                HttpEntity entity = execute.getEntity();
                if (entity == null) {
                    return null;
                }
                InputStream inputStream;
                InputStream content = entity.getContent();
                Header firstHeader = execute.getFirstHeader("Content-Encoding");
                if (firstHeader == null || !firstHeader.getValue().equalsIgnoreCase("deflate")) {
                    inputStream = content;
                } else {
                    inputStream = new InflaterInputStream(content);
                }
                String a = a(inputStream);
                b.a(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tresponse: ").append(property).append(a).toString());
                if (a == null) {
                    return null;
                }
                return a;
            }
            b.c(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tFailed to send message.").append(str).toString());
            return null;
        } catch (ClientProtocolException e) {
            b.c(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tClientProtocolException,Failed to send message.").append(str).toString(), e);
            return null;
        } catch (IOException e2) {
            b.c(a.e, new StringBuilder(String.valueOf(nextInt)).append(":\tIOException,Failed to send message.").append(str).toString(), e2);
            return null;
        }
    }
}
