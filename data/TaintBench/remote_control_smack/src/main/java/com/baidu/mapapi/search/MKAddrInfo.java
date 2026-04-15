package com.baidu.mapapi.search;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;

public class MKAddrInfo {
    public static final int MK_GEOCODE = 0;
    public static final int MK_REVERSEGEOCODE = 1;
    public MKGeocoderAddressComponent addressComponents;
    public GeoPoint geoPt;
    public ArrayList<MKPoiInfo> poiList;
    public String strAddr;
    public String strBusiness;
    public int type;
}
