package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.ads.internal.formats.zzj;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.internal.zzcl.zza;

public class zzcx extends zzg<zzcm> {
    public zzcx() {
        super("com.google.android.gms.ads.NativeAdViewDelegateCreatorImpl");
    }

    private zzcl zzb(Context context, FrameLayout frameLayout, FrameLayout frameLayout2) {
        try {
            return zza.zzu(((zzcm) zzar(context)).zza(zze.zzx(context), zze.zzx(frameLayout), zze.zzx(frameLayout2), 7895000));
        } catch (RemoteException | zzg.zza e) {
            zzb.zzd("Could not create remote NativeAdViewDelegate.", e);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzD */
    public zzcm zzd(IBinder iBinder) {
        return zzcm.zza.zzv(iBinder);
    }

    public zzcl zza(Context context, FrameLayout frameLayout, FrameLayout frameLayout2) {
        if (zzk.zzcE().zzR(context)) {
            zzcl zzb = zzb(context, frameLayout, frameLayout2);
            if (zzb != null) {
                return zzb;
            }
        }
        zzb.zzaC("Using NativeAdViewDelegate from the client jar.");
        return new zzj(frameLayout, frameLayout2);
    }
}
