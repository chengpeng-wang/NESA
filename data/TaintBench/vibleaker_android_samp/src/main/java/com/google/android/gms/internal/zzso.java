package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzso<M extends zzso<M>> extends zzsu {
    protected zzsq zzbuj;

    public void writeTo(zzsn output) throws IOException {
        if (this.zzbuj != null) {
            for (int i = 0; i < this.zzbuj.size(); i++) {
                this.zzbuj.zzmG(i).writeTo(output);
            }
        }
    }

    /* renamed from: zzJp */
    public M clone() throws CloneNotSupportedException {
        zzso zzso = (zzso) super.clone();
        zzss.zza(this, zzso);
        return zzso;
    }

    public final <T> T zza(zzsp<M, T> zzsp) {
        if (this.zzbuj == null) {
            return null;
        }
        zzsr zzmF = this.zzbuj.zzmF(zzsx.zzmJ(zzsp.tag));
        return zzmF != null ? zzmF.zzb(zzsp) : null;
    }

    /* access modifiers changed from: protected|final */
    public final boolean zza(zzsm zzsm, int i) throws IOException {
        int position = zzsm.getPosition();
        if (!zzsm.zzmo(i)) {
            return false;
        }
        int zzmJ = zzsx.zzmJ(i);
        zzsw zzsw = new zzsw(i, zzsm.zzz(position, zzsm.getPosition() - position));
        zzsr zzsr = null;
        if (this.zzbuj == null) {
            this.zzbuj = new zzsq();
        } else {
            zzsr = this.zzbuj.zzmF(zzmJ);
        }
        if (zzsr == null) {
            zzsr = new zzsr();
            this.zzbuj.zza(zzmJ, zzsr);
        }
        zzsr.zza(zzsw);
        return true;
    }

    /* access modifiers changed from: protected */
    public int zzz() {
        int i = 0;
        if (this.zzbuj == null) {
            return 0;
        }
        int i2 = 0;
        while (i < this.zzbuj.size()) {
            i2 += this.zzbuj.zzmG(i).zzz();
            i++;
        }
        return i2;
    }
}
