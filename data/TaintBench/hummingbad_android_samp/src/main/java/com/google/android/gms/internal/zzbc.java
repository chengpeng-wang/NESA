package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzbb.zza;
import java.util.concurrent.Future;

@zzgk
public class zzbc {
    /* access modifiers changed from: private */
    public zzbb zza(Context context, VersionInfoParcel versionInfoParcel, final zzie<zzbb> zzie, zzan zzan) {
        final zzbd zzbd = new zzbd(context, versionInfoParcel, zzan);
        zzbd.zza(new zza() {
            public void zzcj() {
                zzie.zzf(zzbd);
            }
        });
        return zzbd;
    }

    public Future<zzbb> zza(Context context, VersionInfoParcel versionInfoParcel, String str, zzan zzan) {
        final zzie zzie = new zzie();
        final Context context2 = context;
        final VersionInfoParcel versionInfoParcel2 = versionInfoParcel;
        final zzan zzan2 = zzan;
        final String str2 = str;
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                zzbc.this.zza(context2, versionInfoParcel2, zzie, zzan2).zzt(str2);
            }
        });
        return zzie;
    }
}
