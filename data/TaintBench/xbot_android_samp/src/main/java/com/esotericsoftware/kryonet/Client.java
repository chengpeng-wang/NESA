package com.esotericsoftware.kryonet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.FrameworkMessage.DiscoverHost;
import com.esotericsoftware.kryonet.FrameworkMessage.RegisterTCP;
import com.esotericsoftware.kryonet.FrameworkMessage.RegisterUDP;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.security.AccessControlException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.objectweb.asm.Opcodes;

public class Client extends Connection implements EndPoint {
    private InetAddress connectHost;
    private int connectTcpPort;
    private int connectTimeout;
    private int connectUdpPort;
    private int emptySelects;
    private boolean isClosed;
    private Selector selector;
    private final Serialization serialization;
    private volatile boolean shutdown;
    private volatile boolean tcpRegistered;
    private Object tcpRegistrationLock;
    private volatile boolean udpRegistered;
    private Object udpRegistrationLock;
    private final Object updateLock;
    private Thread updateThread;

    static {
        try {
            System.setProperty("java.net.preferIPv6Addresses", "false");
        } catch (AccessControlException e) {
        }
    }

    public Client() {
        this(Opcodes.ACC_ANNOTATION, Opcodes.ACC_STRICT);
    }

    public Client(int i, int i2) {
        this(i, i2, new KryoSerialization());
    }

