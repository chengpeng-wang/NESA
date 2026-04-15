package com.baidu.platform.comjni.map.search;

import android.os.Bundle;

public class JNISearch {
    public native boolean AreaMultiSearch(int i, Bundle bundle);

    public native boolean AreaSearch(int i, Bundle bundle);

    public native boolean BusLineDetailSearch(int i, String str, String str2);

    public native int Create();

    public native boolean ForceSearchByCityName(int i, Bundle bundle);

    public native boolean GeoDetailSearch(int i, Bundle bundle);

    public native boolean GeoSearch(int i, Bundle bundle);

    public native String GetSearchResult(int i, int i2);

    public native boolean MapBoundSearch(int i, Bundle bundle);

    public native boolean POIDetailSearchPlace(int i, String str);

    public native boolean PoiDetailShareUrlSearch(int i, String str);

    public native boolean PoiRGCShareUrlSearch(int i, int i2, int i3, String str, String str2);

    public native int QueryInterface(int i);

    public native int Release(int i);

    public native boolean ReverseGeocodeSearch(int i, int i2, int i3);

    public native boolean RoutePlanByBus(int i, Bundle bundle);

    public native boolean RoutePlanByCar(int i, Bundle bundle);

    public native boolean RoutePlanByFoot(int i, Bundle bundle);

    public native boolean SuggestionSearch(int i, Bundle bundle);

    public native boolean geocode(int i, String str, String str2);
}
