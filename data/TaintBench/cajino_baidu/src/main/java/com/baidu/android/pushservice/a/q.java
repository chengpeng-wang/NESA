package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class q extends c {
    public q(l lVar, Context context) {
        super(lVar, context);
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        super.a(list);
        list.add(new BasicNameValuePair("method", "glist"));
        if (b.a()) {
            for (NameValuePair obj : list) {
                Log.d("Glist", "Glist param -- " + obj.toString());
            }
        }
    }
}
