package com.baidu.platform.comjni.base.location;

import android.os.Bundle;

public class a {
    private int a;
    private JNILocation b;

    public a() {
        this.a = 0;
        this.b = null;
        this.b = new JNILocation();
    }

    public int a() {
        this.a = this.b.Create();
        return this.a;
    }

    public boolean a(float f, float f2, Bundle bundle, String str) {
        return this.b.CoordinateEncryptEx(this.a, f, f2, bundle, str);
    }
}
