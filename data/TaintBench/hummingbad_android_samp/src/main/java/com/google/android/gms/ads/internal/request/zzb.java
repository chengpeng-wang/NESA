package com.google.android.gms.ads.internal.request;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.zzp;
import com.google.android.gms.internal.zzan;
import com.google.android.gms.internal.zzbk;
import com.google.android.gms.internal.zzea;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhq;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;
import org.json.JSONException;

@zzgk
public class zzb extends zzhq implements com.google.android.gms.ads.internal.request.zzc.zza {
    private final Context mContext;
    private final Object zzCE = new Object();
    AdResponseParcel zzCG;
    private final com.google.android.gms.ads.internal.request.zza.zza zzDp;
    private final com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zzDq;
    zzhq zzDr;
    Object zzpc = new Object();
    private final zzan zzwh;
    zzea zzye;

    @zzgk
    static final class zza extends Exception {
        private final int zzCI;

        public zza(String str, int i) {
            super(str);
            this.zzCI = i;
        }

        public int getErrorCode() {
            return this.zzCI;
        }
    }

    public zzb(Context context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel.zza zza, zzan zzan, com.google.android.gms.ads.internal.request.zza.zza zza2) {
        this.zzDp = zza2;
        this.mContext = context;
        this.zzDq = zza;
        this.zzwh = zzan;
    }

