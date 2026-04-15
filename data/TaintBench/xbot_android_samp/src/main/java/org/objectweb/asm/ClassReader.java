package org.objectweb.asm;

import java.io.IOException;
import java.io.InputStream;
import org.java_websocket.framing.CloseFrame;
import org.mozilla.classfile.ByteCode;

public class ClassReader {
    public static final int EXPAND_FRAMES = 8;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    private final int[] a;
    public final byte[] b;
    private final String[] c;
    private final int d;
    public final int header;

    public ClassReader(InputStream inputStream) throws IOException {
        this(a(inputStream, false));
    }

    public ClassReader(String str) throws IOException {
        this(a(ClassLoader.getSystemResourceAsStream(new StringBuffer().append(str.replace('.', '/')).append(".class").toString()), true));
    }

    public ClassReader(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ClassReader(byte[] bArr, int i, int i2) {
        this.b = bArr;
        if (readShort(6) > (short) 51) {
            throw new IllegalArgumentException();
        }
        this.a = new int[readUnsignedShort(i + 8)];
        int length = this.a.length;
        this.c = new String[length];
        int i3 = 0;
        int i4 = 1;
        int i5 = i + 10;
        while (i4 < length) {
            int readUnsignedShort;
            this.a[i4] = i5 + 1;
            switch (bArr[i5]) {
                case (byte) 1:
                    readUnsignedShort = readUnsignedShort(i5 + 1) + 3;
                    if (readUnsignedShort <= i3) {
                        break;
                    }
                    i3 = readUnsignedShort;
                    break;
                case (byte) 3:
                case (byte) 4:
                case (byte) 9:
                case (byte) 10:
                case (byte) 11:
                case (byte) 12:
                case (byte) 18:
                    readUnsignedShort = 5;
                    break;
                case (byte) 5:
                case (byte) 6:
                    readUnsignedShort = 9;
                    i4++;
                    break;
                case (byte) 15:
                    readUnsignedShort = 4;
                    break;
                default:
                    readUnsignedShort = 3;
                    break;
            }
            i4++;
            i5 = readUnsignedShort + i5;
        }
        this.d = i3;
        this.header = i5;
    }

    private int a(int i, char[] cArr, String str, AnnotationVisitor annotationVisitor) {
        int i2 = false;
        if (annotationVisitor == null) {
            switch (this.b[i] & ByteCode.IMPDEP2) {
                case 64:
                    return a(i + 3, cArr, true, null);
                case 91:
                    return a(i + 1, cArr, false, null);
                case 101:
                    return i + 5;
                default:
                    return i + 3;
            }
        }
        int i3 = i + 1;
        switch (this.b[i] & ByteCode.IMPDEP2) {
            case 64:
                return a(i3 + 2, cArr, true, annotationVisitor.visitAnnotation(str, readUTF8(i3, cArr)));
            case 66:
                annotationVisitor.visit(str, new Byte((byte) readInt(this.a[readUnsignedShort(i3)])));
                return i3 + 2;
            case 67:
                annotationVisitor.visit(str, new Character((char) readInt(this.a[readUnsignedShort(i3)])));
                return i3 + 2;
            case 68:
            case 70:
            case 73:
            case 74:
                annotationVisitor.visit(str, readConst(readUnsignedShort(i3), cArr));
                return i3 + 2;
            case 83:
                annotationVisitor.visit(str, new Short((short) readInt(this.a[readUnsignedShort(i3)])));
                return i3 + 2;
            case 90:
                annotationVisitor.visit(str, readInt(this.a[readUnsignedShort(i3)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                return i3 + 2;
            case 91:
                int readUnsignedShort = readUnsignedShort(i3);
                i3 += 2;
                if (readUnsignedShort == 0) {
                    return a(i3 - 2, cArr, false, annotationVisitor.visitArray(str));
                }
                int i4 = i3 + 1;
                switch (this.b[i3] & ByteCode.IMPDEP2) {
                    case 66:
                        byte[] bArr = new byte[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            bArr[i2] = (byte) readInt(this.a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, bArr);
                        return i4 - 1;
                    case 67:
                        char[] cArr2 = new char[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            cArr2[i2] = (char) readInt(this.a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, cArr2);
                        return i4 - 1;
                    case 68:
                        double[] dArr = new double[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            dArr[i2] = Double.longBitsToDouble(readLong(this.a[readUnsignedShort(i4)]));
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, dArr);
                        return i4 - 1;
                    case 70:
                        float[] fArr = new float[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            fArr[i2] = Float.intBitsToFloat(readInt(this.a[readUnsignedShort(i4)]));
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, fArr);
                        return i4 - 1;
                    case 73:
                        int[] iArr = new int[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            iArr[i2] = readInt(this.a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, iArr);
                        return i4 - 1;
                    case 74:
                        long[] jArr = new long[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            jArr[i2] = readLong(this.a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, jArr);
                        return i4 - 1;
                    case 83:
                        short[] sArr = new short[readUnsignedShort];
                        while (i2 < readUnsignedShort) {
                            sArr[i2] = (short) readInt(this.a[readUnsignedShort(i4)]);
                            i4 += 3;
                            i2++;
                        }
                        annotationVisitor.visit(str, sArr);
                        return i4 - 1;
                    case 90:
                        boolean[] zArr = new boolean[readUnsignedShort];
                        int i5 = i4;
                        for (i3 = 0; i3 < readUnsignedShort; i3++) {
                            zArr[i3] = readInt(this.a[readUnsignedShort(i5)]) != 0;
                            i5 += 3;
                        }
                        annotationVisitor.visit(str, zArr);
                        return i5 - 1;
                    default:
                        return a(i4 - 3, cArr, false, annotationVisitor.visitArray(str));
                }
            case 99:
                annotationVisitor.visit(str, Type.getType(readUTF8(i3, cArr)));
                return i3 + 2;
            case 101:
                annotationVisitor.visitEnum(str, readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                return i3 + 4;
            case 115:
                annotationVisitor.visit(str, readUTF8(i3, cArr));
                return i3 + 2;
            default:
                return i3;
        }
    }

    private int a(int i, char[] cArr, boolean z, AnnotationVisitor annotationVisitor) {
        int readUnsignedShort = readUnsignedShort(i);
        int i2 = i + 2;
        int i3;
        if (z) {
            i3 = readUnsignedShort;
            readUnsignedShort = i2;
            i2 = i3;
            while (i2 > 0) {
                i2--;
                readUnsignedShort = a(readUnsignedShort + 2, cArr, readUTF8(readUnsignedShort, cArr), annotationVisitor);
            }
        } else {
            i3 = readUnsignedShort;
            readUnsignedShort = i2;
            i2 = i3;
            while (i2 > 0) {
                i2--;
                readUnsignedShort = a(readUnsignedShort, cArr, null, annotationVisitor);
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return readUnsignedShort;
    }

    private int a(Object[] objArr, int i, int i2, char[] cArr, Label[] labelArr) {
        int i3 = i2 + 1;
        switch (this.b[i2] & ByteCode.IMPDEP2) {
            case 0:
                objArr[i] = Opcodes.TOP;
                return i3;
            case 1:
                objArr[i] = Opcodes.INTEGER;
                return i3;
            case 2:
                objArr[i] = Opcodes.FLOAT;
                return i3;
            case 3:
                objArr[i] = Opcodes.DOUBLE;
                return i3;
            case 4:
                objArr[i] = Opcodes.LONG;
                return i3;
            case 5:
                objArr[i] = Opcodes.NULL;
                return i3;
            case 6:
                objArr[i] = Opcodes.UNINITIALIZED_THIS;
                return i3;
            case 7:
                objArr[i] = readClass(i3, cArr);
                return i3 + 2;
            default:
                objArr[i] = readLabel(readUnsignedShort(i3), labelArr);
                return i3 + 2;
        }
    }

    private String a(int i, int i2, char[] cArr) {
        int i3 = i + i2;
        byte[] bArr = this.b;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i < i3) {
            int i7;
            int i8 = i + 1;
            byte b = bArr[i];
            switch (i5) {
                case 0:
                    int i9 = b & ByteCode.IMPDEP2;
                    if (i9 >= 128) {
                        if (i9 < 224 && i9 > 191) {
                            i4 = (char) (i9 & 31);
                            i5 = 1;
                            i7 = i6;
                            break;
                        }
                        i4 = (char) (i9 & 15);
                        i5 = 2;
                        i7 = i6;
                        break;
                    }
                    i7 = i6 + 1;
                    cArr[i6] = (char) i9;
                    break;
                case 1:
                    i5 = i6 + 1;
                    cArr[i6] = (char) ((b & 63) | (i4 << 6));
                    i7 = i5;
                    i5 = 0;
                    break;
                case 2:
                    i4 = (char) ((i4 << 6) | (b & 63));
                    i5 = 1;
                    i7 = i6;
                    break;
                default:
                    i7 = i6;
                    break;
            }
            i6 = i7;
            i = i8;
        }
        return new String(cArr, 0, i6);
    }

    private Attribute a(Attribute[] attributeArr, String str, int i, int i2, char[] cArr, int i3, Label[] labelArr) {
        for (int i4 = 0; i4 < attributeArr.length; i4++) {
            if (attributeArr[i4].type.equals(str)) {
                return attributeArr[i4].read(this, i, i2, cArr, i3, labelArr);
            }
        }
        return new Attribute(str).read(this, i, i2, null, -1, null);
    }

    private void a(int i, String str, char[] cArr, boolean z, MethodVisitor methodVisitor) {
        int i2 = i + 1;
        int i3 = this.b[i] & ByteCode.IMPDEP2;
        int length = Type.getArgumentTypes(str).length - i3;
        int i4 = 0;
        while (i4 < length) {
            AnnotationVisitor visitParameterAnnotation = methodVisitor.visitParameterAnnotation(i4, "Ljava/lang/Synthetic;", false);
            if (visitParameterAnnotation != null) {
                visitParameterAnnotation.visitEnd();
            }
            i4++;
        }
        while (true) {
            int i5 = i4;
            if (i5 < i3 + length) {
                i2 += 2;
                for (i4 = readUnsignedShort(i2); i4 > 0; i4--) {
                    i2 = a(i2 + 2, cArr, true, methodVisitor.visitParameterAnnotation(i5, readUTF8(i2, cArr), z));
                }
                i4 = i5 + 1;
            } else {
                return;
            }
        }
    }

    private void a(ClassWriter classWriter, Item[] itemArr, char[] cArr) {
        int i;
        int i2 = this.header;
        int readUnsignedShort = ((readUnsignedShort(i2 + 6) << 1) + 8) + i2;
        i2 = readUnsignedShort(readUnsignedShort);
        readUnsignedShort += 2;
        for (i = i2; i > 0; i--) {
            readUnsignedShort += 8;
            for (i2 = readUnsignedShort(readUnsignedShort + 6); i2 > 0; i2--) {
                readUnsignedShort += readInt(readUnsignedShort + 2) + 6;
            }
        }
        i2 = readUnsignedShort(readUnsignedShort);
        readUnsignedShort += 2;
        for (i = i2; i > 0; i--) {
            readUnsignedShort += 8;
            for (i2 = readUnsignedShort(readUnsignedShort + 6); i2 > 0; i2--) {
                readUnsignedShort += readInt(readUnsignedShort + 2) + 6;
            }
        }
        i2 = readUnsignedShort(readUnsignedShort);
        int i3 = readUnsignedShort + 2;
        while (i2 > 0) {
            String readUTF8 = readUTF8(i3, cArr);
            int readInt = readInt(i3 + 2);
            if ("BootstrapMethods".equals(readUTF8)) {
                int readUnsignedShort2 = readUnsignedShort(i3 + 6);
                int i4 = i3 + 8;
                for (int i5 = 0; i5 < readUnsignedShort2; i5++) {
                    i = readConst(readUnsignedShort(i4), cArr).hashCode();
                    i2 = i4 + 4;
                    for (readUnsignedShort = readUnsignedShort(i4 + 2); readUnsignedShort > 0; readUnsignedShort--) {
                        i ^= readConst(readUnsignedShort(i2), cArr).hashCode();
                        i2 += 2;
                    }
                    Item item = new Item(i5);
                    item.a((i4 - i3) - 8, i & Integer.MAX_VALUE);
                    i = item.j % itemArr.length;
                    item.k = itemArr[i];
                    itemArr[i] = item;
                    i4 = i2;
                }
                classWriter.z = readUnsignedShort2;
                ByteVector byteVector = new ByteVector(readInt + 62);
                byteVector.putByteArray(this.b, i3 + 8, readInt - 2);
                classWriter.A = byteVector;
                return;
            }
            i2--;
            i3 = (readInt + 6) + i3;
        }
    }

    private static byte[] a(InputStream inputStream, boolean z) throws IOException {
        int i = 0;
        if (inputStream == null) {
            throw new IOException("Class not found");
        }
        try {
            byte[] bArr = new byte[inputStream.available()];
            while (true) {
                int i2 = i;
                i = inputStream.read(bArr, i2, bArr.length - i2);
                if (i == -1) {
                    byte[] bArr2;
                    if (i2 < bArr.length) {
                        bArr2 = new byte[i2];
                        System.arraycopy(bArr, 0, bArr2, 0, i2);
                    } else {
                        bArr2 = bArr;
                    }
                    if (!z) {
                        return bArr2;
                    }
                    inputStream.close();
                    return bArr2;
                }
                i2 += i;
                if (i2 == bArr.length) {
                    int read = inputStream.read();
                    if (read < 0) {
                        break;
                    }
                    byte[] bArr3 = new byte[(bArr.length + CloseFrame.NORMAL)];
                    System.arraycopy(bArr, 0, bArr3, 0, i2);
                    i = i2 + 1;
                    bArr = (byte) read;
                    bArr3[i2] = bArr;
                    bArr = bArr3;
                } else {
                    i = i2;
                }
            }
            return bArr;
        } finally {
            if (z) {
                inputStream.close();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(ClassWriter classWriter) {
        int i;
        char[] cArr = new char[this.d];
        int length = this.a.length;
        Item[] itemArr = new Item[length];
        int i2 = 1;
        while (i2 < length) {
            i = this.a[i2];
            byte b = this.b[i - 1];
            Item item = new Item(i2);
            int i3;
            int i4;
            switch (b) {
                case (byte) 1:
                    String str = this.c[i2];
                    if (str == null) {
                        i = this.a[i2];
                        String[] strArr = this.c;
                        str = a(i + 2, readUnsignedShort(i), cArr);
                        strArr[i2] = str;
                    }
                    item.a(b, str, null, null);
                    i = i2;
                    break;
                case (byte) 3:
                    item.a(readInt(i));
                    i = i2;
                    break;
                case (byte) 4:
                    item.a(Float.intBitsToFloat(readInt(i)));
                    i = i2;
                    break;
                case (byte) 5:
                    item.a(readLong(i));
                    i = i2 + 1;
                    break;
                case (byte) 6:
                    item.a(Double.longBitsToDouble(readLong(i)));
                    i = i2 + 1;
                    break;
                case (byte) 9:
                case (byte) 10:
                case (byte) 11:
                    i3 = this.a[readUnsignedShort(i + 2)];
                    item.a(b, readClass(i, cArr), readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                    i = i2;
                    break;
                case (byte) 12:
                    item.a(b, readUTF8(i, cArr), readUTF8(i + 2, cArr), null);
                    i = i2;
                    break;
                case (byte) 15:
                    i4 = this.a[readUnsignedShort(i + 1)];
                    i3 = this.a[readUnsignedShort(i4 + 2)];
                    item.a(readByte(i) + 20, readClass(i4, cArr), readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
                    i = i2;
                    break;
                case (byte) 18:
                    if (classWriter.A == null) {
                        a(classWriter, itemArr, cArr);
                    }
                    i4 = this.a[readUnsignedShort(i + 2)];
                    item.a(readUTF8(i4, cArr), readUTF8(i4 + 2, cArr), readUnsignedShort(i));
                    i = i2;
                    break;
                default:
                    item.a(b, readUTF8(i, cArr), null, null);
                    i = i2;
                    break;
            }
            i2 = item.j % itemArr.length;
            item.k = itemArr[i2];
            itemArr[i2] = item;
            i2 = i + 1;
        }
        i = this.a[1] - 1;
        classWriter.d.putByteArray(this.b, i, this.header - i);
        classWriter.e = itemArr;
        classWriter.f = (int) (0.75d * ((double) length));
        classWriter.c = length;
    }

    public void accept(ClassVisitor classVisitor, int i) {
        accept(classVisitor, new Attribute[0], i);
    }

    /* JADX WARNING: Missing block: B:367:0x0b68, code skipped:
            if (r13.charAt(r12) != 'L') goto L_0x0b78;
     */
    /* JADX WARNING: Missing block: B:368:0x0b6a, code skipped:
            r5 = r12 + 1;
     */
    /* JADX WARNING: Missing block: B:370:0x0b72, code skipped:
            if (r13.charAt(r5) == ';') goto L_0x0b77;
     */
    /* JADX WARNING: Missing block: B:371:0x0b74, code skipped:
            r5 = r5 + 1;
     */
    /* JADX WARNING: Missing block: B:372:0x0b77, code skipped:
            r12 = r5;
     */
    /* JADX WARNING: Missing block: B:373:0x0b78, code skipped:
            r5 = r4 + 1;
            r12 = r12 + 1;
            r14[r4] = r13.substring(r6, r12);
            r4 = r5;
            r6 = r12;
     */
    /* JADX WARNING: Missing block: B:377:0x0b91, code skipped:
            r5 = r4 + 1;
            r15 = r6 + 1;
            r6 = r12 + 1;
            r14[r4] = r13.substring(r15, r12);
            r4 = r5;
     */
    public void accept(org.objectweb.asm.ClassVisitor r50, org.objectweb.asm.Attribute[] r51, int r52) {
        /*
        r49 = this;
        r0 = r49;
        r0 = r0.b;
        r42 = r0;
        r0 = r49;
        r4 = r0.d;
        r9 = new char[r4];
        r26 = 0;
        r25 = 0;
        r24 = 0;
        r0 = r49;
        r4 = r0.header;
        r0 = r49;
        r12 = r0.readUnsignedShort(r4);
        r5 = r4 + 2;
        r0 = r49;
        r13 = r0.readClass(r5, r9);
        r0 = r49;
        r5 = r0.a;
        r6 = r4 + 4;
        r0 = r49;
        r6 = r0.readUnsignedShort(r6);
        r5 = r5[r6];
        if (r5 != 0) goto L_0x005e;
    L_0x0034:
        r15 = 0;
    L_0x0035:
        r5 = r4 + 6;
        r0 = r49;
        r5 = r0.readUnsignedShort(r5);
        r0 = new java.lang.String[r5];
        r16 = r0;
        r23 = 0;
        r5 = r4 + 8;
        r4 = 0;
        r17 = r5;
    L_0x0048:
        r0 = r16;
        r5 = r0.length;
        if (r4 >= r5) goto L_0x0065;
    L_0x004d:
        r0 = r49;
        r1 = r17;
        r5 = r0.readClass(r1, r9);
        r16[r4] = r5;
        r5 = r17 + 2;
        r4 = r4 + 1;
        r17 = r5;
        goto L_0x0048;
    L_0x005e:
        r0 = r49;
        r15 = r0.readUTF8(r5, r9);
        goto L_0x0035;
    L_0x0065:
        r4 = r52 & 1;
        if (r4 == 0) goto L_0x00a1;
    L_0x0069:
        r4 = 1;
        r41 = r4;
    L_0x006c:
        r4 = r52 & 2;
        if (r4 == 0) goto L_0x00a5;
    L_0x0070:
        r4 = 1;
        r40 = r4;
    L_0x0073:
        r4 = r52 & 8;
        if (r4 == 0) goto L_0x00a9;
    L_0x0077:
        r4 = 1;
        r27 = r4;
    L_0x007a:
        r0 = r49;
        r1 = r17;
        r4 = r0.readUnsignedShort(r1);
        r5 = r17 + 2;
        r6 = r4;
    L_0x0085:
        if (r6 <= 0) goto L_0x00b1;
    L_0x0087:
        r4 = r5 + 6;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r5 = r5 + 8;
    L_0x0091:
        if (r4 <= 0) goto L_0x00ad;
    L_0x0093:
        r7 = r5 + 2;
        r0 = r49;
        r7 = r0.readInt(r7);
        r7 = r7 + 6;
        r5 = r5 + r7;
        r4 = r4 + -1;
        goto L_0x0091;
    L_0x00a1:
        r4 = 0;
        r41 = r4;
        goto L_0x006c;
    L_0x00a5:
        r4 = 0;
        r40 = r4;
        goto L_0x0073;
    L_0x00a9:
        r4 = 0;
        r27 = r4;
        goto L_0x007a;
    L_0x00ad:
        r4 = r6 + -1;
        r6 = r4;
        goto L_0x0085;
    L_0x00b1:
        r0 = r49;
        r4 = r0.readUnsignedShort(r5);
        r5 = r5 + 2;
        r6 = r4;
    L_0x00ba:
        if (r6 <= 0) goto L_0x00da;
    L_0x00bc:
        r4 = r5 + 6;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r5 = r5 + 8;
    L_0x00c6:
        if (r4 <= 0) goto L_0x00d6;
    L_0x00c8:
        r7 = r5 + 2;
        r0 = r49;
        r7 = r0.readInt(r7);
        r7 = r7 + 6;
        r5 = r5 + r7;
        r4 = r4 + -1;
        goto L_0x00c6;
    L_0x00d6:
        r4 = r6 + -1;
        r6 = r4;
        goto L_0x00ba;
    L_0x00da:
        r14 = 0;
        r22 = 0;
        r21 = 0;
        r20 = 0;
        r19 = 0;
        r18 = 0;
        r28 = 0;
        r0 = r49;
        r4 = r0.readUnsignedShort(r5);
        r5 = r5 + 2;
        r29 = r4;
        r30 = r5;
    L_0x00f3:
        if (r29 <= 0) goto L_0x02f3;
    L_0x00f5:
        r0 = r49;
        r1 = r30;
        r6 = r0.readUTF8(r1, r9);
        r4 = "SourceFile";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0149;
    L_0x0105:
        r4 = r30 + 6;
        r0 = r49;
        r4 = r0.readUTF8(r4, r9);
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r4;
        r11 = r23;
        r4 = r28;
        r18 = r24;
        r19 = r25;
        r20 = r26;
    L_0x0120:
        r21 = r30 + 2;
        r0 = r49;
        r1 = r21;
        r21 = r0.readInt(r1);
        r21 = r21 + 6;
        r22 = r30 + r21;
        r21 = r29 + -1;
        r28 = r4;
        r29 = r21;
        r23 = r11;
        r30 = r22;
        r24 = r18;
        r25 = r19;
        r26 = r20;
        r19 = r6;
        r20 = r7;
        r21 = r8;
        r22 = r10;
        r18 = r5;
        goto L_0x00f3;
    L_0x0149:
        r4 = "InnerClasses";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0167;
    L_0x0151:
        r4 = r30 + 6;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r4;
        r4 = r28;
        r18 = r24;
        r19 = r25;
        r20 = r26;
        goto L_0x0120;
    L_0x0167:
        r4 = "EnclosingMethod";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x01ae;
    L_0x016f:
        r4 = r30 + 6;
        r0 = r49;
        r6 = r0.readClass(r4, r9);
        r4 = r30 + 8;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        if (r4 == 0) goto L_0x1072;
    L_0x0181:
        r0 = r49;
        r5 = r0.a;
        r5 = r5[r4];
        r0 = r49;
        r5 = r0.readUTF8(r5, r9);
        r0 = r49;
        r7 = r0.a;
        r4 = r7[r4];
        r4 = r4 + 2;
        r0 = r49;
        r4 = r0.readUTF8(r4, r9);
    L_0x019b:
        r7 = r6;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r24;
        r19 = r25;
        r20 = r26;
        r6 = r5;
        r5 = r4;
        r4 = r28;
        goto L_0x0120;
    L_0x01ae:
        r4 = "Signature";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x01d4;
    L_0x01b6:
        r4 = r30 + 6;
        r0 = r49;
        r14 = r0.readUTF8(r4, r9);
        r4 = r28;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r24;
        r19 = r25;
        r20 = r26;
        goto L_0x0120;
    L_0x01d4:
        r4 = "RuntimeVisibleAnnotations";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x01f4;
    L_0x01dc:
        r4 = r30 + 6;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r24;
        r19 = r25;
        r20 = r4;
        r4 = r28;
        goto L_0x0120;
    L_0x01f4:
        r4 = "Deprecated";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0215;
    L_0x01fc:
        r4 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r12 = r12 | r4;
        r4 = r28;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r24;
        r19 = r25;
        r20 = r26;
        goto L_0x0120;
    L_0x0215:
        r4 = "Synthetic";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0237;
    L_0x021d:
        r4 = 266240; // 0x41000 float:3.73082E-40 double:1.3154E-318;
        r12 = r12 | r4;
        r4 = r28;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r24;
        r19 = r25;
        r20 = r26;
        goto L_0x0120;
    L_0x0237:
        r4 = "SourceDebugExtension";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0266;
    L_0x023f:
        r4 = r30 + 2;
        r0 = r49;
        r4 = r0.readInt(r4);
        r5 = r30 + 6;
        r6 = new char[r4];
        r0 = r49;
        r4 = r0.a(r5, r4, r6);
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r4;
        r10 = r22;
        r11 = r23;
        r4 = r28;
        r18 = r24;
        r19 = r25;
        r20 = r26;
        goto L_0x0120;
    L_0x0266:
        r4 = "RuntimeInvisibleAnnotations";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0286;
    L_0x026e:
        r4 = r30 + 6;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r24;
        r19 = r4;
        r20 = r26;
        r4 = r28;
        goto L_0x0120;
    L_0x0286:
        r4 = "BootstrapMethods";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x02c3;
    L_0x028e:
        r4 = r30 + 6;
        r0 = r49;
        r7 = r0.readUnsignedShort(r4);
        r4 = new int[r7];
        r5 = r30 + 8;
        r6 = 0;
    L_0x029b:
        if (r6 >= r7) goto L_0x02af;
    L_0x029d:
        r4[r6] = r5;
        r8 = r5 + 2;
        r0 = r49;
        r8 = r0.readUnsignedShort(r8);
        r8 = r8 + 2;
        r8 = r8 << 1;
        r5 = r5 + r8;
        r6 = r6 + 1;
        goto L_0x029b;
    L_0x02af:
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r24;
        r19 = r25;
        r20 = r26;
        goto L_0x0120;
    L_0x02c3:
        r7 = r30 + 6;
        r4 = r30 + 2;
        r0 = r49;
        r8 = r0.readInt(r4);
        r10 = -1;
        r11 = 0;
        r4 = r49;
        r5 = r51;
        r4 = r4.a(r5, r6, r7, r8, r9, r10, r11);
        if (r4 == 0) goto L_0x105c;
    L_0x02d9:
        r0 = r24;
        r4.a = r0;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r4;
        r19 = r25;
        r20 = r26;
        r4 = r28;
        goto L_0x0120;
    L_0x02f3:
        r4 = 4;
        r0 = r49;
        r11 = r0.readInt(r4);
        r10 = r50;
        r10.visit(r11, r12, r13, r14, r15, r16);
        if (r40 != 0) goto L_0x030e;
    L_0x0301:
        if (r22 != 0) goto L_0x0305;
    L_0x0303:
        if (r21 == 0) goto L_0x030e;
    L_0x0305:
        r0 = r50;
        r1 = r22;
        r2 = r21;
        r0.visitSource(r1, r2);
    L_0x030e:
        if (r20 == 0) goto L_0x031b;
    L_0x0310:
        r0 = r50;
        r1 = r20;
        r2 = r19;
        r3 = r18;
        r0.visitOuterClass(r1, r2, r3);
    L_0x031b:
        r4 = 1;
        r7 = r4;
    L_0x031d:
        if (r7 < 0) goto L_0x035a;
    L_0x031f:
        if (r7 != 0) goto L_0x0351;
    L_0x0321:
        r5 = r25;
    L_0x0323:
        if (r5 == 0) goto L_0x0356;
    L_0x0325:
        r0 = r49;
        r4 = r0.readUnsignedShort(r5);
        r5 = r5 + 2;
        r48 = r4;
        r4 = r5;
        r5 = r48;
    L_0x0332:
        if (r5 <= 0) goto L_0x0356;
    L_0x0334:
        r6 = r4 + 2;
        r8 = 1;
        r0 = r49;
        r10 = r0.readUTF8(r4, r9);
        if (r7 == 0) goto L_0x0354;
    L_0x033f:
        r4 = 1;
    L_0x0340:
        r0 = r50;
        r4 = r0.visitAnnotation(r10, r4);
        r0 = r49;
        r6 = r0.a(r6, r9, r8, r4);
        r4 = r5 + -1;
        r5 = r4;
        r4 = r6;
        goto L_0x0332;
    L_0x0351:
        r5 = r26;
        goto L_0x0323;
    L_0x0354:
        r4 = 0;
        goto L_0x0340;
    L_0x0356:
        r4 = r7 + -1;
        r7 = r4;
        goto L_0x031d;
    L_0x035a:
        if (r24 == 0) goto L_0x036f;
    L_0x035c:
        r0 = r24;
        r4 = r0.a;
        r5 = 0;
        r0 = r24;
        r0.a = r5;
        r0 = r50;
        r1 = r24;
        r0.visitAttribute(r1);
        r24 = r4;
        goto L_0x035a;
    L_0x036f:
        if (r23 == 0) goto L_0x03cb;
    L_0x0371:
        r0 = r49;
        r1 = r23;
        r4 = r0.readUnsignedShort(r1);
        r5 = r23 + 2;
        r7 = r4;
        r8 = r5;
    L_0x037d:
        if (r7 <= 0) goto L_0x03cb;
    L_0x037f:
        r0 = r49;
        r4 = r0.readUnsignedShort(r8);
        if (r4 != 0) goto L_0x03b2;
    L_0x0387:
        r4 = 0;
    L_0x0388:
        r5 = r8 + 2;
        r0 = r49;
        r5 = r0.readUnsignedShort(r5);
        if (r5 != 0) goto L_0x03b9;
    L_0x0392:
        r5 = 0;
    L_0x0393:
        r6 = r8 + 4;
        r0 = r49;
        r6 = r0.readUnsignedShort(r6);
        if (r6 != 0) goto L_0x03c2;
    L_0x039d:
        r6 = 0;
    L_0x039e:
        r10 = r8 + 6;
        r0 = r49;
        r10 = r0.readUnsignedShort(r10);
        r0 = r50;
        r0.visitInnerClass(r4, r5, r6, r10);
        r5 = r8 + 8;
        r4 = r7 + -1;
        r7 = r4;
        r8 = r5;
        goto L_0x037d;
    L_0x03b2:
        r0 = r49;
        r4 = r0.readClass(r8, r9);
        goto L_0x0388;
    L_0x03b9:
        r5 = r8 + 2;
        r0 = r49;
        r5 = r0.readClass(r5, r9);
        goto L_0x0393;
    L_0x03c2:
        r6 = r8 + 4;
        r0 = r49;
        r6 = r0.readUTF8(r6, r9);
        goto L_0x039e;
    L_0x03cb:
        r0 = r49;
        r1 = r17;
        r4 = r0.readUnsignedShort(r1);
        r21 = r17 + 2;
        r22 = r4;
    L_0x03d7:
        if (r22 <= 0) goto L_0x0542;
    L_0x03d9:
        r0 = r49;
        r1 = r21;
        r16 = r0.readUnsignedShort(r1);
        r4 = r21 + 2;
        r0 = r49;
        r12 = r0.readUTF8(r4, r9);
        r4 = r21 + 4;
        r0 = r49;
        r13 = r0.readUTF8(r4, r9);
        r15 = 0;
        r14 = 0;
        r19 = 0;
        r18 = 0;
        r17 = 0;
        r4 = r21 + 6;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r5 = r21 + 8;
        r20 = r4;
        r21 = r5;
    L_0x0407:
        if (r20 <= 0) goto L_0x04d5;
    L_0x0409:
        r0 = r49;
        r1 = r21;
        r6 = r0.readUTF8(r1, r9);
        r4 = "ConstantValue";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0445;
    L_0x0419:
        r4 = r21 + 6;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r5 = r16;
        r6 = r17;
        r7 = r18;
        r8 = r19;
    L_0x0429:
        r10 = r21 + 2;
        r0 = r49;
        r10 = r0.readInt(r10);
        r10 = r10 + 6;
        r11 = r21 + r10;
        r10 = r20 + -1;
        r15 = r4;
        r20 = r10;
        r16 = r5;
        r21 = r11;
        r17 = r6;
        r18 = r7;
        r19 = r8;
        goto L_0x0407;
    L_0x0445:
        r4 = "Signature";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x045f;
    L_0x044d:
        r4 = r21 + 6;
        r0 = r49;
        r14 = r0.readUTF8(r4, r9);
        r4 = r15;
        r5 = r16;
        r6 = r17;
        r7 = r18;
        r8 = r19;
        goto L_0x0429;
    L_0x045f:
        r4 = "Deprecated";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0474;
    L_0x0467:
        r4 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r4 = r4 | r16;
        r5 = r4;
        r6 = r17;
        r7 = r18;
        r8 = r19;
        r4 = r15;
        goto L_0x0429;
    L_0x0474:
        r4 = "Synthetic";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x048a;
    L_0x047c:
        r4 = 266240; // 0x41000 float:3.73082E-40 double:1.3154E-318;
        r4 = r4 | r16;
        r5 = r4;
        r6 = r17;
        r7 = r18;
        r8 = r19;
        r4 = r15;
        goto L_0x0429;
    L_0x048a:
        r4 = "RuntimeVisibleAnnotations";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x049d;
    L_0x0492:
        r4 = r21 + 6;
        r5 = r16;
        r6 = r17;
        r7 = r18;
        r8 = r4;
        r4 = r15;
        goto L_0x0429;
    L_0x049d:
        r4 = "RuntimeInvisibleAnnotations";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x04b1;
    L_0x04a5:
        r4 = r21 + 6;
        r5 = r16;
        r6 = r17;
        r7 = r4;
        r8 = r19;
        r4 = r15;
        goto L_0x0429;
    L_0x04b1:
        r7 = r21 + 6;
        r4 = r21 + 2;
        r0 = r49;
        r8 = r0.readInt(r4);
        r10 = -1;
        r11 = 0;
        r4 = r49;
        r5 = r51;
        r4 = r4.a(r5, r6, r7, r8, r9, r10, r11);
        if (r4 == 0) goto L_0x1051;
    L_0x04c7:
        r0 = r17;
        r4.a = r0;
        r5 = r16;
        r6 = r4;
        r7 = r18;
        r8 = r19;
        r4 = r15;
        goto L_0x0429;
    L_0x04d5:
        if (r15 != 0) goto L_0x0516;
    L_0x04d7:
        r15 = 0;
    L_0x04d8:
        r10 = r50;
        r11 = r16;
        r8 = r10.visitField(r11, r12, r13, r14, r15);
        if (r8 == 0) goto L_0x053c;
    L_0x04e2:
        r4 = 1;
        r7 = r4;
    L_0x04e4:
        if (r7 < 0) goto L_0x0526;
    L_0x04e6:
        if (r7 != 0) goto L_0x051d;
    L_0x04e8:
        r5 = r18;
    L_0x04ea:
        if (r5 == 0) goto L_0x0522;
    L_0x04ec:
        r0 = r49;
        r4 = r0.readUnsignedShort(r5);
        r5 = r5 + 2;
        r48 = r4;
        r4 = r5;
        r5 = r48;
    L_0x04f9:
        if (r5 <= 0) goto L_0x0522;
    L_0x04fb:
        r6 = r4 + 2;
        r10 = 1;
        r0 = r49;
        r11 = r0.readUTF8(r4, r9);
        if (r7 == 0) goto L_0x0520;
    L_0x0506:
        r4 = 1;
    L_0x0507:
        r4 = r8.visitAnnotation(r11, r4);
        r0 = r49;
        r6 = r0.a(r6, r9, r10, r4);
        r4 = r5 + -1;
        r5 = r4;
        r4 = r6;
        goto L_0x04f9;
    L_0x0516:
        r0 = r49;
        r15 = r0.readConst(r15, r9);
        goto L_0x04d8;
    L_0x051d:
        r5 = r19;
        goto L_0x04ea;
    L_0x0520:
        r4 = 0;
        goto L_0x0507;
    L_0x0522:
        r4 = r7 + -1;
        r7 = r4;
        goto L_0x04e4;
    L_0x0526:
        if (r17 == 0) goto L_0x0539;
    L_0x0528:
        r0 = r17;
        r4 = r0.a;
        r5 = 0;
        r0 = r17;
        r0.a = r5;
        r0 = r17;
        r8.visitAttribute(r0);
        r17 = r4;
        goto L_0x0526;
    L_0x0539:
        r8.visitEnd();
    L_0x053c:
        r4 = r22 + -1;
        r22 = r4;
        goto L_0x03d7;
    L_0x0542:
        r0 = r49;
        r1 = r21;
        r4 = r0.readUnsignedShort(r1);
        r38 = r21 + 2;
        r39 = r4;
    L_0x054e:
        if (r39 <= 0) goto L_0x0ff8;
    L_0x0550:
        r25 = r38 + 6;
        r0 = r49;
        r1 = r38;
        r21 = r0.readUnsignedShort(r1);
        r4 = r38 + 2;
        r0 = r49;
        r12 = r0.readUTF8(r4, r9);
        r4 = r38 + 4;
        r0 = r49;
        r13 = r0.readUTF8(r4, r9);
        r14 = 0;
        r23 = 0;
        r22 = 0;
        r18 = 0;
        r17 = 0;
        r16 = 0;
        r20 = 0;
        r19 = 0;
        r15 = 0;
        r4 = r38 + 6;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r5 = r38 + 8;
        r24 = r4;
        r38 = r5;
    L_0x0588:
        if (r24 <= 0) goto L_0x06ee;
    L_0x058a:
        r0 = r49;
        r1 = r38;
        r6 = r0.readUTF8(r1, r9);
        r4 = r38 + 2;
        r0 = r49;
        r8 = r0.readInt(r4);
        r7 = r38 + 6;
        r4 = "Code";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x05cf;
    L_0x05a4:
        if (r41 != 0) goto L_0x103e;
    L_0x05a6:
        r4 = r16;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r7;
        r15 = r21;
        r16 = r20;
        r17 = r22;
        r18 = r23;
    L_0x05b6:
        r8 = r8 + r7;
        r7 = r24 + -1;
        r24 = r7;
        r19 = r11;
        r21 = r15;
        r38 = r8;
        r20 = r16;
        r22 = r17;
        r23 = r18;
        r16 = r4;
        r15 = r10;
        r17 = r5;
        r18 = r6;
        goto L_0x0588;
    L_0x05cf:
        r4 = "Exceptions";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x05e9;
    L_0x05d7:
        r4 = r16;
        r5 = r17;
        r6 = r18;
        r10 = r7;
        r11 = r19;
        r15 = r21;
        r16 = r20;
        r17 = r22;
        r18 = r23;
        goto L_0x05b6;
    L_0x05e9:
        r4 = "Signature";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0609;
    L_0x05f1:
        r0 = r49;
        r14 = r0.readUTF8(r7, r9);
        r4 = r16;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r15 = r21;
        r16 = r20;
        r17 = r22;
        r18 = r23;
        goto L_0x05b6;
    L_0x0609:
        r4 = "Deprecated";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0626;
    L_0x0611:
        r4 = 131072; // 0x20000 float:1.83671E-40 double:6.47582E-319;
        r4 = r4 | r21;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r15 = r4;
        r17 = r22;
        r18 = r23;
        r4 = r16;
        r16 = r20;
        goto L_0x05b6;
    L_0x0626:
        r4 = "RuntimeVisibleAnnotations";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0641;
    L_0x062e:
        r4 = r16;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r15 = r21;
        r16 = r20;
        r17 = r22;
        r18 = r7;
        goto L_0x05b6;
    L_0x0641:
        r4 = "AnnotationDefault";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x065b;
    L_0x0649:
        r4 = r16;
        r5 = r17;
        r6 = r7;
        r10 = r15;
        r11 = r19;
        r18 = r23;
        r16 = r20;
        r17 = r22;
        r15 = r21;
        goto L_0x05b6;
    L_0x065b:
        r4 = "Synthetic";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x067a;
    L_0x0663:
        r4 = 266240; // 0x41000 float:3.73082E-40 double:1.3154E-318;
        r4 = r4 | r21;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r15 = r4;
        r17 = r22;
        r18 = r23;
        r4 = r16;
        r16 = r20;
        goto L_0x05b6;
    L_0x067a:
        r4 = "RuntimeInvisibleAnnotations";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x0695;
    L_0x0682:
        r4 = r16;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r15 = r21;
        r16 = r20;
        r17 = r7;
        r18 = r23;
        goto L_0x05b6;
    L_0x0695:
        r4 = "RuntimeVisibleParameterAnnotations";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x06af;
    L_0x069d:
        r4 = r16;
        r5 = r7;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r17 = r22;
        r16 = r20;
        r15 = r21;
        r18 = r23;
        goto L_0x05b6;
    L_0x06af:
        r4 = "RuntimeInvisibleParameterAnnotations";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x06c9;
    L_0x06b7:
        r4 = r7;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r16 = r20;
        r15 = r21;
        r17 = r22;
        r18 = r23;
        goto L_0x05b6;
    L_0x06c9:
        r10 = -1;
        r11 = 0;
        r4 = r49;
        r5 = r51;
        r4 = r4.a(r5, r6, r7, r8, r9, r10, r11);
        if (r4 == 0) goto L_0x103e;
    L_0x06d5:
        r0 = r20;
        r4.a = r0;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r15 = r21;
        r17 = r22;
        r18 = r23;
        r48 = r16;
        r16 = r4;
        r4 = r48;
        goto L_0x05b6;
    L_0x06ee:
        if (r15 != 0) goto L_0x0728;
    L_0x06f0:
        r4 = 0;
        r5 = r15;
        r15 = r4;
    L_0x06f3:
        r10 = r50;
        r11 = r21;
        r11 = r10.visitMethod(r11, r12, r13, r14, r15);
        if (r11 == 0) goto L_0x07e1;
    L_0x06fd:
        r4 = r11 instanceof org.objectweb.asm.MethodWriter;
        if (r4 == 0) goto L_0x0768;
    L_0x0701:
        r4 = r11;
        r4 = (org.objectweb.asm.MethodWriter) r4;
        r6 = r4.b;
        r6 = r6.M;
        r0 = r49;
        if (r6 != r0) goto L_0x0768;
    L_0x070c:
        r6 = r4.g;
        if (r14 != r6) goto L_0x0768;
    L_0x0710:
        r6 = 0;
        if (r15 != 0) goto L_0x0745;
    L_0x0713:
        r5 = r4.j;
        if (r5 != 0) goto L_0x0743;
    L_0x0717:
        r5 = 1;
    L_0x0718:
        if (r5 == 0) goto L_0x0768;
    L_0x071a:
        r0 = r25;
        r4.h = r0;
        r5 = r38 - r25;
        r4.i = r5;
    L_0x0722:
        r4 = r39 + -1;
        r39 = r4;
        goto L_0x054e;
    L_0x0728:
        r0 = r49;
        r4 = r0.readUnsignedShort(r15);
        r6 = new java.lang.String[r4];
        r5 = r15 + 2;
        r4 = 0;
    L_0x0733:
        r7 = r6.length;
        if (r4 >= r7) goto L_0x103b;
    L_0x0736:
        r0 = r49;
        r7 = r0.readClass(r5, r9);
        r6[r4] = r7;
        r5 = r5 + 2;
        r4 = r4 + 1;
        goto L_0x0733;
    L_0x0743:
        r5 = 0;
        goto L_0x0718;
    L_0x0745:
        r7 = r15.length;
        r8 = r4.j;
        if (r7 != r8) goto L_0x1038;
    L_0x074a:
        r6 = 1;
        r7 = r15.length;
        r7 = r7 + -1;
        r48 = r7;
        r7 = r5;
        r5 = r48;
    L_0x0753:
        if (r5 < 0) goto L_0x1038;
    L_0x0755:
        r7 = r7 + -2;
        r8 = r4.k;
        r8 = r8[r5];
        r0 = r49;
        r10 = r0.readUnsignedShort(r7);
        if (r8 == r10) goto L_0x0765;
    L_0x0763:
        r5 = 0;
        goto L_0x0718;
    L_0x0765:
        r5 = r5 + -1;
        goto L_0x0753;
    L_0x0768:
        if (r18 == 0) goto L_0x077b;
    L_0x076a:
        r4 = r11.visitAnnotationDefault();
        r5 = 0;
        r0 = r49;
        r1 = r18;
        r0.a(r1, r9, r5, r4);
        if (r4 == 0) goto L_0x077b;
    L_0x0778:
        r4.visitEnd();
    L_0x077b:
        r4 = 1;
        r7 = r4;
    L_0x077d:
        if (r7 < 0) goto L_0x07b8;
    L_0x077f:
        if (r7 != 0) goto L_0x07af;
    L_0x0781:
        r5 = r22;
    L_0x0783:
        if (r5 == 0) goto L_0x07b4;
    L_0x0785:
        r0 = r49;
        r4 = r0.readUnsignedShort(r5);
        r5 = r5 + 2;
        r48 = r4;
        r4 = r5;
        r5 = r48;
    L_0x0792:
        if (r5 <= 0) goto L_0x07b4;
    L_0x0794:
        r6 = r4 + 2;
        r8 = 1;
        r0 = r49;
        r10 = r0.readUTF8(r4, r9);
        if (r7 == 0) goto L_0x07b2;
    L_0x079f:
        r4 = 1;
    L_0x07a0:
        r4 = r11.visitAnnotation(r10, r4);
        r0 = r49;
        r6 = r0.a(r6, r9, r8, r4);
        r4 = r5 + -1;
        r5 = r4;
        r4 = r6;
        goto L_0x0792;
    L_0x07af:
        r5 = r23;
        goto L_0x0783;
    L_0x07b2:
        r4 = 0;
        goto L_0x07a0;
    L_0x07b4:
        r4 = r7 + -1;
        r7 = r4;
        goto L_0x077d;
    L_0x07b8:
        if (r17 == 0) goto L_0x07c3;
    L_0x07ba:
        r10 = 1;
        r6 = r49;
        r7 = r17;
        r8 = r13;
        r6.a(r7, r8, r9, r10, r11);
    L_0x07c3:
        if (r16 == 0) goto L_0x07ce;
    L_0x07c5:
        r10 = 0;
        r6 = r49;
        r7 = r16;
        r8 = r13;
        r6.a(r7, r8, r9, r10, r11);
    L_0x07ce:
        if (r20 == 0) goto L_0x07e1;
    L_0x07d0:
        r0 = r20;
        r4 = r0.a;
        r5 = 0;
        r0 = r20;
        r0.a = r5;
        r0 = r20;
        r11.visitAttribute(r0);
        r20 = r4;
        goto L_0x07ce;
    L_0x07e1:
        if (r11 == 0) goto L_0x0ff1;
    L_0x07e3:
        if (r19 == 0) goto L_0x0ff1;
    L_0x07e5:
        r0 = r49;
        r1 = r19;
        r43 = r0.readUnsignedShort(r1);
        r4 = r19 + 2;
        r0 = r49;
        r44 = r0.readUnsignedShort(r4);
        r4 = r19 + 4;
        r0 = r49;
        r45 = r0.readInt(r4);
        r35 = r19 + 8;
        r46 = r35 + r45;
        r11.visitCode();
        r4 = r45 + 2;
        r0 = new org.objectweb.asm.Label[r4];
        r20 = r0;
        r4 = r45 + 1;
        r0 = r49;
        r1 = r20;
        r0.readLabel(r4, r1);
        r5 = r35;
    L_0x0815:
        r0 = r46;
        if (r5 >= r0) goto L_0x08f0;
    L_0x0819:
        r7 = r5 - r35;
        r4 = r42[r5];
        r4 = r4 & 255;
        r6 = org.objectweb.asm.ClassWriter.a;
        r4 = r6[r4];
        switch(r4) {
            case 0: goto L_0x082a;
            case 1: goto L_0x08e4;
            case 2: goto L_0x08e8;
            case 3: goto L_0x08e4;
            case 4: goto L_0x082a;
            case 5: goto L_0x08e8;
            case 6: goto L_0x08e8;
            case 7: goto L_0x08ec;
            case 8: goto L_0x08ec;
            case 9: goto L_0x082d;
            case 10: goto L_0x0840;
            case 11: goto L_0x08e4;
            case 12: goto L_0x08e8;
            case 13: goto L_0x08e8;
            case 14: goto L_0x0863;
            case 15: goto L_0x08a8;
            case 16: goto L_0x0826;
            case 17: goto L_0x0853;
            default: goto L_0x0826;
        };
    L_0x0826:
        r4 = r5 + 4;
    L_0x0828:
        r5 = r4;
        goto L_0x0815;
    L_0x082a:
        r4 = r5 + 1;
        goto L_0x0828;
    L_0x082d:
        r4 = r5 + 1;
        r0 = r49;
        r4 = r0.readShort(r4);
        r4 = r4 + r7;
        r0 = r49;
        r1 = r20;
        r0.readLabel(r4, r1);
        r4 = r5 + 3;
        goto L_0x0828;
    L_0x0840:
        r4 = r5 + 1;
        r0 = r49;
        r4 = r0.readInt(r4);
        r4 = r4 + r7;
        r0 = r49;
        r1 = r20;
        r0.readLabel(r4, r1);
        r4 = r5 + 5;
        goto L_0x0828;
    L_0x0853:
        r4 = r5 + 1;
        r4 = r42[r4];
        r4 = r4 & 255;
        r6 = 132; // 0x84 float:1.85E-43 double:6.5E-322;
        if (r4 != r6) goto L_0x0860;
    L_0x085d:
        r4 = r5 + 6;
        goto L_0x0828;
    L_0x0860:
        r4 = r5 + 4;
        goto L_0x0828;
    L_0x0863:
        r4 = r5 + 4;
        r5 = r7 & 3;
        r5 = r4 - r5;
        r0 = r49;
        r4 = r0.readInt(r5);
        r4 = r4 + r7;
        r0 = r49;
        r1 = r20;
        r0.readLabel(r4, r1);
        r4 = r5 + 8;
        r0 = r49;
        r4 = r0.readInt(r4);
        r6 = r5 + 4;
        r0 = r49;
        r6 = r0.readInt(r6);
        r4 = r4 - r6;
        r4 = r4 + 1;
        r5 = r5 + 12;
        r48 = r4;
        r4 = r5;
        r5 = r48;
    L_0x0891:
        if (r5 <= 0) goto L_0x0828;
    L_0x0893:
        r0 = r49;
        r6 = r0.readInt(r4);
        r6 = r6 + r7;
        r0 = r49;
        r1 = r20;
        r0.readLabel(r6, r1);
        r6 = r4 + 4;
        r4 = r5 + -1;
        r5 = r4;
        r4 = r6;
        goto L_0x0891;
    L_0x08a8:
        r4 = r5 + 4;
        r5 = r7 & 3;
        r5 = r4 - r5;
        r0 = r49;
        r4 = r0.readInt(r5);
        r4 = r4 + r7;
        r0 = r49;
        r1 = r20;
        r0.readLabel(r4, r1);
        r4 = r5 + 4;
        r0 = r49;
        r4 = r0.readInt(r4);
        r5 = r5 + 8;
        r48 = r4;
        r4 = r5;
        r5 = r48;
    L_0x08cb:
        if (r5 <= 0) goto L_0x0828;
    L_0x08cd:
        r6 = r4 + 4;
        r0 = r49;
        r6 = r0.readInt(r6);
        r6 = r6 + r7;
        r0 = r49;
        r1 = r20;
        r0.readLabel(r6, r1);
        r6 = r4 + 8;
        r4 = r5 + -1;
        r5 = r4;
        r4 = r6;
        goto L_0x08cb;
    L_0x08e4:
        r4 = r5 + 2;
        goto L_0x0828;
    L_0x08e8:
        r4 = r5 + 3;
        goto L_0x0828;
    L_0x08ec:
        r4 = r5 + 5;
        goto L_0x0828;
    L_0x08f0:
        r0 = r49;
        r4 = r0.readUnsignedShort(r5);
        r5 = r5 + 2;
    L_0x08f8:
        if (r4 <= 0) goto L_0x094b;
    L_0x08fa:
        r0 = r49;
        r6 = r0.readUnsignedShort(r5);
        r0 = r49;
        r1 = r20;
        r6 = r0.readLabel(r6, r1);
        r7 = r5 + 2;
        r0 = r49;
        r7 = r0.readUnsignedShort(r7);
        r0 = r49;
        r1 = r20;
        r7 = r0.readLabel(r7, r1);
        r8 = r5 + 4;
        r0 = r49;
        r8 = r0.readUnsignedShort(r8);
        r0 = r49;
        r1 = r20;
        r8 = r0.readLabel(r8, r1);
        r10 = r5 + 6;
        r0 = r49;
        r10 = r0.readUnsignedShort(r10);
        if (r10 != 0) goto L_0x093b;
    L_0x0932:
        r10 = 0;
        r11.visitTryCatchBlock(r6, r7, r8, r10);
    L_0x0936:
        r5 = r5 + 8;
        r4 = r4 + -1;
        goto L_0x08f8;
    L_0x093b:
        r0 = r49;
        r14 = r0.a;
        r10 = r14[r10];
        r0 = r49;
        r10 = r0.readUTF8(r10, r9);
        r11.visitTryCatchBlock(r6, r7, r8, r10);
        goto L_0x0936;
    L_0x094b:
        r32 = 0;
        r31 = 0;
        r10 = 0;
        r8 = 0;
        r7 = 0;
        r34 = 0;
        r25 = 0;
        r22 = 0;
        r33 = 0;
        r26 = 0;
        r24 = 0;
        r23 = 0;
        r30 = 1;
        r29 = 0;
        r0 = r49;
        r4 = r0.readUnsignedShort(r5);
        r5 = r5 + 2;
        r36 = r4;
        r37 = r5;
    L_0x0970:
        if (r36 <= 0) goto L_0x0ac8;
    L_0x0972:
        r0 = r49;
        r1 = r37;
        r47 = r0.readUTF8(r1, r9);
        r4 = "LocalVariableTable";
        r0 = r47;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x09d0;
    L_0x0984:
        if (r40 != 0) goto L_0x1013;
    L_0x0986:
        r4 = r37 + 6;
        r5 = r37 + 6;
        r0 = r49;
        r5 = r0.readUnsignedShort(r5);
        r6 = r37 + 8;
    L_0x0992:
        if (r5 <= 0) goto L_0x101e;
    L_0x0994:
        r0 = r49;
        r14 = r0.readUnsignedShort(r6);
        r15 = r20[r14];
        if (r15 != 0) goto L_0x09b0;
    L_0x099e:
        r0 = r49;
        r1 = r20;
        r15 = r0.readLabel(r14, r1);
        r0 = r15.a;
        r16 = r0;
        r16 = r16 | 1;
        r0 = r16;
        r15.a = r0;
    L_0x09b0:
        r15 = r6 + 2;
        r0 = r49;
        r15 = r0.readUnsignedShort(r15);
        r14 = r14 + r15;
        r15 = r20[r14];
        if (r15 != 0) goto L_0x09cb;
    L_0x09bd:
        r0 = r49;
        r1 = r20;
        r14 = r0.readLabel(r14, r1);
        r15 = r14.a;
        r15 = r15 | 1;
        r14.a = r15;
    L_0x09cb:
        r6 = r6 + 10;
        r5 = r5 + -1;
        goto L_0x0992;
    L_0x09d0:
        r4 = "LocalVariableTypeTable";
        r0 = r47;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0a01;
    L_0x09da:
        r4 = r37 + 6;
        r5 = r7;
        r6 = r8;
        r7 = r10;
        r8 = r4;
        r4 = r30;
        r10 = r32;
    L_0x09e4:
        r14 = r37 + 2;
        r0 = r49;
        r14 = r0.readInt(r14);
        r14 = r14 + 6;
        r15 = r37 + r14;
        r14 = r36 + -1;
        r30 = r4;
        r31 = r8;
        r32 = r10;
        r36 = r14;
        r37 = r15;
        r8 = r6;
        r10 = r7;
        r7 = r5;
        goto L_0x0970;
    L_0x0a01:
        r4 = "LineNumberTable";
        r0 = r47;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0a42;
    L_0x0a0b:
        if (r40 != 0) goto L_0x1013;
    L_0x0a0d:
        r4 = r37 + 6;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r5 = r37 + 8;
    L_0x0a17:
        if (r4 <= 0) goto L_0x1013;
    L_0x0a19:
        r0 = r49;
        r6 = r0.readUnsignedShort(r5);
        r14 = r20[r6];
        if (r14 != 0) goto L_0x0a31;
    L_0x0a23:
        r0 = r49;
        r1 = r20;
        r14 = r0.readLabel(r6, r1);
        r15 = r14.a;
        r15 = r15 | 1;
        r14.a = r15;
    L_0x0a31:
        r6 = r20[r6];
        r14 = r5 + 2;
        r0 = r49;
        r14 = r0.readUnsignedShort(r14);
        r6.b = r14;
        r5 = r5 + 4;
        r4 = r4 + -1;
        goto L_0x0a17;
    L_0x0a42:
        r4 = "StackMapTable";
        r0 = r47;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0a6d;
    L_0x0a4c:
        r4 = r52 & 4;
        if (r4 != 0) goto L_0x1013;
    L_0x0a50:
        r6 = r37 + 8;
        r4 = r37 + 2;
        r0 = r49;
        r5 = r0.readInt(r4);
        r4 = r37 + 6;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r7 = r6;
        r8 = r31;
        r10 = r32;
        r6 = r5;
        r5 = r4;
        r4 = r30;
        goto L_0x09e4;
    L_0x0a6d:
        r4 = "StackMap";
        r0 = r47;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x0a94;
    L_0x0a77:
        r4 = r52 & 4;
        if (r4 != 0) goto L_0x1013;
    L_0x0a7b:
        r7 = r37 + 8;
        r4 = r37 + 2;
        r0 = r49;
        r6 = r0.readInt(r4);
        r4 = r37 + 6;
        r0 = r49;
        r5 = r0.readUnsignedShort(r4);
        r4 = 0;
        r8 = r31;
        r10 = r32;
        goto L_0x09e4;
    L_0x0a94:
        r4 = 0;
        r6 = r4;
        r5 = r29;
    L_0x0a98:
        r0 = r51;
        r4 = r0.length;
        if (r6 >= r4) goto L_0x102b;
    L_0x0a9d:
        r4 = r51[r6];
        r4 = r4.type;
        r0 = r47;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x1028;
    L_0x0aa9:
        r14 = r51[r6];
        r16 = r37 + 6;
        r4 = r37 + 2;
        r0 = r49;
        r17 = r0.readInt(r4);
        r19 = r35 + -8;
        r15 = r49;
        r18 = r9;
        r4 = r14.read(r15, r16, r17, r18, r19, r20);
        if (r4 == 0) goto L_0x1028;
    L_0x0ac1:
        r4.a = r5;
    L_0x0ac3:
        r5 = r6 + 1;
        r6 = r5;
        r5 = r4;
        goto L_0x0a98;
    L_0x0ac8:
        if (r10 == 0) goto L_0x100b;
    L_0x0aca:
        r0 = r44;
        r14 = new java.lang.Object[r0];
        r0 = r43;
        r0 = new java.lang.Object[r0];
        r16 = r0;
        if (r27 == 0) goto L_0x1007;
    L_0x0ad6:
        r5 = 0;
        r4 = r21 & 8;
        if (r4 != 0) goto L_0x1004;
    L_0x0adb:
        r4 = "<init>";
        r4 = r4.equals(r12);
        if (r4 == 0) goto L_0x0b23;
    L_0x0ae3:
        r4 = 1;
        r6 = org.objectweb.asm.Opcodes.UNINITIALIZED_THIS;
        r14[r5] = r6;
    L_0x0ae8:
        r5 = 1;
        r6 = r5;
    L_0x0aea:
        r12 = r6 + 1;
        r5 = r13.charAt(r6);
        switch(r5) {
            case 66: goto L_0x0b33;
            case 67: goto L_0x0b33;
            case 68: goto L_0x0b4e;
            case 70: goto L_0x0b3c;
            case 73: goto L_0x0b33;
            case 74: goto L_0x0b45;
            case 76: goto L_0x0b86;
            case 83: goto L_0x0b33;
            case 90: goto L_0x0b33;
            case 91: goto L_0x0b57;
            default: goto L_0x0af3;
        };
    L_0x0af3:
        r5 = -1;
        r6 = r10;
    L_0x0af5:
        r12 = r10 + r8;
        r12 = r12 + -2;
        if (r6 >= r12) goto L_0x0ba0;
    L_0x0afb:
        r12 = r42[r6];
        r13 = 8;
        if (r12 != r13) goto L_0x0b20;
    L_0x0b01:
        r12 = r6 + 1;
        r0 = r49;
        r12 = r0.readUnsignedShort(r12);
        if (r12 < 0) goto L_0x0b20;
    L_0x0b0b:
        r0 = r45;
        if (r12 >= r0) goto L_0x0b20;
    L_0x0b0f:
        r13 = r35 + r12;
        r13 = r42[r13];
        r13 = r13 & 255;
        r15 = 187; // 0xbb float:2.62E-43 double:9.24E-322;
        if (r13 != r15) goto L_0x0b20;
    L_0x0b19:
        r0 = r49;
        r1 = r20;
        r0.readLabel(r12, r1);
    L_0x0b20:
        r6 = r6 + 1;
        goto L_0x0af5;
    L_0x0b23:
        r4 = 1;
        r0 = r49;
        r6 = r0.header;
        r6 = r6 + 2;
        r0 = r49;
        r6 = r0.readClass(r6, r9);
        r14[r5] = r6;
        goto L_0x0ae8;
    L_0x0b33:
        r5 = r4 + 1;
        r6 = org.objectweb.asm.Opcodes.INTEGER;
        r14[r4] = r6;
        r4 = r5;
        r6 = r12;
        goto L_0x0aea;
    L_0x0b3c:
        r5 = r4 + 1;
        r6 = org.objectweb.asm.Opcodes.FLOAT;
        r14[r4] = r6;
        r4 = r5;
        r6 = r12;
        goto L_0x0aea;
    L_0x0b45:
        r5 = r4 + 1;
        r6 = org.objectweb.asm.Opcodes.LONG;
        r14[r4] = r6;
        r4 = r5;
        r6 = r12;
        goto L_0x0aea;
    L_0x0b4e:
        r5 = r4 + 1;
        r6 = org.objectweb.asm.Opcodes.DOUBLE;
        r14[r4] = r6;
        r4 = r5;
        r6 = r12;
        goto L_0x0aea;
    L_0x0b57:
        r5 = r13.charAt(r12);
        r15 = 91;
        if (r5 != r15) goto L_0x0b62;
    L_0x0b5f:
        r12 = r12 + 1;
        goto L_0x0b57;
    L_0x0b62:
        r5 = r13.charAt(r12);
        r15 = 76;
        if (r5 != r15) goto L_0x0b78;
    L_0x0b6a:
        r5 = r12 + 1;
    L_0x0b6c:
        r12 = r13.charAt(r5);
        r15 = 59;
        if (r12 == r15) goto L_0x0b77;
    L_0x0b74:
        r5 = r5 + 1;
        goto L_0x0b6c;
    L_0x0b77:
        r12 = r5;
    L_0x0b78:
        r5 = r4 + 1;
        r12 = r12 + 1;
        r6 = r13.substring(r6, r12);
        r14[r4] = r6;
        r4 = r5;
        r6 = r12;
        goto L_0x0aea;
    L_0x0b86:
        r5 = r13.charAt(r12);
        r15 = 59;
        if (r5 == r15) goto L_0x0b91;
    L_0x0b8e:
        r12 = r12 + 1;
        goto L_0x0b86;
    L_0x0b91:
        r5 = r4 + 1;
        r15 = r6 + 1;
        r6 = r12 + 1;
        r12 = r13.substring(r15, r12);
        r14[r4] = r12;
        r4 = r5;
        goto L_0x0aea;
    L_0x0ba0:
        r22 = r4;
        r4 = r14;
    L_0x0ba3:
        r14 = r4;
        r15 = r26;
        r23 = r33;
        r13 = r22;
        r8 = r10;
        r22 = r34;
        r4 = r7;
        r7 = r5;
        r34 = r35;
    L_0x0bb1:
        r0 = r34;
        r1 = r46;
        if (r0 >= r1) goto L_0x0f35;
    L_0x0bb7:
        r36 = r34 - r35;
        r5 = r20[r36];
        if (r5 == 0) goto L_0x0bcb;
    L_0x0bbd:
        r11.visitLabel(r5);
        if (r40 != 0) goto L_0x0bcb;
    L_0x0bc2:
        r6 = r5.b;
        if (r6 <= 0) goto L_0x0bcb;
    L_0x0bc6:
        r6 = r5.b;
        r11.visitLineNumber(r6, r5);
    L_0x0bcb:
        r33 = r4;
    L_0x0bcd:
        if (r14 == 0) goto L_0x0cde;
    L_0x0bcf:
        r0 = r36;
        if (r7 == r0) goto L_0x0bd6;
    L_0x0bd3:
        r4 = -1;
        if (r7 != r4) goto L_0x0cde;
    L_0x0bd6:
        if (r30 == 0) goto L_0x0bda;
    L_0x0bd8:
        if (r27 == 0) goto L_0x0c0c;
    L_0x0bda:
        r12 = -1;
        r11.visitFrame(r12, r13, r14, r15, r16);
    L_0x0bde:
        if (r33 <= 0) goto L_0x0cdb;
    L_0x0be0:
        if (r30 == 0) goto L_0x0c1b;
    L_0x0be2:
        r18 = r8 + 1;
        r4 = r42[r8];
        r12 = r4 & 255;
        r22 = r7;
    L_0x0bea:
        r6 = 0;
        r4 = 64;
        if (r12 >= r4) goto L_0x0c23;
    L_0x0bef:
        r5 = 3;
        r4 = 0;
        r7 = r5;
        r5 = r4;
        r4 = r12;
    L_0x0bf4:
        r4 = r4 + 1;
        r4 = r4 + r22;
        r0 = r49;
        r1 = r20;
        r0.readLabel(r4, r1);
        r8 = r33 + -1;
        r15 = r5;
        r23 = r6;
        r22 = r7;
        r33 = r8;
        r7 = r4;
        r8 = r18;
        goto L_0x0bcd;
    L_0x0c0c:
        r4 = -1;
        if (r7 == r4) goto L_0x0bde;
    L_0x0c0f:
        r21 = r11;
        r24 = r14;
        r25 = r15;
        r26 = r16;
        r21.visitFrame(r22, r23, r24, r25, r26);
        goto L_0x0bde;
    L_0x0c1b:
        r12 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r4 = -1;
        r22 = r4;
        r18 = r8;
        goto L_0x0bea;
    L_0x0c23:
        r4 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        if (r12 >= r4) goto L_0x0c36;
    L_0x0c27:
        r4 = r12 + -64;
        r17 = 0;
        r15 = r49;
        r19 = r9;
        r18 = r15.a(r16, r17, r18, r19, r20);
        r7 = 4;
        r5 = 1;
        goto L_0x0bf4;
    L_0x0c36:
        r0 = r49;
        r1 = r18;
        r21 = r0.readUnsignedShort(r1);
        r18 = r18 + 2;
        r4 = 247; // 0xf7 float:3.46E-43 double:1.22E-321;
        if (r12 != r4) goto L_0x0c55;
    L_0x0c44:
        r17 = 0;
        r15 = r49;
        r19 = r9;
        r18 = r15.a(r16, r17, r18, r19, r20);
        r5 = 4;
        r4 = 1;
        r7 = r5;
        r5 = r4;
        r4 = r21;
        goto L_0x0bf4;
    L_0x0c55:
        r4 = 248; // 0xf8 float:3.48E-43 double:1.225E-321;
        if (r12 < r4) goto L_0x0c68;
    L_0x0c59:
        r4 = 251; // 0xfb float:3.52E-43 double:1.24E-321;
        if (r12 >= r4) goto L_0x0c68;
    L_0x0c5d:
        r6 = 2;
        r5 = 251 - r12;
        r13 = r13 - r5;
        r4 = 0;
        r7 = r6;
        r6 = r5;
        r5 = r4;
        r4 = r21;
        goto L_0x0bf4;
    L_0x0c68:
        r4 = 251; // 0xfb float:3.52E-43 double:1.24E-321;
        if (r12 != r4) goto L_0x0c73;
    L_0x0c6c:
        r5 = 3;
        r4 = 0;
        r7 = r5;
        r5 = r4;
        r4 = r21;
        goto L_0x0bf4;
    L_0x0c73:
        r4 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r12 >= r4) goto L_0x0ca1;
    L_0x0c77:
        if (r27 == 0) goto L_0x0c91;
    L_0x0c79:
        r4 = r13;
    L_0x0c7a:
        r5 = r12 + -251;
        r8 = r18;
        r7 = r4;
        r4 = r5;
    L_0x0c80:
        if (r4 <= 0) goto L_0x0c93;
    L_0x0c82:
        r15 = r7 + 1;
        r5 = r49;
        r6 = r14;
        r10 = r20;
        r8 = r5.a(r6, r7, r8, r9, r10);
        r4 = r4 + -1;
        r7 = r15;
        goto L_0x0c80;
    L_0x0c91:
        r4 = 0;
        goto L_0x0c7a;
    L_0x0c93:
        r6 = 1;
        r5 = r12 + -251;
        r13 = r13 + r5;
        r4 = 0;
        r7 = r6;
        r18 = r8;
        r6 = r5;
        r5 = r4;
        r4 = r21;
        goto L_0x0bf4;
    L_0x0ca1:
        r13 = 0;
        r0 = r49;
        r1 = r18;
        r12 = r0.readUnsignedShort(r1);
        r8 = r18 + 2;
        r7 = 0;
        r4 = r12;
    L_0x0cae:
        if (r4 <= 0) goto L_0x0cbf;
    L_0x0cb0:
        r15 = r7 + 1;
        r5 = r49;
        r6 = r14;
        r10 = r20;
        r8 = r5.a(r6, r7, r8, r9, r10);
        r4 = r4 + -1;
        r7 = r15;
        goto L_0x0cae;
    L_0x0cbf:
        r0 = r49;
        r4 = r0.readUnsignedShort(r8);
        r18 = r8 + 2;
        r17 = 0;
        r5 = r4;
    L_0x0cca:
        if (r5 <= 0) goto L_0x0ffc;
    L_0x0ccc:
        r6 = r17 + 1;
        r15 = r49;
        r19 = r9;
        r18 = r15.a(r16, r17, r18, r19, r20);
        r5 = r5 + -1;
        r17 = r6;
        goto L_0x0cca;
    L_0x0cdb:
        r14 = 0;
        goto L_0x0bcd;
    L_0x0cde:
        r4 = r42[r34];
        r4 = r4 & 255;
        r5 = org.objectweb.asm.ClassWriter.a;
        r5 = r5[r4];
        switch(r5) {
            case 0: goto L_0x0d02;
            case 1: goto L_0x0e19;
            case 2: goto L_0x0e24;
            case 3: goto L_0x0e0c;
            case 4: goto L_0x0d08;
            case 5: goto L_0x0f15;
            case 6: goto L_0x0e5b;
            case 7: goto L_0x0e5b;
            case 8: goto L_0x0ea2;
            case 9: goto L_0x0d26;
            case 10: goto L_0x0d38;
            case 11: goto L_0x0e33;
            case 12: goto L_0x0e46;
            case 13: goto L_0x0f24;
            case 14: goto L_0x0d7a;
            case 15: goto L_0x0dc5;
            case 16: goto L_0x0ce9;
            case 17: goto L_0x0d4c;
            default: goto L_0x0ce9;
        };
    L_0x0ce9:
        r4 = r34 + 1;
        r0 = r49;
        r4 = r0.readClass(r4, r9);
        r5 = r34 + 3;
        r5 = r42[r5];
        r5 = r5 & 255;
        r11.visitMultiANewArrayInsn(r4, r5);
        r5 = r34 + 4;
    L_0x0cfc:
        r4 = r33;
        r34 = r5;
        goto L_0x0bb1;
    L_0x0d02:
        r11.visitInsn(r4);
        r5 = r34 + 1;
        goto L_0x0cfc;
    L_0x0d08:
        r5 = 54;
        if (r4 <= r5) goto L_0x0d1a;
    L_0x0d0c:
        r4 = r4 + -59;
        r5 = r4 >> 2;
        r5 = r5 + 54;
        r4 = r4 & 3;
        r11.visitVarInsn(r5, r4);
    L_0x0d17:
        r5 = r34 + 1;
        goto L_0x0cfc;
    L_0x0d1a:
        r4 = r4 + -26;
        r5 = r4 >> 2;
        r5 = r5 + 21;
        r4 = r4 & 3;
        r11.visitVarInsn(r5, r4);
        goto L_0x0d17;
    L_0x0d26:
        r5 = r34 + 1;
        r0 = r49;
        r5 = r0.readShort(r5);
        r5 = r5 + r36;
        r5 = r20[r5];
        r11.visitJumpInsn(r4, r5);
        r5 = r34 + 3;
        goto L_0x0cfc;
    L_0x0d38:
        r4 = r4 + -33;
        r5 = r34 + 1;
        r0 = r49;
        r5 = r0.readInt(r5);
        r5 = r5 + r36;
        r5 = r20[r5];
        r11.visitJumpInsn(r4, r5);
        r5 = r34 + 5;
        goto L_0x0cfc;
    L_0x0d4c:
        r4 = r34 + 1;
        r4 = r42[r4];
        r4 = r4 & 255;
        r5 = 132; // 0x84 float:1.85E-43 double:6.5E-322;
        if (r4 != r5) goto L_0x0d6c;
    L_0x0d56:
        r4 = r34 + 2;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r5 = r34 + 4;
        r0 = r49;
        r5 = r0.readShort(r5);
        r11.visitIincInsn(r4, r5);
        r5 = r34 + 6;
        goto L_0x0cfc;
    L_0x0d6c:
        r5 = r34 + 2;
        r0 = r49;
        r5 = r0.readUnsignedShort(r5);
        r11.visitVarInsn(r4, r5);
        r5 = r34 + 4;
        goto L_0x0cfc;
    L_0x0d7a:
        r4 = r34 + 4;
        r5 = r36 & 3;
        r4 = r4 - r5;
        r0 = r49;
        r5 = r0.readInt(r4);
        r6 = r36 + r5;
        r5 = r4 + 4;
        r0 = r49;
        r10 = r0.readInt(r5);
        r5 = r4 + 8;
        r0 = r49;
        r12 = r0.readInt(r5);
        r5 = r4 + 12;
        r4 = r12 - r10;
        r4 = r4 + 1;
        r0 = new org.objectweb.asm.Label[r4];
        r17 = r0;
        r4 = 0;
    L_0x0da2:
        r0 = r17;
        r0 = r0.length;
        r18 = r0;
        r0 = r18;
        if (r4 >= r0) goto L_0x0dbc;
    L_0x0dab:
        r0 = r49;
        r18 = r0.readInt(r5);
        r18 = r18 + r36;
        r18 = r20[r18];
        r17[r4] = r18;
        r5 = r5 + 4;
        r4 = r4 + 1;
        goto L_0x0da2;
    L_0x0dbc:
        r4 = r20[r6];
        r0 = r17;
        r11.visitTableSwitchInsn(r10, r12, r4, r0);
        goto L_0x0cfc;
    L_0x0dc5:
        r4 = r34 + 4;
        r5 = r36 & 3;
        r4 = r4 - r5;
        r0 = r49;
        r5 = r0.readInt(r4);
        r6 = r36 + r5;
        r5 = r4 + 4;
        r0 = r49;
        r10 = r0.readInt(r5);
        r5 = r4 + 8;
        r12 = new int[r10];
        r10 = new org.objectweb.asm.Label[r10];
        r4 = 0;
    L_0x0de1:
        r0 = r12.length;
        r17 = r0;
        r0 = r17;
        if (r4 >= r0) goto L_0x0e05;
    L_0x0de8:
        r0 = r49;
        r17 = r0.readInt(r5);
        r12[r4] = r17;
        r17 = r5 + 4;
        r0 = r49;
        r1 = r17;
        r17 = r0.readInt(r1);
        r17 = r17 + r36;
        r17 = r20[r17];
        r10[r4] = r17;
        r5 = r5 + 8;
        r4 = r4 + 1;
        goto L_0x0de1;
    L_0x0e05:
        r4 = r20[r6];
        r11.visitLookupSwitchInsn(r4, r12, r10);
        goto L_0x0cfc;
    L_0x0e0c:
        r5 = r34 + 1;
        r5 = r42[r5];
        r5 = r5 & 255;
        r11.visitVarInsn(r4, r5);
        r5 = r34 + 2;
        goto L_0x0cfc;
    L_0x0e19:
        r5 = r34 + 1;
        r5 = r42[r5];
        r11.visitIntInsn(r4, r5);
        r5 = r34 + 2;
        goto L_0x0cfc;
    L_0x0e24:
        r5 = r34 + 1;
        r0 = r49;
        r5 = r0.readShort(r5);
        r11.visitIntInsn(r4, r5);
        r5 = r34 + 3;
        goto L_0x0cfc;
    L_0x0e33:
        r4 = r34 + 1;
        r4 = r42[r4];
        r4 = r4 & 255;
        r0 = r49;
        r4 = r0.readConst(r4, r9);
        r11.visitLdcInsn(r4);
        r5 = r34 + 2;
        goto L_0x0cfc;
    L_0x0e46:
        r4 = r34 + 1;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r0 = r49;
        r4 = r0.readConst(r4, r9);
        r11.visitLdcInsn(r4);
        r5 = r34 + 3;
        goto L_0x0cfc;
    L_0x0e5b:
        r0 = r49;
        r5 = r0.a;
        r6 = r34 + 1;
        r0 = r49;
        r6 = r0.readUnsignedShort(r6);
        r5 = r5[r6];
        r0 = r49;
        r6 = r0.readClass(r5, r9);
        r0 = r49;
        r10 = r0.a;
        r5 = r5 + 2;
        r0 = r49;
        r5 = r0.readUnsignedShort(r5);
        r5 = r10[r5];
        r0 = r49;
        r10 = r0.readUTF8(r5, r9);
        r5 = r5 + 2;
        r0 = r49;
        r5 = r0.readUTF8(r5, r9);
        r12 = 182; // 0xb6 float:2.55E-43 double:9.0E-322;
        if (r4 >= r12) goto L_0x0e9a;
    L_0x0e8f:
        r11.visitFieldInsn(r4, r6, r10, r5);
    L_0x0e92:
        r5 = 185; // 0xb9 float:2.59E-43 double:9.14E-322;
        if (r4 != r5) goto L_0x0e9e;
    L_0x0e96:
        r5 = r34 + 5;
        goto L_0x0cfc;
    L_0x0e9a:
        r11.visitMethodInsn(r4, r6, r10, r5);
        goto L_0x0e92;
    L_0x0e9e:
        r5 = r34 + 3;
        goto L_0x0cfc;
    L_0x0ea2:
        r0 = r49;
        r4 = r0.a;
        r5 = r34 + 1;
        r0 = r49;
        r5 = r0.readUnsignedShort(r5);
        r4 = r4[r5];
        r0 = r49;
        r5 = r0.readUnsignedShort(r4);
        r5 = r28[r5];
        r0 = r49;
        r6 = r0.a;
        r4 = r4 + 2;
        r0 = r49;
        r4 = r0.readUnsignedShort(r4);
        r4 = r6[r4];
        r0 = r49;
        r10 = r0.readUTF8(r4, r9);
        r4 = r4 + 2;
        r0 = r49;
        r12 = r0.readUTF8(r4, r9);
        r0 = r49;
        r4 = r0.readUnsignedShort(r5);
        r0 = r49;
        r4 = r0.readConst(r4, r9);
        r4 = (org.objectweb.asm.Handle) r4;
        r6 = r5 + 2;
        r0 = r49;
        r17 = r0.readUnsignedShort(r6);
        r0 = r17;
        r0 = new java.lang.Object[r0];
        r18 = r0;
        r6 = r5 + 4;
        r5 = 0;
    L_0x0ef3:
        r0 = r17;
        if (r5 >= r0) goto L_0x0f0c;
    L_0x0ef7:
        r0 = r49;
        r19 = r0.readUnsignedShort(r6);
        r0 = r49;
        r1 = r19;
        r19 = r0.readConst(r1, r9);
        r18[r5] = r19;
        r6 = r6 + 2;
        r5 = r5 + 1;
        goto L_0x0ef3;
    L_0x0f0c:
        r0 = r18;
        r11.visitInvokeDynamicInsn(r10, r12, r4, r0);
        r5 = r34 + 5;
        goto L_0x0cfc;
    L_0x0f15:
        r5 = r34 + 1;
        r0 = r49;
        r5 = r0.readClass(r5, r9);
        r11.visitTypeInsn(r4, r5);
        r5 = r34 + 3;
        goto L_0x0cfc;
    L_0x0f24:
        r4 = r34 + 1;
        r4 = r42[r4];
        r4 = r4 & 255;
        r5 = r34 + 2;
        r5 = r42[r5];
        r11.visitIincInsn(r4, r5);
        r5 = r34 + 3;
        goto L_0x0cfc;
    L_0x0f35:
        r4 = r46 - r35;
        r4 = r20[r4];
        if (r4 == 0) goto L_0x0f3e;
    L_0x0f3b:
        r11.visitLabel(r4);
    L_0x0f3e:
        if (r40 != 0) goto L_0x0fd7;
    L_0x0f40:
        if (r32 == 0) goto L_0x0fd7;
    L_0x0f42:
        r4 = 0;
        if (r31 == 0) goto L_0x0f74;
    L_0x0f45:
        r0 = r49;
        r1 = r31;
        r4 = r0.readUnsignedShort(r1);
        r5 = r4 * 3;
        r6 = r31 + 2;
        r4 = new int[r5];
    L_0x0f53:
        if (r5 <= 0) goto L_0x0f74;
    L_0x0f55:
        r5 = r5 + -1;
        r7 = r6 + 6;
        r4[r5] = r7;
        r5 = r5 + -1;
        r7 = r6 + 8;
        r0 = r49;
        r7 = r0.readUnsignedShort(r7);
        r4[r5] = r7;
        r5 = r5 + -1;
        r0 = r49;
        r7 = r0.readUnsignedShort(r6);
        r4[r5] = r7;
        r6 = r6 + 10;
        goto L_0x0f53;
    L_0x0f74:
        r0 = r49;
        r1 = r32;
        r5 = r0.readUnsignedShort(r1);
        r6 = r32 + 2;
        r7 = r6;
        r6 = r5;
    L_0x0f80:
        if (r6 <= 0) goto L_0x0fd7;
    L_0x0f82:
        r0 = r49;
        r8 = r0.readUnsignedShort(r7);
        r5 = r7 + 2;
        r0 = r49;
        r10 = r0.readUnsignedShort(r5);
        r5 = r7 + 8;
        r0 = r49;
        r17 = r0.readUnsignedShort(r5);
        r14 = 0;
        if (r4 == 0) goto L_0x0fb5;
    L_0x0f9b:
        r5 = 0;
    L_0x0f9c:
        r12 = r4.length;
        if (r5 >= r12) goto L_0x0fb5;
    L_0x0f9f:
        r12 = r4[r5];
        if (r12 != r8) goto L_0x0fd4;
    L_0x0fa3:
        r12 = r5 + 1;
        r12 = r4[r12];
        r0 = r17;
        if (r12 != r0) goto L_0x0fd4;
    L_0x0fab:
        r5 = r5 + 2;
        r5 = r4[r5];
        r0 = r49;
        r14 = r0.readUTF8(r5, r9);
    L_0x0fb5:
        r5 = r7 + 4;
        r0 = r49;
        r12 = r0.readUTF8(r5, r9);
        r5 = r7 + 6;
        r0 = r49;
        r13 = r0.readUTF8(r5, r9);
        r15 = r20[r8];
        r5 = r8 + r10;
        r16 = r20[r5];
        r11.visitLocalVariable(r12, r13, r14, r15, r16, r17);
        r7 = r7 + 10;
        r5 = r6 + -1;
        r6 = r5;
        goto L_0x0f80;
    L_0x0fd4:
        r5 = r5 + 3;
        goto L_0x0f9c;
    L_0x0fd7:
        if (r29 == 0) goto L_0x0fea;
    L_0x0fd9:
        r0 = r29;
        r4 = r0.a;
        r5 = 0;
        r0 = r29;
        r0.a = r5;
        r0 = r29;
        r11.visitAttribute(r0);
        r29 = r4;
        goto L_0x0fd7;
    L_0x0fea:
        r0 = r43;
        r1 = r44;
        r11.visitMaxs(r0, r1);
    L_0x0ff1:
        if (r11 == 0) goto L_0x0722;
    L_0x0ff3:
        r11.visitEnd();
        goto L_0x0722;
    L_0x0ff8:
        r50.visitEnd();
        return;
    L_0x0ffc:
        r5 = r4;
        r6 = r12;
        r7 = r13;
        r13 = r12;
        r4 = r21;
        goto L_0x0bf4;
    L_0x1004:
        r4 = r5;
        goto L_0x0ae8;
    L_0x1007:
        r4 = r22;
        goto L_0x0af3;
    L_0x100b:
        r16 = r23;
        r4 = r24;
        r5 = r25;
        goto L_0x0ba3;
    L_0x1013:
        r4 = r30;
        r5 = r7;
        r6 = r8;
        r7 = r10;
        r8 = r31;
        r10 = r32;
        goto L_0x09e4;
    L_0x101e:
        r5 = r7;
        r6 = r8;
        r7 = r10;
        r8 = r31;
        r10 = r4;
        r4 = r30;
        goto L_0x09e4;
    L_0x1028:
        r4 = r5;
        goto L_0x0ac3;
    L_0x102b:
        r4 = r30;
        r6 = r8;
        r29 = r5;
        r8 = r31;
        r5 = r7;
        r7 = r10;
        r10 = r32;
        goto L_0x09e4;
    L_0x1038:
        r5 = r6;
        goto L_0x0718;
    L_0x103b:
        r15 = r6;
        goto L_0x06f3;
    L_0x103e:
        r4 = r16;
        r5 = r17;
        r6 = r18;
        r10 = r15;
        r11 = r19;
        r15 = r21;
        r16 = r20;
        r17 = r22;
        r18 = r23;
        goto L_0x05b6;
    L_0x1051:
        r4 = r15;
        r5 = r16;
        r6 = r17;
        r7 = r18;
        r8 = r19;
        goto L_0x0429;
    L_0x105c:
        r4 = r28;
        r5 = r18;
        r6 = r19;
        r7 = r20;
        r8 = r21;
        r10 = r22;
        r11 = r23;
        r18 = r24;
        r19 = r25;
        r20 = r26;
        goto L_0x0120;
    L_0x1072:
        r4 = r18;
        r5 = r19;
        goto L_0x019b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.objectweb.asm.ClassReader.accept(org.objectweb.asm.ClassVisitor, org.objectweb.asm.Attribute[], int):void");
    }

    public int getAccess() {
        return readUnsignedShort(this.header);
    }

    public String getClassName() {
        return readClass(this.header + 2, new char[this.d]);
    }

    public String[] getInterfaces() {
        int i = this.header + 6;
        int readUnsignedShort = readUnsignedShort(i);
        String[] strArr = new String[readUnsignedShort];
        if (readUnsignedShort > 0) {
            char[] cArr = new char[this.d];
            for (int i2 = 0; i2 < readUnsignedShort; i2++) {
                i += 2;
                strArr[i2] = readClass(i, cArr);
            }
        }
        return strArr;
    }

    public int getItem(int i) {
        return this.a[i];
    }

    public int getItemCount() {
        return this.a.length;
    }

    public int getMaxStringLength() {
        return this.d;
    }

    public String getSuperName() {
        int i = this.a[readUnsignedShort(this.header + 4)];
        return i == 0 ? null : readUTF8(i, new char[this.d]);
    }

    public int readByte(int i) {
        return this.b[i] & ByteCode.IMPDEP2;
    }

    public String readClass(int i, char[] cArr) {
        return readUTF8(this.a[readUnsignedShort(i)], cArr);
    }

    public Object readConst(int i, char[] cArr) {
        int i2 = this.a[i];
        switch (this.b[i2 - 1]) {
            case (byte) 3:
                return new Integer(readInt(i2));
            case (byte) 4:
                return new Float(Float.intBitsToFloat(readInt(i2)));
            case (byte) 5:
                return new Long(readLong(i2));
            case (byte) 6:
                return new Double(Double.longBitsToDouble(readLong(i2)));
            case (byte) 7:
                return Type.getObjectType(readUTF8(i2, cArr));
            case (byte) 8:
                return readUTF8(i2, cArr);
            case (byte) 16:
                return Type.getMethodType(readUTF8(i2, cArr));
            default:
                int readByte = readByte(i2);
                int[] iArr = this.a;
                i2 = iArr[readUnsignedShort(i2 + 1)];
                String readClass = readClass(i2, cArr);
                int i3 = iArr[readUnsignedShort(i2 + 2)];
                return new Handle(readByte, readClass, readUTF8(i3, cArr), readUTF8(i3 + 2, cArr));
        }
    }

    public int readInt(int i) {
        byte[] bArr = this.b;
        return (bArr[i + 3] & ByteCode.IMPDEP2) | ((((bArr[i] & ByteCode.IMPDEP2) << 24) | ((bArr[i + 1] & ByteCode.IMPDEP2) << 16)) | ((bArr[i + 2] & ByteCode.IMPDEP2) << 8));
    }

    /* access modifiers changed from: protected */
    public Label readLabel(int i, Label[] labelArr) {
        if (labelArr[i] == null) {
            labelArr[i] = new Label();
        }
        return labelArr[i];
    }

    public long readLong(int i) {
        return (((long) readInt(i)) << 32) | (((long) readInt(i + 4)) & 4294967295L);
    }

    public short readShort(int i) {
        byte[] bArr = this.b;
        return (short) ((bArr[i + 1] & ByteCode.IMPDEP2) | ((bArr[i] & ByteCode.IMPDEP2) << 8));
    }

    public String readUTF8(int i, char[] cArr) {
        int readUnsignedShort = readUnsignedShort(i);
        String str = this.c[readUnsignedShort];
        if (str != null) {
            return str;
        }
        int i2 = this.a[readUnsignedShort];
        String[] strArr = this.c;
        str = a(i2 + 2, readUnsignedShort(i2), cArr);
        strArr[readUnsignedShort] = str;
        return str;
    }

    public int readUnsignedShort(int i) {
        byte[] bArr = this.b;
        return (bArr[i + 1] & ByteCode.IMPDEP2) | ((bArr[i] & ByteCode.IMPDEP2) << 8);
    }
}
