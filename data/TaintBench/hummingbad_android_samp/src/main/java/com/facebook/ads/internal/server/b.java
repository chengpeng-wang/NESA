package com.facebook.ads.internal.server;

import com.facebook.ads.internal.dto.a;
import com.facebook.ads.internal.dto.c;
import com.facebook.ads.internal.dto.d;
import com.facebook.ads.internal.util.r;
import com.moceanmobile.mast.MASTNativeAdConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class b {
    private static b a = new b();

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            bVar = a;
        }
        return bVar;
    }

    private d a(JSONObject jSONObject) {
        int i = 0;
        JSONObject jSONObject2 = jSONObject.getJSONArray("placements").getJSONObject(0);
        d a = d.a(jSONObject2.getJSONObject("definition"));
        c cVar = new c(a);
        AdPlacementType a2 = a.a();
        if (jSONObject2.has(MASTNativeAdConstants.RESPONSE_ADS)) {
            JSONArray jSONArray = jSONObject2.getJSONArray(MASTNativeAdConstants.RESPONSE_ADS);
            while (i < jSONArray.length()) {
                a aVar = new a(a2);
                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                aVar.a(jSONObject3.getString("adapter"));
                JSONObject jSONObject4 = jSONObject3.getJSONObject("data");
                JSONArray optJSONArray = jSONObject3.optJSONArray("trackers");
                if (optJSONArray != null) {
                    jSONObject4.put("trackers", optJSONArray);
                }
                aVar.a(jSONObject4);
                cVar.a(aVar);
                i++;
            }
        }
        return new d(cVar);
    }

    private e b(JSONObject jSONObject) {
        try {
            return new e(jSONObject.optString("message", ""), jSONObject.optInt(MASTNativeAdConstants.RESPONSE_ERROR_CODE, 0), new c(d.a(jSONObject.getJSONArray("placements").getJSONObject(0).getJSONObject("definition"))));
        } catch (JSONException e) {
            return c(jSONObject);
        }
    }

    private e c(JSONObject jSONObject) {
        return new e(jSONObject.optString("message", ""), jSONObject.optInt(MASTNativeAdConstants.RESPONSE_ERROR_CODE, 0), null);
    }

    public c a(String str) {
        if (!r.a(str)) {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("type");
            Object obj = -1;
            switch (optString.hashCode()) {
                case 96432:
                    if (optString.equals(MASTNativeAdConstants.RESPONSE_ADS)) {
                        obj = null;
                        break;
                    }
                    break;
                case 96784904:
                    if (optString.equals(MASTNativeAdConstants.RESPONSE_ERROR)) {
                        obj = 1;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    return a(jSONObject);
                case 1:
                    return b(jSONObject);
                default:
                    JSONObject optJSONObject = jSONObject.optJSONObject(MASTNativeAdConstants.RESPONSE_ERROR);
                    if (optJSONObject != null) {
                        return c(optJSONObject);
                    }
                    break;
            }
        }
        return new c(c.a.UNKNOWN, null);
    }
}
