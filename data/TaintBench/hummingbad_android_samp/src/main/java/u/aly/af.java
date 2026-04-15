package u.aly;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* compiled from: UALogEntry */
public class af implements Serializable, Cloneable, az<af, e> {
    public static final Map<e, aL> j;
    /* access modifiers changed from: private|static|final */
    public static final bd k = new bd("UALogEntry");
    /* access modifiers changed from: private|static|final */
    public static final aT l = new aT("client_stats", (byte) 12, (short) 1);
    /* access modifiers changed from: private|static|final */
    public static final aT m = new aT("app_info", (byte) 12, (short) 2);
    /* access modifiers changed from: private|static|final */
    public static final aT n = new aT("device_info", (byte) 12, (short) 3);
    /* access modifiers changed from: private|static|final */
    public static final aT o = new aT("misc_info", (byte) 12, (short) 4);
    /* access modifiers changed from: private|static|final */
    public static final aT p = new aT("activate_msg", (byte) 12, (short) 5);
    /* access modifiers changed from: private|static|final */
    public static final aT q = new aT("instant_msgs", bf.m, (short) 6);
    /* access modifiers changed from: private|static|final */
    public static final aT r = new aT("sessions", bf.m, (short) 7);
    /* access modifiers changed from: private|static|final */
    public static final aT s = new aT("imprint", (byte) 12, (short) 8);
    /* access modifiers changed from: private|static|final */
    public static final aT t = new aT("id_tracking", (byte) 12, (short) 9);
    private static final Map<Class<? extends bg>, bh> u = new HashMap();
    public J a;
    public I b;
    public K c;
    public X d;
    public H e;
    public List<V> f;
    public List<ad> g;
    public T h;
    public S i;
    private e[] v;

    /* compiled from: UALogEntry */
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

    /* compiled from: UALogEntry */
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

    /* compiled from: UALogEntry */
    public enum e implements aG {
        CLIENT_STATS((short) 1, "client_stats"),
        APP_INFO((short) 2, "app_info"),
        DEVICE_INFO((short) 3, "device_info"),
        MISC_INFO((short) 4, "misc_info"),
        ACTIVATE_MSG((short) 5, "activate_msg"),
        INSTANT_MSGS((short) 6, "instant_msgs"),
        SESSIONS((short) 7, "sessions"),
        IMPRINT((short) 8, "imprint"),
        ID_TRACKING((short) 9, "id_tracking");
        
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
                    return CLIENT_STATS;
                case 2:
                    return APP_INFO;
                case 3:
                    return DEVICE_INFO;
                case 4:
                    return MISC_INFO;
                case 5:
                    return ACTIVATE_MSG;
                case 6:
                    return INSTANT_MSGS;
                case 7:
                    return SESSIONS;
                case 8:
                    return IMPRINT;
                case 9:
                    return ID_TRACKING;
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

    /* compiled from: UALogEntry */
    private static class a extends bi<af> {
        private a() {
        }

        /* synthetic */ a(a aVar) {
            this();
        }

        public void a(aY aYVar, af afVar) throws aF {
            aYVar.j();
            while (true) {
                aT l = aYVar.l();
                if (l.b == (byte) 0) {
                    aYVar.k();
                    afVar.I();
                    return;
                }
                aU p;
                int i;
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        afVar.a = new J();
                        afVar.a.a(aYVar);
                        afVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        afVar.b = new I();
                        afVar.b.a(aYVar);
                        afVar.b(true);
                        break;
                    case (short) 3:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        afVar.c = new K();
                        afVar.c.a(aYVar);
                        afVar.c(true);
                        break;
                    case (short) 4:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        afVar.d = new X();
                        afVar.d.a(aYVar);
                        afVar.d(true);
                        break;
                    case (short) 5:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        afVar.e = new H();
                        afVar.e.a(aYVar);
                        afVar.e(true);
                        break;
                    case (short) 6:
                        if (l.b != bf.m) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        p = aYVar.p();
                        afVar.f = new ArrayList(p.b);
                        for (i = 0; i < p.b; i++) {
                            V v = new V();
                            v.a(aYVar);
                            afVar.f.add(v);
                        }
                        aYVar.q();
                        afVar.f(true);
                        break;
                    case (short) 7:
                        if (l.b != bf.m) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        p = aYVar.p();
                        afVar.g = new ArrayList(p.b);
                        for (i = 0; i < p.b; i++) {
                            ad adVar = new ad();
                            adVar.a(aYVar);
                            afVar.g.add(adVar);
                        }
                        aYVar.q();
                        afVar.g(true);
                        break;
                    case (short) 8:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        afVar.h = new T();
                        afVar.h.a(aYVar);
                        afVar.h(true);
                        break;
                    case (short) 9:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        afVar.i = new S();
                        afVar.i.a(aYVar);
                        afVar.i(true);
                        break;
                    default:
                        bb.a(aYVar, l.b);
                        break;
                }
                aYVar.m();
            }
        }

