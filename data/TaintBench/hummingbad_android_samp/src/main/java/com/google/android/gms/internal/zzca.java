package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzp;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class zzca {
    final Context mContext;
    final String zzqK;
    String zzvg;
    BlockingQueue<zzcd> zzvi;
    ExecutorService zzvj;
    LinkedHashMap<String, String> zzvk = new LinkedHashMap();
    private AtomicBoolean zzvl;
    private File zzvm;

    public zzca(Context context, String str, String str2, Map<String, String> map) {
        this.mContext = context;
        this.zzqK = str;
        this.zzvg = str2;
        this.zzvl = new AtomicBoolean(false);
        this.zzvl.set(((Boolean) zzby.zzuD.get()).booleanValue());
        if (this.zzvl.get()) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (externalStorageDirectory != null) {
                this.zzvm = new File(externalStorageDirectory, "sdk_csi_data.txt");
            }
        }
        for (Entry entry : map.entrySet()) {
            this.zzvk.put(entry.getKey(), entry.getValue());
        }
        this.zzvi = new ArrayBlockingQueue(30);
        this.zzvj = Executors.newSingleThreadExecutor();
        this.zzvj.execute(new Runnable() {
            public void run() {
                zzca.this.zzdi();
            }
        });
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x003a A:{SYNTHETIC, Splitter:B:23:0x003a} */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002b A:{SYNTHETIC, Splitter:B:16:0x002b} */
    private void zza(java.io.File r4, java.lang.String r5) {
        /*
        r3 = this;
        if (r4 == 0) goto L_0x0045;
    L_0x0002:
        r2 = 0;
        r1 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0022, all -> 0x0036 }
        r0 = 1;
        r1.<init>(r4, r0);	 Catch:{ IOException -> 0x0022, all -> 0x0036 }
        r0 = r5.getBytes();	 Catch:{ IOException -> 0x004d }
        r1.write(r0);	 Catch:{ IOException -> 0x004d }
        r0 = 10;
        r1.write(r0);	 Catch:{ IOException -> 0x004d }
        if (r1 == 0) goto L_0x001a;
    L_0x0017:
        r1.close();	 Catch:{ IOException -> 0x001b }
    L_0x001a:
        return;
    L_0x001b:
        r0 = move-exception;
        r1 = "CsiReporter: Cannot close file: sdk_csi_data.txt.";
        com.google.android.gms.ads.internal.util.client.zzb.zzd(r1, r0);
        goto L_0x001a;
    L_0x0022:
        r0 = move-exception;
        r1 = r2;
    L_0x0024:
        r2 = "CsiReporter: Cannot write to file: sdk_csi_data.txt.";
        com.google.android.gms.ads.internal.util.client.zzb.zzd(r2, r0);	 Catch:{ all -> 0x004b }
        if (r1 == 0) goto L_0x001a;
    L_0x002b:
        r1.close();	 Catch:{ IOException -> 0x002f }
        goto L_0x001a;
    L_0x002f:
        r0 = move-exception;
        r1 = "CsiReporter: Cannot close file: sdk_csi_data.txt.";
        com.google.android.gms.ads.internal.util.client.zzb.zzd(r1, r0);
        goto L_0x001a;
    L_0x0036:
        r0 = move-exception;
        r1 = r2;
    L_0x0038:
        if (r1 == 0) goto L_0x003d;
    L_0x003a:
        r1.close();	 Catch:{ IOException -> 0x003e }
    L_0x003d:
        throw r0;
    L_0x003e:
        r1 = move-exception;
        r2 = "CsiReporter: Cannot close file: sdk_csi_data.txt.";
        com.google.android.gms.ads.internal.util.client.zzb.zzd(r2, r1);
        goto L_0x003d;
    L_0x0045:
        r0 = "CsiReporter: File doesn't exists. Cannot write CSI data to file.";
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r0);
        goto L_0x001a;
    L_0x004b:
        r0 = move-exception;
        goto L_0x0038;
    L_0x004d:
        r0 = move-exception;
        goto L_0x0024;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzca.zza(java.io.File, java.lang.String):void");
    }

    private void zzc(Map<String, String> map, String str) {
        String zza = zza(this.zzvg, map, str);
        if (this.zzvl.get()) {
            zza(this.zzvm, zza);
        } else {
            zzp.zzbx().zzc(this.mContext, this.zzqK, zza);
        }
    }

    /* access modifiers changed from: private */
    public void zzdi() {
        while (true) {
            try {
                zzcd zzcd = (zzcd) this.zzvi.take();
                String zzdn = zzcd.zzdn();
                if (!TextUtils.isEmpty(zzdn)) {
                    this.zzvk.putAll(zzcd.zzn());
                    zzc(this.zzvk, zzdn);
                }
            } catch (InterruptedException e) {
                zzb.zzd("CsiReporter:reporter interrupted", e);
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public String zza(String str, Map<String, String> map, String str2) {
        Builder buildUpon = Uri.parse(str).buildUpon();
        for (Entry entry : map.entrySet()) {
            buildUpon.appendQueryParameter((String) entry.getKey(), (String) entry.getValue());
        }
        StringBuilder stringBuilder = new StringBuilder(buildUpon.build().toString());
        stringBuilder.append(MASTNativeAdConstants.AMPERSAND).append("it").append(MASTNativeAdConstants.EQUAL).append(str2);
        return stringBuilder.toString();
    }

    public boolean zza(zzcd zzcd) {
        return this.zzvi.offer(zzcd);
    }

    public void zzb(List<String> list) {
        if (list != null && !list.isEmpty()) {
            this.zzvk.put("e", TextUtils.join(",", list));
        }
    }
}
