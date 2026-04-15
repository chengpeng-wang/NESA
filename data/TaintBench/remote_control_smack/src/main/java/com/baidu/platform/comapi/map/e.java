package com.baidu.platform.comapi.map;

import android.graphics.Point;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comjni.map.basemap.a;
import org.json.JSONException;
import org.json.JSONObject;

class e implements Projection {
    private o a = null;

    public e(o oVar) {
        this.a = oVar;
    }

    public GeoPoint fromPixels(int i, int i2) {
        String a = this.a.b().a(i, i2);
        GeoPoint geoPoint = new GeoPoint(0, 0);
        if (a != null) {
            try {
                JSONObject jSONObject = new JSONObject(a);
                geoPoint.setLongitudeE6(jSONObject.getInt("geox"));
                geoPoint.setLatitudeE6(jSONObject.getInt("geoy"));
                return geoPoint;
            } catch (JSONException e) {
            }
        }
        return null;
    }

    public float metersToEquatorPixels(float f) {
        return (float) (((double) f) / this.a.c());
    }

    public Point toPixels(GeoPoint geoPoint, Point point) {
        if (point == null) {
            point = new Point(0, 0);
        }
        a b = this.a.b();
        if (b != null) {
            String b2 = b.b(geoPoint.getLongitudeE6(), geoPoint.getLatitudeE6());
            if (b2 != null) {
                try {
                    JSONObject jSONObject = new JSONObject(b2);
                    point.x = jSONObject.getInt("scrx");
                    point.y = jSONObject.getInt("scry");
                } catch (JSONException e) {
                }
            }
        }
        return point;
    }
}
