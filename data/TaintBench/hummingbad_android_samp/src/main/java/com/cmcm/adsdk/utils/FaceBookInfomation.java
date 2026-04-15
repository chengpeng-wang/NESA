package com.cmcm.adsdk.utils;

import android.net.Uri;
import android.text.TextUtils;
import com.cmcm.adsdk.Const;
import com.facebook.ads.NativeAd;
import com.facebook.ads.internal.adapters.j;
import com.facebook.ads.internal.adapters.k;
import com.facebook.ads.internal.adapters.n;
import java.lang.reflect.Field;
import org.json.JSONException;
import org.json.JSONObject;

public class FaceBookInfomation {
    public static String getRawJson(int operation, NativeAd ad) {
        Object obj = null;
        if (ad == null) {
            return "";
        }
        String str;
        Object obj2;
        Object obj3;
        Object obj4;
        Object obj5;
        Object obj6;
        Object obj7;
        try {
            if (TextUtils.isEmpty(ad.getAdIcon().getUrl())) {
                str = null;
            } else {
                str = ad.getAdIcon().getUrl();
            }
            obj2 = str;
        } catch (Exception e) {
            obj2 = null;
        }
        try {
            if (TextUtils.isEmpty(ad.getAdCoverImage().getUrl())) {
                str = null;
            } else {
                str = ad.getAdCoverImage().getUrl();
            }
            obj3 = str;
        } catch (Exception e2) {
            obj3 = null;
        }
        try {
            if (TextUtils.isEmpty(ad.getAdTitle())) {
                str = null;
            } else {
                str = ad.getAdTitle();
            }
            obj4 = str;
        } catch (Exception e3) {
            obj4 = null;
        }
        try {
            if (TextUtils.isEmpty(ad.getAdBody())) {
                str = null;
            } else {
                str = ad.getAdBody();
            }
            obj5 = str;
        } catch (Exception e4) {
            obj5 = null;
        }
        try {
            if (TextUtils.isEmpty(ad.getAdSocialContext())) {
                str = null;
            } else {
                str = ad.getAdSocialContext();
            }
            obj6 = str;
        } catch (Exception e5) {
            obj6 = null;
        }
        try {
            if (TextUtils.isEmpty(ad.getAdCallToAction())) {
                str = null;
            } else {
                str = ad.getAdCallToAction();
            }
            obj7 = str;
        } catch (Exception e6) {
            obj7 = null;
        }
        try {
            str = getRawFBAdV480(ad);
            if (!TextUtils.isEmpty(str)) {
                obj = str;
            }
        } catch (Exception e7) {
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("operation", operation);
            jSONObject.put("ad_id", ad.getId());
            jSONObject.put("icon_url", obj2);
            jSONObject.put("cover_url", obj3);
            jSONObject.put("title", obj4);
            jSONObject.put("body", obj5);
            jSONObject.put("social_context", obj6);
            jSONObject.put("call2action", obj7);
            jSONObject.put("fbad", obj);
        } catch (JSONException e8) {
            e8.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static String getRawFBAd(NativeAd ad) {
        String str = "";
        try {
            String uri;
            Field declaredField = ad.getClass().getSuperclass().getDeclaredField("adDataModel");
            declaredField.setAccessible(true);
            Field declaredField2 = declaredField.get(ad).getClass().getDeclaredField("b");
            declaredField2.setAccessible(true);
            Object obj = declaredField2.get(declaredField.get(ad));
            if (obj instanceof Uri) {
                uri = ((Uri) obj).toString();
            } else {
                uri = str;
            }
            return uri;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return str;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return str;
        }
    }

    public static String getRawFBAdV470(NativeAd ad) {
        String str = "";
        try {
            Field declaredField = ad.getClass().getDeclaredField("j");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(ad);
            if (obj instanceof k) {
                k kVar = (k) obj;
                Field declaredField2 = kVar.getClass().getDeclaredField("b");
                declaredField2.setAccessible(true);
                obj = declaredField2.get(kVar);
                if (obj instanceof n) {
                    n nVar = (n) obj;
                    declaredField2 = nVar.getClass().getDeclaredField("b");
                    declaredField2.setAccessible(true);
                    obj = declaredField2.get(nVar);
                    if (obj instanceof Uri) {
                        return ((Uri) obj).toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return str;
    }

    public static String getRawFBAdV480(NativeAd ad) {
        String str = "";
        try {
            Field declaredField = ad.getClass().getDeclaredField("k");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(ad);
            if (obj instanceof j) {
                j jVar = (j) obj;
                Field declaredField2 = jVar.getClass().getDeclaredField("c");
                declaredField2.setAccessible(true);
                obj = declaredField2.get(jVar);
                if (obj instanceof Uri) {
                    return ((Uri) obj).toString();
                }
            }
        } catch (Exception e) {
        }
        return str;
    }

    public static String getFBReportPkg(String typeName) {
        String str = "";
        if (typeName.equals(Const.KEY_FB_L)) {
            str = "low";
        } else if (typeName.equals(Const.KEY_FB_B)) {
            str = "balance";
        } else if (typeName.equals(Const.KEY_FB_H)) {
            str = "hight";
        }
        return String.format("%s.%s", new Object[]{"com.facebook.ad", str});
    }
}
