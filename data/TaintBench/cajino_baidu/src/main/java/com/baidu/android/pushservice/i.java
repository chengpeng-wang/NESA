package com.baidu.android.pushservice;

import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.jni.PushSocket;
import com.baidu.android.pushservice.message.b;
import com.baidu.android.pushservice.message.d;
import com.baidu.android.pushservice.util.m;

class i extends Thread {
    final /* synthetic */ e a;

    i(e eVar) {
        this.a = eVar;
        setName("PushService-PushConnection-readThread");
    }

    public void run() {
        while (!this.a.f) {
            b a;
            if (PushSocket.a) {
                byte[] a2;
                try {
                    a2 = PushSocket.a(e.a, this.a.c);
                } catch (Exception e) {
                    a2 = null;
                    Log.e("PushConnection", "Get message exception");
                }
                this.a.b.removeCallbacks(this.a.t);
                if (this.a.r) {
                    this.a.r = false;
                    this.a.b(true);
                }
                if (a2 == null || a2.length == 0) {
                    Log.i("PushConnection", "Receive err,errno:" + PushSocket.getLastSocketError());
                    this.a.g();
                } else {
                    try {
                        a = this.a.c.a(a2, a2.length);
                        if (a != null) {
                            try {
                                if (b.a()) {
                                    Log.d("PushConnection", "ReadThread receive msg :" + a.toString());
                                }
                                this.a.c.b(a);
                            } catch (Exception e2) {
                                Log.e("PushConnection", "Handle message exception " + m.a(e2));
                                this.a.g();
                            }
                        }
                        this.a.n = 0;
                    } catch (Exception e22) {
                        Log.i("PushConnection", "Read message exception " + m.a(e22));
                        this.a.g();
                    }
                }
            } else {
                try {
                    a = this.a.c.b();
                    this.a.b.removeCallbacks(this.a.t);
                    if (this.a.r) {
                        this.a.r = false;
                        this.a.b(true);
                    }
                    if (a != null) {
                        if (b.a()) {
                            Log.d("PushConnection", "ReadThread receive msg :" + a.toString());
                        }
                        try {
                            this.a.c.b(a);
                            this.a.n = 0;
                        } catch (d e222) {
                            Log.e("PushConnection", "handleMessage exception.");
                            Log.e("PushConnection", e222);
                            this.a.g();
                        }
                    }
                } catch (Exception e3) {
                    Log.e("PushConnection", "ReadThread exception: " + e3);
                    this.a.g();
                }
            }
        }
    }
}
