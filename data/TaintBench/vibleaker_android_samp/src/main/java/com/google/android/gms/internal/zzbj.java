package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import java.security.MessageDigest;

@zzhb
public class zzbj extends zzbg {
    private MessageDigest zztw;

    /* access modifiers changed from: 0000 */
    public byte[] zza(String[] strArr) {
        byte[] bArr = new byte[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            bArr[i] = zzk(zzbi.zzx(strArr[i]));
        }
        return bArr;
    }

    /* access modifiers changed from: 0000 */
    public byte zzk(int i) {
        return (byte) ((((i & 255) ^ ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >> 8)) ^ ((16711680 & i) >> 16)) ^ ((ViewCompat.MEASURED_STATE_MASK & i) >> 24));
    }

    public byte[] zzu(String str) {
        byte[] zza = zza(str.split(" "));
        this.zztw = zzcL();
        synchronized (this.zzpV) {
            if (this.zztw == null) {
                zza = new byte[0];
            } else {
                this.zztw.reset();
                this.zztw.update(zza);
                byte[] digest = this.zztw.digest();
                int i = 4;
                if (digest.length <= 4) {
                    i = digest.length;
                }
                zza = new byte[i];
                System.arraycopy(digest, 0, zza, 0, zza.length);
            }
        }
        return zza;
    }
}
