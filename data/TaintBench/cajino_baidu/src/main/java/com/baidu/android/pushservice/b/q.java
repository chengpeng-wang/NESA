package com.baidu.android.pushservice.b;

import android.util.Log;
import com.baidu.android.common.net.ProxyHttpClient;
import com.baidu.android.pushservice.PushSettings;
import com.baidu.android.pushservice.b;
import com.baidu.android.pushservice.util.PushDatabase;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

class q extends Thread {
    final /* synthetic */ o a;
    private final CharSequence b;
    private String c = null;

    public q(o oVar, CharSequence charSequence, String str) {
        this.a = oVar;
        super("Push_UpdateWorker");
        this.b = charSequence;
        this.c = str;
    }

    private InputStream a(HttpResponse httpResponse) {
        PushSettings.a(System.currentTimeMillis());
        PushDatabase.clearBehaviorInfo(PushDatabase.getDb(this.a.b));
        HttpEntity entity = httpResponse.getEntity();
        InputStream a = this.a.a(entity);
        return a == null ? entity.getContent() : a;
    }

    private UrlEncodedFormEntity a() {
        UrlEncodedFormEntity urlEncodedFormEntity;
        UnsupportedEncodingException e;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("stats", this.c));
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(arrayList, "utf-8");
            try {
                urlEncodedFormEntity.setContentType("application/json");
            } catch (UnsupportedEncodingException e2) {
                e = e2;
                e.printStackTrace();
                return urlEncodedFormEntity;
            }
        } catch (UnsupportedEncodingException e3) {
            UnsupportedEncodingException unsupportedEncodingException = e3;
            urlEncodedFormEntity = null;
            e = unsupportedEncodingException;
            e.printStackTrace();
            return urlEncodedFormEntity;
        }
        return urlEncodedFormEntity;
    }

    public void run() {
        String obj = this.b.toString();
        ProxyHttpClient proxyHttpClient = new ProxyHttpClient(this.a.b);
        HttpPost httpPost = new HttpPost(obj);
        try {
            httpPost.addHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
            httpPost.setEntity(a());
            HttpResponse execute = proxyHttpClient.execute(httpPost);
            InputStream a = a(execute);
            if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                this.a.a(a);
            } else if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                this.a.b(a);
            } else if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                this.a.c(a);
            }
            proxyHttpClient.close();
        } catch (ClientProtocolException e) {
            proxyHttpClient.close();
        } catch (IOException e2) {
            proxyHttpClient.close();
        } catch (Exception e3) {
            if (b.a(this.a.b)) {
                Log.w("StatisticPoster", e3);
            }
            proxyHttpClient.close();
        } catch (Throwable th) {
            proxyHttpClient.close();
            throw th;
        }
    }
}
