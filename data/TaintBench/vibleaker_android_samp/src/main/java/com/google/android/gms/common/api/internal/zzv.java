package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

public class zzv extends zzb<Status> {
    @Deprecated
    public zzv(Looper looper) {
        super(looper);
    }

    public zzv(GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzb */
    public Status zzc(Status status) {
        return status;
    }
}
