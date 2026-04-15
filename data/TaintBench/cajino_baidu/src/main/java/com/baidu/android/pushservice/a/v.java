package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class v extends e {
    protected String e = null;
    protected String f = null;
    protected String g = null;

    public v(l lVar, Context context, String str, String str2, String str3) {
        super(lVar, context);
        this.e = str;
        this.f = str2;
        this.g = str3;
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        super.a(list);
        list.add(new BasicNameValuePair("method", "sendmsgtoserver"));
        list.add(new BasicNameValuePair("appid", this.e));
        if (this.g != null && this.f != null) {
            list.add(new BasicNameValuePair(PushConstants.EXTRA_CB_URL, this.f));
            Log.d("SendMsgToServer", "cb_url:" + this.f);
            list.add(new BasicNameValuePair("cb_data", this.g));
            Log.d("SendMsgToServer", "cb_data:" + this.g);
            if (b.a()) {
                for (NameValuePair obj : list) {
                    Log.d("SendMsgToServer", "SendMsgToServer param -- " + obj.toString());
                }
            }
        }
    }
}
