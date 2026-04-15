package com.trilead.ssh2;

import com.trilead.ssh2.auth.AuthenticationManager;
import com.trilead.ssh2.channel.ChannelManager;
import com.trilead.ssh2.crypto.CryptoWishList;
import com.trilead.ssh2.crypto.cipher.BlockCipherFactory;
import com.trilead.ssh2.crypto.digest.MAC;
import com.trilead.ssh2.log.Logger;
import com.trilead.ssh2.packets.PacketIgnore;
import com.trilead.ssh2.transport.KexManager;
import com.trilead.ssh2.transport.TransportManager;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Vector;

public class Connection {
    public static final String identification = "TrileadSSH2Java_213";
    private AuthenticationManager am;
    private boolean authenticated;
    private ChannelManager cm;
    private Vector connectionMonitors;
    private CryptoWishList cryptoWishList;
    private DHGexParameters dhgexpara;
    private SecureRandom generator;
    private final String hostname;
    private final int port;
    private ProxyData proxyData;
    private boolean tcpNoDelay;
    /* access modifiers changed from: private */
    public TransportManager tm;

    public static synchronized String[] getAvailableCiphers() {
        String[] defaultCipherList;
        synchronized (Connection.class) {
            defaultCipherList = BlockCipherFactory.getDefaultCipherList();
        }
        return defaultCipherList;
    }

    public static synchronized String[] getAvailableMACs() {
        String[] macList;
        synchronized (Connection.class) {
            macList = MAC.getMacList();
        }
        return macList;
    }

    public static synchronized String[] getAvailableServerHostKeyAlgorithms() {
        String[] defaultServerHostkeyAlgorithmList;
        synchronized (Connection.class) {
            defaultServerHostkeyAlgorithmList = KexManager.getDefaultServerHostkeyAlgorithmList();
        }
        return defaultServerHostkeyAlgorithmList;
    }

    public Connection(String hostname) {
        this(hostname, 22);
    }

    public Connection(String hostname, int port) {
        this.authenticated = false;
        this.cryptoWishList = new CryptoWishList();
        this.dhgexpara = new DHGexParameters();
        this.tcpNoDelay = false;
        this.proxyData = null;
        this.connectionMonitors = new Vector();
        this.hostname = hostname;
        this.port = port;
    }

