package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class zzsr implements Cloneable {
    private zzsp<?, ?> zzbuq;
    private Object zzbur;
    private List<zzsw> zzbus = new ArrayList();

    zzsr() {
    }

    private byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zzz()];
        writeTo(zzsn.zzE(bArr));
        return bArr;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzsr)) {
            return false;
        }
        zzsr zzsr = (zzsr) o;
        if (this.zzbur == null || zzsr.zzbur == null) {
            if (this.zzbus != null && zzsr.zzbus != null) {
                return this.zzbus.equals(zzsr.zzbus);
            }
            try {
                return Arrays.equals(toByteArray(), zzsr.toByteArray());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else if (this.zzbuq != zzsr.zzbuq) {
            return false;
        } else {
            if (!this.zzbuq.zzbuk.isArray()) {
                return this.zzbur.equals(zzsr.zzbur);
            }
            if (this.zzbur instanceof byte[]) {
                return Arrays.equals((byte[]) this.zzbur, (byte[]) zzsr.zzbur);
            }
            if (this.zzbur instanceof int[]) {
                return Arrays.equals((int[]) this.zzbur, (int[]) zzsr.zzbur);
            }
            if (this.zzbur instanceof long[]) {
                return Arrays.equals((long[]) this.zzbur, (long[]) zzsr.zzbur);
            }
            if (this.zzbur instanceof float[]) {
                return Arrays.equals((float[]) this.zzbur, (float[]) zzsr.zzbur);
            }
            if (this.zzbur instanceof double[]) {
                return Arrays.equals((double[]) this.zzbur, (double[]) zzsr.zzbur);
            }
            return this.zzbur instanceof boolean[] ? Arrays.equals((boolean[]) this.zzbur, (boolean[]) zzsr.zzbur) : Arrays.deepEquals((Object[]) this.zzbur, (Object[]) zzsr.zzbur);
        }
    }

    public int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void writeTo(zzsn output) throws IOException {
        if (this.zzbur != null) {
            this.zzbuq.zza(this.zzbur, output);
            return;
        }
        for (zzsw writeTo : this.zzbus) {
            writeTo.writeTo(output);
        }
    }

    /* renamed from: zzJr */
    public final zzsr clone() {
        int i = 0;
        zzsr zzsr = new zzsr();
        try {
            zzsr.zzbuq = this.zzbuq;
            if (this.zzbus == null) {
                zzsr.zzbus = null;
            } else {
                zzsr.zzbus.addAll(this.zzbus);
            }
            if (this.zzbur != null) {
                if (this.zzbur instanceof zzsu) {
                    zzsr.zzbur = ((zzsu) this.zzbur).clone();
                } else if (this.zzbur instanceof byte[]) {
                    zzsr.zzbur = ((byte[]) this.zzbur).clone();
                } else if (this.zzbur instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.zzbur;
                    byte[][] bArr2 = new byte[bArr.length][];
                    zzsr.zzbur = bArr2;
                    for (int i2 = 0; i2 < bArr.length; i2++) {
                        bArr2[i2] = (byte[]) bArr[i2].clone();
                    }
                } else if (this.zzbur instanceof boolean[]) {
                    zzsr.zzbur = ((boolean[]) this.zzbur).clone();
                } else if (this.zzbur instanceof int[]) {
                    zzsr.zzbur = ((int[]) this.zzbur).clone();
                } else if (this.zzbur instanceof long[]) {
                    zzsr.zzbur = ((long[]) this.zzbur).clone();
                } else if (this.zzbur instanceof float[]) {
                    zzsr.zzbur = ((float[]) this.zzbur).clone();
                } else if (this.zzbur instanceof double[]) {
                    zzsr.zzbur = ((double[]) this.zzbur).clone();
                } else if (this.zzbur instanceof zzsu[]) {
                    zzsu[] zzsuArr = (zzsu[]) this.zzbur;
                    zzsu[] zzsuArr2 = new zzsu[zzsuArr.length];
                    zzsr.zzbur = zzsuArr2;
                    while (i < zzsuArr.length) {
                        zzsuArr2[i] = zzsuArr[i].clone();
                        i++;
                    }
                }
            }
            return zzsr;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void zza(zzsw zzsw) {
        this.zzbus.add(zzsw);
    }

    /* access modifiers changed from: 0000 */
    public <T> T zzb(zzsp<?, T> zzsp) {
        if (this.zzbur == null) {
            this.zzbuq = zzsp;
            this.zzbur = zzsp.zzJ(this.zzbus);
            this.zzbus = null;
        } else if (this.zzbuq != zzsp) {
            throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
        }
        return this.zzbur;
    }

    /* access modifiers changed from: 0000 */
    public int zzz() {
        int i = 0;
        if (this.zzbur != null) {
            return this.zzbuq.zzY(this.zzbur);
        }
        Iterator it = this.zzbus.iterator();
        while (true) {
            int i2 = i;
            if (!it.hasNext()) {
                return i2;
            }
            i = ((zzsw) it.next()).zzz() + i2;
        }
    }
}
