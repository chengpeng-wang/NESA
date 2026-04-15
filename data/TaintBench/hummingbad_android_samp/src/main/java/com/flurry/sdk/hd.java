package com.flurry.sdk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public final class hd {
    public static boolean a(Context context, Intent intent) {
        if (context == null || intent == null) {
            return false;
        }
        try {
            if (!(context instanceof Activity)) {
                intent.addFlags(276824064);
            }
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public static boolean a(Context context, String str) {
        return (context == null || TextUtils.isEmpty(str) || !mb.c(str)) ? false : a(context, new Intent("android.intent.action.VIEW").setData(Uri.parse(str)));
    }

    public static boolean b(Context context, String str) {
        Intent a = hb.a(context, str);
        return a != null && a(context, a);
    }

    public static boolean c(Context context, String str) {
        return (context == null || TextUtils.isEmpty(str)) ? false : a(context, new Intent("android.intent.action.VIEW").setData(Uri.parse(str)));
    }
}