    public void onStop() {
        synchronized (this.zzCE) {
            if (this.zzDr != null) {
                this.zzDr.cancel();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public zzhq zzb(AdRequestInfoParcel adRequestInfoParcel) {
        return zzc.zza(this.mContext, adRequestInfoParcel, this);
    }

    public void zzb(AdResponseParcel adResponseParcel) {
        synchronized (this.zzpc) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Received ad response.");
            this.zzCG = adResponseParcel;
            this.zzpc.notify();
        }
    }

    /* access modifiers changed from: protected */
    public AdSizeParcel zzc(AdRequestInfoParcel adRequestInfoParcel) throws zza {
        if (this.zzCG.zzEa == null) {
            throw new zza("The ad response must specify one of the supported ad sizes.", 0);
        }
        String[] split = this.zzCG.zzEa.split("x");
        if (split.length != 2) {
            throw new zza("Invalid ad size format from the ad response: " + this.zzCG.zzEa, 0);
        }
        try {
            int parseInt = Integer.parseInt(split[0]);
            int parseInt2 = Integer.parseInt(split[1]);
            for (AdSizeParcel adSizeParcel : adRequestInfoParcel.zzqf.zzsI) {
                float f = this.mContext.getResources().getDisplayMetrics().density;
                int i = adSizeParcel.width == -1 ? (int) (((float) adSizeParcel.widthPixels) / f) : adSizeParcel.width;
                int i2 = adSizeParcel.height == -2 ? (int) (((float) adSizeParcel.heightPixels) / f) : adSizeParcel.height;
                if (parseInt == i && parseInt2 == i2) {
                    return new AdSizeParcel(adSizeParcel, adRequestInfoParcel.zzqf.zzsI);
                }
            }
            throw new zza("The ad size from the ad response was not one of the requested sizes: " + this.zzCG.zzEa, 0);
        } catch (NumberFormatException e) {
            throw new zza("Invalid ad size number from the ad response: " + this.zzCG.zzEa, 0);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x007e A:{SYNTHETIC, Splitter:B:31:0x007e} */
    public void zzdG() {
        /*
        r13 = this;
        r9 = 0;
        r12 = r13.zzpc;
        monitor-enter(r12);
        r0 = "AdLoaderBackgroundTask started.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaC(r0);	 Catch:{ all -> 0x00c7 }
        r0 = r13.zzwh;	 Catch:{ all -> 0x00c7 }
        r0 = r0.zzab();	 Catch:{ all -> 0x00c7 }
        r1 = r13.mContext;	 Catch:{ all -> 0x00c7 }
        r2 = r0.zzb(r1);	 Catch:{ all -> 0x00c7 }
        r0 = r13.mContext;	 Catch:{ all -> 0x00c7 }
        r0 = com.google.android.gms.ads.internal.zzm.zzq(r0);	 Catch:{ all -> 0x00c7 }
        r3 = r0.getClientId();	 Catch:{ all -> 0x00c7 }
        r0 = com.google.android.gms.ads.internal.zzp.zzbB();	 Catch:{ all -> 0x00c7 }
        r4 = r0.elapsedRealtime();	 Catch:{ all -> 0x00c7 }
        r0 = new com.google.android.gms.ads.internal.request.AdRequestInfoParcel;	 Catch:{ all -> 0x00c7 }
        r1 = r13.zzDq;	 Catch:{ all -> 0x00c7 }
        r0.m1358init(r1, r2, r3, r4);	 Catch:{ all -> 0x00c7 }
        r6 = -2;
        r2 = -1;
        r1 = r13.zzb(r0);	 Catch:{ zza -> 0x004a }
        r7 = r13.zzCE;	 Catch:{ zza -> 0x004a }
        monitor-enter(r7);	 Catch:{ zza -> 0x004a }
        r13.zzDr = r1;	 Catch:{ all -> 0x0047 }
        r1 = r13.zzDr;	 Catch:{ all -> 0x0047 }
        if (r1 != 0) goto L_0x009c;
    L_0x003e:
        r1 = new com.google.android.gms.ads.internal.request.zzb$zza;	 Catch:{ all -> 0x0047 }
        r4 = "Could not start the ad request service.";
        r5 = 0;
        r1.m1377init(r4, r5);	 Catch:{ all -> 0x0047 }
        throw r1;	 Catch:{ all -> 0x0047 }
    L_0x0047:
        r1 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0047 }
        throw r1;	 Catch:{ zza -> 0x004a }
    L_0x004a:
        r1 = move-exception;
        r4 = r9;
    L_0x004c:
        r6 = r1.getErrorCode();	 Catch:{ all -> 0x00c7 }
        r5 = 3;
        if (r6 == r5) goto L_0x0056;
    L_0x0053:
        r5 = -1;
        if (r6 != r5) goto L_0x00bf;
    L_0x0056:
        r1 = r1.getMessage();	 Catch:{ all -> 0x00c7 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaD(r1);	 Catch:{ all -> 0x00c7 }
    L_0x005d:
        r1 = r13.zzCG;	 Catch:{ all -> 0x00c7 }
        if (r1 != 0) goto L_0x00ca;
    L_0x0061:
        r1 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x00c7 }
        r1.m1360init(r6);	 Catch:{ all -> 0x00c7 }
        r13.zzCG = r1;	 Catch:{ all -> 0x00c7 }
    L_0x0068:
        r1 = com.google.android.gms.internal.zzhu.zzHK;	 Catch:{ all -> 0x00c7 }
        r5 = new com.google.android.gms.ads.internal.request.zzb$1;	 Catch:{ all -> 0x00c7 }
        r5.m1376init();	 Catch:{ all -> 0x00c7 }
        r1.post(r5);	 Catch:{ all -> 0x00c7 }
        r7 = r2;
        r5 = r4;
    L_0x0074:
        r1 = r13.zzCG;	 Catch:{ all -> 0x00c7 }
        r1 = r1.zzEf;	 Catch:{ all -> 0x00c7 }
        r1 = android.text.TextUtils.isEmpty(r1);	 Catch:{ all -> 0x00c7 }
        if (r1 != 0) goto L_0x00dc;
    L_0x007e:
        r11 = new org.json.JSONObject;	 Catch:{ Exception -> 0x00d6 }
        r1 = r13.zzCG;	 Catch:{ Exception -> 0x00d6 }
        r1 = r1.zzEf;	 Catch:{ Exception -> 0x00d6 }
        r11.<init>(r1);	 Catch:{ Exception -> 0x00d6 }
    L_0x0087:
        r1 = new com.google.android.gms.internal.zzhj$zza;	 Catch:{ all -> 0x00c7 }
        r3 = r13.zzCG;	 Catch:{ all -> 0x00c7 }
        r4 = r13.zzye;	 Catch:{ all -> 0x00c7 }
        r2 = r13.zzCG;	 Catch:{ all -> 0x00c7 }
        r9 = r2.zzEb;	 Catch:{ all -> 0x00c7 }
        r2 = r0;
        r1.m1585init(r2, r3, r4, r5, r6, r7, r9, r11);	 Catch:{ all -> 0x00c7 }
        r0 = r13.zzDp;	 Catch:{ all -> 0x00c7 }
        r0.zza(r1);	 Catch:{ all -> 0x00c7 }
        monitor-exit(r12);	 Catch:{ all -> 0x00c7 }
        return;
    L_0x009c:
        monitor-exit(r7);	 Catch:{ all -> 0x0047 }
        r13.zzi(r4);	 Catch:{ zza -> 0x004a }
        r1 = com.google.android.gms.ads.internal.zzp.zzbB();	 Catch:{ zza -> 0x004a }
        r2 = r1.elapsedRealtime();	 Catch:{ zza -> 0x004a }
        r13.zzfw();	 Catch:{ zza -> 0x004a }
        r1 = r0.zzqf;	 Catch:{ zza -> 0x004a }
        r1 = r1.zzsI;	 Catch:{ zza -> 0x004a }
        if (r1 == 0) goto L_0x00e1;
    L_0x00b1:
        r4 = r13.zzc(r0);	 Catch:{ zza -> 0x004a }
    L_0x00b5:
        r1 = r13.zzCG;	 Catch:{ zza -> 0x00de }
        r1 = r1.zzEh;	 Catch:{ zza -> 0x00de }
        r13.zzw(r1);	 Catch:{ zza -> 0x00de }
        r7 = r2;
        r5 = r4;
        goto L_0x0074;
    L_0x00bf:
        r1 = r1.getMessage();	 Catch:{ all -> 0x00c7 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r1);	 Catch:{ all -> 0x00c7 }
        goto L_0x005d;
    L_0x00c7:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x00c7 }
        throw r0;
    L_0x00ca:
        r1 = new com.google.android.gms.ads.internal.request.AdResponseParcel;	 Catch:{ all -> 0x00c7 }
        r5 = r13.zzCG;	 Catch:{ all -> 0x00c7 }
        r7 = r5.zzyA;	 Catch:{ all -> 0x00c7 }
        r1.m1361init(r6, r7);	 Catch:{ all -> 0x00c7 }
        r13.zzCG = r1;	 Catch:{ all -> 0x00c7 }
        goto L_0x0068;
    L_0x00d6:
        r1 = move-exception;
        r2 = "Error parsing the JSON for Active View.";
        com.google.android.gms.ads.internal.util.client.zzb.zzb(r2, r1);	 Catch:{ all -> 0x00c7 }
    L_0x00dc:
        r11 = r9;
        goto L_0x0087;
    L_0x00de:
        r1 = move-exception;
        goto L_0x004c;
    L_0x00e1:
        r4 = r9;
        goto L_0x00b5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.request.zzb.zzdG():void");
    }

    /* access modifiers changed from: protected */
    public boolean zze(long j) throws zza {
        long elapsedRealtime = BuglyBroadcastRecevier.UPLOADLIMITED - (zzp.zzbB().elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.zzpc.wait(elapsedRealtime);
            return true;
        } catch (InterruptedException e) {
            throw new zza("Ad request cancelled.", -1);
        }
    }

    /* access modifiers changed from: protected */
    public void zzfw() throws zza {
        if (this.zzCG.errorCode != -3) {
            if (TextUtils.isEmpty(this.zzCG.body)) {
                throw new zza("No fill from ad server.", 3);
            }
            zzp.zzbA().zza(this.mContext, this.zzCG.zzDG);
            if (this.zzCG.zzDX) {
                try {
                    this.zzye = new zzea(this.zzCG.body);
                } catch (JSONException e) {
                    throw new zza("Could not parse mediation config: " + this.zzCG.body, 0);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void zzi(long j) throws zza {
        while (zze(j)) {
            if (this.zzCG != null) {
                synchronized (this.zzCE) {
                    this.zzDr = null;
                }
                if (this.zzCG.errorCode != -2 && this.zzCG.errorCode != -3) {
                    throw new zza("There was a problem getting an ad response. ErrorCode: " + this.zzCG.errorCode, this.zzCG.errorCode);
                }
                return;
            }
        }
        throw new zza("Timed out waiting for ad response.", 2);
    }

    /* access modifiers changed from: protected */
    public void zzw(boolean z) {
        zzp.zzbA().zzA(z);
        zzbk zzF = zzp.zzbA().zzF(this.mContext);
        if (zzF != null && !zzF.isAlive()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("start fetching content...");
            zzF.zzct();
        }
    }
}
