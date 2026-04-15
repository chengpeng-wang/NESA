package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.util.Log;
import com.facebook.ads.internal.util.o;
import com.facebook.ads.internal.util.r;
import com.facebook.ads.internal.view.d;
import java.util.Collections;
import java.util.Map;

public class l extends a {
    /* access modifiers changed from: private|static|final */
    public static final String b = l.class.getSimpleName();
    /* access modifiers changed from: private|final */
    public final d c;
    /* access modifiers changed from: private */
    public k d;
    private boolean e;

    public l(Context context, d dVar, b bVar) {
        super(context, bVar);
        this.c = dVar;
    }

    private void a(Map<String, String> map) {
        if (this.d != null) {
            if (!r.a(this.d.f())) {
                new o(map).execute(new String[]{r0});
            }
        }
    }

    public void a(k kVar) {
        this.d = kVar;
    }

    /* access modifiers changed from: protected */
    public void b() {
        if (this.d != null) {
            if (!(this.c == null || r.a(this.d.g()))) {
                if (this.c.b()) {
                    Log.w(b, "Webview already destroyed, cannot send impression");
                } else {
                    this.c.loadUrl("javascript:" + this.d.g());
                }
            }
            a(Collections.singletonMap("evt", "native_imp"));
        }
    }

    public synchronized void c() {
        if (!(this.e || this.d == null)) {
            this.e = true;
            if (!(this.c == null || r.a(this.d.e()))) {
                this.c.post(new Runnable() {
                    public void run() {
                        if (l.this.c.b()) {
                            Log.w(l.b, "Webview already destroyed, cannot activate");
                        } else {
                            l.this.c.loadUrl("javascript:" + l.this.d.e());
                        }
                    }
                });
            }
        }
    }

    public void d() {
        a(Collections.singletonMap("evt", "interstitial_displayed"));
    }
}
