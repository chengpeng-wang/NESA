package com.cmcm.adsdk.requestconfig;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.cmcm.adsdk.requestconfig.request.RequestAction;

/* compiled from: ConfigChangeMonitor */
public final class a {
    private static a a = null;
    private RequestAction b;
    private Context c;
    private String d;
    private boolean e = false;
    private PendingIntent f;
    private a g;

    /* compiled from: ConfigChangeMonitor */
    private class a extends BroadcastReceiver {
        private a() {
        }

        /* synthetic */ a(a aVar, byte b) {
            this();
        }

        public final void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("com.cmcm.adsdk.ConfigMonitor_Action")) {
                com.cmcm.adsdk.requestconfig.log.a.a("ConfigChangeMonitor", "monitor requestConfig...");
                RequestConfig.a().a(true);
            }
        }
    }

    private a(Context context) {
        this.c = context;
        this.b = RequestAction.a();
    }

    public static synchronized a a(Context context) {
        a aVar;
        synchronized (a.class) {
            if (a == null) {
                a = new a(context);
            }
            aVar = a;
        }
        return aVar;
    }

    public final synchronized void a(String str) {
        if (this.e) {
            com.cmcm.adsdk.requestconfig.log.a.a("ConfigChangeMonitor", "has start monitor, avoid repeat monitor ...");
        } else {
            com.cmcm.adsdk.requestconfig.log.a.a("ConfigChangeMonitor", "start monitor...");
            this.d = str;
            this.e = true;
            try {
                if (this.g == null) {
                    this.g = new a(this, (byte) 0);
                }
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.cmcm.adsdk.ConfigMonitor_Action");
                this.c.getApplicationContext().registerReceiver(this.g, intentFilter);
                Intent intent = new Intent();
                intent.setAction("com.cmcm.adsdk.ConfigMonitor_Action");
                if (this.f == null) {
                    this.f = PendingIntent.getBroadcast(this.c, 0, intent, 0);
                }
                ((AlarmManager) this.c.getSystemService("alarm")).setRepeating(1, System.currentTimeMillis() + 7200000, 7200000, this.f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public final synchronized void a() {
        com.cmcm.adsdk.requestconfig.log.a.a("ConfigChangeMonitor", "stop monitor...");
        if (this.c != null) {
            try {
                AlarmManager alarmManager = (AlarmManager) this.c.getSystemService("alarm");
                if (this.f != null) {
                    alarmManager.cancel(this.f);
                    this.f = null;
                }
                if (this.g != null) {
                    this.c.getApplicationContext().unregisterReceiver(this.g);
                    this.g = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.e = false;
        return;
    }
}
