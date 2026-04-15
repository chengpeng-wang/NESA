package com.google.android.gms.internal;

public class zzd implements zzo {
    private int zzo;
    private int zzp;
    private final int zzq;
    private final float zzr;

    public zzd() {
        this(2500, 1, 1.0f);
    }

    public zzd(int i, int i2, float f) {
        this.zzo = i;
        this.zzq = i2;
        this.zzr = f;
    }

    public void zza(zzr zzr) throws zzr {
        this.zzp++;
        this.zzo = (int) (((float) this.zzo) + (((float) this.zzo) * this.zzr));
        if (!zzf()) {
            throw zzr;
        }
    }

    public int zzd() {
        return this.zzo;
    }

    public int zze() {
        return this.zzp;
    }

    /* access modifiers changed from: protected */
    public boolean zzf() {
        return this.zzp <= this.zzq;
    }
}
