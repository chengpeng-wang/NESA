package com.google.android.gms.ads.internal.client;

import android.content.Context;
import com.google.android.gms.ads.internal.reward.client.zzi;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.internal.zzeg;

public class zzaa {
    private static final Object zzpm = new Object();
    private static zzaa zztt;
    private zzv zztu;
    private RewardedVideoAd zztv;

    private zzaa() {
    }

    public static zzaa zzcU() {
        zzaa zzaa;
        synchronized (zzpm) {
            if (zztt == null) {
                zztt = new zzaa();
            }
            zzaa = zztt;
        }
        return zzaa;
    }

    public RewardedVideoAd getRewardedVideoAdInstance(Context context) {
        RewardedVideoAd rewardedVideoAd;
        synchronized (zzpm) {
            if (this.zztv != null) {
                rewardedVideoAd = this.zztv;
            } else {
                this.zztv = new zzi(context, zzk.zzcJ().zza(context, new zzeg()));
                rewardedVideoAd = this.zztv;
            }
        }
        return rewardedVideoAd;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    public void zza(android.content.Context r4, java.lang.String r5, com.google.android.gms.ads.internal.client.zzab r6) {
        /*
        r3 = this;
        r1 = zzpm;
        monitor-enter(r1);
        r0 = r3.zztu;	 Catch:{ all -> 0x0013 }
        if (r0 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r1);	 Catch:{ all -> 0x0013 }
    L_0x0008:
        return;
    L_0x0009:
        if (r4 != 0) goto L_0x0016;
    L_0x000b:
        r0 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0013 }
        r2 = "Context cannot be null.";
        r0.<init>(r2);	 Catch:{ all -> 0x0013 }
        throw r0;	 Catch:{ all -> 0x0013 }
    L_0x0013:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0013 }
        throw r0;
    L_0x0016:
        r0 = android.text.TextUtils.isEmpty(r5);	 Catch:{ all -> 0x0013 }
        if (r0 == 0) goto L_0x0024;
    L_0x001c:
        r0 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0013 }
        r2 = "applicationCode cannot be empty.";
        r0.<init>(r2);	 Catch:{ all -> 0x0013 }
        throw r0;	 Catch:{ all -> 0x0013 }
    L_0x0024:
        r0 = com.google.android.gms.ads.internal.client.zzk.zzcH();	 Catch:{ RemoteException -> 0x003e }
        r0 = r0.zzt(r4);	 Catch:{ RemoteException -> 0x003e }
        r3.zztu = r0;	 Catch:{ RemoteException -> 0x003e }
        r2 = r3.zztu;	 Catch:{ RemoteException -> 0x003e }
        if (r6 != 0) goto L_0x0038;
    L_0x0032:
        r0 = 0;
    L_0x0033:
        r2.zza(r5, r0);	 Catch:{ RemoteException -> 0x003e }
    L_0x0036:
        monitor-exit(r1);	 Catch:{ all -> 0x0013 }
        goto L_0x0008;
    L_0x0038:
        r0 = new com.google.android.gms.ads.internal.client.MobileAdsSettingsParcel;	 Catch:{ RemoteException -> 0x003e }
        r0.m1270init(r6);	 Catch:{ RemoteException -> 0x003e }
        goto L_0x0033;
    L_0x003e:
        r0 = move-exception;
        r0 = "Fail to initialize mobile ads setting manager";
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r0);	 Catch:{ all -> 0x0013 }
        goto L_0x0036;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.client.zzaa.zza(android.content.Context, java.lang.String, com.google.android.gms.ads.internal.client.zzab):void");
    }
}
