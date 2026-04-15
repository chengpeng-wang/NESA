package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.resource.ProgressBarDrawable;

public class VastVideoProgressBarWidget extends ImageView {
    @NonNull
    private ProgressBarDrawable mProgressBarDrawable;
    private final int mProgressBarHeight;

    public VastVideoProgressBarWidget(@NonNull Context context) {
        super(context);
        setId((int) Utils.generateUniqueId());
        this.mProgressBarDrawable = new ProgressBarDrawable(context);
        setImageDrawable(this.mProgressBarDrawable);
        this.mProgressBarHeight = Dips.dipsToIntPixels(4.0f, context);
    }

    public void setAnchorId(int anchorId) {
        LayoutParams layoutParams = new LayoutParams(-1, this.mProgressBarHeight);
        layoutParams.addRule(8, anchorId);
        setLayoutParams(layoutParams);
    }

    public void calibrateAndMakeVisible(int duration, int skipOffset) {
        this.mProgressBarDrawable.setDurationAndSkipOffset(duration, skipOffset);
        setVisibility(0);
    }

    public void updateProgress(int progress) {
        this.mProgressBarDrawable.setProgress(progress);
    }

    public void reset() {
        this.mProgressBarDrawable.reset();
        this.mProgressBarDrawable.setProgress(0);
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public ProgressBarDrawable getImageViewDrawable() {
        return this.mProgressBarDrawable;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setImageViewDrawable(@NonNull ProgressBarDrawable drawable) {
        this.mProgressBarDrawable = drawable;
    }
}
