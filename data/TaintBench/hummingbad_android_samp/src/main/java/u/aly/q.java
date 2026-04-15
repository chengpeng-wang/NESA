package u.aly;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.Gender;
import com.umeng.analytics.a;
import java.util.ArrayList;
import java.util.List;

/* compiled from: MemoCache */
public class q {
    private List<p> a = new ArrayList();
    private H b = null;
    private I c = null;
    private K d = null;
    private X e = null;
    private Context f = null;

    public q(Context context) {
        this.f = context;
    }

    public Context a() {
        return this.f;
    }

    /* access modifiers changed from: protected */
    public boolean a(int i) {
        return true;
    }

    public synchronized int b() {
        int size;
        size = this.a.size();
        if (this.b != null) {
            size++;
        }
        return size;
    }

    public synchronized void a(p pVar) {
        this.a.add(pVar);
    }

    public void a(af afVar) {
        String i = i();
        if (i != null) {
            synchronized (this) {
                for (p a : this.a) {
                    a.a(afVar, i);
                }
                this.a.clear();
                if (this.b != null) {
                    afVar.a(this.b);
                    this.b = null;
                }
            }
            afVar.a(c());
            afVar.a(d());
            afVar.a(e());
            afVar.a(h());
            afVar.a(f());
            afVar.a(g());
        }
    }

    private String i() {
        return u.a(this.f).getString("session_id", null);
    }

    public synchronized void a(H h) {
        this.b = h;
    }

    public synchronized I c() {
        if (this.c == null) {
            this.c = new I();
            a(this.f);
        }
        return this.c;
    }

    public synchronized K d() {
        if (this.d == null) {
            this.d = new K();
            b(this.f);
        }
        return this.d;
    }

    public synchronized X e() {
        if (this.e == null) {
            this.e = new X();
            c(this.f);
        }
        return this.e;
    }

    public T f() {
        try {
            return h.b(this.f).a();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public S g() {
        try {
            return h.a(this.f).b();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public J h() {
        try {
            return w.a(this.f);
        } catch (Exception e) {
            e.printStackTrace();
            return new J();
        }
    }

    private void a(Context context) {
        try {
            this.c.a(AnalyticsConfig.getAppkey(context));
            this.c.e(AnalyticsConfig.getChannel(context));
            if (!(AnalyticsConfig.mWrapperType == null || AnalyticsConfig.mWrapperVersion == null)) {
                this.c.f(AnalyticsConfig.mWrapperType);
                this.c.g(AnalyticsConfig.mWrapperVersion);
            }
            this.c.c(ai.u(context));
            this.c.a(ac.ANDROID);
            this.c.d(a.c);
            this.c.b(ai.d(context));
            this.c.a(Integer.parseInt(ai.c(context)));
            if (AnalyticsConfig.mVerticalType == 1) {
                this.c.c(AnalyticsConfig.mVerticalType);
                this.c.d(a.d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void b(Context context) {
        try {
            this.d.f(ai.a());
            this.d.a(ai.f(context));
            this.d.b(ai.g(context));
            this.d.c(ai.p(context));
            this.d.e(Build.MODEL);
            this.d.g("Android");
            this.d.h(VERSION.RELEASE);
            int[] r = ai.r(context);
            if (r != null) {
                this.d.a(new aa(r[1], r[0]));
            }
            if (AnalyticsConfig.GPU_RENDERER != null) {
                String str = AnalyticsConfig.GPU_VENDER;
            }
            this.d.i(Build.BOARD);
            this.d.j(Build.BRAND);
            this.d.a(Build.TIME);
            this.d.k(Build.MANUFACTURER);
            this.d.l(Build.ID);
            this.d.m(Build.DEVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void c(Context context) {
        try {
            String[] j = ai.j(context);
            if (ai.d.equals(j[0])) {
                this.e.a(G.ACCESS_TYPE_WIFI);
            } else if (ai.c.equals(j[0])) {
                this.e.a(G.ACCESS_TYPE_2G_3G);
            } else {
                this.e.a(G.ACCESS_TYPE_UNKNOWN);
            }
            if (!"".equals(j[1])) {
                this.e.e(j[1]);
            }
            this.e.c(ai.s(context));
            j = ai.n(context);
            this.e.b(j[0]);
            this.e.a(j[1]);
            this.e.a(ai.m(context));
            if (AnalyticsConfig.sAge != 0 || AnalyticsConfig.sGender != null || AnalyticsConfig.sId != null || AnalyticsConfig.sSource != null) {
                ag agVar = new ag();
                agVar.a(AnalyticsConfig.sAge);
                agVar.a(Gender.transGender(AnalyticsConfig.sGender));
                agVar.a(AnalyticsConfig.sId);
                agVar.b(AnalyticsConfig.sSource);
                this.e.a(agVar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
