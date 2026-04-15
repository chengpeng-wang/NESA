package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.util.concurrent.Future;

@zzhb
public class zzee {

    private static class zza<JavascriptEngine> extends zzjd<JavascriptEngine> {
        JavascriptEngine zzAR;

        private zza() {
        }

        /* synthetic */ zza(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    /* access modifiers changed from: private */
    public zzed zza(Context context, VersionInfoParcel versionInfoParcel, final zza<zzed> zza, zzan zzan) {
        zzef zzef = new zzef(context, versionInfoParcel, zzan);
        zza.zzAR = zzef;
        zzef.zza(new com.google.android.gms.internal.zzed.zza() {
            public void zzeo() {
                zza.zzg(zza.zzAR);
            }
        });
        return zzef;
    }

    public Future<zzed> zza(Context context, VersionInfoParcel versionInfoParcel, String str, zzan zzan) {
        final zza zza = new zza();
        final Context context2 = context;
        final VersionInfoParcel versionInfoParcel2 = versionInfoParcel;
        final zzan zzan2 = zzan;
        final String str2 = str;
        zzir.zzMc.post(new Runnable() {
            public void run() {
                zzee.this.zza(context2, versionInfoParcel2, zza, zzan2).zzaa(str2);
            }
        });
        return zza;
    }
}
