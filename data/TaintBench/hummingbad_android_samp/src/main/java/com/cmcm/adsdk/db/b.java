package com.cmcm.adsdk.db;

import android.content.Context;

/* compiled from: DbManager */
public class b {
    private static volatile b a;
    private a b;

    private b(Context context) {
        this.b = new a(context);
    }

    public static b a(Context context) {
        if (a == null) {
            synchronized (b.class) {
                if (a == null) {
                    a = new b(context);
                }
            }
        }
        return a;
    }

    public final a a() {
        return this.b;
    }
}
