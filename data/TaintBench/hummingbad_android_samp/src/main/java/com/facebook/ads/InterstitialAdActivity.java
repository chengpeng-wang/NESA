package com.facebook.ads;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.internal.g;
import com.facebook.ads.internal.view.f;
import com.facebook.ads.internal.view.h;
import com.facebook.ads.internal.view.h.a;
import com.facebook.ads.internal.view.m;

public class InterstitialAdActivity extends Activity {
    public static final String PREDEFINED_ORIENTATION_KEY = "predefinedOrientationKey";
    public static final String VIDEO_PLAY_REPORT_URL = "videoPlayReportURL";
    public static final String VIDEO_TIME_REPORT_URL = "videoTimeReportURL";
    public static final String VIDEO_URL = "videoURL";
    public static final String VIEW_TYPE = "viewType";
    /* access modifiers changed from: private */
    public RelativeLayout a;
    /* access modifiers changed from: private */
    public g b;
    private int c = -1;
    private String d;
    private h e;

    public enum Type {
        DISPLAY,
        VIDEO
    }

    private void a(Intent intent, Bundle bundle) {
        if (bundle != null) {
            this.c = bundle.getInt(PREDEFINED_ORIENTATION_KEY, -1);
            this.d = bundle.getString("adInterstitialUniqueId");
            this.e.a(intent, bundle);
            return;
        }
        this.c = intent.getIntExtra(PREDEFINED_ORIENTATION_KEY, -1);
        this.d = intent.getStringExtra("adInterstitialUniqueId");
        this.e.a(intent, bundle);
    }

    /* access modifiers changed from: private */
    public void a(String str) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(str + ":" + this.d));
    }

    public void finish() {
        this.a.removeAllViews();
        this.e.c();
        a("com.facebook.ads.interstitial.dismissed");
        super.finish();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        this.a = new RelativeLayout(this);
        this.a.setBackgroundColor(-16777216);
        setContentView(this.a, new LayoutParams(-1, -1));
        Intent intent = getIntent();
        if (intent.getBooleanExtra("useNativeCloseButton", false)) {
            this.b = new g(this);
            this.b.setId(100002);
            this.b.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    InterstitialAdActivity.this.finish();
                }
            });
        }
        switch ((Type) intent.getSerializableExtra(VIEW_TYPE)) {
            case VIDEO:
                this.e = new m(this, new a() {
                    public void a(View view) {
                        InterstitialAdActivity.this.a.addView(view);
                        if (InterstitialAdActivity.this.b != null) {
                            InterstitialAdActivity.this.a.addView(InterstitialAdActivity.this.b);
                        }
                    }

                    public void a(String str) {
                        InterstitialAdActivity.this.a(str);
                    }
                });
                break;
            default:
                this.e = new f(this, new a() {
                    public void a(View view) {
                        InterstitialAdActivity.this.a.addView(view);
                        if (InterstitialAdActivity.this.b != null) {
                            InterstitialAdActivity.this.a.addView(InterstitialAdActivity.this.b);
                        }
                    }

                    public void a(String str) {
                        InterstitialAdActivity.this.a(str);
                    }
                });
                break;
        }
        a(intent, bundle);
        a("com.facebook.ads.interstitial.displayed");
    }

    public void onPause() {
        super.onPause();
        this.e.a();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void onResume() {
        super.onResume();
        this.e.b();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.e.a(bundle);
        bundle.putInt(PREDEFINED_ORIENTATION_KEY, this.c);
        bundle.putString("adInterstitialUniqueId", this.d);
    }

    public void onStart() {
        super.onStart();
        if (this.c != -1) {
            setRequestedOrientation(this.c);
        }
    }

    public void setRequestedOrientation(int i) {
        super.setRequestedOrientation(i);
    }
}
