package com.mopub.volley.toolbox;

import android.os.SystemClock;
import com.moceanmobile.mast.Defaults;
import com.mopub.volley.Cache;
import com.mopub.volley.Cache.Entry;
import com.mopub.volley.VolleyLog;
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

public class DiskBasedCache implements Cache {
    private static final int CACHE_MAGIC = 538183203;
    private static final int DEFAULT_DISK_USAGE_BYTES = 5242880;
    private static final float HYSTERESIS_FACTOR = 0.9f;
    private final Map<String, CacheHeader> mEntries;
    private final int mMaxCacheSizeInBytes;
    private final File mRootDirectory;
    private long mTotalSize;

    static class CacheHeader {
        public String etag;
        public String key;
        public Map<String, String> responseHeaders;
        public long serverDate;
        public long size;
        public long softTtl;
        public long ttl;

        private CacheHeader() {
        }

        public CacheHeader(String key, Entry entry) {
            this.key = key;
            this.size = (long) entry.data.length;
            this.etag = entry.etag;
            this.serverDate = entry.serverDate;
            this.ttl = entry.ttl;
            this.softTtl = entry.softTtl;
            this.responseHeaders = entry.responseHeaders;
        }

        public static CacheHeader readHeader(InputStream is) throws IOException {
            CacheHeader cacheHeader = new CacheHeader();
            if (DiskBasedCache.readInt(is) != DiskBasedCache.CACHE_MAGIC) {
                throw new IOException();
            }
            cacheHeader.key = DiskBasedCache.readString(is);
            cacheHeader.etag = DiskBasedCache.readString(is);
            if (cacheHeader.etag.equals("")) {
                cacheHeader.etag = null;
            }
            cacheHeader.serverDate = DiskBasedCache.readLong(is);
            cacheHeader.ttl = DiskBasedCache.readLong(is);
            cacheHeader.softTtl = DiskBasedCache.readLong(is);
            cacheHeader.responseHeaders = DiskBasedCache.readStringStringMap(is);
            return cacheHeader;
        }

        public Entry toCacheEntry(byte[] data) {
            Entry entry = new Entry();
            entry.data = data;
            entry.etag = this.etag;
            entry.serverDate = this.serverDate;
            entry.ttl = this.ttl;
            entry.softTtl = this.softTtl;
            entry.responseHeaders = this.responseHeaders;
            return entry;
        }

        public boolean writeHeader(OutputStream os) {
            try {
                DiskBasedCache.writeInt(os, DiskBasedCache.CACHE_MAGIC);
                DiskBasedCache.writeString(os, this.key);
                DiskBasedCache.writeString(os, this.etag == null ? "" : this.etag);
                DiskBasedCache.writeLong(os, this.serverDate);
                DiskBasedCache.writeLong(os, this.ttl);
                DiskBasedCache.writeLong(os, this.softTtl);
                DiskBasedCache.writeStringStringMap(this.responseHeaders, os);
                os.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.d("%s", e.toString());
                return false;
            }
        }
    }

    private static class CountingInputStream extends FilterInputStream {
        /* access modifiers changed from: private */
        public int bytesRead;

        private CountingInputStream(InputStream in) {
            super(in);
            this.bytesRead = 0;
        }

        public int read() throws IOException {
            int read = super.read();
            if (read != -1) {
                this.bytesRead++;
            }
            return read;
        }

        public int read(byte[] buffer, int offset, int count) throws IOException {
            int read = super.read(buffer, offset, count);
            if (read != -1) {
                this.bytesRead += read;
            }
            return read;
        }
    }

    public DiskBasedCache(File rootDirectory, int maxCacheSizeInBytes) {
        this.mEntries = new LinkedHashMap(16, 0.75f, true);
        this.mTotalSize = 0;
        this.mRootDirectory = rootDirectory;
        this.mMaxCacheSizeInBytes = maxCacheSizeInBytes;
    }

    public DiskBasedCache(File rootDirectory) {
        this(rootDirectory, DEFAULT_DISK_USAGE_BYTES);
    }

