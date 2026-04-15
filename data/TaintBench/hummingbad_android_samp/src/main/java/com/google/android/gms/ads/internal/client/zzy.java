package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.doubleclick.OnCustomRenderedAdLoadedListener;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzci;
import com.google.android.gms.internal.zzeg;
import com.google.android.gms.internal.zzfr;
import com.google.android.gms.internal.zzfv;
import java.util.concurrent.atomic.AtomicBoolean;

public class zzy {
    private final zzg zznK;
    private boolean zzoM;
    private String zzoZ;
    private AppEventListener zzsK;
    private AdSize[] zzsL;
    private zza zzsn;
    private AdListener zzso;
    private final zzeg zztk;
    private final AtomicBoolean zztl;
    private zzr zztm;
    private String zztn;
    private ViewGroup zzto;
    private InAppPurchaseListener zztp;
    private PlayStorePurchaseListener zztq;
    private OnCustomRenderedAdLoadedListener zztr;

    public zzy(ViewGroup viewGroup) {
        this(viewGroup, null, false, zzg.zzcA());
    }

    public zzy(ViewGroup viewGroup, AttributeSet attributeSet, boolean z) {
        this(viewGroup, attributeSet, z, zzg.zzcA());
    }

    zzy(ViewGroup viewGroup, AttributeSet attributeSet, boolean z, zzg zzg) {
        this(viewGroup, attributeSet, z, zzg, null);
    }

    zzy(ViewGroup viewGroup, AttributeSet attributeSet, boolean z, zzg zzg, zzr zzr) {
        this.zztk = new zzeg();
        this.zzto = viewGroup;
        this.zznK = zzg;
        this.zztm = zzr;
        this.zztl = new AtomicBoolean(false);
        if (attributeSet != null) {
            Context context = viewGroup.getContext();
            try {
                zzj zzj = new zzj(context, attributeSet);
                this.zzsL = zzj.zzi(z);
                this.zzoZ = zzj.getAdUnitId();
                if (viewGroup.isInEditMode()) {
                    zzk.zzcE().zza(viewGroup, new AdSizeParcel(context, this.zzsL[0]), "Ads by Google");
                }
            } catch (IllegalArgumentException e) {
                zzk.zzcE().zza(viewGroup, new AdSizeParcel(context, AdSize.BANNER), e.getMessage(), e.getMessage());
            }
        }
    }

