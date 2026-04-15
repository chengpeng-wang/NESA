package com.android.Laucher;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.duapps.ad.DuNativeAd;
import com.duapps.ad.base.DuAdNetwork;
import com.nicon.tool.PreferencesUtls;
import com.qq.gdt.utils.Utilstools;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;
import com.umeng.analytics.MobclickAgent;

public class MobulaApplication extends Application {
    public static final int CACHESZIE = 2;
    private static final String FBID = "994677633922059_1070271613029327";
    private static final int PID = 10822;
    private static Context _context = null;
    public static final String keyJson = "{\"native\": [{\"pid\": \"10822\",\"fbids\": [\"994677633922059_1070271613029327\"]}],\"list\": []}";
    private static DuNativeAd nativeAd;

    public void onCreate() {
        super.onCreate();
        _context = this;
        DuAdNetwork.init(this, keyJson);
        Log.i("QQ_Bugly", "初始化Bugly");
        UserStrategy userStrategy = new UserStrategy(this);
        userStrategy.setAppChannel("");
        userStrategy.setAppPackageName("com.newwhale.swipe");
        userStrategy.setAppVersion("");
        CrashReport.initCrashReport(this, "900016527", false, userStrategy);
        if (PreferencesUtls.isFirstOpen(this)) {
            MobclickAgent.onEvent(this, "SSP_New");
            PreferencesUtls.markOpened(this);
            if (Utilstools.isAvilible(this, "com.android.vending")) {
                MobclickAgent.onEvent(this, "GP_install");
            }
        }
    }

    public static DuNativeAd getDuNativeAd() {
        if (nativeAd == null) {
            nativeAd = new DuNativeAd(_context, PID, 2);
        }
        return nativeAd;
    }
}
