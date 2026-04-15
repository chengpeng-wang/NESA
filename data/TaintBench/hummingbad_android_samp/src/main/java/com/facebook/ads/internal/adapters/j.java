package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd.Image;
import com.facebook.ads.NativeAd.Rating;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.internal.extra.AdExtras;
import com.facebook.ads.internal.util.b;
import com.facebook.ads.internal.util.c;
import com.facebook.ads.internal.util.e;
import com.facebook.ads.internal.util.f;
import com.facebook.ads.internal.util.g;
import com.facebook.ads.internal.util.o;
import com.facebook.ads.internal.util.r;
import com.mb.bdapp.db.DuAd;
import com.mopub.mobileads.BaseVideoPlayerActivity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class j extends n implements com.facebook.ads.internal.util.f.a {
    private static final String a = j.class.getSimpleName();
    private String A;
    private String B;
    private Image C;
    private String D;
    private String E;
    private a F;
    private AdExtras G;
    private NativeAdViewAttributes H;
    private boolean I;
    private boolean J;
    private boolean K;
    private boolean L;
    private boolean M;
    private long N = 0;
    private com.facebook.ads.internal.util.b.a O = null;
    private Context b;
    private Uri c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private Image i;
    private Image j;
    private Rating k;
    private String l;
    /* access modifiers changed from: private */
    public String m;
    private String n;
    private String o;
    private e p;
    private String q;
    private Collection<String> r;
    private boolean s;
    private boolean t;
    private boolean u;
    private int v;
    private int w;
    private int x;
    private int y;
    private String z;

    public static class a {
        Map<String, List<String>> a = new HashMap();

        a(JSONArray jSONArray) {
            if (jSONArray != null) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    String optString = optJSONObject.optString("type");
                    if (!r.a(optString)) {
                        JSONArray optJSONArray = optJSONObject.optJSONArray("urls");
                        if (optJSONArray != null) {
                            ArrayList arrayList = new ArrayList(optJSONArray.length());
                            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                                arrayList.add(optJSONArray.optString(i2));
                            }
                            this.a.put(optString, arrayList);
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void a(String str) {
            List<String> list = (List) this.a.get(str);
            if (list != null && !list.isEmpty()) {
                for (String str2 : list) {
                    new o().execute(new String[]{str2});
                }
            }
        }
    }

    private boolean A() {
        return (this.d == null || this.d.length() <= 0 || this.g == null || this.g.length() <= 0 || this.i == null || this.j == null) ? false : true;
    }

    private void B() {
        if (!this.M) {
            new o().execute(new String[]{this.o});
            this.M = true;
        }
    }

    private void a(String str, Map<String, String> map, Map<String, Object> map2) {
        if (map2.containsKey(str)) {
            map.put(str, String.valueOf(map2.get(str)));
        }
    }

    private void a(Map<String, String> map, Map<String, Object> map2) {
        if (map2.containsKey("mil")) {
            boolean booleanValue = ((Boolean) map2.get("mil")).booleanValue();
            map2.remove("mil");
            if (!booleanValue) {
                return;
            }
        }
        map.put("mil", String.valueOf(true));
    }

    private void b(Map<String, String> map, Map<String, Object> map2) {
        a("nti", (Map) map, (Map) map2);
        a("nhs", (Map) map, (Map) map2);
        a("nmv", (Map) map, (Map) map2);
    }

    private Map<String, String> c(Map<String, Object> map) {
        HashMap hashMap = new HashMap();
        if (map.containsKey("view")) {
            hashMap.put("view", String.valueOf(map.get("view")));
        }
        if (map.containsKey("snapshot")) {
            hashMap.put("snapshot", String.valueOf(map.get("snapshot")));
        }
        return hashMap;
    }

    public e a() {
        return this.p;
    }

    public void a(int i) {
        if (z() && i == 0 && this.N > 0 && this.O != null) {
            c.a(b.a(this.N, this.O, this.E));
            this.N = 0;
            this.O = null;
        }
    }

    public void a(Context context, o oVar, Map<String, Object> map) {
        a((JSONObject) map.get("data"));
        if (f.a(context, (com.facebook.ads.internal.util.f.a) this)) {
            oVar.a(this, AdError.NO_FILL);
            return;
        }
        this.b = context;
        if (oVar != null) {
            oVar.a(this);
        }
        b.a = this.E;
    }

    public void a(Map<String, Object> map) {
        if (z() && !this.K) {
            final HashMap hashMap = new HashMap();
            if (map != null) {
                a(hashMap, map);
                b(hashMap, map);
            }
            new o(hashMap).execute(new String[]{this.l});
            if (e() || g()) {
                try {
                    final Map c = c(map);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            new o(hashMap, c).execute(new String[]{j.this.m});
                        }
                    }, (long) (this.v * 1000));
                } catch (Exception e) {
                }
            }
            this.F.a("impression");
            this.K = true;
        }
    }

    public void a(JSONObject jSONObject) {
        JSONArray jSONArray = null;
        if (this.I) {
            throw new IllegalStateException("Adapter already loaded data");
        } else if (jSONObject != null) {
            this.c = Uri.parse(jSONObject.optString("fbad_command"));
            this.d = jSONObject.optString("title");
            this.e = jSONObject.optString("subtitle");
            this.f = jSONObject.optString("body");
            this.g = jSONObject.optString("call_to_action");
            this.h = jSONObject.optString("social_context");
            this.i = Image.fromJSONObject(jSONObject.optJSONObject(DuAd.DB_ICON));
            this.j = Image.fromJSONObject(jSONObject.optJSONObject("image"));
            this.k = Rating.fromJSONObject(jSONObject.optJSONObject("star_rating"));
            this.l = jSONObject.optString("impression_report_url");
            this.m = jSONObject.optString("native_view_report_url");
            this.n = jSONObject.optString("click_report_url");
            this.o = jSONObject.optString("used_report_url");
            this.G = new AdExtras().setIsOrganic(jSONObject.optBoolean("is_organic", false));
            this.s = jSONObject.optBoolean("manual_imp");
            this.t = jSONObject.optBoolean("enable_view_log");
            this.u = jSONObject.optBoolean("enable_snapshot_log");
            this.v = jSONObject.optInt("snapshot_log_delay_second", 4);
            this.w = jSONObject.optInt("snapshot_compress_quality", 0);
            this.x = jSONObject.optInt("viewability_check_initial_delay", 0);
            this.y = jSONObject.optInt("viewability_check_interval", 1000);
            JSONObject optJSONObject = jSONObject.optJSONObject("ad_choices_icon");
            JSONObject optJSONObject2 = jSONObject.optJSONObject("native_ui_config");
            this.H = optJSONObject2 == null ? null : new NativeAdViewAttributes(optJSONObject2);
            if (optJSONObject != null) {
                this.C = Image.fromJSONObject(optJSONObject);
            }
            this.D = jSONObject.optString("ad_choices_link_url");
            this.E = jSONObject.optString("request_id");
            this.p = e.a(jSONObject.optString("invalidation_behavior"));
            this.q = jSONObject.optString("invalidation_report_url");
            try {
                jSONArray = new JSONArray(jSONObject.optString("detection_strings"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.r = f.a(jSONArray);
            this.F = new a(jSONObject.optJSONArray("trackers"));
            this.z = jSONObject.optString(BaseVideoPlayerActivity.VIDEO_URL);
            this.A = jSONObject.optString("video_play_report_url");
            this.B = jSONObject.optString("video_time_report_url");
            this.I = true;
            this.J = A();
        }
    }

    public String b() {
        return this.q;
    }

    public void b(Map<String, Object> map) {
        if (z()) {
            if (!this.L) {
                HashMap hashMap = new HashMap();
                if (map != null) {
                    a(hashMap, map);
                    b(hashMap, map);
                    hashMap.put("touch", g.a((Map) map));
                }
                new o(hashMap).execute(new String[]{this.n});
                this.F.a("click");
                this.L = true;
                g.a(this.b, "Click logged");
            }
            com.facebook.ads.internal.action.a a = com.facebook.ads.internal.action.b.a(this.b, this.c);
            if (a != null) {
                try {
                    this.N = System.currentTimeMillis();
                    this.O = a.a();
                    a.b();
                } catch (Exception e) {
                    Log.e(a, "Error executing action", e);
                }
            }
        }
    }

    public Collection<String> c() {
        return this.r;
    }

    public boolean d() {
        return z() && this.s;
    }

    public boolean e() {
        return z() && this.t;
    }

    public boolean f() {
        return z() && this.H != null;
    }

    public boolean g() {
        return z() && this.u;
    }

    public int h() {
        return (this.w < 0 || this.w > 100) ? 0 : this.w;
    }

    public int i() {
        return this.x;
    }

    public int j() {
        return this.y;
    }

    public Image k() {
        return !z() ? null : this.i;
    }

    public Image l() {
        return !z() ? null : this.j;
    }

    public NativeAdViewAttributes m() {
        return !z() ? null : this.H;
    }

    public String n() {
        if (!z()) {
            return null;
        }
        B();
        return this.d;
    }

    public String o() {
        if (!z()) {
            return null;
        }
        B();
        return this.e;
    }

    public void onDestroy() {
    }

    public String p() {
        if (!z()) {
            return null;
        }
        B();
        return this.f;
    }

    public String q() {
        if (!z()) {
            return null;
        }
        B();
        return this.g;
    }

    public String r() {
        if (!z()) {
            return null;
        }
        B();
        return this.h;
    }

    public Rating s() {
        if (!z()) {
            return null;
        }
        B();
        return this.k;
    }

    public Image t() {
        return !z() ? null : this.C;
    }

    public String u() {
        return !z() ? null : this.D;
    }

    public String v() {
        return !z() ? null : this.z;
    }

    public String w() {
        return !z() ? null : this.A;
    }

    public String x() {
        return !z() ? null : this.B;
    }

    public AdExtras y() {
        return !z() ? null : this.G;
    }

    public boolean z() {
        return this.I && this.J;
    }
}
