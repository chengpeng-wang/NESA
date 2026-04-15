package com.flurry.sdk;

final class jh extends Thread {
    final /* synthetic */ jg a;

    jh(jg jgVar) {
        this.a = jgVar;
    }

    public final void run() {
        try {
            if (this.a.c != null) {
                this.a.c.disconnect();
            }
        } catch (Throwable th) {
        }
    }
}
