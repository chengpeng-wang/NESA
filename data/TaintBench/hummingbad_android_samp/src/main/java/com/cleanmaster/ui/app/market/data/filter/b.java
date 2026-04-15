package com.cleanmaster.ui.app.market.data.filter;

import com.cleanmaster.data.filter.a;
import com.cleanmaster.data.filter.c;
import com.cleanmaster.data.filter.d;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: BaseAdFilter */
public class b implements com.cleanmaster.data.filter.b {
    public static d a = new d();
    public static a b = new a();
    protected int c = -1;
    protected int d = -1;

    public static com.cleanmaster.data.filter.b a(String str) {
        ArrayList arrayList = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(str);
            a(arrayList, c.b(jSONObject));
            a(arrayList, d.b(jSONObject));
            a(arrayList, f.b(jSONObject));
            a(arrayList, h.b(jSONObject));
            a(arrayList, g.b(jSONObject));
            a(arrayList, e.c(jSONObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList.isEmpty() ? a : a(arrayList);
    }

    public final boolean a(a aVar) {
        return aVar == null ? true : b(aVar);
    }

    /* access modifiers changed from: protected */
    public boolean b(a aVar) {
        return true;
    }

    public b a(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.c = jSONObject.optInt("lt", -1);
            this.d = jSONObject.optInt("gt", -1);
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean a(int i) {
        if (this.c != -1 && i > this.c) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean b(int i) {
        if (this.d != -1 && i < this.d) {
            return false;
        }
        return true;
    }

    private static com.cleanmaster.data.filter.b a(ArrayList arrayList) {
        if (arrayList.size() == 1) {
            return (com.cleanmaster.data.filter.b) arrayList.get(0);
        }
        c cVar = new c((com.cleanmaster.data.filter.b) arrayList.remove(0), (com.cleanmaster.data.filter.b) arrayList.remove(0));
        Iterator it = arrayList.iterator();
        while (true) {
            com.cleanmaster.data.filter.b bVar = cVar;
            if (!it.hasNext()) {
                return bVar;
            }
            cVar = new c(bVar, (com.cleanmaster.data.filter.b) it.next());
        }
    }

    private static void a(ArrayList arrayList, b bVar) {
        if (bVar != null) {
            arrayList.add(bVar);
        }
    }

    public String toString() {
        return String.format("%s", new Object[]{getClass().getSimpleName()});
    }

    public boolean a(Set set, Collection collection) {
        if (set == null || set.isEmpty() || collection == null || collection.isEmpty()) {
            return false;
        }
        for (String contains : collection) {
            if (set.contains(contains)) {
                return true;
            }
        }
        return false;
    }
}
