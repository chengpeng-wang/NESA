package u.aly;

import com.moceanmobile.mast.MASTNativeAdConstants;
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

/* compiled from: UserInfo */
public class ag implements Serializable, Cloneable, az<ag, e> {
    public static final Map<e, aL> e;
    /* access modifiers changed from: private|static|final */
    public static final bd f = new bd("UserInfo");
    /* access modifiers changed from: private|static|final */
    public static final aT g = new aT("gender", (byte) 8, (short) 1);
    /* access modifiers changed from: private|static|final */
    public static final aT h = new aT("age", (byte) 8, (short) 2);
    /* access modifiers changed from: private|static|final */
    public static final aT i = new aT(MASTNativeAdConstants.ID_STRING, (byte) 11, (short) 3);
    /* access modifiers changed from: private|static|final */
    public static final aT j = new aT(MASTNativeAdConstants.RESPONSE_MEDIATION_SOURCE, (byte) 11, (short) 4);
    private static final Map<Class<? extends bg>, bh> k = new HashMap();
    private static final int l = 0;
    public P a;
    public int b;
    public String c;
    public String d;
    private byte m;
    private e[] n;

    /* compiled from: UserInfo */
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

    /* compiled from: UserInfo */
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

    /* compiled from: UserInfo */
    public enum e implements aG {
        GENDER((short) 1, "gender"),
        AGE((short) 2, "age"),
        ID((short) 3, MASTNativeAdConstants.ID_STRING),
        SOURCE((short) 4, MASTNativeAdConstants.RESPONSE_MEDIATION_SOURCE);
        
        private static final Map<String, e> e = null;
        private final short f;
        private final String g;

        static {
            e = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                e.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return GENDER;
                case 2:
                    return AGE;
                case 3:
                    return ID;
                case 4:
                    return SOURCE;
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
            return (e) e.get(str);
        }

        private e(short s, String str) {
            this.f = s;
            this.g = str;
        }

        public short a() {
            return this.f;
        }

        public String b() {
            return this.g;
        }
    }

    /* compiled from: UserInfo */
    private static class a extends bi<ag> {
        private a() {
        }

        /* synthetic */ a(a aVar) {
            this();
        }

        public void a(aY aYVar, ag agVar) throws aF {
            aYVar.j();
            while (true) {
                aT l = aYVar.l();
                if (l.b == (byte) 0) {
                    aYVar.k();
                    agVar.p();
                    return;
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        agVar.a = P.a(aYVar.w());
                        agVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        agVar.b = aYVar.w();
                        agVar.b(true);
                        break;
                    case (short) 3:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        agVar.c = aYVar.z();
                        agVar.c(true);
                        break;
                    case (short) 4:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        agVar.d = aYVar.z();
                        agVar.d(true);
                        break;
                    default:
                        bb.a(aYVar, l.b);
                        break;
                }
                aYVar.m();
            }
        }

        public void b(aY aYVar, ag agVar) throws aF {
            agVar.p();
            aYVar.a(ag.f);
            if (agVar.a != null && agVar.e()) {
                aYVar.a(ag.g);
                aYVar.a(agVar.a.a());
                aYVar.c();
            }
            if (agVar.i()) {
                aYVar.a(ag.h);
                aYVar.a(agVar.b);
                aYVar.c();
            }
            if (agVar.c != null && agVar.l()) {
                aYVar.a(ag.i);
                aYVar.a(agVar.c);
                aYVar.c();
            }
            if (agVar.d != null && agVar.o()) {
                aYVar.a(ag.j);
                aYVar.a(agVar.d);
                aYVar.c();
            }
            aYVar.d();
            aYVar.b();
        }
    }

    /* compiled from: UserInfo */
    private static class c extends bj<ag> {
        private c() {
        }

        /* synthetic */ c(c cVar) {
            this();
        }

        /* renamed from: a */
        public void b(aY aYVar, ag agVar) throws aF {
            be beVar = (be) aYVar;
            BitSet bitSet = new BitSet();
            if (agVar.e()) {
                bitSet.set(0);
            }
            if (agVar.i()) {
                bitSet.set(1);
            }
            if (agVar.l()) {
                bitSet.set(2);
            }
            if (agVar.o()) {
                bitSet.set(3);
            }
            beVar.a(bitSet, 4);
            if (agVar.e()) {
                beVar.a(agVar.a.a());
            }
            if (agVar.i()) {
                beVar.a(agVar.b);
            }
            if (agVar.l()) {
                beVar.a(agVar.c);
            }
            if (agVar.o()) {
                beVar.a(agVar.d);
            }
        }

