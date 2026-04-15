package com.facebook.ads.internal.util;

import android.content.Context;
import android.os.Build.VERSION;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.internal.e;
import java.lang.reflect.Constructor;

public class h {
    private static String a = null;

    public static String a() {
        if (r.a(AdSettings.getUrlPrefix())) {
            return "https://www.facebook.com/";
        }
        return String.format("https://www.%s.facebook.com", new Object[]{AdSettings.getUrlPrefix()});
    }

    private static String a(Context context) {
        return WebSettings.getDefaultUserAgent(context);
    }

    public static String a(Context context, e eVar) {
        if (eVar == e.NATIVE_250 || eVar == e.NATIVE_UNKNOWN || eVar == null) {
            return System.getProperty("http.agent");
        }
        if (a == null) {
            if (VERSION.SDK_INT >= 17) {
                try {
                    String a = a(context);
                    a = a;
                    return a;
                } catch (Exception e) {
                }
            }
            try {
                a = a(context, "android.webkit.WebSettings", "android.webkit.WebView");
            } catch (Exception e2) {
                try {
                    a = a(context, "android.webkit.WebSettingsClassic", "android.webkit.WebViewClassic");
                } catch (Exception e3) {
                    WebView webView = new WebView(context.getApplicationContext());
                    a = webView.getSettings().getUserAgentString();
                    webView.destroy();
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

    public static void a(WebView webView) {
        webView.loadUrl("about:blank");
        webView.clearCache(true);
        if (VERSION.SDK_INT > 11) {
            webView.onPause();
            return;
        }
        try {
            WebView.class.getMethod("onPause", new Class[0]).invoke(webView, new Object[0]);
        } catch (Exception e) {
        }
    }

    public static void b(WebView webView) {
        WebSettings settings = webView.getSettings();
        if (VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(0);
            return;
        }
        try {
            WebSettings.class.getMethod("setMixedContentMode", new Class[0]).invoke(settings, new Object[]{Integer.valueOf(0)});
        } catch (Exception e) {
        }
    }
}
