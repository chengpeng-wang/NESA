package u.aly;

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

/* compiled from: Traffic */
public class ae implements Serializable, Cloneable, az<ae, e> {
    public static final Map<e, aL> c;
    /* access modifiers changed from: private|static|final */
    public static final bd d = new bd("Traffic");
    /* access modifiers changed from: private|static|final */
    public static final aT e = new aT("upload_traffic", (byte) 8, (short) 1);
    /* access modifiers changed from: private|static|final */
    public static final aT f = new aT("download_traffic", (byte) 8, (short) 2);
    private static final Map<Class<? extends bg>, bh> g = new HashMap();
    private static final int h = 0;
    private static final int i = 1;
    public int a;
    public int b;
    private byte j;

    /* compiled from: Traffic */
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

    /* compiled from: Traffic */
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

    /* compiled from: Traffic */
    public enum e implements aG {
        UPLOAD_TRAFFIC((short) 1, "upload_traffic"),
        DOWNLOAD_TRAFFIC((short) 2, "download_traffic");
        
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
                    return UPLOAD_TRAFFIC;
                case 2:
                    return DOWNLOAD_TRAFFIC;
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

    /* compiled from: Traffic */
    private static class a extends bi<ae> {
        private a() {
        }

        /* synthetic */ a(a aVar) {
            this();
        }

        public void a(aY aYVar, ae aeVar) throws aF {
            aYVar.j();
            while (true) {
                aT l = aYVar.l();
                if (l.b == (byte) 0) {
                    aYVar.k();
                    if (!aeVar.e()) {
                        throw new aZ("Required field 'upload_traffic' was not found in serialized data! Struct: " + toString());
                    } else if (aeVar.i()) {
                        aeVar.j();
                        return;
                    } else {
                        throw new aZ("Required field 'download_traffic' was not found in serialized data! Struct: " + toString());
                    }
                }
                switch (l.c) {
                    case (short) 1:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        aeVar.a = aYVar.w();
                        aeVar.a(true);
                        break;
                    case (short) 2:
                        if (l.b != (byte) 8) {
                            bb.a(aYVar, l.b);
                            break;
                        }
                        aeVar.b = aYVar.w();
                        aeVar.b(true);
                        break;
                    default:
                        bb.a(aYVar, l.b);
                        break;
                }
                aYVar.m();
            }
        }

        public void b(aY aYVar, ae aeVar) throws aF {
            aeVar.j();
            aYVar.a(ae.d);
            aYVar.a(ae.e);
            aYVar.a(aeVar.a);
            aYVar.c();
            aYVar.a(ae.f);
            aYVar.a(aeVar.b);
            aYVar.c();
            aYVar.d();
            aYVar.b();
        }
    }

    /* compiled from: Traffic */
    private static class c extends bj<ae> {
        private c() {
        }

        /* synthetic */ c(c cVar) {
            this();
        }

        /* renamed from: a */
        public void b(aY aYVar, ae aeVar) throws aF {
            be beVar = (be) aYVar;
            beVar.a(aeVar.a);
            beVar.a(aeVar.b);
        }

        /* renamed from: b */
        public void a(aY aYVar, ae aeVar) throws aF {
            be beVar = (be) aYVar;
            aeVar.a = beVar.w();
            aeVar.a(true);
            aeVar.b = beVar.w();
            aeVar.b(true);
        }
    }

    static {
        g.put(bi.class, new b());
        g.put(bj.class, new d());
        EnumMap enumMap = new EnumMap(e.class);
        enumMap.put(e.UPLOAD_TRAFFIC, new aL("upload_traffic", (byte) 1, new aM((byte) 8)));
        enumMap.put(e.DOWNLOAD_TRAFFIC, new aL("download_traffic", (byte) 1, new aM((byte) 8)));
        c = Collections.unmodifiableMap(enumMap);
        aL.a(ae.class, c);
    }

    public ae() {
        this.j = (byte) 0;
    }

    public ae(int i, int i2) {
        this();
        this.a = i;
        a(true);
        this.b = i2;
        b(true);
    }

    public ae(ae aeVar) {
        this.j = (byte) 0;
        this.j = aeVar.j;
        this.a = aeVar.a;
        this.b = aeVar.b;
    }

    /* renamed from: a */
    public ae g() {
        return new ae(this);
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

    public ae a(int i) {
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

    public ae c(int i) {
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
        StringBuilder stringBuilder = new StringBuilder("Traffic(");
        stringBuilder.append("upload_traffic:");
        stringBuilder.append(this.a);
        stringBuilder.append(", ");
        stringBuilder.append("download_traffic:");
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
