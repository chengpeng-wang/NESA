package com.trilead.ssh2.transport;

import com.trilead.ssh2.ConnectionInfo;
import com.trilead.ssh2.ConnectionMonitor;
import com.trilead.ssh2.DHGexParameters;
import com.trilead.ssh2.HTTPProxyData;
import com.trilead.ssh2.HTTPProxyException;
import com.trilead.ssh2.ProxyData;
import com.trilead.ssh2.ServerHostKeyVerifier;
import com.trilead.ssh2.crypto.Base64;
import com.trilead.ssh2.crypto.CryptoWishList;
import com.trilead.ssh2.crypto.cipher.BlockCipher;
import com.trilead.ssh2.crypto.digest.MAC;
import com.trilead.ssh2.log.Logger;
import com.trilead.ssh2.packets.PacketDisconnect;
import com.trilead.ssh2.packets.TypesReader;
import com.trilead.ssh2.util.Tokenizer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Vector;
import org.apache.http.protocol.HTTP;

public class TransportManager {
    /* access modifiers changed from: private|static|final */
    public static final Logger log = Logger.getLogger(TransportManager.class);
    /* access modifiers changed from: private|final */
    public final Vector asynchronousQueue = new Vector();
    /* access modifiers changed from: private */
    public Thread asynchronousThread = null;
    boolean connectionClosed = false;
    Vector connectionMonitors = new Vector();
    Object connectionSemaphore = new Object();
    boolean flagKexOngoing = false;
    String hostname;
    KexManager km;
    Vector messageHandlers = new Vector();
    boolean monitorsWereInformed = false;
    int port;
    Throwable reasonClosedCause = null;
    Thread receiveThread;
    final Socket sock = new Socket();
    TransportConnection tc;

