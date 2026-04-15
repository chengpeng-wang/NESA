package com.baidu.platform.comapi.map;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.Overlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.b;
import com.baidu.platform.comapi.d.c;
import java.util.Collections;
import java.util.List;

public class q extends GLSurfaceView implements OnDoubleTapListener, OnGestureListener {
    static w a = null;
    static e b = null;
    public static int d = 0;
    private static o n = null;
    private static int o = 0;
    public MapRenderer c;
    u e = null;
    a f = null;
    c g = null;
    z h = null;
    GestureDetector i = new GestureDetector(this);
    List<Overlay> j = null;
    boolean k = false;
    private boolean l = false;
    private boolean m = false;
    private a p = null;
    private int q = 0;
    private int r = 0;

    public interface a {
        void a(Object obj);

        void b(Object obj);
    }

    public q(Context context) {
        super(context);
    }

    private void m() {
        com.baidu.platform.comjni.map.basemap.a b = n.b();
        if (b != null) {
            int a = b.a(0, 0, "compass");
            if (a > 0) {
                d = a;
                b.b(a, true);
                b.a(a, true);
                a.a(a, b.a());
            }
            a = b.a(0, 0, "logo");
            if (a > 0) {
                b.b(a, true);
                b.a(a, false);
                n.c = a;
                n.g.put("logo", Integer.valueOf(a));
            }
            a = b.a(0, 0, "popup");
            if (a > 0) {
                b.b(a, true);
                b.a(a, false);
                n.a = a;
                n.e.put("popup", Integer.valueOf(a));
            }
        }
    }

    public float a(b bVar) {
        if (n == null || n.b() == null) {
            return 3.0f;
        }
        if (bVar.a.a == bVar.b.a || bVar.a.b == bVar.b.b) {
            return 18.0f;
        }
        int abs = Math.abs(bVar.b.a - bVar.a.a);
        int abs2 = Math.abs(bVar.a.b - bVar.b.b);
        c.g();
        c.i();
        int width = getWidth() / 4;
        int height = getHeight() / 4;
        if (width <= 0 || height <= 0) {
            return 18.0f;
        }
        int i;
        abs = 0;
        for (i = abs; i > width; i >>= 1) {
            abs++;
        }
        i = 0;
        int i2 = abs2;
        while (i2 > height) {
            i2 >>= 1;
            i++;
        }
        float max = (float) (20 - Math.max(abs, i));
        return max >= 3.0f ? max > 19.0f ? 19.0f : max : 3.0f;
    }

    public int a(String str) {
        com.baidu.platform.comjni.map.basemap.a b = n.b();
        if (b == null) {
            return 0;
        }
        int a = str.equals("location") ? b.a(8, (int) LocationClientOption.MIN_SCAN_SPAN, "location") : b.a(0, 0, str);
        if (a > 0) {
            b.b(a, true);
            b.a(a, false);
        }
        return a;
    }

    public void a() {
        o--;
        if (o == 0) {
            n.m();
            n = null;
            a = null;
            b = null;
        }
        this.c = null;
    }

    public void a(int i) {
        a.a(i);
    }

    /* access modifiers changed from: 0000 */
    public void a(int i, int i2) {
        n.a((this.q / 2) + i, (this.r / 2) + i2);
    }

    public void a(int i, d dVar) {
        a.a(i, dVar);
    }

    public void a(Bundle bundle, Context context) {
        s sVar = new s();
        sVar.a(this.p);
        this.j = Collections.synchronizedList(sVar);
        if (n == null && a == null) {
            n = new o(context, this);
            a = new w(n);
            if (n != null) {
                n.a(bundle, a);
            }
            m();
            b = new e(n);
        }
        o++;
        if (n != null) {
            n.a(this);
            this.c = new MapRenderer(n.a());
            setRenderer(this.c);
            com.baidu.platform.comjni.map.basemap.a b = n.b();
            if (b != null) {
                b.a(bundle);
            }
        }
        setLongClickable(false);
        setFocusable(false);
    }

    /* access modifiers changed from: 0000 */
    public void a(GeoPoint geoPoint, Message message, Runnable runnable) {
        t k = n.k();
        k.d = geoPoint.getLongitudeE6();
        k.e = geoPoint.getLatitudeE6();
        n.a(k, 500);
    }

    public void a(a aVar) {
        this.f = aVar;
    }

    public void a(a aVar) {
        this.p = aVar;
    }

    public void a(t tVar) {
        n.a(tVar);
    }

    public void a(u uVar) {
        this.e = uVar;
    }

    public void a(boolean z) {
        this.m = z;
        com.baidu.platform.comjni.map.basemap.a b = n.b();
        if (b != null) {
            b.a(this.m);
        }
    }

    public boolean a(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (n == null) {
            return false;
        }
        return n.a(motionEvent) || this.i.onTouchEvent(motionEvent);
    }

    public o b() {
        return n;
    }

    public void b(boolean z) {
        this.l = z;
        com.baidu.platform.comjni.map.basemap.a b = n.b();
        if (b != null) {
            b.b(this.l);
        }
    }

    public List<Overlay> c() {
        return this.j;
    }

    public Projection d() {
        return b;
    }

    public boolean e() {
        return this.m;
    }

    public boolean f() {
        return this.l;
    }

    /* access modifiers changed from: 0000 */
    public double g() {
        return Math.pow(2.0d, (double) (18.0f - i()));
    }

    public GeoPoint h() {
        t k = n.k();
        return new GeoPoint(k.e, k.d);
    }

    public float i() {
        return n.l();
    }

    public int j() {
        return n.k().b;
    }

    public int k() {
        return n.k().c;
    }

    public t l() {
        return n.k();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        if (this.e != null) {
            this.e.b((int) motionEvent.getX(), (int) motionEvent.getY());
        }
        return false;
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return (n == null || !n.d()) ? false : n.a(motionEvent, motionEvent2, f, f2);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return (i == 21 || i == 29) ? n.a(1, 18, 0) == 1 : (i == 19 || i == 51) ? n.a(1, 19, 0) == 1 : (i == 20 || i == 47) ? n.a(1, 17, 0) == 1 : (i == 22 || i == 32) && n.a(1, 16, 0) == 1;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.e != null) {
            this.e.c((int) motionEvent.getX(), (int) motionEvent.getY());
        }
    }

    public void onPause() {
        if (n != null) {
            n.b().d();
        }
        super.onPause();
    }

    public void onResume() {
        if (n != null) {
            n.a(this);
            n.b().f();
            n.b().e();
            n.b().j();
        }
        super.onResume();
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        if (n == null) {
            return false;
        }
        if (n.d(motionEvent)) {
            return true;
        }
        if (this.e == null) {
            return false;
        }
        this.e.a((int) motionEvent.getX(), (int) motionEvent.getY());
        return false;
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        MapRenderer.a = i2;
        MapRenderer.b = i3;
        this.q = i2;
        this.r = i3;
        MapRenderer.c = 0;
        super.surfaceChanged(surfaceHolder, i, i2, i3);
        t l = l();
        l.f.a = 0;
        l.f.c = 0;
        l.f.d = i3;
        l.f.b = i2;
        a(l);
        n.e(this.q, this.r);
    }
}
