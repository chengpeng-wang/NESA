package u.aly;

/* compiled from: TTransportException */
public class bn extends aF {
    public static final int a = 0;
    public static final int b = 1;
    public static final int c = 2;
    public static final int d = 3;
    public static final int e = 4;
    private static final long g = 1;
    protected int f = 0;

    public bn(int i) {
        this.f = i;
    }

    public bn(int i, String str) {
        super(str);
        this.f = i;
    }

    public bn(String str) {
        super(str);
    }

    public bn(int i, Throwable th) {
        super(th);
        this.f = i;
    }

    public bn(Throwable th) {
        super(th);
    }

    public bn(String str, Throwable th) {
        super(str, th);
    }

    public bn(int i, String str, Throwable th) {
        super(str, th);
        this.f = i;
    }

    public int a() {
        return this.f;
    }
}
