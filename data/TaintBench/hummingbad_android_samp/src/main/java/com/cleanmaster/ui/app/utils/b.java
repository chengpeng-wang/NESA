package com.cleanmaster.ui.app.utils;

import android.text.TextUtils;
import com.cleanmaster.ui.app.market.m;

/* compiled from: ParseUrlManager */
class b implements m {
    final /* synthetic */ String a;
    final /* synthetic */ a b;

    b(a aVar, String str) {
        this.b = aVar;
        this.a = str;
    }

    public void a(String str) {
        this.b.c.remove(this.a);
        if (!TextUtils.isEmpty(str)) {
            this.b.a(this.a, str);
        }
        this.b.b();
    }
}
