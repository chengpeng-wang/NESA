package com.baidu.platform.comapi.map;

import android.os.Bundle;

public class y extends d {
    static y f = null;
    String a = null;
    Bundle b = null;

    public y(int i) {
        this.d = i;
    }

    public void a(Bundle bundle) {
        this.b = bundle;
    }

    public void a(String str) {
        if (str != null) {
            this.a = str;
        }
    }

    public String b() {
        return this.a;
    }

    public Bundle c() {
        return this.b;
    }
}
