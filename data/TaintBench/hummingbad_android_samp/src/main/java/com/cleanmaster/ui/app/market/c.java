package com.cleanmaster.ui.app.market;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.cleanmaster.func.cache.a;
import com.cleanmaster.model.b;
import com.cleanmaster.service.LocalService;
import com.cleanmaster.ui.app.utils.MarketContext;
import com.cleanmaster.util.m;
import com.cleanmaster.util.r;
import com.cleanmaster.util.v;
import com.picksbrowser.PicksBrowser;
import com.picksinit.ClickAdFinishListener;
import com.picksinit.SmartGoGp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: MarketUtils */
public class c {
    static m a = new d(8);
    private static Map b;

    public static void a(List list) {
        if (list != null) {
            HashMap hashMap = new HashMap();
            List b = a.a().b();
            if (b != null) {
                for (int i = 0; i < b.size(); i++) {
                    PackageInfo packageInfo = (PackageInfo) b.get(i);
                    hashMap.put(packageInfo.packageName, packageInfo);
                }
            }
            for (int i2 = 0; i2 < list.size(); i2++) {
                Ad ad = (Ad) list.get(i2);
                PackageInfo packageInfo2 = (PackageInfo) hashMap.get(ad.getPkg());
                if (packageInfo2 == null) {
                    ad.installedStatus = 0;
                } else if (ad.versionCode < 0 || ad.versionCode <= packageInfo2.versionCode) {
                    ad.installedStatus = 1;
                } else {
                    ad.installedStatus = 2;
                }
            }
        }
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (str.startsWith("https://play.google.com") || str.startsWith("http://play.google.com") || str.startsWith("market:")) {
            return true;
        }
        return false;
    }

    public static void a(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            if (a(str)) {
                com.cleanmaster.common.a.b(str, context);
                return;
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            com.cleanmaster.common.a.a(context, intent);
        }
    }

