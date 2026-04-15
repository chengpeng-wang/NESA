package com.google.android.gms.internal;

import com.google.android.gms.internal.zzm.zza;
import com.google.android.gms.internal.zzm.zzb;
import java.io.UnsupportedEncodingException;

public class zzab extends zzk<String> {
    private final zzb<String> zzaG;

    public zzab(int i, String str, zzb<String> zzb, zza zza) {
        super(i, str, zza);
        this.zzaG = zzb;
    }

    public zzab(String str, zzb<String> zzb, zza zza) {
        this(0, str, zzb, zza);
    }

    /* access modifiers changed from: protected */
    public zzm<String> zza(zzi zzi) {
        Object str;
        try {
            str = new String(zzi.data, zzx.zza(zzi.zzA));
        } catch (UnsupportedEncodingException e) {
            str = new String(zzi.data);
        }
        return zzm.zza(str, zzx.zzb(zzi));
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzi */
    public void zza(String str) {
        this.zzaG.zzb(str);
    }
}
