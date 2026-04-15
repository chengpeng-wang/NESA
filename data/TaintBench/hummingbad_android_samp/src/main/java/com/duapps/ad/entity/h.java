package com.duapps.ad.entity;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

/* compiled from: NativeAdDLWrapper */
public class h implements NativeAd {
    /* access modifiers changed from: private|static|final */
    public static final String a = h.class.getSimpleName();
    /* access modifiers changed from: private */
    public Context b;
    private List<View> c;
    /* access modifiers changed from: private */
    public View d;
    /* access modifiers changed from: private */
    public a e;
    private WeakHashMap<View, WeakReference<h>> f = new WeakHashMap();
    private a g;
    /* access modifiers changed from: private */
    public b h;
    /* access modifiers changed from: private */
    public DuAdDataCallBack i;
    /* access modifiers changed from: private */
    public DuClickCallback j;

    /* compiled from: NativeAdDLWrapper */
    class a implements OnClickListener, OnTouchListener {
        private boolean b;

        a() {
        }

        public final void onClick(View view) {
            int i = 0;
            if (h.this.i != null) {
                h.this.i.onAdClick();
                f.c("NativeAdDLWrapper", "dl has click.....");
            }
            if (this.b) {
                f.c(h.a, "No touch data recorded,please ensure touch events reach the ad View by returing false if you intercept the event.");
            }
            if (h.this.h == null) {
                h.this.h = new b(h.this.b);
                h.this.h.a(h.this.j);
            }
            if (!h.this.b() || h.this.h.e()) {
                f.c(h.a, "mClickHandler isWorking");
                return;
            }
            f.c(h.a, "mClickHandler handleClick");
            String[] strArr = h.this.e.t;
            if (strArr != null && strArr.length > 0) {
                for (String jVar : strArr) {
                    y.a().a(new j(this, jVar));
                }
            }
            h.this.h.a(new j(h.this.e));
            String[] strArr2 = h.this.e.t;
            if (strArr2 != null && strArr2.length > 0) {
                while (i < strArr2.length) {
                    y.a().a(new k(this, strArr2[i]));
                    i++;
                }
            }
        }

        public final boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && h.this.d != null) {
                h.this.d.getWidth();
                h.this.d.getHeight();
                h.this.d.getLocationInWindow(new int[2]);
                Rect rect = new Rect();
                h.this.d.getGlobalVisibleRect(rect);
                rect.width();
                rect.height();
                view.getLocationInWindow(new int[2]);
                motionEvent.getX();
                motionEvent.getY();
                this.b = true;
            }
            if (h.h(h.this) != null) {
                return h.h(h.this).onTouch(view, motionEvent);
            }
            return false;
        }
    }

    static /* synthetic */ OnTouchListener h(h hVar) {
        return null;
    }

    public /* bridge */ /* synthetic */ Object getRealData() {
        return this.e;
    }

    public h(Context context, a aVar, DuAdDataCallBack duAdDataCallBack) {
        this.e = aVar;
        this.b = context;
        this.i = duAdDataCallBack;
        this.c = Collections.synchronizedList(new ArrayList());
    }

    public void setMobulaAdListener(DuAdDataCallBack duAdDataCallBack) {
        this.i = duAdDataCallBack;
    }

    public void registerViewForInteraction(View view) {
        List arrayList = new ArrayList();
        a(arrayList, view);
        registerViewForInteraction(view, arrayList);
    }

    public void registerViewForInteraction(View view, List<View> list) {
        if (view == null) {
            f.d(a, "registerViewForInteraction() -> Must provide a view");
        } else if (list == null || list.size() == 0) {
            f.d(a, "registerViewForInteraction() -> Invalid set of clickable views");
        } else if (b()) {
            if (this.d != null) {
                f.b(a, "Native Ad was already registered with a View, Auto unregistering and proceeding");
                unregisterView();
            }
            if (this.f.containsKey(view) && ((WeakReference) this.f.get(view)).get() != null) {
                ((h) ((WeakReference) this.f.get(view)).get()).unregisterView();
                f.c("NativeAdDLWrapper", "has perform unregisterview");
            }
            this.g = new a();
            this.d = view;
            for (View view2 : list) {
                synchronized (this.c) {
                    this.c.add(view2);
                }
                view2.setOnClickListener(this.g);
                view2.setOnTouchListener(this.g);
            }
            this.f.put(view, new WeakReference(this));
            d.h(this.b, new j(this.e));
            String[] strArr = this.e.s;
            if (strArr != null && strArr.length > 0) {
                for (String iVar : strArr) {
                    y.a().a(new i(this, iVar));
                }
            }
        } else {
            f.d(a, "registerViewForInteraction() -> Ad not loaded");
        }
    }

    /* access modifiers changed from: private */
    public boolean b() {
        return this.e != null;
    }

    public void unregisterView() {
        if (this.d != null) {
            if (this.f.containsKey(this.d) && ((WeakReference) this.f.get(this.d)).get() == this) {
                this.f.remove(this.d);
                synchronized (this.c) {
                    Iterator it = this.c.iterator();
                    while (it.hasNext()) {
                        View view = (View) it.next();
                        if (view == null) {
                            it.remove();
                        } else {
                            view.setOnClickListener(null);
                            view.setOnTouchListener(null);
                        }
                    }
                    this.c.clear();
                }
                this.d = null;
                return;
            }
            f.b(a, "unregisterView() -> View not regitered with this NativeAd");
        }
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

    public String getAdCoverImageUrl() {
        if (b()) {
            return this.e.l;
        }
        return null;
    }

    public String getAdIconUrl() {
        if (b()) {
            return this.e.f;
        }
        return null;
    }

    public String getAdSocialContext() {
        if (b()) {
            return this.e.d;
        }
        return null;
    }

    public String getAdCallToAction() {
        if (b()) {
            return this.e.r;
        }
        return null;
    }

    public String getAdBody() {
        if (b()) {
            return this.e.e;
        }
        return null;
    }

    public String getAdTitle() {
        if (b()) {
            return this.e.b;
        }
        return null;
    }

    public float getAdStarRating() {
        if (b()) {
            return this.e.h;
        }
        return 0.0f;
    }

    public void destroy() {
        this.j = null;
    }

    public String getId() {
        if (b()) {
            return String.valueOf(this.e.a);
        }
        return null;
    }

    public boolean isValid() {
        return this.e == null ? false : this.e.a();
    }

    public int getAdChannelType() {
        return 1;
    }

    public String getSourceType() {
        return this.e.k;
    }

    public String getAdSource() {
        return "dl";
    }

    public void setProcessClickUrlCallback(DuClickCallback duClickCallback) {
        this.j = duClickCallback;
    }
}
