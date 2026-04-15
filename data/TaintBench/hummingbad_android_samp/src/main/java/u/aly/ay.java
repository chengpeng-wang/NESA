package u.aly;

/* compiled from: TApplicationException */
public class ay extends aF {
    public static final int a = 0;
    public static final int b = 1;
    public static final int c = 2;
    public static final int d = 3;
    public static final int e = 4;
    public static final int f = 5;
    public static final int g = 6;
    public static final int h = 7;
    private static final bd j = new bd("TApplicationException");
    private static final aT k = new aT("message", (byte) 11, (short) 1);
    private static final aT l = new aT("type", (byte) 8, (short) 2);
    private static final long m = 1;
    protected int i = 0;

    public ay(int i) {
        this.i = i;
    }

    public ay(int i, String str) {
        super(str);
        this.i = i;
    }

    public ay(String str) {
        super(str);
    }

    public int a() {
        return this.i;
    }

    public static ay a(aY aYVar) throws aF {
        aYVar.j();
        String str = null;
        int i = 0;
        while (true) {
            aT l = aYVar.l();
            if (l.b == (byte) 0) {
                aYVar.k();
                return new ay(i, str);
            }
            switch (l.c) {
                case (short) 1:
                    if (l.b != (byte) 11) {
                        bb.a(aYVar, l.b);
                        break;
                    }
                    str = aYVar.z();
                    break;
                case (short) 2:
                    if (l.b != (byte) 8) {
                        bb.a(aYVar, l.b);
                        break;
                    }
                    i = aYVar.w();
                    break;
                default:
                    bb.a(aYVar, l.b);
                    break;
            }
            aYVar.m();
        }
    }

    public void b(aY aYVar) throws aF {
        aYVar.a(j);
        if (getMessage() != null) {
            aYVar.a(k);
            aYVar.a(getMessage());
            aYVar.c();
        }
        aYVar.a(l);
        aYVar.a(this.i);
        aYVar.c();
        aYVar.d();
        aYVar.b();
    }
}
