package org.jivesoftware.smack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;

public class XMPPConnection {
    public static boolean DEBUG_ENABLED;
    private static AtomicInteger connectionCounter = new AtomicInteger(0);
    private static final Set<ConnectionCreationListener> connectionEstablishedListeners = new CopyOnWriteArraySet();
    private AccountManager accountManager = null;
    private boolean anonymous = false;
    private boolean authenticated = false;
    private ChatManager chatManager;
    private Collection compressionMethods;
    private ConnectionConfiguration configuration;
    private boolean connected = false;
    int connectionCounterValue = connectionCounter.getAndIncrement();
    String connectionID = null;
    private SmackDebugger debugger = null;
    String host;
    PacketReader packetReader;
    PacketWriter packetWriter;
    int port;
    Reader reader;
    Roster roster = null;
    private SASLAuthentication saslAuthentication = new SASLAuthentication(this);
    String serviceName;
    Socket socket;
    private String user = null;
    private boolean usingCompression;
    private boolean usingTLS = false;
    private boolean wasAuthenticated = false;
    Writer writer;

    static {
        DEBUG_ENABLED = false;
        try {
            DEBUG_ENABLED = Boolean.getBoolean("smack.debugEnabled");
        } catch (Exception e) {
        }
        SmackConfiguration.getVersion();
    }

    public XMPPConnection(String serviceName) {
        ConnectionConfiguration config = new ConnectionConfiguration(serviceName);
        config.setCompressionEnabled(false);
        config.setSASLAuthenticationEnabled(true);
        config.setDebuggerEnabled(DEBUG_ENABLED);
        this.configuration = config;
    }

    public XMPPConnection(ConnectionConfiguration config) {
        this.configuration = config;
    }

