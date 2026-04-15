package com.trilead.ssh2.crypto.digest;

public final class MD5 implements Digest {
    private static final byte[] padding;
    private final byte[] block = new byte[64];
    private long count;
    private int state0;
    private int state1;
    private int state2;
    private int state3;
    private final int[] x = new int[16];

    static {
        byte[] bArr = new byte[64];
        bArr[0] = Byte.MIN_VALUE;
        padding = bArr;
    }

    public MD5() {
        reset();
    }

    private static final int FF(int a, int b, int c, int d, int x, int s, int ac) {
        a += (((b & c) | ((b ^ -1) & d)) + x) + ac;
        return ((a << s) | (a >>> (32 - s))) + b;
    }

    private static final int GG(int a, int b, int c, int d, int x, int s, int ac) {
        a += (((b & d) | ((d ^ -1) & c)) + x) + ac;
        return ((a << s) | (a >>> (32 - s))) + b;
    }

    private static final int HH(int a, int b, int c, int d, int x, int s, int ac) {
        a += (((b ^ c) ^ d) + x) + ac;
        return ((a << s) | (a >>> (32 - s))) + b;
    }

    private static final int II(int a, int b, int c, int d, int x, int s, int ac) {
        a += ((((d ^ -1) | b) ^ c) + x) + ac;
        return ((a << s) | (a >>> (32 - s))) + b;
    }

    private static final void encode(byte[] dst, int dstoff, int word) {
        dst[dstoff] = (byte) word;
        dst[dstoff + 1] = (byte) (word >> 8);
        dst[dstoff + 2] = (byte) (word >> 16);
        dst[dstoff + 3] = (byte) (word >> 24);
    }

    private final void transform(byte[] src, int pos) {
        int a = this.state0;
        int b = this.state1;
        int c = this.state2;
        int d = this.state3;
        int i = 0;
        while (i < 16) {
            this.x[i] = (((src[pos] & 255) | ((src[pos + 1] & 255) << 8)) | ((src[pos + 2] & 255) << 16)) | ((src[pos + 3] & 255) << 24);
            i++;
            pos += 4;
        }
        a = FF(a, b, c, d, this.x[0], 7, -680876936);
        d = FF(d, a, b, c, this.x[1], 12, -389564586);
        c = FF(c, d, a, b, this.x[2], 17, 606105819);
        b = FF(b, c, d, a, this.x[3], 22, -1044525330);
        a = FF(a, b, c, d, this.x[4], 7, -176418897);
        d = FF(d, a, b, c, this.x[5], 12, 1200080426);
        c = FF(c, d, a, b, this.x[6], 17, -1473231341);
        b = FF(b, c, d, a, this.x[7], 22, -45705983);
        a = FF(a, b, c, d, this.x[8], 7, 1770035416);
        d = FF(d, a, b, c, this.x[9], 12, -1958414417);
        c = FF(c, d, a, b, this.x[10], 17, -42063);
        b = FF(b, c, d, a, this.x[11], 22, -1990404162);
        a = FF(a, b, c, d, this.x[12], 7, 1804603682);
        d = FF(d, a, b, c, this.x[13], 12, -40341101);
        c = FF(c, d, a, b, this.x[14], 17, -1502002290);
        b = FF(b, c, d, a, this.x[15], 22, 1236535329);
        a = GG(a, b, c, d, this.x[1], 5, -165796510);
        d = GG(d, a, b, c, this.x[6], 9, -1069501632);
        c = GG(c, d, a, b, this.x[11], 14, 643717713);
        b = GG(b, c, d, a, this.x[0], 20, -373897302);
        a = GG(a, b, c, d, this.x[5], 5, -701558691);
        d = GG(d, a, b, c, this.x[10], 9, 38016083);
        c = GG(c, d, a, b, this.x[15], 14, -660478335);
        b = GG(b, c, d, a, this.x[4], 20, -405537848);
        a = GG(a, b, c, d, this.x[9], 5, 568446438);
        d = GG(d, a, b, c, this.x[14], 9, -1019803690);
        c = GG(c, d, a, b, this.x[3], 14, -187363961);
        b = GG(b, c, d, a, this.x[8], 20, 1163531501);
        a = GG(a, b, c, d, this.x[13], 5, -1444681467);
        d = GG(d, a, b, c, this.x[2], 9, -51403784);
        c = GG(c, d, a, b, this.x[7], 14, 1735328473);
        b = GG(b, c, d, a, this.x[12], 20, -1926607734);
        a = HH(a, b, c, d, this.x[5], 4, -378558);
        d = HH(d, a, b, c, this.x[8], 11, -2022574463);
        c = HH(c, d, a, b, this.x[11], 16, 1839030562);
        b = HH(b, c, d, a, this.x[14], 23, -35309556);
        a = HH(a, b, c, d, this.x[1], 4, -1530992060);
        d = HH(d, a, b, c, this.x[4], 11, 1272893353);
        c = HH(c, d, a, b, this.x[7], 16, -155497632);
        b = HH(b, c, d, a, this.x[10], 23, -1094730640);
        a = HH(a, b, c, d, this.x[13], 4, 681279174);
        d = HH(d, a, b, c, this.x[0], 11, -358537222);
        c = HH(c, d, a, b, this.x[3], 16, -722521979);
        b = HH(b, c, d, a, this.x[6], 23, 76029189);
        a = HH(a, b, c, d, this.x[9], 4, -640364487);
        d = HH(d, a, b, c, this.x[12], 11, -421815835);
        c = HH(c, d, a, b, this.x[15], 16, 530742520);
        b = HH(b, c, d, a, this.x[2], 23, -995338651);
        a = II(a, b, c, d, this.x[0], 6, -198630844);
        d = II(d, a, b, c, this.x[7], 10, 1126891415);
        c = II(c, d, a, b, this.x[14], 15, -1416354905);
        b = II(b, c, d, a, this.x[5], 21, -57434055);
        a = II(a, b, c, d, this.x[12], 6, 1700485571);
        d = II(d, a, b, c, this.x[3], 10, -1894986606);
        c = II(c, d, a, b, this.x[10], 15, -1051523);
        b = II(b, c, d, a, this.x[1], 21, -2054922799);
        a = II(a, b, c, d, this.x[8], 6, 1873313359);
        d = II(d, a, b, c, this.x[15], 10, -30611744);
        c = II(c, d, a, b, this.x[6], 15, -1560198380);
        b = II(b, c, d, a, this.x[13], 21, 1309151649);
        a = II(a, b, c, d, this.x[4], 6, -145523070);
        d = II(d, a, b, c, this.x[11], 10, -1120210379);
        c = II(c, d, a, b, this.x[2], 15, 718787259);
        b = II(b, c, d, a, this.x[9], 21, -343485551);
        this.state0 += a;
        this.state1 += b;
        this.state2 += c;
        this.state3 += d;
    }

