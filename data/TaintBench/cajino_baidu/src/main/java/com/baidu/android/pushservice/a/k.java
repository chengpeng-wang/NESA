package com.baidu.android.pushservice.a;

import android.content.Context;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

public class k extends c {
    String[] e;

    public k(l lVar, Context context, String[] strArr) {
        super(lVar, context);
        this.e = strArr;
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        super.a(list);
        if (r1 == 0) {
            a((int) PushConstants.ERROR_PARAMS_ERROR);
            if (b.a()) {
                Log.d("Delete", "Delete param -- msgIds == null");
                return;
            }
            return;
        }
        list.add(new BasicNameValuePair("method", "delete"));
        JSONArray jSONArray = new JSONArray();
        for (Object put : this.e) {
            jSONArray.put(put);
        }
        list.add(new BasicNameValuePair(PushConstants.EXTRA_MSG_IDS, jSONArray.toString()));
        if (b.a()) {
            for (NameValuePair obj : list) {
                Log.d("Delete", "Delete param -- " + obj.toString());
            }
        }
    }
}
