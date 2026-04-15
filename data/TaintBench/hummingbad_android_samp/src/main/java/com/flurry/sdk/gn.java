package com.flurry.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class gn implements lc {
    private static final String a = gn.class.getSimpleName();

    private static dt b(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        String str = new String(lt.a(inputStream));
        iw.a(5, a, "Ad response string: " + str);
        dt dtVar = new dt();
        try {
            JSONObject jSONObject = new JSONObject(str);
            dtVar.a = go.a(jSONObject);
            dtVar.b = go.b(jSONObject);
            ea eaVar = new ea();
            if (!jSONObject.isNull("configuration")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("configuration");
                if (!jSONObject2.isNull("com.flurry.adServer.networking.protocol.v14.Configuration")) {
                    jSONObject2 = jSONObject2.getJSONObject("com.flurry.adServer.networking.protocol.v14.Configuration");
                    dz dzVar = new dz();
                    dzVar.a = jSONObject2.getString("sdkAssetUrl");
                    dzVar.b = jSONObject2.getInt("cacheSizeMb");
                    dzVar.c = jSONObject2.getInt("maxAssetSizeKb");
                    dzVar.d = jSONObject2.getInt("maxBitRateKbps");
                    eaVar.a = dzVar;
                }
            }
            dtVar.d = eaVar;
            dtVar.c = lv.b(jSONObject.getJSONArray("errors"));
            return dtVar;
        } catch (JSONException e) {
            throw new IOException("Exception while deserialize:", e);
        }
    }

    public final /* synthetic */ Object a(InputStream inputStream) {
        return b(inputStream);
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        throw new IOException("Serialize not supported for response");
    }
}
