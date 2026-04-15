package com.tencent.bugly.crashreport;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.common.strategy.BuglyBroadcastRecevier;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.common.strategy.c;
import com.tencent.bugly.proguard.aa;
import com.tencent.bugly.proguard.ab;
import com.tencent.bugly.proguard.ac;
import com.tencent.bugly.proguard.ae;
import com.tencent.bugly.proguard.ag;
import com.tencent.bugly.proguard.aj;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/* compiled from: BUGLY */
public class CrashReport {
    private static Context a;
    private static String b = null;
    public static boolean isDebug;

    /* compiled from: BUGLY */
    public static class CrashHandleCallback {
        public static final int CRASHTYPE_ANR = 4;
        public static final int CRASHTYPE_COCOS2DX_JS = 5;
        public static final int CRASHTYPE_COCOS2DX_LUA = 6;
        public static final int CRASHTYPE_JAVA_CATCH = 1;
        public static final int CRASHTYPE_JAVA_CRASH = 0;
        public static final int CRASHTYPE_NATIVE = 2;
        public static final int CRASHTYPE_U3D = 3;
        public static final int MAX_USERDATA_KEY_LENGTH = 100;
        public static final int MAX_USERDATA_VALUE_LENGTH = 30000;

        public synchronized Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
            return null;
        }

