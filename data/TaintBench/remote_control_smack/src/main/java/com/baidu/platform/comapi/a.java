package com.baidu.platform.comapi;

import android.content.Context;
import com.baidu.platform.comapi.d.c;
import com.baidu.platform.comjni.engine.AppEngine;
import com.baidu.vi.VMsg;

public class a {
    public static boolean a = false;

    public boolean a() {
        if (!a) {
            a = true;
        }
        return true;
    }

    public boolean a(Context context) {
        a = false;
        c.c(context);
        c.d(context);
        com.baidu.vi.c.a(context);
        VMsg.init();
        boolean InitEngine = AppEngine.InitEngine(c.c());
        c.e();
        if (!InitEngine) {
            return false;
        }
        if (AppEngine.StartSocketProc()) {
            return true;
        }
        AppEngine.UnInitEngine();
        return false;
    }

    public boolean b() {
        a = false;
        return true;
    }

    public void c() {
        if (a) {
            b();
        }
        a = false;
        VMsg.destroy();
        com.baidu.platform.comjni.engine.a.a();
        c.b();
        AppEngine.UnInitEngine();
    }
}
