package com.duapps.ad.entity.strategy;

import android.content.Context;
import com.duapps.ad.DuAdDataCallBack;
import com.duapps.ad.base.d;

/* compiled from: BaseChannel */
public abstract class a<T> {
    public volatile boolean a;
    public volatile boolean b;
    public volatile boolean c;
    public long d;
    protected Context e;
    protected int f;
    protected DuAdDataCallBack g;

    public abstract void a();

    public abstract int b();

    public abstract T d();

    public a(Context context, int i, long j) {
        this.d = j;
        this.e = context;
        this.f = i;
        d.a(i);
    }

    public final void a(DuAdDataCallBack duAdDataCallBack) {
        this.g = duAdDataCallBack;
    }

    public void c() {
    }
}
