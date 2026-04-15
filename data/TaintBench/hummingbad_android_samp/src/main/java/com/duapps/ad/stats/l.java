package com.duapps.ad.stats;

import android.os.SystemClock;
import android.util.Base64;
import com.duapps.ad.base.b;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.i;
import com.duapps.ad.base.w.a;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ToolStatsCore */
final class l extends b {
    private /* synthetic */ long a;
    private /* synthetic */ k b;

    l(k kVar, long j) {
        this.b = kVar;
        this.a = j;
    }

    public final /* synthetic */ void a(int i, Object obj) {
        a aVar = (a) obj;
        i.a(this.b.a.f, aVar.c);
        JSONObject jSONObject = aVar.a;
        if (jSONObject == null) {
            f.c("ToolStatsCore", "getSrc code :" + i + " ,\n responseJson is null!");
            return;
        }
        f.c("ToolStatsCore", "getSrc code :" + i + " ,\n response: " + jSONObject.toString());
        if (CtaButton.WIDTH_DIPS == i && jSONObject != null) {
            try {
                String str;
                jSONObject = jSONObject.getJSONObject("datas");
                JSONArray jSONArray = jSONObject.getJSONArray("list");
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i2);
                    if (optJSONObject != null) {
                        i.a(this.b.a.f, optJSONObject.optInt("sid"), optJSONObject.toString());
                    }
                }
                i.c(this.b.a.f, jSONObject.getInt("logPriority"));
                f.c("TEST", "getSrc logPriority :" + jSONObject.getInt("logPriority"));
                String optString = jSONObject.optString("imId");
                try {
                    f.c("TEST", "from server Inmobi ID = " + optString);
                    str = new String(com.duapps.ad.base.a.a("8a1n9d0i3c1y0c2f", "8a1n9d0i3c1y0c2f", Base64.decode(optString.getBytes(), 0)));
                    try {
                        f.c("TEST", "AES Inmobi ID = " + str);
                    } catch (Exception e) {
                    }
                } catch (Exception e2) {
                    str = optString;
                }
                i.b(this.b.a.f, str);
                f.c("TEST", "getSrc Inmobi ID = " + str);
                i.c(this.b.a.f);
            } catch (JSONException e3) {
                d.a(this.b.a.f, -101, SystemClock.elapsedRealtime() - this.a);
                return;
            }
        } else if (304 == i) {
            return;
        }
        d.a(this.b.a.f, i, SystemClock.elapsedRealtime() - this.a);
    }

    public final void a(int i, String str) {
        i.c(this.b.a.f);
        d.a(this.b.a.f, i, SystemClock.elapsedRealtime() - this.a);
    }
}
