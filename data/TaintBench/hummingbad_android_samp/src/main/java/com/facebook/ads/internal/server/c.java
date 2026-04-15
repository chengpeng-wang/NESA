package com.facebook.ads.internal.server;

public class c {
    private com.facebook.ads.internal.dto.c a;
    private a b;

    public enum a {
        UNKNOWN,
        ERROR,
        ADS
    }

    public c(a aVar, com.facebook.ads.internal.dto.c cVar) {
        this.b = aVar;
        this.a = cVar;
    }

    public a a() {
        return this.b;
    }

    public com.facebook.ads.internal.dto.c b() {
        return this.a;
    }
}
