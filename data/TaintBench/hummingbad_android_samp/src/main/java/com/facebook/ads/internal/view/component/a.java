package com.facebook.ads.internal.view.component;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.view.i;
import com.facebook.ads.internal.view.j;

public class a extends LinearLayout {
    private j a = new j(getContext(), 2);
    private int b;

    public a(Context context, NativeAd nativeAd, NativeAdViewAttributes nativeAdViewAttributes) {
        super(context);
        setOrientation(1);
        setVerticalGravity(16);
        this.a.setMinTextSize((float) (nativeAdViewAttributes.getTitleTextSize() - 2));
        this.a.setText(nativeAd.getAdTitle());
        i.a(this.a, nativeAdViewAttributes);
        this.a.setLayoutParams(new LayoutParams(-2, -2));
        addView(this.a);
        this.b = Math.min(nativeAd.getAdTitle().length(), 21);
        addView(i.a(context, nativeAd, nativeAdViewAttributes));
    }

    public int getMinVisibleTitleCharacters() {
        return this.b;
    }

    public TextView getTitleTextView() {
        return this.a;
    }
}