    public Client(int i, int i2, Serialization serialization) {
        this.tcpRegistrationLock = new Object();
        this.udpRegistrationLock = new Object();
        this.updateLock = new Object();
        this.endPoint = this;
        this.serialization = serialization;
        initialize(serialization, i, i2);
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException("Error opening selector.", e);
        }
    }

    public Serialization getSerialization() {
        return this.serialization;
    }

    public Kryo getKryo() {
        return ((KryoSerialization) this.serialization).getKryo();
    }

    public void connect(int i, String str, int i2) throws IOException {
        connect(i, InetAddress.getByName(str), i2, -1);
    }

    public void connect(int i, String str, int i2, int i3) throws IOException {
        connect(i, InetAddress.getByName(str), i2, i3);
    }

    public void connect(int i, InetAddress inetAddress, int i2) throws IOException {
        connect(i, inetAddress, i2, -1);
    }

    public void connect(int i, InetAddress inetAddress, int i2, int i3) throws IOException {
        if (inetAddress == null) {
            throw new IllegalArgumentException("host cannot be null.");
        } else if (Thread.currentThread() == getUpdateThread()) {
            throw new IllegalStateException("Cannot connect on the connection's update thread.");
        } else {
            this.connectTimeout = i;
            this.connectHost = inetAddress;
            this.connectTcpPort = i2;
            this.connectUdpPort = i3;
            close();
            this.id = -1;
            if (i3 != -1) {
                try {
                    this.udp = new UdpConnection(this.serialization, this.tcp.readBuffer.capacity());
                } catch (IOException e) {
                    close();
                    throw e;
                }
            }
            synchronized (this.updateLock) {
                this.tcpRegistered = false;
                this.selector.wakeup();
                long currentTimeMillis = System.currentTimeMillis() + ((long) i);
                this.tcp.connect(this.selector, new InetSocketAddress(inetAddress, i2), 5000);
            }
            synchronized (this.tcpRegistrationLock) {
                while (!this.tcpRegistered && System.currentTimeMillis() < currentTimeMillis) {
                    try {
                        this.tcpRegistrationLock.wait(100);
                    } catch (InterruptedException e2) {
                    }
                }
                if (this.tcpRegistered) {
                } else {
                    throw new SocketTimeoutException("Connected, but timed out during TCP registration.\nNote: Client#update must be called in a separate thread during connect.");
                }
            }
            if (i3 != -1) {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, i3);
                synchronized (this.updateLock) {
                    this.udpRegistered = false;
                    this.selector.wakeup();
                    this.udp.connect(this.selector, inetSocketAddress);
                }
                synchronized (this.udpRegistrationLock) {
                    while (!this.udpRegistered && System.currentTimeMillis() < currentTimeMillis) {
                        RegisterUDP registerUDP = new RegisterUDP();
                        registerUDP.connectionID = this.id;
                        this.udp.send(this, registerUDP, inetSocketAddress);
                        try {
                            this.udpRegistrationLock.wait(100);
                        } catch (InterruptedException e3) {
                        }
                    }
                    if (this.udpRegistered) {
                    } else {
                        throw new SocketTimeoutException("Connected, but timed out during UDP registration: " + inetAddress + ":" + i3);
                    }
                }
            }
        }
    }

    public void reconnect() throws IOException {
        reconnect(this.connectTimeout);
    }

    public void reconnect(int i) throws IOException {
        if (this.connectHost == null) {
            throw new IllegalStateException("This client has never been connected.");
        }
        connect(this.connectTimeout, this.connectHost, this.connectTcpPort, this.connectUdpPort);
    }

    public void update(int i) throws IOException {
        int select;
        this.updateThread = Thread.currentThread();
        synchronized (this.updateLock) {
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (i > 0) {
            select = this.selector.select((long) i);
        } else {
            select = this.selector.selectNow();
        }
        if (select == 0) {
            this.emptySelects++;
            if (this.emptySelects == 100) {
                this.emptySelects = 0;
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                if (currentTimeMillis2 < 25) {
                    try {
                        Thread.sleep(25 - currentTimeMillis2);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } else {
            this.emptySelects = 0;
            this.isClosed = false;
            Set selectedKeys = this.selector.selectedKeys();
            synchronized (selectedKeys) {
                Iterator it = selectedKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey selectionKey = (SelectionKey) it.next();
                    it.remove();
                    try {
                        int readyOps = selectionKey.readyOps();
                        if ((readyOps & 1) == 1) {
                            Object readObject;
                            if (selectionKey.attachment() == this.tcp) {
                                while (true) {
                                    readObject = this.tcp.readObject(this);
                                    if (readObject == null) {
                                        break;
                                    } else if (this.tcpRegistered) {
                                        if (this.udp == null || this.udpRegistered) {
                                            if (this.isConnected) {
                                                keepAlive();
                                                notifyReceived(readObject);
                                            }
                                        } else if (readObject instanceof RegisterUDP) {
                                            synchronized (this.udpRegistrationLock) {
                                                this.udpRegistered = true;
                                                this.udpRegistrationLock.notifyAll();
                                                setConnected(true);
                                            }
                                            notifyConnected();
                                        } else {
                                            continue;
                                        }
                                    } else if (readObject instanceof RegisterTCP) {
                                        this.id = ((RegisterTCP) readObject).connectionID;
                                        synchronized (this.tcpRegistrationLock) {
                                            this.tcpRegistered = true;
                                            this.tcpRegistrationLock.notifyAll();
                                            if (this.udp == null) {
                                                setConnected(true);
                                            }
                                        }
                                        if (this.udp == null) {
                                            notifyConnected();
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                                while (true) {
                                }
                            } else if (this.udp.readFromAddress() != null) {
                                readObject = this.udp.readObject(this);
                                if (readObject != null) {
                                    keepAlive();
                                    notifyReceived(readObject);
                                }
                            }
                        }
                        if ((readyOps & 4) == 4) {
                            this.tcp.writeOperation();
                        }
                    } catch (CancelledKeyException e2) {
                    }
                }
            }
        }
        if (this.isConnected) {
            if (this.tcp.isTimedOut(System.currentTimeMillis())) {
                close();
            } else {
                keepAlive();
            }
            if (isIdle()) {
                notifyIdle();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void keepAlive() {
        if (this.isConnected) {
            long currentTimeMillis = System.currentTimeMillis();
            if (this.tcp.needsKeepAlive(currentTimeMillis)) {
                sendTCP(FrameworkMessage.keepAlive);
            }
            if (this.udp != null && this.udpRegistered && this.udp.needsKeepAlive(currentTimeMillis)) {
                sendUDP(FrameworkMessage.keepAlive);
            }
        }
    }

    public void run() {
        this.shutdown = false;
        while (!this.shutdown) {
            try {
                update(250);
            } catch (IOException e) {
                close();
            } catch (KryoNetException e2) {
                close();
                throw e2;
            }
        }
    }

    public void start() {
        if (this.updateThread != null) {
            this.shutdown = true;
            try {
                this.updateThread.join(5000);
            } catch (InterruptedException e) {
            }
        }
        this.updateThread = new Thread(this, "Client");
        this.updateThread.setDaemon(true);
        this.updateThread.start();
    }

    public void stop() {
        if (!this.shutdown) {
            close();
            this.shutdown = true;
            this.selector.wakeup();
        }
    }

    public void close() {
        super.close();
        synchronized (this.updateLock) {
            if (!this.isClosed) {
                this.isClosed = true;
                this.selector.wakeup();
                try {
                    this.selector.selectNow();
                } catch (IOException e) {
                }
            }
        }
    }

    public void addListener(Listener listener) {
        super.addListener(listener);
    }

    public void removeListener(Listener listener) {
        super.removeListener(listener);
    }

    public void setKeepAliveUDP(int i) {
        if (this.udp == null) {
            throw new IllegalStateException("Not connected via UDP.");
        }
        this.udp.keepAliveMillis = i;
    }

    public Thread getUpdateThread() {
        return this.updateThread;
    }

    private void broadcast(int i, DatagramSocket datagramSocket) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(64);
        this.serialization.write(null, allocate, new DiscoverHost());
        allocate.flip();
        byte[] bArr = new byte[allocate.limit()];
        allocate.get(bArr);
        Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
        while (it.hasNext()) {
            Iterator it2 = Collections.list(((NetworkInterface) it.next()).getInetAddresses()).iterator();
            while (it2.hasNext()) {
                byte[] address = ((InetAddress) it2.next()).getAddress();
                address[3] = (byte) -1;
                try {
                    datagramSocket.send(new DatagramPacket(bArr, bArr.length, InetAddress.getByAddress(address), i));
                } catch (Exception e) {
                }
                address[2] = (byte) -1;
                try {
                    datagramSocket.send(new DatagramPacket(bArr, bArr.length, InetAddress.getByAddress(address), i));
                } catch (Exception e2) {
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0037  */
    public java.net.InetAddress discoverHost(int r7, int r8) {
        /*
        r6 = this;
        r0 = 0;
        r1 = new java.net.DatagramSocket;	 Catch:{ IOException -> 0x0029, all -> 0x0031 }
        r1.<init>();	 Catch:{ IOException -> 0x0029, all -> 0x0031 }
        r6.broadcast(r7, r1);	 Catch:{ IOException -> 0x003d, all -> 0x003b }
        r1.setSoTimeout(r8);	 Catch:{ IOException -> 0x003d, all -> 0x003b }
        r2 = new java.net.DatagramPacket;	 Catch:{ IOException -> 0x003d, all -> 0x003b }
        r3 = 0;
        r3 = new byte[r3];	 Catch:{ IOException -> 0x003d, all -> 0x003b }
        r4 = 0;
        r2.<init>(r3, r4);	 Catch:{ IOException -> 0x003d, all -> 0x003b }
        r1.receive(r2);	 Catch:{ SocketTimeoutException -> 0x0022 }
        r0 = r2.getAddress();	 Catch:{ IOException -> 0x003d, all -> 0x003b }
        if (r1 == 0) goto L_0x0021;
    L_0x001e:
        r1.close();
    L_0x0021:
        return r0;
    L_0x0022:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0021;
    L_0x0025:
        r1.close();
        goto L_0x0021;
    L_0x0029:
        r1 = move-exception;
        r1 = r0;
    L_0x002b:
        if (r1 == 0) goto L_0x0021;
    L_0x002d:
        r1.close();
        goto L_0x0021;
    L_0x0031:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x0035:
        if (r1 == 0) goto L_0x003a;
    L_0x0037:
        r1.close();
    L_0x003a:
        throw r0;
    L_0x003b:
        r0 = move-exception;
        goto L_0x0035;
    L_0x003d:
        r2 = move-exception;
        goto L_0x002b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryonet.Client.discoverHost(int, int):java.net.InetAddress");
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0037  */
    public java.util.List<java.net.InetAddress> discoverHosts(int r6, int r7) {
        /*
        r5 = this;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r2 = 0;
        r1 = new java.net.DatagramSocket;	 Catch:{ IOException -> 0x003d, all -> 0x0033 }
        r1.<init>();	 Catch:{ IOException -> 0x003d, all -> 0x0033 }
        r5.broadcast(r6, r1);	 Catch:{ IOException -> 0x0025, all -> 0x003b }
        r1.setSoTimeout(r7);	 Catch:{ IOException -> 0x0025, all -> 0x003b }
    L_0x0011:
        r2 = new java.net.DatagramPacket;	 Catch:{ IOException -> 0x0025, all -> 0x003b }
        r3 = 0;
        r3 = new byte[r3];	 Catch:{ IOException -> 0x0025, all -> 0x003b }
        r4 = 0;
        r2.<init>(r3, r4);	 Catch:{ IOException -> 0x0025, all -> 0x003b }
        r1.receive(r2);	 Catch:{ SocketTimeoutException -> 0x002c }
        r2 = r2.getAddress();	 Catch:{ IOException -> 0x0025, all -> 0x003b }
        r0.add(r2);	 Catch:{ IOException -> 0x0025, all -> 0x003b }
        goto L_0x0011;
    L_0x0025:
        r2 = move-exception;
    L_0x0026:
        if (r1 == 0) goto L_0x002b;
    L_0x0028:
        r1.close();
    L_0x002b:
        return r0;
    L_0x002c:
        r2 = move-exception;
        if (r1 == 0) goto L_0x002b;
    L_0x002f:
        r1.close();
        goto L_0x002b;
    L_0x0033:
        r0 = move-exception;
        r1 = r2;
    L_0x0035:
        if (r1 == 0) goto L_0x003a;
    L_0x0037:
        r1.close();
    L_0x003a:
        throw r0;
    L_0x003b:
        r0 = move-exception;
        goto L_0x0035;
    L_0x003d:
        r1 = move-exception;
        r1 = r2;
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryonet.Client.discoverHosts(int, int):java.util.List");
    }
}
