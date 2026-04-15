package com.cleanmaster.ui.app.market.data.filter;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: free_space_condition */
public class f extends b {
    /* access modifiers changed from: protected */
    public boolean b(a aVar) {
        return b(aVar.f()) && a(aVar.f());
    }

    public static f b(JSONObject jSONObject) {
        f fVar = new f();
        try {
            fVar.a(jSONObject.getJSONObject("free_space_condition"));
            return fVar;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
