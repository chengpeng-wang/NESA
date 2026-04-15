package com.flurry.sdk;

import android.graphics.Bitmap;
import android.widget.ImageView;

final class y extends lz {
    final /* synthetic */ ImageView a;
    final /* synthetic */ Bitmap b;
    final /* synthetic */ v c;

    y(v vVar, ImageView imageView, Bitmap bitmap) {
        this.c = vVar;
        this.a = imageView;
        this.b = bitmap;
    }

    public final void safeRun() {
        this.a.setImageBitmap(this.b);
    }
}
