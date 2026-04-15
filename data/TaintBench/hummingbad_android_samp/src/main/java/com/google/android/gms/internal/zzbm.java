package com.google.android.gms.internal;

import android.util.Base64OutputStream;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.PriorityQueue;

public class zzbm {
    private final int zzse;
    private final int zzsf;
    private final int zzsg;
    private final zzbl zzsh = new zzbo();

    static class zza {
        ByteArrayOutputStream zzsj = new ByteArrayOutputStream(4096);
        Base64OutputStream zzsk = new Base64OutputStream(this.zzsj, 10);

        public String toString() {
            String byteArrayOutputStream;
            try {
                this.zzsk.close();
            } catch (IOException e) {
                zzb.zzb("HashManager: Unable to convert to Base64.", e);
            }
            try {
                this.zzsj.close();
                byteArrayOutputStream = this.zzsj.toString();
            } catch (IOException e2) {
                zzb.zzb("HashManager: Unable to convert to Base64.", e2);
                byteArrayOutputStream = "";
            } finally {
                this.zzsj = null;
                this.zzsk = null;
            }
            return byteArrayOutputStream;
        }

        public void write(byte[] data) throws IOException {
            this.zzsk.write(data);
        }
    }

    public zzbm(int i) {
        this.zzsf = i;
        this.zzse = 6;
        this.zzsg = 0;
    }

    private String zzA(String str) {
        String[] split = str.split(MASTNativeAdConstants.NEWLINE);
        if (split.length == 0) {
            return "";
        }
        zza zzcz = zzcz();
        Arrays.sort(split, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s2.length() - s1.length();
            }
        });
        int i = 0;
        while (i < split.length && i < this.zzsf) {
            if (split[i].trim().length() != 0) {
                try {
                    zzcz.write(this.zzsh.zzz(split[i]));
                } catch (IOException e) {
                    zzb.zzb("Error while writing hash to byteStream", e);
                }
            }
            i++;
        }
        return zzcz.toString();
    }

    /* access modifiers changed from: 0000 */
    public String zzB(String str) {
        String[] split = str.split(MASTNativeAdConstants.NEWLINE);
        if (split.length == 0) {
            return "";
        }
        zza zzcz = zzcz();
        PriorityQueue priorityQueue = new PriorityQueue(this.zzsf, new Comparator<com.google.android.gms.internal.zzbp.zza>() {
            /* renamed from: zza */
            public int compare(com.google.android.gms.internal.zzbp.zza zza, com.google.android.gms.internal.zzbp.zza zza2) {
                return (int) (zza.value - zza2.value);
            }
        });
        for (String zzD : split) {
            String[] zzD2 = zzbn.zzD(zzD);
            if (zzD2.length >= this.zzse) {
                zzbp.zza(zzD2, this.zzsf, this.zzse, priorityQueue);
            }
        }
        Iterator it = priorityQueue.iterator();
        while (it.hasNext()) {
            try {
                zzcz.write(this.zzsh.zzz(((com.google.android.gms.internal.zzbp.zza) it.next()).zzsm));
            } catch (IOException e) {
                zzb.zzb("Error while writing hash to byteStream", e);
            }
        }
        return zzcz.toString();
    }

    public String zza(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            stringBuffer.append(((String) it.next()).toLowerCase(Locale.US));
            stringBuffer.append(10);
        }
        switch (this.zzsg) {
            case 0:
                return zzB(stringBuffer.toString());
            case 1:
                return zzA(stringBuffer.toString());
            default:
                return "";
        }
    }

    /* access modifiers changed from: 0000 */
    public zza zzcz() {
        return new zza();
    }
}
