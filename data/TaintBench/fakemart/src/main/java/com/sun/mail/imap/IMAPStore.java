package com.sun.mail.imap;

import com.googleprojects.mm.MMMailContentUtil;
import com.sun.mail.iap.BadCommandException;
import com.sun.mail.iap.ConnectionException;
import com.sun.mail.iap.ProtocolException;
import com.sun.mail.iap.Response;
import com.sun.mail.iap.ResponseHandler;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.imap.protocol.Namespaces;
import com.sun.mail.imap.protocol.Namespaces.Namespace;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Quota;
import javax.mail.QuotaAwareStore;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.URLName;

public class IMAPStore extends Store implements QuotaAwareStore, ResponseHandler {
    static final /* synthetic */ boolean $assertionsDisabled = (!IMAPStore.class.desiredAssertionStatus());
    public static final int RESPONSE = 1000;
    private int appendBufferSize;
    private String authorizationID;
    private int blksize;
    private volatile boolean connected;
    private int defaultPort;
    private boolean disableAuthLogin;
    private boolean disableAuthPlain;
    private boolean enableImapEvents;
    private boolean enableSASL;
    private boolean enableStartTLS;
    private boolean forcePasswordRefresh;
    private String host;
    private boolean isSSL;
    private int minIdleTime;
    private String name;
    private Namespaces namespaces;
    private PrintStream out;
    private String password;
    private ConnectionPool pool;
    private int port;
    private String proxyAuthUser;
    private String[] saslMechanisms;
    private String saslRealm;
    private int statusCacheTimeout;
    private String user;

    static class ConnectionPool {
        private static final int ABORTING = 2;
        private static final int IDLE = 1;
        private static final int RUNNING = 0;
        /* access modifiers changed from: private */
        public Vector authenticatedConnections = new Vector();
        /* access modifiers changed from: private */
        public long clientTimeoutInterval = 45000;
        /* access modifiers changed from: private */
        public boolean debug = false;
        /* access modifiers changed from: private */
        public Vector folders;
        /* access modifiers changed from: private */
        public IMAPProtocol idleProtocol;
        /* access modifiers changed from: private */
        public int idleState = 0;
        /* access modifiers changed from: private */
        public long lastTimePruned;
        /* access modifiers changed from: private */
        public int poolSize = 1;
        /* access modifiers changed from: private */
        public long pruningInterval = 60000;
        /* access modifiers changed from: private */
        public boolean separateStoreConnection = false;
        /* access modifiers changed from: private */
        public long serverTimeoutInterval = 1800000;
        /* access modifiers changed from: private */
        public boolean storeConnectionInUse = false;

        ConnectionPool() {
        }
    }

    public IMAPStore(Session session, URLName url) {
        this(session, url, "imap", 143, false);
    }

