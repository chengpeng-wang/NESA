package com.esotericsoftware.kryonet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

class UdpConnection {
    InetSocketAddress connectedAddress;
    DatagramChannel datagramChannel;
    int keepAliveMillis = 19000;
    private long lastCommunicationTime;
    final ByteBuffer readBuffer;
    private SelectionKey selectionKey;
    private final Serialization serialization;
    final ByteBuffer writeBuffer;
    private final Object writeLock = new Object();

    public UdpConnection(Serialization serialization, int i) {
        this.serialization = serialization;
        this.readBuffer = ByteBuffer.allocate(i);
        this.writeBuffer = ByteBuffer.allocateDirect(i);
    }

    public void bind(Selector selector, InetSocketAddress inetSocketAddress) throws IOException {
        close();
        this.readBuffer.clear();
        this.writeBuffer.clear();
        try {
            this.datagramChannel = selector.provider().openDatagramChannel();
            this.datagramChannel.socket().bind(inetSocketAddress);
            this.datagramChannel.configureBlocking(false);
            this.selectionKey = this.datagramChannel.register(selector, 1);
            this.lastCommunicationTime = System.currentTimeMillis();
        } catch (IOException e) {
            close();
            throw e;
        }
    }

    public void connect(Selector selector, InetSocketAddress inetSocketAddress) throws IOException {
        close();
        this.readBuffer.clear();
        this.writeBuffer.clear();
        try {
            this.datagramChannel = selector.provider().openDatagramChannel();
            this.datagramChannel.socket().bind(null);
            this.datagramChannel.socket().connect(inetSocketAddress);
            this.datagramChannel.configureBlocking(false);
            this.selectionKey = this.datagramChannel.register(selector, 1);
            this.lastCommunicationTime = System.currentTimeMillis();
            this.connectedAddress = inetSocketAddress;
        } catch (IOException e) {
            close();
            IOException iOException = new IOException("Unable to connect to: " + inetSocketAddress);
            iOException.initCause(e);
            throw iOException;
        }
    }

    public InetSocketAddress readFromAddress() throws IOException {
        DatagramChannel datagramChannel = this.datagramChannel;
        if (datagramChannel == null) {
            throw new SocketException("Connection is closed.");
        }
        this.lastCommunicationTime = System.currentTimeMillis();
        return (InetSocketAddress) datagramChannel.receive(this.readBuffer);
    }

    public Object readObject(Connection connection) {
        this.readBuffer.flip();
        try {
            Object read = this.serialization.read(connection, this.readBuffer);
            if (this.readBuffer.hasRemaining()) {
                throw new KryoNetException("Incorrect number of bytes (" + this.readBuffer.remaining() + " remaining) used to deserialize object: " + read);
            }
            this.readBuffer.clear();
            return read;
        } catch (Exception e) {
            throw new KryoNetException("Error during deserialization.", e);
        } catch (Throwable th) {
            this.readBuffer.clear();
        }
    }

    public int send(Connection connection, Object obj, SocketAddress socketAddress) throws IOException {
        DatagramChannel datagramChannel = this.datagramChannel;
        if (datagramChannel == null) {
            throw new SocketException("Connection is closed.");
        }
        int limit;
        synchronized (this.writeLock) {
            try {
                this.serialization.write(connection, this.writeBuffer, obj);
                this.writeBuffer.flip();
                limit = this.writeBuffer.limit();
                datagramChannel.send(this.writeBuffer, socketAddress);
                this.lastCommunicationTime = System.currentTimeMillis();
                if ((!this.writeBuffer.hasRemaining() ? 1 : null) == null) {
                    limit = -1;
                }
                this.writeBuffer.clear();
            } catch (Exception e) {
                throw new KryoNetException("Error serializing object of type: " + obj.getClass().getName(), e);
            } catch (Throwable th) {
                this.writeBuffer.clear();
            }
        }
        return limit;
    }

    public void close() {
        this.connectedAddress = null;
        try {
            if (this.datagramChannel != null) {
                this.datagramChannel.close();
                this.datagramChannel = null;
                if (this.selectionKey != null) {
                    this.selectionKey.selector().wakeup();
                }
            }
        } catch (IOException e) {
        }
    }

    public boolean needsKeepAlive(long j) {
        return this.connectedAddress != null && this.keepAliveMillis > 0 && j - this.lastCommunicationTime > ((long) this.keepAliveMillis);
    }
}
