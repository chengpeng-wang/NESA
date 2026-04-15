package com.google.android.gms.ads.internal;

import android.content.Context;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzcd;
import com.google.android.gms.internal.zzce;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzfc;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhj;
import com.google.android.gms.internal.zzhj.zza;
import com.google.android.gms.internal.zzhu;
import com.google.android.gms.internal.zzip;

@zzgk
public abstract class zzc extends zzb implements zzg, zzfc {
    public zzc(Context context, AdSizeParcel adSizeParcel, String str, zzeh zzeh, VersionInfoParcel versionInfoParcel, zzd zzd) {
        super(context, adSizeParcel, str, zzeh, versionInfoParcel, zzd);
    }

    public void recordClick() {
        onAdClicked();
    }

    public void recordImpression() {
        zza(this.zzos.zzqg, false);
    }

    /* access modifiers changed from: protected */
    public zzip zza(zza zza, zze zze) {
        zzip zzip;
        View nextView = this.zzos.zzqc.getNextView();
        zzip zzip2;
        if (nextView instanceof zzip) {
            zzb.zzaC("Reusing webview...");
            zzip2 = (zzip) nextView;
            zzip2.zza(this.zzos.context, this.zzos.zzqf);
            zzip = zzip2;
        } else {
            if (nextView != null) {
                this.zzos.zzqc.removeView(nextView);
            }
            zzip2 = zzp.zzby().zza(this.zzos.context, this.zzos.zzqf, false, false, this.zzos.zzqa, this.zzos.zzqb, this.zzov);
            if (this.zzos.zzqf.zzsI == null) {
                zzb((View) zzip2.getWebView());
            }
            zzip = zzip2;
        }
        zzip.zzgS().zzb(this, this, this, this, false, this, null, zze, this);
        zzip.zzaG(zza.zzGL.zzDQ);
        zzip.zzaH(zza.zzGL.zzDO);
        return zzip;
    }

    public void zza(int i, int i2, int i3, int i4) {
        zzaS();
    }

    public void zza(zzch zzch) {
        zzx.zzch("setOnCustomRenderedAdLoadedListener must be called on the main UI thread.");
        this.zzos.zzqu = zzch;
    }

    /* access modifiers changed from: protected */
    public void zza(final zza zza, final zzcd zzcd) {
        if (zza.errorCode != -2) {
            zzhu.zzHK.post(new Runnable() {
                public void run() {
                    zzc.this.zzb(new zzhj(zza, null, null, null, null, null, null));
                }
            });
            return;
        }
        if (zza.zzqf != null) {
            this.zzos.zzqf = zza.zzqf;
        }
        if (zza.zzGM.zzDX) {
            this.zzos.zzqz = 0;
            this.zzos.zzqe = zzp.zzbw().zza(this.zzos.context, this, zza, this.zzos.zzqa, null, this.zzow, this, zzcd);
            return;
        }
        zzhu.zzHK.post(new Runnable() {
            public void run() {
                if (zza.zzGM.zzEg && zzc.this.zzos.zzqu != null) {
                    String str = null;
                    if (zza.zzGM.zzAT != null) {
                        str = zzp.zzbx().zzaw(zza.zzGM.zzAT);
                    }
                    zzce zzce = new zzce(zzc.this, str, zza.zzGM.body);
                    zzc.this.zzos.zzqz = 1;
                    try {
                        zzc.this.zzos.zzqu.zza(zzce);
                        return;
                    } catch (RemoteException e) {
                        zzb.zzd("Could not call the onCustomRenderedAdLoadedListener.", e);
                    }
                }
                final zze zze = new zze();
                zzip zza = zzc.this.zza(zza, zze);
                zze.zza(new zze.zzb(zza, zza));
                zza.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        zze.recordClick();
                        return false;
                    }
                });
                zza.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        zze.recordClick();
                    }
                });
                zzc.this.zzos.zzqz = 0;
                zzc.this.zzos.zzqe = zzp.zzbw().zza(zzc.this.zzos.context, zzc.this, zza, zzc.this.zzos.zzqa, zza, zzc.this.zzow, zzc.this, zzcd);
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean zza(zzhj zzhj, zzhj zzhj2) {
        if (this.zzos.zzbP() && this.zzos.zzqc != null) {
            this.zzos.zzqc.zzbT().zzaz(zzhj2.zzEc);
        }
        return super.zza(zzhj, zzhj2);
    }

    public void zzbc() {
        zzaQ();
    }

    public void zzc(View view) {
        this.zzos.zzqy = view;
        zzb(new zzhj(this.zzos.zzqh, null, null, null, null, null, null));
    }
}
