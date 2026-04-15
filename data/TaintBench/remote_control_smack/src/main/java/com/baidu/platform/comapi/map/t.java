package com.baidu.platform.comapi.map;

import com.baidu.platform.comapi.basestruct.c;

public class t {
    public float a = -1.0f;
    public int b = -1;
    public int c = -1;
    public int d = -1;
    public int e = -1;
    public b f = new b();
    public a g = new a();
    public long h = 0;
    public long i = 0;
    public boolean j = false;

    public class a {
        public long a = 0;
        public long b = 0;
        public long c = 0;
        public long d = 0;
        public c e = new c(0, 0);
        public c f = new c(0, 0);
        public c g = new c(0, 0);
        public c h = new c(0, 0);

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            return this.d != aVar.d ? false : this.a != aVar.a ? false : this.b != aVar.b ? false : this.c == aVar.c;
        }

        public int hashCode() {
            return ((((((((int) (this.d ^ (this.d >>> 32))) + 31) * 31) + ((int) (this.a ^ (this.a >>> 32)))) * 31) + ((int) (this.b ^ (this.b >>> 32)))) * 31) + ((int) (this.c ^ (this.c >>> 32)));
        }
    }

    public class b {
        public int a = 0;
        public int b = 0;
        public int c = 0;
        public int d = 0;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            return this.d != bVar.d ? false : this.a != bVar.a ? false : this.b != bVar.b ? false : this.c == bVar.c;
        }

        public int hashCode() {
            return ((((((this.d + 31) * 31) + this.a) * 31) + this.b) * 31) + this.c;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        t tVar = (t) obj;
        if (this.d != tVar.d) {
            return false;
        }
        if (this.e != tVar.e) {
            return false;
        }
        if (this.j != tVar.j) {
            return false;
        }
        if (this.g == null) {
            if (tVar.g != null) {
                return false;
            }
        } else if (!this.g.equals(tVar.g)) {
            return false;
        }
        return Float.floatToIntBits(this.a) != Float.floatToIntBits(tVar.a) ? false : this.c != tVar.c ? false : this.b != tVar.b ? false : this.i != tVar.i ? false : this.h != tVar.h ? false : this.f == null ? tVar.f == null : this.f.equals(tVar.f);
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((((this.g == null ? 0 : this.g.hashCode()) + (((this.j ? 1 : 0) + ((((this.d + 31) * 31) + this.e) * 31)) * 31)) * 31) + Float.floatToIntBits(this.a)) * 31) + this.c) * 31) + this.b) * 31;
        if (this.f != null) {
            i = this.f.hashCode();
        }
        return hashCode + i;
    }
}
