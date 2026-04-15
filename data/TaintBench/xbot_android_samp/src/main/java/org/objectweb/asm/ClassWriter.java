package org.objectweb.asm;

public class ClassWriter extends ClassVisitor {
    public static final int COMPUTE_FRAMES = 2;
    public static final int COMPUTE_MAXS = 1;
    static final byte[] a;
    ByteVector A;
    FieldWriter B;
    FieldWriter C;
    MethodWriter D;
    MethodWriter E;
    private short G;
    Item[] H;
    String I;
    private final boolean J;
    private final boolean K;
    boolean L;
    ClassReader M;
    int b;
    int c;
    final ByteVector d;
    Item[] e;
    int f;
    final Item g;
    final Item h;
    final Item i;
    final Item j;
    private int k;
    private int l;
    private int m;
    private int n;
    private int o;
    private int[] p;
    private int q;
    private ByteVector r;
    private int s;
    private int t;
    private AnnotationWriter u;
    private AnnotationWriter v;
    private Attribute w;
    private int x;
    private ByteVector y;
    int z;

    static {
        byte[] bArr = new byte[220];
        String str = "AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKJJJJJJJJJJJJJJJJJJ";
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (str.charAt(i) - 65);
        }
        a = bArr;
    }

    public ClassWriter(int i) {
        boolean z = true;
        super(Opcodes.ASM4);
        this.c = 1;
        this.d = new ByteVector();
        this.e = new Item[256];
        this.f = (int) (0.75d * ((double) this.e.length));
        this.g = new Item();
        this.h = new Item();
        this.i = new Item();
        this.j = new Item();
        this.K = (i & 1) != 0;
        if ((i & 2) == 0) {
            z = false;
        }
        this.J = z;
    }

    public ClassWriter(ClassReader classReader, int i) {
        this(i);
        classReader.a(this);
        this.M = classReader;
    }

    private Item a(Item item) {
        Item item2 = this.e[item.j % this.e.length];
        while (item2 != null && (item2.b != item.b || !item.a(item2))) {
            item2 = item2.k;
        }
        return item2;
    }

    private void a(int i, int i2, int i3) {
        this.d.b(i, i2).putShort(i3);
    }

    private Item b(String str) {
        this.h.a(8, str, null, null);
        Item a = a(this.h);
        if (a != null) {
            return a;
        }
        this.d.b(8, newUTF8(str));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.h);
        b(a);
        return a;
    }

    private void b(int i, int i2, int i3) {
        this.d.a(i, i2).putShort(i3);
    }

    private void b(Item item) {
        int length;
        if (this.c + this.G > this.f) {
            length = this.e.length;
            int i = (length * 2) + 1;
            Item[] itemArr = new Item[i];
            for (int i2 = length - 1; i2 >= 0; i2--) {
                Item item2 = this.e[i2];
                while (item2 != null) {
                    int length2 = item2.j % itemArr.length;
                    Item item3 = item2.k;
                    item2.k = itemArr[length2];
                    itemArr[length2] = item2;
                    item2 = item3;
                }
            }
            this.e = itemArr;
            this.f = (int) (((double) i) * 0.75d);
        }
        length = item.j % this.e.length;
        item.k = this.e[length];
        this.e[length] = item;
    }

    private Item c(Item item) {
        this.G = (short) (this.G + 1);
        Item item2 = new Item(this.G, this.g);
        b(item2);
        if (this.H == null) {
            this.H = new Item[16];
        }
        if (this.G == this.H.length) {
            Item[] itemArr = new Item[(this.H.length * 2)];
            System.arraycopy(this.H, 0, itemArr, 0, this.H.length);
            this.H = itemArr;
        }
        this.H[this.G] = item2;
        return item2;
    }

    /* access modifiers changed from: 0000 */
    public int a(int i, int i2) {
        this.h.b = 32;
        this.h.d = ((long) i) | (((long) i2) << 32);
        this.h.j = Integer.MAX_VALUE & ((i + 32) + i2);
        Item a = a(this.h);
        if (a == null) {
            String str = this.H[i].g;
            String str2 = this.H[i2].g;
            this.h.c = c(getCommonSuperClass(str, str2));
            a = new Item(0, this.h);
            b(a);
        }
        return a.c;
    }

    /* access modifiers changed from: 0000 */
    public int a(String str, int i) {
        this.g.b = 31;
        this.g.c = i;
        this.g.g = str;
        this.g.j = Integer.MAX_VALUE & ((str.hashCode() + 31) + i);
        Item a = a(this.g);
        if (a == null) {
            a = c(this.g);
        }
        return a.a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(double d) {
        this.g.a(d);
        Item a = a(this.g);
        if (a != null) {
            return a;
        }
        this.d.putByte(6).putLong(this.g.d);
        a = new Item(this.c, this.g);
        this.c += 2;
        b(a);
        return a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(float f) {
        this.g.a(f);
        Item a = a(this.g);
        if (a != null) {
            return a;
        }
        this.d.putByte(4).putInt(this.g.c);
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.g);
        b(a);
        return a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(int i) {
        this.g.a(i);
        Item a = a(this.g);
        if (a != null) {
            return a;
        }
        this.d.putByte(3).putInt(i);
        int i2 = this.c;
        this.c = i2 + 1;
        a = new Item(i2, this.g);
        b(a);
        return a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(int i, String str, String str2, String str3) {
        this.j.a(i + 20, str, str2, str3);
        Item a = a(this.j);
        if (a != null) {
            return a;
        }
        if (i <= 4) {
            b(15, i, newField(str, str2, str3));
        } else {
            b(15, i, newMethod(str, str2, str3, i == 9));
        }
        int i2 = this.c;
        this.c = i2 + 1;
        a = new Item(i2, this.j);
        b(a);
        return a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(long j) {
        this.g.a(j);
        Item a = a(this.g);
        if (a != null) {
            return a;
        }
        this.d.putByte(5).putLong(j);
        a = new Item(this.c, this.g);
        this.c += 2;
        b(a);
        return a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(Object obj) {
        if (obj instanceof Integer) {
            return a(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return a(((Byte) obj).intValue());
        }
        if (obj instanceof Character) {
            return a(((Character) obj).charValue());
        }
        if (obj instanceof Short) {
            return a(((Short) obj).intValue());
        }
        if (obj instanceof Boolean) {
            return a(((Boolean) obj).booleanValue() ? 1 : 0);
        } else if (obj instanceof Float) {
            return a(((Float) obj).floatValue());
        } else {
            if (obj instanceof Long) {
                return a(((Long) obj).longValue());
            }
            if (obj instanceof Double) {
                return a(((Double) obj).doubleValue());
            }
            if (obj instanceof String) {
                return b((String) obj);
            }
            if (obj instanceof Type) {
                Type type = (Type) obj;
                int sort = type.getSort();
                return sort == 9 ? a(type.getDescriptor()) : sort == 10 ? a(type.getInternalName()) : c(type.getDescriptor());
            } else if (obj instanceof Handle) {
                Handle handle = (Handle) obj;
                return a(handle.a, handle.b, handle.c, handle.d);
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("value ").append(obj).toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public Item a(String str) {
        this.h.a(7, str, null, null);
        Item a = a(this.h);
        if (a != null) {
            return a;
        }
        this.d.b(7, newUTF8(str));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.h);
        b(a);
        return a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(String str, String str2) {
        this.h.a(12, str, str2, null);
        Item a = a(this.h);
        if (a != null) {
            return a;
        }
        a(12, newUTF8(str), newUTF8(str2));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.h);
        b(a);
        return a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(String str, String str2, String str3) {
        this.i.a(9, str, str2, str3);
        Item a = a(this.i);
        if (a != null) {
            return a;
        }
        a(9, newClass(str), newNameType(str2, str3));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.i);
        b(a);
        return a;
    }

    /* access modifiers changed from: 0000 */
    public Item a(String str, String str2, String str3, boolean z) {
        int i = z ? 11 : 10;
        this.i.a(i, str, str2, str3);
        Item a = a(this.i);
        if (a != null) {
            return a;
        }
        a(i, newClass(str), newNameType(str2, str3));
        int i2 = this.c;
        this.c = i2 + 1;
        Item item = new Item(i2, this.i);
        b(item);
        return item;
    }

    /* access modifiers changed from: varargs */
    public Item a(String str, String str2, Handle handle, Object... objArr) {
        int i;
        Item item;
        ByteVector byteVector = this.A;
        if (byteVector == null) {
            byteVector = new ByteVector();
            this.A = byteVector;
        }
        int i2 = byteVector.b;
        int hashCode = handle.hashCode();
        byteVector.putShort(newHandle(handle.a, handle.b, handle.c, handle.d));
        byteVector.putShort(r5);
        int i3 = hashCode;
        for (Object obj : objArr) {
            i3 ^= obj.hashCode();
            byteVector.putShort(newConst(obj));
        }
        byte[] bArr = byteVector.a;
        int i4 = (i4 + 2) << 1;
        int i5 = i3 & Integer.MAX_VALUE;
        Item item2 = this.e[i5 % this.e.length];
        loop1:
        while (item2 != null) {
            if (item2.b == 33 && item2.j == i5) {
                int i6 = item2.c;
                hashCode = 0;
                while (hashCode < i4) {
                    if (bArr[i2 + hashCode] != bArr[i6 + hashCode]) {
                        item2 = item2.k;
                    } else {
                        hashCode++;
                    }
                }
                break loop1;
            }
            item2 = item2.k;
        }
        if (item2 != null) {
            hashCode = item2.a;
            byteVector.b = i2;
            i = hashCode;
        } else {
            i = this.z;
            this.z = i + 1;
            item = new Item(i);
            item.a(i2, i5);
            b(item);
        }
        this.i.a(str, str2, i);
        item = a(this.i);
        if (item != null) {
            return item;
        }
        a(18, i, newNameType(str, str2));
        hashCode = this.c;
        this.c = hashCode + 1;
        Item item3 = new Item(hashCode, this.i);
        b(item3);
        return item3;
    }

    /* access modifiers changed from: 0000 */
    public int c(String str) {
        this.g.a(30, str, null, null);
        Item a = a(this.g);
        if (a == null) {
            a = c(this.g);
        }
        return a.a;
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: c */
    public Item m976c(String str) {
        this.h.a(16, str, null, null);
        Item a = a(this.h);
        if (a != null) {
            return a;
        }
        this.d.b(16, newUTF8(str));
        int i = this.c;
        this.c = i + 1;
        a = new Item(i, this.h);
        b(a);
        return a;
    }

    /* access modifiers changed from: protected */
    public String getCommonSuperClass(String str, String str2) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Class cls = Class.forName(str.replace('/', '.'), false, classLoader);
            Class cls2 = Class.forName(str2.replace('/', '.'), false, classLoader);
            if (cls.isAssignableFrom(cls2)) {
                return str;
            }
            if (cls2.isAssignableFrom(cls)) {
                return str2;
            }
            if (cls.isInterface() || cls2.isInterface()) {
                return "java/lang/Object";
            }
            do {
                cls = cls.getSuperclass();
            } while (!cls.isAssignableFrom(cls2));
            return cls.getName().replace('.', '/');
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public int newClass(String str) {
        return a(str).a;
    }

    public int newConst(Object obj) {
        return a(obj).a;
    }

    public int newField(String str, String str2, String str3) {
        return a(str, str2, str3).a;
    }

    public int newHandle(int i, String str, String str2, String str3) {
        return a(i, str, str2, str3).a;
    }

    public int newInvokeDynamic(String str, String str2, Handle handle, Object... objArr) {
        return a(str, str2, handle, objArr).a;
    }

    public int newMethod(String str, String str2, String str3, boolean z) {
        return a(str, str2, str3, z).a;
    }

    public int newMethodType(String str) {
        return c(str).a;
    }

    public int newNameType(String str, String str2) {
        return a(str, str2).a;
    }

    public int newUTF8(String str) {
        this.g.a(1, str, null, null);
        Item a = a(this.g);
        if (a == null) {
            this.d.putByte(1).putUTF8(str);
            int i = this.c;
            this.c = i + 1;
            a = new Item(i, this.g);
            b(a);
        }
        return a.a;
    }

    public byte[] toByteArray() {
        if (this.c > 32767) {
            throw new RuntimeException("Class file too large!");
        }
        int i;
        int i2 = (this.o * 2) + 24;
        FieldWriter fieldWriter = this.B;
        int i3 = 0;
        while (fieldWriter != null) {
            i2 += fieldWriter.a();
            fieldWriter = (FieldWriter) fieldWriter.fv;
            i3++;
        }
        MethodWriter methodWriter = this.D;
        int i4 = 0;
        while (methodWriter != null) {
            i2 += methodWriter.a();
            methodWriter = (MethodWriter) methodWriter.mv;
            i4++;
        }
        if (this.A != null) {
            i = 1;
            i2 += this.A.b + 8;
            newUTF8("BootstrapMethods");
        } else {
            i = 0;
        }
        if (this.m != 0) {
            i++;
            i2 += 8;
            newUTF8("Signature");
        }
        if (this.q != 0) {
            i++;
            i2 += 8;
            newUTF8("SourceFile");
        }
        if (this.r != null) {
            i++;
            i2 += this.r.b + 4;
            newUTF8("SourceDebugExtension");
        }
        if (this.s != 0) {
            i++;
            i2 += 10;
            newUTF8("EnclosingMethod");
        }
        if ((this.k & Opcodes.ACC_DEPRECATED) != 0) {
            i++;
            i2 += 6;
            newUTF8("Deprecated");
        }
        if ((this.k & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b & 65535) < 49 || (this.k & Opcodes.ASM4) != 0)) {
            i++;
            i2 += 6;
            newUTF8("Synthetic");
        }
        if (this.y != null) {
            i++;
            i2 += this.y.b + 8;
            newUTF8("InnerClasses");
        }
        if (this.u != null) {
            i++;
            i2 += this.u.a() + 8;
            newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.v != null) {
            i++;
            i2 += this.v.a() + 8;
            newUTF8("RuntimeInvisibleAnnotations");
        }
        int i5 = i2;
        if (this.w != null) {
            i5 += this.w.a(this, null, 0, -1, -1);
            i2 = i + this.w.a();
        } else {
            i2 = i;
        }
        ByteVector byteVector = new ByteVector(this.d.b + i5);
        byteVector.putInt(-889275714).putInt(this.b);
        byteVector.putShort(this.c).putByteArray(this.d.a, 0, this.d.b);
        byteVector.putShort(((393216 | ((this.k & Opcodes.ASM4) / 64)) ^ -1) & this.k).putShort(this.l).putShort(this.n);
        byteVector.putShort(this.o);
        for (i = 0; i < this.o; i++) {
            byteVector.putShort(this.p[i]);
        }
        byteVector.putShort(i3);
        for (fieldWriter = this.B; fieldWriter != null; fieldWriter = (FieldWriter) fieldWriter.fv) {
            fieldWriter.a(byteVector);
        }
        byteVector.putShort(i4);
        for (methodWriter = this.D; methodWriter != null; methodWriter = (MethodWriter) methodWriter.mv) {
            methodWriter.a(byteVector);
        }
        byteVector.putShort(i2);
        if (this.A != null) {
            byteVector.putShort(newUTF8("BootstrapMethods"));
            byteVector.putInt(this.A.b + 2).putShort(this.z);
            byteVector.putByteArray(this.A.a, 0, this.A.b);
        }
        if (this.m != 0) {
            byteVector.putShort(newUTF8("Signature")).putInt(2).putShort(this.m);
        }
        if (this.q != 0) {
            byteVector.putShort(newUTF8("SourceFile")).putInt(2).putShort(this.q);
        }
        if (this.r != null) {
            i = this.r.b - 2;
            byteVector.putShort(newUTF8("SourceDebugExtension")).putInt(i);
            byteVector.putByteArray(this.r.a, 2, i);
        }
        if (this.s != 0) {
            byteVector.putShort(newUTF8("EnclosingMethod")).putInt(4);
            byteVector.putShort(this.s).putShort(this.t);
        }
        if ((this.k & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(newUTF8("Deprecated")).putInt(0);
        }
        if ((this.k & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b & 65535) < 49 || (this.k & Opcodes.ASM4) != 0)) {
            byteVector.putShort(newUTF8("Synthetic")).putInt(0);
        }
        if (this.y != null) {
            byteVector.putShort(newUTF8("InnerClasses"));
            byteVector.putInt(this.y.b + 2).putShort(this.x);
            byteVector.putByteArray(this.y.a, 0, this.y.b);
        }
        if (this.u != null) {
            byteVector.putShort(newUTF8("RuntimeVisibleAnnotations"));
            this.u.a(byteVector);
        }
        if (this.v != null) {
            byteVector.putShort(newUTF8("RuntimeInvisibleAnnotations"));
            this.v.a(byteVector);
        }
        if (this.w != null) {
            this.w.a(this, null, 0, -1, -1, byteVector);
        }
        if (!this.L) {
            return byteVector.a;
        }
        ClassWriter classWriter = new ClassWriter(2);
        new ClassReader(byteVector.a).accept(classWriter, 4);
        return classWriter.toByteArray();
    }

    public final void visit(int i, int i2, String str, String str2, String str3, String[] strArr) {
        int i3 = 0;
        this.b = i;
        this.k = i2;
        this.l = newClass(str);
        this.I = str;
        if (str2 != null) {
            this.m = newUTF8(str2);
        }
        this.n = str3 == null ? 0 : newClass(str3);
        if (strArr != null && strArr.length > 0) {
            this.o = strArr.length;
            this.p = new int[this.o];
            while (i3 < this.o) {
                this.p[i3] = newClass(strArr[i3]);
                i3++;
            }
        }
    }

    public final AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.u;
            this.u = annotationWriter;
        } else {
            annotationWriter.g = this.v;
            this.v = annotationWriter;
        }
        return annotationWriter;
    }

    public final void visitAttribute(Attribute attribute) {
        attribute.a = this.w;
        this.w = attribute;
    }

    public final void visitEnd() {
    }

    public final FieldVisitor visitField(int i, String str, String str2, String str3, Object obj) {
        return new FieldWriter(this, i, str, str2, str3, obj);
    }

    public final void visitInnerClass(String str, String str2, String str3, int i) {
        int i2 = 0;
        if (this.y == null) {
            this.y = new ByteVector();
        }
        this.x++;
        this.y.putShort(str == null ? 0 : newClass(str));
        this.y.putShort(str2 == null ? 0 : newClass(str2));
        ByteVector byteVector = this.y;
        if (str3 != null) {
            i2 = newUTF8(str3);
        }
        byteVector.putShort(i2);
        this.y.putShort(i);
    }

    public final MethodVisitor visitMethod(int i, String str, String str2, String str3, String[] strArr) {
        return new MethodWriter(this, i, str, str2, str3, strArr, this.K, this.J);
    }

    public final void visitOuterClass(String str, String str2, String str3) {
        this.s = newClass(str);
        if (str2 != null && str3 != null) {
            this.t = newNameType(str2, str3);
        }
    }

    public final void visitSource(String str, String str2) {
        if (str != null) {
            this.q = newUTF8(str);
        }
        if (str2 != null) {
            this.r = new ByteVector().putUTF8(str2);
        }
    }
}
