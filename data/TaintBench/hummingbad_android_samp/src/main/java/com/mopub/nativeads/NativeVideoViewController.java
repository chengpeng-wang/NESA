package com.mopub.nativeads;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.VideoView;
import com.mopub.common.Preconditions;
import com.mopub.common.UrlAction;
import com.mopub.common.UrlHandler.Builder;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.BaseVideoViewController;
import com.mopub.mobileads.BaseVideoViewController.BaseVideoViewControllerListener;
import com.mopub.mobileads.VastVideoConfig;
import com.mopub.nativeads.NativeFullScreenVideoView.Mode;
import com.mopub.nativeads.NativeVideoController.Listener;
import com.mopub.nativeads.NativeVideoController.NativeVideoProgressRunnable.ProgressListener;

@TargetApi(16)
public class NativeVideoViewController extends BaseVideoViewController implements OnAudioFocusChangeListener, SurfaceTextureListener, Listener {
    @NonNull
    public static final String NATIVE_VAST_VIDEO_CONFIG = "native_vast_video_config";
    @NonNull
    public static final String NATIVE_VIDEO_ID = "native_video_id";
    /* access modifiers changed from: private */
    @Nullable
    public Bitmap mCachedVideoFrame;
    /* access modifiers changed from: private */
    public boolean mEnded;
    private boolean mError;
    /* access modifiers changed from: private|final */
    @NonNull
    public final NativeFullScreenVideoView mFullScreenVideoView;
    private int mLatestVideoControllerState;
    /* access modifiers changed from: private|final */
    @NonNull
    public final NativeVideoController mNativeVideoController;
    @NonNull
    private VastVideoConfig mVastVideoConfig;
    @NonNull
    private VideoState mVideoState;

    enum VideoState {
        NONE,
        LOADING,
        BUFFERING,
        PAUSED,
        PLAYING,
        ENDED,
        FAILED_LOAD
    }

    public NativeVideoViewController(@NonNull Context context, @NonNull Bundle intentExtras, @NonNull Bundle savedInstanceState, @NonNull BaseVideoViewControllerListener baseVideoViewControllerListener) {
        this(context, intentExtras, savedInstanceState, baseVideoViewControllerListener, new NativeFullScreenVideoView(context, context.getResources().getConfiguration().orientation, ((VastVideoConfig) intentExtras.get(NATIVE_VAST_VIDEO_CONFIG)).getCustomCtaText()));
    }

    @VisibleForTesting
    NativeVideoViewController(@NonNull Context context, @NonNull Bundle intentExtras, @NonNull Bundle savedInstanceState, @NonNull BaseVideoViewControllerListener baseVideoViewControllerListener, @NonNull NativeFullScreenVideoView fullScreenVideoView) {
        super(context, null, baseVideoViewControllerListener);
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(intentExtras);
        Preconditions.checkNotNull(baseVideoViewControllerListener);
        Preconditions.checkNotNull(fullScreenVideoView);
        this.mVideoState = VideoState.NONE;
        this.mVastVideoConfig = (VastVideoConfig) intentExtras.get(NATIVE_VAST_VIDEO_CONFIG);
        this.mFullScreenVideoView = fullScreenVideoView;
        this.mNativeVideoController = NativeVideoController.getForId(((Long) intentExtras.get(NATIVE_VIDEO_ID)).longValue());
        Preconditions.checkNotNull(this.mVastVideoConfig);
        Preconditions.checkNotNull(this.mNativeVideoController);
    }

