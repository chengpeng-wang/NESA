package com.baidu.platform.comapi.map;

import android.os.Bundle;
import com.baidu.platform.comjni.map.basemap.a;

public abstract class d {
    int c = 0;
    int d = 0;
    o e = null;

    public d(int i) {
        this.d = i;
    }

    public void a(int i, o oVar) {
        this.c = i;
        this.e = oVar;
    }

    public abstract void a(String str);

    public void a(boolean z) {
        if (this.c != 0 && this.e != null) {
            a b = this.e.b();
            if (b != null) {
                b.a(this.c, z);
            }
        }
    }

    public abstract String b();

    public abstract Bundle c();

    public void d() {
        if (this.c != 0 && this.e != null) {
            a b = this.e.b();
            if (b != null) {
                b.a(this.c);
            }
        }
    }

    public int e() {
        return this.d;
    }
}
