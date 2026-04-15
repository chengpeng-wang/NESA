package com.facebook.ads.internal.server;

import com.facebook.ads.internal.dto.c;
import com.facebook.ads.internal.server.c.a;

public class e extends c {
    private final String a;
    private final int b;

    public e(String str, int i, c cVar) {
        super(a.ERROR, cVar);
        this.b = i;
        this.a = str;
    }

    public String c() {
        return this.a;
    }

    public int d() {
        return this.b;
    }
}
