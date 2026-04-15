package com.cmcm.adsdk.splashad;

import android.app.Activity;
import android.widget.RelativeLayout;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.requestconfig.data.PosBean;
import com.cmcm.adsdk.requestconfig.util.b;
import com.cmcm.adsdk.splashad.SplashBaseAdapter.OnSplashAdapterResultListener;
import com.cmcm.adsdk.splashad.listener.ImageLoadListener;
import com.cmcm.baseapi.utils.ThreadUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class SplashAdManager implements SplashAdViewListener, OnSplashAdapterResultListener {
    /* access modifiers changed from: private */
    public Activity a;
    /* access modifiers changed from: private */
    public CMSplashAdView b;
    private String c;
    private SplashAdListener d;
    private int e = 2;
    private int f = 3;
    private boolean g = false;
    private int h = 5;
    private long i = 3000;
    private ImageLoadListener j;
    /* access modifiers changed from: private */
    public List<PosBean> k = null;
    private a l = null;
    /* access modifiers changed from: private */
    public boolean m = false;
    private boolean n = false;
    private SplashBaseAdapter o;
    private AtomicBoolean p = new AtomicBoolean(false);

    private class a extends TimerTask {
        private a() {
        }

        /* synthetic */ a(SplashAdManager splashAdManager, byte b) {
            this();
        }

        public final void run() {
            SplashAdManager.this.m = true;
            com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "timeout, callback failed, and currenttime = " + System.currentTimeMillis());
            SplashAdManager.this.a(Const.KEY_JUHE, "10004");
        }
    }

    public SplashAdManager(Activity context, CMSplashAdView container, String postId, SplashAdListener onSplashListener) {
        this.a = context;
        this.b = container;
        this.c = postId;
        this.d = onSplashListener;
        this.b.addSplashAdViewListener(this);
    }

    public SplashAdManager setShowIntervalFrequency(int showIntervalFrequency) {
        if (showIntervalFrequency > 0) {
            this.e = showIntervalFrequency;
        }
        return this;
    }

    public SplashAdManager setShowNumberFrequency(int showNumberFrequency) {
        if (showNumberFrequency > 0) {
            this.f = showNumberFrequency;
        }
        return this;
    }

    public SplashAdManager setLoadTimeOut(long timeout) {
        if (timeout >= 1000) {
            this.i = timeout;
        }
        return this;
    }

    public SplashAdManager setTimeOutViewNumber(int countDown) {
        if (countDown > 0 && countDown <= this.h) {
            this.h = countDown;
        }
        return this;
    }

    public SplashAdManager setImageLoadListener(ImageLoadListener imageLoadListener) {
        this.j = imageLoadListener;
        return this;
    }

    public SplashAdManager setUseTimeOutView(boolean useTimeOutView) {
        this.g = useTimeOutView;
        return this;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0063  */
    public void loadSplashAd() {
        /*
        r12 = this;
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = 60;
        r5 = 0;
        r1 = 1;
        r2 = 0;
        r0 = r12.a;
        if (r0 == 0) goto L_0x001f;
    L_0x000c:
        r0 = r12.b;
        if (r0 == 0) goto L_0x001f;
    L_0x0010:
        r0 = r12.d;
        if (r0 == 0) goto L_0x001f;
    L_0x0014:
        r0 = r12.n;
        if (r0 == 0) goto L_0x0020;
    L_0x0018:
        r0 = "SplashAdManager";
        r1 = "is loading, so finish load";
        com.cmcm.adsdk.requestconfig.log.a.a(r0, r1);
    L_0x001f:
        return;
    L_0x0020:
        r0 = "splash_show_day";
        r0 = com.cmcm.adsdk.requestconfig.util.b.b(r0, r2);
        r3 = b();
        if (r3 == r0) goto L_0x006b;
    L_0x002c:
        r0 = "SplashAdManager";
        r3 = "this is new day";
        com.cmcm.adsdk.requestconfig.log.a.a(r0, r3);
        r12.c();
        r0 = "load_splash_success_time";
        com.cmcm.adsdk.requestconfig.util.b.a(r0, r5);
        r0 = "splash_show_number_frequency";
        com.cmcm.adsdk.requestconfig.util.b.a(r0, r2);
    L_0x0040:
        r0 = r2;
    L_0x0041:
        r3 = "SplashAdManager";
        r4 = new java.lang.StringBuilder;
        r5 = "loadAd,and isExceedFrequency = ";
        r4.<init>(r5);
        r4 = r4.append(r0);
        r5 = ",and posid = ";
        r4 = r4.append(r5);
        r5 = r12.c;
        r4 = r4.append(r5);
        r4 = r4.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r3, r4);
        if (r0 == 0) goto L_0x00c9;
    L_0x0063:
        r0 = "ad";
        r1 = "10007";
        r12.a(r0, r1);
        goto L_0x001f;
    L_0x006b:
        r0 = r12.d();
        if (r0 != 0) goto L_0x007a;
    L_0x0071:
        r0 = "SplashAdManager";
        r3 = "show number attain max rrequency or config error";
        com.cmcm.adsdk.requestconfig.log.a.a(r0, r3);
        r0 = r1;
        goto L_0x0041;
    L_0x007a:
        r0 = "load_splash_success_time";
        r3 = com.cmcm.adsdk.requestconfig.util.b.b(r0, r5);
        r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r0 != 0) goto L_0x008c;
    L_0x0084:
        r0 = "SplashAdManager";
        r3 = "the saveLoadTime get error";
        com.cmcm.adsdk.requestconfig.log.a.a(r0, r3);
        goto L_0x0040;
    L_0x008c:
        r0 = "SplashAdManager";
        r5 = new java.lang.StringBuilder;
        r6 = "now time = ";
        r5.<init>(r6);
        r6 = java.lang.System.currentTimeMillis();
        r5 = r5.append(r6);
        r6 = ",saveLoadTime = ";
        r5 = r5.append(r6);
        r5 = r5.append(r3);
        r5 = r5.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r0, r5);
        r5 = java.lang.System.currentTimeMillis();
        r3 = r5 - r3;
        r0 = r12.e;
        r5 = (long) r0;
        r0 = java.lang.Long.valueOf(r5);
        r5 = r0.longValue();
        r5 = r5 * r8;
        r5 = r5 * r8;
        r5 = r5 * r10;
        r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r0 >= 0) goto L_0x0040;
    L_0x00c6:
        r0 = r1;
        goto L_0x0041;
    L_0x00c9:
        r12.n = r1;
        r0 = "SplashAdManager";
        r1 = new java.lang.StringBuilder;
        r3 = "start timeout task, and currenttime = ";
        r1.<init>(r3);
        r3 = java.lang.System.currentTimeMillis();
        r1 = r1.append(r3);
        r1 = r1.toString();
        com.cmcm.adsdk.requestconfig.log.a.a(r0, r1);
        r12.m = r2;
        r0 = r12.i;
        r0 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1));
        if (r0 < 0) goto L_0x00fe;
    L_0x00eb:
        r0 = new com.cmcm.adsdk.splashad.SplashAdManager$a;
        r0.m610init(r12, r2);
        r12.l = r0;
        r0 = new java.util.Timer;
        r0.<init>();
        r1 = r12.l;
        r2 = r12.i;
        r0.schedule(r1, r2);
    L_0x00fe:
        r0 = com.cmcm.adsdk.requestconfig.RequestConfig.a();
        r1 = r12.c;
        r2 = new com.cmcm.adsdk.splashad.SplashAdManager$1;
        r2.m3202init();
        r0.a(r1, r2);
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cmcm.adsdk.splashad.SplashAdManager.loadSplashAd():void");
    }

    /* access modifiers changed from: private */
    public void a() {
        if (!this.m) {
            if (this.k == null || this.k.isEmpty()) {
                e();
                a(Const.KEY_JUHE, "10002");
                return;
            }
            com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "loadSplashAd In Order,and temporaryBeans = " + this.k.toString());
            this.o = b.a((PosBean) this.k.remove(0));
            if (this.o != null) {
                this.o.a(this.j);
                RelativeLayout contentView = this.b.getContentView();
                if (contentView != null) {
                    contentView.removeAllViews();
                }
                this.o.a(this.a, this, contentView);
                return;
            }
            a();
        }
    }

    private static int b() {
        return Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date())).intValue();
    }

    private void c() {
        b.a("splash_show_day", b());
    }

    private boolean d() {
        return b.b("splash_show_number_frequency", 0) < this.f;
    }

    private void e() {
        try {
            if (this.l != null) {
                this.l.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAdShiped() {
        if (this.d != null) {
            this.d.onAdSkiped();
        }
    }

    public void onCountDowned() {
        if (this.o != null && Const.KEY_ICLICK.equals(this.o.a()) && this.d != null) {
            this.d.onAdDismissed();
        }
    }

    public void onAdPresent(String adName) {
        com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "onAdPresent,and the adName = " + adName + ",and isTimeout = " + this.m);
        this.n = false;
        e();
        if (!this.m) {
            if (!(!this.g || Const.KEY_GDT.equals(adName) || Const.KEY_ICLICK_VIDEO.equals(adName))) {
                if (!Const.KEY_CM.equals(adName)) {
                    this.h = 5;
                }
                this.b.setCountdown(this.h);
                this.b.startCountDown();
            }
            if (this.d != null) {
                this.d.onAdPresent(adName);
            }
            b.a("load_splash_success_time", System.currentTimeMillis());
        }
    }

    public void onAdDismissed(String adName) {
        com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "onAdDismissed,and the adName = " + adName);
        if (this.d != null) {
            this.d.onAdDismissed();
        }
        if (d()) {
            int b = b.b("splash_show_number_frequency", 0) + 1;
            b.a("splash_show_number_frequency", b);
            if (b == 1) {
                c();
            }
        }
    }

    public void onClicked(String adName) {
        com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "onClicked,and the adName = " + adName);
        if (this.d != null) {
            this.d.onAdClicked(adName);
        }
        this.b.stopCountDown();
    }

    public void onAdFailed(String adName, String errorMessage) {
        if (this.k == null || this.k.size() <= 0) {
            e();
            a(adName, errorMessage);
            return;
        }
        com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "onAdFailed,and the onAdFailed = " + adName + ",and the errorMessage =" + errorMessage);
        ThreadUtils.postOnUiThread(new Runnable() {
            public final void run() {
                if (SplashAdManager.this.a != null && SplashAdManager.this.b != null) {
                    SplashAdManager.this.a();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void a(String str, String str2) {
        this.n = false;
        if (!this.p.get()) {
            this.p.getAndSet(true);
            this.k = null;
            if (this.d != null) {
                String str3 = str + str2;
                com.cmcm.adsdk.requestconfig.log.a.a("SplashAdManager", "notifyAdFailed,and the finalErrorMessage = " + str3);
                this.d.onAdFailed(str3);
            }
        }
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onStop() {
    }

    public void onDestroy() {
        if (this.o != null) {
            this.o.b();
            this.o = null;
        }
        if (this.b != null) {
            this.b.removeSplashAdViewListener();
            this.b = null;
        }
    }
}
