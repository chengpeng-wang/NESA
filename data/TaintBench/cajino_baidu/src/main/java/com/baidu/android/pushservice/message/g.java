package com.baidu.android.pushservice.message;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.net.ProxyHttpClient;
import com.baidu.android.pushservice.a.b;
import com.baidu.android.pushservice.w;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

class g implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ PublicMsg e;

    g(PublicMsg publicMsg, Context context, String str, String str2, String str3) {
        this.e = publicMsg;
        this.a = context;
        this.b = str;
        this.c = str2;
        this.d = str3;
    }

    public void run() {
        ProxyHttpClient proxyHttpClient = new ProxyHttpClient(this.a);
        try {
            HttpPost httpPost = new HttpPost(w.f + this.b);
            httpPost.addHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
            List<NameValuePair> arrayList = new ArrayList();
            b.a((List) arrayList);
            arrayList.add(new BasicNameValuePair("method", "linkhit"));
            arrayList.add(new BasicNameValuePair("channel_token", this.c));
            arrayList.add(new BasicNameValuePair("data", this.d));
            if (com.baidu.android.pushservice.b.a()) {
                for (NameValuePair obj : arrayList) {
                    Log.d("PublicMsg", "linkhit param -- " + obj.toString());
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
            HttpResponse execute = proxyHttpClient.execute(httpPost);
            if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (com.baidu.android.pushservice.b.a()) {
                    Log.i("PublicMsg", "<<< public msg send result return OK!");
                }
            } else if (com.baidu.android.pushservice.b.a()) {
                Log.e("PublicMsg", "networkRegister request failed  " + execute.getStatusLine());
            }
            proxyHttpClient.close();
        } catch (IOException e) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.e("PublicMsg", e.getMessage());
                Log.e("PublicMsg", "io exception do something ? ");
            }
            proxyHttpClient.close();
        } catch (Exception e2) {
            if (com.baidu.android.pushservice.b.a()) {
                Log.e("PublicMsg", e2.getMessage());
            }
            proxyHttpClient.close();
        } catch (Throwable th) {
            proxyHttpClient.close();
        }
    }
}
