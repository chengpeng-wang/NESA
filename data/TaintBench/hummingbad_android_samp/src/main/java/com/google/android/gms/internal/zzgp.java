package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@zzgk
public final class zzgp {
    private int mOrientation = -1;
    private List<String> zzCX;
    private String zzFf;
    private String zzFg;
    private List<String> zzFh;
    private String zzFi;
    private String zzFj;
    private List<String> zzFk;
    private long zzFl = -1;
    private boolean zzFm = false;
    private final long zzFn = -1;
    private long zzFo = -1;
    private boolean zzFp = false;
    private boolean zzFq = false;
    private boolean zzFr = false;
    private boolean zzFs = true;
    private int zzFt = 0;
    private String zzvM;
    private final AdRequestInfoParcel zzyd;

    public zzgp(AdRequestInfoParcel adRequestInfoParcel) {
        this.zzyd = adRequestInfoParcel;
    }

    static String zzd(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        return (list == null || list.isEmpty()) ? null : (String) list.get(0);
    }

    static long zze(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            try {
                return (long) (Float.parseFloat(str2) * 1000.0f);
            } catch (NumberFormatException e) {
                zzb.zzaE("Could not parse float from " + str + " header: " + str2);
            }
        }
        return -1;
    }

    static List<String> zzf(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            if (str2 != null) {
                return Arrays.asList(str2.trim().split("\\s+"));
            }
        }
        return null;
    }

    private boolean zzg(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        return (list == null || list.isEmpty() || !Boolean.valueOf((String) list.get(0)).booleanValue()) ? false : true;
    }

    private void zzi(Map<String, List<String>> map) {
        this.zzFf = zzd(map, "X-Afma-Ad-Size");
    }

    private void zzj(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Click-Tracking-Urls");
        if (zzf != null) {
            this.zzFh = zzf;
        }
    }

    private void zzk(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Debug-Dialog");
        if (list != null && !list.isEmpty()) {
            this.zzFi = (String) list.get(0);
        }
    }

    private void zzl(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Tracking-Urls");
        if (zzf != null) {
            this.zzFk = zzf;
        }
    }

    private void zzm(Map<String, List<String>> map) {
        long zze = zze(map, "X-Afma-Interstitial-Timeout");
        if (zze != -1) {
            this.zzFl = zze;
        }
    }

    private void zzn(Map<String, List<String>> map) {
        this.zzFj = zzd(map, "X-Afma-ActiveView");
    }

    private void zzo(Map<String, List<String>> map) {
        boolean z = this.zzFq;
        int i = (this.zzyd == null || this.zzyd.zzDE == 0) ? 0 : 1;
        this.zzFq = i | z;
    }

    private void zzp(Map<String, List<String>> map) {
        this.zzFp |= zzg(map, "X-Afma-Custom-Rendering-Allowed");
    }

    private void zzq(Map<String, List<String>> map) {
        this.zzFm |= zzg(map, "X-Afma-Mediation");
    }

    private void zzr(Map<String, List<String>> map) {
        List zzf = zzf(map, "X-Afma-Manual-Tracking-Urls");
        if (zzf != null) {
            this.zzCX = zzf;
        }
    }

    private void zzs(Map<String, List<String>> map) {
        long zze = zze(map, "X-Afma-Refresh-Rate");
        if (zze != -1) {
            this.zzFo = zze;
        }
    }

    private void zzt(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Orientation");
        if (list != null && !list.isEmpty()) {
            String str = (String) list.get(0);
            if ("portrait".equalsIgnoreCase(str)) {
                this.mOrientation = zzp.zzbz().zzgw();
            } else if ("landscape".equalsIgnoreCase(str)) {
                this.mOrientation = zzp.zzbz().zzgv();
            }
        }
    }

    private void zzu(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Use-HTTPS");
        if (list != null && !list.isEmpty()) {
            this.zzFr = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void zzv(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Content-Url-Opted-Out");
        if (list != null && !list.isEmpty()) {
            this.zzFs = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void zzw(Map<String, List<String>> map) {
        List<String> zzf = zzf(map, "X-Afma-OAuth-Token-Status");
        this.zzFt = 0;
        if (zzf != null) {
            for (String str : zzf) {
                if ("Clear".equalsIgnoreCase(str)) {
                    this.zzFt = 1;
                    return;
                } else if ("No-Op".equalsIgnoreCase(str)) {
                    this.zzFt = 0;
                    return;
                }
            }
        }
    }

    public void zzb(String str, Map<String, List<String>> map, String str2) {
        this.zzFg = str;
        this.zzvM = str2;
        zzh(map);
    }

    public void zzh(Map<String, List<String>> map) {
        zzi(map);
        zzj((Map) map);
        zzk(map);
        zzl(map);
        zzm(map);
        zzq(map);
        zzr(map);
        zzs(map);
        zzt(map);
        zzn(map);
        zzu(map);
        zzp(map);
        zzo(map);
        zzv(map);
        zzw(map);
    }

    public AdResponseParcel zzj(long j) {
        return new AdResponseParcel(this.zzyd, this.zzFg, this.zzvM, this.zzFh, this.zzFk, this.zzFl, this.zzFm, -1, this.zzCX, this.zzFo, this.mOrientation, this.zzFf, j, this.zzFi, this.zzFj, this.zzFp, this.zzFq, this.zzFr, this.zzFs, false, this.zzFt);
    }
}
