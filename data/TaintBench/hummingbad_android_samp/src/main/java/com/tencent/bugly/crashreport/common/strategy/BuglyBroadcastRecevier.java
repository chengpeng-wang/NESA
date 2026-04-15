package com.tencent.bugly.crashreport.common.strategy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.info.b;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.Date;
import java.util.List;

/* compiled from: BUGLY */
public class BuglyBroadcastRecevier extends BroadcastReceiver {
    public static String ACTION_PROCESS_CRASHED = "com.tencent.feedback.A02";
    public static String ACTION_PROCESS_LAUNCHED = "com.tencent.feedback.A01";
    public static final long UPLOADLIMITED = 60000;
    private static BuglyBroadcastRecevier d = null;
    private IntentFilter a = new IntentFilter();
    private Context b;
    private String c;

    public static synchronized BuglyBroadcastRecevier getInstance() {
        BuglyBroadcastRecevier buglyBroadcastRecevier;
        synchronized (BuglyBroadcastRecevier.class) {
            if (d == null) {
                d = new BuglyBroadcastRecevier();
            }
            buglyBroadcastRecevier = d;
        }
        return buglyBroadcastRecevier;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        if (this.b != null) {
            this.b.unregisterReceiver(this);
        }
    }

    public synchronized void addFilter(String action) {
        if (!this.a.hasAction(action)) {
            this.a.addAction(action);
        }
        z.c("add action %s", action);
    }

    public synchronized void regist(Context context) {
        try {
            z.a("regis BC", new Object[0]);
            context.registerReceiver(this, this.a);
            this.b = context;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return;
    }

    public synchronized void unregist(Context context) {
        try {
            z.a("unregis BC", new Object[0]);
            context.unregisterReceiver(this);
            this.b = context;
        } catch (Throwable th) {
        }
    }

    public final void onReceive(Context context, Intent intent) {
        a(context, intent);
    }

    /* access modifiers changed from: protected|final|declared_synchronized */
    public final synchronized boolean a(Context context, Intent intent) {
        boolean z = true;
        synchronized (this) {
            if (!(context == null || intent == null)) {
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    String e = b.e(this.b);
                    z.c("is Connect BC " + e, new Object[0]);
                    z.a("network changed %s %s", "" + this.c, "" + e);
                    if (e == null) {
                        this.c = null;
                    } else {
                        String str = this.c;
                        this.c = e;
                        long time = new Date().getTime();
                        final c a = c.a();
                        final w a2 = w.a();
                        a a3 = a.a(context);
                        if (a == null || a2 == null || a3 == null) {
                            z.d("not inited BC not work", new Object[0]);
                        } else if (a.b()) {
                            if (!e.equals(str) && time - a2.a(0) > UPLOADLIMITED) {
                                y.a().b(new Runnable() {
                                    public void run() {
                                        List c = a.c(a.d());
                                        if (c != null && c.size() > 0) {
                                            z.a("upload crash on network changed ", new Object[0]);
                                            a2.a(c, a);
                                        }
                                    }
                                });
                            }
                        } else if (a2.a(1) < 0) {
                            z.c("not to req until default strategy reqed ", new Object[0]);
                        } else if (time - a2.a(1) < UPLOADLIMITED) {
                            z.c("too freq not to try strategy by BC", new Object[0]);
                        } else {
                            z.a("try remote strategy on BC", new Object[0]);
                            a2.a(a);
                        }
                    }
                }
            }
            z = false;
        }
        return z;
    }
}
