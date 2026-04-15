package javax.mail.internet;

import android.support.v4.view.MotionEventCompat;
import java.text.ParseException;

/* compiled from: MailDateFormat */
class MailDateParser {
    int index = 0;
    char[] orig = null;

    public MailDateParser(char[] orig) {
        this.orig = orig;
    }

    public void skipUntilNumber() throws ParseException {
        while (true) {
            try {
                switch (this.orig[this.index]) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        return;
                    default:
                        this.index++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParseException("No Number Found", this.index);
            }
        }
    }

    public void skipWhiteSpace() {
        int len = this.orig.length;
        while (this.index < len) {
            switch (this.orig[this.index]) {
                case MotionEventCompat.ACTION_HOVER_ENTER /*9*/:
                case MotionEventCompat.ACTION_HOVER_EXIT /*10*/:
                case 13:
                case ' ':
                    this.index++;
                default:
                    return;
            }
        }
    }

    public int peekChar() throws ParseException {
        if (this.index < this.orig.length) {
            return this.orig[this.index];
        }
        throw new ParseException("No more characters", this.index);
    }

    public void skipChar(char c) throws ParseException {
        if (this.index >= this.orig.length) {
            throw new ParseException("No more characters", this.index);
        } else if (this.orig[this.index] == c) {
            this.index++;
        } else {
            throw new ParseException("Wrong char", this.index);
        }
    }

    public boolean skipIfChar(char c) throws ParseException {
        if (this.index >= this.orig.length) {
            throw new ParseException("No more characters", this.index);
        } else if (this.orig[this.index] != c) {
            return false;
        } else {
            this.index++;
            return true;
        }
    }

