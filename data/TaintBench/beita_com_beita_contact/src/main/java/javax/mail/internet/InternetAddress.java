package javax.mail.internet;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import javax.mail.Address;
import javax.mail.Session;

public class InternetAddress extends Address implements Cloneable {
    private static final String rfc822phrase = HeaderTokenizer.RFC822.replace(' ', 0).replace(9, 0);
    private static final long serialVersionUID = -7507595530758302903L;
    private static final String specialsNoDot = "()<>,;:\\\"[]@";
    private static final String specialsNoDotNoAt = "()<>,;:\\\"[]";
    protected String address;
    protected String encodedPersonal;
    protected String personal;

    public InternetAddress(String address) throws AddressException {
        InternetAddress[] a = parse(address, true);
        if (a.length != 1) {
            throw new AddressException("Illegal address", address);
        }
        this.address = a[0].address;
        this.personal = a[0].personal;
        this.encodedPersonal = a[0].encodedPersonal;
    }

    public InternetAddress(String address, boolean strict) throws AddressException {
        this(address);
        if (strict) {
            checkAddress(this.address, true, true);
        }
    }

    public InternetAddress(String address, String personal) throws UnsupportedEncodingException {
        this(address, personal, null);
    }

    public InternetAddress(String address, String personal, String charset) throws UnsupportedEncodingException {
        this.address = address;
        setPersonal(personal, charset);
    }

    public Object clone() {
        InternetAddress a = null;
        try {
            return (InternetAddress) super.clone();
        } catch (CloneNotSupportedException e) {
            return a;
        }
    }

