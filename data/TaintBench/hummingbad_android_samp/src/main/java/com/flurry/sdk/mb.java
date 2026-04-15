package com.flurry.sdk;

import android.net.Uri;
import android.text.TextUtils;
import com.mopub.common.Constants;
import java.net.URI;
import java.net.URISyntaxException;

public final class mb {
    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        URI b = b(str);
        if (b == null) {
            return str;
        }
        String scheme = b.getScheme();
        if (TextUtils.isEmpty(scheme)) {
            return new StringBuilder(Constants.HTTP).append(str).toString();
        }
        String toLowerCase = scheme.toLowerCase();
        if (scheme.equals(toLowerCase)) {
            return str;
        }
        int indexOf = str.indexOf(scheme);
        return indexOf >= 0 ? toLowerCase + str.substring(scheme.length() + indexOf) : str;
    }

    public static String a(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            if (new URI(str).isAbsolute() || TextUtils.isEmpty(str2)) {
                return str;
            }
            URI uri = new URI(str2);
            return uri.getScheme() + "://" + uri.getHost() + str;
        } catch (Exception e) {
            return str;
        }
    }

    public static URI b(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static boolean c(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Uri parse = Uri.parse(str);
        return parse.getScheme() != null && parse.getScheme().equals("market");
    }

    public static boolean d(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Uri parse = Uri.parse(str);
        return parse.getHost() != null && parse.getHost().equals("play.google.com") && parse.getScheme() != null && parse.getScheme().startsWith(Constants.HTTP);
    }
}
