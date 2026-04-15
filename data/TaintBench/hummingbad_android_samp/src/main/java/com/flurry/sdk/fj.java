package com.flurry.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class fj implements lc {
    private static final String a = fj.class.getSimpleName();

    private static en b(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        String str = new String(lt.a(inputStream));
        iw.a(5, a, "SDK Log response string: " + str);
        en enVar = new en();
        try {
            JSONObject jSONObject = new JSONObject(str);
            enVar.a = jSONObject.optString("result");
            enVar.b = fg.a(jSONObject);
            return enVar;
        } catch (JSONException e) {
            throw new IOException("Exception while deserialize:", e);
        }
    }

    public final /* synthetic */ Object a(InputStream inputStream) {
        return b(inputStream);
    }

    public final /* synthetic */ void a(OutputStream outputStream, Object obj) {
        throw new IOException(a + " Serialize not supported for response");
    }
}
