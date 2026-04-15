package com.cleanmaster.base.util.misc;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* compiled from: ArraySet */
public final class a implements Collection, Set {
    static Object[] a;
    static int b;
    static Object[] c;
    static int d;
    int[] e = c.b;
    Object[] f = c.d;
    int g = 0;
    d h;

    /* access modifiers changed from: private */
    public int a(Object obj, int i) {
        int i2 = this.g;
        if (i2 == 0) {
            return -1;
        }
        int a = c.a(this.e, i2, i);
        if (a < 0 || obj.equals(this.f[a])) {
            return a;
        }
        int i3 = a + 1;
        while (i3 < i2 && this.e[i3] == i) {
            if (obj.equals(this.f[i3])) {
                return i3;
            }
            i3++;
        }
        a--;
        while (a >= 0 && this.e[a] == i) {
            if (obj.equals(this.f[a])) {
                return a;
            }
            a--;
        }
        return i3 ^ -1;
    }

    /* access modifiers changed from: private */
    public int a() {
        int i = this.g;
        if (i == 0) {
            return -1;
        }
        int a = c.a(this.e, i, 0);
        if (a < 0 || this.f[a] == null) {
            return a;
        }
        int i2 = a + 1;
        while (i2 < i && this.e[i2] == 0) {
            if (this.f[i2] == null) {
                return i2;
            }
            i2++;
        }
        a--;
        while (a >= 0 && this.e[a] == 0) {
            if (this.f[a] == null) {
                return a;
            }
            a--;
        }
        return i2 ^ -1;
    }

    private void d(int i) {
        Object[] objArr;
        if (i == 8) {
            synchronized (a.class) {
                if (c != null) {
                    objArr = c;
                    this.f = objArr;
                    c = (Object[]) objArr[0];
                    this.e = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    d--;
                    return;
                }
            }
        } else if (i == 4) {
            synchronized (a.class) {
                if (a != null) {
                    objArr = a;
                    this.f = objArr;
                    a = (Object[]) objArr[0];
                    this.e = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    b--;
                    return;
                }
            }
        }
        this.e = new int[i];
        this.f = new Object[i];
    }

    private static void a(int[] iArr, Object[] objArr, int i) {
        int i2;
        if (iArr.length == 8) {
            synchronized (a.class) {
                if (d < 10) {
                    objArr[0] = c;
                    objArr[1] = iArr;
                    for (i2 = i - 1; i2 >= 2; i2--) {
                        objArr[i2] = null;
                    }
                    c = objArr;
                    d++;
                }
            }
        } else if (iArr.length == 4) {
            synchronized (a.class) {
                if (b < 10) {
                    objArr[0] = a;
                    objArr[1] = iArr;
                    for (i2 = i - 1; i2 >= 2; i2--) {
                        objArr[i2] = null;
                    }
                    a = objArr;
                    b++;
                }
            }
        }
    }

    public final void clear() {
        if (this.g != 0) {
            a(this.e, this.f, this.g);
            this.e = c.b;
            this.f = c.d;
            this.g = 0;
        }
    }

    public final void a(int i) {
        if (this.e.length < i) {
            int[] iArr = this.e;
            Object[] objArr = this.f;
            d(i);
            if (this.g > 0) {
                System.arraycopy(iArr, 0, this.e, 0, this.g);
                System.arraycopy(objArr, 0, this.f, 0, this.g);
            }
            a(iArr, objArr, this.g);
        }
    }

    public final boolean contains(Object key) {
        return key == null ? a() >= 0 : a(key, key.hashCode()) >= 0;
    }

    public final Object b(int i) {
        return this.f[i];
    }

    public final boolean isEmpty() {
        return this.g <= 0;
    }

