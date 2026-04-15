package com.google.android.gms.internal;

import android.content.Intent;

public class zza extends zzr {
    private Intent zza;

    public zza(zzi zzi) {
        super(zzi);
    }

    public String getMessage() {
        return this.zza != null ? "User needs to (re)enter credentials." : super.getMessage();
    }
}
