package org.objectweb.asm.signature;

public class SignatureReader {
    private final String a;

    public SignatureReader(String str) {
        this.a = str;
    }

    private static int a(String str, int i, SignatureVisitor signatureVisitor) {
        int i2 = i + 1;
        char charAt = str.charAt(i);
        switch (charAt) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'V':
            case 'Z':
                signatureVisitor.visitBaseType(charAt);
                return i2;
            case 'T':
                int indexOf = str.indexOf(59, i2);
                signatureVisitor.visitTypeVariable(str.substring(i2, indexOf));
                return indexOf + 1;
            case '[':
                return a(str, i2, signatureVisitor.visitArrayType());
            default:
                Object obj = null;
                int i3 = i2;
                int i4 = i2;
                Object obj2 = null;
                while (true) {
                    int i5 = i4 + 1;
                    char charAt2 = str.charAt(i4);
                    String substring;
                    switch (charAt2) {
                        case '.':
                        case ';':
                            if (obj == null) {
                                substring = str.substring(i3, i5 - 1);
                                if (obj2 != null) {
                                    signatureVisitor.visitInnerClassType(substring);
                                } else {
                                    signatureVisitor.visitClassType(substring);
                                }
                            }
                            if (charAt2 != ';') {
                                obj2 = 1;
                                obj = null;
                                i3 = i5;
                                i4 = i5;
                                break;
                            }
                            signatureVisitor.visitEnd();
                            return i5;
                        case '<':
                            substring = str.substring(i3, i5 - 1);
                            if (obj2 != null) {
                                signatureVisitor.visitInnerClassType(substring);
                            } else {
                                signatureVisitor.visitClassType(substring);
                            }
                            int i6 = i5;
                            while (true) {
                                charAt2 = str.charAt(i6);
                                switch (charAt2) {
                                    case '*':
                                        i6++;
                                        signatureVisitor.visitTypeArgument();
                                        break;
                                    case '+':
                                    case '-':
                                        i6 = a(str, i6 + 1, signatureVisitor.visitTypeArgument(charAt2));
                                        break;
                                    case '>':
                                        i4 = i6;
                                        obj = 1;
                                        continue;
                                    default:
                                        i6 = a(str, i6, signatureVisitor.visitTypeArgument(SignatureVisitor.INSTANCEOF));
                                        break;
                                }
                            }
                        default:
                            i4 = i5;
                            break;
                    }
                }
        }
    }

    public void accept(SignatureVisitor signatureVisitor) {
        int i = 0;
        String str = this.a;
        int length = str.length();
        if (str.charAt(0) == '<') {
            i = 2;
            char charAt;
            do {
                int indexOf = str.indexOf(58, i);
                signatureVisitor.visitFormalTypeParameter(str.substring(i - 1, indexOf));
                i = indexOf + 1;
                charAt = str.charAt(i);
                if (charAt == 'L' || charAt == '[' || charAt == 'T') {
                    i = a(str, i, signatureVisitor.visitClassBound());
                }
                while (true) {
                    indexOf = i;
                    i = indexOf + 1;
                    charAt = str.charAt(indexOf);
                    if (charAt != ':') {
                        break;
                    }
                    i = a(str, i, signatureVisitor.visitInterfaceBound());
                }
            } while (charAt != '>');
        }
        if (str.charAt(i) == '(') {
            i++;
            while (str.charAt(i) != ')') {
                i = a(str, i, signatureVisitor.visitParameterType());
            }
            i = a(str, i + 1, signatureVisitor.visitReturnType());
            while (i < length) {
                i = a(str, i + 1, signatureVisitor.visitExceptionType());
            }
            return;
        }
        i = a(str, i, signatureVisitor.visitSuperclass());
        while (i < length) {
            i = a(str, i, signatureVisitor.visitInterface());
        }
    }

    public void acceptType(SignatureVisitor signatureVisitor) {
        a(this.a, 0, signatureVisitor);
    }
}
