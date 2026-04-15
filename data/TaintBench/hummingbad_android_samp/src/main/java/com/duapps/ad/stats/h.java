package com.duapps.ad.stats;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import com.duapps.ad.DuClickCallback;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.g;
import com.mopub.common.Constants;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

/* compiled from: ToolClickHandlerBase */
public abstract class h implements Callback {
    private static DefaultHttpClient c;
    protected Handler a;
    protected volatile boolean b;
    /* access modifiers changed from: private */
    public Context d;
    /* access modifiers changed from: private */
    public Toast e;
    private boolean f;
    private DuClickCallback g;

    public h(Context context) {
        this.d = context;
        if (Looper.getMainLooper() == Looper.myLooper()) {
            this.a = new Handler(this);
        }
    }

    public boolean handleMessage(Message message) {
        return false;
    }

    public final void a(DuClickCallback duClickCallback) {
        this.g = duClickCallback;
    }

    /* access modifiers changed from: protected|final */
    public final void a() {
        DuClickCallback duClickCallback = this.g;
        if (duClickCallback != null) {
            duClickCallback.onPreClick();
        }
    }

    /* access modifiers changed from: protected|final */
    public final void b() {
        DuClickCallback duClickCallback = this.g;
        if (duClickCallback != null) {
            duClickCallback.onFinish();
        }
    }

    private void b(String str) {
        this.a.post(new i(this, str));
    }

    /* access modifiers changed from: protected|final */
    public final void b(j jVar) {
        if (f.a()) {
            f.c("BaseClickHandler", "Goto installed App: " + jVar.a());
        }
        d.b(this.d, jVar);
        Context context = this.d;
        try {
            Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(jVar.a());
            if (launchIntentForPackage != null) {
                launchIntentForPackage.setFlags(270532608);
                context.startActivity(launchIntentForPackage);
            }
        } catch (Exception e) {
        }
    }

    public final void c() {
        b("Network Error.");
        f.c("BaseClickHandler", "Please check you network and try again.");
    }

    /* access modifiers changed from: protected|final */
    public final void d(j jVar, String str) {
        if (f.a()) {
            f.c("BaseClickHandler", "An apk link.");
        }
        e(jVar, str);
    }

    /* access modifiers changed from: protected|final */
    public final void e(j jVar, String str) {
        if (this.b) {
            f.c("BaseClickHandler", "Has already report");
            return;
        }
        this.b = true;
        if (str == null) {
            if (f.a()) {
                f.c("BaseClickHandler", "startBrowser: url is null");
            }
            d.c(this.d, jVar);
            f.c("BaseClickHandler", "Please check you network and try again.");
            return;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(268435456);
        PackageManager packageManager = this.d.getPackageManager();
        ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 65536);
        if (resolveActivity == null) {
            if (f.a()) {
                f.c("BaseClickHandler", "Goto browser failed.");
            }
            b("No browser or Google Play installed");
            f.c("BaseClickHandler", "No browser or Google Play installed");
            d.c(this.d, jVar);
            return;
        }
        if ("android".equals(resolveActivity.activityInfo.packageName)) {
            intent.setPackage(((ResolveInfo) packageManager.queryIntentActivities(intent, 65536).get(0)).activityInfo.packageName);
        }
        if (f.a()) {
            f.c("BaseClickHandler", "Goto browser");
        }
        this.d.startActivity(intent);
        d.d(this.d, jVar);
        f();
    }

    /* access modifiers changed from: protected|final */
    public final void f(j jVar, String str) {
        if (this.b) {
            f.c("BaseClickHandler", "Has already report");
            return;
        }
        this.b = true;
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setFlags(268435456);
        intent.setPackage("com.android.vending");
        try {
            if (f.a()) {
                f.c("BaseClickHandler", "Goto Play");
            }
            this.d.startActivity(intent);
            d.e(this.d, jVar);
        } catch (Exception e) {
            if (f.a()) {
                f.a("BaseClickHandler", "Goto Play failed:", e);
            }
            e(jVar, str);
        }
    }

    public static boolean a(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        if (str.startsWith("http://market.") || str.startsWith("https://market.") || str.startsWith("https://play.") || str.startsWith("http://play.") || str.startsWith("market:")) {
            return true;
        }
        return false;
    }

    static synchronized DefaultHttpClient d() {
        DefaultHttpClient defaultHttpClient;
        synchronized (h.class) {
            if (c != null) {
                defaultHttpClient = c;
            } else {
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme(Constants.HTTP, PlainSocketFactory.getSocketFactory(), 80));
                schemeRegistry.register(new Scheme(Constants.HTTPS, SSLSocketFactory.getSocketFactory(), 443));
                BasicHttpParams basicHttpParams = new BasicHttpParams();
                defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
                HttpConnectionParams.setConnectionTimeout(defaultHttpClient.getParams(), 10000);
                HttpConnectionParams.setSoTimeout(defaultHttpClient.getParams(), 4000);
                defaultHttpClient.getParams().setIntParameter("http.protocol.max-redirects", 10);
                HttpClientParams.setCookiePolicy(defaultHttpClient.getParams(), "compatibility");
                HttpProtocolParams.setUserAgent(defaultHttpClient.getParams(), g.b);
                defaultHttpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
                c = defaultHttpClient;
            }
        }
        return defaultHttpClient;
    }

    public final synchronized boolean e() {
        return this.f;
    }

    public final synchronized void a(boolean z) {
        this.f = z;
    }

    public final synchronized void f() {
        this.f = false;
    }
}
