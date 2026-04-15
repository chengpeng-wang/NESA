package com.baidu.android.pushservice;

import android.content.Context;
import android.text.TextUtils;

public final class y {
    private static y a;
    private String b;
    private String c;
    private Thread d;
    private boolean e;

    private y() {
        this.b = null;
        this.c = null;
        this.d = null;
        this.c = PushSettings.b();
        this.b = PushSettings.a();
        this.e = false;
    }

    public static synchronized y a() {
        y yVar;
        synchronized (y.class) {
            if (a == null) {
                a = new y();
            }
            yVar = a;
        }
        return yVar;
    }

    public void a(Context context, boolean z) {
        if (this.d == null || !this.d.isAlive()) {
            com.baidu.android.pushservice.a.y yVar = new com.baidu.android.pushservice.a.y(context);
            if (!z) {
                yVar.a(0);
            }
            this.d = new Thread(yVar);
            this.d.start();
        }
    }

    public synchronized void a(String str, String str2) {
        this.b = str;
        this.c = str2;
        PushSettings.a(str);
        PushSettings.c(str2);
    }

    public void a(boolean z) {
        this.e = z;
    }

    public boolean b() {
        return this.e;
    }

    public String c() {
        return this.b;
    }

    public String d() {
        return this.c;
    }

    public boolean e() {
        return (TextUtils.isEmpty(this.b) || TextUtils.isEmpty(this.c)) ? false : true;
    }
}
