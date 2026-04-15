package cn.smsmanager.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

public class NetworkState {
    private static final String TAG = "NetworkState";
    private Context context;

    public NetworkState(Context context) {
        this.context = context; 
    }

    public static boolean isConnect(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected() && info.getState() == State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    public static String GetNetworkType(Context context) {
        String networkTypeNamString = "";
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivity == null) {
                return networkTypeNamString;
            }
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                return networkTypeNamString;
            }
            return info.getTypeName();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return networkTypeNamString;
        }
    }
}
