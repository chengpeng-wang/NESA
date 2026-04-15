package u.aly;

import android.content.Context;
import com.umeng.analytics.a;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* compiled from: IdTracker */
public class d {
    private final String a = "umeng_it.cache";
    private File b;
    private S c = null;
    private long d;
    private long e;
    private Set<a> f = new HashSet();

    public d(Context context) {
        this.b = new File(context.getFilesDir(), "umeng_it.cache");
        this.e = a.m;
    }

    public void a(a aVar) {
        this.f.add(aVar);
    }

    public void a(long j) {
        this.e = j;
    }

    public void a() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.d >= this.e) {
            Object obj = null;
            for (a aVar : this.f) {
                if (!aVar.c()) {
                    obj = 1;
                } else if (aVar.a()) {
                    obj = 1;
                }
            }
            if (obj != null) {
                g();
                f();
            }
            this.d = currentTimeMillis;
        }
    }

    public S b() {
        return this.c;
    }

    private void g() {
        S s = new S();
        Map hashMap = new HashMap();
        List arrayList = new ArrayList();
        for (a aVar : this.f) {
            if (aVar.c()) {
                if (aVar.d() != null) {
                    hashMap.put(aVar.b(), aVar.d());
                }
                if (!(aVar.e() == null || aVar.e().isEmpty())) {
                    arrayList.addAll(aVar.e());
                }
            }
        }
        s.a(arrayList);
        s.a(hashMap);
        synchronized (this) {
            this.c = s;
        }
    }

    public String c() {
        return null;
    }

    public void d() {
        boolean z = false;
        for (a aVar : this.f) {
            if (!(!aVar.c() || aVar.e() == null || aVar.e().isEmpty())) {
                aVar.a(null);
                z = true;
            }
        }
        if (z) {
            this.c.b(false);
            f();
        }
    }

    public void e() {
        S h = h();
        if (h != null) {
            ArrayList<a> arrayList = new ArrayList(this.f.size());
            synchronized (this) {
                this.c = h;
                for (a aVar : this.f) {
                    aVar.a(this.c);
                    if (!aVar.c()) {
                        arrayList.add(aVar);
                    }
                }
                for (a aVar2 : arrayList) {
                    this.f.remove(aVar2);
                }
            }
            g();
        }
    }

    public void f() {
        if (this.c != null) {
            a(this.c);
        }
    }

    private S h() {
        Exception e;
        Throwable th;
        if (!this.b.exists()) {
            return null;
        }
        InputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(this.b);
            try {
                byte[] b = av.b(fileInputStream);
                az s = new S();
                new aC().a(s, b);
                av.c(fileInputStream);
                return s;
            } catch (Exception e2) {
                e = e2;
                try {
                    e.printStackTrace();
                    av.c(fileInputStream);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    av.c(fileInputStream);
                    throw th;
                }
            }
        } catch (Exception e3) {
            e = e3;
            fileInputStream = null;
            e.printStackTrace();
            av.c(fileInputStream);
            return null;
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            av.c(fileInputStream);
            throw th;
        }
    }

    private void a(S s) {
        if (s != null) {
            try {
                byte[] a;
                synchronized (this) {
                    a = new aI().a(s);
                }
                if (a != null) {
                    av.a(this.b, a);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
