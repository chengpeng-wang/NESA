package com.facebook.ads.internal.dto;

import com.facebook.ads.internal.server.AdPlacementType;
import org.json.JSONObject;

public class a {
    public final AdPlacementType a;
    public String b;
    public JSONObject c;

    public a(AdPlacementType adPlacementType) {
        this.a = adPlacementType;
    }

    public void a(String str) {
        this.b = str;
    }

    public void a(JSONObject jSONObject) {
        this.c = jSONObject;
    }
}
