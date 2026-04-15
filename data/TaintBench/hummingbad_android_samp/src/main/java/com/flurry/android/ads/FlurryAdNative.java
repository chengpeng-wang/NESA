package com.flurry.android.ads;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.flurry.sdk.af;
import com.flurry.sdk.ag;
import com.flurry.sdk.al;
import com.flurry.sdk.d;
import com.flurry.sdk.df;
import com.flurry.sdk.e;
import com.flurry.sdk.eh;
import com.flurry.sdk.f;
import com.flurry.sdk.gz;
import com.flurry.sdk.hz;
import com.flurry.sdk.ik;
import com.flurry.sdk.il;
import com.flurry.sdk.iw;
import com.flurry.sdk.lz;
import com.flurry.sdk.n;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class FlurryAdNative {
    private static final String APP_RATING = "appRating";
    private static final String SEC_HQ_RATING_IMG = "secHqRatingIMg";
    private static final String SEC_RATING_IMG = "secRatingImg";
    private static final String SHOW_RATING = "showRating";
    /* access modifiers changed from: private|static|final */
    public static final String kLogTag = FlurryAdNative.class.getSimpleName();
    /* access modifiers changed from: private */
    public ag fAdObject;
    private final ik fAdStateEventListener = new ik() {
        public void notify(final d dVar) {
            if (dVar.a == FlurryAdNative.this.fAdObject && dVar.b != null) {
                if (e.kOnFetched.equals(dVar.b)) {
                    FlurryAdNative.this.buildAssetList();
                }
                final FlurryAdNativeListener access$200 = FlurryAdNative.this.fListener;
                if (access$200 != null) {
                    hz.a.a(new lz() {
                        public void safeRun() {
                            switch (dVar.b) {
                                case kOnFetched:
                                    f.a().a("nativeAdReady");
                                    access$200.onFetched(FlurryAdNative.this);
                                    return;
                                case kOnFetchFailed:
                                    if (dVar.c == df.j) {
                                        f.a().a("nativeAdUnfilled");
                                    }
                                    access$200.onError(FlurryAdNative.this, FlurryAdErrorType.FETCH, dVar.c.m);
                                    iw.c(FlurryAdNative.kLogTag, "Error code: " + dVar.c);
                                    return;
                                case kOnOpen:
                                    access$200.onShowFullscreen(FlurryAdNative.this);
                                    return;
                                case kOnClose:
                                    access$200.onCloseFullscreen(FlurryAdNative.this);
                                    return;
                                case kOnAppExit:
                                    access$200.onAppExit(FlurryAdNative.this);
                                    return;
                                case kOnClicked:
                                    access$200.onClicked(FlurryAdNative.this);
                                    return;
                                case kOnClickFailed:
                                    access$200.onError(FlurryAdNative.this, FlurryAdErrorType.CLICK, dVar.c.m);
                                    return;
                                case kOnImpressionLogged:
                                    access$200.onImpressionLogged(FlurryAdNative.this);
                                    return;
                                default:
                                    return;
                            }
                        }
                    });
                }
            }
        }
    };
    private final List fAssetList = new ArrayList();
    /* access modifiers changed from: private */
    public FlurryAdNativeListener fListener;

    public FlurryAdNative(Context context, String str) {
        if (hz.a == null) {
            throw new IllegalStateException("Flurry SDK must be initialized before starting a session");
        } else if (context == null) {
            throw new IllegalArgumentException("Context cannot be null!");
        } else if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("Ad space must be specified!");
        } else {
            try {
                if (n.a() == null) {
                    throw new IllegalStateException("Could not find FlurryAds module. Please make sure the library is included.");
                }
                this.fAdObject = new ag(context, str);
                iw.a(kLogTag, "NativeAdObject created: " + this.fAdObject);
                il.a().a("com.flurry.android.impl.ads.AdStateEvent", this.fAdStateEventListener);
            } catch (Throwable th) {
                iw.a(kLogTag, "Exception: ", th);
            }
        }
    }

    /* access modifiers changed from: private */
    public void buildAssetList() {
        if (this.fAdObject != null) {
            boolean z = true;
            synchronized (this.fAssetList) {
                for (eh ehVar : this.fAdObject.o()) {
                    if (ehVar.a.equals(SHOW_RATING)) {
                        z = ehVar.c.equals("true");
                        break;
                    }
                }
            }
            synchronized (this.fAssetList) {
                for (eh ehVar2 : this.fAdObject.o()) {
                    if (!ehVar2.a.equals(SHOW_RATING) && (z || !(ehVar2.a.equals(APP_RATING) || ehVar2.a.equals(SEC_RATING_IMG) || ehVar2.a.equals(SEC_HQ_RATING_IMG)))) {
                        this.fAssetList.add(new FlurryAdNativeAsset(ehVar2, this.fAdObject.a));
                    }
                }
            }
        }
    }

    public final void destroy() {
        try {
            il.a().b("com.flurry.android.impl.ads.AdStateEvent", this.fAdStateEventListener);
            this.fListener = null;
            if (this.fAdObject != null) {
                iw.a(kLogTag, "NativeAdObject ready to destroy: " + this.fAdObject);
                this.fAdObject.a();
            }
        } catch (Throwable th) {
            iw.a(kLogTag, "Exception: ", th);
        }
    }

    public final void fetchAd() {
        try {
            iw.a(kLogTag, "NativeAdObject ready to fetch ad: " + this.fAdObject);
            f.a().a("nativeAdFetch");
            af afVar = this.fAdObject;
            Log.i("Flurry", "fetchAd");
            synchronized (afVar) {
                if (al.INIT.equals(afVar.f)) {
                    afVar.b.a(afVar, afVar.h(), afVar.i());
                } else if (al.READY.equals(afVar.f)) {
                    iw.a(ag.e, "NativeAdObject fetched: " + afVar);
                    gz.a(afVar);
                }
            }
        } catch (Throwable th) {
            iw.a(kLogTag, "Exception: ", th);
        }
    }

    public final FlurryAdNativeAsset getAsset(String str) {
        if (n.a() == null) {
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            FlurryAdNativeAsset flurryAdNativeAsset;
            synchronized (this.fAssetList) {
                for (FlurryAdNativeAsset flurryAdNativeAsset2 : this.fAssetList) {
                    if (str.equals(flurryAdNativeAsset2.getName())) {
                        break;
                    }
                }
                flurryAdNativeAsset2 = null;
            }
            return flurryAdNativeAsset2;
        } catch (Throwable th) {
            iw.a(kLogTag, "Exception: ", th);
            return null;
        }
    }

    public final boolean getLogControl() {
        return this.fAdObject.h;
    }

    public final boolean isReady() {
        try {
            return this.fAdObject.m();
        } catch (Throwable th) {
            iw.a(kLogTag, "Exception: ", th);
            return false;
        }
    }

    public final void logClick() {
        if (getLogControl()) {
            this.fAdObject.q();
        }
    }

    public final void logImpression() {
        if (getLogControl()) {
            this.fAdObject.p();
        }
    }

    public final void removeTrackingView() {
        try {
            this.fAdObject.n();
        } catch (Throwable th) {
            iw.a(kLogTag, "Exception: ", th);
        }
    }

    public final void setListener(FlurryAdNativeListener flurryAdNativeListener) {
        try {
            this.fListener = flurryAdNativeListener;
        } catch (Throwable th) {
            iw.a(kLogTag, "Exception: ", th);
        }
    }

    public final void setLogControl(boolean z) {
        this.fAdObject.h = z;
    }

    public final void setTrackingView(View view) {
        if (!getLogControl()) {
            try {
                ag agVar = this.fAdObject;
                agVar.n();
                agVar.g = new WeakReference(view);
                agVar.a((ViewGroup) view);
            } catch (Throwable th) {
                iw.a(kLogTag, "Exception: ", th);
            }
        }
    }
}
