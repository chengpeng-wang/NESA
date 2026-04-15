package com.baidu.mapapi.map;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public interface MKMapTouchListener {
    void onMapClick(GeoPoint geoPoint);

    void onMapDoubleClick(GeoPoint geoPoint);

    void onMapLongClick(GeoPoint geoPoint);
}
