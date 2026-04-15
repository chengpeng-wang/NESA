package com.baidu.android.pushservice;

import android.content.Intent;

class k implements Runnable {
    final /* synthetic */ PushSDK a;

    k(PushSDK pushSDK) {
        this.a = pushSDK;
    }

    public void run() {
        this.a.handleOnStart(new Intent());
    }
}
