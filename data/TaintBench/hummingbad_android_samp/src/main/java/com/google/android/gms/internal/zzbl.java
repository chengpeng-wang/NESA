package com.google.android.gms.internal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class zzbl {
    private static MessageDigest zzsd = null;
    protected Object zzpc = new Object();

    /* access modifiers changed from: protected */
    public MessageDigest zzcy() {
        MessageDigest messageDigest;
        synchronized (this.zzpc) {
            if (zzsd != null) {
                messageDigest = zzsd;
            } else {
                for (int i = 0; i < 2; i++) {
                    try {
                        zzsd = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                    }
                }
                messageDigest = zzsd;
            }
        }
        return messageDigest;
    }

    public abstract byte[] zzz(String str);
}
