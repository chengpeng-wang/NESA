package com.flurry.sdk;

import java.util.Timer;

class jf {
    private static final String a = jf.class.getSimpleName();
    private Timer b;

    public final synchronized void a() {
        if (this.b != null) {
            this.b.cancel();
            this.b = null;
            iw.a(3, a, "HttpRequestTimeoutTimer stopped.");
        }
    }
}
