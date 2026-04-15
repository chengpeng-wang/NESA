package com.duapps.ad.stats;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.duapps.ad.base.DuAdNetwork;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.g;
import com.duapps.ad.base.y;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HttpContext;

/* compiled from: ToolClickHandler */
public final class b extends h {
    /* access modifiers changed from: private */
    public Context c;
    private WebView d;

    /* compiled from: ToolClickHandler */
    class a implements RedirectHandler {
        private j a;
        private volatile boolean b = false;

        public a(j jVar) {
            this.a = jVar;
        }

        public final boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            f.c("ToolClickHandler", "statusCode " + statusCode);
            if (statusCode == 303 || statusCode == 302 || statusCode == 301 || statusCode == 307) {
                String value = httpResponse.getHeaders("Location")[0].getValue();
                if (value == null) {
                    if (f.a()) {
                        f.c("ToolClickHandler", "[Http] null URL.");
                    }
                    if (!this.a.k()) {
                        b.this.b();
                        b.this.g(this.a, this.a.h());
                    }
                    b.this.f();
                } else if (h.a(value)) {
                    if (f.a()) {
                        f.c("ToolClickHandler", "[Http] Market URL: " + value);
                    }
                    b.this.a(this.a, value);
                    this.a.b(true);
                    if (!this.a.k()) {
                        b.this.b();
                        b.this.f(this.a, value);
                    }
                    b.this.f();
                } else {
                    b.this.b(this.a, value);
                }
            } else {
                if (f.a()) {
                    f.c("ToolClickHandler", "[Http] non-Market URL: " + this.a.h());
                }
                if (!this.a.k()) {
                    b.this.b();
                    b.this.e(this.a, this.a.h());
                }
                b.this.f();
            }
            return false;
        }

