package com.duapps.ad.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: BaseInfoHelper */
public final class a {
    private static final boolean a = f.a();

    public static String a(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    private static DisplayMetrics k(Context context) {
        try {
            Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            return displayMetrics;
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to getMetrics!", e);
            }
            return null;
        }
    }

    public static String b(Context context) {
        try {
            DisplayMetrics k = k(context);
            if (k != null) {
                return String.valueOf(k.heightPixels);
            }
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get height info!", e);
            }
        }
        return "";
    }

    public static String c(Context context) {
        try {
            DisplayMetrics k = k(context);
            if (k != null) {
                return String.valueOf(k.widthPixels);
            }
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get width info!", e);
            }
        }
        return "";
    }

    public static String d(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get PkgVersionName!", e);
            }
        }
        return "";
    }

    public static int e(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get PkgVersionCode!", e);
            }
        }
        return -1;
    }

    public static String a() {
        try {
            return Build.MANUFACTURER;
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get the hw info.", e);
            }
            return "";
        }
    }

    public static String b() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "failed to get the model info.", e);
            }
            return "";
        }
    }

    public static String f(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get the hw info.", e);
            }
            return "";
        }
    }

    public static String c() {
        try {
            return String.valueOf(VERSION.SDK_INT);
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get the androidVersion info.", e);
            }
            return "";
        }
    }

    public static String g(Context context) {
        try {
            DisplayMetrics k = k(context);
            if (k != null) {
                return Integer.toString(k.densityDpi);
            }
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get the dpi info.", e);
            }
        }
        return "";
    }

    public static String h(Context context) {
        try {
            return context.getResources().getConfiguration().locale.toString();
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "failed to getLocale Info!", e);
            }
            return "";
        }
    }

    public static String i(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                String typeName = activeNetworkInfo.getTypeName();
                if ("mobile".equals(typeName.toLowerCase())) {
                    return activeNetworkInfo.getSubtypeName();
                }
                return typeName;
            }
        } catch (Exception e) {
            if (a) {
                Log.e("BaseInfoHelper", "Failed to get the netWorkType info.", e);
            }
        }
        return "none";
    }

    public static int j(Context context) {
        if (context.getResources().getConfiguration().orientation == 1) {
            return 1;
        }
        return 3;
    }

    public static byte[] a(String str, String str2, byte[] bArr) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "AES");
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(2, secretKeySpec, new IvParameterSpec(str.getBytes()));
        return instance.doFinal(bArr);
    }
}
