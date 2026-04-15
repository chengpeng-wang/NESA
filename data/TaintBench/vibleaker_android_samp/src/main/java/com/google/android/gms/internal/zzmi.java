package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

abstract class zzmi<R extends Result> extends com.google.android.gms.common.api.internal.zza.zza<R, zzmj> {

    static abstract class zza extends zzmi<Status> {
        public zza(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        /* renamed from: zzb */
        public Status zzc(Status status) {
            return status;
        }
    }

    public zzmi(GoogleApiClient googleApiClient) {
        super(zzmf.zzUI, googleApiClient);
    }
}
