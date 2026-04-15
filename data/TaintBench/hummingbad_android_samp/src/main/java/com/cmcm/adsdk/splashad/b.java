package com.cmcm.adsdk.splashad;

import com.cmcm.adsdk.Const;
import com.cmcm.adsdk.requestconfig.data.PosBean;
import com.cmcm.adsdk.requestconfig.log.a;

/* compiled from: SplashAdapterFactory */
public final class b {
    public static SplashBaseAdapter a(PosBean posBean) {
        try {
            String str = posBean.name;
            if (str.equalsIgnoreCase(Const.KEY_CM)) {
                a.a("SplashAdManager", "create adadapter: cm ");
                return new a(posBean.parameter, posBean.placeid);
            }
            a.a("SplashAdManager", "unmatched adtype:" + str);
            return null;
        } catch (Exception e) {
            a.a("SplashAdManager", e.toString());
        }
    }
}