        public void b(aY aYVar, af afVar) throws aF {
            afVar.I();
            aYVar.a(af.k);
            if (afVar.a != null) {
                aYVar.a(af.l);
                afVar.a.b(aYVar);
                aYVar.c();
            }
            if (afVar.b != null) {
                aYVar.a(af.m);
                afVar.b.b(aYVar);
                aYVar.c();
            }
            if (afVar.c != null) {
                aYVar.a(af.n);
                afVar.c.b(aYVar);
                aYVar.c();
            }
            if (afVar.d != null) {
                aYVar.a(af.o);
                afVar.d.b(aYVar);
                aYVar.c();
            }
            if (afVar.e != null && afVar.r()) {
                aYVar.a(af.p);
                afVar.e.b(aYVar);
                aYVar.c();
            }
            if (afVar.f != null && afVar.w()) {
                aYVar.a(af.q);
                aYVar.a(new aU((byte) 12, afVar.f.size()));
                for (V b : afVar.f) {
                    b.b(aYVar);
                }
                aYVar.f();
                aYVar.c();
            }
            if (afVar.g != null && afVar.B()) {
                aYVar.a(af.r);
                aYVar.a(new aU((byte) 12, afVar.g.size()));
                for (ad b2 : afVar.g) {
                    b2.b(aYVar);
                }
                aYVar.f();
                aYVar.c();
            }
            if (afVar.h != null && afVar.E()) {
                aYVar.a(af.s);
                afVar.h.b(aYVar);
                aYVar.c();
            }
            if (afVar.i != null && afVar.H()) {
                aYVar.a(af.t);
                afVar.i.b(aYVar);
                aYVar.c();
            }
            aYVar.d();
            aYVar.b();
        }
    }

    /* compiled from: UALogEntry */
    private static class c extends bj<af> {
        private c() {
        }

        /* synthetic */ c(c cVar) {
            this();
        }

        /* renamed from: a */
        public void b(aY aYVar, af afVar) throws aF {
            aYVar = (be) aYVar;
            afVar.a.b(aYVar);
            afVar.b.b(aYVar);
            afVar.c.b(aYVar);
            afVar.d.b(aYVar);
            BitSet bitSet = new BitSet();
            if (afVar.r()) {
                bitSet.set(0);
            }
            if (afVar.w()) {
                bitSet.set(1);
            }
            if (afVar.B()) {
                bitSet.set(2);
            }
            if (afVar.E()) {
                bitSet.set(3);
            }
            if (afVar.H()) {
                bitSet.set(4);
            }
            aYVar.a(bitSet, 5);
            if (afVar.r()) {
                afVar.e.b(aYVar);
            }
            if (afVar.w()) {
                aYVar.a(afVar.f.size());
                for (V b : afVar.f) {
                    b.b(aYVar);
                }
            }
            if (afVar.B()) {
                aYVar.a(afVar.g.size());
                for (ad b2 : afVar.g) {
                    b2.b(aYVar);
                }
            }
            if (afVar.E()) {
                afVar.h.b(aYVar);
            }
            if (afVar.H()) {
                afVar.i.b(aYVar);
            }
        }

