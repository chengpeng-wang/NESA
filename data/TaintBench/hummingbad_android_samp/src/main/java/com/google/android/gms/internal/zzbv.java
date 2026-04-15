package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@zzgk
public class zzbv {
    private final Collection<zzbu> zztR = new ArrayList();
    private final Collection<zzbu<String>> zztS = new ArrayList();
    private final Collection<zzbu<String>> zztT = new ArrayList();

    public void zza(zzbu zzbu) {
        this.zztR.add(zzbu);
    }

    public void zzb(zzbu<String> zzbu) {
        this.zztS.add(zzbu);
    }

    public void zzc(zzbu<String> zzbu) {
        this.zztT.add(zzbu);
    }

    public List<String> zzde() {
        ArrayList arrayList = new ArrayList();
        for (zzbu zzbu : this.zztS) {
            String str = (String) zzbu.get();
            if (str != null) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }
}
