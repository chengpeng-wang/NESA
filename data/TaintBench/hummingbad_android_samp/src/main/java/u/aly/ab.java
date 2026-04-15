package u.aly;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Response */
public class ab implements Serializable, Cloneable, az<ab, e> {
    public static final Map<e, aL> d;
    /* access modifiers changed from: private|static|final */
    public static final bd e = new bd("Response");
    /* access modifiers changed from: private|static|final */
    public static final aT f = new aT("resp_code", (byte) 8, (short) 1);
    /* access modifiers changed from: private|static|final */
    public static final aT g = new aT("msg", (byte) 11, (short) 2);
    /* access modifiers changed from: private|static|final */
    public static final aT h = new aT("imprint", (byte) 12, (short) 3);
    private static final Map<Class<? extends bg>, bh> i = new HashMap();
    private static final int j = 0;
    public int a;
    public String b;
    public T c;
    private byte k;
    private e[] l;

    /* compiled from: Response */
    private static class b implements bh {
        private b() {
        }

        /* synthetic */ b(b bVar) {
            this();
        }

        /* renamed from: a */
        public a b() {
            return new a();
        }
    }

    /* compiled from: Response */
    private static class d implements bh {
        private d() {
        }

        /* synthetic */ d(d dVar) {
            this();
        }

        /* renamed from: a */
        public c b() {
            return new c();
        }
    }

    /* compiled from: Response */
    public enum e implements aG {
        RESP_CODE((short) 1, "resp_code"),
        MSG((short) 2, "msg"),
        IMPRINT((short) 3, "imprint");
        
        private static final Map<String, e> d = null;
        private final short e;
        private final String f;

        static {
            d = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                d.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return RESP_CODE;
                case 2:
                    return MSG;
                case 3:
                    return IMPRINT;
                default:
                    return null;
            }
        }

        public static e b(int i) {
            e a = a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        public static e a(String str) {
            return (e) d.get(str);
        }

        private e(short s, String str) {
            this.e = s;
            this.f = str;
        }

        public short a() {
            return this.e;
        }

        public String b() {
            return this.f;
        }
    }

    /* compiled from: Response */
    private static class a extends bi<ab> {
        private a() {
        }

        /* synthetic */ a(a aVar) {
            this();
        }

        public void a(aY aYVar, ab abVar) throws aF {
            aYVar.j();
            while (true) {
                aT l = aYVar.l();
                if (l.b == (byte) 0) {
                    aYVar.k();
                    if (abVar.e()) {
                        abVar.m();
                        return;
                    }
                    throw new aZ("Required field 'resp_code' was not found in serialized data! Struct: " + toString());
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        abVar.a = aYVar.w();
                        abVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        abVar.b = aYVar.z();
                        abVar.b(true);
                        break;
                    case (short) 3:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        abVar.c = new T();
                        abVar.c.a(aYVar);
                        abVar.c(true);
                        break;
                    default:
                        bb.a(aYVar, l.b);
                        break;
                }
                aYVar.m();
            }
        }

        public void b(aY aYVar, ab abVar) throws aF {
            abVar.m();
            aYVar.a(ab.e);
            aYVar.a(ab.f);
            aYVar.a(abVar.a);
            aYVar.c();
            if (abVar.b != null && abVar.i()) {
                aYVar.a(ab.g);
                aYVar.a(abVar.b);
                aYVar.c();
            }
            if (abVar.c != null && abVar.l()) {
                aYVar.a(ab.h);
                abVar.c.b(aYVar);
                aYVar.c();
            }
            aYVar.d();
            aYVar.b();
        }
    }

    /* compiled from: Response */
    private static class c extends bj<ab> {
        private c() {
        }

        /* synthetic */ c(c cVar) {
            this();
        }

        /* renamed from: a */
        public void b(aY aYVar, ab abVar) throws aF {
            aYVar = (be) aYVar;
            aYVar.a(abVar.a);
            BitSet bitSet = new BitSet();
            if (abVar.i()) {
                bitSet.set(0);
            }
            if (abVar.l()) {
                bitSet.set(1);
            }
            aYVar.a(bitSet, 2);
            if (abVar.i()) {
                aYVar.a(abVar.b);
            }
            if (abVar.l()) {
                abVar.c.b(aYVar);
            }
        }

