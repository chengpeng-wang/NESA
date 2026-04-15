package com.baidu.android.pushservice.a;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushSettings;
import com.baidu.android.pushservice.a;
import com.baidu.android.pushservice.b;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class f extends d {
    protected int f = 0;
    private String g;
    private int h;

    public f(l lVar, Context context, int i, String str, int i2) {
        super(lVar, context);
        this.f = i;
        this.g = str;
        this.h = i2;
        if (this.f == 0) {
            this.e = true;
        }
    }

    /* access modifiers changed from: protected */
    public void a(Intent intent) {
        intent.putExtra(PushConstants.EXTRA_BIND_STATUS, this.f);
    }

    /* access modifiers changed from: protected */
    public void a(List list) {
        super.a(list);
        list.add(new BasicNameValuePair("method", "bind"));
        list.add(new BasicNameValuePair(PushConstants.EXTRA_BIND_NAME, TextUtils.isEmpty(this.g) ? Build.MODEL : this.g));
        list.add(new BasicNameValuePair(PushConstants.EXTRA_BIND_STATUS, this.f + ""));
        list.add(new BasicNameValuePair(PushConstants.EXTRA_PUSH_SDK_VERSION, this.h + ""));
        if (b.a()) {
            for (NameValuePair obj : list) {
                Log.d("Bind", "BIND param -- " + obj.toString());
            }
        }
    }

    /* access modifiers changed from: protected */
    public String b(String str) {
        String b = super.b(str);
        CharSequence charSequence = "";
        try {
            charSequence = new JSONObject(b).getJSONObject("response_params").getString("appid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(this.b.e)) {
            a.a(this.a).f(this.b.e);
            if (!TextUtils.isEmpty(this.b.i)) {
                a.a(this.a).a(this.b.e, new g(this.b.i, b));
                if (!TextUtils.isEmpty(charSequence)) {
                    PushSettings.a(charSequence, 0, this.b.i);
                }
            }
        }
        return b;
    }
}
