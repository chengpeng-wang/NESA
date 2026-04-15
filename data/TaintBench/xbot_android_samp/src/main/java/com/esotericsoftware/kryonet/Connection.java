package com.esotericsoftware.kryonet;

import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class Connection {
    EndPoint endPoint;
    int id = -1;
    volatile boolean isConnected;
    private int lastPingID;
    private long lastPingSendTime;
    private Object listenerLock = new Object();
    private Listener[] listeners = new Listener[0];
    private String name;
    private int returnTripTime;
    TcpConnection tcp;
    UdpConnection udp;
    InetSocketAddress udpRemoteAddress;

    protected Connection() {
    }

    /* access modifiers changed from: 0000 */
    public void initialize(Serialization serialization, int i, int i2) {
        this.tcp = new TcpConnection(serialization, i, i2);
    }

    public int getID() {
        return this.id;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public int sendTCP(Object obj) {
        int i = 0;
        if (obj == null) {
            throw new IllegalArgumentException("object cannot be null.");
        }
        try {
            i = this.tcp.send(this, obj);
            if (i == 0) {
            }
        } catch (IOException e) {
            close();
        } catch (KryoNetException e2) {
            close();
        }
        return i;
    }

    public int sendUDP(Object obj) {
        int i = 0;
        if (obj == null) {
            throw new IllegalArgumentException("object cannot be null.");
        }
        SocketAddress socketAddress = this.udpRemoteAddress;
        if (socketAddress == null && this.udp != null) {
            socketAddress = this.udp.connectedAddress;
        }
        if (socketAddress == null && this.isConnected) {
            throw new IllegalStateException("Connection is not connected via UDP.");
        } else if (socketAddress == null) {
            try {
                throw new SocketException("Connection is closed.");
            } catch (IOException e) {
                close();
            } catch (KryoNetException e2) {
                close();
            }
        } else {
            i = this.udp.send(this, obj, socketAddress);
            if (i == 0) {
            }
            return i;
        }
    }

    public void close() {
        boolean z = this.isConnected;
        this.isConnected = false;
        this.tcp.close();
        if (!(this.udp == null || this.udp.connectedAddress == null)) {
            this.udp.close();
        }
        if (z) {
            notifyDisconnected();
        }
        setConnected(false);
    }

    public void updateReturnTripTime() {
        Ping ping = new Ping();
        int i = this.lastPingID;
        this.lastPingID = i + 1;
        ping.id = i;
        this.lastPingSendTime = System.currentTimeMillis();
        sendTCP(ping);
    }

    public int getReturnTripTime() {
        return this.returnTripTime;
    }

    public void setKeepAliveTCP(int i) {
        this.tcp.keepAliveMillis = i;
    }

    public void setTimeout(int i) {
        this.tcp.timeoutMillis = i;
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
            if (r5 == 0) {
                return;
            }
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

    /* access modifiers changed from: 0000 */
    public void notifyConnected() {
        for (Listener connected : this.listeners) {
            connected.connected(this);
        }
    }

    /* access modifiers changed from: 0000 */
    public void notifyDisconnected() {
        for (Listener disconnected : this.listeners) {
            disconnected.disconnected(this);
        }
    }

    /* access modifiers changed from: 0000 */
    public void notifyIdle() {
        Listener[] listenerArr = this.listeners;
        int i = 0;
        int length = listenerArr.length;
        while (i < length) {
            listenerArr[i].idle(this);
            if (isIdle()) {
                i++;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void notifyReceived(Object obj) {
        if (obj instanceof Ping) {
            Ping ping = (Ping) obj;
            if (!ping.isReply) {
                ping.isReply = true;
                sendTCP(ping);
            } else if (ping.id == this.lastPingID - 1) {
                this.returnTripTime = (int) (System.currentTimeMillis() - this.lastPingSendTime);
            }
        }
        for (Listener received : this.listeners) {
            received.received(this, obj);
        }
    }

    public EndPoint getEndPoint() {
        return this.endPoint;
    }

    public InetSocketAddress getRemoteAddressTCP() {
        if (this.tcp.socketChannel != null) {
            Socket socket = this.tcp.socketChannel.socket();
            if (socket != null) {
                return (InetSocketAddress) socket.getRemoteSocketAddress();
            }
        }
        return null;
    }

    public InetSocketAddress getRemoteAddressUDP() {
        InetSocketAddress inetSocketAddress = this.udp.connectedAddress;
        return inetSocketAddress != null ? inetSocketAddress : this.udpRemoteAddress;
    }

    public void setBufferPositionFix(boolean z) {
        this.tcp.bufferPositionFix = z;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getTcpWriteBufferSize() {
        return this.tcp.writeBuffer.position();
    }

    public boolean isIdle() {
        return ((float) this.tcp.writeBuffer.position()) / ((float) this.tcp.writeBuffer.capacity()) < this.tcp.idleThreshold;
    }

    public void setIdleThreshold(float f) {
        this.tcp.idleThreshold = f;
    }

    public String toString() {
        if (this.name != null) {
            return this.name;
        }
        return "Connection " + this.id;
    }

    /* access modifiers changed from: 0000 */
    public void setConnected(boolean z) {
        this.isConnected = z;
        if (z && this.name == null) {
            this.name = "Connection " + this.id;
        }
    }
}
