package u.aly;

import android.content.Context;
import com.umeng.analytics.d;
import com.umeng.analytics.e;
import com.umeng.analytics.onlineconfig.a;
import com.umeng.analytics.onlineconfig.c;

/* compiled from: CacheService */
public final class k implements o {
    private static k c;
    /* access modifiers changed from: private */
    public o a = new j(this.b);
    private Context b;

    private k(Context context) {
        this.b = context.getApplicationContext();
    }

    public static k a(Context context) {
        if (c == null && context != null) {
            c = new k(context);
        }
        return c;
    }

    public void a(o oVar) {
        this.a = oVar;
    }

    public void a(a aVar) {
        if (aVar != null && this.a != null) {
            aVar.a((c) this.a);
        }
    }

    public void a(final p pVar) {
        d.b(new e() {
            public void a() {
                k.this.a.a(pVar);
            }
        });
    }

    public void b(p pVar) {
        this.a.b(pVar);
    }

    public void a() {
        d.b(new e() {
            public void a() {
                k.this.a.a();
            }
        });
    }

    public void b() {
        d.b(new e() {
            public void a() {
                k.this.a.b();
            }
        });
    }
}