    /* JADX WARNING: Missing block: B:11:0x0024, code skipped:
            r0 = true;
            r6.index++;
     */
    public int parseNumber() throws java.text.ParseException {
        /*
        r6 = this;
        r3 = r6.orig;
        r1 = r3.length;
        r0 = 0;
        r2 = 0;
    L_0x0005:
        r3 = r6.index;
        if (r3 < r1) goto L_0x000c;
    L_0x0009:
        if (r0 == 0) goto L_0x0061;
    L_0x000b:
        return r2;
    L_0x000c:
        r3 = r6.orig;
        r4 = r6.index;
        r3 = r3[r4];
        switch(r3) {
            case 48: goto L_0x0021;
            case 49: goto L_0x002b;
            case 50: goto L_0x0031;
            case 51: goto L_0x0037;
            case 52: goto L_0x003d;
            case 53: goto L_0x0043;
            case 54: goto L_0x0049;
            case 55: goto L_0x004f;
            case 56: goto L_0x0055;
            case 57: goto L_0x005b;
            default: goto L_0x0015;
        };
    L_0x0015:
        if (r0 != 0) goto L_0x000b;
    L_0x0017:
        r3 = new java.text.ParseException;
        r4 = "No Number found";
        r5 = r6.index;
        r3.<init>(r4, r5);
        throw r3;
    L_0x0021:
        r2 = r2 * 10;
        r0 = 1;
    L_0x0024:
        r3 = r6.index;
        r3 = r3 + 1;
        r6.index = r3;
        goto L_0x0005;
    L_0x002b:
        r3 = r2 * 10;
        r2 = r3 + 1;
        r0 = 1;
        goto L_0x0024;
    L_0x0031:
        r3 = r2 * 10;
        r2 = r3 + 2;
        r0 = 1;
        goto L_0x0024;
    L_0x0037:
        r3 = r2 * 10;
        r2 = r3 + 3;
        r0 = 1;
        goto L_0x0024;
    L_0x003d:
        r3 = r2 * 10;
        r2 = r3 + 4;
        r0 = 1;
        goto L_0x0024;
    L_0x0043:
        r3 = r2 * 10;
        r2 = r3 + 5;
        r0 = 1;
        goto L_0x0024;
    L_0x0049:
        r3 = r2 * 10;
        r2 = r3 + 6;
        r0 = 1;
        goto L_0x0024;
    L_0x004f:
        r3 = r2 * 10;
        r2 = r3 + 7;
        r0 = 1;
        goto L_0x0024;
    L_0x0055:
        r3 = r2 * 10;
        r2 = r3 + 8;
        r0 = 1;
        goto L_0x0024;
    L_0x005b:
        r3 = r2 * 10;
        r2 = r3 + 9;
        r0 = 1;
        goto L_0x0024;
    L_0x0061:
        r3 = new java.text.ParseException;
        r4 = "No Number found";
        r5 = r6.index;
        r3.<init>(r4, r5);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.MailDateParser.parseNumber():int");
    }

    public int parseMonth() throws java.text.ParseException {
        /*
        r9 = this;
        r8 = 80;
        r7 = 78;
        r6 = 67;
        r5 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        r4 = 69;
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        switch(r1) {
            case 65: goto L_0x00af;
            case 68: goto L_0x0161;
            case 70: goto L_0x005d;
            case 74: goto L_0x0021;
            case 77: goto L_0x007f;
            case 78: goto L_0x0139;
            case 79: goto L_0x0113;
            case 83: goto L_0x00f1;
            case 97: goto L_0x00af;
            case 100: goto L_0x0161;
            case 102: goto L_0x005d;
            case 106: goto L_0x0021;
            case 109: goto L_0x007f;
            case 110: goto L_0x0139;
            case 111: goto L_0x0113;
            case 115: goto L_0x00f1;
            default: goto L_0x0017;
        };
    L_0x0017:
        r1 = new java.text.ParseException;
        r2 = "Bad Month";
        r3 = r9.index;
        r1.<init>(r2, r3);
        throw r1;
    L_0x0021:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        switch(r1) {
            case 65: goto L_0x002f;
            case 85: goto L_0x0041;
            case 97: goto L_0x002f;
            case 117: goto L_0x0041;
            default: goto L_0x002e;
        };	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
    L_0x002e:
        goto L_0x0017;
    L_0x002f:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r7) goto L_0x003f;
    L_0x003b:
        r1 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x003f:
        r1 = 0;
    L_0x0040:
        return r1;
    L_0x0041:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r7) goto L_0x0051;
    L_0x004d:
        r1 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r0 != r1) goto L_0x0053;
    L_0x0051:
        r1 = 5;
        goto L_0x0040;
    L_0x0053:
        r1 = 76;
        if (r0 == r1) goto L_0x005b;
    L_0x0057:
        r1 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x005b:
        r1 = 6;
        goto L_0x0040;
    L_0x005d:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r4) goto L_0x006b;
    L_0x0069:
        if (r0 != r5) goto L_0x0017;
    L_0x006b:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = 66;
        if (r0 == r1) goto L_0x007d;
    L_0x0079:
        r1 = 98;
        if (r0 != r1) goto L_0x0017;
    L_0x007d:
        r1 = 1;
        goto L_0x0040;
    L_0x007f:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = 65;
        if (r0 == r1) goto L_0x0091;
    L_0x008d:
        r1 = 97;
        if (r0 != r1) goto L_0x0017;
    L_0x0091:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = 82;
        if (r0 == r1) goto L_0x00a3;
    L_0x009f:
        r1 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        if (r0 != r1) goto L_0x00a5;
    L_0x00a3:
        r1 = 2;
        goto L_0x0040;
    L_0x00a5:
        r1 = 89;
        if (r0 == r1) goto L_0x00ad;
    L_0x00a9:
        r1 = 121; // 0x79 float:1.7E-43 double:6.0E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x00ad:
        r1 = 4;
        goto L_0x0040;
    L_0x00af:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r8) goto L_0x00bf;
    L_0x00bb:
        r1 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        if (r0 != r1) goto L_0x00d4;
    L_0x00bf:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = 82;
        if (r0 == r1) goto L_0x00d1;
    L_0x00cd:
        r1 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x00d1:
        r1 = 3;
        goto L_0x0040;
    L_0x00d4:
        r1 = 85;
        if (r0 == r1) goto L_0x00dc;
    L_0x00d8:
        r1 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x00dc:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = 71;
        if (r0 == r1) goto L_0x00ee;
    L_0x00ea:
        r1 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x00ee:
        r1 = 7;
        goto L_0x0040;
    L_0x00f1:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r4) goto L_0x00ff;
    L_0x00fd:
        if (r0 != r5) goto L_0x0017;
    L_0x00ff:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r8) goto L_0x010f;
    L_0x010b:
        r1 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x010f:
        r1 = 8;
        goto L_0x0040;
    L_0x0113:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r6) goto L_0x0123;
    L_0x011f:
        r1 = 99;
        if (r0 != r1) goto L_0x0017;
    L_0x0123:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = 84;
        if (r0 == r1) goto L_0x0135;
    L_0x0131:
        r1 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x0135:
        r1 = 9;
        goto L_0x0040;
    L_0x0139:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = 79;
        if (r0 == r1) goto L_0x014b;
    L_0x0147:
        r1 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x014b:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r1 = 86;
        if (r0 == r1) goto L_0x015d;
    L_0x0159:
        r1 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        if (r0 != r1) goto L_0x0017;
    L_0x015d:
        r1 = 10;
        goto L_0x0040;
    L_0x0161:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r4) goto L_0x016f;
    L_0x016d:
        if (r0 != r5) goto L_0x0017;
    L_0x016f:
        r1 = r9.orig;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r2 = r9.index;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r3 = r2 + 1;
        r9.index = r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        r0 = r1[r2];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0183 }
        if (r0 == r6) goto L_0x017f;
    L_0x017b:
        r1 = 99;
        if (r0 != r1) goto L_0x0017;
    L_0x017f:
        r1 = 11;
        goto L_0x0040;
    L_0x0183:
        r1 = move-exception;
        goto L_0x0017;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.MailDateParser.parseMonth():int");
    }

    public int parseTimeZone() throws ParseException {
        if (this.index >= this.orig.length) {
            throw new ParseException("No more characters", this.index);
        }
        char test = this.orig[this.index];
        if (test == '+' || test == '-') {
            return parseNumericTimeZone();
        }
        return parseAlphaTimeZone();
    }

    public int parseNumericTimeZone() throws ParseException {
        boolean switchSign = false;
        char[] cArr = this.orig;
        int i = this.index;
        this.index = i + 1;
        char first = cArr[i];
        if (first == '+') {
            switchSign = true;
        } else if (first != '-') {
            throw new ParseException("Bad Numeric TimeZone", this.index);
        }
        int tz = parseNumber();
        int offset = ((tz / 100) * 60) + (tz % 100);
        if (switchSign) {
            return -offset;
        }
        return offset;
    }

    public int parseAlphaTimeZone() throws ParseException {
        boolean foundCommon = false;
        try {
            int result;
            char curr;
            char[] cArr = this.orig;
            int i = this.index;
            this.index = i + 1;
            switch (cArr[i]) {
                case 'C':
                case 'c':
                    result = 360;
                    foundCommon = true;
                    break;
                case 'E':
                case 'e':
                    result = 300;
                    foundCommon = true;
                    break;
                case 'G':
                case 'g':
                    cArr = this.orig;
                    i = this.index;
                    this.index = i + 1;
                    curr = cArr[i];
                    if (curr == 'M' || curr == 'm') {
                        cArr = this.orig;
                        i = this.index;
                        this.index = i + 1;
                        curr = cArr[i];
                        if (curr == 'T' || curr == 't') {
                            result = 0;
                            break;
                        }
                    }
                    throw new ParseException("Bad Alpha TimeZone", this.index);
                case 'M':
                case 'm':
                    result = 420;
                    foundCommon = true;
                    break;
                case 'P':
                case 'p':
                    result = 480;
                    foundCommon = true;
                    break;
                case 'U':
                case 'u':
                    cArr = this.orig;
                    i = this.index;
                    this.index = i + 1;
                    curr = cArr[i];
                    if (curr == 'T' || curr == 't') {
                        result = 0;
                        break;
                    }
                    throw new ParseException("Bad Alpha TimeZone", this.index);
                    break;
                default:
                    throw new ParseException("Bad Alpha TimeZone", this.index);
            }
            if (!foundCommon) {
                return result;
            }
            cArr = this.orig;
            i = this.index;
            this.index = i + 1;
            curr = cArr[i];
            if (curr == 'S' || curr == 's') {
                cArr = this.orig;
                i = this.index;
                this.index = i + 1;
                curr = cArr[i];
                if (curr == 'T' || curr == 't') {
                    return result;
                }
                throw new ParseException("Bad Alpha TimeZone", this.index);
            } else if (curr != 'D' && curr != 'd') {
                return result;
            } else {
                cArr = this.orig;
                i = this.index;
                this.index = i + 1;
                curr = cArr[i];
                if (curr == 'T' || curr != 't') {
                    return result - 60;
                }
                throw new ParseException("Bad Alpha TimeZone", this.index);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParseException("Bad Alpha TimeZone", this.index);
        }
    }

    /* access modifiers changed from: 0000 */
    public int getIndex() {
        return this.index;
    }
}
