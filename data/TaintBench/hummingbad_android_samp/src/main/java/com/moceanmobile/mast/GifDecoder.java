package com.moceanmobile.mast;

import android.support.v4.app.FragmentTransaction;
import java.io.InputStream;
import java.util.Vector;
import org.xbill.DNS.Type;

public class GifDecoder {
    protected static final int MAX_STACK_SIZE = 4096;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    protected int[] act;
    protected int bgColor;
    protected int bgIndex;
    protected byte[] block = new byte[256];
    protected int blockSize = 0;
    protected int delay = 0;
    protected int dispose = 0;
    protected int frameCount;
    protected Vector<GifFrame> frames;
    protected int[] gct;
    protected boolean gctFlag;
    protected int gctSize;
    protected int height;
    protected int ih;
    protected int[] image;
    protected InputStream in;
    protected boolean interlace;
    protected int iw;
    protected int ix;
    protected int iy;
    protected int lastBgColor;
    protected int[] lastBitmap;
    protected int lastDispose = 0;
    protected int[] lct;
    protected boolean lctFlag;
    protected int lctSize;
    protected int loopCount = 1;
    protected int lrh;
    protected int lrw;
    protected int lrx;
    protected int lry;
    protected int pixelAspect;
    protected byte[] pixelStack;
    protected byte[] pixels;
    protected short[] prefix;
    protected int status;
    protected byte[] suffix;
    protected int transIndex;
    protected boolean transparency = false;
    protected int width;

    private static class GifFrame {
        public int delay;
        public int[] image;

        public GifFrame(int[] im, int del) {
            this.image = im;
            this.delay = del;
        }
    }

