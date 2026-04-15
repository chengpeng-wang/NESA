package com.facebook.ads.internal.view.component;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.view.i;
import com.facebook.ads.internal.view.k;

public class b extends LinearLayout {
    private ImageView a;
    private a b;
    private TextView c;
    private LinearLayout d = new LinearLayout(getContext());

    public b(Context context, NativeAd nativeAd, NativeAdViewAttributes nativeAdViewAttributes, boolean z, int i) {
        super(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        setVerticalGravity(16);
        setOrientation(1);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.setGravity(16);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.setMargins(Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density), Math.round(15.0f * displayMetrics.density));
        linearLayout.setLayoutParams(layoutParams);
        addView(linearLayout);
        layoutParams = new LayoutParams(-1, 0);
        this.d.setOrientation(0);
        this.d.setGravity(16);
        layoutParams.weight = 3.0f;
        this.d.setLayoutParams(layoutParams);
        linearLayout.addView(this.d);
        this.a = new c(getContext());
        int a = a(z, i);
        LayoutParams layoutParams2 = new LayoutParams(Math.round(((float) a) * displayMetrics.density), Math.round(((float) a) * displayMetrics.density));
        layoutParams2.setMargins(0, 0, Math.round(15.0f * displayMetrics.density), 0);
        this.a.setLayoutParams(layoutParams2);
        NativeAd.downloadAndDisplayImage(nativeAd.getAdIcon(), this.a);
        this.d.addView(this.a);
        LinearLayout linearLayout2 = new LinearLayout(getContext());
        linearLayout2.setLayoutParams(new LayoutParams(-1, -1));
        linearLayout2.setOrientation(0);
        linearLayout2.setGravity(16);
        this.d.addView(linearLayout2);
        this.b = new a(getContext(), nativeAd, nativeAdViewAttributes);
        layoutParams2 = new LayoutParams(-2, -1);
        layoutParams2.setMargins(0, 0, Math.round(15.0f * displayMetrics.density), 0);
        layoutParams2.weight = 0.5f;
        this.b.setLayoutParams(layoutParams2);
        linearLayout2.addView(this.b);
        this.c = new TextView(getContext());
        this.c.setPadding(Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density), Math.round(6.0f * displayMetrics.density));
        this.c.setText(nativeAd.getAdCallToAction());
        this.c.setTextColor(nativeAdViewAttributes.getButtonTextColor());
        this.c.setTextSize(14.0f);
        this.c.setTypeface(nativeAdViewAttributes.getTypeface(), 1);
        this.c.setMaxLines(2);
        this.c.setEllipsize(TruncateAt.END);
        this.c.setGravity(17);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(nativeAdViewAttributes.getButtonColor());
        gradientDrawable.setCornerRadius(displayMetrics.density * 5.0f);
        gradientDrawable.setStroke(1, nativeAdViewAttributes.getButtonBorderColor());
        this.c.setBackgroundDrawable(gradientDrawable);
        LayoutParams layoutParams3 = new LayoutParams(-2, -2);
        layoutParams3.weight = 0.25f;
        this.c.setLayoutParams(layoutParams3);
        linearLayout2.addView(this.c);
        if (z) {
            k kVar = new k(getContext());
            kVar.setText(nativeAd.getAdBody());
            i.b(kVar, nativeAdViewAttributes);
            kVar.setMinTextSize((float) (nativeAdViewAttributes.getDescriptionTextSize() - 1));
            layoutParams = new LayoutParams(-1, 0);
            layoutParams.weight = 1.0f;
            kVar.setLayoutParams(layoutParams);
            kVar.setGravity(80);
            linearLayout.addView(kVar);
        }
    }

    private int a(boolean z, int i) {
        return (int) (((double) (i - 30)) * (3.0d / ((double) ((z ? 1 : 0) + 3))));
    }

    public TextView getCallToActionView() {
        return this.c;
    }

    public ImageView getIconView() {
        return this.a;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        TextView titleTextView = this.b.getTitleTextView();
        if (titleTextView.getLayout().getLineEnd(titleTextView.getLineCount() - 1) < this.b.getMinVisibleTitleCharacters()) {
            this.d.removeView(this.a);
            super.onMeasure(i, i2);
        }
    }
}
