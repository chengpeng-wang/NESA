package com.flurry.sdk;

final class fm implements ik {
    final /* synthetic */ fl a;

    fm(fl flVar) {
        this.a = flVar;
    }

    public final /* synthetic */ void notify(ij ijVar) {
        if (gb.REQUEST.equals(this.a.e)) {
            hz.a.b(new fn(this));
        } else if (gb.SELECT.equals(this.a.e)) {
            hz.a.b(new fo(this));
        } else if (gb.PRERENDER.equals(this.a.e)) {
            hz.a.b(new fp(this));
        }
    }
}
