package u.aly;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: UMEnvelope */
public class ah implements Serializable, Cloneable, az<ah, e> {
    public static final Map<e, aL> j;
    /* access modifiers changed from: private|static|final */
    public static final bd k = new bd("UMEnvelope");
    /* access modifiers changed from: private|static|final */
    public static final aT l = new aT("version", (byte) 11, (short) 1);
    /* access modifiers changed from: private|static|final */
    public static final aT m = new aT("address", (byte) 11, (short) 2);
    /* access modifiers changed from: private|static|final */
    public static final aT n = new aT("signature", (byte) 11, (short) 3);
    /* access modifiers changed from: private|static|final */
    public static final aT o = new aT("serial_num", (byte) 8, (short) 4);
    /* access modifiers changed from: private|static|final */
    public static final aT p = new aT("ts_secs", (byte) 8, (short) 5);
    /* access modifiers changed from: private|static|final */
    public static final aT q = new aT("length", (byte) 8, (short) 6);
    /* access modifiers changed from: private|static|final */
    public static final aT r = new aT("entity", (byte) 11, (short) 7);
    /* access modifiers changed from: private|static|final */
    public static final aT s = new aT("guid", (byte) 11, (short) 8);
    /* access modifiers changed from: private|static|final */
    public static final aT t = new aT("checksum", (byte) 11, (short) 9);
    private static final Map<Class<? extends bg>, bh> u = new HashMap();
    private static final int v = 0;
    private static final int w = 1;
    private static final int x = 2;
    public String a;
    public String b;
    public String c;
    public int d;
    public int e;
    public int f;
    public ByteBuffer g;
    public String h;
    public String i;
    private byte y;

    /* compiled from: UMEnvelope */
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

    /* compiled from: UMEnvelope */
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

    /* compiled from: UMEnvelope */
    public enum e implements aG {
        VERSION((short) 1, "version"),
        ADDRESS((short) 2, "address"),
        SIGNATURE((short) 3, "signature"),
        SERIAL_NUM((short) 4, "serial_num"),
        TS_SECS((short) 5, "ts_secs"),
        LENGTH((short) 6, "length"),
        ENTITY((short) 7, "entity"),
        GUID((short) 8, "guid"),
        CHECKSUM((short) 9, "checksum");
        
        private static final Map<String, e> j = null;
        private final short k;
        private final String l;

        static {
            j = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                j.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return VERSION;
                case 2:
                    return ADDRESS;
                case 3:
                    return SIGNATURE;
                case 4:
                    return SERIAL_NUM;
                case 5:
                    return TS_SECS;
                case 6:
                    return LENGTH;
                case 7:
                    return ENTITY;
                case 8:
                    return GUID;
                case 9:
                    return CHECKSUM;
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
            return (e) j.get(str);
        }

        private e(short s, String str) {
            this.k = s;
            this.l = str;
        }

        public short a() {
            return this.k;
        }

        public String b() {
            return this.l;
        }
    }

    /* compiled from: UMEnvelope */
    private static class a extends bi<ah> {
        private a() {
        }

        /* synthetic */ a(a aVar) {
            this();
        }

        public void a(aY aYVar, ah ahVar) throws aF {
            aYVar.j();
            while (true) {
                aT l = aYVar.l();
                if (l.b == (byte) 0) {
                    aYVar.k();
                    if (!ahVar.o()) {
                        throw new aZ("Required field 'serial_num' was not found in serialized data! Struct: " + toString());
                    } else if (!ahVar.r()) {
                        throw new aZ("Required field 'ts_secs' was not found in serialized data! Struct: " + toString());
                    } else if (ahVar.u()) {
                        ahVar.F();
                        return;
                    } else {
                        throw new aZ("Required field 'length' was not found in serialized data! Struct: " + toString());
                    }
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.a = aYVar.z();
                        ahVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.b = aYVar.z();
                        ahVar.b(true);
                        break;
                    case (short) 3:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.c = aYVar.z();
                        ahVar.c(true);
                        break;
                    case (short) 4:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.d = aYVar.w();
                        ahVar.d(true);
                        break;
                    case (short) 5:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.e = aYVar.w();
                        ahVar.e(true);
                        break;
                    case (short) 6:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.f = aYVar.w();
                        ahVar.f(true);
                        break;
                    case (short) 7:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.g = aYVar.A();
                        ahVar.g(true);
                        break;
                    case (short) 8:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.h = aYVar.z();
                        ahVar.h(true);
                        break;
                    case (short) 9:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        ahVar.i = aYVar.z();
                        ahVar.i(true);
                        break;
                    default:
                        bb.a(aYVar, l.b);
                        break;
                }
                aYVar.m();
            }
        }

