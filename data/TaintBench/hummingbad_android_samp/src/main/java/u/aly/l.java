package u.aly;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* compiled from: Caretaker */
public class l {
    private final String a = "umeng_event_snapshot";
    private boolean b = false;
    private SharedPreferences c;
    private Map<String, ArrayList<A>> d = new HashMap();

    public l(Context context) {
        this.c = u.a(context, "umeng_event_snapshot");
    }

    public void a(boolean z) {
        this.b = z;
    }

    public int a(String str) {
        if (this.d.containsKey(str)) {
            return ((ArrayList) this.d.get(str)).size();
        }
        return 0;
    }

    public void a(String str, A a) {
        if (this.b) {
            d(str);
        }
        if (this.d.containsKey(str)) {
            ((ArrayList) this.d.get(str)).add(a);
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.add(a);
            this.d.put(str, arrayList);
        }
        if (this.b) {
            c(str);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002e  */
    public u.aly.A b(java.lang.String r4) {
        /*
        r3 = this;
        r0 = r3.b;
        if (r0 == 0) goto L_0x0007;
    L_0x0004:
        r3.d(r4);
    L_0x0007:
        r1 = 0;
        r0 = r3.d;
        r0 = r0.containsKey(r4);
        if (r0 == 0) goto L_0x0032;
    L_0x0010:
        r0 = r3.d;
        r0 = r0.get(r4);
        r0 = (java.util.ArrayList) r0;
        r2 = r0.size();
        if (r2 <= 0) goto L_0x0032;
    L_0x001e:
        r1 = r0.size();
        r1 = r1 + -1;
        r0 = r0.remove(r1);
        r0 = (u.aly.A) r0;
    L_0x002a:
        r1 = r3.b;
        if (r1 == 0) goto L_0x0031;
    L_0x002e:
        r3.c(r4);
    L_0x0031:
        return r0;
    L_0x0032:
        r0 = r1;
        goto L_0x002a;
        */
        throw new UnsupportedOperationException("Method not decompiled: u.aly.l.b(java.lang.String):u.aly.A");
    }

    private void c(String str) {
        String str2 = null;
        if (this.d.containsKey(str)) {
            Serializable serializable = (ArrayList) this.d.get(str);
            while (serializable.size() > 4) {
                serializable.remove(0);
            }
            str2 = s.a(serializable);
        }
        this.c.edit().putString(str, str2).commit();
    }

    private boolean d(String str) {
        if (this.d.containsKey(str)) {
            return true;
        }
        String string = this.c.getString(str, null);
        if (string != null) {
            ArrayList arrayList = (ArrayList) s.a(string);
            if (arrayList != null) {
                this.d.put(str, arrayList);
                return true;
            }
        }
        return false;
    }
}
