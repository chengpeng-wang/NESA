package com.baidu.mapapi.cloud;

public interface GeoSearchListener {
    void onGetGeoDetailsResult(DetailResult detailResult, int i, int i2);

    void onGetGeoResult(GeoSearchResult geoSearchResult, int i, int i2);
}
