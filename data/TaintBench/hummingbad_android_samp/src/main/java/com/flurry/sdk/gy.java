package com.flurry.sdk;

import java.util.Collections;
import java.util.Map.Entry;

public class gy extends jb {
    private static final String a = gy.class.getSimpleName();

    public final String a(a aVar, String str) {
        String a = a(str);
        while (a != null) {
            String replace;
            b bVar = aVar.c;
            ct ctVar = aVar.c.e;
            cz czVar = (cz) ctVar.c.get(ctVar.a());
            dv dvVar = aVar.c.e.a;
            if (jb.a("fids", a)) {
                StringBuilder stringBuilder = new StringBuilder();
                int i = 1;
                for (Entry entry : Collections.unmodifiableMap(hi.a().a).entrySet()) {
                    if (i == 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(((hv) entry.getKey()).d).append(":");
                    if (((hv) entry.getKey()).e) {
                        stringBuilder.append(new String((byte[]) entry.getValue()));
                        i = 0;
                    } else {
                        stringBuilder.append(lt.a((byte[]) entry.getValue()));
                        i = 0;
                    }
                }
                iw.a(3, a, "Replacing param fids with: " + stringBuilder.toString());
                replace = str.replace(a, lt.b(stringBuilder.toString()));
            } else if (jb.a("sid", a)) {
                replace = String.valueOf(hg.a().a);
                iw.a(3, a, "Replacing param sid with: " + replace);
                replace = str.replace(a, lt.b(replace));
            } else if (jb.a("lid", a)) {
                replace = String.valueOf(czVar.a);
                iw.a(3, a, "Replacing param lid with: " + replace);
                replace = str.replace(a, lt.b(replace));
            } else if (jb.a("guid", a)) {
                replace = czVar.c;
                iw.a(3, a, "Replacing param guid with: " + replace);
                replace = str.replace(a, lt.b(replace));
            } else if (jb.a("ats", a)) {
                replace = String.valueOf(System.currentTimeMillis());
                iw.a(3, a, "Replacing param ats with: " + replace);
                replace = str.replace(a, lt.b(replace));
            } else if (jb.a("apik", a)) {
                replace = hz.a.d;
                iw.a(3, a, "Replacing param apik with: " + replace);
                replace = str.replace(a, lt.b(replace));
            } else if (jb.a("hid", a)) {
                replace = dvVar.b;
                iw.a(3, a, "Replacing param hid with: " + replace);
                replace = str.replace(a, lt.b(replace));
            } else if (jb.a("eso", a)) {
                replace = Long.toString(System.currentTimeMillis() - hg.a().a);
                iw.a(3, a, "Replacing param eso with: " + replace);
                replace = str.replace(a, lt.b(replace));
            } else if (jb.a("uc", a)) {
                String str2 = "";
                iw.a(3, a, "Replacing param uc with: " + str2);
                replace = str.replace(a, str2);
                if (str2.equals("") && replace.length() > 0) {
                    replace = replace.substring(0, replace.length() - 1);
                }
            } else {
                iw.a(3, a, "Unknown param: " + a);
                replace = str.replace(a, "");
            }
            a = a(replace);
            str = replace;
        }
        return str;
    }
}