    public int getDelay(int n) {
        this.delay = -1;
        if (n >= 0 && n < this.frameCount) {
            this.delay = ((GifFrame) this.frames.elementAt(n)).delay;
        }
        return this.delay;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public int[] getBitmap() {
        return getFrame(0);
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    /* access modifiers changed from: protected */
    public void setPixels() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5 = 0;
        int[] iArr = new int[(this.width * this.height)];
        if (this.lastDispose > 0) {
            if (this.lastDispose == 3) {
                i = this.frameCount - 2;
                if (i > 0) {
                    this.lastBitmap = getFrame(i - 1);
                } else {
                    this.lastBitmap = null;
                }
            }
            if (this.lastBitmap != null) {
                iArr = (int[]) this.lastBitmap.clone();
                if (this.lastDispose == 2) {
                    if (this.transparency) {
                        i = 0;
                    } else {
                        i = this.lastBgColor;
                    }
                    for (i2 = 0; i2 < this.lrh; i2++) {
                        i3 = ((this.lry + i2) * this.width) + this.lrx;
                        i4 = this.lrw + i3;
                        while (i3 < i4) {
                            iArr[i3] = i;
                            i3++;
                        }
                    }
                }
            }
        }
        i2 = 8;
        i3 = 1;
        i = 0;
        while (i5 < this.ih) {
            if (this.interlace) {
                if (i >= this.ih) {
                    i3++;
                    switch (i3) {
                        case 2:
                            i = 4;
                            break;
                        case 3:
                            i = 2;
                            i2 = 4;
                            break;
                        case 4:
                            i = 1;
                            i2 = 2;
                            break;
                    }
                }
                int i6 = i;
                i += i2;
                i4 = i6;
            } else {
                i4 = i5;
            }
            i4 += this.iy;
            if (i4 < this.height) {
                int i7 = this.width * i4;
                int i8 = i7 + this.ix;
                i4 = this.iw + i8;
                if (this.width + i7 < i4) {
                    i4 = this.width + i7;
                }
                i7 = this.iw * i5;
                int i9 = i8;
                while (i9 < i4) {
                    i8 = i7 + 1;
                    i7 = this.act[this.pixels[i7] & 255];
                    if (i7 != 0) {
                        iArr[i9] = i7;
                    }
                    i9++;
                    i7 = i8;
                }
            }
            i5++;
        }
        this.image = iArr;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[] getFrame(int n) {
        if (this.frameCount <= 0) {
            return null;
        }
        return ((GifFrame) this.frames.elementAt(n % this.frameCount)).image;
    }

    public int read(InputStream is) {
        init();
        if (is != null) {
            this.in = is;
            readHeader();
            if (!err()) {
                readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                }
            }
        } else {
            this.status = 2;
        }
        try {
            is.close();
        } catch (Exception e) {
        }
        return this.status;
    }

    /* access modifiers changed from: protected */
    public void decodeBitmapData() {
        int i;
        int i2 = this.iw * this.ih;
        if (this.pixels == null || this.pixels.length < i2) {
            this.pixels = new byte[i2];
        }
        if (this.prefix == null) {
            this.prefix = new short[4096];
        }
        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }
        if (this.pixelStack == null) {
            this.pixelStack = new byte[FragmentTransaction.TRANSIT_FRAGMENT_OPEN];
        }
        int read = read();
        int i3 = 1 << read;
        int i4 = i3 + 1;
        int i5 = i3 + 2;
        int i6 = -1;
        int i7 = read + 1;
        int i8 = (1 << i7) - 1;
        for (i = 0; i < i3; i++) {
            this.prefix[i] = (short) 0;
            this.suffix[i] = (byte) i;
        }
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        int i15 = 0;
        i = 0;
        while (i13 < i2) {
            int i16;
            int i17;
            if (i10 == 0) {
                if (i15 >= i7) {
                    i16 = i12 & i8;
                    i12 >>= i7;
                    i15 -= i7;
                    if (i16 <= i5 && i16 != i4) {
                        if (i16 != i3) {
                            if (i6 != -1) {
                                int i18;
                                if (i16 == i5) {
                                    i18 = i10 + 1;
                                    this.pixelStack[i10] = (byte) i11;
                                    i11 = i6;
                                } else {
                                    i18 = i10;
                                    i11 = i16;
                                }
                                while (i11 > i3) {
                                    i10 = i18 + 1;
                                    this.pixelStack[i18] = this.suffix[i11];
                                    i11 = this.prefix[i11];
                                    i18 = i10;
                                }
                                i11 = this.suffix[i11] & 255;
                                if (i5 >= 4096) {
                                    break;
                                }
                                i10 = i18 + 1;
                                this.pixelStack[i18] = (byte) i11;
                                this.prefix[i5] = (short) i6;
                                this.suffix[i5] = (byte) i11;
                                i6 = i5 + 1;
                                if ((i6 & i8) == 0 && i6 < 4096) {
                                    i7++;
                                    i8 += i6;
                                }
                                i17 = i10;
                                i10 = i12;
                                i12 = i16;
                                i16 = i8;
                                i8 = i11;
                                i11 = i15;
                                i15 = i7;
                                i7 = i17;
                            } else {
                                i11 = i10 + 1;
                                this.pixelStack[i10] = this.suffix[i16];
                                i10 = i11;
                                i6 = i16;
                                i11 = i16;
                            }
                        } else {
                            i7 = read + 1;
                            i8 = (1 << i7) - 1;
                            i5 = i3 + 2;
                            i6 = -1;
                        }
                    } else {
                        break;
                    }
                }
                if (i14 == 0) {
                    i14 = readBlock();
                    if (i14 <= 0) {
                        break;
                    }
                    i = 0;
                }
                i12 += (this.block[i] & 255) << i15;
                i15 += 8;
                i++;
                i14--;
            } else {
                i16 = i8;
                i8 = i11;
                i11 = i15;
                i15 = i7;
                i7 = i10;
                i10 = i12;
                i12 = i6;
                i6 = i5;
            }
            i5 = i7 - 1;
            i7 = i9 + 1;
            this.pixels[i9] = this.pixelStack[i5];
            i13++;
            i9 = i7;
            i7 = i15;
            i15 = i11;
            i11 = i8;
            i8 = i16;
            i17 = i12;
            i12 = i10;
            i10 = i5;
            i5 = i6;
            i6 = i17;
        }
        for (i = i9; i < i2; i++) {
            this.pixels[i] = (byte) 0;
        }
    }

    /* access modifiers changed from: protected */
    public boolean err() {
        return this.status != 0;
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.status = 0;
        this.frameCount = 0;
        this.frames = new Vector();
        this.gct = null;
        this.lct = null;
    }

    /* access modifiers changed from: protected */
    public int read() {
        int i = 0;
        try {
            return this.in.read();
        } catch (Exception e) {
            this.status = 1;
            return i;
        }
    }

