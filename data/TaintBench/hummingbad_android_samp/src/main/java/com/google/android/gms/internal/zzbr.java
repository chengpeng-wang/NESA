package com.google.android.gms.internal;

import android.text.TextUtils;

@zzgk
public final class zzbr {
    private String zztN;
    private int zztO;

    public zzbr() {
        this((String) zzby.zztW.zzdd(), -1);
    }

    public zzbr(String str) {
        this(str, -1);
    }

    public zzbr(String str, int i) {
        this.zztO = -1;
        this.zztN = TextUtils.isEmpty(str) ? (String) zzby.zztW.zzdd() : str;
        this.zztO = i;
    }

    public String zzdb() {
        return this.zztN;
    }

    public int zzdc() {
        return this.zztO;
    }
}
