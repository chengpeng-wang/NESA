package com.baidu.platform.comapi.map;

import android.os.Bundle;

public class x extends d {
    static x b = null;
    String a = null;
    int f = 0;
    int g = 0;
    int h = 0;
    int i = 0;

    public x(int i) {
        this.d = i;
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
        Bundle bundle = new Bundle();
        bundle.putInt("accFlag", this.f);
        bundle.putInt("centerFlag", this.g);
        if (this.g == 1) {
            bundle.putInt("centerX", this.h);
            bundle.putInt("centerY", this.i);
        }
        return bundle;
    }
}
