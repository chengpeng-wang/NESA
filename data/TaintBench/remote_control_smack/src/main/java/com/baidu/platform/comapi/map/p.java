package com.baidu.platform.comapi.map;

import android.os.Handler;
import android.os.Message;

class p extends Handler {
    final /* synthetic */ o a;

    p(o oVar) {
        this.a = oVar;
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (!(message.what != 4000 || this.a.l == null || this.a.l.f == null)) {
            this.a.l.f.a(message.arg2 == 1);
        }
        if (message.what == 39 && this.a.l != null) {
            if (message.arg1 == 100) {
                if (this.a.l.e != null) {
                    this.a.l.e.b();
                    if (this.a.af) {
                        this.a.n();
                    }
                    if (this.a.ag) {
                        this.a.o();
                    }
                }
            } else if (message.arg1 == 200) {
                if (this.a.l.e != null) {
                    this.a.l.e.c();
                }
            } else if (message.arg1 == 0) {
                this.a.l.setRenderMode(0);
            } else {
                this.a.l.setRenderMode(1);
            }
            if (this.a.l.e != null) {
                this.a.l.e.a();
            }
        }
        if (message.what == 512) {
            int i = message.arg1;
            if (this.a.l.e != null) {
                this.a.l.e.a(i);
            }
        }
    }
}