    public synchronized void clear() {
        synchronized (this) {
            File[] listFiles = this.mRootDirectory.listFiles();
            if (listFiles != null) {
                for (File delete : listFiles) {
                    delete.delete();
                }
            }
            this.mEntries.clear();
            this.mTotalSize = 0;
            VolleyLog.d("Cache cleared.", new Object[0]);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0064 A:{SYNTHETIC, Splitter:B:31:0x0064} */
    public synchronized com.mopub.volley.Cache.Entry get(java.lang.String r9) {
        /*
        r8 = this;
        r1 = 0;
        monitor-enter(r8);
        r0 = r8.mEntries;	 Catch:{ all -> 0x0068 }
        r0 = r0.get(r9);	 Catch:{ all -> 0x0068 }
        r0 = (com.mopub.volley.toolbox.DiskBasedCache.CacheHeader) r0;	 Catch:{ all -> 0x0068 }
        if (r0 != 0) goto L_0x000f;
    L_0x000c:
        r0 = r1;
    L_0x000d:
        monitor-exit(r8);
        return r0;
    L_0x000f:
        r3 = r8.getFileForKey(r9);	 Catch:{ all -> 0x0068 }
        r2 = new com.mopub.volley.toolbox.DiskBasedCache$CountingInputStream;	 Catch:{ IOException -> 0x003b, all -> 0x0060 }
        r4 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x003b, all -> 0x0060 }
        r4.<init>(r3);	 Catch:{ IOException -> 0x003b, all -> 0x0060 }
        r5 = 0;
        r2.m2374init(r4);	 Catch:{ IOException -> 0x003b, all -> 0x0060 }
        com.mopub.volley.toolbox.DiskBasedCache.CacheHeader.readHeader(r2);	 Catch:{ IOException -> 0x0070 }
        r4 = r3.length();	 Catch:{ IOException -> 0x0070 }
        r6 = r2.bytesRead;	 Catch:{ IOException -> 0x0070 }
        r6 = (long) r6;	 Catch:{ IOException -> 0x0070 }
        r4 = r4 - r6;
        r4 = (int) r4;	 Catch:{ IOException -> 0x0070 }
        r4 = streamToBytes(r2, r4);	 Catch:{ IOException -> 0x0070 }
        r0 = r0.toCacheEntry(r4);	 Catch:{ IOException -> 0x0070 }
        r2.close();	 Catch:{ IOException -> 0x0038 }
        goto L_0x000d;
    L_0x0038:
        r0 = move-exception;
        r0 = r1;
        goto L_0x000d;
    L_0x003b:
        r0 = move-exception;
        r2 = r1;
    L_0x003d:
        r4 = "%s: %s";
        r5 = 2;
        r5 = new java.lang.Object[r5];	 Catch:{ all -> 0x006e }
        r6 = 0;
        r3 = r3.getAbsolutePath();	 Catch:{ all -> 0x006e }
        r5[r6] = r3;	 Catch:{ all -> 0x006e }
        r3 = 1;
        r0 = r0.toString();	 Catch:{ all -> 0x006e }
        r5[r3] = r0;	 Catch:{ all -> 0x006e }
        com.mopub.volley.VolleyLog.d(r4, r5);	 Catch:{ all -> 0x006e }
        r8.remove(r9);	 Catch:{ all -> 0x006e }
        if (r2 == 0) goto L_0x005b;
    L_0x0058:
        r2.close();	 Catch:{ IOException -> 0x005d }
    L_0x005b:
        r0 = r1;
        goto L_0x000d;
    L_0x005d:
        r0 = move-exception;
        r0 = r1;
        goto L_0x000d;
    L_0x0060:
        r0 = move-exception;
        r2 = r1;
    L_0x0062:
        if (r2 == 0) goto L_0x0067;
    L_0x0064:
        r2.close();	 Catch:{ IOException -> 0x006b }
    L_0x0067:
        throw r0;	 Catch:{ all -> 0x0068 }
    L_0x0068:
        r0 = move-exception;
        monitor-exit(r8);
        throw r0;
    L_0x006b:
        r0 = move-exception;
        r0 = r1;
        goto L_0x000d;
    L_0x006e:
        r0 = move-exception;
        goto L_0x0062;
    L_0x0070:
        r0 = move-exception;
        goto L_0x003d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.volley.toolbox.DiskBasedCache.get(java.lang.String):com.mopub.volley.Cache$Entry");
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0058 A:{SYNTHETIC, Splitter:B:27:0x0058} */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0050 A:{SYNTHETIC} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x005d A:{SYNTHETIC, Splitter:B:30:0x005d} */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0066 A:{SYNTHETIC, Splitter:B:35:0x0066} */
    public synchronized void initialize() {
        /*
        r9 = this;
        r0 = 0;
        monitor-enter(r9);
        r1 = r9.mRootDirectory;	 Catch:{ all -> 0x006a }
        r1 = r1.exists();	 Catch:{ all -> 0x006a }
        if (r1 != 0) goto L_0x0025;
    L_0x000a:
        r0 = r9.mRootDirectory;	 Catch:{ all -> 0x006a }
        r0 = r0.mkdirs();	 Catch:{ all -> 0x006a }
        if (r0 != 0) goto L_0x0023;
    L_0x0012:
        r0 = "Unable to create cache dir %s";
        r1 = 1;
        r1 = new java.lang.Object[r1];	 Catch:{ all -> 0x006a }
        r2 = 0;
        r3 = r9.mRootDirectory;	 Catch:{ all -> 0x006a }
        r3 = r3.getAbsolutePath();	 Catch:{ all -> 0x006a }
        r1[r2] = r3;	 Catch:{ all -> 0x006a }
        com.mopub.volley.VolleyLog.e(r0, r1);	 Catch:{ all -> 0x006a }
    L_0x0023:
        monitor-exit(r9);
        return;
    L_0x0025:
        r1 = r9.mRootDirectory;	 Catch:{ all -> 0x006a }
        r3 = r1.listFiles();	 Catch:{ all -> 0x006a }
        if (r3 == 0) goto L_0x0023;
    L_0x002d:
        r4 = r3.length;	 Catch:{ all -> 0x006a }
        r2 = r0;
    L_0x002f:
        if (r2 >= r4) goto L_0x0023;
    L_0x0031:
        r5 = r3[r2];	 Catch:{ all -> 0x006a }
        r1 = 0;
        r0 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x0054, all -> 0x0063 }
        r6 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0054, all -> 0x0063 }
        r6.<init>(r5);	 Catch:{ IOException -> 0x0054, all -> 0x0063 }
        r0.<init>(r6);	 Catch:{ IOException -> 0x0054, all -> 0x0063 }
        r1 = com.mopub.volley.toolbox.DiskBasedCache.CacheHeader.readHeader(r0);	 Catch:{ IOException -> 0x0076 }
        r6 = r5.length();	 Catch:{ IOException -> 0x0076 }
        r1.size = r6;	 Catch:{ IOException -> 0x0076 }
        r6 = r1.key;	 Catch:{ IOException -> 0x0076 }
        r9.putEntry(r6, r1);	 Catch:{ IOException -> 0x0076 }
        r0.close();	 Catch:{ IOException -> 0x006d }
    L_0x0050:
        r0 = r2 + 1;
        r2 = r0;
        goto L_0x002f;
    L_0x0054:
        r0 = move-exception;
        r0 = r1;
    L_0x0056:
        if (r5 == 0) goto L_0x005b;
    L_0x0058:
        r5.delete();	 Catch:{ all -> 0x0071 }
    L_0x005b:
        if (r0 == 0) goto L_0x0050;
    L_0x005d:
        r0.close();	 Catch:{ IOException -> 0x0061 }
        goto L_0x0050;
    L_0x0061:
        r0 = move-exception;
        goto L_0x0050;
    L_0x0063:
        r0 = move-exception;
    L_0x0064:
        if (r1 == 0) goto L_0x0069;
    L_0x0066:
        r1.close();	 Catch:{ IOException -> 0x006f }
    L_0x0069:
        throw r0;	 Catch:{ all -> 0x006a }
    L_0x006a:
        r0 = move-exception;
        monitor-exit(r9);
        throw r0;
    L_0x006d:
        r0 = move-exception;
        goto L_0x0050;
    L_0x006f:
        r1 = move-exception;
        goto L_0x0069;
    L_0x0071:
        r1 = move-exception;
        r8 = r1;
        r1 = r0;
        r0 = r8;
        goto L_0x0064;
    L_0x0076:
        r1 = move-exception;
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.volley.toolbox.DiskBasedCache.initialize():void");
    }

    public synchronized void invalidate(String key, boolean fullExpire) {
        Entry entry = get(key);
        if (entry != null) {
            entry.softTtl = 0;
            if (fullExpire) {
                entry.ttl = 0;
            }
            put(key, entry);
        }
    }

    public synchronized void put(String key, Entry entry) {
        pruneIfNeeded(entry.data.length);
        File fileForKey = getFileForKey(key);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileForKey);
            CacheHeader cacheHeader = new CacheHeader(key, entry);
            if (cacheHeader.writeHeader(fileOutputStream)) {
                fileOutputStream.write(entry.data);
                fileOutputStream.close();
                putEntry(key, cacheHeader);
            } else {
                fileOutputStream.close();
                VolleyLog.d("Failed to write header for %s", fileForKey.getAbsolutePath());
                throw new IOException();
            }
        } catch (IOException e) {
            if (!fileForKey.delete()) {
                VolleyLog.d("Could not clean up file %s", fileForKey.getAbsolutePath());
            }
        }
    }

    public synchronized void remove(String key) {
        boolean delete = getFileForKey(key).delete();
        removeEntry(key);
        if (!delete) {
            VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", key, getFilenameForKey(key));
        }
    }

    private String getFilenameForKey(String key) {
        int length = key.length() / 2;
        return String.valueOf(key.substring(0, length).hashCode()) + String.valueOf(key.substring(length).hashCode());
    }

    public File getFileForKey(String key) {
        return new File(this.mRootDirectory, getFilenameForKey(key));
    }

    private void pruneIfNeeded(int neededSpace) {
        if (this.mTotalSize + ((long) neededSpace) >= ((long) this.mMaxCacheSizeInBytes)) {
            int i;
            if (VolleyLog.DEBUG) {
                VolleyLog.v("Pruning old cache entries.", new Object[0]);
            }
            long j = this.mTotalSize;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Iterator it = this.mEntries.entrySet().iterator();
            int i2 = 0;
            while (it.hasNext()) {
                CacheHeader cacheHeader = (CacheHeader) ((Map.Entry) it.next()).getValue();
                if (getFileForKey(cacheHeader.key).delete()) {
                    this.mTotalSize -= cacheHeader.size;
                } else {
                    VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", cacheHeader.key, getFilenameForKey(cacheHeader.key));
                }
                it.remove();
                i = i2 + 1;
                if (((float) (this.mTotalSize + ((long) neededSpace))) < ((float) this.mMaxCacheSizeInBytes) * HYSTERESIS_FACTOR) {
                    break;
                }
                i2 = i;
            }
            i = i2;
            if (VolleyLog.DEBUG) {
                VolleyLog.v("pruned %d files, %d bytes, %d ms", Integer.valueOf(i), Long.valueOf(this.mTotalSize - j), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
            }
        }
    }

    private void putEntry(String key, CacheHeader entry) {
        if (this.mEntries.containsKey(key)) {
            this.mTotalSize += entry.size - ((CacheHeader) this.mEntries.get(key)).size;
        } else {
            this.mTotalSize += entry.size;
        }
        this.mEntries.put(key, entry);
    }

    private void removeEntry(String key) {
        CacheHeader cacheHeader = (CacheHeader) this.mEntries.get(key);
        if (cacheHeader != null) {
            this.mTotalSize -= cacheHeader.size;
            this.mEntries.remove(key);
        }
    }

    private static byte[] streamToBytes(InputStream in, int length) throws IOException {
        byte[] bArr = new byte[length];
        int i = 0;
        while (i < length) {
            int read = in.read(bArr, i, length - i);
            if (read == -1) {
                break;
            }
            i += read;
        }
        if (i == length) {
            return bArr;
        }
        throw new IOException("Expected " + length + " bytes, read " + i + " bytes");
    }

    private static int read(InputStream is) throws IOException {
        int read = is.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    static void writeInt(OutputStream os, int n) throws IOException {
        os.write((n >> 0) & 255);
        os.write((n >> 8) & 255);
        os.write((n >> 16) & 255);
        os.write((n >> 24) & 255);
    }

    static int readInt(InputStream is) throws IOException {
        return ((((read(is) << 0) | 0) | (read(is) << 8)) | (read(is) << 16)) | (read(is) << 24);
    }

    static void writeLong(OutputStream os, long n) throws IOException {
        os.write((byte) ((int) (n >>> null)));
        os.write((byte) ((int) (n >>> 8)));
        os.write((byte) ((int) (n >>> 16)));
        os.write((byte) ((int) (n >>> 24)));
        os.write((byte) ((int) (n >>> 32)));
        os.write((byte) ((int) (n >>> 40)));
        os.write((byte) ((int) (n >>> 48)));
        os.write((byte) ((int) (n >>> 56)));
    }

    static long readLong(InputStream is) throws IOException {
        return (((((((0 | ((((long) read(is)) & 255) << null)) | ((((long) read(is)) & 255) << 8)) | ((((long) read(is)) & 255) << 16)) | ((((long) read(is)) & 255) << 24)) | ((((long) read(is)) & 255) << 32)) | ((((long) read(is)) & 255) << 40)) | ((((long) read(is)) & 255) << 48)) | ((((long) read(is)) & 255) << 56);
    }

    static void writeString(OutputStream os, String s) throws IOException {
        byte[] bytes = s.getBytes(Defaults.ENCODING_UTF_8);
        writeLong(os, (long) bytes.length);
        os.write(bytes, 0, bytes.length);
    }

    static String readString(InputStream is) throws IOException {
        return new String(streamToBytes(is, (int) readLong(is)), Defaults.ENCODING_UTF_8);
    }

    static void writeStringStringMap(Map<String, String> map, OutputStream os) throws IOException {
        if (map != null) {
            writeInt(os, map.size());
            for (Map.Entry entry : map.entrySet()) {
                writeString(os, (String) entry.getKey());
                writeString(os, (String) entry.getValue());
            }
            return;
        }
        writeInt(os, 0);
    }

    static Map<String, String> readStringStringMap(InputStream is) throws IOException {
        int readInt = readInt(is);
        Map<String, String> emptyMap = readInt == 0 ? Collections.emptyMap() : new HashMap(readInt);
        for (int i = 0; i < readInt; i++) {
            emptyMap.put(readString(is).intern(), readString(is).intern());
        }
        return emptyMap;
    }
}
