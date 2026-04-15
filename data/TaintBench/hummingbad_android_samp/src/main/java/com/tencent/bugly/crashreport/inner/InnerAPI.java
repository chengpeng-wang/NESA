package com.tencent.bugly.crashreport.inner;

import com.tencent.bugly.crashreport.ReportInitializedException;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.z;
import java.util.Map;

/* compiled from: BUGLY */
public class InnerAPI {
    public static void postU3dCrashAsync(String errorType, String errorMsg, String errorStack) {
        if (errorType == null || errorMsg == null || errorStack == null) {
            z.e("post u3d fail args null", new Object[0]);
        }
        c a = c.a();
        if (a == null) {
            throw new ReportInitializedException("post u3d fail,Report has not been initialed! pls to call method 'initCrashReport' first!");
        }
        z.a("post u3d crash %s %s", errorType, errorMsg);
        a.a(Thread.currentThread(), errorType, errorMsg, errorStack);
    }

    public static void postCocos2dxCrashAsync(int category, String errorType, String errorMsg, String errorStack) {
        if (errorType == null || errorMsg == null || errorStack == null) {
            z.e("post cocos2d-x fail args null", new Object[0]);
        } else if (category == 5 || category == 6) {
            c a = c.a();
            if (a == null) {
                throw new ReportInitializedException("post cocos2d-x fail,Report has not been initialed! pleas call method 'initCrashReport' first!");
            }
            z.a("post cocos2d-x crash %s %s", errorType, errorMsg);
            a.a(Thread.currentThread(), category, errorType, errorMsg, errorStack);
        } else {
            z.e("post cocos2d-x fail category illeagle: %d", Integer.valueOf(category));
        }
    }

    public static void postH5CrashAsync(Thread thread, String errorType, String errorMsg, String errorStack, Map<String, String> h5ExtraInfos) {
        if (errorType == null || errorMsg == null || errorStack == null) {
            z.e("post h5 fail args null", new Object[0]);
            return;
        }
        c a = c.a();
        if (a == null) {
            throw new ReportInitializedException("Failed to post H5 crash, CrashReport has not been initialed! Pleas call method 'initCrashReport' first!");
        }
        z.a("post h5 crash %s %s", errorType, errorMsg);
        a.a(thread, errorType, errorMsg, errorStack, (Map) h5ExtraInfos);
    }
}
