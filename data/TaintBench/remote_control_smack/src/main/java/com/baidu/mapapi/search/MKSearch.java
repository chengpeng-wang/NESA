package com.baidu.mapapi.search;

import android.content.Intent;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.platform.comapi.b.c;
import com.baidu.platform.comapi.b.e;
import com.baidu.platform.comapi.b.g;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smackx.Form;
import org.json.JSONException;

public class MKSearch {
    public static final int EBUS_NO_SUBWAY = 6;
    public static final int EBUS_TIME_FIRST = 3;
    public static final int EBUS_TRANSFER_FIRST = 4;
    public static final int EBUS_WALK_FIRST = 5;
    public static final int ECAR_AVOID_JAM = -1;
    public static final int ECAR_DIS_FIRST = 1;
    public static final int ECAR_FEE_FIRST = 2;
    public static final int ECAR_TIME_FIRST = 0;
    public static final int POI_DETAIL_SEARCH = 52;
    public static final int TYPE_AREA_MULTI_POI_LIST = 45;
    public static final int TYPE_AREA_POI_LIST = 21;
    public static final int TYPE_CITY_LIST = 7;
    public static final int TYPE_POI_LIST = 11;
    MKSearchListener a;
    private b b = new b();
    private String c;
    private String[] d;
    private String e;
    private int f;
    private int g = 3;
    private int h = 0;
    /* access modifiers changed from: private */
    public int i = 0;
    /* access modifiers changed from: private */
    public int j = 0;
    private a k = new a();
    /* access modifiers changed from: private */
    public BMapManager l;
    /* access modifiers changed from: private */
    public int m = 0;

    private class a implements c {
        private a() {
        }

