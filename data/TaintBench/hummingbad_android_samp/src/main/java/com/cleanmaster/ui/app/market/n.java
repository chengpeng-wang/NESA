package com.cleanmaster.ui.app.market;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import com.cleanmaster.common.a;

/* compiled from: ParseWebViewUrlUtils */
public class n {
    /* access modifiers changed from: private */
    public WebView a;
    /* access modifiers changed from: private */
    public boolean b;
    /* access modifiers changed from: private */
    public Handler c;
    private q d = null;
    private String e;
    private String f;
    private String g;
    private String h;

    public void a(q qVar) {
        this.d = qVar;
    }

    public n(Context context) {
        if (!a.b(context)) {
            try {
                this.a = new WebView(context);
            } catch (Exception e) {
            }
        }
        if (this.a != null) {
            this.c = new o(this, Looper.getMainLooper());
            this.a.getSettings().setJavaScriptEnabled(true);
            this.a.getSettings().setCacheMode(2);
            if (VERSION.SDK_INT < 18) {
                this.a.getSettings().setSavePassword(false);
            }
        }
    }

    public void a(String str, String str2, String str3, String str4) {
        if (this.a != null) {
            this.e = str;
            this.f = str2;
            this.g = str3;
            this.h = str4;
            this.a.setWebViewClient(new p(this));
            this.a.loadUrl(str);
        }
    }

    /* access modifiers changed from: private */
    public void a(String str) {
        if (this.a != null) {
            this.a.destroy();
            this.a = null;
        }
        if (this.d != null) {
            this.d.a(str);
        }
    }
}
