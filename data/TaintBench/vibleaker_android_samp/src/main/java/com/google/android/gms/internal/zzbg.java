package com.google.android.gms.internal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class zzbg {
    private static MessageDigest zzto = null;
    protected Object zzpV = new Object();

    /* access modifiers changed from: protected */
    public MessageDigest zzcL() {
        MessageDigest messageDigest;
        synchronized (this.zzpV) {
            if (zzto != null) {
                messageDigest = zzto;
            } else {
                for (int i = 0; i < 2; i++) {
                    try {
                        zzto = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                    }
                }
                messageDigest = zzto;
            }
        }
        return messageDigest;
    }

    public abstract byte[] zzu(String str);
}