    public String getType() {
        return "rfc822";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPersonal(String name, String charset) throws UnsupportedEncodingException {
        this.personal = name;
        if (name != null) {
            this.encodedPersonal = MimeUtility.encodeWord(name, charset, null);
        } else {
            this.encodedPersonal = null;
        }
    }

    public void setPersonal(String name) throws UnsupportedEncodingException {
        this.personal = name;
        if (name != null) {
            this.encodedPersonal = MimeUtility.encodeWord(name);
        } else {
            this.encodedPersonal = null;
        }
    }

    public String getAddress() {
        return this.address;
    }

    public String getPersonal() {
        if (this.personal != null) {
            return this.personal;
        }
        if (this.encodedPersonal == null) {
            return null;
        }
        try {
            this.personal = MimeUtility.decodeText(this.encodedPersonal);
            return this.personal;
        } catch (Exception e) {
            Exception ex = e;
            return this.encodedPersonal;
        }
    }

    public String toString() {
        if (this.encodedPersonal == null && this.personal != null) {
            try {
                this.encodedPersonal = MimeUtility.encodeWord(this.personal);
            } catch (UnsupportedEncodingException e) {
            }
        }
        if (this.encodedPersonal != null) {
            return new StringBuilder(String.valueOf(quotePhrase(this.encodedPersonal))).append(" <").append(this.address).append(">").toString();
        }
        if (isGroup() || isSimple()) {
            return this.address;
        }
        return "<" + this.address + ">";
    }

    public String toUnicodeString() {
        String p = getPersonal();
        if (p != null) {
            return quotePhrase(p) + " <" + this.address + ">";
        }
        if (isGroup() || isSimple()) {
            return this.address;
        }
        return "<" + this.address + ">";
    }

    private static String quotePhrase(String phrase) {
        StringBuffer sb;
        int len = phrase.length();
        boolean needQuoting = false;
        for (int i = 0; i < len; i++) {
            char c = phrase.charAt(i);
            if (c == '\"' || c == '\\') {
                sb = new StringBuffer(len + 3);
                sb.append('\"');
                for (int j = 0; j < len; j++) {
                    char cc = phrase.charAt(j);
                    if (cc == '\"' || cc == '\\') {
                        sb.append('\\');
                    }
                    sb.append(cc);
                }
                sb.append('\"');
                return sb.toString();
            }
            if ((c < ' ' && c != 13 && c != 10 && c != 9) || c >= 127 || rfc822phrase.indexOf(c) >= 0) {
                needQuoting = true;
            }
        }
        if (!needQuoting) {
            return phrase;
        }
        sb = new StringBuffer(len + 2);
        sb.append('\"').append(phrase).append('\"');
        return sb.toString();
    }

    private static String unquote(String s) {
        if (!s.startsWith("\"") || !s.endsWith("\"")) {
            return s;
        }
        s = s.substring(1, s.length() - 1);
        if (s.indexOf(92) < 0) {
            return s;
        }
        StringBuffer sb = new StringBuffer(s.length());
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == '\\' && i < s.length() - 1) {
                i++;
                c = s.charAt(i);
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    public boolean equals(Object a) {
        if (!(a instanceof InternetAddress)) {
            return false;
        }
        String s = ((InternetAddress) a).getAddress();
        if (s == this.address) {
            return true;
        }
        if (this.address == null || !this.address.equalsIgnoreCase(s)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.address == null) {
            return 0;
        }
        return this.address.toLowerCase(Locale.ENGLISH).hashCode();
    }

    public static String toString(Address[] addresses) {
        return toString(addresses, 0);
    }

    public static String toString(Address[] addresses, int used) {
        if (addresses == null || addresses.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < addresses.length; i++) {
            if (i != 0) {
                sb.append(", ");
                used += 2;
            }
            String s = addresses[i].toString();
            if (used + lengthOfFirstSegment(s) > 76) {
                sb.append("\r\n\t");
                used = 8;
            }
            sb.append(s);
            used = lengthOfLastSegment(s, used);
        }
        return sb.toString();
    }

    private static int lengthOfFirstSegment(String s) {
        int pos = s.indexOf("\r\n");
        if (pos != -1) {
            return pos;
        }
        return s.length();
    }

    private static int lengthOfLastSegment(String s, int used) {
        int pos = s.lastIndexOf("\r\n");
        if (pos != -1) {
            return (s.length() - pos) - 2;
        }
        return s.length() + used;
    }

    public static InternetAddress getLocalAddress(Session session) {
        String user = null;
        String host = null;
        String address = null;
        if (session == null) {
            try {
                user = System.getProperty("user.name");
                host = InetAddress.getLocalHost().getHostName();
            } catch (SecurityException | UnknownHostException | AddressException e) {
            }
        } else {
            address = session.getProperty("mail.from");
            if (address == null) {
                user = session.getProperty("mail.user");
                if (user == null || user.length() == 0) {
                    user = session.getProperty("user.name");
                }
                if (user == null || user.length() == 0) {
                    user = System.getProperty("user.name");
                }
                host = session.getProperty("mail.host");
                if (host == null || host.length() == 0) {
                    InetAddress me = InetAddress.getLocalHost();
                    if (me != null) {
                        host = me.getHostName();
                    }
                }
            }
        }
        if (!(address != null || user == null || user.length() == 0 || host == null || host.length() == 0)) {
            address = new StringBuilder(String.valueOf(user)).append("@").append(host).toString();
        }
        if (address != null) {
            return new InternetAddress(address);
        }
        return null;
    }

    public static InternetAddress[] parse(String addresslist) throws AddressException {
        return parse(addresslist, true);
    }

    public static InternetAddress[] parse(String addresslist, boolean strict) throws AddressException {
        return parse(addresslist, strict, false);
    }

    public static InternetAddress[] parseHeader(String addresslist, boolean strict) throws AddressException {
        return parse(addresslist, strict, true);
    }

    /* JADX WARNING: Missing block: B:67:0x0147, code skipped:
            r5 = r7;
            r7 = r9;
     */
    /* JADX WARNING: Missing block: B:68:0x0149, code skipped:
            r9 = r7;
            r7 = r5 + 1;
     */
    /* JADX WARNING: Missing block: B:90:0x01b6, code skipped:
            r5 = r5 + 1;
     */
    /* JADX WARNING: Missing block: B:99:0x01e1, code skipped:
            r5 = r5 + 1;
     */
    private static javax.mail.internet.InternetAddress[] parse(java.lang.String r19, boolean r20, boolean r21) throws javax.mail.internet.AddressException {
        /*
        r12 = -1;
        r5 = -1;
        r10 = r19.length();
        r6 = 0;
        r9 = 0;
        r8 = 0;
        r15 = new java.util.Vector;
        r15.<init>();
        r4 = -1;
        r11 = r4;
        r7 = 0;
        r14 = r12;
        r13 = r11;
        r11 = r8;
        r12 = r9;
        r8 = r6;
        r9 = r7;
        r6 = r4;
        r7 = r5;
    L_0x0019:
        if (r9 < r10) goto L_0x007d;
    L_0x001b:
        if (r13 < 0) goto L_0x0337;
    L_0x001d:
        r4 = -1;
        if (r6 != r4) goto L_0x0334;
    L_0x0020:
        r4 = r9;
        r5 = r4;
    L_0x0022:
        r0 = r19;
        r1 = r13;
        r2 = r5;
        r4 = r0.substring(r1, r2);
        r4 = r4.trim();
        if (r11 != 0) goto L_0x0034;
    L_0x0030:
        if (r20 != 0) goto L_0x0034;
    L_0x0032:
        if (r21 == 0) goto L_0x0300;
    L_0x0034:
        if (r20 != 0) goto L_0x0038;
    L_0x0036:
        if (r21 != 0) goto L_0x0041;
    L_0x0038:
        r20 = 0;
        r0 = r4;
        r1 = r12;
        r2 = r20;
        checkAddress(r0, r1, r2);
    L_0x0041:
        r20 = new javax.mail.internet.InternetAddress;
        r20.m269init();
        r0 = r20;
        r1 = r4;
        r0.setAddress(r1);
        if (r14 < 0) goto L_0x0064;
    L_0x004e:
        r0 = r19;
        r1 = r14;
        r2 = r7;
        r19 = r0.substring(r1, r2);
        r19 = r19.trim();
        r19 = unquote(r19);
        r0 = r19;
        r1 = r20;
        r1.encodedPersonal = r0;
    L_0x0064:
        r0 = r15;
        r1 = r20;
        r0.addElement(r1);
        r19 = r5;
    L_0x006c:
        r19 = r15.size();
        r0 = r19;
        r0 = new javax.mail.internet.InternetAddress[r0];
        r19 = r0;
        r0 = r15;
        r1 = r19;
        r0.copyInto(r1);
        return r19;
    L_0x007d:
        r0 = r19;
        r1 = r9;
        r5 = r0.charAt(r1);
        switch(r5) {
            case 9: goto L_0x02f5;
            case 10: goto L_0x02f5;
            case 13: goto L_0x02f5;
            case 32: goto L_0x02f5;
            case 34: goto L_0x018c;
            case 40: goto L_0x00a3;
            case 41: goto L_0x00f6;
            case 44: goto L_0x01e7;
            case 58: goto L_0x0283;
            case 59: goto L_0x02ab;
            case 60: goto L_0x0105;
            case 62: goto L_0x017d;
            case 91: goto L_0x01bc;
            default: goto L_0x0087;
        };
    L_0x0087:
        r4 = -1;
        if (r13 != r4) goto L_0x033b;
    L_0x008a:
        r4 = r9;
        r13 = r14;
        r16 = r11;
        r11 = r12;
        r12 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r16;
    L_0x0097:
        r4 = r8 + 1;
        r8 = r7;
        r14 = r13;
        r7 = r6;
        r13 = r12;
        r12 = r11;
        r6 = r5;
        r11 = r9;
        r9 = r4;
        goto L_0x0019;
    L_0x00a3:
        r11 = 1;
        if (r13 < 0) goto L_0x00ab;
    L_0x00a6:
        r4 = -1;
        if (r6 != r4) goto L_0x00ab;
    L_0x00a9:
        r4 = r9;
        r6 = r4;
    L_0x00ab:
        r4 = -1;
        if (r14 != r4) goto L_0x00b1;
    L_0x00ae:
        r4 = r9 + 1;
        r14 = r4;
    L_0x00b1:
        r4 = r9 + 1;
        r9 = 1;
        r16 = r5;
        r5 = r4;
        r4 = r16;
    L_0x00b9:
        if (r5 >= r10) goto L_0x00bd;
    L_0x00bb:
        if (r9 > 0) goto L_0x00ce;
    L_0x00bd:
        if (r9 <= 0) goto L_0x00e4;
    L_0x00bf:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Missing ')'";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r5;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x00ce:
        r0 = r19;
        r1 = r5;
        r4 = r0.charAt(r1);
        switch(r4) {
            case 40: goto L_0x00de;
            case 41: goto L_0x00e1;
            case 92: goto L_0x00db;
            default: goto L_0x00d8;
        };
    L_0x00d8:
        r5 = r5 + 1;
        goto L_0x00b9;
    L_0x00db:
        r5 = r5 + 1;
        goto L_0x00d8;
    L_0x00de:
        r9 = r9 + 1;
        goto L_0x00d8;
    L_0x00e1:
        r9 = r9 + -1;
        goto L_0x00d8;
    L_0x00e4:
        r9 = r5 + -1;
        r5 = -1;
        if (r7 != r5) goto L_0x0376;
    L_0x00e9:
        r5 = r9;
        r7 = r8;
        r8 = r9;
        r9 = r11;
        r11 = r12;
        r12 = r13;
        r13 = r14;
        r16 = r6;
        r6 = r5;
        r5 = r16;
        goto L_0x0097;
    L_0x00f6:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Missing '('";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r9;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x0105:
        r11 = 1;
        if (r12 == 0) goto L_0x0117;
    L_0x0108:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Extra route-addr";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r9;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x0117:
        if (r8 != 0) goto L_0x0371;
    L_0x0119:
        r12 = r13;
        if (r12 < 0) goto L_0x036e;
    L_0x011c:
        r4 = r9;
    L_0x011d:
        r6 = r9 + 1;
        r13 = r12;
        r12 = r6;
        r6 = r4;
    L_0x0122:
        r7 = 0;
        r4 = r9 + 1;
        r9 = r7;
        r7 = r4;
        r4 = r5;
    L_0x0128:
        if (r7 < r10) goto L_0x013d;
    L_0x012a:
        if (r7 < r10) goto L_0x016f;
    L_0x012c:
        if (r9 == 0) goto L_0x0160;
    L_0x012e:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Missing '\"'";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r7;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x013d:
        r0 = r19;
        r1 = r7;
        r4 = r0.charAt(r1);
        switch(r4) {
            case 34: goto L_0x0152;
            case 62: goto L_0x015d;
            case 92: goto L_0x014e;
            default: goto L_0x0147;
        };
    L_0x0147:
        r5 = r7;
        r7 = r9;
    L_0x0149:
        r5 = r5 + 1;
        r9 = r7;
        r7 = r5;
        goto L_0x0128;
    L_0x014e:
        r5 = r7 + 1;
        r7 = r9;
        goto L_0x0149;
    L_0x0152:
        if (r9 == 0) goto L_0x015b;
    L_0x0154:
        r5 = 0;
    L_0x0155:
        r16 = r5;
        r5 = r7;
        r7 = r16;
        goto L_0x0149;
    L_0x015b:
        r5 = 1;
        goto L_0x0155;
    L_0x015d:
        if (r9 == 0) goto L_0x012a;
    L_0x015f:
        goto L_0x0147;
    L_0x0160:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Missing '>'";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r7;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x016f:
        r9 = 1;
        r5 = r7;
        r16 = r11;
        r11 = r9;
        r9 = r16;
        r17 = r8;
        r8 = r7;
        r7 = r17;
        goto L_0x0097;
    L_0x017d:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Missing '<'";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r9;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x018c:
        r11 = 1;
        r4 = -1;
        if (r13 != r4) goto L_0x0192;
    L_0x0190:
        r4 = r9;
        r13 = r4;
    L_0x0192:
        r4 = r9 + 1;
        r16 = r5;
        r5 = r4;
        r4 = r16;
    L_0x0199:
        if (r5 < r10) goto L_0x01ac;
    L_0x019b:
        if (r5 < r10) goto L_0x0361;
    L_0x019d:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Missing '\"'";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r5;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x01ac:
        r0 = r19;
        r1 = r5;
        r4 = r0.charAt(r1);
        switch(r4) {
            case 34: goto L_0x019b;
            case 92: goto L_0x01b9;
            default: goto L_0x01b6;
        };
    L_0x01b6:
        r5 = r5 + 1;
        goto L_0x0199;
    L_0x01b9:
        r5 = r5 + 1;
        goto L_0x01b6;
    L_0x01bc:
        r11 = 1;
        r4 = r9 + 1;
        r16 = r5;
        r5 = r4;
        r4 = r16;
    L_0x01c4:
        if (r5 < r10) goto L_0x01d7;
    L_0x01c6:
        if (r5 < r10) goto L_0x0361;
    L_0x01c8:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Missing ']'";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r5;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x01d7:
        r0 = r19;
        r1 = r5;
        r4 = r0.charAt(r1);
        switch(r4) {
            case 92: goto L_0x01e4;
            case 93: goto L_0x01c6;
            default: goto L_0x01e1;
        };
    L_0x01e1:
        r5 = r5 + 1;
        goto L_0x01c4;
    L_0x01e4:
        r5 = r5 + 1;
        goto L_0x01e1;
    L_0x01e7:
        r4 = -1;
        if (r13 != r4) goto L_0x01fd;
    L_0x01ea:
        r11 = 0;
        r6 = 0;
        r4 = -1;
        r12 = r4;
        r13 = r14;
        r16 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r16;
        r17 = r5;
        r5 = r4;
        r4 = r17;
        goto L_0x0097;
    L_0x01fd:
        if (r8 == 0) goto L_0x020e;
    L_0x01ff:
        r4 = 0;
        r12 = r13;
        r13 = r14;
        r16 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r11;
        r11 = r16;
        goto L_0x0097;
    L_0x020e:
        r4 = -1;
        if (r6 != r4) goto L_0x035e;
    L_0x0211:
        r4 = r9;
    L_0x0212:
        r0 = r19;
        r1 = r13;
        r2 = r4;
        r4 = r0.substring(r1, r2);
        r4 = r4.trim();
        if (r11 != 0) goto L_0x0224;
    L_0x0220:
        if (r20 != 0) goto L_0x0224;
    L_0x0222:
        if (r21 == 0) goto L_0x0260;
    L_0x0224:
        if (r20 != 0) goto L_0x0228;
    L_0x0226:
        if (r21 != 0) goto L_0x022c;
    L_0x0228:
        r6 = 0;
        checkAddress(r4, r12, r6);
    L_0x022c:
        r6 = new javax.mail.internet.InternetAddress;
        r6.m269init();
        r6.setAddress(r4);
        if (r14 < 0) goto L_0x035a;
    L_0x0236:
        r0 = r19;
        r1 = r14;
        r2 = r7;
        r4 = r0.substring(r1, r2);
        r4 = r4.trim();
        r4 = unquote(r4);
        r6.encodedPersonal = r4;
        r4 = -1;
        r7 = r4;
    L_0x024a:
        r15.addElement(r6);
        r6 = r4;
        r13 = r7;
    L_0x024f:
        r11 = 0;
        r7 = 0;
        r4 = -1;
        r12 = r4;
        r16 = r5;
        r5 = r4;
        r4 = r16;
        r17 = r8;
        r8 = r9;
        r9 = r7;
        r7 = r17;
        goto L_0x0097;
    L_0x0260:
        r11 = new java.util.StringTokenizer;
        r11.<init>(r4);
    L_0x0265:
        r4 = r11.hasMoreTokens();
        if (r4 != 0) goto L_0x026e;
    L_0x026b:
        r6 = r7;
        r13 = r14;
        goto L_0x024f;
    L_0x026e:
        r4 = r11.nextToken();
        r6 = 0;
        r12 = 0;
        checkAddress(r4, r6, r12);
        r6 = new javax.mail.internet.InternetAddress;
        r6.m269init();
        r6.setAddress(r4);
        r15.addElement(r6);
        goto L_0x0265;
    L_0x0283:
        r11 = 1;
        if (r8 == 0) goto L_0x0295;
    L_0x0286:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Nested group";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r9;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x0295:
        r4 = 1;
        r8 = -1;
        if (r13 != r8) goto L_0x034c;
    L_0x0299:
        r8 = r9;
        r13 = r14;
        r16 = r11;
        r11 = r12;
        r12 = r8;
        r8 = r9;
        r9 = r16;
        r17 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r4;
        r4 = r17;
        goto L_0x0097;
    L_0x02ab:
        r4 = -1;
        if (r13 != r4) goto L_0x0349;
    L_0x02ae:
        r4 = r9;
    L_0x02af:
        if (r8 != 0) goto L_0x02c0;
    L_0x02b1:
        r20 = new javax.mail.internet.AddressException;
        r21 = "Illegal semicolon, not in group";
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r3 = r9;
        r0.m483init(r1, r2, r3);
        throw r20;
    L_0x02c0:
        r6 = 0;
        r8 = -1;
        if (r4 != r8) goto L_0x0346;
    L_0x02c4:
        r4 = r9;
        r12 = r4;
    L_0x02c6:
        r8 = new javax.mail.internet.InternetAddress;
        r8.m269init();
        r4 = r9 + 1;
        r0 = r19;
        r1 = r12;
        r2 = r4;
        r4 = r0.substring(r1, r2);
        r4 = r4.trim();
        r8.setAddress(r4);
        r15.addElement(r8);
        r8 = 0;
        r4 = -1;
        r12 = r4;
        r13 = r14;
        r16 = r11;
        r11 = r8;
        r8 = r9;
        r9 = r16;
        r17 = r7;
        r7 = r6;
        r6 = r17;
        r18 = r5;
        r5 = r4;
        r4 = r18;
        goto L_0x0097;
    L_0x02f5:
        r4 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r11;
        r11 = r12;
        r12 = r13;
        r13 = r14;
        goto L_0x0097;
    L_0x0300:
        r21 = new java.util.StringTokenizer;
        r0 = r21;
        r1 = r4;
        r0.<init>(r1);
    L_0x0308:
        r19 = r21.hasMoreTokens();
        if (r19 != 0) goto L_0x0312;
    L_0x030e:
        r19 = r5;
        goto L_0x006c;
    L_0x0312:
        r19 = r21.nextToken();
        r20 = 0;
        r4 = 0;
        r0 = r19;
        r1 = r20;
        r2 = r4;
        checkAddress(r0, r1, r2);
        r20 = new javax.mail.internet.InternetAddress;
        r20.m269init();
        r0 = r20;
        r1 = r19;
        r0.setAddress(r1);
        r0 = r15;
        r1 = r20;
        r0.addElement(r1);
        goto L_0x0308;
    L_0x0334:
        r5 = r6;
        goto L_0x0022;
    L_0x0337:
        r19 = r6;
        goto L_0x006c;
    L_0x033b:
        r4 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r11;
        r11 = r12;
        r12 = r13;
        r13 = r14;
        goto L_0x0097;
    L_0x0346:
        r12 = r4;
        goto L_0x02c6;
    L_0x0349:
        r4 = r13;
        goto L_0x02af;
    L_0x034c:
        r8 = r9;
        r9 = r11;
        r11 = r12;
        r12 = r13;
        r13 = r14;
        r16 = r7;
        r7 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r16;
        goto L_0x0097;
    L_0x035a:
        r4 = r7;
        r7 = r14;
        goto L_0x024a;
    L_0x035e:
        r4 = r6;
        goto L_0x0212;
    L_0x0361:
        r9 = r11;
        r11 = r12;
        r12 = r13;
        r13 = r14;
        r16 = r7;
        r7 = r8;
        r8 = r5;
        r5 = r6;
        r6 = r16;
        goto L_0x0097;
    L_0x036e:
        r4 = r7;
        goto L_0x011d;
    L_0x0371:
        r6 = r7;
        r12 = r13;
        r13 = r14;
        goto L_0x0122;
    L_0x0376:
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r11;
        r11 = r12;
        r12 = r13;
        r13 = r14;
        goto L_0x0097;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.InternetAddress.parse(java.lang.String, boolean, boolean):javax.mail.internet.InternetAddress[]");
    }

    public void validate() throws AddressException {
        checkAddress(getAddress(), true, true);
    }

    private static void checkAddress(String addr, boolean routeAddr, boolean validate) throws AddressException {
        int start = 0;
        if (addr.indexOf(34) < 0) {
            int i;
            String local;
            String domain;
            if (routeAddr) {
                start = 0;
                while (true) {
                    i = indexOfAny(addr, ",:", start);
                    if (i < 0) {
                        break;
                    } else if (addr.charAt(start) != '@') {
                        throw new AddressException("Illegal route-addr", addr);
                    } else if (addr.charAt(i) == ':') {
                        start = i + 1;
                        break;
                    } else {
                        start = i + 1;
                    }
                }
            }
            i = addr.indexOf(64, start);
            if (i >= 0) {
                if (i == start) {
                    throw new AddressException("Missing local name", addr);
                } else if (i == addr.length() - 1) {
                    throw new AddressException("Missing domain", addr);
                } else {
                    local = addr.substring(start, i);
                    domain = addr.substring(i + 1);
                }
            } else if (validate) {
                throw new AddressException("Missing final '@domain'", addr);
            } else {
                local = addr;
                domain = null;
            }
            if (indexOfAny(addr, " \t\n\r") >= 0) {
                throw new AddressException("Illegal whitespace in address", addr);
            } else if (indexOfAny(local, specialsNoDot) >= 0) {
                throw new AddressException("Illegal character in local name", addr);
            } else if (domain != null && domain.indexOf(91) < 0 && indexOfAny(domain, specialsNoDot) >= 0) {
                throw new AddressException("Illegal character in domain", addr);
            }
        }
    }

    private boolean isSimple() {
        return this.address == null || indexOfAny(this.address, specialsNoDotNoAt) < 0;
    }

    public boolean isGroup() {
        return this.address != null && this.address.endsWith(";") && this.address.indexOf(58) > 0;
    }

    public InternetAddress[] getGroup(boolean strict) throws AddressException {
        String addr = getAddress();
        if (!addr.endsWith(";")) {
            return null;
        }
        int ix = addr.indexOf(58);
        if (ix < 0) {
            return null;
        }
        return parseHeader(addr.substring(ix + 1, addr.length() - 1), strict);
    }

    private static int indexOfAny(String s, String any) {
        return indexOfAny(s, any, 0);
    }

    private static int indexOfAny(String s, String any, int start) {
        try {
            int len = s.length();
            for (int i = start; i < len; i++) {
                if (any.indexOf(s.charAt(i)) >= 0) {
                    return i;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException e) {
            return -1;
        }
    }
}
