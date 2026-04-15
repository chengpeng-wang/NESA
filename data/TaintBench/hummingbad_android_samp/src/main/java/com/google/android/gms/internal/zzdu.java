package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.util.client.zzb;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@zzgk
public class zzdu extends zzdr {
    private static final Set<String> zzxB = Collections.synchronizedSet(new HashSet());
    private static final DecimalFormat zzxC = new DecimalFormat("#,###");
    private File zzxD;
    private boolean zzxE;

    public zzdu(zzip zzip) {
        super(zzip);
        File cacheDir = zzip.getContext().getCacheDir();
        if (cacheDir == null) {
            zzb.zzaE("Context.getCacheDir() returned null");
            return;
        }
        this.zzxD = new File(cacheDir, "admobVideoStreams");
        if (!this.zzxD.isDirectory() && !this.zzxD.mkdirs()) {
            zzb.zzaE("Could not create preload cache directory at " + this.zzxD.getAbsolutePath());
            this.zzxD = null;
        } else if (!this.zzxD.setReadable(true, false) || !this.zzxD.setExecutable(true, false)) {
            zzb.zzaE("Could not set cache file permissions at " + this.zzxD.getAbsolutePath());
            this.zzxD = null;
        }
    }

    private File zza(File file) {
        return new File(this.zzxD, file.getName() + ".done");
    }

