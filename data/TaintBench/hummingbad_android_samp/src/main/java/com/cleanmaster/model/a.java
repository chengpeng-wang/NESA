package com.cleanmaster.model;

import android.text.TextUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.VastIconXmlManager;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: BuinessDataItem */
public class a {
    String a;
    int b;
    int c;
    String d;
    int e;
    String f;
    String g;
    int h;
    int i;
    int j;

    public a(String str, int i, int i2, String str2) {
        this(str, i, i2, str2, 0, 0, 0);
    }

    public a(String str, int i, int i2, String str2, int i3, int i4, int i5) {
        if (!TextUtils.isEmpty(str)) {
            this.a = str.replace(MASTNativeAdConstants.AMPERSAND, "_");
        }
        this.b = i;
        this.c = i2;
        this.d = str2;
        this.h = i3;
        this.i = i4;
        this.j = i5;
    }

    public void a(String str, String str2) {
        this.f = str;
        this.g = str2;
    }

    public String a() {
        JSONObject a = a(this.a, this.b, this.c, this.d, this.f, this.g, this.h, this.i, this.j);
        if (a != null) {
            return a.toString();
        }
        return null;
    }

    public JSONObject a(String str, int i, int i2, String str2, String str3, String str4, int i3, int i4, int i5) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("pkg", str);
            jSONObject.put("sug", i);
            jSONObject.put("res", i2);
            jSONObject.put("des", str2);
            if (!(TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4))) {
                jSONObject.put("fbpos", str3);
                jSONObject.put("fbmeta", str4);
            }
            if (this.e > 0) {
                jSONObject.put("seq", this.e);
            }
            if (i3 > 0) {
                jSONObject.put(VastIconXmlManager.DURATION, i3);
                jSONObject.put("playtime", i4);
                jSONObject.put("event", i5);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public a a(int i) {
        this.e = i;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("pkg    =").append(this.a).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * sug =").append(this.b).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * res =").append(this.c).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * des =").append(this.d).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * seq =").append(this.e).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * placementid=").append(this.f).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * rawJson=").append(this.g).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * duration=").append(this.h).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * playtime=").append(this.i).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * event=").append(this.j).append(MASTNativeAdConstants.NEWLINE);
        return stringBuilder.toString();
    }
}
