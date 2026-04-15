package com.esotericsoftware.kryonet;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

class TcpConnection {
    private static final int IPTOS_LOWDELAY = 16;
    boolean bufferPositionFix;
    private int currentObjectLength;
    float idleThreshold = 0.1f;
    int keepAliveMillis = 8000;
    private volatile long lastReadTime;
    private volatile long lastWriteTime;
    final ByteBuffer readBuffer;
    private SelectionKey selectionKey;
    final Serialization serialization;
    SocketChannel socketChannel;
    int timeoutMillis = 12000;
    final ByteBuffer writeBuffer;
    private final Object writeLock = new Object();

    public TcpConnection(Serialization serialization, int i, int i2) {
        this.serialization = serialization;
        this.writeBuffer = ByteBuffer.allocate(i);
        this.readBuffer = ByteBuffer.allocate(i2);
        this.readBuffer.flip();
    }

    public SelectionKey accept(Selector selector, SocketChannel socketChannel) throws IOException {
        this.writeBuffer.clear();
        this.readBuffer.clear();
        this.readBuffer.flip();
        this.currentObjectLength = 0;
        try {
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            socketChannel.socket().setTcpNoDelay(true);
            this.selectionKey = socketChannel.register(selector, 1);
            long currentTimeMillis = System.currentTimeMillis();
            this.lastWriteTime = currentTimeMillis;
            this.lastReadTime = currentTimeMillis;
            return this.selectionKey;
        } catch (IOException e) {
            close();
            throw e;
        }
    }

    public void connect(Selector selector, SocketAddress socketAddress, int i) throws IOException {
        close();
        this.writeBuffer.clear();
        this.readBuffer.clear();
        this.readBuffer.flip();
        this.currentObjectLength = 0;
        try {
            SocketChannel openSocketChannel = selector.provider().openSocketChannel();
            Socket socket = openSocketChannel.socket();
            socket.setTcpNoDelay(true);
            socket.connect(socketAddress, i);
            openSocketChannel.configureBlocking(false);
            this.socketChannel = openSocketChannel;
            this.selectionKey = openSocketChannel.register(selector, 1);
            this.selectionKey.attach(this);
            long currentTimeMillis = System.currentTimeMillis();
            this.lastWriteTime = currentTimeMillis;
            this.lastReadTime = currentTimeMillis;
        } catch (IOException e) {
            close();
            IOException iOException = new IOException("Unable to connect to: " + socketAddress);
            iOException.initCause(e);
            throw iOException;
        }
    }

