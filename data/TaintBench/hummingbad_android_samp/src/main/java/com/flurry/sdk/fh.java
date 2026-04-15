package com.flurry.sdk;

import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class fh implements lc {
    private static final String a = fh.class.getSimpleName();

    private static JSONArray a(List list) {
        JSONArray jSONArray = new JSONArray();
        for (dr drVar : list) {
            JSONObject jSONObject = new JSONObject();
            lv.a(jSONObject, MASTNativeAdConstants.ID_STRING, drVar.b);
            jSONObject.put("type", drVar.a);
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    private static JSONArray b(List list) {
        JSONArray jSONArray = new JSONArray();
        for (el elVar : list) {
            JSONObject jSONObject = new JSONObject();
            lv.a(jSONObject, "adLogGUID", elVar.b);
            jSONObject.put("sessionId", elVar.a);
            lv.a(jSONObject, "sdkAdEvents", c(elVar.c));
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    private static JSONArray c(List list) {
        JSONArray jSONArray = new JSONArray();
        for (ek ekVar : list) {
            JSONObject jSONObject = new JSONObject();
            lv.a(jSONObject, "type", ekVar.a);
            jSONObject.put("timeOffset", ekVar.c);
            lv.a(jSONObject, "params", new JSONObject(ekVar.b));
            jSONArray.put(jSONObject);
        }
        return jSONArray;
    }

    public final /* synthetic */ Object a(InputStream inputStream) {
        throw new IOException(a + " Deserialize not supported for log request");
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        fi fiVar;
        Throwable th;
        Throwable th2;
        em emVar = (em) obj;
        if (outputStream != null && emVar != null) {
            JSONObject jSONObject = new JSONObject();
            try {
                fiVar = new fi(this, outputStream);
                th = null;
                try {
                    lv.a(jSONObject, "apiKey", emVar.a);
                    jSONObject.put("testDevice", emVar.f);
                    lv.a(jSONObject, "agentVersion", emVar.e);
                    jSONObject.put("agentTimestamp", emVar.d);
                    lv.a(jSONObject, "adReportedIds", a(emVar.b));
                    lv.a(jSONObject, "sdkAdLogs", b(emVar.c));
                    fiVar.write(jSONObject.toString().getBytes());
                    fiVar.flush();
                    fiVar.close();
                    return;
                } catch (Throwable th3) {
                    Throwable th4 = th3;
                    th3 = th2;
                    th2 = th4;
                }
            } catch (JSONException e) {
                throw new IOException(a + " Invalid SdkLogRequest: " + emVar, e);
            }
        }
        return;
        throw th2;
        if (th3 != null) {
            try {
                fiVar.close();
            } catch (Throwable th5) {
                th3.addSuppressed(th5);
            }
        } else {
            fiVar.close();
        }
        throw th2;
    }
}
