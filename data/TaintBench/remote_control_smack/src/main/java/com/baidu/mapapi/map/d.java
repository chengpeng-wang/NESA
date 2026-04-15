package com.baidu.mapapi.map;

import android.widget.Toast;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.a;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.f;
import com.baidu.platform.comapi.map.r;
import com.baidu.platform.comapi.map.t;
import com.baidu.platform.comapi.map.u;
import java.util.List;

class d implements u {
    final /* synthetic */ MapView a;

    d(MapView mapView) {
        this.a = mapView;
    }

    public void a() {
        this.a.a();
        if (this.a.o != null && !this.a.u) {
            if (this.a.getHeight() > 0 || this.a.getWidth() > 0) {
                this.a.o.onMapLoadFinish();
                this.a.u = true;
            }
        }
    }

    public void a(int i) {
        this.a.c(i);
    }

    public void a(int i, int i2) {
        if (this.a.p != null) {
            this.a.p.onMapClick(this.a.getProjection().fromPixels(i, i2));
        }
    }

    public void a(int i, GeoPoint geoPoint, int i2) {
        this.a.a(i, geoPoint, i2);
    }

    public void a(List<f> list) {
    }

    public void a(List<r> list, int i) {
        this.a.a((r) list.get(0), i);
    }

    public void b() {
        float i = this.a.a.i();
        if (i <= 3.0f) {
            if (this.a.i != i) {
                this.a.i = i;
                Toast.makeText(this.a.getContext(), "已缩小到最小级别", 0).show();
                this.a.a(true, false);
            }
        } else if (i >= 19.0f) {
            if (this.a.i != i) {
                this.a.i = i;
                Toast.makeText(this.a.getContext(), "已放大到最大级别", 0).show();
                this.a.a(false, true);
            }
        } else if (this.a.i != 0.0f) {
            this.a.i = 0.0f;
            this.a.a(true, true);
        }
        if (!(this.a.d.b == null || this.a.d.b.getTarget() == null)) {
            this.a.d.b.getTarget().sendMessage(this.a.d.b);
            this.a.d.b = null;
        }
        if (this.a.o != null && a.a) {
            this.a.o.onMapAnimationFinish();
        }
    }

    public void b(int i, int i2) {
        if (this.a.p != null) {
            this.a.p.onMapDoubleClick(this.a.getProjection().fromPixels(i, i2));
        }
    }

    public void b(List<r> list) {
    }

    public void b(List<r> list, int i) {
        this.a.c(((r) list.get(0)).b, i);
    }

    public void c() {
        float i = this.a.a.i();
        if (i <= 3.0f) {
            if (this.a.i != i) {
                this.a.i = i;
                Toast.makeText(this.a.getContext(), "已缩小到最小级别", 0).show();
                this.a.a(true, false);
            }
        } else if (i >= 19.0f) {
            if (this.a.i != i) {
                this.a.i = i;
                Toast.makeText(this.a.getContext(), "已放大到最大级别", 0).show();
                this.a.a(false, true);
            }
        } else if (this.a.i != 0.0f) {
            this.a.i = 0.0f;
            this.a.a(true, true);
        }
        if (this.a.o != null && a.a) {
            this.a.o.onMapMoveFinish();
        }
    }

    public void c(int i, int i2) {
        if (this.a.p != null) {
            this.a.p.onMapLongClick(this.a.getProjection().fromPixels(i, i2));
        }
    }

    public void c(List<r> list, int i) {
        if (list != null && list.size() > 0) {
            r rVar = (r) list.get(0);
            if (rVar.e == 17) {
                MapPoi mapPoi = new MapPoi();
                mapPoi.geoPt = e.a(rVar.d);
                mapPoi.strText = rVar.c.replaceAll("\\\\", "");
                mapPoi.offset = rVar.f;
                if (this.a.o != null && a.a) {
                    this.a.o.onClickMapPoi(mapPoi);
                }
            }
            if (rVar.e == 19) {
                t k = this.a.d.a.k();
                k.c = 0;
                k.b = 0;
                this.a.d.a.a(k, (int) MKEvent.ERROR_PERMISSION_DENIED);
            }
            if (rVar.e == 18) {
                this.a.b(i);
            }
            if (rVar.e == 23) {
                this.a.a(rVar, i);
            }
        }
    }
}
