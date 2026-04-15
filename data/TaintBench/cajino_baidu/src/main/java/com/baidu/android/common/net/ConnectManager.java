package com.baidu.android.common.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;

public class ConnectManager {
    private static final boolean DEBUG = false;
    private static final String TAG = ConnectManager.class.getSimpleName();
    private String mApn;
    private String mNetType;
    private String mPort;
    private String mProxy;
    private boolean mUseWap;

    public ConnectManager(Context context) {
        checkNetworkType(context);
    }

    private void checkApn(Context context, NetworkInfo networkInfo) {
        String toLowerCase;
        if (networkInfo.getExtraInfo() != null) {
            toLowerCase = networkInfo.getExtraInfo().toLowerCase();
            if (toLowerCase != null) {
                if (toLowerCase.startsWith("cmwap") || toLowerCase.startsWith("uniwap") || toLowerCase.startsWith("3gwap")) {
                    this.mUseWap = true;
                    this.mApn = toLowerCase;
                    this.mProxy = "10.0.0.172";
                    this.mPort = "80";
                    return;
                } else if (toLowerCase.startsWith("ctwap")) {
                    this.mUseWap = true;
                    this.mApn = toLowerCase;
                    this.mProxy = "10.0.0.200";
                    this.mPort = "80";
                    return;
                } else if (toLowerCase.startsWith("cmnet") || toLowerCase.startsWith("uninet") || toLowerCase.startsWith("ctnet") || toLowerCase.startsWith("3gnet")) {
                    this.mUseWap = false;
                    this.mApn = toLowerCase;
                    return;
                }
            }
        }
        toLowerCase = Proxy.getDefaultHost();
        int defaultPort = Proxy.getDefaultPort();
        if (toLowerCase == null || toLowerCase.length() <= 0) {
            this.mUseWap = false;
            return;
        }
        this.mProxy = toLowerCase;
        if ("10.0.0.172".equals(this.mProxy.trim())) {
            this.mUseWap = true;
            this.mPort = "80";
        } else if ("10.0.0.200".equals(this.mProxy.trim())) {
            this.mUseWap = true;
            this.mPort = "80";
        } else {
            this.mUseWap = false;
            this.mPort = Integer.toString(defaultPort);
        }
    }

    private void checkNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return;
        }
        if ("wifi".equals(activeNetworkInfo.getTypeName().toLowerCase())) {
            this.mNetType = "wifi";
            this.mUseWap = false;
            return;
        }
        checkApn(context, activeNetworkInfo);
        this.mNetType = this.mApn;
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null ? activeNetworkInfo.isConnectedOrConnecting() : false;
    }

    public String getApn() {
        return this.mApn;
    }

    public String getNetType() {
        return this.mNetType;
    }

    public String getProxy() {
        return this.mProxy;
    }

    public String getProxyPort() {
        return this.mPort;
    }

    public boolean isWapNetwork() {
        return this.mUseWap;
    }
}
