package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.ads.internal.zzp;

@zzgk
public class zzhr extends Handler {
    public zzhr(Looper looper) {
        super(looper);
    }

    public void handleMessage(Message msg) {
        try {
            super.handleMessage(msg);
        } catch (Exception e) {
            zzp.zzbA().zzc(e, false);
            throw e;
        }
    }
}