        /* renamed from: b */
        public void a(aY aYVar, ag agVar) throws aF {
            be beVar = (be) aYVar;
            BitSet b = beVar.b(4);
            if (b.get(0)) {
                agVar.a = P.a(beVar.w());
                agVar.a(true);
            }
            if (b.get(1)) {
                agVar.b = beVar.w();
                agVar.b(true);
            }
            if (b.get(2)) {
                agVar.c = beVar.z();
                agVar.c(true);
            }
            if (b.get(3)) {
                agVar.d = beVar.z();
                agVar.d(true);
            }
        }
    }

    static {
        k.put(bi.class, new b());
        k.put(bj.class, new d());
        EnumMap enumMap = new EnumMap(e.class);
        enumMap.put(e.GENDER, new aL("gender", (byte) 2, new aK(bf.n, P.class)));
        enumMap.put(e.AGE, new aL("age", (byte) 2, new aM((byte) 8)));
        enumMap.put(e.ID, new aL(MASTNativeAdConstants.ID_STRING, (byte) 2, new aM((byte) 11)));
        enumMap.put(e.SOURCE, new aL(MASTNativeAdConstants.RESPONSE_MEDIATION_SOURCE, (byte) 2, new aM((byte) 11)));
        e = Collections.unmodifiableMap(enumMap);
        aL.a(ag.class, e);
    }

    public ag() {
        this.m = (byte) 0;
        this.n = new e[]{e.GENDER, e.AGE, e.ID, e.SOURCE};
    }

    public ag(ag agVar) {
        this.m = (byte) 0;
        this.n = new e[]{e.GENDER, e.AGE, e.ID, e.SOURCE};
        this.m = agVar.m;
        if (agVar.e()) {
            this.a = agVar.a;
        }
        this.b = agVar.b;
        if (agVar.l()) {
            this.c = agVar.c;
        }
        if (agVar.o()) {
            this.d = agVar.d;
        }
    }

    /* renamed from: a */
    public ag g() {
        return new ag(this);
    }

    public void b() {
        this.a = null;
        b(false);
        this.b = 0;
        this.c = null;
        this.d = null;
    }

    public P c() {
        return this.a;
    }

    public ag a(P p) {
        this.a = p;
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

    public int f() {
        return this.b;
    }

    public ag a(int i) {
        this.b = i;
        b(true);
        return this;
    }

    public void h() {
        this.m = aw.b(this.m, 0);
    }

    public boolean i() {
        return aw.a(this.m, 0);
    }

    public void b(boolean z) {
        this.m = aw.a(this.m, 0, z);
    }

    public String j() {
        return this.c;
    }

    public ag a(String str) {
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

    public String m() {
        return this.d;
    }

    public ag b(String str) {
        this.d = str;
        return this;
    }

    public void n() {
        this.d = null;
    }

    public boolean o() {
        return this.d != null;
    }

    public void d(boolean z) {
        if (!z) {
            this.d = null;
        }
    }

    /* renamed from: c */
    public e b(int i) {
        return e.a(i);
    }

    public void a(aY aYVar) throws aF {
        ((bh) k.get(aYVar.D())).b().a(aYVar, this);
    }

    public void b(aY aYVar) throws aF {
        ((bh) k.get(aYVar.D())).b().b(aYVar, this);
    }

    public String toString() {
        Object obj = null;
        StringBuilder stringBuilder = new StringBuilder("UserInfo(");
        Object obj2 = 1;
        if (e()) {
            stringBuilder.append("gender:");
            if (this.a == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.a);
            }
            obj2 = null;
        }
        if (i()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("age:");
            stringBuilder.append(this.b);
            obj2 = null;
        }
        if (l()) {
            if (obj2 == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("id:");
            if (this.c == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.c);
            }
        } else {
            obj = obj2;
        }
        if (o()) {
            if (obj == null) {
                stringBuilder.append(", ");
            }
            stringBuilder.append("source:");
            if (this.d == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.d);
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void p() throws aF {
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
            this.m = (byte) 0;
            a(new aS(new bk((InputStream) objectInputStream)));
        } catch (aF e) {
            throw new IOException(e.getMessage());
        }
    }
}
