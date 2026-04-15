package com.facebook.ads.internal.thirdparty.http;

public enum j {
    GET(true, false),
    POST(true, true);
    
    private boolean c;
    private boolean d;

    private j(boolean z, boolean z2) {
        this.c = z;
        this.d = z2;
    }

    public final boolean a() {
        return this.c;
    }

    public final boolean b() {
        return this.d;
    }

    public final String c() {
        return toString();
    }
}
