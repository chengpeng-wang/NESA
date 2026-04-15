package com.google.android.gms.ads.internal.formats;

import android.content.Context;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzn;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzek;
import com.google.android.gms.internal.zzel;
import com.google.android.gms.internal.zzip;
import java.lang.ref.WeakReference;
import java.util.Map;

public class zzg extends zzh {
    private Object zzpc;
    private zzek zzvZ;
    private zzel zzwa;
    private final zzn zzwb;
    private zzh zzwc;
    private boolean zzwd;

    private zzg(Context context, zzn zzn, zzan zzan) {
        super(context, zzn, null, zzan, null, null, null);
        this.zzwd = false;
        this.zzpc = new Object();
        this.zzwb = zzn;
    }

    public zzg(Context context, zzn zzn, zzan zzan, zzek zzek) {
        this(context, zzn, zzan);
        this.zzvZ = zzek;
    }

    public zzg(Context context, zzn zzn, zzan zzan, zzel zzel) {
        this(context, zzn, zzan);
        this.zzwa = zzel;
    }

    public void recordImpression() {
        zzx.zzch("recordImpression must be called on the main UI thread.");
        synchronized (this.zzpc) {
            zzl(true);
            if (this.zzwc != null) {
                this.zzwc.recordImpression();
            } else {
                try {
                    if (this.zzvZ != null && !this.zzvZ.getOverrideClickHandling()) {
                        this.zzvZ.recordImpression();
                    } else if (!(this.zzwa == null || this.zzwa.getOverrideClickHandling())) {
                        this.zzwa.recordImpression();
                    }
                } catch (RemoteException e) {
                    zzb.zzd("Failed to call recordImpression", e);
                }
            }
            this.zzwb.recordImpression();
        }
    }

    public zzb zza(OnClickListener onClickListener) {
        return null;
    }

    public void zzb(View view, Map<String, WeakReference<View>> map) {
        zzx.zzch("performClick must be called on the main UI thread.");
        synchronized (this.zzpc) {
            if (this.zzwc != null) {
                this.zzwc.zzb(view, map);
            } else {
                try {
                    if (!(this.zzvZ == null || this.zzvZ.getOverrideClickHandling())) {
                        this.zzvZ.zzc(zze.zzx(view));
                    }
                    if (!(this.zzwa == null || this.zzwa.getOverrideClickHandling())) {
                        this.zzvZ.zzc(zze.zzx(view));
                    }
                } catch (RemoteException e) {
                    zzb.zzd("Failed to call performClick", e);
                }
            }
            this.zzwb.onAdClicked();
        }
    }

    public void zzb(zzh zzh) {
        synchronized (this.zzpc) {
            this.zzwc = zzh;
        }
    }

    public boolean zzdx() {
        boolean z;
        synchronized (this.zzpc) {
            z = this.zzwd;
        }
        return z;
    }

    public zzip zzdy() {
        return null;
    }

    public void zzh(View view) {
        synchronized (this.zzpc) {
            this.zzwd = true;
            try {
                if (this.zzvZ != null) {
                    this.zzvZ.zzd(zze.zzx(view));
                } else if (this.zzwa != null) {
                    this.zzwa.zzd(zze.zzx(view));
                }
            } catch (RemoteException e) {
                zzb.zzd("Failed to call prepareAd", e);
            }
            this.zzwd = false;
        }
    }
}
