package com.baidu.platform.comapi.d;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.baidu.mapapi.search.MKSearch;

public class a {
    public static int a = -1;
    public static String b = "";
    public static boolean c = false;
    public static String d = "";
    public static int e = 0;

    public static String a(Context context) {
        int i;
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() != 1) {
                switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
                    case 1:
                    case 2:
                        i = 6;
                        break;
                    case 3:
                    case 9:
                    case 10:
                    case 15:
                        i = 9;
                        break;
                    case 4:
                        i = 5;
                        break;
                    case 5:
                    case 6:
                    case 7:
                        i = 7;
                        break;
                    case 8:
                        i = 8;
                        break;
                    case MKSearch.TYPE_POI_LIST /*11*/:
                        i = 2;
                        break;
                }
            }
            i = 1;
            return Integer.toString(i);
        }
        i = 0;
        return Integer.toString(i);
    }
}
