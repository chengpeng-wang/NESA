package com.duapps.ad.b;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.duapps.ad.b.g.a;
import com.duapps.ad.base.f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

/* compiled from: InMobiDataQueue */
public class j implements Callback, a {
    private static j f;
    /* access modifiers changed from: private */
    public ArrayList<n> a;
    private ArrayList<a> b;
    private ArrayList<g> c;
    private ExecutorService d;
    /* access modifiers changed from: private */
    public Context e;
    /* access modifiers changed from: private|volatile */
    public volatile boolean g = false;
    private Handler h;
    private Handler i = new k(this, Looper.getMainLooper());

    static /* synthetic */ void a(j jVar, n nVar) {
        if (jVar.d != null && jVar.c.size() > 0) {
            g gVar = (g) jVar.c.get(0);
            nVar.c = true;
            gVar.c = nVar;
            jVar.d.execute(gVar);
        }
    }

    public static j a(Context context) {
        synchronized (j.class) {
            if (f == null) {
                f = new j(context.getApplicationContext());
            }
        }
        return f;
    }

    private j(Context context) {
        this.e = context;
        this.a = new ArrayList();
        this.i.sendEmptyMessage(100);
        a();
        this.d = Executors.newFixedThreadPool(3);
        this.c = new ArrayList();
        HandlerThread handlerThread = new HandlerThread("inmobiNative", 10);
        handlerThread.start();
        this.h = new Handler(handlerThread.getLooper(), this);
        this.h.sendEmptyMessageDelayed(1, 10800000);
        c();
    }

    private void a() {
        String string = this.e.getSharedPreferences("im_cache_prefs", 0).getString("im_cache_prefs_array", null);
        this.b = new ArrayList();
        if (string != null) {
            ArrayList a = a(string);
            if (a != null) {
                f.c("InMobiDataQueue", "arrayList size:" + a.size());
                Iterator it = a.iterator();
                while (it.hasNext()) {
                    this.b.add((a) it.next());
                }
            }
        }
    }

    private void b() {
        Editor edit = this.e.getSharedPreferences("im_cache_prefs", 0).edit();
        String a = a(this.b);
        if (!TextUtils.isEmpty(a)) {
            edit.putString("im_cache_prefs_array", a);
            edit.apply();
        }
    }

    private void c() {
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        Iterator it = this.b.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (currentTimeMillis - aVar.r < 10800000) {
                arrayList.add(aVar);
            }
        }
        this.b.clear();
        this.b = null;
        this.b = arrayList;
        b();
    }

    private synchronized void a(a aVar, a.a aVar2) {
        Object obj;
        g gVar;
        Object obj2;
        if (aVar2 == a.a.Impression && aVar.s) {
            obj = 1;
        } else if (aVar2 == a.a.Click && aVar.t) {
            int obj3 = 1;
        } else {
            Iterator it = this.c.iterator();
            obj3 = null;
            while (it.hasNext()) {
                gVar = (g) it.next();
                obj2 = (gVar.a.q.equals(aVar.q) && gVar.b == aVar2) ? 1 : obj3;
                obj3 = obj2;
            }
        }
        if (obj3 != null) {
            f.c("InMobiDataQueue", "returning:" + aVar.q);
        } else {
            n nVar = null;
            Iterator it2 = this.a.iterator();
            while (it2.hasNext()) {
                n nVar2 = (n) it2.next();
                if (!nVar2.c) {
                    nVar = nVar2;
                    break;
                }
            }
            gVar = new g(this.e, nVar, aVar, aVar2, this);
            this.c.add(gVar);
            if (nVar != null) {
                this.d.execute(gVar);
                obj2 = null;
            } else {
                int i = 1;
            }
            if (obj2 != null && this.c.size() == 1) {
                this.g = true;
            }
        }
    }

    private synchronized void b(a aVar, a.a aVar2) {
        if (aVar != null) {
            if (!(TextUtils.isEmpty(aVar.q) || TextUtils.isEmpty(aVar.p))) {
                a aVar3;
                Iterator it = this.b.iterator();
                while (it.hasNext()) {
                    aVar3 = (a) it.next();
                    if (aVar3.q.equals(aVar.q)) {
                        break;
                    }
                }
                aVar3 = null;
                if (aVar3 == null) {
                    aVar3 = new a(aVar.q, aVar.p, aVar.r);
                    this.b.add(aVar3);
                }
                a(aVar3, aVar2);
            }
        }
    }

    public final void a(a aVar) {
        if (aVar == null || TextUtils.isEmpty(aVar.q) || TextUtils.isEmpty(aVar.p)) {
            f.c("InMobiDataQueue", "IMData is null or namespace is null or contextCode is null !");
        } else {
            b(aVar, a.a.Impression);
        }
    }

    public final void b(a aVar) {
        if (aVar == null || TextUtils.isEmpty(aVar.q) || TextUtils.isEmpty(aVar.p)) {
            f.c("InMobiDataQueue", "IMData is null or namespace is null or contextCode is null !");
        } else {
            b(aVar, a.a.Click);
        }
    }

    public final void a(g gVar) {
        a aVar;
        g gVar2;
        boolean z = false;
        this.c.remove(gVar);
        Iterator it = this.b.iterator();
        int i = 0;
        while (it.hasNext()) {
            aVar = (a) it.next();
            if (gVar.a.q.equals(aVar.q)) {
                break;
            }
            i++;
        }
        aVar = null;
        if (aVar != null) {
            if (gVar.b == a.a.Impression) {
                aVar.s = true;
            } else if (gVar.b == a.a.Click) {
                aVar.t = true;
            }
            this.b.set(i, aVar);
            b();
        }
        Iterator it2 = this.c.iterator();
        while (it2.hasNext()) {
            g gVar3 = (g) it2.next();
            if (gVar3.c != null) {
                if (!gVar3.c.c) {
                }
            }
            gVar2 = gVar3;
        }
        gVar2 = null;
        if (gVar2 != null && gVar2 != gVar) {
            if (System.currentTimeMillis() - gVar2.a.r < 10800000) {
                z = true;
            }
            if (z) {
                gVar2.c = (n) this.a.get(gVar.c.b);
                this.d.execute(gVar2);
                return;
            }
            this.c.remove(gVar2);
        }
    }

    private static String a(List<a> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        JSONStringer jSONStringer = new JSONStringer();
        try {
            jSONStringer.array();
            for (int i = 0; i < list.size(); i++) {
                a aVar = (a) list.get(i);
                jSONStringer.object();
                jSONStringer.key("namespace").value(aVar.q);
                jSONStringer.key("contextCode").value(aVar.q);
                jSONStringer.key("ts").value(aVar.r);
                jSONStringer.endObject();
            }
            jSONStringer.endArray();
            return jSONStringer.toString();
        } catch (JSONException e) {
            f.c("InMobiDataQueue", "toJson exectpion=" + e.toString());
            return null;
        }
    }

    private static ArrayList<a> a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList<a> arrayList = new ArrayList();
        try {
            JSONArray jSONArray = new JSONArray(str);
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                arrayList.add(new a(jSONObject.getString("namespace"), jSONObject.getString("contextCode"), jSONObject.getLong("ts")));
            }
            return arrayList;
        } catch (JSONException e) {
            f.c("InMobiDataQueue", "fromJson exectpion=" + e.toString());
            return null;
        }
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 1:
                this.h.removeMessages(1);
                c();
                this.h.sendEmptyMessageDelayed(1, 10800000);
                return true;
            default:
                return false;
        }
    }
}
