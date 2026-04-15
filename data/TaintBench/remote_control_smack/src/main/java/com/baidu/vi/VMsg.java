package com.baidu.vi;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class VMsg {
    private static Handler a;
    private static HandlerThread b;

    static class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            VMsg.OnSDKUserCommand(message.what, message.arg1, message.arg2);
        }
    }

    public static native void OnSDKUserCommand(int i, int i2, int i3);

    public static void destroy() {
        b.quit();
        b = null;
        a.removeCallbacksAndMessages(null);
        a = null;
    }

    public static void init() {
        b = new HandlerThread("VIMsgThread");
        b.start();
        a = new a(b.getLooper());
    }

    private static void postMessage(int i, int i2, int i3) {
        if (a != null) {
            a.obtainMessage(i, i2, i3).sendToTarget();
        }
    }
}
