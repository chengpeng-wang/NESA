package com.baidu.android.pushservice;

import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.jni.PushSocket;
import com.baidu.android.pushservice.message.c;
import com.baidu.android.pushservice.util.m;
import java.net.Socket;

class f implements Runnable {
    final /* synthetic */ e a;

    f(e eVar) {
        this.a = eVar;
    }

    public void run() {
        if (PushSocket.a) {
            e.a = PushSocket.createSocket(w.b, w.c);
            if (e.a == -1) {
                Log.e("PushConnection", "Create socket err, errno:" + PushSocket.getLastSocketError());
                e.e = Boolean.valueOf(false);
                this.a.g();
                return;
            }
        }
        try {
            this.a.g = new Socket(w.b, w.c);
            this.a.h = this.a.g.getInputStream();
            this.a.i = this.a.g.getOutputStream();
        } catch (Throwable th) {
            Log.e("PushConnection", "Connecting exception: " + th);
            e.e = Boolean.valueOf(false);
            this.a.g();
            return;
        }
        if (b.a()) {
            Log.i("PushConnection", "create Socket ok");
            m.a("create Socket ok socketfd" + e.a);
        }
        if (PushSocket.a) {
            this.a.c = new c(this.a.o.getApplicationContext(), this.a);
        } else {
            this.a.c = new c(this.a.o.getApplicationContext(), this.a, this.a.h, this.a.i);
        }
        this.a.d = true;
        this.a.a(true);
        if (this.a.l != null) {
            this.a.l.interrupt();
        }
        if (this.a.k != null) {
            this.a.k.interrupt();
        }
        this.a.f = false;
        this.a.l = new i(this.a);
        this.a.l.start();
        this.a.k = new j(this.a);
        this.a.k.start();
        if (PushSocket.a) {
            this.a.c.a(e.a);
        } else {
            this.a.c.a();
        }
        e.e = Boolean.valueOf(false);
    }
}
