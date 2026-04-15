package org.objectweb.asm;

final class Item {
    int a;
    int b;
    int c;
    long d;
    String g;
    String h;
    String i;
    int j;
    Item k;

    Item() {
    }

    Item(int i) {
        this.a = i;
    }

    Item(int i, Item item) {
        this.a = i;
        this.b = item.b;
        this.c = item.c;
        this.d = item.d;
        this.g = item.g;
        this.h = item.h;
        this.i = item.i;
        this.j = item.j;
    }

    /* access modifiers changed from: 0000 */
    public void a(double d) {
        this.b = 6;
        this.d = Double.doubleToRawLongBits(d);
        this.j = Integer.MAX_VALUE & (this.b + ((int) d));
    }

    /* access modifiers changed from: 0000 */
    public void a(float f) {
        this.b = 4;
        this.c = Float.floatToRawIntBits(f);
        this.j = Integer.MAX_VALUE & (this.b + ((int) f));
    }

    /* access modifiers changed from: 0000 */
    public void a(int i) {
        this.b = 3;
        this.c = i;
        this.j = Integer.MAX_VALUE & (this.b + i);
    }

    /* access modifiers changed from: 0000 */
    public void a(int i, int i2) {
        this.b = 33;
        this.c = i;
        this.j = i2;
    }

    /* access modifiers changed from: 0000 */
    public void a(int i, String str, String str2, String str3) {
        this.b = i;
        this.g = str;
        this.h = str2;
        this.i = str3;
        switch (i) {
            case 1:
            case 7:
            case 8:
            case 16:
            case 30:
                this.j = (str.hashCode() + i) & Integer.MAX_VALUE;
                return;
            case 12:
                this.j = ((str.hashCode() * str2.hashCode()) + i) & Integer.MAX_VALUE;
                return;
            default:
                this.j = (((str.hashCode() * str2.hashCode()) * str3.hashCode()) + i) & Integer.MAX_VALUE;
                return;
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(long j) {
        this.b = 5;
        this.d = j;
        this.j = Integer.MAX_VALUE & (this.b + ((int) j));
    }

    /* access modifiers changed from: 0000 */
    public void a(String str, String str2, int i) {
        this.b = 18;
        this.d = (long) i;
        this.g = str;
        this.h = str2;
        this.j = Integer.MAX_VALUE & (((this.g.hashCode() * i) * this.h.hashCode()) + 18);
    }

    /* access modifiers changed from: 0000 */
    public boolean a(Item item) {
        switch (this.b) {
            case 1:
            case 7:
            case 8:
            case 16:
            case 30:
                return item.g.equals(this.g);
            case 3:
            case 4:
                return item.c == this.c;
            case 5:
            case 6:
            case 32:
                return item.d == this.d;
            case 12:
                return item.g.equals(this.g) && item.h.equals(this.h);
            case 18:
                return item.d == this.d && item.g.equals(this.g) && item.h.equals(this.h);
            case 31:
                return item.c == this.c && item.g.equals(this.g);
            default:
                return item.g.equals(this.g) && item.h.equals(this.h) && item.i.equals(this.i);
        }
    }
}
