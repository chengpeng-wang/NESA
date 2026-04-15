package com.cmcm.adsdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.cmcm.adsdk.a;

/* compiled from: NetUtil */
public final class b {
    private static Uri a = Uri.parse("content://telephony/carriers/preferapn");

    private static int d(Context context) {
        int i = 4;
        int i2 = 1;
        if (context == null) {
            return 0;
        }
        int i3;
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() != 1) {
                    int subtype = activeNetworkInfo.getSubtype();
                    switch (subtype) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            i3 = 1;
                            break;
                        default:
                            i3 = 0;
                            break;
                    }
                    if (i3 != 0) {
                        i = 1;
                    } else {
                        switch (subtype) {
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 14:
                            case 15:
                                i3 = 1;
                                break;
                            default:
                                i3 = 0;
                                break;
                        }
                        if (i3 != 0) {
                            i = 2;
                        } else {
                            switch (subtype) {
                                case 13:
                                    break;
                                default:
                                    i2 = 0;
                                    break;
                            }
                            i3 = i2 != 0 ? 5 : 4;
                            return i3;
                        }
                    }
                } else if (a(context)) {
                    i = 3;
                }
            }
            i3 = i;
        } catch (Exception e) {
            i3 = 0;
        }
        return i3;
    }

    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        boolean z;
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 1) {
                z = true;
                return z;
            }
        } catch (Exception e) {
            a.a("NetUtil", e);
        }
        z = false;
        return z;
    }

    public static boolean b(Context context) {
        if (context != null) {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                if (connectivityManager != null) {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {
                        a.a("NetUtil", "isNetworkAvailable");
                        return true;
                    }
                    a.a("NetUtil", "isNetworkAvailable fail");
                    return false;
                }
            } catch (Exception e) {
                a.a("NetUtil", e);
            }
        }
        return false;
    }

    public static boolean c(Context context) {
        int d = d(context);
        if (d == 1 || d == 2 || d == 5) {
            return true;
        }
        return false;
    }
}
