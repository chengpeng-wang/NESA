package com.cleanmaster.ui.app.market.data.filter;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: total_space_condition */
public class h extends b {
    /* access modifiers changed from: protected */
    public boolean b(a aVar) {
        return a(aVar.d()) && b(aVar.d());
    }

    public static h b(JSONObject jSONObject) {
        h hVar = new h();
        try {
            hVar.a(jSONObject.getJSONObject("total_space_condition"));
            return hVar;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
