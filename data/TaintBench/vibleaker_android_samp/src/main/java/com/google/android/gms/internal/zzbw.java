package com.google.android.gms.internal;

import android.text.TextUtils;

@zzhb
public class zzbw {
    public zzbv zza(zzbu zzbu) {
        if (zzbu == null) {
            throw new IllegalArgumentException("CSI configuration can't be null. ");
        } else if (!zzbu.zzdu()) {
            zzin.v("CsiReporterFactory: CSI is not enabled. No CSI reporter created.");
            return null;
        } else if (zzbu.getContext() == null) {
            throw new IllegalArgumentException("Context can't be null. Please set up context in CsiConfiguration.");
        } else if (!TextUtils.isEmpty(zzbu.zzcs())) {
            return new zzbv(zzbu.getContext(), zzbu.zzcs(), zzbu.zzdv(), zzbu.zzdw());
        } else {
            throw new IllegalArgumentException("AfmaVersion can't be null or empty. Please set up afmaVersion in CsiConfiguration.");
        }
    }
}
