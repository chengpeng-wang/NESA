package com.google.android.gms.ads.internal;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.ads.internal.client.MobileAdsSettingsParcel;
import com.google.android.gms.ads.internal.client.zzv.zza;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.purchase.InAppPurchaseActivity;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzoj;
import com.google.android.gms.internal.zzoq;
import com.google.android.gms.internal.zzqf;
import com.google.android.gms.internal.zzqg;
import java.util.regex.Pattern;

@zzgk
public class zzm extends zza implements zzoj.zza, zzqg.zza {
    private static final Object zzpm = new Object();
    private static zzm zzpn;
    private final Context mContext;
    zzqf zzpo;
    String zzpp;
    String zzpq;
    private boolean zzpr = false;
    private boolean zzps;

    zzm(Context context) {
        this.mContext = context;
    }

    public static zzm zzq(Context context) {
        zzm zzm;
        synchronized (zzpm) {
            if (zzpn == null) {
                zzpn = new zzm(context.getApplicationContext());
            }
            zzm = zzpn;
        }
        return zzm;
    }

    public String getClientId() {
        String clientId;
        synchronized (zzpm) {
            if (this.zzps) {
                clientId = GoogleAnalytics.getInstance(this.mContext).getClientId();
            } else {
                clientId = null;
            }
        }
        return clientId;
    }

    public void zza(zzoq zzoq) {
    }

    public void zza(zzoq zzoq, Activity activity) {
        if (zzoq != null && activity != null) {
            if (activity instanceof AdActivity) {
                int zzk = zzp.zzbx().zzk(activity);
                if (zzk == 1) {
                    zzoq.zzam(true);
                    zzoq.setScreenName("Interstitial Ad");
                } else if (zzk == 2 || zzk == 3) {
                    zzoq.setScreenName("Expanded Ad");
                } else {
                    zzoq.setScreenName(null);
                }
            } else if (activity instanceof InAppPurchaseActivity) {
                zzoq.setScreenName(null);
            }
        }
    }

    public void zza(String str, MobileAdsSettingsParcel mobileAdsSettingsParcel) {
        synchronized (zzpm) {
            if (this.zzpr) {
                zzb.zzaE("Mobile ads is initialized already.");
            } else if (this.mContext == null) {
                zzb.zzaE("Fail to initialize mobile ads because context is null.");
            } else if (TextUtils.isEmpty(str)) {
                zzb.zzaE("Fail to initialize mobile ads because ApplicationCode is empty.");
            } else {
                this.zzpr = true;
                zzb(str, mobileAdsSettingsParcel);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void zzb(String str, MobileAdsSettingsParcel mobileAdsSettingsParcel) {
        if (mobileAdsSettingsParcel != null && mobileAdsSettingsParcel.zzty) {
            if (!zzp.zzbx().zza(this.mContext.getPackageManager(), this.mContext.getPackageName(), "android.permission.INTERNET")) {
                zzb.e("Missing permission android.permission.INTERNET");
            } else if (!zzp.zzbx().zza(this.mContext.getPackageManager(), this.mContext.getPackageName(), "android.permission.ACCESS_NETWORK_STATE")) {
                zzb.e("Missing permission android.permission.ACCESS_NETWORK_STATE");
            } else if (Pattern.matches("ca-app-[a-z0-9_-]+~[a-z0-9_-]+", str)) {
                this.zzps = true;
                this.zzpp = str;
                this.zzpq = mobileAdsSettingsParcel.zztz;
                zzqg zzaR = zzqg.zzaR(this.mContext);
                zzqf.zza zza = new zzqf.zza(this.zzpp);
                if (!TextUtils.isEmpty(this.zzpq)) {
                    zza.zzfh(this.zzpq);
                }
                zzaR.zza(zza.zzBm());
                zzaR.zza(this);
                zzoj.zzaJ(this.mContext).zza(this);
                zzaR.start();
            } else {
                throw new IllegalArgumentException("Please provide a valid application code");
            }
        }
    }

    public boolean zzbn() {
        boolean z;
        synchronized (zzpm) {
            z = this.zzps;
        }
        return z;
    }

    public void zzbo() {
        this.zzpo = zzqg.zzaR(this.mContext).zzBn();
    }

    public int zzbp() {
        int i = -1;
        synchronized (zzpm) {
            if (this.zzps) {
                zzoq zzxw = zzoj.zzaJ(this.mContext).zzxw();
                if (zzxw != null) {
                    i = zzxw.zzbp();
                }
            }
        }
        return i;
    }
}
