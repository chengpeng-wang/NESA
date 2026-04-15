package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@zzhb
public class zzbq {
    private final Collection<zzbp> zzvu = new ArrayList();
    private final Collection<zzbp<String>> zzvv = new ArrayList();
    private final Collection<zzbp<String>> zzvw = new ArrayList();

    public void zza(zzbp zzbp) {
        this.zzvu.add(zzbp);
    }

    public void zzb(zzbp<String> zzbp) {
        this.zzvv.add(zzbp);
    }

    public void zzc(zzbp<String> zzbp) {
        this.zzvw.add(zzbp);
    }

    public List<String> zzdr() {
        ArrayList arrayList = new ArrayList();
        for (zzbp zzbp : this.zzvv) {
            String str = (String) zzbp.get();
            if (str != null) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public List<String> zzds() {
        List zzdr = zzdr();
        for (zzbp zzbp : this.zzvw) {
            String str = (String) zzbp.get();
            if (str != null) {
                zzdr.add(str);
            }
        }
        return zzdr;
    }
}