    public static boolean b(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        List queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 65536);
        if (queryIntentActivities == null || queryIntentActivities.size() <= 0) {
            return false;
        }
        intent.setFlags(268435456);
        a(context, intent);
        return true;
    }

    private static boolean a(Context context, com.picksinit.a aVar, String str) {
        if (aVar != null) {
            if (aVar.b() != null && !TextUtils.isEmpty(aVar.c())) {
                if (TextUtils.isEmpty(aVar.a())) {
                    PicksBrowser.a(context, str, aVar.b(), aVar.c());
                } else {
                    PicksBrowser.a(context, aVar.a(), aVar.b(), aVar.c());
                }
                return true;
            } else if (!TextUtils.isEmpty(aVar.a())) {
                PicksBrowser.a(context, aVar.a());
                return true;
            }
        }
        PicksBrowser.a(context, str);
        return true;
    }

    private static boolean a(Context context, Intent intent) {
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static {
        b = null;
        b = new ConcurrentHashMap();
    }

    private static String b(String str) {
        return (String) a.a((Object) str);
    }

    public static void a(Ad ad, String str, String str2) {
        if (ad != null && !TextUtils.isEmpty(str)) {
            b a;
            if (TextUtils.isEmpty(str2)) {
                a = b.a(str);
            } else {
                a = b.a(str).e(str2);
            }
            b.a(a, c(ad));
            com.cleanmaster.model.a toBuinessDataItem = ad.toBuinessDataItem();
            com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
            cVar.a(toBuinessDataItem, a);
            cVar.c((Object[]) new Void[0]);
        }
    }

    public static void a(String str, Ad ad, String str2) {
        if (ad != null && !TextUtils.isEmpty(str)) {
            b b;
            if (TextUtils.isEmpty(str2)) {
                b = b.b(str);
            } else {
                b = b.b(str).e(str2);
            }
            b.a(b, c(ad));
            com.cleanmaster.model.a toBuinessDataItem = ad.toBuinessDataItem();
            com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
            cVar.a(toBuinessDataItem, b);
            cVar.c((Object[]) new Void[0]);
        }
    }

    private static int c(Ad ad) {
        if (ad == null || !com.cleanmaster.common.a.b(com.picksinit.c.getInstance().getContext(), ad.getPkg()) || !ad.isDeepLink()) {
            return 0;
        }
        if (TextUtils.isEmpty(ad.getDeepLink())) {
            return 1;
        }
        return 2;
    }

    public static void b(String str, Ad ad, String str2) {
        if (ad != null && !TextUtils.isEmpty(str)) {
            b d;
            if (TextUtils.isEmpty(str2)) {
                d = b.d(str);
            } else {
                d = b.d(str).e(str2);
            }
            com.cleanmaster.model.a toBuinessDataItem = ad.toBuinessDataItem();
            com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
            cVar.a(toBuinessDataItem, d);
            cVar.c((Object[]) new Void[0]);
        }
    }

    public static void c(String str, Ad ad, String str2) {
        if (ad != null) {
            b a = b.a(str, str2);
            com.cleanmaster.model.a toBuinessDataItem = ad.toBuinessDataItem();
            com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
            cVar.a(toBuinessDataItem, a);
            cVar.c((Object[]) new Void[0]);
        }
    }

    public static void d(String str, Ad ad, String str2) {
        if (ad != null) {
            b b = b.b(str, str2);
            com.cleanmaster.model.a toBuinessDataItem = ad.toBuinessDataItem();
            com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
            cVar.a(toBuinessDataItem, b);
            cVar.c((Object[]) new Void[0]);
        }
    }

    public static void e(String str, Ad ad, String str2) {
        if (ad != null && !TextUtils.isEmpty(str)) {
            b c;
            if (TextUtils.isEmpty(str2)) {
                c = b.c(str);
            } else {
                c = b.c(str).e(str2);
            }
            com.cleanmaster.model.a toBuinessDataItem = ad.toBuinessDataItem();
            com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
            cVar.a(toBuinessDataItem, c);
            cVar.c((Object[]) new Void[0]);
        }
    }

    public static void a(Ad ad, String str, String str2, int i) {
        if (ad != null && !TextUtils.isEmpty(str)) {
            b a = b.a(str, str2, i);
            com.cleanmaster.model.a toBuinessDataItem = ad.toBuinessDataItem();
            r rVar = new r();
            rVar.a(toBuinessDataItem, a);
            rVar.c((Object[]) new Void[0]);
        }
    }

    public static void a(Context context, String str, Ad ad, String str2, boolean z, ClickAdFinishListener clickAdFinishListener) {
        if (context != null) {
            Context marketContext = new MarketContext(context);
            if (ad.isDeepLink() && com.cleanmaster.common.a.b(marketContext, ad.getPkg())) {
                com.cleanmaster.ui.app.market.deeplink.a.a(marketContext, ad);
                if (clickAdFinishListener != null) {
                    clickAdFinishListener.onClickFinish(null);
                }
            } else if (com.cleanmaster.common.a.b(marketContext, ad.getPkg())) {
                com.cleanmaster.common.a.d(marketContext, ad.getPkg());
                if (clickAdFinishListener != null) {
                    clickAdFinishListener.onClickFinish(null);
                }
            } else if (ad.isOpenBrowser()) {
                b(marketContext, ad.getPkgUrl());
                if (clickAdFinishListener != null) {
                    clickAdFinishListener.onClickFinish(null);
                }
            } else if (ad.isOpenInternal()) {
                a(marketContext, com.picksinit.c.getInstance().getBrowserConfig(), ad.getPkgUrl());
                if (clickAdFinishListener != null) {
                    clickAdFinishListener.onClickFinish(null);
                }
            } else {
                a(marketContext, ad.getPkgUrl(), clickAdFinishListener, ad, str);
                LocalService.a(marketContext, ad.getPkg());
            }
            if (z || !ad.hasDetail()) {
                a(str, ad, str2);
            } else {
                e(str, ad, str2);
            }
            a(ad);
        }
    }

    public static void a(Ad ad) {
        if (!TextUtils.isEmpty(ad.getClickTrackingUrl()) && !"null".equals(ad.getClickTrackingUrl())) {
            try {
                JSONArray jSONArray = new JSONArray(ad.getClickTrackingUrl());
                if (jSONArray != null && jSONArray.length() > 0) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        if (jSONObject != null) {
                            v.a(new e(jSONObject));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public static void b(Ad ad) {
        if (!TextUtils.isEmpty(ad.getThirdImpUrl()) && !"null".equals(ad.getThirdImpUrl())) {
            try {
                JSONArray jSONArray = new JSONArray(ad.getThirdImpUrl());
                if (jSONArray != null && jSONArray.length() > 0) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        if (jSONObject != null) {
                            v.a(new f(jSONObject));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public static boolean a(Context context, String str, ClickAdFinishListener clickAdFinishListener, Ad ad, String str2) {
        if (TextUtils.isEmpty(str) || context == null) {
            return false;
        }
        if (!a(str)) {
            String a = com.cleanmaster.ui.app.utils.a.a().a(str);
            if (TextUtils.isEmpty(a)) {
                a = b(str);
            }
            if (TextUtils.isEmpty(a)) {
                h hVar = new h();
                hVar.a(new g(ad, str2, clickAdFinishListener, context, str));
                hVar.b(str);
                return true;
            } else if (clickAdFinishListener != null) {
                clickAdFinishListener.onClickFinish(new SmartGoGp(a));
                return true;
            } else {
                a(context, a);
                return true;
            }
        } else if (clickAdFinishListener != null) {
            clickAdFinishListener.onClickFinish(new SmartGoGp(str));
            return false;
        } else {
            a(context, str);
            return false;
        }
    }

    public static void a(String str, String str2, int i) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            Ad createAd = Ad.createAd(str);
            if (createAd != null && !TextUtils.isEmpty(str2)) {
                b a = b.a(str2);
                createAd.setResType(i);
                com.cleanmaster.model.a toBuinessDataItem = createAd.toBuinessDataItem();
                com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
                cVar.a(toBuinessDataItem, a);
                cVar.c((Object[]) new Void[0]);
            }
        }
    }

    public static void b(String str, String str2, int i) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            Ad createAd = Ad.createAd(str);
            if (createAd != null && !TextUtils.isEmpty(str2)) {
                b b = b.b(str2);
                createAd.setResType(i);
                com.cleanmaster.model.a toBuinessDataItem = createAd.toBuinessDataItem();
                com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
                cVar.a(toBuinessDataItem, b);
                cVar.c((Object[]) new Void[0]);
            }
        }
    }

    public static void a(String str, String str2, String str3, String str4, int i) {
        if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4)) {
            Ad createAd = Ad.createAd(str3);
            if (createAd != null) {
                b a = b.a(str4);
                createAd.setResType(i);
                com.cleanmaster.model.a toBuinessDataItem = createAd.toBuinessDataItem();
                toBuinessDataItem.a(str, str2);
                com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
                cVar.a(toBuinessDataItem, a);
                cVar.c((Object[]) new Void[0]);
            }
        }
    }

    public static void b(String str, String str2, String str3, String str4, int i) {
        if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4)) {
            Ad createAd = Ad.createAd(str3);
            if (createAd != null) {
                b b = b.b(str4);
                createAd.setResType(i);
                com.cleanmaster.model.a toBuinessDataItem = createAd.toBuinessDataItem();
                toBuinessDataItem.a(str, str2);
                com.cleanmaster.util.c cVar = new com.cleanmaster.util.c();
                cVar.a(toBuinessDataItem, b);
                cVar.c((Object[]) new Void[0]);
            }
        }
    }

    public static boolean a() {
        return com.picksinit.c.getInstance().isCnVersion();
    }
}
