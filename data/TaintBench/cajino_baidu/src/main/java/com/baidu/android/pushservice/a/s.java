package com.baidu.android.pushservice.a;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.b;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class s extends c {
    private ArrayList e = new ArrayList();

    public s(l lVar, Context context) {
        super(lVar, context);
    }

    /* access modifiers changed from: protected */
    public void a(int i, byte[] bArr) {
        Intent intent = new Intent(PushConstants.ACTION_RECEIVE);
        intent.putExtra("method", this.b.a);
        intent.putExtra(PushConstants.EXTRA_ERROR_CODE, i);
        intent.putExtra("content", bArr);
        if (!this.e.isEmpty()) {
            intent.putStringArrayListExtra(PushConstants.EXTRA_TAGS_LIST, this.e);
        }
        intent.setFlags(32);
        a(intent);
        if (!TextUtils.isEmpty(this.b.e)) {
            intent.setPackage(this.b.e);
            if (b.a()) {
                Log.d("Glist", "> sendResult to " + this.b.e + " ,method:" + this.b.a + " ,errorCode : " + i + " ,content : " + new String(bArr));
            }
            this.a.sendBroadcast(intent);
        }
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

    /* access modifiers changed from: protected */
    public String b(String str) {
        String b = super.b(str);
        try {
            JSONArray jSONArray = new JSONObject(b).getJSONObject("response_params").getJSONArray("groups");
            for (int i = 0; i < jSONArray.length(); i++) {
                this.e.add(jSONArray.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }
}
