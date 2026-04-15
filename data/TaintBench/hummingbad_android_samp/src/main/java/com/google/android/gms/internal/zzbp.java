package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.util.PriorityQueue;
import org.xbill.DNS.TTL;

public class zzbp {

    public static class zza {
        final long value;
        final String zzsm;

        zza(long j, String str) {
            this.value = j;
            this.zzsm = str;
        }

        public boolean equals(Object other) {
            return other != null && (other instanceof zza) && ((zza) other).value == this.value;
        }

        public int hashCode() {
            return (int) this.value;
        }
    }

    static long zza(int i, int i2, long j, long j2, long j3) {
        return ((((((j + 1073807359) - ((((((long) i) + TTL.MAX_VALUE) % 1073807359) * j2) % 1073807359)) % 1073807359) * j3) % 1073807359) + ((((long) i2) + TTL.MAX_VALUE) % 1073807359)) % 1073807359;
    }

    static long zza(long j, int i) {
        return i == 0 ? 1 : i != 1 ? i % 2 == 0 ? zza((j * j) % 1073807359, i / 2) % 1073807359 : ((zza((j * j) % 1073807359, i / 2) % 1073807359) * j) % 1073807359 : j;
    }

    static String zza(String[] strArr, int i, int i2) {
        if (strArr.length < i + i2) {
            zzb.e("Unable to construct shingle");
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = i; i3 < (i + i2) - 1; i3++) {
            stringBuffer.append(strArr[i3]);
            stringBuffer.append(' ');
        }
        stringBuffer.append(strArr[(i + i2) - 1]);
        return stringBuffer.toString();
    }

    static void zza(int i, long j, String str, PriorityQueue<zza> priorityQueue) {
        zza zza = new zza(j, str);
        if ((priorityQueue.size() != i || ((zza) priorityQueue.peek()).value <= zza.value) && !priorityQueue.contains(zza)) {
            priorityQueue.add(zza);
            if (priorityQueue.size() > i) {
                priorityQueue.poll();
            }
        }
    }

    public static void zza(String[] strArr, int i, int i2, PriorityQueue<zza> priorityQueue) {
        long zzb = zzb(strArr, 0, i2);
        zza(i, zzb, zza(strArr, 0, i2), (PriorityQueue) priorityQueue);
        long zza = zza(16785407, i2 - 1);
        int i3 = 1;
        while (true) {
            int i4 = i3;
            if (i4 < (strArr.length - i2) + 1) {
                zzb = zza(zzbn.zzC(strArr[i4 - 1]), zzbn.zzC(strArr[(i4 + i2) - 1]), zzb, zza, 16785407);
                zza(i, zzb, zza(strArr, i4, i2), (PriorityQueue) priorityQueue);
                i3 = i4 + 1;
            } else {
                return;
            }
        }
    }

    private static long zzb(String[] strArr, int i, int i2) {
        long zzC = (((long) zzbn.zzC(strArr[i])) + TTL.MAX_VALUE) % 1073807359;
        for (int i3 = i + 1; i3 < i + i2; i3++) {
            zzC = (((zzC * 16785407) % 1073807359) + ((((long) zzbn.zzC(strArr[i3])) + TTL.MAX_VALUE) % 1073807359)) % 1073807359;
        }
        return zzC;
    }
}
