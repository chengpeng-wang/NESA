package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

final class zzsw {
    final int tag;
    final byte[] zzbuv;

    zzsw(int i, byte[] bArr) {
        this.tag = i;
        this.zzbuv = bArr;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzsw)) {
            return false;
        }
        zzsw zzsw = (zzsw) o;
        return this.tag == zzsw.tag && Arrays.equals(this.zzbuv, zzsw.zzbuv);
    }

    public int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzbuv);
    }

    /* access modifiers changed from: 0000 */
    public void writeTo(zzsn output) throws IOException {
        output.zzmB(this.tag);
        output.zzH(this.zzbuv);
    }

    /* access modifiers changed from: 0000 */
    public int zzz() {
        return (0 + zzsn.zzmC(this.tag)) + this.zzbuv.length;
    }
}
