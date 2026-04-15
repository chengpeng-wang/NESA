package com.xmpp.client.util;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClinentModle {
    private Context ctx;
    private PushDialogModle pdm = new PushDialogModle(this.ctx);

    public HttpClinentModle(Context context) {
        this.ctx = context;
    }

    public String HttpClinentJason(String url) {
        try {
            String retSrc = EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(url)).getEntity());
            Log.e("getHttpClinentJason:", retSrc);
            return retSrc;
        } catch (IOException e) {
            this.pdm.PushAlertSingleButton("系統出錯", Log.getStackTraceString(e));
            return null;
        } catch (Exception e2) {
            this.pdm.PushAlertSingleButton("系統出錯", Log.getStackTraceString(e2));
            return null;
        }
    }
}
