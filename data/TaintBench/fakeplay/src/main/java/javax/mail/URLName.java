package javax.mail;

import com.googleprojects.mm.MMMailContentUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.BitSet;
import java.util.Locale;

public class URLName {
    static final int caseDiff = 32;
    private static boolean doEncode;
    static BitSet dontNeedEncoding = new BitSet(256);
    private String file;
    protected String fullURL;
    private int hashCode;
    private String host;
    private InetAddress hostAddress;
    private boolean hostAddressKnown;
    private String password;
    private int port;
    private String protocol;
    private String ref;
    private String username;

    static {
        int i;
        boolean z = true;
        doEncode = true;
        try {
            if (Boolean.getBoolean("mail.URLName.dontencode")) {
                z = false;
            }
            doEncode = z;
        } catch (Exception e) {
        }
        for (i = 97; i <= 122; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 65; i <= 90; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 48; i <= 57; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set(32);
        dontNeedEncoding.set(45);
        dontNeedEncoding.set(95);
        dontNeedEncoding.set(46);
        dontNeedEncoding.set(42);
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0037  */
    public URLName(java.lang.String r5, java.lang.String r6, int r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) {
        /*
        r4 = this;
        r3 = -1;
        r2 = 0;
        r4.<init>();
        r4.hostAddressKnown = r2;
        r4.port = r3;
        r4.hashCode = r2;
        r4.protocol = r5;
        r4.host = r6;
        r4.port = r7;
        if (r8 == 0) goto L_0x003e;
    L_0x0013:
        r1 = 35;
        r0 = r8.indexOf(r1);
        if (r0 == r3) goto L_0x003e;
    L_0x001b:
        r1 = r8.substring(r2, r0);
        r4.file = r1;
        r1 = r0 + 1;
        r1 = r8.substring(r1);
        r4.ref = r1;
    L_0x0029:
        r1 = doEncode;
        if (r1 == 0) goto L_0x0031;
    L_0x002d:
        r9 = encode(r9);
    L_0x0031:
        r4.username = r9;
        r1 = doEncode;
        if (r1 == 0) goto L_0x003b;
    L_0x0037:
        r10 = encode(r10);
    L_0x003b:
        r4.password = r10;
        return;
    L_0x003e:
        r4.file = r8;
        r1 = 0;
        r4.ref = r1;
        goto L_0x0029;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.URLName.m422init(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public URLName(URL url) {
        this(url.toString());
    }

    public URLName(String url) {
        this.hostAddressKnown = false;
        this.port = -1;
        this.hashCode = 0;
        parseString(url);
    }

    public String toString() {
        if (this.fullURL == null) {
            StringBuffer tempURL = new StringBuffer();
            if (this.protocol != null) {
                tempURL.append(this.protocol);
                tempURL.append(":");
            }
            if (!(this.username == null && this.host == null)) {
                tempURL.append("//");
                if (this.username != null) {
                    tempURL.append(this.username);
                    if (this.password != null) {
                        tempURL.append(":");
                        tempURL.append(this.password);
                    }
                    tempURL.append("@");
                }
                if (this.host != null) {
                    tempURL.append(this.host);
                }
                if (this.port != -1) {
                    tempURL.append(":");
                    tempURL.append(Integer.toString(this.port));
                }
                if (this.file != null) {
                    tempURL.append("/");
                }
            }
            if (this.file != null) {
                tempURL.append(this.file);
            }
            if (this.ref != null) {
                tempURL.append("#");
                tempURL.append(this.ref);
            }
            this.fullURL = tempURL.toString();
        }
        return this.fullURL;
    }

    /* access modifiers changed from: protected */
    public void parseString(String url) {
        this.password = null;
        this.username = null;
        this.host = null;
        this.ref = null;
        this.file = null;
        this.protocol = null;
        this.port = -1;
        int len = url.length();
        int protocolEnd = url.indexOf(58);
        if (protocolEnd != -1) {
            this.protocol = url.substring(0, protocolEnd);
        }
        if (url.regionMatches(protocolEnd + 1, "//", 0, 2)) {
            String fullhost;
            int portindex;
            int fileStart = url.indexOf(47, protocolEnd + 3);
            if (fileStart != -1) {
                fullhost = url.substring(protocolEnd + 3, fileStart);
                if (fileStart + 1 < len) {
                    this.file = url.substring(fileStart + 1);
                } else {
                    this.file = MMMailContentUtil.MM_MESSAGE_SUBJECT;
                }
            } else {
                fullhost = url.substring(protocolEnd + 3);
            }
            int i = fullhost.indexOf(64);
            if (i != -1) {
                String fulluserpass = fullhost.substring(0, i);
                fullhost = fullhost.substring(i + 1);
                int passindex = fulluserpass.indexOf(58);
                if (passindex != -1) {
                    this.username = fulluserpass.substring(0, passindex);
                    this.password = fulluserpass.substring(passindex + 1);
                } else {
                    this.username = fulluserpass;
                }
            }
            if (fullhost.length() <= 0 || fullhost.charAt(0) != '[') {
                portindex = fullhost.indexOf(58);
            } else {
                portindex = fullhost.indexOf(58, fullhost.indexOf(93));
            }
            if (portindex != -1) {
                String portstring = fullhost.substring(portindex + 1);
                if (portstring.length() > 0) {
                    try {
                        this.port = Integer.parseInt(portstring);
                    } catch (NumberFormatException e) {
                        this.port = -1;
                    }
                }
                this.host = fullhost.substring(0, portindex);
            } else {
                this.host = fullhost;
            }
        } else if (protocolEnd + 1 < len) {
            this.file = url.substring(protocolEnd + 1);
        }
        if (this.file != null) {
            int refStart = this.file.indexOf(35);
            if (refStart != -1) {
                this.ref = this.file.substring(refStart + 1);
                this.file = this.file.substring(0, refStart);
            }
        }
    }

    public int getPort() {
        return this.port;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getFile() {
        return this.file;
    }

    public String getRef() {
        return this.ref;
    }

    public String getHost() {
        return this.host;
    }

    public String getUsername() {
        return doEncode ? decode(this.username) : this.username;
    }

    public String getPassword() {
        return doEncode ? decode(this.password) : this.password;
    }

    public URL getURL() throws MalformedURLException {
        return new URL(getProtocol(), getHost(), getPort(), getFile());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof URLName)) {
            return false;
        }
        URLName u2 = (URLName) obj;
        if (u2.protocol == null || !u2.protocol.equals(this.protocol)) {
            return false;
        }
        InetAddress a1 = getHostAddress();
        InetAddress a2 = u2.getHostAddress();
        if (a1 == null || a2 == null) {
            if (this.host == null || u2.host == null) {
                if (this.host != u2.host) {
                    return false;
                }
            } else if (!this.host.equalsIgnoreCase(u2.host)) {
                return false;
            }
        } else if (!a1.equals(a2)) {
            return false;
        }
        if (this.username != u2.username && (this.username == null || !this.username.equals(u2.username))) {
            return false;
        }
        if ((this.file == null ? MMMailContentUtil.MM_MESSAGE_SUBJECT : this.file).equals(u2.file == null ? MMMailContentUtil.MM_MESSAGE_SUBJECT : u2.file) && this.port == u2.port) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        if (this.protocol != null) {
            this.hashCode += this.protocol.hashCode();
        }
        InetAddress addr = getHostAddress();
        if (addr != null) {
            this.hashCode += addr.hashCode();
        } else if (this.host != null) {
            this.hashCode += this.host.toLowerCase(Locale.ENGLISH).hashCode();
        }
        if (this.username != null) {
            this.hashCode += this.username.hashCode();
        }
        if (this.file != null) {
            this.hashCode += this.file.hashCode();
        }
        this.hashCode += this.port;
        return this.hashCode;
    }

    private synchronized InetAddress getHostAddress() {
        InetAddress inetAddress = null;
        synchronized (this) {
            if (this.hostAddressKnown) {
                inetAddress = this.hostAddress;
            } else if (this.host != null) {
                try {
                    this.hostAddress = InetAddress.getByName(this.host);
                } catch (UnknownHostException e) {
                    this.hostAddress = null;
                }
                this.hostAddressKnown = true;
                inetAddress = this.hostAddress;
            }
        }
        return inetAddress;
    }

    static String encode(String s) {
        if (s == null) {
            return null;
        }
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i);
            if (c == 32 || !dontNeedEncoding.get(c)) {
                return _encode(s);
            }
        }
        return s;
    }

