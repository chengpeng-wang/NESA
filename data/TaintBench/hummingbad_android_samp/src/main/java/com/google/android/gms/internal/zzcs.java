package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd;
import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.List;

@zzgk
public class zzcs implements NativeCustomTemplateAd {
    private final zzcr zzwC;

    public zzcs(zzcr zzcr) {
        this.zzwC = zzcr;
    }

    public List<String> getAvailableAssetNames() {
        try {
            return this.zzwC.getAvailableAssetNames();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get available asset names.", e);
            return null;
        }
    }

    public String getCustomTemplateId() {
        try {
            return this.zzwC.getCustomTemplateId();
        } catch (RemoteException e) {
            zzb.zzb("Failed to get custom template id.", e);
            return null;
        }
    }

    public Image getImage(String assetName) {
        try {
            zzcj zzT = this.zzwC.zzT(assetName);
            if (zzT != null) {
                return new zzck(zzT);
            }
        } catch (RemoteException e) {
            zzb.zzb("Failed to get image.", e);
        }
        return null;
    }

    public CharSequence getText(String assetName) {
        try {
            return this.zzwC.zzS(assetName);
        } catch (RemoteException e) {
            zzb.zzb("Failed to get string.", e);
            return null;
        }
    }

    public void performClick(String assetName) {
        try {
            this.zzwC.performClick(assetName);
        } catch (RemoteException e) {
            zzb.zzb("Failed to perform click.", e);
        }
    }

    public void recordImpression() {
        try {
            this.zzwC.recordImpression();
        } catch (RemoteException e) {
            zzb.zzb("Failed to record impression.", e);
        }
    }
}
