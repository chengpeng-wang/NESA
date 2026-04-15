package com.mopub.common;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.DiskLruCache.Editor;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.DeviceUtils;
import com.mopub.common.util.Streams;
import com.mopub.common.util.Utils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CacheService {
    private static final int APP_VERSION = 1;
    private static final int DISK_CACHE_INDEX = 0;
    static final String UNIQUE_CACHE_NAME = "mopub-cache";
    private static final int VALUE_COUNT = 1;
    private static DiskLruCache sDiskLruCache;

    public interface DiskLruCacheGetListener {
        void onComplete(String str, byte[] bArr);
    }

    private static class DiskLruCacheGetTask extends AsyncTask<Void, Void, byte[]> {
        private final DiskLruCacheGetListener mDiskLruCacheGetListener;
        private final String mKey;

        DiskLruCacheGetTask(String key, DiskLruCacheGetListener diskLruCacheGetListener) {
            this.mDiskLruCacheGetListener = diskLruCacheGetListener;
            this.mKey = key;
        }

        /* access modifiers changed from: protected|varargs */
        public byte[] doInBackground(Void... voids) {
            return CacheService.getFromDiskCache(this.mKey);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(byte[] bytes) {
            if (isCancelled()) {
                onCancelled();
            } else if (this.mDiskLruCacheGetListener != null) {
                this.mDiskLruCacheGetListener.onComplete(this.mKey, bytes);
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            if (this.mDiskLruCacheGetListener != null) {
                this.mDiskLruCacheGetListener.onComplete(this.mKey, null);
            }
        }
    }

    private static class DiskLruCachePutTask extends AsyncTask<Void, Void, Void> {
        private final byte[] mContent;
        private final String mKey;

        DiskLruCachePutTask(String key, byte[] content) {
            this.mKey = key;
            this.mContent = content;
        }

        /* access modifiers changed from: protected|varargs */
        public Void doInBackground(Void... voids) {
            CacheService.putToDiskCache(this.mKey, this.mContent);
            return null;
        }
    }

    public static boolean initializeDiskCache(Context context) {
        if (context == null) {
            return false;
        }
        if (sDiskLruCache == null) {
            File diskCacheDirectory = getDiskCacheDirectory(context);
            if (diskCacheDirectory == null) {
                return false;
            }
            try {
                sDiskLruCache = DiskLruCache.open(diskCacheDirectory, 1, 1, DeviceUtils.diskCacheSizeBytes(diskCacheDirectory));
            } catch (IOException e) {
                MoPubLog.d("Unable to create DiskLruCache", e);
                return false;
            }
        }
        return true;
    }

    public static void initialize(Context context) {
        initializeDiskCache(context);
    }

    public static String createValidDiskCacheKey(String key) {
        return Utils.sha1(key);
    }

    @Nullable
    public static File getDiskCacheDirectory(@NonNull Context context) {
        File cacheDir = context.getCacheDir();
        if (cacheDir == null) {
            return null;
        }
        return new File(cacheDir.getPath() + File.separator + UNIQUE_CACHE_NAME);
    }

    public static boolean containsKeyDiskCache(String key) {
        if (sDiskLruCache == null) {
            return false;
        }
        try {
            if (sDiskLruCache.get(createValidDiskCacheKey(key)) != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getFilePathDiskCache(String key) {
        if (sDiskLruCache == null) {
            return null;
        }
        return sDiskLruCache.getDirectory() + File.separator + createValidDiskCacheKey(key) + ".0";
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0049  */
    public static byte[] getFromDiskCache(java.lang.String r7) {
        /*
        r0 = 0;
        r1 = sDiskLruCache;
        if (r1 != 0) goto L_0x0006;
    L_0x0005:
        return r0;
    L_0x0006:
        r1 = sDiskLruCache;	 Catch:{ Exception -> 0x0058, all -> 0x004d }
        r2 = createValidDiskCacheKey(r7);	 Catch:{ Exception -> 0x0058, all -> 0x004d }
        r2 = r1.get(r2);	 Catch:{ Exception -> 0x0058, all -> 0x004d }
        if (r2 != 0) goto L_0x0018;
    L_0x0012:
        if (r2 == 0) goto L_0x0005;
    L_0x0014:
        r2.close();
        goto L_0x0005;
    L_0x0018:
        r1 = 0;
        r3 = r2.getInputStream(r1);	 Catch:{ Exception -> 0x005b }
        if (r3 == 0) goto L_0x0033;
    L_0x001f:
        r1 = 0;
        r4 = r2.getLength(r1);	 Catch:{ Exception -> 0x005b }
        r1 = (int) r4;	 Catch:{ Exception -> 0x005b }
        r1 = new byte[r1];	 Catch:{ Exception -> 0x005b }
        r0 = new java.io.BufferedInputStream;	 Catch:{ Exception -> 0x003e }
        r0.<init>(r3);	 Catch:{ Exception -> 0x003e }
        com.mopub.common.util.Streams.readStream(r0, r1);	 Catch:{ all -> 0x0039 }
        com.mopub.common.util.Streams.closeStream(r0);	 Catch:{ Exception -> 0x003e }
        r0 = r1;
    L_0x0033:
        if (r2 == 0) goto L_0x0005;
    L_0x0035:
        r2.close();
        goto L_0x0005;
    L_0x0039:
        r3 = move-exception;
        com.mopub.common.util.Streams.closeStream(r0);	 Catch:{ Exception -> 0x003e }
        throw r3;	 Catch:{ Exception -> 0x003e }
    L_0x003e:
        r0 = move-exception;
        r6 = r0;
        r0 = r1;
        r1 = r6;
    L_0x0042:
        r3 = "Unable to get from DiskLruCache";
        com.mopub.common.logging.MoPubLog.d(r3, r1);	 Catch:{ all -> 0x0056 }
        if (r2 == 0) goto L_0x0005;
    L_0x0049:
        r2.close();
        goto L_0x0005;
    L_0x004d:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x0050:
        if (r2 == 0) goto L_0x0055;
    L_0x0052:
        r2.close();
    L_0x0055:
        throw r0;
    L_0x0056:
        r0 = move-exception;
        goto L_0x0050;
    L_0x0058:
        r1 = move-exception;
        r2 = r0;
        goto L_0x0042;
    L_0x005b:
        r1 = move-exception;
        goto L_0x0042;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.common.CacheService.getFromDiskCache(java.lang.String):byte[]");
    }

    public static void getFromDiskCacheAsync(String key, DiskLruCacheGetListener diskLruCacheGetListener) {
        new DiskLruCacheGetTask(key, diskLruCacheGetListener).execute(new Void[0]);
    }

    public static boolean putToDiskCache(String key, byte[] content) {
        return putToDiskCache(key, new ByteArrayInputStream(content));
    }

    public static boolean putToDiskCache(String key, InputStream content) {
        if (sDiskLruCache == null) {
            return false;
        }
        Editor editor = null;
        try {
            editor = sDiskLruCache.edit(createValidDiskCacheKey(key));
            if (editor == null) {
                return false;
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(editor.newOutputStream(0));
            Streams.copyContent(content, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            sDiskLruCache.flush();
            editor.commit();
            return true;
        } catch (Exception e) {
            MoPubLog.d("Unable to put to DiskLruCache", e);
            if (editor == null) {
                return false;
            }
            try {
                editor.abort();
                return false;
            } catch (IOException e2) {
                return false;
            }
        }
    }

    public static void putToDiskCacheAsync(String key, byte[] content) {
        new DiskLruCachePutTask(key, content).execute(new Void[0]);
    }

    @Deprecated
    @VisibleForTesting
    public static void clearAndNullCaches() {
        if (sDiskLruCache != null) {
            try {
                sDiskLruCache.delete();
                sDiskLruCache = null;
            } catch (IOException e) {
                sDiskLruCache = null;
            }
        }
    }

    @Deprecated
    @VisibleForTesting
    public static DiskLruCache getDiskLruCache() {
        return sDiskLruCache;
    }
}
