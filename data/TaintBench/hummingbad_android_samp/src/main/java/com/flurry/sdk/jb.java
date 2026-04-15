package com.flurry.sdk;

import android.text.TextUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class jb {
    private final Pattern a = Pattern.compile(".*?(%\\{\\w+\\}).*?");

    public static boolean a(String str, String str2) {
        return !TextUtils.isEmpty(str2) && str2.equals("%{" + str + "}");
    }

    public final String a(String str) {
        Matcher matcher = this.a.matcher(str);
        return matcher.matches() ? matcher.group(1) : null;
    }
}
