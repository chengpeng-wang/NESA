package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class m extends c {
    int e = 1;
    int f = 1;

    public m(l lVar, Context context, int i, int i2) {
        super(lVar, context);
        this.e = i;
        this.f = i2;
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        super.a(list);
        list.add(new BasicNameValuePair("method", "fetch"));
        list.add(new BasicNameValuePair(PushConstants.EXTRA_FETCH_TYPE, this.e + ""));
        list.add(new BasicNameValuePair(PushConstants.EXTRA_FETCH_NUM, this.f + ""));
        if (b.a()) {
            for (NameValuePair obj : list) {
                Log.d("Fetch", "FETCH param -- " + obj.toString());
            }
        }
    }
}
