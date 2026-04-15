package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzw;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.ArrayList;
import java.util.List;

public class zzhx {
    private final String[] zzHU;
    private final double[] zzHV;
    private final double[] zzHW;
    private final int[] zzHX;
    private int zzHY;

    public static class zza {
        public final int count;
        public final String name;
        public final double zzHZ;
        public final double zzIa;
        public final double zzIb;

        public zza(String str, double d, double d2, double d3, int i) {
            this.name = str;
            this.zzIa = d;
            this.zzHZ = d2;
            this.zzIb = d3;
            this.count = i;
        }

        public boolean equals(Object other) {
            if (!(other instanceof zza)) {
                return false;
            }
            zza zza = (zza) other;
            return zzw.equal(this.name, zza.name) && this.zzHZ == zza.zzHZ && this.zzIa == zza.zzIa && this.count == zza.count && Double.compare(this.zzIb, zza.zzIb) == 0;
        }

        public int hashCode() {
            return zzw.hashCode(new Object[]{this.name, Double.valueOf(this.zzHZ), Double.valueOf(this.zzIa), Double.valueOf(this.zzIb), Integer.valueOf(this.count)});
        }

        public String toString() {
            return zzw.zzu(this).zzg("name", this.name).zzg("minBound", Double.valueOf(this.zzIa)).zzg("maxBound", Double.valueOf(this.zzHZ)).zzg("percent", Double.valueOf(this.zzIb)).zzg(MASTNativeAdConstants.REQUESTPARAM_COUNT, Integer.valueOf(this.count)).toString();
        }
    }

    public static class zzb {
        /* access modifiers changed from: private|final */
        public final List<String> zzIc = new ArrayList();
        /* access modifiers changed from: private|final */
        public final List<Double> zzId = new ArrayList();
        /* access modifiers changed from: private|final */
        public final List<Double> zzIe = new ArrayList();

        public zzb zza(String str, double d, double d2) {
            int i;
            int i2 = 0;
            while (true) {
                i = i2;
                if (i >= this.zzIc.size()) {
                    break;
                }
                double doubleValue = ((Double) this.zzIe.get(i)).doubleValue();
                double doubleValue2 = ((Double) this.zzId.get(i)).doubleValue();
                if (d < doubleValue || (doubleValue == d && d2 < doubleValue2)) {
                    break;
                }
                i2 = i + 1;
            }
            this.zzIc.add(i, str);
            this.zzIe.add(i, Double.valueOf(d));
            this.zzId.add(i, Double.valueOf(d2));
            return this;
        }

        public zzhx zzgz() {
            return new zzhx(this);
        }
    }

    private zzhx(zzb zzb) {
        int size = zzb.zzId.size();
        this.zzHU = (String[]) zzb.zzIc.toArray(new String[size]);
        this.zzHV = zzc(zzb.zzId);
        this.zzHW = zzc(zzb.zzIe);
        this.zzHX = new int[size];
        this.zzHY = 0;
    }

    private double[] zzc(List<Double> list) {
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
        ArrayList arrayList = new ArrayList(this.zzHU.length);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.zzHU.length) {
                return arrayList;
            }
            arrayList.add(new zza(this.zzHU[i2], this.zzHW[i2], this.zzHV[i2], ((double) this.zzHX[i2]) / ((double) this.zzHY), this.zzHX[i2]));
            i = i2 + 1;
        }
    }

    public void zza(double d) {
        this.zzHY++;
        int i = 0;
        while (i < this.zzHW.length) {
            if (this.zzHW[i] <= d && d < this.zzHV[i]) {
                int[] iArr = this.zzHX;
                iArr[i] = iArr[i] + 1;
            }
            if (d >= this.zzHW[i]) {
                i++;
            } else {
                return;
            }
        }
    }
}
