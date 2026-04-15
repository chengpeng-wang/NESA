package com.google.android.gms.internal;

import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.ads.doubleclick.CustomRenderedAd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;

@zzgk
public class zzcf implements CustomRenderedAd {
    private final zzcg zzvy;

    public zzcf(zzcg zzcg) {
        this.zzvy = zzcg;
    }

    public String getBaseUrl() {
        try {
            return this.zzvy.zzdp();
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate getBaseURL to CustomRenderedAd", e);
            return null;
        }
    }

    public String getContent() {
        try {
            return this.zzvy.getContent();
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate getContent to CustomRenderedAd", e);
            return null;
        }
    }

    public void onAdRendered(View view) {
        try {
            this.zzvy.zza(view != null ? zze.zzx(view) : null);
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate onAdRendered to CustomRenderedAd", e);
        }
    }

    public void recordClick() {
        try {
            this.zzvy.recordClick();
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate recordClick to CustomRenderedAd", e);
        }
    }

    public void recordImpression() {
        try {
            this.zzvy.recordImpression();
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate recordImpression to CustomRenderedAd", e);
        }
    }
}
