package com.umeng.analytics.game;

import android.content.Context;
import com.mb.bdapp.db.DuAd;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.VastIconXmlManager;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.Gender;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.a;
import com.umeng.analytics.b;
import com.umeng.analytics.d;
import com.umeng.analytics.e;
import java.util.HashMap;
import u.aly.aj;

/* compiled from: InternalGameAgent */
class c implements b {
    /* access modifiers changed from: private */
    public com.umeng.analytics.c a = MobclickAgent.getAgent();
    /* access modifiers changed from: private */
    public b b = null;
    private final int c = 100;
    private final int d = 1;
    private final int e;
    private final int f;
    private final int g;
    private final String h;
    private final String i;
    private final String j;
    private final String k;
    private final String l;
    private final String m;
    private final String n;
    private final String o;
    private final String p;
    private final String q;
    private final String r;
    private final String s;
    private final String t;
    private final String u;
    private final String v;
    private final String w;
    /* access modifiers changed from: private */
    public Context x;

    public c() {
        this.a.a(1);
        this.e = 0;
        this.f = -1;
        this.g = 1;
        this.h = "level";
        this.i = "pay";
        this.j = "buy";
        this.k = "use";
        this.l = "bonus";
        this.m = "item";
        this.n = "cash";
        this.o = "coin";
        this.p = MASTNativeAdConstants.RESPONSE_MEDIATION_SOURCE;
        this.q = "amount";
        this.r = "user_level";
        this.s = "bonus_source";
        this.t = "level";
        this.u = DuAd.DB_STATUS;
        this.v = VastIconXmlManager.DURATION;
        this.w = "UMGameAgent.init(Context) should be called before any game api";
        a.a = true;
    }

    /* access modifiers changed from: 0000 */
    public void a(Context context) {
        if (context == null) {
            aj.b(a.e, "Context is null, can't init GameAgent");
            return;
        }
        this.x = context.getApplicationContext();
        this.a.a((b) this);
        this.b = new b(this.x);
    }

    /* access modifiers changed from: 0000 */
    public void a(boolean z) {
        aj.c(a.e, String.format("Trace sleep time : %b", new Object[]{Boolean.valueOf(z)}));
        a.a = z;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str, int i, Gender gender, String str2) {
        AnalyticsConfig.sId = str;
        AnalyticsConfig.sAge = i;
        AnalyticsConfig.sGender = gender;
        AnalyticsConfig.sSource = str2;
    }

    /* access modifiers changed from: 0000 */
    public void a(String str) {
        this.b.b = str;
    }

    /* access modifiers changed from: 0000 */
    public void b(final String str) {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        this.b.a = str;
        d.a(new e() {
            public void a() {
                c.this.b.a(str);
                HashMap hashMap = new HashMap();
                hashMap.put("level", str);
                hashMap.put(DuAd.DB_STATUS, Integer.valueOf(0));
                if (c.this.b.b != null) {
                    hashMap.put("user_level", c.this.b.b);
                }
                c.this.a.a(c.this.x, "level", hashMap);
            }
        });
    }

    private void a(final String str, final int i) {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
        } else {
            d.a(new e() {
                public void a() {
                    a b = c.this.b.b(str);
                    if (b != null) {
                        long e = b.e();
                        if (e <= 0) {
                            aj.c(a.e, "level duration is 0");
                            return;
                        }
                        HashMap hashMap = new HashMap();
                        hashMap.put("level", str);
                        hashMap.put(DuAd.DB_STATUS, Integer.valueOf(i));
                        hashMap.put(VastIconXmlManager.DURATION, Long.valueOf(e));
                        if (c.this.b.b != null) {
                            hashMap.put("user_level", c.this.b.b);
                        }
                        c.this.a.a(c.this.x, "level", hashMap);
                        return;
                    }
                    aj.e(a.e, String.format("finishLevel(or failLevel) called before startLevel", new Object[0]));
                }
            });
        }
    }

    /* access modifiers changed from: 0000 */
    public void c(String str) {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
        } else {
            a(str, 1);
        }
    }

    /* access modifiers changed from: 0000 */
    public void d(String str) {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
        } else {
            a(str, -1);
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(double d, double d2, int i) {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("cash", Long.valueOf((long) (d * 100.0d)));
        hashMap.put("coin", Long.valueOf((long) (d2 * 100.0d)));
        hashMap.put(MASTNativeAdConstants.RESPONSE_MEDIATION_SOURCE, Integer.valueOf(i));
        if (this.b.b != null) {
            hashMap.put("user_level", this.b.b);
        }
        if (this.b.a != null) {
            hashMap.put("level", this.b.a);
        }
        this.a.a(this.x, "pay", hashMap);
    }

    /* access modifiers changed from: 0000 */
    public void a(double d, String str, int i, double d2, int i2) {
        a(d, d2 * ((double) i), i2);
        a(str, i, d2);
    }

    /* access modifiers changed from: 0000 */
    public void a(String str, int i, double d) {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("item", str);
        hashMap.put("amount", Integer.valueOf(i));
        hashMap.put("coin", Long.valueOf((long) ((((double) i) * d) * 100.0d)));
        if (this.b.b != null) {
            hashMap.put("user_level", this.b.b);
        }
        if (this.b.a != null) {
            hashMap.put("level", this.b.a);
        }
        this.a.a(this.x, "buy", hashMap);
    }

    /* access modifiers changed from: 0000 */
    public void b(String str, int i, double d) {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("item", str);
        hashMap.put("amount", Integer.valueOf(i));
        hashMap.put("coin", Long.valueOf((long) ((((double) i) * d) * 100.0d)));
        if (this.b.b != null) {
            hashMap.put("user_level", this.b.b);
        }
        if (this.b.a != null) {
            hashMap.put("level", this.b.a);
        }
        this.a.a(this.x, "use", hashMap);
    }

    /* access modifiers changed from: 0000 */
    public void a(double d, int i) {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("coin", Long.valueOf((long) (100.0d * d)));
        hashMap.put("bonus_source", Integer.valueOf(i));
        if (this.b.b != null) {
            hashMap.put("user_level", this.b.b);
        }
        if (this.b.a != null) {
            hashMap.put("level", this.b.a);
        }
        this.a.a(this.x, "bonus", hashMap);
    }

    /* access modifiers changed from: 0000 */
    public void a(String str, int i, double d, int i2) {
        a(((double) i) * d, i2);
        a(str, i, d);
    }

    public void a() {
        aj.c(a.e, "App resume from background");
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
        } else if (a.a) {
            this.b.b();
        }
    }

    public void b() {
        if (this.x == null) {
            aj.b(a.e, "UMGameAgent.init(Context) should be called before any game api");
        } else if (a.a) {
            this.b.a();
        }
    }
}
