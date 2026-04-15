package com.duapps.ad.b;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import com.cmcm.adsdk.Const;
import com.duapps.ad.DuAdDataCallBack;
import com.duapps.ad.DuClickCallback;
import com.duapps.ad.base.d;
import com.duapps.ad.base.f;
import com.duapps.ad.base.y;
import com.duapps.ad.entity.strategy.NativeAd;
import com.duapps.ad.stats.b;
import com.duapps.ad.stats.j;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/* compiled from: NativeAdIMWrapper */
public final class m implements NativeAd {
    private static WeakHashMap<View, WeakReference<m>> e = new WeakHashMap();
    /* access modifiers changed from: private */
    public Context a;
    private List<View> b = new ArrayList();
    /* access modifiers changed from: private */
    public View c;
    /* access modifiers changed from: private */
    public a d;
    private a f;
    /* access modifiers changed from: private */
    public b g;
    /* access modifiers changed from: private */
    public DuAdDataCallBack h;
    /* access modifiers changed from: private */
    public DuClickCallback i;

    /* compiled from: NativeAdIMWrapper */
    class a implements OnClickListener, OnTouchListener {
        private boolean a;

        a() {
        }

        public final void onClick(View view) {
            if (m.this.h != null) {
                m.this.h.onAdClick();
            }
            if (this.a) {
                f.c("NativeAdIMWrapper", "No touch data recorded,please ensure touch events reach the ad View by returing false if you intercept the event.");
            }
            if (m.this.g == null) {
                m.this.g = new b(m.this.a);
                m.this.g.a(m.this.i);
            }
            if (m.this.a()) {
                m.this.g.a(new j(m.this.d));
                y.a().a(new l(m.this.a, true, m.this.d));
            }
        }

        public final boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && m.this.c != null) {
                m.this.c.getWidth();
                m.this.c.getHeight();
                m.this.c.getLocationInWindow(new int[2]);
                Rect rect = new Rect();
                m.this.c.getGlobalVisibleRect(rect);
                rect.width();
                rect.height();
                view.getLocationInWindow(new int[2]);
                motionEvent.getX();
                motionEvent.getY();
                this.a = true;
            }
            if (m.h(m.this) != null) {
                return m.h(m.this).onTouch(view, motionEvent);
            }
            return false;
        }
    }

    static /* synthetic */ OnTouchListener h(m mVar) {
        return null;
    }

    public m(Context context, a aVar, DuAdDataCallBack duAdDataCallBack) {
        this.d = aVar;
        this.a = context;
        this.h = duAdDataCallBack;
    }

    public final void setMobulaAdListener(DuAdDataCallBack duAdDataCallBack) {
        this.h = duAdDataCallBack;
    }

    /* access modifiers changed from: private */
    public boolean a() {
        return this.d != null;
    }

    private void b() {
        for (View view : this.b) {
            view.setOnClickListener(null);
            view.setOnTouchListener(null);
        }
        this.b.clear();
    }

    private void a(List<View> list, View view) {
        list.add(view);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                a((List) list, viewGroup.getChildAt(i));
            }
        }
    }

    public final void registerViewForInteraction(View view) {
        List arrayList = new ArrayList();
        a(arrayList, view);
        registerViewForInteraction(view, arrayList);
    }

    public final void registerViewForInteraction(View view, List<View> list) {
        if (view == null) {
            f.d("NativeAdIMWrapper", "registerViewForInteraction() -> Must provide a view");
        } else if (list == null || list.size() == 0) {
            f.d("NativeAdIMWrapper", "registerViewForInteraction() -> Invalid set of clickable views");
        } else if (a()) {
            if (this.c != null) {
                f.b("NativeAdIMWrapper", "Native Ad was already registered with a View, Auto unregistering and proceeding");
                unregisterView();
            }
            if (e.containsKey(view) && ((WeakReference) e.get(view)).get() != null) {
                ((m) ((WeakReference) e.get(view)).get()).unregisterView();
            }
            this.f = new a();
            this.c = view;
            for (View view2 : list) {
                this.b.add(view2);
                view2.setOnClickListener(this.f);
                view2.setOnTouchListener(this.f);
            }
            e.put(view, new WeakReference(this));
            d.h(this.a, new j(this.d));
            y.a().a(new l(this.a, false, this.d));
        } else {
            f.d("NativeAdIMWrapper", "registerViewForInteraction() -> Ad not loaded");
        }
    }

    public final void unregisterView() {
        if (this.c != null) {
            if (e.containsKey(this.c) && ((WeakReference) e.get(this.c)).get() == this) {
                e.remove(this.c);
                b();
                return;
            }
            f.b("NativeAdIMWrapper", "unregisterView() -> View not regitered with this NativeAd");
        }
    }

    public final void destroy() {
        this.i = null;
        b();
        if (this.c != null) {
            e.remove(this.c);
            this.c = null;
        }
    }

    public final String getId() {
        return null;
    }

    public final String getAdCoverImageUrl() {
        if (a()) {
            return this.d.m;
        }
        return null;
    }

    public final String getAdIconUrl() {
        if (a()) {
            return this.d.l;
        }
        return null;
    }

    public final String getAdSocialContext() {
        return null;
    }

    public final String getAdCallToAction() {
        if (a()) {
            return this.d.o;
        }
        return null;
    }

    public final String getAdBody() {
        if (a()) {
            return this.d.j;
        }
        return null;
    }

    public final String getAdTitle() {
        if (a()) {
            return this.d.i;
        }
        return null;
    }

    public final float getAdStarRating() {
        if (a()) {
            return this.d.k;
        }
        return 0.0f;
    }

    public final boolean isValid() {
        if (a()) {
            return this.d.a();
        }
        return false;
    }

    public final String getSourceType() {
        return "inmobi";
    }

    public final String getAdSource() {
        return Const.KEY_IM;
    }

    public final void setProcessClickUrlCallback(DuClickCallback duClickCallback) {
        this.i = duClickCallback;
    }

    public final Object getRealData() {
        return this.d;
    }

    public final int getAdChannelType() {
        return 3;
    }
}
