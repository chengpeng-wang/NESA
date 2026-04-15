package com.cmcm.adsdk.bannerad;

import android.content.Context;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.requestconfig.log.a;

/* compiled from: BannerLoaderFactory */
public final class b {
    public static c a(String str, Context context, String str2, CMBannerAdSize cMBannerAdSize, String str3) {
        try {
            if (str.equalsIgnoreCase(Const.KEY_MP)) {
                a.a(Const.TAG, "create MopubBannerLoader");
                return new f(context, str2, str3);
            } else if (str.equalsIgnoreCase(Const.KEY_FB)) {
                a.a(Const.TAG, "create FacebookBannerLoader");
                return new e(context, str2, cMBannerAdSize, str3);
            } else if (str.equalsIgnoreCase(Const.KEY_AB)) {
                a.a(Const.TAG, "create AdmobBannerLoader");
                return new a(context, str2, cMBannerAdSize, str3);
            } else {
                a.c(Const.TAG, "unmatched adtype:" + str);
                return null;
            }
        } catch (Exception e) {
            a.c(Const.TAG, e.toString());
        }
    }
}
