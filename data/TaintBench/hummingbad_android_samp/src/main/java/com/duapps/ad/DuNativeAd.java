package com.duapps.ad;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.duapps.ad.base.f;
import com.duapps.ad.base.i;
import com.duapps.ad.entity.strategy.NativeAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.List;

public class DuNativeAd {
    private Context a;
    /* access modifiers changed from: private */
    public NativeAd b;
    /* access modifiers changed from: private */
    public DuAdListener c;
    private int d;
    private a e;
    private View f;
    /* access modifiers changed from: private */
    public DuClickCallback g;
    private DuAdDataCallBack h;

    public DuNativeAd(Context context, int i) {
        this(context, i, 1);
    }

    public DuNativeAd(Context context, int i, int i2) {
        this.h = new f(this);
        this.a = context;
        this.d = i;
        this.e = (a) PullRequestController.getInstance(context.getApplicationContext()).getPullController(this.d, i2);
    }

    public void setFbids(List<String> list) {
        if (list == null || list.size() <= 0) {
            Log.e("DuNativeAdError", "NativeAds fbID couldn't be null");
            return;
        }
        f.c(MASTNativeAdConstants.REQUESTPARAM_TEST, "change FBID :" + list.toString());
        this.e.a((List) list);
    }

    public void setMobulaAdListener(DuAdListener duAdListener) {
        this.c = duAdListener;
    }

    public void setProcessClickCallback(DuClickCallback duClickCallback) {
        this.g = duClickCallback;
    }

    public void fill() {
        if (i.i(this.a)) {
            this.e.fill();
            i.j(this.a);
            return;
        }
        this.h.onAdError(AdError.LOAD_TOO_FREQUENTLY);
    }

    public void registerViewForInteraction(View view) {
        if (isAdLoaded()) {
            if (this.f != null) {
                unregisterView();
            }
            this.f = view;
            this.b.registerViewForInteraction(view);
        }
    }

    public void registerViewForInteraction(View view, List<View> list) {
        if (isAdLoaded()) {
            if (this.f != null) {
                unregisterView();
            }
            this.f = view;
            this.b.registerViewForInteraction(view, list);
        }
    }

    public boolean isAdLoaded() {
        return this.b != null;
    }

    public void unregisterView() {
        if (isAdLoaded()) {
            this.b.unregisterView();
        }
    }

    public void load() {
        if (i.f(this.a)) {
            this.e.a(null);
            this.e.a(this.h);
            this.e.load();
            i.k(this.a);
            return;
        }
        this.h.onAdError(AdError.LOAD_TOO_FREQUENTLY);
    }

    public void destory() {
        if (isAdLoaded()) {
            this.b.destroy();
        }
        this.e.a(null);
        this.e.destroy();
    }

    public void clearCache() {
        this.e.clearCache();
    }

    public String getTitle() {
        if (isAdLoaded()) {
            return this.b.getAdTitle();
        }
        return null;
    }

    public String getShortDesc() {
        if (isAdLoaded()) {
            return this.b.getAdBody();
        }
        return null;
    }

    public String getIconUrl() {
        if (isAdLoaded()) {
            return this.b.getAdIconUrl();
        }
        return null;
    }

    public String getImageUrl() {
        if (isAdLoaded()) {
            return this.b.getAdCoverImageUrl();
        }
        return null;
    }

    public float getRatings() {
        if (isAdLoaded()) {
            return this.b.getAdStarRating();
        }
        return 4.5f;
    }

    public String getCallToAction() {
        if (isAdLoaded()) {
            return this.b.getAdCallToAction();
        }
        return null;
    }

    public String getSource() {
        if (isAdLoaded()) {
            return this.b.getAdSource();
        }
        return null;
    }

    public int getAdChannelType() {
        if (isAdLoaded()) {
            return this.b.getAdChannelType();
        }
        return -1;
    }

    public NativeAd getRealSource() {
        return isAdLoaded() ? this.b : null;
    }
}
