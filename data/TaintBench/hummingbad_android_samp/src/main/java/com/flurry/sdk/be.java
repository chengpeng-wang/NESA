package com.flurry.sdk;

import android.text.TextUtils;
import java.util.List;

public final class be {
    public static boolean a(List list, String str) {
        if (list == null) {
            return false;
        }
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String equals : list) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }
}
