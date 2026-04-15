package u.aly;

import com.mopub.nativeads.MoPubNativeAdPositioning.MoPubClientPositioning;
import u.aly.aS.a;

/* compiled from: TProtocolUtil */
public class bb {
    private static int a = MoPubClientPositioning.NO_REPEAT;

    public static void a(int i) {
        a = i;
    }

    public static void a(aY aYVar, byte b) throws aF {
        a(aYVar, b, a);
    }

    public static void a(aY aYVar, byte b, int i) throws aF {
        int i2 = 0;
        if (i <= 0) {
            throw new aF("Maximum skip depth exceeded");
        }
        switch (b) {
            case (byte) 2:
                aYVar.t();
                return;
            case (byte) 3:
                aYVar.u();
                return;
            case (byte) 4:
                aYVar.y();
                return;
            case (byte) 6:
                aYVar.v();
                return;
            case (byte) 8:
                aYVar.w();
                return;
            case (byte) 10:
                aYVar.x();
                return;
            case (byte) 11:
                aYVar.A();
                return;
            case (byte) 12:
                aYVar.j();
                while (true) {
                    aT l = aYVar.l();
                    if (l.b == (byte) 0) {
                        aYVar.k();
                        return;
                    } else {
                        a(aYVar, l.b, i - 1);
                        aYVar.m();
                    }
                }
            case (byte) 13:
                aV n = aYVar.n();
                while (i2 < n.c) {
                    a(aYVar, n.a, i - 1);
                    a(aYVar, n.b, i - 1);
                    i2++;
                }
                aYVar.o();
                return;
            case (byte) 14:
                bc r = aYVar.r();
                while (i2 < r.b) {
                    a(aYVar, r.a, i - 1);
                    i2++;
                }
                aYVar.s();
                return;
            case (byte) 15:
                aU p = aYVar.p();
                while (i2 < p.b) {
                    a(aYVar, p.a, i - 1);
                    i2++;
                }
                aYVar.q();
                return;
            default:
                return;
        }
    }

    public static ba a(byte[] bArr, ba baVar) {
        if (bArr[0] > bf.n) {
            return new a();
        }
        if (bArr.length <= 1 || (bArr[1] & 128) == 0) {
            return baVar;
        }
        return new a();
    }
}
