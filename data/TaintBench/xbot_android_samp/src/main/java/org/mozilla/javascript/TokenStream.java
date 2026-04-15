package org.mozilla.javascript;

import java.io.IOException;
import java.io.Reader;
import org.mozilla.javascript.Token.CommentType;
import org.objectweb.asm.Opcodes;

class TokenStream {
    static final /* synthetic */ boolean $assertionsDisabled = (!TokenStream.class.desiredAssertionStatus());
    private static final char BYTE_ORDER_MARK = '﻿';
    private static final int EOF_CHAR = -1;
    private ObjToIntMap allStrings = new ObjToIntMap(50);
    private int commentCursor = -1;
    private String commentPrefix = "";
    CommentType commentType;
    int cursor;
    private boolean dirtyLine;
    private boolean hitEOF = false;
    private boolean isHex;
    private boolean isOctal;
    private int lineEndChar = -1;
    private int lineStart = 0;
    int lineno;
    private double number;
    private Parser parser;
    private int quoteChar;
    String regExpFlags;
    private char[] sourceBuffer;
    int sourceCursor;
    private int sourceEnd;
    private Reader sourceReader;
    private String sourceString;
    private String string = "";
    private char[] stringBuffer = new char[128];
    private int stringBufferTop;
    int tokenBeg;
    int tokenEnd;
    private final int[] ungetBuffer = new int[3];
    private int ungetCursor;
    private boolean xmlIsAttribute;
    private boolean xmlIsTagContent;
    private int xmlOpenTagsCount;

    TokenStream(Parser parser, Reader sourceReader, String sourceString, int lineno) {
        this.parser = parser;
        this.lineno = lineno;
        if (sourceReader != null) {
            if (sourceString != null) {
                Kit.codeBug();
            }
            this.sourceReader = sourceReader;
            this.sourceBuffer = new char[Opcodes.ACC_INTERFACE];
            this.sourceEnd = 0;
        } else {
            if (sourceString == null) {
                Kit.codeBug();
            }
            this.sourceString = sourceString;
            this.sourceEnd = sourceString.length();
        }
        this.cursor = 0;
        this.sourceCursor = 0;
    }

    /* access modifiers changed from: 0000 */
    public String tokenToString(int token) {
        return "";
    }

    static boolean isKeyword(String s) {
        return stringToKeyword(s) != 0;
    }

