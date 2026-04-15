package com.cleanmaster.ui.app.market.loader;

import com.cleanmaster.ui.app.market.Ad;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.picksinit.c;

/* compiled from: BaseMarketLoader */
class j implements Runnable {
    final /* synthetic */ i a;

    j(i iVar) {
        this.a = iVar;
    }

    public void run() {
        c.getInstance().reportRender(Ad.createAd(MASTNativeAdConstants.REQUESTPARAM_TEST), this.a.g(), "", 1);
    }
}
