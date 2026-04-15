package com.baidu.mapapi.map;

import com.baidu.platform.comapi.a.a;
import com.baidu.platform.comapi.basestruct.c;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationData {
    public float accuracy;
    public float direction;
    public double latitude;
    public double longitude;
    public int satellitesNum;
    public float speed;

    /* access modifiers changed from: 0000 */
    public String a() {
        JSONObject jSONObject = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject2 = new JSONObject();
        c a = a.a().a((float) this.longitude, (float) this.latitude, "bd09ll");
        if (a != null) {
            try {
                jSONObject.put("type", 0);
                jSONObject2.put("ptx", a.a);
                jSONObject2.put("pty", a.b);
                jSONObject2.put("radius", (double) this.accuracy);
                jSONObject2.put("direction", (double) this.direction);
                jSONObject2.put("iconarrownor", "NormalLocArrow");
                jSONObject2.put("iconarrownorid", 28);
                jSONObject2.put("iconarrowfoc", "FocusLocArrow");
                jSONObject2.put("iconarrowfocid", 29);
                jSONArray.put(jSONObject2);
                jSONObject.put(Data.ELEMENT_NAME, jSONArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject.toString();
    }
}