    private static String _encode(String s) {
        StringBuffer out = new StringBuffer(s.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream(10);
        OutputStreamWriter writer = new OutputStreamWriter(buf);
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i);
            if (dontNeedEncoding.get(c)) {
                if (c == 32) {
                    c = 43;
                }
                out.append((char) c);
            } else {
                try {
                    writer.write(c);
                    writer.flush();
                    byte[] ba = buf.toByteArray();
                    for (int j = 0; j < ba.length; j++) {
                        out.append('%');
                        char ch = Character.forDigit((ba[j] >> 4) & 15, 16);
                        if (Character.isLetter(ch)) {
                            ch = (char) (ch - 32);
                        }
                        out.append(ch);
                        ch = Character.forDigit(ba[j] & 15, 16);
                        if (Character.isLetter(ch)) {
                            ch = (char) (ch - 32);
                        }
                        out.append(ch);
                    }
                    buf.reset();
                } catch (IOException e) {
                    buf.reset();
                }
            }
        }
        return out.toString();
    }

    static String decode(String s) {
        if (s == null) {
            return null;
        }
        if (indexOfAny(s, "+%") == -1) {
            return s;
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            switch (c) {
                case '%':
                    try {
                        sb.append((char) Integer.parseInt(s.substring(i + 1, i + 3), 16));
                        i += 2;
                        break;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    }
                case '+':
                    sb.append(' ');
                    break;
                default:
                    sb.append(c);
                    break;
            }
            i++;
        }
        String result = sb.toString();
        try {
            result = new String(result.getBytes("8859_1"));
        } catch (UnsupportedEncodingException e2) {
        }
        return result;
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
