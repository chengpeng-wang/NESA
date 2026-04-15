package com.cmcm.adsdk.nativead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Handler;
import android.view.View;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.utils.c;

public final class PicksViewCheckHelper {
    private a a;
    private float b = 0.1f;
    /* access modifiers changed from: private */
    public boolean c = false;
    /* access modifiers changed from: private */
    public b d;
    private Context e;
    private View f;
    private boolean g;
    private AdImpressionListener h;

    public interface AdImpressionListener {
        void onAdImpression();
    }

    private class a extends BroadcastReceiver {
        private a() {
        }

        /* synthetic */ a(PicksViewCheckHelper picksViewCheckHelper, byte b) {
            this();
        }

        public final void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "screen on");
                com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "this ad has report show?:" + PicksViewCheckHelper.this.c);
                if (PicksViewCheckHelper.this.c) {
                    com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "this ad has report to unregsiter screen receiver");
                    PicksViewCheckHelper.this.d();
                } else if (PicksViewCheckHelper.this.d != null) {
                    PicksViewCheckHelper.this.d.a();
                }
            } else if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "screen off");
                if (PicksViewCheckHelper.this.d != null) {
                    PicksViewCheckHelper.this.d.b();
                }
            } else if ("android.intent.action.USER_PRESENT".equals(intent.getAction())) {
                com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "screen present");
            }
        }
    }

    private class b {
        public Handler a = new Handler();
        public volatile boolean b = true;
        /* access modifiers changed from: private */
        public long d = 1000;
        private Runnable e = new Runnable() {
            public final void run() {
                if (b.this.b) {
                    PicksViewCheckHelper.this.c();
                    if (b.this.a != null) {
                        b.this.a.postDelayed(this, b.this.d);
                    }
                }
            }
        };

        public b(long j) {
        }

        public final synchronized void a() {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "scheduleImpressionRetry");
            if (this.b) {
                if (this.a == null) {
                    this.a = new Handler();
                }
                this.a.postDelayed(this.e, this.d);
            }
        }

        public final synchronized void b() {
            com.cmcm.adsdk.requestconfig.log.a.a(Const.TAG, "cancelImpressionRetry");
            if (this.b) {
                this.a.removeCallbacks(this.e);
                this.a = null;
                this.b = false;
            }
        }
    }

    public PicksViewCheckHelper(Context context, View adView, AdImpressionListener listener, boolean isYahoo) {
        float f = 0.1f;
        this.e = context.getApplicationContext();
        this.f = adView;
        this.g = isYahoo;
        this.h = listener;
        if (isYahoo) {
            f = 0.5f;
        }
        this.b = f;
        this.d = new b(1000);
    }

    public final void a() {
        com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "start check view");
        if (!this.g) {
            com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "is no yahoo ad, check view");
            c();
        }
        if (this.d != null) {
            this.d.a();
            if (!(this.c || c.a(this.e))) {
                com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "lock screen,cancel schedule check view");
                this.d.b();
            }
            this.a = new a(this, (byte) 0);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            try {
                this.e.getApplicationContext().registerReceiver(this.a, intentFilter);
            } catch (Exception e) {
            }
        }
    }

    public final void b() {
        com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "stop check view");
        if (this.d != null) {
            this.d.b();
            this.d = null;
        }
        d();
        this.f = null;
    }

    /* access modifiers changed from: private */
    public void c() {
        boolean z = false;
        com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "to check view is on screen");
        View view = this.f;
        if (!(view == null || view.getVisibility() != 0 || view.getParent() == null)) {
            boolean z2 = VERSION.SDK_INT >= 11 ? view.getAlpha() > 0.9f : true;
            if (z2) {
                Rect rect = new Rect();
                if (view.getGlobalVisibleRect(rect)) {
                    double height = (double) (rect.height() * rect.width());
                    double width = (double) (view.getWidth() * view.getHeight());
                    com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "is yahoo?" + this.g + " area value :" + this.b);
                    if (height >= width * ((double) this.b)) {
                        z = true;
                    }
                }
            }
        }
        if (z) {
            this.h.onAdImpression();
            this.c = true;
            b();
        }
    }

    /* access modifiers changed from: private */
    public void d() {
        if (this.a != null && this.e != null) {
            com.cmcm.adsdk.requestconfig.log.a.a("PicksViewCheckHelper", "unregister screen receiver");
            try {
                this.e.getApplicationContext().unregisterReceiver(this.a);
            } catch (Exception e) {
            }
            this.a = null;
        }
    }
}
