package u.aly;

import com.moceanmobile.mast.MASTNativeAdConstants;
import com.mopub.mobileads.VastIconXmlManager;
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

/* compiled from: Session */
public class ad implements Serializable, Cloneable, az<ad, e> {
    public static final Map<e, aL> h;
    /* access modifiers changed from: private|static|final */
    public static final bd i = new bd("Session");
    /* access modifiers changed from: private|static|final */
    public static final aT j = new aT(MASTNativeAdConstants.ID_STRING, (byte) 11, (short) 1);
    /* access modifiers changed from: private|static|final */
    public static final aT k = new aT("start_time", (byte) 10, (short) 2);
    /* access modifiers changed from: private|static|final */
    public static final aT l = new aT("end_time", (byte) 10, (short) 3);
    /* access modifiers changed from: private|static|final */
    public static final aT m = new aT(VastIconXmlManager.DURATION, (byte) 10, (short) 4);
    /* access modifiers changed from: private|static|final */
    public static final aT n = new aT("pages", bf.m, (short) 5);
    /* access modifiers changed from: private|static|final */
    public static final aT o = new aT("locations", bf.m, (short) 6);
    /* access modifiers changed from: private|static|final */
    public static final aT p = new aT("traffic", (byte) 12, (short) 7);
    private static final Map<Class<? extends bg>, bh> q = new HashMap();
    private static final int r = 0;
    private static final int s = 1;
    private static final int t = 2;
    public String a;
    public long b;
    public long c;
    public long d;
    public List<Y> e;
    public List<W> f;
    public ae g;
    private byte u;
    private e[] v;

    /* compiled from: Session */
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

    /* compiled from: Session */
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

    /* compiled from: Session */
    public enum e implements aG {
        ID((short) 1, MASTNativeAdConstants.ID_STRING),
        START_TIME((short) 2, "start_time"),
        END_TIME((short) 3, "end_time"),
        DURATION((short) 4, VastIconXmlManager.DURATION),
        PAGES((short) 5, "pages"),
        LOCATIONS((short) 6, "locations"),
        TRAFFIC((short) 7, "traffic");
        
        private static final Map<String, e> h = null;
        private final short i;
        private final String j;

        static {
            h = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                h.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return ID;
                case 2:
                    return START_TIME;
                case 3:
                    return END_TIME;
                case 4:
                    return DURATION;
                case 5:
                    return PAGES;
                case 6:
                    return LOCATIONS;
                case 7:
                    return TRAFFIC;
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
            return (e) h.get(str);
        }

        private e(short s, String str) {
            this.i = s;
            this.j = str;
        }

        public short a() {
            return this.i;
        }

        public String b() {
            return this.j;
        }
    }

    /* compiled from: Session */
    private static class a extends bi<ad> {
        private a() {
        }

        /* synthetic */ a(a aVar) {
            this();
        }

