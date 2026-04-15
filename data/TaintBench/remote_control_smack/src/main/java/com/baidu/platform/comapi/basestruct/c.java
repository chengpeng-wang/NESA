package com.baidu.platform.comapi.basestruct;

import java.io.Serializable;

public class c implements Serializable {
    public int a;
    public int b;

    public c(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    public int a() {
        return this.a;
    }

    public void a(int i) {
        this.a = i;
    }

    public int b() {
        return this.b;
    }

    public void b(int i) {
        this.b = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        c cVar = (c) obj;
        return this.a != cVar.a ? false : this.b == cVar.b;
    }

    public int hashCode() {
        return ((this.a + 31) * 31) + this.b;
    }

    public String toString() {
        return "Point [x=" + this.a + ", y=" + this.b + "]";
    }
}
