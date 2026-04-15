package com.baidu.mapapi.map;

import android.graphics.Bitmap;

public interface MKMapViewListener {
    void onClickMapPoi(MapPoi mapPoi);

    void onGetCurrentMap(Bitmap bitmap);

    void onMapAnimationFinish();

    void onMapLoadFinish();

    void onMapMoveFinish();
}
