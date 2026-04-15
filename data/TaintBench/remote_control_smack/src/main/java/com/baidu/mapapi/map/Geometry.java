package com.baidu.mapapi.map;

import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;

public class Geometry {
    int a;
    ArrayList<GeoPoint> b;
    int c;

    public Geometry() {
        this.b = null;
        this.c = 0;
        this.b = new ArrayList();
    }

    public void setCircle(GeoPoint geoPoint, int i) {
        this.b.clear();
        this.a = 4;
        this.b.add(geoPoint);
        this.c = e.a(geoPoint, i);
    }

    public void setEnvelope(GeoPoint geoPoint, GeoPoint geoPoint2) {
        this.b.clear();
        this.a = 3;
        this.b.add(geoPoint);
        this.b.add(geoPoint2);
    }

    public void setPoint(GeoPoint geoPoint, int i) {
        this.b.clear();
        this.a = 1;
        this.c = i;
        this.b.add(geoPoint);
    }

    public void setPolyLine(GeoPoint[] geoPointArr) {
        this.a = 2;
        if (geoPointArr != null) {
            this.b.clear();
            for (Object add : geoPointArr) {
                this.b.add(add);
            }
        }
    }

    public void setPolygon(GeoPoint[] geoPointArr) {
        this.a = 5;
        if (geoPointArr != null) {
            this.b.clear();
            for (Object add : geoPointArr) {
                this.b.add(add);
            }
        }
    }
}
