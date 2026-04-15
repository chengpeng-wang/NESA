package com.mobclick.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class m {
    public static String a(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return null;
        }
        if (activeNetworkInfo.getType() == 1) {
            return null;
        }
        String extraInfo = activeNetworkInfo.getExtraInfo();
        Log.i("TAG", "net type:" + extraInfo);
        return extraInfo == null ? null : (extraInfo.equals("cmwap") || extraInfo.equals("3gwap") || extraInfo.equals("uniwap")) ? "10.0.0.172" : null;
    }
}