        public void a(aY aYVar, ad adVar) throws aF {
            aYVar.j();
            while (true) {
                aT l = aYVar.l();
                if (l.b == (byte) 0) {
                    aYVar.k();
                    if (!adVar.i()) {
                        throw new aZ("Required field 'start_time' was not found in serialized data! Struct: " + toString());
                    } else if (!adVar.l()) {
                        throw new aZ("Required field 'end_time' was not found in serialized data! Struct: " + toString());
                    } else if (adVar.o()) {
                        adVar.C();
                        return;
                    } else {
                        throw new aZ("Required field 'duration' was not found in serialized data! Struct: " + toString());
                    }
                }
                aU p;
                int i;
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 11) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        adVar.a = aYVar.z();
                        adVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 10) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        adVar.b = aYVar.x();
                        adVar.b(true);
                        break;
                    case (short) 3:
                        if (l.b != (byte) 10) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        adVar.c = aYVar.x();
                        adVar.c(true);
                        break;
                    case (short) 4:
                        if (l.b != (byte) 10) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        adVar.d = aYVar.x();
                        adVar.d(true);
                        break;
                    case (short) 5:
                        if (l.b != bf.m) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        p = aYVar.p();
                        adVar.e = new ArrayList(p.b);
                        for (i = 0; i < p.b; i++) {
                            Y y = new Y();
                            y.a(aYVar);
                            adVar.e.add(y);
                        }
                        aYVar.q();
                        adVar.e(true);
                        break;
                    case (short) 6:
                        if (l.b != bf.m) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        p = aYVar.p();
                        adVar.f = new ArrayList(p.b);
                        for (i = 0; i < p.b; i++) {
                            W w = new W();
                            w.a(aYVar);
                            adVar.f.add(w);
                        }
                        aYVar.q();
                        adVar.f(true);
                        break;
                    case (short) 7:
                        if (l.b != (byte) 12) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        adVar.g = new ae();
                        adVar.g.a(aYVar);
                        adVar.g(true);
                        break;
                    default:
                        bb.a(aYVar, l.b);
                        break;
                }
                aYVar.m();
            }
        }

        public void b(aY aYVar, ad adVar) throws aF {
            adVar.C();
            aYVar.a(ad.i);
            if (adVar.a != null) {
                aYVar.a(ad.j);
                aYVar.a(adVar.a);
                aYVar.c();
            }
            aYVar.a(ad.k);
            aYVar.a(adVar.b);
            aYVar.c();
            aYVar.a(ad.l);
            aYVar.a(adVar.c);
            aYVar.c();
            aYVar.a(ad.m);
            aYVar.a(adVar.d);
            aYVar.c();
            if (adVar.e != null && adVar.t()) {
                aYVar.a(ad.n);
                aYVar.a(new aU((byte) 12, adVar.e.size()));
                for (Y b : adVar.e) {
                    b.b(aYVar);
                }
                aYVar.f();
                aYVar.c();
            }
            if (adVar.f != null && adVar.y()) {
                aYVar.a(ad.o);
                aYVar.a(new aU((byte) 12, adVar.f.size()));
                for (W b2 : adVar.f) {
                    b2.b(aYVar);
                }
                aYVar.f();
                aYVar.c();
            }
            if (adVar.g != null && adVar.B()) {
                aYVar.a(ad.p);
                adVar.g.b(aYVar);
                aYVar.c();
            }
            aYVar.d();
            aYVar.b();
        }
    }

    /* compiled from: Session */
    private static class c extends bj<ad> {
        private c() {
        }

        /* synthetic */ c(c cVar) {
            this();
        }

        /* renamed from: a */
        public void b(aY aYVar, ad adVar) throws aF {
            aYVar = (be) aYVar;
            aYVar.a(adVar.a);
            aYVar.a(adVar.b);
            aYVar.a(adVar.c);
            aYVar.a(adVar.d);
            BitSet bitSet = new BitSet();
            if (adVar.t()) {
                bitSet.set(0);
            }
            if (adVar.y()) {
                bitSet.set(1);
            }
            if (adVar.B()) {
                bitSet.set(2);
            }
            aYVar.a(bitSet, 3);
            if (adVar.t()) {
                aYVar.a(adVar.e.size());
                for (Y b : adVar.e) {
                    b.b(aYVar);
                }
            }
            if (adVar.y()) {
                aYVar.a(adVar.f.size());
                for (W b2 : adVar.f) {
                    b2.b(aYVar);
                }
            }
            if (adVar.B()) {
                adVar.g.b(aYVar);
            }
        }

        /* renamed from: b */
        public void a(aY aYVar, ad adVar) throws aF {
            int i = 0;
            aYVar = (be) aYVar;
            adVar.a = aYVar.z();
            adVar.a(true);
            adVar.b = aYVar.x();
            adVar.b(true);
            adVar.c = aYVar.x();
            adVar.c(true);
            adVar.d = aYVar.x();
            adVar.d(true);
            BitSet b = aYVar.b(3);
            if (b.get(0)) {
                aU aUVar = new aU((byte) 12, aYVar.w());
                adVar.e = new ArrayList(aUVar.b);
                for (int i2 = 0; i2 < aUVar.b; i2++) {
                    Y y = new Y();
                    y.a(aYVar);
                    adVar.e.add(y);
                }
                adVar.e(true);
            }
            if (b.get(1)) {
                aU aUVar2 = new aU((byte) 12, aYVar.w());
                adVar.f = new ArrayList(aUVar2.b);
                while (i < aUVar2.b) {
                    W w = new W();
                    w.a(aYVar);
                    adVar.f.add(w);
                    i++;
                }
                adVar.f(true);
            }
            if (b.get(2)) {
                adVar.g = new ae();
                adVar.g.a(aYVar);
                adVar.g(true);
            }
        }
    }

    static {
        q.put(bi.class, new b());
        q.put(bj.class, new d());
        EnumMap enumMap = new EnumMap(e.class);
        enumMap.put(e.ID, new aL(MASTNativeAdConstants.ID_STRING, (byte) 1, new aM((byte) 11)));
        enumMap.put(e.START_TIME, new aL("start_time", (byte) 1, new aM((byte) 10)));
        enumMap.put(e.END_TIME, new aL("end_time", (byte) 1, new aM((byte) 10)));
        enumMap.put(e.DURATION, new aL(VastIconXmlManager.DURATION, (byte) 1, new aM((byte) 10)));
        enumMap.put(e.PAGES, new aL("pages", (byte) 2, new aN(bf.m, new aQ((byte) 12, Y.class))));
        enumMap.put(e.LOCATIONS, new aL("locations", (byte) 2, new aN(bf.m, new aQ((byte) 12, W.class))));
        enumMap.put(e.TRAFFIC, new aL("traffic", (byte) 2, new aQ((byte) 12, ae.class)));
        h = Collections.unmodifiableMap(enumMap);
        aL.a(ad.class, h);
    }

    public ad() {
        this.u = (byte) 0;
        this.v = new e[]{e.PAGES, e.LOCATIONS, e.TRAFFIC};
    }

    public ad(String str, long j, long j2, long j3) {
        this();
        this.a = str;
        this.b = j;
        b(true);
        this.c = j2;
        c(true);
        this.d = j3;
        d(true);
    }

    public ad(ad adVar) {
        ArrayList arrayList;
        this.u = (byte) 0;
        this.v = new e[]{e.PAGES, e.LOCATIONS, e.TRAFFIC};
        this.u = adVar.u;
        if (adVar.e()) {
            this.a = adVar.a;
        }
        this.b = adVar.b;
        this.c = adVar.c;
        this.d = adVar.d;
        if (adVar.t()) {
            arrayList = new ArrayList();
            for (Y y : adVar.e) {
                arrayList.add(new Y(y));
            }
            this.e = arrayList;
        }
        if (adVar.y()) {
            arrayList = new ArrayList();
            for (W w : adVar.f) {
                arrayList.add(new W(w));
            }
            this.f = arrayList;
        }
        if (adVar.B()) {
            this.g = new ae(adVar.g);
        }
    }

    /* renamed from: a */
    public ad g() {
        return new ad(this);
    }

    public void b() {
        this.a = null;
        b(false);
        this.b = 0;
        c(false);
        this.c = 0;
        d(false);
        this.d = 0;
        this.e = null;
        this.f = null;
        this.g = null;
    }

    public String c() {
        return this.a;
    }

    public ad a(String str) {
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

    public long f() {
        return this.b;
    }

    public ad a(long j) {
        this.b = j;
        b(true);
        return this;
    }

    public void h() {
        this.u = aw.b(this.u, 0);
    }

    public boolean i() {
        return aw.a(this.u, 0);
    }

    public void b(boolean z) {
        this.u = aw.a(this.u, 0, z);
    }

    public long j() {
        return this.c;
    }

    public ad b(long j) {
        this.c = j;
        c(true);
        return this;
    }

    public void k() {
        this.u = aw.b(this.u, 1);
    }

    public boolean l() {
        return aw.a(this.u, 1);
    }

    public void c(boolean z) {
        this.u = aw.a(this.u, 1, z);
    }

    public long m() {
        return this.d;
    }

    public ad c(long j) {
        this.d = j;
        d(true);
        return this;
    }

    public void n() {
        this.u = aw.b(this.u, 2);
    }

    public boolean o() {
        return aw.a(this.u, 2);
    }

    public void d(boolean z) {
        this.u = aw.a(this.u, 2, z);
    }

    public int p() {
        return this.e == null ? 0 : this.e.size();
    }

    public Iterator<Y> q() {
        return this.e == null ? null : this.e.iterator();
    }

    public void a(Y y) {
        if (this.e == null) {
            this.e = new ArrayList();
        }
        this.e.add(y);
    }

    public List<Y> r() {
        return this.e;
    }

    public ad a(List<Y> list) {
        this.e = list;
        return this;
    }

    public void s() {
        this.e = null;
    }

    public boolean t() {
        return this.e != null;
    }

    public void e(boolean z) {
        if (!z) {
            this.e = null;
        }
    }

    public int u() {
        return this.f == null ? 0 : this.f.size();
    }

    public Iterator<W> v() {
        return this.f == null ? null : this.f.iterator();
    }

    public void a(W w) {
        if (this.f == null) {
            this.f = new ArrayList();
        }
        this.f.add(w);
    }

    public List<W> w() {
        return this.f;
    }

    public ad b(List<W> list) {
        this.f = list;
        return this;
    }

    public void x() {
        this.f = null;
    }

    public boolean y() {
        return this.f != null;
    }

    public void f(boolean z) {
        if (!z) {
            this.f = null;
        }
    }

    public ae z() {
        return this.g;
    }

    public ad a(ae aeVar) {
        this.g = aeVar;
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

    /* renamed from: a */
    public e b(int i) {
        return e.a(i);
    }

    public void a(aY aYVar) throws aF {
        ((bh) q.get(aYVar.D())).b().a(aYVar, this);
    }

    public void b(aY aYVar) throws aF {
        ((bh) q.get(aYVar.D())).b().b(aYVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Session(");
        stringBuilder.append("id:");
        if (this.a == null) {
            stringBuilder.append("null");
        } else {
            stringBuilder.append(this.a);
        }
        stringBuilder.append(", ");
        stringBuilder.append("start_time:");
        stringBuilder.append(this.b);
        stringBuilder.append(", ");
        stringBuilder.append("end_time:");
        stringBuilder.append(this.c);
        stringBuilder.append(", ");
        stringBuilder.append("duration:");
        stringBuilder.append(this.d);
        if (t()) {
            stringBuilder.append(", ");
            stringBuilder.append("pages:");
            if (this.e == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.e);
            }
        }
        if (y()) {
            stringBuilder.append(", ");
            stringBuilder.append("locations:");
            if (this.f == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.f);
            }
        }
        if (B()) {
            stringBuilder.append(", ");
            stringBuilder.append("traffic:");
            if (this.g == null) {
                stringBuilder.append("null");
            } else {
                stringBuilder.append(this.g);
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void C() throws aF {
        if (this.a == null) {
            throw new aZ("Required field 'id' was not present! Struct: " + toString());
        } else if (this.g != null) {
            this.g.j();
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
            this.u = (byte) 0;
            a(new aS(new bk((InputStream) objectInputStream)));
        } catch (aF e) {
            throw new IOException(e.getMessage());
        }
    }
}
