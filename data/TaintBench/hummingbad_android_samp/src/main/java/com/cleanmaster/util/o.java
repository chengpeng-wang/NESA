package com.cleanmaster.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.cleanmaster.ui.app.market.b;

/* compiled from: NetworkUtil */
public class o {
    public static boolean a(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        try {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            if (networkInfo == null) {
                return false;
            }
            State state = networkInfo.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static int b(Context context) {
        if (context == null) {
            return 0;
        }
        int i;
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                i = 4;
                return i;
            }
            if (activeNetworkInfo.getType() != 1) {
                i = activeNetworkInfo.getSubtype();
                if (a(i)) {
                    i = 1;
                } else if (b(i)) {
                    i = 5;
                } else {
                    i = 2;
                }
            } else if (c(context)) {
                i = 3;
            } else {
                i = 4;
            }
            return i;
        } catch (Exception e) {
            i = 0;
        }
    }

    private static boolean b(int i) {
        if (i == 13) {
            return true;
        }
        return false;
    }

    public static boolean c(Context context) {
        if (context == null) {
            return false;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int ipAddress = connectionInfo == null ? 0 : connectionInfo.getIpAddress();
        if (!wifiManager.isWifiEnabled() || ipAddress == 0) {
            return false;
        }
        return true;
    }

    public static boolean a(int i) {
        if (i == 1 || i == 2 || i == 3 || i == 7) {
            return true;
        }
        return false;
    }

    public static String a() {
        return b.f();
    }
}
