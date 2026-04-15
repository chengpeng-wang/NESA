package com.esotericsoftware.kryonet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.IntMap;
import com.esotericsoftware.kryonet.FrameworkMessage.RegisterTCP;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import org.objectweb.asm.Opcodes;

public class Server implements EndPoint {
    private Connection[] connections;
    private Listener dispatchListener;
    private ByteBuffer emptyBuffer;
    private int emptySelects;
    private Object listenerLock;
    Listener[] listeners;
    private int nextConnectionID;
    private final int objectBufferSize;
    private IntMap<Connection> pendingConnections;
    private final Selector selector;
    private final Serialization serialization;
    private ServerSocketChannel serverChannel;
    private volatile boolean shutdown;
    private UdpConnection udp;
    private Object updateLock;
    private Thread updateThread;
    private final int writeBufferSize;

    public Server() {
        this(Opcodes.ACC_ENUM, Opcodes.ACC_STRICT);
    }

    public Server(int i, int i2) {
        this(i, i2, new KryoSerialization());
    }

    public Server(int i, int i2, Serialization serialization) {
        this.connections = new Connection[0];
        this.pendingConnections = new IntMap();
        this.listeners = new Listener[0];
        this.listenerLock = new Object();
        this.nextConnectionID = 1;
        this.updateLock = new Object();
        this.emptyBuffer = ByteBuffer.allocate(0);
        this.dispatchListener = new Listener() {
            public void connected(Connection connection) {
                for (Listener connected : Server.this.listeners) {
                    connected.connected(connection);
                }
            }

            public void disconnected(Connection connection) {
                Server.this.removeConnection(connection);
                for (Listener disconnected : Server.this.listeners) {
                    disconnected.disconnected(connection);
                }
            }

            public void received(Connection connection, Object obj) {
                for (Listener received : Server.this.listeners) {
                    received.received(connection, obj);
                }
            }

            public void idle(Connection connection) {
                for (Listener idle : Server.this.listeners) {
                    idle.idle(connection);
                }
            }
        };
        this.writeBufferSize = i;
        this.objectBufferSize = i2;
        this.serialization = serialization;
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

    public void bind(int i) throws IOException {
        bind(new InetSocketAddress(i), null);
    }

    public void bind(int i, int i2) throws IOException {
        bind(new InetSocketAddress(i), new InetSocketAddress(i2));
    }

    public void bind(InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2) throws IOException {
        close();
        synchronized (this.updateLock) {
            this.selector.wakeup();
            try {
                this.serverChannel = this.selector.provider().openServerSocketChannel();
                this.serverChannel.socket().bind(inetSocketAddress);
                this.serverChannel.configureBlocking(false);
                this.serverChannel.register(this.selector, 16);
                if (inetSocketAddress2 != null) {
                    this.udp = new UdpConnection(this.serialization, this.objectBufferSize);
                    this.udp.bind(this.selector, inetSocketAddress2);
                }
            } catch (IOException e) {
                close();
                throw e;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:122:0x0154 A:{SYNTHETIC, Splitter:B:122:0x0154} */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x009d A:{SYNTHETIC, Splitter:B:46:0x009d} */
    public void update(int r13) throws java.io.IOException {
        /*
        r12 = this;
        r6 = 25;
        r3 = 0;
        r0 = java.lang.Thread.currentThread();
        r12.updateThread = r0;
        r1 = r12.updateLock;
        monitor-enter(r1);
        monitor-exit(r1);	 Catch:{ all -> 0x005c }
        r1 = java.lang.System.currentTimeMillis();
        if (r13 <= 0) goto L_0x005f;
    L_0x0013:
        r0 = r12.selector;
        r4 = (long) r13;
        r0 = r0.select(r4);
    L_0x001a:
        if (r0 != 0) goto L_0x0066;
    L_0x001c:
        r0 = r12.emptySelects;
        r0 = r0 + 1;
        r12.emptySelects = r0;
        r0 = r12.emptySelects;
        r4 = 100;
        if (r0 != r4) goto L_0x0039;
    L_0x0028:
        r12.emptySelects = r3;
        r4 = java.lang.System.currentTimeMillis();
        r0 = r4 - r1;
        r2 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r2 >= 0) goto L_0x0039;
    L_0x0034:
        r0 = r6 - r0;
        java.lang.Thread.sleep(r0);	 Catch:{ InterruptedException -> 0x016f }
    L_0x0039:
        r1 = java.lang.System.currentTimeMillis();
        r4 = r12.connections;
        r5 = r4.length;
        r0 = r3;
    L_0x0041:
        if (r0 >= r5) goto L_0x0178;
    L_0x0043:
        r3 = r4[r0];
        r6 = r3.tcp;
        r6 = r6.isTimedOut(r1);
        if (r6 == 0) goto L_0x0160;
    L_0x004d:
        r3.close();
    L_0x0050:
        r6 = r3.isIdle();
        if (r6 == 0) goto L_0x0059;
    L_0x0056:
        r3.notifyIdle();
    L_0x0059:
        r0 = r0 + 1;
        goto L_0x0041;
    L_0x005c:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x005c }
        throw r0;
    L_0x005f:
        r0 = r12.selector;
        r0 = r0.selectNow();
        goto L_0x001a;
    L_0x0066:
        r12.emptySelects = r3;
        r0 = r12.selector;
        r5 = r0.selectedKeys();
        monitor-enter(r5);
        r6 = r12.udp;	 Catch:{ all -> 0x00a1 }
        r7 = r5.iterator();	 Catch:{ all -> 0x00a1 }
    L_0x0075:
        r0 = r7.hasNext();	 Catch:{ all -> 0x00a1 }
        if (r0 == 0) goto L_0x015d;
    L_0x007b:
        r0 = r7.next();	 Catch:{ all -> 0x00a1 }
        r0 = (java.nio.channels.SelectionKey) r0;	 Catch:{ all -> 0x00a1 }
        r7.remove();	 Catch:{ all -> 0x00a1 }
        r1 = r0.attachment();	 Catch:{ all -> 0x00a1 }
        r1 = (com.esotericsoftware.kryonet.Connection) r1;	 Catch:{ all -> 0x00a1 }
        r2 = r0.readyOps();	 Catch:{ CancelledKeyException -> 0x009a }
        if (r1 == 0) goto L_0x00cf;
    L_0x0090:
        if (r6 == 0) goto L_0x00a4;
    L_0x0092:
        r4 = r1.udpRemoteAddress;	 Catch:{ CancelledKeyException -> 0x009a }
        if (r4 != 0) goto L_0x00a4;
    L_0x0096:
        r1.close();	 Catch:{ CancelledKeyException -> 0x009a }
        goto L_0x0075;
    L_0x009a:
        r2 = move-exception;
    L_0x009b:
        if (r1 == 0) goto L_0x0154;
    L_0x009d:
        r1.close();	 Catch:{ all -> 0x00a1 }
        goto L_0x0075;
    L_0x00a1:
        r0 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x00a1 }
        throw r0;
    L_0x00a4:
        r4 = r2 & 1;
        r8 = 1;
        if (r4 != r8) goto L_0x00b1;
    L_0x00a9:
        r4 = r1.tcp;	 Catch:{ IOException -> 0x00c5, KryoNetException -> 0x00ca }
        r4 = r4.readObject(r1);	 Catch:{ IOException -> 0x00c5, KryoNetException -> 0x00ca }
        if (r4 != 0) goto L_0x00c1;
    L_0x00b1:
        r2 = r2 & 4;
        r4 = 4;
        if (r2 != r4) goto L_0x0075;
    L_0x00b6:
        r2 = r1.tcp;	 Catch:{ IOException -> 0x00bc }
        r2.writeOperation();	 Catch:{ IOException -> 0x00bc }
        goto L_0x0075;
    L_0x00bc:
        r2 = move-exception;
        r1.close();	 Catch:{ CancelledKeyException -> 0x009a }
        goto L_0x0075;
    L_0x00c1:
        r1.notifyReceived(r4);	 Catch:{ IOException -> 0x00c5, KryoNetException -> 0x00ca }
        goto L_0x00a9;
    L_0x00c5:
        r4 = move-exception;
        r1.close();	 Catch:{ CancelledKeyException -> 0x009a }
        goto L_0x00b1;
    L_0x00ca:
        r4 = move-exception;
        r1.close();	 Catch:{ CancelledKeyException -> 0x009a }
        goto L_0x00b1;
    L_0x00cf:
        r2 = r2 & 16;
        r4 = 16;
        if (r2 != r4) goto L_0x00e5;
    L_0x00d5:
        r2 = r12.serverChannel;	 Catch:{ CancelledKeyException -> 0x009a }
        if (r2 == 0) goto L_0x0075;
    L_0x00d9:
        r2 = r2.accept();	 Catch:{ IOException -> 0x00e3 }
        if (r2 == 0) goto L_0x0075;
    L_0x00df:
        r12.acceptOperation(r2);	 Catch:{ IOException -> 0x00e3 }
        goto L_0x0075;
    L_0x00e3:
        r0 = move-exception;
        goto L_0x0075;
    L_0x00e5:
        if (r6 != 0) goto L_0x00ef;
    L_0x00e7:
        r2 = r0.channel();	 Catch:{ CancelledKeyException -> 0x009a }
        r2.close();	 Catch:{ CancelledKeyException -> 0x009a }
        goto L_0x0075;
    L_0x00ef:
        r8 = r6.readFromAddress();	 Catch:{ IOException -> 0x0172 }
        if (r8 == 0) goto L_0x0075;
    L_0x00f5:
        r9 = r12.connections;	 Catch:{ CancelledKeyException -> 0x009a }
        r10 = r9.length;	 Catch:{ CancelledKeyException -> 0x009a }
        r4 = r3;
    L_0x00f9:
        if (r4 >= r10) goto L_0x0179;
    L_0x00fb:
        r2 = r9[r4];	 Catch:{ CancelledKeyException -> 0x009a }
        r11 = r2.udpRemoteAddress;	 Catch:{ CancelledKeyException -> 0x009a }
        r11 = r8.equals(r11);	 Catch:{ CancelledKeyException -> 0x009a }
        if (r11 == 0) goto L_0x0139;
    L_0x0105:
        r1 = r6.readObject(r2);	 Catch:{ KryoNetException -> 0x0175 }
        r4 = r1 instanceof com.esotericsoftware.kryonet.FrameworkMessage;	 Catch:{ CancelledKeyException -> 0x0135 }
        if (r4 == 0) goto L_0x014d;
    L_0x010d:
        r4 = r1 instanceof com.esotericsoftware.kryonet.FrameworkMessage.RegisterUDP;	 Catch:{ CancelledKeyException -> 0x0135 }
        if (r4 == 0) goto L_0x013d;
    L_0x0111:
        r1 = (com.esotericsoftware.kryonet.FrameworkMessage.RegisterUDP) r1;	 Catch:{ CancelledKeyException -> 0x0135 }
        r1 = r1.connectionID;	 Catch:{ CancelledKeyException -> 0x0135 }
        r4 = r12.pendingConnections;	 Catch:{ CancelledKeyException -> 0x0135 }
        r1 = r4.remove(r1);	 Catch:{ CancelledKeyException -> 0x0135 }
        r1 = (com.esotericsoftware.kryonet.Connection) r1;	 Catch:{ CancelledKeyException -> 0x0135 }
        if (r1 == 0) goto L_0x0075;
    L_0x011f:
        r4 = r1.udpRemoteAddress;	 Catch:{ CancelledKeyException -> 0x0135 }
        if (r4 != 0) goto L_0x0075;
    L_0x0123:
        r1.udpRemoteAddress = r8;	 Catch:{ CancelledKeyException -> 0x0135 }
        r12.addConnection(r1);	 Catch:{ CancelledKeyException -> 0x0135 }
        r4 = new com.esotericsoftware.kryonet.FrameworkMessage$RegisterUDP;	 Catch:{ CancelledKeyException -> 0x0135 }
        r4.m716init();	 Catch:{ CancelledKeyException -> 0x0135 }
        r1.sendTCP(r4);	 Catch:{ CancelledKeyException -> 0x0135 }
        r1.notifyConnected();	 Catch:{ CancelledKeyException -> 0x0135 }
        goto L_0x0075;
    L_0x0135:
        r1 = move-exception;
        r1 = r2;
        goto L_0x009b;
    L_0x0139:
        r2 = r4 + 1;
        r4 = r2;
        goto L_0x00f9;
    L_0x013d:
        r4 = r1 instanceof com.esotericsoftware.kryonet.FrameworkMessage.DiscoverHost;	 Catch:{ CancelledKeyException -> 0x0135 }
        if (r4 == 0) goto L_0x014d;
    L_0x0141:
        r1 = r6.datagramChannel;	 Catch:{ IOException -> 0x014a }
        r4 = r12.emptyBuffer;	 Catch:{ IOException -> 0x014a }
        r1.send(r4, r8);	 Catch:{ IOException -> 0x014a }
        goto L_0x0075;
    L_0x014a:
        r0 = move-exception;
        goto L_0x0075;
    L_0x014d:
        if (r2 == 0) goto L_0x0075;
    L_0x014f:
        r2.notifyReceived(r1);	 Catch:{ CancelledKeyException -> 0x0135 }
        goto L_0x0075;
    L_0x0154:
        r0 = r0.channel();	 Catch:{ all -> 0x00a1 }
        r0.close();	 Catch:{ all -> 0x00a1 }
        goto L_0x0075;
    L_0x015d:
        monitor-exit(r5);	 Catch:{ all -> 0x00a1 }
        goto L_0x0039;
    L_0x0160:
        r6 = r3.tcp;
        r6 = r6.needsKeepAlive(r1);
        if (r6 == 0) goto L_0x0050;
    L_0x0168:
        r6 = com.esotericsoftware.kryonet.FrameworkMessage.keepAlive;
        r3.sendTCP(r6);
        goto L_0x0050;
    L_0x016f:
        r0 = move-exception;
        goto L_0x0039;
    L_0x0172:
        r0 = move-exception;
        goto L_0x0075;
    L_0x0175:
        r0 = move-exception;
        goto L_0x0075;
    L_0x0178:
        return;
    L_0x0179:
        r2 = r1;
        goto L_0x0105;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryonet.Server.update(int):void");
    }

    public void run() {
        this.shutdown = false;
        while (!this.shutdown) {
            try {
                update(250);
            } catch (IOException e) {
                close();
            }
        }
    }

    public void start() {
        new Thread(this, "Server").start();
    }

    public void stop() {
        if (!this.shutdown) {
            close();
            this.shutdown = true;
        }
    }

    private void acceptOperation(SocketChannel socketChannel) {
        Connection newConnection = newConnection();
        newConnection.initialize(this.serialization, this.writeBufferSize, this.objectBufferSize);
        newConnection.endPoint = this;
        UdpConnection udpConnection = this.udp;
        if (udpConnection != null) {
            newConnection.udp = udpConnection;
        }
        try {
            newConnection.tcp.accept(this.selector, socketChannel).attach(newConnection);
            int i = this.nextConnectionID;
            this.nextConnectionID = i + 1;
            if (this.nextConnectionID == -1) {
                this.nextConnectionID = 1;
            }
            newConnection.id = i;
            newConnection.setConnected(true);
            newConnection.addListener(this.dispatchListener);
            if (udpConnection == null) {
                addConnection(newConnection);
            } else {
                this.pendingConnections.put(i, newConnection);
            }
            RegisterTCP registerTCP = new RegisterTCP();
            registerTCP.connectionID = i;
            newConnection.sendTCP(registerTCP);
            if (udpConnection == null) {
                newConnection.notifyConnected();
            }
        } catch (IOException e) {
            newConnection.close();
        }
    }

    /* access modifiers changed from: protected */
    public Connection newConnection() {
        return new Connection();
    }

    private void addConnection(Connection connection) {
        Connection[] connectionArr = new Connection[(this.connections.length + 1)];
        connectionArr[0] = connection;
        System.arraycopy(this.connections, 0, connectionArr, 1, this.connections.length);
        this.connections = connectionArr;
    }

    /* access modifiers changed from: 0000 */
    public void removeConnection(Connection connection) {
        ArrayList arrayList = new ArrayList(Arrays.asList(this.connections));
        arrayList.remove(connection);
        this.connections = (Connection[]) arrayList.toArray(new Connection[arrayList.size()]);
        this.pendingConnections.remove(connection.id);
    }

    public void sendToAllTCP(Object obj) {
        for (Connection sendTCP : this.connections) {
            sendTCP.sendTCP(obj);
        }
    }

    public void sendToAllExceptTCP(int i, Object obj) {
        for (Connection connection : this.connections) {
            if (connection.id != i) {
                connection.sendTCP(obj);
            }
        }
    }

    public void sendToTCP(int i, Object obj) {
        for (Connection connection : this.connections) {
            if (connection.id == i) {
                connection.sendTCP(obj);
                return;
            }
        }
    }

    public void sendToAllUDP(Object obj) {
        for (Connection sendUDP : this.connections) {
            sendUDP.sendUDP(obj);
        }
    }

    public void sendToAllExceptUDP(int i, Object obj) {
        for (Connection connection : this.connections) {
            if (connection.id != i) {
                connection.sendUDP(obj);
            }
        }
    }

    public void sendToUDP(int i, Object obj) {
        for (Connection connection : this.connections) {
            if (connection.id == i) {
                connection.sendUDP(obj);
                return;
            }
        }
    }

    public void addListener(Listener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null.");
        }
        synchronized (this.listenerLock) {
            Listener[] listenerArr = this.listeners;
            for (Listener listener2 : listenerArr) {
                if (listener == listener2) {
                    return;
                }
            }
            Listener[] listenerArr2 = new Listener[(r3 + 1)];
            listenerArr2[0] = listener;
            System.arraycopy(listenerArr, 0, listenerArr2, 1, r3);
            this.listeners = listenerArr2;
        }
    }

    public void removeListener(Listener listener) {
        int i = 0;
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null.");
        }
        synchronized (this.listenerLock) {
            Listener[] listenerArr = new Listener[(r5 - 1)];
            for (Listener listener2 : this.listeners) {
                if (listener != listener2) {
                    if (i == r5 - 1) {
                        return;
                    }
                    int i2 = i + 1;
                    listenerArr[i] = listener2;
                    i = i2;
                }
            }
            this.listeners = listenerArr;
        }
    }

    public void close() {
        for (Connection close : this.connections) {
            close.close();
        }
        Connection[] connectionArr = new Connection[0];
        ServerSocketChannel serverSocketChannel = this.serverChannel;
        if (serverSocketChannel != null) {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
            }
            this.serverChannel = null;
        }
        UdpConnection udpConnection = this.udp;
        if (udpConnection != null) {
            udpConnection.close();
            this.udp = null;
        }
        synchronized (this.updateLock) {
            this.selector.wakeup();
            try {
                this.selector.selectNow();
            } catch (IOException e2) {
            }
        }
    }

    public Thread getUpdateThread() {
        return this.updateThread;
    }

    public Connection[] getConnections() {
        return this.connections;
    }
}
