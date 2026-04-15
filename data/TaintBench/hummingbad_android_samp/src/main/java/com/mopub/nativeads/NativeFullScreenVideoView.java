package com.mopub.nativeads;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.mopub.common.Preconditions;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Drawables;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.VastVideoProgressBarWidget;
import com.mopub.mobileads.resource.CloseButtonDrawable;
import com.mopub.mobileads.resource.CtaButtonDrawable;
import com.mopub.mobileads.resource.DrawableConstants;
import com.mopub.mobileads.resource.DrawableConstants.GradientStrip;

@TargetApi(16)
public class NativeFullScreenVideoView extends RelativeLayout {
    @NonNull
    private final ImageView mBottomGradient;
    @NonNull
    private final ImageView mCachedVideoFrameView;
    @NonNull
    private final ImageView mCloseControl;
    @VisibleForTesting
    final int mCloseControlSizePx;
    @VisibleForTesting
    final int mClosePaddingPx;
    @NonNull
    private final ImageView mCtaButton;
    @VisibleForTesting
    final int mCtaHeightPx;
    @VisibleForTesting
    final int mCtaMarginPx;
    @VisibleForTesting
    final int mCtaWidthPx;
    @VisibleForTesting
    final int mGradientStripHeightPx;
    @NonNull
    private final ProgressBar mLoadingSpinner;
    @NonNull
    @VisibleForTesting
    Mode mMode;
    private int mOrientation;
    @NonNull
    private final View mOverlay;
    @NonNull
    private final ImageView mPlayButton;
    @VisibleForTesting
    final int mPlayControlSizePx;
    @NonNull
    private final ImageView mPrivacyInformationIcon;
    @VisibleForTesting
    final int mPrivacyInformationIconSizePx;
    @NonNull
    private final ImageView mTopGradient;
    @NonNull
    private final VastVideoProgressBarWidget mVideoProgress;
    @NonNull
    private final TextureView mVideoTexture;

    @VisibleForTesting
    static class LoadingBackground extends Drawable {
        @NonNull
        private final RectF mButtonRect;
        @VisibleForTesting
        final int mCornerRadiusPx;
        @NonNull
        private final Paint mPaint;

        LoadingBackground(@NonNull Context context) {
            this(context, new RectF(), new Paint());
        }

        LoadingBackground(@NonNull Context context, @NonNull RectF rectF, @NonNull Paint paint) {
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(rectF);
            Preconditions.checkNotNull(paint);
            this.mButtonRect = rectF;
            this.mPaint = paint;
            this.mPaint.setColor(-16777216);
            this.mPaint.setAlpha(128);
            this.mPaint.setAntiAlias(true);
            this.mCornerRadiusPx = Dips.asIntPixels(5.0f, context);
        }

        public void draw(Canvas canvas) {
            this.mButtonRect.set(getBounds());
            canvas.drawRoundRect(this.mButtonRect, (float) this.mCornerRadiusPx, (float) this.mCornerRadiusPx, this.mPaint);
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }

        public int getOpacity() {
            return 0;
        }
    }

    public enum Mode {
        LOADING,
        PLAYING,
        PAUSED,
        FINISHED
    }

    public NativeFullScreenVideoView(@NonNull Context context, int orientation, @Nullable String ctaText) {
        this(context, orientation, ctaText, new ImageView(context), new TextureView(context), new ProgressBar(context), new ImageView(context), new ImageView(context), new VastVideoProgressBarWidget(context), new View(context), new ImageView(context), new ImageView(context), new ImageView(context), new ImageView(context));
    }

