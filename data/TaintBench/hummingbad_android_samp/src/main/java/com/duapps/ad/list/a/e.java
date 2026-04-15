package com.duapps.ad.list.a;

import android.content.Context;
import android.view.View;
import com.duapps.ad.DuAdDataCallBack;
import com.duapps.ad.DuClickCallback;
import com.duapps.ad.base.d;
import com.duapps.ad.entity.strategy.NativeAd;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd.Rating;
import java.util.List;

/* compiled from: NativeFBWrapper */
public final class e implements NativeAd {
    private com.facebook.ads.NativeAd a;
    /* access modifiers changed from: private */
    public Context b;
    /* access modifiers changed from: private */
    public DuAdDataCallBack c;
    /* access modifiers changed from: private */
    public int d;
    private long e;
    private AdListener f = new f(this);

    public e(com.facebook.ads.NativeAd nativeAd, Context context, int i, long j) {
        this.e = j;
        this.a = nativeAd;
        this.b = context;
        this.d = i;
        nativeAd.setAdListener(this.f);
    }

    public final void setMobulaAdListener(DuAdDataCallBack duAdDataCallBack) {
        this.c = duAdDataCallBack;
    }

    public final void registerViewForInteraction(View view) {
        try {
            this.a.registerViewForInteraction(view);
            d.e(this.b, this.d);
        } catch (Exception e) {
        }
    }

    public final void registerViewForInteraction(View view, List<View> list) {
        try {
            this.a.registerViewForInteraction(view, list);
            d.e(this.b, this.d);
        } catch (Exception e) {
        }
    }

    public final void unregisterView() {
        this.a.unregisterView();
    }

    public final void destroy() {
        this.a.destroy();
    }

    public final String getId() {
        return this.a.getId();
    }

    public final String getAdCoverImageUrl() {
        if (this.a == null || this.a.getAdCoverImage() == null) {
            return null;
        }
        return this.a.getAdCoverImage().getUrl();
    }

    public final String getAdIconUrl() {
        if (this.a == null || this.a.getAdIcon() == null) {
            return null;
        }
        return this.a.getAdIcon().getUrl();
    }

    public final String getAdSocialContext() {
        return this.a.getAdSocialContext();
    }

    public final String getAdCallToAction() {
        return this.a.getAdCallToAction();
    }

    public final String getAdBody() {
        return this.a.getAdBody();
    }

    public final String getAdTitle() {
        return this.a.getAdTitle();
    }

    public final float getAdStarRating() {
        Rating adStarRating = this.a.getAdStarRating();
        if (adStarRating != null) {
            return (float) adStarRating.getValue();
        }
        return 4.5f;
    }

    public final boolean isValid() {
        long currentTimeMillis = System.currentTimeMillis() - this.e;
        return currentTimeMillis < 3300000 && currentTimeMillis > 0;
    }

    public final int getAdChannelType() {
        return 1;
    }

    public final void setProcessClickUrlCallback(DuClickCallback duClickCallback) {
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        e eVar = (e) obj;
        if (eVar.getAdTitle() == null || this.a.getAdTitle() == null) {
            return false;
        }
        if (this.a.getAdTitle().equals(eVar.getAdTitle())) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return (this.a.getAdTitle() == null ? 0 : this.a.getAdTitle().hashCode()) + 31;
    }

    public final Object getRealData() {
        return this.a;
    }

    public final String getSourceType() {
        return null;
    }

    public final String getAdSource() {
        return null;
    }
}
