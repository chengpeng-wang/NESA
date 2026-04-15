package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.t;

public class d {
    private int a;
    /* access modifiers changed from: private */
    public int b;
    /* access modifiers changed from: private|final */
    public final Context c;
    /* access modifiers changed from: private|final */
    public final View d;
    /* access modifiers changed from: private|final */
    public final int e;
    /* access modifiers changed from: private|final */
    public final a f;
    /* access modifiers changed from: private|final */
    public final Handler g;
    /* access modifiers changed from: private|final */
    public final Runnable h;
    /* access modifiers changed from: private|final */
    public final boolean i;
    /* access modifiers changed from: private|volatile */
    public volatile boolean j;

    public static abstract class a {
        public abstract void a();

        public void b() {
        }
    }

    static final class b extends t<d> {
        public b(d dVar) {
            super(dVar);
        }

        public final void run() {
            d dVar = (d) a();
            if (dVar != null) {
                if (dVar.i || !dVar.j) {
                    View c = dVar.d;
                    a d = dVar.f;
                    if (c != null && d != null) {
                        if (g.a(dVar.c, c, dVar.e)) {
                            d.a();
                            dVar.j = true;
                            return;
                        }
                        d.b();
                        dVar.g.postDelayed(dVar.h, (long) dVar.b);
                    }
                }
            }
        }
    }

    public d(Context context, View view, int i, a aVar) {
        this(context, view, i, false, aVar);
    }

    public d(Context context, View view, int i, boolean z, a aVar) {
        this.a = 0;
        this.b = 1000;
        this.g = new Handler();
        this.h = new b(this);
        this.c = context;
        this.d = view;
        this.e = i;
        this.f = aVar;
        this.i = z;
    }

    public void a() {
        if (!this.i && !this.j) {
            this.g.postDelayed(this.h, (long) this.a);
        }
    }

    public void a(int i) {
        this.a = i;
    }

    public void b() {
        this.g.removeCallbacks(this.h);
    }

    public void b(int i) {
        this.b = i;
    }
}
