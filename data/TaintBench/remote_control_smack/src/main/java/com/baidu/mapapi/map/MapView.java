package com.baidu.mapapi.map;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ZoomControls;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.A;
import com.baidu.platform.comapi.map.Projection;
import com.baidu.platform.comapi.map.d;
import com.baidu.platform.comapi.map.n;
import com.baidu.platform.comapi.map.q;
import com.baidu.platform.comapi.map.r;
import com.baidu.platform.comapi.map.t;
import com.baidu.platform.comapi.map.u;
import com.baidu.platform.comapi.map.x;
import com.baidu.platform.comapi.map.y;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MapView extends ViewGroup {
    q a = null;
    c b = null;
    com.baidu.platform.comapi.map.a c = null;
    /* access modifiers changed from: private */
    public MapController d = null;
    private u e = null;
    private int f = 0;
    private int g = 0;
    private ZoomControls h = null;
    /* access modifiers changed from: private */
    public float i = -1.0f;
    private Projection j = null;
    private int k = 0;
    private int l = 0;
    private com.baidu.platform.comapi.map.q.a m = null;
    private Context n = null;
    /* access modifiers changed from: private */
    public MKMapViewListener o = null;
    /* access modifiers changed from: private */
    public MKMapTouchListener p = null;
    private boolean q = false;
    private t r;
    private boolean s = false;
    private boolean t = false;
    /* access modifiers changed from: private */
    public boolean u = false;
    private List<Overlay> v;

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public static final int BOTTOM = 80;
        public static final int BOTTOM_CENTER = 81;
        public static final int CENTER = 17;
        public static final int CENTER_HORIZONTAL = 1;
        public static final int CENTER_VERTICAL = 16;
        public static final int LEFT = 3;
        public static final int MODE_MAP = 0;
        public static final int MODE_VIEW = 1;
        public static final int RIGHT = 5;
        public static final int TOP = 48;
        public static final int TOP_LEFT = 51;
        public int alignment;
        public int mode;
        public GeoPoint point;
        public int x;
        public int y;

        public LayoutParams(int i, int i2, int i3, int i4, int i5) {
            super(i, i2);
            this.mode = 1;
            this.point = null;
            this.x = 0;
            this.y = 0;
            this.alignment = 51;
            this.mode = 1;
            this.x = i3;
            this.y = i4;
            this.alignment = i5;
        }

        public LayoutParams(int i, int i2, GeoPoint geoPoint, int i3) {
            this(i, i2, geoPoint, 0, 0, i3);
        }

        public LayoutParams(int i, int i2, GeoPoint geoPoint, int i3, int i4, int i5) {
            super(i, i2);
            this.mode = 1;
            this.point = null;
            this.x = 0;
            this.y = 0;
            this.alignment = 51;
            this.mode = 0;
            this.point = geoPoint;
            this.x = i3;
            this.y = i4;
            this.alignment = i5;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.mode = 1;
            this.point = null;
            this.x = 0;
            this.y = 0;
            this.alignment = 51;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.mode = 1;
            this.point = null;
            this.x = 0;
            this.y = 0;
            this.alignment = 51;
        }
    }

    private class c extends View {
        public c(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            MapView.this.a();
        }
    }

    class a implements Projection {
        a() {
        }

        public GeoPoint fromPixels(int i, int i2) {
            GeoPoint fromPixels = MapView.this.a.d().fromPixels(i, i2);
            return fromPixels == null ? null : e.a(fromPixels);
        }

        public float metersToEquatorPixels(float f) {
            return MapView.this.a.d().metersToEquatorPixels(f);
        }

        public Point toPixels(GeoPoint geoPoint, Point point) {
            return MapView.this.a.d().toPixels(e.b(geoPoint), point);
        }
    }

    private class b implements com.baidu.platform.comapi.map.q.a {
        private b() {
        }

        /* synthetic */ b(MapView mapView, b bVar) {
            this();
        }

        public void a(Object obj) {
            MapView.this.a((Overlay) obj);
        }

        public void b(Object obj) {
            MapView.this.b((Overlay) obj);
        }
    }

    public MapView(Context context) {
        super(context);
        a(context);
        addView(this.a);
        addView(this.b);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a(context);
        addView(this.a);
        addView(this.b);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        a(context);
        addView(this.a);
        addView(this.b);
    }

    /* access modifiers changed from: private */
    public void a(int i, GeoPoint geoPoint, int i2) {
        List c = this.a.c();
        if (c != null) {
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 < c.size()) {
                    if (((Overlay) c.get(i4)).mType == 27 && geoPoint != null) {
                        if (!(((ItemizedOverlay) c.get(i4)).onTap(getProjection().fromPixels(geoPoint.getLatitudeE6(), geoPoint.getLongitudeE6()), this) || i == -1 || i2 != ((Overlay) c.get(i4)).mLayerID)) {
                            ((ItemizedOverlay) c.get(i4)).onTap(i);
                        }
                    }
                    i3 = i4 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private void a(Context context) {
        this.n = context;
        if (this.a == null) {
            this.a = new q(context);
            Bundle bundle = new Bundle();
            bundle.remove("overlooking");
            bundle.remove("rotation");
            bundle.putDouble("centerptx", 1.2958162E7d);
            bundle.putDouble("centerpty", 4825907.0d);
            bundle.putString("modulePath", com.baidu.platform.comapi.d.c.q());
            bundle.putString("appSdcardPath", com.baidu.mapapi.utils.b.a());
            bundle.putString("appCachePath", com.baidu.mapapi.utils.b.b());
            bundle.putString("appSecondCachePath", com.baidu.mapapi.utils.b.c());
            bundle.putInt("mapTmpMax", com.baidu.mapapi.utils.b.d());
            bundle.putInt("domTmpMax", com.baidu.mapapi.utils.b.e());
            bundle.putInt("itsTmpMax", com.baidu.mapapi.utils.b.f());
            this.m = new b(this, null);
            this.a.a(this.m);
            this.a.a(bundle, context);
            this.d = new MapController(this);
            this.d.a = this.a.b();
            f();
            this.k = (int) (36.0f * com.baidu.platform.comapi.d.c.C);
            this.l = (int) (40.0f * com.baidu.platform.comapi.d.c.C);
            e();
            d();
            refresh();
            this.a.layout(this.a.getLeft() + 1, this.a.getTop() + 1, this.a.getRight() + 1, this.a.getBottom() + 1);
            this.a.setVisibility(0);
            this.a.setFocusable(true);
            this.h = new ZoomControls(context);
            if (this.h.getParent() == null) {
                this.h.setOnZoomOutClickListener(new b(this));
                this.h.setOnZoomInClickListener(new c(this));
                this.h.setFocusable(true);
                this.h.setVisibility(0);
                this.h.measure(0, 0);
            }
            this.b = new c(context);
            this.b.setBackgroundColor(0);
            this.b.layout(this.a.getLeft() + 1, this.a.getTop() + 1, this.a.getRight() + 1, this.a.getBottom() + 1);
        }
    }

    private void a(View view, android.view.ViewGroup.LayoutParams layoutParams) {
        view.measure(this.f, this.g);
        int i = layoutParams.width;
        int i2 = layoutParams.height;
        int measuredWidth = i == -1 ? this.f : i == -2 ? view.getMeasuredWidth() : i;
        if (i2 == -1) {
            i2 = this.g;
        } else if (i2 == -2) {
            i2 = view.getMeasuredHeight();
        }
        if (checkLayoutParams(layoutParams)) {
            LayoutParams layoutParams2 = (LayoutParams) layoutParams;
            int i3 = layoutParams2.x;
            i = layoutParams2.y;
            if (layoutParams2.mode == 0 && layoutParams2.point != null) {
                Point toPixels = getProjection().toPixels(layoutParams2.point, null);
                i3 = toPixels.x + layoutParams2.x;
                i = toPixels.y + layoutParams2.y;
            }
            switch (layoutParams2.alignment) {
                case 1:
                    i3 -= measuredWidth / 2;
                    break;
                case 5:
                    i3 -= measuredWidth;
                    break;
                case 16:
                    i -= i2 / 2;
                    break;
                case 17:
                    i3 -= measuredWidth / 2;
                    i -= i2 / 2;
                    break;
                case LayoutParams.BOTTOM /*80*/:
                    i -= i2;
                    break;
                case LayoutParams.BOTTOM_CENTER /*81*/:
                    i3 -= measuredWidth / 2;
                    i -= i2;
                    break;
            }
            view.layout(i3, i, measuredWidth + i3, i2 + i);
            return;
        }
        view.layout(0, 0, measuredWidth, i2);
    }

    /* access modifiers changed from: private */
    public void a(Overlay overlay) {
        if (overlay.mType != 21) {
            if (overlay.mLayerID != 0) {
                throw new RuntimeException("cat not add overlay,overlay already exists in mapview");
            }
            if (overlay.mType == 27) {
                ItemizedOverlay itemizedOverlay = (ItemizedOverlay) overlay;
                itemizedOverlay.a();
                itemizedOverlay.b();
            } else if (overlay.mType == 12) {
                ((RouteOverlay) overlay).a();
            } else if (overlay.mType == 28) {
                ((TransitOverlay) overlay).a();
            } else if (overlay.mType == 14) {
                ((PoiOverlay) overlay).a();
            } else if (overlay.mType == 29) {
                GraphicsOverlay graphicsOverlay = (GraphicsOverlay) overlay;
                graphicsOverlay.a();
                graphicsOverlay.c();
            } else if (overlay.mType == 7) {
                ((MyLocationOverlay) overlay).a();
            } else if (overlay.mType == 30) {
                TextOverlay textOverlay = (TextOverlay) overlay;
                textOverlay.a();
                textOverlay.b();
            }
            if (overlay.mLayerID != 0) {
                this.d.a.b().a(overlay.mLayerID, true);
                this.d.a.b().a(overlay.mLayerID);
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(r rVar, int i) {
        List c = this.a.c();
        if (c != null) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < c.size()) {
                    if (((Overlay) c.get(i3)).mLayerID == i) {
                        if (((Overlay) c.get(i3)).mType == 12) {
                            ((RouteOverlay) c.get(i3)).onTap(rVar.b);
                        }
                        if (((Overlay) c.get(i3)).mType == 28) {
                            ((TransitOverlay) c.get(i3)).onTap(rVar.b);
                        }
                    }
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void b(int i) {
        List c = this.a.c();
        if (c != null) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < c.size()) {
                    if (((Overlay) c.get(i3)).mType == 7 && ((Overlay) c.get(i3)).mLayerID == i) {
                        ((MyLocationOverlay) c.get(i3)).dispatchTap();
                    }
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void b(Overlay overlay) {
        if (overlay.mLayerID != 0) {
            if (overlay.mType == 21) {
                this.d.a.b().c(this.d.a.a);
                this.d.a.b().a(this.d.a.a, false);
                return;
            }
            a(overlay.mLayerID);
            this.d.a.b().c(overlay.mLayerID);
            this.d.a.b().a(overlay.mLayerID, false);
            this.d.a.b().a(overlay.mLayerID);
            this.d.a.b().b(overlay.mLayerID);
            overlay.mLayerID = 0;
        }
    }

    /* access modifiers changed from: private */
    public void c(int i) {
        List c = this.a.c();
        if (c != null) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < c.size()) {
                    if (((Overlay) c.get(i3)).mType == 21 && ((PopupOverlay) c.get(i3)).a != null) {
                        ((PopupOverlay) c.get(i3)).a.onClickedPopup(i);
                    }
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void c(int i, int i2) {
        List c = this.a.c();
        if (c != null) {
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 < c.size()) {
                    if (((Overlay) c.get(i4)).mType == 14 && ((Overlay) c.get(i4)).mLayerID == i2) {
                        ((PoiOverlay) c.get(i4)).onTap(i);
                    }
                    i3 = i4 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private void d() {
        try {
            AssetManager assets = this.n.getAssets();
            InputStream open = com.baidu.platform.comapi.d.c.m() >= 180 ? assets.open("logo_h.png") : assets.open("logo_l.png");
            if (open != null) {
                Bitmap decodeStream = BitmapFactory.decodeStream(open);
                open.close();
                Bundle bundle = new Bundle();
                bundle.putInt("logoaddr", ((Integer) this.d.a.g.get("logo")).intValue());
                bundle.putInt("bshow", 1);
                bundle.putInt("imgW", decodeStream.getWidth());
                bundle.putInt("imgH", decodeStream.getHeight());
                bundle.putInt("showLR", 1);
                bundle.putInt("iconwidth", 0);
                bundle.putInt("iconlayer", 1);
                bundle.putInt("bound", 0);
                bundle.putInt("popname", -1288857267);
                ByteBuffer allocate = ByteBuffer.allocate((decodeStream.getWidth() * decodeStream.getHeight()) * 4);
                decodeStream.copyPixelsToBuffer(allocate);
                bundle.putByteArray("imgdata", allocate.array());
                this.d.a.b().e(bundle);
                this.d.a.b().a(this.d.a.c, true);
                this.d.a.b().a(this.d.a.c);
            }
        } catch (Exception e) {
        }
    }

    private void e() {
        com.baidu.platform.comapi.map.b.a().a(String.format("{\"dataset\":[{\"x\":%d,\"y\":%d,\"hidetime\":1000}]}", new Object[]{Integer.valueOf(this.k), Integer.valueOf(this.l)}));
        com.baidu.platform.comapi.map.b.a().d();
    }

    private void f() {
        this.e = new d(this);
        this.a.a(this.e);
        this.c = new e(this);
        this.a.a(this.c);
    }

    /* access modifiers changed from: 0000 */
    public int a(String str) {
        return this.a.a(str);
    }

    /* access modifiers changed from: 0000 */
    public void a() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = super.getChildAt(i);
            if (!(childAt == this.b || childAt == this.h || childAt == this.a)) {
                android.view.ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                if ((layoutParams instanceof LayoutParams) && ((LayoutParams) layoutParams).mode == 0) {
                    a(childAt, layoutParams);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.a.a(i);
    }

    /* access modifiers changed from: 0000 */
    public void a(int i, int i2) {
        t l = this.a.l();
        float f = l.a;
        GeoPoint mapCenter = getMapCenter();
        GeoPoint geoPoint = new GeoPoint(mapCenter.getLatitudeE6() - (i / 2), mapCenter.getLongitudeE6() + (i2 / 2));
        GeoPoint geoPoint2 = new GeoPoint(mapCenter.getLatitudeE6() + (i / 2), mapCenter.getLongitudeE6() - (i2 / 2));
        mapCenter = e.b(geoPoint);
        geoPoint = e.b(geoPoint2);
        com.baidu.platform.comapi.basestruct.c cVar = new com.baidu.platform.comapi.basestruct.c();
        cVar.a(mapCenter.getLongitudeE6());
        cVar.b(mapCenter.getLatitudeE6());
        com.baidu.platform.comapi.basestruct.c cVar2 = new com.baidu.platform.comapi.basestruct.c();
        cVar2.a(geoPoint.getLongitudeE6());
        cVar2.b(geoPoint.getLatitudeE6());
        com.baidu.platform.comapi.basestruct.b bVar = new com.baidu.platform.comapi.basestruct.b();
        bVar.a(cVar);
        bVar.b(cVar2);
        if (bVar != null) {
            f = this.a.a(bVar);
        }
        l.a = f;
        this.d.a.a(l);
    }

    /* access modifiers changed from: 0000 */
    public void a(int i, d dVar) {
        this.a.a(i, dVar);
    }

    /* access modifiers changed from: 0000 */
    public void a(boolean z, boolean z2) {
        this.h.setIsZoomOutEnabled(z2);
        this.h.setIsZoomInEnabled(z);
    }

    /* access modifiers changed from: 0000 */
    public void b(int i, int i2) {
        this.k = i;
        this.l = i2;
        e();
    }

    /* access modifiers changed from: 0000 */
    public boolean b() {
        return this.d.zoomIn();
    }

    /* access modifiers changed from: 0000 */
    public boolean c() {
        return this.d.zoomOut();
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void destroy() {
        if (this.a != null) {
            this.a.a();
            this.a = null;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return keyEvent.getAction() == 0 ? onKeyDown(keyEvent.getKeyCode(), keyEvent) : keyEvent.getAction() == 1 ? onKeyUp(keyEvent.getKeyCode(), keyEvent) : false;
    }

    @Deprecated
    public void displayZoomControls(boolean z) {
        if (!this.q || this.h.getParent() == null) {
            removeView(this.h);
            addView(this.h);
            this.q = true;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        destroy();
        super.finalize();
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public MapController getController() {
        return this.d;
    }

    public boolean getCurrentMap() {
        this.d.a.a(com.baidu.mapapi.utils.b.h() + "/BaiduMapSDK/capture.png");
        return true;
    }

    public int getLatitudeSpan() {
        Projection projection = getProjection();
        return Math.abs(projection.fromPixels(0, 0).getLatitudeE6() - projection.fromPixels(this.f - 1, this.g - 1).getLatitudeE6());
    }

    public int getLongitudeSpan() {
        Projection projection = getProjection();
        return Math.abs(projection.fromPixels(0, 0).getLongitudeE6() - projection.fromPixels(this.f - 1, this.g - 1).getLongitudeE6());
    }

    public GeoPoint getMapCenter() {
        GeoPoint h = this.a.h();
        return h == null ? null : e.a(h);
    }

    public int getMapOverlooking() {
        return this.a.k();
    }

    public int getMapRotation() {
        return this.a.j();
    }

    public int getMaxZoomLevel() {
        return 19;
    }

    public int getMinZoomLevel() {
        return 3;
    }

    public List<Overlay> getOverlays() {
        return this.a != null ? this.a.c() : null;
    }

    public Projection getProjection() {
        if (this.j == null) {
            this.j = new a();
        }
        return this.j;
    }

    @Deprecated
    public View getZoomControls() {
        return this.h;
    }

    public float getZoomLevel() {
        return this.a.i();
    }

    public boolean isDoubleClickZooming() {
        return this.d.a.e();
    }

    public boolean isSatellite() {
        return this.a.e();
    }

    public boolean isTraffic() {
        return this.a.f();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        if (this.a != null && indexOfChild(this.a) == -1) {
            addView(this.a);
        }
        if (this.q) {
            setBuiltInZoomControls(true);
        }
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (this.q && this.h.getParent() != null) {
            removeView(this.h);
        }
        removeView(this.a);
        super.onDetachedFromWindow();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return this.a.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return this.a.onKeyUp(i, keyEvent);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = 0;
        this.f = i3 - i;
        this.g = i4 - i2;
        android.view.ViewGroup.LayoutParams layoutParams = this.b.getLayoutParams();
        layoutParams.width = this.f;
        layoutParams.height = this.g;
        this.b.setVisibility(0);
        this.b.layout(0, 0, this.f, this.g);
        layoutParams = this.a.getLayoutParams();
        layoutParams.width = this.f;
        layoutParams.height = this.g;
        this.a.setVisibility(0);
        this.a.layout(0, 0, this.f, this.g);
        if (this.q && this.h != null) {
            layoutParams = this.h.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.height = -2;
                layoutParams.width = -2;
            }
            this.h.setVisibility(0);
            this.h.measure(i3 - i, i4 - i2);
            this.h.layout((i3 - 10) - this.h.getMeasuredWidth(), ((i4 - 5) - this.h.getMeasuredHeight()) - i2, i3 - 10, (i4 - 5) - i2);
        }
        int childCount = getChildCount();
        while (i5 < childCount) {
            View childAt = super.getChildAt(i5);
            if (!(childAt == this.b || childAt == this.h || childAt == this.a)) {
                a(childAt, childAt.getLayoutParams());
            }
            i5++;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public void onPause() {
        if (this.a != null) {
            this.r = this.a.l();
            this.s = this.a.e();
            this.t = this.a.f();
            this.v = new ArrayList(getOverlays());
            getOverlays().clear();
            this.a.onPause();
        }
    }

    public void onRestoreInstanceState(Bundle bundle) {
        int i = bundle.getInt("lat");
        int i2 = bundle.getInt("lon");
        if (!(i == 0 || i2 == 0)) {
            this.d.setCenter(new GeoPoint(i, i2));
        }
        float f = bundle.getFloat("zoom");
        if (f != 0.0f) {
            this.d.setZoom(f);
        }
        setTraffic(bundle.getBoolean("traffic"));
    }

    public void onResume() {
        if (this.a != null) {
            this.a.onResume();
            if (this.r != null) {
                t l = this.a.l();
                this.r.f = l.f;
                this.a.a(this.r);
            }
            this.a.a(this.s);
            this.a.b(this.t);
            if (this.v != null) {
                getOverlays().clear();
                getOverlays().addAll(this.v);
            }
            refresh();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        GeoPoint mapCenter = getMapCenter();
        bundle.putInt("lat", mapCenter.getLatitudeE6());
        bundle.putInt("lon", mapCenter.getLongitudeE6());
        bundle.putFloat("zoom", getZoomLevel());
        bundle.putBoolean("traffic", isTraffic());
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.f = i;
        this.g = i2;
        this.d.a.e(i, i2);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.a.a(motionEvent);
    }

    public void preLoad() {
    }

    public void refresh() {
        List overlays = getOverlays();
        if (overlays != null) {
            for (int i = 0; i < overlays.size(); i++) {
                if (((Overlay) overlays.get(i)).mType == 27) {
                    ItemizedOverlay itemizedOverlay = (ItemizedOverlay) overlays.get(i);
                    if (itemizedOverlay.c()) {
                        if (itemizedOverlay.getAllItem().size() <= 0) {
                            this.d.a.b().c(itemizedOverlay.d());
                            this.d.a.b().a(itemizedOverlay.d(), false);
                            this.d.a.b().a(itemizedOverlay.d());
                        } else {
                            this.d.a.b().a(itemizedOverlay.d(), true);
                            this.d.a.b().a(itemizedOverlay.d());
                        }
                        itemizedOverlay.a(false);
                    }
                }
                if (((Overlay) overlays.get(i)).mType == 14) {
                    PoiOverlay poiOverlay = (PoiOverlay) overlays.get(i);
                    if (!(poiOverlay.b() == null || poiOverlay.b().equals(""))) {
                        x xVar = (x) poiOverlay.getInnerOverlay();
                        xVar.a(poiOverlay.b());
                        xVar.a(true);
                        xVar.d();
                    }
                }
                if (((Overlay) overlays.get(i)).mType == 12) {
                    RouteOverlay routeOverlay = (RouteOverlay) overlays.get(i);
                    if (!(routeOverlay.e() == null || routeOverlay.e().equals(""))) {
                        y innerOverlay = routeOverlay.getInnerOverlay();
                        innerOverlay.a(routeOverlay.f());
                        innerOverlay.a(routeOverlay.e());
                        innerOverlay.a(true);
                        innerOverlay.d();
                    }
                }
                if (((Overlay) overlays.get(i)).mType == 28) {
                    TransitOverlay transitOverlay = (TransitOverlay) overlays.get(i);
                    if (!(transitOverlay.e() == null || transitOverlay.e().equals(""))) {
                        A innerOverlay2 = transitOverlay.getInnerOverlay();
                        innerOverlay2.a(transitOverlay.f());
                        innerOverlay2.a(transitOverlay.e());
                        innerOverlay2.a(true);
                        innerOverlay2.d();
                    }
                }
                if (((Overlay) overlays.get(i)).mType == 7) {
                    MyLocationOverlay myLocationOverlay = (MyLocationOverlay) overlays.get(i);
                    if (!(myLocationOverlay.d() == null || myLocationOverlay.d().equals(""))) {
                        n nVar = (n) myLocationOverlay.b();
                        nVar.a(myLocationOverlay.c());
                        nVar.a(myLocationOverlay.d());
                        nVar.a(true);
                        nVar.d();
                    }
                }
                if (((Overlay) overlays.get(i)).mType == 29) {
                    GraphicsOverlay graphicsOverlay = (GraphicsOverlay) overlays.get(i);
                    if (graphicsOverlay.d()) {
                        if (graphicsOverlay.getAllGraphics().size() == 0) {
                            this.d.a.b().c(graphicsOverlay.b());
                            this.d.a.b().a(graphicsOverlay.b(), false);
                            this.d.a.b().a(graphicsOverlay.b());
                        } else {
                            this.d.a.b().a(graphicsOverlay.b(), true);
                            this.d.a.b().a(graphicsOverlay.b());
                        }
                        graphicsOverlay.a(false);
                    }
                }
                if (((Overlay) overlays.get(i)).mType == 30) {
                    TextOverlay textOverlay = (TextOverlay) overlays.get(i);
                    if (textOverlay.size() == 0) {
                        this.d.a.b().c(textOverlay.mLayerID);
                        this.d.a.b().a(textOverlay.mLayerID, false);
                        this.d.a.b().a(textOverlay.mLayerID);
                    } else {
                        this.d.a.b().a(textOverlay.mLayerID, true);
                        this.d.a.b().a(textOverlay.mLayerID);
                    }
                }
            }
        }
    }

    public void regMapTouchListner(MKMapTouchListener mKMapTouchListener) {
        this.p = mKMapTouchListener;
    }

    public void regMapViewListener(BMapManager bMapManager, MKMapViewListener mKMapViewListener) {
        if (bMapManager != null) {
            this.o = mKMapViewListener;
        }
    }

    public void setBuiltInZoomControls(boolean z) {
        if (z) {
            if (this.q || this.h.getParent() != null) {
                removeView(this.h);
            }
            addView(this.h);
            this.q = true;
            return;
        }
        this.q = false;
        removeView(this.h);
    }

    public void setDoubleClickZooming(boolean z) {
        this.d.a.e(z);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.a.setOnClickListener(onClickListener);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.a.setOnLongClickListener(onLongClickListener);
    }

    public void setSatellite(boolean z) {
        this.s = z;
        this.a.a(z);
    }

    public void setTraffic(boolean z) {
        this.t = z;
        this.a.b(z);
    }

    public void setVisibility(int i) {
        if (this.a != null) {
            this.a.setVisibility(i);
        }
        super.setVisibility(i);
    }
}
