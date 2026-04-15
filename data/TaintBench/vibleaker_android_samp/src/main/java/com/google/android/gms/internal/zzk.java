package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

public abstract class zzk<T> implements Comparable<zzk<T>> {
    /* access modifiers changed from: private|final */
    public final zza zzD;
    private final int zzE;
    private final String zzF;
    private final int zzG;
    private final com.google.android.gms.internal.zzm.zza zzH;
    private Integer zzI;
    private zzl zzJ;
    private boolean zzK;
    private boolean zzL;
    private boolean zzM;
    private long zzN;
    private zzo zzO;
    private com.google.android.gms.internal.zzb.zza zzP;

    public enum zza {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    public zzk(int i, String str, com.google.android.gms.internal.zzm.zza zza) {
        this.zzD = zza.zzak ? new zza() : null;
        this.zzK = true;
        this.zzL = false;
        this.zzM = false;
        this.zzN = 0;
        this.zzP = null;
        this.zzE = i;
        this.zzF = str;
        this.zzH = zza;
        zza(new zzd());
        this.zzG = zzb(str);
    }

    private byte[] zza(Map<String, String> map, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (Entry entry : map.entrySet()) {
                stringBuilder.append(URLEncoder.encode((String) entry.getKey(), str));
                stringBuilder.append('=');
                stringBuilder.append(URLEncoder.encode((String) entry.getValue(), str));
                stringBuilder.append('&');
            }
            return stringBuilder.toString().getBytes(str);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported: " + str, e);
        }
    }

    private static int zzb(String str) {
        if (!TextUtils.isEmpty(str)) {
            Uri parse = Uri.parse(str);
            if (parse != null) {
                String host = parse.getHost();
                if (host != null) {
                    return host.hashCode();
                }
            }
        }
        return 0;
    }

    public Map<String, String> getHeaders() throws zza {
        return Collections.emptyMap();
    }

    public int getMethod() {
        return this.zzE;
    }

    public String getUrl() {
        return this.zzF;
    }

    public boolean isCanceled() {
        return this.zzL;
    }

    public String toString() {
        return (this.zzL ? "[X] " : "[ ] ") + getUrl() + " " + ("0x" + Integer.toHexString(zzg())) + " " + zzs() + " " + this.zzI;
    }

    public final zzk<?> zza(int i) {
        this.zzI = Integer.valueOf(i);
        return this;
    }

    public zzk<?> zza(com.google.android.gms.internal.zzb.zza zza) {
        this.zzP = zza;
        return this;
    }

    public zzk<?> zza(zzl zzl) {
        this.zzJ = zzl;
        return this;
    }

    public zzk<?> zza(zzo zzo) {
        this.zzO = zzo;
        return this;
    }

    public abstract zzm<T> zza(zzi zzi);

    public abstract void zza(T t);

    /* access modifiers changed from: protected */
    public zzr zzb(zzr zzr) {
        return zzr;
    }

    /* renamed from: zzc */
    public int compareTo(zzk<T> zzk) {
        zza zzs = zzs();
        zza zzs2 = zzk.zzs();
        return zzs == zzs2 ? this.zzI.intValue() - zzk.zzI.intValue() : zzs2.ordinal() - zzs.ordinal();
    }

    public void zzc(zzr zzr) {
        if (this.zzH != null) {
            this.zzH.zze(zzr);
        }
    }

    public void zzc(String str) {
        if (zza.zzak) {
            this.zzD.zza(str, Thread.currentThread().getId());
        } else if (this.zzN == 0) {
            this.zzN = SystemClock.elapsedRealtime();
        }
    }

    /* access modifiers changed from: 0000 */
    public void zzd(final String str) {
        if (this.zzJ != null) {
            this.zzJ.zzf(this);
        }
        if (zza.zzak) {
            final long id = Thread.currentThread().getId();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        zzk.this.zzD.zza(str, id);
                        zzk.this.zzD.zzd(toString());
                    }
                });
                return;
            }
            this.zzD.zza(str, id);
            this.zzD.zzd(toString());
            return;
        }
        if (SystemClock.elapsedRealtime() - this.zzN >= 3000) {
            zzs.zzb("%d ms: %s", Long.valueOf(SystemClock.elapsedRealtime() - this.zzN), toString());
        }
    }

    public int zzg() {
        return this.zzG;
    }

    public String zzh() {
        return getUrl();
    }

    public com.google.android.gms.internal.zzb.zza zzi() {
        return this.zzP;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public Map<String, String> zzj() throws zza {
        return zzn();
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public String zzk() {
        return zzo();
    }

    @Deprecated
    public String zzl() {
        return zzp();
    }

    @Deprecated
    public byte[] zzm() throws zza {
        Map zzj = zzj();
        return (zzj == null || zzj.size() <= 0) ? null : zza(zzj, zzk());
    }

    /* access modifiers changed from: protected */
    public Map<String, String> zzn() throws zza {
        return null;
    }

    /* access modifiers changed from: protected */
    public String zzo() {
        return "UTF-8";
    }

    public String zzp() {
        return "application/x-www-form-urlencoded; charset=" + zzo();
    }

    public byte[] zzq() throws zza {
        Map zzn = zzn();
        return (zzn == null || zzn.size() <= 0) ? null : zza(zzn, zzo());
    }

    public final boolean zzr() {
        return this.zzK;
    }

    public zza zzs() {
        return zza.NORMAL;
    }

    public final int zzt() {
        return this.zzO.zzd();
    }

    public zzo zzu() {
        return this.zzO;
    }

    public void zzv() {
        this.zzM = true;
    }

    public boolean zzw() {
        return this.zzM;
    }
}
