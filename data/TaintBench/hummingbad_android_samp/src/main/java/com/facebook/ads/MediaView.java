package com.facebook.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.internal.util.k;
import com.facebook.ads.internal.util.r;
import com.facebook.ads.internal.view.e;
import com.facebook.ads.internal.view.video.a;

public class MediaView extends RelativeLayout {
    private final e a;
    private final a b;
    private boolean c;
    private boolean d;

    public MediaView(Context context) {
        this(context, null);
    }

    public MediaView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.c = false;
        this.d = true;
        this.a = new e(context);
        this.a.setLayoutParams(new LayoutParams(-1, -1));
        addView(this.a);
        this.b = new a(context);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(13);
        this.b.setLayoutParams(layoutParams);
        this.b.setAutoplay(this.d);
        addView(this.b);
    }

    private boolean a(NativeAd nativeAd) {
        return !r.a(nativeAd.a());
    }

    public boolean isAutoplay() {
        return this.d;
    }

    public void setAutoplay(boolean z) {
        this.d = z;
        this.b.setAutoplay(z);
    }

    public void setNativeAd(NativeAd nativeAd) {
        nativeAd.b(true);
        nativeAd.setMediaViewAutoplay(this.d);
        if (this.c) {
            this.a.a(null, null);
            this.b.b();
            this.c = false;
        }
        if (a(nativeAd)) {
            this.a.setVisibility(4);
            this.b.setVisibility(0);
            bringChildToFront(this.b);
            this.c = true;
            try {
                this.b.setVideoPlayReportURI(nativeAd.b());
                this.b.setVideoTimeReportURI(nativeAd.c());
                this.b.setVideoURI(nativeAd.a());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (nativeAd.getAdCoverImage() != null) {
            this.b.a();
            this.b.setVisibility(4);
            this.a.setVisibility(0);
            bringChildToFront(this.a);
            this.c = true;
            new k(this.a).execute(new String[]{nativeAd.getAdCoverImage().getUrl()});
        }
    }
}
