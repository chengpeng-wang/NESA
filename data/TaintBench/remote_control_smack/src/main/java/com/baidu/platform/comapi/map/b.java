package com.baidu.platform.comapi.map;

import android.os.Bundle;

public class b extends d {
    static b b = null;
    String a = null;

    public b(int i) {
        this.d = i;
    }

    public static d a() {
        if (b == null) {
            b = new b(20);
        }
        return b;
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
        return null;
    }
}
