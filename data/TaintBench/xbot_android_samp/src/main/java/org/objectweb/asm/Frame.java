package org.objectweb.asm;

import org.mozilla.classfile.ByteCode;

final class Frame {
    static final int[] a;
    Label b;
    int[] c;
    int[] d;
    private int[] e;
    private int[] f;
    private int g;
    private int h;
    private int[] i;

    static {
        int[] iArr = new int[ByteCode.BREAKPOINT];
        String str = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = str.charAt(i) - 69;
        }
        a = iArr;
    }

    Frame() {
    }

    private int a() {
        if (this.g > 0) {
            int[] iArr = this.f;
            int i = this.g - 1;
            this.g = i;
            return iArr[i];
        }
        Label label = this.b;
        int i2 = label.f - 1;
        label.f = i2;
        return 50331648 | (-i2);
    }

    private int a(int i) {
        if (this.e == null || i >= this.e.length) {
            return 33554432 | i;
        }
        int i2 = this.e[i];
        if (i2 != 0) {
            return i2;
        }
        i2 = 33554432 | i;
        this.e[i] = i2;
        return i2;
    }

    private int a(ClassWriter classWriter, int i) {
        int c;
        if (i == 16777222) {
            c = classWriter.c(classWriter.I) | 24117248;
        } else if ((-1048576 & i) != 25165824) {
            return i;
        } else {
            c = classWriter.c(classWriter.H[1048575 & i].g) | 24117248;
        }
        for (int i2 = 0; i2 < this.h; i2++) {
            int i3 = this.i[i2];
            int i4 = -268435456 & i3;
            int i5 = 251658240 & i3;
            if (i5 == 33554432) {
                i3 = this.c[i3 & 8388607] + i4;
            } else if (i5 == 50331648) {
                i3 = this.d[this.d.length - (i3 & 8388607)] + i4;
            }
            if (i == i3) {
                return c;
            }
        }
        return i;
    }

    private void a(int i, int i2) {
        if (this.e == null) {
            this.e = new int[10];
        }
        int length = this.e.length;
        if (i >= length) {
            int[] iArr = new int[Math.max(i + 1, length * 2)];
            System.arraycopy(this.e, 0, iArr, 0, length);
            this.e = iArr;
        }
        this.e[i] = i2;
    }

    private void a(String str) {
        char charAt = str.charAt(0);
        if (charAt == '(') {
            c((Type.getArgumentsAndReturnSizes(str) >> 2) - 1);
        } else if (charAt == 'J' || charAt == 'D') {
            c(2);
        } else {
            c(1);
        }
    }

    private void a(ClassWriter classWriter, String str) {
        int b = b(classWriter, str);
        if (b != 0) {
            b(b);
            if (b == 16777220 || b == 16777219) {
                b(16777216);
            }
        }
    }

    private static boolean a(ClassWriter classWriter, int i, int[] iArr, int i2) {
        int i3 = iArr[i2];
        if (i3 == i) {
            return false;
        }
        int i4;
        if ((268435455 & i) != 16777221) {
            i4 = i;
        } else if (i3 == 16777221) {
            return false;
        } else {
            i4 = 16777221;
        }
        if (i3 == 0) {
            iArr[i2] = i4;
            return true;
        }
        if ((i3 & 267386880) == 24117248 || (i3 & -268435456) != 0) {
            if (i4 == 16777221) {
                return false;
            }
            i4 = (-1048576 & i4) == (-1048576 & i3) ? (i3 & 267386880) == 24117248 ? classWriter.a(i4 & 1048575, 1048575 & i3) | ((i4 & -268435456) | 24117248) : classWriter.c("java/lang/Object") | 24117248 : ((i4 & 267386880) == 24117248 || (i4 & -268435456) != 0) ? classWriter.c("java/lang/Object") | 24117248 : 16777216;
        } else if (i3 != 16777221) {
            i4 = 16777216;
        } else if ((i4 & 267386880) != 24117248 && (i4 & -268435456) == 0) {
            i4 = 16777216;
        }
        if (i3 == i4) {
            return false;
        }
        iArr[i2] = i4;
        return true;
    }

    private static int b(ClassWriter classWriter, String str) {
        int i = 16777217;
        int indexOf = str.charAt(0) == '(' ? str.indexOf(41) + 1 : 0;
        switch (str.charAt(indexOf)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                return 16777217;
            case 'D':
                return 16777219;
            case 'F':
                return 16777218;
            case 'J':
                return 16777220;
            case 'L':
                return 24117248 | classWriter.c(str.substring(indexOf + 1, str.length() - 1));
            case 'V':
                return 0;
            default:
                int i2 = indexOf + 1;
                while (str.charAt(i2) == '[') {
                    i2++;
                }
                switch (str.charAt(i2)) {
                    case 'B':
                        i = 16777226;
                        break;
                    case 'C':
                        i = 16777227;
                        break;
                    case 'D':
                        i = 16777219;
                        break;
                    case 'F':
                        i = 16777218;
                        break;
                    case 'I':
                        break;
                    case 'J':
                        i = 16777220;
                        break;
                    case 'S':
                        i = 16777228;
                        break;
                    case 'Z':
                        i = 16777225;
                        break;
                    default:
                        i = classWriter.c(str.substring(i2 + 1, str.length() - 1)) | 24117248;
                        break;
                }
                return ((i2 - indexOf) << 28) | i;
        }
    }

    private void b(int i) {
        if (this.f == null) {
            this.f = new int[10];
        }
        int length = this.f.length;
        if (this.g >= length) {
            int[] iArr = new int[Math.max(this.g + 1, length * 2)];
            System.arraycopy(this.f, 0, iArr, 0, length);
            this.f = iArr;
        }
        int[] iArr2 = this.f;
        int i2 = this.g;
        this.g = i2 + 1;
        iArr2[i2] = i;
        length = this.b.f + this.g;
        if (length > this.b.g) {
            this.b.g = length;
        }
    }

    private void c(int i) {
        if (this.g >= i) {
            this.g -= i;
            return;
        }
        Label label = this.b;
        label.f -= i - this.g;
        this.g = 0;
    }

    private void d(int i) {
        if (this.i == null) {
            this.i = new int[2];
        }
        int length = this.i.length;
        if (this.h >= length) {
            int[] iArr = new int[Math.max(this.h + 1, length * 2)];
            System.arraycopy(this.i, 0, iArr, 0, length);
            this.i = iArr;
        }
        int[] iArr2 = this.i;
        int i2 = this.h;
        this.h = i2 + 1;
        iArr2[i2] = i;
    }

    /* access modifiers changed from: 0000 */
    public void a(int i, int i2, ClassWriter classWriter, Item item) {
        int a;
        int a2;
        int a3;
        String str;
        switch (i) {
            case 0:
            case 116:
            case 117:
            case 118:
            case 119:
            case 145:
            case 146:
            case 147:
            case 167:
            case 177:
                return;
            case 1:
                b(16777221);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            case 17:
            case 21:
                b(16777217);
                return;
            case 9:
            case 10:
            case 22:
                b(16777220);
                b(16777216);
                return;
            case 11:
            case 12:
            case 13:
            case 23:
                b(16777218);
                return;
            case 14:
            case 15:
            case 24:
                b(16777219);
                b(16777216);
                return;
            case 18:
                switch (item.b) {
                    case 3:
                        b(16777217);
                        return;
                    case 4:
                        b(16777218);
                        return;
                    case 5:
                        b(16777220);
                        b(16777216);
                        return;
                    case 6:
                        b(16777219);
                        b(16777216);
                        return;
                    case 7:
                        b(24117248 | classWriter.c("java/lang/Class"));
                        return;
                    case 8:
                        b(24117248 | classWriter.c("java/lang/String"));
                        return;
                    case 16:
                        b(24117248 | classWriter.c("java/lang/invoke/MethodType"));
                        return;
                    default:
                        b(24117248 | classWriter.c("java/lang/invoke/MethodHandle"));
                        return;
                }
            case 25:
                b(a(i2));
                return;
            case 46:
            case 51:
            case 52:
            case 53:
                c(2);
                b(16777217);
                return;
            case 47:
            case 143:
                c(2);
                b(16777220);
                b(16777216);
                return;
            case 48:
                c(2);
                b(16777218);
                return;
            case 49:
            case 138:
                c(2);
                b(16777219);
                b(16777216);
                return;
            case 50:
                c(1);
                b(a() - 268435456);
                return;
            case 54:
            case 56:
            case 58:
                a(i2, a());
                if (i2 > 0) {
                    a = a(i2 - 1);
                    if (a == 16777220 || a == 16777219) {
                        a(i2 - 1, 16777216);
                        return;
                    } else if ((251658240 & a) != 16777216) {
                        a(i2 - 1, a | 8388608);
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case 55:
            case 57:
                c(1);
                a(i2, a());
                a(i2 + 1, 16777216);
                if (i2 > 0) {
                    a = a(i2 - 1);
                    if (a == 16777220 || a == 16777219) {
                        a(i2 - 1, 16777216);
                        return;
                    } else if ((251658240 & a) != 16777216) {
                        a(i2 - 1, a | 8388608);
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
                c(3);
                return;
            case 80:
            case 82:
                c(4);
                return;
            case 87:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 174:
            case 176:
            case 191:
            case 194:
            case 195:
            case 198:
            case 199:
                c(1);
                return;
            case 88:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 173:
            case 175:
                c(2);
                return;
            case 89:
                a = a();
                b(a);
                b(a);
                return;
            case 90:
                a = a();
                a2 = a();
                b(a);
                b(a2);
                b(a);
                return;
            case 91:
                a = a();
                a2 = a();
                a3 = a();
                b(a);
                b(a3);
                b(a2);
                b(a);
                return;
            case 92:
                a = a();
                a2 = a();
                b(a2);
                b(a);
                b(a2);
                b(a);
                return;
            case 93:
                a = a();
                a2 = a();
                a3 = a();
                b(a2);
                b(a);
                b(a3);
                b(a2);
                b(a);
                return;
            case 94:
                a = a();
                a2 = a();
                a3 = a();
                int a4 = a();
                b(a2);
                b(a);
                b(a4);
                b(a3);
                b(a2);
                b(a);
                return;
            case 95:
                a = a();
                a2 = a();
                b(a);
                b(a2);
                return;
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case 120:
            case 122:
            case 124:
            case 126:
            case 128:
            case 130:
            case 136:
            case 142:
            case 149:
            case 150:
                c(2);
                b(16777217);
                return;
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case 129:
            case 131:
                c(4);
                b(16777220);
                b(16777216);
                return;
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
            case 137:
            case 144:
                c(2);
                b(16777218);
                return;
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
                c(4);
                b(16777219);
                b(16777216);
                return;
            case 121:
            case 123:
            case 125:
                c(3);
                b(16777220);
                b(16777216);
                return;
            case 132:
                a(i2, 16777217);
                return;
            case 133:
            case 140:
                c(1);
                b(16777220);
                b(16777216);
                return;
            case 134:
                c(1);
                b(16777218);
                return;
            case 135:
            case 141:
                c(1);
                b(16777219);
                b(16777216);
                return;
            case 139:
            case 190:
            case 193:
                c(1);
                b(16777217);
                return;
            case 148:
            case 151:
            case 152:
                c(4);
                b(16777217);
                return;
            case 168:
            case 169:
                throw new RuntimeException("JSR/RET are not supported with computeFrames option");
            case 178:
                a(classWriter, item.i);
                return;
            case 179:
                a(item.i);
                return;
            case 180:
                c(1);
                a(classWriter, item.i);
                return;
            case 181:
                a(item.i);
                a();
                return;
            case 182:
            case 183:
            case 184:
            case 185:
                a(item.i);
                if (i != 184) {
                    a = a();
                    if (i == 183 && item.h.charAt(0) == '<') {
                        d(a);
                    }
                }
                a(classWriter, item.i);
                return;
            case Opcodes.INVOKEDYNAMIC /*186*/:
                a(item.h);
                a(classWriter, item.h);
                return;
            case 187:
                b(25165824 | classWriter.a(item.g, i2));
                return;
            case 188:
                a();
                switch (i2) {
                    case 4:
                        b(285212681);
                        return;
                    case 5:
                        b(285212683);
                        return;
                    case 6:
                        b(285212674);
                        return;
                    case 7:
                        b(285212675);
                        return;
                    case 8:
                        b(285212682);
                        return;
                    case 9:
                        b(285212684);
                        return;
                    case 10:
                        b(285212673);
                        return;
                    default:
                        b(285212676);
                        return;
                }
            case 189:
                str = item.g;
                a();
                if (str.charAt(0) == '[') {
                    a(classWriter, new StringBuffer().append('[').append(str).toString());
                    return;
                } else {
                    b(classWriter.c(str) | 292552704);
                    return;
                }
            case 192:
                str = item.g;
                a();
                if (str.charAt(0) == '[') {
                    a(classWriter, str);
                    return;
                } else {
                    b(classWriter.c(str) | 24117248);
                    return;
                }
            default:
                c(i2);
                a(classWriter, item.g);
                return;
        }
    }

    /* access modifiers changed from: 0000 */
    public void a(ClassWriter classWriter, int i, Type[] typeArr, int i2) {
        int i3 = 1;
        int i4 = 0;
        this.c = new int[i2];
        this.d = new int[0];
        if ((i & 8) != 0) {
            i3 = 0;
        } else if ((Opcodes.ASM4 & i) == 0) {
            this.c[0] = 24117248 | classWriter.c(classWriter.I);
        } else {
            this.c[0] = 16777222;
        }
        while (i4 < typeArr.length) {
            int b = b(classWriter, typeArr[i4].getDescriptor());
            int i5 = i3 + 1;
            this.c[i3] = b;
            if (b == 16777220 || b == 16777219) {
                i3 = i5 + 1;
                this.c[i5] = 16777216;
            } else {
                i3 = i5;
            }
            i4++;
        }
        while (i3 < i2) {
            i4 = i3 + 1;
            this.c[i3] = 16777216;
            i3 = i4;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean a(ClassWriter classWriter, Frame frame, int i) {
        int i2;
        int i3;
        int i4;
        boolean z = false;
        int length = this.c.length;
        int length2 = this.d.length;
        if (frame.c == null) {
            frame.c = new int[length];
            z = true;
        }
        int i5 = 0;
        boolean z2 = z;
        while (i5 < length) {
            if (this.e == null || i5 >= this.e.length) {
                i2 = this.c[i5];
            } else {
                i2 = this.e[i5];
                if (i2 == 0) {
                    i2 = this.c[i5];
                } else {
                    i3 = -268435456 & i2;
                    i4 = 251658240 & i2;
                    if (i4 != 16777216) {
                        i3 = i4 == 33554432 ? i3 + this.c[8388607 & i2] : i3 + this.d[length2 - (8388607 & i2)];
                        i2 = ((i2 & 8388608) == 0 || !(i3 == 16777220 || i3 == 16777219)) ? i3 : 16777216;
                    }
                }
            }
            if (this.i != null) {
                i2 = a(classWriter, i2);
            }
            z2 |= a(classWriter, i2, frame.c, i5);
            i5++;
        }
        int a;
        if (i > 0) {
            i3 = 0;
            i2 = z2;
            while (i3 < length) {
                a = a(classWriter, this.c[i3], frame.c, i3) | i2;
                i3++;
                i2 = a;
            }
            if (frame.d == null) {
                frame.d = new int[1];
                i2 = 1;
            }
            return a(classWriter, i, frame.d, 0) | i2;
        }
        length = this.d.length + this.b.f;
        if (frame.d == null) {
            frame.d = new int[(this.g + length)];
            z = true;
        } else {
            z = z2;
        }
        boolean z3 = z;
        for (a = 0; a < length; a++) {
            i2 = this.d[a];
            if (this.i != null) {
                i2 = a(classWriter, i2);
            }
            z3 |= a(classWriter, i2, frame.d, a);
        }
        for (i2 = 0; i2 < this.g; i2++) {
            a = this.f[i2];
            i5 = -268435456 & a;
            i4 = 251658240 & a;
            if (i4 != 16777216) {
                i5 = i4 == 33554432 ? i5 + this.c[8388607 & a] : i5 + this.d[length2 - (8388607 & a)];
                a = ((a & 8388608) == 0 || !(i5 == 16777220 || i5 == 16777219)) ? i5 : 16777216;
            }
            if (this.i != null) {
                a = a(classWriter, a);
            }
            z3 |= a(classWriter, a, frame.d, length + i2);
        }
        return z3;
    }
}
