package com.google.android.apps.analytics;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

class NetworkRequestUtil {
    private static final String FAKE_DOMAIN_HASH = "999";
    private static final String GOOGLE_ANALYTICS_GIF_PATH = "/__utm.gif";

    NetworkRequestUtil() {
    }

    public static String constructEventRequestPath(Event event, String str) {
        Locale locale = Locale.getDefault();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(String.format("5(%s*%s", new Object[]{event.category, event.action}));
        if (event.label != null) {
            stringBuilder2.append("*").append(event.label);
        }
        stringBuilder2.append(")");
        if (event.value > -1) {
            stringBuilder2.append(String.format("(%d)", new Object[]{Integer.valueOf(event.value)}));
        }
        stringBuilder.append(GOOGLE_ANALYTICS_GIF_PATH);
        stringBuilder.append("?utmwv=4.3");
        stringBuilder.append("&utmn=").append(event.randomVal);
        stringBuilder.append("&utmt=event");
        stringBuilder.append("&utme=").append(stringBuilder2.toString());
        stringBuilder.append("&utmcs=UTF-8");
        stringBuilder.append(String.format("&utmsr=%dx%d", new Object[]{Integer.valueOf(event.screenWidth), Integer.valueOf(event.screenHeight)}));
        stringBuilder.append(String.format("&utmul=%s-%s", new Object[]{locale.getLanguage(), locale.getCountry()}));
        stringBuilder.append("&utmac=").append(event.accountId);
        stringBuilder.append("&utmcc=").append(getEscapedCookieString(event, str));
        return stringBuilder.toString();
    }

    public static String constructPageviewRequestPath(Event event, String str) {
        String str2 = "/";
        String str3 = "";
        if (event.action != null) {
            str3 = event.action;
        }
        String str4 = "/";
        if (!str3.startsWith(str2)) {
            String str5 = "/";
            str3 = str2 + str3;
        }
        str3 = encode(str3);
        Locale locale = Locale.getDefault();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GOOGLE_ANALYTICS_GIF_PATH);
        stringBuilder.append("?utmwv=4.3");
        stringBuilder.append("&utmn=").append(event.randomVal);
        stringBuilder.append("&utmcs=UTF-8");
        stringBuilder.append(String.format("&utmsr=%dx%d", new Object[]{Integer.valueOf(event.screenWidth), Integer.valueOf(event.screenHeight)}));
        stringBuilder.append(String.format("&utmul=%s-%s", new Object[]{locale.getLanguage(), locale.getCountry()}));
        stringBuilder.append("&utmp=").append(str3);
        stringBuilder.append("&utmac=").append(event.accountId);
        stringBuilder.append("&utmcc=").append(getEscapedCookieString(event, str));
        return stringBuilder.toString();
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getEscapedCookieString(Event event, String str) {
        String str2 = FAKE_DOMAIN_HASH;
        String str3 = ".";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("__utma=");
        String str4 = FAKE_DOMAIN_HASH;
        String str5 = ".";
        stringBuilder.append(str2).append(str3);
        str5 = ".";
        stringBuilder.append(event.userId).append(str3);
        str5 = ".";
        stringBuilder.append(event.timestampFirst).append(str3);
        str5 = ".";
        stringBuilder.append(event.timestampPrevious).append(str3);
        str5 = ".";
        stringBuilder.append(event.timestampCurrent).append(str3);
        stringBuilder.append(event.visits);
        if (str != null) {
            stringBuilder.append("+__utmz=");
            str4 = FAKE_DOMAIN_HASH;
            str5 = ".";
            stringBuilder.append(str2).append(str3);
            str5 = ".";
            stringBuilder.append(event.timestampFirst).append(str3);
            stringBuilder.append("1.1.");
            stringBuilder.append(str);
        }
        return encode(stringBuilder.toString());
    }
}
