package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.zzp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@zzgk
public class zzdq implements Iterable<zzdp> {
    private final List<zzdp> zzxu = new LinkedList();

    private zzdp zzc(zzip zzip) {
        Iterator it = zzp.zzbK().iterator();
        while (it.hasNext()) {
            zzdp zzdp = (zzdp) it.next();
            if (zzdp.zzoL == zzip) {
                return zzdp;
            }
        }
        return null;
    }

    public Iterator<zzdp> iterator() {
        return this.zzxu.iterator();
    }

    public void zza(zzdp zzdp) {
        this.zzxu.add(zzdp);
    }

    public boolean zza(zzip zzip) {
        zzdp zzc = zzc(zzip);
        if (zzc == null) {
            return false;
        }
        zzc.zzxr.abort();
        return true;
    }

    public void zzb(zzdp zzdp) {
        this.zzxu.remove(zzdp);
    }

    public boolean zzb(zzip zzip) {
        return zzc(zzip) != null;
    }
}
