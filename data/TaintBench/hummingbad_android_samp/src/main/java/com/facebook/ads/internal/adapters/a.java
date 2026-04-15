package com.facebook.ads.internal.adapters;

import android.content.Context;
import com.facebook.ads.internal.util.g;

public abstract class a {
    protected final b a;
    private final Context b;
    private boolean c;

    public a(Context context, b bVar) {
        this.b = context;
        this.a = bVar;
    }

    public final void a() {
        if (!this.c) {
            if (this.a != null) {
                this.a.d();
            }
            b();
            this.c = true;
            g.a(this.b, "Impression logged");
            if (this.a != null) {
                this.a.e();
            }
        }
    }

    public abstract void b();
}
