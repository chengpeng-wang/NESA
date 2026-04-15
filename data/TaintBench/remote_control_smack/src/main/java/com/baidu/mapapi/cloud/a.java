package com.baidu.mapapi.cloud;

import android.os.Handler;
import android.os.Message;

final class a extends Handler {
    a() {
    }

    public void handleMessage(Message message) {
        if ((message.arg1 == 50 || message.arg1 == 51) && GeoSearchManager.b != null) {
            GeoSearchManager.b.a(message);
            super.handleMessage(message);
        }
    }
}
