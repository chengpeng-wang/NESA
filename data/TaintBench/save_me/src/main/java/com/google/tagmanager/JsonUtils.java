package com.google.tagmanager;

import com.google.analytics.containertag.common.Key;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.tagmanager.ResourceUtil.ExpandedFunctionCall;
import com.google.tagmanager.ResourceUtil.ExpandedResource;
import com.google.tagmanager.ResourceUtil.ExpandedResourceBuilder;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JsonUtils {
    private JsonUtils() {
    }

    public static ExpandedResource expandedResourceFromJsonString(String jsonString) throws JSONException {
        Value value = jsonObjectToValue(new JSONObject(jsonString));
        ExpandedResourceBuilder builder = ExpandedResource.newBuilder();
        for (int i = 0; i < value.getMapKeyCount(); i++) {
            builder.addMacro(ExpandedFunctionCall.newBuilder().addProperty(Key.INSTANCE_NAME.toString(), value.getMapKey(i)).addProperty(Key.FUNCTION.toString(), Types.functionIdToValue(ConstantMacro.getFunctionId())).addProperty(ConstantMacro.getValueKey(), value.getMapValue(i)).build());
        }
        return builder.build();
    }

    private static Value jsonObjectToValue(Object o) throws JSONException {
        return Types.objectToValue(jsonObjectToObject(o));
    }

    @VisibleForTesting
    static Object jsonObjectToObject(Object o) throws JSONException {
        if (o instanceof JSONArray) {
            throw new RuntimeException("JSONArrays are not supported");
        } else if (JSONObject.NULL.equals(o)) {
            throw new RuntimeException("JSON nulls are not supported");
        } else if (!(o instanceof JSONObject)) {
            return o;
        } else {
            JSONObject jsonObject = (JSONObject) o;
            Object hashMap = new HashMap();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                hashMap.put(key, jsonObjectToObject(jsonObject.get(key)));
            }
            return hashMap;
        }
    }
}
