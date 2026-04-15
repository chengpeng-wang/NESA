package com.flurry.sdk;

import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build.VERSION;

public class ie {
    private static ie b;
    /* access modifiers changed from: private|static|final */
    public static final String c = ie.class.getSimpleName();
    public Object a;

    private ie() {
        if (VERSION.SDK_INT >= 14 && this.a == null) {
            Context context = hz.a.b;
            if (context instanceof Application) {
                this.a = new if(this);
                ((Application) context).registerActivityLifecycleCallbacks((ActivityLifecycleCallbacks) this.a);
            }
        }
    }

    public static synchronized ie a() {
        ie ieVar;
        synchronized (ie.class) {
            if (b == null) {
                b = new ie();
            }
            ieVar = b;
        }
        return ieVar;
    }
}
