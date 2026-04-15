package com.facebook.ads.internal.adapters;

import java.util.Locale;

public enum f {
    UNKNOWN,
    AN;

    public static f a(String str) {
        if (str == null) {
            return UNKNOWN;
        }
        try {
            return (f) Enum.valueOf(f.class, str.toUpperCase(Locale.getDefault()));
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
