package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.WindowManager;
import com.mopub.common.CacheService;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.AsyncTasks;

public class VastManager implements VastXmlManagerAggregatorListener {
    private int mScreenAreaDp;
    private double mScreenAspectRatio;
    private final boolean mShouldPreCacheVideo;
    /* access modifiers changed from: private */
    @Nullable
    public VastManagerListener mVastManagerListener;
    @Nullable
    private VastXmlManagerAggregator mVastXmlManagerAggregator;

    public interface VastManagerListener {
        void onVastVideoConfigurationPrepared(@Nullable VastVideoConfig vastVideoConfig);
    }

    public VastManager(@NonNull Context context, boolean shouldPreCacheVideo) {
        initializeScreenDimensions(context);
        this.mShouldPreCacheVideo = shouldPreCacheVideo;
    }

    public void prepareVastVideoConfiguration(@Nullable String vastXml, @NonNull VastManagerListener vastManagerListener, @NonNull Context context) {
        Preconditions.checkNotNull(vastManagerListener, "vastManagerListener cannot be null");
        Preconditions.checkNotNull(context, "context cannot be null");
        if (this.mVastXmlManagerAggregator == null) {
            this.mVastManagerListener = vastManagerListener;
            this.mVastXmlManagerAggregator = new VastXmlManagerAggregator(this, this.mScreenAspectRatio, this.mScreenAreaDp, context.getApplicationContext());
            try {
                AsyncTasks.safeExecuteOnExecutor(this.mVastXmlManagerAggregator, vastXml);
            } catch (Exception e) {
                MoPubLog.d("Failed to aggregate vast xml", e);
                this.mVastManagerListener.onVastVideoConfigurationPrepared(null);
            }
        }
    }

    public void cancel() {
        if (this.mVastXmlManagerAggregator != null) {
            this.mVastXmlManagerAggregator.cancel(true);
            this.mVastXmlManagerAggregator = null;
        }
    }

    public void onAggregationComplete(@Nullable final VastVideoConfig vastVideoConfig) {
        if (this.mVastManagerListener == null) {
            throw new IllegalStateException("mVastManagerListener cannot be null here. Did you call prepareVastVideoConfiguration()?");
        } else if (vastVideoConfig == null) {
            this.mVastManagerListener.onVastVideoConfigurationPrepared(null);
        } else if (!this.mShouldPreCacheVideo || updateDiskMediaFileUrl(vastVideoConfig)) {
            this.mVastManagerListener.onVastVideoConfigurationPrepared(vastVideoConfig);
        } else {
            VideoDownloader.cache(vastVideoConfig.getNetworkMediaFileUrl(), new VideoDownloaderListener() {
                public void onComplete(boolean success) {
                    if (success && VastManager.this.updateDiskMediaFileUrl(vastVideoConfig)) {
                        VastManager.this.mVastManagerListener.onVastVideoConfigurationPrepared(vastVideoConfig);
                        return;
                    }
                    MoPubLog.d("Failed to download VAST video.");
                    VastManager.this.mVastManagerListener.onVastVideoConfigurationPrepared(null);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public boolean updateDiskMediaFileUrl(@NonNull VastVideoConfig vastVideoConfig) {
        Preconditions.checkNotNull(vastVideoConfig, "vastVideoConfig cannot be null");
        String networkMediaFileUrl = vastVideoConfig.getNetworkMediaFileUrl();
        if (!CacheService.containsKeyDiskCache(networkMediaFileUrl)) {
            return false;
        }
        vastVideoConfig.setDiskMediaFileUrl(CacheService.getFilePathDiskCache(networkMediaFileUrl));
        return true;
    }

    private void initializeScreenDimensions(@NonNull Context context) {
        Preconditions.checkNotNull(context, "context cannot be null");
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        int width = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();
        float f = context.getResources().getDisplayMetrics().density;
        if (f <= 0.0f) {
            f = 1.0f;
        }
        int max = Math.max(width, height);
        width = Math.min(width, height);
        this.mScreenAspectRatio = ((double) max) / ((double) width);
        this.mScreenAreaDp = (int) ((((float) width) / f) * (((float) max) / f));
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public int getScreenAreaDp() {
        return this.mScreenAreaDp;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public double getScreenAspectRatio() {
        return this.mScreenAspectRatio;
    }
}
