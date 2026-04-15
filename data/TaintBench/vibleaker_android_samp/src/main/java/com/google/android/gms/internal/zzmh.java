package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zza.zzb;

public final class zzmh implements zzmg {

    private static class zza extends zzme {
        private final zzb<Status> zzamC;

        public zza(zzb<Status> zzb) {
            this.zzamC = zzb;
        }

        public void zzcb(int i) throws RemoteException {
            this.zzamC.zzs(new Status(i));
        }
    }

    public PendingResult<Status> zzf(GoogleApiClient googleApiClient) {
        return googleApiClient.zzb(new zza(googleApiClient) {
            /* access modifiers changed from: protected */
            public void zza(zzmj zzmj) throws RemoteException {
                ((zzml) zzmj.zzqJ()).zza(new zza(this));
            }
        });
    }
}
