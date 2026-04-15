package com.tencent.bugly.crashreport.crash.anr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.z;

/* compiled from: BUGLY */
public class BuglyTestANR_Reciver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        try {
            context.unregisterReceiver(this);
        } catch (Throwable th) {
            if (!z.a(th)) {
                th.printStackTrace();
            }
        }
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i < 30) {
                z.a("try main sleep for make a test anr! try:%d/30 , kill it if you don't want to wait!", Integer.valueOf(i2));
                ag.a(5000);
                i = i2;
            } else {
                z.a("wait up!! , ANR did not occur?!!", new Object[0]);
                return;
            }
        }
    }
}
