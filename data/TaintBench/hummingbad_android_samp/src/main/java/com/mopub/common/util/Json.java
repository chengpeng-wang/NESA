package com.mopub.common.util;

import android.text.TextUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.logging.MoPubLog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Json {
    public static Map<String, String> jsonStringToMap(String jsonParams) throws JSONException {
        HashMap hashMap = new HashMap();
        if (TextUtils.isEmpty(jsonParams)) {
            return hashMap;
        }
        JSONObject jSONObject = (JSONObject) new JSONTokener(jsonParams).nextValue();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            hashMap.put(str, jSONObject.getString(str));
        }
        return hashMap;
    }

    public static String mapToJsonString(Map<String, String> map) {
        if (map == null) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        Object obj = 1;
        Iterator it = map.entrySet().iterator();
        while (true) {
            Object obj2 = obj;
            if (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (obj2 == null) {
                    stringBuilder.append(",");
                }
                stringBuilder.append("\"");
                stringBuilder.append((String) entry.getKey());
                stringBuilder.append("\":\"");
                stringBuilder.append((String) entry.getValue());
                stringBuilder.append("\"");
                obj = null;
            } else {
                stringBuilder.append("}");
                return stringBuilder.toString();
            }
        }
    }

    public static String[] jsonArrayToStringArray(String jsonString) {
        try {
            JSONArray jSONArray = ((JSONObject) new JSONTokener("{key:" + jsonString + "}").nextValue()).getJSONArray(MASTNativeAdConstants.REQUESTPARAM_KEY);
            String[] strArr = new String[jSONArray.length()];
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = jSONArray.getString(i);
            }
            return strArr;
        } catch (JSONException e) {
            return new String[0];
        }
    }

    public static <T> T getJsonValue(JSONObject jsonObject, String key, Class<T> valueClass) {
        if (jsonObject == null || key == null || valueClass == null) {
            throw new IllegalArgumentException("Cannot pass any null argument to getJsonValue");
        }
        Object opt = jsonObject.opt(key);
        if (opt == null) {
            MoPubLog.w("Tried to get Json value with key: " + key + ", but it was null");
            return null;
        } else if (valueClass.isInstance(opt)) {
            return valueClass.cast(opt);
        } else {
            MoPubLog.w("Tried to get Json value with key: " + key + ", of type: " + valueClass.toString() + ", its type did not match");
            return null;
        }
    }
}
