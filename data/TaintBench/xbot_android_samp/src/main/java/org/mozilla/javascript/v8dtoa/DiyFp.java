package org.mozilla.javascript.v8dtoa;

class DiyFp {
    static final /* synthetic */ boolean $assertionsDisabled = (!DiyFp.class.desiredAssertionStatus());
    static final int kSignificandSize = 64;
    static final long kUint64MSB = Long.MIN_VALUE;
    private int e;
    private long f;

    DiyFp() {
        this.f = 0;
        this.e = 0;
    }

    DiyFp(long f, int e) {
        this.f = f;
        this.e = e;
    }

    private static boolean uint64_gte(long a, long b) {
        if (a != b) {
            if (((b < 0 ? 1 : 0) ^ ((a > b ? 1 : 0) ^ (a < 0 ? 1 : 0))) == 0) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void subtract(DiyFp other) {
        if (!$assertionsDisabled && this.e != other.e) {
            throw new AssertionError();
        } else if ($assertionsDisabled || uint64_gte(this.f, other.f)) {
            this.f -= other.f;
        } else {
            throw new AssertionError();
        }
    }

    static DiyFp minus(DiyFp a, DiyFp b) {
        DiyFp result = new DiyFp(a.f, a.e);
        result.subtract(b);
        return result;
    }

    /* access modifiers changed from: 0000 */
    public void multiply(DiyFp other) {
        long a = this.f >>> 32;
        long b = this.f & 4294967295L;
        long c = other.f >>> 32;
        long d = other.f & 4294967295L;
        long bc = b * c;
        long ad = a * d;
        long result_f = (((ad >>> 32) + (a * c)) + (bc >>> 32)) + ((((((b * d) >>> 32) + (4294967295L & ad)) + (4294967295L & bc)) + 2147483648L) >>> 32);
        this.e += other.e + 64;
        this.f = result_f;
    }

    static DiyFp times(DiyFp a, DiyFp b) {
        DiyFp result = new DiyFp(a.f, a.e);
        result.multiply(b);
        return result;
    }

    /* access modifiers changed from: 0000 */
    public void normalize() {
        if ($assertionsDisabled || this.f != 0) {
            long f = this.f;
            int e = this.e;
            while ((-18014398509481984L & f) == 0) {
                f <<= 10;
                e -= 10;
            }
            while ((kUint64MSB & f) == 0) {
                f <<= 1;
                e--;
            }
            this.f = f;
            this.e = e;
            return;
        }
        throw new AssertionError();
    }

    static DiyFp normalize(DiyFp a) {
        DiyFp result = new DiyFp(a.f, a.e);
        result.normalize();
        return result;
    }

    /* access modifiers changed from: 0000 */
    public long f() {
        return this.f;
    }

    /* access modifiers changed from: 0000 */
    public int e() {
        return this.e;
    }

    /* access modifiers changed from: 0000 */
    public void setF(long new_value) {
        this.f = new_value;
    }

    /* access modifiers changed from: 0000 */
    public void setE(int new_value) {
        this.e = new_value;
    }

    public String toString() {
        return "[DiyFp f:" + this.f + ", e:" + this.e + "]";
    }
}
