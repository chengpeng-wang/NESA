package com.facebook.ads.internal.util;

import com.facebook.ads.internal.server.AdPlacementType;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class b {
    public static String a = null;
    private String b;
    private Map<String, Object> c;
    private int d;
    private String e;

    public enum a {
        OPEN_STORE(0),
        OPEN_LINK(1),
        XOUT(2),
        OPEN_URL(3),
        SHOW_INTERSTITIAL(4);
        
        int f;

        private a(int i) {
            this.f = i;
        }
    }

    public enum b {
        LOADING_AD(0);
        
        int b;

        private b(int i) {
            this.b = i;
        }
    }

    public b(String str, Map<String, Object> map, int i, String str2) {
        this.b = str;
        this.c = map;
        this.d = i;
        this.e = str2;
    }

    public static b a(long j, a aVar, String str) {
        long currentTimeMillis = System.currentTimeMillis();
        HashMap hashMap = new HashMap();
        hashMap.put("Time", String.valueOf(currentTimeMillis - j));
        hashMap.put("AdAction", String.valueOf(aVar.f));
        return new b("bounceback", hashMap, (int) (currentTimeMillis / 1000), str);
    }

    public static b a(b bVar, AdPlacementType adPlacementType, long j, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("LatencyType", String.valueOf(bVar.b));
        hashMap.put("AdPlacementType", adPlacementType.toString());
        hashMap.put("Time", String.valueOf(j));
        String str2 = "latency";
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (str == null) {
            str = a;
        }
        return new b(str2, hashMap, currentTimeMillis, str);
    }

    public static b a(Throwable th, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("ex", th.getClass().getSimpleName());
        hashMap.put("ex_msg", th.getMessage());
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        String str2 = MASTNativeAdConstants.RESPONSE_ERROR;
        if (str == null) {
            str = a;
        }
        return new b(str2, hashMap, currentTimeMillis, str);
    }

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("name", this.b);
            jSONObject.put("data", new JSONObject(this.c));
            jSONObject.put("time", this.d);
            jSONObject.put("request_id", this.e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
