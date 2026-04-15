package com.facebook.ads.internal.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.facebook.ads.AdSettings;
import com.facebook.ads.internal.AdErrorType;
import com.facebook.ads.internal.b;
import com.facebook.ads.internal.dto.c;
import com.facebook.ads.internal.dto.e;
import com.facebook.ads.internal.dto.f;
import com.facebook.ads.internal.thirdparty.http.m;
import com.facebook.ads.internal.thirdparty.http.n;
import com.facebook.ads.internal.util.AdInternalSettings;
import com.facebook.ads.internal.util.d;
import com.facebook.ads.internal.util.p;
import com.facebook.ads.internal.util.r;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.json.JSONException;

public class a {
    private static final p g;
    private static final ThreadPoolExecutor h;
    Map<String, String> a;
    /* access modifiers changed from: private|final */
    public final b b = b.a();
    private a c;
    /* access modifiers changed from: private */
    public e d;
    /* access modifiers changed from: private */
    public com.facebook.ads.internal.thirdparty.http.a e;
    /* access modifiers changed from: private|final */
    public final String f;

    public interface a {
        void a(b bVar);

        void a(d dVar);
    }

    static {
        p pVar = new p();
        g = pVar;
        h = (ThreadPoolExecutor) Executors.newCachedThreadPool(pVar);
    }

    public a() {
        String urlPrefix = AdSettings.getUrlPrefix();
        if (AdInternalSettings.shouldUseLiveRailEndpoint()) {
            if (r.a(urlPrefix)) {
                urlPrefix = "https://ad6.liverail.com/";
            } else {
                urlPrefix = String.format("https://ad6.%s.liverail.com/", new Object[]{urlPrefix});
            }
            this.f = urlPrefix;
            return;
        }
        if (r.a(urlPrefix)) {
            urlPrefix = "https://graph.facebook.com/network_ads_common/";
        } else {
            urlPrefix = String.format("https://graph.%s.facebook.com/network_ads_common/", new Object[]{urlPrefix});
        }
        this.f = urlPrefix;
    }

    /* access modifiers changed from: private */
    public void a(b bVar) {
        if (this.c != null) {
            this.c.a(bVar);
        }
        a();
    }

    private void a(d dVar) {
        if (this.c != null) {
            this.c.a(dVar);
        }
        a();
    }

    /* access modifiers changed from: private */
    public void a(String str) {
        try {
            c a = this.b.a(str);
            c b = a.b();
            if (b != null) {
                d.a(b.a().c(), this.d);
            }
            switch (a.a()) {
                case ADS:
                    d dVar = (d) a;
                    if (b != null && b.a().d()) {
                        d.a(str, this.d);
                    }
                    a(dVar);
                    return;
                case ERROR:
                    e eVar = (e) a;
                    String c = eVar.c();
                    AdErrorType adErrorTypeFromCode = AdErrorType.adErrorTypeFromCode(eVar.d(), AdErrorType.ERROR_MESSAGE);
                    if (c != null) {
                        str = c;
                    }
                    a(adErrorTypeFromCode.getAdErrorWrapper(str));
                    return;
                default:
                    a(AdErrorType.UNKNOWN_RESPONSE.getAdErrorWrapper(str));
                    return;
            }
        } catch (Exception e) {
            a(AdErrorType.PARSER_FAILURE.getAdErrorWrapper(e.getMessage()));
        }
        a(AdErrorType.PARSER_FAILURE.getAdErrorWrapper(e.getMessage()));
    }

    private boolean a(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != 0) {
            return true;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /* access modifiers changed from: private */
    public com.facebook.ads.internal.thirdparty.http.b b() {
        return new com.facebook.ads.internal.thirdparty.http.b() {
            public void a(m mVar) {
                d.b(a.this.d);
                a.this.e = null;
                try {
                    n a = mVar.a();
                    if (a != null) {
                        String e = a.e();
                        c a2 = a.this.b.a(e);
                        if (a2.a() == com.facebook.ads.internal.server.c.a.ERROR) {
                            e eVar = (e) a2;
                            String c = eVar.c();
                            a.this.a(AdErrorType.adErrorTypeFromCode(eVar.d(), AdErrorType.ERROR_MESSAGE).getAdErrorWrapper(c == null ? e : c));
                            return;
                        }
                    }
                } catch (JSONException e2) {
                }
                a.this.a(new b(AdErrorType.NETWORK_ERROR, mVar.getMessage()));
            }

            public void a(n nVar) {
                if (nVar != null) {
                    String e = nVar.e();
                    d.b(a.this.d);
                    a.this.e = null;
                    a.this.a(e);
                }
            }

            public void a(Exception exception) {
                if (m.class.equals(exception.getClass())) {
                    a((m) exception);
                } else {
                    a.this.a(new b(AdErrorType.NETWORK_ERROR, exception.getMessage()));
                }
            }
        };
    }

    public void a() {
        if (this.e != null) {
            this.e.c(1);
            this.e.b(1);
            this.e = null;
        }
    }

    public void a(final Context context, final e eVar) {
        a();
        if (a(context)) {
            this.d = eVar;
            if (d.a(eVar)) {
                String c = d.c(eVar);
                if (c != null) {
                    a(c);
                    return;
                } else {
                    a(AdErrorType.LOAD_TOO_FREQUENTLY.getAdErrorWrapper(null));
                    return;
                }
            }
            h.submit(new Runnable() {
                public void run() {
                    f.b(context);
                    a.this.a = eVar.e();
                    try {
                        a.this.e = new com.facebook.ads.internal.thirdparty.http.a(context, eVar.e);
                        a.this.e.a(a.this.f, a.this.e.b().a(a.this.a), a.this.b());
                    } catch (Exception e) {
                        a.this.a(AdErrorType.AD_REQUEST_FAILED.getAdErrorWrapper(e.getMessage()));
                    }
                }
            });
            return;
        }
        a(new b(AdErrorType.NETWORK_ERROR, "No network connection"));
    }

    public void a(a aVar) {
        this.c = aVar;
    }
}
