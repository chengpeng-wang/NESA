package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.ads.mediation.AdUrlAdapter;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdOptions.Builder;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzes.zza;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

@zzhb
public final class zzer implements zza {
    private final Context mContext;
    /* access modifiers changed from: private|final */
    public final String zzCd;
    private final long zzCe;
    private final zzeo zzCf;
    private final zzen zzCg;
    private final AdSizeParcel zzCh;
    /* access modifiers changed from: private */
    public zzey zzCi;
    /* access modifiers changed from: private */
    public int zzCj = -2;
    private zzfa zzCk;
    private final NativeAdOptionsParcel zzpP;
    private final List<String> zzpQ;
    private final VersionInfoParcel zzpT;
    /* access modifiers changed from: private|final */
    public final Object zzpV = new Object();
    private final zzex zzpn;
    private final AdRequestParcel zzqH;
    private final boolean zzsA;
    private final boolean zzuS;

    public zzer(Context context, String str, zzex zzex, zzeo zzeo, zzen zzen, AdRequestParcel adRequestParcel, AdSizeParcel adSizeParcel, VersionInfoParcel versionInfoParcel, boolean z, boolean z2, NativeAdOptionsParcel nativeAdOptionsParcel, List<String> list) {
        this.mContext = context;
        this.zzpn = zzex;
        this.zzCg = zzen;
        if ("com.google.ads.mediation.customevent.CustomEventAdapter".equals(str)) {
            this.zzCd = zzey();
        } else {
            this.zzCd = str;
        }
        this.zzCf = zzeo;
        this.zzCe = zzeo.zzBP != -1 ? zzeo.zzBP : 10000;
        this.zzqH = adRequestParcel;
        this.zzCh = adSizeParcel;
        this.zzpT = versionInfoParcel;
        this.zzsA = z;
        this.zzuS = z2;
        this.zzpP = nativeAdOptionsParcel;
        this.zzpQ = list;
    }

    private void zza(long j, long j2, long j3, long j4) {
        while (this.zzCj == -2) {
            zzb(j, j2, j3, j4);
        }
    }

