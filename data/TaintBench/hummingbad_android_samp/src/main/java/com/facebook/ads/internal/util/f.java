package com.facebook.ads.internal.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import org.json.JSONArray;

public class f {

    public interface a {
        e a();

        String b();

        Collection<String> c();
    }

    public static Collection<String> a(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() == 0) {
            return null;
        }
        HashSet hashSet = new HashSet();
        for (int i = 0; i < jSONArray.length(); i++) {
            hashSet.add(jSONArray.optString(i));
        }
        return hashSet;
    }

    public static boolean a(Context context, a aVar) {
        e a = aVar.a();
        if (a == null || a == e.NONE) {
            return false;
        }
        Collection<String> c = aVar.c();
        if (c == null || c.isEmpty()) {
            return false;
        }
        boolean z;
        for (String a2 : c) {
            if (a(context, a2)) {
                z = true;
                break;
            }
        }
        z = false;
        if (z != (a == e.INSTALLED)) {
            return false;
        }
        if (r.a(aVar.b())) {
            return true;
        }
        new o().execute(new String[]{aVar.b()});
        return false;
    }

    public static boolean a(Context context, String str) {
        if (r.a(str)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
}
