package org.java_websocket.server;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketFactory;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WebSocketListener;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;

public abstract class WebSocketServer extends WebSocketAdapter implements Runnable {
    public static int DECODERS = Runtime.getRuntime().availableProcessors();
    private InetSocketAddress address;
    private BlockingQueue<ByteBuffer> buffers;
    private final Set<WebSocket> connections;
    private List<WebSocketWorker> decoders;
    private List<Draft> drafts;
    private List<WebSocketImpl> iqueue;
    private volatile AtomicBoolean isclosed;
    private BlockingQueue<WebSocketImpl> oqueue;
    private int queueinvokes;
    private AtomicInteger queuesize;
    private Selector selector;
    private Thread selectorthread;
    private ServerSocketChannel server;
    private WebSocketServerFactory wsf;

    public class WebSocketWorker extends Thread {
        static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketServer.class.desiredAssertionStatus());
        private BlockingQueue<WebSocketImpl> iqueue = new LinkedBlockingQueue();

        public WebSocketWorker() {
            setName("WebSocketWorker-" + getId());
            setUncaughtExceptionHandler(new UncaughtExceptionHandler(WebSocketServer.this) {
                public void uncaughtException(Thread t, Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, e);
                }
            });
        }

        public void put(WebSocketImpl ws) throws InterruptedException {
            this.iqueue.put(ws);
        }

        public void run() {
            WebSocketImpl ws = null;
            while (true) {
                try {
                    ws = (WebSocketImpl) this.iqueue.take();
                    ByteBuffer buf = (ByteBuffer) ws.inQueue.poll();
                    if ($assertionsDisabled || buf != null) {
                        try {
                            ws.decode(buf);
                        } catch (IOException e) {
                            WebSocketServer.this.handleIOException(ws, e);
                        } finally {
                            WebSocketServer.this.pushBuffer(buf);
                        }
                    } else {
                        throw new AssertionError();
                    }
                } catch (RuntimeException e2) {
                    WebSocketServer.this.handleFatal(ws, e2);
                    return;
                } catch (InterruptedException e3) {
                    return;
                } catch (Throwable e4) {
                    e4.printStackTrace();
                    return;
                }
            }
        }
    }

    public interface WebSocketServerFactory extends WebSocketFactory {
        WebSocketImpl createWebSocket(WebSocketAdapter webSocketAdapter, List<Draft> list, Socket socket);

        WebSocketImpl createWebSocket(WebSocketAdapter webSocketAdapter, Draft draft, Socket socket);

        ByteChannel wrapChannel(SelectionKey selectionKey) throws IOException;
    }

    public abstract void onClose(WebSocket webSocket, int i, String str, boolean z);

    public abstract void onError(WebSocket webSocket, Exception exception);

    public abstract void onMessage(WebSocket webSocket, String str);

    public abstract void onOpen(WebSocket webSocket, ClientHandshake clientHandshake);

    public WebSocketServer() throws UnknownHostException {
        this(new InetSocketAddress(80), DECODERS, null);
    }

    public WebSocketServer(InetSocketAddress address) {
        this(address, DECODERS, null);
    }

    public WebSocketServer(InetSocketAddress address, int decoders) {
        this(address, decoders, null);
    }

    public WebSocketServer(InetSocketAddress address, List<Draft> drafts) {
        this(address, DECODERS, drafts);
    }

    public WebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts) {
        this.connections = new HashSet();
        this.isclosed = new AtomicBoolean(false);
        this.queueinvokes = 0;
        this.queuesize = new AtomicInteger(0);
        this.wsf = new WebSocketServerFactory() {
            public WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d, Socket s) {
                return new WebSocketImpl((WebSocketListener) a, d, s);
            }

            public WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> d, Socket s) {
                return new WebSocketImpl((WebSocketListener) a, (List) d, s);
            }

            public SocketChannel wrapChannel(SelectionKey c) {
                return (SocketChannel) c.channel();
            }
        };
        if (drafts == null) {
            this.drafts = Collections.emptyList();
        } else {
            this.drafts = drafts;
        }
        setAddress(address);
        this.oqueue = new LinkedBlockingQueue();
        this.iqueue = new LinkedList();
        this.decoders = new ArrayList(decodercount);
        this.buffers = new LinkedBlockingQueue();
        for (int i = 0; i < decodercount; i++) {
            WebSocketWorker ex = new WebSocketWorker();
            this.decoders.add(ex);
            ex.start();
        }
    }

    public void start() {
        if (this.selectorthread != null) {
            throw new IllegalStateException(getClass().getName() + " can only be started once.");
        }
        new Thread(this).start();
    }

    public void stop(int timeout) throws IOException, InterruptedException {
        if (this.isclosed.compareAndSet(false, true)) {
            synchronized (this.connections) {
                for (WebSocket ws : this.connections) {
                    ws.close((int) CloseFrame.NORMAL);
                }
            }
            synchronized (this) {
                if (this.selectorthread != null) {
                    if (Thread.currentThread() != this.selectorthread) {
                    }
                    this.selectorthread.interrupt();
                    this.selectorthread.join();
                }
                if (this.decoders != null) {
                    for (WebSocketWorker w : this.decoders) {
                        w.interrupt();
                    }
                }
                if (this.server != null) {
                    this.server.close();
                }
            }
        }
    }

    public void stop() throws IOException, InterruptedException {
        stop(0);
    }

    public Set<WebSocket> connections() {
        return this.connections;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        int port = getAddress().getPort();
        if (port != 0 || this.server == null) {
            return port;
        }
        return this.server.socket().getLocalPort();
    }

    public List<Draft> getDraft() {
        return Collections.unmodifiableList(this.drafts);
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e9 A:{Splitter:B:21:0x00ad, ExcHandler: CancelledKeyException (e java.nio.channels.CancelledKeyException)} */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01a8 A:{Splitter:B:21:0x00ad, ExcHandler: InterruptedException (e java.lang.InterruptedException)} */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Missing block: B:14:0x0040, code skipped:
            r16.selectorthread.setName("WebsocketSelector" + r16.selectorthread.getId());
     */
    /* JADX WARNING: Missing block: B:16:?, code skipped:
            r16.server = java.nio.channels.ServerSocketChannel.open();
            r16.server.configureBlocking(false);
            r10 = r16.server.socket();
            r10.setReceiveBufferSize(org.java_websocket.WebSocket.RCVBUF);
            r10.bind(r16.address);
            r16.selector = java.nio.channels.Selector.open();
            r16.server.register(r16.selector, r16.server.validOps());
     */
    /* JADX WARNING: Missing block: B:19:0x00a9, code skipped:
            if (r16.selectorthread.isInterrupted() != false) goto L_0x003e;
     */
    /* JADX WARNING: Missing block: B:20:0x00ab, code skipped:
            r8 = null;
            r4 = null;
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            r16.selector.select();
            registerWrite();
            r7 = r16.selector.selectedKeys().iterator();
     */
    /* JADX WARNING: Missing block: B:24:0x00c7, code skipped:
            if (r7.hasNext() == false) goto L_0x01bf;
     */
    /* JADX WARNING: Missing block: B:25:0x00c9, code skipped:
            r8 = (java.nio.channels.SelectionKey) r7.next();
     */
    /* JADX WARNING: Missing block: B:26:0x00d5, code skipped:
            if (r8.isValid() == false) goto L_0x00c3;
     */
    /* JADX WARNING: Missing block: B:28:0x00db, code skipped:
            if (r8.isAcceptable() == false) goto L_0x0148;
     */
    /* JADX WARNING: Missing block: B:30:0x00e3, code skipped:
            if (onConnect(r8) != false) goto L_0x00f4;
     */
    /* JADX WARNING: Missing block: B:31:0x00e5, code skipped:
            r8.cancel();
     */
    /* JADX WARNING: Missing block: B:35:0x00eb, code skipped:
            r6 = move-exception;
     */
    /* JADX WARNING: Missing block: B:36:0x00ec, code skipped:
            onWebsocketError(null, r6);
     */
    /* JADX WARNING: Missing block: B:38:?, code skipped:
            r3 = r16.server.accept();
            r3.configureBlocking(false);
            r11 = r16.wsf.createWebSocket((org.java_websocket.WebSocketAdapter) r16, r16.drafts, r3.socket());
            r11.key = r3.register(r16.selector, 1, r11);
            r11.channel = r16.wsf.wrapChannel(r11.key);
            r7.remove();
            allocateBuffers(r11);
     */
    /* JADX WARNING: Missing block: B:40:0x0132, code skipped:
            r6 = move-exception;
     */
    /* JADX WARNING: Missing block: B:41:0x0133, code skipped:
            if (r8 != null) goto L_0x0135;
     */
    /* JADX WARNING: Missing block: B:43:?, code skipped:
            r8.cancel();
     */
    /* JADX WARNING: Missing block: B:44:0x0138, code skipped:
            handleIOException(r4, r6);
     */
    /* JADX WARNING: Missing block: B:46:0x013f, code skipped:
            r5 = move-exception;
     */
    /* JADX WARNING: Missing block: B:47:0x0140, code skipped:
            handleFatal(null, r5);
     */
    /* JADX WARNING: Missing block: B:50:0x014c, code skipped:
            if (r8.isReadable() == false) goto L_0x0186;
     */
    /* JADX WARNING: Missing block: B:51:0x014e, code skipped:
            r4 = (org.java_websocket.WebSocketImpl) r8.attachment();
            r1 = takeBuffer();
     */
    /* JADX WARNING: Missing block: B:54:0x0160, code skipped:
            if (org.java_websocket.SocketChannelIOHelper.read(r1, r4, r4.channel) == false) goto L_0x01ab;
     */
    /* JADX WARNING: Missing block: B:55:0x0162, code skipped:
            r4.inQueue.put(r1);
            queue(r4);
            r7.remove();
     */
    /* JADX WARNING: Missing block: B:56:0x0173, code skipped:
            if ((r4.channel instanceof org.java_websocket.WrappedByteChannel) == false) goto L_0x0186;
     */
    /* JADX WARNING: Missing block: B:58:0x017d, code skipped:
            if (((org.java_websocket.WrappedByteChannel) r4.channel).isNeedRead() == false) goto L_0x0186;
     */
    /* JADX WARNING: Missing block: B:59:0x017f, code skipped:
            r16.iqueue.add(r4);
     */
    /* JADX WARNING: Missing block: B:62:0x018a, code skipped:
            if (r8.isWritable() == false) goto L_0x00c3;
     */
    /* JADX WARNING: Missing block: B:63:0x018c, code skipped:
            r4 = (org.java_websocket.WebSocketImpl) r8.attachment();
     */
    /* JADX WARNING: Missing block: B:64:0x019a, code skipped:
            if (org.java_websocket.SocketChannelIOHelper.batch(r4, r4.channel) == false) goto L_0x00c3;
     */
    /* JADX WARNING: Missing block: B:66:0x01a0, code skipped:
            if (r8.isValid() == false) goto L_0x00c3;
     */
    /* JADX WARNING: Missing block: B:67:0x01a2, code skipped:
            r8.interestOps(1);
     */
    /* JADX WARNING: Missing block: B:71:?, code skipped:
            pushBuffer(r1);
     */
    /* JADX WARNING: Missing block: B:72:0x01b1, code skipped:
            r5 = move-exception;
     */
    /* JADX WARNING: Missing block: B:74:?, code skipped:
            pushBuffer(r1);
     */
    /* JADX WARNING: Missing block: B:75:0x01b7, code skipped:
            throw r5;
     */
    /* JADX WARNING: Missing block: B:76:0x01b8, code skipped:
            r5 = move-exception;
     */
    /* JADX WARNING: Missing block: B:77:0x01b9, code skipped:
            pushBuffer(r1);
     */
    /* JADX WARNING: Missing block: B:78:0x01be, code skipped:
            throw r5;
     */
    /* JADX WARNING: Missing block: B:80:0x01c7, code skipped:
            if (r16.iqueue.isEmpty() != false) goto L_0x00a1;
     */
    /* JADX WARNING: Missing block: B:81:0x01c9, code skipped:
            r4 = (org.java_websocket.WebSocketImpl) r16.iqueue.remove(0);
            r2 = r4.channel;
            r1 = takeBuffer();
     */
    /* JADX WARNING: Missing block: B:82:0x01e2, code skipped:
            if (org.java_websocket.SocketChannelIOHelper.readMore(r1, r4, r2) == false) goto L_0x01eb;
     */
    /* JADX WARNING: Missing block: B:83:0x01e4, code skipped:
            r16.iqueue.add(r4);
     */
    /* JADX WARNING: Missing block: B:84:0x01eb, code skipped:
            r4.inQueue.put(r1);
            queue(r4);
     */
    /* JADX WARNING: Missing block: B:101:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:102:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:103:?, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:104:?, code skipped:
            return;
     */
    public void run() {
        /*
        r16 = this;
        monitor-enter(r16);
        r0 = r16;
        r12 = r0.selectorthread;	 Catch:{ all -> 0x0028 }
        if (r12 == 0) goto L_0x002b;
    L_0x0007:
        r12 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0028 }
        r13 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0028 }
        r13.<init>();	 Catch:{ all -> 0x0028 }
        r14 = r16.getClass();	 Catch:{ all -> 0x0028 }
        r14 = r14.getName();	 Catch:{ all -> 0x0028 }
        r13 = r13.append(r14);	 Catch:{ all -> 0x0028 }
        r14 = " can only be started once.";
        r13 = r13.append(r14);	 Catch:{ all -> 0x0028 }
        r13 = r13.toString();	 Catch:{ all -> 0x0028 }
        r12.<init>(r13);	 Catch:{ all -> 0x0028 }
        throw r12;	 Catch:{ all -> 0x0028 }
    L_0x0028:
        r12 = move-exception;
        monitor-exit(r16);	 Catch:{ all -> 0x0028 }
        throw r12;
    L_0x002b:
        r12 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0028 }
        r0 = r16;
        r0.selectorthread = r12;	 Catch:{ all -> 0x0028 }
        r0 = r16;
        r12 = r0.isclosed;	 Catch:{ all -> 0x0028 }
        r12 = r12.get();	 Catch:{ all -> 0x0028 }
        if (r12 == 0) goto L_0x003f;
    L_0x003d:
        monitor-exit(r16);	 Catch:{ all -> 0x0028 }
    L_0x003e:
        return;
    L_0x003f:
        monitor-exit(r16);	 Catch:{ all -> 0x0028 }
        r0 = r16;
        r12 = r0.selectorthread;
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r14 = "WebsocketSelector";
        r13 = r13.append(r14);
        r0 = r16;
        r14 = r0.selectorthread;
        r14 = r14.getId();
        r13 = r13.append(r14);
        r13 = r13.toString();
        r12.setName(r13);
        r12 = java.nio.channels.ServerSocketChannel.open();	 Catch:{ IOException -> 0x00eb }
        r0 = r16;
        r0.server = r12;	 Catch:{ IOException -> 0x00eb }
        r0 = r16;
        r12 = r0.server;	 Catch:{ IOException -> 0x00eb }
        r13 = 0;
        r12.configureBlocking(r13);	 Catch:{ IOException -> 0x00eb }
        r0 = r16;
        r12 = r0.server;	 Catch:{ IOException -> 0x00eb }
        r10 = r12.socket();	 Catch:{ IOException -> 0x00eb }
        r12 = org.java_websocket.WebSocket.RCVBUF;	 Catch:{ IOException -> 0x00eb }
        r10.setReceiveBufferSize(r12);	 Catch:{ IOException -> 0x00eb }
        r0 = r16;
        r12 = r0.address;	 Catch:{ IOException -> 0x00eb }
        r10.bind(r12);	 Catch:{ IOException -> 0x00eb }
        r12 = java.nio.channels.Selector.open();	 Catch:{ IOException -> 0x00eb }
        r0 = r16;
        r0.selector = r12;	 Catch:{ IOException -> 0x00eb }
        r0 = r16;
        r12 = r0.server;	 Catch:{ IOException -> 0x00eb }
        r0 = r16;
        r13 = r0.selector;	 Catch:{ IOException -> 0x00eb }
        r0 = r16;
        r14 = r0.server;	 Catch:{ IOException -> 0x00eb }
        r14 = r14.validOps();	 Catch:{ IOException -> 0x00eb }
        r12.register(r13, r14);	 Catch:{ IOException -> 0x00eb }
    L_0x00a1:
        r0 = r16;
        r12 = r0.selectorthread;	 Catch:{ RuntimeException -> 0x013f }
        r12 = r12.isInterrupted();	 Catch:{ RuntimeException -> 0x013f }
        if (r12 != 0) goto L_0x003e;
    L_0x00ab:
        r8 = 0;
        r4 = 0;
        r0 = r16;
        r12 = r0.selector;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12.select();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r16.registerWrite();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r16;
        r12 = r0.selector;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r9 = r12.selectedKeys();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r7 = r9.iterator();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
    L_0x00c3:
        r12 = r7.hasNext();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x01bf;
    L_0x00c9:
        r12 = r7.next();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r12;
        r0 = (java.nio.channels.SelectionKey) r0;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r8 = r0;
        r12 = r8.isValid();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x00c3;
    L_0x00d7:
        r12 = r8.isAcceptable();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x0148;
    L_0x00dd:
        r0 = r16;
        r12 = r0.onConnect(r8);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 != 0) goto L_0x00f4;
    L_0x00e5:
        r8.cancel();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        goto L_0x00c3;
    L_0x00e9:
        r12 = move-exception;
        goto L_0x00a1;
    L_0x00eb:
        r6 = move-exception;
        r12 = 0;
        r0 = r16;
        r0.onWebsocketError(r12, r6);
        goto L_0x003e;
    L_0x00f4:
        r0 = r16;
        r12 = r0.server;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r3 = r12.accept();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12 = 0;
        r3.configureBlocking(r12);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r16;
        r12 = r0.wsf;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r16;
        r13 = r0.drafts;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r14 = r3.socket();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r16;
        r11 = r12.createWebSocket(r0, r13, r14);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r16;
        r12 = r0.selector;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r13 = 1;
        r12 = r3.register(r12, r13, r11);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r11.key = r12;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r16;
        r12 = r0.wsf;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r13 = r11.key;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12 = r12.wrapChannel(r13);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r11.channel = r12;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r7.remove();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r16;
        r0.allocateBuffers(r11);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        goto L_0x00c3;
    L_0x0132:
        r6 = move-exception;
        if (r8 == 0) goto L_0x0138;
    L_0x0135:
        r8.cancel();	 Catch:{ RuntimeException -> 0x013f }
    L_0x0138:
        r0 = r16;
        r0.handleIOException(r4, r6);	 Catch:{ RuntimeException -> 0x013f }
        goto L_0x00a1;
    L_0x013f:
        r5 = move-exception;
        r12 = 0;
        r0 = r16;
        r0.handleFatal(r12, r5);
        goto L_0x003e;
    L_0x0148:
        r12 = r8.isReadable();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x0186;
    L_0x014e:
        r12 = r8.attachment();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r12;
        r0 = (org.java_websocket.WebSocketImpl) r0;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r4 = r0;
        r1 = r16.takeBuffer();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12 = r4.channel;	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r12 = org.java_websocket.SocketChannelIOHelper.read(r1, r4, r12);	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x01ab;
    L_0x0162:
        r12 = r4.inQueue;	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r12.put(r1);	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r0 = r16;
        r0.queue(r4);	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r7.remove();	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r12 = r4.channel;	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r12 = r12 instanceof org.java_websocket.WrappedByteChannel;	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x0186;
    L_0x0175:
        r12 = r4.channel;	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r12 = (org.java_websocket.WrappedByteChannel) r12;	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r12 = r12.isNeedRead();	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x0186;
    L_0x017f:
        r0 = r16;
        r12 = r0.iqueue;	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        r12.add(r4);	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
    L_0x0186:
        r12 = r8.isWritable();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x00c3;
    L_0x018c:
        r12 = r8.attachment();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r12;
        r0 = (org.java_websocket.WebSocketImpl) r0;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r4 = r0;
        r12 = r4.channel;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12 = org.java_websocket.SocketChannelIOHelper.batch(r4, r12);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x00c3;
    L_0x019c:
        r12 = r8.isValid();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x00c3;
    L_0x01a2:
        r12 = 1;
        r8.interestOps(r12);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        goto L_0x00c3;
    L_0x01a8:
        r5 = move-exception;
        goto L_0x003e;
    L_0x01ab:
        r0 = r16;
        r0.pushBuffer(r1);	 Catch:{ IOException -> 0x01b1, RuntimeException -> 0x01b8, CancelledKeyException -> 0x00e9, InterruptedException -> 0x01a8 }
        goto L_0x0186;
    L_0x01b1:
        r5 = move-exception;
        r0 = r16;
        r0.pushBuffer(r1);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        throw r5;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
    L_0x01b8:
        r5 = move-exception;
        r0 = r16;
        r0.pushBuffer(r1);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        throw r5;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
    L_0x01bf:
        r0 = r16;
        r12 = r0.iqueue;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12 = r12.isEmpty();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 != 0) goto L_0x00a1;
    L_0x01c9:
        r0 = r16;
        r12 = r0.iqueue;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r13 = 0;
        r12 = r12.remove(r13);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r12;
        r0 = (org.java_websocket.WebSocketImpl) r0;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r4 = r0;
        r2 = r4.channel;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r2 = (org.java_websocket.WrappedByteChannel) r2;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r1 = r16.takeBuffer();	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12 = org.java_websocket.SocketChannelIOHelper.readMore(r1, r4, r2);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        if (r12 == 0) goto L_0x01eb;
    L_0x01e4:
        r0 = r16;
        r12 = r0.iqueue;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12.add(r4);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
    L_0x01eb:
        r12 = r4.inQueue;	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r12.put(r1);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        r0 = r16;
        r0.queue(r4);	 Catch:{ CancelledKeyException -> 0x00e9, IOException -> 0x0132, InterruptedException -> 0x01a8 }
        goto L_0x01bf;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.java_websocket.server.WebSocketServer.run():void");
    }

    /* access modifiers changed from: protected */
    public void allocateBuffers(WebSocket c) throws InterruptedException {
        if (this.queuesize.get() < (this.decoders.size() * 2) + 1) {
            this.queuesize.incrementAndGet();
            this.buffers.put(createBuffer());
        }
    }

    /* access modifiers changed from: protected */
    public void releaseBuffers(WebSocket c) throws InterruptedException {
    }

    public ByteBuffer createBuffer() {
        return ByteBuffer.allocate(WebSocket.RCVBUF);
    }

    private void queue(WebSocketImpl ws) throws InterruptedException {
        if (ws.workerThread == null) {
            ws.workerThread = (WebSocketWorker) this.decoders.get(this.queueinvokes % this.decoders.size());
            this.queueinvokes++;
        }
        ws.workerThread.put(ws);
    }

    private ByteBuffer takeBuffer() throws InterruptedException {
        return (ByteBuffer) this.buffers.take();
    }

    /* access modifiers changed from: private */
    public void pushBuffer(ByteBuffer buf) throws InterruptedException {
        if (this.buffers.size() <= this.queuesize.intValue()) {
            this.buffers.put(buf);
        }
    }

    private void registerWrite() throws CancelledKeyException {
        int size = this.oqueue.size();
        for (int i = 0; i < size; i++) {
            ((WebSocketImpl) this.oqueue.remove()).key.interestOps(5);
        }
    }

    /* access modifiers changed from: private */
    public void handleIOException(WebSocket conn, IOException ex) {
        onWebsocketError(conn, ex);
        if (conn != null) {
            conn.close((int) CloseFrame.ABNORMAL_CLOSE);
        }
    }

    /* access modifiers changed from: private */
    public void handleFatal(WebSocket conn, RuntimeException e) {
        onError(conn, e);
        try {
            stop();
        } catch (IOException e1) {
            onError(null, e1);
        } catch (InterruptedException e12) {
            Thread.currentThread().interrupt();
            onError(null, e12);
        }
    }

    /* access modifiers changed from: protected */
    public String getFlashSecurityPolicy() {
        return "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + getPort() + "\" /></cross-domain-policy>";
    }

    public final void onWebsocketMessage(WebSocket conn, String message) {
        onMessage(conn, message);
    }

    public final void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
        onMessage(conn, blob);
    }

    public final void onWebsocketOpen(WebSocket conn, Handshakedata handshake) {
        synchronized (this.connections) {
            if (this.connections.add(conn)) {
                onOpen(conn, (ClientHandshake) handshake);
            }
        }
    }

    public final void onWebsocketClose(WebSocket conn, int code, String reason, boolean remote) {
        this.oqueue.add((WebSocketImpl) conn);
        this.selector.wakeup();
        try {
            synchronized (this.connections) {
                if (this.connections.remove(conn)) {
                    onClose(conn, code, reason, remote);
                }
            }
        } finally {
            try {
                releaseBuffers(conn);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public final void onWebsocketError(WebSocket conn, Exception ex) {
        onError(conn, ex);
    }

    public final void onWriteDemand(WebSocket w) {
        this.oqueue.add((WebSocketImpl) w);
        this.selector.wakeup();
    }

    public final void setWebSocketFactory(WebSocketServerFactory wsf) {
        this.wsf = wsf;
    }

    public final WebSocketFactory getWebSocketFactory() {
        return this.wsf;
    }

    /* access modifiers changed from: protected */
    public boolean onConnect(SelectionKey key) {
        return true;
    }

    public void onMessage(WebSocket conn, ByteBuffer message) {
    }
}