        /* renamed from: b */
        public void a(aY aYVar, af afVar) throws aF {
            int i = 0;
            aYVar = (be) aYVar;
            afVar.a = new J();
            afVar.a.a(aYVar);
            afVar.a(true);
            afVar.b = new I();
            afVar.b.a(aYVar);
            afVar.b(true);
            afVar.c = new K();
            afVar.c.a(aYVar);
            afVar.c(true);
            afVar.d = new X();
            afVar.d.a(aYVar);
            afVar.d(true);
            BitSet b = aYVar.b(5);
            if (b.get(0)) {
                afVar.e = new H();
                afVar.e.a(aYVar);
                afVar.e(true);
            }
            if (b.get(1)) {
                aU aUVar = new aU((byte) 12, aYVar.w());
                afVar.f = new ArrayList(aUVar.b);
                for (int i2 = 0; i2 < aUVar.b; i2++) {
                    V v = new V();
                    v.a(aYVar);
                    afVar.f.add(v);
                }
                afVar.f(true);
            }
            if (b.get(2)) {
                aU aUVar2 = new aU((byte) 12, aYVar.w());
                afVar.g = new ArrayList(aUVar2.b);
                while (i < aUVar2.b) {
                    ad adVar = new ad();
                    adVar.a(aYVar);
                    afVar.g.add(adVar);
                    i++;
                }
                afVar.g(true);
            }
            if (b.get(3)) {
                afVar.h = new T();
                afVar.h.a(aYVar);
                afVar.h(true);
            }
            if (b.get(4)) {
                afVar.i = new S();
                afVar.i.a(aYVar);
                afVar.i(true);
            }
        }
    }

    static {
        u.put(bi.class, new b());
        u.put(bj.class, new d());
        EnumMap enumMap = new EnumMap(e.class);
        enumMap.put(e.CLIENT_STATS, new aL("client_stats", (byte) 1, new aQ((byte) 12, J.class)));
        enumMap.put(e.APP_INFO, new aL("app_info", (byte) 1, new aQ((byte) 12, I.class)));
        enumMap.put(e.DEVICE_INFO, new aL("device_info", (byte) 1, new aQ((byte) 12, K.class)));
        enumMap.put(e.MISC_INFO, new aL("misc_info", (byte) 1, new aQ((byte) 12, X.class)));
        enumMap.put(e.ACTIVATE_MSG, new aL("activate_msg", (byte) 2, new aQ((byte) 12, H.class)));
        enumMap.put(e.INSTANT_MSGS, new aL("instant_msgs", (byte) 2, new aN(bf.m, new aQ((byte) 12, V.class))));
        enumMap.put(e.SESSIONS, new aL("sessions", (byte) 2, new aN(bf.m, new aQ((byte) 12, ad.class))));
        enumMap.put(e.IMPRINT, new aL("imprint", (byte) 2, new aQ((byte) 12, T.class)));
        enumMap.put(e.ID_TRACKING, new aL("id_tracking", (byte) 2, new aQ((byte) 12, S.class)));
        j = Collections.unmodifiableMap(enumMap);
        aL.a(af.class, j);
    }

    public af() {
        this.v = new e[]{e.ACTIVATE_MSG, e.INSTANT_MSGS, e.SESSIONS, e.IMPRINT, e.ID_TRACKING};
    }

    public af(J j, I i, K k, X x) {
        this();
        this.a = j;
        this.b = i;
        this.c = k;
        this.d = x;
    }

    public af(af afVar) {
        ArrayList arrayList;
        this.v = new e[]{e.ACTIVATE_MSG, e.INSTANT_MSGS, e.SESSIONS, e.IMPRINT, e.ID_TRACKING};
        if (afVar.e()) {
            this.a = new J(afVar.a);
        }
        if (afVar.i()) {
            this.b = new I(afVar.b);
        }
        if (afVar.l()) {
            this.c = new K(afVar.c);
        }
        if (afVar.o()) {
            this.d = new X(afVar.d);
        }
        if (afVar.r()) {
            this.e = new H(afVar.e);
        }
        if (afVar.w()) {
            arrayList = new ArrayList();
            for (V v : afVar.f) {
                arrayList.add(new V(v));
            }
            this.f = arrayList;
        }
        if (afVar.B()) {
            arrayList = new ArrayList();
            for (ad adVar : afVar.g) {
                arrayList.add(new ad(adVar));
            }
            this.g = arrayList;
        }
        if (afVar.E()) {
            this.h = new T(afVar.h);
        }
        if (afVar.H()) {
            this.i = new S(afVar.i);
        }
    }

    /* renamed from: a */
    public af g() {
        return new af(this);
    }

    public void b() {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
    }

    public J c() {
        return this.a;
    }

