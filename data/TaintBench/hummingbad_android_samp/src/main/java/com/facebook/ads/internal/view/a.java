package com.facebook.ads.internal.view;

import android.content.Context;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.facebook.ads.internal.adapters.d;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.h;

public class a extends d {
    /* access modifiers changed from: private|final */
    public final a a;
    /* access modifiers changed from: private */
    public d b;

    public interface a {
        void a();

        void a(int i);

        void a(String str);

        void b();
    }

    class b extends WebViewClient {
        private b() {
        }

        /* synthetic */ b(a aVar, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            if (g.a()) {
                sslErrorHandler.proceed();
            } else {
                sslErrorHandler.cancel();
            }
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            a.this.a.a(str);
            return true;
        }
    }

    public class c {
        private final String b = c.class.getSimpleName();

        public void alert(String str) {
            Log.e(this.b, str);
        }

        public String getAnalogInfo() {
            return g.a(com.facebook.ads.internal.util.a.a());
        }

        public void onPageInitialized() {
            if (!a.this.b()) {
                a.this.a.a();
                if (a.this.b != null) {
                    a.this.b.a();
                }
            }
        }
    }

    public a(Context context, final a aVar, int i) {
        super(context);
        this.a = aVar;
        setWebViewClient(new b(this, null));
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(false);
        h.b(this);
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        addJavascriptInterface(new c(), "AdControl");
        this.b = new d(getContext(), this, i, new com.facebook.ads.internal.adapters.d.a() {
            public void a() {
                aVar.b();
            }
        });
    }

    public void a(int i, int i2) {
        this.b.a(i);
        this.b.b(i2);
    }

    public void destroy() {
        if (this.b != null) {
            this.b.b();
            this.b = null;
        }
        h.a((WebView) this);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (this.a != null) {
            this.a.a(i);
        }
        if (i == 0) {
            if (this.b != null) {
                this.b.a();
            }
        } else if (i == 8 && this.b != null) {
            this.b.b();
        }
    }
}