        public final URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) {
            return null;
        }
    }

    /* compiled from: ToolClickHandler */
    class b extends WebViewClient {
        j a;
        WebView b;
        private Runnable d = new f(this);
        private Runnable e = new g(this);
        /* access modifiers changed from: private|volatile */
        public volatile boolean f = false;
        private volatile boolean g = false;
        /* access modifiers changed from: private|volatile */
        public volatile boolean h = false;

        static /* synthetic */ boolean c(b bVar) {
            return false;
        }

        public b(j jVar) {
            this.a = jVar;
        }

        public final void onReceivedError(WebView webView, int i, String str, String str2) {
            String str3 = "Error: " + i;
            if (f.a()) {
                f.c("ToolClickHandler", "[WebView] handleError");
            }
            b.this.a.removeCallbacks(this.e);
            b.this.a.removeCallbacks(this.d);
            if (!this.h) {
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView] onReceivedError: " + str3);
                }
                this.b.stopLoading();
                this.h = true;
                b.this.b();
                b.this.g(this.a, this.a.h());
                b.this.f();
            } else if (f.a()) {
                f.c("ToolClickHandler", "[WebView] already consumed");
            }
        }

        public final void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            if (f.a()) {
                f.c("ToolClickHandler", "[WebView] onPageStarted.");
            }
            this.b = webView;
            this.f = false;
            this.h = false;
            b.this.a.removeCallbacks(this.e);
            b.this.a.removeCallbacks(this.d);
            if (f.a()) {
                f.c("ToolClickHandler", "[WebView] start TIMEOUT_START: " + str);
            }
            b.this.a.postDelayed(this.e, 4000);
        }

        public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (f.a()) {
                f.c("ToolClickHandler", "[WebView] shouldOverrideUrlLoading.");
            }
            b.this.a.removeCallbacks(this.e);
            b.this.a.removeCallbacks(this.d);
            if (this.h || this.f) {
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView]Action canceled.");
                }
                this.h = true;
                b.this.f();
                return true;
            } else if (str == null) {
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView] null URL.");
                }
                b.this.b();
                b.this.g(this.a, this.a.h());
                webView.stopLoading();
                b.this.f();
                this.h = true;
                return true;
            } else if (h.a(str)) {
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView] Market URL: " + str);
                }
                b.this.a(this.a, str);
                this.a.b(true);
                b.this.b();
                b.this.f(this.a, str);
                webView.stopLoading();
                b.this.f();
                this.h = true;
                return true;
            } else {
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView] Decode URL: " + str);
                }
                if (!this.f) {
                    if (f.a()) {
                        f.c("ToolClickHandler", "[WebView] start TIMEOUT_START: " + str);
                    }
                    b.this.a.postDelayed(this.e, 4000);
                }
                return false;
            }
        }

        public final void onPageFinished(WebView webView, String str) {
            if (f.a()) {
                f.c("ToolClickHandler", "[WebView] Page finished");
            }
            b.this.a.removeCallbacks(this.e);
            b.this.a.removeCallbacks(this.d);
            if (this.h) {
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView] already consumed");
                }
            } else if (!this.f) {
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView] start TIMEOUT_FINISH: " + str);
                }
                b.this.a.postDelayed(this.d, 2000);
            }
        }
    }

    /* access modifiers changed from: private */
    public void g(j jVar, String str) {
        if (!this.b) {
            com.duapps.ad.entity.a aVar = jVar.c;
            CharSequence charSequence = aVar != null ? aVar.c : null;
            if (TextUtils.isEmpty(charSequence)) {
                f.c("ToolClickHandler", "browserUrl：" + str + " no pkgname");
                e(jVar, str);
                return;
            }
            String str2 = "https://play.google.com/store/apps/details?id=" + charSequence;
            f.c("ToolClickHandler", jVar.c.b + " start google play via mock url -->" + str2);
            if (d.a(this.c, "com.android.vending")) {
                f(jVar, str2);
            } else {
                e(jVar, str);
            }
        }
    }

    /* access modifiers changed from: final */
    public final void a(j jVar, String str) {
        if (jVar.d() > 0) {
            d dVar = new d();
            dVar.a = jVar.h();
            dVar.d = str;
            dVar.b = jVar.a();
            dVar.c = 1;
            dVar.e = System.currentTimeMillis();
            n.a(this.c).a(dVar);
        }
    }

    public b(Context context) {
        super(context);
        this.c = context;
    }

    public final void a(j jVar) {
        if (!e()) {
            a(true);
            this.b = false;
            if (d.a(this.c, jVar.a())) {
                b(jVar);
                f();
                return;
            }
            d.a(this.c, jVar);
            if (!d.b(this.c)) {
                a(false);
                if (f.a()) {
                    f.c("BaseClickHandler", "No network.");
                }
                c();
                f();
            } else if (jVar.f()) {
                d(jVar, jVar.h());
            } else if (jVar.g()) {
                jVar.b(false);
                if (f.a()) {
                    f.c("ToolClickHandler", "Clicked URL: " + jVar.h());
                }
                if (DuAdNetwork.d()) {
                    boolean a = d.a(this.c, "com.android.vending");
                    if (f.a()) {
                        f.c("ToolClickHandler", "Click with Play installed? " + a);
                    }
                    if (a) {
                        String h = jVar.h();
                        if (h.a(h)) {
                            jVar.b(true);
                            f(jVar, h);
                            f();
                            return;
                        } else if (jVar.d() > 0) {
                            d a2 = g.a(this.c).a(h);
                            jVar.a(a2);
                            if (1 == a2.c) {
                                jVar.b(true);
                                f(jVar, a2.d);
                                f();
                                return;
                            } else if (a2.c == 2 || a2.c == 3) {
                                h = "https://play.google.com/store/apps/details?id=" + jVar.c.c;
                                f.c("ToolClickHandler", jVar.c.b + " parse result is " + a2.c + " and start google play via url -->" + h);
                                f(jVar, h);
                                return;
                            } else {
                                a();
                                h(jVar, h);
                                return;
                            }
                        } else {
                            a();
                            h(jVar, h);
                            return;
                        }
                    }
                    e(jVar, jVar.h());
                    f();
                    return;
                }
                if (f.a()) {
                    f.c("ToolClickHandler", "CHINA Click to download:" + jVar.a());
                }
                e(jVar, jVar.h());
                f();
            } else if (f.a()) {
                f.c("ToolClickHandler", "Unknown Open type: " + jVar.c());
            }
        }
    }

    private void h(j jVar, String str) {
        Object obj = 1;
        if (VERSION.SDK_INT < 11) {
            obj = null;
        }
        if (obj != null) {
            if (f.a()) {
                f.c("ToolClickHandler", "Newer OS, use WebView redirect.");
            }
            try {
                if (this.d == null) {
                    this.d = new WebView(this.c);
                    WebSettings settings = this.d.getSettings();
                    settings.setAllowContentAccess(true);
                    settings.setJavaScriptEnabled(true);
                    settings.setUserAgentString(g.b);
                }
                this.d.stopLoading();
                this.d.setWebViewClient(new b(jVar));
                if (f.a()) {
                    f.c("ToolClickHandler", "[WebView] Decode URL: " + str);
                }
                this.d.loadUrl(str);
                return;
            } catch (Throwable th) {
                y.b(new c(this, jVar, str));
                return;
            }
        }
        if (f.a()) {
            f.c("ToolClickHandler", "Older OS, use Http redirect.");
        }
        y.b(new d(this, jVar, str));
    }

    /* access modifiers changed from: protected|final */
    public final void b(j jVar, String str) {
        DefaultHttpClient d = h.d();
        d.setRedirectHandler(new a(jVar));
        if (f.a()) {
            f.c("ToolClickHandler", "[Http] Decode URL: " + str);
        }
        try {
            HttpGet httpGet = new HttpGet(str);
            HttpConnectionParams.setConnectionTimeout(httpGet.getParams(), 10000);
            HttpConnectionParams.setSoTimeout(httpGet.getParams(), 4000);
            d.execute(httpGet).getEntity();
        } catch (Exception e) {
            f.b("ToolClickHandler", "[Http] Others error: ", e);
            if (!jVar.k()) {
                b();
                g(jVar, str);
            }
            f();
        }
    }

    public final void c(j jVar, String str) {
        d.i(this.c, jVar);
        y.b(new e(this, jVar, str));
    }
}
