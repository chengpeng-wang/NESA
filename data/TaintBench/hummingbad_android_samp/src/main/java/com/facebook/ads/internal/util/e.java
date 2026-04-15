package com.facebook.ads.internal.util;

import java.util.Locale;

public enum e {
    NONE,
    INSTALLED,
    NOT_INSTALLED;

    public static e a(String str) {
        if (r.a(str)) {
            return NONE;
        }
        try {
            return valueOf(str.toUpperCase(Locale.US));
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}
