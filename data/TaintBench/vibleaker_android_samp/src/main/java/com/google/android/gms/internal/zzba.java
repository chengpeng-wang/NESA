package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzeg.zzd;
import com.google.android.gms.internal.zzji.zza;
import com.google.android.gms.internal.zzji.zzc;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzba extends zzau {
    private zzd zzsF;
    /* access modifiers changed from: private */
    public boolean zzsG;

    public zzba(Context context, AdSizeParcel adSizeParcel, zzif zzif, VersionInfoParcel versionInfoParcel, zzbb zzbb, zzeg zzeg) {
        super(context, adSizeParcel, zzif, versionInfoParcel, zzbb);
        this.zzsF = zzeg.zzer();
        try {
            final JSONObject zzd = zzd(zzbb.zzcq().zzco());
            this.zzsF.zza(new zzc<zzeh>() {
                /* renamed from: zzd */
                public void zze(zzeh zzeh) {
                    zzba.this.zza(zzd);
                }
            }, new zza() {
                public void run() {
                }
            });
        } catch (JSONException e) {
        } catch (RuntimeException e2) {
            zzb.zzb("Failure while processing active view data.", e2);
        }
        this.zzsF.zza(new zzc<zzeh>() {
            /* renamed from: zzd */
            public void zze(zzeh zzeh) {
                zzba.this.zzsG = true;
                zzba.this.zzb(zzeh);
                zzba.this.zzcd();
                zzba.this.zzh(false);
            }
        }, new zza() {
            public void run() {
                zzba.this.destroy();
            }
        });
        zzb.zzaI("Tracking ad unit: " + this.zzrZ.zzcu());
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        synchronized (this.zzpV) {
            super.destroy();
            this.zzsF.zza(new zzc<zzeh>() {
                /* renamed from: zzd */
                public void zze(zzeh zzeh) {
                    zzba.this.zzc(zzeh);
                }
            }, new zzji.zzb());
            this.zzsF.release();
        }
    }

    /* access modifiers changed from: protected */
    public void zzb(final JSONObject jSONObject) {
        this.zzsF.zza(new zzc<zzeh>() {
            /* renamed from: zzd */
            public void zze(zzeh zzeh) {
                zzeh.zza("AFMA_updateActiveView", jSONObject);
            }
        }, new zzji.zzb());
    }

    /* access modifiers changed from: protected */
    public boolean zzcl() {
        return this.zzsG;
    }
}
