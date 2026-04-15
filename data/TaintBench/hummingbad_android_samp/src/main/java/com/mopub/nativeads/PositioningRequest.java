package com.mopub.nativeads;

import android.support.annotation.NonNull;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.common.VisibleForTesting;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.network.MoPubNetworkError;
import com.mopub.network.MoPubNetworkError.Reason;
import com.mopub.volley.NetworkResponse;
import com.mopub.volley.Response;
import com.mopub.volley.Response.ErrorListener;
import com.mopub.volley.Response.Listener;
import com.mopub.volley.VolleyError;
import com.mopub.volley.toolbox.HttpHeaderParser;
import com.mopub.volley.toolbox.JsonRequest;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PositioningRequest extends JsonRequest<MoPubClientPositioning> {
    private static final String FIXED_KEY = "fixed";
    private static final String INTERVAL_KEY = "interval";
    private static final int MAX_VALUE = 65536;
    private static final String POSITION_KEY = "position";
    private static final String REPEATING_KEY = "repeating";
    private static final String SECTION_KEY = "section";

    public PositioningRequest(String url, Listener<MoPubClientPositioning> listener, ErrorListener errorListener) {
        super(0, url, null, listener, errorListener);
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(MoPubClientPositioning response) {
        super.deliverResponse(response);
    }

    /* access modifiers changed from: protected */
    public Response<MoPubClientPositioning> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode != CtaButton.WIDTH_DIPS) {
            return Response.error(new VolleyError(response));
        }
        if (response.data.length == 0) {
            return Response.error(new VolleyError("Empty positioning response", new JSONException("Empty response")));
        }
        try {
            return Response.success(parseJson(new String(response.data, HttpHeaderParser.parseCharset(response.headers))), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError("Couldn't parse JSON from Charset", e));
        } catch (JSONException e2) {
            return Response.error(new VolleyError("JSON Parsing Error", e2));
        } catch (MoPubNetworkError e3) {
            return Response.error(e3);
        }
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    @VisibleForTesting
    public MoPubClientPositioning parseJson(@NonNull String jsonString) throws JSONException, MoPubNetworkError {
        JSONObject jSONObject = new JSONObject(jsonString);
        String optString = jSONObject.optString(MASTNativeAdConstants.RESPONSE_ERROR, null);
        if (optString == null) {
            JSONArray optJSONArray = jSONObject.optJSONArray(FIXED_KEY);
            jSONObject = jSONObject.optJSONObject(REPEATING_KEY);
            if (optJSONArray == null && jSONObject == null) {
                throw new JSONException("Must contain fixed or repeating positions");
            }
            MoPubClientPositioning moPubClientPositioning = new MoPubClientPositioning();
            if (optJSONArray != null) {
                parseFixedJson(optJSONArray, moPubClientPositioning);
            }
            if (jSONObject != null) {
                parseRepeatingJson(jSONObject, moPubClientPositioning);
            }
            return moPubClientPositioning;
        } else if (optString.equalsIgnoreCase("WARMING_UP")) {
            throw new MoPubNetworkError(Reason.WARMING_UP);
        } else {
            throw new JSONException(optString);
        }
    }

    private void parseFixedJson(@NonNull JSONArray fixed, @NonNull MoPubClientPositioning positioning) throws JSONException {
        for (int i = 0; i < fixed.length(); i++) {
            JSONObject jSONObject = fixed.getJSONObject(i);
            int optInt = jSONObject.optInt(SECTION_KEY, 0);
            if (optInt < 0) {
                throw new JSONException("Invalid section " + optInt + " in JSON response");
            }
            if (optInt <= 0) {
                int i2 = jSONObject.getInt(POSITION_KEY);
                if (i2 < 0 || i2 > 65536) {
                    throw new JSONException("Invalid position " + i2 + " in JSON response");
                }
                positioning.addFixedPosition(i2);
            }
        }
    }

    private void parseRepeatingJson(@NonNull JSONObject repeatingObject, @NonNull MoPubClientPositioning positioning) throws JSONException {
        int i = repeatingObject.getInt(INTERVAL_KEY);
        if (i < 2 || i > 65536) {
            throw new JSONException("Invalid interval " + i + " in JSON response");
        }
        positioning.enableRepeatingPositions(i);
    }
}
