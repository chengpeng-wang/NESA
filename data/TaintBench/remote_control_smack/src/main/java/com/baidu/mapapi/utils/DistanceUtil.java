package com.baidu.mapapi.utils;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.c;
import com.baidu.platform.comjni.tools.a;

public class DistanceUtil {
    public static double getDistance(GeoPoint geoPoint, GeoPoint geoPoint2) {
        GeoPoint b = e.b(geoPoint);
        GeoPoint b2 = e.b(geoPoint2);
        return (b == null || b2 == null) ? 0.0d : a.a(new c(b.getLongitudeE6(), b.getLatitudeE6()), new c(b2.getLongitudeE6(), b2.getLatitudeE6()));
    }
}
