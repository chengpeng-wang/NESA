package com.baidu.mapapi.search;

import android.os.Bundle;
import com.baidu.mapapi.cloud.CustomPoiInfo;
import com.baidu.mapapi.cloud.DetailResult;
import com.baidu.mapapi.cloud.GeoSearchResult;
import com.baidu.mapapi.utils.e;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.basestruct.a;
import com.baidu.platform.comjni.tools.JNITools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c {
    private static Bundle a = new Bundle();

    public static String a(ArrayList<MKTransitRoutePlan> arrayList) {
        if (arrayList == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2;
            jSONObject.put("result_type", 14);
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                GeoPoint b;
                int i2;
                int i3;
                GeoPoint geoPoint;
                MKTransitRoutePlan mKTransitRoutePlan = (MKTransitRoutePlan) arrayList.get(i);
                jSONObject2 = new JSONObject();
                jSONObject2.put("uid", "");
                JSONObject jSONObject3 = new JSONObject();
                if (mKTransitRoutePlan.getStart() != null) {
                    b = e.b(mKTransitRoutePlan.getStart());
                    jSONObject3.put(GroupChatInvitation.ELEMENT_NAME, b.getLongitudeE6());
                    jSONObject3.put("y", b.getLatitudeE6());
                    jSONObject2.put("geopt", jSONObject3);
                }
                jSONObject.put("start_point", jSONObject2);
                jSONObject2 = new JSONObject();
                jSONObject2.put("uid", "");
                jSONObject3 = new JSONObject();
                if (mKTransitRoutePlan.getEnd() != null) {
                    b = e.b(mKTransitRoutePlan.getEnd());
                    jSONObject3.put(GroupChatInvitation.ELEMENT_NAME, b.getLongitudeE6());
                    jSONObject3.put("y", b.getLatitudeE6());
                    jSONObject2.put("geopt", jSONObject3);
                }
                jSONObject.put("end_point", jSONObject2);
                JSONArray jSONArray2 = new JSONArray();
                int numLines = mKTransitRoutePlan.getNumLines();
                for (i2 = 0; i2 < numLines; i2++) {
                    GeoPoint b2;
                    JSONObject jSONObject4 = new JSONObject();
                    JSONArray jSONArray3 = new JSONArray();
                    MKLine line = mKTransitRoutePlan.getLine(i2);
                    JSONObject jSONObject5 = new JSONObject();
                    jSONObject5.put("type", 3);
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("type", line.getType());
                    jSONObject2.put("start_uid", line.getGetOnStop().uid);
                    jSONObject2.put("end_uid", line.getGetOffStop().uid);
                    jSONObject5.put("vehicle", jSONObject2);
                    jSONObject5.put("instructions", line.getTip());
                    jSONObject2 = new JSONObject();
                    if (line.getGetOnStop().pt != null) {
                        b2 = e.b(line.getGetOnStop().pt);
                        jSONObject2.put(GroupChatInvitation.ELEMENT_NAME, b2.getLongitudeE6());
                        jSONObject2.put("y", b2.getLatitudeE6());
                        jSONObject5.put("start_location_pt", jSONObject2);
                    }
                    jSONObject2 = new JSONObject();
                    if (line.getGetOffStop().pt != null) {
                        b2 = e.b(line.getGetOffStop().pt);
                        jSONObject2.put(GroupChatInvitation.ELEMENT_NAME, b2.getLongitudeE6());
                        jSONObject2.put("y", b2.getLatitudeE6());
                        jSONObject5.put("end_location_pt", jSONObject2);
                    }
                    ArrayList arrayList2 = line.a;
                    JSONArray jSONArray4 = new JSONArray();
                    for (i3 = 0; i3 < arrayList2.size(); i3++) {
                        JSONObject jSONObject6 = new JSONObject();
                        geoPoint = (GeoPoint) arrayList2.get(i3);
                        if (geoPoint != null) {
                            jSONObject6.put(GroupChatInvitation.ELEMENT_NAME, geoPoint.getLongitudeE6());
                            jSONObject6.put("y", geoPoint.getLatitudeE6());
                            jSONArray4.put(jSONObject6);
                        }
                    }
                    jSONObject5.put("distance", line.getDistance());
                    jSONObject5.put("path_geo_pt", jSONArray4);
                    jSONArray3.put(jSONObject5);
                    jSONObject4.put("busline", jSONArray3);
                    jSONArray2.put(jSONObject4);
                }
                int numRoute = mKTransitRoutePlan.getNumRoute();
                for (numLines = 0; numLines < numRoute; numLines++) {
                    JSONObject jSONObject7 = new JSONObject();
                    JSONArray jSONArray5 = new JSONArray();
                    MKRoute route = mKTransitRoutePlan.getRoute(numLines);
                    JSONObject jSONObject8 = new JSONObject();
                    jSONObject8.put("type", 5);
                    jSONObject2 = new JSONObject();
                    jSONObject8.put("instructions", route.getTip());
                    geoPoint = e.b(route.getStart());
                    jSONObject3 = new JSONObject();
                    jSONObject3.put(GroupChatInvitation.ELEMENT_NAME, geoPoint.getLongitudeE6());
                    jSONObject3.put("y", geoPoint.getLatitudeE6());
                    jSONObject8.put("end_location", jSONObject3);
                    ArrayList arrayList3 = route.a;
                    JSONArray jSONArray6 = new JSONArray();
                    for (i2 = 0; i2 < arrayList3.size(); i2++) {
                        for (i3 = 0; i3 < ((ArrayList) arrayList3.get(i2)).size(); i3++) {
                            JSONObject jSONObject9 = new JSONObject();
                            geoPoint = (GeoPoint) ((ArrayList) arrayList3.get(i2)).get(i3);
                            if (geoPoint != null) {
                                jSONObject9.put(GroupChatInvitation.ELEMENT_NAME, geoPoint.getLongitudeE6());
                                jSONObject9.put("y", geoPoint.getLatitudeE6());
                                jSONArray6.put(jSONObject9);
                            }
                        }
                    }
                    jSONObject8.put("distance", route.getDistance());
                    jSONObject8.put("path_geo_pt", jSONArray6);
                    jSONArray5.put(jSONObject8);
                    jSONObject7.put("busline", jSONArray5);
                    jSONArray2.put(jSONObject7);
                }
                JSONObject jSONObject10 = new JSONObject();
                jSONObject10.put("steps", jSONArray2);
                jSONArray.put(jSONObject10);
            }
            JSONArray jSONArray7 = new JSONArray();
            jSONObject2 = new JSONObject();
            jSONObject2.put("legs", jSONArray);
            jSONArray7.put(jSONObject2);
            jSONObject.put("routes", jSONArray7);
        } catch (JSONException e) {
        }
        return jSONObject.toString();
    }

    public static ArrayList<MKCityListInfo> a(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || str.equals("")) {
            return null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null || optJSONArray.length() <= 0) {
            return null;
        }
        ArrayList<MKCityListInfo> arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < optJSONArray.length()) {
                JSONObject jSONObject2 = (JSONObject) optJSONArray.opt(i2);
                MKCityListInfo mKCityListInfo = new MKCityListInfo();
                mKCityListInfo.num = jSONObject2.optInt("num");
                mKCityListInfo.city = jSONObject2.optString("name");
                arrayList.add(mKCityListInfo);
                i = i2 + 1;
            } else {
                arrayList.trimToSize();
                return arrayList;
            }
        }
    }

    private static ArrayList<MKPoiInfo> a(JSONObject jSONObject, String str, String str2) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        ArrayList<MKPoiInfo> arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= optJSONArray.length()) {
                return arrayList;
            }
            JSONObject jSONObject2 = (JSONObject) optJSONArray.opt(i2);
            MKPoiInfo mKPoiInfo = new MKPoiInfo();
            mKPoiInfo.address = jSONObject2.optString("addr");
            mKPoiInfo.uid = jSONObject2.optString("uid");
            mKPoiInfo.name = jSONObject2.optString("name");
            mKPoiInfo.pt = f(jSONObject2, "geo");
            mKPoiInfo.city = str2;
            arrayList.add(mKPoiInfo);
            i = i2 + 1;
        }
    }

    static void a(String str, ArrayList<ArrayList<GeoPoint>> arrayList, ArrayList<ArrayList<GeoPoint>> arrayList2) {
        a a = com.baidu.platform.comjni.tools.a.a(str);
        if (a != null && a.d != null) {
            ArrayList arrayList3 = a.d;
            for (int i = 0; i < arrayList3.size(); i++) {
                ArrayList arrayList4 = (ArrayList) arrayList3.get(i);
                ArrayList arrayList5 = new ArrayList(arrayList4.size());
                arrayList.add(arrayList5);
                ArrayList arrayList6 = new ArrayList(arrayList4.size());
                arrayList2.add(arrayList6);
                for (int i2 = 0; i2 < arrayList4.size(); i2++) {
                    com.baidu.platform.comapi.basestruct.c cVar = (com.baidu.platform.comapi.basestruct.c) arrayList4.get(i2);
                    arrayList6.add(new GeoPoint(cVar.b / 100, cVar.a / 100));
                    arrayList5.add(e.a(new GeoPoint(cVar.b / 100, cVar.a / 100)));
                }
                arrayList5.trimToSize();
                arrayList6.trimToSize();
            }
            arrayList.trimToSize();
            arrayList2.trimToSize();
        }
    }

    public static boolean a(String str, DetailResult detailResult) {
        if (!(str == null || detailResult == null)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                detailResult.status = jSONObject.optInt("status");
                detailResult.message = jSONObject.optString("message");
                jSONObject = jSONObject.optJSONObject("content");
                if (jSONObject != null) {
                    CustomPoiInfo customPoiInfo = new CustomPoiInfo();
                    detailResult.content = customPoiInfo;
                    customPoiInfo.uid = jSONObject.optInt("uid");
                    customPoiInfo.name = jSONObject.optString("name");
                    customPoiInfo.address = jSONObject.optString("addr");
                    customPoiInfo.telephone = jSONObject.optString("tel");
                    customPoiInfo.postCode = jSONObject.optString("zip");
                    customPoiInfo.provinceId = jSONObject.optInt("province_id");
                    customPoiInfo.cityId = jSONObject.optInt("city_id");
                    customPoiInfo.districtId = jSONObject.optInt("district_id");
                    customPoiInfo.provinceName = jSONObject.optString("province");
                    customPoiInfo.cityName = jSONObject.optString("city");
                    customPoiInfo.districtName = jSONObject.optString("district");
                    customPoiInfo.latitude = (float) jSONObject.optDouble("latitude");
                    customPoiInfo.longitude = (float) jSONObject.optDouble("longitude");
                    customPoiInfo.databoxId = jSONObject.optInt("databox_id");
                    customPoiInfo.tag = jSONObject.optString("tag");
                    JSONObject optJSONObject = jSONObject.optJSONObject("ext");
                    if (optJSONObject != null) {
                        for (int i = 0; i < optJSONObject.length(); i++) {
                            customPoiInfo.poiExtend = new HashMap();
                            Iterator keys = optJSONObject.keys();
                            while (keys.hasNext()) {
                                String str2 = (String) keys.next();
                                customPoiInfo.poiExtend.put(str2, optJSONObject.opt(str2));
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean a(String str, GeoSearchResult geoSearchResult) {
        if (str == null || geoSearchResult == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            geoSearchResult.status = jSONObject.optInt("status");
            geoSearchResult.total = jSONObject.optInt("total");
            geoSearchResult.message = jSONObject.optString("message");
            geoSearchResult.size = jSONObject.optInt("size");
            geoSearchResult.poiList = new ArrayList();
            JSONArray optJSONArray = jSONObject.optJSONArray("content");
            if (optJSONArray != null && optJSONArray.length() > 0) {
                for (int i = 0; i < optJSONArray.length(); i++) {
                    jSONObject = (JSONObject) optJSONArray.opt(i);
                    CustomPoiInfo customPoiInfo = new CustomPoiInfo();
                    geoSearchResult.poiList.add(customPoiInfo);
                    customPoiInfo.uid = jSONObject.optInt("uid");
                    customPoiInfo.name = jSONObject.optString("name");
                    customPoiInfo.address = jSONObject.optString("addr");
                    customPoiInfo.telephone = jSONObject.optString("tel");
                    customPoiInfo.postCode = jSONObject.optString("zip");
                    customPoiInfo.provinceId = jSONObject.optInt("province_id");
                    customPoiInfo.cityId = jSONObject.optInt("city_id");
                    customPoiInfo.districtId = jSONObject.optInt("district_id");
                    customPoiInfo.provinceName = jSONObject.optString("province");
                    customPoiInfo.cityName = jSONObject.optString("city");
                    customPoiInfo.districtName = jSONObject.optString("district");
                    customPoiInfo.latitude = (float) jSONObject.optDouble("latitude");
                    customPoiInfo.longitude = (float) jSONObject.optDouble("longitude");
                    customPoiInfo.databoxId = jSONObject.optInt("databox_id");
                    customPoiInfo.tag = jSONObject.optString("tag");
                    JSONObject optJSONObject = jSONObject.optJSONObject("ext");
                    if (optJSONObject != null) {
                        for (int i2 = 0; i2 < optJSONObject.length(); i2++) {
                            customPoiInfo.poiExtend = new HashMap();
                            Iterator keys = optJSONObject.keys();
                            while (keys.hasNext()) {
                                String str2 = (String) keys.next();
                                customPoiInfo.poiExtend.put(str2, optJSONObject.opt(str2));
                            }
                        }
                    }
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean a(String str, MKAddrInfo mKAddrInfo) {
        if (str == null || "".equals(str) || mKAddrInfo == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            mKAddrInfo.strAddr = jSONObject.optString("address");
            mKAddrInfo.strBusiness = jSONObject.optString("business");
            mKAddrInfo.addressComponents = c(jSONObject, "addr_detail");
            mKAddrInfo.type = 1;
            mKAddrInfo.geoPt = g(jSONObject, "point");
            mKAddrInfo.poiList = b(jSONObject, "surround_poi");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean a(String str, MKBusLineResult mKBusLineResult) {
        if (str != null) {
            try {
                if (!(str.equals("") || mKBusLineResult == null)) {
                    JSONObject jSONObject = new JSONObject(str);
                    int optInt = jSONObject.optInt("count");
                    JSONArray optJSONArray = jSONObject.optJSONArray("details");
                    if (optJSONArray == null || optInt <= 0) {
                        return true;
                    }
                    for (optInt = 0; optInt < optJSONArray.length(); optInt++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(optInt);
                        mKBusLineResult.a(optJSONObject.optString("starttime"));
                        mKBusLineResult.b(optJSONObject.optString("endtime"));
                        mKBusLineResult.a(null, optJSONObject.optString("name"), optJSONObject.optBoolean("ismonticket") ? 1 : 0);
                        mKBusLineResult.getBusRoute().b(new ArrayList());
                        mKBusLineResult.getBusRoute().a = new ArrayList();
                        a(optJSONObject.optString("geo"), mKBusLineResult.getBusRoute().getArrayPoints(), mKBusLineResult.getBusRoute().a);
                        mKBusLineResult.getBusRoute().c(3);
                        JSONArray optJSONArray2 = optJSONObject.optJSONArray("stations");
                        if (optJSONArray2 != null) {
                            MKRoute busRoute = mKBusLineResult.getBusRoute();
                            ArrayList arrayList = new ArrayList();
                            busRoute.a(arrayList);
                            for (int i = 0; i < optJSONArray2.length(); i++) {
                                JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i);
                                if (optJSONObject2 != null) {
                                    MKStep mKStep = new MKStep();
                                    mKStep.a(optJSONObject2.optString("name"));
                                    mKStep.a(f(optJSONObject2, "geo"));
                                    arrayList.add(mKStep);
                                    if (i == 0) {
                                        busRoute.a(new GeoPoint(mKStep.getPoint().getLatitudeE6(), mKStep.getPoint().getLongitudeE6()));
                                    } else if (i == optJSONArray2.length() - 1) {
                                        busRoute.b(new GeoPoint(mKStep.getPoint().getLatitudeE6(), mKStep.getPoint().getLongitudeE6()));
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean a(String str, MKDrivingRouteResult mKDrivingRouteResult) throws JSONException {
        int i = 0;
        if (str == null || "".equals(str) || mKDrivingRouteResult == null) {
            return false;
        }
        JSONObject jSONObject = new JSONObject(str);
        JSONObject optJSONObject = jSONObject.optJSONObject("taxi");
        if (optJSONObject != null) {
            mKDrivingRouteResult.a(optJSONObject.optInt("total_price"));
        }
        mKDrivingRouteResult.a(jSONObject.optInt("avoid_jam") != 0);
        mKDrivingRouteResult.a(d(jSONObject, "start_point"));
        mKDrivingRouteResult.b(d(jSONObject, "end_point"));
        if (jSONObject.optBoolean("have_way_points_endcity")) {
            mKDrivingRouteResult.a(e(jSONObject, "way_points_endcity"));
        }
        GeoPoint geoPoint = mKDrivingRouteResult.getStart() != null ? mKDrivingRouteResult.getStart().pt : null;
        GeoPoint geoPoint2 = mKDrivingRouteResult.getEnd() != null ? mKDrivingRouteResult.getEnd().pt : null;
        String str2 = "驾车方案：" + jSONObject.optJSONObject("start_point").optString("name") + "_" + jSONObject.optJSONObject("end_point").optString("name");
        jSONObject = jSONObject.optJSONObject("routes");
        if (jSONObject != null) {
            jSONObject = jSONObject.optJSONObject("legs");
            if (jSONObject != null) {
                ArrayList arrayList = new ArrayList();
                mKDrivingRouteResult.a(arrayList);
                MKRoutePlan mKRoutePlan = new MKRoutePlan();
                arrayList.add(mKRoutePlan);
                int optInt = jSONObject.optInt("distance");
                mKRoutePlan.a(optInt);
                int optInt2 = jSONObject.optInt("duration");
                mKRoutePlan.b(optInt2);
                ArrayList arrayList2 = new ArrayList();
                mKRoutePlan.a(arrayList2);
                MKRoute mKRoute = new MKRoute();
                arrayList2.add(mKRoute);
                mKRoute.d(optInt2);
                mKRoute.a(str2);
                mKRoute.b(0);
                mKRoute.a(optInt);
                mKRoute.b(new ArrayList());
                mKRoute.a = new ArrayList();
                if (geoPoint != null) {
                    mKRoute.a(new GeoPoint(geoPoint.getLatitudeE6(), geoPoint.getLongitudeE6()));
                }
                if (geoPoint2 != null) {
                    mKRoute.b(new GeoPoint(geoPoint2.getLatitudeE6(), geoPoint2.getLongitudeE6()));
                }
                mKRoute.c(1);
                JSONArray optJSONArray = jSONObject.optJSONArray("steps");
                if (optJSONArray != null) {
                    ArrayList arrayList3 = new ArrayList();
                    mKRoute.a(arrayList3);
                    jSONObject = optJSONArray.optJSONObject(0);
                    MKStep mKStep = new MKStep();
                    mKStep.a(jSONObject.optString("start_desc"));
                    mKStep.a(jSONObject.optInt("direction"));
                    mKStep.b(jSONObject.optString("start_loc"));
                    mKStep.a(f(jSONObject, "start_loc"));
                    arrayList3.add(mKStep);
                    while (i < optJSONArray.length()) {
                        MKStep mKStep2 = new MKStep();
                        JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                        if (optJSONObject2 != null) {
                            mKStep2.a(optJSONObject2.optString("end_desc"));
                            mKStep2.a(optJSONObject2.optInt("direction"));
                            System.currentTimeMillis();
                            mKStep2.a(f(optJSONObject2, "end_loc"));
                            arrayList3.add(mKStep2);
                            mKStep2.b(optJSONObject2.optString("path"));
                        }
                        i++;
                    }
                }
            }
        }
        mKDrivingRouteResult.a(null);
        return true;
    }

    public static boolean a(String str, MKPoiResult mKPoiResult) {
        if (str == null || "".equals(str)) {
            return false;
        }
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
            jSONObject = null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray("citys");
        if (optJSONArray != null && optJSONArray.length() > 0) {
            ArrayList arrayList = new ArrayList();
            if (mKPoiResult != null) {
                mKPoiResult.c(arrayList);
            }
            for (int i = 0; i < optJSONArray.length(); i++) {
                jSONObject = (JSONObject) optJSONArray.opt(i);
                MKCityListInfo mKCityListInfo = new MKCityListInfo();
                mKCityListInfo.num = jSONObject.optInt("num");
                mKCityListInfo.city = jSONObject.optString("name");
                arrayList.add(mKCityListInfo);
            }
            arrayList.trimToSize();
        }
        return true;
    }

    public static boolean a(String str, MKPoiResult mKPoiResult, int i) {
        if (str != null) {
            try {
                if (!(str.equals("") || mKPoiResult == null)) {
                    JSONObject jSONObject = new JSONObject(str);
                    int optInt = jSONObject.optInt("total");
                    int optInt2 = jSONObject.optInt("count");
                    mKPoiResult.b(optInt);
                    mKPoiResult.a(optInt2);
                    mKPoiResult.d(i);
                    mKPoiResult.c((optInt % optInt2 > 0 ? 1 : 0) + (optInt / optInt2));
                    JSONObject optJSONObject = jSONObject.optJSONObject("current_city");
                    String optString = optJSONObject != null ? optJSONObject.optString("name") : null;
                    JSONArray optJSONArray = jSONObject.optJSONArray("pois");
                    if (optJSONArray == null) {
                        return true;
                    }
                    ArrayList arrayList = new ArrayList();
                    mKPoiResult.a(arrayList);
                    for (optInt = 0; optInt < optJSONArray.length(); optInt++) {
                        JSONObject optJSONObject2 = optJSONArray.optJSONObject(optInt);
                        MKPoiInfo mKPoiInfo = new MKPoiInfo();
                        mKPoiInfo.name = optJSONObject2.optString("name");
                        mKPoiInfo.address = optJSONObject2.optString("addr");
                        mKPoiInfo.uid = optJSONObject2.optString("uid");
                        mKPoiInfo.phoneNum = optJSONObject2.optString("tel");
                        mKPoiInfo.ePoiType = optJSONObject2.optInt("type");
                        if (!(mKPoiInfo.ePoiType == 2 || mKPoiInfo.ePoiType == 4)) {
                            mKPoiInfo.pt = f(optJSONObject2, "geo");
                        }
                        mKPoiInfo.city = optString;
                        JSONObject optJSONObject3 = optJSONObject2.optJSONObject("place");
                        if (optJSONObject3 != null) {
                            if ("cater".equals(optJSONObject3.optString("src_name")) && optJSONObject2.optBoolean("detail")) {
                                mKPoiInfo.hasCaterDetails = true;
                            }
                        }
                        arrayList.add(mKPoiInfo);
                    }
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean a(String str, MKRouteAddrResult mKRouteAddrResult) {
        if (str == null || "".equals(str) || mKRouteAddrResult == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONObject optJSONObject = jSONObject.optJSONObject("address_info");
            if (optJSONObject == null) {
                return false;
            }
            String optString = optJSONObject.optString("st_cityname");
            String optString2 = optJSONObject.optString("en_cityname");
            if (optJSONObject.optBoolean("have_stcitylist")) {
                mKRouteAddrResult.mStartCityList = a(jSONObject, "startcitys");
            } else {
                mKRouteAddrResult.mStartPoiList = a(jSONObject, "startpoints", optString);
            }
            if (optJSONObject.optBoolean("have_encitylist")) {
                mKRouteAddrResult.mEndCityList = a(jSONObject, "endcitys");
            } else {
                mKRouteAddrResult.mEndPoiList = a(jSONObject, "endpoints", optString2);
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("way_points_citylist");
            if (optJSONArray == null || optJSONArray.length() <= 0) {
                return true;
            }
            mKRouteAddrResult.mWpCityList = new ArrayList();
            mKRouteAddrResult.mWpPoiList = new ArrayList();
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                if (jSONObject2.optBoolean("have_citylist")) {
                    mKRouteAddrResult.mWpCityList.add(a(jSONObject2, "way_points_item"));
                } else {
                    mKRouteAddrResult.mWpCityList.add(null);
                }
                if (jSONObject2.optBoolean("have_poilist")) {
                    mKRouteAddrResult.mWpPoiList.add(a(jSONObject2, "way_points_poilist", ""));
                } else {
                    mKRouteAddrResult.mWpPoiList.add(null);
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean a(String str, MKShareUrlResult mKShareUrlResult) {
        if (str == null || mKShareUrlResult == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject == null) {
                return false;
            }
            mKShareUrlResult.url = jSONObject.optString("url");
            mKShareUrlResult.type = jSONObject.optInt("type");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean a(String str, MKSuggestionResult mKSuggestionResult) {
        if (str == null) {
            return false;
        }
        try {
            if (str.equals("") || mKSuggestionResult == null) {
                return false;
            }
            JSONObject jSONObject = new JSONObject(str);
            JSONArray optJSONArray = jSONObject.optJSONArray("cityname");
            JSONArray optJSONArray2 = jSONObject.optJSONArray("poiname");
            JSONArray optJSONArray3 = jSONObject.optJSONArray("districtname");
            if (optJSONArray2 != null && optJSONArray2.length() > 0) {
                ArrayList arrayList = new ArrayList();
                mKSuggestionResult.a(arrayList);
                int length = optJSONArray2.length();
                for (int i = 0; i < length; i++) {
                    MKSuggestionInfo mKSuggestionInfo = new MKSuggestionInfo();
                    mKSuggestionInfo.city = optJSONArray.optString(i);
                    mKSuggestionInfo.key = optJSONArray2.optString(i);
                    mKSuggestionInfo.district = optJSONArray3.optString(i);
                    arrayList.add(mKSuggestionInfo);
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean a(String str, MKTransitRouteResult mKTransitRouteResult) throws JSONException {
        if (str == null || "".equals(str) || mKTransitRouteResult == null) {
            return false;
        }
        JSONObject jSONObject = new JSONObject(str);
        JSONObject optJSONObject = jSONObject.optJSONObject("taxi");
        if (optJSONObject != null) {
            mKTransitRouteResult.a(optJSONObject.optInt("total_price"));
        }
        mKTransitRouteResult.a(d(jSONObject, "start_point"));
        mKTransitRouteResult.b(d(jSONObject, "end_point"));
        JSONArray optJSONArray = jSONObject.optJSONArray("routes");
        if (optJSONArray != null && optJSONArray.length() > 0) {
            ArrayList arrayList = new ArrayList();
            mKTransitRouteResult.a(arrayList);
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= optJSONArray.length()) {
                    break;
                }
                jSONObject = ((JSONObject) optJSONArray.opt(i2)).optJSONObject("legs");
                if (jSONObject != null) {
                    MKTransitRoutePlan mKTransitRoutePlan = new MKTransitRoutePlan();
                    arrayList.add(mKTransitRoutePlan);
                    mKTransitRoutePlan.a(jSONObject.optInt("distance"));
                    mKTransitRoutePlan.a(f(jSONObject, "start_geo"));
                    mKTransitRoutePlan.b(f(jSONObject, "end_geo"));
                    mKTransitRoutePlan.b(jSONObject.optInt("time"));
                    JSONArray optJSONArray2 = jSONObject.optJSONArray("steps");
                    if (optJSONArray2 != null && optJSONArray2.length() > 0) {
                        ArrayList arrayList2 = new ArrayList();
                        ArrayList arrayList3 = new ArrayList();
                        mKTransitRoutePlan.a(arrayList2);
                        mKTransitRoutePlan.setLine(arrayList3);
                        String str2 = "";
                        for (int i3 = 0; i3 < optJSONArray2.length(); i3++) {
                            JSONArray optJSONArray3 = optJSONArray2.optJSONObject(i3).optJSONArray("busline");
                            if (optJSONArray3 != null && optJSONArray3.length() > 0) {
                                String str3 = null;
                                JSONObject optJSONObject2 = optJSONArray3.optJSONObject(0);
                                if (optJSONObject2 != null) {
                                    int i4 = optJSONObject2.optInt("type") == 5 ? 2 : 3;
                                    GeoPoint f = f(optJSONObject2, "start_location");
                                    GeoPoint f2 = f(optJSONObject2, "end_location");
                                    if (null == null) {
                                        str3 = optJSONObject2.optString("instructions");
                                    }
                                    if (i4 == 2) {
                                        MKRoute mKRoute = new MKRoute();
                                        mKRoute.c(i4);
                                        mKRoute.a(optJSONObject2.optInt("distance"));
                                        mKRoute.d(optJSONObject2.optInt("duration"));
                                        mKRoute.a(f);
                                        mKRoute.b(f2);
                                        mKRoute.b(arrayList3.size() - 1);
                                        mKRoute.a(str3);
                                        mKRoute.b(new ArrayList());
                                        mKRoute.a = new ArrayList();
                                        a(optJSONObject2.optString("path_geo"), mKRoute.getArrayPoints(), mKRoute.a);
                                        arrayList2.add(mKRoute);
                                    } else {
                                        MKLine mKLine = new MKLine();
                                        mKLine.b(optJSONObject2.optInt("distance"));
                                        mKLine.c(optJSONObject2.optInt("duration"));
                                        MKPoiInfo mKPoiInfo = new MKPoiInfo();
                                        mKPoiInfo.pt = f;
                                        mKLine.a(mKPoiInfo);
                                        MKPoiInfo mKPoiInfo2 = new MKPoiInfo();
                                        mKPoiInfo2.pt = f2;
                                        mKLine.b(mKPoiInfo2);
                                        mKLine.a(new ArrayList());
                                        mKLine.a = new ArrayList();
                                        b(optJSONObject2.optString("path_geo"), mKLine.getPoints(), mKLine.a);
                                        optJSONObject2 = optJSONObject2.optJSONObject("vehicle");
                                        if (optJSONObject2 != null) {
                                            mKLine.d(optJSONObject2.optInt("type"));
                                            mKPoiInfo.name = optJSONObject2.optString("start_name");
                                            mKPoiInfo.uid = optJSONObject2.optString("start_uid");
                                            mKPoiInfo2.name = optJSONObject2.optString("end_name");
                                            mKPoiInfo2.uid = optJSONObject2.optString("end_uid");
                                            mKLine.a(optJSONObject2.optInt("stop_num"));
                                            mKLine.d(optJSONObject2.optInt("type"));
                                            mKLine.b(optJSONObject2.optString("name"));
                                            if (!str2.equals("")) {
                                                str2 = str2 + "_";
                                            }
                                            str2 = str2 + mKLine.getTitle();
                                        }
                                        mKLine.a(str3);
                                        arrayList3.add(mKLine);
                                    }
                                }
                            }
                        }
                        mKTransitRoutePlan.a(str2);
                    }
                }
                i = i2 + 1;
            }
        }
        mKTransitRouteResult.a(null);
        return true;
    }

    public static boolean a(String str, MKWalkingRouteResult mKWalkingRouteResult) throws JSONException {
        if (str == null || "".equals(str) || mKWalkingRouteResult == null) {
            return false;
        }
        JSONObject jSONObject = new JSONObject(str);
        JSONObject optJSONObject = jSONObject.optJSONObject("taxi");
        if (optJSONObject != null) {
            mKWalkingRouteResult.a(optJSONObject.optInt("total_price"));
        }
        mKWalkingRouteResult.a(d(jSONObject, "start_point"));
        mKWalkingRouteResult.b(d(jSONObject, "end_point"));
        GeoPoint geoPoint = mKWalkingRouteResult.getStart() != null ? mKWalkingRouteResult.getStart().pt : null;
        GeoPoint geoPoint2 = mKWalkingRouteResult.getEnd() != null ? mKWalkingRouteResult.getEnd().pt : null;
        String str2 = "步行方案：" + jSONObject.optJSONObject("start_point").optString("name") + "_" + jSONObject.optJSONObject("end_point").optString("name");
        jSONObject = jSONObject.optJSONObject("routes");
        if (jSONObject != null) {
            jSONObject = jSONObject.optJSONObject("legs");
            if (jSONObject != null) {
                ArrayList arrayList = new ArrayList();
                mKWalkingRouteResult.a(arrayList);
                MKRoutePlan mKRoutePlan = new MKRoutePlan();
                arrayList.add(mKRoutePlan);
                int optInt = jSONObject.optInt("distance");
                mKRoutePlan.a(optInt);
                int optInt2 = jSONObject.optInt("duration");
                mKRoutePlan.b(optInt2);
                ArrayList arrayList2 = new ArrayList();
                mKRoutePlan.a(arrayList2);
                MKRoute mKRoute = new MKRoute();
                arrayList2.add(mKRoute);
                mKRoute.d(optInt2);
                mKRoute.a(str2);
                mKRoute.b(0);
                mKRoute.a(optInt);
                if (geoPoint != null) {
                    mKRoute.a(new GeoPoint(geoPoint.getLatitudeE6(), geoPoint.getLongitudeE6()));
                }
                if (geoPoint2 != null) {
                    mKRoute.b(new GeoPoint(geoPoint2.getLatitudeE6(), geoPoint2.getLongitudeE6()));
                }
                mKRoute.c(2);
                mKRoute.b(new ArrayList());
                mKRoute.a = new ArrayList();
                JSONArray optJSONArray = jSONObject.optJSONArray("steps");
                if (optJSONArray != null) {
                    ArrayList arrayList3 = new ArrayList();
                    mKRoute.a(arrayList3);
                    optJSONObject = optJSONArray.optJSONObject(0);
                    MKStep mKStep = new MKStep();
                    mKStep.a(optJSONObject.optString("start_desc"));
                    mKStep.a(optJSONObject.optInt("direction"));
                    mKStep.a(f(optJSONObject, "start_loc"));
                    arrayList3.add(mKStep);
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        MKStep mKStep2 = new MKStep();
                        JSONObject optJSONObject2 = optJSONArray.optJSONObject(i);
                        if (optJSONObject2 != null) {
                            mKStep2.a(optJSONObject2.optString("end_desc"));
                            mKStep2.a(optJSONObject2.optInt("direction"));
                            mKStep2.a(f(optJSONObject2, "end_loc"));
                            arrayList3.add(mKStep2);
                            a(optJSONObject2.optString("path"), mKRoute.getArrayPoints(), mKRoute.a);
                        }
                    }
                }
            }
        }
        mKWalkingRouteResult.a(null);
        return true;
    }

    public static boolean a(String str, e eVar) {
        if (str == null || eVar == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject != null) {
                jSONObject = jSONObject.optJSONObject("content");
                if (jSONObject == null) {
                    return false;
                }
                eVar.d = jSONObject.optString("uid");
                eVar.b = jSONObject.optString("addr");
                eVar.a = jSONObject.optString("name");
                JSONObject optJSONObject = jSONObject.optJSONObject("ext");
                if (optJSONObject != null) {
                    int i;
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject("detail_info");
                    if (optJSONObject2 != null) {
                        eVar.i = optJSONObject2.optString("environment_rating");
                        eVar.e = optJSONObject2.optString("image");
                        JSONArray optJSONArray = optJSONObject2.optJSONArray("link");
                        if (optJSONArray != null && optJSONArray.length() > 0) {
                            eVar.o = new ArrayList();
                            for (i = 0; i < optJSONArray.length(); i++) {
                                jSONObject = (JSONObject) optJSONArray.opt(i);
                                if (!"dianping".equals(jSONObject.optString("name"))) {
                                    d dVar = new d();
                                    dVar.b = jSONObject.optString("cn_name");
                                    dVar.c = jSONObject.optString("url");
                                    dVar.d = jSONObject.optString("name");
                                    eVar.o.add(dVar);
                                }
                            }
                        }
                        eVar.f = optJSONObject2.optString("overall_rating");
                        eVar.c = optJSONObject2.optString("phone");
                        eVar.g = optJSONObject2.optString("price");
                        eVar.j = optJSONObject2.optString("service_rating");
                        eVar.h = optJSONObject2.optString("taste_rating");
                    }
                    jSONObject = optJSONObject.optJSONObject("rich_info");
                    if (jSONObject != null) {
                        eVar.k = jSONObject.optString("description");
                        eVar.l = jSONObject.optString("recommendation");
                    }
                    JSONArray optJSONArray2 = optJSONObject.optJSONArray("review");
                    if (optJSONArray2 != null && optJSONArray2.length() > 0) {
                        loop1:
                        for (i = 0; i < optJSONArray2.length(); i++) {
                            jSONObject = (JSONObject) optJSONArray2.opt(i);
                            if (!"dianping.com".equals(jSONObject.optString(PrivacyRule.SUBSCRIPTION_FROM))) {
                                JSONArray optJSONArray3 = jSONObject.optJSONArray("info");
                                if (optJSONArray3 != null && optJSONArray3.length() > 0) {
                                    for (int i2 = 0; i2 < optJSONArray3.length(); i2++) {
                                        JSONObject optJSONObject3 = optJSONArray3.optJSONObject(i2);
                                        if (optJSONObject3 != null) {
                                            eVar.m = optJSONObject3.optString("content");
                                            break loop1;
                                        }
                                    }
                                    continue;
                                }
                            }
                        }
                    }
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String b(ArrayList<MKRoute> arrayList) {
        if (arrayList == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("result_type", 20);
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                GeoPoint b;
                MKRoute mKRoute = (MKRoute) arrayList.get(i);
                if (mKRoute.getRouteType() == 3) {
                    jSONObject.put("result_buslinedetail", true);
                }
                JSONObject jSONObject2 = new JSONObject();
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("uid", "");
                JSONObject jSONObject4 = new JSONObject();
                if (mKRoute.getStart() != null) {
                    b = e.b(mKRoute.getStart());
                    jSONObject4.put(GroupChatInvitation.ELEMENT_NAME, b.getLongitudeE6());
                    jSONObject4.put("y", b.getLatitudeE6());
                    jSONObject3.put("geopt", jSONObject4);
                }
                jSONObject2.put("start_point", jSONObject3);
                jSONObject3 = new JSONObject();
                jSONObject3.put("uid", "");
                jSONObject4 = new JSONObject();
                if (mKRoute.getEnd() != null) {
                    b = e.b(mKRoute.getEnd());
                    jSONObject4.put(GroupChatInvitation.ELEMENT_NAME, b.getLongitudeE6());
                    jSONObject4.put("y", b.getLatitudeE6());
                    jSONObject3.put("geopt", jSONObject4);
                }
                jSONObject2.put("end_point", jSONObject3);
                int numSteps = mKRoute.getNumSteps();
                ArrayList arrayList2 = mKRoute.a;
                JSONArray jSONArray2 = new JSONArray();
                for (int i2 = 0; i2 < numSteps; i2++) {
                    GeoPoint b2;
                    JSONObject jSONObject5 = new JSONObject();
                    MKStep step = mKRoute.getStep(i2);
                    jSONObject5.put("direction", step.a());
                    if (step.getPoint() != null) {
                        b2 = e.b(step.getPoint());
                        JSONObject jSONObject6 = new JSONObject();
                        jSONObject6.put(GroupChatInvitation.ELEMENT_NAME, b2.getLongitudeE6());
                        jSONObject6.put("y", b2.getLatitudeE6());
                        jSONObject5.put("end_loc_pt", jSONObject6);
                    }
                    jSONObject5.put("end_desc", step.getContent());
                    ArrayList arrayList3;
                    JSONArray jSONArray3;
                    int i3;
                    JSONObject jSONObject7;
                    if (mKRoute.getRouteType() == 3) {
                        if (arrayList2.size() > i2) {
                            arrayList3 = (ArrayList) arrayList2.get(i2);
                            jSONArray3 = new JSONArray();
                            for (i3 = 0; i3 < arrayList3.size(); i3++) {
                                jSONObject7 = new JSONObject();
                                b2 = (GeoPoint) arrayList3.get(i3);
                                if (b2 != null) {
                                    jSONObject7.put(GroupChatInvitation.ELEMENT_NAME, b2.getLongitudeE6());
                                    jSONObject7.put("y", b2.getLatitudeE6());
                                    jSONArray3.put(jSONObject7);
                                }
                            }
                            jSONObject5.put("pathPt", jSONArray3);
                        }
                    } else if (mKRoute.getRouteType() == 1) {
                        if (i2 < numSteps) {
                            jSONObject5.put("path", step.b());
                        }
                    } else if (i2 < numSteps - 1) {
                        arrayList3 = (ArrayList) arrayList2.get(i2);
                        jSONArray3 = new JSONArray();
                        for (i3 = 0; i3 < arrayList3.size(); i3++) {
                            jSONObject7 = new JSONObject();
                            b2 = (GeoPoint) arrayList3.get(i3);
                            if (b2 != null) {
                                jSONObject7.put(GroupChatInvitation.ELEMENT_NAME, b2.getLongitudeE6());
                                jSONObject7.put("y", b2.getLatitudeE6());
                                jSONArray3.put(jSONObject7);
                            }
                        }
                        jSONObject5.put("pathPt", jSONArray3);
                    }
                    jSONArray2.put(jSONObject5);
                }
                jSONObject2.put("steps", jSONArray2);
                jSONArray.put(jSONObject2);
            }
            JSONObject jSONObject8 = new JSONObject();
            jSONObject8.put("legs", jSONArray);
            jSONObject.put("routes", jSONObject8);
        } catch (JSONException e) {
        }
        return jSONObject.toString();
    }

    private static ArrayList<MKPoiInfo> b(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        ArrayList<MKPoiInfo> arrayList = new ArrayList();
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
            MKPoiInfo mKPoiInfo = new MKPoiInfo();
            mKPoiInfo.address = optJSONObject.optString("addr");
            mKPoiInfo.phoneNum = optJSONObject.optString("tel");
            mKPoiInfo.uid = optJSONObject.optString("uid");
            mKPoiInfo.postCode = optJSONObject.optString("zip");
            mKPoiInfo.name = optJSONObject.optString("name");
            mKPoiInfo.pt = g(optJSONObject, "point");
            arrayList.add(mKPoiInfo);
        }
        return arrayList;
    }

    private static void b(String str, ArrayList<GeoPoint> arrayList, ArrayList<GeoPoint> arrayList2) {
        int i = 0;
        a a = com.baidu.platform.comjni.tools.a.a(str);
        if (a != null && a.d != null) {
            ArrayList arrayList3 = a.d;
            if (arrayList3.size() > 0) {
                arrayList3 = (ArrayList) arrayList3.get(0);
                while (true) {
                    int i2 = i;
                    if (i2 >= arrayList3.size()) {
                        break;
                    }
                    com.baidu.platform.comapi.basestruct.c cVar = (com.baidu.platform.comapi.basestruct.c) arrayList3.get(i2);
                    arrayList2.add(new GeoPoint(cVar.b / 100, cVar.a / 100));
                    arrayList.add(e.a(new GeoPoint(cVar.b / 100, cVar.a / 100)));
                    i = i2 + 1;
                }
            }
            arrayList.trimToSize();
            arrayList2.trimToSize();
        }
    }

    public static boolean b(String str, MKAddrInfo mKAddrInfo) {
        if (str == null || "".equals(str) || mKAddrInfo == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject == null || jSONObject.optInt("error") != 0) {
                return false;
            }
            mKAddrInfo.type = 0;
            mKAddrInfo.geoPt = e.a(new GeoPoint(jSONObject.optInt("y"), jSONObject.optInt(GroupChatInvitation.ELEMENT_NAME)));
            mKAddrInfo.strAddr = jSONObject.optString("addr");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean b(String str, MKPoiResult mKPoiResult, int i) {
        ArrayList arrayList = new ArrayList();
        mKPoiResult.b(arrayList);
        try {
            JSONObject jSONObject = new JSONObject(str);
            JSONArray optJSONArray = jSONObject.optJSONArray("content");
            JSONArray optJSONArray2 = jSONObject.optJSONArray(Form.TYPE_RESULT);
            if (optJSONArray == null || optJSONArray.length() <= 0 || optJSONArray2 == null || optJSONArray2.length() <= 0) {
                return true;
            }
            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                int i3;
                MKPoiResult mKPoiResult2 = new MKPoiResult();
                arrayList.add(mKPoiResult2);
                JSONArray optJSONArray3 = optJSONArray.optJSONObject(i2).optJSONArray("cont");
                if (optJSONArray3 != null && optJSONArray3.length() > 0) {
                    ArrayList arrayList2 = new ArrayList();
                    for (i3 = 0; i3 < optJSONArray3.length(); i3++) {
                        JSONObject optJSONObject = optJSONArray3.optJSONObject(i3);
                        MKPoiInfo mKPoiInfo = new MKPoiInfo();
                        mKPoiInfo.name = optJSONObject.optString("name");
                        mKPoiInfo.address = optJSONObject.optString("addr");
                        mKPoiInfo.pt = f(optJSONObject, "geo");
                        mKPoiInfo.uid = optJSONObject.optString("uid");
                        mKPoiInfo.phoneNum = optJSONObject.optString("tel");
                        mKPoiInfo.ePoiType = optJSONObject.optInt("type");
                        arrayList2.add(mKPoiInfo);
                    }
                    mKPoiResult2.a(arrayList2);
                }
                jSONObject = optJSONArray2.optJSONObject(i2);
                int optInt = jSONObject.optInt("total");
                mKPoiResult2.b(optInt);
                mKPoiResult2.d(jSONObject.optInt("page_num"));
                i3 = jSONObject.optInt("count");
                mKPoiResult2.a(i3);
                mKPoiResult2.d(i);
                mKPoiResult2.c((optInt % i3 > 0 ? 1 : 0) + (optInt / i3));
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static MKGeocoderAddressComponent c(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        MKGeocoderAddressComponent mKGeocoderAddressComponent = new MKGeocoderAddressComponent();
        mKGeocoderAddressComponent.city = optJSONObject.optString("city");
        mKGeocoderAddressComponent.district = optJSONObject.optString("district");
        mKGeocoderAddressComponent.province = optJSONObject.optString("province");
        mKGeocoderAddressComponent.street = optJSONObject.optString("street");
        mKGeocoderAddressComponent.streetNumber = optJSONObject.optString("street_number");
        return mKGeocoderAddressComponent;
    }

    public static String c(ArrayList<MKPoiInfo> arrayList) {
        if (arrayList == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("result_type", 11);
            JSONArray jSONArray = new JSONArray();
            for (int i = 0; i < arrayList.size(); i++) {
                MKPoiInfo mKPoiInfo = (MKPoiInfo) arrayList.get(i);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("uid", mKPoiInfo.uid);
                jSONObject2.put("type", mKPoiInfo.ePoiType);
                jSONObject2.put("name", mKPoiInfo.name);
                JSONObject jSONObject3 = new JSONObject();
                if (mKPoiInfo.pt != null) {
                    GeoPoint b = e.b(mKPoiInfo.pt);
                    jSONObject3.put(GroupChatInvitation.ELEMENT_NAME, b.getLongitudeE6());
                    jSONObject3.put("y", b.getLatitudeE6());
                    jSONObject2.put("geopt", jSONObject3);
                }
                jSONArray.put(jSONObject2);
            }
            if (arrayList.size() > 0) {
                jSONObject.put("pois", jSONArray);
            }
        } catch (JSONException e) {
        }
        return jSONObject.toString();
    }

    private static MKPlanNode d(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        MKPlanNode mKPlanNode = new MKPlanNode();
        mKPlanNode.name = optJSONObject.optString("name");
        mKPlanNode.pt = f(optJSONObject, "geo");
        return mKPlanNode;
    }

    private static List<MKWpNode> e(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < optJSONArray.length(); i++) {
            MKWpNode mKWpNode = new MKWpNode();
            try {
                mKWpNode.name = optJSONArray.getJSONObject(i).optString("name");
                if (optJSONArray.getJSONObject(i).has("bus_stop")) {
                    mKWpNode.a = optJSONArray.getJSONObject(i).optBoolean("bus_stop");
                }
                mKWpNode.pt = f(optJSONArray.getJSONObject(i), "geo");
                arrayList.add(mKWpNode);
            } catch (JSONException e) {
            }
        }
        return arrayList;
    }

    private static GeoPoint f(JSONObject jSONObject, String str) {
        if (jSONObject == null || jSONObject == null || str == null) {
            return null;
        }
        String optString = jSONObject.optString(str);
        a.clear();
        a.putString("strkey", optString);
        JNITools.TransGeoStr2Pt(a);
        GeoPoint geoPoint = new GeoPoint(0, 0);
        geoPoint.setLongitudeE6(a.getInt("ptx"));
        geoPoint.setLatitudeE6(a.getInt("pty"));
        return e.a(geoPoint);
    }

    private static GeoPoint g(JSONObject jSONObject, String str) {
        if (jSONObject == null || str == null || "".equals(str)) {
            return null;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        if (optJSONObject == null) {
            return null;
        }
        return e.a(new GeoPoint(optJSONObject.optInt("y"), optJSONObject.optInt(GroupChatInvitation.ELEMENT_NAME)));
    }
}