    private void zzcR() {
        try {
            zzd zzaM = this.zztm.zzaM();
            if (zzaM != null) {
                this.zzto.addView((View) zze.zzp(zzaM));
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to get an ad frame.", e);
        }
    }

    public void destroy() {
        try {
            if (this.zztm != null) {
                this.zztm.destroy();
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to destroy AdView.", e);
        }
    }

    public AdListener getAdListener() {
        return this.zzso;
    }

    public AdSize getAdSize() {
        try {
            if (this.zztm != null) {
                AdSizeParcel zzaN = this.zztm.zzaN();
                if (zzaN != null) {
                    return zzaN.zzcC();
                }
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to get the current AdSize.", e);
        }
        return this.zzsL != null ? this.zzsL[0] : null;
    }

    public AdSize[] getAdSizes() {
        return this.zzsL;
    }

    public String getAdUnitId() {
        return this.zzoZ;
    }

    public AppEventListener getAppEventListener() {
        return this.zzsK;
    }

    public InAppPurchaseListener getInAppPurchaseListener() {
        return this.zztp;
    }

    public String getMediationAdapterClassName() {
        try {
            if (this.zztm != null) {
                return this.zztm.getMediationAdapterClassName();
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to get the mediation adapter class name.", e);
        }
        return null;
    }

    public OnCustomRenderedAdLoadedListener getOnCustomRenderedAdLoadedListener() {
        return this.zztr;
    }

    public boolean isLoading() {
        try {
            if (this.zztm != null) {
                return this.zztm.isLoading();
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to check if ad is loading.", e);
        }
        return false;
    }

    public void pause() {
        try {
            if (this.zztm != null) {
                this.zztm.pause();
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to call pause.", e);
        }
    }

    public void recordManualImpression() {
        if (!this.zztl.getAndSet(true)) {
            try {
                if (this.zztm != null) {
                    this.zztm.zzaP();
                }
            } catch (RemoteException e) {
                zzb.zzd("Failed to record impression.", e);
            }
        }
    }

    public void resume() {
        try {
            if (this.zztm != null) {
                this.zztm.resume();
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to call resume.", e);
        }
    }

    public void setAdListener(AdListener adListener) {
        try {
            this.zzso = adListener;
            if (this.zztm != null) {
                this.zztm.zza(adListener != null ? new zzc(adListener) : null);
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to set the AdListener.", e);
        }
    }

    public void setAdSizes(AdSize... adSizes) {
        if (this.zzsL != null) {
            throw new IllegalStateException("The ad size can only be set once on AdView.");
        }
        zza(adSizes);
    }

    public void setAdUnitId(String adUnitId) {
        if (this.zzoZ != null) {
            throw new IllegalStateException("The ad unit ID can only be set once on AdView.");
        }
        this.zzoZ = adUnitId;
    }

    public void setAppEventListener(AppEventListener appEventListener) {
        try {
            this.zzsK = appEventListener;
            if (this.zztm != null) {
                this.zztm.zza(appEventListener != null ? new zzi(appEventListener) : null);
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to set the AppEventListener.", e);
        }
    }

    public void setInAppPurchaseListener(InAppPurchaseListener inAppPurchaseListener) {
        if (this.zztq != null) {
            throw new IllegalStateException("Play store purchase parameter has already been set.");
        }
        try {
            this.zztp = inAppPurchaseListener;
            if (this.zztm != null) {
                this.zztm.zza(inAppPurchaseListener != null ? new zzfr(inAppPurchaseListener) : null);
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to set the InAppPurchaseListener.", e);
        }
    }

    public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) {
        this.zzoM = manualImpressionsEnabled;
        try {
            if (this.zztm != null) {
                this.zztm.setManualImpressionsEnabled(this.zzoM);
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to set manual impressions.", e);
        }
    }

    public void setOnCustomRenderedAdLoadedListener(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        this.zztr = onCustomRenderedAdLoadedListener;
        try {
            if (this.zztm != null) {
                this.zztm.zza(onCustomRenderedAdLoadedListener != null ? new zzci(onCustomRenderedAdLoadedListener) : null);
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to set the onCustomRenderedAdLoadedListener.", e);
        }
    }

    public void setPlayStorePurchaseParams(PlayStorePurchaseListener playStorePurchaseListener, String publicKey) {
        if (this.zztp != null) {
            throw new IllegalStateException("InAppPurchaseListener has already been set.");
        }
        try {
            this.zztq = playStorePurchaseListener;
            this.zztn = publicKey;
            if (this.zztm != null) {
                this.zztm.zza(playStorePurchaseListener != null ? new zzfv(playStorePurchaseListener) : null, publicKey);
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to set the play store purchase parameter.", e);
        }
    }

    public void zza(zza zza) {
        try {
            this.zzsn = zza;
            if (this.zztm != null) {
                this.zztm.zza(zza != null ? new zzb(zza) : null);
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to set the AdClickListener.", e);
        }
    }

    public void zza(zzx zzx) {
        try {
            if (this.zztm == null) {
                zzcS();
            }
            if (this.zztm.zza(this.zznK.zza(this.zzto.getContext(), zzx))) {
                this.zztk.zze(zzx.zzcN());
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to load ad.", e);
        }
    }

    public void zza(AdSize... adSizeArr) {
        this.zzsL = adSizeArr;
        try {
            if (this.zztm != null) {
                this.zztm.zza(new AdSizeParcel(this.zzto.getContext(), this.zzsL));
            }
        } catch (RemoteException e) {
            zzb.zzd("Failed to set the ad size.", e);
        }
        this.zzto.requestLayout();
    }

    /* access modifiers changed from: 0000 */
    public void zzcS() throws RemoteException {
        if ((this.zzsL == null || this.zzoZ == null) && this.zztm == null) {
            throw new IllegalStateException("The ad size and ad unit ID must be set before loadAd is called.");
        }
        this.zztm = zzcT();
        if (this.zzso != null) {
            this.zztm.zza(new zzc(this.zzso));
        }
        if (this.zzsn != null) {
            this.zztm.zza(new zzb(this.zzsn));
        }
        if (this.zzsK != null) {
            this.zztm.zza(new zzi(this.zzsK));
        }
        if (this.zztp != null) {
            this.zztm.zza(new zzfr(this.zztp));
        }
        if (this.zztq != null) {
            this.zztm.zza(new zzfv(this.zztq), this.zztn);
        }
        if (this.zztr != null) {
            this.zztm.zza(new zzci(this.zztr));
        }
        this.zztm.zza(zzk.zzcG());
        this.zztm.setManualImpressionsEnabled(this.zzoM);
        zzcR();
    }

    /* access modifiers changed from: protected */
    public zzr zzcT() throws RemoteException {
        Context context = this.zzto.getContext();
        return zzk.zzcF().zza(context, new AdSizeParcel(context, this.zzsL), this.zzoZ, this.zztk);
    }
}
