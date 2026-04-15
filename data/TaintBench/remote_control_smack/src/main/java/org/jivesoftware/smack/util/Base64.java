package org.jivesoftware.smack.util;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Base64 {
    private static final byte[] ALPHABET;
    /* access modifiers changed from: private|static|final */
    public static final byte[] DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 62, (byte) -9, (byte) -9, (byte) -9, (byte) 63, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 58, (byte) 59, (byte) 60, EQUALS_SIGN, (byte) -9, (byte) -9, (byte) -9, EQUALS_SIGN_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, NEW_LINE, (byte) 11, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, (byte) 32, (byte) 33, (byte) 34, (byte) 35, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, (byte) 41, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
    public static final int DECODE = 0;
    public static final int DONT_BREAK_LINES = 8;
    public static final int ENCODE = 1;
    private static final byte EQUALS_SIGN = (byte) 61;
    private static final byte EQUALS_SIGN_ENC = (byte) -1;
    public static final int GZIP = 2;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte NEW_LINE = (byte) 10;
    public static final int NO_OPTIONS = 0;
    private static final String PREFERRED_ENCODING = "UTF-8";
    private static final byte WHITE_SPACE_ENC = (byte) -5;
    private static final byte[] _NATIVE_ALPHABET = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};

    public static class InputStream extends FilterInputStream {
        private boolean breakLines;
        private byte[] buffer;
        private int bufferLength;
        private boolean encode;
        private int lineLength;
        private int numSigBytes;
        private int position;

        public InputStream(java.io.InputStream in) {
            this(in, 0);
        }

        public InputStream(java.io.InputStream in, int options) {
            boolean z = true;
            super(in);
            this.breakLines = (options & 8) != 8;
            if ((options & 1) != 1) {
                z = false;
            }
            this.encode = z;
            this.bufferLength = this.encode ? 4 : 3;
            this.buffer = new byte[this.bufferLength];
            this.position = -1;
            this.lineLength = 0;
        }

        public int read() throws IOException {
            int b;
            if (this.position < 0) {
                int i;
                if (this.encode) {
                    byte[] b3 = new byte[3];
                    int numBinaryBytes = 0;
                    for (i = 0; i < 3; i++) {
                        try {
                            b = this.in.read();
                            if (b >= 0) {
                                b3[i] = (byte) b;
                                numBinaryBytes++;
                            }
                        } catch (IOException e) {
                            if (i == 0) {
                                throw e;
                            }
                        }
                    }
                    if (numBinaryBytes <= 0) {
                        return -1;
                    }
                    Base64.encode3to4(b3, 0, numBinaryBytes, this.buffer, 0);
                    this.position = 0;
                    this.numSigBytes = 4;
                } else {
                    byte[] b4 = new byte[4];
                    i = 0;
                    while (i < 4) {
                        do {
                            b = this.in.read();
                            if (b < 0) {
                                break;
                            }
                        } while (Base64.DECODABET[b & TransportMediator.KEYCODE_MEDIA_PAUSE] <= Base64.WHITE_SPACE_ENC);
                        if (b < 0) {
                            break;
                        }
                        b4[i] = (byte) b;
                        i++;
                    }
                    if (i == 4) {
                        this.numSigBytes = Base64.decode4to3(b4, 0, this.buffer, 0);
                        this.position = 0;
                    } else if (i == 0) {
                        return -1;
                    } else {
                        throw new IOException("Improperly padded Base64 input.");
                    }
                }
            }
            if (this.position < 0) {
                throw new IOException("Error in Base64 code reading stream.");
            } else if (this.position >= this.numSigBytes) {
                return -1;
            } else {
                if (this.encode && this.breakLines && this.lineLength >= Base64.MAX_LINE_LENGTH) {
                    this.lineLength = 0;
                    return 10;
                }
                this.lineLength++;
                byte[] bArr = this.buffer;
                int i2 = this.position;
                this.position = i2 + 1;
                b = bArr[i2];
                if (this.position >= this.bufferLength) {
                    this.position = -1;
                }
                return b & MotionEventCompat.ACTION_MASK;
            }
        }

        public int read(byte[] dest, int off, int len) throws IOException {
            int i = 0;
            while (i < len) {
                int b = read();
                if (b >= 0) {
                    dest[off + i] = (byte) b;
                    i++;
                } else if (i == 0) {
                    return -1;
                } else {
                    return i;
                }
            }
            return i;
        }
    }

    public static class OutputStream extends FilterOutputStream {
        private byte[] b4;
        private boolean breakLines;
        private byte[] buffer;
        private int bufferLength;
        private boolean encode;
        private int lineLength;
        private int position;
        private boolean suspendEncoding;

        public OutputStream(java.io.OutputStream out) {
            this(out, 1);
        }

        public OutputStream(java.io.OutputStream out, int options) {
            int i;
            boolean z = true;
            super(out);
            this.breakLines = (options & 8) != 8;
            if ((options & 1) != 1) {
                z = false;
            }
            this.encode = z;
            if (this.encode) {
                i = 3;
            } else {
                i = 4;
            }
            this.bufferLength = i;
            this.buffer = new byte[this.bufferLength];
            this.position = 0;
            this.lineLength = 0;
            this.suspendEncoding = false;
            this.b4 = new byte[4];
        }

        public void write(int theByte) throws IOException {
            byte[] bArr;
            int i;
            if (this.suspendEncoding) {
                this.out.write(theByte);
            } else if (this.encode) {
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength));
                    this.lineLength += 4;
                    if (this.breakLines && this.lineLength >= Base64.MAX_LINE_LENGTH) {
                        this.out.write(10);
                        this.lineLength = 0;
                    }
                    this.position = 0;
                }
            } else if (Base64.DECODABET[theByte & TransportMediator.KEYCODE_MEDIA_PAUSE] > Base64.WHITE_SPACE_ENC) {
                bArr = this.buffer;
                i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    this.out.write(this.b4, 0, Base64.decode4to3(this.buffer, 0, this.b4, 0));
                    this.position = 0;
                }
            } else if (Base64.DECODABET[theByte & TransportMediator.KEYCODE_MEDIA_PAUSE] != Base64.WHITE_SPACE_ENC) {
                throw new IOException("Invalid character in Base64 data.");
            }
        }

        public void write(byte[] theBytes, int off, int len) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(theBytes, off, len);
                return;
            }
            for (int i = 0; i < len; i++) {
                write(theBytes[off + i]);
            }
        }

        public void flushBase64() throws IOException {
            if (this.position <= 0) {
                return;
            }
            if (this.encode) {
                this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position));
                this.position = 0;
                return;
            }
            throw new IOException("Base64 input not properly padded.");
        }

        public void close() throws IOException {
            flushBase64();
            super.close();
            this.buffer = null;
            this.out = null;
        }

        public void suspendEncoding() throws IOException {
            flushBase64();
            this.suspendEncoding = true;
        }

        public void resumeEncoding() {
            this.suspendEncoding = false;
        }
    }

    static {
        byte[] __bytes;
        try {
            __bytes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            __bytes = _NATIVE_ALPHABET;
        }
        ALPHABET = __bytes;
    }

    private Base64() {
    }

    /* access modifiers changed from: private|static */
    public static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes) {
        encode3to4(threeBytes, 0, numSigBytes, b4, 0);
        return b4;
    }

    /* access modifiers changed from: private|static */
    public static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {
        int i;
        int i2 = 0;
        if (numSigBytes > 0) {
            i = (source[srcOffset] << 24) >>> 8;
        } else {
            i = 0;
        }
        int i3 = (numSigBytes > 1 ? (source[srcOffset + 1] << 24) >>> 16 : 0) | i;
        if (numSigBytes > 2) {
            i2 = (source[srcOffset + 2] << 24) >>> 24;
        }
        int inBuff = i3 | i2;
        switch (numSigBytes) {
            case 1:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = EQUALS_SIGN;
                destination[destOffset + 3] = EQUALS_SIGN;
                break;
            case 2:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = EQUALS_SIGN;
                break;
            case 3:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                destination[destOffset + 3] = ALPHABET[inBuff & 63];
                break;
        }
        return destination;
    }

    public static String encodeObject(Serializable serializableObject) {
        return encodeObject(serializableObject, 0);
    }

    public static String encodeObject(Serializable serializableObject, int options) {
        IOException e;
        Throwable th;
        ByteArrayOutputStream baos = null;
        java.io.OutputStream b64os = null;
        ObjectOutputStream oos = null;
        GZIPOutputStream gzos = null;
        int gzip = options & 2;
        int dontBreakLines = options & 8;
        try {
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            try {
                java.io.OutputStream b64os2 = new OutputStream(baos2, dontBreakLines | 1);
                if (gzip == 2) {
                    try {
                        GZIPOutputStream gzos2 = new GZIPOutputStream(b64os2);
                        try {
                            gzos = gzos2;
                            oos = new ObjectOutputStream(gzos2);
                        } catch (IOException e2) {
                            e = e2;
                            gzos = gzos2;
                            b64os = b64os2;
                            baos = baos2;
                            try {
                                e.printStackTrace();
                                try {
                                    oos.close();
                                } catch (Exception e3) {
                                }
                                try {
                                    gzos.close();
                                } catch (Exception e4) {
                                }
                                try {
                                    b64os.close();
                                } catch (Exception e5) {
                                }
                                try {
                                    baos.close();
                                    return null;
                                } catch (Exception e6) {
                                    return null;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    oos.close();
                                } catch (Exception e7) {
                                }
                                try {
                                    gzos.close();
                                } catch (Exception e8) {
                                }
                                try {
                                    b64os.close();
                                } catch (Exception e9) {
                                }
                                try {
                                    baos.close();
                                } catch (Exception e10) {
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            gzos = gzos2;
                            b64os = b64os2;
                            baos = baos2;
                            oos.close();
                            gzos.close();
                            b64os.close();
                            baos.close();
                            throw th;
                        }
                    } catch (IOException e11) {
                        e = e11;
                        b64os = b64os2;
                        baos = baos2;
                        e.printStackTrace();
                        oos.close();
                        gzos.close();
                        b64os.close();
                        baos.close();
                        return null;
                    } catch (Throwable th4) {
                        th = th4;
                        b64os = b64os2;
                        baos = baos2;
                        oos.close();
                        gzos.close();
                        b64os.close();
                        baos.close();
                        throw th;
                    }
                }
                oos = new ObjectOutputStream(b64os2);
                oos.writeObject(serializableObject);
                try {
                    oos.close();
                } catch (Exception e12) {
                }
                try {
                    gzos.close();
                } catch (Exception e13) {
                }
                try {
                    b64os2.close();
                } catch (Exception e14) {
                }
                try {
                    baos2.close();
                } catch (Exception e15) {
                }
                try {
                    b64os = b64os2;
                    baos = baos2;
                    return new String(baos2.toByteArray(), "UTF-8");
                } catch (UnsupportedEncodingException e16) {
                    b64os = b64os2;
                    baos = baos2;
                    return new String(baos2.toByteArray());
                }
            } catch (IOException e17) {
                e = e17;
                baos = baos2;
                e.printStackTrace();
                oos.close();
                gzos.close();
                b64os.close();
                baos.close();
                return null;
            } catch (Throwable th5) {
                th = th5;
                baos = baos2;
                oos.close();
                gzos.close();
                b64os.close();
                baos.close();
                throw th;
            }
        } catch (IOException e18) {
            e = e18;
            e.printStackTrace();
            oos.close();
            gzos.close();
            b64os.close();
            baos.close();
            return null;
        }
    }

    public static String encodeBytes(byte[] source) {
        return encodeBytes(source, 0, source.length, 0);
    }

    public static String encodeBytes(byte[] source, int options) {
        return encodeBytes(source, 0, source.length, options);
    }

    public static String encodeBytes(byte[] source, int off, int len) {
        return encodeBytes(source, off, len, 0);
    }

    public static String encodeBytes(byte[] source, int off, int len, int options) {
        IOException e;
        Throwable th;
        int dontBreakLines = options & 8;
        if ((options & 2) == 2) {
            ByteArrayOutputStream baos = null;
            GZIPOutputStream gzos = null;
            OutputStream b64os = null;
            try {
                OutputStream b64os2;
                GZIPOutputStream gzos2;
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                try {
                    b64os2 = new OutputStream(baos2, dontBreakLines | 1);
                    try {
                        gzos2 = new GZIPOutputStream(b64os2);
                    } catch (IOException e2) {
                        e = e2;
                        b64os = b64os2;
                        baos = baos2;
                        try {
                            e.printStackTrace();
                            try {
                                gzos.close();
                            } catch (Exception e3) {
                            }
                            try {
                                b64os.close();
                            } catch (Exception e4) {
                            }
                            try {
                                baos.close();
                                return null;
                            } catch (Exception e5) {
                                return null;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                gzos.close();
                            } catch (Exception e6) {
                            }
                            try {
                                b64os.close();
                            } catch (Exception e7) {
                            }
                            try {
                                baos.close();
                            } catch (Exception e8) {
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        b64os = b64os2;
                        baos = baos2;
                        gzos.close();
                        b64os.close();
                        baos.close();
                        throw th;
                    }
                } catch (IOException e9) {
                    e = e9;
                    baos = baos2;
                    e.printStackTrace();
                    gzos.close();
                    b64os.close();
                    baos.close();
                    return null;
                } catch (Throwable th4) {
                    th = th4;
                    baos = baos2;
                    gzos.close();
                    b64os.close();
                    baos.close();
                    throw th;
                }
                try {
                    gzos2.write(source, off, len);
                    gzos2.close();
                    try {
                        gzos2.close();
                    } catch (Exception e10) {
                    }
                    try {
                        b64os2.close();
                    } catch (Exception e11) {
                    }
                    try {
                        baos2.close();
                    } catch (Exception e12) {
                    }
                    try {
                        return new String(baos2.toByteArray(), "UTF-8");
                    } catch (UnsupportedEncodingException e13) {
                        return new String(baos2.toByteArray());
                    }
                } catch (IOException e14) {
                    e = e14;
                    b64os = b64os2;
                    gzos = gzos2;
                    baos = baos2;
                    e.printStackTrace();
                    gzos.close();
                    b64os.close();
                    baos.close();
                    return null;
                } catch (Throwable th5) {
                    th = th5;
                    b64os = b64os2;
                    gzos = gzos2;
                    baos = baos2;
                    gzos.close();
                    b64os.close();
                    baos.close();
                    throw th;
                }
            } catch (IOException e15) {
                e = e15;
                e.printStackTrace();
                gzos.close();
                b64os.close();
                baos.close();
                return null;
            }
        }
        int i;
        boolean breakLines = dontBreakLines == 0;
        int len43 = (len * 4) / 3;
        int i2 = len43 + (len % 3 > 0 ? 4 : 0);
        if (breakLines) {
            i = len43 / MAX_LINE_LENGTH;
        } else {
            i = 0;
        }
        byte[] outBuff = new byte[(i + i2)];
        int d = 0;
        e = null;
        int len2 = len - 2;
        int lineLength = 0;
        while (d < len2) {
            int e16;
            encode3to4(source, d + off, 3, outBuff, e);
            lineLength += 4;
            if (breakLines && lineLength == MAX_LINE_LENGTH) {
                outBuff[e + 4] = NEW_LINE;
                e16 = e + 1;
                lineLength = 0;
            }
            d += 3;
            e = e16 + 4;
        }
        if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e);
            e += 4;
        }
        try {
            return new String(outBuff, 0, e, "UTF-8");
        } catch (UnsupportedEncodingException e17) {
            return new String(outBuff, 0, e);
        }
    }

    /* access modifiers changed from: private|static */
    public static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset) {
        int outBuff;
        if (source[srcOffset + 2] == EQUALS_SIGN) {
            destination[destOffset] = (byte) ((((DECODABET[source[srcOffset]] & MotionEventCompat.ACTION_MASK) << 18) | ((DECODABET[source[srcOffset + 1]] & MotionEventCompat.ACTION_MASK) << 12)) >>> 16);
            return 1;
        } else if (source[srcOffset + 3] == EQUALS_SIGN) {
            outBuff = (((DECODABET[source[srcOffset]] & MotionEventCompat.ACTION_MASK) << 18) | ((DECODABET[source[srcOffset + 1]] & MotionEventCompat.ACTION_MASK) << 12)) | ((DECODABET[source[srcOffset + 2]] & MotionEventCompat.ACTION_MASK) << 6);
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);
            return 2;
        } else {
            try {
                outBuff = ((((DECODABET[source[srcOffset]] & MotionEventCompat.ACTION_MASK) << 18) | ((DECODABET[source[srcOffset + 1]] & MotionEventCompat.ACTION_MASK) << 12)) | ((DECODABET[source[srcOffset + 2]] & MotionEventCompat.ACTION_MASK) << 6)) | (DECODABET[source[srcOffset + 3]] & MotionEventCompat.ACTION_MASK);
                destination[destOffset] = (byte) (outBuff >> 16);
                destination[destOffset + 1] = (byte) (outBuff >> 8);
                destination[destOffset + 2] = (byte) outBuff;
                return 3;
            } catch (Exception e) {
                System.out.println("" + source[srcOffset] + ": " + DECODABET[source[srcOffset]]);
                System.out.println("" + source[srcOffset + 1] + ": " + DECODABET[source[srcOffset + 1]]);
                System.out.println("" + source[srcOffset + 2] + ": " + DECODABET[source[srcOffset + 2]]);
                System.out.println("" + source[srcOffset + 3] + ": " + DECODABET[source[srcOffset + 3]]);
                return -1;
            }
        }
    }

    public static byte[] decode(byte[] source, int off, int len) {
        byte[] outBuff = new byte[((len * 3) / 4)];
        int outBuffPosn = 0;
        byte[] b4 = new byte[4];
        int b4Posn = 0;
        int i = off;
        while (true) {
            int b4Posn2 = b4Posn;
            if (i >= off + len) {
                b4Posn = b4Posn2;
                break;
            }
            byte sbiCrop = (byte) (source[i] & TransportMediator.KEYCODE_MEDIA_PAUSE);
            byte sbiDecode = DECODABET[sbiCrop];
            if (sbiDecode >= WHITE_SPACE_ENC) {
                if (sbiDecode >= EQUALS_SIGN_ENC) {
                    b4Posn = b4Posn2 + 1;
                    b4[b4Posn2] = sbiCrop;
                    if (b4Posn > 3) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
                        b4Posn = 0;
                        if (sbiCrop == EQUALS_SIGN) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    b4Posn = b4Posn2;
                }
                i++;
            } else {
                System.err.println("Bad Base64 input character at " + i + ": " + source[i] + "(decimal)");
                b4Posn = b4Posn2;
                return null;
            }
        }
        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    public static byte[] decode(String s) {
        byte[] bytes;
        Throwable th;
        try {
            bytes = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            bytes = s.getBytes();
        }
        bytes = decode(bytes, 0, bytes.length);
        if (bytes != null && bytes.length >= 4 && 35615 == ((bytes[0] & MotionEventCompat.ACTION_MASK) | ((bytes[1] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK))) {
            ByteArrayInputStream bais = null;
            GZIPInputStream gzis = null;
            ByteArrayOutputStream baos = null;
            byte[] buffer = new byte[2048];
            try {
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                try {
                    ByteArrayInputStream bais2 = new ByteArrayInputStream(bytes);
                    try {
                        GZIPInputStream gzis2 = new GZIPInputStream(bais2);
                        while (true) {
                            try {
                                int length = gzis2.read(buffer);
                                if (length < 0) {
                                    break;
                                }
                                baos2.write(buffer, 0, length);
                            } catch (IOException e2) {
                                baos = baos2;
                                gzis = gzis2;
                                bais = bais2;
                            } catch (Throwable th2) {
                                th = th2;
                                baos = baos2;
                                gzis = gzis2;
                                bais = bais2;
                                try {
                                    baos.close();
                                } catch (Exception e3) {
                                }
                                try {
                                    gzis.close();
                                } catch (Exception e4) {
                                }
                                try {
                                    bais.close();
                                } catch (Exception e5) {
                                }
                                throw th;
                            }
                        }
                        bytes = baos2.toByteArray();
                        try {
                            baos2.close();
                        } catch (Exception e6) {
                        }
                        try {
                            gzis2.close();
                        } catch (Exception e7) {
                        }
                        try {
                            bais2.close();
                        } catch (Exception e8) {
                        }
                    } catch (IOException e9) {
                        baos = baos2;
                        bais = bais2;
                        try {
                            baos.close();
                        } catch (Exception e10) {
                        }
                        try {
                            gzis.close();
                        } catch (Exception e11) {
                        }
                        try {
                            bais.close();
                        } catch (Exception e12) {
                        }
                        return bytes;
                    } catch (Throwable th3) {
                        th = th3;
                        baos = baos2;
                        bais = bais2;
                        baos.close();
                        gzis.close();
                        bais.close();
                        throw th;
                    }
                } catch (IOException e13) {
                    baos = baos2;
                    baos.close();
                    gzis.close();
                    bais.close();
                    return bytes;
                } catch (Throwable th4) {
                    th = th4;
                    baos = baos2;
                    baos.close();
                    gzis.close();
                    bais.close();
                    throw th;
                }
            } catch (IOException e14) {
                baos.close();
                gzis.close();
                bais.close();
                return bytes;
            } catch (Throwable th5) {
                th = th5;
                baos.close();
                gzis.close();
                bais.close();
                throw th;
            }
        }
        return bytes;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:18:0x0027=Splitter:B:18:0x0027, B:29:0x0039=Splitter:B:29:0x0039} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x003f A:{SYNTHETIC, Splitter:B:33:0x003f} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0044 A:{SYNTHETIC, Splitter:B:36:0x0044} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x004d A:{SYNTHETIC, Splitter:B:41:0x004d} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0052 A:{SYNTHETIC, Splitter:B:44:0x0052} */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x002d A:{SYNTHETIC, Splitter:B:22:0x002d} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0032 A:{SYNTHETIC, Splitter:B:25:0x0032} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x003f A:{SYNTHETIC, Splitter:B:33:0x003f} */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0044 A:{SYNTHETIC, Splitter:B:36:0x0044} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x004d A:{SYNTHETIC, Splitter:B:41:0x004d} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0052 A:{SYNTHETIC, Splitter:B:44:0x0052} */
    public static java.lang.Object decodeToObject(java.lang.String r9) {
        /*
        r4 = decode(r9);
        r0 = 0;
        r5 = 0;
        r3 = 0;
        r1 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x0026, ClassNotFoundException -> 0x0038 }
        r1.<init>(r4);	 Catch:{ IOException -> 0x0026, ClassNotFoundException -> 0x0038 }
        r6 = new java.io.ObjectInputStream;	 Catch:{ IOException -> 0x006e, ClassNotFoundException -> 0x0067, all -> 0x0060 }
        r6.<init>(r1);	 Catch:{ IOException -> 0x006e, ClassNotFoundException -> 0x0067, all -> 0x0060 }
        r3 = r6.readObject();	 Catch:{ IOException -> 0x0071, ClassNotFoundException -> 0x006a, all -> 0x0063 }
        if (r1 == 0) goto L_0x001a;
    L_0x0017:
        r1.close();	 Catch:{ Exception -> 0x0056 }
    L_0x001a:
        if (r6 == 0) goto L_0x001f;
    L_0x001c:
        r6.close();	 Catch:{ Exception -> 0x0022 }
    L_0x001f:
        r5 = r6;
        r0 = r1;
    L_0x0021:
        return r3;
    L_0x0022:
        r7 = move-exception;
        r5 = r6;
        r0 = r1;
        goto L_0x0021;
    L_0x0026:
        r2 = move-exception;
    L_0x0027:
        r2.printStackTrace();	 Catch:{ all -> 0x004a }
        r3 = 0;
        if (r0 == 0) goto L_0x0030;
    L_0x002d:
        r0.close();	 Catch:{ Exception -> 0x0058 }
    L_0x0030:
        if (r5 == 0) goto L_0x0021;
    L_0x0032:
        r5.close();	 Catch:{ Exception -> 0x0036 }
        goto L_0x0021;
    L_0x0036:
        r7 = move-exception;
        goto L_0x0021;
    L_0x0038:
        r2 = move-exception;
    L_0x0039:
        r2.printStackTrace();	 Catch:{ all -> 0x004a }
        r3 = 0;
        if (r0 == 0) goto L_0x0042;
    L_0x003f:
        r0.close();	 Catch:{ Exception -> 0x005a }
    L_0x0042:
        if (r5 == 0) goto L_0x0021;
    L_0x0044:
        r5.close();	 Catch:{ Exception -> 0x0048 }
        goto L_0x0021;
    L_0x0048:
        r7 = move-exception;
        goto L_0x0021;
    L_0x004a:
        r7 = move-exception;
    L_0x004b:
        if (r0 == 0) goto L_0x0050;
    L_0x004d:
        r0.close();	 Catch:{ Exception -> 0x005c }
    L_0x0050:
        if (r5 == 0) goto L_0x0055;
    L_0x0052:
        r5.close();	 Catch:{ Exception -> 0x005e }
    L_0x0055:
        throw r7;
    L_0x0056:
        r7 = move-exception;
        goto L_0x001a;
    L_0x0058:
        r7 = move-exception;
        goto L_0x0030;
    L_0x005a:
        r7 = move-exception;
        goto L_0x0042;
    L_0x005c:
        r8 = move-exception;
        goto L_0x0050;
    L_0x005e:
        r8 = move-exception;
        goto L_0x0055;
    L_0x0060:
        r7 = move-exception;
        r0 = r1;
        goto L_0x004b;
    L_0x0063:
        r7 = move-exception;
        r5 = r6;
        r0 = r1;
        goto L_0x004b;
    L_0x0067:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0039;
    L_0x006a:
        r2 = move-exception;
        r5 = r6;
        r0 = r1;
        goto L_0x0039;
    L_0x006e:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0027;
    L_0x0071:
        r2 = move-exception;
        r5 = r6;
        r0 = r1;
        goto L_0x0027;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.util.Base64.decodeToObject(java.lang.String):java.lang.Object");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x001f A:{SYNTHETIC, Splitter:B:16:0x001f} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0028 A:{SYNTHETIC, Splitter:B:21:0x0028} */
    public static boolean encodeToFile(byte[] r6, java.lang.String r7) {
        /*
        r3 = 0;
        r0 = 0;
        r1 = new org.jivesoftware.smack.util.Base64$OutputStream;	 Catch:{ IOException -> 0x001b, all -> 0x0025 }
        r4 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x001b, all -> 0x0025 }
        r4.<init>(r7);	 Catch:{ IOException -> 0x001b, all -> 0x0025 }
        r5 = 1;
        r1.m1469init(r4, r5);	 Catch:{ IOException -> 0x001b, all -> 0x0025 }
        r1.write(r6);	 Catch:{ IOException -> 0x0031, all -> 0x002e }
        r3 = 1;
        if (r1 == 0) goto L_0x0016;
    L_0x0013:
        r1.close();	 Catch:{ Exception -> 0x0018 }
    L_0x0016:
        r0 = r1;
    L_0x0017:
        return r3;
    L_0x0018:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0017;
    L_0x001b:
        r2 = move-exception;
    L_0x001c:
        r3 = 0;
        if (r0 == 0) goto L_0x0017;
    L_0x001f:
        r0.close();	 Catch:{ Exception -> 0x0023 }
        goto L_0x0017;
    L_0x0023:
        r4 = move-exception;
        goto L_0x0017;
    L_0x0025:
        r4 = move-exception;
    L_0x0026:
        if (r0 == 0) goto L_0x002b;
    L_0x0028:
        r0.close();	 Catch:{ Exception -> 0x002c }
    L_0x002b:
        throw r4;
    L_0x002c:
        r5 = move-exception;
        goto L_0x002b;
    L_0x002e:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0026;
    L_0x0031:
        r2 = move-exception;
        r0 = r1;
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.util.Base64.encodeToFile(byte[], java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0025 A:{SYNTHETIC, Splitter:B:16:0x0025} */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x002e A:{SYNTHETIC, Splitter:B:21:0x002e} */
    public static boolean decodeToFile(java.lang.String r6, java.lang.String r7) {
        /*
        r3 = 0;
        r0 = 0;
        r1 = new org.jivesoftware.smack.util.Base64$OutputStream;	 Catch:{ IOException -> 0x0021, all -> 0x002b }
        r4 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0021, all -> 0x002b }
        r4.<init>(r7);	 Catch:{ IOException -> 0x0021, all -> 0x002b }
        r5 = 0;
        r1.m1469init(r4, r5);	 Catch:{ IOException -> 0x0021, all -> 0x002b }
        r4 = "UTF-8";
        r4 = r6.getBytes(r4);	 Catch:{ IOException -> 0x0037, all -> 0x0034 }
        r1.write(r4);	 Catch:{ IOException -> 0x0037, all -> 0x0034 }
        r3 = 1;
        if (r1 == 0) goto L_0x001c;
    L_0x0019:
        r1.close();	 Catch:{ Exception -> 0x001e }
    L_0x001c:
        r0 = r1;
    L_0x001d:
        return r3;
    L_0x001e:
        r4 = move-exception;
        r0 = r1;
        goto L_0x001d;
    L_0x0021:
        r2 = move-exception;
    L_0x0022:
        r3 = 0;
        if (r0 == 0) goto L_0x001d;
    L_0x0025:
        r0.close();	 Catch:{ Exception -> 0x0029 }
        goto L_0x001d;
    L_0x0029:
        r4 = move-exception;
        goto L_0x001d;
    L_0x002b:
        r4 = move-exception;
    L_0x002c:
        if (r0 == 0) goto L_0x0031;
    L_0x002e:
        r0.close();	 Catch:{ Exception -> 0x0032 }
    L_0x0031:
        throw r4;
    L_0x0032:
        r5 = move-exception;
        goto L_0x0031;
    L_0x0034:
        r4 = move-exception;
        r0 = r1;
        goto L_0x002c;
    L_0x0037:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0022;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.util.Base64.decodeToFile(java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x008a A:{SYNTHETIC, Splitter:B:28:0x008a} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0093 A:{SYNTHETIC, Splitter:B:33:0x0093} */
    public static byte[] decodeFromFile(java.lang.String r12) {
        /*
        r3 = 0;
        r0 = 0;
        r5 = new java.io.File;	 Catch:{ IOException -> 0x006f }
        r5.<init>(r12);	 Catch:{ IOException -> 0x006f }
        r6 = 0;
        r8 = r5.length();	 Catch:{ IOException -> 0x006f }
        r10 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r8 <= 0) goto L_0x003c;
    L_0x0013:
        r8 = java.lang.System.err;	 Catch:{ IOException -> 0x006f }
        r9 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x006f }
        r9.<init>();	 Catch:{ IOException -> 0x006f }
        r10 = "File is too big for this convenience method (";
        r9 = r9.append(r10);	 Catch:{ IOException -> 0x006f }
        r10 = r5.length();	 Catch:{ IOException -> 0x006f }
        r9 = r9.append(r10);	 Catch:{ IOException -> 0x006f }
        r10 = " bytes).";
        r9 = r9.append(r10);	 Catch:{ IOException -> 0x006f }
        r9 = r9.toString();	 Catch:{ IOException -> 0x006f }
        r8.println(r9);	 Catch:{ IOException -> 0x006f }
        r8 = 0;
        if (r0 == 0) goto L_0x003b;
    L_0x0038:
        r0.close();	 Catch:{ Exception -> 0x0097 }
    L_0x003b:
        return r8;
    L_0x003c:
        r8 = r5.length();	 Catch:{ IOException -> 0x006f }
        r8 = (int) r8;	 Catch:{ IOException -> 0x006f }
        r2 = new byte[r8];	 Catch:{ IOException -> 0x006f }
        r1 = new org.jivesoftware.smack.util.Base64$InputStream;	 Catch:{ IOException -> 0x006f }
        r8 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x006f }
        r9 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x006f }
        r9.<init>(r5);	 Catch:{ IOException -> 0x006f }
        r8.<init>(r9);	 Catch:{ IOException -> 0x006f }
        r9 = 0;
        r1.m1467init(r8, r9);	 Catch:{ IOException -> 0x006f }
    L_0x0053:
        r8 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r7 = r1.read(r2, r6, r8);	 Catch:{ IOException -> 0x009e, all -> 0x009b }
        if (r7 < 0) goto L_0x005d;
    L_0x005b:
        r6 = r6 + r7;
        goto L_0x0053;
    L_0x005d:
        r3 = new byte[r6];	 Catch:{ IOException -> 0x009e, all -> 0x009b }
        r8 = 0;
        r9 = 0;
        java.lang.System.arraycopy(r2, r8, r3, r9, r6);	 Catch:{ IOException -> 0x009e, all -> 0x009b }
        if (r1 == 0) goto L_0x0069;
    L_0x0066:
        r1.close();	 Catch:{ Exception -> 0x006c }
    L_0x0069:
        r0 = r1;
    L_0x006a:
        r8 = r3;
        goto L_0x003b;
    L_0x006c:
        r8 = move-exception;
        r0 = r1;
        goto L_0x006a;
    L_0x006f:
        r4 = move-exception;
    L_0x0070:
        r8 = java.lang.System.err;	 Catch:{ all -> 0x0090 }
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0090 }
        r9.<init>();	 Catch:{ all -> 0x0090 }
        r10 = "Error decoding from file ";
        r9 = r9.append(r10);	 Catch:{ all -> 0x0090 }
        r9 = r9.append(r12);	 Catch:{ all -> 0x0090 }
        r9 = r9.toString();	 Catch:{ all -> 0x0090 }
        r8.println(r9);	 Catch:{ all -> 0x0090 }
        if (r0 == 0) goto L_0x006a;
    L_0x008a:
        r0.close();	 Catch:{ Exception -> 0x008e }
        goto L_0x006a;
    L_0x008e:
        r8 = move-exception;
        goto L_0x006a;
    L_0x0090:
        r8 = move-exception;
    L_0x0091:
        if (r0 == 0) goto L_0x0096;
    L_0x0093:
        r0.close();	 Catch:{ Exception -> 0x0099 }
    L_0x0096:
        throw r8;
    L_0x0097:
        r9 = move-exception;
        goto L_0x003b;
    L_0x0099:
        r9 = move-exception;
        goto L_0x0096;
    L_0x009b:
        r8 = move-exception;
        r0 = r1;
        goto L_0x0091;
    L_0x009e:
        r4 = move-exception;
        r0 = r1;
        goto L_0x0070;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.util.Base64.decodeFromFile(java.lang.String):byte[]");
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x005f A:{SYNTHETIC, Splitter:B:19:0x005f} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0068 A:{SYNTHETIC, Splitter:B:24:0x0068} */
    public static java.lang.String encodeFromFile(java.lang.String r13) {
        /*
        r4 = 0;
        r0 = 0;
        r6 = new java.io.File;	 Catch:{ IOException -> 0x0044 }
        r6.<init>(r13);	 Catch:{ IOException -> 0x0044 }
        r9 = r6.length();	 Catch:{ IOException -> 0x0044 }
        r9 = (double) r9;	 Catch:{ IOException -> 0x0044 }
        r11 = 4608983858650965606; // 0x3ff6666666666666 float:2.720083E23 double:1.4;
        r9 = r9 * r11;
        r9 = (int) r9;	 Catch:{ IOException -> 0x0044 }
        r2 = new byte[r9];	 Catch:{ IOException -> 0x0044 }
        r7 = 0;
        r1 = new org.jivesoftware.smack.util.Base64$InputStream;	 Catch:{ IOException -> 0x0044 }
        r9 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x0044 }
        r10 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0044 }
        r10.<init>(r6);	 Catch:{ IOException -> 0x0044 }
        r9.<init>(r10);	 Catch:{ IOException -> 0x0044 }
        r10 = 1;
        r1.m1467init(r9, r10);	 Catch:{ IOException -> 0x0044 }
    L_0x0026:
        r9 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r8 = r1.read(r2, r7, r9);	 Catch:{ IOException -> 0x0071, all -> 0x006e }
        if (r8 < 0) goto L_0x0030;
    L_0x002e:
        r7 = r7 + r8;
        goto L_0x0026;
    L_0x0030:
        r5 = new java.lang.String;	 Catch:{ IOException -> 0x0071, all -> 0x006e }
        r9 = 0;
        r10 = "UTF-8";
        r5.<init>(r2, r9, r7, r10);	 Catch:{ IOException -> 0x0071, all -> 0x006e }
        if (r1 == 0) goto L_0x003d;
    L_0x003a:
        r1.close();	 Catch:{ Exception -> 0x0040 }
    L_0x003d:
        r0 = r1;
        r4 = r5;
    L_0x003f:
        return r4;
    L_0x0040:
        r9 = move-exception;
        r0 = r1;
        r4 = r5;
        goto L_0x003f;
    L_0x0044:
        r3 = move-exception;
    L_0x0045:
        r9 = java.lang.System.err;	 Catch:{ all -> 0x0065 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0065 }
        r10.<init>();	 Catch:{ all -> 0x0065 }
        r11 = "Error encoding from file ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x0065 }
        r10 = r10.append(r13);	 Catch:{ all -> 0x0065 }
        r10 = r10.toString();	 Catch:{ all -> 0x0065 }
        r9.println(r10);	 Catch:{ all -> 0x0065 }
        if (r0 == 0) goto L_0x003f;
    L_0x005f:
        r0.close();	 Catch:{ Exception -> 0x0063 }
        goto L_0x003f;
    L_0x0063:
        r9 = move-exception;
        goto L_0x003f;
    L_0x0065:
        r9 = move-exception;
    L_0x0066:
        if (r0 == 0) goto L_0x006b;
    L_0x0068:
        r0.close();	 Catch:{ Exception -> 0x006c }
    L_0x006b:
        throw r9;
    L_0x006c:
        r10 = move-exception;
        goto L_0x006b;
    L_0x006e:
        r9 = move-exception;
        r0 = r1;
        goto L_0x0066;
    L_0x0071:
        r3 = move-exception;
        r0 = r1;
        goto L_0x0045;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.util.Base64.encodeFromFile(java.lang.String):java.lang.String");
    }
}
