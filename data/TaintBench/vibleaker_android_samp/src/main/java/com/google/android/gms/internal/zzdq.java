package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.zzr;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzhb
public class zzdq implements Iterable<zzdp> {
    private final List<zzdp> zzzM = new LinkedList();

    private zzdp zzf(zzjp zzjp) {
        Iterator it = zzr.zzbR().iterator();
        while (it.hasNext()) {
            zzdp zzdp = (zzdp) it.next();
            if (zzdp.zzpD == zzjp) {
                return zzdp;
            }
        }
        return null;
    }

    public Iterator<zzdp> iterator() {
        return this.zzzM.iterator();
    }

    public void zza(zzdp zzdp) {
        this.zzzM.add(zzdp);
    }

    public void zzb(zzdp zzdp) {
        this.zzzM.remove(zzdp);
    }

    public boolean zzd(zzjp zzjp) {
        zzdp zzf = zzf(zzjp);
        if (zzf == null) {
            return false;
        }
        zzf.zzzJ.abort();
        return true;
    }

    public boolean zze(zzjp zzjp) {
        return zzf(zzjp) != null;
    }
}
