package com.baidu.mapapi.utils;

import com.baidu.platform.comapi.a.a;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.c;

public class CoordinateConvert {
    private static GeoPoint a(GeoPoint geoPoint, String str) {
        if (geoPoint == null) {
            return null;
        }
        c a = a.a().a((float) (((double) geoPoint.getLongitudeE6()) * 1.0E-6d), (float) (((double) geoPoint.getLatitudeE6()) * 1.0E-6d), str);
        return a != null ? e.a(new GeoPoint(a.b(), a.a())) : null;
    }

    public static GeoPoint fromGcjToBaidu(GeoPoint geoPoint) {
        return a(geoPoint, "gcj02");
    }

    public static GeoPoint fromWgs84ToBaidu(GeoPoint geoPoint) {
        return a(geoPoint, "wgs84");
    }
}
