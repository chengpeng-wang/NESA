package u.aly;

import com.mopub.mobileads.VastIconXmlManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* compiled from: Resolution */
public class aa implements Serializable, Cloneable, az<aa, e> {
    public static final Map<e, aL> c;
    /* access modifiers changed from: private|static|final */
    public static final bd d = new bd("Resolution");
    /* access modifiers changed from: private|static|final */
    public static final aT e = new aT(VastIconXmlManager.HEIGHT, (byte) 8, (short) 1);
    /* access modifiers changed from: private|static|final */
    public static final aT f = new aT(VastIconXmlManager.WIDTH, (byte) 8, (short) 2);
    private static final Map<Class<? extends bg>, bh> g = new HashMap();
    private static final int h = 0;
    private static final int i = 1;
    public int a;
    public int b;
    private byte j;

    /* compiled from: Resolution */
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

    /* compiled from: Resolution */
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

    /* compiled from: Resolution */
    public enum e implements aG {
        HEIGHT((short) 1, VastIconXmlManager.HEIGHT),
        WIDTH((short) 2, VastIconXmlManager.WIDTH);
        
        private static final Map<String, e> c = null;
        private final short d;
        private final String e;

        static {
            c = new HashMap();
            Iterator it = EnumSet.allOf(e.class).iterator();
            while (it.hasNext()) {
                e eVar = (e) it.next();
                c.put(eVar.b(), eVar);
            }
        }

        public static e a(int i) {
            switch (i) {
                case 1:
                    return HEIGHT;
                case 2:
                    return WIDTH;
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
            return (e) c.get(str);
        }

        private e(short s, String str) {
            this.d = s;
            this.e = str;
        }

        public short a() {
            return this.d;
        }

        public String b() {
            return this.e;
        }
    }

    /* compiled from: Resolution */
    private static class a extends bi<aa> {
        private a() {
        }

        /* synthetic */ a(a aVar) {
            this();
        }

        public void a(aY aYVar, aa aaVar) throws aF {
            aYVar.j();
            while (true) {
                aT l = aYVar.l();
                if (l.b == (byte) 0) {
                    aYVar.k();
                    if (!aaVar.e()) {
                        throw new aZ("Required field 'height' was not found in serialized data! Struct: " + toString());
                    } else if (aaVar.i()) {
                        aaVar.j();
                        return;
                    } else {
                        throw new aZ("Required field 'width' was not found in serialized data! Struct: " + toString());
                    }
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        aaVar.a = aYVar.w();
                        aaVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        aaVar.b = aYVar.w();
                        aaVar.b(true);
                        break;
                    default:
                        bb.a(aYVar, l.b);
                        break;
                }
                aYVar.m();
            }
        }

        public void b(aY aYVar, aa aaVar) throws aF {
            aaVar.j();
            aYVar.a(aa.d);
            aYVar.a(aa.e);
            aYVar.a(aaVar.a);
            aYVar.c();
            aYVar.a(aa.f);
            aYVar.a(aaVar.b);
            aYVar.c();
            aYVar.d();
            aYVar.b();
        }
    }

    /* compiled from: Resolution */
    private static class c extends bj<aa> {
        private c() {
        }

        /* synthetic */ c(c cVar) {
            this();
        }

        /* renamed from: a */
        public void b(aY aYVar, aa aaVar) throws aF {
            be beVar = (be) aYVar;
            beVar.a(aaVar.a);
            beVar.a(aaVar.b);
        }

        /* renamed from: b */
        public void a(aY aYVar, aa aaVar) throws aF {
            be beVar = (be) aYVar;
            aaVar.a = beVar.w();
            aaVar.a(true);
            aaVar.b = beVar.w();
            aaVar.b(true);
        }
    }

    static {
        g.put(bi.class, new b());
        g.put(bj.class, new d());
        EnumMap enumMap = new EnumMap(e.class);
        enumMap.put(e.HEIGHT, new aL(VastIconXmlManager.HEIGHT, (byte) 1, new aM((byte) 8)));
        enumMap.put(e.WIDTH, new aL(VastIconXmlManager.WIDTH, (byte) 1, new aM((byte) 8)));
        c = Collections.unmodifiableMap(enumMap);
        aL.a(aa.class, c);
    }

    public aa() {
        this.j = (byte) 0;
    }

    public aa(int i, int i2) {
        this();
        this.a = i;
        a(true);
        this.b = i2;
        b(true);
    }

    public aa(aa aaVar) {
        this.j = (byte) 0;
        this.j = aaVar.j;
        this.a = aaVar.a;
        this.b = aaVar.b;
    }

    /* renamed from: a */
    public aa g() {
        return new aa(this);
    }

    public void b() {
        a(false);
        this.a = 0;
        b(false);
        this.b = 0;
    }

    public int c() {
        return this.a;
    }

    public aa a(int i) {
        this.a = i;
        a(true);
        return this;
    }

    public void d() {
        this.j = aw.b(this.j, 0);
    }

    public boolean e() {
        return aw.a(this.j, 0);
    }

    public void a(boolean z) {
        this.j = aw.a(this.j, 0, z);
    }

    public int f() {
        return this.b;
    }

    public aa c(int i) {
        this.b = i;
        b(true);
        return this;
    }

    public void h() {
        this.j = aw.b(this.j, 1);
    }

    public boolean i() {
        return aw.a(this.j, 1);
    }

    public void b(boolean z) {
        this.j = aw.a(this.j, 1, z);
    }

    /* renamed from: d */
    public e b(int i) {
        return e.a(i);
    }

    public void a(aY aYVar) throws aF {
        ((bh) g.get(aYVar.D())).b().a(aYVar, this);
    }

    public void b(aY aYVar) throws aF {
        ((bh) g.get(aYVar.D())).b().b(aYVar, this);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Resolution(");
        stringBuilder.append("height:");
        stringBuilder.append(this.a);
        stringBuilder.append(", ");
        stringBuilder.append("width:");
        stringBuilder.append(this.b);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void j() throws aF {
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
            this.j = (byte) 0;
            a(new aS(new bk((InputStream) objectInputStream)));
        } catch (aF e) {
            throw new IOException(e.getMessage());
        }
    }
}
