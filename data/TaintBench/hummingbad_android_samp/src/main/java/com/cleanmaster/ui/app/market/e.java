package com.cleanmaster.ui.app.market;

import com.cleanmaster.ui.app.market.transport.CmMarketHttpClient;
import com.moceanmobile.mast.MASTNativeAdConstants;
import org.json.JSONObject;

/* compiled from: MarketUtils */
final class e implements Runnable {
    final /* synthetic */ JSONObject a;

    e(JSONObject jSONObject) {
        this.a = jSONObject;
    }

    public final void run() {
        try {
            CmMarketHttpClient.a(null, this.a.optString(MASTNativeAdConstants.RESPONSE_URL, ""), true);
        } catch (Exception e) {
        }
    }
}
