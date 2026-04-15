package org.apache.harmony.awt.datatransfer;

public final class RawBitmap {
    public final int bMask;
    public final int bits;
    public final Object buffer;
    public final int gMask;
    public final int height;
    public final int rMask;
    public final int stride;
    public final int width;

    public RawBitmap(int w, int h, int stride, int bits, int rMask, int gMask, int bMask, Object buffer) {
        this.width = w;
        this.height = h;
        this.stride = stride;
        this.bits = bits;
        this.rMask = rMask;
        this.gMask = gMask;
        this.bMask = bMask;
        this.buffer = buffer;
    }

    public RawBitmap(int[] header, Object buffer) {
        this.width = header[0];
        this.height = header[1];
        this.stride = header[2];
        this.bits = header[3];
        this.rMask = header[4];
        this.gMask = header[5];
        this.bMask = header[6];
        this.buffer = buffer;
    }

    public int[] getHeader() {
        return new int[]{this.width, this.height, this.stride, this.bits, this.rMask, this.gMask, this.bMask};
    }
}
