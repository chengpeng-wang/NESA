package com.flurry.sdk;

import android.widget.Toast;

final class ff implements Runnable {
    final /* synthetic */ fe a;

    ff(fe feVar) {
        this.a = feVar;
    }

    public final void run() {
        Toast.makeText(hz.a.b, "Ad log report sent", 0).show();
    }
}