    /* access modifiers changed from: protected */
    public VideoView getVideoView() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void onCreate() {
        this.mFullScreenVideoView.setSurfaceTextureListener(this);
        this.mFullScreenVideoView.setMode(Mode.LOADING);
        this.mFullScreenVideoView.setPlayControlClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (NativeVideoViewController.this.mEnded) {
                    NativeVideoViewController.this.mEnded = false;
                    NativeVideoViewController.this.mFullScreenVideoView.resetProgress();
                    NativeVideoViewController.this.mNativeVideoController.seekTo(0);
                }
                NativeVideoViewController.this.applyState(VideoState.PLAYING);
            }
        });
        this.mFullScreenVideoView.setCloseControlListener(new OnClickListener() {
            public void onClick(View v) {
                NativeVideoViewController.this.applyState(VideoState.PAUSED, true);
                NativeVideoViewController.this.getBaseVideoViewControllerListener().onFinish();
            }
        });
        this.mFullScreenVideoView.setCtaClickListener(new OnClickListener() {
            public void onClick(View v) {
                NativeVideoViewController.this.mNativeVideoController.setPlayWhenReady(false);
                NativeVideoViewController.this.mCachedVideoFrame = NativeVideoViewController.this.mFullScreenVideoView.getTextureView().getBitmap();
                NativeVideoViewController.this.mNativeVideoController.handleCtaClick((Activity) NativeVideoViewController.this.getContext());
            }
        });
        this.mFullScreenVideoView.setPrivacyInformationClickListener(new OnClickListener() {
            public void onClick(View v) {
                NativeVideoViewController.this.mNativeVideoController.setPlayWhenReady(false);
                NativeVideoViewController.this.mCachedVideoFrame = NativeVideoViewController.this.mFullScreenVideoView.getTextureView().getBitmap();
                new Builder().withSupportedUrlActions(UrlAction.OPEN_IN_APP_BROWSER, new UrlAction[0]).build().handleUrl(NativeVideoViewController.this.getContext(), "https://www.mopub.com/optout/");
            }
        });
        this.mFullScreenVideoView.setLayoutParams(new LayoutParams(-1, -1));
        getBaseVideoViewControllerListener().onSetContentView(this.mFullScreenVideoView);
        this.mNativeVideoController.setProgressListener(new ProgressListener() {
            public void updateProgress(int progressTenthPercent) {
                NativeVideoViewController.this.mFullScreenVideoView.updateProgress(progressTenthPercent);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        if (this.mCachedVideoFrame != null) {
            this.mFullScreenVideoView.setCachedVideoFrame(this.mCachedVideoFrame);
        }
        this.mNativeVideoController.prepare(this);
        this.mNativeVideoController.setListener(this);
        this.mNativeVideoController.setOnAudioFocusChangeListener(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        this.mFullScreenVideoView.setOrientation(configuration.orientation);
    }

    /* access modifiers changed from: protected */
    public void onBackPressed() {
        applyState(VideoState.PAUSED, true);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.mNativeVideoController.setTextureView(this.mFullScreenVideoView.getTextureView());
        if (!this.mEnded) {
            this.mNativeVideoController.seekTo(this.mNativeVideoController.getCurrentPosition());
        }
        this.mNativeVideoController.setPlayWhenReady(!this.mEnded);
        if (this.mNativeVideoController.getDuration() - this.mNativeVideoController.getCurrentPosition() < 750) {
            this.mEnded = true;
            maybeChangeState();
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        this.mNativeVideoController.release(this);
        applyState(VideoState.PAUSED);
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public void onStateChanged(boolean playWhenReady, int playbackState) {
        this.mLatestVideoControllerState = playbackState;
        maybeChangeState();
    }

    public void onError(Exception e) {
        MoPubLog.w("Error playing back video.", e);
        this.mError = true;
        maybeChangeState();
    }

    public void onAudioFocusChange(int focusChange) {
        if (focusChange == -1 || focusChange == -2) {
            applyState(VideoState.PAUSED);
        } else if (focusChange == -3) {
            this.mNativeVideoController.setAudioVolume(0.3f);
        } else if (focusChange == 1) {
            this.mNativeVideoController.setAudioVolume(1.0f);
            maybeChangeState();
        }
    }

    /* JADX WARNING: Missing block: B:21:0x0035, code skipped:
            if (r3.mLatestVideoControllerState != 6) goto L_0x0008;
     */
    private void maybeChangeState() {
        /*
        r3 = this;
        r0 = r3.mVideoState;
        r1 = r3.mError;
        if (r1 == 0) goto L_0x000c;
    L_0x0006:
        r0 = com.mopub.nativeads.NativeVideoViewController.VideoState.FAILED_LOAD;
    L_0x0008:
        r3.applyState(r0);
        return;
    L_0x000c:
        r1 = r3.mEnded;
        if (r1 != 0) goto L_0x0037;
    L_0x0010:
        r1 = r3.mLatestVideoControllerState;
        r2 = 2;
        if (r1 == r2) goto L_0x001a;
    L_0x0015:
        r1 = r3.mLatestVideoControllerState;
        r2 = 1;
        if (r1 != r2) goto L_0x001d;
    L_0x001a:
        r0 = com.mopub.nativeads.NativeVideoViewController.VideoState.LOADING;
        goto L_0x0008;
    L_0x001d:
        r1 = r3.mLatestVideoControllerState;
        r2 = 3;
        if (r1 != r2) goto L_0x0025;
    L_0x0022:
        r0 = com.mopub.nativeads.NativeVideoViewController.VideoState.BUFFERING;
        goto L_0x0008;
    L_0x0025:
        r1 = r3.mLatestVideoControllerState;
        r2 = 4;
        if (r1 != r2) goto L_0x002d;
    L_0x002a:
        r0 = com.mopub.nativeads.NativeVideoViewController.VideoState.PLAYING;
        goto L_0x0008;
    L_0x002d:
        r1 = r3.mLatestVideoControllerState;
        r2 = 5;
        if (r1 == r2) goto L_0x0037;
    L_0x0032:
        r1 = r3.mLatestVideoControllerState;
        r2 = 6;
        if (r1 != r2) goto L_0x0008;
    L_0x0037:
        r0 = com.mopub.nativeads.NativeVideoViewController.VideoState.ENDED;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.nativeads.NativeVideoViewController.maybeChangeState():void");
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void applyState(@NonNull VideoState videoState) {
        applyState(videoState, false);
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public void applyState(@NonNull VideoState videoState, boolean transitionToInline) {
        Preconditions.checkNotNull(videoState);
        if (this.mVideoState != videoState) {
            switch (videoState) {
                case FAILED_LOAD:
                    this.mNativeVideoController.setPlayWhenReady(false);
                    this.mNativeVideoController.setAudioEnabled(false);
                    this.mNativeVideoController.setAppAudioEnabled(false);
                    this.mFullScreenVideoView.setMode(Mode.LOADING);
                    this.mVastVideoConfig.handleError(getContext(), null, 0);
                    break;
                case LOADING:
                case BUFFERING:
                    this.mNativeVideoController.setPlayWhenReady(true);
                    this.mFullScreenVideoView.setMode(Mode.LOADING);
                    break;
                case PLAYING:
                    this.mNativeVideoController.setPlayWhenReady(true);
                    this.mNativeVideoController.setAudioEnabled(true);
                    this.mNativeVideoController.setAppAudioEnabled(true);
                    this.mFullScreenVideoView.setMode(Mode.PLAYING);
                    break;
                case PAUSED:
                    if (!transitionToInline) {
                        this.mNativeVideoController.setAppAudioEnabled(false);
                    }
                    this.mNativeVideoController.setPlayWhenReady(false);
                    this.mFullScreenVideoView.setMode(Mode.PAUSED);
                    break;
                case ENDED:
                    this.mEnded = true;
                    this.mNativeVideoController.setAppAudioEnabled(false);
                    this.mFullScreenVideoView.updateProgress(1000);
                    this.mFullScreenVideoView.setMode(Mode.FINISHED);
                    this.mVastVideoConfig.handleComplete(getContext(), 0);
                    break;
            }
            this.mVideoState = videoState;
        }
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public NativeFullScreenVideoView getNativeFullScreenVideoView() {
        return this.mFullScreenVideoView;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public VideoState getVideoState() {
        return this.mVideoState;
    }
}
