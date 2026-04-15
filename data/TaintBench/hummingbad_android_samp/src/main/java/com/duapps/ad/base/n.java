package com.duapps.ad.base;

import android.os.SystemClock;
import com.duapps.ad.AdError;
import com.duapps.ad.base.w.a;
import com.duapps.ad.entity.c;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ToolCacheManager */
final class n extends b {
    private /* synthetic */ long a;
    private /* synthetic */ m b;

    n(m mVar, long j) {
        this.b = mVar;
        this.a = j;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        a aVar = (a) obj;
        JSONObject jSONObject;
        a a;
        if (i == CtaButton.WIDTH_DIPS && aVar != null) {
            try {
                JSONObject jSONObject2 = aVar.a;
                jSONObject = jSONObject2.getJSONObject("datas");
                f.a("ToolboxCacheManager", "getOnlineWall sType :" + this.b.c + ", response ->" + jSONObject2.toString());
                a = this.b.g.a(this.b.e);
                a.b = aVar.a.toString();
                a.c = System.currentTimeMillis();
                a.a = this.b.e;
                this.b.g.a(a);
                c cVar = new c(this.b.a, this.b.b, this.b.c, jSONObject, a.c);
                g.a(this.b.g.d).a(cVar.a);
                this.b.f.a(i, cVar);
                i.a(this.b.g.d, this.b.b, aVar.c);
            } catch (JSONException e) {
                f.a("ToolboxCacheManager", "getOnlineWall sType :" + this.b.c + ",parse JsonException :", e);
                this.b.f.a(AdError.SERVER_ERROR.getErrorMessage());
                d.c(this.b.g.d, this.b.b, -101, SystemClock.elapsedRealtime() - this.a);
                return;
            }
        } else if (i == 304) {
            f.c("ToolboxCacheManager", "Url->" + this.b.d + " 304 called.");
            a = this.b.g.a(this.b.e);
            if (a.b == null) {
                this.b.f.a(AdError.NO_FILL.getErrorMessage());
                return;
            }
            a.c = System.currentTimeMillis();
            this.b.g.a(a);
            jSONObject = new JSONObject(a.b).getJSONObject("datas");
            f.a("ToolboxCacheManager", "getOnlineWall sType :" + this.b.c + " from cache, response ->" + aVar);
            this.b.f.a(CtaButton.WIDTH_DIPS, new c(this.b.a, this.b.b, this.b.c, jSONObject, a.c));
        }
        d.c(this.b.g.d, this.b.b, i, SystemClock.elapsedRealtime() - this.a);
    }

    public final void a(int i, String str) {
        f.c("ToolboxCacheManager", "getOnlineWall sType :" + this.b.c + ", parse failed: " + str);
        this.b.f.a(AdError.INTERNAL_ERROR.getErrorMessage());
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.a;
        if (i != 204) {
            d.a("ol", i, this.b.g.d, this.b.b, elapsedRealtime, this.b.c, this.b.a, str);
        }
        d.c(this.b.g.d, this.b.b, i, SystemClock.elapsedRealtime() - this.a);
    }
}
