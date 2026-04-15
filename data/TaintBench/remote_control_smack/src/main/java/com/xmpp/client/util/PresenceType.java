package com.xmpp.client.util;

import android.content.Context;
import android.util.Log;
import com.android.service.R;
import java.io.IOException;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class PresenceType {
    public String Domainanme;
    public Context context;

    public PresenceType(Context context) {
        this.context = context;
        this.Domainanme = context.getString(R.string.hostname);
    }

    public Boolean GetPresenceType(String imei) {
        try {
            Log.e("url", "http://" + this.Domainanme + ":9090/plugins/presence/status?jid=" + imei + "@monkeydiy&type=text");
            String retSrc = EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet("http://" + this.Domainanme + ":9090/plugins/presence/status?jid=" + imei + "@monkeydiy&type=text")).getEntity());
            Log.e("Status", retSrc);
            String Src = retSrc.trim();
            if (Src.equalsIgnoreCase("null")) {
                return Boolean.valueOf(true);
            }
            Log.e("Status", Src);
            return Boolean.valueOf(false);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Status", "error");
            return Boolean.valueOf(true);
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.e("Status", "error");
            return Boolean.valueOf(true);
        }
    }
}
