package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class u extends d {
    protected String f = null;

    public u(l lVar, Context context, String str) {
        super(lVar, context);
        this.f = str;
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        super.a(list);
        list.add(new BasicNameValuePair("method", "sendmsgtoserver"));
        if (this.f != null) {
            try {
                JSONObject jSONObject = new JSONObject(this.f);
                if (jSONObject.has("to")) {
                    list.add(new BasicNameValuePair(PushConstants.EXTRA_CB_URL, jSONObject.getString("to")));
                    Log.d("Send", jSONObject.getString("to"));
                }
                if (jSONObject.has("data")) {
                    list.add(new BasicNameValuePair("cb_data", jSONObject.getString("data")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (b.a()) {
                for (NameValuePair obj : list) {
                    Log.d("Send", "send param -- " + obj.toString());
                }
            }
        }
    }
}
