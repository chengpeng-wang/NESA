package com.facebook.ads.internal.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.facebook.ads.AdError;

public class c extends BroadcastReceiver {
    private String a;
    private Context b;
    private InterstitialAdapterListener c;
    private InterstitialAdapter d;

    public c(Context context, String str, InterstitialAdapter interstitialAdapter, InterstitialAdapterListener interstitialAdapterListener) {
        this.b = context;
        this.a = str;
        this.c = interstitialAdapterListener;
        this.d = interstitialAdapter;
    }

    public void a() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.facebook.ads.interstitial.impression.logged:" + this.a);
        intentFilter.addAction("com.facebook.ads.interstitial.displayed:" + this.a);
        intentFilter.addAction("com.facebook.ads.interstitial.dismissed:" + this.a);
        intentFilter.addAction("com.facebook.ads.interstitial.clicked:" + this.a);
        intentFilter.addAction("com.facebook.ads.interstitial.error:" + this.a);
        LocalBroadcastManager.getInstance(this.b).registerReceiver(this, intentFilter);
    }

    public void b() {
        try {
            LocalBroadcastManager.getInstance(this.b).unregisterReceiver(this);
        } catch (Exception e) {
        }
    }

    public void onReceive(Context context, Intent intent) {
        Object obj = intent.getAction().split(":")[0];
        if (this.c != null && obj != null) {
            if ("com.facebook.ads.interstitial.clicked".equals(obj)) {
                this.c.onInterstitialAdClicked(this.d, intent.getStringExtra("com.facebook.ads.interstitial.ad.click.url"), intent.getBooleanExtra("com.facebook.ads.interstitial.ad.player.handles.click", true));
            } else if ("com.facebook.ads.interstitial.dismissed".equals(obj)) {
                this.c.onInterstitialAdDismissed(this.d);
            } else if ("com.facebook.ads.interstitial.displayed".equals(obj)) {
                this.c.onInterstitialAdDisplayed(this.d);
            } else if ("com.facebook.ads.interstitial.impression.logged".equals(obj)) {
                this.c.onInterstitialLoggingImpression(this.d);
            } else if ("com.facebook.ads.interstitial.error".equals(obj)) {
                this.c.onInterstitialError(this.d, AdError.INTERNAL_ERROR);
            }
        }
    }
}
