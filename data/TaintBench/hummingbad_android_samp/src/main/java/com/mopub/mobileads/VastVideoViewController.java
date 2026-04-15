package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.VideoView;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.BaseVideoViewController.BaseVideoViewControllerListener;
import com.mopub.network.TrackingRequest;
import java.io.Serializable;

public class VastVideoViewController extends BaseVideoViewController {
    static final String CURRENT_POSITION = "current_position";
    static final int DEFAULT_VIDEO_DURATION_FOR_CLOSE_BUTTON = 5000;
    static final int MAX_VIDEO_DURATION_FOR_CLOSE_BUTTON = 16000;
    static final String RESUMED_VAST_CONFIG = "resumed_vast_config";
    private static final int SEEKER_POSITION_NOT_INITIALIZED = -1;
    static final String VAST_VIDEO_CONFIG = "vast_video_config";
    private static final long VIDEO_COUNTDOWN_UPDATE_INTERVAL = 250;
    private static final long VIDEO_PROGRESS_TIMER_CHECKER_DELAY = 50;
    public static final int WEBVIEW_PADDING = 16;
    /* access modifiers changed from: private */
    @NonNull
    public ImageView mBlurredLastVideoFrameImageView;
    /* access modifiers changed from: private */
    @NonNull
    public VastVideoGradientStripWidget mBottomGradientStripWidget;
    @NonNull
    private final OnTouchListener mClickThroughListener;
    @NonNull
    private VastVideoCloseButtonWidget mCloseButtonWidget;
    @NonNull
    private final VastVideoViewCountdownRunnable mCountdownRunnable;
    /* access modifiers changed from: private */
    @NonNull
    public VastVideoCtaButtonWidget mCtaButtonWidget;
    /* access modifiers changed from: private */
    public int mDuration;
    private boolean mHasSkipOffset = false;
    /* access modifiers changed from: private|final */
    @NonNull
    public final View mIconView;
    /* access modifiers changed from: private */
    public boolean mIsCalibrationDone = false;
    /* access modifiers changed from: private */
    public boolean mIsClosing = false;
    /* access modifiers changed from: private */
    public boolean mIsVideoFinishedPlaying;
    /* access modifiers changed from: private|final */
    @NonNull
    public final View mLandscapeCompanionAdView;
    /* access modifiers changed from: private|final */
    @NonNull
    public final View mPortraitCompanionAdView;
    /* access modifiers changed from: private */
    @NonNull
    public VastVideoProgressBarWidget mProgressBarWidget;
    @NonNull
    private final VastVideoViewProgressRunnable mProgressCheckerRunnable;
    /* access modifiers changed from: private */
    @NonNull
    public VastVideoRadialCountdownWidget mRadialCountdownWidget;
    private int mSeekerPositionOnPause = -1;
    /* access modifiers changed from: private */
    public int mShowCloseButtonDelay = 5000;
    private boolean mShowCloseButtonEventFired;
    /* access modifiers changed from: private */
    @NonNull
    public VastVideoGradientStripWidget mTopGradientStripWidget;
    /* access modifiers changed from: private */
    @Nullable
    public VastCompanionAdConfig mVastCompanionAdConfig;
    @Nullable
    private final VastIconConfig mVastIconConfig;
    /* access modifiers changed from: private|final */
    public final VastVideoConfig mVastVideoConfig;
    /* access modifiers changed from: private */
    public boolean mVideoError;
    /* access modifiers changed from: private|final */
    @NonNull
    public final VastVideoView mVideoView;

