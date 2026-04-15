package com.duapps.ad.c.a;

import android.content.Context;
import java.util.HashMap;

/* compiled from: PolicyConfig */
public class a {
    private static a a;
    private Context b;
    private HashMap<Integer, b> c = new HashMap();

    private a(Context context) {
        this.b = context;
    }

    public static a a(Context context) {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a(context);
                }
            }
        }
        return a;
    }

    public final b a(int i) {
        return a(i, false);
    }

    public final b a(int i, boolean z) {
        b bVar;
        Integer valueOf = Integer.valueOf(i);
        synchronized (this.c) {
            if (this.c.containsKey(valueOf)) {
                bVar = (b) this.c.get(valueOf);
            } else {
                bVar = b.a(this.b, i, z);
                synchronized (this.c) {
                    this.c.put(valueOf, bVar);
                }
            }
        }
        return bVar;
    }
}
