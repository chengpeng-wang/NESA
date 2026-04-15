package com.flurry.sdk;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ag extends ab {
    public static final String e = ag.class.getSimpleName();
    public al f;
    public WeakReference g = new WeakReference(null);
    public boolean h = false;
    /* access modifiers changed from: private|final */
    public final GestureDetector i;
    private boolean j = false;
    private boolean k = false;
    private final Rect l = new Rect();
    private int m = 0;
    /* access modifiers changed from: private */
    public WeakReference n = new WeakReference(null);

    public ag(Context context, String str) {
        super(context, str);
        this.i = new GestureDetector(context, new ah(this));
        ai aiVar = new ai(this);
        this.f = al.INIT;
        il.a().a("com.flurry.android.impl.ads.RegisterCTAViewEvent", aiVar);
    }

    /* access modifiers changed from: private */
    public void a(WeakReference weakReference) {
        if (weakReference.get() != null) {
            Button button = (Button) weakReference.get();
            button.setClickable(true);
            button.setOnClickListener(new aj(this));
        }
    }

    public final void a(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                a((ViewGroup) viewGroup.getChildAt(i));
            }
            viewGroup.getChildAt(i).setFocusable(false);
            viewGroup.getChildAt(i).setClickable(false);
        }
    }

    /* access modifiers changed from: protected|final */
    public final void a(d dVar) {
        super.a(dVar);
        if (e.kOnFetched.equals(dVar.b)) {
            ct ctVar = this.c;
            if (ctVar == null) {
                gz.a(this, df.kMissingAdController);
                return;
            }
            dv dvVar = ctVar.a;
            if (dvVar == null) {
                gz.a(this, df.kInvalidAdUnit);
            } else if (dx.a.equals(dvVar.a)) {
                this.d = this.c;
                this.c = null;
                synchronized (this) {
                    this.f = al.READY;
                }
            } else {
                gz.a(this, df.kIncorrectClassForAdSpace);
            }
        }
    }

    /* access modifiers changed from: protected|final */
    public final void k() {
        super.k();
        if (al.READY.equals(this.f)) {
            View view = (View) this.g.get();
            if (view != null) {
                if (this.n.get() != null) {
                    a(this.n);
                }
                if (view != null) {
                    view.setOnTouchListener(new ak(this));
                }
                if (!this.j) {
                    long width = view.getGlobalVisibleRect(this.l) ? (long) (this.l.width() * this.l.height()) : 0;
                    if (width > 0) {
                        int i = (this.l.top == 0 && this.l.left == 0) ? 1 : 0;
                        if (((float) width) < ((float) ((long) (view.getHeight() * view.getWidth()))) * 0.5f || i != 0) {
                            this.m = 0;
                            return;
                        }
                        int i2 = this.m + 1;
                        this.m = i2;
                        if (i2 >= 10 && !this.h) {
                            p();
                            return;
                        }
                        return;
                    }
                    this.m = 0;
                }
            }
        }
    }

    /* access modifiers changed from: protected|final */
    public final void l() {
        if (this.k) {
            this.k = false;
            gx.a(dg.EV_AD_CLOSED, Collections.emptyMap(), e(), this, this.d, 0);
        }
    }

    public final boolean m() {
        boolean equals;
        synchronized (this) {
            equals = al.READY.equals(this.f);
        }
        return equals;
    }

    public final void n() {
        View view = (View) this.g.get();
        if (view != null) {
            view.setOnTouchListener(null);
            this.g.clear();
        }
    }

    public final List o() {
        return !al.READY.equals(this.f) ? Collections.emptyList() : new ArrayList(this.d.b());
    }

    public final synchronized void p() {
        if (!this.j) {
            Log.i("Flurry", "Impression logged");
            gx.a(dg.EV_NATIVE_IMPRESSION, Collections.emptyMap(), e(), this, this.d, 0);
            this.j = true;
        }
    }

    public final synchronized void q() {
        Log.i("Flurry", "Click logged");
        gx.a(dg.EV_CLICKED, Collections.emptyMap(), e(), this, this.d, 0);
        this.k = true;
    }
}
