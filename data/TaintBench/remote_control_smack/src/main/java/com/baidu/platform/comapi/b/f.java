package com.baidu.platform.comapi.b;

import android.os.Handler;
import android.os.Message;

class f extends Handler {
    final /* synthetic */ e a;

    f(e eVar) {
        this.a = eVar;
    }

    public void handleMessage(Message message) {
        if (message.arg1 != 50 && message.arg1 != 51 && this.a.c != null) {
            this.a.c.a(message);
            super.handleMessage(message);
        }
    }
}
