package com.baidu.platform.comjni.map.commonmemcache;

import android.os.Bundle;

public class a {
    private int a;
    private JNICommonMemCache b;

    public a() {
        this.a = 0;
        this.b = null;
        this.b = new JNICommonMemCache();
    }

    public int a() {
        this.a = this.b.Create();
        return this.a;
    }

    public void a(Bundle bundle) {
        this.b.Init(this.a, bundle);
    }

    public int b() {
        return this.b.Release(this.a);
    }
}
