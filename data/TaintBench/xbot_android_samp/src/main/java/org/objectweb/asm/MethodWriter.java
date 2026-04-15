package org.objectweb.asm;

import org.java_websocket.framing.CloseFrame;
import org.mozilla.classfile.ByteCode;

class MethodWriter extends MethodVisitor {
    private int A;
    private Handler B;
    private Handler C;
    private int D;
    private ByteVector E;
    private int F;
    private ByteVector G;
    private int H;
    private ByteVector I;
    private Attribute J;
    private boolean K;
    private int L;
    private final int M;
    private Label N;
    private Label O;
    private Label P;
    private int Q;
    private int R;
    private int S;
    private int T;
    final ClassWriter b;
    private int c;
    private final int d;
    private final int e;
    private final String f;
    String g;
    int h;
    int i;
    int j;
    int[] k;
    private ByteVector l;
    private AnnotationWriter m;
    private AnnotationWriter n;
    private AnnotationWriter[] o;
    private AnnotationWriter[] p;
    private Attribute q;
    private ByteVector r = new ByteVector();
    private int s;
    private int t;
    private int u;
    private ByteVector v;
    private int w;
    private int[] x;
    private int y;
    private int[] z;

    MethodWriter(ClassWriter classWriter, int i, String str, String str2, String str3, String[] strArr, boolean z, boolean z2) {
        int i2;
        int i3 = 0;
        super(Opcodes.ASM4);
        if (classWriter.D == null) {
            classWriter.D = this;
        } else {
            classWriter.E.mv = this;
        }
        classWriter.E = this;
        this.b = classWriter;
        this.c = i;
        this.d = classWriter.newUTF8(str);
        this.e = classWriter.newUTF8(str2);
        this.f = str2;
        this.g = str3;
        if (strArr != null && strArr.length > 0) {
            this.j = strArr.length;
            this.k = new int[this.j];
            for (i2 = 0; i2 < this.j; i2++) {
                this.k[i2] = classWriter.newClass(strArr[i2]);
            }
        }
        if (!z2) {
            i3 = z ? 1 : 2;
        }
        this.M = i3;
        if (z || z2) {
            if (z2 && "<init>".equals(str)) {
                this.c |= Opcodes.ASM4;
            }
            i2 = Type.getArgumentsAndReturnSizes(this.f) >> 2;
            if ((i & 8) != 0) {
                i2--;
            }
            this.t = i2;
            this.T = i2;
            this.N = new Label();
            Label label = this.N;
            label.a |= 8;
            visitLabel(this.N);
        }
    }

    static int a(byte[] bArr, int i) {
        return ((((bArr[i] & ByteCode.IMPDEP2) << 24) | ((bArr[i + 1] & ByteCode.IMPDEP2) << 16)) | ((bArr[i + 2] & ByteCode.IMPDEP2) << 8)) | (bArr[i + 3] & ByteCode.IMPDEP2);
    }

    static int a(int[] iArr, int[] iArr2, int i, int i2) {
        int i3 = i2 - i;
        int i4 = 0;
        while (i4 < iArr.length) {
            if (i < iArr[i4] && iArr[i4] <= i2) {
                i3 += iArr2[i4];
            } else if (i2 < iArr[i4] && iArr[i4] <= i) {
                i3 -= iArr2[i4];
            }
            i4++;
        }
        return i3;
    }

    private void a(int i, int i2) {
        while (i < i2) {
            int i3 = this.z[i];
            int i4 = -268435456 & i3;
            if (i4 == 0) {
                i4 = i3 & 1048575;
                switch (i3 & 267386880) {
                    case 24117248:
                        this.v.putByte(7).putShort(this.b.newClass(this.b.H[i4].g));
                        break;
                    case 25165824:
                        this.v.putByte(8).putShort(this.b.H[i4].c);
                        break;
                    default:
                        this.v.putByte(i4);
                        break;
                }
            }
            StringBuffer stringBuffer = new StringBuffer();
            i4 >>= 28;
            while (true) {
                int i5 = i4 - 1;
                if (i4 > 0) {
                    stringBuffer.append('[');
                    i4 = i5;
                } else {
                    if ((i3 & 267386880) != 24117248) {
                        switch (i3 & 15) {
                            case 1:
                                stringBuffer.append('I');
                                break;
                            case 2:
                                stringBuffer.append('F');
                                break;
                            case 3:
                                stringBuffer.append('D');
                                break;
                            case 9:
                                stringBuffer.append('Z');
                                break;
                            case 10:
                                stringBuffer.append('B');
                                break;
                            case 11:
                                stringBuffer.append('C');
                                break;
                            case 12:
                                stringBuffer.append('S');
                                break;
                            default:
                                stringBuffer.append('J');
                                break;
                        }
                    }
                    stringBuffer.append('L');
                    stringBuffer.append(this.b.H[i3 & 1048575].g);
                    stringBuffer.append(';');
                    this.v.putByte(7).putShort(this.b.newClass(stringBuffer.toString()));
                }
            }
            i++;
        }
    }

    private void a(int i, int i2, int i3) {
        int i4 = (i2 + 3) + i3;
        if (this.z == null || this.z.length < i4) {
            this.z = new int[i4];
        }
        this.z[0] = i;
        this.z[1] = i2;
        this.z[2] = i3;
        this.y = 3;
    }

    private void a(int i, Label label) {
        Edge edge = new Edge();
        edge.a = i;
        edge.b = label;
        edge.c = this.P.j;
        this.P.j = edge;
    }

    private void a(Object obj) {
        if (obj instanceof String) {
            this.v.putByte(7).putShort(this.b.newClass((String) obj));
        } else if (obj instanceof Integer) {
            this.v.putByte(((Integer) obj).intValue());
        } else {
            this.v.putByte(8).putShort(((Label) obj).c);
        }
    }

