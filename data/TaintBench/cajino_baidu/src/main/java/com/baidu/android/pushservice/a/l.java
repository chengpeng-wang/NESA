package com.baidu.android.pushservice.a;

import android.app.PendingIntent;
import android.content.Intent;
import com.baidu.android.pushservice.PushConstants;

public class l {
    public String a = "";
    public String b = "";
    public String c = "";
    public String d = "";
    public String e = "";
    public String f = "";
    public String g = "";
    public String h = "";
    public String i = "";

    public l(Intent intent) {
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra(PushConstants.EXTRA_APP);
        if (pendingIntent != null) {
            this.e = pendingIntent.getTargetPackage();
        }
        this.d = intent.getStringExtra(PushConstants.EXTRA_ACCESS_TOKEN);
        this.i = intent.getStringExtra(PushConstants.EXTRA_API_KEY);
        this.a = intent.getStringExtra("method");
        this.b = intent.getStringExtra("method_type");
        this.c = intent.getStringExtra("method_version");
        this.h = intent.getStringExtra("bduss");
        this.f = intent.getStringExtra("appid");
    }

    public String toString() {
        return "method=" + this.a + ", rsarsaAccessToken=" + this.d + ", packageName=" + this.e + ", appId=" + this.f + ", userId=" + this.g + ", rsaBduss=" + this.h;
    }
}