    public String getConnectionID() {
        if (isConnected()) {
            return this.connectionID;
        }
        return null;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getUser() {
        if (isAuthenticated()) {
            return this.user;
        }
        return null;
    }

    public void login(String username, String password) throws XMPPException {
        login(username, password, "Smack");
    }

    public synchronized void login(String username, String password, String resource) throws XMPPException {
        login(username, password, resource, true);
    }

    public synchronized void login(String username, String password, String resource, boolean sendPresence) throws XMPPException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        } else if (this.authenticated) {
            throw new IllegalStateException("Already logged in to server.");
        } else {
            String response;
            username = username.toLowerCase().trim();
            if (this.configuration.isSASLAuthenticationEnabled() && this.saslAuthentication.hasNonAnonymousAuthentication()) {
                response = this.saslAuthentication.authenticate(username, password, resource);
            } else {
                response = new NonSASLAuthentication(this).authenticate(username, password, resource);
            }
            if (response != null) {
                this.user = response;
                this.serviceName = StringUtils.parseServer(response);
            } else {
                this.user = username + "@" + this.serviceName;
                if (resource != null) {
                    this.user += "/" + resource;
                }
            }
            if (this.configuration.isCompressionEnabled()) {
                useCompression();
            }
            if (this.roster == null) {
                this.roster = new Roster(this);
            }
            this.roster.reload();
            if (sendPresence) {
                this.packetWriter.sendPacket(new Presence(Type.available));
            }
            this.authenticated = true;
            this.anonymous = false;
            getConfiguration().setLoginInfo(username, password, resource, sendPresence);
            if (this.configuration.isDebuggerEnabled() && this.debugger != null) {
                this.debugger.userHasLogged(this.user);
            }
        }
    }

    public synchronized void loginAnonymously() throws XMPPException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        } else if (this.authenticated) {
            throw new IllegalStateException("Already logged in to server.");
        } else {
            String response;
            if (this.configuration.isSASLAuthenticationEnabled() && this.saslAuthentication.hasAnonymousAuthentication()) {
                response = this.saslAuthentication.authenticateAnonymously();
            } else {
                response = new NonSASLAuthentication(this).authenticateAnonymously();
            }
            this.user = response;
            this.serviceName = StringUtils.parseServer(response);
            if (this.configuration.isCompressionEnabled()) {
                useCompression();
            }
            this.roster = null;
            this.packetWriter.sendPacket(new Presence(Type.available));
            this.authenticated = true;
            this.anonymous = true;
            if (this.configuration.isDebuggerEnabled() && this.debugger != null) {
                this.debugger.userHasLogged(this.user);
            }
        }
    }

    public Roster getRoster() {
        if (this.roster == null) {
            return null;
        }
        if (!this.roster.rosterInitialized) {
            try {
                synchronized (this.roster) {
                    long waitTime = (long) SmackConfiguration.getPacketReplyTimeout();
                    long start = System.currentTimeMillis();
                    while (!this.roster.rosterInitialized && waitTime > 0) {
                        this.roster.wait(waitTime);
                        long now = System.currentTimeMillis();
                        waitTime -= now - start;
                        start = now;
                    }
                }
            } catch (InterruptedException e) {
            }
        }
        return this.roster;
    }

    public synchronized AccountManager getAccountManager() {
        if (this.accountManager == null) {
            this.accountManager = new AccountManager(this);
        }
        return this.accountManager;
    }

    public synchronized ChatManager getChatManager() {
        if (this.chatManager == null) {
            this.chatManager = new ChatManager(this);
        }
        return this.chatManager;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public boolean isSecureConnection() {
        return isUsingTLS();
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }

    /* access modifiers changed from: protected */
    public void shutdown(Presence unavailablePresence) {
        this.packetWriter.sendPacket(unavailablePresence);
        setWasAuthenticated(this.authenticated);
        this.authenticated = false;
        this.connected = false;
        this.packetReader.shutdown();
        this.packetWriter.shutdown();
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }
        if (this.reader != null) {
            try {
                this.reader.close();
            } catch (Throwable th) {
            }
            this.reader = null;
        }
        if (this.writer != null) {
            try {
                this.writer.close();
            } catch (Throwable th2) {
            }
            this.writer = null;
        }
        try {
            this.socket.close();
        } catch (Exception e2) {
        }
        this.saslAuthentication.init();
    }

    public void disconnect() {
        disconnect(new Presence(Type.unavailable));
    }

    public void disconnect(Presence unavailablePresence) {
        if (this.packetReader != null && this.packetWriter != null) {
            shutdown(unavailablePresence);
            if (this.roster != null) {
                this.roster.cleanup();
                this.roster = null;
            }
            this.wasAuthenticated = false;
            this.packetWriter.cleanup();
            this.packetWriter = null;
            this.packetReader.cleanup();
            this.packetReader = null;
        }
    }

    public void sendPacket(Packet packet) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        } else if (packet == null) {
            throw new NullPointerException("Packet is null.");
        } else {
            this.packetWriter.sendPacket(packet);
        }
    }

    public void addPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (isConnected()) {
            this.packetReader.addPacketListener(packetListener, packetFilter);
            return;
        }
        throw new IllegalStateException("Not connected to server.");
    }

    public void removePacketListener(PacketListener packetListener) {
        this.packetReader.removePacketListener(packetListener);
    }

    public void addPacketWriterListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (isConnected()) {
            this.packetWriter.addPacketListener(packetListener, packetFilter);
            return;
        }
        throw new IllegalStateException("Not connected to server.");
    }

    public void removePacketWriterListener(PacketListener packetListener) {
        this.packetWriter.removePacketListener(packetListener);
    }

    public void addPacketWriterInterceptor(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
        if (isConnected()) {
            this.packetWriter.addPacketInterceptor(packetInterceptor, packetFilter);
            return;
        }
        throw new IllegalStateException("Not connected to server.");
    }

    public void removePacketWriterInterceptor(PacketInterceptor packetInterceptor) {
        this.packetWriter.removePacketInterceptor(packetInterceptor);
    }

    public PacketCollector createPacketCollector(PacketFilter packetFilter) {
        return this.packetReader.createPacketCollector(packetFilter);
    }

    public void addConnectionListener(ConnectionListener connectionListener) {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected to server.");
        } else if (connectionListener != null && !this.packetReader.connectionListeners.contains(connectionListener)) {
            this.packetReader.connectionListeners.add(connectionListener);
        }
    }

    public void removeConnectionListener(ConnectionListener connectionListener) {
        this.packetReader.connectionListeners.remove(connectionListener);
    }

    public static void addConnectionCreationListener(ConnectionCreationListener connectionCreationListener) {
        connectionEstablishedListeners.add(connectionCreationListener);
    }

    public static void removeConnectionCreationListener(ConnectionCreationListener connectionCreationListener) {
        connectionEstablishedListeners.remove(connectionCreationListener);
    }

    private void connectUsingConfiguration(ConnectionConfiguration config) throws XMPPException {
        String errorMessage;
        this.host = config.getHost();
        this.port = config.getPort();
        try {
            if (config.getSocketFactory() == null) {
                this.socket = new Socket(this.host, this.port);
            } else {
                this.socket = config.getSocketFactory().createSocket(this.host, this.port);
            }
            this.serviceName = config.getServiceName();
            initConnection();
        } catch (UnknownHostException uhe) {
            errorMessage = "Could not connect to " + this.host + ":" + this.port + ".";
            throw new XMPPException(errorMessage, new XMPPError(Condition.remote_server_timeout, errorMessage), uhe);
        } catch (IOException ioe) {
            errorMessage = "XMPPError connecting to " + this.host + ":" + this.port + ".";
            throw new XMPPException(errorMessage, new XMPPError(Condition.remote_server_error, errorMessage), ioe);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x009a A:{SYNTHETIC, Splitter:B:39:0x009a} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00a5 A:{SYNTHETIC, Splitter:B:44:0x00a5} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008f A:{SYNTHETIC, Splitter:B:34:0x008f} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x009a A:{SYNTHETIC, Splitter:B:39:0x009a} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00a5 A:{SYNTHETIC, Splitter:B:44:0x00a5} */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0084 A:{SYNTHETIC, Splitter:B:29:0x0084} */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x008f A:{SYNTHETIC, Splitter:B:34:0x008f} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x009a A:{SYNTHETIC, Splitter:B:39:0x009a} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00a5 A:{SYNTHETIC, Splitter:B:44:0x00a5} */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00a5 A:{SYNTHETIC, Splitter:B:44:0x00a5} */
    private void initConnection() throws org.jivesoftware.smack.XMPPException {
        /*
        r9 = this;
        r2 = 1;
        r4 = 0;
        r8 = 0;
        r5 = r9.packetReader;
        if (r5 == 0) goto L_0x000b;
    L_0x0007:
        r5 = r9.packetWriter;
        if (r5 != 0) goto L_0x00b6;
    L_0x000b:
        if (r2 != 0) goto L_0x000f;
    L_0x000d:
        r9.usingCompression = r4;
    L_0x000f:
        r9.initReaderAndWriter();
        if (r2 == 0) goto L_0x00b9;
    L_0x0014:
        r5 = new org.jivesoftware.smack.PacketWriter;	 Catch:{ XMPPException -> 0x0074 }
        r5.m1396init(r9);	 Catch:{ XMPPException -> 0x0074 }
        r9.packetWriter = r5;	 Catch:{ XMPPException -> 0x0074 }
        r5 = new org.jivesoftware.smack.PacketReader;	 Catch:{ XMPPException -> 0x0074 }
        r5.m1391init(r9);	 Catch:{ XMPPException -> 0x0074 }
        r9.packetReader = r5;	 Catch:{ XMPPException -> 0x0074 }
        r5 = r9.configuration;	 Catch:{ XMPPException -> 0x0074 }
        r5 = r5.isDebuggerEnabled();	 Catch:{ XMPPException -> 0x0074 }
        if (r5 == 0) goto L_0x004a;
    L_0x002a:
        r5 = r9.packetReader;	 Catch:{ XMPPException -> 0x0074 }
        r6 = r9.debugger;	 Catch:{ XMPPException -> 0x0074 }
        r6 = r6.getReaderListener();	 Catch:{ XMPPException -> 0x0074 }
        r7 = 0;
        r5.addPacketListener(r6, r7);	 Catch:{ XMPPException -> 0x0074 }
        r5 = r9.debugger;	 Catch:{ XMPPException -> 0x0074 }
        r5 = r5.getWriterListener();	 Catch:{ XMPPException -> 0x0074 }
        if (r5 == 0) goto L_0x004a;
    L_0x003e:
        r5 = r9.packetWriter;	 Catch:{ XMPPException -> 0x0074 }
        r6 = r9.debugger;	 Catch:{ XMPPException -> 0x0074 }
        r6 = r6.getWriterListener();	 Catch:{ XMPPException -> 0x0074 }
        r7 = 0;
        r5.addPacketListener(r6, r7);	 Catch:{ XMPPException -> 0x0074 }
    L_0x004a:
        r5 = r9.packetWriter;	 Catch:{ XMPPException -> 0x0074 }
        r5.startup();	 Catch:{ XMPPException -> 0x0074 }
        r5 = r9.packetReader;	 Catch:{ XMPPException -> 0x0074 }
        r5.startup();	 Catch:{ XMPPException -> 0x0074 }
        r5 = 1;
        r9.connected = r5;	 Catch:{ XMPPException -> 0x0074 }
        r5 = r9.packetWriter;	 Catch:{ XMPPException -> 0x0074 }
        r5.startKeepAliveProcess();	 Catch:{ XMPPException -> 0x0074 }
        if (r2 == 0) goto L_0x00c4;
    L_0x005e:
        r5 = connectionEstablishedListeners;	 Catch:{ XMPPException -> 0x0074 }
        r1 = r5.iterator();	 Catch:{ XMPPException -> 0x0074 }
    L_0x0064:
        r5 = r1.hasNext();	 Catch:{ XMPPException -> 0x0074 }
        if (r5 == 0) goto L_0x00c9;
    L_0x006a:
        r3 = r1.next();	 Catch:{ XMPPException -> 0x0074 }
        r3 = (org.jivesoftware.smack.ConnectionCreationListener) r3;	 Catch:{ XMPPException -> 0x0074 }
        r3.connectionCreated(r9);	 Catch:{ XMPPException -> 0x0074 }
        goto L_0x0064;
    L_0x0074:
        r0 = move-exception;
        r5 = r9.packetWriter;
        if (r5 == 0) goto L_0x0080;
    L_0x0079:
        r5 = r9.packetWriter;	 Catch:{ Throwable -> 0x00d2 }
        r5.shutdown();	 Catch:{ Throwable -> 0x00d2 }
    L_0x007e:
        r9.packetWriter = r8;
    L_0x0080:
        r5 = r9.packetReader;
        if (r5 == 0) goto L_0x008b;
    L_0x0084:
        r5 = r9.packetReader;	 Catch:{ Throwable -> 0x00d0 }
        r5.shutdown();	 Catch:{ Throwable -> 0x00d0 }
    L_0x0089:
        r9.packetReader = r8;
    L_0x008b:
        r5 = r9.reader;
        if (r5 == 0) goto L_0x0096;
    L_0x008f:
        r5 = r9.reader;	 Catch:{ Throwable -> 0x00ce }
        r5.close();	 Catch:{ Throwable -> 0x00ce }
    L_0x0094:
        r9.reader = r8;
    L_0x0096:
        r5 = r9.writer;
        if (r5 == 0) goto L_0x00a1;
    L_0x009a:
        r5 = r9.writer;	 Catch:{ Throwable -> 0x00cc }
        r5.close();	 Catch:{ Throwable -> 0x00cc }
    L_0x009f:
        r9.writer = r8;
    L_0x00a1:
        r5 = r9.socket;
        if (r5 == 0) goto L_0x00ac;
    L_0x00a5:
        r5 = r9.socket;	 Catch:{ Exception -> 0x00ca }
        r5.close();	 Catch:{ Exception -> 0x00ca }
    L_0x00aa:
        r9.socket = r8;
    L_0x00ac:
        r5 = r9.authenticated;
        r9.setWasAuthenticated(r5);
        r9.authenticated = r4;
        r9.connected = r4;
        throw r0;
    L_0x00b6:
        r2 = r4;
        goto L_0x000b;
    L_0x00b9:
        r5 = r9.packetWriter;	 Catch:{ XMPPException -> 0x0074 }
        r5.init();	 Catch:{ XMPPException -> 0x0074 }
        r5 = r9.packetReader;	 Catch:{ XMPPException -> 0x0074 }
        r5.init();	 Catch:{ XMPPException -> 0x0074 }
        goto L_0x004a;
    L_0x00c4:
        r5 = r9.packetReader;	 Catch:{ XMPPException -> 0x0074 }
        r5.notifyReconnection();	 Catch:{ XMPPException -> 0x0074 }
    L_0x00c9:
        return;
    L_0x00ca:
        r5 = move-exception;
        goto L_0x00aa;
    L_0x00cc:
        r5 = move-exception;
        goto L_0x009f;
    L_0x00ce:
        r5 = move-exception;
        goto L_0x0094;
    L_0x00d0:
        r5 = move-exception;
        goto L_0x0089;
    L_0x00d2:
        r5 = move-exception;
        goto L_0x007e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.XMPPConnection.initConnection():void");
    }

    private void initReaderAndWriter() throws XMPPException {
        try {
            if (this.usingCompression) {
                try {
                    Class<?> zoClass = Class.forName("com.jcraft.jzlib.ZOutputStream");
                    Object out = zoClass.getConstructor(new Class[]{OutputStream.class, Integer.TYPE}).newInstance(new Object[]{this.socket.getOutputStream(), Integer.valueOf(9)});
                    zoClass.getMethod("setFlushMode", new Class[]{Integer.TYPE}).invoke(out, new Object[]{Integer.valueOf(2)});
                    this.writer = new BufferedWriter(new OutputStreamWriter((OutputStream) out, StringEncodings.UTF8));
                    Class<?> ziClass = Class.forName("com.jcraft.jzlib.ZInputStream");
                    Object in = ziClass.getConstructor(new Class[]{InputStream.class}).newInstance(new Object[]{this.socket.getInputStream()});
                    ziClass.getMethod("setFlushMode", new Class[]{Integer.TYPE}).invoke(in, new Object[]{Integer.valueOf(2)});
                    this.reader = new BufferedReader(new InputStreamReader((InputStream) in, StringEncodings.UTF8));
                } catch (Exception e) {
                    e.printStackTrace();
                    this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StringEncodings.UTF8));
                    this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), StringEncodings.UTF8));
                }
            } else {
                this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StringEncodings.UTF8));
                this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), StringEncodings.UTF8));
            }
            if (!this.configuration.isDebuggerEnabled()) {
                return;
            }
            if (this.debugger == null) {
                String className = null;
                try {
                    className = System.getProperty("smack.debuggerClass");
                } catch (Throwable th) {
                }
                Class<?> debuggerClass = null;
                if (className != null) {
                    try {
                        debuggerClass = Class.forName(className);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (debuggerClass == null) {
                    try {
                        debuggerClass = Class.forName("org.jivesoftware.smackx.debugger.EnhancedDebugger");
                    } catch (Exception e3) {
                        try {
                            debuggerClass = Class.forName("org.jivesoftware.smack.debugger.LiteDebugger");
                        } catch (Exception ex2) {
                            ex2.printStackTrace();
                        }
                    }
                }
                try {
                    this.debugger = (SmackDebugger) debuggerClass.getConstructor(new Class[]{XMPPConnection.class, Writer.class, Reader.class}).newInstance(new Object[]{this, this.writer, this.reader});
                    this.reader = this.debugger.getReader();
                    this.writer = this.debugger.getWriter();
                    return;
                } catch (Exception e22) {
                    e22.printStackTrace();
                    DEBUG_ENABLED = false;
                    return;
                }
            }
            this.reader = this.debugger.newConnectionReader(this.reader);
            this.writer = this.debugger.newConnectionWriter(this.writer);
        } catch (IOException ioe) {
            throw new XMPPException("XMPPError establishing connection with server.", new XMPPError(Condition.remote_server_error, "XMPPError establishing connection with server."), ioe);
        }
    }

    public boolean isUsingTLS() {
        return this.usingTLS;
    }

    public SASLAuthentication getSASLAuthentication() {
        return this.saslAuthentication;
    }

    /* access modifiers changed from: protected */
    public ConnectionConfiguration getConfiguration() {
        return this.configuration;
    }

    /* access modifiers changed from: 0000 */
    public void startTLSReceived(boolean required) {
        if (required && this.configuration.getSecurityMode() == SecurityMode.disabled) {
            this.packetReader.notifyConnectionError(new IllegalStateException("TLS required by server but not allowed by connection configuration"));
        } else if (this.configuration.getSecurityMode() != SecurityMode.disabled) {
            try {
                this.writer.write("<starttls xmlns=\"urn:ietf:params:xml:ns:xmpp-tls\"/>");
                this.writer.flush();
            } catch (IOException e) {
                this.packetReader.notifyConnectionError(e);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void proceedTLSReceived() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{new OpenTrustManager()}, new SecureRandom());
        Socket plain = this.socket;
        this.socket = context.getSocketFactory().createSocket(plain, plain.getInetAddress().getHostName(), plain.getPort(), true);
        initReaderAndWriter();
        ((SSLSocket) this.socket).startHandshake();
        this.usingTLS = true;
        this.packetWriter.setWriter(this.writer);
        this.packetWriter.openStream();
    }

    /* access modifiers changed from: 0000 */
    public void setAvailableCompressionMethods(Collection methods) {
        this.compressionMethods = methods;
    }

    private boolean hasAvailableCompressionMethod(String method) {
        return this.compressionMethods != null && this.compressionMethods.contains(method);
    }

    public boolean isUsingCompression() {
        return this.usingCompression;
    }

    private boolean useCompression() {
        if (this.authenticated) {
            throw new IllegalStateException("Compression should be negotiated before authentication.");
        }
        try {
            Class.forName("com.jcraft.jzlib.ZOutputStream");
            if (!hasAvailableCompressionMethod("zlib")) {
                return false;
            }
            requestStreamCompression();
            synchronized (this) {
                try {
                    wait((long) (SmackConfiguration.getPacketReplyTimeout() * 5));
                } catch (InterruptedException e) {
                }
            }
            return this.usingCompression;
        } catch (ClassNotFoundException e2) {
            throw new IllegalStateException("Cannot use compression. Add smackx.jar to the classpath");
        }
    }

    private void requestStreamCompression() {
        try {
            this.writer.write("<compress xmlns='http://jabber.org/protocol/compress'>");
            this.writer.write("<method>zlib</method></compress>");
            this.writer.flush();
        } catch (IOException e) {
            this.packetReader.notifyConnectionError(e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void startStreamCompression() throws Exception {
        this.usingCompression = true;
        initReaderAndWriter();
        this.packetWriter.setWriter(this.writer);
        this.packetWriter.openStream();
        synchronized (this) {
            notify();
        }
    }

    /* access modifiers changed from: 0000 */
    public void streamCompressionDenied() {
        synchronized (this) {
            notify();
        }
    }

    public void connect() throws XMPPException {
        connectUsingConfiguration(this.configuration);
        if (this.connected && this.wasAuthenticated) {
            try {
                if (isAnonymous()) {
                    loginAnonymously();
                } else {
                    login(getConfiguration().getUsername(), getConfiguration().getPassword(), getConfiguration().getResource(), getConfiguration().isSendPresence());
                }
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    private void setWasAuthenticated(boolean wasAuthenticated) {
        if (!this.wasAuthenticated) {
            this.wasAuthenticated = wasAuthenticated;
        }
    }
}
