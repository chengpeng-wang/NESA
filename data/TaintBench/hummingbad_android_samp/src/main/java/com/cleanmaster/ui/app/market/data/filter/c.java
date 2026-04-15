package com.cleanmaster.ui.app.market.data.filter;

import java.util.ArrayList;
import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: app_installed_condition */
public class c extends b {
    ArrayList e = new ArrayList();

    /* access modifiers changed from: protected */
    public boolean b(a aVar) {
        return a(aVar.e(), (Collection) this.e);
    }

    public static c b(JSONObject jSONObject) {
        c cVar = new c();
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("app_installed_condition");
            for (int i = 0; i < jSONArray.length(); i++) {
                cVar.e.add(jSONArray.getString(i));
            }
            return cVar;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