        public void a(int i) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                switch (i) {
                    case 5:
                    case 8:
                    case 404:
                        i = 2;
                        break;
                    case MKSearch.TYPE_POI_LIST /*11*/:
                    case 12:
                    case 13:
                    case MKEvent.MKEVENT_MAP_MOVE_FINISH /*14*/:
                        i = 100;
                        break;
                }
                switch (MKSearch.this.j) {
                    case 1:
                        MKSearch.this.a.onGetWalkingRouteResult(null, i);
                        return;
                    case 2:
                        MKSearch.this.a.onGetTransitRouteResult(null, i);
                        return;
                    case 3:
                        MKSearch.this.a.onGetTransitRouteResult(null, i);
                        return;
                    case 4:
                        MKSearch.this.a.onGetAddrResult(null, i);
                        MKSearch.this.a.onGetAddrResult(null, i);
                        return;
                    case 6:
                        MKSearch.this.a.onGetSuggestionResult(null, i);
                        return;
                    case 7:
                    case 8:
                    case 9:
                        MKSearch.this.a.onGetPoiResult(null, 1, i);
                        return;
                    case 13:
                        MKSearch.this.a.onGetBusDetailResult(null, i);
                        return;
                    default:
                        return;
                }
            }
        }

        public void a(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                MKPoiResult mKPoiResult;
                switch (MKSearch.this.j) {
                    case 4:
                        MKAddrInfo mKAddrInfo = new MKAddrInfo();
                        if (c.b(str, mKAddrInfo)) {
                            MKSearch.this.a.onGetAddrResult(mKAddrInfo, 0);
                            return;
                        } else {
                            MKSearch.this.a.onGetAddrResult(mKAddrInfo, 100);
                            return;
                        }
                    case 7:
                        mKPoiResult = new MKPoiResult();
                        c.a(str, mKPoiResult, MKSearch.this.m);
                        if (MKSearch.this.a != null) {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 11, 0);
                            return;
                        } else {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 11, 100);
                            return;
                        }
                    case 8:
                    case 9:
                        mKPoiResult = new MKPoiResult();
                        c.a(str, mKPoiResult, MKSearch.this.m);
                        if (MKSearch.this.a != null) {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 21, 0);
                            return;
                        } else {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 21, 100);
                            return;
                        }
                    case 10:
                    case MKSearch.TYPE_POI_LIST /*11*/:
                        mKPoiResult = new MKPoiResult();
                        if (c.b(str, mKPoiResult, MKSearch.this.m)) {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 45, 0);
                            return;
                        } else {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 45, 100);
                            return;
                        }
                    default:
                        return;
                }
            }
        }

        public void b(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                MKPoiResult mKPoiResult;
                switch (MKSearch.this.j) {
                    case 7:
                    case 8:
                    case 9:
                        mKPoiResult = new MKPoiResult();
                        c.a(str, mKPoiResult);
                        if (MKSearch.this.a != null) {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 7, 4);
                            return;
                        } else {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 7, 100);
                            return;
                        }
                    case 10:
                    case MKSearch.TYPE_POI_LIST /*11*/:
                        mKPoiResult = new MKPoiResult();
                        if (c.b(str, mKPoiResult, MKSearch.this.m)) {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 45, 4);
                            return;
                        } else {
                            MKSearch.this.a.onGetPoiResult(mKPoiResult, 45, 100);
                            return;
                        }
                    default:
                        return;
                }
            }
        }

        public void c(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                MKRouteAddrResult mKRouteAddrResult = new MKRouteAddrResult();
                switch (MKSearch.this.j) {
                    case 1:
                        MKWalkingRouteResult mKWalkingRouteResult = new MKWalkingRouteResult();
                        if (c.a(str, mKRouteAddrResult)) {
                            mKWalkingRouteResult.a(mKRouteAddrResult);
                            MKSearch.this.a.onGetWalkingRouteResult(mKWalkingRouteResult, 4);
                            return;
                        }
                        MKSearch.this.a.onGetWalkingRouteResult(mKWalkingRouteResult, 100);
                        return;
                    case 2:
                        MKDrivingRouteResult mKDrivingRouteResult = new MKDrivingRouteResult();
                        if (c.a(str, mKRouteAddrResult)) {
                            mKDrivingRouteResult.a(mKRouteAddrResult);
                            MKSearch.this.a.onGetDrivingRouteResult(mKDrivingRouteResult, 4);
                            return;
                        }
                        MKSearch.this.a.onGetDrivingRouteResult(mKDrivingRouteResult, 100);
                        return;
                    case 3:
                        MKTransitRouteResult mKTransitRouteResult = new MKTransitRouteResult();
                        if (c.a(str, mKRouteAddrResult)) {
                            mKTransitRouteResult.a(mKRouteAddrResult);
                            MKSearch.this.a.onGetTransitRouteResult(mKTransitRouteResult, 4);
                            return;
                        }
                        MKSearch.this.a.onGetTransitRouteResult(mKTransitRouteResult, 100);
                        return;
                    default:
                        return;
                }
            }
        }

        public void d(String str) {
        }

        public void e(String str) {
        }

        public void f(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                switch (MKSearch.this.j) {
                    case 12:
                        if (!PlaceCaterActivity.isShow()) {
                            if (c.a(str, new e())) {
                                Intent intent = new Intent(MKSearch.this.l.getContext(), PlaceCaterActivity.class);
                                intent.putExtra(Form.TYPE_RESULT, str);
                                intent.addFlags(268435456);
                                MKSearch.this.l.getContext().startActivity(intent);
                                MKSearch.this.a.onGetPoiDetailSearchResult(52, 0);
                            } else {
                                MKSearch.this.a.onGetPoiDetailSearchResult(52, 100);
                            }
                        }
                        MKSearch.this.j = MKSearch.this.i;
                        return;
                    default:
                        return;
                }
            }
        }

        public void g(String str) {
            if (str != null && MKSearch.this.a != null) {
                MKShareUrlResult mKShareUrlResult = new MKShareUrlResult();
                if (c.a(str, mKShareUrlResult)) {
                    switch (MKSearch.this.j) {
                        case MKEvent.MKEVENT_MAP_MOVE_FINISH /*14*/:
                            mKShareUrlResult.type = 17;
                            break;
                        case 15:
                            mKShareUrlResult.type = 18;
                            break;
                    }
                    MKSearch.this.a.onGetShareUrlResult(mKShareUrlResult, mKShareUrlResult.type, 0);
                    return;
                }
                MKSearch.this.a.onGetShareUrlResult(mKShareUrlResult, -1, -1);
            }
        }

        public void h(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                MKDrivingRouteResult mKDrivingRouteResult = new MKDrivingRouteResult();
                try {
                    if (c.a(str, mKDrivingRouteResult)) {
                        MKSearch.this.a.onGetDrivingRouteResult(mKDrivingRouteResult, 0);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MKSearch.this.a.onGetDrivingRouteResult(mKDrivingRouteResult, 100);
            }
        }

        public void i(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                MKWalkingRouteResult mKWalkingRouteResult = new MKWalkingRouteResult();
                try {
                    if (c.a(str, mKWalkingRouteResult)) {
                        MKSearch.this.a.onGetWalkingRouteResult(mKWalkingRouteResult, 0);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MKSearch.this.a.onGetWalkingRouteResult(mKWalkingRouteResult, 100);
            }
        }

        public void j(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                MKTransitRouteResult mKTransitRouteResult = new MKTransitRouteResult();
                try {
                    if (c.a(str, mKTransitRouteResult)) {
                        MKSearch.this.a.onGetTransitRouteResult(mKTransitRouteResult, 0);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MKSearch.this.a.onGetTransitRouteResult(mKTransitRouteResult, 100);
            }
        }

        public void k(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                MKAddrInfo mKAddrInfo;
                switch (MKSearch.this.j) {
                    case 4:
                        mKAddrInfo = new MKAddrInfo();
                        if (c.b(str, mKAddrInfo)) {
                            MKSearch.this.a.onGetAddrResult(mKAddrInfo, 0);
                            return;
                        } else {
                            MKSearch.this.a.onGetAddrResult(mKAddrInfo, 100);
                            return;
                        }
                    case 5:
                        mKAddrInfo = new MKAddrInfo();
                        if (c.a(str, mKAddrInfo)) {
                            MKSearch.this.a.onGetAddrResult(mKAddrInfo, 0);
                            return;
                        } else {
                            MKSearch.this.a.onGetAddrResult(mKAddrInfo, 100);
                            return;
                        }
                    default:
                        return;
                }
            }
        }

        public void l(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                MKBusLineResult mKBusLineResult = new MKBusLineResult();
                if (c.a(str, mKBusLineResult)) {
                    MKSearch.this.a.onGetBusDetailResult(mKBusLineResult, 0);
                } else {
                    MKSearch.this.a.onGetBusDetailResult(mKBusLineResult, 100);
                }
            }
        }

        public void m(String str) {
            if (MKSearch.this.a != null && com.baidu.platform.comapi.a.a) {
                switch (MKSearch.this.j) {
                    case 6:
                        MKSuggestionResult mKSuggestionResult = new MKSuggestionResult();
                        if (c.a(str, mKSuggestionResult)) {
                            MKSearch.this.a.onGetSuggestionResult(mKSuggestionResult, 0);
                            return;
                        } else {
                            MKSearch.this.a.onGetSuggestionResult(mKSuggestionResult, 100);
                            return;
                        }
                    default:
                        return;
                }
            }
        }

        public void n(String str) {
        }

        public void o(String str) {
        }
    }

    private static com.baidu.platform.comapi.basestruct.c a(GeoPoint geoPoint) {
        return com.baidu.platform.comapi.a.a.a().a((float) (((double) geoPoint.getLongitudeE6()) / 1000000.0d), (float) (((double) geoPoint.getLatitudeE6()) / 1000000.0d), "bd09ll");
    }

    public static int getPoiPageCapacity() {
        return e.a().c();
    }

    public static void setPoiPageCapacity(int i) {
        e.a().a(i);
    }

    public int busLineSearch(String str, String str2) {
        if (str == null || str.length() <= 0 || str2 == null || str2.length() <= 0 || str.length() > 31) {
            return -1;
        }
        this.i = this.j;
        this.j = 13;
        return e.a().a(str, str2) ? 0 : -1;
    }

    public int drivingSearch(String str, MKPlanNode mKPlanNode, String str2, MKPlanNode mKPlanNode2) {
        if (mKPlanNode == null || mKPlanNode2 == null) {
            return -1;
        }
        String str3;
        String str4;
        com.baidu.platform.comapi.b.b bVar = new com.baidu.platform.comapi.b.b();
        if (mKPlanNode.name != null) {
            bVar.c = mKPlanNode.name;
        }
        if (mKPlanNode.pt != null) {
            bVar.b = a(mKPlanNode.pt);
            bVar.a = 1;
            str3 = str;
        } else {
            str3 = (str == null || "".equals(str)) ? "1" : str;
        }
        com.baidu.platform.comapi.b.b bVar2 = new com.baidu.platform.comapi.b.b();
        if (mKPlanNode2.name != null) {
            bVar2.c = mKPlanNode2.name;
        }
        if (mKPlanNode2.pt != null) {
            bVar2.b = a(mKPlanNode2.pt);
            bVar2.a = 1;
            str4 = str2;
        } else {
            str4 = (str2 == null || "".equals(str2)) ? "1" : str2;
        }
        this.i = this.j;
        this.j = 2;
        return e.a().a(bVar, bVar2, null, str3, str4, null, 12, this.h, 0, null, null) ? 0 : -1;
    }

    public int drivingSearch(String str, MKPlanNode mKPlanNode, String str2, MKPlanNode mKPlanNode2, List<MKWpNode> list) {
        if (mKPlanNode == null || mKPlanNode2 == null) {
            return -1;
        }
        String str3;
        String str4;
        com.baidu.platform.comapi.b.b bVar = new com.baidu.platform.comapi.b.b();
        if (mKPlanNode.name != null) {
            bVar.c = mKPlanNode.name;
        }
        if (mKPlanNode.pt != null) {
            bVar.b = a(mKPlanNode.pt);
            bVar.a = 1;
            str3 = str;
        } else {
            str3 = (str == null || "".equals(str)) ? "1" : str;
        }
        com.baidu.platform.comapi.b.b bVar2 = new com.baidu.platform.comapi.b.b();
        if (mKPlanNode2.name != null) {
            bVar2.c = mKPlanNode2.name;
        }
        if (mKPlanNode2.pt != null) {
            bVar2.b = a(mKPlanNode2.pt);
            bVar2.a = 1;
            str4 = str2;
        } else {
            str4 = (str2 == null || "".equals(str2)) ? "1" : str2;
        }
        this.i = this.j;
        this.j = 2;
        ArrayList arrayList = new ArrayList();
        for (MKWpNode mKWpNode : list) {
            if (!(mKWpNode.pt == null && (mKWpNode.name == null || mKWpNode.city == null || mKWpNode.name.length() == 0 || mKWpNode.city.length() == 0))) {
                g gVar = new g();
                if (mKWpNode.name != null) {
                    gVar.b = mKWpNode.name;
                }
                if (mKWpNode.pt != null) {
                    gVar.a = a(mKWpNode.pt);
                }
                if (mKWpNode.city == null) {
                    gVar.c = "";
                } else {
                    gVar.c = mKWpNode.city;
                }
                arrayList.add(gVar);
            }
        }
        return e.a().a(bVar, bVar2, null, str3, str4, null, 12, this.h, 0, arrayList, null) ? 0 : -1;
    }

    public int geocode(String str, String str2) {
        if (str == null || str.length() == 0 || str.length() > 99) {
            return -1;
        }
        this.i = this.j;
        this.j = 4;
        return e.a().b(str, str2) ? 0 : -1;
    }

    public int goToPoiPage(int i) {
        e a;
        String str;
        switch (this.j) {
            case 7:
                a = e.a();
                str = this.c;
                String str2 = this.e;
                this.m = i;
                return a.a(str, str2, i, null, 12, null) ? 0 : -1;
            case 8:
                a = e.a();
                str = this.c;
                this.m = i;
                return a.a(str, 1, i, this.b, 12, null, null) ? 0 : -1;
            case 9:
                Map hashMap = new HashMap();
                hashMap.put("distance", "" + this.f);
                a = e.a();
                str = this.c;
                this.m = i;
                return a.a(str, 1, i, 12, this.b, this.b, hashMap) ? 0 : -1;
            case 10:
            case TYPE_POI_LIST /*11*/:
                a = e.a();
                String[] strArr = this.d;
                this.m = i;
                return a.a(strArr, 1, i, 12, 0, this.b, this.b, null) ? 0 : -1;
            default:
                return 0;
        }
    }

    public boolean init(BMapManager bMapManager, MKSearchListener mKSearchListener) {
        if (bMapManager == null) {
            return false;
        }
        this.l = bMapManager;
        if (mKSearchListener != null) {
            this.a = mKSearchListener;
        }
        e.a().a(this.k);
        return true;
    }

    public int poiDetailSearch(String str) {
        if (str == null || this.j == 12) {
            return -1;
        }
        this.i = this.j;
        this.j = 12;
        return e.a().a(str) ? 0 : -1;
    }

    public boolean poiDetailShareURLSearch(String str) {
        if (str == null) {
            return false;
        }
        this.i = this.j;
        this.j = 15;
        return e.a().b(str);
    }

    public int poiMultiSearchInbounds(String[] strArr, GeoPoint geoPoint, GeoPoint geoPoint2) {
        if (strArr == null || geoPoint == null || geoPoint2 == null) {
            return -1;
        }
        if (strArr.length < 2 || strArr.length > 10) {
            return -1;
        }
        com.baidu.platform.comapi.basestruct.c a = com.baidu.platform.comapi.a.a.a().a((float) (((double) geoPoint.getLongitudeE6()) / 1000000.0d), (float) (((double) geoPoint.getLatitudeE6()) / 1000000.0d), "bd09ll");
        com.baidu.platform.comapi.basestruct.c a2 = com.baidu.platform.comapi.a.a.a().a((float) (((double) geoPoint2.getLongitudeE6()) / 1000000.0d), (float) (((double) geoPoint2.getLatitudeE6()) / 1000000.0d), "bd09ll");
        this.b.a(a);
        this.b.b(a2);
        this.i = this.j;
        this.j = 10;
        this.d = strArr;
        e a3 = e.a();
        String[] strArr2 = this.d;
        this.m = 0;
        return !a3.a(strArr2, 1, 0, 12, 0, this.b, this.b, null) ? -1 : 0;
    }

    public int poiMultiSearchNearBy(String[] strArr, GeoPoint geoPoint, int i) {
        if (geoPoint == null || strArr == null) {
            return -1;
        }
        if (i <= 0) {
            return -1;
        }
        if (strArr.length < 2 || strArr.length > 10) {
            return -1;
        }
        com.baidu.platform.comapi.basestruct.c a = com.baidu.platform.comapi.a.a.a().a((float) (((double) geoPoint.getLongitudeE6()) / 1000000.0d), (float) (((double) geoPoint.getLatitudeE6()) / 1000000.0d), "bd09ll");
        com.baidu.platform.comapi.basestruct.c cVar = new com.baidu.platform.comapi.basestruct.c(a.a - i, a.b - i);
        com.baidu.platform.comapi.basestruct.c cVar2 = new com.baidu.platform.comapi.basestruct.c(a.a + i, a.b + i);
        this.b.a(cVar);
        this.b.b(cVar2);
        this.i = this.j;
        this.j = 11;
        this.d = strArr;
        e a2 = e.a();
        String[] strArr2 = this.d;
        this.m = 0;
        return !a2.a(strArr2, 1, 0, 12, 0, this.b, this.b, null) ? -1 : 0;
    }

    public boolean poiRGCShareURLSearch(GeoPoint geoPoint, String str, String str2) {
        if (geoPoint == null) {
            return false;
        }
        GeoPoint b = com.baidu.mapapi.utils.e.b(geoPoint);
        com.baidu.platform.comapi.basestruct.c cVar = new com.baidu.platform.comapi.basestruct.c(b.getLongitudeE6(), b.getLatitudeE6());
        this.i = this.j;
        this.j = 14;
        return e.a().a(cVar, str, str2);
    }

    public int poiSearchInCity(String str, String str2) {
        if (str2 == null) {
            return -1;
        }
        if (str == null) {
            str = "";
        }
        String trim = str.trim();
        String trim2 = str2.trim();
        if (trim.length() > 16) {
            return -1;
        }
        if (trim2.length() == 0 || trim2.length() > 99) {
            return -1;
        }
        this.c = trim2;
        this.e = trim;
        this.i = this.j;
        this.j = 7;
        e a = e.a();
        trim2 = this.c;
        String str3 = this.e;
        this.m = 0;
        return !a.a(trim2, str3, 0, null, 12, null) ? -1 : 0;
    }

    public int poiSearchInbounds(String str, GeoPoint geoPoint, GeoPoint geoPoint2) {
        if (str == null || geoPoint == null || geoPoint2 == null) {
            return -1;
        }
        String trim = str.trim();
        if (trim.length() == 0 || trim.length() > 99) {
            return -1;
        }
        com.baidu.platform.comapi.basestruct.c a = com.baidu.platform.comapi.a.a.a().a((float) (((double) geoPoint.getLongitudeE6()) / 1000000.0d), (float) (((double) geoPoint.getLatitudeE6()) / 1000000.0d), "bd09ll");
        com.baidu.platform.comapi.basestruct.c a2 = com.baidu.platform.comapi.a.a.a().a((float) (((double) geoPoint2.getLongitudeE6()) / 1000000.0d), (float) (((double) geoPoint2.getLatitudeE6()) / 1000000.0d), "bd09ll");
        this.b.a(a);
        this.b.b(a2);
        this.i = this.j;
        this.j = 8;
        this.c = trim;
        e a3 = e.a();
        String str2 = this.c;
        this.m = 0;
        return a3.a(str2, 1, 0, this.b, 12, null, null) ? 0 : -1;
    }

    public int poiSearchNearBy(String str, GeoPoint geoPoint, int i) {
        if (geoPoint == null || str == null) {
            return -1;
        }
        if (i <= 0) {
            return -1;
        }
        String trim = str.trim();
        if (trim.length() == 0 || trim.length() > 99) {
            return -1;
        }
        GeoPoint b = com.baidu.mapapi.utils.e.b(geoPoint);
        com.baidu.platform.comapi.basestruct.c cVar = new com.baidu.platform.comapi.basestruct.c(b.getLongitudeE6() - i, b.getLatitudeE6() - i);
        com.baidu.platform.comapi.basestruct.c cVar2 = new com.baidu.platform.comapi.basestruct.c(b.getLongitudeE6() + i, b.getLatitudeE6() + i);
        this.b.a(cVar);
        this.b.b(cVar2);
        this.c = trim;
        this.i = this.j;
        this.j = 9;
        this.f = i;
        Map hashMap = new HashMap();
        hashMap.put("distance", "" + this.f);
        e a = e.a();
        String str2 = this.c;
        this.m = 0;
        return !a.a(str2, 1, 0, 12, this.b, this.b, hashMap) ? -1 : 0;
    }

    public int reverseGeocode(GeoPoint geoPoint) {
        if (geoPoint == null) {
            return -1;
        }
        com.baidu.platform.comapi.basestruct.c a = a(geoPoint);
        this.i = this.j;
        this.j = 5;
        return e.a().a(a) ? 0 : -1;
    }

    public int setDrivingPolicy(int i) {
        if (i > 2 || i < -1) {
            return -1;
        }
        this.h = i;
        return 0;
    }

    public int setTransitPolicy(int i) {
        if (i > 6 || i < 3) {
            return -1;
        }
        this.g = i;
        return 0;
    }

    public int suggestionSearch(String str, String str2) {
        if (str == null) {
            return -1;
        }
        String str3 = (str2 == null || str2.length() == 0) ? "1" : str2;
        String trim = str.trim();
        if (trim.length() == 0 || trim.length() > 99) {
            return -1;
        }
        this.i = this.j;
        this.j = 6;
        return !e.a().a(trim, 0, str3, null, 12, null) ? -1 : 0;
    }

    public int transitSearch(String str, MKPlanNode mKPlanNode, MKPlanNode mKPlanNode2) {
        if (str == null || mKPlanNode == null || mKPlanNode2 == null) {
            return -1;
        }
        if (str.length() > 31) {
            return -1;
        }
        com.baidu.platform.comapi.b.b bVar = new com.baidu.platform.comapi.b.b();
        if (mKPlanNode.name != null) {
            bVar.c = mKPlanNode.name;
        }
        if (mKPlanNode.pt != null) {
            bVar.b = a(mKPlanNode.pt);
            bVar.a = 1;
        }
        com.baidu.platform.comapi.b.b bVar2 = new com.baidu.platform.comapi.b.b();
        if (mKPlanNode2.name != null) {
            bVar2.c = mKPlanNode2.name;
        }
        if (mKPlanNode2.pt != null) {
            bVar2.b = a(mKPlanNode2.pt);
            bVar2.a = 1;
        }
        this.i = this.j;
        this.j = 3;
        return e.a().a(bVar, bVar2, str, null, 12, this.g, null) ? 0 : -1;
    }

    public int walkingSearch(String str, MKPlanNode mKPlanNode, String str2, MKPlanNode mKPlanNode2) {
        if (mKPlanNode == null || mKPlanNode2 == null) {
            return -1;
        }
        String str3;
        String str4;
        com.baidu.platform.comapi.b.b bVar = new com.baidu.platform.comapi.b.b();
        if (mKPlanNode.name != null) {
            bVar.c = mKPlanNode.name;
        }
        if (mKPlanNode.pt != null) {
            bVar.b = a(mKPlanNode.pt);
            bVar.a = 1;
            str3 = str;
        } else {
            str3 = (str == null || "".equals(str)) ? "1" : str;
        }
        com.baidu.platform.comapi.b.b bVar2 = new com.baidu.platform.comapi.b.b();
        if (mKPlanNode2.name != null) {
            bVar2.c = mKPlanNode2.name;
        }
        if (mKPlanNode2.pt != null) {
            bVar2.b = a(mKPlanNode2.pt);
            bVar2.a = 1;
            str4 = str2;
        } else {
            str4 = (str2 == null || "".equals(str2)) ? "1" : str2;
        }
        this.i = this.j;
        this.j = 1;
        return e.a().a(bVar, bVar2, null, str3, str4, null, 12, null) ? 0 : -1;
    }
}