        public void b(aY aYVar, ah ahVar) throws aF {
            ahVar.F();
            aYVar.a(ah.k);
            if (ahVar.a != null) {
                aYVar.a(ah.l);
                aYVar.a(ahVar.a);
                aYVar.c();
            }
            if (ahVar.b != null) {
                aYVar.a(ah.m);
                aYVar.a(ahVar.b);
                aYVar.c();
            }
            if (ahVar.c != null) {
                aYVar.a(ah.n);
                aYVar.a(ahVar.c);
                aYVar.c();
            }
            aYVar.a(ah.o);
            aYVar.a(ahVar.d);
            aYVar.c();
            aYVar.a(ah.p);
            aYVar.a(ahVar.e);
            aYVar.c();
            aYVar.a(ah.q);
            aYVar.a(ahVar.f);
            aYVar.c();
            if (ahVar.g != null) {
                aYVar.a(ah.r);
                aYVar.a(ahVar.g);
                aYVar.c();
            }
            if (ahVar.h != null) {
                aYVar.a(ah.s);
                aYVar.a(ahVar.h);
                aYVar.c();
            }
            if (ahVar.i != null) {
                aYVar.a(ah.t);
                aYVar.a(ahVar.i);
                aYVar.c();
            }
            aYVar.d();
            aYVar.b();
        }
    }

    /* compiled from: UMEnvelope */
    private static class c extends bj<ah> {
        private c() {
        }

        /* synthetic */ c(c cVar) {
            this();
        }

        /* renamed from: a */
        public void b(aY aYVar, ah ahVar) throws aF {
            be beVar = (be) aYVar;
            beVar.a(ahVar.a);
            beVar.a(ahVar.b);
            beVar.a(ahVar.c);
            beVar.a(ahVar.d);
            beVar.a(ahVar.e);
            beVar.a(ahVar.f);
            beVar.a(ahVar.g);
            beVar.a(ahVar.h);
            beVar.a(ahVar.i);
        }

        /* renamed from: b */
        public void a(aY aYVar, ah ahVar) throws aF {
            be beVar = (be) aYVar;
            ahVar.a = beVar.z();
            ahVar.a(true);
            ahVar.b = beVar.z();
            ahVar.b(true);
            ahVar.c = beVar.z();
            ahVar.c(true);
            ahVar.d = beVar.w();
            ahVar.d(true);
            ahVar.e = beVar.w();
            ahVar.e(true);
            ahVar.f = beVar.w();
            ahVar.f(true);
            ahVar.g = beVar.A();
            ahVar.g(true);
            ahVar.h = beVar.z();
            ahVar.h(true);
            ahVar.i = beVar.z();
            ahVar.i(true);
        }
    }

    static {
        u.put(bi.class, new b());
        u.put(bj.class, new d());
        EnumMap enumMap = new EnumMap(e.class);
        enumMap.put(e.VERSION, new aL("version", (byte) 1, new aM((byte) 11)));
        enumMap.put(e.ADDRESS, new aL("address", (byte) 1, new aM((byte) 11)));
        enumMap.put(e.SIGNATURE, new aL("signature", (byte) 1, new aM((byte) 11)));
        enumMap.put(e.SERIAL_NUM, new aL("serial_num", (byte) 1, new aM((byte) 8)));
        enumMap.put(e.TS_SECS, new aL("ts_secs", (byte) 1, new aM((byte) 8)));
        enumMap.put(e.LENGTH, new aL("length", (byte) 1, new aM((byte) 8)));
        enumMap.put(e.ENTITY, new aL("entity", (byte) 1, new aM((byte) 11, true)));
        enumMap.put(e.GUID, new aL("guid", (byte) 1, new aM((byte) 11)));
        enumMap.put(e.CHECKSUM, new aL("checksum", (byte) 1, new aM((byte) 11)));
        j = Collections.unmodifiableMap(enumMap);
        aL.a(ah.class, j);
    }

    public ah() {
        this.y = (byte) 0;
    }

    public ah(String str, String str2, String str3, int i, int i2, int i3, ByteBuffer byteBuffer, String str4, String str5) {
        this();
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = i;
        d(true);
        this.e = i2;
        e(true);
        this.f = i3;
        f(true);
        this.g = byteBuffer;
        this.h = str4;
        this.i = str5;
    }

    public ah(ah ahVar) {
        this.y = (byte) 0;
        this.y = ahVar.y;
        if (ahVar.e()) {
            this.a = ahVar.a;
        }
        if (ahVar.i()) {
            this.b = ahVar.b;
        }
        if (ahVar.l()) {
            this.c = ahVar.c;
        }
        this.d = ahVar.d;
        this.e = ahVar.e;
        this.f = ahVar.f;
        if (ahVar.y()) {
            this.g = aA.d(ahVar.g);
        }
        if (ahVar.B()) {
            this.h = ahVar.h;
        }
        if (ahVar.E()) {
            this.i = ahVar.i;
        }
    }

