package com.cleanmaster.ui.app.market.data.filter;

import com.cleanmaster.base.util.io.b;
import com.cleanmaster.base.util.io.c;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.List;
import java.util.Set;

/* compiled from: AdEnv */
public class a {
    private static a f = new a();
    int a = 0;
    int b = 0;
    int c = 0;
    Set d = new com.cleanmaster.base.util.misc.a();
    long e = 0;

    public static a a() {
        return f;
    }

    public a b() {
        List c = com.cleanmaster.func.cache.a.a().c();
        if (!(c == null || c.isEmpty())) {
            this.a = c.size();
            this.d.addAll(c);
            b a = c.a();
            this.b = com.cleanmaster.functionevent.report.a.a(a.a);
            this.c = com.cleanmaster.functionevent.report.a.a(a.b);
        }
        this.e = System.currentTimeMillis();
        return this;
    }

    public int c() {
        return this.a;
    }

    public int d() {
        return this.b;
    }

    public Set e() {
        return this.d;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--------------------------------------[AdEnv]\n");
        stringBuilder.append(" *     app_installed_num = ").append(this.a).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * total_space_condition = ").append(this.b + "M").append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * free_space_condition  = ").append(this.c + "M").append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(" * \t\t\t\t\t\t\t\t\tnow = ").append(g.a((long) this.a)).append(MASTNativeAdConstants.NEWLINE);
        stringBuilder.append(MASTNativeAdConstants.NEWLINE);
        return super.toString();
    }

    public int f() {
        return this.c;
    }

    public long g() {
        return this.e;
    }
}
