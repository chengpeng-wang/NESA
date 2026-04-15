package com.flurry.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ht extends BroadcastReceiver {
    private static ht b;
    public boolean a;
    private boolean c;
    private boolean d = false;

    private ht() {
        boolean z = false;
        Context context = hz.a.b;
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
            z = true;
        }
        this.d = z;
        this.a = a(context);
        if (this.d) {
            c();
        }
    }

    public static synchronized ht a() {
        ht htVar;
        synchronized (ht.class) {
            if (b == null) {
                b = new ht();
            }
            htVar = b;
        }
        return htVar;
    }

    private boolean a(Context context) {
        if (!this.d || context == null) {
            return true;
        }
        NetworkInfo activeNetworkInfo = d().getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private synchronized void c() {
        if (!this.c) {
            Context context = hz.a.b;
            this.a = a(context);
            context.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.c = true;
        }
    }

    private static ConnectivityManager d() {
        return (ConnectivityManager) hz.a.b.getSystemService("connectivity");
    }

    public final hu b() {
        if (!this.d) {
            return hu.NONE_OR_UNKNOWN;
        }
        NetworkInfo activeNetworkInfo = d().getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return hu.NONE_OR_UNKNOWN;
        }
        switch (activeNetworkInfo.getType()) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
                return hu.CELL;
            case 1:
                return hu.WIFI;
            case 8:
                return hu.NONE_OR_UNKNOWN;
            default:
                return hu.NETWORK_AVAILABLE;
        }
    }

    public final void onReceive(Context context, Intent intent) {
        boolean a = a(context);
        if (this.a != a) {
            this.a = a;
            ij hsVar = new hs();
            hsVar.a = a;
            hsVar.b = b();
            il.a().a(hsVar);
        }
    }
}
