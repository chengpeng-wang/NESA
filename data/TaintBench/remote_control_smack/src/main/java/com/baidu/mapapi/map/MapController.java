package com.baidu.mapapi.map;

import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.o;
import com.baidu.platform.comapi.map.t;

public class MapController {
    o a = null;
    Message b = null;
    private MapView c = null;
    private boolean d = true;
    private boolean e = true;
    private boolean f = true;
    private boolean g = true;

    public MapController(MapView mapView) {
        this.c = mapView;
    }

    public void animateTo(GeoPoint geoPoint) {
        if (geoPoint != null) {
            this.a.a(e.b(geoPoint));
        }
    }

    public void animateTo(GeoPoint geoPoint, Message message) {
        if (geoPoint != null) {
            GeoPoint b = e.b(geoPoint);
            this.b = message;
            this.a.a(b, message);
        }
    }

    public void enableClick(boolean z) {
        this.a.f(z);
    }

    public boolean handleFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return this.a.a(motionEvent, motionEvent2, f, f2);
    }

    public boolean isOverlookingGesturesEnabled() {
        return this.g;
    }

    public boolean isRotationGesturesEnabled() {
        return this.f;
    }

    public boolean isScrollGesturesEnabled() {
        return this.d;
    }

    public boolean isZoomGesturesEnabled() {
        return this.e;
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return this.a.onKey(view, i, keyEvent);
    }

    public void scrollBy(int i, int i2) {
        this.a.f(i, i2);
    }

    public void setCenter(GeoPoint geoPoint) {
        if (geoPoint != null) {
            t k = this.a.k();
            if (k != null) {
                GeoPoint b = e.b(geoPoint);
                k.d = b.getLongitudeE6();
                k.e = b.getLatitudeE6();
                this.a.a(k);
            }
        }
    }

    public void setCompassMargin(int i, int i2) {
        this.c.b(i, i2);
    }

    public void setOverlooking(int i) {
        if (i <= 0 && i >= -45) {
            t k = this.a.k();
            if (k != null) {
                k.c = i;
                this.a.a(k, (int) MKEvent.ERROR_PERMISSION_DENIED);
            }
        }
    }

    public void setOverlookingGesturesEnabled(boolean z) {
        this.g = z;
        this.a.d(z);
    }

    public void setRotation(int i) {
        t k = this.a.k();
        if (k != null) {
            k.b = i;
            this.a.a(k, (int) MKEvent.ERROR_PERMISSION_DENIED);
        }
    }

    public void setRotationGesturesEnabled(boolean z) {
        this.f = z;
        this.a.c(z);
    }

    public void setScrollGesturesEnabled(boolean z) {
        this.d = z;
        this.a.a(z);
    }

    public float setZoom(float f) {
        t k = this.a.k();
        if (k == null) {
            return -1.0f;
        }
        if (f < 3.0f) {
            f = 3.0f;
        } else if (f > 19.0f) {
            f = 19.0f;
        }
        k.a = f;
        this.a.a(k);
        if (f == 3.0f) {
            this.c.a(true, false);
            return f;
        } else if (f == 19.0f) {
            this.c.a(false, true);
            return f;
        } else {
            this.c.a(true, true);
            return f;
        }
    }

    public void setZoomGesturesEnabled(boolean z) {
        this.e = z;
        this.a.b(z);
    }

    public boolean zoomIn() {
        boolean g = this.a.g();
        int l = (int) this.a.l();
        if (l <= 3) {
            this.c.a(true, false);
        } else if (l >= 19) {
            this.c.a(false, true);
        } else {
            this.c.a(true, true);
        }
        return g;
    }

    public boolean zoomInFixing(int i, int i2) {
        boolean c = this.a.c(i, i2);
        int l = (int) this.a.l();
        if (l <= 3) {
            this.c.a(true, false);
        } else if (l >= 19) {
            this.c.a(false, true);
        } else {
            this.c.a(true, true);
        }
        return c;
    }

    public boolean zoomOut() {
        boolean h = this.a.h();
        int l = (int) this.a.l();
        if (l <= 3) {
            this.c.a(true, false);
        } else if (l >= 19) {
            this.c.a(false, true);
        } else {
            this.c.a(true, true);
        }
        return h;
    }

    public boolean zoomOutFixing(int i, int i2) {
        boolean d = this.a.d(i, i2);
        int l = (int) this.a.l();
        if (l <= 3) {
            this.c.a(true, false);
        } else if (l >= 19) {
            this.c.a(false, true);
        } else {
            this.c.a(true, true);
        }
        return d;
    }

    public void zoomToSpan(int i, int i2) {
        this.c.a(i, i2);
    }
}
