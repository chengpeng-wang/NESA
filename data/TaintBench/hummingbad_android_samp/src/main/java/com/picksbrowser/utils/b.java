package com.picksbrowser.utils;

import android.os.Build.VERSION;

/* compiled from: VersionCode */
public enum b {
    BASE(1),
    BASE_1_1(2),
    CUPCAKE(3),
    DONUT(4),
    ECLAIR(5),
    ECLAIR_0_1(6),
    ECLAIR_MR1(7),
    FROYO(8),
    GINGERBREAD(9),
    GINGERBREAD_MR1(10),
    HONEYCOMB(11),
    HONEYCOMB_MR1(12),
    HONEYCOMB_MR2(13),
    ICE_CREAM_SANDWICH(14),
    ICE_CREAM_SANDWICH_MR1(15),
    JELLY_BEAN(16),
    JELLY_BEAN_MR1(17),
    JELLY_BEAN_MR2(18),
    KITKAT(19),
    CUR_DEVELOPMENT(10000);
    
    private int u;

    public static b a() {
        int i = VERSION.SDK_INT;
        for (b bVar : values()) {
            if (bVar.u == i) {
                return bVar;
            }
        }
        return CUR_DEVELOPMENT;
    }

    private b(int i) {
        this.u = i;
    }

    public final int b() {
        return this.u;
    }
}
