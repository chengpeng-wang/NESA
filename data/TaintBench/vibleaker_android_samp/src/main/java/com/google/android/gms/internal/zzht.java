package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.ads.mediation.AbstractAdViewAdapter;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.reward.client.RewardedVideoAdRequestParcel;
import com.google.android.gms.ads.internal.reward.mediation.client.RewardItemParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.zzb;
import com.google.android.gms.ads.internal.zzd;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzif.zza;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public class zzht extends zzb implements zzhw {
    private static final zzew zzKv = new zzew();
    private final Map<String, zzia> zzKw = new HashMap();
    private boolean zzKx;

    public zzht(Context context, zzd zzd, AdSizeParcel adSizeParcel, zzex zzex, VersionInfoParcel versionInfoParcel) {
        super(context, adSizeParcel, null, zzex, versionInfoParcel, zzd);
    }

    private zza zzc(zza zza) {
        zzin.v("Creating mediation ad response for non-mediated rewarded ad.");
        try {
            String jSONObject = zzhe.zzc(zza.zzLe).toString();
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(AbstractAdViewAdapter.AD_UNIT_ID_PARAMETER, zza.zzLd.zzrj);
            zzen zzen = new zzen(jSONObject, null, Arrays.asList(new String[]{"com.google.ads.mediation.admob.AdMobAdapter"}), null, null, Collections.emptyList(), Collections.emptyList(), jSONObject2.toString(), null, Collections.emptyList(), Collections.emptyList(), null, null, null, null, null);
            return new zza(zza.zzLd, zza.zzLe, new zzeo(Arrays.asList(new zzen[]{zzen}), -1, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), "", -1, 0, 1, null, 0, -1, -1), zza.zzrp, zza.errorCode, zza.zzKY, zza.zzKZ, zza.zzKT);
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Unable to generate ad state for non-mediated rewarded video.", e);
            return zzd(zza);
        }
    }

    private zza zzd(zza zza) {
        return new zza(zza.zzLd, zza.zzLe, null, zza.zzrp, 0, zza.zzKY, zza.zzKZ, zza.zzKT);
    }

    public void destroy() {
        zzx.zzcD("destroy must be called on the main UI thread.");
        for (String str : this.zzKw.keySet()) {
            try {
                zzia zzia = (zzia) this.zzKw.get(str);
                if (!(zzia == null || zzia.zzgP() == null)) {
                    zzia.zzgP().destroy();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Fail to destroy adapter: " + str);
            }
        }
    }

    public boolean isLoaded() {
        zzx.zzcD("isLoaded must be called on the main UI thread.");
        return this.zzpj.zzrn == null && this.zzpj.zzro == null && this.zzpj.zzrq != null && !this.zzKx;
    }

    public void onRewardedVideoAdClosed() {
        zzaQ();
    }

    public void onRewardedVideoAdLeftApplication() {
        zzaR();
    }

    public void onRewardedVideoAdOpened() {
        zza(this.zzpj.zzrq, false);
        zzaS();
    }

    public void onRewardedVideoStarted() {
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzCp == null)) {
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq, this.zzpj.zzrj, false, this.zzpj.zzrq.zzCp.zzBH);
        }
        zzaU();
    }

    public void pause() {
        zzx.zzcD("pause must be called on the main UI thread.");
        for (String str : this.zzKw.keySet()) {
            try {
                zzia zzia = (zzia) this.zzKw.get(str);
                if (!(zzia == null || zzia.zzgP() == null)) {
                    zzia.zzgP().pause();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Fail to pause adapter: " + str);
            }
        }
    }

    public void resume() {
        zzx.zzcD("resume must be called on the main UI thread.");
        for (String str : this.zzKw.keySet()) {
            try {
                zzia zzia = (zzia) this.zzKw.get(str);
                if (!(zzia == null || zzia.zzgP() == null)) {
                    zzia.zzgP().resume();
                }
            } catch (RemoteException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaK("Fail to resume adapter: " + str);
            }
        }
    }

    public void zza(RewardedVideoAdRequestParcel rewardedVideoAdRequestParcel) {
        zzx.zzcD("loadAd must be called on the main UI thread.");
        if (TextUtils.isEmpty(rewardedVideoAdRequestParcel.zzrj)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaK("Invalid ad unit id. Aborting.");
            return;
        }
        this.zzKx = false;
        this.zzpj.zzrj = rewardedVideoAdRequestParcel.zzrj;
        super.zzb(rewardedVideoAdRequestParcel.zzHt);
    }

    public void zza(final zza zza, zzcb zzcb) {
        if (zza.errorCode != -2) {
            zzir.zzMc.post(new Runnable() {
                public void run() {
                    zzht.this.zzb(new zzif(zza, null, null, null, null, null, null));
                }
            });
            return;
        }
        this.zzpj.zzrr = zza;
        if (zza.zzKV == null) {
            this.zzpj.zzrr = zzc(zza);
        }
        this.zzpj.zzrL = 0;
        this.zzpj.zzro = zzr.zzbB().zza(this.zzpj.context, this.zzpj.getUserId(), this.zzpj.zzrr, this);
    }

    public boolean zza(zzif zzif, zzif zzif2) {
        return true;
    }

    @Nullable
    public zzia zzaw(String str) {
        Throwable e;
        zzia zzia = (zzia) this.zzKw.get(str);
        if (zzia != null) {
            return zzia;
        }
        try {
            zzia zzia2 = new zzia(("com.google.ads.mediation.admob.AdMobAdapter".equals(str) ? zzKv : this.zzpn).zzaf(str), this);
            try {
                this.zzKw.put(str, zzia2);
                return zzia2;
            } catch (Exception e2) {
                Throwable th = e2;
                zzia = zzia2;
                e = th;
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to instantiate adapter " + str, e);
                return zzia;
            }
        } catch (Exception e3) {
            e = e3;
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Fail to instantiate adapter " + str, e);
            return zzia;
        }
    }

    public void zzc(@Nullable RewardItemParcel rewardItemParcel) {
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzCp == null)) {
            zzr.zzbP().zza(this.zzpj.context, this.zzpj.zzrl.afmaVersion, this.zzpj.zzrq, this.zzpj.zzrj, false, this.zzpj.zzrq.zzCp.zzBI);
        }
        if (!(this.zzpj.zzrq == null || this.zzpj.zzrq.zzKV == null || TextUtils.isEmpty(this.zzpj.zzrq.zzKV.zzBV))) {
            rewardItemParcel = new RewardItemParcel(this.zzpj.zzrq.zzKV.zzBV, this.zzpj.zzrq.zzKV.zzBW);
        }
        zza(rewardItemParcel);
    }

    public void zzgL() {
        zzx.zzcD("showAd must be called on the main UI thread.");
        if (isLoaded()) {
            this.zzKx = true;
            zzia zzaw = zzaw(this.zzpj.zzrq.zzCr);
            if (zzaw != null && zzaw.zzgP() != null) {
                try {
                    zzaw.zzgP().showVideo();
                    return;
                } catch (RemoteException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzd("Could not call showVideo.", e);
                    return;
                }
            }
            return;
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaK("The reward video has not loaded.");
    }

    public void zzgM() {
        onAdClicked();
    }
}
