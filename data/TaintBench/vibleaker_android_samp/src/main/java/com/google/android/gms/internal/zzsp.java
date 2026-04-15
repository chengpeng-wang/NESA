package com.google.android.gms.internal;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class zzsp<M extends zzso<M>, T> {
    public final int tag;
    protected final int type;
    protected final Class<T> zzbuk;
    protected final boolean zzbul;

    private zzsp(int i, Class<T> cls, int i2, boolean z) {
        this.type = i;
        this.zzbuk = cls;
        this.tag = i2;
        this.zzbul = z;
    }

    private T zzK(List<zzsw> list) {
        int i;
        int i2 = 0;
        List arrayList = new ArrayList();
        for (i = 0; i < list.size(); i++) {
            zzsw zzsw = (zzsw) list.get(i);
            if (zzsw.zzbuv.length != 0) {
                zza(zzsw, arrayList);
            }
        }
        i = arrayList.size();
        if (i == 0) {
            return null;
        }
        Object cast = this.zzbuk.cast(Array.newInstance(this.zzbuk.getComponentType(), i));
        while (i2 < i) {
            Array.set(cast, i2, arrayList.get(i2));
            i2++;
        }
        return cast;
    }

    private T zzL(List<zzsw> list) {
        if (list.isEmpty()) {
            return null;
        }
        return this.zzbuk.cast(zzP(zzsm.zzD(((zzsw) list.get(list.size() - 1)).zzbuv)));
    }

    public static <M extends zzso<M>, T extends zzsu> zzsp<M, T> zza(int i, Class<T> cls, long j) {
        return new zzsp(i, cls, (int) j, false);
    }

    /* access modifiers changed from: final */
    public final T zzJ(List<zzsw> list) {
        return list == null ? null : this.zzbul ? zzK(list) : zzL(list);
    }

    /* access modifiers changed from: protected */
    public Object zzP(zzsm zzsm) {
        Object componentType;
        if (this.zzbul) {
            componentType = this.zzbuk.getComponentType();
        } else {
            Class componentType2 = this.zzbuk;
        }
        try {
            zzsu zzsu;
            switch (this.type) {
                case 10:
                    zzsu = (zzsu) componentType2.newInstance();
                    zzsm.zza(zzsu, zzsx.zzmJ(this.tag));
                    return zzsu;
                case 11:
                    zzsu = (zzsu) componentType2.newInstance();
                    zzsm.zza(zzsu);
                    return zzsu;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType2, e);
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType2, e2);
        } catch (IOException e3) {
            throw new IllegalArgumentException("Error reading extension field", e3);
        }
    }

    /* access modifiers changed from: 0000 */
    public int zzY(Object obj) {
        return this.zzbul ? zzZ(obj) : zzaa(obj);
    }

    /* access modifiers changed from: protected */
    public int zzZ(Object obj) {
        int i = 0;
        int length = Array.getLength(obj);
        for (int i2 = 0; i2 < length; i2++) {
            if (Array.get(obj, i2) != null) {
                i += zzaa(Array.get(obj, i2));
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void zza(zzsw zzsw, List<Object> list) {
        list.add(zzP(zzsm.zzD(zzsw.zzbuv)));
    }

    /* access modifiers changed from: 0000 */
    public void zza(Object obj, zzsn zzsn) throws IOException {
        if (this.zzbul) {
            zzc(obj, zzsn);
        } else {
            zzb(obj, zzsn);
        }
    }

    /* access modifiers changed from: protected */
    public int zzaa(Object obj) {
        int zzmJ = zzsx.zzmJ(this.tag);
        switch (this.type) {
            case 10:
                return zzsn.zzb(zzmJ, (zzsu) obj);
            case 11:
                return zzsn.zzc(zzmJ, (zzsu) obj);
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }
    }

    /* access modifiers changed from: protected */
    public void zzb(Object obj, zzsn zzsn) {
        try {
            zzsn.zzmB(this.tag);
            switch (this.type) {
                case 10:
                    zzsu zzsu = (zzsu) obj;
                    int zzmJ = zzsx.zzmJ(this.tag);
                    zzsn.zzb(zzsu);
                    zzsn.zzE(zzmJ, 4);
                    return;
                case 11:
                    zzsn.zzc((zzsu) obj);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException(e);
    }

    /* access modifiers changed from: protected */
    public void zzc(Object obj, zzsn zzsn) {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null) {
                zzb(obj2, zzsn);
            }
        }
    }
}
