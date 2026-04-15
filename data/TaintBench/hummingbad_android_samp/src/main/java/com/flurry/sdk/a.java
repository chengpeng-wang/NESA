package com.flurry.sdk;

import android.text.TextUtils;
import java.util.Map;
import java.util.Map.Entry;

public class a {
    private static final String d = a.class.getSimpleName();
    public final de a;
    final Map b;
    public final b c;

    public a(de deVar, Map map, b bVar) {
        this.a = deVar;
        this.b = map;
        this.c = bVar;
    }

    public static de b(String str) {
        for (de deVar : de.values()) {
            if (deVar.toString().equals(str)) {
                iw.a(5, d, "Action Type for name: " + str + " is " + deVar);
                return deVar;
            }
        }
        return de.AC_UNKNOWN;
    }

    public final String a(String str) {
        return (this.b == null || TextUtils.isEmpty(str)) ? null : (String) this.b.get(str);
    }

    public final String a(String str, String str2) {
        return (this.b == null || TextUtils.isEmpty(str)) ? null : (String) this.b.put(str, str2);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("action=");
        stringBuilder.append(this.a.toString());
        stringBuilder.append(",params=");
        for (Entry entry : this.b.entrySet()) {
            stringBuilder.append(",key=").append((String) entry.getKey()).append(",value=").append((String) entry.getValue());
        }
        stringBuilder.append(",");
        stringBuilder.append(",fTriggeringEvent=");
        stringBuilder.append(this.c);
        return stringBuilder.toString();
    }
}
