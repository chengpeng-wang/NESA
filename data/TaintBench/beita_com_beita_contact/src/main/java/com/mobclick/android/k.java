package com.mobclick.android;

import android.content.Context;
import org.json.JSONObject;

final class k implements Runnable {
    private static final Object a = new Object();
    private MobclickAgent b = MobclickAgent.a;
    private Context c;
    private JSONObject d;

    k(MobclickAgent mobclickAgent, Context context, JSONObject jSONObject) {
        this.c = context;
        this.d = jSONObject;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public void run() {
        /*
        r4 = this;
        r0 = r4.d;	 Catch:{ Exception -> 0x0034 }
        r1 = "type";
        r0 = r0.getString(r1);	 Catch:{ Exception -> 0x0034 }
        r1 = "update";
        r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0034 }
        if (r0 == 0) goto L_0x001a;
    L_0x0010:
        r0 = r4.b;	 Catch:{ Exception -> 0x0034 }
        r1 = r4.c;	 Catch:{ Exception -> 0x0034 }
        r2 = r4.d;	 Catch:{ Exception -> 0x0034 }
        r0.a(r1, r2);	 Catch:{ Exception -> 0x0034 }
    L_0x0019:
        return;
    L_0x001a:
        r0 = r4.d;	 Catch:{ Exception -> 0x0034 }
        r1 = "type";
        r0 = r0.getString(r1);	 Catch:{ Exception -> 0x0034 }
        r1 = "online_config";
        r0 = r0.equals(r1);	 Catch:{ Exception -> 0x0034 }
        if (r0 == 0) goto L_0x0040;
    L_0x002a:
        r0 = r4.b;	 Catch:{ Exception -> 0x0034 }
        r1 = r4.c;	 Catch:{ Exception -> 0x0034 }
        r2 = r4.d;	 Catch:{ Exception -> 0x0034 }
        r0.f(r1, r2);	 Catch:{ Exception -> 0x0034 }
        goto L_0x0019;
    L_0x0034:
        r0 = move-exception;
        r1 = "MobclickAgent";
        r2 = "Exception occurred when sending message.";
        android.util.Log.e(r1, r2);
        r0.printStackTrace();
        goto L_0x0019;
    L_0x0040:
        r0 = a;	 Catch:{ Exception -> 0x0034 }
        monitor-enter(r0);	 Catch:{ Exception -> 0x0034 }
        r1 = r4.b;	 Catch:{ all -> 0x004e }
        r2 = r4.c;	 Catch:{ all -> 0x004e }
        r3 = r4.d;	 Catch:{ all -> 0x004e }
        r1.c(r2, r3);	 Catch:{ all -> 0x004e }
        monitor-exit(r0);	 Catch:{ all -> 0x004e }
        goto L_0x0019;
    L_0x004e:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x004e }
        throw r1;	 Catch:{ Exception -> 0x0034 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mobclick.android.k.run():void");
    }
}