    /* access modifiers changed from: private */
    public void zza(zzeq zzeq) {
        if ("com.google.ads.mediation.AdUrlAdapter".equals(this.zzCd)) {
            Bundle bundle = this.zzqH.zztM.getBundle(this.zzCd);
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("sdk_less_network_id", this.zzCg.zzBA);
            this.zzqH.zztM.putBundle(this.zzCd, bundle);
        }
        String zzac = zzac(this.zzCg.zzBG);
        try {
            if (this.zzpT.zzNa < 4100000) {
                if (this.zzCh.zzui) {
                    this.zzCi.zza(zze.zzC(this.mContext), this.zzqH, zzac, zzeq);
                } else {
                    this.zzCi.zza(zze.zzC(this.mContext), this.zzCh, this.zzqH, zzac, (zzez) zzeq);
                }
            } else if (this.zzsA) {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzqH, zzac, this.zzCg.zzBz, zzeq, this.zzpP, this.zzpQ);
            } else if (this.zzCh.zzui) {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzqH, zzac, this.zzCg.zzBz, (zzez) zzeq);
            } else if (!this.zzuS) {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzCh, this.zzqH, zzac, this.zzCg.zzBz, zzeq);
            } else if (this.zzCg.zzBJ != null) {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzqH, zzac, this.zzCg.zzBz, zzeq, new NativeAdOptionsParcel(zzad(this.zzCg.zzBN)), this.zzCg.zzBM);
            } else {
                this.zzCi.zza(zze.zzC(this.mContext), this.zzCh, this.zzqH, zzac, this.zzCg.zzBz, zzeq);
            }
        } catch (RemoteException e) {
            zzb.zzd("Could not request ad from mediation adapter.", e);
            zzr(5);
        }
    }

    private String zzac(String str) {
        if (str == null || !zzeB() || zzs(2)) {
            return str;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.remove("cpm_floor_cents");
            return jSONObject.toString();
        } catch (JSONException e) {
            zzb.zzaK("Could not remove field. Returning the original value");
            return str;
        }
    }

    private static NativeAdOptions zzad(String str) {
        Builder builder = new Builder();
        if (str == null) {
            return builder.build();
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            builder.setRequestMultipleImages(jSONObject.optBoolean("multiple_images", false));
            builder.setReturnUrlsForImageAssets(jSONObject.optBoolean("only_urls", false));
            builder.setImageOrientation(zzae(jSONObject.optString("native_image_orientation", "any")));
        } catch (JSONException e) {
            zzb.zzd("Exception occurred when creating native ad options", e);
        }
        return builder.build();
    }

    private static int zzae(String str) {
        return "landscape".equals(str) ? 2 : "portrait".equals(str) ? 1 : 0;
    }

    private void zzb(long j, long j2, long j3, long j4) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j5 = j2 - (elapsedRealtime - j);
        elapsedRealtime = j4 - (elapsedRealtime - j3);
        if (j5 <= 0 || elapsedRealtime <= 0) {
            zzb.zzaJ("Timed out waiting for adapter.");
            this.zzCj = 3;
            return;
        }
        try {
            this.zzpV.wait(Math.min(j5, elapsedRealtime));
        } catch (InterruptedException e) {
            this.zzCj = -1;
        }
    }

    /* access modifiers changed from: private */
    public zzey zzeA() {
        zzb.zzaJ("Instantiating mediation adapter: " + this.zzCd);
        if (((Boolean) zzbt.zzwV.get()).booleanValue() && "com.google.ads.mediation.admob.AdMobAdapter".equals(this.zzCd)) {
            return new zzfe(new AdMobAdapter());
        }
        if (((Boolean) zzbt.zzwW.get()).booleanValue() && "com.google.ads.mediation.AdUrlAdapter".equals(this.zzCd)) {
            return new zzfe(new AdUrlAdapter());
        }
        try {
            return this.zzpn.zzaf(this.zzCd);
        } catch (RemoteException e) {
            zzb.zza("Could not instantiate mediation adapter: " + this.zzCd, e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public boolean zzeB() {
        return this.zzCf.zzBX != -1;
    }

    private int zzeC() {
        if (this.zzCg.zzBG == null) {
            return 0;
        }
        try {
            JSONObject jSONObject = new JSONObject(this.zzCg.zzBG);
            if ("com.google.ads.mediation.admob.AdMobAdapter".equals(this.zzCd)) {
                return jSONObject.optInt("cpm_cents", 0);
            }
            int optInt = zzs(2) ? jSONObject.optInt("cpm_floor_cents", 0) : 0;
            return optInt == 0 ? jSONObject.optInt("penalized_average_cpm_cents", 0) : optInt;
        } catch (JSONException e) {
            zzb.zzaK("Could not convert to json. Returning 0");
            return 0;
        }
    }

    private String zzey() {
        try {
            if (!TextUtils.isEmpty(this.zzCg.zzBD)) {
                return this.zzpn.zzag(this.zzCg.zzBD) ? "com.google.android.gms.ads.mediation.customevent.CustomEventAdapter" : "com.google.ads.mediation.customevent.CustomEventAdapter";
            }
        } catch (RemoteException e) {
            zzb.zzaK("Fail to determine the custom event's version, assuming the old one.");
        }
        return "com.google.ads.mediation.customevent.CustomEventAdapter";
    }

    private zzfa zzez() {
        if (this.zzCj != 0 || !zzeB()) {
            return null;
        }
        try {
            if (!(!zzs(4) || this.zzCk == null || this.zzCk.zzeD() == 0)) {
                return this.zzCk;
            }
        } catch (RemoteException e) {
            zzb.zzaK("Could not get cpm value from MediationResponseMetadata");
        }
        return zzt(zzeC());
    }

    /* access modifiers changed from: private */
    public boolean zzs(int i) {
        try {
            Bundle zzeI = this.zzsA ? this.zzCi.zzeI() : this.zzCh.zzui ? this.zzCi.getInterstitialAdapterInfo() : this.zzCi.zzeH();
            if (zzeI == null) {
                return false;
            }
            return (zzeI.getInt("capabilities", 0) & i) == i;
        } catch (RemoteException e) {
            zzb.zzaK("Could not get adapter info. Returning false");
            return false;
        }
    }

    private static zzfa zzt(final int i) {
        return new zzfa.zza() {
            public int zzeD() throws RemoteException {
                return i;
            }
        };
    }

    public void cancel() {
        synchronized (this.zzpV) {
            try {
                if (this.zzCi != null) {
                    this.zzCi.destroy();
                }
            } catch (RemoteException e) {
                zzb.zzd("Could not destroy mediation adapter.", e);
            }
            this.zzCj = -1;
            this.zzpV.notify();
        }
    }

    public zzes zza(long j, long j2) {
        zzes zzes;
        synchronized (this.zzpV) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            final zzeq zzeq = new zzeq();
            zzir.zzMc.post(new Runnable() {
                public void run() {
                    synchronized (zzer.this.zzpV) {
                        if (zzer.this.zzCj != -2) {
                            return;
                        }
                        zzer.this.zzCi = zzer.this.zzeA();
                        if (zzer.this.zzCi == null) {
                            zzer.this.zzr(4);
                        } else if (!zzer.this.zzeB() || zzer.this.zzs(1)) {
                            zzeq.zza(zzer.this);
                            zzer.this.zza(zzeq);
                        } else {
                            zzb.zzaK("Ignoring adapter " + zzer.this.zzCd + " as delayed" + " impression is not supported");
                            zzer.this.zzr(2);
                        }
                    }
                }
            });
            zza(elapsedRealtime, this.zzCe, j, j2);
            zzes = new zzes(this.zzCg, this.zzCi, this.zzCd, zzeq, this.zzCj, zzez());
        }
        return zzes;
    }

    public void zza(int i, zzfa zzfa) {
        synchronized (this.zzpV) {
            this.zzCj = i;
            this.zzCk = zzfa;
            this.zzpV.notify();
        }
    }

    public void zzr(int i) {
        synchronized (this.zzpV) {
            this.zzCj = i;
            this.zzpV.notify();
        }
    }
}
