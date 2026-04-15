package org.java_websocket;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public class SSLSocketChannel2 implements ByteChannel, WrappedByteChannel {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected static ByteBuffer emptybuffer = ByteBuffer.allocate(0);
    protected ExecutorService exec;
    protected ByteBuffer inCrypt;
    protected ByteBuffer inData;
    protected SelectionKey key;
    protected ByteBuffer outCrypt;
    protected SSLEngineResult res;
    protected SocketChannel sc;
    protected SSLEngine sslEngine;
    protected List<Future<?>> tasks = new ArrayList(3);

    static {
        boolean z;
        if (SSLSocketChannel2.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public SSLSocketChannel2(SelectionKey key, SSLEngine sslEngine, ExecutorService exec) throws IOException {
        this.sc = (SocketChannel) key.channel();
        this.key = key;
        this.sslEngine = sslEngine;
        this.exec = exec;
        this.key.interestOps(key.interestOps() | 4);
        sslEngine.setEnableSessionCreation(true);
        createBuffers(sslEngine.getSession());
        this.sc.write(wrap(emptybuffer));
        processHandshake();
    }

    private void processHandshake() throws IOException {
        if (!this.tasks.isEmpty()) {
            Iterator<Future<?>> it = this.tasks.iterator();
            while (it.hasNext()) {
                if (((Future) it.next()).isDone()) {
                    it.remove();
                } else {
                    return;
                }
            }
        }
        if (this.res.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP) {
            this.inCrypt.compact();
            if (this.sc.read(this.inCrypt) == -1) {
                throw new IOException("connection closed unexpectedly by peer");
            }
            this.inCrypt.flip();
            this.inData.compact();
            unwrap();
        }
        consumeDelegatedTasks();
        if (this.tasks.isEmpty() || this.res.getHandshakeStatus() == HandshakeStatus.NEED_WRAP) {
            this.sc.write(wrap(emptybuffer));
        }
    }

    private synchronized ByteBuffer wrap(ByteBuffer b) throws SSLException {
        this.outCrypt.compact();
        this.res = this.sslEngine.wrap(b, this.outCrypt);
        this.outCrypt.flip();
        return this.outCrypt;
    }

    private synchronized ByteBuffer unwrap() throws SSLException {
        int rem;
        do {
            rem = this.inData.remaining();
            this.res = this.sslEngine.unwrap(this.inCrypt, this.inData);
        } while (rem != this.inData.remaining());
        this.inData.flip();
        return this.inData;
    }

    /* access modifiers changed from: protected */
    public void consumeDelegatedTasks() {
        while (true) {
            Runnable task = this.sslEngine.getDelegatedTask();
            if (task != null) {
                this.tasks.add(this.exec.submit(task));
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void createBuffers(SSLSession session) {
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();
        this.inData = ByteBuffer.allocate(appBufferMax);
        this.outCrypt = ByteBuffer.allocate(netBufferMax);
        this.inCrypt = ByteBuffer.allocate(netBufferMax);
        this.inData.flip();
        this.inCrypt.flip();
        this.outCrypt.flip();
    }

    public int write(ByteBuffer src) throws IOException {
        if (isHandShakeComplete()) {
            return this.sc.write(wrap(src));
        }
        processHandshake();
        return 0;
    }

    public int read(ByteBuffer dst) throws IOException {
        if (isHandShakeComplete()) {
            int purged = readRemaining(dst);
            if (purged != 0) {
                return purged;
            }
            if ($assertionsDisabled || this.inData.position() == 0) {
                this.inData.clear();
                if (this.inCrypt.hasRemaining()) {
                    this.inCrypt.compact();
                } else {
                    this.inCrypt.clear();
                }
                if (this.sc.read(this.inCrypt) == -1) {
                    return -1;
                }
                this.inCrypt.flip();
                unwrap();
                return transfereTo(this.inData, dst);
            }
            throw new AssertionError();
        }
        processHandshake();
        return 0;
    }

    private int readRemaining(ByteBuffer dst) throws SSLException {
        if (!$assertionsDisabled && !dst.hasRemaining()) {
            throw new AssertionError();
        } else if (this.inData.hasRemaining()) {
            return transfereTo(this.inData, dst);
        } else {
            if ($assertionsDisabled || !this.inData.hasRemaining()) {
                this.inData.clear();
                if (this.inCrypt.hasRemaining()) {
                    unwrap();
                    int amount = transfereTo(this.inData, dst);
                    if (amount > 0) {
                        return amount;
                    }
                }
                return 0;
            }
            throw new AssertionError();
        }
    }

    public boolean isConnected() {
        return this.sc.isConnected();
    }

    public void close() throws IOException {
        this.sslEngine.closeOutbound();
        this.sslEngine.getSession().invalidate();
        if (this.sc.isOpen()) {
            this.sc.write(wrap(emptybuffer));
        }
        this.sc.close();
    }

    private boolean isHandShakeComplete() {
        HandshakeStatus status = this.res.getHandshakeStatus();
        return status == HandshakeStatus.FINISHED || status == HandshakeStatus.NOT_HANDSHAKING;
    }

    public SelectableChannel configureBlocking(boolean b) throws IOException {
        return this.sc.configureBlocking(b);
    }

    public boolean connect(SocketAddress remote) throws IOException {
        return this.sc.connect(remote);
    }

    public boolean finishConnect() throws IOException {
        return this.sc.finishConnect();
    }

    public Socket socket() {
        return this.sc.socket();
    }

    public boolean isInboundDone() {
        return this.sslEngine.isInboundDone();
    }

    public boolean isOpen() {
        return this.sc.isOpen();
    }

    public boolean isNeedWrite() {
        return this.outCrypt.hasRemaining() || !isHandShakeComplete();
    }

    public void writeMore() throws IOException {
        write(this.outCrypt);
    }

    public boolean isNeedRead() {
        return this.inData.hasRemaining() || (this.inCrypt.hasRemaining() && this.res.getStatus() != Status.BUFFER_UNDERFLOW);
    }

    public int readMore(ByteBuffer dst) throws SSLException {
        return readRemaining(dst);
    }

    private int transfereTo(ByteBuffer from, ByteBuffer to) {
        int fremain = from.remaining();
        int toremain = to.remaining();
        if (fremain > toremain) {
            int min = Math.min(fremain, toremain);
            for (int i = 0; i < min; i++) {
                to.put(from.get());
            }
            return min;
        }
        to.put(from);
        return fremain;
    }
}
