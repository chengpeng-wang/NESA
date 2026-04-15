package com.cmcm.adsdk.nativead;

import android.content.Context;
import android.text.TextUtils;
import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.requestconfig.data.PosBean;
import com.cmcm.adsdk.requestconfig.log.a;

/* compiled from: NativeAdLoaderFactory */
public final class f {
    public static e a(Context context, PosBean posBean) {
        if (posBean == null || TextUtils.isEmpty(posBean.name)) {
            return null;
        }
        try {
            String[] split = posBean.name.split("_");
            if (split.length == 0) {
                a.a(Const.TAG, "config type:" + posBean.name + ",has error");
                return null;
            }
            String str = split[0];
            String valueOf = String.valueOf(posBean.placeid);
            String str2 = posBean.parameter;
            String str3 = posBean.name;
            if (str.equalsIgnoreCase(Const.KEY_FB)) {
                a.a(Const.TAG, "config type:" + str3 + ",create FBNativeAdLoader");
                if (posBean.mIsLoadList) {
                    return new b(context, str3, valueOf, str2);
                }
                return new c(context, str3, valueOf, str2);
            } else if (str.equalsIgnoreCase(Const.KEY_CM)) {
                a.a(Const.TAG, "create PicksNativeAdLoader");
                return new g(context, valueOf);
            } else if (str.equalsIgnoreCase(Const.KEY_YH)) {
                a.a(Const.TAG, "config type:" + str3 + ",create FlurryNativeLoader");
                return a("com.cmcm.adsdk.nativead.FlurryNativeLoader", context, valueOf, str2, str3);
            } else if (str.equalsIgnoreCase(Const.KEY_MP)) {
                a.a(Const.TAG, "create MopubNativeAdLoader");
                return a("com.cmcm.adsdk.nativead.MopubNativeAdLoader", context, valueOf, str2, str3);
            } else if (str.equalsIgnoreCase(Const.KEY_AB)) {
                a.a(Const.TAG, "create AdmobNativeLoader");
                return a("com.cmcm.adsdk.nativead.AdmobNativeLoader", context, valueOf, str2, str3);
            } else {
                a.c(Const.TAG, "unmatched adtype:" + str3);
                return null;
            }
        } catch (Exception e) {
            a.c(Const.TAG, e.toString());
            return null;
        }
    }

    private static e a(String str, Context context, String str2, String str3, String str4) {
        try {
            return (e) Class.forName(str).getConstructor(new Class[]{Context.class, String.class, String.class, String.class}).newInstance(new Object[]{context, str2, str3, str4});
        } catch (Exception e) {
            a.c(Const.TAG, e.toString());
            return null;
        }
    }
}
