package com.flurry.sdk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class jo {
    private static final String b = jo.class.getSimpleName();
    public boolean a;
    private final ii c;
    /* access modifiers changed from: private|final */
    public final List d = new ArrayList();
    private int e;
    private long f;
    private final Runnable g = new jp(this);
    private final ik h = new jq(this);

    public jo() {
        il.a().a("com.flurry.android.sdk.NetworkStateEvent", this.h);
        this.c = a();
        this.f = 10000;
        this.e = -1;
        hz.a.b(new jr(this));
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void a(List list) {
        lt.b();
        List list2 = (List) this.c.a();
        if (list2 != null) {
            list.addAll(list2);
        }
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void b() {
        if (!this.a) {
            if (this.e >= 0) {
                iw.a(3, b, "Transmit is in progress");
            } else {
                e();
                if (this.d.isEmpty()) {
                    this.f = 10000;
                    this.e = -1;
                } else {
                    this.e = 0;
                    hz.a.b(new jw(this));
                }
            }
        }
    }

    private synchronized void b(List list) {
        lt.b();
        this.c.a(new ArrayList(list));
    }

    /* access modifiers changed from: private|declared_synchronized */
    public synchronized void c() {
        jn jnVar;
        lt.b();
        if (ht.a().a) {
            while (this.e < this.d.size()) {
                List list = this.d;
                int i = this.e;
                this.e = i + 1;
                jnVar = (jn) list.get(i);
                if (!jnVar.d) {
                    break;
                }
            }
            jnVar = null;
        } else {
            iw.a(3, b, "Network is not available, aborting transmission");
            jnVar = null;
        }
        if (jnVar == null) {
            d();
        } else {
            a(jnVar);
        }
    }

    private synchronized void d() {
        e();
        b(this.d);
        if (this.a) {
            iw.a(3, b, "Reporter paused");
            this.f = 10000;
        } else if (this.d.isEmpty()) {
            iw.a(3, b, "All reports sent successfully");
            this.f = 10000;
        } else {
            this.f <<= 1;
            iw.a(3, b, "One or more reports failed to send, backing off: " + this.f + "ms");
            hz.a.a(this.g, this.f);
        }
        this.e = -1;
    }

    private synchronized void e() {
        Iterator it = this.d.iterator();
        while (it.hasNext()) {
            jn jnVar = (jn) it.next();
            if (jnVar.d) {
                iw.a(3, b, "Url transmitted - " + jnVar.f + " Attempts: " + jnVar.e);
                it.remove();
            } else if (jnVar.e > jnVar.a()) {
                iw.a(3, b, "Exceeded max no of attempts - " + jnVar.f + " Attempts: " + jnVar.e);
                it.remove();
            } else if (System.currentTimeMillis() > jnVar.c && jnVar.e > 0) {
                iw.a(3, b, "Expired: Time expired - " + jnVar.f + " Attempts: " + jnVar.e);
                it.remove();
            }
        }
    }

    public abstract ii a();

    public abstract void a(jn jnVar);

    public final synchronized void b(jn jnVar) {
        this.d.add(jnVar);
        hz.a.b(new jt(this));
    }

    public final synchronized void c(jn jnVar) {
        jnVar.d = true;
        hz.a.b(new ju(this));
    }

    public final synchronized void d(jn jnVar) {
        jnVar.e++;
        hz.a.b(new jv(this));
    }
}
