package com.cleanmaster.ui.app.market;

import android.content.Context;
import android.text.TextUtils;
import com.picksinit.ClickAdFinishListener;
import com.picksinit.SmartGoGp;

/* compiled from: MarketUtils */
final class g implements m {
    final /* synthetic */ Ad a;
    final /* synthetic */ String b;
    final /* synthetic */ ClickAdFinishListener c;
    final /* synthetic */ Context d;
    final /* synthetic */ String e;

    g(Ad ad, String str, ClickAdFinishListener clickAdFinishListener, Context context, String str2) {
        this.a = ad;
        this.b = str;
        this.c = clickAdFinishListener;
        this.d = context;
        this.e = str2;
    }

    public final void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (!c.a(str)) {
                str = "market://details?id=" + this.a.getPkg();
                c.b(this.b, this.a, null);
            }
            if (this.c != null) {
                this.c.onClickFinish(new SmartGoGp(str));
            } else {
                c.a(this.d, str);
            }
            c.a.b(this.e, str);
        }
    }
}
