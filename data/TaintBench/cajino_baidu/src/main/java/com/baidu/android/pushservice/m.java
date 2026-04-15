package com.baidu.android.pushservice;

class m implements Runnable {
    final /* synthetic */ PushSDK a;

    m(PushSDK pushSDK) {
        this.a = pushSDK;
    }

    public void run() {
        synchronized (PushSDK.mPushConnLock) {
            if (PushSDK.mPushConnection != null) {
                PushSDK.mPushConnection.b();
            }
        }
    }
}
