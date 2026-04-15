package com.cleanmaster.ui.app.market.deeplink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.cleanmaster.ui.app.market.Ad;

/* compiled from: DeepLinkUtil */
public class a {
    public static boolean a(Context context, Ad ad) {
        String pkg = ad.getPkg();
        String deepLink = ad.getDeepLink();
        if (context == null || TextUtils.isEmpty(pkg)) {
            return false;
        }
        return a(context, pkg, deepLink);
    }

    static boolean a(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            com.cleanmaster.common.a.d(context, str);
            return true;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str2));
        if (!(context instanceof Activity)) {
            intent.setFlags(268435456);
        }
        return com.cleanmaster.common.a.a(context, intent);
    }
}
