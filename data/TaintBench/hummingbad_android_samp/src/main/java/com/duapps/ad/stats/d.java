package com.duapps.ad.stats;

/* compiled from: ToolClickHandler */
final class d implements Runnable {
    private /* synthetic */ j a;
    private /* synthetic */ String b;
    private /* synthetic */ b c;

    d(b bVar, j jVar, String str) {
        this.c = bVar;
        this.a = jVar;
        this.b = str;
    }

    public final void run() {
        this.c.b(this.a, this.b);
    }
}
