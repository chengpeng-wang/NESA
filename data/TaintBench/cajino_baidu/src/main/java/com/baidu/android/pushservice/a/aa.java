package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class aa extends d {
    public aa(l lVar, Context context) {
        super(lVar, context);
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        b.a(list);
        list.add(new BasicNameValuePair("method", "unbindapp"));
        list.add(new BasicNameValuePair("appid", this.b.f));
        if (b.a()) {
            for (NameValuePair obj : list) {
                Log.d("UnbindApp", "UNBINDAPP param -- " + obj.toString());
            }
        }
    }
}