    protected IMAPStore(Session session, URLName url, String name, int defaultPort, boolean isSSL) {
        super(session, url);
        this.name = "imap";
        this.defaultPort = 143;
        this.isSSL = false;
        this.port = -1;
        this.blksize = 16384;
        this.statusCacheTimeout = RESPONSE;
        this.appendBufferSize = -1;
        this.minIdleTime = 10;
        this.disableAuthLogin = false;
        this.disableAuthPlain = false;
        this.enableStartTLS = false;
        this.enableSASL = false;
        this.forcePasswordRefresh = false;
        this.enableImapEvents = false;
        this.connected = false;
        this.pool = new ConnectionPool();
        if (url != null) {
            name = url.getProtocol();
        }
        this.name = name;
        this.defaultPort = defaultPort;
        this.isSSL = isSSL;
        this.pool.lastTimePruned = System.currentTimeMillis();
        this.debug = session.getDebug();
        this.out = session.getDebugOut();
        if (this.out == null) {
            this.out = System.out;
        }
        String s = session.getProperty("mail." + name + ".connectionpool.debug");
        if (s != null && s.equalsIgnoreCase("true")) {
            this.pool.debug = true;
        }
        s = session.getProperty("mail." + name + ".partialfetch");
        if (s == null || !s.equalsIgnoreCase("false")) {
            s = session.getProperty("mail." + name + ".fetchsize");
            if (s != null) {
                this.blksize = Integer.parseInt(s);
            }
            if (this.debug) {
                this.out.println("DEBUG: mail.imap.fetchsize: " + this.blksize);
            }
        } else {
            this.blksize = -1;
            if (this.debug) {
                this.out.println("DEBUG: mail.imap.partialfetch: false");
            }
        }
        s = session.getProperty("mail." + name + ".statuscachetimeout");
        if (s != null) {
            this.statusCacheTimeout = Integer.parseInt(s);
            if (this.debug) {
                this.out.println("DEBUG: mail.imap.statuscachetimeout: " + this.statusCacheTimeout);
            }
        }
        s = session.getProperty("mail." + name + ".appendbuffersize");
        if (s != null) {
            this.appendBufferSize = Integer.parseInt(s);
            if (this.debug) {
                this.out.println("DEBUG: mail.imap.appendbuffersize: " + this.appendBufferSize);
            }
        }
        s = session.getProperty("mail." + name + ".minidletime");
        if (s != null) {
            this.minIdleTime = Integer.parseInt(s);
            if (this.debug) {
                this.out.println("DEBUG: mail.imap.minidletime: " + this.minIdleTime);
            }
        }
        s = session.getProperty("mail." + name + ".connectionpoolsize");
        if (s != null) {
            try {
                int size = Integer.parseInt(s);
                if (size > 0) {
                    this.pool.poolSize = size;
                }
            } catch (NumberFormatException e) {
            }
            if (this.pool.debug) {
                this.out.println("DEBUG: mail.imap.connectionpoolsize: " + this.pool.poolSize);
            }
        }
        s = session.getProperty("mail." + name + ".connectionpooltimeout");
        if (s != null) {
            try {
                int connectionPoolTimeout = Integer.parseInt(s);
                if (connectionPoolTimeout > 0) {
                    this.pool.clientTimeoutInterval = (long) connectionPoolTimeout;
                }
            } catch (NumberFormatException e2) {
            }
            if (this.pool.debug) {
                this.out.println("DEBUG: mail.imap.connectionpooltimeout: " + this.pool.clientTimeoutInterval);
            }
        }
        s = session.getProperty("mail." + name + ".servertimeout");
        if (s != null) {
            try {
                int serverTimeout = Integer.parseInt(s);
                if (serverTimeout > 0) {
                    this.pool.serverTimeoutInterval = (long) serverTimeout;
                }
            } catch (NumberFormatException e3) {
            }
            if (this.pool.debug) {
                this.out.println("DEBUG: mail.imap.servertimeout: " + this.pool.serverTimeoutInterval);
            }
        }
        s = session.getProperty("mail." + name + ".separatestoreconnection");
        if (s != null && s.equalsIgnoreCase("true")) {
            if (this.pool.debug) {
                this.out.println("DEBUG: dedicate a store connection");
            }
            this.pool.separateStoreConnection = true;
        }
        s = session.getProperty("mail." + name + ".proxyauth.user");
        if (s != null) {
            this.proxyAuthUser = s;
            if (this.debug) {
                this.out.println("DEBUG: mail.imap.proxyauth.user: " + this.proxyAuthUser);
            }
        }
        s = session.getProperty("mail." + name + ".auth.login.disable");
        if (s != null && s.equalsIgnoreCase("true")) {
            if (this.debug) {
                this.out.println("DEBUG: disable AUTH=LOGIN");
            }
            this.disableAuthLogin = true;
        }
        s = session.getProperty("mail." + name + ".auth.plain.disable");
        if (s != null && s.equalsIgnoreCase("true")) {
            if (this.debug) {
                this.out.println("DEBUG: disable AUTH=PLAIN");
            }
            this.disableAuthPlain = true;
        }
        s = session.getProperty("mail." + name + ".starttls.enable");
        if (s != null && s.equalsIgnoreCase("true")) {
            if (this.debug) {
                this.out.println("DEBUG: enable STARTTLS");
            }
            this.enableStartTLS = true;
        }
        s = session.getProperty("mail." + name + ".sasl.enable");
        if (s != null && s.equalsIgnoreCase("true")) {
            if (this.debug) {
                this.out.println("DEBUG: enable SASL");
            }
            this.enableSASL = true;
        }
        if (this.enableSASL) {
            s = session.getProperty("mail." + name + ".sasl.mechanisms");
            if (s != null && s.length() > 0) {
                if (this.debug) {
                    this.out.println("DEBUG: SASL mechanisms allowed: " + s);
                }
                Vector v = new Vector(5);
                StringTokenizer st = new StringTokenizer(s, " ,");
                while (st.hasMoreTokens()) {
                    String m = st.nextToken();
                    if (m.length() > 0) {
                        v.addElement(m);
                    }
                }
                this.saslMechanisms = new String[v.size()];
                v.copyInto(this.saslMechanisms);
            }
        }
        s = session.getProperty("mail." + name + ".sasl.authorizationid");
        if (s != null) {
            this.authorizationID = s;
            if (this.debug) {
                this.out.println("DEBUG: mail.imap.sasl.authorizationid: " + this.authorizationID);
            }
        }
        s = session.getProperty("mail." + name + ".sasl.realm");
        if (s != null) {
            this.saslRealm = s;
            if (this.debug) {
                this.out.println("DEBUG: mail.imap.sasl.realm: " + this.saslRealm);
            }
        }
        s = session.getProperty("mail." + name + ".forcepasswordrefresh");
        if (s != null && s.equalsIgnoreCase("true")) {
            if (this.debug) {
                this.out.println("DEBUG: enable forcePasswordRefresh");
            }
            this.forcePasswordRefresh = true;
        }
        s = session.getProperty("mail." + name + ".enableimapevents");
        if (s != null && s.equalsIgnoreCase("true")) {
            if (this.debug) {
                this.out.println("DEBUG: enable IMAP events");
            }
            this.enableImapEvents = true;
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing block: B:14:0x0045, code skipped:
            return r4;
     */
    /* JADX WARNING: Missing block: B:65:0x0157, code skipped:
            r4 = th;
     */
    public synchronized boolean protocolConnect(java.lang.String r18, int r19, java.lang.String r20, java.lang.String r21) throws javax.mail.MessagingException {
        /*
        r17 = this;
        monitor-enter(r17);
        r16 = 0;
        if (r18 == 0) goto L_0x0009;
    L_0x0005:
        if (r21 == 0) goto L_0x0009;
    L_0x0007:
        if (r20 != 0) goto L_0x0049;
    L_0x0009:
        r0 = r17;
        r4 = r0.debug;	 Catch:{ all -> 0x0138 }
        if (r4 == 0) goto L_0x0041;
    L_0x000f:
        r0 = r17;
        r5 = r0.out;	 Catch:{ all -> 0x0138 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0138 }
        r6 = "DEBUG: protocolConnect returning false, host=";
        r4.<init>(r6);	 Catch:{ all -> 0x0138 }
        r0 = r18;
        r4 = r4.append(r0);	 Catch:{ all -> 0x0138 }
        r6 = ", user=";
        r4 = r4.append(r6);	 Catch:{ all -> 0x0138 }
        r0 = r20;
        r4 = r4.append(r0);	 Catch:{ all -> 0x0138 }
        r6 = ", password=";
        r6 = r4.append(r6);	 Catch:{ all -> 0x0138 }
        if (r21 == 0) goto L_0x0046;
    L_0x0034:
        r4 = "<non-null>";
    L_0x0036:
        r4 = r6.append(r4);	 Catch:{ all -> 0x0138 }
        r4 = r4.toString();	 Catch:{ all -> 0x0138 }
        r5.println(r4);	 Catch:{ all -> 0x0138 }
    L_0x0041:
        r4 = 0;
        r3 = r16;
    L_0x0044:
        monitor-exit(r17);
        return r4;
    L_0x0046:
        r4 = "<null>";
        goto L_0x0036;
    L_0x0049:
        r4 = -1;
        r0 = r19;
        if (r0 == r4) goto L_0x010b;
    L_0x004e:
        r0 = r19;
        r1 = r17;
        r1.port = r0;	 Catch:{ all -> 0x0138 }
    L_0x0054:
        r0 = r17;
        r4 = r0.port;	 Catch:{ all -> 0x0138 }
        r5 = -1;
        if (r4 != r5) goto L_0x0063;
    L_0x005b:
        r0 = r17;
        r4 = r0.defaultPort;	 Catch:{ all -> 0x0138 }
        r0 = r17;
        r0.port = r4;	 Catch:{ all -> 0x0138 }
    L_0x0063:
        r0 = r17;
        r5 = r0.pool;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        monitor-enter(r5);	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r0 = r17;
        r4 = r0.pool;	 Catch:{ all -> 0x013d }
        r4 = r4.authenticatedConnections;	 Catch:{ all -> 0x013d }
        r14 = r4.isEmpty();	 Catch:{ all -> 0x013d }
        monitor-exit(r5);	 Catch:{ all -> 0x013d }
        if (r14 == 0) goto L_0x017c;
    L_0x0077:
        r3 = new com.sun.mail.imap.protocol.IMAPProtocol;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r0 = r17;
        r4 = r0.name;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r0 = r17;
        r6 = r0.port;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r0 = r17;
        r5 = r0.session;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r7 = r5.getDebug();	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r0 = r17;
        r5 = r0.session;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r8 = r5.getDebugOut();	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r0 = r17;
        r5 = r0.session;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r9 = r5.getProperties();	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r0 = r17;
        r10 = r0.isSSL;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r5 = r18;
        r3.m646init(r4, r5, r6, r7, r8, r9, r10);	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
        r0 = r17;
        r4 = r0.debug;	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        if (r4 == 0) goto L_0x00d2;
    L_0x00a8:
        r0 = r17;
        r4 = r0.out;	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r5 = new java.lang.StringBuilder;	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r6 = "DEBUG: protocolConnect login, host=";
        r5.<init>(r6);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r0 = r18;
        r5 = r5.append(r0);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r6 = ", user=";
        r5 = r5.append(r6);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r0 = r20;
        r5 = r5.append(r0);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r6 = ", password=<non-null>";
        r5 = r5.append(r6);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r5 = r5.toString();	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r4.println(r5);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
    L_0x00d2:
        r0 = r17;
        r1 = r20;
        r2 = r21;
        r0.login(r3, r1, r2);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r0 = r17;
        r3.addResponseHandler(r0);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r0 = r18;
        r1 = r17;
        r1.host = r0;	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r0 = r20;
        r1 = r17;
        r1.user = r0;	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r0 = r21;
        r1 = r17;
        r1.password = r0;	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r0 = r17;
        r5 = r0.pool;	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        monitor-enter(r5);	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
        r0 = r17;
        r4 = r0.pool;	 Catch:{ all -> 0x0159 }
        r4 = r4.authenticatedConnections;	 Catch:{ all -> 0x0159 }
        r4.addElement(r3);	 Catch:{ all -> 0x0159 }
        monitor-exit(r5);	 Catch:{ all -> 0x0159 }
    L_0x0103:
        r4 = 1;
        r0 = r17;
        r0.connected = r4;	 Catch:{ all -> 0x0157 }
        r4 = 1;
        goto L_0x0044;
    L_0x010b:
        r0 = r17;
        r4 = r0.session;	 Catch:{ all -> 0x0138 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0138 }
        r6 = "mail.";
        r5.<init>(r6);	 Catch:{ all -> 0x0138 }
        r0 = r17;
        r6 = r0.name;	 Catch:{ all -> 0x0138 }
        r5 = r5.append(r6);	 Catch:{ all -> 0x0138 }
        r6 = ".port";
        r5 = r5.append(r6);	 Catch:{ all -> 0x0138 }
        r5 = r5.toString();	 Catch:{ all -> 0x0138 }
        r15 = r4.getProperty(r5);	 Catch:{ all -> 0x0138 }
        if (r15 == 0) goto L_0x0054;
    L_0x012e:
        r4 = java.lang.Integer.parseInt(r15);	 Catch:{ all -> 0x0138 }
        r0 = r17;
        r0.port = r4;	 Catch:{ all -> 0x0138 }
        goto L_0x0054;
    L_0x0138:
        r4 = move-exception;
        r3 = r16;
    L_0x013b:
        monitor-exit(r17);
        throw r4;
    L_0x013d:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x013d }
        throw r4;	 Catch:{ CommandFailedException -> 0x0140, ProtocolException -> 0x015e, IOException -> 0x016b }
    L_0x0140:
        r11 = move-exception;
        r3 = r16;
    L_0x0143:
        if (r3 == 0) goto L_0x0148;
    L_0x0145:
        r3.disconnect();	 Catch:{ all -> 0x0157 }
    L_0x0148:
        r3 = 0;
        r4 = new javax.mail.AuthenticationFailedException;	 Catch:{ all -> 0x0157 }
        r5 = r11.getResponse();	 Catch:{ all -> 0x0157 }
        r5 = r5.getRest();	 Catch:{ all -> 0x0157 }
        r4.m688init(r5);	 Catch:{ all -> 0x0157 }
        throw r4;	 Catch:{ all -> 0x0157 }
    L_0x0157:
        r4 = move-exception;
        goto L_0x013b;
    L_0x0159:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0159 }
        throw r4;	 Catch:{ CommandFailedException -> 0x015c, ProtocolException -> 0x017a, IOException -> 0x0178 }
    L_0x015c:
        r11 = move-exception;
        goto L_0x0143;
    L_0x015e:
        r13 = move-exception;
        r3 = r16;
    L_0x0161:
        r4 = new javax.mail.MessagingException;	 Catch:{ all -> 0x0157 }
        r5 = r13.getMessage();	 Catch:{ all -> 0x0157 }
        r4.m404init(r5, r13);	 Catch:{ all -> 0x0157 }
        throw r4;	 Catch:{ all -> 0x0157 }
    L_0x016b:
        r12 = move-exception;
        r3 = r16;
    L_0x016e:
        r4 = new javax.mail.MessagingException;	 Catch:{ all -> 0x0157 }
        r5 = r12.getMessage();	 Catch:{ all -> 0x0157 }
        r4.m404init(r5, r12);	 Catch:{ all -> 0x0157 }
        throw r4;	 Catch:{ all -> 0x0157 }
    L_0x0178:
        r12 = move-exception;
        goto L_0x016e;
    L_0x017a:
        r13 = move-exception;
        goto L_0x0161;
    L_0x017c:
        r3 = r16;
        goto L_0x0103;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.protocolConnect(java.lang.String, int, java.lang.String, java.lang.String):boolean");
    }

    private void login(IMAPProtocol p, String u, String pw) throws ProtocolException {
        if (this.enableStartTLS && p.hasCapability("STARTTLS")) {
            p.startTLS();
            p.capability();
        }
        if (!p.isAuthenticated()) {
            String authzid;
            p.getCapabilities().put("__PRELOGIN__", MMMailContentUtil.MM_MESSAGE_SUBJECT);
            if (this.authorizationID != null) {
                authzid = this.authorizationID;
            } else if (this.proxyAuthUser != null) {
                authzid = this.proxyAuthUser;
            } else {
                authzid = u;
            }
            if (this.enableSASL) {
                p.sasllogin(this.saslMechanisms, this.saslRealm, authzid, u, pw);
            }
            if (!p.isAuthenticated()) {
                if (p.hasCapability("AUTH=PLAIN") && !this.disableAuthPlain) {
                    p.authplain(authzid, u, pw);
                } else if ((p.hasCapability("AUTH-LOGIN") || p.hasCapability("AUTH=LOGIN")) && !this.disableAuthLogin) {
                    p.authlogin(u, pw);
                } else if (p.hasCapability("LOGINDISABLED")) {
                    throw new ProtocolException("No login methods supported!");
                } else {
                    p.login(u, pw);
                }
            }
            if (this.proxyAuthUser != null) {
                p.proxyauth(this.proxyAuthUser);
            }
            if (p.hasCapability("__PRELOGIN__")) {
                try {
                    p.capability();
                } catch (ConnectionException cex) {
                    throw cex;
                } catch (ProtocolException e) {
                }
            }
        }
    }

    public synchronized void setUsername(String user) {
        this.user = user;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00bd A:{SYNTHETIC, Splitter:B:30:0x00bd} */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0138 A:{Catch:{ all -> 0x00c5 }} */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00cf A:{SYNTHETIC, Splitter:B:41:0x00cf} */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00bd A:{SYNTHETIC, Splitter:B:30:0x00bd} */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0138 A:{Catch:{ all -> 0x00c5 }} */
    public com.sun.mail.imap.protocol.IMAPProtocol getProtocol(com.sun.mail.imap.IMAPFolder r20) throws javax.mail.MessagingException {
        /*
        r19 = this;
        r3 = 0;
        r15 = r3;
    L_0x0002:
        if (r15 == 0) goto L_0x0005;
    L_0x0004:
        return r15;
    L_0x0005:
        r0 = r19;
        r0 = r0.pool;
        r18 = r0;
        monitor-enter(r18);
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x0170 }
        r1 = r1.authenticatedConnections;	 Catch:{ all -> 0x0170 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0170 }
        if (r1 != 0) goto L_0x003d;
    L_0x001a:
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x0170 }
        r1 = r1.authenticatedConnections;	 Catch:{ all -> 0x0170 }
        r1 = r1.size();	 Catch:{ all -> 0x0170 }
        r4 = 1;
        if (r1 != r4) goto L_0x00d4;
    L_0x0029:
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x0170 }
        r1 = r1.separateStoreConnection;	 Catch:{ all -> 0x0170 }
        if (r1 != 0) goto L_0x003d;
    L_0x0033:
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x0170 }
        r1 = r1.storeConnectionInUse;	 Catch:{ all -> 0x0170 }
        if (r1 == 0) goto L_0x00d4;
    L_0x003d:
        r0 = r19;
        r1 = r0.debug;	 Catch:{ all -> 0x0170 }
        if (r1 == 0) goto L_0x004c;
    L_0x0043:
        r0 = r19;
        r1 = r0.out;	 Catch:{ all -> 0x0170 }
        r4 = "DEBUG: no connections in the pool, creating a new one";
        r1.println(r4);	 Catch:{ all -> 0x0170 }
    L_0x004c:
        r0 = r19;
        r1 = r0.forcePasswordRefresh;	 Catch:{ Exception -> 0x00cb }
        if (r1 == 0) goto L_0x0081;
    L_0x0052:
        r0 = r19;
        r1 = r0.host;	 Catch:{ UnknownHostException -> 0x00c8 }
        r2 = java.net.InetAddress.getByName(r1);	 Catch:{ UnknownHostException -> 0x00c8 }
    L_0x005a:
        r0 = r19;
        r1 = r0.session;	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r3 = r0.port;	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r4 = r0.name;	 Catch:{ Exception -> 0x00cb }
        r5 = 0;
        r0 = r19;
        r6 = r0.user;	 Catch:{ Exception -> 0x00cb }
        r16 = r1.requestPasswordAuthentication(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x00cb }
        if (r16 == 0) goto L_0x0081;
    L_0x0071:
        r1 = r16.getUserName();	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r0.user = r1;	 Catch:{ Exception -> 0x00cb }
        r1 = r16.getPassword();	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r0.password = r1;	 Catch:{ Exception -> 0x00cb }
    L_0x0081:
        r3 = new com.sun.mail.imap.protocol.IMAPProtocol;	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r4 = r0.name;	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r5 = r0.host;	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r6 = r0.port;	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r1 = r0.session;	 Catch:{ Exception -> 0x00cb }
        r7 = r1.getDebug();	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r1 = r0.session;	 Catch:{ Exception -> 0x00cb }
        r8 = r1.getDebugOut();	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r1 = r0.session;	 Catch:{ Exception -> 0x00cb }
        r9 = r1.getProperties();	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r10 = r0.isSSL;	 Catch:{ Exception -> 0x00cb }
        r3.m646init(r4, r5, r6, r7, r8, r9, r10);	 Catch:{ Exception -> 0x00cb }
        r0 = r19;
        r1 = r0.user;	 Catch:{ Exception -> 0x0176 }
        r0 = r19;
        r4 = r0.password;	 Catch:{ Exception -> 0x0176 }
        r0 = r19;
        r0.login(r3, r1, r4);	 Catch:{ Exception -> 0x0176 }
    L_0x00bb:
        if (r3 != 0) goto L_0x0133;
    L_0x00bd:
        r1 = new javax.mail.MessagingException;	 Catch:{ all -> 0x00c5 }
        r4 = "connection failure";
        r1.m403init(r4);	 Catch:{ all -> 0x00c5 }
        throw r1;	 Catch:{ all -> 0x00c5 }
    L_0x00c5:
        r1 = move-exception;
    L_0x00c6:
        monitor-exit(r18);	 Catch:{ all -> 0x00c5 }
        throw r1;
    L_0x00c8:
        r11 = move-exception;
        r2 = 0;
        goto L_0x005a;
    L_0x00cb:
        r12 = move-exception;
        r3 = r15;
    L_0x00cd:
        if (r3 == 0) goto L_0x00d2;
    L_0x00cf:
        r3.disconnect();	 Catch:{ Exception -> 0x016d }
    L_0x00d2:
        r3 = 0;
        goto L_0x00bb;
    L_0x00d4:
        r0 = r19;
        r1 = r0.debug;	 Catch:{ all -> 0x0170 }
        if (r1 == 0) goto L_0x00fc;
    L_0x00da:
        r0 = r19;
        r1 = r0.out;	 Catch:{ all -> 0x0170 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0170 }
        r5 = "DEBUG: connection available -- size: ";
        r4.<init>(r5);	 Catch:{ all -> 0x0170 }
        r0 = r19;
        r5 = r0.pool;	 Catch:{ all -> 0x0170 }
        r5 = r5.authenticatedConnections;	 Catch:{ all -> 0x0170 }
        r5 = r5.size();	 Catch:{ all -> 0x0170 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0170 }
        r4 = r4.toString();	 Catch:{ all -> 0x0170 }
        r1.println(r4);	 Catch:{ all -> 0x0170 }
    L_0x00fc:
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x0170 }
        r1 = r1.authenticatedConnections;	 Catch:{ all -> 0x0170 }
        r3 = r1.lastElement();	 Catch:{ all -> 0x0170 }
        r3 = (com.sun.mail.imap.protocol.IMAPProtocol) r3;	 Catch:{ all -> 0x0170 }
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x00c5 }
        r1 = r1.authenticatedConnections;	 Catch:{ all -> 0x00c5 }
        r1.removeElement(r3);	 Catch:{ all -> 0x00c5 }
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x00c5 }
        r6 = r3.getTimestamp();	 Catch:{ all -> 0x00c5 }
        r13 = r4 - r6;
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x00c5 }
        r4 = r1.serverTimeoutInterval;	 Catch:{ all -> 0x00c5 }
        r1 = (r13 > r4 ? 1 : (r13 == r4 ? 0 : -1));
        if (r1 <= 0) goto L_0x012e;
    L_0x012b:
        r3.noop();	 Catch:{ ProtocolException -> 0x015f }
    L_0x012e:
        r0 = r19;
        r3.removeResponseHandler(r0);	 Catch:{ all -> 0x00c5 }
    L_0x0133:
        r19.timeoutConnections();	 Catch:{ all -> 0x00c5 }
        if (r20 == 0) goto L_0x015b;
    L_0x0138:
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x00c5 }
        r1 = r1.folders;	 Catch:{ all -> 0x00c5 }
        if (r1 != 0) goto L_0x014e;
    L_0x0142:
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x00c5 }
        r4 = new java.util.Vector;	 Catch:{ all -> 0x00c5 }
        r4.<init>();	 Catch:{ all -> 0x00c5 }
        r1.folders = r4;	 Catch:{ all -> 0x00c5 }
    L_0x014e:
        r0 = r19;
        r1 = r0.pool;	 Catch:{ all -> 0x00c5 }
        r1 = r1.folders;	 Catch:{ all -> 0x00c5 }
        r0 = r20;
        r1.addElement(r0);	 Catch:{ all -> 0x00c5 }
    L_0x015b:
        monitor-exit(r18);	 Catch:{ all -> 0x00c5 }
        r15 = r3;
        goto L_0x0002;
    L_0x015f:
        r17 = move-exception;
        r0 = r19;
        r3.removeResponseHandler(r0);	 Catch:{ all -> 0x0174 }
        r3.disconnect();	 Catch:{ all -> 0x0174 }
    L_0x0168:
        r3 = 0;
        monitor-exit(r18);	 Catch:{ all -> 0x00c5 }
        r15 = r3;
        goto L_0x0002;
    L_0x016d:
        r1 = move-exception;
        goto L_0x00d2;
    L_0x0170:
        r1 = move-exception;
        r3 = r15;
        goto L_0x00c6;
    L_0x0174:
        r1 = move-exception;
        goto L_0x0168;
    L_0x0176:
        r12 = move-exception;
        goto L_0x00cd;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.getProtocol(com.sun.mail.imap.IMAPFolder):com.sun.mail.imap.protocol.IMAPProtocol");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0062 A:{SYNTHETIC, Splitter:B:28:0x0062} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x004e A:{SYNTHETIC, Splitter:B:16:0x004e} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x005d A:{SYNTHETIC, Splitter:B:25:0x005d} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x004e A:{SYNTHETIC, Splitter:B:16:0x004e} */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0062 A:{SYNTHETIC, Splitter:B:28:0x0062} */
    /* JADX WARNING: Missing block: B:38:0x0080, code skipped:
            r9 = r0;
     */
    public com.sun.mail.imap.protocol.IMAPProtocol getStoreProtocol() throws com.sun.mail.iap.ProtocolException {
        /*
        r11 = this;
        r0 = 0;
        r9 = r0;
    L_0x0002:
        if (r9 == 0) goto L_0x0005;
    L_0x0004:
        return r9;
    L_0x0005:
        r10 = r11.pool;
        monitor-enter(r10);
        r11.waitIfIdle();	 Catch:{ all -> 0x00cd }
        r1 = r11.pool;	 Catch:{ all -> 0x00cd }
        r1 = r1.authenticatedConnections;	 Catch:{ all -> 0x00cd }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x00cd }
        if (r1 == 0) goto L_0x0082;
    L_0x0017:
        r1 = r11.pool;	 Catch:{ all -> 0x00cd }
        r1 = r1.debug;	 Catch:{ all -> 0x00cd }
        if (r1 == 0) goto L_0x0026;
    L_0x001f:
        r1 = r11.out;	 Catch:{ all -> 0x00cd }
        r2 = "DEBUG: getStoreProtocol() - no connections in the pool, creating a new one";
        r1.println(r2);	 Catch:{ all -> 0x00cd }
    L_0x0026:
        r0 = new com.sun.mail.imap.protocol.IMAPProtocol;	 Catch:{ Exception -> 0x0059 }
        r1 = r11.name;	 Catch:{ Exception -> 0x0059 }
        r2 = r11.host;	 Catch:{ Exception -> 0x0059 }
        r3 = r11.port;	 Catch:{ Exception -> 0x0059 }
        r4 = r11.session;	 Catch:{ Exception -> 0x0059 }
        r4 = r4.getDebug();	 Catch:{ Exception -> 0x0059 }
        r5 = r11.session;	 Catch:{ Exception -> 0x0059 }
        r5 = r5.getDebugOut();	 Catch:{ Exception -> 0x0059 }
        r6 = r11.session;	 Catch:{ Exception -> 0x0059 }
        r6 = r6.getProperties();	 Catch:{ Exception -> 0x0059 }
        r7 = r11.isSSL;	 Catch:{ Exception -> 0x0059 }
        r0.m646init(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0059 }
        r1 = r11.user;	 Catch:{ Exception -> 0x00d2 }
        r2 = r11.password;	 Catch:{ Exception -> 0x00d2 }
        r11.login(r0, r1, r2);	 Catch:{ Exception -> 0x00d2 }
    L_0x004c:
        if (r0 != 0) goto L_0x0062;
    L_0x004e:
        r1 = new com.sun.mail.iap.ConnectionException;	 Catch:{ all -> 0x0056 }
        r2 = "failed to create new store connection";
        r1.m603init(r2);	 Catch:{ all -> 0x0056 }
        throw r1;	 Catch:{ all -> 0x0056 }
    L_0x0056:
        r1 = move-exception;
    L_0x0057:
        monitor-exit(r10);	 Catch:{ all -> 0x0056 }
        throw r1;
    L_0x0059:
        r8 = move-exception;
        r0 = r9;
    L_0x005b:
        if (r0 == 0) goto L_0x0060;
    L_0x005d:
        r0.logout();	 Catch:{ Exception -> 0x00cb }
    L_0x0060:
        r0 = 0;
        goto L_0x004c;
    L_0x0062:
        r0.addResponseHandler(r11);	 Catch:{ all -> 0x0056 }
        r1 = r11.pool;	 Catch:{ all -> 0x0056 }
        r1 = r1.authenticatedConnections;	 Catch:{ all -> 0x0056 }
        r1.addElement(r0);	 Catch:{ all -> 0x0056 }
    L_0x006e:
        r1 = r11.pool;	 Catch:{ all -> 0x0056 }
        r1 = r1.storeConnectionInUse;	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x00b5;
    L_0x0076:
        r0 = 0;
        r1 = r11.pool;	 Catch:{ InterruptedException -> 0x00d0 }
        r1.wait();	 Catch:{ InterruptedException -> 0x00d0 }
    L_0x007c:
        r11.timeoutConnections();	 Catch:{ all -> 0x0056 }
        monitor-exit(r10);	 Catch:{ all -> 0x0056 }
        r9 = r0;
        goto L_0x0002;
    L_0x0082:
        r1 = r11.pool;	 Catch:{ all -> 0x00cd }
        r1 = r1.debug;	 Catch:{ all -> 0x00cd }
        if (r1 == 0) goto L_0x00a8;
    L_0x008a:
        r1 = r11.out;	 Catch:{ all -> 0x00cd }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cd }
        r3 = "DEBUG: getStoreProtocol() - connection available -- size: ";
        r2.<init>(r3);	 Catch:{ all -> 0x00cd }
        r3 = r11.pool;	 Catch:{ all -> 0x00cd }
        r3 = r3.authenticatedConnections;	 Catch:{ all -> 0x00cd }
        r3 = r3.size();	 Catch:{ all -> 0x00cd }
        r2 = r2.append(r3);	 Catch:{ all -> 0x00cd }
        r2 = r2.toString();	 Catch:{ all -> 0x00cd }
        r1.println(r2);	 Catch:{ all -> 0x00cd }
    L_0x00a8:
        r1 = r11.pool;	 Catch:{ all -> 0x00cd }
        r1 = r1.authenticatedConnections;	 Catch:{ all -> 0x00cd }
        r0 = r1.firstElement();	 Catch:{ all -> 0x00cd }
        r0 = (com.sun.mail.imap.protocol.IMAPProtocol) r0;	 Catch:{ all -> 0x00cd }
        goto L_0x006e;
    L_0x00b5:
        r1 = r11.pool;	 Catch:{ all -> 0x0056 }
        r2 = 1;
        r1.storeConnectionInUse = r2;	 Catch:{ all -> 0x0056 }
        r1 = r11.pool;	 Catch:{ all -> 0x0056 }
        r1 = r1.debug;	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x007c;
    L_0x00c3:
        r1 = r11.out;	 Catch:{ all -> 0x0056 }
        r2 = "DEBUG: getStoreProtocol() -- storeConnectionInUse";
        r1.println(r2);	 Catch:{ all -> 0x0056 }
        goto L_0x007c;
    L_0x00cb:
        r1 = move-exception;
        goto L_0x0060;
    L_0x00cd:
        r1 = move-exception;
        r0 = r9;
        goto L_0x0057;
    L_0x00d0:
        r1 = move-exception;
        goto L_0x007c;
    L_0x00d2:
        r8 = move-exception;
        goto L_0x005b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.getStoreProtocol():com.sun.mail.imap.protocol.IMAPProtocol");
    }

    /* access modifiers changed from: 0000 */
    public boolean allowReadOnlySelect() {
        String s = this.session.getProperty("mail." + this.name + ".allowreadonlyselect");
        return s != null && s.equalsIgnoreCase("true");
    }

    /* access modifiers changed from: 0000 */
    public boolean hasSeparateStoreConnection() {
        return this.pool.separateStoreConnection;
    }

    /* access modifiers changed from: 0000 */
    public boolean getConnectionPoolDebug() {
        return this.pool.debug;
    }

    /* access modifiers changed from: 0000 */
    public boolean isConnectionPoolFull() {
        boolean z;
        synchronized (this.pool) {
            if (this.pool.debug) {
                this.out.println("DEBUG: current size: " + this.pool.authenticatedConnections.size() + "   pool size: " + this.pool.poolSize);
            }
            z = this.pool.authenticatedConnections.size() >= this.pool.poolSize;
        }
        return z;
    }

    /* access modifiers changed from: 0000 */
    public void releaseProtocol(IMAPFolder folder, IMAPProtocol protocol) {
        synchronized (this.pool) {
            if (protocol != null) {
                if (isConnectionPoolFull()) {
                    if (this.debug) {
                        this.out.println("DEBUG: pool is full, not adding an Authenticated connection");
                    }
                    try {
                        protocol.logout();
                    } catch (ProtocolException e) {
                    }
                } else {
                    protocol.addResponseHandler(this);
                    this.pool.authenticatedConnections.addElement(protocol);
                    if (this.debug) {
                        this.out.println("DEBUG: added an Authenticated connection -- size: " + this.pool.authenticatedConnections.size());
                    }
                }
            }
            if (this.pool.folders != null) {
                this.pool.folders.removeElement(folder);
            }
            timeoutConnections();
        }
    }

    /* access modifiers changed from: 0000 */
    public void releaseStoreProtocol(IMAPProtocol protocol) {
        if (protocol != null) {
            synchronized (this.pool) {
                this.pool.storeConnectionInUse = false;
                this.pool.notifyAll();
                if (this.pool.debug) {
                    this.out.println("DEBUG: releaseStoreProtocol()");
                }
                timeoutConnections();
            }
        }
    }

    private void emptyConnectionPool(boolean force) {
        synchronized (this.pool) {
            for (int index = this.pool.authenticatedConnections.size() - 1; index >= 0; index--) {
                try {
                    IMAPProtocol p = (IMAPProtocol) this.pool.authenticatedConnections.elementAt(index);
                    p.removeResponseHandler(this);
                    if (force) {
                        p.disconnect();
                    } else {
                        p.logout();
                    }
                } catch (ProtocolException e) {
                }
            }
            this.pool.authenticatedConnections.removeAllElements();
        }
        if (this.pool.debug) {
            this.out.println("DEBUG: removed all authenticated connections");
        }
    }

    private void timeoutConnections() {
        synchronized (this.pool) {
            if (System.currentTimeMillis() - this.pool.lastTimePruned > this.pool.pruningInterval && this.pool.authenticatedConnections.size() > 1) {
                if (this.pool.debug) {
                    this.out.println("DEBUG: checking for connections to prune: " + (System.currentTimeMillis() - this.pool.lastTimePruned));
                    this.out.println("DEBUG: clientTimeoutInterval: " + this.pool.clientTimeoutInterval);
                }
                for (int index = this.pool.authenticatedConnections.size() - 1; index > 0; index--) {
                    IMAPProtocol p = (IMAPProtocol) this.pool.authenticatedConnections.elementAt(index);
                    if (this.pool.debug) {
                        this.out.println("DEBUG: protocol last used: " + (System.currentTimeMillis() - p.getTimestamp()));
                    }
                    if (System.currentTimeMillis() - p.getTimestamp() > this.pool.clientTimeoutInterval) {
                        if (this.pool.debug) {
                            this.out.println("DEBUG: authenticated connection timed out");
                            this.out.println("DEBUG: logging out the connection");
                        }
                        p.removeResponseHandler(this);
                        this.pool.authenticatedConnections.removeElementAt(index);
                        try {
                            p.logout();
                        } catch (ProtocolException e) {
                        }
                    }
                }
                this.pool.lastTimePruned = System.currentTimeMillis();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public int getFetchBlockSize() {
        return this.blksize;
    }

    /* access modifiers changed from: 0000 */
    public Session getSession() {
        return this.session;
    }

    /* access modifiers changed from: 0000 */
    public int getStatusCacheTimeout() {
        return this.statusCacheTimeout;
    }

    /* access modifiers changed from: 0000 */
    public int getAppendBufferSize() {
        return this.appendBufferSize;
    }

    /* access modifiers changed from: 0000 */
    public int getMinIdleTime() {
        return this.minIdleTime;
    }

    public synchronized boolean hasCapability(String capability) throws MessagingException {
        boolean hasCapability;
        IMAPProtocol p = null;
        try {
            p = getStoreProtocol();
            hasCapability = p.hasCapability(capability);
            releaseStoreProtocol(p);
        } catch (ProtocolException pex) {
            if (p == null) {
                cleanup();
            }
            throw new MessagingException(pex.getMessage(), pex);
        } catch (Throwable th) {
            releaseStoreProtocol(p);
        }
        return hasCapability;
    }

    public synchronized boolean isConnected() {
        boolean z = false;
        synchronized (this) {
            if (this.connected) {
                IMAPProtocol p = null;
                try {
                    p = getStoreProtocol();
                    p.noop();
                    releaseStoreProtocol(p);
                } catch (ProtocolException e) {
                    if (p == null) {
                        cleanup();
                    }
                    releaseStoreProtocol(p);
                } catch (Throwable th) {
                    releaseStoreProtocol(p);
                }
                z = super.isConnected();
            } else {
                super.setConnected(false);
            }
        }
        return z;
    }

    public synchronized void close() throws MessagingException {
        if (super.isConnected()) {
            IMAPProtocol protocol = null;
            try {
                boolean isEmpty;
                synchronized (this.pool) {
                    isEmpty = this.pool.authenticatedConnections.isEmpty();
                }
                if (isEmpty) {
                    if (this.pool.debug) {
                        this.out.println("DEBUG: close() - no connections ");
                    }
                    cleanup();
                    releaseStoreProtocol(null);
                } else {
                    protocol = getStoreProtocol();
                    synchronized (this.pool) {
                        this.pool.authenticatedConnections.removeElement(protocol);
                    }
                    protocol.logout();
                    releaseStoreProtocol(protocol);
                }
            } catch (ProtocolException pex) {
                try {
                    cleanup();
                    throw new MessagingException(pex.getMessage(), pex);
                } catch (Throwable th) {
                    releaseStoreProtocol(protocol);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        close();
    }

    private void cleanup() {
        cleanup(false);
    }

    private void cleanup(boolean force) {
        if (this.debug) {
            this.out.println("DEBUG: IMAPStore cleanup, force " + force);
        }
        Vector foldersCopy = null;
        while (true) {
            boolean done;
            synchronized (this.pool) {
                if (this.pool.folders != null) {
                    done = false;
                    foldersCopy = this.pool.folders;
                    this.pool.folders = null;
                } else {
                    done = true;
                }
            }
            if (done) {
                synchronized (this.pool) {
                    emptyConnectionPool(force);
                }
                this.connected = false;
                notifyConnectionListeners(3);
                if (this.debug) {
                    this.out.println("DEBUG: IMAPStore cleanup done");
                    return;
                }
                return;
            }
            int fsize = foldersCopy.size();
            for (int i = 0; i < fsize; i++) {
                IMAPFolder f = (IMAPFolder) foldersCopy.elementAt(i);
                if (force) {
                    try {
                        if (this.debug) {
                            this.out.println("DEBUG: force folder to close");
                        }
                        f.forceClose();
                    } catch (IllegalStateException | MessagingException e) {
                    }
                } else {
                    if (this.debug) {
                        this.out.println("DEBUG: close folder");
                    }
                    f.close(false);
                }
            }
        }
        while (true) {
        }
    }

    public synchronized Folder getDefaultFolder() throws MessagingException {
        checkConnected();
        return new DefaultFolder(this);
    }

    public synchronized Folder getFolder(String name) throws MessagingException {
        checkConnected();
        return new IMAPFolder(name, 65535, this);
    }

    public synchronized Folder getFolder(URLName url) throws MessagingException {
        checkConnected();
        return new IMAPFolder(url.getFile(), 65535, this);
    }

    public Folder[] getPersonalNamespaces() throws MessagingException {
        Namespaces ns = getNamespaces();
        if (ns == null || ns.personal == null) {
            return super.getPersonalNamespaces();
        }
        return namespaceToFolders(ns.personal, null);
    }

    public Folder[] getUserNamespaces(String user) throws MessagingException {
        Namespaces ns = getNamespaces();
        if (ns == null || ns.otherUsers == null) {
            return super.getUserNamespaces(user);
        }
        return namespaceToFolders(ns.otherUsers, user);
    }

    public Folder[] getSharedNamespaces() throws MessagingException {
        Namespaces ns = getNamespaces();
        if (ns == null || ns.shared == null) {
            return super.getSharedNamespaces();
        }
        return namespaceToFolders(ns.shared, null);
    }

    private synchronized Namespaces getNamespaces() throws MessagingException {
        checkConnected();
        IMAPProtocol p = null;
        if (this.namespaces == null) {
            try {
                p = getStoreProtocol();
                this.namespaces = p.namespace();
                releaseStoreProtocol(p);
                if (p == null) {
                    cleanup();
                }
            } catch (BadCommandException e) {
                releaseStoreProtocol(p);
                if (p == null) {
                    cleanup();
                }
            } catch (ConnectionException cex) {
                throw new StoreClosedException(this, cex.getMessage());
            } catch (ProtocolException pex) {
                throw new MessagingException(pex.getMessage(), pex);
            } catch (Throwable th) {
                releaseStoreProtocol(p);
                if (p == null) {
                    cleanup();
                }
            }
        }
        return this.namespaces;
    }

    private Folder[] namespaceToFolders(Namespace[] ns, String user) {
        Folder[] fa = new Folder[ns.length];
        int i = 0;
        while (i < fa.length) {
            boolean z;
            String name = ns[i].prefix;
            if (user == null) {
                int len = name.length();
                if (len > 0 && name.charAt(len - 1) == ns[i].delimiter) {
                    name = name.substring(0, len - 1);
                }
            } else {
                name = new StringBuilder(String.valueOf(name)).append(user).toString();
            }
            char c = ns[i].delimiter;
            if (user == null) {
                z = true;
            } else {
                z = false;
            }
            fa[i] = new IMAPFolder(name, c, this, z);
            i++;
        }
        return fa;
    }

    public synchronized Quota[] getQuota(String root) throws MessagingException {
        Quota[] qa;
        checkConnected();
        qa = null;
        IMAPProtocol p = null;
        try {
            p = getStoreProtocol();
            qa = p.getQuotaRoot(root);
            releaseStoreProtocol(p);
            if (p == null) {
                cleanup();
            }
        } catch (BadCommandException bex) {
            throw new MessagingException("QUOTA not supported", bex);
        } catch (ConnectionException cex) {
            throw new StoreClosedException(this, cex.getMessage());
        } catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        } catch (Throwable th) {
            releaseStoreProtocol(p);
            if (p == null) {
                cleanup();
            }
        }
        return qa;
    }

    public synchronized void setQuota(Quota quota) throws MessagingException {
        checkConnected();
        IMAPProtocol p = null;
        try {
            p = getStoreProtocol();
            p.setQuota(quota);
            releaseStoreProtocol(p);
            if (p == null) {
                cleanup();
            }
        } catch (BadCommandException bex) {
            throw new MessagingException("QUOTA not supported", bex);
        } catch (ConnectionException cex) {
            throw new StoreClosedException(this, cex.getMessage());
        } catch (ProtocolException pex) {
            throw new MessagingException(pex.getMessage(), pex);
        } catch (Throwable th) {
            releaseStoreProtocol(p);
            if (p == null) {
                cleanup();
            }
        }
    }

    private void checkConnected() {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (!this.connected) {
            super.setConnected(false);
            throw new IllegalStateException("Not connected");
        }
    }

    public void handleResponse(Response r) {
        if (r.isOK() || r.isNO() || r.isBAD() || r.isBYE()) {
            handleResponseCode(r);
        }
        if (r.isBYE()) {
            if (this.debug) {
                this.out.println("DEBUG: IMAPStore connection dead");
            }
            if (this.connected) {
                cleanup(r.isSynthetic());
            }
        }
    }

    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r5 = r3.readIdleResponse();
            r7 = r10.pool;
     */
    /* JADX WARNING: Missing block: B:20:0x003c, code skipped:
            monitor-enter(r7);
     */
    /* JADX WARNING: Missing block: B:21:0x003d, code skipped:
            if (r5 == null) goto L_0x0045;
     */
    /* JADX WARNING: Missing block: B:24:0x0043, code skipped:
            if (r3.processIdleResponse(r5) != false) goto L_0x00ad;
     */
    /* JADX WARNING: Missing block: B:25:0x0045, code skipped:
            com.sun.mail.imap.IMAPStore.ConnectionPool.access$20(r10.pool, 0);
            r10.pool.notifyAll();
     */
    /* JADX WARNING: Missing block: B:26:0x0050, code skipped:
            monitor-exit(r7);
     */
    /* JADX WARNING: Missing block: B:28:?, code skipped:
            r2 = getMinIdleTime();
     */
    /* JADX WARNING: Missing block: B:29:0x0055, code skipped:
            if (r2 <= 0) goto L_0x005b;
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            java.lang.Thread.sleep((long) r2);
     */
    /* JADX WARNING: Missing block: B:49:0x0077, code skipped:
            r7 = r10.pool;
     */
    /* JADX WARNING: Missing block: B:50:0x0079, code skipped:
            monitor-enter(r7);
     */
    /* JADX WARNING: Missing block: B:52:?, code skipped:
            com.sun.mail.imap.IMAPStore.ConnectionPool.access$18(r10.pool, null);
     */
    /* JADX WARNING: Missing block: B:53:0x0080, code skipped:
            monitor-exit(r7);
     */
    /* JADX WARNING: Missing block: B:54:0x0081, code skipped:
            releaseStoreProtocol(r3);
     */
    /* JADX WARNING: Missing block: B:55:0x0084, code skipped:
            if (r3 != null) goto L_?;
     */
    /* JADX WARNING: Missing block: B:56:0x0086, code skipped:
            cleanup();
     */
    /* JADX WARNING: Missing block: B:81:?, code skipped:
            monitor-exit(r7);
     */
    /* JADX WARNING: Missing block: B:84:0x00b0, code skipped:
            if (r10.enableImapEvents == false) goto L_0x0036;
     */
    /* JADX WARNING: Missing block: B:86:0x00b6, code skipped:
            if (r5.isUnTagged() == false) goto L_0x0036;
     */
    /* JADX WARNING: Missing block: B:87:0x00b8, code skipped:
            notifyStoreListeners(RESPONSE, r5.toString());
     */
    /* JADX WARNING: Missing block: B:136:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:137:?, code skipped:
            return;
     */
    public void idle() throws javax.mail.MessagingException {
        /*
        r10 = this;
        r3 = 0;
        r6 = $assertionsDisabled;
        if (r6 != 0) goto L_0x0013;
    L_0x0005:
        r6 = r10.pool;
        r6 = java.lang.Thread.holdsLock(r6);
        if (r6 == 0) goto L_0x0013;
    L_0x000d:
        r6 = new java.lang.AssertionError;
        r6.<init>();
        throw r6;
    L_0x0013:
        monitor-enter(r10);
        r10.checkConnected();	 Catch:{ all -> 0x006e }
        monitor-exit(r10);	 Catch:{ all -> 0x006e }
        r7 = r10.pool;	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        monitor-enter(r7);	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        r3 = r10.getStoreProtocol();	 Catch:{ all -> 0x008d }
        r6 = r10.pool;	 Catch:{ all -> 0x008d }
        r6 = r6.idleState;	 Catch:{ all -> 0x008d }
        if (r6 != 0) goto L_0x0071;
    L_0x0027:
        r3.idleStart();	 Catch:{ all -> 0x008d }
        r6 = r10.pool;	 Catch:{ all -> 0x008d }
        r8 = 1;
        r6.idleState = r8;	 Catch:{ all -> 0x008d }
        r6 = r10.pool;	 Catch:{ all -> 0x008d }
        r6.idleProtocol = r3;	 Catch:{ all -> 0x008d }
        monitor-exit(r7);	 Catch:{ all -> 0x008d }
    L_0x0036:
        r5 = r3.readIdleResponse();	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        r7 = r10.pool;	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        monitor-enter(r7);	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        if (r5 == 0) goto L_0x0045;
    L_0x003f:
        r6 = r3.processIdleResponse(r5);	 Catch:{ all -> 0x00ce }
        if (r6 != 0) goto L_0x00ad;
    L_0x0045:
        r6 = r10.pool;	 Catch:{ all -> 0x00ce }
        r8 = 0;
        r6.idleState = r8;	 Catch:{ all -> 0x00ce }
        r6 = r10.pool;	 Catch:{ all -> 0x00ce }
        r6.notifyAll();	 Catch:{ all -> 0x00ce }
        monitor-exit(r7);	 Catch:{ all -> 0x00ce }
        r2 = r10.getMinIdleTime();	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        if (r2 <= 0) goto L_0x005b;
    L_0x0057:
        r6 = (long) r2;
        java.lang.Thread.sleep(r6);	 Catch:{ InterruptedException -> 0x00e2 }
    L_0x005b:
        r7 = r10.pool;
        monitor-enter(r7);
        r6 = r10.pool;	 Catch:{ all -> 0x00df }
        r8 = 0;
        r6.idleProtocol = r8;	 Catch:{ all -> 0x00df }
        monitor-exit(r7);	 Catch:{ all -> 0x00df }
        r10.releaseStoreProtocol(r3);
        if (r3 != 0) goto L_0x006d;
    L_0x006a:
        r10.cleanup();
    L_0x006d:
        return;
    L_0x006e:
        r6 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x006e }
        throw r6;
    L_0x0071:
        r6 = r10.pool;	 Catch:{ InterruptedException -> 0x00e5 }
        r6.wait();	 Catch:{ InterruptedException -> 0x00e5 }
    L_0x0076:
        monitor-exit(r7);	 Catch:{ all -> 0x008d }
        r7 = r10.pool;
        monitor-enter(r7);
        r6 = r10.pool;	 Catch:{ all -> 0x008a }
        r8 = 0;
        r6.idleProtocol = r8;	 Catch:{ all -> 0x008a }
        monitor-exit(r7);	 Catch:{ all -> 0x008a }
        r10.releaseStoreProtocol(r3);
        if (r3 != 0) goto L_0x006d;
    L_0x0086:
        r10.cleanup();
        goto L_0x006d;
    L_0x008a:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x008a }
        throw r6;
    L_0x008d:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x008d }
        throw r6;	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
    L_0x0090:
        r0 = move-exception;
        r6 = new javax.mail.MessagingException;	 Catch:{ all -> 0x0099 }
        r7 = "IDLE not supported";
        r6.m404init(r7, r0);	 Catch:{ all -> 0x0099 }
        throw r6;	 Catch:{ all -> 0x0099 }
    L_0x0099:
        r6 = move-exception;
        r7 = r10.pool;
        monitor-enter(r7);
        r8 = r10.pool;	 Catch:{ all -> 0x00dc }
        r9 = 0;
        r8.idleProtocol = r9;	 Catch:{ all -> 0x00dc }
        monitor-exit(r7);	 Catch:{ all -> 0x00dc }
        r10.releaseStoreProtocol(r3);
        if (r3 != 0) goto L_0x00ac;
    L_0x00a9:
        r10.cleanup();
    L_0x00ac:
        throw r6;
    L_0x00ad:
        monitor-exit(r7);	 Catch:{ all -> 0x00ce }
        r6 = r10.enableImapEvents;	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        if (r6 == 0) goto L_0x0036;
    L_0x00b2:
        r6 = r5.isUnTagged();	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        if (r6 == 0) goto L_0x0036;
    L_0x00b8:
        r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = r5.toString();	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        r10.notifyStoreListeners(r6, r7);	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
        goto L_0x0036;
    L_0x00c3:
        r1 = move-exception;
        r6 = new javax.mail.StoreClosedException;	 Catch:{ all -> 0x0099 }
        r7 = r1.getMessage();	 Catch:{ all -> 0x0099 }
        r6.m719init(r10, r7);	 Catch:{ all -> 0x0099 }
        throw r6;	 Catch:{ all -> 0x0099 }
    L_0x00ce:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x00ce }
        throw r6;	 Catch:{ BadCommandException -> 0x0090, ConnectionException -> 0x00c3, ProtocolException -> 0x00d1 }
    L_0x00d1:
        r4 = move-exception;
        r6 = new javax.mail.MessagingException;	 Catch:{ all -> 0x0099 }
        r7 = r4.getMessage();	 Catch:{ all -> 0x0099 }
        r6.m404init(r7, r4);	 Catch:{ all -> 0x0099 }
        throw r6;	 Catch:{ all -> 0x0099 }
    L_0x00dc:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x00dc }
        throw r6;
    L_0x00df:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x00df }
        throw r6;
    L_0x00e2:
        r6 = move-exception;
        goto L_0x005b;
    L_0x00e5:
        r6 = move-exception;
        goto L_0x0076;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.mail.imap.IMAPStore.idle():void");
    }

    private void waitIfIdle() throws ProtocolException {
        if ($assertionsDisabled || Thread.holdsLock(this.pool)) {
            while (this.pool.idleState != 0) {
                if (this.pool.idleState == 1) {
                    this.pool.idleProtocol.idleAbort();
                    this.pool.idleState = 2;
                }
                try {
                    this.pool.wait();
                } catch (InterruptedException e) {
                }
            }
            return;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: 0000 */
    public void handleResponseCode(Response r) {
        String s = r.getRest();
        boolean isAlert = false;
        if (s.startsWith("[")) {
            int i = s.indexOf(93);
            if (i > 0 && s.substring(0, i + 1).equalsIgnoreCase("[ALERT]")) {
                isAlert = true;
            }
            s = s.substring(i + 1).trim();
        }
        if (isAlert) {
            notifyStoreListeners(1, s);
        } else if (r.isUnTagged() && s.length() > 0) {
            notifyStoreListeners(2, s);
        }
    }
}
