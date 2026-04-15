package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzbt;
import com.google.android.gms.internal.zzeo;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzim;
import com.google.android.gms.internal.zziq;
import com.google.android.gms.internal.zzir;
import com.google.android.gms.internal.zzit;
import com.google.android.gms.internal.zzji;
import com.google.android.gms.internal.zzjj;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzb extends zzim implements com.google.android.gms.ads.internal.request.zzc.zza {
    private final Context mContext;
    zzeo zzCf;
    private AdRequestInfoParcel zzCu;
    AdResponseParcel zzGe;
    /* access modifiers changed from: private */
    public Runnable zzGf;
    /* access modifiers changed from: private|final */
    public final Object zzGg = new Object();
    private final com.google.android.gms.ads.internal.request.zza.zza zzHg;
    /* access modifiers changed from: private|final */
    public final com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zzHh;
    zzit zzHi;
    private final zzan zzyt;

    @zzhb
    static final class zza extends Exception {
        private final int zzGu;

        public zza(String str, int i) {
            super(str);
            this.zzGu = i;
        }

        public int getErrorCode() {
            return this.zzGu;
        }
    }

    public zzb(Context context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zza, zzan zzan, com.google.android.gms.ads.internal.request.zza.zza zza2) {
        this.zzHg = zza2;
        this.mContext = context;
        this.zzHh = zza;
        this.zzyt = zzan;
    }

    /* access modifiers changed from: private */
    public void zzc(int i, String str) {
        if (i == 3 || i == -1) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaJ(str);
        } else {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK(str);
        }
        if (this.zzGe == null) {
            this.zzGe = new AdResponseParcel(i);
        } else {
            this.zzGe = new AdResponseParcel(i, this.zzGe.zzBU);
        }
        this.zzHg.zza(new com.google.android.gms.internal.zzif.zza(this.zzCu != null ? this.zzCu : new AdRequestInfoParcel(this.zzHh, null, -1), this.zzGe, this.zzCf, null, i, -1, this.zzGe.zzHX, null));
    }

    public void onStop() {
        synchronized (this.zzGg) {
            if (this.zzHi != null) {
                this.zzHi.cancel();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public zzit zza(VersionInfoParcel versionInfoParcel, zzji<AdRequestInfoParcel> zzji) {
        return zzc.zza(this.mContext, versionInfoParcel, zzji, this);
    }

    /* access modifiers changed from: protected */
    public AdSizeParcel zzb(AdRequestInfoParcel adRequestInfoParcel) throws zza {
        if (this.zzGe.zzHW == null) {
            throw new zza("The ad response must specify one of the supported ad sizes.", 0);
        }
        String[] split = this.zzGe.zzHW.split("x");
        if (split.length != 2) {
            throw new zza("Invalid ad size format from the ad response: " + this.zzGe.zzHW, 0);
        }
        try {
            int parseInt = Integer.parseInt(split[0]);
            int parseInt2 = Integer.parseInt(split[1]);
            for (AdSizeParcel adSizeParcel : adRequestInfoParcel.zzrp.zzuj) {
                float f = this.mContext.getResources().getDisplayMetrics().density;
                int i = adSizeParcel.width == -1 ? (int) (((float) adSizeParcel.widthPixels) / f) : adSizeParcel.width;
                int i2 = adSizeParcel.height == -2 ? (int) (((float) adSizeParcel.heightPixels) / f) : adSizeParcel.height;
                if (parseInt == i && parseInt2 == i2) {
                    return new AdSizeParcel(adSizeParcel, adRequestInfoParcel.zzrp.zzuj);
                }
            }
            throw new zza("The ad size from the ad response was not one of the requested sizes: " + this.zzGe.zzHW, 0);
        } catch (NumberFormatException e) {
            throw new zza("Invalid ad size number from the ad response: " + this.zzGe.zzHW, 0);
        }
    }

    public void zzb(@NonNull AdResponseParcel adResponseParcel) {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("Received ad response.");
        this.zzGe = adResponseParcel;
        long elapsedRealtime = zzr.zzbG().elapsedRealtime();
        synchronized (this.zzGg) {
            this.zzHi = null;
        }
        try {
            if (this.zzGe.errorCode == -2 || this.zzGe.errorCode == -3) {
                JSONObject jSONObject;
                zzgq();
                AdSizeParcel zzb = this.zzCu.zzrp.zzuj != null ? zzb(this.zzCu) : null;
                zzr.zzbF().zzB(this.zzGe.zzId);
                if (!TextUtils.isEmpty(this.zzGe.zzIb)) {
                    try {
                        jSONObject = new JSONObject(this.zzGe.zzIb);
                    } catch (Exception e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzb("Error parsing the JSON for Active View.", e);
                    }
                    this.zzHg.zza(new com.google.android.gms.internal.zzif.zza(this.zzCu, this.zzGe, this.zzCf, zzb, -2, elapsedRealtime, this.zzGe.zzHX, jSONObject));
                    zzir.zzMc.removeCallbacks(this.zzGf);
                    return;
                }
                jSONObject = null;
                this.zzHg.zza(new com.google.android.gms.internal.zzif.zza(this.zzCu, this.zzGe, this.zzCf, zzb, -2, elapsedRealtime, this.zzGe.zzHX, jSONObject));
                zzir.zzMc.removeCallbacks(this.zzGf);
                return;
            }
            throw new zza("There was a problem getting an ad response. ErrorCode: " + this.zzGe.errorCode, this.zzGe.errorCode);
        } catch (zza e2) {
            zzc(e2.getErrorCode(), e2.getMessage());
            zzir.zzMc.removeCallbacks(this.zzGf);
        }
    }

    public void zzbr() {
        com.google.android.gms.ads.internal.util.client.zzb.zzaI("AdLoaderBackgroundTask started.");
        this.zzGf = new Runnable() {
            public void run() {
                synchronized (zzb.this.zzGg) {
                    if (zzb.this.zzHi == null) {
                        return;
                    }
                    zzb.this.onStop();
                    zzb.this.zzc(2, "Timed out waiting for ad response.");
                }
            }
        };
        zzir.zzMc.postDelayed(this.zzGf, ((Long) zzbt.zzwX.get()).longValue());
        final zzjj zzjj = new zzjj();
        long elapsedRealtime = zzr.zzbG().elapsedRealtime();
        zziq.zza(new Runnable() {
            public void run() {
                synchronized (zzb.this.zzGg) {
                    zzb.this.zzHi = zzb.this.zza(zzb.this.zzHh.zzrl, zzjj);
                    if (zzb.this.zzHi == null) {
                        zzb.this.zzc(0, "Could not start the ad request service.");
                        zzir.zzMc.removeCallbacks(zzb.this.zzGf);
                    }
                }
            }
        });
        this.zzCu = new AdRequestInfoParcel(this.zzHh, this.zzyt.zzab().zzb(this.mContext), elapsedRealtime);
        zzjj.zzh(this.zzCu);
    }

    /* access modifiers changed from: protected */
    public void zzgq() throws zza {
        if (this.zzGe.errorCode != -3) {
            if (TextUtils.isEmpty(this.zzGe.body)) {
                throw new zza("No fill from ad server.", 3);
            }
            zzr.zzbF().zza(this.mContext, this.zzGe.zzHB);
            if (this.zzGe.zzHT) {
                try {
                    this.zzCf = new zzeo(this.zzGe.body);
                } catch (JSONException e) {
                    throw new zza("Could not parse mediation config: " + this.zzGe.body, 0);
                }
            }
        }
    }
}