    public final boolean add(Object value) {
        int a;
        int i;
        int i2 = 8;
        if (value == null) {
            a = a();
            i = 0;
        } else {
            i = value.hashCode();
            a = a(value, i);
        }
        if (a >= 0) {
            return false;
        }
        a ^= -1;
        if (this.g >= this.e.length) {
            if (this.g >= 8) {
                i2 = this.g + (this.g >> 1);
            } else if (this.g < 4) {
                i2 = 4;
            }
            int[] iArr = this.e;
            Object[] objArr = this.f;
            d(i2);
            if (this.e.length > 0) {
                System.arraycopy(iArr, 0, this.e, 0, iArr.length);
                System.arraycopy(objArr, 0, this.f, 0, objArr.length);
            }
            a(iArr, objArr, this.g);
        }
        if (a < this.g) {
            System.arraycopy(this.e, a, this.e, a + 1, this.g - a);
            System.arraycopy(this.f, a, this.f, a + 1, this.g - a);
        }
        this.e[a] = i;
        this.f[a] = value;
        this.g++;
        return true;
    }

    public final boolean remove(Object object) {
        int a = object == null ? a() : a(object, object.hashCode());
        if (a < 0) {
            return false;
        }
        c(a);
        return true;
    }

    public final Object c(int i) {
        int i2 = 8;
        Object obj = this.f[i];
        if (this.g <= 1) {
            a(this.e, this.f, this.g);
            this.e = c.b;
            this.f = c.d;
            this.g = 0;
        } else if (this.e.length <= 8 || this.g >= this.e.length / 3) {
            this.g--;
            if (i < this.g) {
                System.arraycopy(this.e, i + 1, this.e, i, this.g - i);
                System.arraycopy(this.f, i + 1, this.f, i, this.g - i);
            }
            this.f[this.g] = null;
        } else {
            if (this.g > 8) {
                i2 = this.g + (this.g >> 1);
            }
            int[] iArr = this.e;
            Object[] objArr = this.f;
            d(i2);
            this.g--;
            if (i > 0) {
                System.arraycopy(iArr, 0, this.e, 0, i);
                System.arraycopy(objArr, 0, this.f, 0, i);
            }
            if (i < this.g) {
                System.arraycopy(iArr, i + 1, this.e, i, this.g - i);
                System.arraycopy(objArr, i + 1, this.f, i, this.g - i);
            }
        }
        return obj;
    }

    public final int size() {
        return this.g;
    }

    public final Object[] toArray() {
        Object[] objArr = new Object[this.g];
        System.arraycopy(this.f, 0, objArr, 0, this.g);
        return objArr;
    }

    public final Object[] toArray(Object[] array) {
        T[] array2;
        if (array2.length < this.g) {
            array2 = (Object[]) Array.newInstance(array2.getClass().getComponentType(), this.g);
        }
        System.arraycopy(this.f, 0, array2, 0, this.g);
        if (array2.length > this.g) {
            array2[this.g] = null;
        }
        return array2;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Set)) {
            return false;
        }
        Set set = (Set) object;
        if (size() != set.size()) {
            return false;
        }
        int i = 0;
        while (i < this.g) {
            try {
                if (!set.contains(b(i))) {
                    return false;
                }
                i++;
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }
        return true;
    }

    public final int hashCode() {
        int i = 0;
        int[] iArr = this.e;
        int i2 = 0;
        while (i < this.g) {
            i2 += iArr[i];
            i++;
        }
        return i2;
    }

    public final String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder(this.g * 14);
        stringBuilder.append('{');
        for (int i = 0; i < this.g; i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            a b = b(i);
            if (b != this) {
                stringBuilder.append(b);
            } else {
                stringBuilder.append("(this Set)");
            }
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private d b() {
        if (this.h == null) {
            this.h = new b(this);
        }
        return this.h;
    }

    public final Iterator iterator() {
        return b().d().iterator();
    }

    public final boolean containsAll(Collection collection) {
        for (Object contains : collection) {
            if (!contains(contains)) {
                return false;
            }
        }
        return true;
    }

    public final boolean addAll(Collection collection) {
        a(this.g + collection.size());
        boolean z = false;
        for (Object add : collection) {
            z |= add(add);
        }
        return z;
    }

    public final boolean removeAll(Collection collection) {
        boolean z = false;
        for (Object remove : collection) {
            z |= remove(remove);
        }
        return z;
    }

    public final boolean retainAll(Collection collection) {
        boolean z = false;
        for (int i = this.g - 1; i >= 0; i--) {
            if (!collection.contains(this.f[i])) {
                c(i);
                z = true;
            }
        }
        return z;
    }
}
