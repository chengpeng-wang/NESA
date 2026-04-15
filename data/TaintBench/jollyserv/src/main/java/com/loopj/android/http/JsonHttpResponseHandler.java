package com.loopj.android.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonHttpResponseHandler extends AsyncHttpResponseHandler {
    public void onSuccess(JSONObject jSONObject) {
    }

    public void onSuccess(JSONArray jSONArray) {
    }

    /* access modifiers changed from: protected */
    public void handleSuccessMessage(String str) {
        super.handleSuccessMessage(str);
        try {
            Object parseResponse = parseResponse(str);
            if (parseResponse instanceof JSONObject) {
                onSuccess((JSONObject) parseResponse);
            } else if (parseResponse instanceof JSONArray) {
                onSuccess((JSONArray) parseResponse);
            } else {
                throw new JSONException("Unexpected type " + parseResponse.getClass().getName());
            }
        } catch (JSONException e) {
            onFailure(e, str);
        }
    }

    /* access modifiers changed from: protected */
    public Object parseResponse(String str) throws JSONException {
        return new JSONTokener(str).nextValue();
    }

    public void onFailure(Throwable th, JSONObject jSONObject) {
    }

    public void onFailure(Throwable th, JSONArray jSONArray) {
    }

    /* access modifiers changed from: protected */
    public void handleFailureMessage(Throwable th, String str) {
        super.handleFailureMessage(th, str);
        if (str != null) {
            try {
                Object parseResponse = parseResponse(str);
                if (parseResponse instanceof JSONObject) {
                    onFailure(th, (JSONObject) parseResponse);
                    return;
                } else if (parseResponse instanceof JSONArray) {
                    onFailure(th, (JSONArray) parseResponse);
                    return;
                } else {
                    return;
                }
            } catch (JSONException e) {
                onFailure(th, str);
                return;
            }
        }
        onFailure(th, "");
    }
}
