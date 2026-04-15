package com.sun.mail.smtp;

import android.support.v4.view.MotionEventCompat;
import com.googleprojects.mm.MMMailContentUtil;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.SocketFetcher;
import com.sun.mail.util.TraceInputStream;
import com.sun.mail.util.TraceOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.ParseException;

public class SMTPTransport extends Transport {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final byte[] CRLF = new byte[]{(byte) 13, (byte) 10};
    private static final String UNKNOWN = "UNKNOWN";
    private static char[] hexchar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String[] ignoreList = new String[]{"Bcc", "Content-Length"};
    private Address[] addresses;
    private SMTPOutputStream dataStream;
    private int defaultPort;
    private MessagingException exception;
    private Hashtable extMap;
    private Address[] invalidAddr;
    private boolean isSSL;
    private int lastReturnCode;
    private String lastServerResponse;
    private LineInputStream lineInputStream;
    private String localHostName;
    private DigestMD5 md5support;
    private MimeMessage message;
    private String name;
    private PrintStream out;
    private boolean quitWait;
    private boolean reportSuccess;
    private String saslRealm;
    private boolean sendPartiallyFailed;
    private BufferedInputStream serverInput;
    private OutputStream serverOutput;
    private Socket serverSocket;
    private boolean useRset;
    private boolean useStartTLS;
    private Address[] validSentAddr;
    private Address[] validUnsentAddr;

