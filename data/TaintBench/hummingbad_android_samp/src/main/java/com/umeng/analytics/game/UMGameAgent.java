package com.umeng.analytics.game;

import android.content.Context;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import com.umeng.analytics.Gender;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.a;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.analytics.social.UMSocialService;
import com.umeng.analytics.social.e;
import u.aly.aj;

public class UMGameAgent extends MobclickAgent {
    private static final String a = "Input string is null or empty";
    private static final String b = "Input string must be less than 64 chars";
    private static final String c = "Input value type is negative";
    private static final String d = "The int value for 'Pay Channels' ranges between 1 ~ 99 ";
    private static final c e = new c();
    private static Context f;

    public static void init(Context context) {
        e.a(context);
        f = context.getApplicationContext();
    }

    public static void setTraceSleepTime(boolean z) {
        e.a(z);
    }

    public static void setPlayerInfo(String str, int i, int i2, String str2) {
        if (a(str)) {
            aj.b(a.e, a);
            str = null;
        }
        if (i < 0 || i > CtaButton.WIDTH_DIPS) {
            aj.c(a.e, "The int value for 'Age' range between 0~200");
            i = 0;
        }
        if (a(str2)) {
            aj.b(a.e, a);
            str2 = null;
        }
        e.a(str, i, Gender.getGender(i2), str2);
    }

    public static void setPlayerLevel(String str) {
        if (a(str)) {
            aj.b(a.e, a);
        } else if (str.length() > 64) {
            aj.b(a.e, b);
        } else {
            e.a(str);
        }
    }

    public static void startLevel(String str) {
        if (a(str)) {
            aj.b(a.e, a);
        } else if (str.length() > 64) {
            aj.b(a.e, b);
        } else {
            e.b(str);
        }
    }

    public static void finishLevel(String str) {
        if (a(str)) {
            aj.b(a.e, a);
        } else if (str.length() > 64) {
            aj.b(a.e, b);
        } else {
            e.c(str);
        }
    }

    public static void failLevel(String str) {
        if (a(str)) {
            aj.b(a.e, a);
        } else if (str.length() > 64) {
            aj.b(a.e, b);
        } else {
            e.d(str);
        }
    }

    public static void pay(double d, double d2, int i) {
        if (i <= 0 || i >= 100) {
            aj.b(a.e, d);
        } else if (d < 0.0d || d2 < 0.0d) {
            aj.b(a.e, c);
        } else {
            e.a(d, d2, i);
        }
    }

    public static void pay(double d, String str, int i, double d2, int i2) {
        if (i2 <= 0 || i2 >= 100) {
            aj.b(a.e, d);
        } else if (d < 0.0d || i < 0 || d2 < 0.0d) {
            aj.b(a.e, c);
        } else if (a(str)) {
            aj.b(a.e, a);
        } else {
            e.a(d, str, i, d2, i2);
        }
    }

    public static void buy(String str, int i, double d) {
        if (a(str)) {
            aj.b(a.e, a);
        } else if (i < 0 || d < 0.0d) {
            aj.b(a.e, c);
        } else {
            e.a(str, i, d);
        }
    }

    public static void use(String str, int i, double d) {
        if (a(str)) {
            aj.b(a.e, a);
        } else if (i < 0 || d < 0.0d) {
            aj.b(a.e, c);
        } else {
            e.b(str, i, d);
        }
    }

    public static void bonus(double d, int i) {
        if (d < 0.0d) {
            aj.b(a.e, c);
        } else if (i <= 0 || i >= 100) {
            aj.b(a.e, d);
        } else {
            e.a(d, i);
        }
    }

    public static void bonus(String str, int i, double d, int i2) {
        if (a(str)) {
            aj.b(a.e, a);
        } else if (i < 0 || d < 0.0d) {
            aj.b(a.e, c);
        } else if (i2 <= 0 || i2 >= 100) {
            aj.b(a.e, d);
        } else {
            e.a(str, i, d, i2);
        }
    }

    private static boolean a(String str) {
        if (str != null && str.trim().length() > 0) {
            return false;
        }
        return true;
    }

    public static void onEvent(String str, String str2) {
        MobclickAgent.onEvent(f, str, str2);
    }

    public static void onSocialEvent(Context context, String str, UMPlatformData... uMPlatformDataArr) {
        if (context == null) {
            aj.b(a.e, "context is null in onShareEvent");
            return;
        }
        e.e = "4";
        UMSocialService.share(context, str, uMPlatformDataArr);
    }

    public static void onSocialEvent(Context context, UMPlatformData... uMPlatformDataArr) {
        if (context == null) {
            aj.b(a.e, "context is null in onShareEvent");
            return;
        }
        e.e = "4";
        UMSocialService.share(context, uMPlatformDataArr);
    }
}
