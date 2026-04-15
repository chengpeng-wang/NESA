package com.google.android.gms.internal;

import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.ads.doubleclick.CustomRenderedAd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;

@zzhb
public class zzcd implements CustomRenderedAd {
    private final zzce zzxH;

    public zzcd(zzce zzce) {
        this.zzxH = zzce;
    }

    public String getBaseUrl() {
        try {
            return this.zzxH.zzdF();
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate getBaseURL to CustomRenderedAd", e);
            return null;
        }
    }

    public String getContent() {
        try {
            return this.zzxH.getContent();
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate getContent to CustomRenderedAd", e);
            return null;
        }
    }

    public void onAdRendered(View view) {
        try {
            this.zzxH.zzb(view != null ? zze.zzC(view) : null);
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate onAdRendered to CustomRenderedAd", e);
        }
    }

    public void recordClick() {
        try {
            this.zzxH.recordClick();
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate recordClick to CustomRenderedAd", e);
        }
    }

    public void recordImpression() {
        try {
            this.zzxH.recordImpression();
        } catch (RemoteException e) {
            zzb.zzd("Could not delegate recordImpression to CustomRenderedAd", e);
        }
    }
}