    /* JADX WARNING: Missing block: B:13:0x0046, code skipped:
            if (r6.readBuffer.remaining() < r2) goto L_0x0048;
     */
    /* JADX WARNING: Missing block: B:30:0x00c7, code skipped:
            if (r6.readBuffer.remaining() >= r2) goto L_0x00c9;
     */
    public java.lang.Object readObject(com.esotericsoftware.kryonet.Connection r7) throws java.io.IOException {
        /*
        r6 = this;
        r0 = 0;
        r5 = -1;
        r1 = r6.socketChannel;
        if (r1 != 0) goto L_0x000e;
    L_0x0006:
        r0 = new java.net.SocketException;
        r1 = "Connection is closed.";
        r0.<init>(r1);
        throw r0;
    L_0x000e:
        r2 = r6.currentObjectLength;
        if (r2 != 0) goto L_0x0097;
    L_0x0012:
        r2 = r6.serialization;
        r2 = r2.getLengthLength();
        r3 = r6.readBuffer;
        r3 = r3.remaining();
        if (r3 >= r2) goto L_0x0049;
    L_0x0020:
        r3 = r6.readBuffer;
        r3.compact();
        r3 = r6.readBuffer;
        r3 = r1.read(r3);
        r4 = r6.readBuffer;
        r4.flip();
        if (r3 != r5) goto L_0x003a;
    L_0x0032:
        r0 = new java.net.SocketException;
        r1 = "Connection is closed.";
        r0.<init>(r1);
        throw r0;
    L_0x003a:
        r3 = java.lang.System.currentTimeMillis();
        r6.lastReadTime = r3;
        r3 = r6.readBuffer;
        r3 = r3.remaining();
        if (r3 >= r2) goto L_0x0049;
    L_0x0048:
        return r0;
    L_0x0049:
        r2 = r6.serialization;
        r3 = r6.readBuffer;
        r2 = r2.readLength(r3);
        r6.currentObjectLength = r2;
        r2 = r6.currentObjectLength;
        if (r2 > 0) goto L_0x0072;
    L_0x0057:
        r0 = new com.esotericsoftware.kryonet.KryoNetException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Invalid object length: ";
        r1 = r1.append(r2);
        r2 = r6.currentObjectLength;
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.m154init(r1);
        throw r0;
    L_0x0072:
        r2 = r6.currentObjectLength;
        r3 = r6.readBuffer;
        r3 = r3.capacity();
        if (r2 <= r3) goto L_0x0097;
    L_0x007c:
        r0 = new com.esotericsoftware.kryonet.KryoNetException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Unable to read object larger than read buffer: ";
        r1 = r1.append(r2);
        r2 = r6.currentObjectLength;
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.m154init(r1);
        throw r0;
    L_0x0097:
        r2 = r6.currentObjectLength;
        r3 = r6.readBuffer;
        r3 = r3.remaining();
        if (r3 >= r2) goto L_0x00c9;
    L_0x00a1:
        r3 = r6.readBuffer;
        r3.compact();
        r3 = r6.readBuffer;
        r1 = r1.read(r3);
        r3 = r6.readBuffer;
        r3.flip();
        if (r1 != r5) goto L_0x00bb;
    L_0x00b3:
        r0 = new java.net.SocketException;
        r1 = "Connection is closed.";
        r0.<init>(r1);
        throw r0;
    L_0x00bb:
        r3 = java.lang.System.currentTimeMillis();
        r6.lastReadTime = r3;
        r1 = r6.readBuffer;
        r1 = r1.remaining();
        if (r1 < r2) goto L_0x0048;
    L_0x00c9:
        r0 = 0;
        r6.currentObjectLength = r0;
        r0 = r6.readBuffer;
        r1 = r0.position();
        r0 = r6.readBuffer;
        r3 = r0.limit();
        r0 = r6.readBuffer;
        r4 = r1 + r2;
        r0.limit(r4);
        r0 = r6.serialization;	 Catch:{ Exception -> 0x0120 }
        r4 = r6.readBuffer;	 Catch:{ Exception -> 0x0120 }
        r0 = r0.read(r7, r4);	 Catch:{ Exception -> 0x0120 }
        r4 = r6.readBuffer;
        r4.limit(r3);
        r3 = r6.readBuffer;
        r3 = r3.position();
        r3 = r3 - r1;
        if (r3 == r2) goto L_0x0048;
    L_0x00f5:
        r3 = new com.esotericsoftware.kryonet.KryoNetException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Incorrect number of bytes (";
        r4 = r4.append(r5);
        r1 = r1 + r2;
        r2 = r6.readBuffer;
        r2 = r2.position();
        r1 = r1 - r2;
        r1 = r4.append(r1);
        r2 = " remaining) used to deserialize object: ";
        r1 = r1.append(r2);
        r0 = r1.append(r0);
        r0 = r0.toString();
        r3.m154init(r0);
        throw r3;
    L_0x0120:
        r0 = move-exception;
        r1 = new com.esotericsoftware.kryonet.KryoNetException;
        r2 = "Error during deserialization.";
        r1.m155init(r2, r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryonet.TcpConnection.readObject(com.esotericsoftware.kryonet.Connection):java.lang.Object");
    }

    public void writeOperation() throws IOException {
        synchronized (this.writeLock) {
            if (writeToSocket()) {
                this.selectionKey.interestOps(1);
            }
            this.lastWriteTime = System.currentTimeMillis();
        }
    }

    private boolean writeToSocket() throws IOException {
        SocketChannel socketChannel = this.socketChannel;
        if (socketChannel == null) {
            throw new SocketException("Connection is closed.");
        }
        ByteBuffer byteBuffer = this.writeBuffer;
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            if (this.bufferPositionFix) {
                byteBuffer.compact();
                byteBuffer.flip();
            }
            if (socketChannel.write(byteBuffer) == 0) {
                break;
            }
        }
        byteBuffer.compact();
        return byteBuffer.position() == 0;
    }

    public int send(Connection connection, Object obj) throws IOException {
        if (this.socketChannel == null) {
            throw new SocketException("Connection is closed.");
        }
        int position;
        synchronized (this.writeLock) {
            position = this.writeBuffer.position();
            int lengthLength = this.serialization.getLengthLength();
            this.writeBuffer.position(this.writeBuffer.position() + lengthLength);
            try {
                this.serialization.write(connection, this.writeBuffer, obj);
                int position2 = this.writeBuffer.position();
                this.writeBuffer.position(position);
                this.serialization.writeLength(this.writeBuffer, (position2 - lengthLength) - position);
                this.writeBuffer.position(position2);
                if (position != 0 || writeToSocket()) {
                    this.selectionKey.selector().wakeup();
                } else {
                    this.selectionKey.interestOps(5);
                }
                this.lastWriteTime = System.currentTimeMillis();
                position = position2 - position;
            } catch (KryoNetException e) {
                throw new KryoNetException("Error serializing object of type: " + obj.getClass().getName(), e);
            }
        }
        return position;
    }

    public void close() {
        try {
            if (this.socketChannel != null) {
                this.socketChannel.close();
                this.socketChannel = null;
                if (this.selectionKey != null) {
                    this.selectionKey.selector().wakeup();
                }
            }
        } catch (IOException e) {
        }
    }

    public boolean needsKeepAlive(long j) {
        return this.socketChannel != null && this.keepAliveMillis > 0 && j - this.lastWriteTime > ((long) this.keepAliveMillis);
    }

    public boolean isTimedOut(long j) {
        return this.socketChannel != null && this.timeoutMillis > 0 && j - this.lastReadTime > ((long) this.timeoutMillis);
    }
}
