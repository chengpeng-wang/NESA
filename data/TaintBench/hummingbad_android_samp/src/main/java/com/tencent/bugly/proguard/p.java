package com.tencent.bugly.proguard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.os.EnvironmentCompat;
import com.tencent.bugly.crashreport.common.strategy.c;

/* compiled from: BUGLY */
public class p {
    private static p c = new p();
    public int a;
    public String b = EnvironmentCompat.MEDIA_UNKNOWN;
    private boolean d;
    private boolean e;
    /* access modifiers changed from: private */
    public long f;

    @TargetApi(14)
    /* compiled from: BUGLY */
    class a implements ActivityLifecycleCallbacks {
        a() {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityResumed(Activity activity) {
            if (activity != null) {
                p.this.b = activity.getClass().getName();
            } else {
                p.this.b = EnvironmentCompat.MEDIA_UNKNOWN;
            }
            if (SystemClock.elapsedRealtime() - p.this.f > 30000) {
                c a = c.a();
                if (a != null) {
                    p pVar = p.this;
                    pVar.a++;
                    z.a("[session] launch app 1 times (app in background over 30 seconds)", new Object[0]);
                    if (p.this.a % 10 == 0) {
                        a.a(true);
                    } else {
                        a.a(false);
                    }
                }
            }
        }

        public void onActivityPaused(Activity activity) {
            p.this.f = SystemClock.elapsedRealtime();
            if (activity != null) {
                p.this.b = "background";
                return;
            }
            p.this.b = EnvironmentCompat.MEDIA_UNKNOWN;
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }
    }

    public static p a() {
        return c;
    }

    public void a(Context context) {
        if (!this.d) {
            this.d = true;
            b(context);
        }
    }

    public void a(long j) {
        c.a().a(j);
        z.a("[session] launch app 1 times (app new start)", new Object[0]);
        this.f = SystemClock.elapsedRealtime();
    }

    @TargetApi(14)
    private void b(Context context) {
        Application application = null;
        if (VERSION.SDK_INT < 14) {
            this.e = false;
            return;
        }
        if (context.getApplicationContext() instanceof Application) {
            application = (Application) context.getApplicationContext();
            this.e = true;
        }
        if (application != null) {
            try {
                application.registerActivityLifecycleCallbacks(new a());
            } catch (Exception e) {
                this.e = false;
            }
            if (this.e) {
                z.c("[session] registed by api", new Object[0]);
            }
        }
    }
}
