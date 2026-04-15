package com.baidu.platform.comapi.basestruct;

import java.io.Serializable;

public class b implements Serializable {
    public c a;
    public c b;

    public b() {
        if (this.a == null) {
            this.a = new c();
        }
        if (this.b == null) {
            this.b = new c();
        }
    }

    public void a(c cVar) {
        this.a = cVar;
    }

    public void b(c cVar) {
        this.b = cVar;
    }
}
