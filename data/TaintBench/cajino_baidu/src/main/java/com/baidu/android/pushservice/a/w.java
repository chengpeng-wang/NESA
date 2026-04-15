package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class w extends e {
    protected String e = null;
    protected String f = null;
    protected String g = null;
    protected String h = null;

    public w(l lVar, Context context, String str, String str2, String str3, String str4) {
        super(lVar, context);
        this.e = str;
        this.f = str2;
        this.g = str3;
        this.h = str4;
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        super.a(list);
        list.add(new BasicNameValuePair("method", "sendmsgtouser"));
        list.add(new BasicNameValuePair("appid", this.e));
        list.add(new BasicNameValuePair(PushConstants.EXTRA_USER_ID, this.f));
        if (this.h != null && this.g != null) {
            Log.d(PushConstants.EXTRA_USER_ID, this.f);
            StringBuilder stringBuilder = new StringBuilder("[\"");
            stringBuilder.append(this.g).append("\"]");
            StringBuilder stringBuilder2 = new StringBuilder("[\"");
            stringBuilder2.append(this.h).append("\"]");
            list.add(new BasicNameValuePair("msg_keys", stringBuilder.toString()));
            list.add(new BasicNameValuePair("messages", stringBuilder2.toString()));
            Log.d("Send", "key:" + this.g.toString() + " messages:" + this.h.toString());
            if (b.a()) {
                for (NameValuePair obj : list) {
                    Log.d("Send", "sendMsgToUser param -- " + obj.toString());
                }
            }
        }
    }
}
