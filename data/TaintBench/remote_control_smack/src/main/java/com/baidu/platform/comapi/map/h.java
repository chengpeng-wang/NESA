package com.baidu.platform.comapi.map;

import android.os.Handler;
import com.baidu.platform.comapi.basestruct.c;
import com.baidu.platform.comjni.map.basemap.a;
import java.util.ArrayList;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class h {
    private static a a = null;
    private static h b = null;
    /* access modifiers changed from: private|static */
    public static m c = null;
    private static Handler d = null;

    private h() {
    }

    public static h a(o oVar) {
        if (b == null) {
            b = new h();
        }
        return (b == null || !b.b(oVar)) ? null : b;
    }

    public static void a() {
        if (a != null) {
            if (b != null) {
                if (d != null) {
                    com.baidu.platform.comjni.engine.a.b(65289, d);
                    d = null;
                }
                if (c != null) {
                    c.a();
                    c = null;
                }
                b = null;
            }
            a = null;
        }
    }

    private boolean b(o oVar) {
        if (oVar == null) {
            return false;
        }
        if (a == null) {
            a = oVar.b();
        }
        if (a == null) {
            return false;
        }
        c = new m();
        d = new i(this);
        com.baidu.platform.comjni.engine.a.a(65289, d);
        return true;
    }

    public ArrayList<g> a(String str) {
        if (str.equals("") || a == null) {
            return null;
        }
        String str2 = "";
        String b = a.b(str);
        if (b == null || b.equals("")) {
            return null;
        }
        ArrayList<g> arrayList = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(b);
            if (jSONObject == null || jSONObject.length() == 0) {
                return null;
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("dataset");
            if (optJSONArray == null) {
                return null;
            }
            for (int i = 0; i < optJSONArray.length(); i++) {
                g gVar = new g();
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                gVar.a = jSONObject2.optInt("id");
                gVar.b = jSONObject2.optString("name");
                gVar.c = jSONObject2.optInt("size");
                gVar.d = jSONObject2.optInt("cty");
                if (jSONObject2.has("child")) {
                    JSONArray optJSONArray2 = jSONObject2.optJSONArray("child");
                    ArrayList arrayList2 = new ArrayList();
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        g gVar2 = new g();
                        JSONObject optJSONObject = optJSONArray2.optJSONObject(i2);
                        gVar2.a = optJSONObject.optInt("id");
                        gVar2.b = optJSONObject.optString("name");
                        gVar2.c = optJSONObject.optInt("size");
                        gVar2.d = optJSONObject.optInt("cty");
                        arrayList2.add(gVar2);
                    }
                    gVar.a(arrayList2);
                }
                arrayList.add(gVar);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void a(l lVar) {
        c.a(lVar);
    }

    public boolean a(int i) {
        return (a == null || i < 0) ? false : a.d(i);
    }

    public boolean a(boolean z) {
        return a == null ? false : a.c(z);
    }

    public ArrayList<g> b() {
        if (a == null) {
            return null;
        }
        String str = "";
        String i = a.i();
        ArrayList<g> arrayList = new ArrayList();
        try {
            JSONArray optJSONArray = new JSONObject(i).optJSONArray("dataset");
            for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                g gVar = new g();
                JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                gVar.a = optJSONObject.optInt("id");
                gVar.b = optJSONObject.optString("name");
                gVar.c = optJSONObject.optInt("size");
                gVar.d = optJSONObject.optInt("cty");
                if (optJSONObject.has("child")) {
                    JSONArray optJSONArray2 = optJSONObject.optJSONArray("child");
                    ArrayList arrayList2 = new ArrayList();
                    for (int i3 = 0; i3 < optJSONArray2.length(); i3++) {
                        g gVar2 = new g();
                        JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i3);
                        gVar2.a = optJSONObject2.optInt("id");
                        gVar2.b = optJSONObject2.optString("name");
                        gVar2.c = optJSONObject2.optInt("size");
                        gVar2.d = optJSONObject2.optInt("cty");
                        arrayList2.add(gVar2);
                    }
                    gVar.a(arrayList2);
                }
                arrayList.add(gVar);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void b(l lVar) {
        c.b(lVar);
    }

    public boolean b(int i) {
        return (a == null || i < 0) ? false : a.a(i, false, 0);
    }

    public ArrayList<g> c() {
        String str = "";
        if (a == null) {
            return null;
        }
        String b = a.b("");
        ArrayList<g> arrayList = new ArrayList();
        try {
            JSONArray optJSONArray = new JSONObject(b).optJSONArray("dataset");
            for (int i = 0; i < optJSONArray.length(); i++) {
                g gVar = new g();
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                gVar.a = optJSONObject.optInt("id");
                gVar.b = optJSONObject.optString("name");
                gVar.c = optJSONObject.optInt("size");
                gVar.d = optJSONObject.optInt("cty");
                if (optJSONObject.has("child")) {
                    JSONArray optJSONArray2 = optJSONObject.optJSONArray("child");
                    ArrayList arrayList2 = new ArrayList();
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        g gVar2 = new g();
                        JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i2);
                        gVar2.a = optJSONObject2.optInt("id");
                        gVar2.b = optJSONObject2.optString("name");
                        gVar2.c = optJSONObject2.optInt("size");
                        gVar2.d = optJSONObject2.optInt("cty");
                        arrayList2.add(gVar2);
                    }
                    gVar.a(arrayList2);
                }
                arrayList.add(gVar);
            }
            return arrayList;
        } catch (Exception | JSONException e) {
            return null;
        }
    }

    public boolean c(int i) {
        return (a == null || i < 0) ? false : a.b(i, false, 0);
    }

    public ArrayList<k> d() {
        if (a == null) {
            return null;
        }
        String str = "";
        String h = a.h();
        if (h == null || h.equals("")) {
            return null;
        }
        ArrayList<k> arrayList = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(h);
            if (jSONObject.length() == 0) {
                return null;
            }
            JSONArray optJSONArray = jSONObject.optJSONArray("dataset");
            for (int i = 0; i < optJSONArray.length(); i++) {
                k kVar = new k();
                j jVar = new j();
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                jVar.a = optJSONObject.optInt("id");
                jVar.b = optJSONObject.optString("name");
                jVar.c = optJSONObject.optString("pinyin");
                jVar.h = optJSONObject.optInt("size");
                jVar.i = optJSONObject.optInt("ratio");
                jVar.l = optJSONObject.optInt("status");
                jVar.g = new c();
                jVar.g.a(optJSONObject.optInt(GroupChatInvitation.ELEMENT_NAME));
                jVar.g.b(optJSONObject.optInt("y"));
                if (optJSONObject.optInt("up") == 1) {
                    jVar.j = true;
                } else {
                    jVar.j = false;
                }
                jVar.e = optJSONObject.optInt("lev");
                if (jVar.j) {
                    jVar.k = optJSONObject.optInt("svr_size");
                } else {
                    jVar.k = 0;
                }
                kVar.a(jVar);
                arrayList.add(kVar);
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean d(int i) {
        return a == null ? false : a.b(0, true, i);
    }

    public boolean e(int i) {
        return (a == null || i < 0) ? false : a.c(i, false);
    }

    public k f(int i) {
        if (a == null || i < 0) {
            return null;
        }
        String str = "";
        String e = a.e(i);
        if (e == null || e.equals("")) {
            return null;
        }
        k kVar = new k();
        j jVar = new j();
        try {
            JSONObject jSONObject = new JSONObject(e);
            if (jSONObject.length() == 0) {
                return null;
            }
            jVar.a = jSONObject.optInt("id");
            jVar.b = jSONObject.optString("name");
            jVar.c = jSONObject.optString("pinyin");
            jVar.d = jSONObject.optString("headchar");
            jVar.h = jSONObject.optInt("size");
            jVar.i = jSONObject.optInt("ratio");
            jVar.l = jSONObject.optInt("status");
            jVar.g = new c();
            jVar.g.a(jSONObject.optInt(GroupChatInvitation.ELEMENT_NAME));
            jVar.g.b(jSONObject.optInt("y"));
            if (jSONObject.optInt("up") == 1) {
                jVar.j = true;
            } else {
                jVar.j = false;
            }
            jVar.e = jSONObject.optInt("lev");
            jVar.f = jSONObject.optInt("ver");
            kVar.a(jVar);
            return kVar;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
