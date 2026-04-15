package com.duapps.ad;

import android.content.Context;
import android.widget.RelativeLayout;
import com.duapps.ad.entity.strategy.NativeAd;
import com.facebook.ads.AdChoicesView;

public class DuAdChoicesView extends RelativeLayout {
    private boolean a;

    public DuAdChoicesView(Context context, DuNativeAd duNativeAd) {
        this(context, duNativeAd.getRealSource(), false);
    }

    public DuAdChoicesView(Context context, DuNativeAd duNativeAd, boolean z) {
        this(context, duNativeAd.getRealSource(), z);
    }

    public DuAdChoicesView(Context context, NativeAd nativeAd) {
        this(context, nativeAd, false);
    }

    public DuAdChoicesView(Context context, NativeAd nativeAd, boolean z) {
        super(context);
        this.a = z;
        if (nativeAd == null) {
            setVisibility(8);
            return;
        }
        Object realData = nativeAd.getRealData();
        if (realData != null) {
            switch (nativeAd.getAdChannelType()) {
                case 2:
                    addView(new AdChoicesView(getContext(), (com.facebook.ads.NativeAd) realData, this.a));
                    return;
                default:
                    return;
            }
        }
    }
}
