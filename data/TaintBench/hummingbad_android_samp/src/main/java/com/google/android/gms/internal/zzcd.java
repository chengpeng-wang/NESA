package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.ads.internal.zzp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class zzcd {
    private final Object zzpc = new Object();
    boolean zzvf;
    private final List<zzcc> zzvr = new LinkedList();
    private final Map<String, String> zzvs = new LinkedHashMap();
    private String zzvt;
    private zzcc zzvu;

    public zzcd(boolean z, String str, String str2) {
        this.zzvf = z;
        this.zzvs.put("action", str);
        this.zzvs.put("ad_format", str2);
    }

    private boolean zza(zzcc zzcc, long j, String... strArr) {
        synchronized (this.zzpc) {
            for (String zzcc2 : strArr) {
                this.zzvr.add(new zzcc(j, zzcc2, zzcc));
            }
        }
        return true;
    }

    public void zzR(String str) {
        if (this.zzvf) {
            synchronized (this.zzpc) {
                this.zzvt = str;
            }
        }
    }

    public boolean zza(zzcc zzcc, String... strArr) {
        return (!this.zzvf || zzcc == null) ? false : zza(zzcc, zzp.zzbB().elapsedRealtime(), strArr);
    }

    public zzcc zzb(long j) {
        return !this.zzvf ? null : new zzcc(j, null, null);
    }

    public void zzd(String str, String str2) {
        if (this.zzvf) {
            synchronized (this.zzpc) {
                this.zzvs.put(str, str2);
            }
        }
    }

    public zzcc zzdl() {
        return zzb(zzp.zzbB().elapsedRealtime());
    }

    public void zzdm() {
        synchronized (this.zzpc) {
            this.zzvu = zzdl();
        }
    }

    public String zzdn() {
        String stringBuilder;
        StringBuilder stringBuilder2 = new StringBuilder();
        synchronized (this.zzpc) {
            for (zzcc zzcc : this.zzvr) {
                long time = zzcc.getTime();
                String zzdj = zzcc.zzdj();
                zzcc zzcc2 = zzcc2.zzdk();
                if (zzcc2 != null && time > 0) {
                    stringBuilder2.append(zzdj).append('.').append(time - zzcc2.getTime()).append(',');
                }
            }
            this.zzvr.clear();
            if (!TextUtils.isEmpty(this.zzvt)) {
                stringBuilder2.append(this.zzvt);
            } else if (stringBuilder2.length() > 0) {
                stringBuilder2.setLength(stringBuilder2.length() - 1);
            }
            stringBuilder = stringBuilder2.toString();
        }
        return stringBuilder;
    }

    public zzcc zzdo() {
        zzcc zzcc;
        synchronized (this.zzpc) {
            zzcc = this.zzvu;
        }
        return zzcc;
    }

    /* access modifiers changed from: 0000 */
    public Map<String, String> zzn() {
        Map map;
        synchronized (this.zzpc) {
            map = this.zzvs;
        }
        return map;
    }
}
