package org.java_websocket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.objectweb.asm.Opcodes;

public abstract class WebSocket {
    public static boolean DEBUG = false;
    public static final int DEFAULT_PORT = 80;
    public static final int DEFAULT_WSS_PORT = 443;
    public static int RCVBUF = Opcodes.ACC_ENUM;
    public static final int READY_STATE_CLOSED = 3;
    public static final int READY_STATE_CLOSING = 2;
    public static final int READY_STATE_CONNECTING = 0;
    public static final int READY_STATE_OPEN = 1;

    public enum Role {
        CLIENT,
        SERVER
    }

    public abstract void close(int i);

    public abstract void close(int i, String str);

    public abstract void close(InvalidDataException invalidDataException);

    public abstract Draft getDraft();

    public abstract InetSocketAddress getLocalSocketAddress();

    public abstract int getReadyState();

    public abstract InetSocketAddress getRemoteSocketAddress();

    public abstract boolean hasBufferedData();

    public abstract boolean isClosed();

    public abstract boolean isClosing();

    public abstract boolean isConnecting();

    public abstract boolean isOpen();

    public abstract void send(String str) throws NotYetConnectedException;

    public abstract void send(ByteBuffer byteBuffer) throws IllegalArgumentException, NotYetConnectedException;

    public abstract void send(byte[] bArr) throws IllegalArgumentException, NotYetConnectedException;

    public abstract void sendFrame(Framedata framedata);

    public abstract void startHandshake(ClientHandshakeBuilder clientHandshakeBuilder) throws InvalidHandshakeException;
}
