package com.flurry.sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class lv {
    public static List a(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= jSONArray.length()) {
                return arrayList;
            }
            Object obj = jSONArray.get(i2);
            if (obj instanceof JSONObject) {
                arrayList.add((JSONObject) obj);
                i = i2 + 1;
            } else {
                throw new JSONException("Array contains unsupported objects. JSONArray param must contain JSON object.");
            }
        }
    }

    public static Map a(JSONObject jSONObject) {
        HashMap hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            Object next = keys.next();
            if (next instanceof String) {
                String str = (String) next;
                Object obj = jSONObject.get(str);
                if (obj instanceof String) {
                    hashMap.put(str, (String) obj);
                } else {
                    throw new JSONException("JSONObject contains unsupported type for value in the map.");
                }
            }
            throw new JSONException("JSONObject contains unsupported type for key in the map.");
        }
        return hashMap;
    }

    public static void a(JSONObject jSONObject, String str, float f) {
        jSONObject.putOpt(str, Float.valueOf(f));
    }

    public static void a(JSONObject jSONObject, String str, Object obj) {
        if (obj == null) {
            jSONObject.put(str, JSONObject.NULL);
        } else {
            jSONObject.put(str, obj);
        }
    }

    public static void a(JSONObject jSONObject, String str, String str2) {
        if (str2 != null) {
            jSONObject.put(str, str2);
        } else {
            jSONObject.put(str, JSONObject.NULL);
        }
    }

    public static List b(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= jSONArray.length()) {
                return arrayList;
            }
            Object obj = jSONArray.get(i2);
            if (obj instanceof String) {
                arrayList.add((String) obj);
                i = i2 + 1;
            } else {
                throw new JSONException("Array contains unsupported objects. JSONArray param must contain String object.");
            }
        }
    }
}
