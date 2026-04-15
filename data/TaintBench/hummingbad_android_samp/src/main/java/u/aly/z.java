package u.aly;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import com.umeng.analytics.a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/* compiled from: ViewPageTracker */
public class z {
    private static final String a = "activities";
    private final Map<String, Long> b = new HashMap();
    private final ArrayList<x> c = new ArrayList();

    public void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.b) {
                this.b.put(str, Long.valueOf(System.currentTimeMillis()));
            }
        }
    }

    public void b(String str) {
        if (!TextUtils.isEmpty(str)) {
            Long l;
            synchronized (this.b) {
                l = (Long) this.b.remove(str);
            }
            if (l == null) {
                aj.e(a.e, String.format("please call 'onPageStart(%s)' before onPageEnd", new Object[]{str}));
                return;
            }
            long currentTimeMillis = System.currentTimeMillis() - l.longValue();
            synchronized (this.c) {
                this.c.add(new x(str, currentTimeMillis));
            }
        }
    }

    public void a() {
        String str;
        synchronized (this.b) {
            str = null;
            long j = 0;
            for (Entry entry : this.b.entrySet()) {
                if (((Long) entry.getValue()).longValue() > j) {
                    str = (String) entry.getKey();
                    j = ((Long) entry.getValue()).longValue();
                }
            }
        }
        if (str != null) {
            b(str);
        }
    }

    public void a(Context context) {
        SharedPreferences a = u.a(context);
        Editor edit = a.edit();
        if (this.c.size() > 0) {
            String string = a.getString(a, "");
            StringBuilder stringBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(string)) {
                stringBuilder.append(string);
                stringBuilder.append(";");
            }
            synchronized (this.c) {
                Iterator it = this.c.iterator();
                while (it.hasNext()) {
                    x xVar = (x) it.next();
                    stringBuilder.append(String.format("[\"%s\",%d]", new Object[]{xVar.a, Long.valueOf(xVar.b)}));
                    stringBuilder.append(";");
                }
                this.c.clear();
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            edit.remove(a);
            edit.putString(a, stringBuilder.toString());
        }
        edit.commit();
    }

    public static List<Y> a(SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString(a, "");
        if ("".equals(string)) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        try {
            String[] split = string.split(";");
            for (String str : split) {
                if (!TextUtils.isEmpty(str)) {
                    arrayList.add(new E(str));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (arrayList.size() > 0) {
            return arrayList;
        }
        return null;
    }
}
