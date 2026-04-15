package com.mopub.mobileads;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.mopub.common.MoPubBrowser;
import com.mopub.common.Preconditions;
import com.mopub.common.Preconditions.NoThrow;
import com.mopub.common.UrlAction;
import com.mopub.common.UrlHandler.Builder;
import com.mopub.common.UrlHandler.ResultActions;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.DeviceUtils.ForceOrientation;
import com.mopub.common.util.Intents;
import com.mopub.common.util.Strings;
import com.mopub.exceptions.IntentNotResolvableException;
import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import com.mopub.network.TrackingRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VastVideoConfig implements Serializable {
    private static final long serialVersionUID = 1;
    @NonNull
    private final ArrayList<VastAbsoluteProgressTracker> mAbsoluteTrackers = new ArrayList();
    @Nullable
    private String mClickThroughUrl;
    @NonNull
    private final ArrayList<VastTracker> mClickTrackers = new ArrayList();
    @NonNull
    private final ArrayList<VastTracker> mCloseTrackers = new ArrayList();
    @NonNull
    private final ArrayList<VastTracker> mCompleteTrackers = new ArrayList();
    @Nullable
    private String mCustomCloseIconUrl;
    @Nullable
    private String mCustomCtaText;
    @NonNull
    private ForceOrientation mCustomForceOrientation = ForceOrientation.FORCE_LANDSCAPE;
    @Nullable
    private String mCustomSkipText;
    @Nullable
    private String mDiskMediaFileUrl;
    @NonNull
    private final ArrayList<VastTracker> mErrorTrackers = new ArrayList();
    @NonNull
    private final ArrayList<VastFractionalProgressTracker> mFractionalTrackers = new ArrayList();
    @NonNull
    private final ArrayList<VastTracker> mImpressionTrackers = new ArrayList();
    private boolean mIsForceOrientationSet;
    @Nullable
    private VastCompanionAdConfig mLandscapeVastCompanionAdConfig;
    @Nullable
    private String mNetworkMediaFileUrl;
    @NonNull
    private final ArrayList<VastTracker> mPauseTrackers = new ArrayList();
    @Nullable
    private VastCompanionAdConfig mPortraitVastCompanionAdConfig;
    @NonNull
    private final ArrayList<VastTracker> mResumeTrackers = new ArrayList();
    @Nullable
    private String mSkipOffset;
    @NonNull
    private final ArrayList<VastTracker> mSkipTrackers = new ArrayList();
    @Nullable
    private VastIconConfig mVastIconConfig;
    @Nullable
    private VideoViewabilityTracker mVideoViewabilityTracker;

    public void addImpressionTrackers(@NonNull List<VastTracker> impressionTrackers) {
        Preconditions.checkNotNull(impressionTrackers, "impressionTrackers cannot be null");
        this.mImpressionTrackers.addAll(impressionTrackers);
    }

    public void addFractionalTrackers(@NonNull List<VastFractionalProgressTracker> fractionalTrackers) {
        Preconditions.checkNotNull(fractionalTrackers, "fractionalTrackers cannot be null");
        this.mFractionalTrackers.addAll(fractionalTrackers);
        Collections.sort(this.mFractionalTrackers);
    }

    public void addAbsoluteTrackers(@NonNull List<VastAbsoluteProgressTracker> absoluteTrackers) {
        Preconditions.checkNotNull(absoluteTrackers, "absoluteTrackers cannot be null");
        this.mAbsoluteTrackers.addAll(absoluteTrackers);
        Collections.sort(this.mAbsoluteTrackers);
    }

    public void addCompleteTrackers(@NonNull List<VastTracker> completeTrackers) {
        Preconditions.checkNotNull(completeTrackers, "completeTrackers cannot be null");
        this.mCompleteTrackers.addAll(completeTrackers);
    }

    public void addPauseTrackers(@NonNull List<VastTracker> pauseTrackers) {
        Preconditions.checkNotNull(pauseTrackers, "pauseTrackers cannot be null");
        this.mPauseTrackers.addAll(pauseTrackers);
    }

    public void addResumeTrackers(@NonNull List<VastTracker> resumeTrackers) {
        Preconditions.checkNotNull(resumeTrackers, "resumeTrackers cannot be null");
        this.mResumeTrackers.addAll(resumeTrackers);
    }

    public void addCloseTrackers(@NonNull List<VastTracker> closeTrackers) {
        Preconditions.checkNotNull(closeTrackers, "closeTrackers cannot be null");
        this.mCloseTrackers.addAll(closeTrackers);
    }

    public void addSkipTrackers(@NonNull List<VastTracker> skipTrackers) {
        Preconditions.checkNotNull(skipTrackers, "skipTrackers cannot be null");
        this.mSkipTrackers.addAll(skipTrackers);
    }

    public void addClickTrackers(@NonNull List<VastTracker> clickTrackers) {
        Preconditions.checkNotNull(clickTrackers, "clickTrackers cannot be null");
        this.mClickTrackers.addAll(clickTrackers);
    }

    public void addErrorTrackers(@NonNull List<VastTracker> errorTrackers) {
        Preconditions.checkNotNull(errorTrackers, "errorTrackers cannot be null");
        this.mErrorTrackers.addAll(errorTrackers);
    }

    public void setClickThroughUrl(@Nullable String clickThroughUrl) {
        this.mClickThroughUrl = clickThroughUrl;
    }

    public void setNetworkMediaFileUrl(@Nullable String networkMediaFileUrl) {
        this.mNetworkMediaFileUrl = networkMediaFileUrl;
    }

    public void setDiskMediaFileUrl(@Nullable String diskMediaFileUrl) {
        this.mDiskMediaFileUrl = diskMediaFileUrl;
    }

    public void setVastCompanionAd(@Nullable VastCompanionAdConfig landscapeVastCompanionAdConfig, @Nullable VastCompanionAdConfig portraitVastCompanionAdConfig) {
        this.mLandscapeVastCompanionAdConfig = landscapeVastCompanionAdConfig;
        this.mPortraitVastCompanionAdConfig = portraitVastCompanionAdConfig;
    }

    public void setVastIconConfig(@Nullable VastIconConfig vastIconConfig) {
        this.mVastIconConfig = vastIconConfig;
    }

    public void setCustomCtaText(@Nullable String customCtaText) {
        if (customCtaText != null) {
            this.mCustomCtaText = customCtaText;
        }
    }

    public void setCustomSkipText(@Nullable String customSkipText) {
        if (customSkipText != null) {
            this.mCustomSkipText = customSkipText;
        }
    }

    public void setCustomCloseIconUrl(@Nullable String customCloseIconUrl) {
        if (customCloseIconUrl != null) {
            this.mCustomCloseIconUrl = customCloseIconUrl;
        }
    }

    public void setCustomForceOrientation(@Nullable ForceOrientation customForceOrientation) {
        if (customForceOrientation != null && customForceOrientation != ForceOrientation.UNDEFINED) {
            this.mCustomForceOrientation = customForceOrientation;
            this.mIsForceOrientationSet = true;
        }
    }

    public void setSkipOffset(@Nullable String skipOffset) {
        if (skipOffset != null) {
            this.mSkipOffset = skipOffset;
        }
    }

    public void setVideoViewabilityTracker(@Nullable VideoViewabilityTracker videoViewabilityTracker) {
        if (videoViewabilityTracker != null) {
            this.mVideoViewabilityTracker = videoViewabilityTracker;
        }
    }

    @NonNull
    public List<VastTracker> getImpressionTrackers() {
        return this.mImpressionTrackers;
    }

    @NonNull
    public ArrayList<VastAbsoluteProgressTracker> getAbsoluteTrackers() {
        return this.mAbsoluteTrackers;
    }

    @NonNull
    public ArrayList<VastFractionalProgressTracker> getFractionalTrackers() {
        return this.mFractionalTrackers;
    }

    @NonNull
    public List<VastTracker> getPauseTrackers() {
        return this.mPauseTrackers;
    }

    @NonNull
    public List<VastTracker> getResumeTrackers() {
        return this.mResumeTrackers;
    }

    @NonNull
    public List<VastTracker> getCompleteTrackers() {
        return this.mCompleteTrackers;
    }

    @NonNull
    public List<VastTracker> getCloseTrackers() {
        return this.mCloseTrackers;
    }

    @NonNull
    public List<VastTracker> getSkipTrackers() {
        return this.mSkipTrackers;
    }

    @NonNull
    public List<VastTracker> getClickTrackers() {
        return this.mClickTrackers;
    }

    @NonNull
    public List<VastTracker> getErrorTrackers() {
        return this.mErrorTrackers;
    }

    @Nullable
    public String getClickThroughUrl() {
        return this.mClickThroughUrl;
    }

    @Nullable
    public String getNetworkMediaFileUrl() {
        return this.mNetworkMediaFileUrl;
    }

    @Nullable
    public String getDiskMediaFileUrl() {
        return this.mDiskMediaFileUrl;
    }

    @Nullable
    public VastCompanionAdConfig getVastCompanionAd(int orientation) {
        switch (orientation) {
            case 1:
                return this.mPortraitVastCompanionAdConfig;
            case 2:
                return this.mLandscapeVastCompanionAdConfig;
            default:
                return this.mLandscapeVastCompanionAdConfig;
        }
    }

    @Nullable
    public VastIconConfig getVastIconConfig() {
        return this.mVastIconConfig;
    }

    @Nullable
    public String getCustomCtaText() {
        return this.mCustomCtaText;
    }

    @Nullable
    public String getCustomSkipText() {
        return this.mCustomSkipText;
    }

    @Nullable
    public String getCustomCloseIconUrl() {
        return this.mCustomCloseIconUrl;
    }

    @Nullable
    public VideoViewabilityTracker getVideoViewabilityTracker() {
        return this.mVideoViewabilityTracker;
    }

    public boolean isCustomForceOrientationSet() {
        return this.mIsForceOrientationSet;
    }

    public boolean hasCompanionAd() {
        return (this.mLandscapeVastCompanionAdConfig == null || this.mPortraitVastCompanionAdConfig == null) ? false : true;
    }

    @NonNull
    public ForceOrientation getCustomForceOrientation() {
        return this.mCustomForceOrientation;
    }

    @Nullable
    public String getSkipOffsetString() {
        return this.mSkipOffset;
    }

    public void handleImpression(@NonNull Context context, int contentPlayHead) {
        Preconditions.checkNotNull(context, "context cannot be null");
        TrackingRequest.makeVastTrackingHttpRequest(this.mImpressionTrackers, null, Integer.valueOf(contentPlayHead), this.mNetworkMediaFileUrl, context);
    }

    public void handleClickForResult(@NonNull Activity activity, int contentPlayHead, int requestCode) {
        handleClick(activity, contentPlayHead, Integer.valueOf(requestCode));
    }

    public void handleClickWithoutResult(@NonNull Context context, int contentPlayHead) {
        handleClick(context.getApplicationContext(), contentPlayHead, null);
    }

    private void handleClick(@NonNull final Context context, int contentPlayHead, @Nullable final Integer requestCode) {
        Preconditions.checkNotNull(context, "context cannot be null");
        TrackingRequest.makeVastTrackingHttpRequest(this.mClickTrackers, null, Integer.valueOf(contentPlayHead), this.mNetworkMediaFileUrl, context);
        if (!TextUtils.isEmpty(this.mClickThroughUrl)) {
            new Builder().withSupportedUrlActions(UrlAction.IGNORE_ABOUT_SCHEME, UrlAction.OPEN_APP_MARKET, UrlAction.OPEN_NATIVE_BROWSER, UrlAction.OPEN_IN_APP_BROWSER, UrlAction.HANDLE_SHARE_TWEET, UrlAction.FOLLOW_DEEP_LINK_WITH_FALLBACK, UrlAction.FOLLOW_DEEP_LINK).withResultActions(new ResultActions() {
                public void urlHandlingSucceeded(@NonNull String url, @NonNull UrlAction urlAction) {
                    if (urlAction == UrlAction.OPEN_IN_APP_BROWSER) {
                        Bundle bundle = new Bundle();
                        bundle.putString(MoPubBrowser.DESTINATION_URL_KEY, url);
                        Class cls = MoPubBrowser.class;
                        Intent startActivityIntent = Intents.getStartActivityIntent(context, cls, bundle);
                        try {
                            if (context instanceof Activity) {
                                Preconditions.checkNotNull(requestCode);
                                ((Activity) context).startActivityForResult(startActivityIntent, requestCode.intValue());
                                return;
                            }
                            Intents.startActivity(context, startActivityIntent);
                        } catch (ActivityNotFoundException e) {
                            MoPubLog.d("Activity " + cls.getName() + " not found. Did you declare it in your AndroidManifest.xml?");
                        } catch (IntentNotResolvableException e2) {
                            MoPubLog.d("Activity " + cls.getName() + " not found. Did you declare it in your AndroidManifest.xml?");
                        }
                    }
                }

                public void urlHandlingFailed(@NonNull String url, @NonNull UrlAction lastFailedUrlAction) {
                }
            }).withoutMoPubBrowser().build().handleUrl(context, this.mClickThroughUrl);
        }
    }

    public void handleResume(@NonNull Context context, int contentPlayHead) {
        Preconditions.checkNotNull(context, "context cannot be null");
        TrackingRequest.makeVastTrackingHttpRequest(this.mResumeTrackers, null, Integer.valueOf(contentPlayHead), this.mNetworkMediaFileUrl, context);
    }

    public void handlePause(@NonNull Context context, int contentPlayHead) {
        Preconditions.checkNotNull(context, "context cannot be null");
        TrackingRequest.makeVastTrackingHttpRequest(this.mPauseTrackers, null, Integer.valueOf(contentPlayHead), this.mNetworkMediaFileUrl, context);
    }

    public void handleClose(@NonNull Context context, int contentPlayHead) {
        Preconditions.checkNotNull(context, "context cannot be null");
        TrackingRequest.makeVastTrackingHttpRequest(this.mCloseTrackers, null, Integer.valueOf(contentPlayHead), this.mNetworkMediaFileUrl, context);
        TrackingRequest.makeVastTrackingHttpRequest(this.mSkipTrackers, null, Integer.valueOf(contentPlayHead), this.mNetworkMediaFileUrl, context);
    }

    public void handleComplete(@NonNull Context context, int contentPlayHead) {
        Preconditions.checkNotNull(context, "context cannot be null");
        TrackingRequest.makeVastTrackingHttpRequest(this.mCompleteTrackers, null, Integer.valueOf(contentPlayHead), this.mNetworkMediaFileUrl, context);
    }

    public void handleError(@NonNull Context context, @Nullable VastErrorCode errorCode, int contentPlayHead) {
        Preconditions.checkNotNull(context, "context cannot be null");
        TrackingRequest.makeVastTrackingHttpRequest(this.mErrorTrackers, errorCode, Integer.valueOf(contentPlayHead), this.mNetworkMediaFileUrl, context);
    }

    @NonNull
    public List<VastTracker> getUntriggeredTrackersBefore(int currentPositionMillis, int videoLengthMillis) {
        int i = 0;
        if (!NoThrow.checkArgument(videoLengthMillis > 0)) {
            return Collections.emptyList();
        }
        float f = ((float) currentPositionMillis) / ((float) videoLengthMillis);
        ArrayList arrayList = new ArrayList();
        VastAbsoluteProgressTracker vastAbsoluteProgressTracker = new VastAbsoluteProgressTracker("", currentPositionMillis);
        int size = this.mAbsoluteTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            VastAbsoluteProgressTracker vastAbsoluteProgressTracker2 = (VastAbsoluteProgressTracker) this.mAbsoluteTrackers.get(i2);
            if (vastAbsoluteProgressTracker2.compareTo(vastAbsoluteProgressTracker) > 0) {
                break;
            }
            if (!vastAbsoluteProgressTracker2.isTracked()) {
                arrayList.add(vastAbsoluteProgressTracker2);
            }
        }
        VastFractionalProgressTracker vastFractionalProgressTracker = new VastFractionalProgressTracker("", f);
        int size2 = this.mFractionalTrackers.size();
        while (i < size2) {
            VastFractionalProgressTracker vastFractionalProgressTracker2 = (VastFractionalProgressTracker) this.mFractionalTrackers.get(i);
            if (vastFractionalProgressTracker2.compareTo(vastFractionalProgressTracker) > 0) {
                break;
            }
            if (!vastFractionalProgressTracker2.isTracked()) {
                arrayList.add(vastFractionalProgressTracker2);
            }
            i++;
        }
        return arrayList;
    }

    public int getRemainingProgressTrackerCount() {
        return getUntriggeredTrackersBefore(MoPubClientPositioning.NO_REPEAT, MoPubClientPositioning.NO_REPEAT).size();
    }

    @Nullable
    public Integer getSkipOffsetMillis(int videoDuration) {
        if (this.mSkipOffset != null) {
            try {
                if (Strings.isAbsoluteTracker(this.mSkipOffset)) {
                    Integer parseAbsoluteOffset = Strings.parseAbsoluteOffset(this.mSkipOffset);
                    if (parseAbsoluteOffset != null && parseAbsoluteOffset.intValue() < videoDuration) {
                        return parseAbsoluteOffset;
                    }
                } else if (Strings.isPercentageTracker(this.mSkipOffset)) {
                    int round = Math.round((Float.parseFloat(this.mSkipOffset.replace("%", "")) / 100.0f) * ((float) videoDuration));
                    if (round < videoDuration) {
                        return Integer.valueOf(round);
                    }
                } else {
                    MoPubLog.d(String.format("Invalid VAST skipoffset format: %s", new Object[]{this.mSkipOffset}));
                }
            } catch (NumberFormatException e) {
                MoPubLog.d(String.format("Failed to parse skipoffset %s", new Object[]{this.mSkipOffset}));
            }
        }
        return null;
    }
}
