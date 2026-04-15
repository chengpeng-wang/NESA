package com.cmcm.adsdk.nativead;

/* compiled from: ReportHelper */
public final class h extends i {
    private static h a;

    private h() {
    }

    public static synchronized h a() {
        h hVar;
        synchronized (h.class) {
            if (a == null) {
                a = new h();
            }
            hVar = a;
        }
        return hVar;
    }
}
