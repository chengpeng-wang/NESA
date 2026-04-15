package com.google.android.gms.ads.doubleclick;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.internal.client.zzy;

public final class PublisherAdView extends ViewGroup {
    private final zzy zznS;

    public PublisherAdView(Context context) {
        super(context);
        this.zznS = new zzy(this);
    }

    public PublisherAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zznS = new zzy(this, attrs, true);
    }

    public PublisherAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.zznS = new zzy(this, attrs, true);
    }

    public void destroy() {
        this.zznS.destroy();
    }

    public AdListener getAdListener() {
        return this.zznS.getAdListener();
    }

    public AdSize getAdSize() {
        return this.zznS.getAdSize();
    }

    public AdSize[] getAdSizes() {
        return this.zznS.getAdSizes();
    }

    public String getAdUnitId() {
        return this.zznS.getAdUnitId();
    }

    public AppEventListener getAppEventListener() {
        return this.zznS.getAppEventListener();
    }

    public String getMediationAdapterClassName() {
        return this.zznS.getMediationAdapterClassName();
    }

    public OnCustomRenderedAdLoadedListener getOnCustomRenderedAdLoadedListener() {
        return this.zznS.getOnCustomRenderedAdLoadedListener();
    }

    public void loadAd(PublisherAdRequest publisherAdRequest) {
        this.zznS.zza(publisherAdRequest.zzaF());
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View childAt = getChildAt(0);
        if (childAt != null && childAt.getVisibility() != 8) {
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            int i = ((right - left) - measuredWidth) / 2;
            int i2 = ((bottom - top) - measuredHeight) / 2;
            childAt.layout(i, i2, measuredWidth + i, measuredHeight + i2);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthInPixels;
        int i = 0;
        View childAt = getChildAt(0);
        if (childAt == null || childAt.getVisibility() == 8) {
            AdSize adSize = getAdSize();
            if (adSize != null) {
                Context context = getContext();
                widthInPixels = adSize.getWidthInPixels(context);
                i = adSize.getHeightInPixels(context);
            } else {
                widthInPixels = 0;
            }
        } else {
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            widthInPixels = childAt.getMeasuredWidth();
            i = childAt.getMeasuredHeight();
        }
        setMeasuredDimension(View.resolveSize(Math.max(widthInPixels, getSuggestedMinimumWidth()), widthMeasureSpec), View.resolveSize(Math.max(i, getSuggestedMinimumHeight()), heightMeasureSpec));
    }

    public void pause() {
        this.zznS.pause();
    }

    public void recordManualImpression() {
        this.zznS.recordManualImpression();
    }

    public void resume() {
        this.zznS.resume();
    }

    public void setAdListener(AdListener adListener) {
        this.zznS.setAdListener(adListener);
    }

    public void setAdSizes(AdSize... adSizes) {
        if (adSizes == null || adSizes.length < 1) {
            throw new IllegalArgumentException("The supported ad sizes must contain at least one valid ad size.");
        }
        this.zznS.zza(adSizes);
    }

    public void setAdUnitId(String adUnitId) {
        this.zznS.setAdUnitId(adUnitId);
    }

    public void setAppEventListener(AppEventListener appEventListener) {
        this.zznS.setAppEventListener(appEventListener);
    }

    public void setManualImpressionsEnabled(boolean manualImpressionsEnabled) {
        this.zznS.setManualImpressionsEnabled(manualImpressionsEnabled);
    }

    public void setOnCustomRenderedAdLoadedListener(OnCustomRenderedAdLoadedListener onCustomRenderedAdLoadedListener) {
        this.zznS.setOnCustomRenderedAdLoadedListener(onCustomRenderedAdLoadedListener);
    }
}
