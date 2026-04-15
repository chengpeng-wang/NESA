package com.facebook.ads.internal.adapters;

import android.content.Intent;
import android.os.Bundle;
import com.facebook.ads.internal.util.e;
import com.facebook.ads.internal.util.f;
import com.facebook.ads.internal.util.f.a;
import com.facebook.ads.internal.util.g;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class k implements a {
    private final String a;
    private final String b;
    private final String c;
    private final e d;
    private final String e;
    private final Collection<String> f;
    private final Map<String, String> g;
    private final String h;
    private final int i;
    private final int j;

    private k(String str, String str2, String str3, e eVar, String str4, Collection<String> collection, Map<String, String> map, String str5, int i, int i2) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = eVar;
        this.e = str4;
        this.f = collection;
        this.g = map;
        this.h = str5;
        this.i = i;
        this.j = i2;
    }

    public static k a(Bundle bundle) {
        return new k(bundle.getString("markup"), null, bundle.getString("native_impression_report_url"), e.NONE, "", null, null, bundle.getString("request_id"), bundle.getInt("viewability_check_initial_delay"), bundle.getInt("viewability_check_interval"));
    }

    public static k a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        JSONArray jSONArray;
        String optString = jSONObject.optString("markup");
        String optString2 = jSONObject.optString("activation_command");
        String optString3 = jSONObject.optString("native_impression_report_url");
        String optString4 = jSONObject.optString("request_id");
        e a = e.a(jSONObject.optString("invalidation_behavior"));
        String optString5 = jSONObject.optString("invalidation_report_url");
        try {
            jSONArray = new JSONArray(jSONObject.optString("detection_strings"));
        } catch (JSONException e) {
            e.printStackTrace();
            jSONArray = null;
        }
        Collection a2 = f.a(jSONArray);
        JSONObject optJSONObject = jSONObject.optJSONObject("metadata");
        HashMap hashMap = new HashMap();
        if (optJSONObject != null) {
            Iterator keys = optJSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                hashMap.put(str, optJSONObject.optString(str));
            }
        }
        int i = 0;
        int i2 = 1000;
        if (hashMap.containsKey("viewability_check_initial_delay")) {
            i = Integer.parseInt((String) hashMap.get("viewability_check_initial_delay"));
        }
        if (hashMap.containsKey("viewability_check_interval")) {
            i2 = Integer.parseInt((String) hashMap.get("viewability_check_interval"));
        }
        return new k(optString, optString2, optString3, a, optString5, a2, hashMap, optString4, i, i2);
    }

    public static k b(Intent intent) {
        return new k(g.a(intent.getByteArrayExtra("markup")), intent.getStringExtra("activation_command"), intent.getStringExtra("native_impression_report_url"), e.NONE, "", null, null, intent.getStringExtra("request_id"), intent.getIntExtra("viewability_check_initial_delay", 0), intent.getIntExtra("viewability_check_interval", 1000));
    }

    public e a() {
        return this.d;
    }

    public void a(Intent intent) {
        intent.putExtra("markup", g.a(this.a));
        intent.putExtra("activation_command", this.b);
        intent.putExtra("native_impression_report_url", this.c);
        intent.putExtra("request_id", this.h);
        intent.putExtra("viewability_check_initial_delay", this.i);
        intent.putExtra("viewability_check_interval", this.j);
    }

    public String b() {
        return this.e;
    }

    public Collection<String> c() {
        return this.f;
    }

    public String d() {
        return this.a;
    }

    public String e() {
        return this.b;
    }

    public String f() {
        return this.c;
    }

    public String g() {
        return "facebookAd.sendImpression();";
    }

    public Map<String, String> h() {
        return this.g;
    }

    public String i() {
        return this.h;
    }

    public int j() {
        return this.i;
    }

    public int k() {
        return this.j;
    }

    public Bundle l() {
        Bundle bundle = new Bundle();
        bundle.putString("markup", this.a);
        bundle.putString("native_impression_report_url", this.c);
        bundle.putString("request_id", this.h);
        bundle.putInt("viewability_check_initial_delay", this.i);
        bundle.putInt("viewability_check_interval", this.j);
        return bundle;
    }
}
