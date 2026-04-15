package com.baidu.android.pushservice;

class l implements Runnable {
    final /* synthetic */ PushSDK a;

    l(PushSDK pushSDK) {
        this.a = pushSDK;
    }

    public void run() {
        this.a.sendRequestTokenIntent();
    }
}
