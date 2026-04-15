package com.cmcm.adsdk.splashad;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cmcm.adsdk.R;
import com.cmcm.adsdk.requestconfig.log.a;

public class CMSplashAdView extends RelativeLayout implements OnClickListener {
    /* access modifiers changed from: private */
    public SplashAdViewListener a;
    /* access modifiers changed from: private */
    public int b;
    /* access modifiers changed from: private */
    public Handler c;
    public TextView jump_to_main;
    public Runnable mCountDownTask = new Runnable() {
        public final void run() {
            a.a("CMSplashAdView", "mCountdownNum:" + CMSplashAdView.this.b);
            CMSplashAdView.this.number.setText(String.valueOf(CMSplashAdView.b(CMSplashAdView.this)));
            if (CMSplashAdView.this.b > 0) {
                CMSplashAdView.this.c.postDelayed(this, 1000);
            } else if (CMSplashAdView.this.a != null) {
                CMSplashAdView.this.a.onCountDowned();
            }
        }
    };
    public TextView number;
    public RelativeLayout rl_contentview;
    public RelativeLayout rl_time_layout;
    public View vertical_line;

    interface SplashAdViewListener {
        void onAdShiped();

        void onCountDowned();
    }

    static /* synthetic */ int b(CMSplashAdView cMSplashAdView) {
        int i = cMSplashAdView.b - 1;
        cMSplashAdView.b = i;
        return i;
    }

    public CMSplashAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = LayoutInflater.from(context).inflate(R.layout.splash_time, this);
        this.rl_time_layout = (RelativeLayout) inflate.findViewById(R.id.rl_time_layout);
        this.rl_time_layout.bringToFront();
        this.rl_contentview = (RelativeLayout) inflate.findViewById(R.id.rl_contentview);
        this.number = (TextView) inflate.findViewById(R.id.number);
        this.jump_to_main = (TextView) inflate.findViewById(R.id.jump_to_main);
        this.vertical_line = inflate.findViewById(R.id.vertical_line);
        this.jump_to_main.setOnClickListener(this);
    }

    public RelativeLayout getContentView() {
        return this.rl_contentview;
    }

    public void addSplashAdViewListener(SplashAdViewListener listener) {
        this.a = listener;
    }

    public void removeSplashAdViewListener() {
        this.a = null;
    }

    public void onClick(View v) {
        if (this.a != null) {
            this.a.onAdShiped();
        }
    }

    public void setCountdown(int num) {
        this.b = num;
    }

    public void startCountDown() {
        this.number.setVisibility(0);
        this.number.setText(String.valueOf(this.b));
        this.rl_time_layout.setVisibility(0);
        this.vertical_line.setVisibility(0);
        this.jump_to_main.setVisibility(0);
        if (this.c == null) {
            this.c = new Handler();
        }
        this.c.postDelayed(this.mCountDownTask, 1000);
    }

    public void stopCountDown() {
        if (this.c != null) {
            this.c.removeCallbacks(this.mCountDownTask);
            this.c = null;
        }
    }
}
