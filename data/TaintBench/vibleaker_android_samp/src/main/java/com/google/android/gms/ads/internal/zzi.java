package com.google.android.gms.ads.internal;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzq;
import com.google.android.gms.ads.internal.client.zzr.zza;
import com.google.android.gms.ads.internal.client.zzx;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzcr;
import com.google.android.gms.internal.zzcs;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzex;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzir;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@zzhb
public class zzi extends zza {
    private final Context mContext;
    /* access modifiers changed from: private|final */
    public final zzq zzpK;
    /* access modifiers changed from: private|final */
    public final zzcr zzpL;
    /* access modifiers changed from: private|final */
    public final zzcs zzpM;
    /* access modifiers changed from: private|final */
    public final SimpleArrayMap<String, zzcu> zzpN;
    /* access modifiers changed from: private|final */
    public final SimpleArrayMap<String, zzct> zzpO;
    /* access modifiers changed from: private|final */
    public final NativeAdOptionsParcel zzpP;
    private final List<String> zzpQ;
    /* access modifiers changed from: private|final */
    public final zzx zzpR;
    private final String zzpS;
    private final VersionInfoParcel zzpT;
    /* access modifiers changed from: private */
    public WeakReference<zzp> zzpU;
    /* access modifiers changed from: private|final */
    public final Object zzpV = new Object();
    private final zzd zzpm;
    private final zzex zzpn;

    zzi(Context context, String str, zzex zzex, VersionInfoParcel versionInfoParcel, zzq zzq, zzcr zzcr, zzcs zzcs, SimpleArrayMap<String, zzcu> simpleArrayMap, SimpleArrayMap<String, zzct> simpleArrayMap2, NativeAdOptionsParcel nativeAdOptionsParcel, zzx zzx, zzd zzd) {
        this.mContext = context;
        this.zzpS = str;
        this.zzpn = zzex;
        this.zzpT = versionInfoParcel;
        this.zzpK = zzq;
        this.zzpM = zzcs;
        this.zzpL = zzcr;
        this.zzpN = simpleArrayMap;
        this.zzpO = simpleArrayMap2;
        this.zzpP = nativeAdOptionsParcel;
        this.zzpQ = zzbl();
        this.zzpR = zzx;
        this.zzpm = zzd;
    }

    /* access modifiers changed from: private */
    public List<String> zzbl() {
        ArrayList arrayList = new ArrayList();
        if (this.zzpM != null) {
            arrayList.add("1");
        }
        if (this.zzpL != null) {
            arrayList.add("2");
        }
        if (this.zzpN.size() > 0) {
            arrayList.add("3");
        }
        return arrayList;
    }

    /* JADX WARNING: Missing block: B:15:?, code skipped:
            return r0;
     */
    public java.lang.String getMediationAdapterClassName() {
        /*
        r3 = this;
        r1 = 0;
        r2 = r3.zzpV;
        monitor-enter(r2);
        r0 = r3.zzpU;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001a;
    L_0x0008:
        r0 = r3.zzpU;	 Catch:{ all -> 0x001d }
        r0 = r0.get();	 Catch:{ all -> 0x001d }
        r0 = (com.google.android.gms.ads.internal.zzp) r0;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0018;
    L_0x0012:
        r0 = r0.getMediationAdapterClassName();	 Catch:{ all -> 0x001d }
    L_0x0016:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
    L_0x0017:
        return r0;
    L_0x0018:
        r0 = r1;
        goto L_0x0016;
    L_0x001a:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        r0 = r1;
        goto L_0x0017;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.zzi.getMediationAdapterClassName():java.lang.String");
    }

    /* JADX WARNING: Missing block: B:15:?, code skipped:
            return r0;
     */
    public boolean isLoading() {
        /*
        r3 = this;
        r1 = 0;
        r2 = r3.zzpV;
        monitor-enter(r2);
        r0 = r3.zzpU;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001a;
    L_0x0008:
        r0 = r3.zzpU;	 Catch:{ all -> 0x001d }
        r0 = r0.get();	 Catch:{ all -> 0x001d }
        r0 = (com.google.android.gms.ads.internal.zzp) r0;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0018;
    L_0x0012:
        r0 = r0.isLoading();	 Catch:{ all -> 0x001d }
    L_0x0016:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
    L_0x0017:
        return r0;
    L_0x0018:
        r0 = r1;
        goto L_0x0016;
    L_0x001a:
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        r0 = r1;
        goto L_0x0017;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.ads.internal.zzi.isLoading():boolean");
    }

    /* access modifiers changed from: protected */
    public void runOnUiThread(Runnable runnable) {
        zzir.zzMc.post(runnable);
    }

    /* access modifiers changed from: protected */
    public zzp zzbm() {
        return new zzp(this.mContext, this.zzpm, AdSizeParcel.zzt(this.mContext), this.zzpS, this.zzpn, this.zzpT);
    }

    public void zzf(final AdRequestParcel adRequestParcel) {
        runOnUiThread(new Runnable() {
            public void run() {
                synchronized (zzi.this.zzpV) {
                    zzp zzbm = zzi.this.zzbm();
                    zzi.this.zzpU = new WeakReference(zzbm);
                    zzbm.zzb(zzi.this.zzpL);
                    zzbm.zzb(zzi.this.zzpM);
                    zzbm.zza(zzi.this.zzpN);
                    zzbm.zza(zzi.this.zzpK);
                    zzbm.zzb(zzi.this.zzpO);
                    zzbm.zza(zzi.this.zzbl());
                    zzbm.zzb(zzi.this.zzpP);
                    zzbm.zza(zzi.this.zzpR);
                    zzbm.zzb(adRequestParcel);
                }
            }
        });
    }
}
