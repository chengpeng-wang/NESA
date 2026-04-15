package com.facebook.ads.internal.view;

import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.facebook.ads.internal.adapters.d;
import com.facebook.ads.internal.adapters.k;
import com.facebook.ads.internal.adapters.l;
import com.facebook.ads.internal.ssp.ANAdRenderer.Listener;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.h;

public class c extends d {
    /* access modifiers changed from: private|static|final */
    public static final String a = c.class.getSimpleName();
    private final k b;
    private final int c;
    /* access modifiers changed from: private|final */
    public final Listener d;
    /* access modifiers changed from: private */
    public l e;
    /* access modifiers changed from: private */
    public d f;
    /* access modifiers changed from: private */
    public long g;
    /* access modifiers changed from: private */
    public com.facebook.ads.internal.util.b.a h;

    class a extends WebViewClient {
        private a() {
        }

        /* synthetic */ a(c cVar, AnonymousClass1 anonymousClass1) {
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
            Uri parse = Uri.parse(str);
            if ("fbad".equals(parse.getScheme()) && "close".equals(parse.getAuthority())) {
                c.this.d.onAdClose();
            } else {
                c.this.d.onAdClick();
                com.facebook.ads.internal.action.a a = com.facebook.ads.internal.action.b.a(c.this.getContext(), parse);
                if (a != null) {
                    try {
                        c.this.h = a.a();
                        c.this.g = System.currentTimeMillis();
                        a.b();
                    } catch (Exception e) {
                        Log.e(c.a, "Error executing action", e);
                    }
                }
            }
            return true;
        }
    }

    class b {
        private final String b;

        private b() {
            this.b = b.class.getSimpleName();
        }

        /* synthetic */ b(c cVar, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void alert(String str) {
            Log.e(this.b, str);
        }

        public String getAnalogInfo() {
            return g.a(com.facebook.ads.internal.util.a.a());
        }

        public void onPageInitialized() {
            if (!c.this.b()) {
                c.this.e.c();
                if (c.this.f != null) {
                    c.this.f.a();
                }
            }
        }
    }

    public c(Context context, k kVar, int i, Listener listener) {
        super(context);
        if (kVar == null || listener == null) {
            throw new IllegalArgumentException();
        }
        this.b = kVar;
        this.c = i;
        this.d = listener;
        c();
    }

    private void c() {
        setWebViewClient(new a(this, null));
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(false);
        h.b(this);
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        addJavascriptInterface(new b(this, null), "AdControl");
        this.e = new l(getContext(), this, new com.facebook.ads.internal.adapters.b() {
            public void d() {
                c.this.d.onAdImpression();
            }
        });
        this.e.a(this.b);
        this.f = new d(getContext(), this, this.c, new com.facebook.ads.internal.adapters.d.a() {
            public void a() {
                c.this.e.a();
            }
        });
        this.f.a(this.b.j());
        this.f.b(this.b.k());
        this.f.a();
        loadDataWithBaseURL(h.a(), this.b.d(), "text/html", "utf-8", null);
    }

    private void d() {
        this.e.c();
    }

    public void destroy() {
        if (this.f != null) {
            this.f.b();
            this.f = null;
        }
        h.a((WebView) this);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i == 0) {
            if (this.g > 0 && this.h != null) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(this.g, this.h, this.b.i()));
                this.g = 0;
                this.h = null;
            }
            if (this.f != null) {
                this.f.a();
            }
        } else if (i == 8 && this.f != null) {
            this.f.b();
        }
    }
}
