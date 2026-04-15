package com.cleanmaster.ui.app.market.data.filter;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: app_installed_num */
public class d extends b {
    /* access modifiers changed from: protected */
    public boolean b(a aVar) {
        return b(aVar.c()) && a(aVar.c());
    }

    public static d b(JSONObject jSONObject) {
        d dVar = new d();
        try {
            dVar.a(jSONObject.getJSONObject("app_installed_num"));
            return dVar;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
