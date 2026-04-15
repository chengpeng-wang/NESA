package com.mopub.mobileads;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;

public class VideoDownloader {
    private static final int MAX_VIDEO_SIZE = 26214400;
    /* access modifiers changed from: private|static|final */
    public static final Deque<WeakReference<VideoDownloaderTask>> sDownloaderTasks = new ArrayDeque();

    interface VideoDownloaderListener {
        void onComplete(boolean z);
    }

    @VisibleForTesting
    static class VideoDownloaderTask extends AsyncTask<String, Void, Boolean> {
        @NonNull
        private final VideoDownloaderListener mListener;
        @NonNull
        private final WeakReference<VideoDownloaderTask> mWeakSelf = new WeakReference(this);

        @VisibleForTesting
        VideoDownloaderTask(@NonNull VideoDownloaderListener listener) {
            this.mListener = listener;
            VideoDownloader.sDownloaderTasks.add(this.mWeakSelf);
        }

        /* access modifiers changed from: protected|varargs */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x00b1  */
        /* JADX WARNING: Removed duplicated region for block: B:62:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:39:0x00a4  */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x00b1  */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x00b1  */
        public java.lang.Boolean doInBackground(java.lang.String... r7) {
            /*
            r6 = this;
            r2 = 0;
            r5 = 26214400; // 0x1900000 float:5.2897246E-38 double:1.29516345E-316;
            r1 = 0;
            if (r7 == 0) goto L_0x000d;
        L_0x0006:
            r0 = r7.length;
            if (r0 == 0) goto L_0x000d;
        L_0x0009:
            r0 = r7[r1];
            if (r0 != 0) goto L_0x0017;
        L_0x000d:
            r0 = "VideoDownloader task tried to execute null or empty url.";
            com.mopub.common.logging.MoPubLog.d(r0);
            r0 = java.lang.Boolean.valueOf(r1);
        L_0x0016:
            return r0;
        L_0x0017:
            r0 = r7[r1];
            r3 = com.mopub.common.MoPubHttpUrlConnection.getHttpUrlConnection(r0);	 Catch:{ Exception -> 0x0093, all -> 0x00a9 }
            r1 = new java.io.BufferedInputStream;	 Catch:{ Exception -> 0x00bd, all -> 0x00b5 }
            r4 = r3.getInputStream();	 Catch:{ Exception -> 0x00bd, all -> 0x00b5 }
            r1.<init>(r4);	 Catch:{ Exception -> 0x00bd, all -> 0x00b5 }
            r2 = r3.getResponseCode();	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r2 < r4) goto L_0x0032;
        L_0x002e:
            r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
            if (r2 < r4) goto L_0x0052;
        L_0x0032:
            r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r4 = "VideoDownloader encountered unexpected statusCode: ";
            r0.<init>(r4);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r0 = r0.append(r2);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r0 = r0.toString();	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            com.mopub.common.logging.MoPubLog.d(r0);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r0 = 0;
            r0 = java.lang.Boolean.valueOf(r0);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            com.mopub.common.util.Streams.closeStream(r1);
            if (r3 == 0) goto L_0x0016;
        L_0x004e:
            r3.disconnect();
            goto L_0x0016;
        L_0x0052:
            r2 = r3.getContentLength();	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            if (r2 <= r5) goto L_0x0082;
        L_0x0058:
            r0 = "VideoDownloader encountered video larger than disk cap. (%d bytes / %d maximum).";
            r4 = 2;
            r4 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r5 = 0;
            r2 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r4[r5] = r2;	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r2 = 1;
            r5 = 26214400; // 0x1900000 float:5.2897246E-38 double:1.29516345E-316;
            r5 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r4[r2] = r5;	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r0 = java.lang.String.format(r0, r4);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            com.mopub.common.logging.MoPubLog.d(r0);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r0 = 0;
            r0 = java.lang.Boolean.valueOf(r0);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            com.mopub.common.util.Streams.closeStream(r1);
            if (r3 == 0) goto L_0x0016;
        L_0x007e:
            r3.disconnect();
            goto L_0x0016;
        L_0x0082:
            r0 = com.mopub.common.CacheService.putToDiskCache(r0, r1);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            r0 = java.lang.Boolean.valueOf(r0);	 Catch:{ Exception -> 0x00c1, all -> 0x00b8 }
            com.mopub.common.util.Streams.closeStream(r1);
            if (r3 == 0) goto L_0x0016;
        L_0x008f:
            r3.disconnect();
            goto L_0x0016;
        L_0x0093:
            r0 = move-exception;
            r1 = r2;
        L_0x0095:
            r3 = "VideoDownloader task threw an internal exception.";
            com.mopub.common.logging.MoPubLog.d(r3, r0);	 Catch:{ all -> 0x00ba }
            r0 = 0;
            r0 = java.lang.Boolean.valueOf(r0);	 Catch:{ all -> 0x00ba }
            com.mopub.common.util.Streams.closeStream(r1);
            if (r2 == 0) goto L_0x0016;
        L_0x00a4:
            r2.disconnect();
            goto L_0x0016;
        L_0x00a9:
            r0 = move-exception;
            r1 = r2;
            r3 = r2;
        L_0x00ac:
            com.mopub.common.util.Streams.closeStream(r1);
            if (r3 == 0) goto L_0x00b4;
        L_0x00b1:
            r3.disconnect();
        L_0x00b4:
            throw r0;
        L_0x00b5:
            r0 = move-exception;
            r1 = r2;
            goto L_0x00ac;
        L_0x00b8:
            r0 = move-exception;
            goto L_0x00ac;
        L_0x00ba:
            r0 = move-exception;
            r3 = r2;
            goto L_0x00ac;
        L_0x00bd:
            r0 = move-exception;
            r1 = r2;
            r2 = r3;
            goto L_0x0095;
        L_0x00c1:
            r0 = move-exception;
            r2 = r3;
            goto L_0x0095;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mopub.mobileads.VideoDownloader$VideoDownloaderTask.doInBackground(java.lang.String[]):java.lang.Boolean");
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean success) {
            if (isCancelled()) {
                onCancelled();
                return;
            }
            VideoDownloader.sDownloaderTasks.remove(this.mWeakSelf);
            if (success == null) {
                this.mListener.onComplete(false);
            } else {
                this.mListener.onComplete(success.booleanValue());
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            MoPubLog.d("VideoDownloader task was cancelled.");
            VideoDownloader.sDownloaderTasks.remove(this.mWeakSelf);
            this.mListener.onComplete(false);
        }
    }

    private VideoDownloader() {
    }

    public static void cache(@Nullable String url, @NonNull VideoDownloaderListener listener) {
        Preconditions.checkNotNull(listener);
        if (url == null) {
            MoPubLog.d("VideoDownloader attempted to cache video with null url.");
            listener.onComplete(false);
            return;
        }
        try {
            AsyncTasks.safeExecuteOnExecutor(new VideoDownloaderTask(listener), url);
        } catch (Exception e) {
            listener.onComplete(false);
        }
    }

    public static void cancelAllDownloaderTasks() {
        for (WeakReference cancelOneTask : sDownloaderTasks) {
            cancelOneTask(cancelOneTask);
        }
        sDownloaderTasks.clear();
    }

    public static void cancelLastDownloadTask() {
        if (!sDownloaderTasks.isEmpty()) {
            cancelOneTask((WeakReference) sDownloaderTasks.peekLast());
            sDownloaderTasks.removeLast();
        }
    }

    private static boolean cancelOneTask(@Nullable WeakReference<VideoDownloaderTask> weakDownloaderTask) {
        if (weakDownloaderTask == null) {
            return false;
        }
        VideoDownloaderTask videoDownloaderTask = (VideoDownloaderTask) weakDownloaderTask.get();
        if (videoDownloaderTask == null) {
            return false;
        }
        return videoDownloaderTask.cancel(true);
    }

    @Deprecated
    @VisibleForTesting
    public static Deque<WeakReference<VideoDownloaderTask>> getDownloaderTasks() {
        return sDownloaderTasks;
    }

    @Deprecated
    @VisibleForTesting
    public static void clearDownloaderTasks() {
        sDownloaderTasks.clear();
    }
}
