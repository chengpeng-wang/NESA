package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.internal.zzdv.zzd;
import java.util.Map;
import java.util.concurrent.Future;

@zzgk
public final class zzgo {
    /* access modifiers changed from: private */
    public String zzBm;
    /* access modifiers changed from: private */
    public String zzEZ;
    /* access modifiers changed from: private */
    public zzie<zzgq> zzFa = new zzie();
    zzd zzFb;
    public final zzdg zzFc = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            synchronized (zzgo.this.zzpc) {
                if (zzgo.this.zzFa.isDone()) {
                } else if (zzgo.this.zzBm.equals(map.get("request_id"))) {
                    zzgq zzgq = new zzgq(1, map);
                    zzb.zzaE("Invalid " + zzgq.getType() + " request error: " + zzgq.zzfK());
                    zzgo.this.zzFa.zzf(zzgq);
                }
            }
        }
    };
    public final zzdg zzFd = new zzdg() {
        public void zza(zzip zzip, Map<String, String> map) {
            synchronized (zzgo.this.zzpc) {
                if (zzgo.this.zzFa.isDone()) {
                    return;
                }
                zzgq zzgq = new zzgq(-2, map);
                if (zzgo.this.zzBm.equals(zzgq.getRequestId())) {
                    String url = zzgq.getUrl();
                    if (url == null) {
                        zzb.zzaE("URL missing in loadAdUrl GMSG.");
                        return;
                    }
                    if (url.contains("%40mediation_adapters%40")) {
                        String replaceAll = url.replaceAll("%40mediation_adapters%40", zzhp.zza(zzip.getContext(), (String) map.get("check_adapters"), zzgo.this.zzEZ));
                        zzgq.setUrl(replaceAll);
                        zzb.v("Ad request URL modified to " + replaceAll);
                    }
                    zzgo.this.zzFa.zzf(zzgq);
                    return;
                }
                zzb.zzaE(zzgq.getRequestId() + " ==== " + zzgo.this.zzBm);
            }
        }
    };
    zzip zzoL;
    /* access modifiers changed from: private|final */
    public final Object zzpc = new Object();

    public zzgo(String str, String str2) {
        this.zzEZ = str2;
        this.zzBm = str;
    }

    public void zzb(zzd zzd) {
        this.zzFb = zzd;
    }

    public void zze(zzip zzip) {
        this.zzoL = zzip;
    }

    public zzd zzfH() {
        return this.zzFb;
    }

    public Future<zzgq> zzfI() {
        return this.zzFa;
    }

    public void zzfJ() {
        if (this.zzoL != null) {
            this.zzoL.destroy();
            this.zzoL = null;
        }
    }
}
