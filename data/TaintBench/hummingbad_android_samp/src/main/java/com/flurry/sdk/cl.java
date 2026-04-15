package com.flurry.sdk;

import android.os.FileObserver;

final class cl extends FileObserver {
    final /* synthetic */ ck a;

    cl(ck ckVar, String str) {
        this.a = ckVar;
        super(str);
    }

    public final void onEvent(int i, String str) {
        if ((i & 2048) != 0 || (i & 1024) != 0) {
            hz.a.c.post(new cm(this));
        }
    }
}
