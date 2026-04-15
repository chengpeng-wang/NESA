package com.nicon.tool;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtls {
    private static final String HAS_REPORT_UM_FB = "has_report_um_fb";
    private static final String IS_FIRST_OPEN = "is_first_open";
    private static final String NIcon = "nicon";
    private static final String POSTSTAUS = "PostStaus";
    private static SharedPreferences mPreferences;

    private static SharedPreferences getNIconSharedPreferences(Context context) {
        if (mPreferences == null && context != null) {
            mPreferences = context.getSharedPreferences(NIcon, 0);
        }
        return mPreferences;
    }

    public static void setPostStaus(Context context, boolean b) {
        getNIconSharedPreferences(context).edit().putBoolean(POSTSTAUS, b).commit();
    }

    public static boolean getPostStaus(Context context) {
        mPreferences = getNIconSharedPreferences(context);
        return mPreferences.getBoolean(POSTSTAUS, true);
    }

    public static void setFbUmReported(Context context, boolean b) {
        getNIconSharedPreferences(context).edit().putBoolean(HAS_REPORT_UM_FB, b).commit();
    }

    public static boolean getFbUmReported(Context context) {
        mPreferences = getNIconSharedPreferences(context);
        return mPreferences.getBoolean(HAS_REPORT_UM_FB, false);
    }

    public static boolean isFirstOpen(Context context) {
        mPreferences = getNIconSharedPreferences(context);
        return mPreferences.getBoolean(IS_FIRST_OPEN, true);
    }

    public static void markOpened(Context context) {
        mPreferences = getNIconSharedPreferences(context);
        mPreferences.edit().putBoolean(IS_FIRST_OPEN, false).commit();
    }
}
