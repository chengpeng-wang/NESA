package com.baidu.platform.comapi.c;

import android.os.Bundle;
import com.baidu.platform.comapi.d.c;
import org.json.JSONException;
import org.json.JSONObject;

public class a {
    public static a a = null;
    private JSONObject b = null;
    private com.baidu.platform.comjni.base.userdatacollect.a c = null;

    public static a a() {
        if (a == null) {
            a = new a();
            if (!a.e()) {
                a = null;
                return null;
            }
        }
        return a;
    }

    public static void b() {
        if (a != null) {
            if (a.c != null) {
                a.c.b();
                a.c = null;
            }
            a = null;
        }
    }

    private boolean e() {
        if (this.c != null) {
            return true;
        }
        this.c = new com.baidu.platform.comjni.base.userdatacollect.a();
        if (this.c.a() == 0) {
            this.c = null;
            return false;
        }
        this.b = new JSONObject();
        return true;
    }

    private Bundle f() {
        Bundle bundle = new Bundle();
        bundle.putString("pd", "mapsdk");
        bundle.putString("os", "android");
        bundle.putString("sv", c.h());
        bundle.putString("ov", c.j());
        bundle.putString("im", c.n());
        bundle.putString("channel", c.o());
        bundle.putString("mb", c.f());
        bundle.putString("ver", "2");
        bundle.putInt("sw", c.g());
        bundle.putInt("sh", c.i());
        bundle.putString("resid", "02");
        bundle.putString("dpi", String.format("(%d,%d)", new Object[]{Integer.valueOf(c.k()), Integer.valueOf(c.l())}));
        return bundle;
    }

    public void a(String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("ActParam", this.b);
        } catch (JSONException e) {
        }
        if (this.b.length() <= 0) {
            this.c.a(str, null);
        } else {
            this.c.a(str, jSONObject.toString());
        }
        this.b = null;
        this.b = new JSONObject();
    }

    public void a(String str, String str2) {
        try {
            this.b.put(str, str2);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean c() {
        return this.c.a(c.q() + "/udc/", f());
    }

    public void d() {
        this.c.c();
    }
}
