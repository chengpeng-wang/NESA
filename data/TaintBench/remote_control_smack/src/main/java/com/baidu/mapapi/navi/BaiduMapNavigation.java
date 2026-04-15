package com.baidu.mapapi.navi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.net.URISyntaxException;

public class BaiduMapNavigation {
    public static void GetLatestBaiduMapApp(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse("http://mo.baidu.com/map/"));
        activity.startActivity(intent);
    }

    private static int a(Context context) {
        String str = "";
        try {
            str = context.getPackageManager().getPackageInfo("com.baidu.BaiduMap", 0).versionName;
            return (str == null || str.length() <= 0) ? 0 : Integer.valueOf(str.trim().replace(".", "").trim()).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private static String a(NaviPara naviPara) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("intent://map/direction?");
        stringBuffer.append("origin=");
        if (naviPara.startName != null) {
            stringBuffer.append("name:" + naviPara.startName + "|");
        }
        stringBuffer.append(String.format("latlng:%f,%f", new Object[]{Double.valueOf(((double) naviPara.startPoint.getLatitudeE6()) * 1.0E-6d), Double.valueOf(((double) naviPara.startPoint.getLongitudeE6()) * 1.0E-6d)}));
        stringBuffer.append("&destination=");
        if (naviPara.endName != null) {
            stringBuffer.append("name:" + naviPara.endName + "|");
        }
        stringBuffer.append(String.format("latlng:%f,%f", new Object[]{Double.valueOf(((double) naviPara.endPoint.getLatitudeE6()) * 1.0E-6d), Double.valueOf(((double) naviPara.endPoint.getLongitudeE6()) * 1.0E-6d)}));
        stringBuffer.append("&coord_type=bd09ll");
        stringBuffer.append("&mode=navigation");
        stringBuffer.append("#Intent;scheme=bdapp;");
        stringBuffer.append("package=com.baidu.BaiduMap;");
        stringBuffer.append("end");
        return stringBuffer.toString();
    }

    public static void openBaiduMapNavi(NaviPara naviPara, Activity activity) {
        if (naviPara.endPoint == null || naviPara.startPoint == null) {
            throw new IllegalNaviArgumentException("start point or end point can not be null.");
        }
        int a = a((Context) activity);
        if (a == 0) {
            throw new BaiduMapAppNotSupportNaviException("BaiduMap app is not installed");
        } else if (a < 500) {
            throw new BaiduMapAppNotSupportNaviException("current baidumap app version not support navigation.");
        } else {
            try {
                activity.startActivity(Intent.parseUri(a(naviPara), 0));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
