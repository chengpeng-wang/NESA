package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzw;
import gr.georkouk.kastorakiacounter_new.MyVariables;
import java.util.ArrayList;
import java.util.List;

@zzhb
public class zziv {
    private final String[] zzMn;
    private final double[] zzMo;
    private final double[] zzMp;
    private final int[] zzMq;
    private int zzMr;

    public static class zza {
        public final int count;
        public final String name;
        public final double zzMs;
        public final double zzMt;
        public final double zzMu;

        public zza(String str, double d, double d2, double d3, int i) {
            this.name = str;
            this.zzMt = d;
            this.zzMs = d2;
            this.zzMu = d3;
            this.count = i;
        }

        public boolean equals(Object other) {
            if (!(other instanceof zza)) {
                return false;
            }
            zza zza = (zza) other;
            return zzw.equal(this.name, zza.name) && this.zzMs == zza.zzMs && this.zzMt == zza.zzMt && this.count == zza.count && Double.compare(this.zzMu, zza.zzMu) == 0;
        }

        public int hashCode() {
            return zzw.hashCode(this.name, Double.valueOf(this.zzMs), Double.valueOf(this.zzMt), Double.valueOf(this.zzMu), Integer.valueOf(this.count));
        }

        public String toString() {
            return zzw.zzy(this).zzg(MyVariables.KEY_NAME, this.name).zzg("minBound", Double.valueOf(this.zzMt)).zzg("maxBound", Double.valueOf(this.zzMs)).zzg("percent", Double.valueOf(this.zzMu)).zzg("count", Integer.valueOf(this.count)).toString();
        }
    }

    public static class zzb {
        /* access modifiers changed from: private|final */
        public final List<String> zzMv = new ArrayList();
        /* access modifiers changed from: private|final */
        public final List<Double> zzMw = new ArrayList();
        /* access modifiers changed from: private|final */
        public final List<Double> zzMx = new ArrayList();

        public zzb zza(String str, double d, double d2) {
            int i;
            int i2 = 0;
            while (true) {
                i = i2;
                if (i >= this.zzMv.size()) {
                    break;
                }
                double doubleValue = ((Double) this.zzMx.get(i)).doubleValue();
                double doubleValue2 = ((Double) this.zzMw.get(i)).doubleValue();
                if (d < doubleValue || (doubleValue == d && d2 < doubleValue2)) {
                    break;
                }
                i2 = i + 1;
            }
            this.zzMv.add(i, str);
            this.zzMx.add(i, Double.valueOf(d));
            this.zzMw.add(i, Double.valueOf(d2));
            return this;
        }

        public zziv zzhA() {
            return new zziv(this);
        }
    }

    private zziv(zzb zzb) {
        int size = zzb.zzMw.size();
        this.zzMn = (String[]) zzb.zzMv.toArray(new String[size]);
        this.zzMo = zzk(zzb.zzMw);
        this.zzMp = zzk(zzb.zzMx);
        this.zzMq = new int[size];
        this.zzMr = 0;
    }

    private double[] zzk(List<Double> list) {
        double[] dArr = new double[list.size()];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= dArr.length) {
                return dArr;
            }
            dArr[i2] = ((Double) list.get(i2)).doubleValue();
            i = i2 + 1;
        }
    }

    public List<zza> getBuckets() {
        ArrayList arrayList = new ArrayList(this.zzMn.length);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.zzMn.length) {
                return arrayList;
            }
            arrayList.add(new zza(this.zzMn[i2], this.zzMp[i2], this.zzMo[i2], ((double) this.zzMq[i2]) / ((double) this.zzMr), this.zzMq[i2]));
            i = i2 + 1;
        }
    }

    public void zza(double d) {
        this.zzMr++;
        int i = 0;
        while (i < this.zzMp.length) {
            if (this.zzMp[i] <= d && d < this.zzMo[i]) {
                int[] iArr = this.zzMq;
                iArr[i] = iArr[i] + 1;
            }
            if (d >= this.zzMp[i]) {
                i++;
            } else {
                return;
            }
        }
    }
}
