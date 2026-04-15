package com.facebook.ads;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.facebook.ads.NativeAd.Image;
import com.facebook.ads.internal.util.g;

public class AdChoicesView extends RelativeLayout {
    /* access modifiers changed from: private|final */
    public final Context a;
    /* access modifiers changed from: private|final */
    public final NativeAd b;
    private final DisplayMetrics c;
    /* access modifiers changed from: private */
    public boolean d;
    /* access modifiers changed from: private */
    public TextView e;

    public AdChoicesView(Context context, NativeAd nativeAd) {
        this(context, nativeAd, false);
    }

    public AdChoicesView(Context context, NativeAd nativeAd, boolean z) {
        super(context);
        this.d = false;
        this.a = context;
        this.b = nativeAd;
        this.c = this.a.getResources().getDisplayMetrics();
        Image adChoicesIcon = this.b.getAdChoicesIcon();
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0) {
                    return false;
                }
                if (AdChoicesView.this.d) {
                    g.a(AdChoicesView.this.a, Uri.parse(AdChoicesView.this.b.getAdChoicesLinkUrl()));
                } else {
                    AdChoicesView.this.a();
                }
                return true;
            }
        });
        this.e = new TextView(this.a);
        addView(this.e);
        LayoutParams layoutParams2 = new LayoutParams(-2, -2);
        if (z) {
            layoutParams2.addRule(11, a(adChoicesIcon).getId());
            layoutParams2.width = 0;
            layoutParams.width = Math.round(((float) (adChoicesIcon.getWidth() + 4)) * this.c.density);
            layoutParams.height = Math.round(((float) (adChoicesIcon.getHeight() + 2)) * this.c.density);
            this.d = false;
        } else {
            this.d = true;
        }
        setLayoutParams(layoutParams);
        layoutParams2.addRule(15, -1);
        this.e.setLayoutParams(layoutParams2);
        this.e.setSingleLine();
        this.e.setText("AdChoices");
        this.e.setTextSize(10.0f);
        this.e.setTextColor(-4341303);
    }

    private ImageView a(Image image) {
        ImageView imageView = new ImageView(this.a);
        addView(imageView);
        LayoutParams layoutParams = new LayoutParams(Math.round(((float) image.getWidth()) * this.c.density), Math.round(((float) image.getHeight()) * this.c.density));
        layoutParams.addRule(9);
        layoutParams.addRule(15, -1);
        layoutParams.setMargins(Math.round(4.0f * this.c.density), Math.round(this.c.density * 2.0f), Math.round(this.c.density * 2.0f), Math.round(this.c.density * 2.0f));
        imageView.setLayoutParams(layoutParams);
        NativeAd.downloadAndDisplayImage(image, imageView);
        return imageView;
    }

    /* access modifiers changed from: private */
    public void a() {
        Paint paint = new Paint();
        paint.setTextSize(this.e.getTextSize());
        int round = Math.round(paint.measureText("AdChoices") + (4.0f * this.c.density));
        final int width = getWidth();
        round += width;
        this.d = true;
        AnonymousClass2 anonymousClass2 = new Animation() {
            /* access modifiers changed from: protected */
            public void applyTransformation(float f, Transformation transformation) {
                int i = (int) (((float) width) + (((float) (round - width)) * f));
                AdChoicesView.this.getLayoutParams().width = i;
                AdChoicesView.this.requestLayout();
                AdChoicesView.this.e.getLayoutParams().width = i - width;
                AdChoicesView.this.e.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        anonymousClass2.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (AdChoicesView.this.d) {
                            AdChoicesView.this.d = false;
                            AnonymousClass1 anonymousClass1 = new Animation() {
                                /* access modifiers changed from: protected */
                                public void applyTransformation(float f, Transformation transformation) {
                                    int i = (int) (((float) round) + (((float) (width - round)) * f));
                                    AdChoicesView.this.getLayoutParams().width = i;
                                    AdChoicesView.this.requestLayout();
                                    AdChoicesView.this.e.getLayoutParams().width = i - width;
                                    AdChoicesView.this.e.requestLayout();
                                }

                                public boolean willChangeBounds() {
                                    return true;
                                }
                            };
                            anonymousClass1.setDuration(300);
                            anonymousClass1.setFillAfter(true);
                            AdChoicesView.this.startAnimation(anonymousClass1);
                        }
                    }
                }, 3000);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        anonymousClass2.setDuration(300);
        anonymousClass2.setFillAfter(true);
        startAnimation(anonymousClass2);
    }
}
