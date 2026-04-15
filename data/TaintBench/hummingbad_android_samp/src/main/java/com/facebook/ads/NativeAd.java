package com.facebook.ads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.facebook.ads.NativeAdView.Type;
import com.facebook.ads.internal.adapters.m;
import com.facebook.ads.internal.adapters.n;
import com.facebook.ads.internal.dto.d;
import com.facebook.ads.internal.e;
import com.facebook.ads.internal.extra.AdExtras;
import com.facebook.ads.internal.h;
import com.facebook.ads.internal.server.AdPlacementType;
import com.facebook.ads.internal.util.k;
import com.facebook.ads.internal.util.l;
import com.facebook.ads.internal.view.o;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.VastIconXmlManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.json.JSONObject;

public class NativeAd implements Ad {
    private static final com.facebook.ads.internal.c a = com.facebook.ads.internal.c.ADS;
    private static final String b = NativeAd.class.getSimpleName();
    private static WeakHashMap<View, WeakReference<NativeAd>> c = new WeakHashMap();
    /* access modifiers changed from: private|final */
    public final Context d;
    private final String e;
    /* access modifiers changed from: private|final */
    public final String f;
    /* access modifiers changed from: private */
    public AdListener g;
    /* access modifiers changed from: private */
    public ImpressionListener h;
    /* access modifiers changed from: private */
    public h i;
    private volatile boolean j;
    /* access modifiers changed from: private */
    public n k;
    private d l;
    /* access modifiers changed from: private */
    public View m;
    private List<View> n;
    /* access modifiers changed from: private */
    public OnTouchListener o;
    private com.facebook.ads.internal.adapters.d p;
    /* access modifiers changed from: private */
    public m q;
    private a r;
    private b s;
    private o t;
    /* access modifiers changed from: private */
    public Type u;
    /* access modifiers changed from: private */
    public boolean v;
    /* access modifiers changed from: private */
    public boolean w;
    /* access modifiers changed from: private */
    public boolean x;
    /* access modifiers changed from: private */
    public long y;

    public static class Image {
        private final String a;
        private final int b;
        private final int c;

        private Image(String str, int i, int i2) {
            this.a = str;
            this.b = i;
            this.c = i2;
        }

        public static Image fromJSONObject(JSONObject jSONObject) {
            if (jSONObject == null) {
                return null;
            }
            String optString = jSONObject.optString(MASTNativeAdConstants.RESPONSE_URL);
            return optString != null ? new Image(optString, jSONObject.optInt(VastIconXmlManager.WIDTH, 0), jSONObject.optInt(VastIconXmlManager.HEIGHT, 0)) : null;
        }

        public int getHeight() {
            return this.c;
        }

        public String getUrl() {
            return this.a;
        }

        public int getWidth() {
            return this.b;
        }
    }

    public enum MediaCacheFlag {
        NONE(0),
        ICON(1),
        IMAGE(2);
        
        public static final EnumSet<MediaCacheFlag> ALL = null;
        private final long a;

        static {
            ALL = EnumSet.allOf(MediaCacheFlag.class);
        }

        private MediaCacheFlag(long j) {
            this.a = j;
        }

        public final long getCacheFlagValue() {
            return this.a;
        }
    }

    public static class Rating {
        private final double a;
        private final double b;

        private Rating(double d, double d2) {
            this.a = d;
            this.b = d2;
        }

        public static Rating fromJSONObject(JSONObject jSONObject) {
            if (jSONObject == null) {
                return null;
            }
            double optDouble = jSONObject.optDouble(MASTNativeAdConstants.RESPONSE_VALUE, 0.0d);
            double optDouble2 = jSONObject.optDouble("scale", 0.0d);
            return (optDouble == 0.0d || optDouble2 == 0.0d) ? null : new Rating(optDouble, optDouble2);
        }

        public double getScale() {
            return this.b;
        }

        public double getValue() {
            return this.a;
        }
    }

    class a implements OnClickListener, OnTouchListener {
        private int b;
        private int c;
        private int d;
        private int e;
        private float f;
        private float g;
        private int h;
        private int i;
        private boolean j;

        private a() {
        }

        /* synthetic */ a(NativeAd nativeAd, AnonymousClass1 anonymousClass1) {
            this();
        }

