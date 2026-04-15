package com.tencent.bugly.proguard;

import android.webkit.WebView;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
public class aj {
    private static HashMap<aj, WebView> a = new HashMap();
    private String b = null;
    private Thread c = null;
    private String d = null;
    private Map<String, String> e = null;

    private aj() {
    }

    public static aj a(WebView webView) {
        if (a.values().contains(webView)) {
            return null;
        }
        aj ajVar = new aj();
        a.put(ajVar, webView);
        ajVar.c = Thread.currentThread();
        ajVar.d = a(ajVar.c);
        ajVar.e = b(webView);
        return ajVar;
    }

    private static String a(Thread thread) {
        if (thread == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MASTNativeAdConstants.NEWLINE);
        for (int i = 2; i < thread.getStackTrace().length; i++) {
            StackTraceElement stackTraceElement = thread.getStackTrace()[i];
            if (!stackTraceElement.toString().contains("crashreport")) {
                stringBuilder.append(stackTraceElement.toString()).append(MASTNativeAdConstants.NEWLINE);
            }
        }
        return stringBuilder.toString();
    }

    private static Map<String, String> b(WebView webView) {
        HashMap hashMap = new HashMap();
        hashMap.put("[WebView] ContentDescription", "" + webView.getContentDescription());
        return hashMap;
    }
}
