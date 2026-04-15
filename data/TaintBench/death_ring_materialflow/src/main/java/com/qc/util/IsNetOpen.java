package com.qc.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.provider.Settings.System;
import java.util.List;

public class IsNetOpen {
    private final ConnectivityManager conManager;
    private Context context;

    public IsNetOpen(Context context) {
        this.context = context;
        this.conManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public boolean checkWifi() {
        return this.conManager.getNetworkInfo(1).isAvailable();
    }

    public boolean chckMobile() {
        return this.conManager.getNetworkInfo(0).isAvailable();
    }

    public boolean checkNet() {
        NetworkInfo info = this.conManager.getActiveNetworkInfo();
        if (info != null && info.isConnected() && info.getState() == State.CONNECTED) {
            return true;
        }
        return false;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) this.context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info == null) {
            return false;
        }
        for (NetworkInfo state : info) {
            if (state.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnLine() {
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] info = ((ConnectivityManager) context.getSystemService("connectivity")).getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo state : info) {
                if (state.getState() == State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFly() {
        if (System.getInt(this.context.getContentResolver(), "airplane_mode_on", 0) != 0) {
            return true;
        }
        return false;
    }

    public String getMac() {
        return ((WifiManager) this.context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    public boolean isGpsEnabled() {
        List<String> accessibleProviders = ((LocationManager) this.context.getSystemService("location")).getProviders(true);
        if (accessibleProviders == null || accessibleProviders.size() <= 0) {
            return false;
        }
        return true;
    }

    public String getNetworkType() {
        ConnectivityManager connManager = (ConnectivityManager) this.context.getSystemService("connectivity");
        State state = connManager.getNetworkInfo(1).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return "wifi";
        }
        state = connManager.getNetworkInfo(0).getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return "mobile";
        }
        return "none";
    }
}
