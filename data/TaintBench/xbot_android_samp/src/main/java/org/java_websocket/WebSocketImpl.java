package org.java_websocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.java_websocket.WebSocket.Role;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft.CloseHandshakeType;
import org.java_websocket.drafts.Draft.HandshakeState;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.drafts.Draft_75;
import org.java_websocket.drafts.Draft_76;
import org.java_websocket.exceptions.IncompleteHandshakeException;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.framing.CloseFrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.Framedata.Opcode;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer.WebSocketWorker;
import org.java_websocket.util.Charsetfunctions;

public class WebSocketImpl extends WebSocket {
    static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketImpl.class.desiredAssertionStatus());
    public ByteChannel channel;
    private volatile boolean closeHandshakeSent;
    private volatile boolean connectionClosed;
    private Draft draft;
    private volatile boolean handshakeComplete;
    private ClientHandshake handshakerequest;
    public final BlockingQueue<ByteBuffer> inQueue;
    public SelectionKey key;
    private List<Draft> knownDrafts;
    public final BlockingQueue<ByteBuffer> outQueue;
    private Role role;
    public final Socket socket;
    private Framedata tempContiniousFrame;
    private ByteBuffer tmpHandshakeBytes;
    public volatile WebSocketWorker workerThread;
    private final WebSocketListener wsl;

    public WebSocketImpl(WebSocketListener listener, List<Draft> drafts, Socket sock) {
        this(listener, (Draft) null, sock);
        this.role = Role.SERVER;
        if (this.knownDrafts == null || drafts.isEmpty()) {
            this.knownDrafts = new ArrayList(1);
            this.knownDrafts.add(new Draft_17());
            this.knownDrafts.add(new Draft_10());
            this.knownDrafts.add(new Draft_76());
            this.knownDrafts.add(new Draft_75());
            return;
        }
        this.knownDrafts = drafts;
    }

    public WebSocketImpl(WebSocketListener listener, Draft draft, Socket sock) {
        this.handshakeComplete = false;
        this.closeHandshakeSent = false;
        this.connectionClosed = false;
        this.draft = null;
        this.handshakerequest = null;
        this.outQueue = new LinkedBlockingQueue();
        this.inQueue = new LinkedBlockingQueue();
        this.wsl = listener;
        this.role = Role.CLIENT;
        this.draft = draft;
        this.socket = sock;
    }

    public void decode(ByteBuffer socketBuffer) throws IOException {
        if (socketBuffer.hasRemaining() && !this.connectionClosed) {
            if (DEBUG) {
                System.out.println("process(" + socketBuffer.remaining() + "): {" + (socketBuffer.remaining() > CloseFrame.NORMAL ? "too big to display" : new String(socketBuffer.array(), socketBuffer.position(), socketBuffer.remaining())) + "}");
            }
            if (this.handshakeComplete) {
                decodeFrames(socketBuffer);
            } else if (decodeHandshake(socketBuffer)) {
                decodeFrames(socketBuffer);
            }
            if (!$assertionsDisabled && !isClosing() && !isClosed() && socketBuffer.hasRemaining()) {
                throw new AssertionError();
            }
        }
    }

    private boolean decodeHandshake(ByteBuffer socketBufferNew) throws IOException {
        ByteBuffer socketBuffer;
        if (this.tmpHandshakeBytes == null) {
            socketBuffer = socketBufferNew;
        } else {
            if (this.tmpHandshakeBytes.remaining() < socketBufferNew.remaining()) {
                ByteBuffer buf = ByteBuffer.allocate(this.tmpHandshakeBytes.capacity() + socketBufferNew.remaining());
                this.tmpHandshakeBytes.flip();
                buf.put(this.tmpHandshakeBytes);
                this.tmpHandshakeBytes = buf;
            }
            this.tmpHandshakeBytes.put(socketBufferNew);
            this.tmpHandshakeBytes.flip();
            socketBuffer = this.tmpHandshakeBytes;
        }
        socketBuffer.mark();
        try {
            if (this.draft == null && isFlashEdgeCase(socketBuffer) == HandshakeState.MATCHED) {
                write(ByteBuffer.wrap(Charsetfunctions.utf8Bytes(this.wsl.getFlashPolicy(this))));
                close(-3, "");
                return false;
            }
            try {
                Handshakedata tmphandshake;
                ClientHandshake handshake;
                if (this.role != Role.SERVER) {
                    if (this.role == Role.CLIENT) {
                        this.draft.setParseMode(this.role);
                        tmphandshake = this.draft.translateHandshake(socketBuffer);
                        if (tmphandshake instanceof ServerHandshake) {
                            ServerHandshake handshake2 = (ServerHandshake) tmphandshake;
                            if (this.draft.acceptHandshakeAsClient(this.handshakerequest, handshake2) == HandshakeState.MATCHED) {
                                try {
                                    this.wsl.onWebsocketHandshakeReceivedAsClient(this, this.handshakerequest, handshake2);
                                    open(handshake2);
                                    return true;
                                } catch (InvalidDataException e) {
                                    closeConnection(e.getCloseCode(), e.getMessage(), false);
                                    return false;
                                }
                            }
                            close(CloseFrame.PROTOCOL_ERROR, "draft " + this.draft + " refuses handshake");
                        } else {
                            closeConnection(CloseFrame.PROTOCOL_ERROR, "Wwrong http function", false);
                            return false;
                        }
                    }
                    return false;
                } else if (this.draft == null) {
                    for (Draft d : this.knownDrafts) {
                        try {
                            d.setParseMode(this.role);
                            socketBuffer.reset();
                            tmphandshake = d.translateHandshake(socketBuffer);
                            if (tmphandshake instanceof ClientHandshake) {
                                handshake = (ClientHandshake) tmphandshake;
                                if (d.acceptHandshakeAsServer(handshake) == HandshakeState.MATCHED) {
                                    try {
                                        write(d.createHandshake(d.postProcessHandshakeResponseAsServer(handshake, this.wsl.onWebsocketHandshakeReceivedAsServer(this, d, handshake)), this.role));
                                        this.draft = d;
                                        open(handshake);
                                        return true;
                                    } catch (InvalidDataException e2) {
                                        closeConnection(e2.getCloseCode(), e2.getMessage(), false);
                                        return false;
                                    }
                                }
                                continue;
                            } else {
                                closeConnection(CloseFrame.PROTOCOL_ERROR, "wrong http function", false);
                                return false;
                            }
                        } catch (InvalidHandshakeException e3) {
                        }
                    }
                    if (this.draft == null) {
                        close(CloseFrame.PROTOCOL_ERROR, "no draft matches");
                    }
                    return false;
                } else {
                    tmphandshake = this.draft.translateHandshake(socketBuffer);
                    if (tmphandshake instanceof ClientHandshake) {
                        handshake = (ClientHandshake) tmphandshake;
                        if (this.draft.acceptHandshakeAsServer(handshake) == HandshakeState.MATCHED) {
                            open(handshake);
                            return true;
                        }
                        close(CloseFrame.PROTOCOL_ERROR, "the handshake did finaly not match");
                        return false;
                    }
                    closeConnection(CloseFrame.PROTOCOL_ERROR, "wrong http function", false);
                    return false;
                }
            } catch (InvalidHandshakeException e22) {
                close(e22);
            }
        } catch (IncompleteHandshakeException e4) {
            if (this.tmpHandshakeBytes == null) {
                socketBuffer.reset();
                int newsize = e4.getPreferedSize();
                if (newsize == 0) {
                    newsize = socketBuffer.capacity() + 16;
                } else if (!$assertionsDisabled && e4.getPreferedSize() < socketBuffer.remaining()) {
                    throw new AssertionError();
                }
                this.tmpHandshakeBytes = ByteBuffer.allocate(newsize);
                this.tmpHandshakeBytes.put(socketBufferNew);
            } else {
                this.tmpHandshakeBytes.position(this.tmpHandshakeBytes.limit());
                this.tmpHandshakeBytes.limit(this.tmpHandshakeBytes.capacity());
            }
        }
    }

    private void decodeFrames(ByteBuffer socketBuffer) {
        try {
            for (Framedata f : this.draft.translateFrame(socketBuffer)) {
                if (DEBUG) {
                    System.out.println("matched frame: " + f);
                }
                Opcode curop = f.getOpcode();
                if (curop == Opcode.CLOSING) {
                    int code = CloseFrame.NOCODE;
                    String reason = "";
                    if (f instanceof CloseFrame) {
                        CloseFrame cf = (CloseFrame) f;
                        code = cf.getCloseCode();
                        reason = cf.getMessage();
                    }
                    if (this.closeHandshakeSent) {
                        closeConnection(code, reason, true);
                    } else {
                        if (this.draft.getCloseHandshakeType() == CloseHandshakeType.TWOWAY) {
                            close(code, reason);
                        }
                        closeConnection(code, reason, false);
                    }
                } else if (curop == Opcode.PING) {
                    this.wsl.onWebsocketPing(this, f);
                } else if (curop == Opcode.PONG) {
                    this.wsl.onWebsocketPong(this, f);
                } else if (this.tempContiniousFrame == null) {
                    if (f.getOpcode() == Opcode.CONTINUOUS) {
                        throw new InvalidFrameException("unexpected continious frame");
                    } else if (f.isFin()) {
                        deliverMessage(f);
                    } else {
                        this.tempContiniousFrame = f;
                    }
                } else if (f.getOpcode() == Opcode.CONTINUOUS) {
                    this.tempContiniousFrame.append(f);
                    if (f.isFin()) {
                        deliverMessage(this.tempContiniousFrame);
                        this.tempContiniousFrame = null;
                    }
                } else {
                    throw new InvalidDataException((int) CloseFrame.PROTOCOL_ERROR, "non control or continious frame expected");
                }
            }
        } catch (InvalidDataException e1) {
            this.wsl.onWebsocketError(this, e1);
            close(e1);
        }
    }

    public void close(int code, String message) {
        if (!this.closeHandshakeSent) {
            if (this.handshakeComplete) {
                if (code == CloseFrame.ABNORMAL_CLOSE) {
                    closeConnection(code, true);
                    this.closeHandshakeSent = true;
                    return;
                } else if (this.draft.getCloseHandshakeType() != CloseHandshakeType.NONE) {
                    try {
                        sendFrame(new CloseFrameBuilder(code, message));
                    } catch (InvalidDataException e) {
                        this.wsl.onWebsocketError(this, e);
                        closeConnection(CloseFrame.ABNORMAL_CLOSE, "generated frame is invalid", false);
                    }
                } else {
                    closeConnection(code, false);
                }
            } else if (code == -3) {
                closeConnection(-3, true);
            } else {
                closeConnection(-1, false);
            }
            if (code == CloseFrame.PROTOCOL_ERROR) {
                closeConnection(code, false);
            }
            this.closeHandshakeSent = true;
            this.tmpHandshakeBytes = null;
        }
    }

    /* access modifiers changed from: protected|declared_synchronized */
    public synchronized void closeConnection(int code, String message, boolean remote) {
        if (!this.connectionClosed) {
            this.connectionClosed = true;
            this.wsl.onWriteDemand(this);
            this.wsl.onWebsocketClose(this, code, message, remote);
            if (this.draft != null) {
                this.draft.reset();
            }
            this.tempContiniousFrame = null;
            this.handshakerequest = null;
        }
    }

    /* access modifiers changed from: protected */
    public void closeConnection(int code, boolean remote) {
        closeConnection(code, "", remote);
    }

    public void eot() {
        if (this.draft == null) {
            closeConnection(CloseFrame.ABNORMAL_CLOSE, true);
        } else if (this.draft.getCloseHandshakeType() == CloseHandshakeType.NONE) {
            closeConnection(CloseFrame.NORMAL, true);
        } else if (this.draft.getCloseHandshakeType() != CloseHandshakeType.ONEWAY) {
            closeConnection(CloseFrame.ABNORMAL_CLOSE, true);
        } else if (this.role == Role.SERVER) {
            closeConnection(CloseFrame.ABNORMAL_CLOSE, true);
        } else {
            closeConnection(CloseFrame.NORMAL, true);
        }
    }

    public void close(int code) {
        close(code, "");
    }

    public void close(InvalidDataException e) {
        close(e.getCloseCode(), e.getMessage());
    }

    public void send(String text) throws NotYetConnectedException {
        if (text == null) {
            throw new IllegalArgumentException("Cannot send 'null' data to a WebSocketImpl.");
        }
        send(this.draft.createFrames(text, this.role == Role.CLIENT));
    }

    public void send(ByteBuffer bytes) throws IllegalArgumentException, NotYetConnectedException {
        if (bytes == null) {
            throw new IllegalArgumentException("Cannot send 'null' data to a WebSocketImpl.");
        }
        send(this.draft.createFrames(bytes, this.role == Role.CLIENT));
    }

    public void send(byte[] bytes) throws IllegalArgumentException, NotYetConnectedException {
        send(ByteBuffer.wrap(bytes));
    }

    private void send(Collection<Framedata> frames) {
        if (this.handshakeComplete) {
            for (Framedata f : frames) {
                sendFrame(f);
            }
            return;
        }
        throw new NotYetConnectedException();
    }

    public void sendFrame(Framedata framedata) {
        if (DEBUG) {
            System.out.println("send frame: " + framedata);
        }
        write(this.draft.createBinaryFrame(framedata));
    }

    public boolean hasBufferedData() {
        return !this.outQueue.isEmpty();
    }

    private HandshakeState isFlashEdgeCase(ByteBuffer request) throws IncompleteHandshakeException {
        request.mark();
        if (request.limit() > Draft.FLASH_POLICY_REQUEST.length) {
            return HandshakeState.NOT_MATCHED;
        }
        if (request.limit() < Draft.FLASH_POLICY_REQUEST.length) {
            throw new IncompleteHandshakeException(Draft.FLASH_POLICY_REQUEST.length);
        }
        int flash_policy_index = 0;
        while (request.hasRemaining()) {
            if (Draft.FLASH_POLICY_REQUEST[flash_policy_index] != request.get()) {
                request.reset();
                return HandshakeState.NOT_MATCHED;
            }
            flash_policy_index++;
        }
        return HandshakeState.MATCHED;
    }

    public void startHandshake(ClientHandshakeBuilder handshakedata) throws InvalidHandshakeException {
        if (this.handshakeComplete) {
            throw new IllegalStateException("Handshake has already been sent.");
        }
        this.handshakerequest = this.draft.postProcessHandshakeRequestAsClient(handshakedata);
        try {
            this.wsl.onWebsocketHandshakeSentAsClient(this, this.handshakerequest);
            write(this.draft.createHandshake(this.handshakerequest, this.role));
        } catch (InvalidDataException e) {
            throw new InvalidHandshakeException("Handshake data rejected by client.");
        }
    }

    private void write(ByteBuffer buf) {
        if (DEBUG) {
            System.out.println("write(" + buf.remaining() + "): {" + (buf.remaining() > CloseFrame.NORMAL ? "too big to display" : new String(buf.array())) + "}");
        }
        this.outQueue.add(buf);
        this.wsl.onWriteDemand(this);
    }

    private void write(List<ByteBuffer> bufs) {
        for (ByteBuffer b : bufs) {
            write(b);
        }
    }

    private void deliverMessage(Framedata d) throws InvalidDataException {
        try {
            if (d.getOpcode() == Opcode.TEXT) {
                this.wsl.onWebsocketMessage((WebSocket) this, Charsetfunctions.stringUtf8(d.getPayloadData()));
            } else if (d.getOpcode() == Opcode.BINARY) {
                this.wsl.onWebsocketMessage((WebSocket) this, d.getPayloadData());
            } else {
                if (DEBUG) {
                    System.out.println("Ignoring frame:" + d.toString());
                }
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
        } catch (RuntimeException e) {
            this.wsl.onWebsocketError(this, e);
        }
    }

    private void open(Handshakedata d) throws IOException {
        if (DEBUG) {
            System.out.println("open using draft: " + this.draft.getClass().getSimpleName());
        }
        this.handshakeComplete = true;
        this.wsl.onWebsocketOpen(this, d);
    }

    public boolean isConnecting() {
        return (this.connectionClosed || this.closeHandshakeSent || this.handshakeComplete) ? false : true;
    }

    public boolean isOpen() {
        return (this.connectionClosed || this.closeHandshakeSent || !this.handshakeComplete) ? false : true;
    }

    public boolean isClosing() {
        return !this.connectionClosed && this.closeHandshakeSent;
    }

    public boolean isClosed() {
        return this.connectionClosed;
    }

    public int getReadyState() {
        if (isConnecting()) {
            return 0;
        }
        if (isOpen()) {
            return 1;
        }
        if (isClosing()) {
            return 2;
        }
        if (isClosed()) {
            return 3;
        }
        if ($assertionsDisabled) {
            return -1;
        }
        throw new AssertionError();
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }

    public InetSocketAddress getRemoteSocketAddress() {
        return (InetSocketAddress) this.socket.getRemoteSocketAddress();
    }

    public InetSocketAddress getLocalSocketAddress() {
        return (InetSocketAddress) this.socket.getLocalSocketAddress();
    }

    public Draft getDraft() {
        return this.draft;
    }
}