    public synchronized boolean authenticateWithDSA(String user, String pem, String password) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        } else if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        } else {
            if (this.am == null) {
                this.am = new AuthenticationManager(this.tm);
            }
            if (this.cm == null) {
                this.cm = new ChannelManager(this.tm);
            }
            if (user == null) {
                throw new IllegalArgumentException("user argument is null");
            } else if (pem == null) {
                throw new IllegalArgumentException("pem argument is null");
            } else {
                this.authenticated = this.am.authenticatePublicKey(user, pem.toCharArray(), password, getOrCreateSecureRND());
            }
        }
        return this.authenticated;
    }

    public synchronized boolean authenticateWithKeyboardInteractive(String user, InteractiveCallback cb) throws IOException {
        return authenticateWithKeyboardInteractive(user, null, cb);
    }

    public synchronized boolean authenticateWithKeyboardInteractive(String user, String[] submethods, InteractiveCallback cb) throws IOException {
        if (cb == null) {
            throw new IllegalArgumentException("Callback may not ne NULL!");
        } else if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        } else if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        } else {
            if (this.am == null) {
                this.am = new AuthenticationManager(this.tm);
            }
            if (this.cm == null) {
                this.cm = new ChannelManager(this.tm);
            }
            if (user == null) {
                throw new IllegalArgumentException("user argument is null");
            }
            this.authenticated = this.am.authenticateInteractive(user, submethods, cb);
        }
        return this.authenticated;
    }

    public synchronized boolean authenticateWithPassword(String user, String password) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        } else if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        } else {
            if (this.am == null) {
                this.am = new AuthenticationManager(this.tm);
            }
            if (this.cm == null) {
                this.cm = new ChannelManager(this.tm);
            }
            if (user == null) {
                throw new IllegalArgumentException("user argument is null");
            } else if (password == null) {
                throw new IllegalArgumentException("password argument is null");
            } else {
                this.authenticated = this.am.authenticatePassword(user, password);
            }
        }
        return this.authenticated;
    }

    public synchronized boolean authenticateWithNone(String user) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        } else if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        } else {
            if (this.am == null) {
                this.am = new AuthenticationManager(this.tm);
            }
            if (this.cm == null) {
                this.cm = new ChannelManager(this.tm);
            }
            if (user == null) {
                throw new IllegalArgumentException("user argument is null");
            }
            this.authenticated = this.am.authenticateNone(user);
        }
        return this.authenticated;
    }

    public synchronized boolean authenticateWithPublicKey(String user, char[] pemPrivateKey, String password) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        } else if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        } else {
            if (this.am == null) {
                this.am = new AuthenticationManager(this.tm);
            }
            if (this.cm == null) {
                this.cm = new ChannelManager(this.tm);
            }
            if (user == null) {
                throw new IllegalArgumentException("user argument is null");
            } else if (pemPrivateKey == null) {
                throw new IllegalArgumentException("pemPrivateKey argument is null");
            } else {
                this.authenticated = this.am.authenticatePublicKey(user, pemPrivateKey, password, getOrCreateSecureRND());
            }
        }
        return this.authenticated;
    }

    public synchronized boolean authenticateWithPublicKey(String user, File pemFile, String password) throws IOException {
        CharArrayWriter cw;
        if (pemFile == null) {
            throw new IllegalArgumentException("pemFile argument is null");
        }
        char[] buff = new char[256];
        cw = new CharArrayWriter();
        FileReader fr = new FileReader(pemFile);
        while (true) {
            int len = fr.read(buff);
            if (len < 0) {
                fr.close();
            } else {
                cw.write(buff, 0, len);
            }
        }
        return authenticateWithPublicKey(user, cw.toCharArray(), password);
    }

    public synchronized void addConnectionMonitor(ConnectionMonitor cmon) {
        if (cmon == null) {
            throw new IllegalArgumentException("cmon argument is null");
        }
        this.connectionMonitors.addElement(cmon);
        if (this.tm != null) {
            this.tm.setConnectionMonitors(this.connectionMonitors);
        }
    }

    public synchronized void close() {
        close(new Throwable("Closed due to user request."), false);
    }

    private void close(Throwable t, boolean hard) {
        if (this.cm != null) {
            this.cm.closeAllChannels();
        }
        if (this.tm != null) {
            this.tm.close(t, !hard);
            this.tm = null;
        }
        this.am = null;
        this.cm = null;
        this.authenticated = false;
    }

    public synchronized ConnectionInfo connect() throws IOException {
        return connect(null, 0, 0);
    }

    public synchronized ConnectionInfo connect(ServerHostKeyVerifier verifier) throws IOException {
        return connect(verifier, 0, 0);
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x00d3 A:{Splitter:B:21:0x006a, ExcHandler: IOException (r9_0 'e1' java.io.IOException)} */
    /* JADX WARNING: Missing block: B:46:0x00d3, code skipped:
            r9 = move-exception;
     */
    /* JADX WARNING: Missing block: B:48:?, code skipped:
            close(new java.lang.Throwable("There was a problem during connect."), false);
     */
    /* JADX WARNING: Missing block: B:49:0x00e1, code skipped:
            monitor-enter(r11);
     */
    /* JADX WARNING: Missing block: B:52:0x00e4, code skipped:
            if (r11.timeoutSocketClosed != false) goto L_0x00e6;
     */
    /* JADX WARNING: Missing block: B:54:0x0102, code skipped:
            throw new java.net.SocketTimeoutException("The kexTimeout (" + r20 + " ms) expired.");
     */
    /* JADX WARNING: Missing block: B:69:0x010f, code skipped:
            if ((r9 instanceof com.trilead.ssh2.HTTPProxyException) != false) goto L_0x0111;
     */
    /* JADX WARNING: Missing block: B:70:0x0111, code skipped:
            throw r9;
     */
    /* JADX WARNING: Missing block: B:72:0x013e, code skipped:
            throw ((java.io.IOException) new java.io.IOException("There was a problem while connecting to " + r17.hostname + ":" + r17.port).initCause(r9));
     */
    public synchronized com.trilead.ssh2.ConnectionInfo connect(com.trilead.ssh2.ServerHostKeyVerifier r18, int r19, int r20) throws java.io.IOException {
        /*
        r17 = this;
        monitor-enter(r17);
        r0 = r17;
        r1 = r0.tm;	 Catch:{ all -> 0x0026 }
        if (r1 == 0) goto L_0x0029;
    L_0x0007:
        r1 = new java.io.IOException;	 Catch:{ all -> 0x0026 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0026 }
        r3 = "Connection to ";
        r2.<init>(r3);	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r3 = r0.hostname;	 Catch:{ all -> 0x0026 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0026 }
        r3 = " is already in connected state!";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0026 }
        r2 = r2.toString();	 Catch:{ all -> 0x0026 }
        r1.<init>(r2);	 Catch:{ all -> 0x0026 }
        throw r1;	 Catch:{ all -> 0x0026 }
    L_0x0026:
        r1 = move-exception;
        monitor-exit(r17);
        throw r1;
    L_0x0029:
        if (r19 >= 0) goto L_0x0033;
    L_0x002b:
        r1 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0026 }
        r2 = "connectTimeout must be non-negative!";
        r1.<init>(r2);	 Catch:{ all -> 0x0026 }
        throw r1;	 Catch:{ all -> 0x0026 }
    L_0x0033:
        if (r20 >= 0) goto L_0x003d;
    L_0x0035:
        r1 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0026 }
        r2 = "kexTimeout must be non-negative!";
        r1.<init>(r2);	 Catch:{ all -> 0x0026 }
        throw r1;	 Catch:{ all -> 0x0026 }
    L_0x003d:
        r11 = new com.trilead.ssh2.Connection$1TimeoutState;	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r11.m1init();	 Catch:{ all -> 0x0026 }
        r1 = new com.trilead.ssh2.transport.TransportManager;	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r2 = r0.hostname;	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r3 = r0.port;	 Catch:{ all -> 0x0026 }
        r1.m146init(r2, r3);	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r0.tm = r1;	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r1 = r0.tm;	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r2 = r0.connectionMonitors;	 Catch:{ all -> 0x0026 }
        r1.setConnectionMonitors(r2);	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r1 = r0.tm;	 Catch:{ all -> 0x0026 }
        monitor-enter(r1);	 Catch:{ all -> 0x0026 }
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        r16 = 0;
        if (r20 <= 0) goto L_0x007e;
    L_0x006a:
        r13 = new com.trilead.ssh2.Connection$1;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r0 = r17;
        r13.m0init(r11);	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r1 = java.lang.System.currentTimeMillis();	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r0 = r20;
        r3 = (long) r0;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r14 = r1 + r3;
        r16 = com.trilead.ssh2.util.TimeoutService.addTimeoutHandler(r14, r13);	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
    L_0x007e:
        r0 = r17;
        r1 = r0.tm;	 Catch:{ SocketTimeoutException -> 0x00c4, IOException -> 0x00d3 }
        r0 = r17;
        r2 = r0.cryptoWishList;	 Catch:{ SocketTimeoutException -> 0x00c4, IOException -> 0x00d3 }
        r0 = r17;
        r4 = r0.dhgexpara;	 Catch:{ SocketTimeoutException -> 0x00c4, IOException -> 0x00d3 }
        r6 = r17.getOrCreateSecureRND();	 Catch:{ SocketTimeoutException -> 0x00c4, IOException -> 0x00d3 }
        r0 = r17;
        r7 = r0.proxyData;	 Catch:{ SocketTimeoutException -> 0x00c4, IOException -> 0x00d3 }
        r3 = r18;
        r5 = r19;
        r1.initialize(r2, r3, r4, r5, r6, r7);	 Catch:{ SocketTimeoutException -> 0x00c4, IOException -> 0x00d3 }
        r0 = r17;
        r1 = r0.tm;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r0 = r17;
        r2 = r0.tcpNoDelay;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r1.setTcpNoDelay(r2);	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r0 = r17;
        r1 = r0.tm;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r2 = 1;
        r8 = r1.getConnectionInfo(r2);	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        if (r16 == 0) goto L_0x010a;
    L_0x00af:
        com.trilead.ssh2.util.TimeoutService.cancelTimeoutHandler(r16);	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        monitor-enter(r11);	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r1 = r11.timeoutSocketClosed;	 Catch:{ all -> 0x00bf }
        if (r1 == 0) goto L_0x0106;
    L_0x00b7:
        r1 = new java.io.IOException;	 Catch:{ all -> 0x00bf }
        r2 = "This exception will be replaced by the one below =)";
        r1.<init>(r2);	 Catch:{ all -> 0x00bf }
        throw r1;	 Catch:{ all -> 0x00bf }
    L_0x00bf:
        r1 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x00bf }
        throw r1;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
    L_0x00c2:
        r12 = move-exception;
        throw r12;	 Catch:{ all -> 0x0026 }
    L_0x00c4:
        r10 = move-exception;
        r1 = new java.net.SocketTimeoutException;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r2 = "The connect() operation on the socket timed out.";
        r1.<init>(r2);	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r1 = r1.initCause(r10);	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        r1 = (java.net.SocketTimeoutException) r1;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
        throw r1;	 Catch:{ SocketTimeoutException -> 0x00c2, IOException -> 0x00d3 }
    L_0x00d3:
        r9 = move-exception;
        r1 = new java.lang.Throwable;	 Catch:{ all -> 0x0026 }
        r2 = "There was a problem during connect.";
        r1.<init>(r2);	 Catch:{ all -> 0x0026 }
        r2 = 0;
        r0 = r17;
        r0.close(r1, r2);	 Catch:{ all -> 0x0026 }
        monitor-enter(r11);	 Catch:{ all -> 0x0026 }
        r1 = r11.timeoutSocketClosed;	 Catch:{ all -> 0x0103 }
        if (r1 == 0) goto L_0x010c;
    L_0x00e6:
        r1 = new java.net.SocketTimeoutException;	 Catch:{ all -> 0x0103 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0103 }
        r3 = "The kexTimeout (";
        r2.<init>(r3);	 Catch:{ all -> 0x0103 }
        r0 = r20;
        r2 = r2.append(r0);	 Catch:{ all -> 0x0103 }
        r3 = " ms) expired.";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0103 }
        r2 = r2.toString();	 Catch:{ all -> 0x0103 }
        r1.<init>(r2);	 Catch:{ all -> 0x0103 }
        throw r1;	 Catch:{ all -> 0x0103 }
    L_0x0103:
        r1 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0103 }
        throw r1;	 Catch:{ all -> 0x0026 }
    L_0x0106:
        r1 = 1;
        r11.isCancelled = r1;	 Catch:{ all -> 0x00bf }
        monitor-exit(r11);	 Catch:{ all -> 0x00bf }
    L_0x010a:
        monitor-exit(r17);
        return r8;
    L_0x010c:
        monitor-exit(r11);	 Catch:{ all -> 0x0103 }
        r1 = r9 instanceof com.trilead.ssh2.HTTPProxyException;	 Catch:{ all -> 0x0026 }
        if (r1 == 0) goto L_0x0112;
    L_0x0111:
        throw r9;	 Catch:{ all -> 0x0026 }
    L_0x0112:
        r1 = new java.io.IOException;	 Catch:{ all -> 0x0026 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0026 }
        r3 = "There was a problem while connecting to ";
        r2.<init>(r3);	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r3 = r0.hostname;	 Catch:{ all -> 0x0026 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0026 }
        r3 = ":";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0026 }
        r0 = r17;
        r3 = r0.port;	 Catch:{ all -> 0x0026 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0026 }
        r2 = r2.toString();	 Catch:{ all -> 0x0026 }
        r1.<init>(r2);	 Catch:{ all -> 0x0026 }
        r1 = r1.initCause(r9);	 Catch:{ all -> 0x0026 }
        r1 = (java.io.IOException) r1;	 Catch:{ all -> 0x0026 }
        throw r1;	 Catch:{ all -> 0x0026 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.Connection.connect(com.trilead.ssh2.ServerHostKeyVerifier, int, int):com.trilead.ssh2.ConnectionInfo");
    }

    public synchronized LocalPortForwarder createLocalPortForwarder(int local_port, String host_to_connect, int port_to_connect) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot forward ports, you need to establish a connection first.");
        } else if (this.authenticated) {
        } else {
            throw new IllegalStateException("Cannot forward ports, connection is not authenticated.");
        }
        return new LocalPortForwarder(this.cm, local_port, host_to_connect, port_to_connect);
    }

    public synchronized LocalPortForwarder createLocalPortForwarder(InetSocketAddress addr, String host_to_connect, int port_to_connect) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot forward ports, you need to establish a connection first.");
        } else if (this.authenticated) {
        } else {
            throw new IllegalStateException("Cannot forward ports, connection is not authenticated.");
        }
        return new LocalPortForwarder(this.cm, addr, host_to_connect, port_to_connect);
    }

    public synchronized LocalStreamForwarder createLocalStreamForwarder(String host_to_connect, int port_to_connect) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot forward, you need to establish a connection first.");
        } else if (this.authenticated) {
        } else {
            throw new IllegalStateException("Cannot forward, connection is not authenticated.");
        }
        return new LocalStreamForwarder(this.cm, host_to_connect, port_to_connect);
    }

    public synchronized SCPClient createSCPClient() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot create SCP client, you need to establish a connection first.");
        } else if (this.authenticated) {
        } else {
            throw new IllegalStateException("Cannot create SCP client, connection is not authenticated.");
        }
        return new SCPClient(this);
    }

    public synchronized void forceKeyExchange() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("You need to establish a connection first.");
        }
        this.tm.forceKeyExchange(this.cryptoWishList, this.dhgexpara);
    }

    public synchronized String getHostname() {
        return this.hostname;
    }

    public synchronized int getPort() {
        return this.port;
    }

    public synchronized ConnectionInfo getConnectionInfo() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot get details of connection, you need to establish a connection first.");
        }
        return this.tm.getConnectionInfo(1);
    }

    public synchronized String[] getRemainingAuthMethods(String user) throws IOException {
        if (user == null) {
            throw new IllegalArgumentException("user argument may not be NULL!");
        } else if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        } else if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        } else {
            if (this.am == null) {
                this.am = new AuthenticationManager(this.tm);
            }
            if (this.cm == null) {
                this.cm = new ChannelManager(this.tm);
            }
        }
        return this.am.getRemainingMethods(user);
    }

    public synchronized boolean isAuthenticationComplete() {
        return this.authenticated;
    }

    public synchronized boolean isAuthenticationPartialSuccess() {
        boolean z;
        if (this.am == null) {
            z = false;
        } else {
            z = this.am.getPartialSuccess();
        }
        return z;
    }

    public synchronized boolean isAuthMethodAvailable(String user, String method) throws IOException {
        boolean z;
        if (method == null) {
            throw new IllegalArgumentException("method argument may not be NULL!");
        }
        String[] methods = getRemainingAuthMethods(user);
        for (String compareTo : methods) {
            if (compareTo.compareTo(method) == 0) {
                z = true;
                break;
            }
        }
        z = false;
        return z;
    }

    private final SecureRandom getOrCreateSecureRND() {
        if (this.generator == null) {
            this.generator = new SecureRandom();
        }
        return this.generator;
    }

    public synchronized Session openSession() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot open session, you need to establish a connection first.");
        } else if (this.authenticated) {
        } else {
            throw new IllegalStateException("Cannot open session, connection is not authenticated.");
        }
        return new Session(this.cm, getOrCreateSecureRND());
    }

    public synchronized void sendIgnorePacket() throws IOException {
        SecureRandom rnd = getOrCreateSecureRND();
        byte[] data = new byte[rnd.nextInt(16)];
        rnd.nextBytes(data);
        sendIgnorePacket(data);
    }

    public synchronized void sendIgnorePacket(byte[] data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("data argument must not be null.");
        } else if (this.tm == null) {
            throw new IllegalStateException("Cannot send SSH_MSG_IGNORE packet, you need to establish a connection first.");
        } else {
            PacketIgnore pi = new PacketIgnore();
            pi.setData(data);
            this.tm.sendMessage(pi.getPayload());
        }
    }

    private String[] removeDuplicates(String[] list) {
        if (list == null || list.length < 2) {
            return list;
        }
        String[] list2 = new String[list.length];
        int count = 0;
        for (int i = 0; i < list.length; i++) {
            boolean duplicate = false;
            String element = list[i];
            int j = 0;
            while (j < count) {
                if ((element == null && list2[j] == null) || (element != null && element.equals(list2[j]))) {
                    duplicate = true;
                    break;
                }
                j++;
            }
            if (!duplicate) {
                int count2 = count + 1;
                list2[count] = list[i];
                count = count2;
            }
        }
        if (count == list2.length) {
            return list2;
        }
        String[] tmp = new String[count];
        System.arraycopy(list2, 0, tmp, 0, count);
        return tmp;
    }

    public synchronized void setClient2ServerCiphers(String[] ciphers) {
        if (ciphers != null) {
            if (ciphers.length != 0) {
                ciphers = removeDuplicates(ciphers);
                BlockCipherFactory.checkCipherList(ciphers);
                this.cryptoWishList.c2s_enc_algos = ciphers;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setClient2ServerMACs(String[] macs) {
        if (macs != null) {
            if (macs.length != 0) {
                macs = removeDuplicates(macs);
                MAC.checkMacList(macs);
                this.cryptoWishList.c2s_mac_algos = macs;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setDHGexParameters(DHGexParameters dgp) {
        if (dgp == null) {
            throw new IllegalArgumentException();
        }
        this.dhgexpara = dgp;
    }

    public synchronized void setServer2ClientCiphers(String[] ciphers) {
        if (ciphers != null) {
            if (ciphers.length != 0) {
                ciphers = removeDuplicates(ciphers);
                BlockCipherFactory.checkCipherList(ciphers);
                this.cryptoWishList.s2c_enc_algos = ciphers;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setServer2ClientMACs(String[] macs) {
        if (macs != null) {
            if (macs.length != 0) {
                macs = removeDuplicates(macs);
                MAC.checkMacList(macs);
                this.cryptoWishList.s2c_mac_algos = macs;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setServerHostKeyAlgorithms(String[] algos) {
        if (algos != null) {
            if (algos.length != 0) {
                algos = removeDuplicates(algos);
                KexManager.checkServerHostkeyAlgorithmsList(algos);
                this.cryptoWishList.serverHostKeyAlgorithms = algos;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setTCPNoDelay(boolean enable) throws IOException {
        this.tcpNoDelay = enable;
        if (this.tm != null) {
            this.tm.setTcpNoDelay(enable);
        }
    }

    public synchronized void setProxyData(ProxyData proxyData) {
        this.proxyData = proxyData;
    }

    public synchronized void requestRemotePortForwarding(String bindAddress, int bindPort, String targetAddress, int targetPort) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("You need to establish a connection first.");
        } else if (!this.authenticated) {
            throw new IllegalStateException("The connection is not authenticated.");
        } else if (bindAddress == null || targetAddress == null || bindPort <= 0 || targetPort <= 0) {
            throw new IllegalArgumentException();
        } else {
            this.cm.requestGlobalForward(bindAddress, bindPort, targetAddress, targetPort);
        }
    }

    public synchronized void cancelRemotePortForwarding(int bindPort) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("You need to establish a connection first.");
        } else if (this.authenticated) {
            this.cm.requestCancelGlobalForward(bindPort);
        } else {
            throw new IllegalStateException("The connection is not authenticated.");
        }
    }

    public synchronized void setSecureRandom(SecureRandom rnd) {
        if (rnd == null) {
            throw new IllegalArgumentException();
        }
        this.generator = rnd;
    }

    public synchronized void enableDebugging(boolean enable, DebugLogger logger) {
        Logger.enabled = enable;
        if (!enable) {
            Logger.logger = null;
        } else if (logger == null) {
            logger = new DebugLogger() {
                public void log(int level, String className, String message) {
                    System.err.println(System.currentTimeMillis() + " : " + className + ": " + message);
                }
            };
        }
    }

    public synchronized void ping() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("You need to establish a connection first.");
        } else if (this.authenticated) {
            this.cm.requestGlobalTrileadPing();
        } else {
            throw new IllegalStateException("The connection is not authenticated.");
        }
    }
}
