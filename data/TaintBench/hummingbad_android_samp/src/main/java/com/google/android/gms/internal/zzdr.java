package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.client.zzk;
import com.google.android.gms.ads.internal.util.client.zza;
import com.google.android.gms.common.api.Releasable;
import java.util.HashMap;

@zzgk
public abstract class zzdr implements Releasable {
    protected zzip zzoL;

    public zzdr(zzip zzip) {
        this.zzoL = zzip;
    }

    public abstract void abort();

    public void release() {
    }

    public abstract boolean zzZ(String str);

    /* access modifiers changed from: protected */
    public void zza(final String str, final String str2, final int i) {
        zza.zzIy.post(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put("event", "precacheComplete");
                hashMap.put("src", str);
                hashMap.put("cachedSrc", str2);
                hashMap.put("totalBytes", Integer.toString(i));
                zzdr.this.zzoL.zzc("onPrecacheEvent", hashMap);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void zza(String str, String str2, int i, int i2, boolean z) {
        final String str3 = str;
        final String str4 = str2;
        final int i3 = i;
        final int i4 = i2;
        final boolean z2 = z;
        zza.zzIy.post(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put("event", "precacheProgress");
                hashMap.put("src", str3);
                hashMap.put("cachedSrc", str4);
                hashMap.put("bytesLoaded", Integer.toString(i3));
                hashMap.put("totalBytes", Integer.toString(i4));
                hashMap.put("cacheReady", z2 ? "1" : "0");
                zzdr.this.zzoL.zzc("onPrecacheEvent", hashMap);
            }
        });
    }

    /* access modifiers changed from: protected */
    public String zzaa(String str) {
        return zzk.zzcE().zzaB(str);
    }

    /* access modifiers changed from: protected */
    public void zzf(final String str, final String str2) {
        zza.zzIy.post(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put("event", "precacheCanceled");
                hashMap.put("src", str);
                if (str2 != null) {
                    hashMap.put("cachedSrc", str2);
                }
                zzdr.this.zzoL.zzc("onPrecacheEvent", hashMap);
            }
        });
    }
}
