package com.cleanmaster.ui.app.market.data.filter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: app_uninstalled_condition */
public class e extends c {
    /* access modifiers changed from: protected */
    public boolean b(a aVar) {
        return !super.b(aVar);
    }

    public static e c(JSONObject jSONObject) {
        e eVar = new e();
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("app_uninstalled_condition");
            for (int i = 0; i < jSONArray.length(); i++) {
                eVar.e.add(jSONArray.getString(i));
            }
            return eVar;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
