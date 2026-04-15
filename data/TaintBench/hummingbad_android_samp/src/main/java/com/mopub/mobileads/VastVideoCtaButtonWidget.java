package com.mopub.mobileads;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.resource.CtaButtonDrawable;

public class VastVideoCtaButtonWidget extends ImageView {
    @NonNull
    private CtaButtonDrawable mCtaButtonDrawable;
    private boolean mHasClickthroughUrl;
    private boolean mHasCompanionAd;
    private boolean mIsVideoComplete;
    private boolean mIsVideoSkippable;
    @NonNull
    private final LayoutParams mLandscapeLayoutParams;
    @NonNull
    private final LayoutParams mPortraitLayoutParams;

    public VastVideoCtaButtonWidget(@NonNull Context context, int videoViewId, boolean hasCompanionAd, boolean hasClickthroughUrl) {
        super(context);
        this.mHasCompanionAd = hasCompanionAd;
        this.mHasClickthroughUrl = hasClickthroughUrl;
        setId((int) Utils.generateUniqueId());
        int dipsToIntPixels = Dips.dipsToIntPixels(200.0f, context);
        int dipsToIntPixels2 = Dips.dipsToIntPixels(42.0f, context);
        int dipsToIntPixels3 = Dips.dipsToIntPixels(16.0f, context);
        this.mCtaButtonDrawable = new CtaButtonDrawable(context);
        setImageDrawable(this.mCtaButtonDrawable);
        this.mLandscapeLayoutParams = new LayoutParams(dipsToIntPixels, dipsToIntPixels2);
        this.mLandscapeLayoutParams.setMargins(dipsToIntPixels3, dipsToIntPixels3, dipsToIntPixels3, dipsToIntPixels3);
        this.mLandscapeLayoutParams.addRule(8, videoViewId);
        this.mLandscapeLayoutParams.addRule(7, videoViewId);
        this.mPortraitLayoutParams = new LayoutParams(dipsToIntPixels, dipsToIntPixels2);
        this.mPortraitLayoutParams.setMargins(dipsToIntPixels3, dipsToIntPixels3, dipsToIntPixels3, dipsToIntPixels3);
        this.mPortraitLayoutParams.addRule(3, videoViewId);
        this.mPortraitLayoutParams.addRule(14);
        updateLayoutAndVisibility();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateLayoutAndVisibility();
    }

    /* access modifiers changed from: 0000 */
    public void updateCtaText(@NonNull String customCtaText) {
        this.mCtaButtonDrawable.setCtaText(customCtaText);
    }

    /* access modifiers changed from: 0000 */
    public void notifyVideoSkippable() {
        this.mIsVideoSkippable = true;
        updateLayoutAndVisibility();
    }

    /* access modifiers changed from: 0000 */
    public void notifyVideoComplete() {
        this.mIsVideoSkippable = true;
        this.mIsVideoComplete = true;
        updateLayoutAndVisibility();
    }

    private void updateLayoutAndVisibility() {
        if (!this.mHasClickthroughUrl) {
            setVisibility(8);
        } else if (!this.mIsVideoSkippable) {
            setVisibility(4);
        } else if (this.mIsVideoComplete && this.mHasCompanionAd) {
            setVisibility(8);
        } else {
            switch (getResources().getConfiguration().orientation) {
                case 0:
                    MoPubLog.d("Screen orientation undefined: CTA button widget defaulting to portrait layout");
                    setVisibility(0);
                    setLayoutParams(this.mPortraitLayoutParams);
                    return;
                case 1:
                    setVisibility(0);
                    setLayoutParams(this.mPortraitLayoutParams);
                    return;
                case 2:
                    setVisibility(0);
                    setLayoutParams(this.mLandscapeLayoutParams);
                    return;
                case 3:
                    MoPubLog.d("Screen orientation is deprecated ORIENTATION_SQUARE: CTA button widget defaulting to portrait layout");
                    setVisibility(0);
                    setLayoutParams(this.mPortraitLayoutParams);
                    return;
                default:
                    MoPubLog.d("Unrecognized screen orientation: CTA button widget defaulting to portrait layout");
                    setVisibility(0);
                    setLayoutParams(this.mPortraitLayoutParams);
                    return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public String getCtaText() {
        return this.mCtaButtonDrawable.getCtaText();
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public boolean hasPortraitLayoutParams() {
        return getLayoutParams().equals(this.mPortraitLayoutParams);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public boolean hasLandscapeLayoutParams() {
        return getLayoutParams().equals(this.mLandscapeLayoutParams);
    }
}