    private static int stringToKeyword(java.lang.String r69) {
        /*
        r4 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r6 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r11 = 121; // 0x79 float:1.7E-43 double:6.0E-322;
        r13 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        r14 = 31;
        r15 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        r17 = 113; // 0x71 float:1.58E-43 double:5.6E-322;
        r19 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r21 = 44;
        r25 = 119; // 0x77 float:1.67E-43 double:5.9E-322;
        r26 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        r28 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        r31 = 52;
        r35 = 153; // 0x99 float:2.14E-43 double:7.56E-322;
        r38 = 30;
        r39 = 42;
        r44 = 4;
        r48 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        r50 = 43;
        r54 = 45;
        r56 = 32;
        r57 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        r58 = 126; // 0x7e float:1.77E-43 double:6.23E-322;
        r60 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        r61 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r62 = 72;
        r2 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r3 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r5 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r7 = 124; // 0x7c float:1.74E-43 double:6.13E-322;
        r8 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r9 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r10 = 154; // 0x9a float:2.16E-43 double:7.6E-322;
        r12 = 160; // 0xa0 float:2.24E-43 double:7.9E-322;
        r16 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r18 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r20 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r22 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r23 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r24 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r27 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r29 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r30 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r32 = 53;
        r33 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r34 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r36 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r37 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r40 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r41 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r42 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r43 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r45 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r46 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r47 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r49 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r51 = 50;
        r52 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r53 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r55 = 81;
        r59 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r66 = r69;
        r65 = 0;
        r63 = 0;
        r67 = r66.length();
        switch(r67) {
            case 2: goto L_0x00a0;
            case 3: goto L_0x00f1;
            case 4: goto L_0x01ba;
            case 5: goto L_0x02eb;
            case 6: goto L_0x0380;
            case 7: goto L_0x03e3;
            case 8: goto L_0x0412;
            case 9: goto L_0x043b;
            case 10: goto L_0x046b;
            case 11: goto L_0x0087;
            case 12: goto L_0x048d;
            default: goto L_0x0087;
        };
    L_0x0087:
        if (r63 == 0) goto L_0x009b;
    L_0x0089:
        r0 = r63;
        r1 = r66;
        if (r0 == r1) goto L_0x009b;
    L_0x008f:
        r0 = r63;
        r1 = r66;
        r67 = r0.equals(r1);
        if (r67 != 0) goto L_0x009b;
    L_0x0099:
        r65 = 0;
    L_0x009b:
        if (r65 != 0) goto L_0x0493;
    L_0x009d:
        r67 = 0;
    L_0x009f:
        return r67;
    L_0x00a0:
        r67 = 1;
        r64 = r66.charAt(r67);
        r67 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x00bf;
    L_0x00ae:
        r67 = 0;
        r67 = r66.charAt(r67);
        r68 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x00bc:
        r65 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        goto L_0x009b;
    L_0x00bf:
        r67 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x00d8;
    L_0x00c7:
        r67 = 0;
        r67 = r66.charAt(r67);
        r68 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x00d5:
        r65 = 52;
        goto L_0x009b;
    L_0x00d8:
        r67 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x00e0:
        r67 = 0;
        r67 = r66.charAt(r67);
        r68 = 100;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x00ee:
        r65 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        goto L_0x009b;
    L_0x00f1:
        r67 = 0;
        r67 = r66.charAt(r67);
        switch(r67) {
            case 102: goto L_0x00fb;
            case 105: goto L_0x011a;
            case 108: goto L_0x013a;
            case 110: goto L_0x015a;
            case 116: goto L_0x017a;
            case 118: goto L_0x019a;
            default: goto L_0x00fa;
        };
    L_0x00fa:
        goto L_0x0087;
    L_0x00fb:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0109:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0117:
        r65 = 119; // 0x77 float:1.67E-43 double:5.9E-322;
        goto L_0x009b;
    L_0x011a:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0128:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0136:
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x009b;
    L_0x013a:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0148:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0156:
        r65 = 153; // 0x99 float:2.14E-43 double:7.56E-322;
        goto L_0x009b;
    L_0x015a:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 119; // 0x77 float:1.67E-43 double:5.9E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0168:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0176:
        r65 = 30;
        goto L_0x009b;
    L_0x017a:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 121; // 0x79 float:1.7E-43 double:6.0E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0188:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0196:
        r65 = 81;
        goto L_0x009b;
    L_0x019a:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x01a8:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 97;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x01b6:
        r65 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        goto L_0x009b;
    L_0x01ba:
        r67 = 0;
        r67 = r66.charAt(r67);
        switch(r67) {
            case 98: goto L_0x01c5;
            case 99: goto L_0x01cb;
            case 101: goto L_0x0221;
            case 103: goto L_0x0277;
            case 108: goto L_0x027d;
            case 110: goto L_0x0283;
            case 116: goto L_0x0289;
            case 118: goto L_0x02df;
            case 119: goto L_0x02e5;
            default: goto L_0x01c3;
        };
    L_0x01c3:
        goto L_0x0087;
    L_0x01c5:
        r63 = "byte";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x01cb:
        r67 = 3;
        r64 = r66.charAt(r67);
        r67 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x01f9;
    L_0x01d9:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x01e7:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 97;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x01f5:
        r65 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        goto L_0x009b;
    L_0x01f9:
        r67 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x0201:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 97;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x020f:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x021d:
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x009b;
    L_0x0221:
        r67 = 3;
        r64 = r66.charAt(r67);
        r67 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x024f;
    L_0x022f:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x023d:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x024b:
        r65 = 113; // 0x71 float:1.58E-43 double:5.6E-322;
        goto L_0x009b;
    L_0x024f:
        r67 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x0257:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0265:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x0273:
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x009b;
    L_0x0277:
        r63 = "goto";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x027d:
        r63 = "long";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x0283:
        r63 = "null";
        r65 = 42;
        goto L_0x0087;
    L_0x0289:
        r67 = 3;
        r64 = r66.charAt(r67);
        r67 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x02b7;
    L_0x0297:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x02a5:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x02b3:
        r65 = 45;
        goto L_0x009b;
    L_0x02b7:
        r67 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x02bf:
        r67 = 2;
        r67 = r66.charAt(r67);
        r68 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x02cd:
        r67 = 1;
        r67 = r66.charAt(r67);
        r68 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        r0 = r67;
        r1 = r68;
        if (r0 != r1) goto L_0x0087;
    L_0x02db:
        r65 = 43;
        goto L_0x009b;
    L_0x02df:
        r63 = "void";
        r65 = 126; // 0x7e float:1.77E-43 double:6.23E-322;
        goto L_0x0087;
    L_0x02e5:
        r63 = "with";
        r65 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        goto L_0x0087;
    L_0x02eb:
        r67 = 2;
        r67 = r66.charAt(r67);
        switch(r67) {
            case 97: goto L_0x02f6;
            case 98: goto L_0x02f4;
            case 99: goto L_0x02f4;
            case 100: goto L_0x02f4;
            case 101: goto L_0x02fc;
            case 102: goto L_0x02f4;
            case 103: goto L_0x02f4;
            case 104: goto L_0x02f4;
            case 105: goto L_0x031e;
            case 106: goto L_0x02f4;
            case 107: goto L_0x02f4;
            case 108: goto L_0x0324;
            case 109: goto L_0x02f4;
            case 110: goto L_0x032a;
            case 111: goto L_0x034c;
            case 112: goto L_0x036e;
            case 113: goto L_0x02f4;
            case 114: goto L_0x0374;
            case 115: goto L_0x02f4;
            case 116: goto L_0x037a;
            default: goto L_0x02f4;
        };
    L_0x02f4:
        goto L_0x0087;
    L_0x02f6:
        r63 = "class";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x02fc:
        r67 = 0;
        r64 = r66.charAt(r67);
        r67 = 98;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0310;
    L_0x030a:
        r63 = "break";
        r65 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        goto L_0x0087;
    L_0x0310:
        r67 = 121; // 0x79 float:1.7E-43 double:6.0E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x0318:
        r63 = "yield";
        r65 = 72;
        goto L_0x0087;
    L_0x031e:
        r63 = "while";
        r65 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        goto L_0x0087;
    L_0x0324:
        r63 = "false";
        r65 = 44;
        goto L_0x0087;
    L_0x032a:
        r67 = 0;
        r64 = r66.charAt(r67);
        r67 = 99;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x033e;
    L_0x0338:
        r63 = "const";
        r65 = 154; // 0x9a float:2.16E-43 double:7.6E-322;
        goto L_0x0087;
    L_0x033e:
        r67 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x0346:
        r63 = "final";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x034c:
        r67 = 0;
        r64 = r66.charAt(r67);
        r67 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0360;
    L_0x035a:
        r63 = "float";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x0360:
        r67 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x0368:
        r63 = "short";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x036e:
        r63 = "super";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x0374:
        r63 = "throw";
        r65 = 50;
        goto L_0x0087;
    L_0x037a:
        r63 = "catch";
        r65 = 124; // 0x7c float:1.74E-43 double:6.13E-322;
        goto L_0x0087;
    L_0x0380:
        r67 = 1;
        r67 = r66.charAt(r67);
        switch(r67) {
            case 97: goto L_0x038b;
            case 101: goto L_0x0391;
            case 104: goto L_0x03b3;
            case 109: goto L_0x03b9;
            case 111: goto L_0x03bf;
            case 116: goto L_0x03c5;
            case 117: goto L_0x03cb;
            case 119: goto L_0x03d1;
            case 120: goto L_0x03d7;
            case 121: goto L_0x03dd;
            default: goto L_0x0389;
        };
    L_0x0389:
        goto L_0x0087;
    L_0x038b:
        r63 = "native";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x0391:
        r67 = 0;
        r64 = r66.charAt(r67);
        r67 = 100;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x03a5;
    L_0x039f:
        r63 = "delete";
        r65 = 31;
        goto L_0x0087;
    L_0x03a5:
        r67 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x03ad:
        r63 = "return";
        r65 = 4;
        goto L_0x0087;
    L_0x03b3:
        r63 = "throws";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x03b9:
        r63 = "import";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x03bf:
        r63 = "double";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x03c5:
        r63 = "static";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x03cb:
        r63 = "public";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x03d1:
        r63 = "switch";
        r65 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        goto L_0x0087;
    L_0x03d7:
        r63 = "export";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x03dd:
        r63 = "typeof";
        r65 = 32;
        goto L_0x0087;
    L_0x03e3:
        r67 = 1;
        r67 = r66.charAt(r67);
        switch(r67) {
            case 97: goto L_0x03ee;
            case 101: goto L_0x03f4;
            case 105: goto L_0x03fa;
            case 111: goto L_0x0400;
            case 114: goto L_0x0406;
            case 120: goto L_0x040c;
            default: goto L_0x03ec;
        };
    L_0x03ec:
        goto L_0x0087;
    L_0x03ee:
        r63 = "package";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x03f4:
        r63 = "default";
        r65 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        goto L_0x0087;
    L_0x03fa:
        r63 = "finally";
        r65 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        goto L_0x0087;
    L_0x0400:
        r63 = "boolean";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x0406:
        r63 = "private";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x040c:
        r63 = "extends";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x0412:
        r67 = 0;
        r67 = r66.charAt(r67);
        switch(r67) {
            case 97: goto L_0x041d;
            case 99: goto L_0x0423;
            case 100: goto L_0x0429;
            case 102: goto L_0x042f;
            case 118: goto L_0x0435;
            default: goto L_0x041b;
        };
    L_0x041b:
        goto L_0x0087;
    L_0x041d:
        r63 = "abstract";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x0423:
        r63 = "continue";
        r65 = 121; // 0x79 float:1.7E-43 double:6.0E-322;
        goto L_0x0087;
    L_0x0429:
        r63 = "debugger";
        r65 = 160; // 0xa0 float:2.24E-43 double:7.9E-322;
        goto L_0x0087;
    L_0x042f:
        r63 = "function";
        r65 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        goto L_0x0087;
    L_0x0435:
        r63 = "volatile";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x043b:
        r67 = 0;
        r64 = r66.charAt(r67);
        r67 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x044f;
    L_0x0449:
        r63 = "interface";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x044f:
        r67 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x045d;
    L_0x0457:
        r63 = "protected";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x045d:
        r67 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x0465:
        r63 = "transient";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x046b:
        r67 = 1;
        r64 = r66.charAt(r67);
        r67 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x047f;
    L_0x0479:
        r63 = "implements";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x047f:
        r67 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        r0 = r64;
        r1 = r67;
        if (r0 != r1) goto L_0x0087;
    L_0x0487:
        r63 = "instanceof";
        r65 = 53;
        goto L_0x0087;
    L_0x048d:
        r63 = "synchronized";
        r65 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        goto L_0x0087;
    L_0x0493:
        r0 = r65;
        r0 = r0 & 255;
        r67 = r0;
        goto L_0x009f;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.TokenStream.stringToKeyword(java.lang.String):int");
    }

    /* access modifiers changed from: final */
    public final String getSourceString() {
        return this.sourceString;
    }

    /* access modifiers changed from: final */
    public final int getLineno() {
        return this.lineno;
    }

    /* access modifiers changed from: final */
    public final String getString() {
        return this.string;
    }

    /* access modifiers changed from: final */
    public final char getQuoteChar() {
        return (char) this.quoteChar;
    }

    /* access modifiers changed from: final */
    public final double getNumber() {
        return this.number;
    }

    /* access modifiers changed from: final */
    public final boolean isNumberOctal() {
        return this.isOctal;
    }

    /* access modifiers changed from: final */
    public final boolean isNumberHex() {
        return this.isHex;
    }

    /* access modifiers changed from: final */
    public final boolean eof() {
        return this.hitEOF;
    }

    /* access modifiers changed from: final */
    public final int getToken() throws java.io.IOException {
        /*
        r24 = this;
    L_0x0000:
        r4 = r24.getChar();
        r21 = -1;
        r0 = r21;
        if (r4 != r0) goto L_0x0027;
    L_0x000a:
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r21 = r21 + -1;
        r0 = r21;
        r1 = r24;
        r1.tokenBeg = r0;
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r0 = r21;
        r1 = r24;
        r1.tokenEnd = r0;
        r18 = 0;
    L_0x0026:
        return r18;
    L_0x0027:
        r21 = 10;
        r0 = r21;
        if (r4 != r0) goto L_0x0052;
    L_0x002d:
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.dirtyLine = r0;
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r21 = r21 + -1;
        r0 = r21;
        r1 = r24;
        r1.tokenBeg = r0;
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r0 = r21;
        r1 = r24;
        r1.tokenEnd = r0;
        r18 = 1;
        goto L_0x0026;
    L_0x0052:
        r21 = isJSSpace(r4);
        if (r21 != 0) goto L_0x0000;
    L_0x0058:
        r21 = 45;
        r0 = r21;
        if (r4 == r0) goto L_0x0066;
    L_0x005e:
        r21 = 1;
        r0 = r21;
        r1 = r24;
        r1.dirtyLine = r0;
    L_0x0066:
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r21 = r21 + -1;
        r0 = r21;
        r1 = r24;
        r1.tokenBeg = r0;
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r0 = r21;
        r1 = r24;
        r1.tokenEnd = r0;
        r21 = 64;
        r0 = r21;
        if (r4 != r0) goto L_0x0089;
    L_0x0086:
        r18 = 147; // 0x93 float:2.06E-43 double:7.26E-322;
        goto L_0x0026;
    L_0x0089:
        r15 = 0;
        r21 = 92;
        r0 = r21;
        if (r4 != r0) goto L_0x00d5;
    L_0x0090:
        r4 = r24.getChar();
        r21 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        r0 = r21;
        if (r4 != r0) goto L_0x00cc;
    L_0x009a:
        r13 = 1;
        r15 = 1;
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.stringBufferTop = r0;
    L_0x00a4:
        if (r13 == 0) goto L_0x01e0;
    L_0x00a6:
        r6 = r15;
    L_0x00a7:
        if (r15 == 0) goto L_0x00f6;
    L_0x00a9:
        r10 = 0;
        r12 = 0;
    L_0x00ab:
        r21 = 4;
        r0 = r21;
        if (r12 == r0) goto L_0x00bb;
    L_0x00b1:
        r4 = r24.getChar();
        r10 = org.mozilla.javascript.Kit.xDigitToInt(r4, r10);
        if (r10 >= 0) goto L_0x00ec;
    L_0x00bb:
        if (r10 >= 0) goto L_0x00ef;
    L_0x00bd:
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r22 = "msg.invalid.escape";
        r21.addError(r22);
        r18 = -1;
        goto L_0x0026;
    L_0x00cc:
        r13 = 0;
        r0 = r24;
        r0.ungetChar(r4);
        r4 = 92;
        goto L_0x00a4;
    L_0x00d5:
        r0 = (char) r4;
        r21 = r0;
        r13 = java.lang.Character.isJavaIdentifierStart(r21);
        if (r13 == 0) goto L_0x00a4;
    L_0x00de:
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.stringBufferTop = r0;
        r0 = r24;
        r0.addToString(r4);
        goto L_0x00a4;
    L_0x00ec:
        r12 = r12 + 1;
        goto L_0x00ab;
    L_0x00ef:
        r0 = r24;
        r0.addToString(r10);
        r15 = 0;
        goto L_0x00a7;
    L_0x00f6:
        r4 = r24.getChar();
        r21 = 92;
        r0 = r21;
        if (r4 != r0) goto L_0x011c;
    L_0x0100:
        r4 = r24.getChar();
        r21 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        r0 = r21;
        if (r4 != r0) goto L_0x010d;
    L_0x010a:
        r15 = 1;
        r6 = 1;
        goto L_0x00a7;
    L_0x010d:
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r22 = "msg.illegal.character";
        r21.addError(r22);
        r18 = -1;
        goto L_0x0026;
    L_0x011c:
        r21 = -1;
        r0 = r21;
        if (r4 == r0) goto L_0x0132;
    L_0x0122:
        r21 = 65279; // 0xfeff float:9.1475E-41 double:3.2252E-319;
        r0 = r21;
        if (r4 == r0) goto L_0x0132;
    L_0x0129:
        r0 = (char) r4;
        r21 = r0;
        r21 = java.lang.Character.isJavaIdentifierPart(r21);
        if (r21 != 0) goto L_0x01c7;
    L_0x0132:
        r0 = r24;
        r0.ungetChar(r4);
        r19 = r24.getStringFromBuffer();
        if (r6 != 0) goto L_0x01d1;
    L_0x013d:
        r18 = stringToKeyword(r19);
        if (r18 == 0) goto L_0x01ad;
    L_0x0143:
        r21 = 153; // 0x99 float:2.14E-43 double:7.56E-322;
        r0 = r18;
        r1 = r21;
        if (r0 == r1) goto L_0x0153;
    L_0x014b:
        r21 = 72;
        r0 = r18;
        r1 = r21;
        if (r0 != r1) goto L_0x017d;
    L_0x0153:
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r0 = r21;
        r0 = r0.compilerEnv;
        r21 = r0;
        r21 = r21.getLanguageVersion();
        r22 = 170; // 0xaa float:2.38E-43 double:8.4E-322;
        r0 = r21;
        r1 = r22;
        if (r0 >= r1) goto L_0x017d;
    L_0x016b:
        r21 = 153; // 0x99 float:2.14E-43 double:7.56E-322;
        r0 = r18;
        r1 = r21;
        if (r0 != r1) goto L_0x01ce;
    L_0x0173:
        r21 = "let";
    L_0x0175:
        r0 = r21;
        r1 = r24;
        r1.string = r0;
        r18 = 39;
    L_0x017d:
        r0 = r24;
        r0 = r0.allStrings;
        r21 = r0;
        r0 = r21;
        r1 = r19;
        r21 = r0.intern(r1);
        r21 = (java.lang.String) r21;
        r0 = r21;
        r1 = r24;
        r1.string = r0;
        r21 = 127; // 0x7f float:1.78E-43 double:6.27E-322;
        r0 = r18;
        r1 = r21;
        if (r0 != r1) goto L_0x0026;
    L_0x019b:
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r0 = r21;
        r0 = r0.compilerEnv;
        r21 = r0;
        r21 = r21.isReservedKeywordAsIdentifier();
        if (r21 == 0) goto L_0x0026;
    L_0x01ad:
        r0 = r24;
        r0 = r0.allStrings;
        r21 = r0;
        r0 = r21;
        r1 = r19;
        r21 = r0.intern(r1);
        r21 = (java.lang.String) r21;
        r0 = r21;
        r1 = r24;
        r1.string = r0;
        r18 = 39;
        goto L_0x0026;
    L_0x01c7:
        r0 = r24;
        r0.addToString(r4);
        goto L_0x00a7;
    L_0x01ce:
        r21 = "yield";
        goto L_0x0175;
    L_0x01d1:
        r21 = isKeyword(r19);
        if (r21 == 0) goto L_0x01ad;
    L_0x01d7:
        r0 = r24;
        r1 = r19;
        r19 = r0.convertLastCharToHex(r1);
        goto L_0x01ad;
    L_0x01e0:
        r21 = isDigit(r4);
        if (r21 != 0) goto L_0x01f6;
    L_0x01e6:
        r21 = 46;
        r0 = r21;
        if (r4 != r0) goto L_0x036a;
    L_0x01ec:
        r21 = r24.peekChar();
        r21 = isDigit(r21);
        if (r21 == 0) goto L_0x036a;
    L_0x01f6:
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.isOctal = r0;
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.stringBufferTop = r0;
        r3 = 10;
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.isOctal = r0;
        r0 = r21;
        r1 = r24;
        r1.isHex = r0;
        r21 = 48;
        r0 = r21;
        if (r4 != r0) goto L_0x023a;
    L_0x021c:
        r4 = r24.getChar();
        r21 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r0 = r21;
        if (r4 == r0) goto L_0x022c;
    L_0x0226:
        r21 = 88;
        r0 = r21;
        if (r4 != r0) goto L_0x0254;
    L_0x022c:
        r3 = 16;
        r21 = 1;
        r0 = r21;
        r1 = r24;
        r1.isHex = r0;
        r4 = r24.getChar();
    L_0x023a:
        r21 = 16;
        r0 = r21;
        if (r3 != r0) goto L_0x026f;
    L_0x0240:
        r21 = 0;
        r0 = r21;
        r21 = org.mozilla.javascript.Kit.xDigitToInt(r4, r0);
        if (r21 < 0) goto L_0x02af;
    L_0x024a:
        r0 = r24;
        r0.addToString(r4);
        r4 = r24.getChar();
        goto L_0x0240;
    L_0x0254:
        r21 = isDigit(r4);
        if (r21 == 0) goto L_0x0265;
    L_0x025a:
        r3 = 8;
        r21 = 1;
        r0 = r21;
        r1 = r24;
        r1.isOctal = r0;
        goto L_0x023a;
    L_0x0265:
        r21 = 48;
        r0 = r24;
        r1 = r21;
        r0.addToString(r1);
        goto L_0x023a;
    L_0x026f:
        r21 = 48;
        r0 = r21;
        if (r0 > r4) goto L_0x02af;
    L_0x0275:
        r21 = 57;
        r0 = r21;
        if (r4 > r0) goto L_0x02af;
    L_0x027b:
        r21 = 8;
        r0 = r21;
        if (r3 != r0) goto L_0x02a2;
    L_0x0281:
        r21 = 56;
        r0 = r21;
        if (r4 < r0) goto L_0x02a2;
    L_0x0287:
        r0 = r24;
        r0 = r0.parser;
        r22 = r0;
        r23 = "msg.bad.octal.literal";
        r21 = 56;
        r0 = r21;
        if (r4 != r0) goto L_0x02ac;
    L_0x0295:
        r21 = "8";
    L_0x0297:
        r0 = r22;
        r1 = r23;
        r2 = r21;
        r0.addWarning(r1, r2);
        r3 = 10;
    L_0x02a2:
        r0 = r24;
        r0.addToString(r4);
        r4 = r24.getChar();
        goto L_0x026f;
    L_0x02ac:
        r21 = "9";
        goto L_0x0297;
    L_0x02af:
        r14 = 1;
        r21 = 10;
        r0 = r21;
        if (r3 != r0) goto L_0x032c;
    L_0x02b6:
        r21 = 46;
        r0 = r21;
        if (r4 == r0) goto L_0x02c8;
    L_0x02bc:
        r21 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r0 = r21;
        if (r4 == r0) goto L_0x02c8;
    L_0x02c2:
        r21 = 69;
        r0 = r21;
        if (r4 != r0) goto L_0x032c;
    L_0x02c8:
        r14 = 0;
        r21 = 46;
        r0 = r21;
        if (r4 != r0) goto L_0x02de;
    L_0x02cf:
        r0 = r24;
        r0.addToString(r4);
        r4 = r24.getChar();
        r21 = isDigit(r4);
        if (r21 != 0) goto L_0x02cf;
    L_0x02de:
        r21 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r0 = r21;
        if (r4 == r0) goto L_0x02ea;
    L_0x02e4:
        r21 = 69;
        r0 = r21;
        if (r4 != r0) goto L_0x032c;
    L_0x02ea:
        r0 = r24;
        r0.addToString(r4);
        r4 = r24.getChar();
        r21 = 43;
        r0 = r21;
        if (r4 == r0) goto L_0x02ff;
    L_0x02f9:
        r21 = 45;
        r0 = r21;
        if (r4 != r0) goto L_0x0308;
    L_0x02ff:
        r0 = r24;
        r0.addToString(r4);
        r4 = r24.getChar();
    L_0x0308:
        r21 = isDigit(r4);
        if (r21 != 0) goto L_0x031d;
    L_0x030e:
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r22 = "msg.missing.exponent";
        r21.addError(r22);
        r18 = -1;
        goto L_0x0026;
    L_0x031d:
        r0 = r24;
        r0.addToString(r4);
        r4 = r24.getChar();
        r21 = isDigit(r4);
        if (r21 != 0) goto L_0x031d;
    L_0x032c:
        r0 = r24;
        r0.ungetChar(r4);
        r17 = r24.getStringFromBuffer();
        r0 = r17;
        r1 = r24;
        r1.string = r0;
        r21 = 10;
        r0 = r21;
        if (r3 != r0) goto L_0x035f;
    L_0x0341:
        if (r14 != 0) goto L_0x035f;
    L_0x0343:
        r7 = java.lang.Double.parseDouble(r17);	 Catch:{ NumberFormatException -> 0x034f }
    L_0x0347:
        r0 = r24;
        r0.number = r7;
        r18 = 40;
        goto L_0x0026;
    L_0x034f:
        r11 = move-exception;
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r22 = "msg.caught.nfe";
        r21.addError(r22);
        r18 = -1;
        goto L_0x0026;
    L_0x035f:
        r21 = 0;
        r0 = r17;
        r1 = r21;
        r7 = org.mozilla.javascript.ScriptRuntime.stringToNumber(r0, r1, r3);
        goto L_0x0347;
    L_0x036a:
        r21 = 34;
        r0 = r21;
        if (r4 == r0) goto L_0x0376;
    L_0x0370:
        r21 = 39;
        r0 = r21;
        if (r4 != r0) goto L_0x04c7;
    L_0x0376:
        r0 = r24;
        r0.quoteChar = r4;
        r21 = 0;
        r0 = r21;
        r1 = r24;
        r1.stringBufferTop = r0;
        r21 = 0;
        r0 = r24;
        r1 = r21;
        r4 = r0.getChar(r1);
    L_0x038c:
        r0 = r24;
        r0 = r0.quoteChar;
        r21 = r0;
        r0 = r21;
        if (r4 == r0) goto L_0x04a9;
    L_0x0396:
        r21 = 10;
        r0 = r21;
        if (r4 == r0) goto L_0x03a2;
    L_0x039c:
        r21 = -1;
        r0 = r21;
        if (r4 != r0) goto L_0x03c2;
    L_0x03a2:
        r0 = r24;
        r0.ungetChar(r4);
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r0 = r21;
        r1 = r24;
        r1.tokenEnd = r0;
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r22 = "msg.unterminated.string.lit";
        r21.addError(r22);
        r18 = -1;
        goto L_0x0026;
    L_0x03c2:
        r21 = 92;
        r0 = r21;
        if (r4 != r0) goto L_0x041c;
    L_0x03c8:
        r4 = r24.getChar();
        switch(r4) {
            case 10: goto L_0x04a3;
            case 98: goto L_0x042d;
            case 102: goto L_0x0430;
            case 110: goto L_0x0433;
            case 114: goto L_0x0436;
            case 116: goto L_0x0439;
            case 117: goto L_0x043f;
            case 118: goto L_0x043c;
            case 120: goto L_0x046c;
            default: goto L_0x03cf;
        };
    L_0x03cf:
        r21 = 48;
        r0 = r21;
        if (r0 > r4) goto L_0x041c;
    L_0x03d5:
        r21 = 56;
        r0 = r21;
        if (r4 >= r0) goto L_0x041c;
    L_0x03db:
        r20 = r4 + -48;
        r4 = r24.getChar();
        r21 = 48;
        r0 = r21;
        if (r0 > r4) goto L_0x0415;
    L_0x03e7:
        r21 = 56;
        r0 = r21;
        if (r4 >= r0) goto L_0x0415;
    L_0x03ed:
        r21 = r20 * 8;
        r21 = r21 + r4;
        r20 = r21 + -48;
        r4 = r24.getChar();
        r21 = 48;
        r0 = r21;
        if (r0 > r4) goto L_0x0415;
    L_0x03fd:
        r21 = 56;
        r0 = r21;
        if (r4 >= r0) goto L_0x0415;
    L_0x0403:
        r21 = 31;
        r0 = r20;
        r1 = r21;
        if (r0 > r1) goto L_0x0415;
    L_0x040b:
        r21 = r20 * 8;
        r21 = r21 + r4;
        r20 = r21 + -48;
        r4 = r24.getChar();
    L_0x0415:
        r0 = r24;
        r0.ungetChar(r4);
        r4 = r20;
    L_0x041c:
        r0 = r24;
        r0.addToString(r4);
        r21 = 0;
        r0 = r24;
        r1 = r21;
        r4 = r0.getChar(r1);
        goto L_0x038c;
    L_0x042d:
        r4 = 8;
        goto L_0x041c;
    L_0x0430:
        r4 = 12;
        goto L_0x041c;
    L_0x0433:
        r4 = 10;
        goto L_0x041c;
    L_0x0436:
        r4 = 13;
        goto L_0x041c;
    L_0x0439:
        r4 = 9;
        goto L_0x041c;
    L_0x043c:
        r4 = 11;
        goto L_0x041c;
    L_0x043f:
        r0 = r24;
        r9 = r0.stringBufferTop;
        r21 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        r0 = r24;
        r1 = r21;
        r0.addToString(r1);
        r10 = 0;
        r12 = 0;
    L_0x044e:
        r21 = 4;
        r0 = r21;
        if (r12 == r0) goto L_0x0466;
    L_0x0454:
        r4 = r24.getChar();
        r10 = org.mozilla.javascript.Kit.xDigitToInt(r4, r10);
        if (r10 < 0) goto L_0x038c;
    L_0x045e:
        r0 = r24;
        r0.addToString(r4);
        r12 = r12 + 1;
        goto L_0x044e;
    L_0x0466:
        r0 = r24;
        r0.stringBufferTop = r9;
        r4 = r10;
        goto L_0x041c;
    L_0x046c:
        r4 = r24.getChar();
        r21 = 0;
        r0 = r21;
        r10 = org.mozilla.javascript.Kit.xDigitToInt(r4, r0);
        if (r10 >= 0) goto L_0x0485;
    L_0x047a:
        r21 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r0 = r24;
        r1 = r21;
        r0.addToString(r1);
        goto L_0x038c;
    L_0x0485:
        r5 = r4;
        r4 = r24.getChar();
        r10 = org.mozilla.javascript.Kit.xDigitToInt(r4, r10);
        if (r10 >= 0) goto L_0x04a0;
    L_0x0490:
        r21 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r0 = r24;
        r1 = r21;
        r0.addToString(r1);
        r0 = r24;
        r0.addToString(r5);
        goto L_0x038c;
    L_0x04a0:
        r4 = r10;
        goto L_0x041c;
    L_0x04a3:
        r4 = r24.getChar();
        goto L_0x038c;
    L_0x04a9:
        r19 = r24.getStringFromBuffer();
        r0 = r24;
        r0 = r0.allStrings;
        r21 = r0;
        r0 = r21;
        r1 = r19;
        r21 = r0.intern(r1);
        r21 = (java.lang.String) r21;
        r0 = r21;
        r1 = r24;
        r1.string = r0;
        r18 = 41;
        goto L_0x0026;
    L_0x04c7:
        switch(r4) {
            case 33: goto L_0x05b5;
            case 37: goto L_0x079a;
            case 38: goto L_0x056d;
            case 40: goto L_0x04ed;
            case 41: goto L_0x04f1;
            case 42: goto L_0x06b4;
            case 43: goto L_0x07b2;
            case 44: goto L_0x04f5;
            case 45: goto L_0x07d6;
            case 46: goto L_0x0511;
            case 47: goto L_0x06c8;
            case 58: goto L_0x04fd;
            case 59: goto L_0x04d9;
            case 60: goto L_0x05d9;
            case 61: goto L_0x0591;
            case 62: goto L_0x0660;
            case 63: goto L_0x04f9;
            case 91: goto L_0x04dd;
            case 93: goto L_0x04e1;
            case 94: goto L_0x0559;
            case 123: goto L_0x04e5;
            case 124: goto L_0x0535;
            case 125: goto L_0x04e9;
            case 126: goto L_0x07ae;
            default: goto L_0x04ca;
        };
    L_0x04ca:
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r22 = "msg.illegal.character";
        r21.addError(r22);
        r18 = -1;
        goto L_0x0026;
    L_0x04d9:
        r18 = 82;
        goto L_0x0026;
    L_0x04dd:
        r18 = 83;
        goto L_0x0026;
    L_0x04e1:
        r18 = 84;
        goto L_0x0026;
    L_0x04e5:
        r18 = 85;
        goto L_0x0026;
    L_0x04e9:
        r18 = 86;
        goto L_0x0026;
    L_0x04ed:
        r18 = 87;
        goto L_0x0026;
    L_0x04f1:
        r18 = 88;
        goto L_0x0026;
    L_0x04f5:
        r18 = 89;
        goto L_0x0026;
    L_0x04f9:
        r18 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        goto L_0x0026;
    L_0x04fd:
        r21 = 58;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x050d;
    L_0x0509:
        r18 = 144; // 0x90 float:2.02E-43 double:7.1E-322;
        goto L_0x0026;
    L_0x050d:
        r18 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        goto L_0x0026;
    L_0x0511:
        r21 = 46;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0521;
    L_0x051d:
        r18 = 143; // 0x8f float:2.0E-43 double:7.07E-322;
        goto L_0x0026;
    L_0x0521:
        r21 = 40;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0531;
    L_0x052d:
        r18 = 146; // 0x92 float:2.05E-43 double:7.2E-322;
        goto L_0x0026;
    L_0x0531:
        r18 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        goto L_0x0026;
    L_0x0535:
        r21 = 124; // 0x7c float:1.74E-43 double:6.13E-322;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0545;
    L_0x0541:
        r18 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        goto L_0x0026;
    L_0x0545:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0555;
    L_0x0551:
        r18 = 91;
        goto L_0x0026;
    L_0x0555:
        r18 = 9;
        goto L_0x0026;
    L_0x0559:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0569;
    L_0x0565:
        r18 = 92;
        goto L_0x0026;
    L_0x0569:
        r18 = 10;
        goto L_0x0026;
    L_0x056d:
        r21 = 38;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x057d;
    L_0x0579:
        r18 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        goto L_0x0026;
    L_0x057d:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x058d;
    L_0x0589:
        r18 = 93;
        goto L_0x0026;
    L_0x058d:
        r18 = 11;
        goto L_0x0026;
    L_0x0591:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x05b1;
    L_0x059d:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x05ad;
    L_0x05a9:
        r18 = 46;
        goto L_0x0026;
    L_0x05ad:
        r18 = 12;
        goto L_0x0026;
    L_0x05b1:
        r18 = 90;
        goto L_0x0026;
    L_0x05b5:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x05d5;
    L_0x05c1:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x05d1;
    L_0x05cd:
        r18 = 47;
        goto L_0x0026;
    L_0x05d1:
        r18 = 13;
        goto L_0x0026;
    L_0x05d5:
        r18 = 26;
        goto L_0x0026;
    L_0x05d9:
        r21 = 33;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x062c;
    L_0x05e5:
        r21 = 45;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0623;
    L_0x05f1:
        r21 = 45;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x061a;
    L_0x05fd:
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r21 = r21 + -4;
        r0 = r21;
        r1 = r24;
        r1.tokenBeg = r0;
        r24.skipLine();
        r21 = org.mozilla.javascript.Token.CommentType.HTML;
        r0 = r21;
        r1 = r24;
        r1.commentType = r0;
        r18 = 161; // 0xa1 float:2.26E-43 double:7.95E-322;
        goto L_0x0026;
    L_0x061a:
        r21 = 45;
        r0 = r24;
        r1 = r21;
        r0.ungetCharIgnoreLineEnd(r1);
    L_0x0623:
        r21 = 33;
        r0 = r24;
        r1 = r21;
        r0.ungetCharIgnoreLineEnd(r1);
    L_0x062c:
        r21 = 60;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x064c;
    L_0x0638:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0648;
    L_0x0644:
        r18 = 94;
        goto L_0x0026;
    L_0x0648:
        r18 = 18;
        goto L_0x0026;
    L_0x064c:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x065c;
    L_0x0658:
        r18 = 15;
        goto L_0x0026;
    L_0x065c:
        r18 = 14;
        goto L_0x0026;
    L_0x0660:
        r21 = 62;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x06a0;
    L_0x066c:
        r21 = 62;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x068c;
    L_0x0678:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0688;
    L_0x0684:
        r18 = 96;
        goto L_0x0026;
    L_0x0688:
        r18 = 20;
        goto L_0x0026;
    L_0x068c:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x069c;
    L_0x0698:
        r18 = 95;
        goto L_0x0026;
    L_0x069c:
        r18 = 19;
        goto L_0x0026;
    L_0x06a0:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x06b0;
    L_0x06ac:
        r18 = 17;
        goto L_0x0026;
    L_0x06b0:
        r18 = 16;
        goto L_0x0026;
    L_0x06b4:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x06c4;
    L_0x06c0:
        r18 = 99;
        goto L_0x0026;
    L_0x06c4:
        r18 = 23;
        goto L_0x0026;
    L_0x06c8:
        r24.markCommentStart();
        r21 = 47;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x06f4;
    L_0x06d7:
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r21 = r21 + -2;
        r0 = r21;
        r1 = r24;
        r1.tokenBeg = r0;
        r24.skipLine();
        r21 = org.mozilla.javascript.Token.CommentType.LINE;
        r0 = r21;
        r1 = r24;
        r1.commentType = r0;
        r18 = 161; // 0xa1 float:2.26E-43 double:7.95E-322;
        goto L_0x0026;
    L_0x06f4:
        r21 = 42;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0786;
    L_0x0700:
        r16 = 0;
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r21 = r21 + -2;
        r0 = r21;
        r1 = r24;
        r1.tokenBeg = r0;
        r21 = 42;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x074d;
    L_0x071c:
        r16 = 1;
        r21 = org.mozilla.javascript.Token.CommentType.JSDOC;
        r0 = r21;
        r1 = r24;
        r1.commentType = r0;
    L_0x0726:
        r4 = r24.getChar();
        r21 = -1;
        r0 = r21;
        if (r4 != r0) goto L_0x0756;
    L_0x0730:
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r21 = r21 + -1;
        r0 = r21;
        r1 = r24;
        r1.tokenEnd = r0;
        r0 = r24;
        r0 = r0.parser;
        r21 = r0;
        r22 = "msg.unterminated.comment";
        r21.addError(r22);
        r18 = 161; // 0xa1 float:2.26E-43 double:7.95E-322;
        goto L_0x0026;
    L_0x074d:
        r21 = org.mozilla.javascript.Token.CommentType.BLOCK_COMMENT;
        r0 = r21;
        r1 = r24;
        r1.commentType = r0;
        goto L_0x0726;
    L_0x0756:
        r21 = 42;
        r0 = r21;
        if (r4 != r0) goto L_0x075f;
    L_0x075c:
        r16 = 1;
        goto L_0x0726;
    L_0x075f:
        r21 = 47;
        r0 = r21;
        if (r4 != r0) goto L_0x0777;
    L_0x0765:
        if (r16 == 0) goto L_0x0726;
    L_0x0767:
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r0 = r21;
        r1 = r24;
        r1.tokenEnd = r0;
        r18 = 161; // 0xa1 float:2.26E-43 double:7.95E-322;
        goto L_0x0026;
    L_0x0777:
        r16 = 0;
        r0 = r24;
        r0 = r0.cursor;
        r21 = r0;
        r0 = r21;
        r1 = r24;
        r1.tokenEnd = r0;
        goto L_0x0726;
    L_0x0786:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0796;
    L_0x0792:
        r18 = 100;
        goto L_0x0026;
    L_0x0796:
        r18 = 24;
        goto L_0x0026;
    L_0x079a:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x07aa;
    L_0x07a6:
        r18 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        goto L_0x0026;
    L_0x07aa:
        r18 = 25;
        goto L_0x0026;
    L_0x07ae:
        r18 = 27;
        goto L_0x0026;
    L_0x07b2:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x07c2;
    L_0x07be:
        r18 = 97;
        goto L_0x0026;
    L_0x07c2:
        r21 = 43;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x07d2;
    L_0x07ce:
        r18 = 106; // 0x6a float:1.49E-43 double:5.24E-322;
        goto L_0x0026;
    L_0x07d2:
        r18 = 21;
        goto L_0x0026;
    L_0x07d6:
        r21 = 61;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x07f0;
    L_0x07e2:
        r4 = 98;
    L_0x07e4:
        r21 = 1;
        r0 = r21;
        r1 = r24;
        r1.dirtyLine = r0;
        r18 = r4;
        goto L_0x0026;
    L_0x07f0:
        r21 = 45;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x082b;
    L_0x07fc:
        r0 = r24;
        r0 = r0.dirtyLine;
        r21 = r0;
        if (r21 != 0) goto L_0x0828;
    L_0x0804:
        r21 = 62;
        r0 = r24;
        r1 = r21;
        r21 = r0.matchChar(r1);
        if (r21 == 0) goto L_0x0828;
    L_0x0810:
        r21 = "--";
        r0 = r24;
        r1 = r21;
        r0.markCommentStart(r1);
        r24.skipLine();
        r21 = org.mozilla.javascript.Token.CommentType.HTML;
        r0 = r21;
        r1 = r24;
        r1.commentType = r0;
        r18 = 161; // 0xa1 float:2.26E-43 double:7.95E-322;
        goto L_0x0026;
    L_0x0828:
        r4 = 107; // 0x6b float:1.5E-43 double:5.3E-322;
        goto L_0x07e4;
    L_0x082b:
        r4 = 22;
        goto L_0x07e4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.javascript.TokenStream.getToken():int");
    }

    private static boolean isAlpha(int c) {
        if (c <= 90) {
            if (65 <= c) {
                return true;
            }
            return false;
        } else if (97 > c || c > 122) {
            return false;
        } else {
            return true;
        }
    }

    static boolean isDigit(int c) {
        return 48 <= c && c <= 57;
    }

    static boolean isJSSpace(int c) {
        if (c <= 127) {
            if (c == 32 || c == 9 || c == 12 || c == 11) {
                return true;
            }
            return false;
        } else if (c == 160 || c == 65279 || Character.getType((char) c) == 12) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isJSFormatChar(int c) {
        return c > 127 && Character.getType((char) c) == 16;
    }

    /* access modifiers changed from: 0000 */
    public void readRegExp(int startToken) throws IOException {
        int c;
        int start = this.tokenBeg;
        this.stringBufferTop = 0;
        if (startToken == 100) {
            addToString(61);
        } else if (startToken != 24) {
            Kit.codeBug();
        }
        boolean inCharSet = false;
        while (true) {
            c = getChar();
            if (c == 47 && !inCharSet) {
                int reEnd = this.stringBufferTop;
                while (true) {
                    if (!matchChar(103)) {
                        if (!matchChar(105)) {
                            if (!matchChar(109)) {
                                if (!matchChar(121)) {
                                    break;
                                }
                                addToString(121);
                            } else {
                                addToString(109);
                            }
                        } else {
                            addToString(105);
                        }
                    } else {
                        addToString(103);
                    }
                }
                this.tokenEnd = (this.stringBufferTop + start) + 2;
                if (isAlpha(peekChar())) {
                    this.parser.reportError("msg.invalid.re.flag");
                }
                this.string = new String(this.stringBuffer, 0, reEnd);
                this.regExpFlags = new String(this.stringBuffer, reEnd, this.stringBufferTop - reEnd);
                return;
            } else if (c == 10 || c == -1) {
                ungetChar(c);
                this.tokenEnd = this.cursor - 1;
                this.string = new String(this.stringBuffer, 0, this.stringBufferTop);
                this.parser.reportError("msg.unterminated.re.lit");
            } else {
                if (c == 92) {
                    addToString(c);
                    c = getChar();
                } else if (c == 91) {
                    inCharSet = true;
                } else if (c == 93) {
                    inCharSet = false;
                }
                addToString(c);
            }
        }
        ungetChar(c);
        this.tokenEnd = this.cursor - 1;
        this.string = new String(this.stringBuffer, 0, this.stringBufferTop);
        this.parser.reportError("msg.unterminated.re.lit");
    }

    /* access modifiers changed from: 0000 */
    public String readAndClearRegExpFlags() {
        String flags = this.regExpFlags;
        this.regExpFlags = null;
        return flags;
    }

    /* access modifiers changed from: 0000 */
    public boolean isXMLAttribute() {
        return this.xmlIsAttribute;
    }

    /* access modifiers changed from: 0000 */
    public int getFirstXMLToken() throws IOException {
        this.xmlOpenTagsCount = 0;
        this.xmlIsAttribute = false;
        this.xmlIsTagContent = false;
        if (!canUngetChar()) {
            return -1;
        }
        ungetChar(60);
        return getNextXMLToken();
    }

    /* access modifiers changed from: 0000 */
    public int getNextXMLToken() throws IOException {
        this.tokenBeg = this.cursor;
        this.stringBufferTop = 0;
        int c = getChar();
        while (c != -1) {
            if (!this.xmlIsTagContent) {
                switch (c) {
                    case 60:
                        addToString(c);
                        switch (peekChar()) {
                            case 33:
                                addToString(getChar());
                                switch (peekChar()) {
                                    case 45:
                                        addToString(getChar());
                                        c = getChar();
                                        if (c == 45) {
                                            addToString(c);
                                            if (readXmlComment()) {
                                                break;
                                            }
                                            return -1;
                                        }
                                        this.stringBufferTop = 0;
                                        this.string = null;
                                        this.parser.addError("msg.XML.bad.form");
                                        return -1;
                                    case 91:
                                        addToString(getChar());
                                        if (getChar() == 67 && getChar() == 68 && getChar() == 65 && getChar() == 84 && getChar() == 65 && getChar() == 91) {
                                            addToString(67);
                                            addToString(68);
                                            addToString(65);
                                            addToString(84);
                                            addToString(65);
                                            addToString(91);
                                            if (readCDATA()) {
                                                break;
                                            }
                                            return -1;
                                        }
                                        this.stringBufferTop = 0;
                                        this.string = null;
                                        this.parser.addError("msg.XML.bad.form");
                                        return -1;
                                    default:
                                        if (readEntity()) {
                                            break;
                                        }
                                        return -1;
                                }
                            case 47:
                                addToString(getChar());
                                if (this.xmlOpenTagsCount != 0) {
                                    this.xmlIsTagContent = true;
                                    this.xmlOpenTagsCount--;
                                    break;
                                }
                                this.stringBufferTop = 0;
                                this.string = null;
                                this.parser.addError("msg.XML.bad.form");
                                return -1;
                            case 63:
                                addToString(getChar());
                                if (readPI()) {
                                    break;
                                }
                                return -1;
                            default:
                                this.xmlIsTagContent = true;
                                this.xmlOpenTagsCount++;
                                break;
                        }
                    case 123:
                        ungetChar(c);
                        this.string = getStringFromBuffer();
                        return 145;
                    default:
                        addToString(c);
                        break;
                }
            }
            switch (c) {
                case 9:
                case 10:
                case 13:
                case 32:
                    addToString(c);
                    break;
                case 34:
                case 39:
                    addToString(c);
                    if (!readQuotedString(c)) {
                        return -1;
                    }
                    break;
                case 47:
                    addToString(c);
                    if (peekChar() == 62) {
                        addToString(getChar());
                        this.xmlIsTagContent = false;
                        this.xmlOpenTagsCount--;
                        break;
                    }
                    break;
                case 61:
                    addToString(c);
                    this.xmlIsAttribute = true;
                    break;
                case 62:
                    addToString(c);
                    this.xmlIsTagContent = false;
                    this.xmlIsAttribute = false;
                    break;
                case 123:
                    ungetChar(c);
                    this.string = getStringFromBuffer();
                    return 145;
                default:
                    addToString(c);
                    this.xmlIsAttribute = false;
                    break;
            }
            if (!this.xmlIsTagContent && this.xmlOpenTagsCount == 0) {
                this.string = getStringFromBuffer();
                return 148;
            }
            c = getChar();
        }
        this.tokenEnd = this.cursor;
        this.stringBufferTop = 0;
        this.string = null;
        this.parser.addError("msg.XML.bad.form");
        return -1;
    }

    private boolean readQuotedString(int quote) throws IOException {
        int c = getChar();
        while (c != -1) {
            addToString(c);
            if (c == quote) {
                return true;
            }
            c = getChar();
        }
        this.stringBufferTop = 0;
        this.string = null;
        this.parser.addError("msg.XML.bad.form");
        return false;
    }

    private boolean readXmlComment() throws IOException {
        int c = getChar();
        while (c != -1) {
            addToString(c);
            if (c == 45 && peekChar() == 45) {
                c = getChar();
                addToString(c);
                if (peekChar() == 62) {
                    addToString(getChar());
                    return true;
                }
            } else {
                c = getChar();
            }
        }
        this.stringBufferTop = 0;
        this.string = null;
        this.parser.addError("msg.XML.bad.form");
        return false;
    }

    private boolean readCDATA() throws IOException {
        int c = getChar();
        while (c != -1) {
            addToString(c);
            if (c == 93 && peekChar() == 93) {
                c = getChar();
                addToString(c);
                if (peekChar() == 62) {
                    addToString(getChar());
                    return true;
                }
            } else {
                c = getChar();
            }
        }
        this.stringBufferTop = 0;
        this.string = null;
        this.parser.addError("msg.XML.bad.form");
        return false;
    }

    private boolean readEntity() throws IOException {
        int declTags = 1;
        int c = getChar();
        while (c != -1) {
            addToString(c);
            switch (c) {
                case 60:
                    declTags++;
                    break;
                case 62:
                    declTags--;
                    if (declTags != 0) {
                        break;
                    }
                    return true;
                default:
                    break;
            }
            c = getChar();
        }
        this.stringBufferTop = 0;
        this.string = null;
        this.parser.addError("msg.XML.bad.form");
        return false;
    }

    private boolean readPI() throws IOException {
        int c = getChar();
        while (c != -1) {
            addToString(c);
            if (c == 63 && peekChar() == 62) {
                addToString(getChar());
                return true;
            }
            c = getChar();
        }
        this.stringBufferTop = 0;
        this.string = null;
        this.parser.addError("msg.XML.bad.form");
        return false;
    }

    private String getStringFromBuffer() {
        this.tokenEnd = this.cursor;
        return new String(this.stringBuffer, 0, this.stringBufferTop);
    }

    private void addToString(int c) {
        int N = this.stringBufferTop;
        if (N == this.stringBuffer.length) {
            char[] tmp = new char[(this.stringBuffer.length * 2)];
            System.arraycopy(this.stringBuffer, 0, tmp, 0, N);
            this.stringBuffer = tmp;
        }
        this.stringBuffer[N] = (char) c;
        this.stringBufferTop = N + 1;
    }

    private boolean canUngetChar() {
        return this.ungetCursor == 0 || this.ungetBuffer[this.ungetCursor - 1] != 10;
    }

    private void ungetChar(int c) {
        if (this.ungetCursor != 0 && this.ungetBuffer[this.ungetCursor - 1] == 10) {
            Kit.codeBug();
        }
        int[] iArr = this.ungetBuffer;
        int i = this.ungetCursor;
        this.ungetCursor = i + 1;
        iArr[i] = c;
        this.cursor--;
    }

    private boolean matchChar(int test) throws IOException {
        int c = getCharIgnoreLineEnd();
        if (c == test) {
            this.tokenEnd = this.cursor;
            return true;
        }
        ungetCharIgnoreLineEnd(c);
        return false;
    }

    private int peekChar() throws IOException {
        int c = getChar();
        ungetChar(c);
        return c;
    }

    private int getChar() throws IOException {
        return getChar(true);
    }

    private int getChar(boolean skipFormattingChars) throws IOException {
        if (this.ungetCursor != 0) {
            this.cursor++;
            int[] iArr = this.ungetBuffer;
            int i = this.ungetCursor - 1;
            this.ungetCursor = i;
            return iArr[i];
        }
        int c;
        while (true) {
            int i2;
            if (this.sourceString != null) {
                if (this.sourceCursor == this.sourceEnd) {
                    this.hitEOF = true;
                    return -1;
                }
                this.cursor++;
                String str = this.sourceString;
                i2 = this.sourceCursor;
                this.sourceCursor = i2 + 1;
                c = str.charAt(i2);
            } else if (this.sourceCursor != this.sourceEnd || fillSourceBuffer()) {
                this.cursor++;
                char[] cArr = this.sourceBuffer;
                i2 = this.sourceCursor;
                this.sourceCursor = i2 + 1;
                c = cArr[i2];
            } else {
                this.hitEOF = true;
                return -1;
            }
            if (this.lineEndChar >= 0) {
                if (this.lineEndChar == 13 && c == 10) {
                    this.lineEndChar = 10;
                } else {
                    this.lineEndChar = -1;
                    this.lineStart = this.sourceCursor - 1;
                    this.lineno++;
                }
            }
            if (c <= 127) {
                if (c != 10 && c != 13) {
                    return c;
                }
                this.lineEndChar = c;
                return 10;
            } else if (c == 65279) {
                return c;
            } else {
                if (skipFormattingChars && isJSFormatChar(c)) {
                }
            }
        }
        if (!ScriptRuntime.isJSLineTerminator(c)) {
            return c;
        }
        this.lineEndChar = c;
        return 10;
    }

    private int getCharIgnoreLineEnd() throws IOException {
        if (this.ungetCursor != 0) {
            this.cursor++;
            int[] iArr = this.ungetBuffer;
            int i = this.ungetCursor - 1;
            this.ungetCursor = i;
            return iArr[i];
        }
        int c;
        do {
            int i2;
            if (this.sourceString != null) {
                if (this.sourceCursor == this.sourceEnd) {
                    this.hitEOF = true;
                    return -1;
                }
                this.cursor++;
                String str = this.sourceString;
                i2 = this.sourceCursor;
                this.sourceCursor = i2 + 1;
                c = str.charAt(i2);
            } else if (this.sourceCursor != this.sourceEnd || fillSourceBuffer()) {
                this.cursor++;
                char[] cArr = this.sourceBuffer;
                i2 = this.sourceCursor;
                this.sourceCursor = i2 + 1;
                c = cArr[i2];
            } else {
                this.hitEOF = true;
                return -1;
            }
            if (c <= 127) {
                if (c != 10 && c != 13) {
                    return c;
                }
                this.lineEndChar = c;
                return 10;
            } else if (c == 65279) {
                return c;
            }
        } while (isJSFormatChar(c));
        if (!ScriptRuntime.isJSLineTerminator(c)) {
            return c;
        }
        this.lineEndChar = c;
        return 10;
    }

    private void ungetCharIgnoreLineEnd(int c) {
        int[] iArr = this.ungetBuffer;
        int i = this.ungetCursor;
        this.ungetCursor = i + 1;
        iArr[i] = c;
        this.cursor--;
    }

    private void skipLine() throws IOException {
        int c;
        do {
            c = getChar();
            if (c == -1) {
                break;
            }
        } while (c != 10);
        ungetChar(c);
        this.tokenEnd = this.cursor;
    }

    /* access modifiers changed from: final */
    public final int getOffset() {
        int n = this.sourceCursor - this.lineStart;
        if (this.lineEndChar >= 0) {
            return n - 1;
        }
        return n;
    }

    private final int charAt(int index) {
        if (index < 0) {
            return -1;
        }
        if (this.sourceString == null) {
            if (index >= this.sourceEnd) {
                int oldSourceCursor = this.sourceCursor;
                try {
                    if (!fillSourceBuffer()) {
                        return -1;
                    }
                    index -= oldSourceCursor - this.sourceCursor;
                } catch (IOException e) {
                    return -1;
                }
            }
            return this.sourceBuffer[index];
        } else if (index < this.sourceEnd) {
            return this.sourceString.charAt(index);
        } else {
            return -1;
        }
    }

    private final String substring(int beginIndex, int endIndex) {
        if (this.sourceString != null) {
            return this.sourceString.substring(beginIndex, endIndex);
        }
        return new String(this.sourceBuffer, beginIndex, endIndex - beginIndex);
    }

    /* access modifiers changed from: final */
    public final String getLine() {
        int lineEnd = this.sourceCursor;
        if (this.lineEndChar >= 0) {
            lineEnd--;
            if (this.lineEndChar == 10 && charAt(lineEnd - 1) == 13) {
                lineEnd--;
            }
        } else {
            int lineLength = lineEnd - this.lineStart;
            while (true) {
                int c = charAt(this.lineStart + lineLength);
                if (c == -1 || ScriptRuntime.isJSLineTerminator(c)) {
                    lineEnd = this.lineStart + lineLength;
                } else {
                    lineLength++;
                }
            }
            lineEnd = this.lineStart + lineLength;
        }
        return substring(this.lineStart, lineEnd);
    }

    /* access modifiers changed from: final */
    public final String getLine(int position, int[] linep) {
        if (!$assertionsDisabled && (position < 0 || position > this.cursor)) {
            throw new AssertionError();
        } else if ($assertionsDisabled || linep.length == 2) {
            int delta = (this.cursor + this.ungetCursor) - position;
            int cur = this.sourceCursor;
            if (delta > cur) {
                return null;
            }
            int i;
            int end = 0;
            int lines = 0;
            while (delta > 0) {
                if ($assertionsDisabled || cur > 0) {
                    int c = charAt(cur - 1);
                    if (ScriptRuntime.isJSLineTerminator(c)) {
                        if (c == 10 && charAt(cur - 2) == 13) {
                            delta--;
                            cur--;
                        }
                        lines++;
                        end = cur - 1;
                    }
                    delta--;
                    cur--;
                } else {
                    throw new AssertionError();
                }
            }
            int start = 0;
            int offset = 0;
            while (cur > 0) {
                if (ScriptRuntime.isJSLineTerminator(charAt(cur - 1))) {
                    start = cur;
                    break;
                }
                cur--;
                offset++;
            }
            int i2 = this.lineno - lines;
            if (this.lineEndChar >= 0) {
                i = 1;
            } else {
                i = 0;
            }
            linep[0] = i + i2;
            linep[1] = offset;
            if (lines == 0) {
                return getLine();
            }
            return substring(start, end);
        } else {
            throw new AssertionError();
        }
    }

    private boolean fillSourceBuffer() throws IOException {
        if (this.sourceString != null) {
            Kit.codeBug();
        }
        if (this.sourceEnd == this.sourceBuffer.length) {
            if (this.lineStart == 0 || isMarkingComment()) {
                char[] tmp = new char[(this.sourceBuffer.length * 2)];
                System.arraycopy(this.sourceBuffer, 0, tmp, 0, this.sourceEnd);
                this.sourceBuffer = tmp;
            } else {
                System.arraycopy(this.sourceBuffer, this.lineStart, this.sourceBuffer, 0, this.sourceEnd - this.lineStart);
                this.sourceEnd -= this.lineStart;
                this.sourceCursor -= this.lineStart;
                this.lineStart = 0;
            }
        }
        int n = this.sourceReader.read(this.sourceBuffer, this.sourceEnd, this.sourceBuffer.length - this.sourceEnd);
        if (n < 0) {
            return false;
        }
        this.sourceEnd += n;
        return true;
    }

    public int getCursor() {
        return this.cursor;
    }

    public int getTokenBeg() {
        return this.tokenBeg;
    }

    public int getTokenEnd() {
        return this.tokenEnd;
    }

    public int getTokenLength() {
        return this.tokenEnd - this.tokenBeg;
    }

    public CommentType getCommentType() {
        return this.commentType;
    }

    private void markCommentStart() {
        markCommentStart("");
    }

    private void markCommentStart(String prefix) {
        if (this.parser.compilerEnv.isRecordingComments() && this.sourceReader != null) {
            this.commentPrefix = prefix;
            this.commentCursor = this.sourceCursor - 1;
        }
    }

    private boolean isMarkingComment() {
        return this.commentCursor != -1;
    }

    /* access modifiers changed from: final */
    public final String getAndResetCurrentComment() {
        if (this.sourceString != null) {
            if (isMarkingComment()) {
                Kit.codeBug();
            }
            return this.sourceString.substring(this.tokenBeg, this.tokenEnd);
        }
        if (!isMarkingComment()) {
            Kit.codeBug();
        }
        StringBuilder comment = new StringBuilder(this.commentPrefix);
        comment.append(this.sourceBuffer, this.commentCursor, getTokenLength() - this.commentPrefix.length());
        this.commentCursor = -1;
        return comment.toString();
    }

    private String convertLastCharToHex(String str) {
        int lastIndex = str.length() - 1;
        StringBuffer buf = new StringBuffer(str.substring(0, lastIndex));
        buf.append("\\u");
        String hexCode = Integer.toHexString(str.charAt(lastIndex));
        for (int i = 0; i < 4 - hexCode.length(); i++) {
            buf.append('0');
        }
        buf.append(hexCode);
        return buf.toString();
    }
}
