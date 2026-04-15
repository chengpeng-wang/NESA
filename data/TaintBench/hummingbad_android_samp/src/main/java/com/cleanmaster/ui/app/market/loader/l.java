package com.cleanmaster.ui.app.market.loader;

import com.cleanmaster.ui.app.market.data.MarketResponse;

/* compiled from: BaseMarketLoader */
class l implements Runnable {
    final /* synthetic */ MarketResponse a;
    final /* synthetic */ i b;

    l(i iVar, MarketResponse marketResponse) {
        this.b = iVar;
        this.a = marketResponse;
    }

    public void run() {
        if (this.a == null) {
            this.b.d(this.a);
            return;
        }
        if (this.a.success()) {
            this.b.e(this.b.f(this.a));
        } else {
            this.b.d(this.a);
        }
        this.b.c("\n\n");
    }
}