    public af a(J j) {
        this.a = j;
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

    public I f() {
        return this.b;
    }

    public af a(I i) {
        this.b = i;
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

    public K j() {
        return this.c;
    }

    public af a(K k) {
        this.c = k;
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

    public X m() {
        return this.d;
    }

    public af a(X x) {
        this.d = x;
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

    public H p() {
        return this.e;
    }

    public af a(H h) {
        this.e = h;
        return this;
    }

    public void q() {
        this.e = null;
    }

    public boolean r() {
        return this.e != null;
    }

    public void e(boolean z) {
        if (!z) {
            this.e = null;
        }
    }

    public int s() {
        return this.f == null ? 0 : this.f.size();
    }

    public Iterator<V> t() {
        return this.f == null ? null : this.f.iterator();
    }

    public void a(V v) {
        if (this.f == null) {
            this.f = new ArrayList();
        }
        this.f.add(v);
    }

    public List<V> u() {
        return this.f;
    }

    public af a(List<V> list) {
        this.f = list;
        return this;
    }

    public void v() {
        this.f = null;
    }

    public boolean w() {
        return this.f != null;
    }

    public void f(boolean z) {
        if (!z) {
            this.f = null;
        }
    }

    public int x() {
        return this.g == null ? 0 : this.g.size();
    }

    public Iterator<ad> y() {
        return this.g == null ? null : this.g.iterator();
    }

    public void a(ad adVar) {
        if (this.g == null) {
            this.g = new ArrayList();
        }
        this.g.add(adVar);
    }

    public List<ad> z() {
        return this.g;
    }

    public af b(List<ad> list) {
        this.g = list;
        return this;
    }

    public void A() {
        this.g = null;
    }

    public boolean B() {
        return this.g != null;
    }

    public void g(boolean z) {
        if (!z) {
            this.g = null;
        }
    }

    public T C() {
        return this.h;
    }

    public af a(T t) {
        this.h = t;
        return this;
    }

    public void D() {
        this.h = null;
    }

    public boolean E() {
        return this.h != null;
    }

    public void h(boolean z) {
        if (!z) {
            this.h = null;
        }
    }

    public S F() {
        return this.i;
    }

    public af a(S s) {
        this.i = s;
        return this;
    }

    public void G() {
        this.i = null;
    }

    public boolean H() {
        return this.i != null;
    }

    public void i(boolean z) {
        if (!z) {
            this.i = null;
        }
    }

    /* renamed from: a */
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
        StringBuilder stringBuilder = new StringBuilder("UALogEntry(");
        stringBuilder.append("client_stats:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("app_info:");
        if (this.b == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.b);
        }
        stringBuilder.append(", ");
        stringBuilder.append("device_info:");
        if (this.c == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.c);
        }
        stringBuilder.append(", ");
        stringBuilder.append("misc_info:");
        if (this.d == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.d);
        }
        if (r()) {
            stringBuilder.append(", ");
            stringBuilder.append("activate_msg:");
            if (this.e == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.e);
            }
        }
        if (w()) {
            stringBuilder.append(", ");
            stringBuilder.append("instant_msgs:");
            if (this.f == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.f);
            }
        }
        if (B()) {
            stringBuilder.append(", ");
            stringBuilder.append("sessions:");
            if (this.g == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.g);
            }
        }
        if (E()) {
            stringBuilder.append(", ");
            stringBuilder.append("imprint:");
            if (this.h == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.h);
            }
        }
        if (H()) {
            stringBuilder.append(", ");
            stringBuilder.append("id_tracking:");
            if (this.i == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.i);
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void I() throws aF {
        if (this.a == null) {
            throw new aZ("Required field 'client_stats' was not present! Struct: " + toString());
        } else if (this.b == null) {
            throw new aZ("Required field 'app_info' was not present! Struct: " + toString());
        } else if (this.c == null) {
            throw new aZ("Required field 'device_info' was not present! Struct: " + toString());
        } else if (this.d == null) {
            throw new aZ("Required field 'misc_info' was not present! Struct: " + toString());
        } else {
            if (this.a != null) {
                this.a.m();
            }
            if (this.b != null) {
                this.b.H();
            }
            if (this.c != null) {
                this.c.ac();
            }
            if (this.d != null) {
                this.d.K();
            }
            if (this.e != null) {
                this.e.f();
            }
            if (this.h != null) {
                this.h.n();
            }
            if (this.i != null) {
                this.i.p();
            }
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
            a(new aS(new bk((InputStream) objectInputStream)));
        } catch (aF e) {
            throw new IOException(e.getMessage());
        }
    }
}
