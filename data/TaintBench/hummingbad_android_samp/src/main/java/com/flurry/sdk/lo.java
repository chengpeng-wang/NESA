package com.flurry.sdk;

final class lo {
    long a = 1000;
    boolean b = true;
    boolean c = false;
    final lz d = new lp(this);

    public final synchronized void a() {
        if (!this.c) {
            hz.a.a(this.d, this.a);
            this.c = true;
        }
    }

    public final synchronized void b() {
        if (this.c) {
            hz hzVar = hz.a;
            lz lzVar = this.d;
            if (lzVar != null) {
                hzVar.c.removeCallbacks(lzVar);
            }
            this.c = false;
        }
    }
}
