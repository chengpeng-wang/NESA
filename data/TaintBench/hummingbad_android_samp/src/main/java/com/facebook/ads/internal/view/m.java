package com.facebook.ads.internal.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.InterstitialAdActivity;
import com.facebook.ads.internal.view.h.a;

public class m implements h {
    private g a;

    public m(InterstitialAdActivity interstitialAdActivity, a aVar) {
        this.a = new g(interstitialAdActivity);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.addRule(15);
        this.a.setLayoutParams(layoutParams);
        aVar.a(this.a);
    }

    public void a() {
    }

    public void a(Intent intent, Bundle bundle) {
        String stringExtra = intent.getStringExtra(InterstitialAdActivity.VIDEO_URL);
        String stringExtra2 = intent.getStringExtra(InterstitialAdActivity.VIDEO_PLAY_REPORT_URL);
        String stringExtra3 = intent.getStringExtra(InterstitialAdActivity.VIDEO_TIME_REPORT_URL);
        this.a.setVideoPlayReportURI(stringExtra2);
        this.a.setVideoTimeReportURI(stringExtra3);
        this.a.setVideoURI(stringExtra);
        this.a.a();
    }

    public void a(Bundle bundle) {
    }

    public void b() {
    }

    public void c() {
        this.a.b();
    }
}
