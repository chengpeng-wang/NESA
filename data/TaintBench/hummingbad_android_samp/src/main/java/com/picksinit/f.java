package com.picksinit;

import com.cleanmaster.ui.app.market.Ad;
import com.cleanmaster.ui.app.market.storage.MarketStorage;

/* compiled from: PicksMobBase */
class f implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ Ad b;
    final /* synthetic */ c c;

    f(c cVar, String str, Ad ad) {
        this.c = cVar;
        this.a = str;
        this.b = ad;
    }

    public void run() {
        MarketStorage.a().a(this.a, this.b);
    }
}
