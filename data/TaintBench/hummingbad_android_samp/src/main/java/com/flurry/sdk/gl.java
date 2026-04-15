package com.flurry.sdk;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class gl implements lc {
    private static final String a = gl.class.getSimpleName();

    private static JSONArray a(List list) {
        JSONArray jSONArray = new JSONArray();
        for (eo eoVar : list) {
            JSONObject jSONObject = new JSONObject();
            lv.a(jSONObject, "adId", eoVar.a);
            lv.a(jSONObject, "lastEvent", eoVar.b);
            jSONObject.put("renderedTime", eoVar.c);
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    private static JSONArray b(List list) {
        JSONArray jSONArray = new JSONArray();
        for (eb ebVar : list) {
            JSONObject jSONObject = new JSONObject();
            lv.a(jSONObject, "capType", ebVar.a);
            lv.a(jSONObject, MASTNativeAdConstants.ID_STRING, ebVar.b);
            jSONObject.put("serveTime", ebVar.c);
            jSONObject.put("expirationTime", ebVar.d);
            jSONObject.put("lastViewedTime", ebVar.e);
            jSONObject.put("streamCapDurationMillis", ebVar.f);
            jSONObject.put("views", ebVar.g);
            jSONObject.put("capRemaining", ebVar.h);
            jSONObject.put("totalCap", ebVar.i);
            jSONObject.put("capDurationType", ebVar.j);
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    private static JSONArray c(List list) {
        JSONArray jSONArray = new JSONArray();
        for (dr drVar : list) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", drVar.a);
            lv.a(jSONObject, MASTNativeAdConstants.ID_STRING, drVar.b);
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    public final /* synthetic */ Object a(InputStream inputStream) {
        throw new IOException("Deserialize not supported for request");
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        ds dsVar = (ds) obj;
        if (outputStream != null && dsVar != null) {
            gm gmVar = new gm(this, outputStream);
            JSONObject jSONObject = new JSONObject();
            try {
                JSONObject jSONObject2;
                jSONObject.put("requestTime", dsVar.a);
                lv.a(jSONObject, "apiKey", dsVar.b);
                lv.a(jSONObject, "agentVersion", dsVar.c);
                lv.a(jSONObject, "adViewType", dsVar.d.toString());
                lv.a(jSONObject, "adSpaceName", dsVar.e);
                jSONObject.put("sessionId", dsVar.f);
                lv.a(jSONObject, "adReportedIds", c(dsVar.g));
                String str = "location";
                ee eeVar = dsVar.h;
                JSONObject jSONObject3 = new JSONObject();
                if (eeVar != null) {
                    lv.a(jSONObject3, MASTNativeAdConstants.REQUESTPARAM_LATITUDE, eeVar.a);
                    lv.a(jSONObject3, "lon", eeVar.b);
                } else {
                    lv.a(jSONObject3, MASTNativeAdConstants.REQUESTPARAM_LATITUDE, 0.0f);
                    lv.a(jSONObject3, "lon", 0.0f);
                }
                lv.a(jSONObject, str, (Object) jSONObject3);
                jSONObject.put("testDevice", dsVar.i);
                lv.a(jSONObject, "bindings", new JSONArray(dsVar.j));
                String str2 = "adViewContainer";
                dw dwVar = dsVar.k;
                Object jSONObject22 = new JSONObject();
                if (dwVar != null) {
                    jSONObject22.put("viewWidth", dwVar.a);
                    jSONObject22.put("viewHeight", dwVar.b);
                    jSONObject22.put("screenHeight", dwVar.d);
                    jSONObject22.put("screenWidth", dwVar.c);
                    lv.a((JSONObject) jSONObject22, "density", dwVar.e);
                    lv.a((JSONObject) jSONObject22, "screenOrientation", dwVar.f);
                } else {
                    jSONObject22 = (JSONObject) JSONObject.NULL;
                }
                lv.a(jSONObject, str2, jSONObject22);
                lv.a(jSONObject, "locale", dsVar.l);
                lv.a(jSONObject, "timezone", dsVar.m);
                lv.a(jSONObject, "osVersion", dsVar.n);
                lv.a(jSONObject, "devicePlatform", dsVar.o);
                lv.a(jSONObject, "keywords", new JSONObject(dsVar.p));
                jSONObject.put("canDoSKAppStore", dsVar.q);
                jSONObject.put("networkStatus", dsVar.r);
                lv.a(jSONObject, "frequencyCapRequestInfoList", b(dsVar.s));
                lv.a(jSONObject, "streamInfoList", a(dsVar.t));
                jSONObject.put("adTrackingEnabled", dsVar.u);
                lv.a(jSONObject, "preferredLanguage", dsVar.v);
                lv.a(jSONObject, "bcat", new JSONArray(dsVar.w));
                lv.a(jSONObject, "userAgent", dsVar.x);
                str = "targetingOverride";
                ep epVar = dsVar.y;
                jSONObject3 = new JSONObject();
                if (epVar != null) {
                    jSONObject3.put("ageRange", epVar.a);
                    jSONObject3.put("gender", epVar.b);
                    lv.a(jSONObject3, "personas", new JSONArray(epVar.c));
                } else {
                    jSONObject3.put("ageRange", -2);
                    jSONObject3.put("gender", -2);
                    lv.a(jSONObject3, "personas", Collections.emptyList());
                }
                lv.a(jSONObject, str, (Object) jSONObject3);
                jSONObject.put("sendConfiguration", dsVar.z);
                lv.a(jSONObject, "origins", new JSONArray(dsVar.A));
                jSONObject.put("renderTime", dsVar.B);
                lv.a(jSONObject, "clientSideRtbPayload", new JSONObject(dsVar.C));
                str2 = "nativeAdConfiguration";
                ef efVar = dsVar.D;
                if (efVar == null) {
                    jSONObject22 = (JSONObject) JSONObject.NULL;
                } else {
                    jSONObject22 = new JSONObject();
                    if (efVar.a != null) {
                        lv.a(jSONObject22, "requestedStyles", new JSONArray(efVar.a));
                    } else {
                        lv.a(jSONObject22, "requestedStyles", new JSONArray(Collections.emptyList()));
                    }
                    if (efVar.b != null) {
                        lv.a(jSONObject22, "requestedAssets", new JSONArray(efVar.b));
                    } else {
                        lv.a(jSONObject22, "requestedAssets", JSONObject.NULL);
                    }
                }
                lv.a(jSONObject, str2, jSONObject22);
                lv.a(jSONObject, "bCookie", dsVar.E);
                lv.a(jSONObject, "appBundleId", dsVar.F);
                iw.a(5, a, "Ad Request String: " + jSONObject.toString());
                gmVar.write(jSONObject.toString().getBytes());
                gmVar.flush();
                gmVar.close();
            } catch (JSONException e) {
                throw new IOException("Invalid Json", e);
            } catch (Throwable th) {
                gmVar.close();
            }
        }
    }
}
