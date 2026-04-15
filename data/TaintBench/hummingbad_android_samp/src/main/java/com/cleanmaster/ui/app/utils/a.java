package com.cleanmaster.ui.app.utils;

import android.os.Build.VERSION;
import android.text.TextUtils;
import com.cleanmaster.ui.app.market.c;
import com.cleanmaster.ui.app.market.h;
import com.cleanmaster.ui.app.market.j;
import com.cleanmaster.ui.app.market.loader.AsyncTaskEx.Status;
import com.cleanmaster.util.m;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/* compiled from: ParseUrlManager */
public class a {
    private static volatile a a;
    private LinkedList b = new LinkedList();
    /* access modifiers changed from: private */
    public HashMap c = new HashMap();
    private Object d = new Object();
    private m e = new m(100);

    public static a a() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a();
                }
            }
        }
        return a;
    }

    protected a() {
    }

    public void a(int i, String str, String str2, String str3, String str4) {
        if (i >= 0 && !TextUtils.isEmpty(str) && VERSION.SDK_INT > 10 && !c.a(str)) {
            synchronized (this.d) {
                if (d(str)) {
                    return;
                }
                if (b(str)) {
                    c c = c(str);
                    if (c != null) {
                        this.b.remove(c);
                        this.b.addFirst(c);
                    }
                } else {
                    if (this.b.size() >= 50) {
                        this.b.removeLast();
                    }
                    this.b.addFirst(new c(this, i, str, str2, str3, str4));
                }
                b();
            }
        }
    }

    public String a(String str) {
        String str2;
        synchronized (this.d) {
            str2 = (String) this.e.a((Object) str);
        }
        return str2;
    }

    public void a(int i) {
        synchronized (this.d) {
            LinkedList linkedList = new LinkedList();
            Iterator it = this.b.iterator();
            while (it.hasNext()) {
                c cVar = (c) it.next();
                if (cVar.a == i) {
                    linkedList.add(cVar);
                }
            }
            if (!linkedList.isEmpty()) {
                this.b.removeAll(linkedList);
            }
            if (this.c != null && this.c.size() > 0) {
                for (String str : this.c.keySet()) {
                    j a = ((h) this.c.get(str)).a();
                    if (!(a == null || a.c() != Status.RUNNING || a.f())) {
                        a.a(true);
                    }
                }
                this.c.clear();
            }
        }
    }

    private boolean b(String str) {
        Iterator it = this.b.iterator();
        while (it.hasNext()) {
            if (((c) it.next()).b.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private c c(String str) {
        Iterator it = this.b.iterator();
        while (it.hasNext()) {
            c cVar = (c) it.next();
            if (cVar.b.equals(str)) {
                return cVar;
            }
        }
        return null;
    }

    private boolean d(String str) {
        if (this.e.a((Object) str) == null && !this.c.containsKey(str)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void a(String str, String str2) {
        synchronized (this.d) {
            this.e.b(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void b() {
        while (this.c.size() < 2 && !this.b.isEmpty()) {
            c cVar = (c) this.b.removeFirst();
            String str = cVar.b;
            h hVar = new h();
            hVar.a(new b(this, str));
            this.c.put(str, hVar);
            hVar.a(str, cVar.c, cVar.d, cVar.e);
        }
    }
}
