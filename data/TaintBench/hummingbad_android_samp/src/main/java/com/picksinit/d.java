package com.picksinit;

import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient;

/* compiled from: PicksMobBase */
class d implements Runnable {
    final /* synthetic */ c a;

    d(c cVar) {
        this.a = cVar;
    }

    public void run() {
        CmMarketHttpClient.a().a(this.a.getMid());
    }
}