    class AsynchronousWorker extends Thread {
        AsynchronousWorker() {
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* JADX WARNING: Missing block: B:16:?, code skipped:
            r8.this$0.sendMessage(r2);
     */
        /* JADX WARNING: Missing block: B:29:?, code skipped:
            return;
     */
        public void run() {
            /*
            r8 = this;
            r3 = 0;
        L_0x0001:
            r2 = r3;
            r2 = (byte[]) r2;
            r4 = com.trilead.ssh2.transport.TransportManager.this;
            r5 = r4.asynchronousQueue;
            monitor-enter(r5);
            r4 = com.trilead.ssh2.transport.TransportManager.this;	 Catch:{ all -> 0x004e }
            r4 = r4.asynchronousQueue;	 Catch:{ all -> 0x004e }
            r4 = r4.size();	 Catch:{ all -> 0x004e }
            if (r4 != 0) goto L_0x0036;
        L_0x0017:
            r4 = com.trilead.ssh2.transport.TransportManager.this;	 Catch:{ InterruptedException -> 0x0051 }
            r4 = r4.asynchronousQueue;	 Catch:{ InterruptedException -> 0x0051 }
            r6 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
            r4.wait(r6);	 Catch:{ InterruptedException -> 0x0051 }
        L_0x0022:
            r4 = com.trilead.ssh2.transport.TransportManager.this;	 Catch:{ all -> 0x004e }
            r4 = r4.asynchronousQueue;	 Catch:{ all -> 0x004e }
            r4 = r4.size();	 Catch:{ all -> 0x004e }
            if (r4 != 0) goto L_0x0036;
        L_0x002e:
            r3 = com.trilead.ssh2.transport.TransportManager.this;	 Catch:{ all -> 0x004e }
            r4 = 0;
            r3.asynchronousThread = r4;	 Catch:{ all -> 0x004e }
            monitor-exit(r5);	 Catch:{ all -> 0x004e }
        L_0x0035:
            return;
        L_0x0036:
            r4 = com.trilead.ssh2.transport.TransportManager.this;	 Catch:{ all -> 0x004e }
            r4 = r4.asynchronousQueue;	 Catch:{ all -> 0x004e }
            r6 = 0;
            r4 = r4.remove(r6);	 Catch:{ all -> 0x004e }
            r0 = r4;
            r0 = (byte[]) r0;	 Catch:{ all -> 0x004e }
            r2 = r0;
            monitor-exit(r5);	 Catch:{ all -> 0x004e }
            r4 = com.trilead.ssh2.transport.TransportManager.this;	 Catch:{ IOException -> 0x004c }
            r4.sendMessage(r2);	 Catch:{ IOException -> 0x004c }
            goto L_0x0001;
        L_0x004c:
            r1 = move-exception;
            goto L_0x0035;
        L_0x004e:
            r3 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x004e }
            throw r3;
        L_0x0051:
            r4 = move-exception;
            goto L_0x0022;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.trilead.ssh2.transport.TransportManager$AsynchronousWorker.run():void");
        }
    }

    class HandlerEntry {
        int high;
        int low;
        MessageHandler mh;

        HandlerEntry() {
        }
    }

    private InetAddress createInetAddress(String host) throws UnknownHostException {
        InetAddress addr = parseIPv4Address(host);
        return addr != null ? addr : InetAddress.getByName(host);
    }

    private InetAddress parseIPv4Address(String host) throws UnknownHostException {
        if (host == null) {
            return null;
        }
        String[] quad = Tokenizer.parseTokens(host, '.');
        if (quad == null || quad.length != 4) {
            return null;
        }
        byte[] addr = new byte[4];
        int i = 0;
        while (i < 4) {
            int part = 0;
            if (quad[i].length() == 0 || quad[i].length() > 3) {
                return null;
            }
            for (int k = 0; k < quad[i].length(); k++) {
                char c = quad[i].charAt(k);
                if (c < '0' || c > '9') {
                    return null;
                }
                part = (part * 10) + (c - 48);
            }
            if (part > 255) {
                return null;
            }
            addr[i] = (byte) part;
            i++;
        }
        return InetAddress.getByAddress(host, addr);
    }

    public TransportManager(String host, int port) throws IOException {
        this.hostname = host;
        this.port = port;
    }

    public int getPacketOverheadEstimate() {
        return this.tc.getPacketOverheadEstimate();
    }

    public void setTcpNoDelay(boolean state) throws IOException {
        this.sock.setTcpNoDelay(state);
    }

    public void setSoTimeout(int timeout) throws IOException {
        this.sock.setSoTimeout(timeout);
    }

    public ConnectionInfo getConnectionInfo(int kexNumber) throws IOException {
        return this.km.getOrWaitForConnectionInfo(kexNumber);
    }

    public Throwable getReasonClosedCause() {
        Throwable th;
        synchronized (this.connectionSemaphore) {
            th = this.reasonClosedCause;
        }
        return th;
    }

    public byte[] getSessionIdentifier() {
        return this.km.sessionId;
    }

    public void close(Throwable cause, boolean useDisconnectPacket) {
        if (!useDisconnectPacket) {
            try {
                this.sock.close();
            } catch (IOException e) {
            }
        }
        synchronized (this.connectionSemaphore) {
            if (!this.connectionClosed) {
                if (useDisconnectPacket) {
                    try {
                        byte[] msg = new PacketDisconnect(11, cause.getMessage(), "").getPayload();
                        if (this.tc != null) {
                            this.tc.sendMessage(msg);
                        }
                    } catch (IOException e2) {
                    }
                    try {
                        this.sock.close();
                    } catch (IOException e3) {
                    }
                }
                this.connectionClosed = true;
                this.reasonClosedCause = cause;
            }
            this.connectionSemaphore.notifyAll();
        }
        Vector monitors = null;
        synchronized (this) {
            if (!this.monitorsWereInformed) {
                this.monitorsWereInformed = true;
                monitors = (Vector) this.connectionMonitors.clone();
            }
        }
        if (monitors != null) {
            for (int i = 0; i < monitors.size(); i++) {
                try {
                    ((ConnectionMonitor) monitors.elementAt(i)).connectionLost(this.reasonClosedCause);
                } catch (Exception e4) {
                }
            }
        }
    }

    private void establishConnection(ProxyData proxyData, int connectTimeout) throws IOException {
        if (proxyData == null) {
            this.sock.connect(new InetSocketAddress(createInetAddress(this.hostname), this.port), connectTimeout);
            this.sock.setSoTimeout(0);
        } else if (proxyData instanceof HTTPProxyData) {
            HTTPProxyData pd = (HTTPProxyData) proxyData;
            this.sock.connect(new InetSocketAddress(createInetAddress(pd.proxyHost), pd.proxyPort), connectTimeout);
            this.sock.setSoTimeout(0);
            StringBuffer sb = new StringBuffer();
            sb.append("CONNECT ");
            sb.append(this.hostname);
            sb.append(':');
            sb.append(this.port);
            sb.append(" HTTP/1.0\r\n");
            if (!(pd.proxyUser == null || pd.proxyPass == null)) {
                char[] encoded = Base64.encode((pd.proxyUser + ":" + pd.proxyPass).getBytes("ISO-8859-1"));
                sb.append("Proxy-Authorization: Basic ");
                sb.append(encoded);
                sb.append("\r\n");
            }
            if (pd.requestHeaderLines != null) {
                for (int i = 0; i < pd.requestHeaderLines.length; i++) {
                    if (pd.requestHeaderLines[i] != null) {
                        sb.append(pd.requestHeaderLines[i]);
                        sb.append("\r\n");
                    }
                }
            }
            sb.append("\r\n");
            OutputStream out = this.sock.getOutputStream();
            out.write(sb.toString().getBytes("ISO-8859-1"));
            out.flush();
            byte[] buffer = new byte[1024];
            InputStream in = this.sock.getInputStream();
            String httpReponse = new String(buffer, 0, ClientServerHello.readLineRN(in, buffer), "ISO-8859-1");
            if (!httpReponse.startsWith("HTTP/")) {
                throw new IOException("The proxy did not send back a valid HTTP response.");
            } else if (httpReponse.length() >= 14 && httpReponse.charAt(8) == ' ' && httpReponse.charAt(12) == ' ') {
                try {
                    int errorCode = Integer.parseInt(httpReponse.substring(9, 12));
                    if (errorCode < 0 || errorCode > 999) {
                        throw new IOException("The proxy did not send back a valid HTTP response.");
                    } else if (errorCode != 200) {
                        throw new HTTPProxyException(httpReponse.substring(13), errorCode);
                    } else {
                        do {
                        } while (ClientServerHello.readLineRN(in, buffer) != 0);
                    }
                } catch (NumberFormatException e) {
                    throw new IOException("The proxy did not send back a valid HTTP response.");
                }
            } else {
                throw new IOException("The proxy did not send back a valid HTTP response.");
            }
        } else {
            throw new IOException("Unsupported ProxyData");
        }
    }

    public void initialize(CryptoWishList cwl, ServerHostKeyVerifier verifier, DHGexParameters dhgex, int connectTimeout, SecureRandom rnd, ProxyData proxyData) throws IOException {
        establishConnection(proxyData, connectTimeout);
        ClientServerHello csh = new ClientServerHello(this.sock.getInputStream(), this.sock.getOutputStream());
        this.tc = new TransportConnection(this.sock.getInputStream(), this.sock.getOutputStream(), rnd);
        this.km = new KexManager(this, csh, cwl, this.hostname, this.port, verifier, rnd);
        this.km.initiateKEX(cwl, dhgex);
        this.receiveThread = new Thread(new Runnable() {
            public void run() {
                try {
                    TransportManager.this.receiveLoop();
                } catch (IOException e) {
                    TransportManager.this.close(e, false);
                    if (TransportManager.log.isEnabled()) {
                        TransportManager.log.log(10, "Receive thread: error in receiveLoop: " + e.getMessage());
                    }
                }
                if (TransportManager.log.isEnabled()) {
                    TransportManager.log.log(50, "Receive thread: back from receiveLoop");
                }
                if (TransportManager.this.km != null) {
                    try {
                        TransportManager.this.km.handleMessage(null, 0);
                    } catch (IOException e2) {
                    }
                }
                for (int i = 0; i < TransportManager.this.messageHandlers.size(); i++) {
                    try {
                        ((HandlerEntry) TransportManager.this.messageHandlers.elementAt(i)).mh.handleMessage(null, 0);
                    } catch (Exception e3) {
                    }
                }
            }
        });
        this.receiveThread.setDaemon(true);
        this.receiveThread.start();
    }

    public void registerMessageHandler(MessageHandler mh, int low, int high) {
        HandlerEntry he = new HandlerEntry();
        he.mh = mh;
        he.low = low;
        he.high = high;
        synchronized (this.messageHandlers) {
            this.messageHandlers.addElement(he);
        }
    }

    public void removeMessageHandler(MessageHandler mh, int low, int high) {
        synchronized (this.messageHandlers) {
            for (int i = 0; i < this.messageHandlers.size(); i++) {
                HandlerEntry he = (HandlerEntry) this.messageHandlers.elementAt(i);
                if (he.mh == mh && he.low == low && he.high == high) {
                    this.messageHandlers.removeElementAt(i);
                    break;
                }
            }
        }
    }

    public void sendKexMessage(byte[] msg) throws IOException {
        synchronized (this.connectionSemaphore) {
            if (this.connectionClosed) {
                throw ((IOException) new IOException("Sorry, this connection is closed.").initCause(this.reasonClosedCause));
            }
            this.flagKexOngoing = true;
            try {
                this.tc.sendMessage(msg);
            } catch (IOException e) {
                close(e, false);
                throw e;
            }
        }
    }

    public void kexFinished() throws IOException {
        synchronized (this.connectionSemaphore) {
            this.flagKexOngoing = false;
            this.connectionSemaphore.notifyAll();
        }
    }

    public void forceKeyExchange(CryptoWishList cwl, DHGexParameters dhgex) throws IOException {
        this.km.initiateKEX(cwl, dhgex);
    }

    public void changeRecvCipher(BlockCipher bc, MAC mac) {
        this.tc.changeRecvCipher(bc, mac);
    }

    public void changeSendCipher(BlockCipher bc, MAC mac) {
        this.tc.changeSendCipher(bc, mac);
    }

    public void sendAsynchronousMessage(byte[] msg) throws IOException {
        synchronized (this.asynchronousQueue) {
            this.asynchronousQueue.addElement(msg);
            if (this.asynchronousQueue.size() > 100) {
                throw new IOException("Error: the peer is not consuming our asynchronous replies.");
            }
            if (this.asynchronousThread == null) {
                this.asynchronousThread = new AsynchronousWorker();
                this.asynchronousThread.setDaemon(true);
                this.asynchronousThread.start();
            }
        }
    }

    public void setConnectionMonitors(Vector monitors) {
        synchronized (this) {
            this.connectionMonitors = (Vector) monitors.clone();
        }
    }

    public void sendMessage(byte[] msg) throws IOException {
        if (Thread.currentThread() == this.receiveThread) {
            throw new IOException("Assertion error: sendMessage may never be invoked by the receiver thread!");
        }
        synchronized (this.connectionSemaphore) {
            while (!this.connectionClosed) {
                if (this.flagKexOngoing) {
                    try {
                        this.connectionSemaphore.wait();
                    } catch (InterruptedException e) {
                    }
                } else {
                    try {
                        this.tc.sendMessage(msg);
                    } catch (IOException e2) {
                        close(e2, false);
                        throw e2;
                    }
                }
            }
            throw ((IOException) new IOException("Sorry, this connection is closed.").initCause(this.reasonClosedCause));
        }
    }

    public void receiveLoop() throws IOException {
        byte[] msg = new byte[35000];
        while (true) {
            int msglen = this.tc.receiveMessage(msg, 0, msg.length);
            int type = msg[0] & 255;
            if (type != 2) {
                TypesReader tr;
                int i;
                char c;
                if (type == 4) {
                    if (log.isEnabled()) {
                        tr = new TypesReader(msg, 0, msglen);
                        tr.readByte();
                        tr.readBoolean();
                        StringBuffer debugMessageBuffer = new StringBuffer();
                        debugMessageBuffer.append(tr.readString(HTTP.UTF_8));
                        for (i = 0; i < debugMessageBuffer.length(); i++) {
                            c = debugMessageBuffer.charAt(i);
                            if (c < ' ' || c > '~') {
                                debugMessageBuffer.setCharAt(i, 65533);
                            }
                        }
                        log.log(50, "DEBUG Message from remote: '" + debugMessageBuffer.toString() + "'");
                    }
                } else if (type == 3) {
                    throw new IOException("Peer sent UNIMPLEMENTED message, that should not happen.");
                } else if (type == 1) {
                    tr = new TypesReader(msg, 0, msglen);
                    tr.readByte();
                    int reason_code = tr.readUINT32();
                    StringBuffer reasonBuffer = new StringBuffer();
                    reasonBuffer.append(tr.readString(HTTP.UTF_8));
                    if (reasonBuffer.length() > 255) {
                        reasonBuffer.setLength(255);
                        reasonBuffer.setCharAt(254, '.');
                        reasonBuffer.setCharAt(253, '.');
                        reasonBuffer.setCharAt(252, '.');
                    }
                    for (i = 0; i < reasonBuffer.length(); i++) {
                        c = reasonBuffer.charAt(i);
                        if (c < ' ' || c > '~') {
                            reasonBuffer.setCharAt(i, 65533);
                        }
                    }
                    throw new IOException("Peer sent DISCONNECT message (reason code " + reason_code + "): " + reasonBuffer.toString());
                } else if (type == 20 || type == 21 || (type >= 30 && type <= 49)) {
                    this.km.handleMessage(msg, msglen);
                } else {
                    MessageHandler mh = null;
                    for (i = 0; i < this.messageHandlers.size(); i++) {
                        HandlerEntry he = (HandlerEntry) this.messageHandlers.elementAt(i);
                        if (he.low <= type && type <= he.high) {
                            mh = he.mh;
                            break;
                        }
                    }
                    if (mh == null) {
                        throw new IOException("Unexpected SSH message (type " + type + ")");
                    }
                    mh.handleMessage(msg, msglen);
                }
            }
        }
    }
}
