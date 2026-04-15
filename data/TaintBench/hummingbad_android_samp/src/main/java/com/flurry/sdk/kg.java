package com.flurry.sdk;

import java.util.UUID;

public final class kg {
    String a;
    byte[] b;

    private kg() {
        this.a = null;
        this.b = null;
    }

    /* synthetic */ kg(byte b) {
        this();
    }

    public kg(byte[] bArr) {
        this.a = null;
        this.b = null;
        this.a = UUID.randomUUID().toString();
        this.b = bArr;
    }

    public static String a(String str) {
        return ".yflurrydatasenderblock." + str;
    }
}