        public Map<String, Object> a() {
            HashMap hashMap = new HashMap();
            hashMap.put("clickX", Integer.valueOf(this.b));
            hashMap.put("clickY", Integer.valueOf(this.c));
            hashMap.put(VastIconXmlManager.WIDTH, Integer.valueOf(this.d));
            hashMap.put(VastIconXmlManager.HEIGHT, Integer.valueOf(this.e));
            hashMap.put("adPositionX", Float.valueOf(this.f));
            hashMap.put("adPositionY", Float.valueOf(this.g));
            hashMap.put("visibleWidth", Integer.valueOf(this.i));
            hashMap.put("visibleHeight", Integer.valueOf(this.h));
            return hashMap;
        }

        public void onClick(View view) {
            if (NativeAd.this.g != null) {
                NativeAd.this.g.onAdClicked(NativeAd.this);
            }
            if (!this.j) {
                Log.e("FBAudienceNetworkLog", "No touch data recorded, please ensure touch events reach the ad View by returning false if you intercept the event.");
            }
            Map a = a();
            if (NativeAd.this.u != null) {
                a.put("nti", String.valueOf(NativeAd.this.u.getValue()));
            }
            if (NativeAd.this.v) {
                a.put("nhs", String.valueOf(NativeAd.this.v));
            }
            NativeAd.this.k.b(a);
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && NativeAd.this.m != null) {
                this.d = NativeAd.this.m.getWidth();
                this.e = NativeAd.this.m.getHeight();
                int[] iArr = new int[2];
                NativeAd.this.m.getLocationInWindow(iArr);
                this.f = (float) iArr[0];
                this.g = (float) iArr[1];
                Rect rect = new Rect();
                NativeAd.this.m.getGlobalVisibleRect(rect);
                this.i = rect.width();
                this.h = rect.height();
                int[] iArr2 = new int[2];
                view.getLocationInWindow(iArr2);
                this.b = (((int) motionEvent.getX()) + iArr2[0]) - iArr[0];
                this.c = (iArr2[1] + ((int) motionEvent.getY())) - iArr[1];
                this.j = true;
            }
            return NativeAd.this.o != null && NativeAd.this.o.onTouch(view, motionEvent);
        }
    }

    class b extends BroadcastReceiver {
        private boolean b;

        private b() {
        }

        /* synthetic */ b(NativeAd nativeAd, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void a() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.facebook.ads.native.impression:" + NativeAd.this.f);
            intentFilter.addAction("com.facebook.ads.native.click:" + NativeAd.this.f);
            LocalBroadcastManager.getInstance(NativeAd.this.d).registerReceiver(this, intentFilter);
            this.b = true;
        }

        public void b() {
            if (this.b) {
                try {
                    LocalBroadcastManager.getInstance(NativeAd.this.d).unregisterReceiver(this);
                } catch (Exception e) {
                }
            }
        }

        public void onReceive(Context context, Intent intent) {
            Object obj = intent.getAction().split(":")[0];
            if ("com.facebook.ads.native.impression".equals(obj)) {
                NativeAd.this.q.a();
            } else if ("com.facebook.ads.native.click".equals(obj)) {
                HashMap hashMap = new HashMap();
                hashMap.put("mil", Boolean.valueOf(true));
                NativeAd.this.k.b(hashMap);
            }
        }
    }

    class c extends com.facebook.ads.internal.adapters.b {
        private c() {
        }

        /* synthetic */ c(NativeAd nativeAd, AnonymousClass1 anonymousClass1) {
            this();
        }

        public boolean a() {
            return false;
        }

        public void d() {
            if (NativeAd.this.h != null) {
                NativeAd.this.h.onLoggingImpression(NativeAd.this);
            }
            if ((NativeAd.this.g instanceof ImpressionListener) && NativeAd.this.g != NativeAd.this.h) {
                ((ImpressionListener) NativeAd.this.g).onLoggingImpression(NativeAd.this);
            }
        }

        public void e() {
        }
    }

    public NativeAd(Context context, n nVar, d dVar) {
        this(context, null);
        this.l = dVar;
        this.j = true;
        this.k = nVar;
    }

    public NativeAd(Context context, String str) {
        this.f = UUID.randomUUID().toString();
        this.n = new ArrayList();
        this.d = context;
        this.e = str;
    }

    NativeAd(NativeAd nativeAd) {
        this(nativeAd.d, null);
        this.l = nativeAd.l;
        this.j = true;
        this.k = nativeAd.k;
    }

    private void a(View view) {
        this.n.add(view);
        view.setOnClickListener(this.r);
        view.setOnTouchListener(this.r);
    }

    private void a(List<View> list, View view) {
        if (!(view instanceof com.facebook.ads.internal.view.video.a) && !(view instanceof AdChoicesView)) {
            list.add(view);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    a((List) list, viewGroup.getChildAt(i));
                }
            }
        }
    }

    private int d() {
        return this.l != null ? this.l.e() : this.k != null ? this.k.i() : (this.i == null || this.i.a() == null) ? 0 : this.i.a().f();
    }

    public static void downloadAndDisplayImage(Image image, ImageView imageView) {
        if (image != null && imageView != null) {
            new k(imageView).execute(new String[]{image.getUrl()});
        }
    }

    private int e() {
        return this.l != null ? this.l.g() : this.k != null ? this.k.j() : (this.i == null || this.i.a() == null) ? 1000 : this.i.a().g();
    }

    private void f() {
        for (View view : this.n) {
            view.setOnClickListener(null);
            view.setOnTouchListener(null);
        }
        this.n.clear();
    }

    /* access modifiers changed from: private */
    public void g() {
        if (this.k != null && this.k.d()) {
            this.s = new b(this, null);
            this.s.a();
            this.q = new m(this.d, new com.facebook.ads.internal.adapters.b() {
                public boolean a() {
                    return true;
                }
            }, this.k);
        }
    }

    private int getMinViewabilityPercentage() {
        return this.l != null ? this.l.e() : (this.i == null || this.i.a() == null) ? 1 : this.i.a().e();
    }

    private void logExternalClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("eil", Boolean.valueOf(true));
        hashMap.put("eil_source", str);
        this.k.b(hashMap);
    }

    private void logExternalImpression() {
        this.q.a();
    }

    private void registerExternalLogReceiver(final String str) {
        this.q = new m(this.d, new com.facebook.ads.internal.adapters.b() {
            public boolean b() {
                return true;
            }

            public String c() {
                return str;
            }
        }, this.k);
    }

    /* access modifiers changed from: 0000 */
    public String a() {
        return !isAdLoaded() ? null : this.k.v();
    }

    /* access modifiers changed from: 0000 */
    public void a(Type type) {
        this.u = type;
    }

    /* access modifiers changed from: 0000 */
    public void a(boolean z) {
        this.v = z;
    }

    /* access modifiers changed from: 0000 */
    public String b() {
        return !isAdLoaded() ? null : this.k.w();
    }

    /* access modifiers changed from: 0000 */
    public void b(boolean z) {
        this.w = z;
    }

    /* access modifiers changed from: 0000 */
    public String c() {
        return !isAdLoaded() ? null : this.k.x();
    }

    public void destroy() {
        if (this.s != null) {
            this.s.b();
            this.s = null;
        }
        if (this.i != null) {
            this.i.d();
            this.i = null;
        }
    }

    public String getAdBody() {
        return !isAdLoaded() ? null : this.k.p();
    }

    public String getAdCallToAction() {
        return !isAdLoaded() ? null : this.k.q();
    }

    public Image getAdChoicesIcon() {
        return !isAdLoaded() ? null : this.k.t();
    }

    public String getAdChoicesLinkUrl() {
        return !isAdLoaded() ? null : this.k.u();
    }

    public Image getAdCoverImage() {
        return !isAdLoaded() ? null : this.k.l();
    }

    public AdExtras getAdExtras() {
        return !isAdLoaded() ? null : this.k.y();
    }

    public Image getAdIcon() {
        return !isAdLoaded() ? null : this.k.k();
    }

    public String getAdSocialContext() {
        return !isAdLoaded() ? null : this.k.r();
    }

    public Rating getAdStarRating() {
        return !isAdLoaded() ? null : this.k.s();
    }

    public String getAdSubtitle() {
        return !isAdLoaded() ? null : this.k.o();
    }

    public String getAdTitle() {
        return !isAdLoaded() ? null : this.k.n();
    }

    public NativeAdViewAttributes getAdViewAttributes() {
        return !isAdLoaded() ? null : this.k.m();
    }

    public String getId() {
        return !isAdLoaded() ? null : this.f;
    }

    public boolean isAdLoaded() {
        return this.k != null;
    }

    public boolean isNativeConfigEnabled() {
        return isAdLoaded() && this.k.f();
    }

    public void loadAd() {
        loadAd(EnumSet.of(MediaCacheFlag.NONE));
    }

    public void loadAd(final EnumSet<MediaCacheFlag> enumSet) {
        if (this.j) {
            throw new IllegalStateException("loadAd cannot be called more than once");
        }
        this.y = System.currentTimeMillis();
        this.j = true;
        this.i = new h(this.d, this.e, e.NATIVE_UNKNOWN, null, a, 1, true);
        this.i.a(new com.facebook.ads.internal.a() {
            public void a() {
                if (NativeAd.this.i != null) {
                    NativeAd.this.i.c();
                }
            }

            public void a(final n nVar) {
                com.facebook.ads.internal.util.c.a(com.facebook.ads.internal.util.b.a(com.facebook.ads.internal.util.b.b.LOADING_AD, AdPlacementType.NATIVE, System.currentTimeMillis() - NativeAd.this.y, null));
                if (nVar != null) {
                    List arrayList = new ArrayList(2);
                    if (enumSet.contains(MediaCacheFlag.ICON) && nVar.k() != null) {
                        arrayList.add(nVar.k().getUrl());
                    }
                    if (enumSet.contains(MediaCacheFlag.IMAGE) && nVar.l() != null) {
                        arrayList.add(nVar.l().getUrl());
                    }
                    com.facebook.ads.internal.util.m.a(NativeAd.this.d, arrayList, new l() {
                        public void a() {
                            NativeAd.this.k = nVar;
                            NativeAd.this.g();
                            if (NativeAd.this.g != null) {
                                NativeAd.this.g.onAdLoaded(NativeAd.this);
                            }
                        }
                    });
                }
            }

            public void a(com.facebook.ads.internal.b bVar) {
                if (NativeAd.this.g != null) {
                    NativeAd.this.g.onError(NativeAd.this, bVar.b());
                }
            }

            public void b() {
                if (NativeAd.this.g != null) {
                    NativeAd.this.g.onAdClicked(NativeAd.this);
                }
            }

            public void c() {
                throw new IllegalStateException("Native ads manager their own impressions.");
            }
        });
        this.i.b();
    }

    public void registerViewForInteraction(View view) {
        List arrayList = new ArrayList();
        a(arrayList, view);
        registerViewForInteraction(view, arrayList);
    }

    public void registerViewForInteraction(View view, List<View> list) {
        if (view == null) {
            throw new IllegalArgumentException("Must provide a View");
        } else if (list == null || list.size() == 0) {
            throw new IllegalArgumentException("Invalid set of clickable views");
        } else if (isAdLoaded()) {
            if (this.m != null) {
                Log.w(b, "Native Ad was already registered with a View. Auto unregistering and proceeding.");
                unregisterView();
            }
            if (c.containsKey(view)) {
                Log.w(b, "View already registered with a NativeAd. Auto unregistering and proceeding.");
                ((NativeAd) ((WeakReference) c.get(view)).get()).unregisterView();
            }
            this.r = new a(this, null);
            this.m = view;
            if (view instanceof ViewGroup) {
                this.t = new o(view.getContext(), new com.facebook.ads.internal.view.n() {
                    public void a(int i) {
                        if (NativeAd.this.k != null) {
                            NativeAd.this.k.a(i);
                        }
                    }
                });
                ((ViewGroup) view).addView(this.t);
            }
            for (View a : list) {
                a(a);
            }
            this.q = new m(this.d, new c(this, null), this.k);
            this.q.a((List) list);
            this.p = new com.facebook.ads.internal.adapters.d(this.d, this.m, getMinViewabilityPercentage(), new com.facebook.ads.internal.adapters.d.a() {
                public void a() {
                    NativeAd.this.q.a(NativeAd.this.m);
                    NativeAd.this.q.a(NativeAd.this.u);
                    NativeAd.this.q.a(NativeAd.this.v);
                    NativeAd.this.q.b(NativeAd.this.w);
                    NativeAd.this.q.c(NativeAd.this.x);
                    NativeAd.this.q.a();
                }
            });
            this.p.a(d());
            this.p.b(e());
            this.p.a();
            c.put(view, new WeakReference(this));
        } else {
            Log.e(b, "Ad not loaded");
        }
    }

    public void setAdListener(AdListener adListener) {
        this.g = adListener;
    }

    public void setImpressionListener(ImpressionListener impressionListener) {
        this.h = impressionListener;
    }

    public void setMediaViewAutoplay(boolean z) {
        this.x = z;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.o = onTouchListener;
    }

    public void unregisterView() {
        if (this.m != null) {
            if (c.containsKey(this.m) && ((WeakReference) c.get(this.m)).get() == this) {
                if ((this.m instanceof ViewGroup) && this.t != null) {
                    ((ViewGroup) this.m).removeView(this.t);
                    this.t = null;
                }
                c.remove(this.m);
                f();
                this.m = null;
                if (this.p != null) {
                    this.p.b();
                    this.p = null;
                }
                this.q = null;
                return;
            }
            throw new IllegalStateException("View not registered with this NativeAd");
        }
    }
}