    private static void zzb(File file) {
        if (file.isFile()) {
            file.setLastModified(System.currentTimeMillis());
            return;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
    }

    public void abort() {
        this.zzxE = true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:85:0x030d  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0134  */
    /* JADX WARNING: Missing block: B:29:?, code skipped:
            r3 = new java.net.URL(r25).openConnection();
            r2 = ((java.lang.Integer) com.google.android.gms.internal.zzby.zzuo.get()).intValue();
            r3.setConnectTimeout(r2);
            r3.setReadTimeout(r2);
     */
    /* JADX WARNING: Missing block: B:30:0x00f6, code skipped:
            if ((r3 instanceof java.net.HttpURLConnection) == false) goto L_0x018b;
     */
    /* JADX WARNING: Missing block: B:31:0x00f8, code skipped:
            r2 = ((java.net.HttpURLConnection) r3).getResponseCode();
     */
    /* JADX WARNING: Missing block: B:32:0x0102, code skipped:
            if (r2 < com.picksinit.ErrorInfo.ERROR_CODE_NO_FILL) goto L_0x018b;
     */
    /* JADX WARNING: Missing block: B:34:0x0128, code skipped:
            throw new java.io.IOException("HTTP status code " + r2 + " at " + r25);
     */
    /* JADX WARNING: Missing block: B:35:0x0129, code skipped:
            r2 = e;
     */
    /* JADX WARNING: Missing block: B:36:0x012a, code skipped:
            r3 = null;
     */
    /* JADX WARNING: Missing block: B:38:?, code skipped:
            r3.close();
     */
    /* JADX WARNING: Missing block: B:40:0x0132, code skipped:
            if (r24.zzxE == false) goto L_0x030d;
     */
    /* JADX WARNING: Missing block: B:41:0x0134, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaD("Preload aborted for URL \"" + r25 + "\"");
     */
    /* JADX WARNING: Missing block: B:46:0x015e, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("Could not delete partial cache file at " + r9.getAbsolutePath());
     */
    /* JADX WARNING: Missing block: B:47:0x0178, code skipped:
            zzf(r25, r9.getAbsolutePath());
            zzxB.remove(r11);
     */
    /* JADX WARNING: Missing block: B:49:?, code skipped:
            r6 = r3.getContentLength();
     */
    /* JADX WARNING: Missing block: B:50:0x018f, code skipped:
            if (r6 >= 0) goto L_0x01bc;
     */
    /* JADX WARNING: Missing block: B:51:0x0191, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("Stream cache aborted, missing content-length header at " + r25);
            zzf(r25, r9.getAbsolutePath());
            zzxB.remove(r11);
     */
    /* JADX WARNING: Missing block: B:52:0x01bc, code skipped:
            r5 = zzxC.format((long) r6);
            r12 = ((java.lang.Integer) com.google.android.gms.internal.zzby.zzuk.get()).intValue();
     */
    /* JADX WARNING: Missing block: B:53:0x01cf, code skipped:
            if (r6 <= r12) goto L_0x0206;
     */
    /* JADX WARNING: Missing block: B:54:0x01d1, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaE("Content length " + r5 + " exceeds limit at " + r25);
            zzf(r25, r9.getAbsolutePath());
            zzxB.remove(r11);
     */
    /* JADX WARNING: Missing block: B:55:0x0206, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Caching " + r5 + " bytes from " + r25);
            r13 = java.nio.channels.Channels.newChannel(r3.getInputStream());
            r8 = new java.io.FileOutputStream(r9);
     */
    /* JADX WARNING: Missing block: B:57:?, code skipped:
            r14 = r8.getChannel();
            r15 = java.nio.ByteBuffer.allocate(android.support.v4.view.accessibility.AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
            r16 = com.google.android.gms.ads.internal.zzp.zzbB();
            r5 = 0;
            r17 = r16.currentTimeMillis();
            r0 = new com.google.android.gms.internal.zzib(((java.lang.Long) com.google.android.gms.internal.zzby.zzun.get()).longValue());
            r20 = ((java.lang.Long) com.google.android.gms.internal.zzby.zzum.get()).longValue();
     */
    /* JADX WARNING: Missing block: B:58:0x0267, code skipped:
            r2 = r13.read(r15);
     */
    /* JADX WARNING: Missing block: B:59:0x026b, code skipped:
            if (r2 < 0) goto L_0x02bf;
     */
    /* JADX WARNING: Missing block: B:60:0x026d, code skipped:
            r5 = r5 + r2;
     */
    /* JADX WARNING: Missing block: B:61:0x026e, code skipped:
            if (r5 <= r12) goto L_0x027c;
     */
    /* JADX WARNING: Missing block: B:63:0x0277, code skipped:
            throw new java.io.IOException("stream cache file size limit exceeded");
     */
    /* JADX WARNING: Missing block: B:64:0x0278, code skipped:
            r2 = e;
     */
    /* JADX WARNING: Missing block: B:65:0x0279, code skipped:
            r3 = r8;
     */
    /* JADX WARNING: Missing block: B:66:0x027c, code skipped:
            r15.flip();
     */
    /* JADX WARNING: Missing block: B:68:0x0283, code skipped:
            if (r14.write(r15) > 0) goto L_0x027f;
     */
    /* JADX WARNING: Missing block: B:69:0x0285, code skipped:
            r15.clear();
     */
    /* JADX WARNING: Missing block: B:70:0x0294, code skipped:
            if ((r16.currentTimeMillis() - r17) <= (1000 * r20)) goto L_0x029e;
     */
    /* JADX WARNING: Missing block: B:72:0x029d, code skipped:
            throw new java.io.IOException("stream cache time limit exceeded");
     */
    /* JADX WARNING: Missing block: B:74:0x02a2, code skipped:
            if (r24.zzxE == false) goto L_0x02ac;
     */
    /* JADX WARNING: Missing block: B:76:0x02ab, code skipped:
            throw new java.io.IOException("abort requested");
     */
    /* JADX WARNING: Missing block: B:78:0x02b0, code skipped:
            if (r0.tryAcquire() == false) goto L_0x0267;
     */
    /* JADX WARNING: Missing block: B:79:0x02b2, code skipped:
            zza(r25, r9.getAbsolutePath(), r5, r6, false);
     */
    /* JADX WARNING: Missing block: B:80:0x02bf, code skipped:
            r8.close();
     */
    /* JADX WARNING: Missing block: B:81:0x02c7, code skipped:
            if (com.google.android.gms.ads.internal.util.client.zzb.zzM(3) == false) goto L_0x02f2;
     */
    /* JADX WARNING: Missing block: B:82:0x02c9, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzaC("Preloaded " + zzxC.format((long) r5) + " bytes from " + r25);
     */
    /* JADX WARNING: Missing block: B:83:0x02f2, code skipped:
            r9.setReadable(true, false);
            zzb(r10);
            zza(r25, r9.getAbsolutePath(), r5);
            zzxB.remove(r11);
     */
    /* JADX WARNING: Missing block: B:85:0x030d, code skipped:
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Preload failed for URL \"" + r25 + "\"", r2);
     */
    /* JADX WARNING: Missing block: B:101:?, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:102:?, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:103:?, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:104:?, code skipped:
            return true;
     */
    public boolean zzZ(java.lang.String r25) {
        /*
        r24 = this;
        r0 = r24;
        r2 = r0.zzxD;
        if (r2 != 0) goto L_0x0010;
    L_0x0006:
        r2 = 0;
        r0 = r24;
        r1 = r25;
        r0.zzf(r1, r2);
        r2 = 0;
    L_0x000f:
        return r2;
    L_0x0010:
        r3 = r24.zzdH();
        r2 = com.google.android.gms.internal.zzby.zzuj;
        r2 = r2.get();
        r2 = (java.lang.Integer) r2;
        r2 = r2.intValue();
        if (r3 <= r2) goto L_0x0037;
    L_0x0022:
        r2 = r24.zzdI();
        if (r2 != 0) goto L_0x0010;
    L_0x0028:
        r2 = "Unable to expire stream cache";
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r2);
        r2 = 0;
        r0 = r24;
        r1 = r25;
        r0.zzf(r1, r2);
        r2 = 0;
        goto L_0x000f;
    L_0x0037:
        r2 = r24.zzaa(r25);
        r9 = new java.io.File;
        r0 = r24;
        r3 = r0.zzxD;
        r9.<init>(r3, r2);
        r0 = r24;
        r10 = r0.zza(r9);
        r2 = r9.isFile();
        if (r2 == 0) goto L_0x0080;
    L_0x0050:
        r2 = r10.isFile();
        if (r2 == 0) goto L_0x0080;
    L_0x0056:
        r2 = r9.length();
        r2 = (int) r2;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Stream cache hit at ";
        r3 = r3.append(r4);
        r0 = r25;
        r3 = r3.append(r0);
        r3 = r3.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaC(r3);
        r3 = r9.getAbsolutePath();
        r0 = r24;
        r1 = r25;
        r0.zza(r1, r3, r2);
        r2 = 1;
        goto L_0x000f;
    L_0x0080:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = r24;
        r3 = r0.zzxD;
        r3 = r3.getAbsolutePath();
        r2 = r2.append(r3);
        r0 = r25;
        r2 = r2.append(r0);
        r11 = r2.toString();
        r3 = zzxB;
        monitor-enter(r3);
        r2 = zzxB;	 Catch:{ all -> 0x00cd }
        r2 = r2.contains(r11);	 Catch:{ all -> 0x00cd }
        if (r2 == 0) goto L_0x00d0;
    L_0x00a6:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cd }
        r2.<init>();	 Catch:{ all -> 0x00cd }
        r4 = "Stream cache already in progress at ";
        r2 = r2.append(r4);	 Catch:{ all -> 0x00cd }
        r0 = r25;
        r2 = r2.append(r0);	 Catch:{ all -> 0x00cd }
        r2 = r2.toString();	 Catch:{ all -> 0x00cd }
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r2);	 Catch:{ all -> 0x00cd }
        r2 = r9.getAbsolutePath();	 Catch:{ all -> 0x00cd }
        r0 = r24;
        r1 = r25;
        r0.zzf(r1, r2);	 Catch:{ all -> 0x00cd }
        r2 = 0;
        monitor-exit(r3);	 Catch:{ all -> 0x00cd }
        goto L_0x000f;
    L_0x00cd:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x00cd }
        throw r2;
    L_0x00d0:
        r2 = zzxB;	 Catch:{ all -> 0x00cd }
        r2.add(r11);	 Catch:{ all -> 0x00cd }
        monitor-exit(r3);	 Catch:{ all -> 0x00cd }
        r4 = 0;
        r2 = new java.net.URL;	 Catch:{ IOException -> 0x0129 }
        r0 = r25;
        r2.<init>(r0);	 Catch:{ IOException -> 0x0129 }
        r3 = r2.openConnection();	 Catch:{ IOException -> 0x0129 }
        r2 = com.google.android.gms.internal.zzby.zzuo;	 Catch:{ IOException -> 0x0129 }
        r2 = r2.get();	 Catch:{ IOException -> 0x0129 }
        r2 = (java.lang.Integer) r2;	 Catch:{ IOException -> 0x0129 }
        r2 = r2.intValue();	 Catch:{ IOException -> 0x0129 }
        r3.setConnectTimeout(r2);	 Catch:{ IOException -> 0x0129 }
        r3.setReadTimeout(r2);	 Catch:{ IOException -> 0x0129 }
        r2 = r3 instanceof java.net.HttpURLConnection;	 Catch:{ IOException -> 0x0129 }
        if (r2 == 0) goto L_0x018b;
    L_0x00f8:
        r0 = r3;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ IOException -> 0x0129 }
        r2 = r0;
        r2 = r2.getResponseCode();	 Catch:{ IOException -> 0x0129 }
        r5 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
        if (r2 < r5) goto L_0x018b;
    L_0x0104:
        r3 = new java.io.IOException;	 Catch:{ IOException -> 0x0129 }
        r5 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0129 }
        r5.<init>();	 Catch:{ IOException -> 0x0129 }
        r6 = "HTTP status code ";
        r5 = r5.append(r6);	 Catch:{ IOException -> 0x0129 }
        r2 = r5.append(r2);	 Catch:{ IOException -> 0x0129 }
        r5 = " at ";
        r2 = r2.append(r5);	 Catch:{ IOException -> 0x0129 }
        r0 = r25;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x0129 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0129 }
        r3.<init>(r2);	 Catch:{ IOException -> 0x0129 }
        throw r3;	 Catch:{ IOException -> 0x0129 }
    L_0x0129:
        r2 = move-exception;
        r3 = r4;
    L_0x012b:
        r3.close();	 Catch:{ IOException | NullPointerException -> 0x032d, IOException | NullPointerException -> 0x032d }
    L_0x012e:
        r0 = r24;
        r3 = r0.zzxE;
        if (r3 == 0) goto L_0x030d;
    L_0x0134:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Preload aborted for URL \"";
        r2 = r2.append(r3);
        r0 = r25;
        r2 = r2.append(r0);
        r3 = "\"";
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaD(r2);
    L_0x0152:
        r2 = r9.exists();
        if (r2 == 0) goto L_0x0178;
    L_0x0158:
        r2 = r9.delete();
        if (r2 != 0) goto L_0x0178;
    L_0x015e:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Could not delete partial cache file at ";
        r2 = r2.append(r3);
        r3 = r9.getAbsolutePath();
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r2);
    L_0x0178:
        r2 = r9.getAbsolutePath();
        r0 = r24;
        r1 = r25;
        r0.zzf(r1, r2);
        r2 = zzxB;
        r2.remove(r11);
        r2 = 0;
        goto L_0x000f;
    L_0x018b:
        r6 = r3.getContentLength();	 Catch:{ IOException -> 0x0129 }
        if (r6 >= 0) goto L_0x01bc;
    L_0x0191:
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0129 }
        r2.<init>();	 Catch:{ IOException -> 0x0129 }
        r3 = "Stream cache aborted, missing content-length header at ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x0129 }
        r0 = r25;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x0129 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0129 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r2);	 Catch:{ IOException -> 0x0129 }
        r2 = r9.getAbsolutePath();	 Catch:{ IOException -> 0x0129 }
        r0 = r24;
        r1 = r25;
        r0.zzf(r1, r2);	 Catch:{ IOException -> 0x0129 }
        r2 = zzxB;	 Catch:{ IOException -> 0x0129 }
        r2.remove(r11);	 Catch:{ IOException -> 0x0129 }
        r2 = 0;
        goto L_0x000f;
    L_0x01bc:
        r2 = zzxC;	 Catch:{ IOException -> 0x0129 }
        r7 = (long) r6;	 Catch:{ IOException -> 0x0129 }
        r5 = r2.format(r7);	 Catch:{ IOException -> 0x0129 }
        r2 = com.google.android.gms.internal.zzby.zzuk;	 Catch:{ IOException -> 0x0129 }
        r2 = r2.get();	 Catch:{ IOException -> 0x0129 }
        r2 = (java.lang.Integer) r2;	 Catch:{ IOException -> 0x0129 }
        r12 = r2.intValue();	 Catch:{ IOException -> 0x0129 }
        if (r6 <= r12) goto L_0x0206;
    L_0x01d1:
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0129 }
        r2.<init>();	 Catch:{ IOException -> 0x0129 }
        r3 = "Content length ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x0129 }
        r2 = r2.append(r5);	 Catch:{ IOException -> 0x0129 }
        r3 = " exceeds limit at ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x0129 }
        r0 = r25;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x0129 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0129 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaE(r2);	 Catch:{ IOException -> 0x0129 }
        r2 = r9.getAbsolutePath();	 Catch:{ IOException -> 0x0129 }
        r0 = r24;
        r1 = r25;
        r0.zzf(r1, r2);	 Catch:{ IOException -> 0x0129 }
        r2 = zzxB;	 Catch:{ IOException -> 0x0129 }
        r2.remove(r11);	 Catch:{ IOException -> 0x0129 }
        r2 = 0;
        goto L_0x000f;
    L_0x0206:
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0129 }
        r2.<init>();	 Catch:{ IOException -> 0x0129 }
        r7 = "Caching ";
        r2 = r2.append(r7);	 Catch:{ IOException -> 0x0129 }
        r2 = r2.append(r5);	 Catch:{ IOException -> 0x0129 }
        r5 = " bytes from ";
        r2 = r2.append(r5);	 Catch:{ IOException -> 0x0129 }
        r0 = r25;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x0129 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0129 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaC(r2);	 Catch:{ IOException -> 0x0129 }
        r2 = r3.getInputStream();	 Catch:{ IOException -> 0x0129 }
        r13 = java.nio.channels.Channels.newChannel(r2);	 Catch:{ IOException -> 0x0129 }
        r8 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0129 }
        r8.<init>(r9);	 Catch:{ IOException -> 0x0129 }
        r14 = r8.getChannel();	 Catch:{ IOException -> 0x0278 }
        r2 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r15 = java.nio.ByteBuffer.allocate(r2);	 Catch:{ IOException -> 0x0278 }
        r16 = com.google.android.gms.ads.internal.zzp.zzbB();	 Catch:{ IOException -> 0x0278 }
        r5 = 0;
        r17 = r16.currentTimeMillis();	 Catch:{ IOException -> 0x0278 }
        r2 = com.google.android.gms.internal.zzby.zzun;	 Catch:{ IOException -> 0x0278 }
        r2 = r2.get();	 Catch:{ IOException -> 0x0278 }
        r2 = (java.lang.Long) r2;	 Catch:{ IOException -> 0x0278 }
        r2 = r2.longValue();	 Catch:{ IOException -> 0x0278 }
        r19 = new com.google.android.gms.internal.zzib;	 Catch:{ IOException -> 0x0278 }
        r0 = r19;
        r0.m1623init(r2);	 Catch:{ IOException -> 0x0278 }
        r2 = com.google.android.gms.internal.zzby.zzum;	 Catch:{ IOException -> 0x0278 }
        r2 = r2.get();	 Catch:{ IOException -> 0x0278 }
        r2 = (java.lang.Long) r2;	 Catch:{ IOException -> 0x0278 }
        r20 = r2.longValue();	 Catch:{ IOException -> 0x0278 }
    L_0x0267:
        r2 = r13.read(r15);	 Catch:{ IOException -> 0x0278 }
        if (r2 < 0) goto L_0x02bf;
    L_0x026d:
        r5 = r5 + r2;
        if (r5 <= r12) goto L_0x027c;
    L_0x0270:
        r2 = new java.io.IOException;	 Catch:{ IOException -> 0x0278 }
        r3 = "stream cache file size limit exceeded";
        r2.<init>(r3);	 Catch:{ IOException -> 0x0278 }
        throw r2;	 Catch:{ IOException -> 0x0278 }
    L_0x0278:
        r2 = move-exception;
        r3 = r8;
        goto L_0x012b;
    L_0x027c:
        r15.flip();	 Catch:{ IOException -> 0x0278 }
    L_0x027f:
        r2 = r14.write(r15);	 Catch:{ IOException -> 0x0278 }
        if (r2 > 0) goto L_0x027f;
    L_0x0285:
        r15.clear();	 Catch:{ IOException -> 0x0278 }
        r2 = r16.currentTimeMillis();	 Catch:{ IOException -> 0x0278 }
        r2 = r2 - r17;
        r22 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r22 = r22 * r20;
        r2 = (r2 > r22 ? 1 : (r2 == r22 ? 0 : -1));
        if (r2 <= 0) goto L_0x029e;
    L_0x0296:
        r2 = new java.io.IOException;	 Catch:{ IOException -> 0x0278 }
        r3 = "stream cache time limit exceeded";
        r2.<init>(r3);	 Catch:{ IOException -> 0x0278 }
        throw r2;	 Catch:{ IOException -> 0x0278 }
    L_0x029e:
        r0 = r24;
        r2 = r0.zzxE;	 Catch:{ IOException -> 0x0278 }
        if (r2 == 0) goto L_0x02ac;
    L_0x02a4:
        r2 = new java.io.IOException;	 Catch:{ IOException -> 0x0278 }
        r3 = "abort requested";
        r2.<init>(r3);	 Catch:{ IOException -> 0x0278 }
        throw r2;	 Catch:{ IOException -> 0x0278 }
    L_0x02ac:
        r2 = r19.tryAcquire();	 Catch:{ IOException -> 0x0278 }
        if (r2 == 0) goto L_0x0267;
    L_0x02b2:
        r4 = r9.getAbsolutePath();	 Catch:{ IOException -> 0x0278 }
        r7 = 0;
        r2 = r24;
        r3 = r25;
        r2.zza(r3, r4, r5, r6, r7);	 Catch:{ IOException -> 0x0278 }
        goto L_0x0267;
    L_0x02bf:
        r8.close();	 Catch:{ IOException -> 0x0278 }
        r2 = 3;
        r2 = com.google.android.gms.ads.internal.util.client.zzb.zzM(r2);	 Catch:{ IOException -> 0x0278 }
        if (r2 == 0) goto L_0x02f2;
    L_0x02c9:
        r2 = zzxC;	 Catch:{ IOException -> 0x0278 }
        r3 = (long) r5;	 Catch:{ IOException -> 0x0278 }
        r2 = r2.format(r3);	 Catch:{ IOException -> 0x0278 }
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0278 }
        r3.<init>();	 Catch:{ IOException -> 0x0278 }
        r4 = "Preloaded ";
        r3 = r3.append(r4);	 Catch:{ IOException -> 0x0278 }
        r2 = r3.append(r2);	 Catch:{ IOException -> 0x0278 }
        r3 = " bytes from ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x0278 }
        r0 = r25;
        r2 = r2.append(r0);	 Catch:{ IOException -> 0x0278 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0278 }
        com.google.android.gms.ads.internal.util.client.zzb.zzaC(r2);	 Catch:{ IOException -> 0x0278 }
    L_0x02f2:
        r2 = 1;
        r3 = 0;
        r9.setReadable(r2, r3);	 Catch:{ IOException -> 0x0278 }
        zzb(r10);	 Catch:{ IOException -> 0x0278 }
        r2 = r9.getAbsolutePath();	 Catch:{ IOException -> 0x0278 }
        r0 = r24;
        r1 = r25;
        r0.zza(r1, r2, r5);	 Catch:{ IOException -> 0x0278 }
        r2 = zzxB;	 Catch:{ IOException -> 0x0278 }
        r2.remove(r11);	 Catch:{ IOException -> 0x0278 }
        r2 = 1;
        goto L_0x000f;
    L_0x030d:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Preload failed for URL \"";
        r3 = r3.append(r4);
        r0 = r25;
        r3 = r3.append(r0);
        r4 = "\"";
        r3 = r3.append(r4);
        r3 = r3.toString();
        com.google.android.gms.ads.internal.util.client.zzb.zzd(r3, r2);
        goto L_0x0152;
    L_0x032d:
        r3 = move-exception;
        goto L_0x012e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdu.zzZ(java.lang.String):boolean");
    }

    public int zzdH() {
        int i = 0;
        if (this.zzxD != null) {
            for (File name : this.zzxD.listFiles()) {
                if (!name.getName().endsWith(".done")) {
                    i++;
                }
            }
        }
        return i;
    }

    public boolean zzdI() {
        if (this.zzxD == null) {
            return false;
        }
        boolean delete;
        File file = null;
        long j = Long.MAX_VALUE;
        File[] listFiles = this.zzxD.listFiles();
        int length = listFiles.length;
        int i = 0;
        while (i < length) {
            long lastModified;
            File file2;
            File file3 = listFiles[i];
            if (!file3.getName().endsWith(".done")) {
                lastModified = file3.lastModified();
                if (lastModified < j) {
                    file2 = file3;
                    i++;
                    file = file2;
                    j = lastModified;
                }
            }
            lastModified = j;
            file2 = file;
            i++;
            file = file2;
            j = lastModified;
        }
        if (file != null) {
            delete = file.delete();
            File zza = zza(file);
            if (zza.isFile()) {
                delete &= zza.delete();
            }
        } else {
            delete = false;
        }
        return delete;
    }
}
