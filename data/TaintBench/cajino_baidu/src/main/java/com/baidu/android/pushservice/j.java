package com.baidu.android.pushservice;

import com.baidu.android.common.logging.Log;
import com.baidu.android.pushservice.jni.PushSocket;
import com.baidu.android.pushservice.message.b;
import java.io.IOException;
import org.apache.log4j.helpers.FileWatchdog;

class j extends Thread {
    final /* synthetic */ e a;

    j(e eVar) {
        this.a = eVar;
        setName("PushService-PushConnection-SendThread");
    }

    public void run() {
        while (!this.a.f) {
            b bVar;
            synchronized (this.a.j) {
                if (this.a.j.size() == 0) {
                    try {
                        this.a.j.wait();
                    } catch (InterruptedException e) {
                        Log.e("PushConnection", "SendThread wait exception: " + e);
                    }
                }
                bVar = this.a.j.size() > 0 ? (b) this.a.j.removeFirst() : null;
            }
            if (!this.a.f) {
                if (!(bVar == null || bVar.c == null)) {
                    if (b.a()) {
                        Log.d("PushConnection", "SendThread send msg :" + bVar.toString());
                    }
                    if (bVar.d) {
                        if (bVar.a()) {
                            this.a.r = true;
                        } else {
                            this.a.r = false;
                        }
                        this.a.b.removeCallbacks(this.a.t);
                        this.a.b.postDelayed(this.a.t, FileWatchdog.DEFAULT_DELAY);
                    }
                    if (!PushSocket.a) {
                        try {
                            this.a.i.write(bVar.c);
                            this.a.i.flush();
                        } catch (IOException e2) {
                            Log.e("PushConnection", "SendThread exception: " + e2);
                            this.a.g();
                        }
                    } else if (PushSocket.sendMsg(e.a, bVar.c, bVar.c.length) == -1) {
                        Log.e("PushConnection", "sendMsg err, errno:" + PushSocket.getLastSocketError());
                        this.a.g();
                    }
                }
            } else {
                return;
            }
        }
    }
}
