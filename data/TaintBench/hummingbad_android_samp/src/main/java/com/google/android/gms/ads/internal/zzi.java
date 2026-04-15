package com.google.android.gms.ads.internal;

import android.content.Context;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.client.zzo.zza;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzct;
import com.google.android.gms.internal.zzcu;
import com.google.android.gms.internal.zzcv;
import com.google.android.gms.internal.zzcw;
import com.google.android.gms.internal.zzeh;
import com.google.android.gms.internal.zzgk;
import com.google.android.gms.internal.zzhu;
import com.google.android.gms.internal.zzlh;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@zzgk
public class zzi extends zza {
    private final Context mContext;
    /* access modifiers changed from: private|final */
    public final zzn zzoS;
    /* access modifiers changed from: private|final */
    public final zzct zzoT;
    /* access modifiers changed from: private|final */
    public final zzcu zzoU;
    /* access modifiers changed from: private|final */
    public final zzlh<String, zzcw> zzoV;
    /* access modifiers changed from: private|final */
    public final zzlh<String, zzcv> zzoW;
    /* access modifiers changed from: private|final */
    public final NativeAdOptionsParcel zzoX;
    private final List<String> zzoY;
    private final String zzoZ;
    private final zzeh zzow;
    private final VersionInfoParcel zzpa;
    /* access modifiers changed from: private */
    public WeakReference<zzn> zzpb;
    /* access modifiers changed from: private */
    public Object zzpc = new Object();

    zzi(Context context, String str, zzeh zzeh, VersionInfoParcel versionInfoParcel, zzn zzn, zzct zzct, zzcu zzcu, zzlh<String, zzcw> zzlh, zzlh<String, zzcv> zzlh2, NativeAdOptionsParcel nativeAdOptionsParcel) {
        this.mContext = context;
        this.zzoZ = str;
        this.zzow = zzeh;
        this.zzpa = versionInfoParcel;
        this.zzoS = zzn;
        this.zzoU = zzcu;
        this.zzoT = zzct;
        this.zzoV = zzlh;
        this.zzoW = zzlh2;
        this.zzoX = nativeAdOptionsParcel;
        this.zzoY = zzbi();
    }

    /* access modifiers changed from: private */
    public List<String> zzbi() {
        ArrayList arrayList = new ArrayList();
        if (this.zzoU != null) {
            arrayList.add("1");
        }
        if (this.zzoT != null) {
            arrayList.add("2");
        }
        if (this.zzoV.size() > 0) {
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
        r2 = r3.zzpc;
        monitor-enter(r2);
        r0 = r3.zzpb;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001a;
    L_0x0008:
        r0 = r3.zzpb;	 Catch:{ all -> 0x001d }
        r0 = r0.get();	 Catch:{ all -> 0x001d }
        r0 = (com.google.android.gms.ads.internal.zzn) r0;	 Catch:{ all -> 0x001d }
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
        r2 = r3.zzpc;
        monitor-enter(r2);
        r0 = r3.zzpb;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001a;
    L_0x0008:
        r0 = r3.zzpb;	 Catch:{ all -> 0x001d }
        r0 = r0.get();	 Catch:{ all -> 0x001d }
        r0 = (com.google.android.gms.ads.internal.zzn) r0;	 Catch:{ all -> 0x001d }
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
        zzhu.zzHK.post(runnable);
    }

    /* access modifiers changed from: protected */
    public zzn zzbj() {
        return new zzn(this.mContext, AdSizeParcel.zzs(this.mContext), this.zzoZ, this.zzow, this.zzpa);
    }

    public void zze(final AdRequestParcel adRequestParcel) {
        runOnUiThread(new Runnable() {
            public void run() {
                synchronized (zzi.this.zzpc) {
                    zzn zzbj = zzi.this.zzbj();
                    zzi.this.zzpb = new WeakReference(zzbj);
                    zzbj.zzb(zzi.this.zzoT);
                    zzbj.zzb(zzi.this.zzoU);
                    zzbj.zza(zzi.this.zzoV);
                    zzbj.zza(zzi.this.zzoS);
                    zzbj.zzb(zzi.this.zzoW);
                    zzbj.zza(zzi.this.zzbi());
                    zzbj.zzb(zzi.this.zzoX);
                    zzbj.zza(adRequestParcel);
                }
            }
        });
    }
}
