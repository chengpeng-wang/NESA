package com.duapps.ad.base;

import android.os.SystemClock;
import com.duapps.ad.AdError;
import com.duapps.ad.base.w.a;
import com.duapps.ad.entity.c;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ToolCacheManager */
final class l extends b {
    private /* synthetic */ long a;
    private /* synthetic */ k b;

    l(k kVar, long j) {
        this.b = kVar;
        this.a = j;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        a aVar = (a) obj;
        JSONObject jSONObject;
        JSONObject jSONObject2;
        a a;
        if (i == CtaButton.WIDTH_DIPS && aVar != null) {
            try {
                jSONObject = aVar.a;
                jSONObject2 = jSONObject.getJSONObject("datas");
                f.a("ToolboxCacheManager", "getWall sType :" + this.b.d + "," + this.b.c + ", response ->" + jSONObject.toString());
                a = this.b.h.a(this.b.f);
                a.b = aVar.a.toString();
                a.c = System.currentTimeMillis();
                a.a = this.b.f;
                this.b.h.a(a);
                c cVar = new c(this.b.a, this.b.b, this.b.c, jSONObject2, a.c);
                g.a(this.b.h.d).a(cVar.a);
                this.b.h.a(cVar);
                this.b.g.a(i, cVar);
                i.a(this.b.h.d, this.b.b, aVar.c);
            } catch (JSONException e) {
                f.a("ToolboxCacheManager", "getWall sType :" + this.b.c + ",parse JsonException :", e);
                this.b.g.a(AdError.SERVER_ERROR.getErrorMessage());
                d.a(this.b.h.d, this.b.d, this.b.b, -101, SystemClock.elapsedRealtime() - this.a);
                return;
            }
        } else if (i == 304) {
            f.c("ToolboxCacheManager", "Url->" + this.b.e + " 304 called.");
            a = this.b.h.a(this.b.f);
            if (a.b == null) {
                this.b.g.a(AdError.NO_FILL.getErrorMessage());
                return;
            }
            a.c = System.currentTimeMillis();
            this.b.h.a(a);
            jSONObject = new JSONObject(a.b);
            jSONObject2 = jSONObject.getJSONObject("datas");
            f.a("ToolboxCacheManager", "getWall sType :" + this.b.c + " from cache, response ->" + jSONObject.toString());
            this.b.g.a(CtaButton.WIDTH_DIPS, new c(this.b.a, this.b.b, this.b.c, jSONObject2, a.c));
        }
        d.a(this.b.h.d, this.b.d, this.b.b, i, SystemClock.elapsedRealtime() - this.a);
    }

    public final void a(int i, String str) {
        f.c("ToolboxCacheManager", "getWall sType :" + this.b.c + ", parse failed: " + str);
        this.b.g.a(AdError.INTERNAL_ERROR.getErrorMessage());
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.a;
        if (i != 204) {
            d.a("dl", i, this.b.h.d, this.b.b, elapsedRealtime, this.b.d, this.b.a, str);
        }
        d.a(this.b.h.d, this.b.d, this.b.b, i, elapsedRealtime);
    }
}
