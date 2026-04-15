package com.mobclick.android;

import android.content.Context;
import android.util.Log;

final class j extends Thread {
    private static final Object a = new Object();
    private Context b;
    private int c;
    private String d;
    private String e;
    private String f;
    private String g;
    private int h;

    j(Context context, int i) {
        this.b = context;
        this.c = i;
    }

    j(Context context, String str, int i) {
        this.b = context;
        this.c = i;
        this.d = str;
    }

    j(Context context, String str, String str2, int i) {
        this.b = context;
        this.c = i;
        this.d = str;
        this.e = str2;
    }

    j(Context context, String str, String str2, String str3, int i, int i2) {
        this.b = context;
        this.d = str;
        this.f = str2;
        this.g = str3;
        this.h = i;
        this.c = i2;
    }

    public void run() {
        try {
            synchronized (a) {
                if (this.c == 0) {
                    try {
                        if (this.b == null) {
                            Log.e(UmengConstants.LOG_TAG, "unexpected null context");
                            return;
                        }
                        MobclickAgent.a.b(this.b);
                    } catch (Exception e) {
                        Log.e(UmengConstants.LOG_TAG, "Exception occurred in Mobclick.onRause(). ");
                        e.printStackTrace();
                    }
                } else if (this.c == 1) {
                    MobclickAgent.a.a(this.b, this.d, this.e);
                } else if (this.c == 2) {
                    MobclickAgent.a.b(this.b, this.d);
                } else if (this.c == 3) {
                    MobclickAgent.a.a(this.b, this.d, this.f, this.g, this.h);
                }
            }
        } catch (Exception e2) {
            Log.e(UmengConstants.LOG_TAG, "Exception occurred when recording usage.");
            e2.printStackTrace();
        }
    }
}