    private void a(Label label, Label[] labelArr) {
        int i = 0;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(171, 0, null, null);
                a(0, label);
                Label a = label.a();
                a.a |= 16;
                for (int i2 = 0; i2 < labelArr.length; i2++) {
                    a(0, labelArr[i2]);
                    Label a2 = labelArr[i2].a();
                    a2.a |= 16;
                }
            } else {
                this.Q--;
                a(this.Q, label);
                while (i < labelArr.length) {
                    a(this.Q, labelArr[i]);
                    i++;
                }
            }
            e();
        }
    }

    static void a(byte[] bArr, int i, int i2) {
        bArr[i] = (byte) (i2 >>> 8);
        bArr[i + 1] = (byte) i2;
    }

    static void a(int[] iArr, int[] iArr2, Label label) {
        if ((label.a & 4) == 0) {
            label.c = a(iArr, iArr2, 0, label.c);
            label.a |= 4;
        }
    }

    static short b(byte[] bArr, int i) {
        return (short) (((bArr[i] & ByteCode.IMPDEP2) << 8) | (bArr[i + 1] & ByteCode.IMPDEP2));
    }

    private void b() {
        if (this.x != null) {
            if (this.v == null) {
                this.v = new ByteVector();
            }
            c();
            this.u++;
        }
        this.x = this.z;
        this.z = null;
    }

    private void b(Frame frame) {
        int i;
        int i2 = 0;
        int[] iArr = frame.c;
        int[] iArr2 = frame.d;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < iArr.length) {
            i = iArr[i3];
            if (i == 16777216) {
                i5++;
            } else {
                i4 += i5 + 1;
                i5 = 0;
            }
            if (i == 16777220 || i == 16777219) {
                i3++;
            }
            i3++;
        }
        i3 = 0;
        i5 = 0;
        while (i3 < iArr2.length) {
            i = iArr2[i3];
            i5++;
            if (i == 16777220 || i == 16777219) {
                i3++;
            }
            i3++;
        }
        a(frame.b.c, i4, i5);
        i3 = 0;
        while (i4 > 0) {
            i5 = iArr[i3];
            int[] iArr3 = this.z;
            int i6 = this.y;
            this.y = i6 + 1;
            iArr3[i6] = i5;
            if (i5 == 16777220 || i5 == 16777219) {
                i3++;
            }
            i3++;
            i4--;
        }
        while (i2 < iArr2.length) {
            i3 = iArr2[i2];
            int[] iArr4 = this.z;
            i5 = this.y;
            this.y = i5 + 1;
            iArr4[i5] = i3;
            if (i3 == 16777220 || i3 == 16777219) {
                i2++;
            }
            i2++;
        }
        b();
    }

    static int c(byte[] bArr, int i) {
        return ((bArr[i] & ByteCode.IMPDEP2) << 8) | (bArr[i + 1] & ByteCode.IMPDEP2);
    }

    private void c() {
        int i = 64;
        int i2 = 0;
        int i3 = this.z[1];
        int i4 = this.z[2];
        if ((this.b.b & 65535) < 50) {
            this.v.putShort(this.z[0]).putShort(i3);
            a(3, i3 + 3);
            this.v.putShort(i4);
            a(i3 + 3, (i3 + 3) + i4);
            return;
        }
        int i5;
        int i6;
        int i7 = this.x[1];
        int i8 = this.u == 0 ? this.z[0] : (this.z[0] - this.x[0]) - 1;
        if (i4 == 0) {
            i5 = i3 - i7;
            switch (i5) {
                case CloseFrame.FLASHPOLICY /*-3*/:
                case CloseFrame.BUGGYCLOSE /*-2*/:
                case -1:
                    i = 248;
                    i7 = i3;
                    break;
                case 0:
                    if (i8 >= 64) {
                        i = 251;
                        break;
                    } else {
                        i = 0;
                        break;
                    }
                case 1:
                case 2:
                case 3:
                    i = 252;
                    break;
                default:
                    i = ByteCode.IMPDEP2;
                    break;
            }
            i6 = i7;
        } else if (i3 == i7 && i4 == 1) {
            if (i8 >= 63) {
                i = 247;
            }
            i5 = 0;
            i6 = i7;
        } else {
            i5 = 0;
            i = ByteCode.IMPDEP2;
            i6 = i7;
        }
        if (i != 255) {
            i7 = 3;
            while (i2 < i6) {
                if (this.z[i7] != this.x[i7]) {
                    i = ByteCode.IMPDEP2;
                } else {
                    i7++;
                    i2++;
                }
            }
        }
        switch (i) {
            case 0:
                this.v.putByte(i8);
                return;
            case 64:
                this.v.putByte(i8 + 64);
                a(i3 + 3, i3 + 4);
                return;
            case 247:
                this.v.putByte(247).putShort(i8);
                a(i3 + 3, i3 + 4);
                return;
            case 248:
                this.v.putByte(i5 + 251).putShort(i8);
                return;
            case 251:
                this.v.putByte(251).putShort(i8);
                return;
            case 252:
                this.v.putByte(i5 + 251).putShort(i8);
                a(i6 + 3, i3 + 3);
                return;
            default:
                this.v.putByte(ByteCode.IMPDEP2).putShort(i8).putShort(i3);
                a(3, i3 + 3);
                this.v.putShort(i4);
                a(i3 + 3, (i3 + 3) + i4);
                return;
        }
    }

    private void d() {
        byte[] bArr = this.r.a;
        int[] iArr = new int[0];
        int[] iArr2 = new int[0];
        boolean[] zArr = new boolean[this.r.b];
        int i = 3;
        while (true) {
            int i2;
            int c;
            if (i == 3) {
                i = 2;
            }
            int i3 = i;
            i = 0;
            while (i < bArr.length) {
                i2 = bArr[i] & ByteCode.IMPDEP2;
                int i4 = 0;
                switch (ClassWriter.a[i2]) {
                    case (byte) 0:
                    case (byte) 4:
                        i++;
                        break;
                    case (byte) 1:
                    case (byte) 3:
                    case (byte) 11:
                        i += 2;
                        break;
                    case (byte) 2:
                    case (byte) 5:
                    case (byte) 6:
                    case (byte) 12:
                    case (byte) 13:
                        i += 3;
                        break;
                    case (byte) 7:
                    case (byte) 8:
                        i += 5;
                        break;
                    case (byte) 9:
                        if (i2 > ByteCode.JSR_W) {
                            i2 = i2 < 218 ? i2 - 49 : i2 - 20;
                            c = c(bArr, i + 1) + i;
                        } else {
                            c = b(bArr, i + 1) + i;
                        }
                        c = a(iArr, iArr2, i, c);
                        if ((c < -32768 || c > 32767) && !zArr[i]) {
                            c = (i2 == 167 || i2 == 168) ? 2 : 5;
                            zArr[i] = true;
                        } else {
                            c = 0;
                        }
                        i += 3;
                        i4 = c;
                        break;
                    case (byte) 10:
                        i += 5;
                        break;
                    case (byte) 14:
                        if (i3 == 1) {
                            i4 = -(a(iArr, iArr2, 0, i) & 3);
                        } else if (!zArr[i]) {
                            i4 = i & 3;
                            zArr[i] = true;
                        }
                        i = (i + 4) - (i & 3);
                        i += (((a(bArr, i + 8) - a(bArr, i + 4)) + 1) * 4) + 12;
                        break;
                    case (byte) 15:
                        if (i3 == 1) {
                            i4 = -(a(iArr, iArr2, 0, i) & 3);
                        } else if (!zArr[i]) {
                            i4 = i & 3;
                            zArr[i] = true;
                        }
                        i = (i + 4) - (i & 3);
                        i += (a(bArr, i + 4) * 8) + 8;
                        break;
                    case (byte) 17:
                        if ((bArr[i + 1] & ByteCode.IMPDEP2) != 132) {
                            i += 4;
                            break;
                        } else {
                            i += 6;
                            break;
                        }
                    default:
                        i += 4;
                        break;
                }
                if (i4 != 0) {
                    int[] iArr3 = new int[(iArr.length + 1)];
                    int[] iArr4 = new int[(iArr2.length + 1)];
                    System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                    System.arraycopy(iArr2, 0, iArr4, 0, iArr2.length);
                    iArr3[iArr.length] = i;
                    iArr4[iArr2.length] = i4;
                    if (i4 > 0) {
                        i3 = 3;
                        iArr2 = iArr4;
                        iArr = iArr3;
                    } else {
                        iArr2 = iArr4;
                        iArr = iArr3;
                    }
                }
            }
            if (i3 < 3) {
                i3--;
            }
            if (i3 == 0) {
                ByteVector byteVector = new ByteVector(this.r.b);
                i = 0;
                while (i < this.r.b) {
                    c = bArr[i] & ByteCode.IMPDEP2;
                    int i5;
                    switch (ClassWriter.a[c]) {
                        case (byte) 0:
                        case (byte) 4:
                            byteVector.putByte(c);
                            i++;
                            continue;
                        case (byte) 1:
                        case (byte) 3:
                        case (byte) 11:
                            byteVector.putByteArray(bArr, i, 2);
                            i += 2;
                            continue;
                        case (byte) 2:
                        case (byte) 5:
                        case (byte) 6:
                        case (byte) 12:
                        case (byte) 13:
                            byteVector.putByteArray(bArr, i, 3);
                            i += 3;
                            continue;
                        case (byte) 7:
                        case (byte) 8:
                            byteVector.putByteArray(bArr, i, 5);
                            i += 5;
                            continue;
                        case (byte) 9:
                            if (c > ByteCode.JSR_W) {
                                c = c < 218 ? c - 49 : c - 20;
                                i3 = c(bArr, i + 1) + i;
                            } else {
                                i3 = b(bArr, i + 1) + i;
                            }
                            i2 = a(iArr, iArr2, i, i3);
                            if (zArr[i]) {
                                if (c == 167) {
                                    byteVector.putByte(ByteCode.GOTO_W);
                                    i3 = i2;
                                } else if (c == 168) {
                                    byteVector.putByte(ByteCode.JSR_W);
                                    i3 = i2;
                                } else {
                                    byteVector.putByte(c <= 166 ? ((c + 1) ^ 1) - 1 : c ^ 1);
                                    byteVector.putShort(8);
                                    byteVector.putByte(ByteCode.GOTO_W);
                                    i3 = i2 - 3;
                                }
                                byteVector.putInt(i3);
                            } else {
                                byteVector.putByte(c);
                                byteVector.putShort(i2);
                            }
                            i += 3;
                            continue;
                        case (byte) 10:
                            i3 = a(iArr, iArr2, i, a(bArr, i + 1) + i);
                            byteVector.putByte(c);
                            byteVector.putInt(i3);
                            i += 5;
                            continue;
                        case (byte) 14:
                            i3 = (i + 4) - (i & 3);
                            byteVector.putByte(170);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.b % 4)) % 4);
                            i3 += 4;
                            byteVector.putInt(a(iArr, iArr2, i, a(bArr, i3) + i));
                            c = a(bArr, i3);
                            i2 = i3 + 4;
                            byteVector.putInt(c);
                            i3 = (a(bArr, i2) - c) + 1;
                            c = i2 + 4;
                            byteVector.putInt(a(bArr, c - 4));
                            i5 = i3;
                            i3 = c;
                            c = i5;
                            while (c > 0) {
                                i2 = i3 + 4;
                                byteVector.putInt(a(iArr, iArr2, i, i + a(bArr, i3)));
                                c--;
                                i3 = i2;
                            }
                            break;
                        case (byte) 15:
                            i3 = (i + 4) - (i & 3);
                            byteVector.putByte(171);
                            byteVector.putByteArray(null, 0, (4 - (byteVector.b % 4)) % 4);
                            i2 = i3 + 4;
                            byteVector.putInt(a(iArr, iArr2, i, a(bArr, i3) + i));
                            i3 = a(bArr, i2);
                            c = i2 + 4;
                            byteVector.putInt(i3);
                            i5 = i3;
                            i3 = c;
                            c = i5;
                            while (c > 0) {
                                byteVector.putInt(a(bArr, i3));
                                i3 += 4;
                                i2 = i3 + 4;
                                byteVector.putInt(a(iArr, iArr2, i, i + a(bArr, i3)));
                                c--;
                                i3 = i2;
                            }
                            break;
                        case (byte) 17:
                            if ((bArr[i + 1] & ByteCode.IMPDEP2) != 132) {
                                byteVector.putByteArray(bArr, i, 4);
                                i += 4;
                                break;
                            }
                            byteVector.putByteArray(bArr, i, 6);
                            i += 6;
                            continue;
                        default:
                            byteVector.putByteArray(bArr, i, 4);
                            i += 4;
                            continue;
                    }
                    i = i3;
                }
                if (this.u > 0) {
                    if (this.M == 0) {
                        this.u = 0;
                        this.v = null;
                        this.x = null;
                        this.z = null;
                        Frame frame = new Frame();
                        frame.b = this.N;
                        frame.a(this.b, this.c, Type.getArgumentTypes(this.f), this.t);
                        b(frame);
                        for (Label label = this.N; label != null; label = label.i) {
                            i3 = label.c - 3;
                            if ((label.a & 32) != 0 || (i3 >= 0 && zArr[i3])) {
                                a(iArr, iArr2, label);
                                b(label.h);
                            }
                        }
                    } else {
                        this.b.L = true;
                    }
                }
                for (Handler handler = this.B; handler != null; handler = handler.f) {
                    a(iArr, iArr2, handler.a);
                    a(iArr, iArr2, handler.b);
                    a(iArr, iArr2, handler.c);
                }
                i = 0;
                while (true) {
                    c = i;
                    if (c < 2) {
                        ByteVector byteVector2 = c == 0 ? this.E : this.G;
                        if (byteVector2 != null) {
                            byte[] bArr2 = byteVector2.a;
                            for (i = 0; i < byteVector2.b; i += 10) {
                                int c2 = c(bArr2, i);
                                int a = a(iArr, iArr2, 0, c2);
                                a(bArr2, i, a);
                                a(bArr2, i + 2, a(iArr, iArr2, 0, c2 + c(bArr2, i + 2)) - a);
                            }
                        }
                        i = c + 1;
                    } else {
                        if (this.I != null) {
                            byte[] bArr3 = this.I.a;
                            for (i = 0; i < this.I.b; i += 4) {
                                a(bArr3, i, a(iArr, iArr2, 0, c(bArr3, i)));
                            }
                        }
                        for (Attribute attribute = this.J; attribute != null; attribute = attribute.a) {
                            Label[] labels = attribute.getLabels();
                            if (labels != null) {
                                for (i = labels.length - 1; i >= 0; i--) {
                                    a(iArr, iArr2, labels[i]);
                                }
                            }
                        }
                        this.r = byteVector;
                        return;
                    }
                }
            }
            i = i3;
        }
    }

    private void e() {
        if (this.M == 0) {
            Label label = new Label();
            label.h = new Frame();
            label.h.b = label;
            label.a(this, this.r.b, this.r.a);
            this.O.i = label;
            this.O = label;
        } else {
            this.P.g = this.R;
        }
        this.P = null;
    }

    /* access modifiers changed from: final */
    public final int a() {
        if (this.h != 0) {
            return this.i + 6;
        }
        int i;
        int length;
        if (this.K) {
            d();
        }
        int i2 = 8;
        if (this.r.b > 0) {
            if (this.r.b > 65536) {
                throw new RuntimeException("Method code too large!");
            }
            this.b.newUTF8("Code");
            i = ((this.r.b + 18) + (this.A * 8)) + 8;
            if (this.E != null) {
                this.b.newUTF8("LocalVariableTable");
                i += this.E.b + 8;
            }
            if (this.G != null) {
                this.b.newUTF8("LocalVariableTypeTable");
                i += this.G.b + 8;
            }
            if (this.I != null) {
                this.b.newUTF8("LineNumberTable");
                i += this.I.b + 8;
            }
            if (this.v != null) {
                this.b.newUTF8(((this.b.b & 65535) >= 50 ? 1 : null) != null ? "StackMapTable" : "StackMap");
                i2 = i + (this.v.b + 8);
            } else {
                i2 = i;
            }
            if (this.J != null) {
                i2 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
        }
        if (this.j > 0) {
            this.b.newUTF8("Exceptions");
            i2 += (this.j * 2) + 8;
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & Opcodes.ASM4) != 0)) {
            this.b.newUTF8("Synthetic");
            i2 += 6;
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            this.b.newUTF8("Deprecated");
            i2 += 6;
        }
        if (this.g != null) {
            this.b.newUTF8("Signature");
            this.b.newUTF8(this.g);
            i2 += 8;
        }
        if (this.l != null) {
            this.b.newUTF8("AnnotationDefault");
            i2 += this.l.b + 6;
        }
        if (this.m != null) {
            this.b.newUTF8("RuntimeVisibleAnnotations");
            i2 += this.m.a() + 8;
        }
        if (this.n != null) {
            this.b.newUTF8("RuntimeInvisibleAnnotations");
            i2 += this.n.a() + 8;
        }
        if (this.o != null) {
            this.b.newUTF8("RuntimeVisibleParameterAnnotations");
            length = i2 + (((this.o.length - this.S) * 2) + 7);
            i = this.o.length;
            while (true) {
                i--;
                if (i < this.S) {
                    break;
                }
                length += this.o[i] == null ? 0 : this.o[i].a();
            }
        } else {
            length = i2;
        }
        if (this.p != null) {
            this.b.newUTF8("RuntimeInvisibleParameterAnnotations");
            length += ((this.p.length - this.S) * 2) + 7;
            i = this.p.length;
            while (true) {
                i--;
                if (i < this.S) {
                    break;
                }
                length += this.p[i] == null ? 0 : this.p[i].a();
            }
        }
        i2 = length;
        return this.q != null ? i2 + this.q.a(this.b, null, 0, -1, -1) : i2;
    }

    /* access modifiers changed from: final */
    public final void a(ByteVector byteVector) {
        int i = 1;
        byteVector.putShort(((393216 | ((this.c & Opcodes.ASM4) / 64)) ^ -1) & this.c).putShort(this.d).putShort(this.e);
        if (this.h != 0) {
            byteVector.putByteArray(this.b.M.b, this.h, this.i);
            return;
        }
        int i2 = this.r.b > 0 ? 1 : 0;
        if (this.j > 0) {
            i2++;
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & Opcodes.ASM4) != 0)) {
            i2++;
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            i2++;
        }
        if (this.g != null) {
            i2++;
        }
        if (this.l != null) {
            i2++;
        }
        if (this.m != null) {
            i2++;
        }
        if (this.n != null) {
            i2++;
        }
        if (this.o != null) {
            i2++;
        }
        if (this.p != null) {
            i2++;
        }
        if (this.q != null) {
            i2 += this.q.a();
        }
        byteVector.putShort(i2);
        if (this.r.b > 0) {
            i2 = (this.r.b + 12) + (this.A * 8);
            if (this.E != null) {
                i2 += this.E.b + 8;
            }
            if (this.G != null) {
                i2 += this.G.b + 8;
            }
            if (this.I != null) {
                i2 += this.I.b + 8;
            }
            int i3 = this.v != null ? i2 + (this.v.b + 8) : i2;
            if (this.J != null) {
                i3 += this.J.a(this.b, this.r.a, this.r.b, this.s, this.t);
            }
            byteVector.putShort(this.b.newUTF8("Code")).putInt(i3);
            byteVector.putShort(this.s).putShort(this.t);
            byteVector.putInt(this.r.b).putByteArray(this.r.a, 0, this.r.b);
            byteVector.putShort(this.A);
            if (this.A > 0) {
                for (Handler handler = this.B; handler != null; handler = handler.f) {
                    byteVector.putShort(handler.a.c).putShort(handler.b.c).putShort(handler.c.c).putShort(handler.e);
                }
            }
            i2 = this.E != null ? 1 : 0;
            if (this.G != null) {
                i2++;
            }
            if (this.I != null) {
                i2++;
            }
            if (this.v != null) {
                i2++;
            }
            if (this.J != null) {
                i2 += this.J.a();
            }
            byteVector.putShort(i2);
            if (this.E != null) {
                byteVector.putShort(this.b.newUTF8("LocalVariableTable"));
                byteVector.putInt(this.E.b + 2).putShort(this.D);
                byteVector.putByteArray(this.E.a, 0, this.E.b);
            }
            if (this.G != null) {
                byteVector.putShort(this.b.newUTF8("LocalVariableTypeTable"));
                byteVector.putInt(this.G.b + 2).putShort(this.F);
                byteVector.putByteArray(this.G.a, 0, this.G.b);
            }
            if (this.I != null) {
                byteVector.putShort(this.b.newUTF8("LineNumberTable"));
                byteVector.putInt(this.I.b + 2).putShort(this.H);
                byteVector.putByteArray(this.I.a, 0, this.I.b);
            }
            if (this.v != null) {
                if ((this.b.b & 65535) < 50) {
                    i = 0;
                }
                byteVector.putShort(this.b.newUTF8(i != 0 ? "StackMapTable" : "StackMap"));
                byteVector.putInt(this.v.b + 2).putShort(this.u);
                byteVector.putByteArray(this.v.a, 0, this.v.b);
            }
            if (this.J != null) {
                this.J.a(this.b, this.r.a, this.r.b, this.t, this.s, byteVector);
            }
        }
        if (this.j > 0) {
            byteVector.putShort(this.b.newUTF8("Exceptions")).putInt((this.j * 2) + 2);
            byteVector.putShort(this.j);
            for (i2 = 0; i2 < this.j; i2++) {
                byteVector.putShort(this.k[i2]);
            }
        }
        if ((this.c & Opcodes.ACC_SYNTHETIC) != 0 && ((this.b.b & 65535) < 49 || (this.c & Opcodes.ASM4) != 0)) {
            byteVector.putShort(this.b.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.c & Opcodes.ACC_DEPRECATED) != 0) {
            byteVector.putShort(this.b.newUTF8("Deprecated")).putInt(0);
        }
        if (this.g != null) {
            byteVector.putShort(this.b.newUTF8("Signature")).putInt(2).putShort(this.b.newUTF8(this.g));
        }
        if (this.l != null) {
            byteVector.putShort(this.b.newUTF8("AnnotationDefault"));
            byteVector.putInt(this.l.b);
            byteVector.putByteArray(this.l.a, 0, this.l.b);
        }
        if (this.m != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeVisibleAnnotations"));
            this.m.a(byteVector);
        }
        if (this.n != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeInvisibleAnnotations"));
            this.n.a(byteVector);
        }
        if (this.o != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.a(this.o, this.S, byteVector);
        }
        if (this.p != null) {
            byteVector.putShort(this.b.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.a(this.p, this.S, byteVector);
        }
        if (this.q != null) {
            this.q.a(this.b, null, 0, -1, -1, byteVector);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            annotationWriter.g = this.m;
            this.m = annotationWriter;
        } else {
            annotationWriter.g = this.n;
            this.n = annotationWriter;
        }
        return annotationWriter;
    }

    public AnnotationVisitor visitAnnotationDefault() {
        this.l = new ByteVector();
        return new AnnotationWriter(this.b, false, this.l, null, 0);
    }

    public void visitAttribute(Attribute attribute) {
        if (attribute.isCodeAttribute()) {
            attribute.a = this.J;
            this.J = attribute;
            return;
        }
        attribute.a = this.q;
        this.q = attribute;
    }

    public void visitCode() {
    }

    public void visitEnd() {
    }

    public void visitFieldInsn(int i, String str, String str2, String str3) {
        int i2 = 1;
        int i3 = -2;
        Item a = this.b.a(str, str2, str3);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, this.b, a);
            } else {
                char charAt = str3.charAt(0);
                switch (i) {
                    case 178:
                        i3 = this.Q;
                        if (charAt == 'D' || charAt == 'J') {
                            i2 = 2;
                        }
                        i2 += i3;
                        break;
                    case 179:
                        int i4 = this.Q;
                        i2 = (charAt == 'D' || charAt == 'J') ? -2 : -1;
                        i2 += i4;
                        break;
                    case 180:
                        i3 = this.Q;
                        if (!(charAt == 'D' || charAt == 'J')) {
                            i2 = 0;
                        }
                        i2 += i3;
                        break;
                    default:
                        i2 = this.Q;
                        if (charAt == 'D' || charAt == 'J') {
                            i3 = -3;
                        }
                        i2 += i3;
                        break;
                }
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        this.r.b(i, a.a);
    }

    public void visitFrame(int i, int i2, Object[] objArr, int i3, Object[] objArr2) {
        int i4 = 0;
        if (this.M != 0) {
            if (i == -1) {
                this.T = i2;
                a(this.r.b, i2, i3);
                for (int i5 = 0; i5 < i2; i5++) {
                    int[] iArr;
                    int i6;
                    if (objArr[i5] instanceof String) {
                        iArr = this.z;
                        i6 = this.y;
                        this.y = i6 + 1;
                        iArr[i6] = this.b.c((String) objArr[i5]) | 24117248;
                    } else if (objArr[i5] instanceof Integer) {
                        iArr = this.z;
                        i6 = this.y;
                        this.y = i6 + 1;
                        iArr[i6] = ((Integer) objArr[i5]).intValue();
                    } else {
                        iArr = this.z;
                        i6 = this.y;
                        this.y = i6 + 1;
                        iArr[i6] = this.b.a("", ((Label) objArr[i5]).c) | 25165824;
                    }
                }
                while (i4 < i3) {
                    int[] iArr2;
                    int i7;
                    if (objArr2[i4] instanceof String) {
                        iArr2 = this.z;
                        i7 = this.y;
                        this.y = i7 + 1;
                        iArr2[i7] = this.b.c((String) objArr2[i4]) | 24117248;
                    } else if (objArr2[i4] instanceof Integer) {
                        iArr2 = this.z;
                        i7 = this.y;
                        this.y = i7 + 1;
                        iArr2[i7] = ((Integer) objArr2[i4]).intValue();
                    } else {
                        iArr2 = this.z;
                        i7 = this.y;
                        this.y = i7 + 1;
                        iArr2[i7] = this.b.a("", ((Label) objArr2[i4]).c) | 25165824;
                    }
                    i4++;
                }
                b();
            } else {
                int i8;
                if (this.v == null) {
                    this.v = new ByteVector();
                    i8 = this.r.b;
                } else {
                    i8 = (this.r.b - this.w) - 1;
                    if (i8 < 0) {
                        if (i != 3) {
                            throw new IllegalStateException();
                        }
                        return;
                    }
                }
                switch (i) {
                    case 0:
                        this.T = i2;
                        this.v.putByte(ByteCode.IMPDEP2).putShort(i8).putShort(i2);
                        for (i8 = 0; i8 < i2; i8++) {
                            a(objArr[i8]);
                        }
                        this.v.putShort(i3);
                        while (i4 < i3) {
                            a(objArr2[i4]);
                            i4++;
                        }
                        break;
                    case 1:
                        this.T += i2;
                        this.v.putByte(i2 + 251).putShort(i8);
                        for (i8 = 0; i8 < i2; i8++) {
                            a(objArr[i8]);
                        }
                        break;
                    case 2:
                        this.T -= i2;
                        this.v.putByte(251 - i2).putShort(i8);
                        break;
                    case 3:
                        if (i8 >= 64) {
                            this.v.putByte(251).putShort(i8);
                            break;
                        } else {
                            this.v.putByte(i8);
                            break;
                        }
                    case 4:
                        if (i8 < 64) {
                            this.v.putByte(i8 + 64);
                        } else {
                            this.v.putByte(247).putShort(i8);
                        }
                        a(objArr2[0]);
                        break;
                }
                this.w = this.r.b;
                this.u++;
            }
            this.s = Math.max(this.s, i3);
            this.t = Math.max(this.t, this.T);
        }
    }

    public void visitIincInsn(int i, int i2) {
        if (this.P != null && this.M == 0) {
            this.P.h.a(132, i, null, null);
        }
        if (this.M != 2) {
            int i3 = i + 1;
            if (i3 > this.t) {
                this.t = i3;
            }
        }
        if (i > ByteCode.IMPDEP2 || i2 > 127 || i2 < -128) {
            this.r.putByte(ByteCode.WIDE).b(132, i).putShort(i2);
        } else {
            this.r.putByte(132).a(i, i2);
        }
    }

    public void visitInsn(int i) {
        this.r.putByte(i);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, null, null);
            } else {
                int i2 = this.Q + Frame.a[i];
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
            if ((i >= 172 && i <= 177) || i == 191) {
                e();
            }
        }
    }

    public void visitIntInsn(int i, int i2) {
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, i2, null, null);
            } else if (i != 188) {
                int i3 = this.Q + 1;
                if (i3 > this.R) {
                    this.R = i3;
                }
                this.Q = i3;
            }
        }
        if (i == 17) {
            this.r.b(i, i2);
        } else {
            this.r.a(i, i2);
        }
    }

    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        Item a = this.b.a(str, str2, handle, objArr);
        int i = a.c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a((int) Opcodes.INVOKEDYNAMIC, 0, this.b, a);
            } else {
                if (i == 0) {
                    i = Type.getArgumentsAndReturnSizes(str2);
                    a.c = i;
                }
                i = ((i & 3) + (this.Q - (i >> 2))) + 1;
                if (i > this.R) {
                    this.R = i;
                }
                this.Q = i;
            }
        }
        this.r.b(Opcodes.INVOKEDYNAMIC, a.a);
        this.r.putShort(0);
    }

    public void visitJumpInsn(int i, Label label) {
        Label label2 = null;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, null, null);
                Label a = label.a();
                a.a |= 16;
                a(0, label);
                if (i != 167) {
                    label2 = new Label();
                }
            } else if (i == 168) {
                if ((label.a & Opcodes.ACC_INTERFACE) == 0) {
                    label.a |= Opcodes.ACC_INTERFACE;
                    this.L++;
                }
                label2 = this.P;
                label2.a |= 128;
                a(this.Q + 1, label);
                label2 = new Label();
            } else {
                this.Q += Frame.a[i];
                a(this.Q, label);
            }
        }
        if ((label.a & 2) == 0 || label.c - this.r.b >= -32768) {
            this.r.putByte(i);
            label.a(this, this.r, this.r.b - 1, false);
        } else {
            if (i == 167) {
                this.r.putByte(ByteCode.GOTO_W);
            } else if (i == 168) {
                this.r.putByte(ByteCode.JSR_W);
            } else {
                if (label2 != null) {
                    label2.a |= 16;
                }
                this.r.putByte(i <= 166 ? ((i + 1) ^ 1) - 1 : i ^ 1);
                this.r.putShort(8);
                this.r.putByte(ByteCode.GOTO_W);
            }
            label.a(this, this.r, this.r.b - 1, true);
        }
        if (this.P != null) {
            if (label2 != null) {
                visitLabel(label2);
            }
            if (i == 167) {
                e();
            }
        }
    }

    public void visitLabel(Label label) {
        this.K |= label.a(this, this.r.b, this.r.a);
        if ((label.a & 1) == 0) {
            if (this.M == 0) {
                Label label2;
                if (this.P != null) {
                    if (label.c == this.P.c) {
                        label2 = this.P;
                        label2.a |= label.a & 16;
                        label.h = this.P.h;
                        return;
                    }
                    a(0, label);
                }
                this.P = label;
                if (label.h == null) {
                    label.h = new Frame();
                    label.h.b = label;
                }
                if (this.O != null) {
                    if (label.c == this.O.c) {
                        label2 = this.O;
                        label2.a |= label.a & 16;
                        label.h = this.O.h;
                        this.P = this.O;
                        return;
                    }
                    this.O.i = label;
                }
                this.O = label;
            } else if (this.M == 1) {
                if (this.P != null) {
                    this.P.g = this.R;
                    a(this.Q, label);
                }
                this.P = label;
                this.Q = 0;
                this.R = 0;
                if (this.O != null) {
                    this.O.i = label;
                }
                this.O = label;
            }
        }
    }

    public void visitLdcInsn(Object obj) {
        int i;
        Item a = this.b.a(obj);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(18, 0, this.b, a);
            } else {
                i = (a.b == 5 || a.b == 6) ? this.Q + 2 : this.Q + 1;
                if (i > this.R) {
                    this.R = i;
                }
                this.Q = i;
            }
        }
        i = a.a;
        if (a.b == 5 || a.b == 6) {
            this.r.b(20, i);
        } else if (i >= 256) {
            this.r.b(19, i);
        } else {
            this.r.a(18, i);
        }
    }

    public void visitLineNumber(int i, Label label) {
        if (this.I == null) {
            this.I = new ByteVector();
        }
        this.H++;
        this.I.putShort(label.c);
        this.I.putShort(i);
    }

    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i) {
        int i2 = 2;
        if (str3 != null) {
            if (this.G == null) {
                this.G = new ByteVector();
            }
            this.F++;
            this.G.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(str)).putShort(this.b.newUTF8(str3)).putShort(i);
        }
        if (this.E == null) {
            this.E = new ByteVector();
        }
        this.D++;
        this.E.putShort(label.c).putShort(label2.c - label.c).putShort(this.b.newUTF8(str)).putShort(this.b.newUTF8(str2)).putShort(i);
        if (this.M != 2) {
            char charAt = str2.charAt(0);
            if (!(charAt == 'J' || charAt == 'D')) {
                i2 = 1;
            }
            i2 += i;
            if (i2 > this.t) {
                this.t = i2;
            }
        }
    }

    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        int i = 0;
        int i2 = this.r.b;
        this.r.putByte(171);
        this.r.putByteArray(null, 0, (4 - (this.r.b % 4)) % 4);
        label.a(this, this.r, i2, true);
        this.r.putInt(labelArr.length);
        while (i < labelArr.length) {
            this.r.putInt(iArr[i]);
            labelArr[i].a(this, this.r, i2, true);
            i++;
        }
        a(label, labelArr);
    }

    public void visitMaxs(int i, int i2) {
        Label a;
        Label a2;
        Label label;
        Edge edge;
        int i3;
        Label label2;
        int length;
        Handler handler;
        if (this.M == 0) {
            Label a3;
            int c;
            Handler handler2 = this.B;
            while (handler2 != null) {
                a = handler2.a.a();
                a2 = handler2.c.a();
                a3 = handler2.b.a();
                c = 24117248 | this.b.c(handler2.d == null ? "java/lang/Throwable" : handler2.d);
                a2.a |= 16;
                for (label = a; label != a3; label = label.i) {
                    edge = new Edge();
                    edge.a = c;
                    edge.b = a2;
                    edge.c = label.j;
                    label.j = edge;
                }
                handler2 = handler2.f;
            }
            Frame frame = this.N.h;
            frame.a(this.b, this.c, Type.getArgumentTypes(this.f), this.t);
            b(frame);
            a2 = this.N;
            i3 = 0;
            while (a2 != null) {
                label2 = a2.k;
                a2.k = null;
                Frame frame2 = a2.h;
                if ((a2.a & 16) != 0) {
                    a2.a |= 32;
                }
                a2.a |= 64;
                length = frame2.d.length + a2.g;
                if (length <= i3) {
                    length = i3;
                }
                Edge edge2 = a2.j;
                while (edge2 != null) {
                    a = edge2.b.a();
                    if (frame2.a(this.b, a.h, edge2.a) && a.k == null) {
                        a.k = label2;
                    } else {
                        a = label2;
                    }
                    edge2 = edge2.c;
                    label2 = a;
                }
                a2 = label2;
                i3 = length;
            }
            length = i3;
            for (a2 = this.N; a2 != null; a2 = a2.i) {
                Frame frame3 = a2.h;
                if ((a2.a & 32) != 0) {
                    b(frame3);
                }
                if ((a2.a & 64) == 0) {
                    a3 = a2.i;
                    int i4 = a2.c;
                    c = (a3 == null ? this.r.b : a3.c) - 1;
                    if (c >= i4) {
                        length = Math.max(length, 1);
                        for (i3 = i4; i3 < c; i3++) {
                            this.r.a[i3] = (byte) 0;
                        }
                        this.r.a[c] = (byte) -65;
                        a(i4, 0, 1);
                        int[] iArr = this.z;
                        i4 = this.y;
                        this.y = i4 + 1;
                        iArr[i4] = this.b.c("java/lang/Throwable") | 24117248;
                        b();
                        this.B = Handler.a(this.B, a2, a3);
                    }
                }
            }
            this.A = 0;
            for (handler = this.B; handler != null; handler = handler.f) {
                this.A++;
            }
            this.s = length;
        } else if (this.M == 1) {
            for (handler = this.B; handler != null; handler = handler.f) {
                label2 = handler.c;
                a2 = handler.b;
                for (label = handler.a; label != a2; label = label.i) {
                    Edge edge3 = new Edge();
                    edge3.a = Integer.MAX_VALUE;
                    edge3.b = label2;
                    if ((label.a & 128) == 0) {
                        edge3.c = label.j;
                        label.j = edge3;
                    } else {
                        edge3.c = label.j.c.c;
                        label.j.c.c = edge3;
                    }
                }
            }
            if (this.L > 0) {
                this.N.b(null, 1, this.L);
                length = 0;
                for (a = this.N; a != null; a = a.i) {
                    if ((a.a & 128) != 0) {
                        label2 = a.j.c.b;
                        if ((label2.a & Opcodes.ACC_ABSTRACT) == 0) {
                            length++;
                            label2.b(null, ((((long) length) / 32) << 32) | (1 << (length % 32)), this.L);
                        }
                    }
                }
                for (a = this.N; a != null; a = a.i) {
                    if ((a.a & 128) != 0) {
                        label = this.N;
                        while (label != null) {
                            label.a &= -2049;
                            label = label.i;
                        }
                        a.j.c.b.b(a, 0, this.L);
                    }
                }
            }
            label2 = this.N;
            i3 = 0;
            while (label2 != null) {
                a2 = label2.k;
                int i5 = label2.f;
                length = label2.g + i5;
                if (length <= i3) {
                    length = i3;
                }
                edge = label2.j;
                Edge edge4 = (label2.a & 128) != 0 ? edge.c : edge;
                while (edge4 != null) {
                    label2 = edge4.b;
                    if ((label2.a & 8) == 0) {
                        label2.f = edge4.a == Integer.MAX_VALUE ? 1 : edge4.a + i5;
                        label2.a |= 8;
                        label2.k = a2;
                        a = label2;
                    } else {
                        a = a2;
                    }
                    edge4 = edge4.c;
                    a2 = a;
                }
                label2 = a2;
                i3 = length;
            }
            this.s = Math.max(i, i3);
        } else {
            this.s = i;
            this.t = i2;
        }
    }

    public void visitMethodInsn(int i, String str, String str2, String str3) {
        boolean z = i == 185;
        Item a = this.b.a(str, str2, str3, z);
        int i2 = a.c;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, 0, this.b, a);
            } else {
                int argumentsAndReturnSizes;
                if (i2 == 0) {
                    argumentsAndReturnSizes = Type.getArgumentsAndReturnSizes(str3);
                    a.c = argumentsAndReturnSizes;
                } else {
                    argumentsAndReturnSizes = i2;
                }
                i2 = i == 184 ? ((this.Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3)) + 1 : (this.Q - (argumentsAndReturnSizes >> 2)) + (argumentsAndReturnSizes & 3);
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
                i2 = argumentsAndReturnSizes;
            }
        }
        if (z) {
            int argumentsAndReturnSizes2;
            if (i2 == 0) {
                argumentsAndReturnSizes2 = Type.getArgumentsAndReturnSizes(str3);
                a.c = argumentsAndReturnSizes2;
            } else {
                argumentsAndReturnSizes2 = i2;
            }
            this.r.b(185, a.a).a(argumentsAndReturnSizes2 >> 2, 0);
            return;
        }
        this.r.b(i, a.a);
    }

    public void visitMultiANewArrayInsn(String str, int i) {
        Item a = this.b.a(str);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(197, i, this.b, a);
            } else {
                this.Q += 1 - i;
            }
        }
        this.r.b(197, a.a).putByte(i);
    }

    public AnnotationVisitor visitParameterAnnotation(int i, String str, boolean z) {
        ByteVector byteVector = new ByteVector();
        if ("Ljava/lang/Synthetic;".equals(str)) {
            this.S = Math.max(this.S, i + 1);
            return new AnnotationWriter(this.b, false, byteVector, null, 0);
        }
        byteVector.putShort(this.b.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.b, true, byteVector, byteVector, 2);
        if (z) {
            if (this.o == null) {
                this.o = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
            }
            annotationWriter.g = this.o[i];
            this.o[i] = annotationWriter;
            return annotationWriter;
        }
        if (this.p == null) {
            this.p = new AnnotationWriter[Type.getArgumentTypes(this.f).length];
        }
        annotationWriter.g = this.p[i];
        this.p[i] = annotationWriter;
        return annotationWriter;
    }

    public void visitTableSwitchInsn(int i, int i2, Label label, Label... labelArr) {
        int i3 = 0;
        int i4 = this.r.b;
        this.r.putByte(170);
        this.r.putByteArray(null, 0, (4 - (this.r.b % 4)) % 4);
        label.a(this, this.r, i4, true);
        this.r.putInt(i).putInt(i2);
        while (i3 < labelArr.length) {
            labelArr[i3].a(this, this.r, i4, true);
            i3++;
        }
        a(label, labelArr);
    }

    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.A++;
        Handler handler = new Handler();
        handler.a = label;
        handler.b = label2;
        handler.c = label3;
        handler.d = str;
        handler.e = str != null ? this.b.newClass(str) : 0;
        if (this.C == null) {
            this.B = handler;
        } else {
            this.C.f = handler;
        }
        this.C = handler;
    }

    public void visitTypeInsn(int i, String str) {
        Item a = this.b.a(str);
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, this.r.b, this.b, a);
            } else if (i == 187) {
                int i2 = this.Q + 1;
                if (i2 > this.R) {
                    this.R = i2;
                }
                this.Q = i2;
            }
        }
        this.r.b(i, a.a);
    }

    public void visitVarInsn(int i, int i2) {
        int i3;
        if (this.P != null) {
            if (this.M == 0) {
                this.P.h.a(i, i2, null, null);
            } else if (i == 169) {
                Label label = this.P;
                label.a |= 256;
                this.P.f = this.Q;
                e();
            } else {
                i3 = this.Q + Frame.a[i];
                if (i3 > this.R) {
                    this.R = i3;
                }
                this.Q = i3;
            }
        }
        if (this.M != 2) {
            i3 = (i == 22 || i == 24 || i == 55 || i == 57) ? i2 + 2 : i2 + 1;
            if (i3 > this.t) {
                this.t = i3;
            }
        }
        if (i2 < 4 && i != 169) {
            this.r.putByte(i < 54 ? (((i - 21) << 2) + 26) + i2 : (((i - 54) << 2) + 59) + i2);
        } else if (i2 >= 256) {
            this.r.putByte(ByteCode.WIDE).b(i, i2);
        } else {
            this.r.a(i, i2);
        }
        if (i >= 54 && this.M == 0 && this.A > 0) {
            visitLabel(new Label());
        }
    }
}
