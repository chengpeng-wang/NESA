package com.cleanmaster.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.lang.reflect.Constructor;

/* compiled from: AdWebViewUtils */
public class b {
    private static String a = null;

    public static String a(Context context) {
        WebView webView;
        if (a == null) {
            if (VERSION.SDK_INT >= 17) {
                try {
                    a = b(context);
                } catch (Exception e) {
                    try {
                        a = a(context, "android.webkit.WebSettings", "android.webkit.WebView");
                    } catch (Exception e2) {
                        try {
                            a = a(context, "android.webkit.WebSettingsClassic", "android.webkit.WebViewClassic");
                        } catch (Exception e3) {
                            webView = new WebView(context.getApplicationContext());
                            a = webView.getSettings().getUserAgentString();
                            webView.destroy();
                        }
                    }
                }
            } else {
                try {
                    a = a(context, "android.webkit.WebSettings", "android.webkit.WebView");
                } catch (Exception e4) {
                    try {
                        a = a(context, "android.webkit.WebSettingsClassic", "android.webkit.WebViewClassic");
                    } catch (Exception e5) {
                        webView = new WebView(context.getApplicationContext());
                        a = webView.getSettings().getUserAgentString();
                        webView.destroy();
                    }
                }
            }
        }
        return a;
    }

    private static String a(Context context, String str, String str2) {
        Class cls = Class.forName(str);
        Constructor declaredConstructor = cls.getDeclaredConstructor(new Class[]{Context.class, Class.forName(str2)});
        declaredConstructor.setAccessible(true);
        try {
            String str3 = (String) cls.getMethod("getUserAgentString", new Class[0]).invoke(declaredConstructor.newInstance(new Object[]{context, null}), new Object[0]);
            return str3;
        } finally {
            declaredConstructor.setAccessible(false);
        }
    }

    @TargetApi(17)
    private static String b(Context context) {
        return WebSettings.getDefaultUserAgent(context);
    }
}
