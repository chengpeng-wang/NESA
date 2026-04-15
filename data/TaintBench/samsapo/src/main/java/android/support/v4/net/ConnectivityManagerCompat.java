package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;

public class ConnectivityManagerCompat {
    private static final ConnectivityManagerCompatImpl IMPL;

    static class BaseConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl {
        BaseConnectivityManagerCompatImpl() {
        }

        public boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return true;
            }
            switch (activeNetworkInfo.getType()) {
                case 0:
                    return true;
                case 1:
                    return false;
                default:
                    return true;
            }
        }
    }

    interface ConnectivityManagerCompatImpl {
        boolean isActiveNetworkMetered(ConnectivityManager connectivityManager);
    }

    static class GingerbreadConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl {
        GingerbreadConnectivityManagerCompatImpl() {
        }

        public boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatGingerbread.isActiveNetworkMetered(connectivityManager);
        }
    }

    static class HoneycombMR2ConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl {
        HoneycombMR2ConnectivityManagerCompatImpl() {
        }

        public boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(connectivityManager);
        }
    }

    static class JellyBeanConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl {
        JellyBeanConnectivityManagerCompatImpl() {
        }

        public boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(connectivityManager);
        }
    }

    public ConnectivityManagerCompat() {
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            JellyBeanConnectivityManagerCompatImpl jellyBeanConnectivityManagerCompatImpl = r2;
            JellyBeanConnectivityManagerCompatImpl jellyBeanConnectivityManagerCompatImpl2 = new JellyBeanConnectivityManagerCompatImpl();
            IMPL = jellyBeanConnectivityManagerCompatImpl;
        } else if (VERSION.SDK_INT >= 13) {
            HoneycombMR2ConnectivityManagerCompatImpl honeycombMR2ConnectivityManagerCompatImpl = r2;
            HoneycombMR2ConnectivityManagerCompatImpl honeycombMR2ConnectivityManagerCompatImpl2 = new HoneycombMR2ConnectivityManagerCompatImpl();
            IMPL = honeycombMR2ConnectivityManagerCompatImpl;
        } else if (VERSION.SDK_INT >= 8) {
            GingerbreadConnectivityManagerCompatImpl gingerbreadConnectivityManagerCompatImpl = r2;
            GingerbreadConnectivityManagerCompatImpl gingerbreadConnectivityManagerCompatImpl2 = new GingerbreadConnectivityManagerCompatImpl();
            IMPL = gingerbreadConnectivityManagerCompatImpl;
        } else {
            BaseConnectivityManagerCompatImpl baseConnectivityManagerCompatImpl = r2;
            BaseConnectivityManagerCompatImpl baseConnectivityManagerCompatImpl2 = new BaseConnectivityManagerCompatImpl();
            IMPL = baseConnectivityManagerCompatImpl;
        }
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
        return IMPL.isActiveNetworkMetered(connectivityManager);
    }

    public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager connectivityManager, Intent intent) {
        return connectivityManager.getNetworkInfo(((NetworkInfo) intent.getParcelableExtra("networkInfo")).getType());
    }
}
