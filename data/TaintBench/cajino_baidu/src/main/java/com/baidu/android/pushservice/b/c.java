package com.baidu.android.pushservice.b;

import android.location.Location;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.b;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c {
    public List a = new ArrayList();
    private final String b = "AppStatInfo";
    private int c = 0;
    private Location d;
    private int e;
    private String f;
    private final String g = "3";

    public String a() {
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject = new JSONObject();
        try {
            for (d a : this.a) {
                jSONArray.put(a.a());
            }
            JSONObject jSONObject2 = new JSONObject();
            if (this.d != null) {
                jSONObject2.put("latitude", this.d.getLatitude());
                jSONObject2.put("longitude", this.d.getLongitude());
                jSONObject2.put("altitude", this.d.getAltitude());
            }
            jSONObject.put("id", this.c);
            jSONObject.put("gps_location", jSONObject2);
            jSONObject.put("cell_id", this.e);
            jSONObject.put("wifi", this.f);
            jSONObject.put("platform", "3");
            jSONObject.put("items", jSONArray);
            return jSONObject.toString();
        } catch (JSONException e) {
            if (b.a()) {
                Log.e("AppStatInfo", "export json exception, return null");
            }
            return null;
        }
    }

    public void a(int i, Location location, int i2, String str) {
        this.c = i;
        this.d = location;
        this.e = i2;
        this.f = str;
    }

    public void a(d dVar) {
        this.a.add(dVar);
    }
}
