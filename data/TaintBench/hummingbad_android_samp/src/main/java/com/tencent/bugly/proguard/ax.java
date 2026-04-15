package com.tencent.bugly.proguard;

import java.util.ArrayList;

/* compiled from: BUGLY */
public final class ax extends m implements Cloneable {
    static ArrayList<aw> b;
    public ArrayList<aw> a = null;

    public void a(l lVar) {
        lVar.a(this.a, 0);
    }

    public void a(k kVar) {
        if (b == null) {
            b = new ArrayList();
            b.add(new aw());
        }
        this.a = (ArrayList) kVar.a(b, 0, true);
    }

    public void a(StringBuilder stringBuilder, int i) {
    }
}
