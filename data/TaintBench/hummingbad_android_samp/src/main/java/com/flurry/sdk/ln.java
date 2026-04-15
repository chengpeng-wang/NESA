package com.flurry.sdk;

public final class ln {
    private static ln a = null;
    private final lo b = new lo();

    public static synchronized ln a() {
        ln lnVar;
        synchronized (ln.class) {
            if (a == null) {
                a = new ln();
            }
            lnVar = a;
        }
        return lnVar;
    }

    public final synchronized void a(ik ikVar) {
        il.a().a("com.flurry.android.sdk.TickEvent", ikVar);
        if (il.a().b() > 0) {
            this.b.a();
        }
    }

    public final synchronized void b(ik ikVar) {
        il.a().b("com.flurry.android.sdk.TickEvent", ikVar);
        if (il.a().b() == 0) {
            this.b.b();
        }
    }
}