    public final void reset() {
        this.count = 0;
        this.state0 = 1732584193;
        this.state1 = -271733879;
        this.state2 = -1732584194;
        this.state3 = 271733878;
        for (int i = 0; i < 16; i++) {
            this.x[i] = 0;
        }
    }

    public final void update(byte b) {
        int space = 64 - ((int) (this.count & 63));
        this.count++;
        this.block[64 - space] = b;
        if (space == 1) {
            transform(this.block, 0);
        }
    }

    public final void update(byte[] buff, int pos, int len) {
        int space = 64 - ((int) (this.count & 63));
        this.count += (long) len;
        while (len > 0) {
            if (len < space) {
                System.arraycopy(buff, pos, this.block, 64 - space, len);
                return;
            }
            if (space == 64) {
                transform(buff, pos);
            } else {
                System.arraycopy(buff, pos, this.block, 64 - space, space);
                transform(this.block, 0);
            }
            pos += space;
            len -= space;
            space = 64;
        }
    }

    public final void update(byte[] b) {
        update(b, 0, b.length);
    }

    public final void digest(byte[] dst, int pos) {
        byte[] bits = new byte[8];
        encode(bits, 0, (int) (this.count << 3));
        encode(bits, 4, (int) (this.count >> 29));
        int idx = ((int) this.count) & 63;
        update(padding, 0, idx < 56 ? 56 - idx : 120 - idx);
        update(bits, 0, 8);
        encode(dst, pos, this.state0);
        encode(dst, pos + 4, this.state1);
        encode(dst, pos + 8, this.state2);
        encode(dst, pos + 12, this.state3);
        reset();
    }

    public final void digest(byte[] dst) {
        digest(dst, 0);
    }

    public final int getDigestLength() {
        return 16;
    }
}
