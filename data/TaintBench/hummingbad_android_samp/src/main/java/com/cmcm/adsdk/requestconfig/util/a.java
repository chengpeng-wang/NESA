package com.cmcm.adsdk.requestconfig.util;

import java.util.List;
import java.util.Locale;

/* compiled from: CommonUtils */
public final class a {
    public static boolean a(List<?> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        return true;
    }

    public static String a() {
        try {
            String language = Locale.getDefault().getLanguage();
            return language + "_" + Locale.getDefault().getCountry();
        } catch (Exception e) {
            com.cmcm.adsdk.requestconfig.log.a.a("Get Language and Country is error..." + e.getMessage());
            return "";
        }
    }
}
