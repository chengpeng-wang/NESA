package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import com.mopub.common.VisibleForTesting;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Utils;
import com.mopub.mobileads.resource.RadialCountdownDrawable;

public class VastVideoRadialCountdownWidget extends ImageView {
    private int mLastProgressMilliseconds;
    @NonNull
    private RadialCountdownDrawable mRadialCountdownDrawable;

    public VastVideoRadialCountdownWidget(@NonNull Context context) {
        super(context);
        setId((int) Utils.generateUniqueId());
        int dipsToIntPixels = Dips.dipsToIntPixels(45.0f, context);
        int dipsToIntPixels2 = Dips.dipsToIntPixels(16.0f, context);
        int dipsToIntPixels3 = Dips.dipsToIntPixels(16.0f, context);
        int dipsToIntPixels4 = Dips.dipsToIntPixels(5.0f, context);
        this.mRadialCountdownDrawable = new RadialCountdownDrawable(context);
        setImageDrawable(this.mRadialCountdownDrawable);
        setPadding(dipsToIntPixels4, dipsToIntPixels4, dipsToIntPixels4, dipsToIntPixels4);
        LayoutParams layoutParams = new LayoutParams(dipsToIntPixels, dipsToIntPixels);
        layoutParams.setMargins(0, dipsToIntPixels2, dipsToIntPixels3, 0);
        layoutParams.addRule(11);
        setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: 0000 */
    public void calibrateAndMakeVisible(int initialCountdownMilliseconds) {
        this.mRadialCountdownDrawable.setInitialCountdown(initialCountdownMilliseconds);
        setVisibility(0);
    }

    /* access modifiers changed from: 0000 */
    public void updateCountdownProgress(int initialCountdownMilliseconds, int currentProgressMilliseconds) {
        if (currentProgressMilliseconds < this.mLastProgressMilliseconds) {
            return;
        }
        if (initialCountdownMilliseconds - currentProgressMilliseconds < 0) {
            setVisibility(8);
            return;
        }
        this.mRadialCountdownDrawable.updateCountdownProgress(currentProgressMilliseconds);
        this.mLastProgressMilliseconds = currentProgressMilliseconds;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public RadialCountdownDrawable getImageViewDrawable() {
        return this.mRadialCountdownDrawable;
    }

    /* access modifiers changed from: 0000 */
    @Deprecated
    @VisibleForTesting
    public void setImageViewDrawable(RadialCountdownDrawable drawable) {
        this.mRadialCountdownDrawable = drawable;
    }
}
