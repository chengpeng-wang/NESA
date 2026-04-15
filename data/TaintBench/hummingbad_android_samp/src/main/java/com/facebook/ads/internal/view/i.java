package com.facebook.ads.internal.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAd.Rating;
import com.facebook.ads.NativeAdViewAttributes;

public abstract class i {
    public static LinearLayout a(Context context, NativeAd nativeAd, NativeAdViewAttributes nativeAdViewAttributes) {
        LinearLayout linearLayout = new LinearLayout(context);
        Rating adStarRating = nativeAd.getAdStarRating();
        if (adStarRating == null || adStarRating.getValue() < 3.0d) {
            k kVar = new k(context);
            kVar.setText(nativeAd.getAdSocialContext());
            b(kVar, nativeAdViewAttributes);
            linearLayout.addView(kVar);
        } else {
            RatingBar ratingBar = new RatingBar(context, null, 16842877);
            ratingBar.setLayoutParams(new LayoutParams(-2, -2));
            ratingBar.setStepSize(0.1f);
            ratingBar.setIsIndicator(true);
            ratingBar.setNumStars((int) adStarRating.getScale());
            ratingBar.setRating((float) adStarRating.getValue());
            linearLayout.addView(ratingBar);
        }
        return linearLayout;
    }

    public static void a(TextView textView, NativeAdViewAttributes nativeAdViewAttributes) {
        textView.setTextColor(nativeAdViewAttributes.getTitleTextColor());
        textView.setTextSize((float) nativeAdViewAttributes.getTitleTextSize());
        textView.setTypeface(nativeAdViewAttributes.getTypeface(), 1);
    }

    public static void b(TextView textView, NativeAdViewAttributes nativeAdViewAttributes) {
        textView.setTextColor(nativeAdViewAttributes.getDescriptionTextColor());
        textView.setTextSize((float) nativeAdViewAttributes.getDescriptionTextSize());
        textView.setTypeface(nativeAdViewAttributes.getTypeface());
    }
}
