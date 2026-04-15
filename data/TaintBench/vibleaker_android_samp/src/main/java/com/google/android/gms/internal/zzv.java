package com.google.android.gms.internal;

import android.os.SystemClock;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class zzv implements zzb {
    private final int zzaA;
    private final Map<String, zza> zzax;
    private long zzay;
    private final File zzaz;

    static class zza {
        public String key;
        public long zzaB;
        public String zzb;
        public long zzc;
        public long zzd;
        public long zze;
        public long zzf;
        public Map<String, String> zzg;

        private zza() {
        }

        public zza(String str, com.google.android.gms.internal.zzb.zza zza) {
            this.key = str;
            this.zzaB = (long) zza.data.length;
            this.zzb = zza.zzb;
            this.zzc = zza.zzc;
            this.zzd = zza.zzd;
            this.zze = zza.zze;
            this.zzf = zza.zzf;
            this.zzg = zza.zzg;
        }

        public static zza zzf(InputStream inputStream) throws IOException {
            zza zza = new zza();
            if (zzv.zzb(inputStream) != 538247942) {
                throw new IOException();
            }
            zza.key = zzv.zzd(inputStream);
            zza.zzb = zzv.zzd(inputStream);
            if (zza.zzb.equals("")) {
                zza.zzb = null;
            }
            zza.zzc = zzv.zzc(inputStream);
            zza.zzd = zzv.zzc(inputStream);
            zza.zze = zzv.zzc(inputStream);
            zza.zzf = zzv.zzc(inputStream);
            zza.zzg = zzv.zze(inputStream);
            return zza;
        }

        public boolean zza(OutputStream outputStream) {
            try {
                zzv.zza(outputStream, 538247942);
                zzv.zza(outputStream, this.key);
                zzv.zza(outputStream, this.zzb == null ? "" : this.zzb);
                zzv.zza(outputStream, this.zzc);
                zzv.zza(outputStream, this.zzd);
                zzv.zza(outputStream, this.zze);
                zzv.zza(outputStream, this.zzf);
                zzv.zza(this.zzg, outputStream);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                zzs.zzb("%s", e.toString());
                return false;
            }
        }

        public com.google.android.gms.internal.zzb.zza zzb(byte[] bArr) {
            com.google.android.gms.internal.zzb.zza zza = new com.google.android.gms.internal.zzb.zza();
            zza.data = bArr;
            zza.zzb = this.zzb;
            zza.zzc = this.zzc;
            zza.zzd = this.zzd;
            zza.zze = this.zze;
            zza.zzf = this.zzf;
            zza.zzg = this.zzg;
            return zza;
        }
    }

    private static class zzb extends FilterInputStream {
        /* access modifiers changed from: private */
        public int zzaC;

        private zzb(InputStream inputStream) {
            super(inputStream);
            this.zzaC = 0;
        }

        public int read() throws IOException {
            int read = super.read();
            if (read != -1) {
                this.zzaC++;
            }
            return read;
        }

        public int read(byte[] buffer, int offset, int count) throws IOException {
            int read = super.read(buffer, offset, count);
            if (read != -1) {
                this.zzaC += read;
            }
            return read;
        }
    }

    public zzv(File file) {
        this(file, 5242880);
    }

    public zzv(File file, int i) {
        this.zzax = new LinkedHashMap(16, 0.75f, true);
        this.zzay = 0;
        this.zzaz = file;
        this.zzaA = i;
    }

    private void removeEntry(String key) {
        zza zza = (zza) this.zzax.get(key);
        if (zza != null) {
            this.zzay -= zza.zzaB;
            this.zzax.remove(key);
        }
    }

    private static int zza(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    static void zza(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 0) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 24) & 255);
    }

    static void zza(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) ((int) (j >>> null)));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 56)));
    }

    static void zza(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        zza(outputStream, (long) bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    private void zza(String str, zza zza) {
        if (this.zzax.containsKey(str)) {
            zza zza2 = (zza) this.zzax.get(str);
            this.zzay = (zza.zzaB - zza2.zzaB) + this.zzay;
        } else {
            this.zzay += zza.zzaB;
        }
        this.zzax.put(str, zza);
    }

    static void zza(Map<String, String> map, OutputStream outputStream) throws IOException {
        if (map != null) {
            zza(outputStream, map.size());
            for (Entry entry : map.entrySet()) {
                zza(outputStream, (String) entry.getKey());
                zza(outputStream, (String) entry.getValue());
            }
            return;
        }
        zza(outputStream, 0);
    }

    private static byte[] zza(InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read == -1) {
                break;
            }
            i2 += read;
        }
        if (i2 == i) {
            return bArr;
        }
        throw new IOException("Expected " + i + " bytes, read " + i2 + " bytes");
    }

    static int zzb(InputStream inputStream) throws IOException {
        return (((0 | (zza(inputStream) << 0)) | (zza(inputStream) << 8)) | (zza(inputStream) << 16)) | (zza(inputStream) << 24);
    }

    static long zzc(InputStream inputStream) throws IOException {
        return (((((((0 | ((((long) zza(inputStream)) & 255) << null)) | ((((long) zza(inputStream)) & 255) << 8)) | ((((long) zza(inputStream)) & 255) << 16)) | ((((long) zza(inputStream)) & 255) << 24)) | ((((long) zza(inputStream)) & 255) << 32)) | ((((long) zza(inputStream)) & 255) << 40)) | ((((long) zza(inputStream)) & 255) << 48)) | ((((long) zza(inputStream)) & 255) << 56);
    }

    private void zzc(int i) {
        if (this.zzay + ((long) i) >= ((long) this.zzaA)) {
            int i2;
            if (zzs.DEBUG) {
                zzs.zza("Pruning old cache entries.", new Object[0]);
            }
            long j = this.zzay;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Iterator it = this.zzax.entrySet().iterator();
            int i3 = 0;
            while (it.hasNext()) {
                zza zza = (zza) ((Entry) it.next()).getValue();
                if (zzf(zza.key).delete()) {
                    this.zzay -= zza.zzaB;
                } else {
                    zzs.zzb("Could not delete cache entry for key=%s, filename=%s", zza.key, zze(zza.key));
                }
                it.remove();
                i2 = i3 + 1;
                if (((float) (this.zzay + ((long) i))) < ((float) this.zzaA) * 0.9f) {
                    break;
                }
                i3 = i2;
            }
            i2 = i3;
            if (zzs.DEBUG) {
                zzs.zza("pruned %d files, %d bytes, %d ms", Integer.valueOf(i2), Long.valueOf(this.zzay - j), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
            }
        }
    }

    static String zzd(InputStream inputStream) throws IOException {
        return new String(zza(inputStream, (int) zzc(inputStream)), "UTF-8");
    }

    private String zze(String str) {
        int length = str.length() / 2;
        return String.valueOf(str.substring(0, length).hashCode()) + String.valueOf(str.substring(length).hashCode());
    }

    static Map<String, String> zze(InputStream inputStream) throws IOException {
        int zzb = zzb(inputStream);
        Map<String, String> emptyMap = zzb == 0 ? Collections.emptyMap() : new HashMap(zzb);
        for (int i = 0; i < zzb; i++) {
            emptyMap.put(zzd(inputStream).intern(), zzd(inputStream).intern());
        }
        return emptyMap;
    }

    public synchronized void remove(String key) {
        boolean delete = zzf(key).delete();
        removeEntry(key);
        if (!delete) {
            zzs.zzb("Could not delete cache entry for key=%s, filename=%s", key, zze(key));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0066 A:{SYNTHETIC, Splitter:B:32:0x0066} */
    public synchronized com.google.android.gms.internal.zzb.zza zza(java.lang.String r9) {
        /*
        r8 = this;
        r1 = 0;
        monitor-enter(r8);
        r0 = r8.zzax;	 Catch:{ all -> 0x006a }
        r0 = r0.get(r9);	 Catch:{ all -> 0x006a }
        r0 = (com.google.android.gms.internal.zzv.zza) r0;	 Catch:{ all -> 0x006a }
        if (r0 != 0) goto L_0x000f;
    L_0x000c:
        r0 = r1;
    L_0x000d:
        monitor-exit(r8);
        return r0;
    L_0x000f:
        r3 = r8.zzf(r9);	 Catch:{ all -> 0x006a }
        r2 = new com.google.android.gms.internal.zzv$zzb;	 Catch:{ IOException -> 0x003d, all -> 0x0062 }
        r4 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x003d, all -> 0x0062 }
        r4.<init>(r3);	 Catch:{ IOException -> 0x003d, all -> 0x0062 }
        r5 = 0;
        r2.m3533init(r4);	 Catch:{ IOException -> 0x003d, all -> 0x0062 }
        com.google.android.gms.internal.zzv.zza.zzf(r2);	 Catch:{ IOException -> 0x0072 }
        r4 = r3.length();	 Catch:{ IOException -> 0x0072 }
        r6 = r2.zzaC;	 Catch:{ IOException -> 0x0072 }
        r6 = (long) r6;	 Catch:{ IOException -> 0x0072 }
        r4 = r4 - r6;
        r4 = (int) r4;	 Catch:{ IOException -> 0x0072 }
        r4 = zza(r2, r4);	 Catch:{ IOException -> 0x0072 }
        r0 = r0.zzb(r4);	 Catch:{ IOException -> 0x0072 }
        if (r2 == 0) goto L_0x000d;
    L_0x0036:
        r2.close();	 Catch:{ IOException -> 0x003a }
        goto L_0x000d;
    L_0x003a:
        r0 = move-exception;
        r0 = r1;
        goto L_0x000d;
    L_0x003d:
        r0 = move-exception;
        r2 = r1;
    L_0x003f:
        r4 = "%s: %s";
        r5 = 2;
        r5 = new java.lang.Object[r5];	 Catch:{ all -> 0x0070 }
        r6 = 0;
        r3 = r3.getAbsolutePath();	 Catch:{ all -> 0x0070 }
        r5[r6] = r3;	 Catch:{ all -> 0x0070 }
        r3 = 1;
        r0 = r0.toString();	 Catch:{ all -> 0x0070 }
        r5[r3] = r0;	 Catch:{ all -> 0x0070 }
        com.google.android.gms.internal.zzs.zzb(r4, r5);	 Catch:{ all -> 0x0070 }
        r8.remove(r9);	 Catch:{ all -> 0x0070 }
        if (r2 == 0) goto L_0x005d;
    L_0x005a:
        r2.close();	 Catch:{ IOException -> 0x005f }
    L_0x005d:
        r0 = r1;
        goto L_0x000d;
    L_0x005f:
        r0 = move-exception;
        r0 = r1;
        goto L_0x000d;
    L_0x0062:
        r0 = move-exception;
        r2 = r1;
    L_0x0064:
        if (r2 == 0) goto L_0x0069;
    L_0x0066:
        r2.close();	 Catch:{ IOException -> 0x006d }
    L_0x0069:
        throw r0;	 Catch:{ all -> 0x006a }
    L_0x006a:
        r0 = move-exception;
        monitor-exit(r8);
        throw r0;
    L_0x006d:
        r0 = move-exception;
        r0 = r1;
        goto L_0x000d;
    L_0x0070:
        r0 = move-exception;
        goto L_0x0064;
    L_0x0072:
        r0 = move-exception;
        goto L_0x003f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzv.zza(java.lang.String):com.google.android.gms.internal.zzb$zza");
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x005a A:{SYNTHETIC, Splitter:B:28:0x005a} */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0052 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x005f A:{SYNTHETIC, Splitter:B:31:0x005f} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0068 A:{SYNTHETIC, Splitter:B:36:0x0068} */
    public synchronized void zza() {
        /*
        r9 = this;
        r0 = 0;
        monitor-enter(r9);
        r1 = r9.zzaz;	 Catch:{ all -> 0x006c }
        r1 = r1.exists();	 Catch:{ all -> 0x006c }
        if (r1 != 0) goto L_0x0025;
    L_0x000a:
        r0 = r9.zzaz;	 Catch:{ all -> 0x006c }
        r0 = r0.mkdirs();	 Catch:{ all -> 0x006c }
        if (r0 != 0) goto L_0x0023;
    L_0x0012:
        r0 = "Unable to create cache dir %s";
        r1 = 1;
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x006c }
        r2 = 0;
        r3 = r9.zzaz;	 Catch:{ all -> 0x006c }
        r3 = r3.getAbsolutePath();	 Catch:{ all -> 0x006c }
        r1[r2] = r3;	 Catch:{ all -> 0x006c }
        com.google.android.gms.internal.zzs.zzc(r0, r1);	 Catch:{ all -> 0x006c }
    L_0x0023:
        monitor-exit(r9);
        return;
    L_0x0025:
        r1 = r9.zzaz;	 Catch:{ all -> 0x006c }
        r3 = r1.listFiles();	 Catch:{ all -> 0x006c }
        if (r3 == 0) goto L_0x0023;
    L_0x002d:
        r4 = r3.length;	 Catch:{ all -> 0x006c }
        r2 = r0;
    L_0x002f:
        if (r2 >= r4) goto L_0x0023;
    L_0x0031:
        r5 = r3[r2];	 Catch:{ all -> 0x006c }
        r1 = 0;
        r0 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x0056, all -> 0x0065 }
        r6 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0056, all -> 0x0065 }
        r6.<init>(r5);	 Catch:{ IOException -> 0x0056, all -> 0x0065 }
        r0.<init>(r6);	 Catch:{ IOException -> 0x0056, all -> 0x0065 }
        r1 = com.google.android.gms.internal.zzv.zza.zzf(r0);	 Catch:{ IOException -> 0x0078 }
        r6 = r5.length();	 Catch:{ IOException -> 0x0078 }
        r1.zzaB = r6;	 Catch:{ IOException -> 0x0078 }
        r6 = r1.key;	 Catch:{ IOException -> 0x0078 }
        r9.zza(r6, r1);	 Catch:{ IOException -> 0x0078 }
        if (r0 == 0) goto L_0x0052;
    L_0x004f:
        r0.close();	 Catch:{ IOException -> 0x006f }
    L_0x0052:
        r0 = r2 + 1;
        r2 = r0;
        goto L_0x002f;
    L_0x0056:
        r0 = move-exception;
        r0 = r1;
    L_0x0058:
        if (r5 == 0) goto L_0x005d;
    L_0x005a:
        r5.delete();	 Catch:{ all -> 0x0073 }
    L_0x005d:
        if (r0 == 0) goto L_0x0052;
    L_0x005f:
        r0.close();	 Catch:{ IOException -> 0x0063 }
        goto L_0x0052;
    L_0x0063:
        r0 = move-exception;
        goto L_0x0052;
    L_0x0065:
        r0 = move-exception;
    L_0x0066:
        if (r1 == 0) goto L_0x006b;
    L_0x0068:
        r1.close();	 Catch:{ IOException -> 0x0071 }
    L_0x006b:
        throw r0;	 Catch:{ all -> 0x006c }
    L_0x006c:
        r0 = move-exception;
        monitor-exit(r9);
        throw r0;
    L_0x006f:
        r0 = move-exception;
        goto L_0x0052;
    L_0x0071:
        r1 = move-exception;
        goto L_0x006b;
    L_0x0073:
        r1 = move-exception;
        r8 = r1;
        r1 = r0;
        r0 = r8;
        goto L_0x0066;
    L_0x0078:
        r1 = move-exception;
        goto L_0x0058;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzv.zza():void");
    }

    public synchronized void zza(String str, com.google.android.gms.internal.zzb.zza zza) {
        zzc(zza.data.length);
        File zzf = zzf(str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zzf);
            zza zza2 = new zza(str, zza);
            if (zza2.zza(fileOutputStream)) {
                fileOutputStream.write(zza.data);
                fileOutputStream.close();
                zza(str, zza2);
            } else {
                fileOutputStream.close();
                zzs.zzb("Failed to write header for %s", zzf.getAbsolutePath());
                throw new IOException();
            }
        } catch (IOException e) {
            if (!zzf.delete()) {
                zzs.zzb("Could not clean up file %s", zzf.getAbsolutePath());
            }
        }
    }

    public File zzf(String str) {
        return new File(this.zzaz, zze(str));
    }
}
