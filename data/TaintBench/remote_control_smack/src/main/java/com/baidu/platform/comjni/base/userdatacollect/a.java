package com.baidu.platform.comjni.base.userdatacollect;

import android.os.Bundle;

public class a {
    private int a;
    private JNIUserdataCollect b;

    public a() {
        this.a = 0;
        this.b = null;
        this.b = new JNIUserdataCollect();
    }

    public int a() {
        this.a = this.b.Create();
        return this.a;
    }

    public void a(String str, String str2) {
        this.b.AppendRecord(this.a, str, str2);
    }

    public boolean a(String str, Bundle bundle) {
        return this.b.CreateUDC(this.a, str, bundle);
    }

    public int b() {
        return this.b.Release(this.a);
    }

    public void c() {
        this.b.Save(this.a);
    }
}
