package com.baidu.platform.comapi.map;

import android.os.Handler;
import android.os.Message;

class i extends Handler {
    final /* synthetic */ h a;

    i(h hVar) {
        this.a = hVar;
    }

    public void handleMessage(Message message) {
        if (h.c != null) {
            h.c.a(message);
            super.handleMessage(message);
        }
    }
}
