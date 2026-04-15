package u.aly;

import android.support.v4.os.EnvironmentCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* compiled from: AbstractIdTracker */
public abstract class a {
    private final int a = 10;
    private final int b = 20;
    private final String c;
    private List<Q> d;
    private R e;

    public abstract String f();

    public a(String str) {
        this.c = str;
    }

    public boolean a() {
        return g();
    }

    public String b() {
        return this.c;
    }

    public boolean c() {
        if (this.e == null || this.e.j() <= 20) {
            return true;
        }
        return false;
    }

    private boolean g() {
        R r = this.e;
        String c = r == null ? null : r.c();
        int j = r == null ? 0 : r.j();
        String a = a(f());
        if (a == null || a.equals(c)) {
            return false;
        }
        if (r == null) {
            r = new R();
        }
        r.a(a);
        r.a(System.currentTimeMillis());
        r.a(j + 1);
        Q q = new Q();
        q.a(this.c);
        q.c(a);
        q.b(c);
        q.a(r.f());
        if (this.d == null) {
            this.d = new ArrayList(2);
        }
        this.d.add(q);
        if (this.d.size() > 10) {
            this.d.remove(0);
        }
        this.e = r;
        return true;
    }

    public R d() {
        return this.e;
    }

    public void a(R r) {
        this.e = r;
    }

    public List<Q> e() {
        return this.d;
    }

    public void a(List<Q> list) {
        this.d = list;
    }

    public String a(String str) {
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        if (trim.length() == 0 || "0".equals(trim) || EnvironmentCompat.MEDIA_UNKNOWN.equals(trim.toLowerCase(Locale.US))) {
            return null;
        }
        return trim;
    }

    public void a(S s) {
        this.e = (R) s.d().get("mName");
        List<Q> j = s.j();
        if (j != null && j.size() > 0) {
            if (this.d == null) {
                this.d = new ArrayList();
            }
            for (Q q : j) {
                if (this.c.equals(q.a)) {
                    this.d.add(q);
                }
            }
        }
    }
}
