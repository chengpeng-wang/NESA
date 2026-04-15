package com.cmcm.adsdk;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import com.cmcm.adsdk.requestconfig.RequestConfig;
import com.cmcm.adsdk.requestconfig.log.a;
import com.cmcm.baseapi.IEventReport;
import com.picksinit.PicksConfig;
import com.picksinit.c;
import java.util.HashMap;
import java.util.Map;

public abstract class CMAdManager {
    private static final int PICKS_MAX_PROTECT_TIME = 2000;
    private static final int PICKS_MIN_PROTECT_TIME = 0;
    protected static RequestConfig config = null;
    private static boolean isChinaVersion = false;
    private static boolean isInner = false;
    private static Context mContext;
    private static String mMid;
    private static Map<String, Long> sCacheTimeMap;
    private static String sChannelId;
    private static IEventReport sEventReport;
    private static boolean sIsDebug = false;
    private static boolean sIsWork = true;
    private static int sPicksProtectTime = 0;

    public static void setIsInner() {
        isInner = true;
    }

    public static void applicationInit(Context context, String mid, String channelId) {
        if (TextUtils.isEmpty(mid)) {
            throw new IllegalArgumentException("PublisherID cannot be null or empty");
        }
        mContext = context;
        mMid = mid;
        sChannelId = channelId;
        c.getInstance().setmAdResourceRp(isInner ? 1 : 2);
        c.getInstance().setmCver(getAppVersionCode());
        isChinaVersion = false;
        c.getInstance().init(context, mid, channelId, isChinaVersion);
        c.getInstance().initUserAgent();
        c.getInstance().setSSPId(2);
        RequestConfig a = RequestConfig.a();
        config = a;
        a.a(context, mid);
        config.a(false);
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isChinaVersion() {
        return isChinaVersion;
    }

    public static void stopWork() {
        a.a(Const.TAG, "set cmadsdk stop work");
        sIsWork = false;
        com.cmcm.adsdk.requestconfig.a.a(mContext).a();
    }

    public static void startWork() {
        a.a(Const.TAG, "set cmadsdk start work");
        sIsWork = true;
        com.cmcm.adsdk.requestconfig.a.a(mContext).a(mMid);
    }

    public static synchronized boolean isSDKWork() {
        boolean z;
        synchronized (CMAdManager.class) {
            z = sIsWork;
        }
        return z;
    }

    public static String getMid() {
        return mMid;
    }

    public static int getAppVersionCode() {
        int i = 0;
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return i;
        }
    }

    public static void setAdTypeCacheTime(String adTypeName, long cacheTime) {
        if (sCacheTimeMap == null) {
            sCacheTimeMap = new HashMap();
        }
        sCacheTimeMap.put(adTypeName, Long.valueOf(cacheTime));
    }

    public static long getAdTypeCacheTime(String adTypeName) {
        if (sCacheTimeMap == null || sCacheTimeMap.get(adTypeName) == null) {
            return 0;
        }
        return ((Long) sCacheTimeMap.get(adTypeName)).longValue();
    }

    public static void setAdConfig(Map<String, Object> items) {
        if (items != null) {
            c.getInstance().updateConfig(items);
            PicksConfig config = c.getInstance().getConfig();
            config.enable_webview = false;
            if (config.enable_debug) {
                setDebug();
            }
        }
    }

    public static String getChannelId() {
        return sChannelId;
    }

    public static void setPicksProtectTime(int time) {
        if (time <= 0) {
            sPicksProtectTime = 0;
        } else if (time >= 2000) {
            sPicksProtectTime = 2000;
        } else {
            sPicksProtectTime = time;
        }
    }

    public static int getPicksProtectTime() {
        return sPicksProtectTime;
    }

    public static void setDebug() {
        sIsDebug = true;
        a.a = true;
    }

    public static boolean isDebug() {
        return sIsDebug;
    }

    public static void setDefaultConfig(String strConfig, boolean force) {
        RequestConfig.a().a(strConfig, force);
    }

    public static void setReportProxy(IEventReport eventReport) {
        sEventReport = eventReport;
    }

    public static IEventReport getReportProxy() {
        return sEventReport;
    }
}
