package com.baidu.android.pushservice;

class n implements Runnable {
    final /* synthetic */ PushService a;

    n(PushService pushService) {
        this.a = pushService;
    }

    public void run() {
        this.a.stopSelf();
    }
}
