package com.facebook.ads.internal.util;

import android.os.AsyncTask;
import android.util.Log;
import com.facebook.ads.internal.thirdparty.http.a;
import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.resource.DrawableConstants.CtaButton;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class o extends AsyncTask<String, Void, Void> {
    private static final String a = o.class.getSimpleName();
    private static final Set<String> b;
    private Map<String, String> c;
    private Map<String, String> d;

    static {
        HashSet hashSet = new HashSet();
        b = hashSet;
        hashSet.add("#");
        b.add("null");
    }

    public o() {
        this(null, null);
    }

    public o(Map<String, String> map) {
        this(map, null);
    }

    public o(Map<String, String> map, Map<String, String> map2) {
        this.c = map;
        this.d = map2;
    }

    private String a(String str, String str2, String str3) {
        if (r.a(str) || r.a(str2) || r.a(str3)) {
            return str;
        }
        return str + (str.contains(MASTNativeAdConstants.QUESTIONMARK) ? MASTNativeAdConstants.AMPERSAND : MASTNativeAdConstants.QUESTIONMARK) + str2 + MASTNativeAdConstants.EQUAL + URLEncoder.encode(str3);
    }

    private boolean a(String str) {
        a c = g.c();
        try {
            if (this.d == null || this.d.size() == 0) {
                return c.a(str, null).a() == CtaButton.WIDTH_DIPS;
            } else {
                com.facebook.ads.internal.thirdparty.http.o oVar = new com.facebook.ads.internal.thirdparty.http.o();
                oVar.a(this.d);
                return c.b(str, oVar).a() == CtaButton.WIDTH_DIPS;
            }
        } catch (Exception e) {
            Log.e(a, "Error opening url: " + str, e);
            return false;
        }
    }

    private String b(String str) {
        try {
            return a(str, "analog", g.a(a.a()));
        } catch (Exception e) {
            return str;
        }
    }

    /* access modifiers changed from: protected|varargs */
    /* renamed from: a */
    public Void doInBackground(String... strArr) {
        String str = strArr[0];
        if (!r.a(str) && !b.contains(str)) {
            str = b(str);
            if (!(this.c == null || this.c.isEmpty())) {
                String str2;
                Iterator it = this.c.entrySet().iterator();
                while (true) {
                    str2 = str;
                    if (!it.hasNext()) {
                        break;
                    }
                    Entry entry = (Entry) it.next();
                    str = a(str2, (String) entry.getKey(), (String) entry.getValue());
                }
                str = str2;
            }
            int i = 1;
            while (true) {
                int i2 = i + 1;
                if (i > 2 || a(r0)) {
                    break;
                }
                i = i2;
            }
        }
        return null;
    }
}
