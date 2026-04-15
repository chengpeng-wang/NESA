package com.cleanmaster.util;

import java.util.LinkedHashMap;
import java.util.Map;

/* compiled from: IniFileUtils */
public class l {
    final /* synthetic */ k a;
    /* access modifiers changed from: private */
    public String b;
    private Map c = new LinkedHashMap();

    public l(k kVar) {
        this.a = kVar;
    }

    public void a(String str, Object obj) {
        this.c.put(str, obj);
    }

    public Object a(String str) {
        return this.c.get(str);
    }
}
