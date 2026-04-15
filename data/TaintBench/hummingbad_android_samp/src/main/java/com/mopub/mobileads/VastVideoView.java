package com.mopub.mobileads;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.VideoView;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;
import com.mopub.common.util.Streams;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;

public class VastVideoView extends VideoView {
    private static final int MAX_VIDEO_RETRIES = 1;
    private static final int VIDEO_VIEW_FILE_PERMISSION_ERROR = Integer.MIN_VALUE;
    @Nullable
    private VastVideoBlurLastVideoFrameTask mBlurLastVideoFrameTask;
    @Nullable
    private MediaMetadataRetriever mMediaMetadataRetriever = createMediaMetadataRetriever();
    private int mVideoRetries;

    public VastVideoView(@NonNull Context context) {
        super(context);
        Preconditions.checkNotNull(context, "context cannot be null");
    }

    public void prepareBlurredLastVideoFrame(@NonNull ImageView blurredLastVideoFrameImageView, @NonNull String diskMediaFileUrl) {
        if (this.mMediaMetadataRetriever != null) {
            this.mBlurLastVideoFrameTask = new VastVideoBlurLastVideoFrameTask(this.mMediaMetadataRetriever, blurredLastVideoFrameImageView, getDuration());
            try {
                AsyncTasks.safeExecuteOnExecutor(this.mBlurLastVideoFrameTask, diskMediaFileUrl);
            } catch (Exception e) {
                MoPubLog.d("Failed to blur last video frame", e);
            }
        }
    }

    public void onDestroy() {
        if (this.mBlurLastVideoFrameTask != null && this.mBlurLastVideoFrameTask.getStatus() != Status.FINISHED) {
            this.mBlurLastVideoFrameTask.cancel(true);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean retryMediaPlayer(MediaPlayer mediaPlayer, int what, int extra, @NonNull String diskMediaFileUrl) {
        Closeable closeable;
        Throwable th;
        if (VERSION.SDK_INT >= 16 || what != 1 || extra != Integer.MIN_VALUE || this.mVideoRetries > 0) {
            return false;
        }
        Closeable closeable2 = null;
        try {
            mediaPlayer.reset();
            FileInputStream fileInputStream = new FileInputStream(new File(diskMediaFileUrl));
            try {
                mediaPlayer.setDataSource(fileInputStream.getFD());
                mediaPlayer.prepareAsync();
                start();
                Streams.closeStream(fileInputStream);
                this.mVideoRetries++;
                return true;
            } catch (Exception e) {
                Object closeable3 = fileInputStream;
                Streams.closeStream(closeable3);
                this.mVideoRetries++;
                return false;
            } catch (Throwable th2) {
                th = th2;
                Object closeable22 = fileInputStream;
                Streams.closeStream(closeable22);
                this.mVideoRetries++;
                throw th;
            }
        } catch (Exception e2) {
            closeable3 = null;
            Streams.closeStream(closeable3);
            this.mVideoRetries++;
            return false;
        } catch (Throwable th3) {
            th = th3;
            Streams.closeStream(closeable22);
            this.mVideoRetries++;
            throw th;
        }
    }

    public void onResume() {
        this.mVideoRetries = 0;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @VisibleForTesting
    public MediaMetadataRetriever createMediaMetadataRetriever() {
        if (VERSION.SDK_INT >= 10) {
            return new MediaMetadataRetriever();
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setMediaMetadataRetriever(@NonNull MediaMetadataRetriever mediaMetadataRetriever) {
        this.mMediaMetadataRetriever = mediaMetadataRetriever;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    @Deprecated
    @VisibleForTesting
    public VastVideoBlurLastVideoFrameTask getBlurLastVideoFrameTask() {
        return this.mBlurLastVideoFrameTask;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setBlurLastVideoFrameTask(@NonNull VastVideoBlurLastVideoFrameTask blurLastVideoFrameTask) {
        this.mBlurLastVideoFrameTask = blurLastVideoFrameTask;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public int getVideoRetries() {
        return this.mVideoRetries;
    }
}
