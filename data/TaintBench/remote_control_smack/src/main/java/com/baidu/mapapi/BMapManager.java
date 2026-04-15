package com.baidu.mapapi;

import android.content.Context;
import android.os.Handler;
import com.baidu.mapapi.utils.PermissionCheck;
import com.baidu.mapapi.utils.b;
import com.baidu.platform.comapi.a;
import com.baidu.platform.comapi.b.e;

public class BMapManager {
    MKGeneralListener a = null;
    private a b = null;
    private Context c;
    private Handler d = null;
    private String e = null;
    private String f = null;
    private PermissionCheck g = null;
    /* access modifiers changed from: private */
    public boolean h = false;

    static {
        System.loadLibrary("BaiduMapVOS_v2_1_3");
        System.loadLibrary("BaiduMapSDK_v2_1_3");
    }

    public BMapManager(Context context) {
        this.c = context;
    }

    public void destroy() {
        if (this.d != null) {
            com.baidu.platform.comjni.engine.a.b(2000, this.d);
            com.baidu.platform.comjni.engine.a.b(2010, this.d);
            this.d = null;
        }
        if (this.a != null) {
            this.a = null;
        }
        e.b();
        this.b.c();
        com.baidu.mapapi.search.a.a();
    }

    public Context getContext() {
        return this.c;
    }

    public boolean init(String str, MKGeneralListener mKGeneralListener) {
        this.e = str;
        try {
            this.f = this.c.getPackageManager().getPackageInfo(this.c.getPackageName(), 0).applicationInfo.loadLabel(this.c.getPackageManager()).toString();
        } catch (Exception e) {
            this.f = null;
        }
        if (this.b == null) {
            this.b = new a();
        }
        b.a(this.c);
        com.baidu.mapapi.search.a.a(this.c);
        this.a = mKGeneralListener;
        this.d = new a(this);
        com.baidu.platform.comjni.engine.a.a(2000, this.d);
        com.baidu.platform.comjni.engine.a.a(2010, this.d);
        if (!this.b.a(this.c)) {
            return false;
        }
        start();
        PermissionCheck.InitParam(str, this.f, com.baidu.mapapi.utils.a.a(this.c));
        return PermissionCheck.check();
    }

    public boolean start() {
        return this.b.a();
    }

    public boolean stop() {
        return this.b.b();
    }
}
