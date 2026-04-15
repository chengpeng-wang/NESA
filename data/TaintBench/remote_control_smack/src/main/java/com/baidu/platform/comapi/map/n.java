package com.baidu.platform.comapi.map;

import android.os.Bundle;

public class n extends d {
    static n a = null;
    String b = null;
    Bundle f = null;

    public n(int i) {
        this.d = i;
    }

    public void a(Bundle bundle) {
        this.f = bundle;
    }

    public void a(String str) {
        this.b = str;
    }

    public String b() {
        return this.b;
    }

    public Bundle c() {
        return this.f;
    }
}
