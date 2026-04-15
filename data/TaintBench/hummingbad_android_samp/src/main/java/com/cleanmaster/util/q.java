package com.cleanmaster.util;

import com.cleanmaster.common.a;
import com.picksinit.c;

/* compiled from: ResourceUtil */
public class q {
    public static int a(String str) {
        return c.getInstance().getContext().getResources().getIdentifier(str, "string", a.c());
    }
}