    @VisibleForTesting
    NativeFullScreenVideoView(@NonNull Context context, int orientation, @Nullable String ctaText, @NonNull ImageView cachedImageView, @NonNull TextureView videoTexture, @NonNull ProgressBar loadingSpinner, @NonNull ImageView bottomGradient, @NonNull ImageView topGradient, @NonNull VastVideoProgressBarWidget videoProgress, @NonNull View overlay, @NonNull ImageView playButton, @NonNull ImageView privacyInformationIcon, @NonNull ImageView ctaButton, @NonNull ImageView closeControl) {
        super(context);
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(cachedImageView);
        Preconditions.checkNotNull(videoTexture);
        Preconditions.checkNotNull(loadingSpinner);
        Preconditions.checkNotNull(bottomGradient);
        Preconditions.checkNotNull(topGradient);
        Preconditions.checkNotNull(videoProgress);
        Preconditions.checkNotNull(overlay);
        Preconditions.checkNotNull(playButton);
        Preconditions.checkNotNull(privacyInformationIcon);
        Preconditions.checkNotNull(ctaButton);
        Preconditions.checkNotNull(closeControl);
        this.mOrientation = orientation;
        this.mMode = Mode.LOADING;
        this.mCtaWidthPx = Dips.asIntPixels(200.0f, context);
        this.mCtaHeightPx = Dips.asIntPixels(42.0f, context);
        this.mCtaMarginPx = Dips.asIntPixels(10.0f, context);
        this.mCloseControlSizePx = Dips.asIntPixels(50.0f, context);
        this.mClosePaddingPx = Dips.asIntPixels(8.0f, context);
        this.mPrivacyInformationIconSizePx = Dips.asIntPixels(44.0f, context);
        this.mPlayControlSizePx = Dips.asIntPixels(50.0f, context);
        this.mGradientStripHeightPx = Dips.asIntPixels(45.0f, context);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.mVideoTexture = videoTexture;
        this.mVideoTexture.setId((int) Utils.generateUniqueId());
        this.mVideoTexture.setLayoutParams(layoutParams);
        addView(this.mVideoTexture);
        this.mCachedVideoFrameView = cachedImageView;
        this.mCachedVideoFrameView.setId((int) Utils.generateUniqueId());
        this.mCachedVideoFrameView.setLayoutParams(layoutParams);
        this.mCachedVideoFrameView.setBackgroundColor(0);
        addView(this.mCachedVideoFrameView);
        layoutParams = new LayoutParams(this.mPlayControlSizePx, this.mPlayControlSizePx);
        layoutParams.addRule(13);
        this.mLoadingSpinner = loadingSpinner;
        this.mLoadingSpinner.setId((int) Utils.generateUniqueId());
        this.mLoadingSpinner.setBackground(new LoadingBackground(context));
        this.mLoadingSpinner.setLayoutParams(layoutParams);
        this.mLoadingSpinner.setIndeterminate(true);
        addView(this.mLoadingSpinner);
        layoutParams = new LayoutParams(-1, this.mGradientStripHeightPx);
        layoutParams.addRule(8, this.mVideoTexture.getId());
        this.mBottomGradient = bottomGradient;
        this.mBottomGradient.setId((int) Utils.generateUniqueId());
        this.mBottomGradient.setLayoutParams(layoutParams);
        this.mBottomGradient.setImageDrawable(new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{GradientStrip.START_COLOR, GradientStrip.END_COLOR}));
        addView(this.mBottomGradient);
        layoutParams = new LayoutParams(-1, this.mGradientStripHeightPx);
        layoutParams.addRule(10);
        this.mTopGradient = topGradient;
        this.mTopGradient.setId((int) Utils.generateUniqueId());
        this.mTopGradient.setLayoutParams(layoutParams);
        this.mTopGradient.setImageDrawable(new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{GradientStrip.START_COLOR, GradientStrip.END_COLOR}));
        addView(this.mTopGradient);
        this.mVideoProgress = videoProgress;
        this.mVideoProgress.setId((int) Utils.generateUniqueId());
        this.mVideoProgress.setAnchorId(this.mVideoTexture.getId());
        this.mVideoProgress.calibrateAndMakeVisible(1000, 0);
        addView(this.mVideoProgress);
        layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.mOverlay = overlay;
        this.mOverlay.setId((int) Utils.generateUniqueId());
        this.mOverlay.setLayoutParams(layoutParams);
        this.mOverlay.setBackgroundColor(DrawableConstants.TRANSPARENT_GRAY);
        addView(this.mOverlay);
        layoutParams = new LayoutParams(this.mPlayControlSizePx, this.mPlayControlSizePx);
        layoutParams.addRule(13);
        this.mPlayButton = playButton;
        this.mPlayButton.setId((int) Utils.generateUniqueId());
        this.mPlayButton.setLayoutParams(layoutParams);
        this.mPlayButton.setImageDrawable(Drawables.NATIVE_PLAY.createDrawable(context));
        addView(this.mPlayButton);
        this.mPrivacyInformationIcon = privacyInformationIcon;
        this.mPrivacyInformationIcon.setId((int) Utils.generateUniqueId());
        this.mPrivacyInformationIcon.setImageDrawable(Drawables.NATIVE_PRIVACY_INFORMATION_ICON.createDrawable(context));
        this.mPrivacyInformationIcon.setPadding(this.mClosePaddingPx, this.mClosePaddingPx, this.mClosePaddingPx * 2, this.mClosePaddingPx * 2);
        addView(this.mPrivacyInformationIcon);
        CtaButtonDrawable ctaButtonDrawable = new CtaButtonDrawable(context);
        if (!TextUtils.isEmpty(ctaText)) {
            ctaButtonDrawable.setCtaText(ctaText);
        }
        this.mCtaButton = ctaButton;
        this.mCtaButton.setId((int) Utils.generateUniqueId());
        this.mCtaButton.setImageDrawable(ctaButtonDrawable);
        addView(this.mCtaButton);
        this.mCloseControl = closeControl;
        this.mCloseControl.setId((int) Utils.generateUniqueId());
        this.mCloseControl.setImageDrawable(new CloseButtonDrawable());
        this.mCloseControl.setPadding(this.mClosePaddingPx * 3, this.mClosePaddingPx, this.mClosePaddingPx, this.mClosePaddingPx * 3);
        addView(this.mCloseControl);
        updateViewState();
    }

    public void resetProgress() {
        this.mVideoProgress.reset();
    }

    public void setMode(@NonNull Mode mode) {
        Preconditions.checkNotNull(mode);
        if (this.mMode != mode) {
            this.mMode = mode;
            updateViewState();
        }
    }

    @NonNull
    public TextureView getTextureView() {
        return this.mVideoTexture;
    }

    public void setOrientation(int orientation) {
        if (this.mOrientation != orientation) {
            this.mOrientation = orientation;
            updateViewState();
        }
    }

    public void setSurfaceTextureListener(@Nullable SurfaceTextureListener surfaceTextureListener) {
        this.mVideoTexture.setSurfaceTextureListener(surfaceTextureListener);
        SurfaceTexture surfaceTexture = this.mVideoTexture.getSurfaceTexture();
        if (surfaceTexture != null && surfaceTextureListener != null) {
            surfaceTextureListener.onSurfaceTextureAvailable(surfaceTexture, this.mVideoTexture.getWidth(), this.mVideoTexture.getHeight());
        }
    }

    public void setCloseControlListener(@Nullable OnClickListener closeControlListener) {
        this.mCloseControl.setOnClickListener(closeControlListener);
    }

    public void setPrivacyInformationClickListener(@Nullable OnClickListener privacyInformationListener) {
        this.mPrivacyInformationIcon.setOnClickListener(privacyInformationListener);
    }

    public void setCtaClickListener(@Nullable OnClickListener ctaListener) {
        this.mCtaButton.setOnClickListener(ctaListener);
    }

    public void setPlayControlClickListener(@Nullable OnClickListener playControlListener) {
        this.mPlayButton.setOnClickListener(playControlListener);
        this.mOverlay.setOnClickListener(playControlListener);
    }

    public void updateProgress(int progressPercentage) {
        this.mVideoProgress.updateProgress(progressPercentage);
    }

    public void setCachedVideoFrame(@Nullable Bitmap cachedVideoFrame) {
        this.mCachedVideoFrameView.setImageBitmap(cachedVideoFrame);
    }

    private void updateViewState() {
        switch (this.mMode) {
            case LOADING:
                setCachedImageVisibility(0);
                setLoadingSpinnerVisibility(0);
                setVideoProgressVisibility(4);
                setPlayButtonVisibility(4);
                break;
            case PLAYING:
                setCachedImageVisibility(4);
                setLoadingSpinnerVisibility(4);
                setVideoProgressVisibility(0);
                setPlayButtonVisibility(4);
                break;
            case PAUSED:
                setCachedImageVisibility(4);
                setLoadingSpinnerVisibility(4);
                setVideoProgressVisibility(0);
                setPlayButtonVisibility(0);
                break;
            case FINISHED:
                setCachedImageVisibility(0);
                setLoadingSpinnerVisibility(4);
                setVideoProgressVisibility(4);
                setPlayButtonVisibility(0);
                break;
        }
        updateVideoTextureLayout();
        updateControlLayouts();
    }

    private void setCachedImageVisibility(int visibility) {
        this.mCachedVideoFrameView.setVisibility(visibility);
    }

    private void setLoadingSpinnerVisibility(int visibility) {
        this.mLoadingSpinner.setVisibility(visibility);
    }

    private void setVideoProgressVisibility(int visibility) {
        this.mVideoProgress.setVisibility(visibility);
    }

    private void setPlayButtonVisibility(int visibility) {
        this.mPlayButton.setVisibility(visibility);
        this.mOverlay.setVisibility(visibility);
    }

    private void updateVideoTextureLayout() {
        Configuration configuration = getContext().getResources().getConfiguration();
        ViewGroup.LayoutParams layoutParams = this.mVideoTexture.getLayoutParams();
        int dipsToIntPixels = Dips.dipsToIntPixels((float) configuration.screenWidthDp, getContext());
        if (dipsToIntPixels != layoutParams.width) {
            layoutParams.width = dipsToIntPixels;
        }
        int dipsToIntPixels2 = Dips.dipsToIntPixels((((float) configuration.screenWidthDp) * 9.0f) / 16.0f, getContext());
        if (dipsToIntPixels2 != layoutParams.height) {
            layoutParams.height = dipsToIntPixels2;
        }
    }

    private void updateControlLayouts() {
        LayoutParams layoutParams = new LayoutParams(this.mCtaWidthPx, this.mCtaHeightPx);
        layoutParams.setMargins(this.mCtaMarginPx, this.mCtaMarginPx, this.mCtaMarginPx, this.mCtaMarginPx);
        LayoutParams layoutParams2 = new LayoutParams(this.mPrivacyInformationIconSizePx, this.mPrivacyInformationIconSizePx);
        LayoutParams layoutParams3 = new LayoutParams(this.mCloseControlSizePx, this.mCloseControlSizePx);
        switch (this.mOrientation) {
            case 1:
                layoutParams.addRule(3, this.mVideoTexture.getId());
                layoutParams.addRule(14);
                layoutParams2.addRule(10);
                layoutParams2.addRule(9);
                layoutParams3.addRule(10);
                layoutParams3.addRule(11);
                break;
            case 2:
                layoutParams.addRule(2, this.mVideoProgress.getId());
                layoutParams.addRule(11);
                layoutParams2.addRule(6, this.mVideoTexture.getId());
                layoutParams2.addRule(5, this.mVideoTexture.getId());
                layoutParams3.addRule(6, this.mVideoTexture.getId());
                layoutParams3.addRule(7, this.mVideoTexture.getId());
                break;
        }
        this.mCtaButton.setLayoutParams(layoutParams);
        this.mPrivacyInformationIcon.setLayoutParams(layoutParams2);
        this.mCloseControl.setLayoutParams(layoutParams3);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public ImageView getCtaButton() {
        return this.mCtaButton;
    }
}
