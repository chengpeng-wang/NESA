package com.baidu.android.pushservice.b;

class p implements Runnable {
    final /* synthetic */ o a;

    p(o oVar) {
        this.a = oVar;
    }

    public void run() {
        if (this.a.d() && m.e(this.a.b)) {
            this.a.c();
        }
    }
}
