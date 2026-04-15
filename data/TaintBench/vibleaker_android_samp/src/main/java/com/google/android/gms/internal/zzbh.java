package com.google.android.gms.internal;

import android.util.Base64OutputStream;
import com.google.android.gms.ads.internal.util.client.zzb;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.PriorityQueue;

@zzhb
public class zzbh {
    private final int zztp;
    private final int zztq;
    private final int zztr;
    private final zzbg zzts = new zzbj();

    static class zza {
        ByteArrayOutputStream zztu = new ByteArrayOutputStream(4096);
        Base64OutputStream zztv = new Base64OutputStream(this.zztu, 10);

        public String toString() {
            String byteArrayOutputStream;
            try {
                this.zztv.close();
            } catch (IOException e) {
                zzb.zzb("HashManager: Unable to convert to Base64.", e);
            }
            try {
                this.zztu.close();
                byteArrayOutputStream = this.zztu.toString();
            } catch (IOException e2) {
                zzb.zzb("HashManager: Unable to convert to Base64.", e2);
                byteArrayOutputStream = "";
            } finally {
                this.zztu = null;
                this.zztv = null;
            }
            return byteArrayOutputStream;
        }

        public void write(byte[] data) throws IOException {
            this.zztv.write(data);
        }
    }

    public zzbh(int i) {
        this.zztq = i;
        this.zztp = 6;
        this.zztr = 0;
    }

    private String zzv(String str) {
        String[] split = str.split("\n");
        if (split.length == 0) {
            return "";
        }
        zza zzcM = zzcM();
        Arrays.sort(split, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s2.length() - s1.length();
            }
        });
        int i = 0;
        while (i < split.length && i < this.zztq) {
            if (split[i].trim().length() != 0) {
                try {
                    zzcM.write(this.zzts.zzu(split[i]));
                } catch (IOException e) {
                    zzb.zzb("Error while writing hash to byteStream", e);
                }
            }
            i++;
        }
        return zzcM.toString();
    }

    public String zza(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            stringBuffer.append(((String) it.next()).toLowerCase(Locale.US));
            stringBuffer.append(10);
        }
        switch (this.zztr) {
            case 0:
                return zzw(stringBuffer.toString());
            case 1:
                return zzv(stringBuffer.toString());
            default:
                return "";
        }
    }

    /* access modifiers changed from: 0000 */
    public zza zzcM() {
        return new zza();
    }

    /* access modifiers changed from: 0000 */
    public String zzw(String str) {
        String[] split = str.split("\n");
        if (split.length == 0) {
            return "";
        }
        zza zzcM = zzcM();
        PriorityQueue priorityQueue = new PriorityQueue(this.zztq, new Comparator<com.google.android.gms.internal.zzbk.zza>() {
            /* renamed from: zza */
            public int compare(com.google.android.gms.internal.zzbk.zza zza, com.google.android.gms.internal.zzbk.zza zza2) {
                int i = zza.zzty - zza2.zzty;
                return i != 0 ? i : (int) (zza.value - zza2.value);
            }
        });
        for (String zzy : split) {
            String[] zzy2 = zzbi.zzy(zzy);
            if (zzy2.length != 0) {
                zzbk.zza(zzy2, this.zztq, this.zztp, priorityQueue);
            }
        }
        Iterator it = priorityQueue.iterator();
        while (it.hasNext()) {
            try {
                zzcM.write(this.zzts.zzu(((com.google.android.gms.internal.zzbk.zza) it.next()).zztx));
            } catch (IOException e) {
                zzb.zzb("Error while writing hash to byteStream", e);
            }
        }
        return zzcM.toString();
    }
}
