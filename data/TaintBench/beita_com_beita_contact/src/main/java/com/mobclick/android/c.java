package com.mobclick.android;

class c extends Thread {
    final /* synthetic */ a a;

    c(a aVar) {
        this.a = aVar;
    }

    public void run() {
        if (this.a.l != d.c) {
            this.a.b(true);
        } else if (this.a.k) {
            this.a.b(false);
        }
    }
}