    /* access modifiers changed from: protected */
    public int readBlock() {
        this.blockSize = read();
        int i = 0;
        if (this.blockSize > 0) {
            while (i < this.blockSize) {
                try {
                    int read = this.in.read(this.block, i, this.blockSize - i);
                    if (read == -1) {
                        break;
                    }
                    i += read;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (i < this.blockSize) {
                this.status = 1;
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public int[] readColorTable(int ncolors) {
        int read;
        int i = 0;
        int i2 = ncolors * 3;
        int[] iArr = null;
        byte[] bArr = new byte[i2];
        try {
            read = this.in.read(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            read = 0;
        }
        if (read < i2) {
            this.status = 1;
        } else {
            iArr = new int[256];
            read = 0;
            while (i < ncolors) {
                i2 = read + 1;
                int i3 = bArr[read] & 255;
                int i4 = i2 + 1;
                int i5 = bArr[i2] & 255;
                read = i4 + 1;
                i2 = i + 1;
                iArr[i] = (((i3 << 16) | -16777216) | (i5 << 8)) | (bArr[i4] & 255);
                i = i2;
            }
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public void readContents() {
        int i = 0;
        while (i == 0 && !err()) {
            switch (read()) {
                case 33:
                    switch (read()) {
                        case 1:
                            skip();
                            break;
                        case Type.TKEY /*249*/:
                            readGraphicControlExt();
                            break;
                        case 254:
                            skip();
                            break;
                        case 255:
                            readBlock();
                            String str = "";
                            for (int i2 = 0; i2 < 11; i2++) {
                                str = new StringBuilder(String.valueOf(str)).append((char) this.block[i2]).toString();
                            }
                            if (!str.equals("NETSCAPE2.0")) {
                                skip();
                                break;
                            } else {
                                readNetscapeExt();
                                break;
                            }
                        default:
                            skip();
                            break;
                    }
                case 44:
                    readBitmap();
                    break;
                case 59:
                    i = 1;
                    break;
                default:
                    this.status = 1;
                    break;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void readGraphicControlExt() {
        boolean z = true;
        read();
        int read = read();
        this.dispose = (read & 28) >> 2;
        if (this.dispose == 0) {
            this.dispose = 1;
        }
        if ((read & 1) == 0) {
            z = false;
        }
        this.transparency = z;
        this.delay = readShort() * 10;
        this.transIndex = read();
        read();
    }

    /* access modifiers changed from: protected */
    public void readHeader() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            str = new StringBuilder(String.valueOf(str)).append((char) read()).toString();
        }
        if (str.startsWith("GIF")) {
            readLSD();
            if (this.gctFlag && !err()) {
                this.gct = readColorTable(this.gctSize);
                this.bgColor = this.gct[this.bgIndex];
                return;
            }
            return;
        }
        this.status = 1;
    }

    /* access modifiers changed from: protected */
    public void readBitmap() {
        boolean z;
        int i = 0;
        this.ix = readShort();
        this.iy = readShort();
        this.iw = readShort();
        this.ih = readShort();
        int read = read();
        this.lctFlag = (read & 128) != 0;
        this.lctSize = (int) Math.pow(2.0d, (double) ((read & 7) + 1));
        if ((read & 64) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.interlace = z;
        if (this.lctFlag) {
            this.lct = readColorTable(this.lctSize);
            this.act = this.lct;
        } else {
            this.act = this.gct;
            if (this.bgIndex == this.transIndex) {
                this.bgColor = 0;
            }
        }
        if (this.transparency) {
            int i2 = this.act[this.transIndex];
            this.act[this.transIndex] = 0;
            i = i2;
        }
        if (this.act == null) {
            this.status = 1;
        }
        if (!err()) {
            decodeBitmapData();
            skip();
            if (!err()) {
                this.frameCount++;
                this.image = new int[(this.width * this.height)];
                setPixels();
                this.frames.addElement(new GifFrame(this.image, this.delay));
                if (this.transparency) {
                    this.act[this.transIndex] = i;
                }
                resetFrame();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void readLSD() {
        this.width = readShort();
        this.height = readShort();
        int read = read();
        this.gctFlag = (read & 128) != 0;
        this.gctSize = 2 << (read & 7);
        this.bgIndex = read();
        this.pixelAspect = read();
    }

    /* access modifiers changed from: protected */
    public void readNetscapeExt() {
        do {
            readBlock();
            if (this.block[0] == (byte) 1) {
                this.loopCount = (this.block[1] & 255) | ((this.block[2] & 255) << 8);
            }
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }

    /* access modifiers changed from: protected */
    public int readShort() {
        return read() | (read() << 8);
    }

    /* access modifiers changed from: protected */
    public void resetFrame() {
        this.lastDispose = this.dispose;
        this.lrx = this.ix;
        this.lry = this.iy;
        this.lrw = this.iw;
        this.lrh = this.ih;
        this.lastBitmap = this.image;
        this.lastBgColor = this.bgColor;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.lct = null;
    }

    /* access modifiers changed from: protected */
    public void skip() {
        do {
            readBlock();
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }
}
