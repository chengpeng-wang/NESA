package com.flurry.sdk;

import android.text.TextUtils;

public abstract class ij {
    String d = "com.flurry.android.sdk.ReplaceMeWithAProperEventName";

    public ij(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Event must have a name!");
        }
        this.d = str;
    }

    public final void a() {
        il.a().a(this);
    }
}
