package com.cleanmaster.ui.app.market;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.cmcm.adsdk.nativead.CMNativeAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.picksinit.PicksConfig;
import com.picksinit.c;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: MarketConfig */
public class b {
    private static int a;
    private static Map b;

    public static int a() {
        if (a < 1800) {
            int e = e(g(CMNativeAd.KEY_CACHE_TIME));
            a = e;
            if (e < 1800) {
                a = 3600;
            }
        }
        return a * 1000;
    }

    public static int b() {
        return e(g("req_timeout_ms"));
    }

    private static int e(String str) {
        if (TextUtils.isEmpty(str) || !TextUtils.isDigitsOnly(str)) {
            return -1;
        }
        return Integer.parseInt(str);
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            a(MASTNativeAdConstants.RESPONSE_ERROR_CODE, jSONObject.optString(MASTNativeAdConstants.RESPONSE_ERROR_CODE));
            a("adn", jSONObject.optString("adn"));
            a("banner_interval_ms", jSONObject.optString("cm_softer_banner_interval_ms"));
            String optString = jSONObject.optString("cm_softer_cache_time", "");
            a = e(optString);
            a(CMNativeAd.KEY_CACHE_TIME, optString);
            a("request_url", jSONObject.optString("request_url"));
            a("req_timeout_ms", jSONObject.optString("req_timeout_ms"));
            a("recommend_cache_time", jSONObject.optInt("recommend_cache_time"));
            a("recommend_view_frequency", jSONObject.optInt("recommend_view_frequency"));
            a("gamebox_per_query_count", jSONObject.optInt("gamebox_per_query_count", 15));
            a("sessionid_timeout_days", jSONObject.optInt("sessionid_timeout_days", 2));
            a("render_report_ratio", jSONObject.optInt("render_report_ratio"));
            optString = jSONObject.optString("https_request_url", "");
            if (!TextUtils.isEmpty(optString)) {
                URI uri = new URI(optString);
                String scheme = uri.getScheme();
                String host = uri.getHost();
                if (!TextUtils.isEmpty(scheme)) {
                    a("scheme", scheme);
                }
                if (!TextUtils.isEmpty(host)) {
                    a("host", host);
                }
                a("https_request_url", optString);
            }
            optString = jSONObject.optString("https_report_url", "");
            if (!TextUtils.isEmpty(optString)) {
                a("https_report_url", optString);
            }
            JSONArray jSONArray = jSONObject.getJSONArray("pos_cache");
            if (b == null) {
                b = new HashMap();
            }
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                long j = jSONObject2.getLong("posid");
                long j2 = jSONObject2.getLong(CMNativeAd.KEY_CACHE_TIME);
                b.put(Long.valueOf(j), Long.valueOf(j2));
                b(String.valueOf(j) + "_posid_expire_time", j2);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String c() {
        return c.getInstance().getContext().getSharedPreferences("market_config", 0).getString("scheme", "");
    }

    public static String d() {
        return c.getInstance().getContext().getSharedPreferences("market_config", 0).getString("host", "");
    }

    public static String e() {
        return c.getInstance().getContext().getSharedPreferences("market_config", 0).getString("https_report_url", "");
    }

    public static long a(Long l) {
        if (b == null) {
            return 0;
        }
        Long l2 = (Long) b.get(l);
        if (l2 == null) {
            return 0;
        }
        return l2.longValue() * 1000;
    }

    public static void a(long j, long j2) {
        if (b == null) {
            b = new HashMap();
        }
        if (a(Long.valueOf(j)) == 0) {
            b.put(Long.valueOf(j), Long.valueOf(j2));
            b(String.valueOf(j) + "_posid_expire_time", j2);
        }
    }

    private static void b(String str, long j) {
        a(c.getInstance().getContext().getSharedPreferences("market_config", 0).edit().putLong(str, j));
    }

    private static void a(String str, int i) {
        c.getInstance().getContext().getSharedPreferences("market_config", 0).edit().putInt(str, i).commit();
    }

    private static void a(String str, String str2) {
        c.getInstance().getContext().getSharedPreferences("market_config", 0).edit().putString(str, str2).commit();
    }

    private static int f(String str) {
        return c.getInstance().getContext().getSharedPreferences("market_config", 0).getInt(str, -1);
    }

    private static String g(String str) {
        return c.getInstance().getContext().getSharedPreferences("market_config", 0).getString(str, "");
    }

    public static long b(String str) {
        PicksConfig config = c.getInstance().getConfig();
        if (config != null && config.enable_debug && config.cache_time > 0) {
            return (long) (config.cache_time * 1000);
        }
        return c.getInstance().getContext().getSharedPreferences("market_config", 0).getLong(str + "_posid_expire_time", 0) * 1000;
    }

    public static boolean a(String str, long j) {
        SharedPreferences sharedPreferences = c.getInstance().getContext().getSharedPreferences("market_config", 0);
        if (System.currentTimeMillis() - sharedPreferences.getLong(str, 0) <= j) {
            return false;
        }
        if (VERSION.SDK_INT < 9) {
            sharedPreferences.edit().putLong(str, System.currentTimeMillis()).commit();
        } else {
            sharedPreferences.edit().putLong(str, System.currentTimeMillis()).apply();
        }
        return true;
    }

    public static void a(Editor editor) {
        if (VERSION.SDK_INT > 8) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static String f() {
        return c.getInstance().getContext().getSharedPreferences("market_request_total_time", 0).getString("total_time", "");
    }

    public static void c(String str) {
        c.getInstance().getContext().getSharedPreferences("market_request_total_time", 0).edit().putString("total_time", str).commit();
    }

    public static int g() {
        int f = f("render_report_ratio");
        if (f == 0) {
            return 100;
        }
        return f;
    }

    public static String h() {
        return c.getInstance().getContext().getSharedPreferences("market_request_offer_id", 0).getString("offer_id", "");
    }

    public static void d(String str) {
        c.getInstance().getContext().getSharedPreferences("market_request_offer_id", 0).edit().putString("offer_id", str).commit();
    }

    public static void a(boolean z) {
        c.getInstance().getContext().getSharedPreferences("market_request_download_service", 0).edit().putBoolean("is_resume", z).commit();
    }
}
