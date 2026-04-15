package com.baidu.platform.comapi.map;

import android.graphics.Point;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public interface Projection {
    GeoPoint fromPixels(int i, int i2);

    float metersToEquatorPixels(float f);

    Point toPixels(GeoPoint geoPoint, Point point);
}
