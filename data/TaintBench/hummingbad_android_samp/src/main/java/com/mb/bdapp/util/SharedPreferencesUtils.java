package com.mb.bdapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtils {
    private static final String AD_ACTION_ = "ad_action_";
    private static final String PRE_FILE_NAME = "duapp_shared.xml";
    private static final String SPF_AD_ACTION = "spf_ad_action";
    private static SharedPreferences mSDKPreferences;

    public static SharedPreferences getSDKSharedPreferences(Context context) {
        if (mSDKPreferences == null && context != null) {
            mSDKPreferences = context.getSharedPreferences(PRE_FILE_NAME, 0);
        }
        return mSDKPreferences;
    }

    public static void putString(Context context, String key, String value) {
        getSDKSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getSDKSharedPreferences(context).getString(key, null);
    }

    public static void putInt(Context context, String key, int value) {
        getSDKSharedPreferences(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int def) {
        return getSDKSharedPreferences(context).getInt(key, def);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSDKSharedPreferences(context).getBoolean(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getSDKSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        return getSDKSharedPreferences(context).getLong(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        getSDKSharedPreferences(context).edit().putLong(key, value).commit();
    }

    private static SharedPreferences getAdActionShared(Context context) {
        return context.getSharedPreferences(SPF_AD_ACTION, 0);
    }

    public static void addAdvAction(Context context, int action) {
        SharedPreferences spf = getAdActionShared(context);
        String key = new StringBuilder(AD_ACTION_).append(action).toString();
        spf.edit().putInt(key, spf.getInt(key, 0) + 1).commit();
    }

    public static String getAdvAction(Context context) {
        SharedPreferences spf = getAdActionShared(context);
        StringBuffer sb = new StringBuffer();
        int i = 1;
        while (i <= 13) {
            sb.append(new StringBuilder(String.valueOf(i)).append(":").append(spf.getInt(new StringBuilder(AD_ACTION_).append(i).toString(), 0)).toString());
            sb.append(i == 13 ? "" : ",");
            i++;
        }
        return sb.toString();
    }

    public static void cleanAdvAction(Context context) {
        Editor edit = getAdActionShared(context).edit();
        for (int i = 1; i <= 13; i++) {
            edit.putInt(new StringBuilder(AD_ACTION_).append(i).toString(), 0);
        }
        edit.commit();
    }
}