        /* renamed from: b */
        public void a(aY aYVar, ab abVar) throws aF {
            aYVar = (be) aYVar;
            abVar.a = aYVar.w();
            abVar.a(true);
            BitSet b = aYVar.b(2);
            if (b.get(0)) {
                abVar.b = aYVar.z();
                abVar.b(true);
            }
            if (b.get(1)) {
                abVar.c = new T();
                abVar.c.a(aYVar);
                abVar.c(true);
            }
        }
    }

    static {
        i.put(bi.class, new b());
        i.put(bj.class, new d());
        EnumMap enumMap = new EnumMap(e.class);
        enumMap.put(e.RESP_CODE, new aL("resp_code", (byte) 1, new aM((byte) 8)));
        enumMap.put(e.MSG, new aL("msg", (byte) 2, new aM((byte) 11)));
        enumMap.put(e.IMPRINT, new aL("imprint", (byte) 2, new aQ((byte) 12, T.class)));
        d = Collections.unmodifiableMap(enumMap);
        aL.a(ab.class, d);
    }

    public ab() {
        this.k = (byte) 0;
        this.l = new e[]{e.MSG, e.IMPRINT};
    }

    public ab(int i) {
        this();
        this.a = i;
        a(true);
    }

    public ab(ab abVar) {
        this.k = (byte) 0;
        this.l = new e[]{e.MSG, e.IMPRINT};
        this.k = abVar.k;
        this.a = abVar.a;
        if (abVar.i()) {
            this.b = abVar.b;
        }
        if (abVar.l()) {
            this.c = new T(abVar.c);
        }
    }

    /* renamed from: a */
    public ab g() {
        return new ab(this);
    }

    public void b() {
        a(false);
        this.a = 0;
        this.b = null;
        this.c = null;
    }

    public int c() {
        return this.a;
    }

    public ab a(int i) {
        this.a = i;
        a(true);
        return this;
    }

    public void d() {
        this.k = aw.b(this.k, 0);
    }

    public boolean e() {
        return aw.a(this.k, 0);
    }

    public void a(boolean z) {
        this.k = aw.a(this.k, 0, z);
    }

    public String f() {
        return this.b;
    }

    public ab a(String str) {
        this.b = str;
        return this;
    }

    public void h() {
        this.b = null;
    }

    public boolean i() {
        return this.b != null;
    }

    public void b(boolean z) {
        if (!z) {
            this.b = null;
        }
    }

    public T j() {
        return this.c;
    }

    public ab a(T t) {
        this.c = t;
        return this;
    }

    public void k() {
        this.c = null;
    }

    public boolean l() {
        return this.c != null;
    }

    public void c(boolean z) {
        if (!z) {
            this.c = null;
        }
    }

    /* renamed from: c */
    public e b(int i) {
        return e.a(i);
    }

    public void a(aY aYVar) throws aF {
        ((bh) i.get(aYVar.D())).b().a(aYVar, this);
    }

    public void b(aY aYVar) throws aF {
        ((bh) i.get(aYVar.D())).b().b(aYVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Response(");
        stringBuilder.append("resp_code:");
        stringBuilder.append(this.a);
        if (i()) {
            stringBuilder.append(", ");
            stringBuilder.append("msg:");
            if (this.b == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.b);
            }
        }
        if (l()) {
            stringBuilder.append(", ");
            stringBuilder.append("imprint:");
            if (this.c == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.c);
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void m() throws aF {
        if (this.c != null) {
            this.c.n();
        }
    }

    private void a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            b(new aS(new bk((OutputStream) objectOutputStream)));
        } catch (aF e) {
            throw new IOException(e.getMessage());
        }
    }

    private void a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            this.k = (byte) 0;
            a(new aS(new bk((InputStream) objectInputStream)));
        } catch (aF e) {
            throw new IOException(e.getMessage());
        }
    }
}
