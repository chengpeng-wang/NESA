package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class i extends c {
    protected String e;

    public i(l lVar, Context context, String str) {
        super(lVar, context);
        this.e = str;
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        super.a(list);
        list.add(new BasicNameValuePair("method", "gmsgcount"));
        list.add(new BasicNameValuePair(PushConstants.EXTRA_GID, this.e));
        if (b.a()) {
            for (NameValuePair obj : list) {
                Log.d("CountGmsg", "CountGmsg param -- " + obj.toString());
            }
        }
    }
}