    VastVideoViewController(final Activity activity, Bundle intentExtras, @Nullable Bundle savedInstanceState, long broadcastIdentifier, BaseVideoViewControllerListener baseVideoViewControllerListener) throws IllegalStateException {
        super(activity, Long.valueOf(broadcastIdentifier), baseVideoViewControllerListener);
        Serializable serializable = null;
        if (savedInstanceState != null) {
            serializable = savedInstanceState.getSerializable(RESUMED_VAST_CONFIG);
        }
        Serializable serializable2 = intentExtras.getSerializable(VAST_VIDEO_CONFIG);
        if (serializable != null && (serializable instanceof VastVideoConfig)) {
            this.mVastVideoConfig = (VastVideoConfig) serializable;
            this.mSeekerPositionOnPause = savedInstanceState.getInt(CURRENT_POSITION, -1);
        } else if (serializable2 == null || !(serializable2 instanceof VastVideoConfig)) {
            throw new IllegalStateException("VastVideoConfig is invalid");
        } else {
            this.mVastVideoConfig = (VastVideoConfig) serializable2;
        }
        if (this.mVastVideoConfig.getDiskMediaFileUrl() == null) {
            throw new IllegalStateException("VastVideoConfig does not have a video disk path");
        }
        this.mVastCompanionAdConfig = this.mVastVideoConfig.getVastCompanionAd(activity.getResources().getConfiguration().orientation);
        this.mVastIconConfig = this.mVastVideoConfig.getVastIconConfig();
        this.mClickThroughListener = new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1 && VastVideoViewController.this.shouldAllowClickThrough()) {
                    VastVideoViewController.this.mIsClosing = true;
                    VastVideoViewController.this.broadcastAction(EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_CLICK);
                    VastVideoViewController.this.mVastVideoConfig.handleClickForResult(activity, VastVideoViewController.this.mIsVideoFinishedPlaying ? VastVideoViewController.this.mDuration : VastVideoViewController.this.getCurrentPosition(), 1);
                }
                return true;
            }
        };
        getLayout().setBackgroundColor(-16777216);
        this.mVideoView = createVideoView(activity, 0);
        this.mVideoView.requestFocus();
        this.mLandscapeCompanionAdView = createCompanionAdView(activity, this.mVastVideoConfig.getVastCompanionAd(2), 4);
        this.mPortraitCompanionAdView = createCompanionAdView(activity, this.mVastVideoConfig.getVastCompanionAd(1), 4);
        addTopGradientStripWidget(activity);
        addProgressBarWidget(activity, 4);
        addBottomGradientStripWidget(activity);
        addRadialCountdownWidget(activity, 4);
        this.mIconView = createIconView(activity, this.mVastIconConfig, 4);
        addBlurredLastVideoFrameImageView(activity, 4);
        addCtaButtonWidget(activity);
        addCloseButtonWidget(activity, 8);
        Handler handler = new Handler(Looper.getMainLooper());
        this.mProgressCheckerRunnable = new VastVideoViewProgressRunnable(this, this.mVastVideoConfig, handler);
        this.mCountdownRunnable = new VastVideoViewCountdownRunnable(this, handler);
    }

    /* access modifiers changed from: protected */
    public VideoView getVideoView() {
        return this.mVideoView;
    }

    /* access modifiers changed from: protected */
    public void onCreate() {
        super.onCreate();
        switch (this.mVastVideoConfig.getCustomForceOrientation()) {
            case FORCE_PORTRAIT:
                getBaseVideoViewControllerListener().onSetRequestedOrientation(1);
                break;
            case FORCE_LANDSCAPE:
                getBaseVideoViewControllerListener().onSetRequestedOrientation(0);
                break;
        }
        this.mVastVideoConfig.handleImpression(getContext(), getCurrentPosition());
        broadcastAction(EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_SHOW);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        startRunnables();
        if (this.mSeekerPositionOnPause > 0) {
            this.mVideoView.seekTo(this.mSeekerPositionOnPause);
        }
        if (!this.mIsVideoFinishedPlaying) {
            this.mVideoView.start();
        }
        if (this.mSeekerPositionOnPause != -1) {
            this.mVastVideoConfig.handleResume(getContext(), this.mSeekerPositionOnPause);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        stopRunnables();
        this.mSeekerPositionOnPause = getCurrentPosition();
        this.mVideoView.pause();
        if (!this.mIsVideoFinishedPlaying && !this.mIsClosing) {
            this.mVastVideoConfig.handlePause(getContext(), this.mSeekerPositionOnPause);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        stopRunnables();
        broadcastAction(EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_DISMISS);
        this.mVideoView.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_POSITION, this.mSeekerPositionOnPause);
        outState.putSerializable(RESUMED_VAST_CONFIG, this.mVastVideoConfig);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        int i = getContext().getResources().getConfiguration().orientation;
        this.mVastCompanionAdConfig = this.mVastVideoConfig.getVastCompanionAd(i);
        if (this.mLandscapeCompanionAdView.getVisibility() == 0 || this.mPortraitCompanionAdView.getVisibility() == 0) {
            if (i == 1) {
                this.mLandscapeCompanionAdView.setVisibility(4);
                this.mPortraitCompanionAdView.setVisibility(0);
            } else {
                this.mPortraitCompanionAdView.setVisibility(4);
                this.mLandscapeCompanionAdView.setVisibility(0);
            }
            if (this.mVastCompanionAdConfig != null) {
                this.mVastCompanionAdConfig.handleImpression(getContext(), this.mDuration);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBackPressed() {
    }

    public boolean backButtonEnabled() {
        return this.mShowCloseButtonEventFired;
    }

    /* access modifiers changed from: 0000 */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1) {
            getBaseVideoViewControllerListener().onFinish();
        }
    }

    /* access modifiers changed from: private */
    public void adjustSkipOffset() {
        int duration = getDuration();
        if (duration < MAX_VIDEO_DURATION_FOR_CLOSE_BUTTON) {
            this.mShowCloseButtonDelay = duration;
        }
        Integer skipOffsetMillis = this.mVastVideoConfig.getSkipOffsetMillis(duration);
        if (skipOffsetMillis != null) {
            this.mShowCloseButtonDelay = skipOffsetMillis.intValue();
            this.mHasSkipOffset = true;
        }
    }

    private VastVideoView createVideoView(@NonNull final Context context, int initialVisibility) {
        if (this.mVastVideoConfig.getDiskMediaFileUrl() == null) {
            throw new IllegalStateException("VastVideoConfig does not have a video disk path");
        }
        final VastVideoView vastVideoView = new VastVideoView(context);
        vastVideoView.setId((int) Utils.generateUniqueId());
        vastVideoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                VastVideoViewController.this.mDuration = VastVideoViewController.this.mVideoView.getDuration();
                VastVideoViewController.this.adjustSkipOffset();
                if (VastVideoViewController.this.mVastCompanionAdConfig == null) {
                    vastVideoView.prepareBlurredLastVideoFrame(VastVideoViewController.this.mBlurredLastVideoFrameImageView, VastVideoViewController.this.mVastVideoConfig.getDiskMediaFileUrl());
                }
                VastVideoViewController.this.mProgressBarWidget.calibrateAndMakeVisible(VastVideoViewController.this.getDuration(), VastVideoViewController.this.mShowCloseButtonDelay);
                VastVideoViewController.this.mRadialCountdownWidget.calibrateAndMakeVisible(VastVideoViewController.this.mShowCloseButtonDelay);
                VastVideoViewController.this.mIsCalibrationDone = true;
            }
        });
        vastVideoView.setOnTouchListener(this.mClickThroughListener);
        vastVideoView.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                VastVideoViewController.this.stopRunnables();
                VastVideoViewController.this.makeVideoInteractable();
                VastVideoViewController.this.videoCompleted(false);
                VastVideoViewController.this.mIsVideoFinishedPlaying = true;
                if (!VastVideoViewController.this.mVideoError && VastVideoViewController.this.mVastVideoConfig.getRemainingProgressTrackerCount() == 0) {
                    VastVideoViewController.this.mVastVideoConfig.handleComplete(VastVideoViewController.this.getContext(), VastVideoViewController.this.getCurrentPosition());
                }
                vastVideoView.setVisibility(4);
                VastVideoViewController.this.mProgressBarWidget.setVisibility(8);
                VastVideoViewController.this.mIconView.setVisibility(8);
                VastVideoViewController.this.mTopGradientStripWidget.notifyVideoComplete();
                VastVideoViewController.this.mBottomGradientStripWidget.notifyVideoComplete();
                VastVideoViewController.this.mCtaButtonWidget.notifyVideoComplete();
                if (VastVideoViewController.this.mVastCompanionAdConfig != null) {
                    if (context.getResources().getConfiguration().orientation == 1) {
                        VastVideoViewController.this.mPortraitCompanionAdView.setVisibility(0);
                    } else {
                        VastVideoViewController.this.mLandscapeCompanionAdView.setVisibility(0);
                    }
                    VastVideoViewController.this.mVastCompanionAdConfig.handleImpression(context, VastVideoViewController.this.mDuration);
                } else if (VastVideoViewController.this.mBlurredLastVideoFrameImageView.getDrawable() != null) {
                    VastVideoViewController.this.mBlurredLastVideoFrameImageView.setVisibility(0);
                }
            }
        });
        vastVideoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                if (vastVideoView.retryMediaPlayer(mediaPlayer, what, extra, VastVideoViewController.this.mVastVideoConfig.getDiskMediaFileUrl())) {
                    return true;
                }
                VastVideoViewController.this.stopRunnables();
                VastVideoViewController.this.makeVideoInteractable();
                VastVideoViewController.this.videoError(false);
                VastVideoViewController.this.mVideoError = true;
                VastVideoViewController.this.mVastVideoConfig.handleError(VastVideoViewController.this.getContext(), VastErrorCode.GENERAL_LINEAR_AD_ERROR, VastVideoViewController.this.getCurrentPosition());
                return false;
            }
        });
        vastVideoView.setVideoPath(this.mVastVideoConfig.getDiskMediaFileUrl());
        vastVideoView.setVisibility(initialVisibility);
        return vastVideoView;
    }

    private void addTopGradientStripWidget(@NonNull Context context) {
        this.mTopGradientStripWidget = new VastVideoGradientStripWidget(context, Orientation.TOP_BOTTOM, this.mVastVideoConfig.getCustomForceOrientation(), this.mVastCompanionAdConfig != null, 0, 6, getLayout().getId());
        getLayout().addView(this.mTopGradientStripWidget);
    }

    private void addBottomGradientStripWidget(@NonNull Context context) {
        this.mBottomGradientStripWidget = new VastVideoGradientStripWidget(context, Orientation.BOTTOM_TOP, this.mVastVideoConfig.getCustomForceOrientation(), this.mVastCompanionAdConfig != null, 8, 2, this.mProgressBarWidget.getId());
        getLayout().addView(this.mBottomGradientStripWidget);
    }

    private void addProgressBarWidget(@NonNull Context context, int initialVisibility) {
        this.mProgressBarWidget = new VastVideoProgressBarWidget(context);
        this.mProgressBarWidget.setAnchorId(this.mVideoView.getId());
        this.mProgressBarWidget.setVisibility(initialVisibility);
        getLayout().addView(this.mProgressBarWidget);
    }

    private void addRadialCountdownWidget(@NonNull Context context, int initialVisibility) {
        this.mRadialCountdownWidget = new VastVideoRadialCountdownWidget(context);
        this.mRadialCountdownWidget.setVisibility(initialVisibility);
        getLayout().addView(this.mRadialCountdownWidget);
    }

    private void addCtaButtonWidget(@NonNull Context context) {
        boolean z = true;
        boolean z2 = this.mVastCompanionAdConfig != null;
        if (TextUtils.isEmpty(this.mVastVideoConfig.getClickThroughUrl())) {
            z = false;
        }
        this.mCtaButtonWidget = new VastVideoCtaButtonWidget(context, this.mVideoView.getId(), z2, z);
        getLayout().addView(this.mCtaButtonWidget);
        this.mCtaButtonWidget.setOnTouchListener(this.mClickThroughListener);
        String customCtaText = this.mVastVideoConfig.getCustomCtaText();
        if (customCtaText != null) {
            this.mCtaButtonWidget.updateCtaText(customCtaText);
        }
    }

    private void addCloseButtonWidget(@NonNull Context context, int initialVisibility) {
        this.mCloseButtonWidget = new VastVideoCloseButtonWidget(context);
        this.mCloseButtonWidget.setVisibility(initialVisibility);
        getLayout().addView(this.mCloseButtonWidget);
        this.mCloseButtonWidget.setOnTouchListenerToContent(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int access$300;
                if (VastVideoViewController.this.mIsVideoFinishedPlaying) {
                    access$300 = VastVideoViewController.this.mDuration;
                } else {
                    access$300 = VastVideoViewController.this.getCurrentPosition();
                }
                if (motionEvent.getAction() == 1) {
                    VastVideoViewController.this.mIsClosing = true;
                    VastVideoViewController.this.mVastVideoConfig.handleClose(VastVideoViewController.this.getContext(), access$300);
                    VastVideoViewController.this.getBaseVideoViewControllerListener().onFinish();
                }
                return true;
            }
        });
        String customSkipText = this.mVastVideoConfig.getCustomSkipText();
        if (customSkipText != null) {
            this.mCloseButtonWidget.updateCloseButtonText(customSkipText);
        }
        customSkipText = this.mVastVideoConfig.getCustomCloseIconUrl();
        if (customSkipText != null) {
            this.mCloseButtonWidget.updateCloseButtonIcon(customSkipText);
        }
    }

    private void addBlurredLastVideoFrameImageView(@NonNull Context context, int initialVisibility) {
        this.mBlurredLastVideoFrameImageView = new ImageView(context);
        this.mBlurredLastVideoFrameImageView.setVisibility(initialVisibility);
        getLayout().addView(this.mBlurredLastVideoFrameImageView, new LayoutParams(-1, -1));
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    @VisibleForTesting
    public View createCompanionAdView(@NonNull final Context context, @Nullable final VastCompanionAdConfig vastCompanionAdConfig, int initialVisibility) {
        Preconditions.checkNotNull(context);
        View view;
        if (vastCompanionAdConfig == null) {
            view = new View(context);
            view.setVisibility(4);
            return view;
        }
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(17);
        getLayout().addView(relativeLayout, new LayoutParams(-1, -1));
        view = VastWebView.createView(context, vastCompanionAdConfig.getVastResource());
        view.setVastWebViewClickListener(new VastWebViewClickListener() {
            public void onVastWebViewClick() {
                VastVideoViewController.this.broadcastAction(EventForwardingBroadcastReceiver.ACTION_INTERSTITIAL_CLICK);
                TrackingRequest.makeVastTrackingHttpRequest(vastCompanionAdConfig.getClickTrackers(), null, Integer.valueOf(VastVideoViewController.this.mDuration), null, context);
                vastCompanionAdConfig.handleClick(context, 1, null);
            }
        });
        view.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                vastCompanionAdConfig.handleClick(context, 1, url);
                return true;
            }
        });
        view.setVisibility(initialVisibility);
        LayoutParams layoutParams = new LayoutParams(Dips.dipsToIntPixels((float) (vastCompanionAdConfig.getWidth() + 16), context), Dips.dipsToIntPixels((float) (vastCompanionAdConfig.getHeight() + 16), context));
        layoutParams.addRule(13, -1);
        relativeLayout.addView(view, layoutParams);
        return view;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    @VisibleForTesting
    public View createIconView(@NonNull final Context context, @Nullable final VastIconConfig vastIconConfig, int initialVisibility) {
        Preconditions.checkNotNull(context);
        if (vastIconConfig == null) {
            return new View(context);
        }
        View createView = VastWebView.createView(context, vastIconConfig.getVastResource());
        createView.setVastWebViewClickListener(new VastWebViewClickListener() {
            public void onVastWebViewClick() {
                TrackingRequest.makeVastTrackingHttpRequest(vastIconConfig.getClickTrackingUris(), null, Integer.valueOf(VastVideoViewController.this.getCurrentPosition()), VastVideoViewController.this.getNetworkMediaFileUrl(), context);
                vastIconConfig.handleClick(VastVideoViewController.this.getContext(), null);
            }
        });
        createView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                vastIconConfig.handleClick(VastVideoViewController.this.getContext(), url);
                return true;
            }
        });
        createView.setVisibility(initialVisibility);
        LayoutParams layoutParams = new LayoutParams(Dips.asIntPixels((float) (vastIconConfig.getWidth() + 16), context), Dips.asIntPixels((float) (vastIconConfig.getHeight() + 16), context));
        layoutParams.addRule(9);
        layoutParams.addRule(10);
        getLayout().addView(createView, layoutParams);
        return createView;
    }

    /* access modifiers changed from: 0000 */
    public int getDuration() {
        return this.mVideoView.getDuration();
    }

    /* access modifiers changed from: 0000 */
    public int getCurrentPosition() {
        return this.mVideoView.getCurrentPosition();
    }

    /* access modifiers changed from: 0000 */
    public void makeVideoInteractable() {
        this.mShowCloseButtonEventFired = true;
        this.mRadialCountdownWidget.setVisibility(8);
        this.mCloseButtonWidget.setVisibility(0);
        this.mCtaButtonWidget.notifyVideoSkippable();
    }

    /* access modifiers changed from: 0000 */
    public boolean shouldBeInteractable() {
        return !this.mShowCloseButtonEventFired && getCurrentPosition() >= this.mShowCloseButtonDelay;
    }

    /* access modifiers changed from: 0000 */
    public void updateCountdown() {
        if (this.mIsCalibrationDone) {
            this.mRadialCountdownWidget.updateCountdownProgress(this.mShowCloseButtonDelay, getCurrentPosition());
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateProgressBar() {
        this.mProgressBarWidget.updateProgress(getCurrentPosition());
    }

    /* access modifiers changed from: 0000 */
    public String getNetworkMediaFileUrl() {
        if (this.mVastVideoConfig == null) {
            return null;
        }
        return this.mVastVideoConfig.getNetworkMediaFileUrl();
    }

    /* access modifiers changed from: 0000 */
    public void handleIconDisplay(int currentPosition) {
        if (this.mVastIconConfig != null && currentPosition >= this.mVastIconConfig.getOffsetMS()) {
            this.mIconView.setVisibility(0);
            this.mVastIconConfig.handleImpression(getContext(), currentPosition, getNetworkMediaFileUrl());
            if (this.mVastIconConfig.getDurationMS() != null && currentPosition >= this.mVastIconConfig.getOffsetMS() + this.mVastIconConfig.getDurationMS().intValue()) {
                this.mIconView.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean shouldAllowClickThrough() {
        return this.mShowCloseButtonEventFired;
    }

    private void startRunnables() {
        this.mProgressCheckerRunnable.startRepeating(VIDEO_PROGRESS_TIMER_CHECKER_DELAY);
        this.mCountdownRunnable.startRepeating(VIDEO_COUNTDOWN_UPDATE_INTERVAL);
    }

    /* access modifiers changed from: private */
    public void stopRunnables() {
        this.mProgressCheckerRunnable.stop();
        this.mCountdownRunnable.stop();
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoViewProgressRunnable getProgressCheckerRunnable() {
        return this.mProgressCheckerRunnable;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoViewCountdownRunnable getCountdownRunnable() {
        return this.mCountdownRunnable;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public boolean getHasSkipOffset() {
        return this.mHasSkipOffset;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public int getShowCloseButtonDelay() {
        return this.mShowCloseButtonDelay;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public boolean isShowCloseButtonEventFired() {
        return this.mShowCloseButtonEventFired;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setCloseButtonVisible(boolean visible) {
        this.mShowCloseButtonEventFired = visible;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public boolean isVideoFinishedPlaying() {
        return this.mIsVideoFinishedPlaying;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public boolean isCalibrationDone() {
        return this.mIsCalibrationDone;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public View getLandscapeCompanionAdView() {
        return this.mLandscapeCompanionAdView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public View getPortraitCompanionAdView() {
        return this.mPortraitCompanionAdView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public boolean getVideoError() {
        return this.mVideoError;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setVideoError() {
        this.mVideoError = true;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public View getIconView() {
        return this.mIconView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoGradientStripWidget getTopGradientStripWidget() {
        return this.mTopGradientStripWidget;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoGradientStripWidget getBottomGradientStripWidget() {
        return this.mBottomGradientStripWidget;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoProgressBarWidget getProgressBarWidget() {
        return this.mProgressBarWidget;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setProgressBarWidget(@NonNull VastVideoProgressBarWidget progressBarWidget) {
        this.mProgressBarWidget = progressBarWidget;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoRadialCountdownWidget getRadialCountdownWidget() {
        return this.mRadialCountdownWidget;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setRadialCountdownWidget(@NonNull VastVideoRadialCountdownWidget radialCountdownWidget) {
        this.mRadialCountdownWidget = radialCountdownWidget;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoCtaButtonWidget getCtaButtonWidget() {
        return this.mCtaButtonWidget;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoCloseButtonWidget getCloseButtonWidget() {
        return this.mCloseButtonWidget;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public ImageView getBlurredLastVideoFrameImageView() {
        return this.mBlurredLastVideoFrameImageView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VastVideoView getVastVideoView() {
        return this.mVideoView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setIsClosing(boolean isClosing) {
        this.mIsClosing = isClosing;
    }
}
