package com.baidu.mapapi.cloud;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.baidu.mapapi.search.c;

public class GeoSearchManager {
    public static final int GEO_SEARCH = 50;
    public static final int GEO_SEARCH_DETAILS = 51;
    static a b;
    static Handler c = new a();
    private static GeoSearchManager e;
    com.baidu.platform.comjni.map.search.a a;
    private Bundle d = null;

    private class a {
        GeoSearchListener a;
        GeoSearchManager b;

        public a(GeoSearchListener geoSearchListener) {
            this.a = geoSearchListener;
        }

        public void a(Message message) {
            if (message.what == 2000 && this.a != null) {
                String a;
                switch (message.arg1) {
                    case GeoSearchManager.GEO_SEARCH /*50*/:
                        a = this.b.a(50);
                        GeoSearchResult geoSearchResult = new GeoSearchResult();
                        c.a(a, geoSearchResult);
                        this.a.onGetGeoResult(geoSearchResult, message.arg1, message.arg2);
                        return;
                    case 51:
                        a = this.b.a(51);
                        DetailResult detailResult = new DetailResult();
                        c.a(a, detailResult);
                        this.a.onGetGeoDetailsResult(detailResult, message.arg1, message.arg2);
                        return;
                    default:
                        return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void a(GeoSearchManager geoSearchManager) {
            this.b = geoSearchManager;
        }
    }

    public static GeoSearchManager getInstance() {
        if (e == null) {
            e = new GeoSearchManager();
        }
        return e;
    }

    /* access modifiers changed from: 0000 */
    public String a(int i) {
        String a = this.a.a(i);
        return (a == null || a.trim().length() > 0) ? a : null;
    }

    public void destory() {
        if (b != null) {
            b.a(null);
            b.a = null;
        }
        b = null;
        if (c != null) {
            com.baidu.platform.comjni.engine.a.b(2000, c);
        }
        if (this.a != null) {
            this.a.c();
            this.a = null;
        }
    }

    public boolean init(GeoSearchListener geoSearchListener) {
        if (this.a == null) {
            this.a = new com.baidu.platform.comjni.map.search.a();
            if (this.a.a() == 0) {
                this.a = null;
                return false;
            }
            if (b == null) {
                b = new a(geoSearchListener);
            } else {
                b.a = geoSearchListener;
            }
            b.a(this);
            com.baidu.platform.comjni.engine.a.a(2000, c);
        } else {
            this.a.b();
            if (b == null) {
                b = new a(geoSearchListener);
            } else {
                b.a = geoSearchListener;
            }
            b.a(this);
            com.baidu.platform.comjni.engine.a.a(2000, c);
        }
        return true;
    }

    public boolean searchBounds(BoundsSearchInfo boundsSearchInfo) {
        if (boundsSearchInfo.ak == null) {
            return false;
        }
        if (this.d == null) {
            this.d = new Bundle();
        } else {
            this.d.clear();
        }
        this.d.putString("url", "http://api.map.baidu.com/geosearch/poi" + boundsSearchInfo.a());
        return this.a.i(this.d);
    }

    public boolean searchDetail(DetailSearchInfo detailSearchInfo) {
        if (detailSearchInfo.ak == null) {
            return false;
        }
        if (this.d == null) {
            this.d = new Bundle();
        } else {
            this.d.clear();
        }
        this.d.putString("url", "http://api.map.baidu.com/geosearch/detail" + detailSearchInfo.a());
        return this.a.j(this.d);
    }

    public boolean searchNearby(NearbySearchInfo nearbySearchInfo) {
        if (nearbySearchInfo.ak == null) {
            return false;
        }
        if (this.d == null) {
            this.d = new Bundle();
        } else {
            this.d.clear();
        }
        this.d.putString("url", "http://api.map.baidu.com/geosearch/poi" + nearbySearchInfo.a());
        return this.a.i(this.d);
    }

    public boolean searchRegion(RegionSearchInfo regionSearchInfo) {
        if (regionSearchInfo.ak == null) {
            return false;
        }
        if (this.d == null) {
            this.d = new Bundle();
        } else {
            this.d.clear();
        }
        this.d.putString("url", "http://api.map.baidu.com/geosearch/poi" + regionSearchInfo.a());
        return this.a.i(this.d);
    }
}
