package u.aly;

import android.content.Context;
import android.text.TextUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.ReportPolicy;
import com.umeng.analytics.ReportPolicy.b;
import com.umeng.analytics.ReportPolicy.d;
import com.umeng.analytics.ReportPolicy.e;
import com.umeng.analytics.a;
import com.umeng.analytics.f;
import com.umeng.analytics.onlineconfig.c;

/* compiled from: CacheImpl */
public final class j implements c, o {
    private q a = null;
    private r b = null;
    private e c = null;
    private f d = null;
    private w e = null;
    private d f = null;
    private int g = 10;
    private Context h;

    public j(Context context) {
        this.h = context;
        this.a = new q(context);
        this.f = h.a(context);
        this.e = new w(context);
        this.b = new r(context);
        this.b.a(this.e);
        this.d = f.a(context);
        int[] reportPolicy = AnalyticsConfig.getReportPolicy(this.h);
        a(reportPolicy[0], reportPolicy[1]);
    }

    public void a() {
        if (ai.l(this.h)) {
            e();
        } else if (aj.a) {
            aj.c(a.e, "network is unavailable");
        }
    }

    public void a(p pVar) {
        if (pVar != null) {
            this.a.a(pVar);
        }
        if (a(pVar instanceof ad)) {
            e();
        } else if (d()) {
            b();
        }
    }

    public void b(p pVar) {
        this.a.a(pVar);
    }

    public void b() {
        if (this.a.b() > 0) {
            try {
                byte[] c = c();
                if (c != null) {
                    this.d.a(c);
                }
            } catch (Throwable th) {
                if (th instanceof OutOfMemoryError) {
                    this.d.c();
                }
                if (th != null) {
                    th.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public byte[] c() {
        try {
            if (TextUtils.isEmpty(AnalyticsConfig.getAppkey(this.h))) {
                aj.b(a.e, "Appkey is missing ,Please check AndroidManifest.xml");
                return null;
            }
            byte[] b = f.a(this.h).b();
            af a = b == null ? null : a(b);
            if (a == null && this.a.b() == 0) {
                return null;
            }
            af afVar;
            if (a == null) {
                afVar = new af();
            } else {
                afVar = a;
            }
            this.a.a(afVar);
            if (aj.a && afVar.B()) {
                Object obj = null;
                for (ad p : afVar.z()) {
                    if (p.p() > 0) {
                        obj = 1;
                    }
                }
                if (obj == null) {
                    aj.e(a.e, "missing Activities or PageViews");
                }
            }
            try {
                b = a(afVar);
                try {
                    if (!aj.a) {
                        return b;
                    }
                    aj.c(a.e, afVar.toString());
                    return b;
                } catch (Exception e) {
                    aj.b(a.e, "Fail to serialize log ...");
                    return b;
                }
            } catch (Exception e2) {
                b = null;
            }
        } catch (Exception e3) {
            aj.b(a.e, "Fail to construct message ...", e3);
            f.a(this.h).c();
            return null;
        }
    }

    private af a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            az afVar = new af();
            new aC().a(afVar, bArr);
            return afVar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] a(af afVar) {
        try {
            return new aI().a(afVar);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean a(boolean z) {
        if (!ai.l(this.h)) {
            if (aj.a) {
                aj.c(a.e, "network is unavailable");
            }
            return false;
        } else if (this.e.b()) {
            return true;
        } else {
            if (aj.a && ai.w(this.h)) {
                return true;
            }
            return this.c.a(z);
        }
    }

    private boolean d() {
        return this.a.b() > this.g;
    }

    private void e() {
        try {
            if (this.e.b()) {
                this.a.a(new H(this.e.j()));
            }
            f();
        } catch (Throwable th) {
            boolean z = th instanceof OutOfMemoryError;
            if (th != null) {
                th.printStackTrace();
            }
        }
    }

    private void f() {
        byte[] d;
        f a = f.a(this.h);
        boolean f = a.f();
        if (f) {
            d = a.d();
        } else {
            this.f.a();
            d = c();
            if (d == null) {
                aj.e(a.e, "message is null");
                return;
            } else {
                d = c.a(this.h, AnalyticsConfig.getAppkey(this.h), d).c();
                a.c();
            }
        }
        switch (this.b.a(d)) {
            case 1:
                if (!f) {
                    a.b(d);
                }
                aj.b(a.e, "connection error");
                return;
            case 2:
                if (this.e.i()) {
                    this.e.h();
                }
                this.f.d();
                this.e.g();
                if (f) {
                    a.e();
                    return;
                }
                return;
            case 3:
                this.e.g();
                if (f) {
                    a.e();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void a(int i, int i2) {
        switch (i) {
            case 0:
                this.c = new e();
                break;
            case 1:
                this.c = new ReportPolicy.a();
                break;
            case 4:
                this.c = new d(this.e);
                break;
            case 5:
                this.c = new ReportPolicy.f(this.h);
                break;
            case 6:
                this.c = new b(this.e, (long) i2);
                break;
            case 7:
                this.c = new ReportPolicy.c(this.a, i2);
                break;
            default:
                this.c = new ReportPolicy.a();
                break;
        }
        aj.c(a.e, "report policy:" + i + " interval:" + i2);
    }

    public void a(int i, long j) {
        AnalyticsConfig.setReportPolicy(i, (int) j);
        a(i, (int) j);
    }
}
