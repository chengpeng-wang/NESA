package com.flurry.sdk;

import android.graphics.Bitmap;
import android.widget.ImageView;

final class x implements je {
    final /* synthetic */ ImageView a;
    final /* synthetic */ v b;

    x(v vVar, ImageView imageView) {
        this.b = vVar;
        this.a = imageView;
    }

    public final /* synthetic */ void a(jc jcVar, Object obj) {
        Bitmap bitmap = (Bitmap) obj;
        iw.a(3, v.a, "Image request -- HTTP status code is:" + jcVar.i);
        if (jcVar.b()) {
            this.b.a(this.a, bitmap);
        }
    }
}
