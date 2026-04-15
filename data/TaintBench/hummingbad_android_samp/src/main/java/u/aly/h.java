package u.aly;

import android.content.Context;

/* compiled from: SDKContext */
public class h {
    private static d a = null;
    private static f b = null;

    public static synchronized d a(Context context) {
        d dVar;
        synchronized (h.class) {
            if (a == null) {
                a = new d(context);
                a.a(new e(context));
                a.a(new g(context));
                a.a(new b(context));
                a.a(new i(context));
                a.e();
            }
            dVar = a;
        }
        return dVar;
    }

    public static synchronized f b(Context context) {
        f fVar;
        synchronized (h.class) {
            if (b == null) {
                b = new f(context);
                b.b();
            }
            fVar = b;
        }
        return fVar;
    }
}