    static {
        boolean z;
        if (SMTPTransport.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public SMTPTransport(Session session, URLName urlname) {
        this(session, urlname, "smtp", 25, false);
    }

    protected SMTPTransport(Session session, URLName urlname, String name, int defaultPort, boolean isSSL) {
        boolean z = true;
        super(session, urlname);
        this.name = "smtp";
        this.defaultPort = 25;
        this.isSSL = false;
        this.sendPartiallyFailed = false;
        this.quitWait = false;
        this.saslRealm = UNKNOWN;
        if (urlname != null) {
            name = urlname.getProtocol();
        }
        this.name = name;
        this.defaultPort = defaultPort;
        this.isSSL = isSSL;
        this.out = session.getDebugOut();
        String s = session.getProperty("mail." + name + ".quitwait");
        boolean z2 = s == null || s.equalsIgnoreCase("true");
        this.quitWait = z2;
        s = session.getProperty("mail." + name + ".reportsuccess");
        if (s == null || !s.equalsIgnoreCase("true")) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.reportSuccess = z2;
        s = session.getProperty("mail." + name + ".starttls.enable");
        if (s == null || !s.equalsIgnoreCase("true")) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.useStartTLS = z2;
        s = session.getProperty("mail." + name + ".userset");
        if (s == null || !s.equalsIgnoreCase("true")) {
            z = false;
        }
        this.useRset = z;
    }

    public synchronized String getLocalHost() {
        try {
            if (this.localHostName == null || this.localHostName.length() <= 0) {
                this.localHostName = this.session.getProperty("mail." + this.name + ".localhost");
            }
            if (this.localHostName == null || this.localHostName.length() <= 0) {
                this.localHostName = this.session.getProperty("mail." + this.name + ".localaddress");
            }
            if (this.localHostName == null || this.localHostName.length() <= 0) {
                InetAddress localHost = InetAddress.getLocalHost();
                this.localHostName = localHost.getHostName();
                if (this.localHostName == null) {
                    this.localHostName = "[" + localHost.getHostAddress() + "]";
                }
            }
        } catch (UnknownHostException e) {
        }
        return this.localHostName;
    }

    public synchronized void setLocalHost(String localhost) {
        this.localHostName = localhost;
    }

    public synchronized void connect(Socket socket) throws MessagingException {
        this.serverSocket = socket;
        super.connect();
    }

    public synchronized String getSASLRealm() {
        if (this.saslRealm == UNKNOWN) {
            this.saslRealm = this.session.getProperty("mail." + this.name + ".sasl.realm");
            if (this.saslRealm == null) {
                this.saslRealm = this.session.getProperty("mail." + this.name + ".saslrealm");
            }
        }
        return this.saslRealm;
    }

    public synchronized void setSASLRealm(String saslRealm) {
        this.saslRealm = saslRealm;
    }

    public synchronized boolean getReportSuccess() {
        return this.reportSuccess;
    }

    public synchronized void setReportSuccess(boolean reportSuccess) {
        this.reportSuccess = reportSuccess;
    }

    public synchronized boolean getStartTLS() {
        return this.useStartTLS;
    }

    public synchronized void setStartTLS(boolean useStartTLS) {
        this.useStartTLS = useStartTLS;
    }

    public synchronized boolean getUseRset() {
        return this.useRset;
    }

    public synchronized void setUseRset(boolean useRset) {
        this.useRset = useRset;
    }

    public synchronized String getLastServerResponse() {
        return this.lastServerResponse;
    }

    public synchronized int getLastReturnCode() {
        return this.lastReturnCode;
    }

    private synchronized DigestMD5 getMD5() {
        if (this.md5support == null) {
            this.md5support = new DigestMD5(this.debug ? this.out : null);
        }
        return this.md5support;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Missing block: B:46:0x011d, code skipped:
            if (supportsExtension("AUTH=LOGIN") != false) goto L_0x011f;
     */
    public boolean protocolConnect(java.lang.String r19, int r20, java.lang.String r21, java.lang.String r22) throws javax.mail.MessagingException {
        /*
        r18 = this;
        r0 = r18;
        r2 = r0.session;
        r3 = new java.lang.StringBuilder;
        r4 = "mail.";
        r3.<init>(r4);
        r0 = r18;
        r4 = r0.name;
        r3 = r3.append(r4);
        r4 = ".ehlo";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r11 = r2.getProperty(r3);
        if (r11 == 0) goto L_0x008c;
    L_0x0023:
        r2 = "false";
        r2 = r11.equalsIgnoreCase(r2);
        if (r2 == 0) goto L_0x008c;
    L_0x002b:
        r17 = 0;
    L_0x002d:
        r0 = r18;
        r2 = r0.session;
        r3 = new java.lang.StringBuilder;
        r4 = "mail.";
        r3.<init>(r4);
        r0 = r18;
        r4 = r0.name;
        r3 = r3.append(r4);
        r4 = ".auth";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r7 = r2.getProperty(r3);
        if (r7 == 0) goto L_0x008f;
    L_0x0050:
        r2 = "true";
        r2 = r7.equalsIgnoreCase(r2);
        if (r2 == 0) goto L_0x008f;
    L_0x0058:
        r16 = 1;
    L_0x005a:
        r0 = r18;
        r2 = r0.debug;
        if (r2 == 0) goto L_0x0084;
    L_0x0060:
        r0 = r18;
        r2 = r0.out;
        r3 = new java.lang.StringBuilder;
        r4 = "DEBUG SMTP: useEhlo ";
        r3.<init>(r4);
        r0 = r17;
        r3 = r3.append(r0);
        r4 = ", useAuth ";
        r3 = r3.append(r4);
        r0 = r16;
        r3 = r3.append(r0);
        r3 = r3.toString();
        r2.println(r3);
    L_0x0084:
        if (r16 == 0) goto L_0x0092;
    L_0x0086:
        if (r21 == 0) goto L_0x008a;
    L_0x0088:
        if (r22 != 0) goto L_0x0092;
    L_0x008a:
        r2 = 0;
    L_0x008b:
        return r2;
    L_0x008c:
        r17 = 1;
        goto L_0x002d;
    L_0x008f:
        r16 = 0;
        goto L_0x005a;
    L_0x0092:
        r2 = -1;
        r0 = r20;
        if (r0 != r2) goto L_0x00be;
    L_0x0097:
        r0 = r18;
        r2 = r0.session;
        r3 = new java.lang.StringBuilder;
        r4 = "mail.";
        r3.<init>(r4);
        r0 = r18;
        r4 = r0.name;
        r3 = r3.append(r4);
        r4 = ".port";
        r3 = r3.append(r4);
        r3 = r3.toString();
        r13 = r2.getProperty(r3);
        if (r13 == 0) goto L_0x01c3;
    L_0x00ba:
        r20 = java.lang.Integer.parseInt(r13);
    L_0x00be:
        if (r19 == 0) goto L_0x00c6;
    L_0x00c0:
        r2 = r19.length();
        if (r2 != 0) goto L_0x00c8;
    L_0x00c6:
        r19 = "localhost";
    L_0x00c8:
        r15 = 0;
        r0 = r18;
        r2 = r0.serverSocket;
        if (r2 == 0) goto L_0x01cb;
    L_0x00cf:
        r18.openServer();
    L_0x00d2:
        if (r17 == 0) goto L_0x00de;
    L_0x00d4:
        r2 = r18.getLocalHost();
        r0 = r18;
        r15 = r0.ehlo(r2);
    L_0x00de:
        if (r15 != 0) goto L_0x00e9;
    L_0x00e0:
        r2 = r18.getLocalHost();
        r0 = r18;
        r0.helo(r2);
    L_0x00e9:
        r0 = r18;
        r2 = r0.useStartTLS;
        if (r2 == 0) goto L_0x0105;
    L_0x00ef:
        r2 = "STARTTLS";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 == 0) goto L_0x0105;
    L_0x00f9:
        r18.startTLS();
        r2 = r18.getLocalHost();
        r0 = r18;
        r0.ehlo(r2);
    L_0x0105:
        if (r16 != 0) goto L_0x010b;
    L_0x0107:
        if (r21 == 0) goto L_0x02d8;
    L_0x0109:
        if (r22 == 0) goto L_0x02d8;
    L_0x010b:
        r2 = "AUTH";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 != 0) goto L_0x011f;
    L_0x0115:
        r2 = "AUTH=LOGIN";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 == 0) goto L_0x02d8;
    L_0x011f:
        r0 = r18;
        r2 = r0.debug;
        if (r2 == 0) goto L_0x014b;
    L_0x0125:
        r0 = r18;
        r2 = r0.out;
        r3 = "DEBUG SMTP: Attempt to authenticate";
        r2.println(r3);
        r2 = "LOGIN";
        r0 = r18;
        r2 = r0.supportsAuthentication(r2);
        if (r2 != 0) goto L_0x014b;
    L_0x0138:
        r2 = "AUTH=LOGIN";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 == 0) goto L_0x014b;
    L_0x0142:
        r0 = r18;
        r2 = r0.out;
        r3 = "DEBUG SMTP: use AUTH=LOGIN hack";
        r2.println(r3);
    L_0x014b:
        r2 = "LOGIN";
        r0 = r18;
        r2 = r0.supportsAuthentication(r2);
        if (r2 != 0) goto L_0x015f;
    L_0x0155:
        r2 = "AUTH=LOGIN";
        r0 = r18;
        r2 = r0.supportsExtension(r2);
        if (r2 == 0) goto L_0x01e7;
    L_0x015f:
        r2 = "AUTH LOGIN";
        r0 = r18;
        r14 = r0.simpleCommand(r2);
        r2 = 530; // 0x212 float:7.43E-43 double:2.62E-321;
        if (r14 != r2) goto L_0x0176;
    L_0x016b:
        r18.startTLS();
        r2 = "AUTH LOGIN";
        r0 = r18;
        r14 = r0.simpleCommand(r2);
    L_0x0176:
        r10 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r10.<init>();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9 = new com.sun.mail.util.BASE64EncoderStream;	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r9.m341init(r10, r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x019e;
    L_0x0187:
        r2 = com.sun.mail.util.ASCIIUtility.getBytes(r21);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9.write(r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9.flush();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r2 = r10.toByteArray();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r0 = r18;
        r14 = r0.simpleCommand(r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r10.reset();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
    L_0x019e:
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x01b9;
    L_0x01a2:
        r2 = com.sun.mail.util.ASCIIUtility.getBytes(r22);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9.write(r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r9.flush();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r2 = r10.toByteArray();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r0 = r18;
        r14 = r0.simpleCommand(r2);	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
        r10.reset();	 Catch:{ IOException -> 0x01d0, all -> 0x01db }
    L_0x01b9:
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x01bd:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x01c3:
        r0 = r18;
        r0 = r0.defaultPort;
        r20 = r0;
        goto L_0x00be;
    L_0x01cb:
        r18.openServer(r19, r20);
        goto L_0x00d2;
    L_0x01d0:
        r2 = move-exception;
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x01d5:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x01db:
        r2 = move-exception;
        r3 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r3) goto L_0x01e6;
    L_0x01e0:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x01e6:
        throw r2;
    L_0x01e7:
        r2 = "PLAIN";
        r0 = r18;
        r2 = r0.supportsAuthentication(r2);
        if (r2 == 0) goto L_0x024e;
    L_0x01f1:
        r2 = "AUTH PLAIN";
        r0 = r18;
        r14 = r0.simpleCommand(r2);
        r10 = new java.io.ByteArrayOutputStream;	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r10.<init>();	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r9 = new com.sun.mail.util.BASE64EncoderStream;	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r9.m341init(r10, r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x022d;
    L_0x020a:
        r2 = 0;
        r9.write(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = com.sun.mail.util.ASCIIUtility.getBytes(r21);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r9.write(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = 0;
        r9.write(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = com.sun.mail.util.ASCIIUtility.getBytes(r22);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r9.write(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r9.flush();	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r2 = r10.toByteArray();	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
        r0 = r18;
        r14 = r0.simpleCommand(r2);	 Catch:{ IOException -> 0x0237, all -> 0x0242 }
    L_0x022d:
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x0231:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x0237:
        r2 = move-exception;
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x023c:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x0242:
        r2 = move-exception;
        r3 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r3) goto L_0x024d;
    L_0x0247:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x024d:
        throw r2;
    L_0x024e:
        r2 = "DIGEST-MD5";
        r0 = r18;
        r2 = r0.supportsAuthentication(r2);
        if (r2 == 0) goto L_0x02d8;
    L_0x0258:
        r1 = r18.getMD5();
        if (r1 == 0) goto L_0x02d8;
    L_0x025e:
        r2 = "AUTH DIGEST-MD5";
        r0 = r18;
        r14 = r0.simpleCommand(r2);
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x0291;
    L_0x026a:
        r5 = r18.getSASLRealm();	 Catch:{ Exception -> 0x02a5 }
        r0 = r18;
        r6 = r0.lastServerResponse;	 Catch:{ Exception -> 0x02a5 }
        r2 = r19;
        r3 = r21;
        r4 = r22;
        r8 = r1.authClient(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x02a5 }
        r0 = r18;
        r14 = r0.simpleCommand(r8);	 Catch:{ Exception -> 0x02a5 }
        r2 = 334; // 0x14e float:4.68E-43 double:1.65E-321;
        if (r14 != r2) goto L_0x0291;
    L_0x0286:
        r0 = r18;
        r2 = r0.lastServerResponse;	 Catch:{ Exception -> 0x02a5 }
        r2 = r1.authServer(r2);	 Catch:{ Exception -> 0x02a5 }
        if (r2 != 0) goto L_0x029b;
    L_0x0290:
        r14 = -1;
    L_0x0291:
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x0295:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x029b:
        r2 = 0;
        r2 = new byte[r2];	 Catch:{ Exception -> 0x02a5 }
        r0 = r18;
        r14 = r0.simpleCommand(r2);	 Catch:{ Exception -> 0x02a5 }
        goto L_0x0291;
    L_0x02a5:
        r12 = move-exception;
        r0 = r18;
        r2 = r0.debug;	 Catch:{ all -> 0x02cc }
        if (r2 == 0) goto L_0x02c2;
    L_0x02ac:
        r0 = r18;
        r2 = r0.out;	 Catch:{ all -> 0x02cc }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02cc }
        r4 = "DEBUG SMTP: DIGEST-MD5: ";
        r3.<init>(r4);	 Catch:{ all -> 0x02cc }
        r3 = r3.append(r12);	 Catch:{ all -> 0x02cc }
        r3 = r3.toString();	 Catch:{ all -> 0x02cc }
        r2.println(r3);	 Catch:{ all -> 0x02cc }
    L_0x02c2:
        r2 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r2) goto L_0x02d8;
    L_0x02c6:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x02cc:
        r2 = move-exception;
        r3 = 235; // 0xeb float:3.3E-43 double:1.16E-321;
        if (r14 == r3) goto L_0x02d7;
    L_0x02d1:
        r18.closeConnection();
        r2 = 0;
        goto L_0x008b;
    L_0x02d7:
        throw r2;
    L_0x02d8:
        r2 = 1;
        goto L_0x008b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.smtp.SMTPTransport.protocolConnect(java.lang.String, int, java.lang.String, java.lang.String):boolean");
    }

    public synchronized void sendMessage(Message message, Address[] addresses) throws MessagingException, SendFailedException {
        checkConnected();
        if (message instanceof MimeMessage) {
            int i = 0;
            while (i < addresses.length) {
                if (addresses[i] instanceof InternetAddress) {
                    i++;
                } else {
                    throw new MessagingException(addresses[i] + " is not an InternetAddress");
                }
            }
            this.message = (MimeMessage) message;
            this.addresses = addresses;
            this.validUnsentAddr = addresses;
            expandGroups();
            boolean use8bit = false;
            if (message instanceof SMTPMessage) {
                use8bit = ((SMTPMessage) message).getAllow8bitMIME();
            }
            if (!use8bit) {
                String ebStr = this.session.getProperty("mail." + this.name + ".allow8bitmime");
                if (ebStr == null || !ebStr.equalsIgnoreCase("true")) {
                    use8bit = false;
                } else {
                    use8bit = true;
                }
            }
            if (this.debug) {
                this.out.println("DEBUG SMTP: use8bit " + use8bit);
            }
            if (use8bit && supportsExtension("8BITMIME") && convertTo8Bit(this.message)) {
                try {
                    this.message.saveChanges();
                } catch (MessagingException e) {
                }
            }
            try {
                mailFrom();
                rcptTo();
                this.message.writeTo(data(), ignoreList);
                finishData();
                if (this.sendPartiallyFailed) {
                    if (this.debug) {
                        this.out.println("DEBUG SMTP: Sending partially failed because of invalid destination addresses");
                    }
                    notifyTransportListeners(3, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                    throw new SMTPSendFailedException(".", this.lastReturnCode, this.lastServerResponse, this.exception, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
                }
                notifyTransportListeners(1, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                this.invalidAddr = null;
                this.validUnsentAddr = null;
                this.validSentAddr = null;
                this.addresses = null;
                this.message = null;
                this.exception = null;
                this.sendPartiallyFailed = false;
            } catch (MessagingException mex) {
                if (this.debug) {
                    mex.printStackTrace(this.out);
                }
                notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                throw mex;
            } catch (IOException ex) {
                if (this.debug) {
                    ex.printStackTrace(this.out);
                }
                try {
                    closeConnection();
                } catch (MessagingException e2) {
                }
                notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
                throw new MessagingException("IOException while sending message", ex);
            } catch (Throwable th) {
                this.invalidAddr = null;
                this.validUnsentAddr = null;
                this.validSentAddr = null;
                this.addresses = null;
                this.message = null;
                this.exception = null;
                this.sendPartiallyFailed = false;
            }
        } else {
            if (this.debug) {
                this.out.println("DEBUG SMTP: Can only send RFC822 msgs");
            }
            throw new MessagingException("SMTP can only send RFC822 messages");
        }
    }

    public synchronized void close() throws MessagingException {
        if (super.isConnected()) {
            try {
                if (this.serverSocket != null) {
                    sendCommand("QUIT");
                    if (this.quitWait) {
                        int resp = readServerResponse();
                        if (!(resp == 221 || resp == -1)) {
                            this.out.println("DEBUG SMTP: QUIT failed with " + resp);
                        }
                    }
                }
            } finally {
                closeConnection();
            }
        }
    }

    private void closeConnection() throws MessagingException {
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
            this.serverSocket = null;
            this.serverOutput = null;
            this.serverInput = null;
            this.lineInputStream = null;
            if (super.isConnected()) {
                super.close();
            }
        } catch (IOException ioex) {
            throw new MessagingException("Server Close Failed", ioex);
        } catch (Throwable th) {
            this.serverSocket = null;
            this.serverOutput = null;
            this.serverInput = null;
            this.lineInputStream = null;
            if (super.isConnected()) {
                super.close();
            }
        }
    }

    public synchronized boolean isConnected() {
        boolean z = false;
        synchronized (this) {
            if (super.isConnected()) {
                try {
                    if (this.useRset) {
                        sendCommand("RSET");
                    } else {
                        sendCommand("NOOP");
                    }
                    int resp = readServerResponse();
                    if (resp < 0 || resp == 421) {
                        try {
                            closeConnection();
                        } catch (MessagingException e) {
                        }
                    } else {
                        z = true;
                    }
                } catch (Exception e2) {
                    try {
                        closeConnection();
                    } catch (MessagingException e3) {
                    }
                }
            }
        }
        return z;
    }

    private void expandGroups() {
        Vector groups = null;
        for (int i = 0; i < this.addresses.length; i++) {
            InternetAddress a = this.addresses[i];
            if (a.isGroup()) {
                if (groups == null) {
                    groups = new Vector();
                    for (int k = 0; k < i; k++) {
                        groups.addElement(this.addresses[k]);
                    }
                }
                try {
                    InternetAddress[] ia = a.getGroup(true);
                    if (ia != null) {
                        for (Object addElement : ia) {
                            groups.addElement(addElement);
                        }
                    } else {
                        groups.addElement(a);
                    }
                } catch (ParseException e) {
                    groups.addElement(a);
                }
            } else if (groups != null) {
                groups.addElement(a);
            }
        }
        if (groups != null) {
            InternetAddress[] newa = new InternetAddress[groups.size()];
            groups.copyInto(newa);
            this.addresses = newa;
        }
    }

    private boolean convertTo8Bit(MimePart part) {
        boolean changed = false;
        try {
            if (part.isMimeType("text/*")) {
                String enc = part.getEncoding();
                if (enc == null) {
                    return false;
                }
                if ((!enc.equalsIgnoreCase("quoted-printable") && !enc.equalsIgnoreCase("base64")) || !is8Bit(part.getInputStream())) {
                    return false;
                }
                part.setContent(part.getContent(), part.getContentType());
                part.setHeader("Content-Transfer-Encoding", "8bit");
                return true;
            } else if (!part.isMimeType("multipart/*")) {
                return false;
            } else {
                MimeMultipart mp = (MimeMultipart) part.getContent();
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    if (convertTo8Bit((MimePart) mp.getBodyPart(i))) {
                        changed = true;
                    }
                }
                return changed;
            }
        } catch (IOException | MessagingException e) {
            return false;
        }
    }

    private boolean is8Bit(InputStream is) {
        int linelen = 0;
        boolean need8bit = false;
        while (true) {
            try {
                int b = is.read();
                if (b >= 0) {
                    b &= MotionEventCompat.ACTION_MASK;
                    if (b == 13 || b == 10) {
                        linelen = 0;
                    } else if (b == 0) {
                        return false;
                    } else {
                        linelen++;
                        if (linelen > 998) {
                            return false;
                        }
                    }
                    if (b > 127) {
                        need8bit = true;
                    }
                } else if (!this.debug || !need8bit) {
                    return need8bit;
                } else {
                    this.out.println("DEBUG SMTP: found an 8bit part");
                    return need8bit;
                }
            } catch (IOException e) {
                return false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        try {
            closeConnection();
        } catch (MessagingException e) {
        }
    }

    /* access modifiers changed from: protected */
    public void helo(String domain) throws MessagingException {
        if (domain != null) {
            issueCommand("HELO " + domain, 250);
        } else {
            issueCommand("HELO", 250);
        }
    }

    /* access modifiers changed from: protected */
    public boolean ehlo(String domain) throws MessagingException {
        String cmd;
        if (domain != null) {
            cmd = "EHLO " + domain;
        } else {
            cmd = "EHLO";
        }
        sendCommand(cmd);
        int resp = readServerResponse();
        if (resp == 250) {
            BufferedReader rd = new BufferedReader(new StringReader(this.lastServerResponse));
            this.extMap = new Hashtable();
            boolean first = true;
            while (true) {
                try {
                    String line = rd.readLine();
                    if (line == null) {
                        break;
                    } else if (first) {
                        first = false;
                    } else if (line.length() >= 5) {
                        line = line.substring(4);
                        int i = line.indexOf(32);
                        String arg = MMMailContentUtil.MM_MESSAGE_SUBJECT;
                        if (i > 0) {
                            arg = line.substring(i + 1);
                            line = line.substring(0, i);
                        }
                        if (this.debug) {
                            this.out.println("DEBUG SMTP: Found extension \"" + line + "\", arg \"" + arg + "\"");
                        }
                        this.extMap.put(line.toUpperCase(Locale.ENGLISH), arg);
                    }
                } catch (IOException e) {
                }
            }
        }
        if (resp == 250) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0166  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0050  */
    public void mailFrom() throws javax.mail.MessagingException {
        /*
        r12 = this;
        r4 = 0;
        r9 = r12.message;
        r9 = r9 instanceof com.sun.mail.smtp.SMTPMessage;
        if (r9 == 0) goto L_0x000f;
    L_0x0007:
        r9 = r12.message;
        r9 = (com.sun.mail.smtp.SMTPMessage) r9;
        r4 = r9.getEnvelopeFrom();
    L_0x000f:
        if (r4 == 0) goto L_0x0017;
    L_0x0011:
        r9 = r4.length();
        if (r9 > 0) goto L_0x0034;
    L_0x0017:
        r9 = r12.session;
        r10 = new java.lang.StringBuilder;
        r11 = "mail.";
        r10.<init>(r11);
        r11 = r12.name;
        r10 = r10.append(r11);
        r11 = ".from";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r4 = r9.getProperty(r10);
    L_0x0034:
        if (r4 == 0) goto L_0x003c;
    L_0x0036:
        r9 = r4.length();
        if (r9 > 0) goto L_0x0056;
    L_0x003c:
        r9 = r12.message;
        if (r9 == 0) goto L_0x015e;
    L_0x0040:
        r9 = r12.message;
        r3 = r9.getFrom();
        if (r3 == 0) goto L_0x015e;
    L_0x0048:
        r9 = r3.length;
        if (r9 <= 0) goto L_0x015e;
    L_0x004b:
        r9 = 0;
        r5 = r3[r9];
    L_0x004e:
        if (r5 == 0) goto L_0x0166;
    L_0x0050:
        r5 = (javax.mail.internet.InternetAddress) r5;
        r4 = r5.getAddress();
    L_0x0056:
        r9 = new java.lang.StringBuilder;
        r10 = "MAIL FROM:";
        r9.<init>(r10);
        r10 = r12.normalizeAddress(r4);
        r9 = r9.append(r10);
        r0 = r9.toString();
        r9 = "DSN";
        r9 = r12.supportsExtension(r9);
        if (r9 == 0) goto L_0x00b8;
    L_0x0071:
        r6 = 0;
        r9 = r12.message;
        r9 = r9 instanceof com.sun.mail.smtp.SMTPMessage;
        if (r9 == 0) goto L_0x0080;
    L_0x0078:
        r9 = r12.message;
        r9 = (com.sun.mail.smtp.SMTPMessage) r9;
        r6 = r9.getDSNRet();
    L_0x0080:
        if (r6 != 0) goto L_0x009f;
    L_0x0082:
        r9 = r12.session;
        r10 = new java.lang.StringBuilder;
        r11 = "mail.";
        r10.<init>(r11);
        r11 = r12.name;
        r10 = r10.append(r11);
        r11 = ".dsn.ret";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r6 = r9.getProperty(r10);
    L_0x009f:
        if (r6 == 0) goto L_0x00b8;
    L_0x00a1:
        r9 = new java.lang.StringBuilder;
        r10 = java.lang.String.valueOf(r0);
        r9.<init>(r10);
        r10 = " RET=";
        r9 = r9.append(r10);
        r9 = r9.append(r6);
        r0 = r9.toString();
    L_0x00b8:
        r9 = "AUTH";
        r9 = r12.supportsExtension(r9);
        if (r9 == 0) goto L_0x010b;
    L_0x00c0:
        r8 = 0;
        r9 = r12.message;
        r9 = r9 instanceof com.sun.mail.smtp.SMTPMessage;
        if (r9 == 0) goto L_0x00cf;
    L_0x00c7:
        r9 = r12.message;
        r9 = (com.sun.mail.smtp.SMTPMessage) r9;
        r8 = r9.getSubmitter();
    L_0x00cf:
        if (r8 != 0) goto L_0x00ee;
    L_0x00d1:
        r9 = r12.session;
        r10 = new java.lang.StringBuilder;
        r11 = "mail.";
        r10.<init>(r11);
        r11 = r12.name;
        r10 = r10.append(r11);
        r11 = ".submitter";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r8 = r9.getProperty(r10);
    L_0x00ee:
        if (r8 == 0) goto L_0x010b;
    L_0x00f0:
        r7 = xtext(r8);	 Catch:{ IllegalArgumentException -> 0x016e }
        r9 = new java.lang.StringBuilder;	 Catch:{ IllegalArgumentException -> 0x016e }
        r10 = java.lang.String.valueOf(r0);	 Catch:{ IllegalArgumentException -> 0x016e }
        r9.<init>(r10);	 Catch:{ IllegalArgumentException -> 0x016e }
        r10 = " AUTH=";
        r9 = r9.append(r10);	 Catch:{ IllegalArgumentException -> 0x016e }
        r9 = r9.append(r7);	 Catch:{ IllegalArgumentException -> 0x016e }
        r0 = r9.toString();	 Catch:{ IllegalArgumentException -> 0x016e }
    L_0x010b:
        r2 = 0;
        r9 = r12.message;
        r9 = r9 instanceof com.sun.mail.smtp.SMTPMessage;
        if (r9 == 0) goto L_0x011a;
    L_0x0112:
        r9 = r12.message;
        r9 = (com.sun.mail.smtp.SMTPMessage) r9;
        r2 = r9.getMailExtension();
    L_0x011a:
        if (r2 != 0) goto L_0x0139;
    L_0x011c:
        r9 = r12.session;
        r10 = new java.lang.StringBuilder;
        r11 = "mail.";
        r10.<init>(r11);
        r11 = r12.name;
        r10 = r10.append(r11);
        r11 = ".mailextension";
        r10 = r10.append(r11);
        r10 = r10.toString();
        r2 = r9.getProperty(r10);
    L_0x0139:
        if (r2 == 0) goto L_0x0158;
    L_0x013b:
        r9 = r2.length();
        if (r9 <= 0) goto L_0x0158;
    L_0x0141:
        r9 = new java.lang.StringBuilder;
        r10 = java.lang.String.valueOf(r0);
        r9.<init>(r10);
        r10 = " ";
        r9 = r9.append(r10);
        r9 = r9.append(r2);
        r0 = r9.toString();
    L_0x0158:
        r9 = 250; // 0xfa float:3.5E-43 double:1.235E-321;
        r12.issueSendCommand(r0, r9);
        return;
    L_0x015e:
        r9 = r12.session;
        r5 = javax.mail.internet.InternetAddress.getLocalAddress(r9);
        goto L_0x004e;
    L_0x0166:
        r9 = new javax.mail.MessagingException;
        r10 = "can't determine local email address";
        r9.m403init(r10);
        throw r9;
    L_0x016e:
        r1 = move-exception;
        r9 = r12.debug;
        if (r9 == 0) goto L_0x010b;
    L_0x0173:
        r9 = r12.out;
        r10 = new java.lang.StringBuilder;
        r11 = "DEBUG SMTP: ignoring invalid submitter: ";
        r10.<init>(r11);
        r10 = r10.append(r8);
        r11 = ", Exception: ";
        r10 = r10.append(r11);
        r10 = r10.append(r1);
        r10 = r10.toString();
        r9.println(r10);
        goto L_0x010b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.smtp.SMTPTransport.mailFrom():void");
    }

    /* access modifiers changed from: protected */
    public void rcptTo() throws MessagingException {
        int i;
        int j;
        Vector valid = new Vector();
        Vector validUnsent = new Vector();
        Vector invalid = new Vector();
        MessagingException mex = null;
        boolean sendFailed = false;
        this.invalidAddr = null;
        this.validUnsentAddr = null;
        this.validSentAddr = null;
        boolean sendPartial = false;
        if (this.message instanceof SMTPMessage) {
            sendPartial = ((SMTPMessage) this.message).getSendPartial();
        }
        if (!sendPartial) {
            String sp = this.session.getProperty("mail." + this.name + ".sendpartial");
            if (sp != null) {
                if (sp.equalsIgnoreCase("true")) {
                    sendPartial = true;
                }
            }
            sendPartial = false;
        }
        if (this.debug && sendPartial) {
            this.out.println("DEBUG SMTP: sendPartial set");
        }
        boolean dsn = false;
        String notify = null;
        if (supportsExtension("DSN")) {
            if (this.message instanceof SMTPMessage) {
                notify = ((SMTPMessage) this.message).getDSNNotify();
            }
            if (notify == null) {
                notify = this.session.getProperty("mail." + this.name + ".dsn.notify");
            }
            if (notify != null) {
                dsn = true;
            }
        }
        for (InternetAddress ia : this.addresses) {
            String cmd = "RCPT TO:" + normalizeAddress(ia.getAddress());
            if (dsn) {
                cmd = new StringBuilder(String.valueOf(cmd)).append(" NOTIFY=").append(notify).toString();
            }
            sendCommand(cmd);
            int retCode = readServerResponse();
            Exception sMTPAddressSucceededException;
            Exception mex2;
            switch (retCode) {
                case 250:
                case 251:
                    valid.addElement(ia);
                    if (!this.reportSuccess) {
                        break;
                    }
                    sMTPAddressSucceededException = new SMTPAddressSucceededException(ia, cmd, retCode, this.lastServerResponse);
                    if (mex2 != null) {
                        mex2.setNextException(sMTPAddressSucceededException);
                        break;
                    } else {
                        mex2 = sMTPAddressSucceededException;
                        break;
                    }
                case 450:
                case 451:
                case 452:
                case 552:
                    if (!sendPartial) {
                        sendFailed = true;
                    }
                    validUnsent.addElement(ia);
                    sMTPAddressSucceededException = new SMTPAddressFailedException(ia, cmd, retCode, this.lastServerResponse);
                    if (mex2 != null) {
                        mex2.setNextException(sMTPAddressSucceededException);
                        break;
                    } else {
                        mex2 = sMTPAddressSucceededException;
                        break;
                    }
                case 501:
                case 503:
                case 550:
                case 551:
                case 553:
                    if (!sendPartial) {
                        sendFailed = true;
                    }
                    invalid.addElement(ia);
                    sMTPAddressSucceededException = new SMTPAddressFailedException(ia, cmd, retCode, this.lastServerResponse);
                    if (mex2 != null) {
                        mex2.setNextException(sMTPAddressSucceededException);
                        break;
                    } else {
                        mex2 = sMTPAddressSucceededException;
                        break;
                    }
                default:
                    if (retCode >= 400 && retCode <= 499) {
                        validUnsent.addElement(ia);
                    } else if (retCode < 500 || retCode > 599) {
                        if (this.debug) {
                            this.out.println("DEBUG SMTP: got response code " + retCode + ", with response: " + this.lastServerResponse);
                        }
                        String _lsr = this.lastServerResponse;
                        int _lrc = this.lastReturnCode;
                        if (this.serverSocket != null) {
                            issueCommand("RSET", 250);
                        }
                        this.lastServerResponse = _lsr;
                        this.lastReturnCode = _lrc;
                        throw new SMTPAddressFailedException(ia, cmd, retCode, _lsr);
                    } else {
                        invalid.addElement(ia);
                    }
                    if (!sendPartial) {
                        sendFailed = true;
                    }
                    sMTPAddressSucceededException = new SMTPAddressFailedException(ia, cmd, retCode, this.lastServerResponse);
                    if (mex2 != null) {
                        mex2.setNextException(sMTPAddressSucceededException);
                        break;
                    } else {
                        mex2 = sMTPAddressSucceededException;
                        break;
                    }
                    break;
            }
        }
        if (sendPartial && valid.size() == 0) {
            sendFailed = true;
        }
        if (sendFailed) {
            int i2;
            this.invalidAddr = new Address[invalid.size()];
            invalid.copyInto(this.invalidAddr);
            this.validUnsentAddr = new Address[(valid.size() + validUnsent.size())];
            i = 0;
            j = 0;
            while (j < valid.size()) {
                i2 = i + 1;
                this.validUnsentAddr[i] = (Address) valid.elementAt(j);
                j++;
                i = i2;
            }
            j = 0;
            while (j < validUnsent.size()) {
                i2 = i + 1;
                this.validUnsentAddr[i] = (Address) validUnsent.elementAt(j);
                j++;
                i = i2;
            }
        } else if (this.reportSuccess || (sendPartial && (invalid.size() > 0 || validUnsent.size() > 0))) {
            this.sendPartiallyFailed = true;
            this.exception = mex2;
            this.invalidAddr = new Address[invalid.size()];
            invalid.copyInto(this.invalidAddr);
            this.validUnsentAddr = new Address[validUnsent.size()];
            validUnsent.copyInto(this.validUnsentAddr);
            this.validSentAddr = new Address[valid.size()];
            valid.copyInto(this.validSentAddr);
        } else {
            this.validSentAddr = this.addresses;
        }
        if (this.debug) {
            if (this.validSentAddr != null && this.validSentAddr.length > 0) {
                this.out.println("DEBUG SMTP: Verified Addresses");
                for (Object obj : this.validSentAddr) {
                    this.out.println("DEBUG SMTP:   " + obj);
                }
            }
            if (this.validUnsentAddr != null && this.validUnsentAddr.length > 0) {
                this.out.println("DEBUG SMTP: Valid Unsent Addresses");
                for (Object obj2 : this.validUnsentAddr) {
                    this.out.println("DEBUG SMTP:   " + obj2);
                }
            }
            if (this.invalidAddr != null && this.invalidAddr.length > 0) {
                this.out.println("DEBUG SMTP: Invalid Addresses");
                for (Object obj22 : this.invalidAddr) {
                    this.out.println("DEBUG SMTP:   " + obj22);
                }
            }
        }
        if (sendFailed) {
            if (this.debug) {
                this.out.println("DEBUG SMTP: Sending failed because of invalid destination addresses");
            }
            notifyTransportListeners(2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr, this.message);
            String lsr = this.lastServerResponse;
            int lrc = this.lastReturnCode;
            try {
                if (this.serverSocket != null) {
                    issueCommand("RSET", 250);
                }
                this.lastServerResponse = lsr;
                this.lastReturnCode = lrc;
            } catch (MessagingException e) {
                try {
                    close();
                } catch (MessagingException ex2) {
                    if (this.debug) {
                        ex2.printStackTrace(this.out);
                    }
                } catch (Throwable th) {
                    this.lastServerResponse = lsr;
                    this.lastReturnCode = lrc;
                }
                this.lastServerResponse = lsr;
                this.lastReturnCode = lrc;
            }
            throw new SendFailedException("Invalid Addresses", mex2, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
        }
    }

    /* access modifiers changed from: protected */
    public OutputStream data() throws MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            issueSendCommand("DATA", 354);
            this.dataStream = new SMTPOutputStream(this.serverOutput);
            return this.dataStream;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void finishData() throws IOException, MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            this.dataStream.ensureAtBOL();
            issueSendCommand(".", 250);
            return;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void startTLS() throws MessagingException {
        issueCommand("STARTTLS", 220);
        try {
            this.serverSocket = SocketFetcher.startTLS(this.serverSocket, this.session.getProperties(), "mail." + this.name);
            initStreams();
        } catch (IOException ioex) {
            closeConnection();
            throw new MessagingException("Could not convert socket to TLS", ioex);
        }
    }

    private void openServer(String server, int port) throws MessagingException {
        if (this.debug) {
            this.out.println("DEBUG SMTP: trying to connect to host \"" + server + "\", port " + port + ", isSSL " + this.isSSL);
        }
        try {
            this.serverSocket = SocketFetcher.getSocket(server, port, this.session.getProperties(), "mail." + this.name, this.isSSL);
            port = this.serverSocket.getPort();
            initStreams();
            int r = readServerResponse();
            if (r != 220) {
                this.serverSocket.close();
                this.serverSocket = null;
                this.serverOutput = null;
                this.serverInput = null;
                this.lineInputStream = null;
                if (this.debug) {
                    this.out.println("DEBUG SMTP: could not connect to host \"" + server + "\", port: " + port + ", response: " + r + "\n");
                }
                throw new MessagingException("Could not connect to SMTP host: " + server + ", port: " + port + ", response: " + r);
            } else if (this.debug) {
                this.out.println("DEBUG SMTP: connected to host \"" + server + "\", port: " + port + "\n");
            }
        } catch (UnknownHostException uhex) {
            throw new MessagingException("Unknown SMTP host: " + server, uhex);
        } catch (IOException ioe) {
            throw new MessagingException("Could not connect to SMTP host: " + server + ", port: " + port, ioe);
        }
    }

    private void openServer() throws MessagingException {
        int port = -1;
        String server = UNKNOWN;
        try {
            port = this.serverSocket.getPort();
            server = this.serverSocket.getInetAddress().getHostName();
            if (this.debug) {
                this.out.println("DEBUG SMTP: starting protocol to host \"" + server + "\", port " + port);
            }
            initStreams();
            int r = readServerResponse();
            if (r != 220) {
                this.serverSocket.close();
                this.serverSocket = null;
                this.serverOutput = null;
                this.serverInput = null;
                this.lineInputStream = null;
                if (this.debug) {
                    this.out.println("DEBUG SMTP: got bad greeting from host \"" + server + "\", port: " + port + ", response: " + r + "\n");
                }
                throw new MessagingException("Got bad greeting from SMTP host: " + server + ", port: " + port + ", response: " + r);
            } else if (this.debug) {
                this.out.println("DEBUG SMTP: protocol started to host \"" + server + "\", port: " + port + "\n");
            }
        } catch (IOException ioe) {
            throw new MessagingException("Could not start protocol to SMTP host: " + server + ", port: " + port, ioe);
        }
    }

    private void initStreams() throws IOException {
        Properties props = this.session.getProperties();
        PrintStream out = this.session.getDebugOut();
        boolean debug = this.session.getDebug();
        String s = props.getProperty("mail.debug.quote");
        boolean quote = s != null && s.equalsIgnoreCase("true");
        TraceInputStream traceInput = new TraceInputStream(this.serverSocket.getInputStream(), out);
        traceInput.setTrace(debug);
        traceInput.setQuote(quote);
        TraceOutputStream traceOutput = new TraceOutputStream(this.serverSocket.getOutputStream(), out);
        traceOutput.setTrace(debug);
        traceOutput.setQuote(quote);
        this.serverOutput = new BufferedOutputStream(traceOutput);
        this.serverInput = new BufferedInputStream(traceInput);
        this.lineInputStream = new LineInputStream(this.serverInput);
    }

    public synchronized void issueCommand(String cmd, int expect) throws MessagingException {
        sendCommand(cmd);
        if (readServerResponse() != expect) {
            throw new MessagingException(this.lastServerResponse);
        }
    }

    private void issueSendCommand(String cmd, int expect) throws MessagingException {
        sendCommand(cmd);
        int ret = readServerResponse();
        if (ret != expect) {
            int vul;
            int vsl = this.validSentAddr == null ? 0 : this.validSentAddr.length;
            if (this.validUnsentAddr == null) {
                vul = 0;
            } else {
                vul = this.validUnsentAddr.length;
            }
            Address[] valid = new Address[(vsl + vul)];
            if (vsl > 0) {
                System.arraycopy(this.validSentAddr, 0, valid, 0, vsl);
            }
            if (vul > 0) {
                System.arraycopy(this.validUnsentAddr, 0, valid, vsl, vul);
            }
            this.validSentAddr = null;
            this.validUnsentAddr = valid;
            if (this.debug) {
                this.out.println("DEBUG SMTP: got response code " + ret + ", with response: " + this.lastServerResponse);
            }
            String _lsr = this.lastServerResponse;
            int _lrc = this.lastReturnCode;
            if (this.serverSocket != null) {
                issueCommand("RSET", 250);
            }
            this.lastServerResponse = _lsr;
            this.lastReturnCode = _lrc;
            throw new SMTPSendFailedException(cmd, ret, this.lastServerResponse, this.exception, this.validSentAddr, this.validUnsentAddr, this.invalidAddr);
        }
    }

    public synchronized int simpleCommand(String cmd) throws MessagingException {
        sendCommand(cmd);
        return readServerResponse();
    }

    /* access modifiers changed from: protected */
    public int simpleCommand(byte[] cmd) throws MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            sendCommand(cmd);
            return readServerResponse();
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void sendCommand(String cmd) throws MessagingException {
        sendCommand(ASCIIUtility.getBytes(cmd));
    }

    private void sendCommand(byte[] cmdBytes) throws MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            try {
                this.serverOutput.write(cmdBytes);
                this.serverOutput.write(CRLF);
                this.serverOutput.flush();
                return;
            } catch (IOException ex) {
                throw new MessagingException("Can't send command to SMTP host", ex);
            }
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public int readServerResponse() throws MessagingException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            int returnCode;
            String serverResponse = MMMailContentUtil.MM_MESSAGE_SUBJECT;
            StringBuffer buf = new StringBuffer(100);
            String line;
            do {
                try {
                    line = this.lineInputStream.readLine();
                    if (line == null) {
                        serverResponse = buf.toString();
                        if (serverResponse.length() == 0) {
                            serverResponse = "[EOF]";
                        }
                        this.lastServerResponse = serverResponse;
                        this.lastReturnCode = -1;
                        if (!this.debug) {
                            return -1;
                        }
                        this.out.println("DEBUG SMTP: EOF: " + serverResponse);
                        return -1;
                    }
                    buf.append(line);
                    buf.append("\n");
                } catch (IOException ioex) {
                    if (this.debug) {
                        this.out.println("DEBUG SMTP: exception reading response: " + ioex);
                    }
                    this.lastServerResponse = MMMailContentUtil.MM_MESSAGE_SUBJECT;
                    this.lastReturnCode = 0;
                    throw new MessagingException("Exception reading response", ioex);
                }
            } while (isNotLastLine(line));
            serverResponse = buf.toString();
            if (serverResponse == null || serverResponse.length() < 3) {
                returnCode = -1;
            } else {
                try {
                    returnCode = Integer.parseInt(serverResponse.substring(0, 3));
                } catch (NumberFormatException e) {
                    try {
                        close();
                    } catch (MessagingException mex) {
                        if (this.debug) {
                            mex.printStackTrace(this.out);
                        }
                    }
                    returnCode = -1;
                } catch (StringIndexOutOfBoundsException e2) {
                    try {
                        close();
                    } catch (MessagingException mex2) {
                        if (this.debug) {
                            mex2.printStackTrace(this.out);
                        }
                    }
                    returnCode = -1;
                }
            }
            if (returnCode == -1 && this.debug) {
                this.out.println("DEBUG SMTP: bad server response: " + serverResponse);
            }
            this.lastServerResponse = serverResponse;
            this.lastReturnCode = returnCode;
            return returnCode;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void checkConnected() {
        if (!super.isConnected()) {
            throw new IllegalStateException("Not connected");
        }
    }

    private boolean isNotLastLine(String line) {
        return line != null && line.length() >= 4 && line.charAt(3) == '-';
    }

    private String normalizeAddress(String addr) {
        if (addr.startsWith("<") || addr.endsWith(">")) {
            return addr;
        }
        return "<" + addr + ">";
    }

    public boolean supportsExtension(String ext) {
        return (this.extMap == null || this.extMap.get(ext.toUpperCase(Locale.ENGLISH)) == null) ? false : true;
    }

    public String getExtensionParameter(String ext) {
        if (this.extMap == null) {
            return null;
        }
        return (String) this.extMap.get(ext.toUpperCase(Locale.ENGLISH));
    }

    /* access modifiers changed from: protected */
    public boolean supportsAuthentication(String auth) {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (this.extMap == null) {
            return false;
        } else {
            String a = (String) this.extMap.get("AUTH");
            if (a == null) {
                return false;
            }
            StringTokenizer st = new StringTokenizer(a);
            while (st.hasMoreTokens()) {
                if (st.nextToken().equalsIgnoreCase(auth)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected static String xtext(String s) {
        StringBuffer sb = null;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 128) {
                throw new IllegalArgumentException("Non-ASCII character in SMTP submitter: " + s);
            }
            if (c < '!' || c > '~' || c == '+' || c == '=') {
                if (sb == null) {
                    sb = new StringBuffer(s.length() + 4);
                    sb.append(s.substring(0, i));
                }
                sb.append('+');
                sb.append(hexchar[(c & 240) >> 4]);
                sb.append(hexchar[c & 15]);
            } else if (sb != null) {
                sb.append(c);
            }
        }
        return sb != null ? sb.toString() : s;
    }
}
