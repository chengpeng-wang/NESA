package com.duapps.ad.base;

import android.content.Context;
import android.text.TextUtils;
import com.moceanmobile.mast.MASTNativeAdConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ToolboxLicenseManager */
public class t {
    private static final String a = t.class.getSimpleName();
    private static t b;
    private Map<Integer, List<String>> c = new HashMap();
    private Map<Integer, String> d = new HashMap();
    private Context e;

    public static synchronized t a(Context context) {
        t tVar;
        synchronized (t.class) {
            if (b == null) {
                b = new t(context.getApplicationContext());
            }
            tVar = b;
        }
        return tVar;
    }

    public static String a() {
        return DuAdNetwork.c().b();
    }

    public final List<String> a(int i) {
        List<String> a = i.a(this.e, i);
        if (a == null || a.size() == 0) {
            return (List) this.c.get(Integer.valueOf(i));
        }
        return a;
    }

    public final HashSet<Integer> b() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(this.c.keySet());
        hashSet.addAll(this.d.keySet());
        return hashSet;
    }

    public final String b(int i) {
        return (String) this.d.get(Integer.valueOf(i));
    }

    public final String c() {
        return i.m(this.e);
    }

    private t(Context context) {
        this.e = context;
    }

    public final void a(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("pidsJson cannot be null");
            }
            int length;
            int i;
            int i2;
            JSONObject jSONObject = new JSONObject(str);
            JSONArray optJSONArray = jSONObject.optJSONArray(MASTNativeAdConstants.RESPONSE_NATIVE_STRING);
            JSONArray optJSONArray2 = jSONObject.optJSONArray("list");
            JSONArray optJSONArray3 = jSONObject.optJSONArray("lockscreen");
            if (optJSONArray != null) {
                length = optJSONArray.length();
                for (i = 0; i < length; i++) {
                    jSONObject = optJSONArray.getJSONObject(i);
                    int optInt = jSONObject.optInt("pid");
                    ArrayList arrayList = new ArrayList();
                    JSONArray optJSONArray4 = jSONObject.optJSONArray("fbids");
                    if (optJSONArray4 != null) {
                        int length2 = optJSONArray4.length();
                        for (i2 = 0; i2 < length2; i2++) {
                            String optString = optJSONArray4.optString(i2);
                            if (!TextUtils.isEmpty(optString)) {
                                arrayList.add(optString);
                            }
                        }
                    }
                    this.c.put(Integer.valueOf(optInt), arrayList);
                }
            }
            if (optJSONArray2 != null) {
                i = optJSONArray2.length();
                for (i2 = 0; i2 < i; i2++) {
                    JSONObject jSONObject2 = optJSONArray2.getJSONObject(i2);
                    length = jSONObject2.optInt("pid");
                    this.d.put(Integer.valueOf(length), jSONObject2.optString("fbids"));
                }
            }
            if (optJSONArray3 != null) {
                int length3 = optJSONArray3.length();
                for (i = 0; i < length3; i++) {
                    jSONObject = optJSONArray3.getJSONObject(i);
                    int optInt2 = jSONObject.optInt("pid");
                    ArrayList arrayList2 = new ArrayList();
                    JSONArray optJSONArray5 = jSONObject.optJSONArray("fbids");
                    if (optJSONArray5 != null) {
                        int length4 = optJSONArray5.length();
                        for (i2 = 0; i2 < length4; i2++) {
                            String optString2 = optJSONArray5.optString(i2);
                            if (!TextUtils.isEmpty(optString2)) {
                                arrayList2.add(optString2);
                            }
                        }
                    }
                    this.c.put(Integer.valueOf(optInt2), arrayList2);
                }
            }
        } catch (JSONException e) {
            f.d(a, "JSON parse Exception :" + e.getMessage());
        }
    }
}
