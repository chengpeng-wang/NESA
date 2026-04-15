package client.core.model;

import java.util.ArrayList;

/* compiled from: TimeStamp */
public final class a {
    ArrayList a;
    boolean b;
    private long c;
    private long d;
    private long e;
    private String f;

    public final a a(String str) {
        this.a.add(new d(this, str));
        return this;
    }

    public a() {
        this.c = 0;
        this.d = 0;
        this.e = 0;
        this.f = "TRACER";
        this.a = new ArrayList();
        this.b = true;
        this.c = System.currentTimeMillis();
        a();
        a(".");
    }

    public final a a() {
        this.a = new ArrayList();
        switch (c.a[b.a - 1]) {
            case 1:
                this.d = System.currentTimeMillis();
                break;
            case 2:
                this.e = System.currentTimeMillis();
                break;
        }
        return this;
    }
}
