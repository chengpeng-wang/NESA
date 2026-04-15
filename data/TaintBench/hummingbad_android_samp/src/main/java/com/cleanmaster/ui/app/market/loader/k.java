package com.cleanmaster.ui.app.market.loader;

import com.cleanmaster.ui.app.market.storage.MarketStorage;

/* compiled from: BaseMarketLoader */
class k implements Runnable {
    final /* synthetic */ i a;

    k(i iVar) {
        this.a = iVar;
    }

    public void run() {
        try {
            MarketStorage.a().b(this.a.g());
            MarketStorage.a().a(this.a.g());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
