package com.baidu.android.pushservice.b;

import android.content.Context;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.common.net.ConnectManager;
import com.baidu.android.common.net.ProxyHttpClient;
import com.baidu.android.pushservice.a.b;
import com.baidu.android.pushservice.y;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EntityUtils;

public abstract class k {
    protected Context a;
    protected String b;
    /* access modifiers changed from: private */
    public boolean c = false;

    public k(Context context) {
        this.a = context.getApplicationContext();
    }

    /* access modifiers changed from: private */
    public void f() {
        if (!TextUtils.isEmpty(this.b)) {
            ProxyHttpClient proxyHttpClient = new ProxyHttpClient(this.a);
            try {
                String b = b();
                while (!TextUtils.isEmpty(b)) {
                    if (!e()) {
                        this.b += y.a().c();
                    }
                    HttpPost httpPost = new HttpPost(this.b);
                    httpPost.addHeader("Content-Type", URLEncodedUtils.CONTENT_TYPE);
                    List arrayList = new ArrayList();
                    b.a(arrayList);
                    a(b, arrayList);
                    httpPost.setEntity(new UrlEncodedFormEntity(arrayList, "UTF-8"));
                    HttpResponse execute = proxyHttpClient.execute(httpPost);
                    if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        if (com.baidu.android.pushservice.b.a(this.a)) {
                            Log.d("Statistics-BaseSender", "Send statistics data OK, continue!");
                        }
                        c();
                        proxyHttpClient.close();
                        if (e()) {
                            break;
                        }
                        b = b();
                    } else {
                        if (com.baidu.android.pushservice.b.a(this.a)) {
                            Log.w("Statistics-BaseSender", "Send statistics data failed, abort!" + execute.getStatusLine());
                            Log.w("Statistics-BaseSender", "Response info: " + execute.getStatusLine() + EntityUtils.toString(execute.getEntity()));
                        }
                        d();
                        proxyHttpClient.close();
                    }
                }
                proxyHttpClient.close();
            } catch (Exception e) {
                Log.e("Statistics-BaseSender", "startSendLoop Exception: " + e);
                proxyHttpClient.close();
            } catch (Throwable th) {
                proxyHttpClient.close();
                throw th;
            }
        } else if (com.baidu.android.pushservice.b.a(this.a)) {
            Log.e("Statistics-BaseSender", "mUrl is null");
        }
    }

    public abstract void a(String str, List list);

    public abstract boolean a();

    public abstract String b();

    public abstract void c();

    public abstract void d();

    public abstract boolean e();

    public synchronized void g() {
        if (!this.c) {
            if (a()) {
                if (ConnectManager.isNetworkConnected(this.a)) {
                    if (y.a().e()) {
                        this.c = true;
                        Thread thread = new Thread(new l(this));
                        thread.setName("PushService-stats-sender");
                        thread.start();
                    } else if (com.baidu.android.pushservice.b.a(this.a)) {
                        Log.e("Statistics-BaseSender", "Fail Send Statistics. Token invalid!");
                    }
                } else if (com.baidu.android.pushservice.b.a(this.a)) {
                    Log.w("Statistics-BaseSender", "Network is not reachable!");
                }
            } else if (com.baidu.android.pushservice.b.a(this.a)) {
                Log.w("Statistics-BaseSender", "No new data producted, do nothing!");
            }
        }
    }
}
