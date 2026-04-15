package org.java_websocket.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import org.java_websocket.SocketChannelIOHelper;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketFactory;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.WrappedByteChannel;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.HandshakeImpl1Client;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;

public abstract class WebSocketClient extends WebSocketAdapter implements Runnable {
    static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketClient.class.desiredAssertionStatus());
    private SocketChannel channel;
    private CountDownLatch closeLatch;
    private WebSocketImpl conn;
    private CountDownLatch connectLatch;
    private Draft draft;
    private Map<String, String> headers;
    private SelectionKey key;
    private Selector selector;
    private Thread thread;
    private URI uri;
    WebSocketClientFactory wf;
    private ByteChannel wrappedchannel;

    public interface WebSocketClientFactory extends WebSocketFactory {
        ByteChannel wrapChannel(SelectionKey selectionKey, String str, int i) throws IOException;
    }

    public abstract void onClose(int i, String str, boolean z);

    public abstract void onError(Exception exception);

    public abstract void onMessage(String str);

    public abstract void onOpen(ServerHandshake serverHandshake);

    public WebSocketClient(URI serverURI) {
        this(serverURI, new Draft_10());
    }

    public WebSocketClient(URI serverUri, Draft draft) {
        this(serverUri, draft, null);
    }

    public WebSocketClient(URI serverUri, Draft draft, Map<String, String> headers) {
        this.uri = null;
        this.conn = null;
        this.channel = null;
        this.wrappedchannel = null;
        this.key = null;
        this.selector = null;
        this.connectLatch = new CountDownLatch(1);
        this.closeLatch = new CountDownLatch(1);
        this.wf = new WebSocketClientFactory() {
            public WebSocket createWebSocket(WebSocketAdapter a, Draft d, Socket s) {
                return new WebSocketImpl(WebSocketClient.this, d, s);
            }

            public WebSocket createWebSocket(WebSocketAdapter a, List<Draft> d, Socket s) {
                return new WebSocketImpl(WebSocketClient.this, (List) d, s);
            }

            public ByteChannel wrapChannel(SelectionKey c, String host, int port) {
                return (ByteChannel) c.channel();
            }
        };
        if (serverUri == null) {
            throw new IllegalArgumentException();
        } else if (draft == null) {
            throw new IllegalArgumentException("null as draft is permitted for `WebSocketServer` only!");
        } else {
            this.uri = serverUri;
            this.draft = draft;
            this.headers = headers;
        }
    }

    public URI getURI() {
        return this.uri;
    }

    public Draft getDraft() {
        return this.draft;
    }

    public void connect() {
        if (this.thread != null) {
            throw new IllegalStateException("WebSocketClient objects are not reuseable");
        }
        this.thread = new Thread(this);
        this.thread.start();
    }

    public boolean connectBlocking() throws InterruptedException {
        connect();
        this.connectLatch.await();
        return this.conn.isOpen();
    }

    public void close() {
        if (this.thread != null && this.conn != null) {
            this.conn.close((int) CloseFrame.NORMAL);
        }
    }

    public void closeBlocking() throws InterruptedException {
        close();
        this.closeLatch.await();
    }

    public void send(String text) throws NotYetConnectedException {
        if (this.conn != null) {
            this.conn.send(text);
        }
    }

    public void send(byte[] data) throws NotYetConnectedException {
        if (this.conn != null) {
            this.conn.send(data);
        }
    }

    private void tryToConnect(InetSocketAddress remote) throws IOException {
        this.channel = SocketChannel.open();
        this.channel.configureBlocking(false);
        this.channel.connect(remote);
        this.selector = Selector.open();
        this.key = this.channel.register(this.selector, 8);
    }

    public void run() {
        if (this.thread == null) {
            this.thread = Thread.currentThread();
        }
        interruptableRun();
        if ($assertionsDisabled || !this.channel.isOpen()) {
            try {
                if (this.selector != null) {
                    this.selector.close();
                    return;
                }
                return;
            } catch (IOException e) {
                onError(e);
                return;
            }
        }
        throw new AssertionError();
    }

    private final void interruptableRun() {
        try {
            tryToConnect(new InetSocketAddress(this.uri.getHost(), getPort()));
            this.conn = (WebSocketImpl) this.wf.createWebSocket((WebSocketAdapter) this, this.draft, this.channel.socket());
            ByteBuffer buff = ByteBuffer.allocate(WebSocket.RCVBUF);
            while (this.channel.isOpen()) {
                try {
                    this.selector.select();
                    Iterator<SelectionKey> i = this.selector.selectedKeys().iterator();
                    while (i.hasNext()) {
                        SelectionKey key = (SelectionKey) i.next();
                        i.remove();
                        if (key.isValid()) {
                            if (key.isReadable() && SocketChannelIOHelper.read(buff, this.conn, this.wrappedchannel)) {
                                this.conn.decode(buff);
                            }
                            if (key.isConnectable()) {
                                try {
                                    finishConnect(key);
                                } catch (InvalidHandshakeException e) {
                                    this.conn.close(e);
                                }
                            }
                            if (key.isWritable()) {
                                if (!SocketChannelIOHelper.batch(this.conn, this.wrappedchannel)) {
                                    key.interestOps(5);
                                } else if (key.isValid()) {
                                    key.interestOps(1);
                                }
                            }
                        } else {
                            this.conn.eot();
                        }
                    }
                    if (this.wrappedchannel instanceof WrappedByteChannel) {
                        WrappedByteChannel w = this.wrappedchannel;
                        if (w.isNeedRead()) {
                            while (SocketChannelIOHelper.read(buff, this.conn, w)) {
                                this.conn.decode(buff);
                            }
                        }
                    }
                } catch (CancelledKeyException e2) {
                    this.conn.eot();
                    return;
                } catch (IOException e3) {
                    this.conn.eot();
                    return;
                } catch (RuntimeException e4) {
                    onError(e4);
                    this.conn.close((int) CloseFrame.ABNORMAL_CLOSE);
                    return;
                }
            }
        } catch (ClosedByInterruptException e5) {
            onWebsocketError(null, e5);
        } catch (IOException e6) {
            onWebsocketError(this.conn, e6);
        } catch (SecurityException e7) {
            onWebsocketError(this.conn, e7);
        } catch (UnresolvedAddressException e8) {
            onWebsocketError(this.conn, e8);
        }
    }

    private int getPort() {
        int port = this.uri.getPort();
        if (port != -1) {
            return port;
        }
        String scheme = this.uri.getScheme();
        if (scheme.equals("wss")) {
            return WebSocket.DEFAULT_WSS_PORT;
        }
        if (scheme.equals("ws")) {
            return 80;
        }
        throw new RuntimeException("unkonow scheme" + scheme);
    }

    private void finishConnect(SelectionKey key) throws IOException, InvalidHandshakeException {
        if (this.channel.isConnectionPending()) {
            this.channel.finishConnect();
        }
        this.conn.key = key.interestOps(5);
        WebSocketImpl webSocketImpl = this.conn;
        ByteChannel wrapChannel = this.wf.wrapChannel(key, this.uri.getHost(), getPort());
        this.wrappedchannel = wrapChannel;
        webSocketImpl.channel = wrapChannel;
        sendHandshake();
    }

    private void sendHandshake() throws InvalidHandshakeException {
        String path;
        String part1 = this.uri.getPath();
        String part2 = this.uri.getQuery();
        if (part1 == null || part1.length() == 0) {
            path = "/";
        } else {
            path = part1;
        }
        if (part2 != null) {
            path = path + "?" + part2;
        }
        int port = getPort();
        String host = this.uri.getHost() + (port != 80 ? ":" + port : "");
        HandshakeImpl1Client handshake = new HandshakeImpl1Client();
        handshake.setResourceDescriptor(path);
        handshake.put("Host", host);
        if (this.headers != null) {
            for (Entry<String, String> kv : this.headers.entrySet()) {
                handshake.put((String) kv.getKey(), (String) kv.getValue());
            }
        }
        this.conn.startHandshake(handshake);
    }

    public int getReadyState() {
        if (this.conn == null) {
            return 0;
        }
        return this.conn.getReadyState();
    }

    public final void onWebsocketMessage(WebSocket conn, String message) {
        onMessage(message);
    }

    public final void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
        onMessage(blob);
    }

    public final void onWebsocketOpen(WebSocket conn, Handshakedata handshake) {
        this.connectLatch.countDown();
        onOpen((ServerHandshake) handshake);
    }

    public final void onWebsocketClose(WebSocket conn, int code, String reason, boolean remote) {
        this.connectLatch.countDown();
        this.closeLatch.countDown();
        onClose(code, reason, remote);
    }

    public final void onWebsocketError(WebSocket conn, Exception ex) {
        onError(ex);
    }

    public final void onWriteDemand(WebSocket conn) {
        try {
            this.key.interestOps(5);
            this.selector.wakeup();
        } catch (CancelledKeyException e) {
        }
    }

    public WebSocket getConnection() {
        return this.conn;
    }

    public final void setWebSocketFactory(WebSocketClientFactory wsf) {
        this.wf = wsf;
    }

    public final WebSocketFactory getWebSocketFactory() {
        return this.wf;
    }

    public void onMessage(ByteBuffer bytes) {
    }
}