        public synchronized byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType, String errorMessage, String errorStack) {
            return null;
        }
    }

    /* compiled from: BUGLY */
    public static class UserStrategy {
        /* access modifiers changed from: private */
        public String a;
        /* access modifiers changed from: private */
        public String b;
        /* access modifiers changed from: private */
        public String c;
        /* access modifiers changed from: private */
        public long d;
        /* access modifiers changed from: private */
        public String e;
        /* access modifiers changed from: private */
        public String f;
        /* access modifiers changed from: private */
        public Map<String, String> g;
        private CrashHandleCallback h;
        private boolean i = true;
        private boolean j = true;
        /* access modifiers changed from: private */
        public boolean k = true;

        public UserStrategy(Context context) {
            a a = a.a(ag.a(context));
            this.a = a.e();
            this.b = a.B();
            this.c = a.f();
            this.e = null;
            this.d = 0;
            this.g = null;
        }

        public synchronized void setBuglyLogUpload(boolean isBuglyLogUpload) {
            this.k = isBuglyLogUpload;
        }

        public synchronized boolean isBuglyLogUpload() {
            return this.k;
        }

        public synchronized String getAppVersion() {
            return this.a;
        }

        public synchronized UserStrategy setAppVersion(String appVersion) {
            this.a = appVersion;
            return this;
        }

        public synchronized String getAppChannel() {
            return this.b;
        }

        public synchronized UserStrategy setAppChannel(String appChannel) {
            this.b = appChannel;
            return this;
        }

        public synchronized String getAppPackageName() {
            return this.c;
        }

        public synchronized UserStrategy setAppPackageName(String appPackageName) {
            this.c = appPackageName;
            return this;
        }

        public synchronized long getAppReportDelay() {
            return this.d;
        }

        public synchronized UserStrategy setAppReportDelay(long appReportDelay) {
            this.d = appReportDelay;
            return this;
        }

        public synchronized String getLibBuglySOFilePath() {
            return this.e;
        }

        public synchronized void setLibBuglySOFilePath(String customerBuglySOFilePath) {
            this.e = customerBuglySOFilePath;
        }

        public synchronized String getDeviceID() {
            return this.f;
        }

        public synchronized void setDeviceID(String deviceID) {
            this.f = deviceID;
        }

        public synchronized CrashHandleCallback getCrashHandleCallback() {
            return this.h;
        }

        public synchronized void setCrashHandleCallback(CrashHandleCallback crashHandleCallback) {
            this.h = crashHandleCallback;
        }

        public synchronized boolean isEnableNativeCrashMonitor() {
            return this.i;
        }

        public synchronized void setEnableNativeCrashMonitor(boolean enableNativeCrashMonitor) {
            this.i = enableNativeCrashMonitor;
        }

        public synchronized boolean isEnableANRCrashMonitor() {
            return this.j;
        }

        public synchronized void setEnableANRCrashMonitor(boolean enableANRCrashMonitor) {
            this.j = enableANRCrashMonitor;
        }

        public synchronized void setOuterSdkVersion(Map<String, String> map) {
            this.g = map;
        }

        public synchronized void putOuterSdkVersion(String sdk, String version) {
            if (!(sdk == null || version == null)) {
                if (this.g == null) {
                    this.g = new HashMap();
                }
                this.g.put(sdk, version);
            }
        }

        public synchronized Map<String, String> getOuterSdkVersion() {
            return this.g;
        }
    }

    public static synchronized void initCrashReport(Context appContext, String crashReportAppID, boolean isDebug) {
        synchronized (CrashReport.class) {
            initCrashReport(appContext, crashReportAppID, isDebug, null);
        }
    }

    public static synchronized void initCrashReport(Context appContext, String crashReportAppID, boolean isDebug, UserStrategy customStrategy) {
        synchronized (CrashReport.class) {
            if (isDebug) {
                isDebug = true;
                z.a(new ac());
                z.d("Bugly debug模式开启，请在发布时把isDebug关闭。 -- 'isDebug' is enabled. Now is running in debug model, please disable it when you release.", new Object[0]);
                z.e("--------------------------------------------------------------------------------------------", new Object[0]);
                z.d("Bugly debug模式将有以下行为特性 -- The following list shows the behaviour of debug model: ", new Object[0]);
                z.d("[1] 输出详细的Bugly SDK的Log -- More detailed log of Bugly SDK will be output to logcat;", new Object[0]);
                z.d("[2] 每一条Crash都会被立即上报 -- Every crash caught by Bugly will be uploaded immediately.", new Object[0]);
                z.d("[3] 自定义日志将会在Logcat中输出 -- Custom log will be output to logcat.", new Object[0]);
                z.e("--------------------------------------------------------------------------------------------", new Object[0]);
                z.b("[init] bugly in debug mode.", new Object[0]);
            }
            if (customStrategy == null || customStrategy.k) {
                ab.a(appContext);
                z.a("BuglyLog is inited.", new Object[0]);
            }
            if (a != null) {
                z.d("already inited ! nothing to do !", new Object[0]);
            } else {
                appContext = ag.a(appContext);
                a = appContext;
                if (appContext == null) {
                    throw new ReportInitializedException("init arg 'appContext' should not be null!");
                }
                z.b("your appid is set to: %s", crashReportAppID);
                a a = a.a(appContext);
                a(appContext, a);
                List<String> H = a.H();
                Object i = a.i().equals("") ? "bugly" : a.i();
                if (H == null || !H.contains(i)) {
                    String substring;
                    if (H != null && H.size() > 0) {
                        for (String substring2 : H) {
                            z.a("[Configuration] channel disabled: " + substring2, new Object[0]);
                        }
                    }
                    z.a(a.i() + " crash report start init!", new Object[0]);
                    z.b("[init] bugly start init...", new Object[0]);
                    if (crashReportAppID == null) {
                        throw new ReportInitializedException("init arg 'crashReportAppID' should not be null!");
                    }
                    a.a(crashReportAppID);
                    z.a("setted APPID:%s", crashReportAppID);
                    if (customStrategy != null) {
                        String b = customStrategy.a;
                        if (!TextUtils.isEmpty(b)) {
                            if (b.length() > 100) {
                                substring2 = b.substring(0, 100);
                                z.d("appVersion %s length is over limit %d substring to %s", b, Integer.valueOf(100), substring2);
                            } else {
                                substring2 = b;
                            }
                            a.c(substring2);
                            z.a("setted APPVERSION:%s", customStrategy.a);
                        }
                        substring2 = customStrategy.b;
                        if (!TextUtils.isEmpty(substring2)) {
                            if (substring2.length() > 100) {
                                z.d("appChannel %s length is over limit %d substring to %s", substring2, Integer.valueOf(100), substring2.substring(0, 100));
                                substring2 = b;
                            }
                            a.g(substring2);
                            z.a("setted APPCHANNEL:%s", customStrategy.b);
                        }
                        String str = substring2;
                        b = customStrategy.c;
                        if (!TextUtils.isEmpty(b)) {
                            if (b.length() > 100) {
                                substring2 = b.substring(0, 100);
                                z.d("appPackageName %s length is over limit %d substring to %s", b, Integer.valueOf(100), substring2);
                            } else {
                                substring2 = b;
                            }
                            a.b(substring2);
                            z.a("setted PACKAGENAME:%s", customStrategy.c);
                        }
                        substring2 = customStrategy.e;
                        if (!TextUtils.isEmpty(substring2)) {
                            a.h(substring2);
                            z.a("setted libBugly.so file path :%s", substring2);
                        }
                        substring2 = customStrategy.f;
                        if (substring2 != null) {
                            if (substring2.length() > 100) {
                                substring2 = substring2.substring(0, 100);
                                z.d("deviceId %s length is over limit %d substring to %s", str, Integer.valueOf(100), substring2);
                            }
                            a.e(substring2);
                            z.a("setted deviceId :%s", substring2);
                        }
                        Map g = customStrategy.g;
                        if (g != null && g.size() > 0) {
                            for (Entry entry : g.entrySet()) {
                                a(a, "SDK_" + ((String) entry.getKey()), (String) entry.getValue());
                            }
                        }
                    }
                    p.a().a(a);
                    y a2 = y.a();
                    q a3 = q.a(appContext);
                    w a4 = w.a(appContext, a2, a, a3);
                    c a5 = c.a(appContext, a, new StrategyBean(), q.a(appContext), a4, a2);
                    if (customStrategy == null || customStrategy.d <= 0) {
                        p.a().a(0);
                    } else {
                        p.a().a(customStrategy.d);
                        z.a("setted APP_REPORT_DELAY %d", Long.valueOf(customStrategy.d));
                    }
                    CrashHandleCallback crashHandleCallback = null;
                    if (!(customStrategy == null || customStrategy.getCrashHandleCallback() == null)) {
                        crashHandleCallback = customStrategy.getCrashHandleCallback();
                        z.a("setted CrashHanldeCallback", new Object[0]);
                    }
                    com.tencent.bugly.crashreport.crash.c a6 = com.tencent.bugly.crashreport.crash.c.a(appContext, a3, a5, a4, a, a2, isDebug, crashHandleCallback);
                    a6.d();
                    if (customStrategy == null || customStrategy.isEnableNativeCrashMonitor()) {
                        a6.f();
                    } else {
                        z.a("closed native!", new Object[0]);
                        a6.e();
                    }
                    a6.g();
                    BuglyBroadcastRecevier instance = BuglyBroadcastRecevier.getInstance();
                    instance.addFilter("android.net.conn.CONNECTIVITY_CHANGE");
                    instance.regist(appContext);
                    a(a);
                    z.a("crash report inited!", new Object[0]);
                    z.b("[init] bugly init finished.", new Object[0]);
                } else {
                    z.a("[init] bugly(%s) is closed.", i);
                }
            }
        }
    }

    private static void a(a aVar) {
        Class cls;
        Object obj;
        String i;
        try {
            cls = Class.forName("com.tencent.bugly.unity.UnityAgent");
            obj = "com.tencent.bugly";
            i = aVar.i();
            if (!"".equals(i)) {
                obj = obj + "." + i;
            }
            ae.a(cls, "sdkPackageName", obj, null);
        } catch (Throwable th) {
            z.a("no unity agent", new Object[0]);
        }
        try {
            cls = Class.forName("com.tencent.bugly.cococs.Cocos2dxAgent");
            obj = "com.tencent.bugly";
            i = aVar.i();
            if (!"".equals(i)) {
                obj = obj + "." + i;
            }
            ae.a(cls, "sdkPackageName", obj, null);
        } catch (Throwable th2) {
            z.a("no cocos agent", new Object[0]);
        }
    }

    private static void a(Context context, a aVar) {
        List I = aVar.I();
        if (I == null || I.size() == 0) {
            z.a("not have bugly channel version", new Object[0]);
        } else if (I.size() != 1) {
            StringBuilder append = new StringBuilder(16).append((String) I.get(1));
            int i = 2;
            while (true) {
                int i2 = i;
                if (i2 >= I.size()) {
                    break;
                }
                append.append(",").append((String) I.get(i2));
                i = i2 + 1;
            }
            if (!a(I, aVar.H())) {
                String format = String.format("# BUGLY CRASH REPORTER EXISTS IN MULTIPLE SDK. FOR MORE EFFICIENCY, \n# PLEASE SET <meta-data> IN [AndroidManifest.xml]:\n# <application>\n# ...\n#     <meta-data android:name=\"BUGLY_DISABLE\" android:value=\"%s\"/>\n# ...\n# </application>\n# FOR MORE INFOMATION, VISIT:\n# http://bugly.qq.com/androidsdk", new Object[]{append});
                String format2 = String.format("# 您的App中含有多个异常上报模块，为了节省您的资源，\n# 请在[AndroidManifest.xml]中添加meta-data配置：[AndroidManifest.xml]:\n# <application>\n# ...\n#     <meta-data android:name=\"BUGLY_DISABLE\" android:value=\"%s\"/>\n# ...\n# </application>\n# 更多信息请参见：\n# http://bugly.qq.com/androidsdk", new Object[]{append});
                z.d("--------------------------BUGLY TIPS--------------------------", new Object[0]);
                z.d(format, new Object[0]);
                z.d("--------------------------------------------------------------", new Object[0]);
                z.d(format2, new Object[0]);
                z.d("--------------------------------------------------------------", new Object[0]);
            }
        }
    }

    private static boolean a(List<String> list, List<String> list2) {
        if (list2 == null || list2.size() == 0) {
            return false;
        }
        int i = 0;
        for (String contains : list2) {
            int i2;
            if (list.contains(contains)) {
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2;
        }
        if (i >= list.size() - 1) {
            return true;
        }
        return false;
    }

    public static synchronized String getAppID() {
        String d;
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            d = a.a(a).d();
        }
        return d;
    }

    public static synchronized void setUserId(String userId) {
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            if (userId != null) {
                if (userId.length() > 100) {
                    z.d("userId %s length is over limit %d substring to %s", userId, Integer.valueOf(100), userId.substring(0, 100));
                    userId = userId;
                }
            }
            a.a(a).d(userId);
            z.b("[param] set userId : %s", userId);
            c.a().f();
        }
    }

    public static synchronized String getUserId() {
        String m;
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            m = a.a(a).m();
        }
        return m;
    }

    public static synchronized String getAppVer() {
        String e;
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            e = a.a(a).e();
        }
        return e;
    }

    public static synchronized String getAppChannel() {
        String B;
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            B = a.a(a).B();
        }
        return B;
    }

    public static synchronized boolean isLastSessionCrash() {
        boolean b;
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            b = com.tencent.bugly.crashreport.crash.c.a().b();
        }
        return b;
    }

    public static synchronized void testJavaCrash() {
        synchronized (CrashReport.class) {
            z.d("Test java crash...", new Object[0]);
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            throw new RuntimeException("This Crash create for Test! You can go to Bugly see more detail!");
        }
    }

    public static synchronized void testNativeCrash() {
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            z.a("start to create a native crash for test!", new Object[0]);
            com.tencent.bugly.crashreport.crash.c.a().j();
        }
    }

    public static synchronized void testANRCrash() {
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            z.a("start to create a anr crash for test!", new Object[0]);
            com.tencent.bugly.crashreport.crash.c.a().k();
        }
    }

    public static synchronized void postCatchedException(Throwable catchedThrowable) {
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            com.tencent.bugly.crashreport.crash.c.a().a(Thread.currentThread(), catchedThrowable, false);
        }
    }

    public static synchronized void closeNativeReport() {
        synchronized (CrashReport.class) {
            if (a == null) {
                throw new ReportInitializedException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            com.tencent.bugly.crashreport.crash.c.a().e();
        }
    }

    public static synchronized void closeCrashReport() {
        synchronized (CrashReport.class) {
            if (a != null) {
                com.tencent.bugly.crashreport.crash.c.a().i();
            }
        }
    }

    public static void setUserSceneTag(Context context, int tagId) {
        if (context == null) {
            throw new ReportInitializedException("setTag args context should not be null");
        } else if (tagId <= 0) {
            throw new ReportInitializedException("setTag args tagId should > 0");
        } else {
            a.a(context).a(tagId);
            z.b("[param] set user scene tag: %d", Integer.valueOf(tagId));
        }
    }

    public static int getUserSceneTagId(Context context) {
        if (context != null) {
            return a.a(context).N();
        }
        throw new ReportInitializedException("getUserSceneTagId args context should not be null");
    }

    public static String getUserData(Context context, String key) {
        if (context == null) {
            throw new ReportInitializedException("getUserDataValue args context should not be null");
        } else if (ag.b(key)) {
            return null;
        } else {
            return a.a(context).k(key);
        }
    }

    public static void putUserData(Context context, String key, String value) {
        if (context == null) {
            throw new ReportInitializedException("putUserData args context should not be null");
        } else if (ag.b(key)) {
            throw new ReportInitializedException("putUserData args key should not be null");
        } else if (ag.b(value)) {
            z.d("putUserData args value should not be null", new Object[0]);
        } else if (key.matches("[a-zA-Z[0-9]]+")) {
            if (value.length() > CtaButton.WIDTH_DIPS) {
                z.d("user data value length over limit %d , has been cutted!", Integer.valueOf(CtaButton.WIDTH_DIPS));
                value = value.substring(0, CtaButton.WIDTH_DIPS);
            }
            a a = a.a(context);
            if (a.L().contains(key)) {
                a.a(context).a(key, value);
                z.c("replace KV %s %s", key, value);
            } else if (a.K() >= 10) {
                z.d("user data size is over limit %d , will drop this new key %s", Integer.valueOf(10), key);
            } else if (key.length() > 50) {
                z.d("user data key length over limit %d , will drop this new key %s", Integer.valueOf(50), key);
            } else {
                a.a(context).a(key, value);
                z.b("[param] set user data: %s - %s", key, value);
            }
        } else {
            throw new ReportInitializedException("putUserData args key should match [a-zA-Z[0-9]]+  {" + key + "}");
        }
    }

    private static void a(Context context, String str, String str2) {
        if (context != null && !ag.b(str) && !ag.b(str2)) {
            String replace = str.replace("[a-zA-Z[0-9]]+", "");
            if (replace.length() > 50) {
                z.d("putSdkData key length over limit %d , will drop this new key %s", Integer.valueOf(50), replace);
                return;
            }
            if (str2.length() > CtaButton.WIDTH_DIPS) {
                z.d("putSdkData value length over limit %d , has been cutted!", Integer.valueOf(CtaButton.WIDTH_DIPS));
                str2 = str2.substring(0, CtaButton.WIDTH_DIPS);
            }
            a.a(context).b(replace, str2);
            z.b("[param] putSdkData data: %s - %s", replace, str2);
        }
    }

    public static String removeUserData(Context context, String dataKey) {
        if (context == null) {
            throw new ReportInitializedException("removeUserData args context should not be null");
        } else if (ag.b(dataKey)) {
            return null;
        } else {
            z.b("[param] remove user data: %s", dataKey);
            return a.a(context).j(dataKey);
        }
    }

    public static Set<String> getAllUserDataKeys(Context context) {
        if (context != null) {
            return a.a(context).L();
        }
        throw new ReportInitializedException("getAllUserDataKeys args context should not be null");
    }

    public static int getUserDatasSize(Context context) {
        if (context != null) {
            return a.a(context).K();
        }
        throw new ReportInitializedException("getUserDatasSize args context should not be null");
    }

    public static boolean setJavascriptMonitor(WebView webView, boolean autoInject) {
        return setJavascriptMonitor(webView, autoInject, false);
    }

    public static boolean setJavascriptMonitor(WebView webView, boolean autoInject, boolean force) {
        if (webView.getUrl() == null) {
            return false;
        }
        if (b != null && b.equals(webView.getUrl())) {
            return true;
        }
        b = webView.getUrl();
        if (force || VERSION.SDK_INT >= 19) {
            z.a("Set webview monitor.", new Object[0]);
            WebSettings settings = webView.getSettings();
            if (!settings.getJavaScriptEnabled()) {
                z.a("Enable the javascript needed by webview monitor.", new Object[0]);
                settings.setJavaScriptEnabled(true);
            }
            aj a = aj.a(webView);
            if (a != null) {
                z.a("Add a secure javascript interface to the webview.", new Object[0]);
                webView.addJavascriptInterface(a, "exceptionUploader");
            }
            if (autoInject) {
                if (a == null) {
                    z.e("CrashReport has not been initialed! please to call method 'initCrashReport' first!", new Object[0]);
                    return false;
                }
                z.a("Inject bugly.js(v%s) to the webview.", aa.b());
                webView.loadUrl("javascript:" + aa.a());
            }
            return true;
        }
        z.e("This interface is only available for Android 4.4 or later.", new Object[0]);
        return false;
    }
}
