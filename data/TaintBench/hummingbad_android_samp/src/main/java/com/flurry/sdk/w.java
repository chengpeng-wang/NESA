package com.flurry.sdk;

import android.view.View;
import android.widget.ImageView;

final class w extends lz {
    final /* synthetic */ eh a;
    final /* synthetic */ View b;
    final /* synthetic */ int c;
    final /* synthetic */ v d;

    w(v vVar, eh ehVar, View view, int i) {
        this.d = vVar;
        this.a = ehVar;
        this.b = view;
        this.c = i;
    }

    public final void safeRun() {
        v.a(this.d, this.a, (ImageView) this.b, this.c);
    }
}
