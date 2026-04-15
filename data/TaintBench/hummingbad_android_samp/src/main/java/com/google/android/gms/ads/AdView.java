package com.google.android.gms.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.internal.client.zza;
import com.google.android.gms.ads.internal.client.zzy;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;
import com.google.android.gms.ads.purchase.PlayStorePurchaseListener;

public final class AdView extends ViewGroup {
    private final zzy zznS;

    public AdView(Context context) {
        super(context);
        this.zznS = new zzy(this);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zznS = new zzy(this, attrs, false);
    }

    public AdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.zznS = new zzy(this, attrs, false);
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

    public String getAdUnitId() {
        return this.zznS.getAdUnitId();
    }

    public InAppPurchaseListener getInAppPurchaseListener() {
        return this.zznS.getInAppPurchaseListener();
    }

    public String getMediationAdapterClassName() {
        return this.zznS.getMediationAdapterClassName();
    }

    public boolean isLoading() {
        return this.zznS.isLoading();
    }

    public void loadAd(AdRequest adRequest) {
        this.zznS.zza(adRequest.zzaF());
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

    public void resume() {
        this.zznS.resume();
    }

    public void setAdListener(AdListener adListener) {
        this.zznS.setAdListener(adListener);
        if (adListener != null && (adListener instanceof zza)) {
            this.zznS.zza((zza) adListener);
        } else if (adListener == null) {
            this.zznS.zza(null);
        }
    }

    public void setAdSize(AdSize adSize) {
        this.zznS.setAdSizes(adSize);
    }

    public void setAdUnitId(String adUnitId) {
        this.zznS.setAdUnitId(adUnitId);
    }

    public void setInAppPurchaseListener(InAppPurchaseListener inAppPurchaseListener) {
        this.zznS.setInAppPurchaseListener(inAppPurchaseListener);
    }

    public void setPlayStorePurchaseParams(PlayStorePurchaseListener playStorePurchaseListener, String publicKey) {
        this.zznS.setPlayStorePurchaseParams(playStorePurchaseListener, publicKey);
    }
}
