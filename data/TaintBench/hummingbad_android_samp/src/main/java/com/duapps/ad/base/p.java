package com.duapps.ad.base;

import android.os.SystemClock;
import com.cmcm.adsdk.Const;
import com.duapps.ad.AdError;
import com.duapps.ad.b.c;
import com.duapps.ad.base.w.a;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ToolCacheManager */
final class p extends b {
    private /* synthetic */ long a;
    private /* synthetic */ o b;

    p(o oVar, long j) {
        this.b = oVar;
        this.a = j;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        a aVar = (a) obj;
        if (i == CtaButton.WIDTH_DIPS && aVar != null) {
            try {
                JSONObject jSONObject = aVar.a;
                JSONObject jSONObject2 = jSONObject.getJSONObject("datas");
                f.a("ToolboxCacheManager", "getInmobiNativeAds sType :native, response ->" + jSONObject.toString());
                this.b.c.a(i, new c(this.b.a, this.b.b, MASTNativeAdConstants.RESPONSE_NATIVE_STRING, jSONObject2, System.currentTimeMillis()));
            } catch (JSONException e) {
                f.a("ToolboxCacheManager", "getInmobiNativeAds sType :native,parse JsonException :", e);
                this.b.c.a(AdError.SERVER_ERROR.getErrorMessage());
                d.b(this.b.d.d, this.b.b, -101, SystemClock.elapsedRealtime() - this.a);
                return;
            }
        }
        d.b(this.b.d.d, this.b.b, i, SystemClock.elapsedRealtime() - this.a);
    }

    public final void a(int i, String str) {
        f.c("ToolboxCacheManager", "getInmobiNativeAds sType :native, parse failed: " + str);
        this.b.c.a(AdError.INTERNAL_ERROR.getErrorMessage());
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.a;
        if (i != 204) {
            d.a(Const.KEY_IM, i, this.b.d.d, this.b.b, elapsedRealtime, MASTNativeAdConstants.RESPONSE_NATIVE_STRING, this.b.a, str);
        }
        d.b(this.b.d.d, this.b.b, i, SystemClock.elapsedRealtime() - this.a);
    }
}
