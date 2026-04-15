package com.baidu.android.pushservice;

import com.baidu.android.common.logging.Log;

class h implements Runnable {
    final /* synthetic */ e a;

    h(e eVar) {
        this.a = eVar;
    }

    public void run() {
        if (b.a()) {
            Log.i("PushConnection", " -- Send Timeout --");
        }
        if (this.a.r) {
            this.a.r = false;
            this.a.b(false);
        }
        this.a.h();
    }
}
