package com.duapps.ad.c.a;

import android.content.Context;
import android.text.TextUtils;
import com.duapps.ad.base.i;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: PriorityPolicy */
public final class b implements Serializable {
    public List<String> a;
    private Map<String, Long> b;

    public final long a(String str) {
        if (this.b.containsKey(str)) {
            return ((Long) this.b.get(str)).longValue();
        }
        return 2000;
    }

    private static b a(JSONObject jSONObject) {
        b bVar = new b();
        jSONObject.optInt("sid");
        JSONArray optJSONArray = jSONObject.optJSONArray("priority");
        if (optJSONArray != null && optJSONArray.length() > 0) {
            int length = optJSONArray.length();
            bVar.a = new ArrayList(length);
            bVar.b = new HashMap(length);
            for (int i = 0; i < length; i++) {
                bVar.a.add(optJSONArray.optString(i));
            }
        }
        JSONObject optJSONObject = jSONObject.optJSONObject("wt");
        for (String str : bVar.a) {
            bVar.b.put(str, Long.valueOf(optJSONObject.optLong(str, 2000)));
        }
        return bVar;
    }

    public static b a(Context context, int i, boolean z) {
        String d = i.d(context, i);
        try {
            if (!TextUtils.isEmpty(d)) {
                return a(new JSONObject(d));
            }
        } catch (JSONException e) {
        }
        b bVar = new b();
        ArrayList arrayList = new ArrayList(2);
        bVar.a = arrayList;
        arrayList.add("facebook");
        if (z) {
            arrayList.add("dlh");
            arrayList.add("online");
            arrayList.add("inmobi");
        }
        arrayList.add("download");
        HashMap hashMap = new HashMap(2);
        bVar.b = hashMap;
        hashMap.put("facebook", Long.valueOf(2000));
        hashMap.put("download", Long.valueOf(2000));
        if (!z) {
            return bVar;
        }
        hashMap.put("dlh", Long.valueOf(2000));
        hashMap.put("inmobi", Long.valueOf(2000));
        hashMap.put("online", Long.valueOf(2000));
        return bVar;
    }
}
