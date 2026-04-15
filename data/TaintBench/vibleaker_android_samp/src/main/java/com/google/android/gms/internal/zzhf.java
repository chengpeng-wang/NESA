package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzeg.zzd;
import java.util.Map;
import java.util.concurrent.Future;

@zzhb
public final class zzhf {
    /* access modifiers changed from: private */
    public String zzEY;
    /* access modifiers changed from: private */
    public String zzJh;
    /* access modifiers changed from: private */
    public zzjd<zzhi> zzJi = new zzjd();
    zzd zzJj;
    public final zzdf zzJk = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            synchronized (zzhf.this.zzpV) {
                if (zzhf.this.zzJi.isDone()) {
                } else if (zzhf.this.zzEY.equals(map.get("request_id"))) {
                    zzhi zzhi = new zzhi(1, map);
                    zzb.zzaK("Invalid " + zzhi.getType() + " request error: " + zzhi.zzgE());
                    zzhf.this.zzJi.zzg(zzhi);
                }
            }
        }
    };
    public final zzdf zzJl = new zzdf() {
        public void zza(zzjp zzjp, Map<String, String> map) {
            synchronized (zzhf.this.zzpV) {
                if (zzhf.this.zzJi.isDone()) {
                    return;
                }
                zzhi zzhi = new zzhi(-2, map);
                if (zzhf.this.zzEY.equals(zzhi.getRequestId())) {
                    String url = zzhi.getUrl();
                    if (url == null) {
                        zzb.zzaK("URL missing in loadAdUrl GMSG.");
                        return;
                    }
                    if (url.contains("%40mediation_adapters%40")) {
                        String replaceAll = url.replaceAll("%40mediation_adapters%40", zzil.zza(zzjp.getContext(), (String) map.get("check_adapters"), zzhf.this.zzJh));
                        zzhi.setUrl(replaceAll);
                        zzin.v("Ad request URL modified to " + replaceAll);
                    }
                    zzhf.this.zzJi.zzg(zzhi);
                    return;
                }
                zzb.zzaK(zzhi.getRequestId() + " ==== " + zzhf.this.zzEY);
            }
        }
    };
    zzjp zzpD;
    /* access modifiers changed from: private|final */
    public final Object zzpV = new Object();

    public zzhf(String str, String str2) {
        this.zzJh = str2;
        this.zzEY = str;
    }

    public void zzb(zzd zzd) {
        this.zzJj = zzd;
    }

    public zzd zzgB() {
        return this.zzJj;
    }

    public Future<zzhi> zzgC() {
        return this.zzJi;
    }

    public void zzgD() {
        if (this.zzpD != null) {
            this.zzpD.destroy();
            this.zzpD = null;
        }
    }

    public void zzh(zzjp zzjp) {
        this.zzpD = zzjp;
    }
}