    /* renamed from: a */
    public ah g() {
        return new ah(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
        this.c = null;
        d(false);
        this.d = 0;
        e(false);
        this.e = 0;
        f(false);
        this.f = 0;
        this.g = null;
        this.h = null;
        this.i = null;
    }

    public String c() {
        return this.a;
    }

    public ah a(String str) {
        this.a = str;
        return this;
    }

    public void d() {
        this.a = null;
    }

    public boolean e() {
        return this.a != null;
    }

    public void a(boolean z) {
        if (!z) {
            this.a = null;
        }
    }

    public String f() {
        return this.b;
    }

    public ah b(String str) {
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

    public String j() {
        return this.c;
    }

    public ah c(String str) {
        this.c = str;
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

    public int m() {
        return this.d;
    }

    public ah a(int i) {
        this.d = i;
        d(true);
        return this;
    }

    public void n() {
        this.y = aw.b(this.y, 0);
    }

    public boolean o() {
        return aw.a(this.y, 0);
    }

    public void d(boolean z) {
        this.y = aw.a(this.y, 0, z);
    }

    public int p() {
        return this.e;
    }

    public ah c(int i) {
        this.e = i;
        e(true);
        return this;
    }

    public void q() {
        this.y = aw.b(this.y, 1);
    }

    public boolean r() {
        return aw.a(this.y, 1);
    }

    public void e(boolean z) {
        this.y = aw.a(this.y, 1, z);
    }

    public int s() {
        return this.f;
    }

    public ah d(int i) {
        this.f = i;
        f(true);
        return this;
    }

    public void t() {
        this.y = aw.b(this.y, 2);
    }

    public boolean u() {
        return aw.a(this.y, 2);
    }

    public void f(boolean z) {
        this.y = aw.a(this.y, 2, z);
    }

    public byte[] v() {
        a(aA.c(this.g));
        return this.g == null ? null : this.g.array();
    }

    public ByteBuffer w() {
        return this.g;
    }

    public ah a(byte[] bArr) {
        a(bArr == null ? null : ByteBuffer.wrap(bArr));
        return this;
    }

    public ah a(ByteBuffer byteBuffer) {
        this.g = byteBuffer;
        return this;
    }

    public void x() {
        this.g = null;
    }

    public boolean y() {
        return this.g != null;
    }

    public void g(boolean z) {
        if (!z) {
            this.g = null;
        }
    }

    public String z() {
        return this.h;
    }

    public ah d(String str) {
        this.h = str;
        return this;
    }

    public void A() {
        this.h = null;
    }

    public boolean B() {
        return this.h != null;
    }

    public void h(boolean z) {
        if (!z) {
            this.h = null;
        }
    }

    public String C() {
        return this.i;
    }

    public ah e(String str) {
        this.i = str;
        return this;
    }

    public void D() {
        this.i = null;
    }

    public boolean E() {
        return this.i != null;
    }

    public void i(boolean z) {
        if (!z) {
            this.i = null;
        }
    }

    /* renamed from: e */
    public e b(int i) {
        return e.a(i);
    }

    public void a(aY aYVar) throws aF {
        ((bh) u.get(aYVar.D())).b().a(aYVar, this);
    }

    public void b(aY aYVar) throws aF {
        ((bh) u.get(aYVar.D())).b().b(aYVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("UMEnvelope(");
        stringBuilder.append("version:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("address:");
        if (this.b == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.b);
        }
        stringBuilder.append(", ");
        stringBuilder.append("signature:");
        if (this.c == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.c);
        }
        stringBuilder.append(", ");
        stringBuilder.append("serial_num:");
        stringBuilder.append(this.d);
        stringBuilder.append(", ");
        stringBuilder.append("ts_secs:");
        stringBuilder.append(this.e);
        stringBuilder.append(", ");
        stringBuilder.append("length:");
        stringBuilder.append(this.f);
        stringBuilder.append(", ");
        stringBuilder.append("entity:");
        if (this.g == null) {
            stringBuilder.append("null");
        } else {
            aA.a(this.g, stringBuilder);
        }
        stringBuilder.append(", ");
        stringBuilder.append("guid:");
        if (this.h == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.h);
        }
        stringBuilder.append(", ");
        stringBuilder.append("checksum:");
        if (this.i == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.i);
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void F() throws aF {
        if (this.a == null) {
            throw new aZ("Required field 'version' was not present! Struct: " + toString());
        } else if (this.b == null) {
            throw new aZ("Required field 'address' was not present! Struct: " + toString());
        } else if (this.c == null) {
            throw new aZ("Required field 'signature' was not present! Struct: " + toString());
        } else if (this.g == null) {
            throw new aZ("Required field 'entity' was not present! Struct: " + toString());
        } else if (this.h == null) {
            throw new aZ("Required field 'guid' was not present! Struct: " + toString());
        } else if (this.i == null) {
            throw new aZ("Required field 'checksum' was not present! Struct: " + toString());
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
            this.y = (byte) 0;
            a(new aS(new bk((InputStream) objectInputStream)));
        } catch (aF e) {
            throw new IOException(e.getMessage());
        }
    }
}
